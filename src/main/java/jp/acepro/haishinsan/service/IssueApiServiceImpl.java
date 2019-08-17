package jp.acepro.haishinsan.service;

import java.time.LocalDate;
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
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdSet;
import com.facebook.ads.sdk.AdsInsights.EnumDatePreset;
import com.facebook.ads.sdk.Campaign;
import com.facebook.ads.sdk.IDName;
import com.facebook.ads.sdk.Targeting;
import com.facebook.ads.sdk.TargetingGeoLocation;
import com.facebook.ads.sdk.TargetingGeoLocationCity;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dao.IssueCustomDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.DspCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.enums.ApprovalFlag;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
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
    
    @Async
    @Override
    @Transactional
    public void startFacebookIssueAsync() {
    	
    	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	LocalDateTime dateTime = LocalDateTime.now();
    	String dateString = df.format(dateTime);
    	List<Issue> issueList = issueCustomDao.selectFacebookIssueNeededStart(dateString);
    	
    	APIContext context = new APIContext(applicationProperties.getFacebookAccessToken(), applicationProperties.getFacebookAppSecret());
    	//AdAccount account = new AdAccount(applicationProperties.getFacebookAccountId(), context);

		if (CodeMasterServiceImpl.facebookAreaNameList == null) {
			codeMasterService.getFacebookAreaList();
		}

    	for (Issue issue : issueList) {
            try {

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
    			Double averageUnitPriceDouble = CodeMasterServiceImpl.facebookAreaUnitPriceClickList.stream().filter(obj -> locationLongList.contains(obj.getFirst())).mapToInt(obj -> obj.getSecond()).average().getAsDouble();
    			bidAmount = Math.round(averageUnitPriceDouble);

    			// 1日の予算
    			Long dailyBudget = issue.getFacebookOnedayBudget();
    			// マージン率をかけた1日の予算
    			//Long realDailyBudget = CalculateUtil.calRealBudget(dailyBudget);
    			Long realDailyBudget = dailyBudget;

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
    			        .setFieldPublisherPlatforms(Arrays.asList("facebook")).setFieldFacebookPositions(Arrays.asList("feed"));
            	APINodeList<AdSet> adSets = new Campaign(issue.getFacebookCampaignManageId(), context).getAdSets()
            			  //.requestNameField()
            			  //.requestConfiguredStatusField()
            			  //.requestEffectiveStatusField()
            			  .execute();
            	adSets.get(0).update()
            	        .setDailyBudget(realDailyBudget)
            	        .setBidAmount(bidAmount)
            	        .setTargeting(targeting)
            	        .execute();
            	
            	Campaign campaign = new Campaign(issue.getFacebookCampaignManageId(), context).update()
            			.setStatus(Campaign.EnumStatus.VALUE_ACTIVE)
            			.execute();

    			issue.setStartTimestamp(dateTime);
    			issueDao.update(issue);            	
            }
	        catch (APIException e) {
			e.printStackTrace();
			throw new SystemException("システムエラー発生しました");
	        }
    	}

    }

}
