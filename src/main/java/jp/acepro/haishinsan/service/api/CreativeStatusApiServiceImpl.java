package jp.acepro.haishinsan.service.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.Ad;
import com.facebook.ads.sdk.Ad.EnumEffectiveStatus;
import com.facebook.ads.sdk.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.PolicyApprovalStatus;

//gitlab.com/acepro/reporting/haishinsan.git
import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dao.FacebookCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.FacebookCampaignManageDao;
import jp.acepro.haishinsan.dao.GoogleCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.GoogleCampaignManageDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.dao.ShopDao;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.enums.CheckStatus;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspCreativeService;
import jp.acepro.haishinsan.service.google.api.GetAdGroups;
import jp.acepro.haishinsan.service.google.api.GetExpandedTextAds;
import jp.acepro.haishinsan.service.google.api.GetImageAd;
import jp.acepro.haishinsan.service.google.api.GetResponsiveDisplayAd;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreativeStatusApiServiceImpl implements CreativeStatusApiService {

	@Autowired
	OperationService operationService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	ShopDao shopDao;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Autowired
	FacebookCampaignManageDao facebookCampaignManageDao;

	@Autowired
	FacebookCampaignManageCustomDao facebookCampaignManageCustomDao;

	@Autowired
	GoogleCampaignManageDao googleCampaignManageDao;

	@Autowired
	GoogleCampaignManageCustomDao googleCampaignManageCustomDao;

	@Autowired
	DspCreativeService dspCreativeService;

	@Async
	@Override
	@Transactional
	public void executeAsync() {

		// DSP
		try {
			// クリエイティブ審査状態更新
			dspCreativeService.updateCreatives();
			// オペレーションログ記録
			operationService.createWithoutUser(Operation.DSP_CREATIVE_UPDATE.getValue(), "クリエイティブ審査状態更新が成功しました。");
		} catch (Exception e) {
			log.error("クリエイティブ審査状態更新中エラー発生", e);
			// オペレーションログ記録
			operationService.createWithoutUser(Operation.DSP_CREATIVE_UPDATE.getValue(), e.getMessage());
		}

		// Facebook
		updateLocalFacebookAdEffectiveStatusAsync();

		// Google
		updateLocalGoogleAdCombinedApprovalStatusAsync();
	}

	@Transactional
	private void updateLocalFacebookAdEffectiveStatusAsync() {

		List<FacebookCampaignManage> facebookCampaignManageList = facebookCampaignManageCustomDao.selectWithActiveShop();

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());

		for (FacebookCampaignManage facebookCampaignManage : facebookCampaignManageList) {
			try {

				APINodeList<Ad> ads = new Campaign(facebookCampaignManage.getCampaignId(), context).getAds()
						// .requestNameField()
						// .requestConfiguredStatusField()
						.requestEffectiveStatusField().execute();
				if (ads == null || ads.size() < 1) {
					continue;
				}
				EnumEffectiveStatus effectiveStatus = ads.get(0).getFieldEffectiveStatus();
				String checkStatus = null;
				if (effectiveStatus == null) {
					continue;
				}
				// キャンペーンはオフの場合も実は承認済みと見なす
				if (effectiveStatus.equals(EnumEffectiveStatus.VALUE_CAMPAIGN_PAUSED) || effectiveStatus.equals(EnumEffectiveStatus.VALUE_ACTIVE) || effectiveStatus.equals(EnumEffectiveStatus.VALUE_PREAPPROVED)) {
					checkStatus = CheckStatus.PREAPPROVED.getValue();
				} else if (effectiveStatus.equals(EnumEffectiveStatus.VALUE_DELETED) || effectiveStatus.equals(EnumEffectiveStatus.VALUE_DISAPPROVED)) {
					checkStatus = CheckStatus.DISAPPROVED.getValue();
				} else {
					checkStatus = CheckStatus.PROCESSING.getValue();
				}

				facebookCampaignManage.setCheckStatus(checkStatus);
				facebookCampaignManageDao.update(facebookCampaignManage);
			} catch (APIException e) {
				e.printStackTrace();
				throw new SystemException("システムエラー発生しました");
			}
		}
	}
	
	@Transactional
	private void updateLocalGoogleAdCombinedApprovalStatusAsync() {

		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignManageCustomDao.selectWithActiveShop();

		for (GoogleCampaignManage googleCampaignManage : googleCampaignManageList) {
			Shop shop = shopDao.selectById(googleCampaignManage.getShopId());

			// ---------広告グループ取得API実行
			GetAdGroups getAdGroups = new GetAdGroups();
			getAdGroups.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			getAdGroups.campaignId = googleCampaignManage.getCampaignId();
			getAdGroups.googleAccountId = shop.getGoogleAccountId();
			getAdGroups.run();
			// ---------広告グループ取得API実行

			// ---------広告取得API実行
			List<PolicyApprovalStatus> policyApprovalStatusList = new ArrayList<PolicyApprovalStatus>();
			switch (GoogleAdType.of(googleCampaignManage.getAdType())) {
			case RESPONSIVE:
				GetResponsiveDisplayAd getResponsiveDisplayAd = new GetResponsiveDisplayAd();
				getResponsiveDisplayAd.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
				getResponsiveDisplayAd.adGroupId = getAdGroups.adGroupIdList.get(0);
				getResponsiveDisplayAd.googleAccountId = shop.getGoogleAccountId();
				getResponsiveDisplayAd.run();
				policyApprovalStatusList = getResponsiveDisplayAd.policyApprovalStatusList;
				break;
			case IMAGE:
				GetImageAd getImageAd = new GetImageAd();
				getImageAd.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
				getImageAd.adGroupId = getAdGroups.adGroupIdList.get(0);
				getImageAd.googleAccountId = shop.getGoogleAccountId();
				getImageAd.run();
				policyApprovalStatusList = getImageAd.policyApprovalStatusList;
				break;
			case TEXT:
				GetExpandedTextAds getExpandedTextAds = new GetExpandedTextAds();
				getExpandedTextAds.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
				getExpandedTextAds.adGroupId = getAdGroups.adGroupIdList.get(0);
				getExpandedTextAds.googleAccountId = shop.getGoogleAccountId();
				getExpandedTextAds.run();
				policyApprovalStatusList = getExpandedTextAds.policyApprovalStatusList;
				break;
			}
			// ---------広告取得API実行
			for (PolicyApprovalStatus policyApprovalStatus : policyApprovalStatusList) {
				// 任意の広告が不承認の場合、不承認と判断
				if (PolicyApprovalStatus._DISAPPROVED.equals(policyApprovalStatus.getValue())) {
					googleCampaignManage.setCheckStatus(CheckStatus.DISAPPROVED.getValue());
					break;
				}
		
				// 全部の広告が承認の場合、承認と判断
				if (PolicyApprovalStatus._APPROVED.equals(policyApprovalStatus.getValue()) || PolicyApprovalStatus._APPROVED_LIMITED.equals(policyApprovalStatus.getValue())
						|| PolicyApprovalStatus._ELIGIBLE.equals(policyApprovalStatus.getValue())) {
					googleCampaignManage.setCheckStatus(CheckStatus.PREAPPROVED.getValue());
				} else {
					// 任意の広告がその他の場合、審査中と判断
					googleCampaignManage.setCheckStatus(CheckStatus.PROCESSING.getValue());
					break;
				}
			}
			googleCampaignManageDao.update(googleCampaignManage);
		}

	}

}
