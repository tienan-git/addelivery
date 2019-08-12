package jp.acepro.haishinsan.service.facebook;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.Ad;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdCreative;
import com.facebook.ads.sdk.AdCreativeLinkData;
import com.facebook.ads.sdk.AdCreativeObjectStorySpec;
import com.facebook.ads.sdk.AdImage;
import com.facebook.ads.sdk.AdSet;
import com.facebook.ads.sdk.AdSet.EnumBidStrategy;
import com.facebook.ads.sdk.AdSet.EnumBillingEvent;
import com.facebook.ads.sdk.AdSet.EnumOptimizationGoal;
import com.facebook.ads.sdk.AdsInsights;
import com.facebook.ads.sdk.AdsInsights.EnumBreakdowns;
import com.facebook.ads.sdk.AdsInsights.EnumDatePreset;
import com.facebook.ads.sdk.Campaign;
import com.facebook.ads.sdk.Campaign.EnumObjective;
import com.facebook.ads.sdk.Campaign.EnumStatus;
import com.facebook.ads.sdk.FlexibleTargeting;
import com.facebook.ads.sdk.IDName;
import com.facebook.ads.sdk.Targeting;
import com.facebook.ads.sdk.TargetingGeoLocation;
import com.facebook.ads.sdk.TargetingGeoLocationCity;
import com.google.gson.Gson;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.bean.FacebookDateReportCsvBean;
import jp.acepro.haishinsan.bean.FacebookDeviceReportCsvBean;
import jp.acepro.haishinsan.bean.FacebookRegionReportCsvBean;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.DspSegmentCustomDao;
import jp.acepro.haishinsan.dao.FacebookCampaignManageCustomDao;
import jp.acepro.haishinsan.dao.FacebookCampaignManageDao;
import jp.acepro.haishinsan.dao.FacebookDeviceReportCustomDao;
import jp.acepro.haishinsan.dao.FacebookDeviceReportDao;
import jp.acepro.haishinsan.dao.FacebookRegionReportCustomDao;
import jp.acepro.haishinsan.dao.FacebookRegionReportDao;
import jp.acepro.haishinsan.dao.FacebookTemplateCustomDao;
import jp.acepro.haishinsan.dao.FacebookTemplateDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.FacebookDeviceReport;
import jp.acepro.haishinsan.db.entity.FacebookRegionReport;
import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbCreativeDto;
import jp.acepro.haishinsan.dto.facebook.FbDeviceReportDto;
import jp.acepro.haishinsan.dto.facebook.FbGraphReportDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbRegionReportDto;
import jp.acepro.haishinsan.dto.facebook.FbReportDisplayDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.dto.facebook.InstagramAccountRes;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.FacebookArrangePlace;
import jp.acepro.haishinsan.enums.FacebookCampaignStatus;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.MediaCollection;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.mapper.FacebookMapper;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.EmailService;
import jp.acepro.haishinsan.util.CalculateUtil;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.DateUtil;
import jp.acepro.haishinsan.util.ReportUtil;

@Service
public class FacebookServiceImpl extends BaseService implements FacebookService {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	FacebookTemplateCustomDao facebookTemplateCustomDao;

	@Autowired
	FacebookTemplateDao facebookTemplateDao;

	@Autowired
	IssueDao issueDao;

	@Autowired
	DspSegmentCustomDao dspSegmentCustomDao;

	@Autowired
	FacebookCampaignManageDao facebookCampaignManageDao;

	@Autowired
	FacebookCampaignManageCustomDao facebookCampaignManageCustomDao;

	@Autowired
	FacebookRegionReportDao facebookRegionReportDao;

	@Autowired
	FacebookRegionReportCustomDao facebookRegionReportCustomDao;

	@Autowired
	FacebookDeviceReportDao facebookDeviceReportDao;

	@Autowired
	FacebookDeviceReportCustomDao facebookDeviceReportCustomDao;

	@Autowired
	EmailService emailService;

	// 案件簡単作成のため、新しいトランザクションで実行する
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createFacebookCampaign(FbCampaignDto fbCampaignDto, IssueDto issueDto) {
		createCampaign(fbCampaignDto, issueDto);
	}

	@Override
	@Transactional
	public void createDefaultTemplate(long shopId) {

		// 表示重視テンプレート作成
		FacebookTemplate facebookTemplate1 = new FacebookTemplate();
		facebookTemplate1.setTemplateName("表示重視");
		facebookTemplate1.setShopId(shopId);
		facebookTemplate1.setTemplatePriority(1);
		facebookTemplate1.setUnitPriceType(UnitPriceType.DISPLAY.getValue());
		facebookTemplateDao.insert(facebookTemplate1);

		// クリック重視テンプレート作成
		FacebookTemplate facebookTemplate2 = new FacebookTemplate();
		facebookTemplate2.setTemplateName("クリック重視");
		facebookTemplate2.setShopId(shopId);
		facebookTemplate2.setTemplatePriority(2);
		facebookTemplate2.setUnitPriceType(UnitPriceType.CLICK.getValue());
		facebookTemplateDao.insert(facebookTemplate2);

		// カスタムテンプレート作成
		FacebookTemplate facebookTemplate3 = new FacebookTemplate();
		facebookTemplate3.setTemplateName("カスタム");
		facebookTemplate3.setShopId(shopId);
		facebookTemplate3.setTemplatePriority(3);
		facebookTemplate3.setUnitPriceType(UnitPriceType.CLICK.getValue());
		facebookTemplateDao.insert(facebookTemplate3);

	}

	@Override
	@Transactional
	public FbTemplateDto create(FbTemplateDto fbTemplateDto) {

		// 例外処理
		// テンプレート名チェック
		if (facebookTemplateCustomDao.selectByTemplateName(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplateName()).size() > 0) {
			// 該当テンプレート名が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E00018);
		}

		// テンプレート優先度チェック
		if (facebookTemplateCustomDao.selectByTemplatePriority(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplatePriority()).size() > 0) {
			// 該当テンプレート優先度が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E00019);
		}

		StringBuilder stringBuilder = new StringBuilder();
		// 地域を組み立てる
		for (Long location : fbTemplateDto.getLocationList()) {
			stringBuilder.append(location.toString());
			stringBuilder.append(",");
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.substring(0, stringBuilder.length() - 1);
		}

		// DTO->Entity
		FacebookTemplate facebookTemplate = new FacebookTemplate();
		facebookTemplate.setTemplateName(fbTemplateDto.getTemplateName());
		facebookTemplate.setShopId(ContextUtil.getCurrentShop().getShopId());
		facebookTemplate.setTemplatePriority(fbTemplateDto.getTemplatePriority());
		facebookTemplate.setUnitPriceType(fbTemplateDto.getUnitPriceType());
		if (stringBuilder.length() > 0) {
			facebookTemplate.setGeolocation(stringBuilder.substring(0, stringBuilder.length() - 1));
		} else {
			facebookTemplate.setGeolocation(stringBuilder.toString());
		}

		// DB access
		facebookTemplateDao.insert(facebookTemplate);

		// Entity->DTO
		FbTemplateDto newTemplateDto = new FbTemplateDto();
		newTemplateDto.setTemplateId(facebookTemplate.getTemplateId());
		newTemplateDto.setTemplateName(facebookTemplate.getTemplateName());
		newTemplateDto.setShopId(facebookTemplate.getShopId());
		newTemplateDto.setTemplatePriority(facebookTemplate.getTemplatePriority());
		newTemplateDto.setUnitPriceType(facebookTemplate.getUnitPriceType());
		// 地域を設定
		if (facebookTemplate.getGeolocation() == null || facebookTemplate.getGeolocation().isEmpty()) {
			newTemplateDto.setLocationList(new ArrayList<Long>());
		} else {
			String[] strArray = facebookTemplate.getGeolocation().split(",");
			List<Long> geolocationLong = Stream.of(strArray).map(element -> Long.parseLong(element)).collect(Collectors.toList());
			newTemplateDto.setLocationList(geolocationLong);
		}

		return newTemplateDto;
	}

	@Override
	@Transactional
	public FbTemplateDto templateDetail(Long templateId) {

		FacebookTemplate facebookTemplate = facebookTemplateDao.selectById(templateId);
		FbTemplateDto fbTemplateDto = FacebookMapper.INSTANCE.mapEntityToDto(facebookTemplate);
		// 地域を設定
		if (fbTemplateDto.getGeolocation() == null || fbTemplateDto.getGeolocation().isEmpty()) {
			fbTemplateDto.setLocationList(new ArrayList<Long>());
		} else {
			String[] strArray = fbTemplateDto.getGeolocation().split(",");
			List<Long> geolocationLong = Stream.of(strArray).map(element -> Long.parseLong(element)).collect(Collectors.toList());
			fbTemplateDto.setLocationList(geolocationLong);
		}

		return fbTemplateDto;

	}

	@Override
	@Transactional
	public void templateUpdate(FbTemplateDto fbTemplateDto) {

		// 例外処理
		// テンプレート名チェック
		List<FacebookTemplate> facebookTemplateListOld1 = facebookTemplateCustomDao.selectByTemplateName(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplateName());
		if (facebookTemplateListOld1.stream().filter(obj -> !obj.getTemplateId().equals(fbTemplateDto.getTemplateId())).count() > 0) {
			// 該当テンプレート名が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E00018);
		}

		// テンプレート優先度チェック
		List<FacebookTemplate> facebookTemplateListOld2 = facebookTemplateCustomDao.selectByTemplatePriority(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplatePriority());
		if (facebookTemplateListOld2.stream().filter(obj -> !obj.getTemplateId().equals(fbTemplateDto.getTemplateId())).count() > 0) {
			// 該当テンプレート優先度が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E00019);
		}

		StringBuilder stringBuilder = new StringBuilder();
		// 地域を組み立てる
		for (Long location : fbTemplateDto.getLocationList()) {
			stringBuilder.append(location.toString());
			stringBuilder.append(",");
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.substring(0, stringBuilder.length() - 1);
		}

		FacebookTemplate facebookTemplate = facebookTemplateDao.selectById(fbTemplateDto.getTemplateId());
		facebookTemplate.setTemplateName(fbTemplateDto.getTemplateName());
		facebookTemplate.setTemplatePriority(fbTemplateDto.getTemplatePriority());
		facebookTemplate.setUnitPriceType(fbTemplateDto.getUnitPriceType());
		if (stringBuilder.length() > 0) {
			facebookTemplate.setGeolocation(stringBuilder.substring(0, stringBuilder.length() - 1));
		} else {
			facebookTemplate.setGeolocation(stringBuilder.toString());
		}

		facebookTemplateDao.update(facebookTemplate);
	}

	@Override
	@Transactional
	public List<FbTemplateDto> searchList() {

		// DBからテンプレートをすべて取得して、リストとして返却
		List<FacebookTemplate> facebookTemplateList = facebookTemplateCustomDao.selectAll(ContextUtil.getCurrentShop().getShopId());
		List<FbTemplateDto> fbTemplateDtoList = FacebookMapper.INSTANCE.mapListEntityToDto(facebookTemplateList);

		for (FbTemplateDto fbTemplateDto : fbTemplateDtoList) {
			// 地域を設定
			if (fbTemplateDto.getGeolocation() == null || fbTemplateDto.getGeolocation().isEmpty()) {
				fbTemplateDto.setLocationList(new ArrayList<Long>());
			} else {
				String[] strArray = fbTemplateDto.getGeolocation().split(",");
				List<Long> geolocationLong = Stream.of(strArray).map(element -> Long.parseLong(element)).collect(Collectors.toList());
				fbTemplateDto.setLocationList(geolocationLong);
			}

		}
		return fbTemplateDtoList;
	}

	@Override
	@Transactional
	public FbTemplateDto templateDelete(Long templateId) {
		// テンプレートIdでDBからテンプレート情報を取得
		FacebookTemplate facebookTemplate = facebookTemplateDao.selectById(templateId);

		facebookTemplateDao.delete(facebookTemplate);

		// Dtoとして返却
		FbTemplateDto fbTemplateDto = FacebookMapper.INSTANCE.mapEntityToDto(facebookTemplate);

		// 地域を設定
		List<String> geolocationList = Arrays.asList(fbTemplateDto.getGeolocation().split(","));
		List<Long> geolocationLong = new ArrayList<Long>();
		for (String geolocation : geolocationList) {
			geolocationLong.add(Long.valueOf(geolocation));
		}
		fbTemplateDto.setLocationList(geolocationLong);

		return fbTemplateDto;
	}

	@Override
	@Transactional
	public List<FacebookCampaignManage> searchFacebookCampaignManageList() {

		// DBから該当店舗所有するキャンペーンをすべて取得して、リストとして返却
		return facebookCampaignManageCustomDao.selectByShopId(ContextUtil.getCurrentShop().getShopId());
	}

	@Override
	@Transactional
	public void createCreative(FbCreativeDto fbCreativeDto, List<DspSegmentListDto> dspSegmentDtoList) {

		LocalDate today = LocalDate.now();

		// マージン率をかけた1日の予算
		Long realDailyBudget = 200L;
		// 配信期間トータルの予算
		Long totalBudget = realDailyBudget;

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());

		// キャンペーンの目的はブランドのリーチ
		EnumObjective enumObjective = Campaign.EnumObjective.VALUE_REACH;
		// キャンペーン配信ステータス設定
		EnumStatus enumCpStatus = Campaign.EnumStatus.VALUE_PAUSED;
		// 審査状態を設定
		ApprovalFlag approvalFlag = ApprovalFlag.WAITING;

		// fbCampaignDto.setCampaignDisplayStatus(FacebookCampaignStatus.of(enumCpStatus.toString()).getLabel());
		// fbCampaignDto.setCheckStatus(approvalFlag.getValue());
		// AdSet配信ステータス設定
		com.facebook.ads.sdk.AdSet.EnumStatus enumSetStatus = AdSet.EnumStatus.VALUE_ACTIVE;
		// Ad配信ステータス設定
		com.facebook.ads.sdk.Ad.EnumStatus enumAdStatus = Ad.EnumStatus.VALUE_ACTIVE;

		try {
			int cnt = 0;
			AdAccount account = new AdAccount(applicationProperties.getFacebookAccountId(), context);
			// 請求タイミング
			EnumBillingEvent enumBillingEvent = AdSet.EnumBillingEvent.VALUE_IMPRESSIONS;
			// 最大入札価格を設定
			Long bidAmount = 200l;
			// 地域設定 とりあえず固定
			// location_typesが指定されていない場合、デフォルトはこの地域に住んでる人です。
			List<TargetingGeoLocationCity> TargetingGeoLocationCityList = new ArrayList<TargetingGeoLocationCity>();
			TargetingGeoLocationCityList.add(new TargetingGeoLocationCity().setFieldKey("2686399"));

			// 趣味設定
			List<IDName> idNameList = new ArrayList<IDName>();
			idNameList.add(new IDName().setFieldId("6003484127669").setFieldName("Casino"));// カジノ
			idNameList.add(new IDName().setFieldId("6003012317397").setFieldName("Gambling"));// ギャンブル

			// ターゲット
			// 性別のデフォルトはすべてです。
			// デフォルトはこの地域に住んでる人
			Targeting targeting = new Targeting().setFieldAgeMin(18L) // 最小年齢
					.setFieldInterests(idNameList)// 趣味
					.setFieldGeoLocations(new TargetingGeoLocation().setFieldCities(TargetingGeoLocationCityList));
			targeting.setFieldPublisherPlatforms(Arrays.asList("facebook")).setFieldFacebookPositions(Arrays.asList("feed"));
			// 広告セットを作成
			String startDateTime = DateFormatter.yyyyMMdd_HYPHEN.format(today.plusDays(1L)) + "T00:00:00+0900";
			String nextEndDateTime = DateFormatter.yyyyMMdd_HYPHEN.format(today.plusDays(180L)) + "T00:00:00+0900";
			AdImage adImage = account.createAdImage().addUploadFile("filename", fbCreativeDto.getImageFile()).execute();
			for (DspSegmentListDto dspSegmentListDto : dspSegmentDtoList) {
				cnt++;
				String campaignName = fbCreativeDto.getCreativeName() + "Campaign" + cnt;
				Campaign campaign = account.createCampaign().setName(campaignName).setObjective(enumObjective).setStatus(enumCpStatus).execute();
				String campaignId = campaign.fetch().getId();

				// 広告セット名
				String adSetName = fbCreativeDto.getCreativeName() + "AdSet" + cnt;
				AdSet adset = account.createAdSet().setName(adSetName).setCampaignId(campaignId)
						// 配信ステータス
						.setStatus(enumSetStatus)
						// 入札戦略（最小コスト）
						.setDailyBudget(realDailyBudget).setStartTime(startDateTime).setEndTime(nextEndDateTime).setBillingEvent(enumBillingEvent).setBidStrategy(EnumBidStrategy.VALUE_LOWEST_COST_WITH_BID_CAP).setBidAmount(bidAmount)
						// 広告配信の最適化対象
						.setOptimizationGoal(EnumOptimizationGoal.VALUE_IMPRESSIONS).setTargeting(targeting).execute();
				String adSetId = adset.getFieldId();

				String linkUrl = dspSegmentListDto.getUrl();
				AdCreativeLinkData link = (new AdCreativeLinkData()).setFieldLink(linkUrl).setFieldImageHash(adImage.getFieldHash());
				AdCreativeObjectStorySpec spec = (new AdCreativeObjectStorySpec()).setFieldPageId(ContextUtil.getCurrentShop().getFacebookPageId()).setFieldLinkData(link);
				AdCreative creative = account.createAdCreative().setName(fbCreativeDto.getCreativeName() + "Creative" + cnt).setObjectStorySpec(spec).execute();
				account.createAd().setName(fbCreativeDto.getCreativeName() + "Ad" + cnt).setAdsetId(Long.parseLong(adSetId)).setCreative(creative).setStatus(enumAdStatus).execute();

				FacebookCampaignManage facebookCampaignManage = new FacebookCampaignManage();
				facebookCampaignManage.setCampaignId(campaignId);
				facebookCampaignManage.setCampaignName(campaignName);
				facebookCampaignManage.setShopId(ContextUtil.getCurrentShop().getShopId());
				facebookCampaignManage.setBudget(totalBudget);
				facebookCampaignManage.setApprovalFlag(approvalFlag.getValue());
				facebookCampaignManage.setImageUrl(adImage.getFieldUrl());
				facebookCampaignManage.setLinkUrl(linkUrl);
				facebookCampaignManageDao.insert(facebookCampaignManage);

			}

		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
	}

	@Override
	@Transactional
	public FbCampaignDto createCampaign(FbCampaignDto fbCampaignDto, IssueDto issueDto) {

		// 配信期間チェック
		LocalDate startDate = LocalDate.parse(fbCampaignDto.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endDate = LocalDate.parse(fbCampaignDto.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if (startDate.isAfter(endDate)) {
			// 配信期間の開始日と終了日を確認してください。
			throw new BusinessException(ErrorCodeConstant.E00010);
		}

		// 地域チェック
		if (fbCampaignDto.getLocationList().size() == 0) {
			// 配信地域を選択してください。
			throw new BusinessException(ErrorCodeConstant.E00011);
		}

		// 配信時間まで設定
		String startDateTime = fbCampaignDto.getStartDate() + "T00:00:00+0900";
		String endDateTime = fbCampaignDto.getEndDate() + "T23:59:59+0900";

		String startDateString = startDateTime.substring(0, 10);
		String endDateString = endDateTime.substring(0, 10);
		// 1日の予算
		Long dailyBudget = fbCampaignDto.getDailyBudget();
		// マージン率をかけた1日の予算
		Long realDailyBudget = CalculateUtil.calRealBudget(dailyBudget);
		// 配信期間トータルの予算
		Long totalBudget = CalculateUtil.calTotalBudget(dailyBudget, startDateString, endDateString);

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());

		// キャンペーンの目的はブランドのリーチ
		EnumObjective enumObjective = Campaign.EnumObjective.VALUE_REACH;

		// キャンペーン配信ステータス設定
		EnumStatus enumCpStatus = Campaign.EnumStatus.VALUE_ACTIVE;
		// 審査状態を設定
		ApprovalFlag approvalFlag = ApprovalFlag.COMPLETED;
		if (Flag.ON.getValue().toString().equals(ContextUtil.getCurrentShop().getSalesCheckFlag())) {
			// 営業チェックが必要な場合、停止状態で作られる
			enumCpStatus = Campaign.EnumStatus.VALUE_PAUSED;
			// 営業チェックが必要な場合、審査状態を承認待ちにする
			approvalFlag = ApprovalFlag.WAITING;
		}
		fbCampaignDto.setCampaignDisplayStatus(FacebookCampaignStatus.of(enumCpStatus.toString()).getLabel());
		fbCampaignDto.setCheckStatus(approvalFlag.getValue());
		// AdSet配信ステータス設定
		com.facebook.ads.sdk.AdSet.EnumStatus enumSetStatus = AdSet.EnumStatus.VALUE_ACTIVE;
		// Ad配信ステータス設定
		com.facebook.ads.sdk.Ad.EnumStatus enumAdStatus = Ad.EnumStatus.VALUE_ACTIVE;

		try {
			String campaignName = fbCampaignDto.getCampaignName();
			AdAccount account = new AdAccount(applicationProperties.getFacebookAccountId(), context);
			Campaign campaign = account.createCampaign().setName(campaignName).setObjective(enumObjective).setStatus(enumCpStatus).execute();
			String campaignId = campaign.fetch().getId();
			fbCampaignDto.setCampaignId(campaignId);
			// String campaignId = "23843046668180277";
			// System.out.println(campaignId);

			// 広告セット名
			String adSetName = campaignName + "広告セット";
			// 請求タイミング
			EnumBillingEvent enumBillingEvent = AdSet.EnumBillingEvent.VALUE_IMPRESSIONS;
			// 最大入札価格を設定（地域の価格の平均値を算出）
			Long bidAmount = 200l;
			if (fbCampaignDto.getUnitPriceType().equals(UnitPriceType.CLICK.getValue())) {
				Double averageClickUnitPriceDouble = CodeMasterServiceImpl.facebookAreaUnitPriceClickList.stream().filter(obj -> fbCampaignDto.getLocationList().contains(obj.getFirst())).mapToInt(obj -> obj.getSecond()).average().getAsDouble();
				bidAmount = Math.round(averageClickUnitPriceDouble);
			}
			if (fbCampaignDto.getUnitPriceType().equals(UnitPriceType.DISPLAY.getValue())) {
				Double averageDisplayUnitPriceDouble = CodeMasterServiceImpl.facebookAreaUnitPriceDisplayList.stream().filter(obj -> fbCampaignDto.getLocationList().contains(obj.getFirst())).mapToInt(obj -> obj.getSecond()).average().getAsDouble();
				bidAmount = Math.round(averageDisplayUnitPriceDouble);
			}

			// 地域設定
			// location_typesが指定されていない場合、デフォルトはこの地域に住んでる人です。
			List<TargetingGeoLocationCity> TargetingGeoLocationCityList = new ArrayList<TargetingGeoLocationCity>();
			for (Long location : fbCampaignDto.getLocationList()) {
				TargetingGeoLocationCityList.add(new TargetingGeoLocationCity().setFieldKey(location.toString()));
			}
			// 趣味設定
			List<IDName> idNameList = new ArrayList<IDName>();
			idNameList.add(new IDName().setFieldId("6003484127669").setFieldName("Casino"));// カジノ
			idNameList.add(new IDName().setFieldId("6003012317397").setFieldName("Gambling"));// ギャンブル

			// ターゲット
			// 性別のデフォルトはすべてです。
			// デフォルトはこの地域に住んでる人
			// 配置場所はfacebookのフィードとinstagramのフィードのみ
			Targeting targeting = new Targeting().setFieldAgeMin(18L) // 最小年齢
					.setFieldInterests(idNameList)// 趣味
					.setFieldGeoLocations(new TargetingGeoLocation().setFieldCities(TargetingGeoLocationCityList));

			if (fbCampaignDto.getArrangePlace().equals(FacebookArrangePlace.BOTH.getValue())) {
				targeting.setFieldPublisherPlatforms(Arrays.asList("facebook", "instagram")).setFieldFacebookPositions(Arrays.asList("feed")).setFieldInstagramPositions(Arrays.asList("stream"));
			}
			if (fbCampaignDto.getArrangePlace().equals(FacebookArrangePlace.FACEBOOK.getValue())) {
				targeting.setFieldPublisherPlatforms(Arrays.asList("facebook")).setFieldFacebookPositions(Arrays.asList("feed"));
			}
			if (fbCampaignDto.getArrangePlace().equals(FacebookArrangePlace.INSTAGRAM.getValue())) {
				targeting.setFieldPublisherPlatforms(Arrays.asList("instagram")).setFieldInstagramPositions(Arrays.asList("stream"));
			}

			// 広告セットを作成
			String nextEndDateTime = endDateTime;
			if (startDate.isEqual(endDate)) {
				nextEndDateTime = DateFormatter.yyyyMMdd_HYPHEN.format(endDate.plusDays(1L)) + "T00:00:00+0900";
			}
			AdSet adset = account.createAdSet().setName(adSetName).setCampaignId(campaignId)
					// 配信ステータス
					.setStatus(enumSetStatus)
					// 入札戦略（最小コスト）
					.setDailyBudget(realDailyBudget).setStartTime(startDateTime).setEndTime(nextEndDateTime).setBillingEvent(enumBillingEvent).setBidStrategy(EnumBidStrategy.VALUE_LOWEST_COST_WITH_BID_CAP).setBidAmount(bidAmount)
					// 広告配信の最適化対象
					.setOptimizationGoal(EnumOptimizationGoal.VALUE_IMPRESSIONS).setTargeting(targeting).execute();
			fbCampaignDto.setStartDate(DateUtil.toDateTime(startDateTime));
			fbCampaignDto.setEndDate(DateUtil.toDateTime(endDateTime));

			String adSetId = adset.fetch().getFieldId();

			// SegmentIdでDBからセグメント情報取得
			SegmentManage segmentManage = dspSegmentCustomDao.selectBySegmentId(fbCampaignDto.getSegmentId());
			if (segmentManage == null) {
				// セグメントのURLが存在しない。
				throw new BusinessException(ErrorCodeConstant.E00012);
			}
			fbCampaignDto.setLinkUrl(segmentManage.getUrl());
			AdImage adImage = account.createAdImage().addUploadFile("filename", fbCampaignDto.getImageFile()).execute();
			AdCreativeLinkData link = (new AdCreativeLinkData()).setFieldLink(segmentManage.getUrl()).setFieldImageHash(adImage.getFieldHash());
			// Page AccessToken 取得
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
			builder = builder.scheme(applicationProperties.getDspScheme());
			builder = builder.host("graph.facebook.com");
			builder = builder.path(ContextUtil.getCurrentShop().getFacebookPageId());
			builder = builder.queryParam("fields", "access_token");
			builder = builder.queryParam("access_token", applicationProperties.getPageToken());
			String resource = builder.build().toUri().toString();

			HashMap<String, String> res = null;

			try {
				res = call(resource, HttpMethod.GET, null, null, HashMap.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (Objects.isNull(res) || !res.containsKey("access_token")) {
				throw new BusinessException(ErrorCodeConstant.E40003);
			}

			String pageToken = res.get("access_token");

			// Insアカウントを取得 TODO
			UriComponentsBuilder insAccountBuilder = UriComponentsBuilder.newInstance();
			insAccountBuilder = insAccountBuilder.scheme(applicationProperties.getDspScheme());
			insAccountBuilder = insAccountBuilder.host("graph.facebook.com");
			insAccountBuilder = insAccountBuilder.path("/v3.2/" + ContextUtil.getCurrentShop().getFacebookPageId() + "/instagram_accounts");
			insAccountBuilder = insAccountBuilder.queryParam("access_token", pageToken);
			String insAccountResource = insAccountBuilder.build().toUri().toString();
			InstagramAccountRes insAccount = null;
			try {
				insAccount = call(insAccountResource, HttpMethod.GET, null, null, InstagramAccountRes.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			AdCreativeObjectStorySpec spec = (new AdCreativeObjectStorySpec()).setFieldInstagramActorId(insAccount.getData().get(0).getId()).setFieldPageId(ContextUtil.getCurrentShop().getFacebookPageId()).setFieldLinkData(link);
			AdCreative creative = account.createAdCreative().setName(fbCampaignDto.getCampaignName() + "Creative").setObjectStorySpec(spec).execute();
			account.createAd().setName(fbCampaignDto.getCampaignName() + "Ad").setAdsetId(Long.parseLong(adSetId)).setCreative(creative).setStatus(enumAdStatus).execute();

			FacebookCampaignManage facebookCampaignManage = new FacebookCampaignManage();
			facebookCampaignManage.setCampaignId(campaignId);
			facebookCampaignManage.setCampaignName(campaignName);
			facebookCampaignManage.setSegmentId(fbCampaignDto.getSegmentId());
			facebookCampaignManage.setBudget(totalBudget);
			facebookCampaignManage.setApprovalFlag(approvalFlag.getValue());
			facebookCampaignManageDao.insert(facebookCampaignManage);

			Issue issue = new Issue();
			if (issueDto == null) {
				issue = new Issue();
				issue.setShopId(ContextUtil.getCurrentShop().getShopId());
				issue.setFacebookCampaignManageId(facebookCampaignManage.getFacebookCampaignManageId());
				issue.setCampaignName(campaignName);
				issue.setBudget(totalBudget);
				issue.setStartDate(startDateString);
				issue.setEndDate(endDateString);
				issueDao.insert(issue);
			} else {
				issueDto.setFacebookCampaignManageId(facebookCampaignManage.getFacebookCampaignManageId());
			}

			// メール送信
			EmailDto emailDto = new EmailDto();
			emailDto.setIssueId(issue.getIssueId());
			EmailCampDetailDto emailCampDetailDto = new EmailCampDetailDto();
			emailCampDetailDto.setMediaType(MediaCollection.FACEBOOK.getValue());
			emailCampDetailDto.setCampaignId(campaignId);
			emailCampDetailDto.setCampaignName(campaignName);
			List<EmailCampDetailDto> emailCampDetailDtoList = new ArrayList<EmailCampDetailDto>();
			emailCampDetailDtoList.add(emailCampDetailDto);
			emailDto.setCampaignList(emailCampDetailDtoList);
			emailDto.setTemplateType(EmailTemplateType.CAMPAIGN.getValue());
			emailService.sendEmail(emailDto);

			// アップロードしたイメージを削除する
			fbCampaignDto.getImageFile().delete();

			return fbCampaignDto;
		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
	}

	@Override
	@Transactional
	public List<FbCampaignDto> campaignList(List<FacebookCampaignManage> facebookCampaignManageList) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());

		List<FbCampaignDto> fbCampaignDtoList = new ArrayList<FbCampaignDto>();

		if (facebookCampaignManageList.size() == 0) {
			return fbCampaignDtoList;
		}

		for (FacebookCampaignManage facebookCampaignManage : facebookCampaignManageList) {
			FbCampaignDto fbCampaignDto = new FbCampaignDto();
			fbCampaignDto.setCampaignId(facebookCampaignManage.getCampaignId());
			fbCampaignDto.setCampaignName(facebookCampaignManage.getCampaignName());
			fbCampaignDto.setLinkUrl(facebookCampaignManage.getLinkUrl());
			fbCampaignDto.setImageUrl(facebookCampaignManage.getImageUrl());

			fbCampaignDtoList.add(fbCampaignDto);
		}

		return fbCampaignDtoList;

	}

	@Override
	@Transactional
	public FbCampaignDto campaignDetail(String campaignId) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());

		// DBからキャンペーンを取得する
		FacebookCampaignManage facebookCampaignManage = facebookCampaignManageCustomDao.selectByCampaignId(campaignId);
		// SegmentIdでDBからセグメント情報取得
		SegmentManage segmentManage = dspSegmentCustomDao.selectBySegmentId(facebookCampaignManage.getSegmentId());
		if (segmentManage == null) {
			// セグメントのURLが存在しない。
			throw new BusinessException(ErrorCodeConstant.E00012);
		}
		FbCampaignDto fbCampaignDto = new FbCampaignDto();
		fbCampaignDto.setLinkUrl(segmentManage.getUrl());
		// 審査フラグを設定
		fbCampaignDto.setCheckStatus(facebookCampaignManage.getApprovalFlag());
		try {
			Campaign campaign = Campaign.fetchById(campaignId, context);

			fbCampaignDto.setCampaignId(campaign.getFieldId());
			fbCampaignDto.setCampaignName(campaign.getFieldName());
			if (EnumStatus.VALUE_ACTIVE.equals(campaign.getFieldStatus())) {
				fbCampaignDto.setCampaignDisplayStatus(FacebookCampaignStatus.ACTIVE.getLabel());
			} else {
				fbCampaignDto.setCampaignDisplayStatus(FacebookCampaignStatus.PAUSED.getLabel());
			}
			fbCampaignDto.setStartDate(DateUtil.toDateTime(campaign.getFieldStartTime()));
			fbCampaignDto.setEndDate(DateUtil.toDateTime(campaign.getFieldStopTime()));
			fbCampaignDto.setUpdatedDate(DateUtil.toDateTime(campaign.getFieldUpdatedTime()));
			fbCampaignDto.setCreatedDate(DateUtil.toDateTime(campaign.getFieldCreatedTime()));

			APINodeList<AdSet> adSetList = campaign.getAdSets().requestAllFields().execute();

			if (adSetList != null && adSetList.size() > 0) {
				AdSet adSet = adSetList.get(0);
				fbCampaignDto.setDailyBudget(Long.valueOf(adSet.getFieldDailyBudget()));
				APINodeList<Ad> adList = adSet.getAds().requestAllFields().execute();
				if (adList != null && adList.size() > 0) {
					Ad ad = adList.get(0);
					ad.getFieldSourceAd();
				}
			}
			// System.out.println(fbCampaignDto);

		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
		return fbCampaignDto;

	}

	@Override
	@Transactional
	public void deleteCampaign(String campaignId) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());
		// キャンペーン配信ステータス設定

		try {
			// キャンペーンを取得
			Campaign campaign = Campaign.fetchById(campaignId, context);
			// キャンプーンのステータスがアクティブの場合、一時停止に変更
			if (campaign.getFieldStatus().equals(Campaign.EnumStatus.VALUE_ACTIVE)) {
				campaign.update().setStatus(Campaign.EnumStatus.VALUE_PAUSED).execute();
			}

		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}

		// DBからキャンペーンを取得する
		FacebookCampaignManage facebookCampaignManage = facebookCampaignManageCustomDao.selectByCampaignId(campaignId);
		// 該当キャンペーンを論理削除
		facebookCampaignManage.setIsActived(Flag.OFF.getValue());
		facebookCampaignManageDao.update(facebookCampaignManage);
	}

	@Override
	@Transactional
	public void updateCampaignStatus(String campaignId, String switchFlag) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());

		// キャンペーン配信ステータス設定
		EnumStatus enumCpStatus;
		if (switchFlag.equals("ON")) {
			enumCpStatus = Campaign.EnumStatus.VALUE_ACTIVE;
		} else {
			enumCpStatus = Campaign.EnumStatus.VALUE_PAUSED;
		}

		try {
			// キャンペーンを取得
			Campaign campaign = Campaign.fetchById(campaignId, context);
			// キャンプーンのステータスを更新
			campaign.update().setStatus(enumCpStatus).execute();

		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
	}

	@Override
	@Transactional
	public void updateCampaignCheckStatus(String campaignId, String checkStatus) {

		// DBからキャンペーンを取得する
		FacebookCampaignManage facebookCampaignManage = facebookCampaignManageCustomDao.selectByCampaignId(campaignId);
		// 審査状態が不一致の場合、更新する
		if (!checkStatus.equals(facebookCampaignManage.getApprovalFlag())) {
			facebookCampaignManage.setApprovalFlag(checkStatus);
			facebookCampaignManageDao.update(facebookCampaignManage);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getReportDetails(EnumDatePreset enumDatePreset) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());
		Gson gson = new Gson();
		LocalDate localDate = LocalDate.now();
		;
		if (EnumDatePreset.VALUE_YESTERDAY.equals(enumDatePreset)) {
			localDate = localDate.minusDays(1);
		}
		try {
			// String idList = "[\"23843032433030277\",\"23843046668180277\"]}]";

			APINodeList<AdsInsights> requestGetRegionInsights = new AdAccount(applicationProperties.getFacebookAccountId(), context).getInsights().setLevel(AdsInsights.EnumLevel.VALUE_CAMPAIGN)

					// .setFiltering("[]")
					.setTimeIncrement("1").setBreakdowns(Arrays.asList(EnumBreakdowns.VALUE_REGION))
					// .setFiltering("[{\"field\":\"campaign.id\",\"operator\":\"IN\",\"value\":[\"ARCHIVED\",\"DELETED\"]}]")
					// .setFiltering("[{\"field\":\"campaign.id\",\"operator\":\"IN\",\"value\":" +
					// idList)
					.setDatePreset(enumDatePreset)
					// .setActionReportTime(EnumActionReportTime.VALUE_IMPRESSION)
					// .setDatePreset(AdsInsights.EnumDatePreset.VALUE_LIFETIME)
					// .setTimeRange("{\"since\":\"2018-08-08\",\"until\":\"2018-11-07\"}")
					// .requestField("actions:link_click")
					// .requestField("actions:photo_view")
					// .requestField("cost_per_result")
					.requestField("campaign_id").requestField("campaign_name").requestField("impressions").requestField("clicks").requestField("spend")
					// .requestAllFields()
					.execute();
			// List<FacebookRegionReport> facebookRegionReportList = new
			// ArrayList<FacebookRegionReport>();
			for (AdsInsights requestGetRegionInsight : requestGetRegionInsights) {

				FbRegionReportDto fbRegionReportDto = gson.fromJson(requestGetRegionInsight.getRawValue(), FbRegionReportDto.class);

				FacebookRegionReport facebookRegionReportOld = facebookRegionReportCustomDao.selectByCampaignIdDateRegion(fbRegionReportDto.getCampaign_id(), DateUtil.toLocalDate(fbRegionReportDto.getDate_start()), fbRegionReportDto.getRegion());
				if (facebookRegionReportOld != null && facebookRegionReportOld.getCampaignId() != null) {
					facebookRegionReportOld.setImpressions(Long.parseLong(fbRegionReportDto.getImpressions()));
					facebookRegionReportOld.setClicks(Long.parseLong(fbRegionReportDto.getClicks()));
					facebookRegionReportOld.setSpend(Long.parseLong(fbRegionReportDto.getSpend()));
					facebookRegionReportDao.update(facebookRegionReportOld);
				} else {
					FacebookRegionReport facebookRegionReportNew = new FacebookRegionReport();
					facebookRegionReportNew.setCampaignId(fbRegionReportDto.getCampaign_id());
					facebookRegionReportNew.setCampaignName(fbRegionReportDto.getCampaign_name());
					facebookRegionReportNew.setDate(DateUtil.toLocalDate(fbRegionReportDto.getDate_start()));
					facebookRegionReportNew.setRegion(fbRegionReportDto.getRegion());
					facebookRegionReportNew.setImpressions(Long.parseLong(fbRegionReportDto.getImpressions()));
					facebookRegionReportNew.setClicks(Long.parseLong(fbRegionReportDto.getClicks()));
					facebookRegionReportNew.setSpend(Long.parseLong(fbRegionReportDto.getSpend()));
					facebookRegionReportDao.insert(facebookRegionReportNew);
				}

				// facebookRegionReportList.add(facebookRegionReport);
			}

			APINodeList<AdsInsights> requestGetDeviceInsights = new AdAccount(applicationProperties.getFacebookAccountId(), context).getInsights().setLevel(AdsInsights.EnumLevel.VALUE_CAMPAIGN).setTimeIncrement("1")
					// .setDatePreset(AdsInsights.EnumDatePreset.VALUE_LIFETIME)
					.setBreakdowns(Arrays.asList(EnumBreakdowns.VALUE_DEVICE_PLATFORM)).setDatePreset(enumDatePreset)
					// .setTimeRange("{\"since\":\"2018-08-08\",\"until\":\"2018-11-07\"}")
					// .requestField("actions:link_click")
					// .requestField("actions:photo_view")
					// .requestField("cost_per_result")
					.requestField("campaign_id").requestField("campaign_name").requestField("impressions").requestField("clicks").requestField("spend")
					// .requestAllFields()
					.execute();
			// List<FacebookDeviceReport> facebookDeviceReportList = new
			// ArrayList<FacebookDeviceReport>();
			for (AdsInsights requestGetDeviceInsight : requestGetDeviceInsights) {

				FbDeviceReportDto fbDeviceReportDto = gson.fromJson(requestGetDeviceInsight.getRawValue(), FbDeviceReportDto.class);

				FacebookDeviceReport facebookDeviceReportOld = facebookDeviceReportCustomDao.selectByCampaignIdDateDevice(fbDeviceReportDto.getCampaign_id(), DateUtil.toLocalDate(fbDeviceReportDto.getDate_start()), fbDeviceReportDto.getDevice());
				if (facebookDeviceReportOld != null && facebookDeviceReportOld.getCampaignId() != null) {
					facebookDeviceReportOld.setImpressions(Long.parseLong(fbDeviceReportDto.getImpressions()));
					facebookDeviceReportOld.setClicks(Long.parseLong(fbDeviceReportDto.getClicks()));
					facebookDeviceReportOld.setSpend(Long.parseLong(fbDeviceReportDto.getSpend()));

					facebookDeviceReportDao.update(facebookDeviceReportOld);
				} else {
					FacebookDeviceReport facebookDeviceReportNew = new FacebookDeviceReport();
					facebookDeviceReportNew.setCampaignId(fbDeviceReportDto.getCampaign_id());
					facebookDeviceReportNew.setCampaignName(fbDeviceReportDto.getCampaign_name());
					facebookDeviceReportNew.setDate(DateUtil.toLocalDate(fbDeviceReportDto.getDate_start()));
					facebookDeviceReportNew.setDevice(fbDeviceReportDto.getDevice() == null ? "不明" : fbDeviceReportDto.getDevice());
					facebookDeviceReportNew.setImpressions(Long.parseLong(fbDeviceReportDto.getImpressions()));
					facebookDeviceReportNew.setClicks(Long.parseLong(fbDeviceReportDto.getClicks()));
					facebookDeviceReportNew.setSpend(Long.parseLong(fbDeviceReportDto.getSpend()));
					facebookDeviceReportDao.insert(facebookDeviceReportNew);
				}
				// facebookDeviceReportList.add(facebookDeviceReport);
			}

		} catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
		}
	}

	@Override
	@Transactional
	public List<FbReportDisplayDto> getDeviceReport(List<String> campaignIdList, String startDate, String endDate) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = new ArrayList<FbReportDisplayDto>();

		List<FacebookDeviceReport> facebookDeviceReportList = facebookDeviceReportCustomDao.selectDeviceReport(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));
		for (FacebookDeviceReport facebookDeviceReport : facebookDeviceReportList) {
			FbReportDisplayDto fbReportDisplayDto = new FbReportDisplayDto();
			fbReportDisplayDto.setCampaignId(facebookDeviceReport.getCampaignId());
			fbReportDisplayDto.setCampaignName(facebookDeviceReport.getCampaignName());
			fbReportDisplayDto.setDevice(facebookDeviceReport.getDevice());
			fbReportDisplayDto.setImpressions(facebookDeviceReport.getImpressions());
			fbReportDisplayDto.setClicks(facebookDeviceReport.getClicks());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDeviceReport.getSpend());
			fbReportDisplayDto.setSpend(displayCosts.longValue());
			fbReportDisplayDto.setCtr(ReportUtil.calCtr(facebookDeviceReport.getClicks(), facebookDeviceReport.getImpressions()));
			fbReportDisplayDto.setCpc(ReportUtil.calCpc(facebookDeviceReport.getClicks(), displayCosts));
			fbReportDisplayDto.setCpm(ReportUtil.calCpm(facebookDeviceReport.getImpressions(), displayCosts));

			fbReportDisplayDtoList.add(fbReportDisplayDto);
		}

		return getSummary(fbReportDisplayDtoList);

	}

	@Override
	@Transactional
	public List<FbReportDisplayDto> getRegionReport(List<String> campaignIdList, String startDate, String endDate) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = new ArrayList<FbReportDisplayDto>();

		List<FacebookRegionReport> facebookRegionReportList = facebookRegionReportCustomDao.selectRegionReport(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));
		for (FacebookRegionReport facebookRegionReport : facebookRegionReportList) {
			FbReportDisplayDto fbReportDisplayDto = new FbReportDisplayDto();
			fbReportDisplayDto.setCampaignId(facebookRegionReport.getCampaignId());
			fbReportDisplayDto.setCampaignName(facebookRegionReport.getCampaignName());
			fbReportDisplayDto.setRegion(facebookRegionReport.getRegion());
			fbReportDisplayDto.setImpressions(facebookRegionReport.getImpressions());
			fbReportDisplayDto.setClicks(facebookRegionReport.getClicks());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookRegionReport.getSpend());
			fbReportDisplayDto.setSpend(displayCosts.longValue());
			fbReportDisplayDto.setCtr(ReportUtil.calCtr(facebookRegionReport.getClicks(), facebookRegionReport.getImpressions()));
			fbReportDisplayDto.setCpc(ReportUtil.calCpc(facebookRegionReport.getClicks(), displayCosts));
			fbReportDisplayDto.setCpm(ReportUtil.calCpm(facebookRegionReport.getImpressions(), displayCosts));

			fbReportDisplayDtoList.add(fbReportDisplayDto);
		}

		return getSummary(fbReportDisplayDtoList);

	}

	@Override
	@Transactional
	public List<FbReportDisplayDto> getDateReport(List<String> campaignIdList, String startDate, String endDate) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = new ArrayList<FbReportDisplayDto>();

		List<FacebookDeviceReport> facebookDeviceReportList = facebookDeviceReportCustomDao.selectDateReport(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));
		for (FacebookDeviceReport facebookDeviceReport : facebookDeviceReportList) {
			FbReportDisplayDto fbReportDisplayDto = new FbReportDisplayDto();
			fbReportDisplayDto.setCampaignId(facebookDeviceReport.getCampaignId());
			fbReportDisplayDto.setCampaignName(facebookDeviceReport.getCampaignName());
			fbReportDisplayDto.setDate(DateUtil.fromLocalDate(facebookDeviceReport.getDate()));
			fbReportDisplayDto.setImpressions(facebookDeviceReport.getImpressions());
			fbReportDisplayDto.setClicks(facebookDeviceReport.getClicks());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDeviceReport.getSpend());
			fbReportDisplayDto.setSpend(displayCosts.longValue());
			fbReportDisplayDto.setCtr(ReportUtil.calCtr(facebookDeviceReport.getClicks(), facebookDeviceReport.getImpressions()));
			fbReportDisplayDto.setCpc(ReportUtil.calCpc(facebookDeviceReport.getClicks(), displayCosts));
			fbReportDisplayDto.setCpm(ReportUtil.calCpm(facebookDeviceReport.getImpressions(), displayCosts));

			fbReportDisplayDtoList.add(fbReportDisplayDto);
		}

		return getSummary(fbReportDisplayDtoList);

	}

	// DB : デバイス別レポート（グラフ用）
	@Override
	@Transactional
	public FbGraphReportDto getFacebookDeviceReportingGraph(List<String> campaignIdList, String startDate, String endDate) {

		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<FacebookDeviceReport> facebookDeviceReportList = facebookDeviceReportCustomDao.selectDeviceReportGraph(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (FacebookDeviceReport facebookDeviceReport : facebookDeviceReportList) {
			reportTypeList.add(facebookDeviceReport.getDevice());
			impressionList.add(facebookDeviceReport.getImpressions().toString());
			clicksList.add(facebookDeviceReport.getClicks().toString());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDeviceReport.getSpend()).longValue();
			spendList.add(displayCosts.toString());
			CTRList.add(ReportUtil.calCtr(facebookDeviceReport.getClicks(), facebookDeviceReport.getImpressions()).toString());
			CPCList.add(ReportUtil.calCpc(facebookDeviceReport.getClicks(), displayCosts).toString());
			CPMList.add(ReportUtil.calCpm(facebookDeviceReport.getImpressions(), displayCosts).toString());
		}

		FbGraphReportDto facebookGraphReportDto = new FbGraphReportDto();
		facebookGraphReportDto.setReportTypeList(reportTypeList);
		facebookGraphReportDto.setImpressionList(impressionList);
		facebookGraphReportDto.setClicksList(clicksList);
		facebookGraphReportDto.setSpendList(spendList);
		facebookGraphReportDto.setCTRList(CTRList);
		facebookGraphReportDto.setCPCList(CPCList);
		facebookGraphReportDto.setCPMList(CPMList);

		return facebookGraphReportDto;
	}

	// DB : 日別レポート（グラフ用）
	@Override
	@Transactional
	public FbGraphReportDto getFacebookDateReportingGraph(List<String> campaignIdList, String startDate, String endDate) {

		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<FacebookDeviceReport> facebookDateReportList = facebookDeviceReportCustomDao.selectDateReportGraph(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (FacebookDeviceReport facebookDateReport : facebookDateReportList) {
			reportTypeList.add(facebookDateReport.getDate().toString());
			impressionList.add(facebookDateReport.getImpressions().toString());
			clicksList.add(facebookDateReport.getClicks().toString());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookDateReport.getSpend()).longValue();
			spendList.add(displayCosts.toString());
			CTRList.add(ReportUtil.calCtr(facebookDateReport.getClicks(), facebookDateReport.getImpressions()).toString());
			CPCList.add(ReportUtil.calCpc(facebookDateReport.getClicks(), displayCosts).toString());
			CPMList.add(ReportUtil.calCpm(facebookDateReport.getImpressions(), displayCosts).toString());
		}

		FbGraphReportDto facebookGraphReportDto = new FbGraphReportDto();
		facebookGraphReportDto.setReportTypeList(reportTypeList);
		facebookGraphReportDto.setImpressionList(impressionList);
		facebookGraphReportDto.setClicksList(clicksList);
		facebookGraphReportDto.setSpendList(spendList);
		facebookGraphReportDto.setCTRList(CTRList);
		facebookGraphReportDto.setCPCList(CPCList);
		facebookGraphReportDto.setCPMList(CPMList);

		return facebookGraphReportDto;
	}

	// DB : 地域別レポート（グラフ用）
	@Override
	@Transactional
	public FbGraphReportDto getFacebookRegionReportingGraph(List<String> campaignIdList, String startDate, String endDate) {

		List<String> reportTypeList = new ArrayList<>(Arrays.asList("x"));
		List<String> impressionList = new ArrayList<>(Arrays.asList("impressions"));
		List<String> clicksList = new ArrayList<>(Arrays.asList("clicks"));
		List<String> spendList = new ArrayList<>(Arrays.asList("spend"));
		List<String> CTRList = new ArrayList<>(Arrays.asList("ctr"));
		List<String> CPCList = new ArrayList<>(Arrays.asList("cpc"));
		List<String> CPMList = new ArrayList<>(Arrays.asList("cpm"));

		List<FacebookRegionReport> facebookRegionReportList = facebookRegionReportCustomDao.selectRegionReportGraph(campaignIdList, DateUtil.toLocalDate(startDate), DateUtil.toLocalDate(endDate));

		for (FacebookRegionReport facebookRegionReport : facebookRegionReportList) {
			reportTypeList.add(facebookRegionReport.getRegion());
			impressionList.add(facebookRegionReport.getImpressions().toString());
			clicksList.add(facebookRegionReport.getClicks().toString());
			Long displayCosts = ReportUtil.calDisplaySpend((double) facebookRegionReport.getSpend()).longValue();
			spendList.add(displayCosts.toString());
			CTRList.add(ReportUtil.calCtr(facebookRegionReport.getClicks(), facebookRegionReport.getImpressions()).toString());
			CPCList.add(ReportUtil.calCpc(facebookRegionReport.getClicks(), displayCosts).toString());
			CPMList.add(ReportUtil.calCpm(facebookRegionReport.getImpressions(), displayCosts).toString());
		}

		FbGraphReportDto facebookGraphReportDto = new FbGraphReportDto();
		facebookGraphReportDto.setReportTypeList(reportTypeList);
		facebookGraphReportDto.setImpressionList(impressionList);
		facebookGraphReportDto.setClicksList(clicksList);
		facebookGraphReportDto.setSpendList(spendList);
		facebookGraphReportDto.setCTRList(CTRList);
		facebookGraphReportDto.setCPCList(CPCList);
		facebookGraphReportDto.setCPMList(CPMList);

		return facebookGraphReportDto;
	}

	@Override
	@Transactional
	public String download(List<String> campaignIdList, String startDate, String endDate, Integer reportType) {

		List<FbReportDisplayDto> fbReportDisplayDtoList = null;

		List<FacebookDeviceReportCsvBean> facebookDeviceReportCsvBeanList = new ArrayList<FacebookDeviceReportCsvBean>();
		List<FacebookDateReportCsvBean> facebookDateReportCsvBeanList = new ArrayList<FacebookDateReportCsvBean>();
		List<FacebookRegionReportCsvBean> facebookRegionReportCsvBeanList = new ArrayList<FacebookRegionReportCsvBean>();
		switch (ReportType.of(reportType)) {
		case DEVICE:// デバイス別の場合
			fbReportDisplayDtoList = getDeviceReport(campaignIdList, startDate, endDate);
			for (FbReportDisplayDto fbReportDisplayDto : fbReportDisplayDtoList) {
				FacebookDeviceReportCsvBean facebookDeviceReportCsvBean = new FacebookDeviceReportCsvBean();
				facebookDeviceReportCsvBean.setCampaignName(fbReportDisplayDto.getCampaignName());
				facebookDeviceReportCsvBean.setCampaignId(fbReportDisplayDto.getCampaignId());
				facebookDeviceReportCsvBean.setImpressions(fbReportDisplayDto.getImpressions().toString());
				facebookDeviceReportCsvBean.setClicks(fbReportDisplayDto.getClicks().toString());
				facebookDeviceReportCsvBean.setCosts(fbReportDisplayDto.getSpend().toString());
				facebookDeviceReportCsvBean.setCtr(fbReportDisplayDto.getCtr().toString());
				facebookDeviceReportCsvBean.setCpc(fbReportDisplayDto.getCpc().toString());
				facebookDeviceReportCsvBean.setCpm(fbReportDisplayDto.getCpm().toString());
				facebookDeviceReportCsvBeanList.add(facebookDeviceReportCsvBean);
			}
		case REGIONS:// 地域別の場合
			fbReportDisplayDtoList = getRegionReport(campaignIdList, startDate, endDate);
			for (FbReportDisplayDto fbReportDisplayDto : fbReportDisplayDtoList) {
				FacebookRegionReportCsvBean facebookRegionReportCsvBean = new FacebookRegionReportCsvBean();
				facebookRegionReportCsvBean.setCampaignName(fbReportDisplayDto.getCampaignName());
				facebookRegionReportCsvBean.setCampaignId(fbReportDisplayDto.getCampaignId());
				facebookRegionReportCsvBean.setImpressions(fbReportDisplayDto.getImpressions().toString());
				facebookRegionReportCsvBean.setClicks(fbReportDisplayDto.getClicks().toString());
				facebookRegionReportCsvBean.setCosts(fbReportDisplayDto.getSpend().toString());
				facebookRegionReportCsvBean.setCtr(fbReportDisplayDto.getCtr().toString());
				facebookRegionReportCsvBean.setCpc(fbReportDisplayDto.getCpc().toString());
				facebookRegionReportCsvBean.setCpm(fbReportDisplayDto.getCpm().toString());
				facebookRegionReportCsvBeanList.add(facebookRegionReportCsvBean);
			}
		case DATE:// 日別の場合
			fbReportDisplayDtoList = getDateReport(campaignIdList, startDate, endDate);
			for (FbReportDisplayDto fbReportDisplayDto : fbReportDisplayDtoList) {
				FacebookDateReportCsvBean facebookDateReportCsvBean = new FacebookDateReportCsvBean();
				facebookDateReportCsvBean.setCampaignName(fbReportDisplayDto.getCampaignName());
				facebookDateReportCsvBean.setCampaignId(fbReportDisplayDto.getCampaignId());
				facebookDateReportCsvBean.setImpressions(fbReportDisplayDto.getImpressions().toString());
				facebookDateReportCsvBean.setClicks(fbReportDisplayDto.getClicks().toString());
				facebookDateReportCsvBean.setCosts(fbReportDisplayDto.getSpend().toString());
				facebookDateReportCsvBean.setCtr(fbReportDisplayDto.getCtr().toString());
				facebookDateReportCsvBean.setCpc(fbReportDisplayDto.getCpc().toString());
				facebookDateReportCsvBean.setCpm(fbReportDisplayDto.getCpm().toString());
				facebookDateReportCsvBeanList.add(facebookDateReportCsvBean);
			}
		}

		StringWriter out = new StringWriter();
		CsvWriterSettings settings = new CsvWriterSettings();
		CsvWriter writer = null;
		BeanWriterProcessor<FacebookDeviceReportCsvBean> deviceWriterProcessor = null;
		BeanWriterProcessor<FacebookDateReportCsvBean> dateWriterProcessor = null;
		BeanWriterProcessor<FacebookRegionReportCsvBean> regionWriterProcessor = null;

		switch (ReportType.of(reportType)) {
		case DEVICE:// デバイス別の場合
			settings.setHeaders(FacebookDeviceReportCsvBean.columnName);
			deviceWriterProcessor = new BeanWriterProcessor<>(FacebookDeviceReportCsvBean.class);
			settings.setRowWriterProcessor(deviceWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(facebookDeviceReportCsvBeanList);
			break;
		case REGIONS:// 地域別の場合
			settings.setHeaders(FacebookRegionReportCsvBean.columnName);
			regionWriterProcessor = new BeanWriterProcessor<>(FacebookRegionReportCsvBean.class);
			settings.setRowWriterProcessor(regionWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(facebookRegionReportCsvBeanList);
			break;
		case DATE:// 日別の場合
			settings.setHeaders(FacebookDateReportCsvBean.columnName);
			dateWriterProcessor = new BeanWriterProcessor<>(FacebookDateReportCsvBean.class);
			settings.setRowWriterProcessor(dateWriterProcessor);

			writer = new CsvWriter(out, settings);
			writer.writeHeaders();
			writer.processRecordsAndClose(facebookDateReportCsvBeanList);
			break;
		}

		// close
		writer.close();

		return out.toString();
	}

	
	@Override
	@Transactional
	public Issue createIssue(FbIssueDto fbIssueDto) {

		// 案件表にインサート
		Issue issue = new Issue();
		//issue.setBudget(fbIssueDto.getBudget());

		issue.setShopId(ContextUtil.getCurrentShopId());
		issue.setFacebookCampaignManageId(Long.valueOf(fbIssueDto.getCampaignId()));
		issue.setCampaignName(fbIssueDto.getCampaignName());
		issue.setStartDate(fbIssueDto.getStartDate());
		issue.setEndDate(fbIssueDto.getEndDate());
		issue.setFacebookOnedayBudget(fbIssueDto.getDailyBudget());
		issue.setFacebookRegions(assembleLocationString(fbIssueDto.getLocationList()));
		issueDao.insert(issue);
		
		return issue;
	}

	// 合計データ
	private List<FbReportDisplayDto> getSummary(List<FbReportDisplayDto> fbReportDisplayDtoList) {

		FbReportDisplayDto sumReportDto = new FbReportDisplayDto();
		sumReportDto.setCampaignId("合計");
		sumReportDto.setCampaignName("-");
		sumReportDto.setDevice("-");
		sumReportDto.setRegion("-");
		sumReportDto.setDate("-");
		sumReportDto.setImpressions(fbReportDisplayDtoList.stream().mapToLong(FbReportDisplayDto::getImpressions).sum());
		sumReportDto.setClicks(fbReportDisplayDtoList.stream().mapToLong(FbReportDisplayDto::getClicks).sum());
		sumReportDto.setSpend(fbReportDisplayDtoList.stream().mapToLong(FbReportDisplayDto::getSpend).sum());
		sumReportDto.setCtr(ReportUtil.calCtr(sumReportDto.getClicks(), sumReportDto.getImpressions()));
		sumReportDto.setCpc(ReportUtil.calCpc(sumReportDto.getClicks(), sumReportDto.getSpend()));
		sumReportDto.setCpm(ReportUtil.calCpm(sumReportDto.getImpressions(), sumReportDto.getSpend()));

		fbReportDisplayDtoList.add(sumReportDto);

		return fbReportDisplayDtoList;
	}
	
	// 地域を組み立てる
	private String assembleLocationString(List<Long> locationList) {

		StringBuilder stringBuilder = new StringBuilder();
		// 地域を組み立てる
		for (Long location : locationList) {
			stringBuilder.append(location.toString());
			stringBuilder.append(",");
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.substring(0, stringBuilder.length() - 1);
		}
		if (stringBuilder.length() > 0) {
			return stringBuilder.substring(0, stringBuilder.length() - 1);
		} else {
			return stringBuilder.toString();
		}
	}

}
