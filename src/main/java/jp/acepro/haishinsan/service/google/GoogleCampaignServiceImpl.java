package jp.acepro.haishinsan.service.google;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.ads.adwords.axis.v201809.cm.Budget;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignCriterion;
import com.google.api.ads.adwords.axis.v201809.cm.ExpandedTextAd;
import com.google.api.ads.adwords.axis.v201809.cm.ImageAd;
import com.google.api.ads.adwords.axis.v201809.cm.ResponsiveDisplayAd;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.GoogleCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.GoogleCampaignManageDao;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDetailDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignInfoDto;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.DeviceType;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.MediaCollection;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.service.google.api.AddAdGroups;
import jp.acepro.haishinsan.service.google.api.AddCampaign;
import jp.acepro.haishinsan.service.google.api.AddExpandedTextAds;
import jp.acepro.haishinsan.service.google.api.AddImageAd;
import jp.acepro.haishinsan.service.google.api.AddMultiAssetResponsiveDisplayAd;
import jp.acepro.haishinsan.service.google.api.GetAdGroups;
import jp.acepro.haishinsan.service.google.api.GetCampaigns;
import jp.acepro.haishinsan.service.google.api.GetExpandedTextAds;
import jp.acepro.haishinsan.service.google.api.GetImageAd;
import jp.acepro.haishinsan.service.google.api.GetResponsiveDisplayAd;
import jp.acepro.haishinsan.service.google.api.UpdateCampaign;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleCampaignServiceImpl implements GoogleCampaignService {

	@Autowired
	IssueDao issueDao;

	@Autowired
	IssueCustomDao issueCustomDao;

	@Autowired
	GoogleCampaignManageDao googleCampaignManageDao;

	@Autowired
	GoogleCampaignManageCustomDao googleCampaignManageCustomDao;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	EmailService emailService;

	@Autowired
	CodeMasterService codeMasterService;

	// 案件簡単作成のため、新しいトランザクションで実行する
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createGoogleCampaign(GoogleCampaignDto googleCampaignDto, IssueDto issueDto) {
		createCampaign(googleCampaignDto, issueDto);
	}

	// キャンペーン新規作成（API経由）
	@Override
	@Transactional
	public void createCampaign(GoogleCampaignDto googleCampaignDto, IssueDto issueDto) {

		// Get Keyword List
		if (CodeMasterServiceImpl.keywordNameList == null) {
			codeMasterService.getKeywordNameList();
		}

		// Get Area List
		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}

		// 例外処理
		// 配信期間チェック
		LocalDate startDate = LocalDate.parse(googleCampaignDto.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endDate = LocalDate.parse(googleCampaignDto.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if (startDate.isAfter(endDate)) {
			// 配信期間の開始日と終了日を確認してください。
			throw new BusinessException(ErrorCodeConstant.E70002);
		}

		// 地域チェック
		if (googleCampaignDto.getLocationList().size() == 0) {
			// 配信地域を選択してください。
			throw new BusinessException(ErrorCodeConstant.E70003);
		}

		log.debug("キャンペーン新規作成開始----------------------------");
		log.debug("キャンペーン新規作成用パラメータ: {}", googleCampaignDto);
		// キャンプーン作成
		AddCampaign addCampaign = new AddCampaign();
		addCampaign.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		addCampaign.googleCampaignDto = googleCampaignDto;
		addCampaign.run();

		// 広告グループ作成（広告グループを２つ作成）
		AddAdGroups addAdGroups = new AddAdGroups();
		addAdGroups.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		addAdGroups.newCampaign = addCampaign.newCampaign;
		addAdGroups.googleCampaignDto = googleCampaignDto;
		addAdGroups.run();

		// 広告作成（２つの広告グループに同じ広告を作成）
		switch (GoogleAdType.of(googleCampaignDto.getAdType())) {
		case RESPONSIVE:
			AddMultiAssetResponsiveDisplayAd addMultiAssetResponsiveDisplayAd = new AddMultiAssetResponsiveDisplayAd();
			addMultiAssetResponsiveDisplayAd.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			addMultiAssetResponsiveDisplayAd.googleCampaignDto = googleCampaignDto;
			addMultiAssetResponsiveDisplayAd.newAdGroupUser = addAdGroups.newAdGroupUser;
			addMultiAssetResponsiveDisplayAd.newAdGroupKeyword = addAdGroups.newAdGroupKeyword;
			addMultiAssetResponsiveDisplayAd.run();
			break;
		case IMAGE:
			AddImageAd addImageAd = new AddImageAd();
			addImageAd.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			addImageAd.googleCampaignDto = googleCampaignDto;
			addImageAd.newAdGroupUser = addAdGroups.newAdGroupUser;
			addImageAd.newAdGroupKeyword = addAdGroups.newAdGroupKeyword;
			addImageAd.run();
			break;
		case TEXT:
			AddExpandedTextAds addExpandedTextAds = new AddExpandedTextAds();
			addExpandedTextAds.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			addExpandedTextAds.googleCampaignDto = googleCampaignDto;
			addExpandedTextAds.newAdGroupUser = addAdGroups.newAdGroupUser;
			addExpandedTextAds.newAdGroupKeyword = addAdGroups.newAdGroupKeyword;
			addExpandedTextAds.run();
			break;
		}

		// キャンペーンDB登録
		GoogleCampaignManage googleCampaignManage = new GoogleCampaignManage();
		googleCampaignManage.setCampaignId(addCampaign.newCampaign.getId());
		googleCampaignManage.setCampaignName(addCampaign.newCampaign.getName());
		if (Flag.ON.getValue().toString().equals(ContextUtil.getCurrentShop().getSalesCheckFlag())) {
			// 営業チェックが必要有りの場合
			googleCampaignManage.setApprovalFlag(ApprovalFlag.WAITING.getValue());
		} else {
			// 営業チェックが必要無しの場合
			googleCampaignManage.setApprovalFlag(ApprovalFlag.COMPLETED.getValue());
		}
		googleCampaignManage.setRegions(googleCampaignDto.getLocationList().toString().replace("[", "").replace("]", ""));
		googleCampaignManage.setAdType(googleCampaignDto.getAdType());
		googleCampaignManage.setBudget(googleCampaignDto.getBudget());
		googleCampaignManageDao.insert(googleCampaignManage);
		googleCampaignDto.setCampaignId(addCampaign.newCampaign.getId());

		// 案件DB登録
		Issue issue = new Issue();
		if (issueDto == null) {
			issue.setShopId(ContextUtil.getCurrentShop().getShopId());
			issue.setGoogleCampaignManageId(googleCampaignManage.getGoogleCampaignManageId());
			issue.setCampaignName(googleCampaignDto.getCampaignName());
			issue.setBudget(CalculateUtil.calTotalBudget(googleCampaignDto.getBudget(), googleCampaignDto.getStartDate(), googleCampaignDto.getEndDate()));
			issue.setStartDate(googleCampaignDto.getStartDate());
			issue.setEndDate(googleCampaignDto.getEndDate());
			issueDao.insert(issue);
		} else {
			issueDto.setGoogleCampaignManageId(googleCampaignManage.getGoogleCampaignManageId());
		}

		// メール送信
		EmailDto emailDto = new EmailDto();
		emailDto.setIssueId(issue.getIssueId());
		EmailCampDetailDto emailCampDetailDto = new EmailCampDetailDto();
		emailCampDetailDto.setMediaType(MediaCollection.GOOGLE.getValue());
		emailCampDetailDto.setCampaignId(String.valueOf(googleCampaignManage.getCampaignId()));
		emailCampDetailDto.setCampaignName(googleCampaignManage.getCampaignName());
		List<EmailCampDetailDto> emailCampDetailDtoList = new ArrayList<EmailCampDetailDto>();
		emailCampDetailDtoList.add(emailCampDetailDto);
		emailDto.setCampaignList(emailCampDetailDtoList);
		emailDto.setTemplateType(EmailTemplateType.CAMPAIGN.getValue());
		emailService.sendEmail(emailDto);

		log.debug("キャンペーン新規作成完了----------------------------");
	}

	// キャンペーン状態変更（API経由）
	@Override
	@Transactional
	public void updateCampaignStatus(Long campaignId, String switchFlag) {

		// キャンペーン情報更新（API経由）
		UpdateCampaign updateCampaign = new UpdateCampaign();
		updateCampaign.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		updateCampaign.run(campaignId, switchFlag);

		// キャンペーン情報更新（DB）
		GoogleCampaignManage googleCampaignManage = googleCampaignManageCustomDao.selectByCampaignId(campaignId);
		if (googleCampaignManage.getApprovalFlag().equals(ApprovalFlag.WAITING.getValue())) {
			// 承認フラグ設定
			if (switchFlag.equals("ON")) {
				googleCampaignManage.setApprovalFlag(ApprovalFlag.COMPLETED.getValue());
				googleCampaignManageDao.update(googleCampaignManage);
			}
		}

	}

	// キャンペーン一覧取得（API経由）
	@Override
	@Transactional
	public List<GoogleCampaignInfoDto> getCampaignList() {

		List<GoogleCampaignInfoDto> googleCampaignInfoDtoList = new ArrayList<GoogleCampaignInfoDto>();

		// キャンペーン情報取得（DBから）
		List<Issue> issueList = issueCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
		List<Long> campaignManageIdList = issueList.stream().filter(obj -> obj.getGoogleCampaignManageId() != null).map(obj -> obj.getGoogleCampaignManageId()).collect(Collectors.toList());
		List<GoogleCampaignManage> googleCampaignManageList = googleCampaignManageCustomDao.selectByCampaignManageIdList(campaignManageIdList);
		List<Long> campaignIdList = googleCampaignManageList.stream().map(obj -> obj.getCampaignId()).collect(Collectors.toList());

		if (campaignIdList.size() > 0) {

			// キャンペーン情報取得（API経由）
			GetCampaigns getCampaigns = new GetCampaigns();
			getCampaigns.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			getCampaigns.campaignIdList = campaignIdList;
			getCampaigns.runOption = "INFO";
			getCampaigns.run();
			List<Campaign> campaignList = new ArrayList<Campaign>();
			campaignList = getCampaigns.campaignList;

			// キャンペーン一覧作成
			for (GoogleCampaignManage googleCampaignManage : googleCampaignManageList) {
				GoogleCampaignInfoDto googleCampaignInfoDto = new GoogleCampaignInfoDto();
				Campaign campaign = new Campaign();
				if (campaignList.size() > 0) {
					campaign = campaignList.stream().filter(obj -> obj.getId().equals(googleCampaignManage.getCampaignId())).findFirst().get();
					googleCampaignInfoDto.setStartDate(campaign.getStartDate());
					googleCampaignInfoDto.setEndDate(campaign.getEndDate());
					googleCampaignInfoDto.setCampaignStatus(campaign.getStatus().toString());
					googleCampaignInfoDto.setCampaignName(campaign.getName());
					googleCampaignInfoDto.setAdType(googleCampaignManage.getAdType());
					googleCampaignInfoDto.setApprovalFlag(googleCampaignManage.getApprovalFlag());
					googleCampaignInfoDto.setCampaignId(googleCampaignManage.getCampaignId());
					googleCampaignInfoDtoList.add(googleCampaignInfoDto);
				}
			}
		}
		log.debug("googleCampaignInfoDtoList : {}", googleCampaignInfoDtoList);
		return googleCampaignInfoDtoList;
	}

	// キャンペーン詳細取得（API経由）
	@Override
	@Transactional
	public GoogleCampaignDetailDto getCampaign(Long campaignId) {

		GoogleCampaignDetailDto googleCampaignDetailDto = new GoogleCampaignDetailDto();

		// ---------キャンペーン取得API実行
		GoogleCampaignManage googleCampaignManage = googleCampaignManageCustomDao.selectByCampaignId(campaignId);

		GetCampaigns getCampaigns = new GetCampaigns();
		List<Long> campaignIdList = new ArrayList<Long>();
		campaignIdList.add(googleCampaignManage.getCampaignId());
		getCampaigns.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		getCampaigns.campaignIdList = campaignIdList;
		getCampaigns.runOption = "DETAIL";
		getCampaigns.run();

		Campaign campaign = new Campaign();
		campaign = getCampaigns.campaignList.get(0);
		Budget budget = new Budget();
		budget = getCampaigns.budgetList.get(0);
		List<CampaignCriterion> campaignCriterionList = new ArrayList<CampaignCriterion>();
		campaignCriterionList = getCampaigns.campaignCriterionList;

		// 承認フラグ設定
		googleCampaignDetailDto.setApprovalFlag(googleCampaignManage.getApprovalFlag());
		// キャンペーンステータス
		googleCampaignDetailDto.setCampaignStatus(campaign.getStatus().getValue());
		// キャンペーンスID設定
		googleCampaignDetailDto.setCampaignId(campaignId);
		// キャンペーン名設定
		googleCampaignDetailDto.setCampaignName(campaign.getName());
		// 地域設定
		List<Long> locationList = new ArrayList<Long>();
		for (CampaignCriterion campaignCriterion : campaignCriterionList) {
			if (campaignCriterion.getCriterion().getCriterionType().equals("Location")) {
				locationList.add(campaignCriterion.getCriterion().getId());
			}
		}
		googleCampaignDetailDto.setLocationList(locationList);
		// 予算設定
		googleCampaignDetailDto.setBudget(googleCampaignManage.getBudget());
		// 配信開始日設定
		googleCampaignDetailDto.setStartDate(campaign.getStartDate());
		// 配信終了日設定
		googleCampaignDetailDto.setEndDate(campaign.getEndDate());
		// デバイスタイプ設定
		Boolean pc = false;
		Boolean mobile = false;
		for (CampaignCriterion campaignCriterion : campaignCriterionList) {
			if (campaignCriterion.getCriterion().getCriterionType().equals("Platform")) {
				// パソコンの場合
				if (campaignCriterion.getCriterion().getId() == 30000L) {
					if (campaignCriterion.getBidModifier() != null && campaignCriterion.getBidModifier() == 0.0) {
						pc = false;
					} else {
						pc = true;
					}
				}
				// モバイル（スマホ）の場合
				if (campaignCriterion.getCriterion().getId() == 30001L) {
					if (campaignCriterion.getBidModifier() != null && campaignCriterion.getBidModifier() == 0.0) {
						mobile = false;
					} else {
						mobile = true;
					}
				}
				// モバイル（タブレット）の場合
				if (campaignCriterion.getCriterion().getId() == 30002L) {
					if (campaignCriterion.getBidModifier() != null && campaignCriterion.getBidModifier() == 0.0) {
						mobile = false;
					} else {
						mobile = true;
					}
				}
			}
		}
		if (pc && mobile) {
			googleCampaignDetailDto.setDeviceType(DeviceType.ALL.getValue());
		} else if (pc) {
			googleCampaignDetailDto.setDeviceType(DeviceType.PC.getValue());
		} else {
			googleCampaignDetailDto.setDeviceType(DeviceType.MOBILE.getValue());
		}
		// ---------キャンペーン取得API実行

		// ---------広告グループ取得API実行
		GetAdGroups getAdGroups = new GetAdGroups();
		getAdGroups.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		getAdGroups.campaignId = getCampaigns.campaignIdList.get(0);
		getAdGroups.run();
		// ---------広告グループ取得API実行

		// ---------広告取得API実行
		// 広告タイプ設定
		googleCampaignDetailDto.setAdType(googleCampaignManage.getAdType());
		switch (GoogleAdType.of(googleCampaignManage.getAdType())) {
		case RESPONSIVE:
			GetResponsiveDisplayAd getResponsiveDisplayAd = new GetResponsiveDisplayAd();
			getResponsiveDisplayAd.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			getResponsiveDisplayAd.adGroupId = getAdGroups.adGroupIdList.get(0);
			getResponsiveDisplayAd.run();
			ResponsiveDisplayAd responsiveDisplayAd = new ResponsiveDisplayAd();
			responsiveDisplayAd = getResponsiveDisplayAd.responsiveDisplayAdList.get(0);
			googleCampaignDetailDto.setResAdDescription(responsiveDisplayAd.getDescription());
			googleCampaignDetailDto.setResAdFinalPageUrl(responsiveDisplayAd.getFinalUrls()[0]);
			googleCampaignDetailDto.setResAdShortTitle(responsiveDisplayAd.getShortHeadline());
			List<String> resAdUrlList = new ArrayList<String>();
			resAdUrlList.add(responsiveDisplayAd.getMarketingImage().getUrls(0).getValue());
			resAdUrlList.add(responsiveDisplayAd.getSquareMarketingImage().getUrls(0).getValue());
			googleCampaignDetailDto.setResAdImageUrlList(resAdUrlList);
			break;
		case IMAGE:
			GetImageAd getImageAd = new GetImageAd();
			getImageAd.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			getImageAd.adGroupId = getAdGroups.adGroupIdList.get(0);
			getImageAd.run();
			List<String> imgAdUrlList = new ArrayList<String>();
			for (ImageAd imageAd : getImageAd.imageAdList) {
				imgAdUrlList.add(imageAd.getImage().getUrls(0).getValue());
				if (googleCampaignDetailDto.getImageAdFinalPageUrl() == null) {
					googleCampaignDetailDto.setImageAdFinalPageUrl(imageAd.getFinalUrls(0));
				}
			}
			googleCampaignDetailDto.setImageAdImageUrlList(imgAdUrlList);
			break;
		case TEXT:
			GetExpandedTextAds getExpandedTextAds = new GetExpandedTextAds();
			getExpandedTextAds.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
			getExpandedTextAds.adGroupId = getAdGroups.adGroupIdList.get(0);
			getExpandedTextAds.run();
			ExpandedTextAd expandedTextAd = new ExpandedTextAd();
			expandedTextAd = getExpandedTextAds.expandedTextAdList.get(0);
			googleCampaignDetailDto.setTextAdDescription(expandedTextAd.getDescription());
			googleCampaignDetailDto.setTextAdFinalPageUrl(expandedTextAd.getFinalUrls(0));
			googleCampaignDetailDto.setTextAdTitle1(expandedTextAd.getHeadlinePart1());
			googleCampaignDetailDto.setTextAdTitle2(expandedTextAd.getHeadlinePart2());
			break;
		}
		// ---------広告取得API実行

		return googleCampaignDetailDto;
	}

	// キャンペーン削除（API経由）
	@Override
	@Transactional
	public void deleteCampaign(Long campaignId) {

		// キャンペーン情報更新（API経由）
		UpdateCampaign updateCampaign = new UpdateCampaign();
		updateCampaign.propFileName = "ads-" + applicationProperties.getActive() + ".properties";
		updateCampaign.run(campaignId, "OFF");

		// キャンペーン情報更新（DB）
		GoogleCampaignManage googleCampaignManage = googleCampaignManageCustomDao.selectByCampaignId(campaignId);
		googleCampaignManage.setIsActived(Flag.OFF.getValue());
		googleCampaignManageDao.update(googleCampaignManage);

	}

	@Override
	@Transactional
	public List<GoogleCampaignManage> searchGoogleCampaignManageList(String adType) {

		// DBから該当店舗所有するキャンペーンをすべて取得して、リストとして返却
		return googleCampaignManageCustomDao.selectByShopIdAndAdType(ContextUtil.getCurrentShop().getShopId(), adType);
	}
	
	@Override
	@Transactional
	public List<GoogleCampaignDto> campaignList(List<GoogleCampaignManage> googleCampaignManageList) {

		List<GoogleCampaignDto> googleCampaignDtoList = new ArrayList<GoogleCampaignDto>();

		if (googleCampaignDtoList.size() == 0) {
			return googleCampaignDtoList;
		}

		for (GoogleCampaignManage googleCampaignManage : googleCampaignManageList) {
			GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
			googleCampaignDto.setCampaignId(googleCampaignManage.getCampaignId());
			googleCampaignDto.setCampaignName(googleCampaignManage.getCampaignName());
			googleCampaignDto.setImage1Url(googleCampaignManage.getImage1Url());
			googleCampaignDto.setImage2Url(googleCampaignManage.getImage2Url());
			googleCampaignDto.setImage3Url(googleCampaignManage.getImage3Url());
			googleCampaignDto.setImage4Url(googleCampaignManage.getImage4Url());
			googleCampaignDto.setAdTitle1(googleCampaignManage.getTitle1());
			googleCampaignDto.setAdTitle2(googleCampaignManage.getTitle2());
			googleCampaignDto.setAdDescription(googleCampaignManage.getDescription());
			googleCampaignDto.setLinkUrl(googleCampaignManage.getLinkUrl());

			googleCampaignDtoList.add(googleCampaignDto);
		}

		return googleCampaignDtoList;

	}
}
