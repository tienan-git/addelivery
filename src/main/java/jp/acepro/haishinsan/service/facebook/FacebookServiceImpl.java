package jp.acepro.haishinsan.service.facebook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.facebook.ads.sdk.Campaign;
import com.facebook.ads.sdk.Campaign.EnumObjective;
import com.facebook.ads.sdk.Campaign.EnumStatus;
import com.facebook.ads.sdk.IDName;
import com.facebook.ads.sdk.Targeting;
import com.facebook.ads.sdk.TargetingGeoLocation;
import com.facebook.ads.sdk.TargetingGeoLocationCity;

import jp.acepro.haishinsan.ApplicationProperties;
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
import jp.acepro.haishinsan.db.entity.FacebookTemplate;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.SegmentManage;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbCreativeDto;
import jp.acepro.haishinsan.dto.facebook.FbIssueDto;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.dto.facebook.InstagramAccountRes;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.enums.FacebookArrangePlace;
import jp.acepro.haishinsan.enums.FacebookCampaignStatus;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.MediaCollection;
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
		if (facebookTemplateCustomDao
				.selectByTemplateName(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplateName())
				.size() > 0) {
			// 該当テンプレート名が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E00018);
		}

		// テンプレート優先度チェック
		if (facebookTemplateCustomDao
				.selectByTemplatePriority(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplatePriority())
				.size() > 0) {
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
			List<Long> geolocationLong = Stream.of(strArray).map(element -> Long.parseLong(element))
					.collect(Collectors.toList());
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
			List<Long> geolocationLong = Stream.of(strArray).map(element -> Long.parseLong(element))
					.collect(Collectors.toList());
			fbTemplateDto.setLocationList(geolocationLong);
		}

		return fbTemplateDto;

	}

	@Override
	@Transactional
	public void templateUpdate(FbTemplateDto fbTemplateDto) {

		// 例外処理
		// テンプレート名チェック
		List<FacebookTemplate> facebookTemplateListOld1 = facebookTemplateCustomDao
				.selectByTemplateName(ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplateName());
		if (facebookTemplateListOld1.stream().filter(obj -> !obj.getTemplateId().equals(fbTemplateDto.getTemplateId()))
				.count() > 0) {
			// 該当テンプレート名が既に登録されたため、修正してください。
			throw new BusinessException(ErrorCodeConstant.E00018);
		}

		// テンプレート優先度チェック
		List<FacebookTemplate> facebookTemplateListOld2 = facebookTemplateCustomDao.selectByTemplatePriority(
				ContextUtil.getCurrentShop().getShopId(), fbTemplateDto.getTemplatePriority());
		if (facebookTemplateListOld2.stream().filter(obj -> !obj.getTemplateId().equals(fbTemplateDto.getTemplateId()))
				.count() > 0) {
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
		List<FacebookTemplate> facebookTemplateList = facebookTemplateCustomDao
				.selectAll(ContextUtil.getCurrentShop().getShopId());
		List<FbTemplateDto> fbTemplateDtoList = FacebookMapper.INSTANCE.mapListEntityToDto(facebookTemplateList);

		for (FbTemplateDto fbTemplateDto : fbTemplateDtoList) {
			// 地域を設定
			if (fbTemplateDto.getGeolocation() == null || fbTemplateDto.getGeolocation().isEmpty()) {
				fbTemplateDto.setLocationList(new ArrayList<Long>());
			} else {
				String[] strArray = fbTemplateDto.getGeolocation().split(",");
				List<Long> geolocationLong = Stream.of(strArray).map(element -> Long.parseLong(element))
						.collect(Collectors.toList());
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

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());

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
			// 地域設定 （仮に東京銀座に設定）
			// location_typesが指定されていない場合、デフォルトはこの地域に住んでる人です。
			List<TargetingGeoLocationCity> TargetingGeoLocationCityList = new ArrayList<TargetingGeoLocationCity>();
			TargetingGeoLocationCityList.add(new TargetingGeoLocationCity().setFieldKey("1200778"));

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
			targeting.setFieldPublisherPlatforms(Arrays.asList("facebook"))
					.setFieldFacebookPositions(Arrays.asList("feed"));
			// 広告セットを作成（スタート日時は仮に次の日に設定、終了日時は設定せず）
			String startDateTime = DateFormatter.yyyyMMdd_HYPHEN.format(today.plusDays(1L)) + "T00:00:00+0900";
			AdImage adImage = account.createAdImage().addUploadFile("filename", fbCreativeDto.getImageFile()).execute();
			for (DspSegmentListDto dspSegmentListDto : dspSegmentDtoList) {
				cnt++;
				String campaignName = fbCreativeDto.getCreativeName() + "Campaign" + cnt;
				Campaign campaign = account.createCampaign().setName(campaignName).setObjective(enumObjective)
						.setStatus(enumCpStatus).execute();
				String campaignId = campaign.fetch().getId();

				// 広告セット名
				String adSetName = fbCreativeDto.getCreativeName() + "AdSet" + cnt;
				AdSet adset = account.createAdSet().setName(adSetName).setCampaignId(campaignId)
						// 配信ステータス
						.setStatus(enumSetStatus)
						// 入札戦略（最小コスト）
						.setDailyBudget(realDailyBudget).setStartTime(startDateTime).setBillingEvent(enumBillingEvent)
						.setBidStrategy(EnumBidStrategy.VALUE_LOWEST_COST_WITH_BID_CAP).setBidAmount(bidAmount)
						// 広告配信の最適化対象
						.setOptimizationGoal(EnumOptimizationGoal.VALUE_IMPRESSIONS).setTargeting(targeting).execute();
				String adSetId = adset.getFieldId();

				String linkUrl = dspSegmentListDto.getUrl();
				AdCreativeLinkData link = (new AdCreativeLinkData()).setFieldLink(linkUrl)
						.setFieldImageHash(adImage.getFieldHash());
				AdCreativeObjectStorySpec spec = (new AdCreativeObjectStorySpec())
						.setFieldPageId(ContextUtil.getCurrentShop().getFacebookPageId()).setFieldLinkData(link);
				AdCreative creative = account.createAdCreative()
						.setName(fbCreativeDto.getCreativeName() + "Creative" + cnt).setObjectStorySpec(spec).execute();
				account.createAd().setName(fbCreativeDto.getCreativeName() + "Ad" + cnt)
						.setAdsetId(Long.parseLong(adSetId)).setCreative(creative).setStatus(enumAdStatus).execute();

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

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());

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
			Campaign campaign = account.createCampaign().setName(campaignName).setObjective(enumObjective)
					.setStatus(enumCpStatus).execute();
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
				Double averageClickUnitPriceDouble = CodeMasterServiceImpl.facebookAreaUnitPriceClickList.stream()
						.filter(obj -> fbCampaignDto.getLocationList().contains(obj.getFirst()))
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
				bidAmount = Math.round(averageClickUnitPriceDouble);
			}
			if (fbCampaignDto.getUnitPriceType().equals(UnitPriceType.DISPLAY.getValue())) {
				Double averageDisplayUnitPriceDouble = CodeMasterServiceImpl.facebookAreaUnitPriceDisplayList.stream()
						.filter(obj -> fbCampaignDto.getLocationList().contains(obj.getFirst()))
						.mapToInt(obj -> obj.getSecond()).average().getAsDouble();
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
				targeting.setFieldPublisherPlatforms(Arrays.asList("facebook", "instagram"))
						.setFieldFacebookPositions(Arrays.asList("feed"))
						.setFieldInstagramPositions(Arrays.asList("stream"));
			}
			if (fbCampaignDto.getArrangePlace().equals(FacebookArrangePlace.FACEBOOK.getValue())) {
				targeting.setFieldPublisherPlatforms(Arrays.asList("facebook"))
						.setFieldFacebookPositions(Arrays.asList("feed"));
			}
			if (fbCampaignDto.getArrangePlace().equals(FacebookArrangePlace.INSTAGRAM.getValue())) {
				targeting.setFieldPublisherPlatforms(Arrays.asList("instagram"))
						.setFieldInstagramPositions(Arrays.asList("stream"));
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
					.setDailyBudget(realDailyBudget).setStartTime(startDateTime).setEndTime(nextEndDateTime)
					.setBillingEvent(enumBillingEvent).setBidStrategy(EnumBidStrategy.VALUE_LOWEST_COST_WITH_BID_CAP)
					.setBidAmount(bidAmount)
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
			AdCreativeLinkData link = (new AdCreativeLinkData()).setFieldLink(segmentManage.getUrl())
					.setFieldImageHash(adImage.getFieldHash());
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
			insAccountBuilder = insAccountBuilder
					.path("/v3.2/" + ContextUtil.getCurrentShop().getFacebookPageId() + "/instagram_accounts");
			insAccountBuilder = insAccountBuilder.queryParam("access_token", pageToken);
			String insAccountResource = insAccountBuilder.build().toUri().toString();
			InstagramAccountRes insAccount = null;
			try {
				insAccount = call(insAccountResource, HttpMethod.GET, null, null, InstagramAccountRes.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			AdCreativeObjectStorySpec spec = (new AdCreativeObjectStorySpec())
					.setFieldInstagramActorId(insAccount.getData().get(0).getId())
					.setFieldPageId(ContextUtil.getCurrentShop().getFacebookPageId()).setFieldLinkData(link);
			AdCreative creative = account.createAdCreative().setName(fbCampaignDto.getCampaignName() + "Creative")
					.setObjectStorySpec(spec).execute();
			account.createAd().setName(fbCampaignDto.getCampaignName() + "Ad").setAdsetId(Long.parseLong(adSetId))
					.setCreative(creative).setStatus(enumAdStatus).execute();

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
				issue.setFacebookCampaignId(facebookCampaignManage.getCampaignId());
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

//	@Override
//	@Transactional
//	public FbCampaignDto campaignDetail(String campaignId) {
//
//		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());
//
//		// DBからキャンペーンを取得する
//		FacebookCampaignManage facebookCampaignManage = facebookCampaignManageCustomDao.selectByCampaignId(campaignId);
//		// SegmentIdでDBからセグメント情報取得
//		SegmentManage segmentManage = dspSegmentCustomDao.selectBySegmentId(facebookCampaignManage.getSegmentId());
//		if (segmentManage == null) {
//			// セグメントのURLが存在しない。
//			throw new BusinessException(ErrorCodeConstant.E00012);
//		}
//		FbCampaignDto fbCampaignDto = new FbCampaignDto();
//		fbCampaignDto.setLinkUrl(segmentManage.getUrl());
//		// 審査フラグを設定
//		fbCampaignDto.setCheckStatus(facebookCampaignManage.getApprovalFlag());
//		try {
//			Campaign campaign = Campaign.fetchById(campaignId, context);
//
//			fbCampaignDto.setCampaignId(campaign.getFieldId());
//			fbCampaignDto.setCampaignName(campaign.getFieldName());
//			if (EnumStatus.VALUE_ACTIVE.equals(campaign.getFieldStatus())) {
//				fbCampaignDto.setCampaignDisplayStatus(FacebookCampaignStatus.ACTIVE.getLabel());
//			} else {
//				fbCampaignDto.setCampaignDisplayStatus(FacebookCampaignStatus.PAUSED.getLabel());
//			}
//			fbCampaignDto.setStartDate(DateUtil.toDateTime(campaign.getFieldStartTime()));
//			fbCampaignDto.setEndDate(DateUtil.toDateTime(campaign.getFieldStopTime()));
//			fbCampaignDto.setUpdatedDate(DateUtil.toDateTime(campaign.getFieldUpdatedTime()));
//			fbCampaignDto.setCreatedDate(DateUtil.toDateTime(campaign.getFieldCreatedTime()));
//
//			APINodeList<AdSet> adSetList = campaign.getAdSets().requestAllFields().execute();
//
//			if (adSetList != null && adSetList.size() > 0) {
//				AdSet adSet = adSetList.get(0);
//				fbCampaignDto.setDailyBudget(Long.valueOf(adSet.getFieldDailyBudget()));
//				APINodeList<Ad> adList = adSet.getAds().requestAllFields().execute();
//				if (adList != null && adList.size() > 0) {
//					Ad ad = adList.get(0);
//					ad.getFieldSourceAd();
//				}
//			}
//			// System.out.println(fbCampaignDto);
//
//		} catch (APIException e) {
//			e.printStackTrace();
//			throw new SystemException("システムエラー発生しました");
//		}
//		return fbCampaignDto;
//
//	}

	@Override
	@Transactional
	public FbCampaignDto campaignDetail(String campaignId) {

		// DBからキャンペーンを取得する
		FacebookCampaignManage facebookCampaignManage = facebookCampaignManageCustomDao.selectByCampaignId(campaignId);

		FbCampaignDto fbCampaignDto = new FbCampaignDto();
		fbCampaignDto.setLinkUrl(facebookCampaignManage.getLinkUrl());
		fbCampaignDto.setCampaignId(facebookCampaignManage.getCampaignId());
		fbCampaignDto.setImageUrl(facebookCampaignManage.getImageUrl());
		fbCampaignDto.setCampaignName(facebookCampaignManage.getCampaignName());
		return fbCampaignDto;

	}

	@Override
	@Transactional
	public void deleteCampaign(String campaignId) {

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());
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

		APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(),
				applicationProperties.getFacebookAppSecret());

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
	@Transactional
	public Issue createIssue(FbIssueDto fbIssueDto) {

		// 案件表にインサート
		Issue issue = new Issue();
		// issue.setBudget(fbIssueDto.getBudget());

		issue.setShopId(ContextUtil.getCurrentShopId());
		issue.setFacebookCampaignId(fbIssueDto.getCampaignId());
		issue.setCampaignName(fbIssueDto.getCampaignName());
		issue.setBudget(CalculateUtil.calTotalBudget(fbIssueDto.getDailyBudget(), fbIssueDto.getStartDate(),
				fbIssueDto.getEndDate()));
		issue.setStartDate(fbIssueDto.getStartDate());
		issue.setEndDate(fbIssueDto.getEndDate());
		issue.setFacebookOnedayBudget(fbIssueDto.getDailyBudget());
		issue.setFacebookRegions(assembleLocationString(fbIssueDto.getLocationList()));
		issueDao.insert(issue);

		return issue;
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
