package jp.acepro.haishinsan.service.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdSet;
import com.facebook.ads.sdk.Campaign;
import com.facebook.ads.sdk.IDName;
import com.facebook.ads.sdk.Targeting;
import com.facebook.ads.sdk.TargetingGeoLocation;
import com.facebook.ads.sdk.TargetingGeoLocationCity;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dao.GoogleCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.ShopCustomDao;
import jp.acepro.haishinsan.db.entity.GoogleCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.Shop;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.service.google.api.GetAdGroups;
import jp.acepro.haishinsan.service.google.api.UpdateAdGroup;
import jp.acepro.haishinsan.service.google.api.UpdateCampaign;
import jp.acepro.haishinsan.service.google.api.UpdateCampaignStatus;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import jp.acepro.haishinsan.service.youtube.YoutubeReportService;
import jp.acepro.haishinsan.util.CalculateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IssueApiServiceImpl implements IssueApiService {

	@Autowired
	OperationService operationService;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	GoogleReportService googleReportService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	TwitterReportingService twitterReportingService;

	@Autowired
	YoutubeReportService youtubeReportService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	IssueDao issueDao;

	@Autowired
	IssueCustomDao issueCustomDao;

	@Autowired
	ShopCustomDao shopCustomDao;

	@Autowired
	GoogleCampaignManageCustomDao googleCampaignManageCustomDao;

	@Async
	@Override
	@Transactional
	public void startFacebookIssueAsync() {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.now();
		String dateString = df.format(dateTime);
		List<Issue> issueList = issueCustomDao.selectFacebookIssueNeededStart(dateString);

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());
		// AdAccount account = new
		// AdAccount(applicationProperties.getFacebookAccountId(), context);

		if (CodeMasterServiceImpl.facebookAreaNameList == null) {
			codeMasterService.getFacebookAreaList();
		}

		for (Issue issue : issueList) {
			try {

				// 店舗情報取得
				List<Shop> shopList = shopCustomDao.selectByIssueId(issue.getIssueId());
				// 地域設定
				// location_typesが指定されていない場合、デフォルトはこの地域に住んでる人です。
				List<TargetingGeoLocationCity> TargetingGeoLocationCityList = new ArrayList<TargetingGeoLocationCity>();
				List<String> locationStringList = Arrays.asList(issue.getFacebookRegions().split(","));
				List<Long> locationLongList = new ArrayList<Long>();
				for (String location : locationStringList) {
					TargetingGeoLocationCityList.add(new TargetingGeoLocationCity().setFieldKey(location));
					locationLongList.add(Long.valueOf(location));
				}

				// 最大入札価格を設定（地域の価格の平均値を算出）
				Long bidAmount = 200l;
				Double averageUnitPriceDouble = CodeMasterServiceImpl.facebookAreaUnitPriceClickList.stream()
						.filter(obj -> locationLongList.contains(obj.getFirst())).mapToInt(obj -> obj.getSecond())
						.average().getAsDouble();
				bidAmount = Math.round(averageUnitPriceDouble);

				// 1日の予算
				Long dailyBudget = issue.getFacebookOnedayBudget();
				// マージン率をかけた1日の予算
				Long realDailyBudget = CalculateUtil.calRealBudgetWithShopRatio(dailyBudget,
						shopList.get(0).getMarginRatio());

				// 趣味設定
				List<IDName> idNameList = new ArrayList<IDName>();
				idNameList.add(new IDName().setFieldId("6003484127669").setFieldName("Casino"));// カジノ
				idNameList.add(new IDName().setFieldId("6003012317397").setFieldName("Gambling"));// ギャンブル

				// ターゲット
				// 性別のデフォルトはすべてです。
				// デフォルトはこの地域に住んでる人
				// 配置場所はfacebookのフィードのみ
				Targeting targeting = new Targeting().setFieldAgeMin(18L) // 最小年齢
						.setFieldInterests(idNameList)// 趣味
						.setFieldGeoLocations(new TargetingGeoLocation().setFieldCities(TargetingGeoLocationCityList))
						.setFieldPublisherPlatforms(Arrays.asList("facebook"))
						.setFieldFacebookPositions(Arrays.asList("feed"));
				APINodeList<AdSet> adSets = new Campaign(issue.getFacebookCampaignManageId(), context).getAdSets()
						// .requestNameField()
						// .requestConfiguredStatusField()
						// .requestEffectiveStatusField()
						.execute();
				adSets.get(0).update().setDailyBudget(realDailyBudget).setBidAmount(bidAmount).setTargeting(targeting)
						.execute();

				Campaign campaign = new Campaign(issue.getFacebookCampaignManageId(), context).update()
						.setStatus(Campaign.EnumStatus.VALUE_ACTIVE).execute();

				issue.setStartTimestamp(dateTime);
				issueDao.update(issue);
			} catch (APIException e) {
				e.printStackTrace();
				throw new SystemException("システムエラー発生しました");
			}
		}

	}

	@Async
	@Override
	@Transactional
	public void stopFacebookIssueAsync() {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.now();
		String dateString = df.format(dateTime);
		List<Issue> issueList = issueCustomDao.selectFacebookIssueNeededStop(dateString);

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());

		for (Issue issue : issueList) {
			try {

				Campaign campaign = new Campaign(issue.getFacebookCampaignManageId(), context).update()
						.setStatus(Campaign.EnumStatus.VALUE_PAUSED).execute();
				issue.setEndTimestamp(dateTime);
				issueDao.update(issue);
			} catch (APIException e) {
				e.printStackTrace();
				throw new SystemException("システムエラー発生しました");
			}
		}

	}

	@Async
	@Override
	@Transactional
	public void startGoogleIssueAsync() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.now();
		String dateString = df.format(dateTime);
		List<Issue> issueList = issueCustomDao.selectGoogleIssueNeededStart(dateString);

		// Get Area List
		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}

		String propFileName = "ads-" + applicationProperties.getActive() + ".properties";

		for (Issue issue : issueList) {
			// 店舗情報取得
			List<Shop> shopList = shopCustomDao.selectByIssueId(issue.getIssueId());
			GoogleCampaignManage googleCampaignManage = googleCampaignManageCustomDao
					.selectByCampaignId(issue.getGoogleCampaignManageId());
			GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
			googleCampaignDto.setUnitPriceType(UnitPriceType.DISPLAY.getValue());
			googleCampaignDto.setAdType(googleCampaignManage.getAdType());
			List<String> locationStringList = Arrays.asList(issue.getGoogleRegions().split(","));
			List<Long> locationLongList = new ArrayList<Long>();
			for (String location : locationStringList) {
				locationLongList.add(Long.valueOf(location));
			}
			googleCampaignDto.setLocationList(locationLongList);
			googleCampaignDto.setBudget(issue.getGoogleOnedayBudget());

			// ---------広告グループ取得API実行
			GetAdGroups getAdGroups = new GetAdGroups();
			getAdGroups.propFileName = propFileName;
			getAdGroups.campaignId = issue.getGoogleCampaignManageId();
			getAdGroups.googleAccountId = shopList.get(0).getGoogleAccountId();
			getAdGroups.run();
			// ---------広告グループ取得API実行
			UpdateAdGroup updateAdGroup = new UpdateAdGroup();
			updateAdGroup.propFileName = propFileName;
			updateAdGroup.googleAccountId = shopList.get(0).getGoogleAccountId();
			updateAdGroup.googleCampaignDto = googleCampaignDto;
			for (Long adGroupId : getAdGroups.adGroupIdList) {
				updateAdGroup.adGroupId = adGroupId;
				updateAdGroup.run();
			}

			UpdateCampaign updateCampaign = new UpdateCampaign();
			updateCampaign.propFileName = propFileName;
			updateCampaign.googleAccountId = shopList.get(0).getGoogleAccountId();
			updateCampaign.ratio = shopList.get(0).getMarginRatio();
			updateCampaign.googleCampaignDto = googleCampaignDto;
			updateCampaign.run(issue.getGoogleCampaignManageId(), "ON");

			issue.setStartTimestamp(dateTime);
			issueDao.update(issue);
		}
	}

	@Async
	@Override
	@Transactional
	public void stopGoogleIssueAsync() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.now();
		String dateString = df.format(dateTime);
		List<Issue> issueList = issueCustomDao.selectGoogleIssueNeededStop(dateString);

		String propFileName = "ads-" + applicationProperties.getActive() + ".properties";

		for (Issue issue : issueList) {
			// 店舗情報取得
			List<Shop> shopList = shopCustomDao.selectByIssueId(issue.getIssueId());

			UpdateCampaignStatus updateCampaignStatus = new UpdateCampaignStatus();
			updateCampaignStatus.propFileName = propFileName;
			updateCampaignStatus.googleAccountId = shopList.get(0).getGoogleAccountId();
			;
			updateCampaignStatus.run(issue.getGoogleCampaignManageId(), "OFF");

			issue.setEndTimestamp(dateTime);
			issueDao.update(issue);
		}
	}
}
