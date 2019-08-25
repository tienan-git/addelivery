package jp.acepro.haishinsan.controller.issue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dao.DspCampaignCustomDao;
import jp.acepro.haishinsan.dao.DspCampaignManageDao;
import jp.acepro.haishinsan.dao.FacebookCampaignManageDao;
import jp.acepro.haishinsan.dao.GoogleCampaignManageDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.dao.YahooCampaignManageDao;
import jp.acepro.haishinsan.db.entity.DspCampaignManage;
import jp.acepro.haishinsan.db.entity.Issue;
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingListDto;
import jp.acepro.haishinsan.dto.facebook.FbCampaignDto;
import jp.acepro.haishinsan.dto.facebook.FbGraphReportDto;
import jp.acepro.haishinsan.dto.facebook.FbReportDisplayDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDetailDto;
import jp.acepro.haishinsan.dto.google.GoogleReportDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;
import jp.acepro.haishinsan.dto.twitter.TwitterCampaignData;
import jp.acepro.haishinsan.dto.twitter.TwitterDisplayReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterGraphReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;
import jp.acepro.haishinsan.dto.yahoo.YahooGraphReportDto;
import jp.acepro.haishinsan.dto.yahoo.YahooIssueDto;
import jp.acepro.haishinsan.dto.yahoo.YahooLocationDto;
import jp.acepro.haishinsan.dto.yahoo.YahooReportDisplayDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.form.YahooCsvInputForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.service.issue.IssuesService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import jp.acepro.haishinsan.service.yahoo.YahooService;
import jp.acepro.haishinsan.util.ContextUtil;

@Controller
@RequestMapping("/issue/report")
public class ReportingController {

	@Autowired
	TwitterReportingService twitterReportingService;

	@Autowired
	IssuesService issuesService;

	@Autowired
	HttpSession session;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	OperationService operationService;

	@Autowired
	DspCampaignCustomDao dspCampaignCustomDao;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	FacebookCampaignManageDao facebookCampaignManageDao;

	@Autowired
	FacebookService facebookService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	GoogleCampaignManageDao googleCampaignManageDao;

	@Autowired
	GoogleReportService googleReportService;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	IssueDao issueDao;

	@Autowired
	YahooService yahooService;

	@Autowired
	YahooCampaignManageDao yahooCampaignManageDao;

	@PostMapping("/allReporting")
	public ModelAndView allReporting(@RequestParam Long issueId, @RequestParam String media) {
		switch (media) {
		case "FreakOut":
			return getDspReporting(issueId);
		case "Twitter":
			return getTwitterReporting(issueId);
		case "FaceBook":
			return getFacebookReporting(issueId);
		case "Google":
			return getGoogleReporting(issueId);
		case "Yahoo":
			return getYahooReporting(issueId);
		}

		return null;
	}

	@GetMapping("/dspReporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ModelAndView getDspReporting(@RequestParam Long issueId) {

		Issue issue = issueDao.selectById(issueId);

		DspCampaignManage dspCampaginManage = dspCampaignCustomDao.selectByCampaignId(issue.getDspCampaignId());
		DspCampaignDetailDto dspCampaignDetailDto = dspCampaignService
				.getCampaignDetail(dspCampaginManage.getCampaignId(), ContextUtil.getCurrentShop().getDspUserId());

		// 検索条件を集める
		DspAdReportDto dspAdReportDto = new DspAdReportDto();
		dspAdReportDto.setCampaignId(dspCampaginManage.getCampaignId());

		// 日付別のグラフレポートを取得
		dspAdReportDto.setReportType(ReportType.DATE.getValue());
		DspReportingGraphDto dspDateReportingGraphDto = dspApiService.getDspReportingGraph(dspAdReportDto);
		// 日付別のリストレポートを取得
		List<DspReportingListDto> dspDateReportingDtoList = dspApiService.getDspReportingList(dspAdReportDto);
		// デバイス別のリストレポートを取得
		dspAdReportDto.setReportType(ReportType.DEVICE.getValue());
		List<DspReportingListDto> dspDeviceReportingDtoList = dspApiService.getDspReportingList(dspAdReportDto);
		// クリエイティブ別のグラフレポートを取得
		dspAdReportDto.setReportType(ReportType.CREATIVE.getValue());
		DspReportingGraphDto dspCreativeReportingGraphDto = dspApiService.getDspReportingGraph(dspAdReportDto);
		// クリエイティブ別のリストレポートを取得
		List<DspReportingListDto> dspCreativeReportingDtoList = dspApiService.getDspReportingList(dspAdReportDto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("reporting/dspReporting");
		modelAndView.addObject("dspDateReportingGraphDto", dspDateReportingGraphDto);
		modelAndView.addObject("dspDateReportingDtoList", dspDateReportingDtoList);
		modelAndView.addObject("dspDeviceReportingDtoList", dspDeviceReportingDtoList);
		modelAndView.addObject("dspCreativeReportingGraphDto", dspCreativeReportingGraphDto);
		modelAndView.addObject("dspCreativeReportingDtoList", dspCreativeReportingDtoList);
		modelAndView.addObject("dspCampaignDetailDto", dspCampaignDetailDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_REPORT_VIEW.getValue(), null);

		return modelAndView;
	}

	@GetMapping("/twitterReporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.TWITTER_REPORT_VIEW + "')")
	public ModelAndView getTwitterReporting(@RequestParam Long issueId) {

		TwitterReportDto twitterReportDto = new TwitterReportDto();
		TwitterCampaignData twitterCampaignData = issuesService.selectCampaignIdByIssueId(issueId);
		List<String> campaignIdList = new ArrayList<>(Arrays.asList(twitterCampaignData.getId()));
		twitterReportDto.setCampaignIdList(campaignIdList);

		// 地域別のTable用データ取得
		List<TwitterDisplayReportDto> twitterRegionsTableDtoList = twitterReportingService
				.getTwitterRegionReporting(twitterReportDto);
		// 地域別のGraph用データ取得
		TwitterGraphReportDto twitterRegionsGraphReportDto = twitterReportingService
				.getTwitterRegionReportingGraph(twitterReportDto);
		// 日別のTable用データ取得
		List<TwitterDisplayReportDto> twitterDateTableDtoList = twitterReportingService
				.getTwitterDayReporting(twitterReportDto);
		// 日別のGraph用データ取得
		TwitterGraphReportDto twitterDateGraphReportDto = twitterReportingService
				.getTwitterDayReportingGraph(twitterReportDto);
		// デバイスのTable&グラフ用データ取得
		List<TwitterDisplayReportDto> twitterDeviceTableDtoList = twitterReportingService
				.getTwitterDeviceReporting(twitterReportDto);
		// viewに設定
		ModelAndView mv = new ModelAndView();
		mv.addObject("twitterRegionsTableDtoList", twitterRegionsTableDtoList);
		mv.addObject("twitterRegionsGraphReportDto", twitterRegionsGraphReportDto);
		mv.addObject("twitterDateTableDtoList", twitterDateTableDtoList);
		mv.addObject("twitterDateGraphReportDto", twitterDateGraphReportDto);
		mv.addObject("twitterDeviceTableDtoList", twitterDeviceTableDtoList);
		mv.addObject("twitterCampaignData", twitterCampaignData);
		mv.setViewName("reporting/twitterReporting");

		// オペレーションログ記録
		operationService.create(Operation.TWITTER_REPORT_VIEW.getValue(), null);

		return mv;
	}

	@GetMapping("/facebookReporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
	public ModelAndView getFacebookReporting(@RequestParam Long issueId) {

		Issue issue = issueDao.selectById(issueId);
		FbCampaignDto fbCampaignDto = facebookService.campaignDetail(issue.getFacebookCampaignId().toString());

		// 検索条件を集める
		List<String> campaignIdList = new ArrayList<String>();
		campaignIdList.add(fbCampaignDto.getCampaignId());
		String startDate = issue.getStartDate();
		String endDate = issue.getEndDate();

		// 日付別のグラフレポートを取得
		FbGraphReportDto fbDateGraphReportDto = facebookReportingService.getFacebookDateReportingGraph(campaignIdList,
				startDate, endDate);
		// 日付別のリストレポートを取得
		List<FbReportDisplayDto> fbDateReportDisplayDtoList = facebookReportingService.getDateReport(campaignIdList,
				startDate, endDate);
		// デバイス別のリグラフレポートを取得
		FbGraphReportDto fbDeviceGraphReportDto = facebookReportingService
				.getFacebookDeviceReportingGraph(campaignIdList, startDate, endDate);
		// デバイス別のリストレポートを取得
		List<FbReportDisplayDto> fbDeviceReportDisplayDtoList = facebookReportingService.getDeviceReport(campaignIdList,
				startDate, endDate);
		// 地域別のリグラフレポートを取得
		FbGraphReportDto fbRegionGraphReportDto = facebookReportingService
				.getFacebookRegionReportingGraph(campaignIdList, startDate, endDate);
		// 地域別のリストレポートを取得
		List<FbReportDisplayDto> fbRegionReportDisplayDtoList = facebookReportingService.getRegionReport(campaignIdList,
				startDate, endDate);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("reporting/facebookReporting");
		modelAndView.addObject("issue", issue);
		modelAndView.addObject("fbCampaignDto", fbCampaignDto);
		modelAndView.addObject("fbDateGraphReportDto", fbDateGraphReportDto);
		modelAndView.addObject("fbDateReportDisplayDtoList", fbDateReportDisplayDtoList);
		modelAndView.addObject("fbDeviceGraphReportDto", fbDeviceGraphReportDto);
		modelAndView.addObject("fbDeviceReportDisplayDtoList", fbDeviceReportDisplayDtoList);
		modelAndView.addObject("fbRegionGraphReportDto", fbRegionGraphReportDto);
		modelAndView.addObject("fbRegionReportDisplayDtoList", fbRegionReportDisplayDtoList);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_REPORT_VIEW.getValue(), null);

		return modelAndView;
	}

	@GetMapping("/googleReporting")
	public ModelAndView getGoogleReporting(@RequestParam Long issueId) {

		// 検索条件を集める
		GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();
		Issue issue = issueDao.selectById(issueId);
		List<Long> ids = new ArrayList<Long>();
		ids.add(issue.getGoogleCampaignId());
		googleReportSearchDto.setCampaignIdList(ids);

		// レポート表示（地域別）
		GoogleReportDto googleLocationReportDto = googleReportService.showLocationReport(googleReportSearchDto);
		// レポート表示（日付別）
		GoogleReportDto googleDailyReportDto = googleReportService.showDailyReport(googleReportSearchDto);
		// レポート表示（デバイス別）
		GoogleReportDto googleDeviceReportDto = googleReportService.showDeviceReport(googleReportSearchDto);
		// キャンプーン詳細取得
		GoogleCampaignDetailDto googleCampaignDetailDto = new GoogleCampaignDetailDto();
		googleCampaignDetailDto = googleCampaignService.getCampaign(issue.getGoogleCampaignId());

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("reporting/googleReporting");
		modelAndView.addObject("googleLocationReportDto", googleLocationReportDto);
		modelAndView.addObject("googleDailyReportDto", googleDailyReportDto);
		modelAndView.addObject("googleDeviceReportDto", googleDeviceReportDto);
		modelAndView.addObject("googleCampaignDetailDto", googleCampaignDetailDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_REGION_REPORT_VIEW.getValue(), null);
		return modelAndView;
	}

	@GetMapping("/yahooReporting")
	public ModelAndView getYahooReporting(@RequestParam Long issueId) {

		// 検索条件を集める
		List<String> campaignIdList = new ArrayList<String>();
		Issue issue = issueDao.selectById(issueId);
		YahooCampaignManage yahooCampaignManage = yahooCampaignManageDao.selectById(issue.getYahooCampaignManageId());
		campaignIdList.add(yahooCampaignManage.getCampaignId());

		// Yahoo広告詳細取得
		YahooIssueDto yahooIssueDto = yahooService.getIssueDetail(issueId);
		// 地域詳細処理
		List<String> locationIdListString = Arrays.asList(yahooIssueDto.getLocationIds().split(","));
		List<Long> locationIdList = locationIdListString.stream().map(s -> Long.parseLong(s))
				.collect(Collectors.toList());
		List<YahooLocationDto> locationList = yahooService.getLocationList(locationIdList);
		yahooIssueDto.setLocationList(locationList);

		// デバイス別レポート
		List<YahooReportDisplayDto> yahooDeviceReportDisplayDtoList = yahooService.getDeviceReport(campaignIdList, null,
				null);
		YahooGraphReportDto yahooDeviceGraphReportDto = yahooService.getYahooDeviceReportingGraph(campaignIdList, null,
				null);
		// 地域別レポート
		List<YahooReportDisplayDto> yahooRegionReportDisplayDtoList = yahooService.getRegionReport(campaignIdList, null,
				null);
		YahooGraphReportDto yahooRegionGraphReportDto = yahooService.getYahooRegionReportingGraph(campaignIdList, null,
				null);

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("reporting/yahooReporting");
		modelAndView.addObject("yahooCsvInputForm", new YahooCsvInputForm());
		modelAndView.addObject("yahooDeviceReportDisplayDtoList", yahooDeviceReportDisplayDtoList);
		modelAndView.addObject("yahooDeviceGraphReportDto", yahooDeviceGraphReportDto);
		modelAndView.addObject("yahooRegionReportDisplayDtoList", yahooRegionReportDisplayDtoList);
		modelAndView.addObject("yahooRegionGraphReportDto", yahooRegionGraphReportDto);
		modelAndView.addObject("yahooIssueDto", yahooIssueDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_REGION_REPORT_VIEW.getValue(), null);
		return modelAndView;
	}

}
