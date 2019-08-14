package jp.acepro.haishinsan.controller.issue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dao.DspCampaignManageDao;
import jp.acepro.haishinsan.dao.IssueDao;
import jp.acepro.haishinsan.db.entity.DspCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDetailDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingListDto;
import jp.acepro.haishinsan.dto.twitter.TwitterDisplayReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterGraphReportDto;
import jp.acepro.haishinsan.dto.twitter.TwitterReportDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.issue.IssuesService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import jp.acepro.haishinsan.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/issues")
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
	DspCampaignManageDao dspCampaignManageDao;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	IssueDao issueDao;

	@PostMapping("/allReporting")
	public ModelAndView allReporting(@RequestParam Long issueId, @RequestParam String media) {
		issueId = 1l;
		switch (media) {
		case "FreakOut":
			return getDspReporting(issueId);
		case "Twitter":
			return getTwitterReporting(issueId);
		}
		return null;
	}

	@GetMapping("/dspReporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ModelAndView getDspReporting(@RequestParam Long issueId) {

		DspCampaignManage dspCampaginManage = dspCampaignManageDao.selectById(issueId);
		DspCampaignDetailDto dspCampaignDetailDto = dspCampaignService.getCampaignDetail(dspCampaginManage.getCampaignId(), ContextUtil.getCurrentShop().getDspUserId());

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
	public ModelAndView getTwitterReporting(@RequestParam Long issueId) {

		TwitterReportDto twitterReportDto = new TwitterReportDto();
		String campaignId = issuesService.selectCampaignIdByIssueId(issueId);
		twitterReportDto.setCampaignId(campaignId);
		/**
		 * 地域別の場合
		 **/
		List<TwitterDisplayReportDto> twitterRegionsTableDtoList = new ArrayList<>();
		TwitterGraphReportDto twitterRegionsGraphReportDto = new TwitterGraphReportDto();
		// Table用データ取得
		twitterRegionsTableDtoList = twitterReportingService.getTwitterRegionReporting(twitterReportDto);
		// Graph用データ取得
		twitterRegionsGraphReportDto = twitterReportingService.getTwitterRegionReportingGraph(twitterReportDto);
		log.debug("----------------------------------------------------------------------");
		log.debug("地域別レポート_tableデータ：" + twitterRegionsTableDtoList.toString());
		log.debug("地域別レポート_graphデータ：" + twitterRegionsGraphReportDto.toString());
		log.debug("----------------------------------------------------------------------");
		/**
		 * 日別の場合
		 **/
		List<TwitterDisplayReportDto> twitterDateTableDtoList = new ArrayList<>();
		TwitterGraphReportDto twitterDateGraphReportDto = new TwitterGraphReportDto();
		// Table用
		twitterDateTableDtoList = twitterReportingService.getTwitterDayReporting(twitterReportDto);
		// Graph用
		twitterDateGraphReportDto = twitterReportingService.getTwitterDayReportingGraph(twitterReportDto);
		log.debug("----------------------------------------------------------------------");
		log.debug("日別レポート_tableデータ：" + twitterDateTableDtoList.toString());
		log.debug("日別レポート_graphデータ：" + twitterDateGraphReportDto.toString());
		log.debug("----------------------------------------------------------------------");
		/**
		 * デバイスの場合
		 **/
		List<TwitterDisplayReportDto> twitterDeviceTableDtoList = new ArrayList<>();
		TwitterGraphReportDto twitterDeviceGraphReportDto = new TwitterGraphReportDto();
		twitterDeviceTableDtoList = twitterReportingService.getTwitterDeviceReporting(twitterReportDto);
		// Graph用
		twitterDeviceGraphReportDto = twitterReportingService.getTwitterDeviceReportingGraph(twitterReportDto);
		log.debug("----------------------------------------------------------------------");
		log.debug("デバイス別レポート_tableデータ：" + twitterDeviceTableDtoList.toString());
		log.debug("デバイス別レポート_graphデータ：" + twitterDeviceGraphReportDto.toString());
		log.debug("----------------------------------------------------------------------");
		// viewに設定
		ModelAndView mv = new ModelAndView();
		mv.addObject("twitterRegionsTableDtoList", twitterRegionsTableDtoList);
		mv.addObject("twitterRegionsGraphReportDto", twitterRegionsGraphReportDto);
		mv.addObject("twitterDateTableDtoList", twitterDateTableDtoList);
		mv.addObject("twitterDateGraphReportDto", twitterDateGraphReportDto);
		mv.addObject("twitterDeviceTableDtoList", twitterDeviceTableDtoList);
		mv.addObject("twitterDeviceGraphReportDto", twitterDeviceGraphReportDto);
		mv.setViewName("reporting/twitterReporting");
		return mv;
	}

	@GetMapping("/facebookReporting")
	public ModelAndView getFacebookReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/facebookReporting");
		return mv;
	}

	@GetMapping("/googleReporting")
	public ModelAndView getGoogleReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/googleReporting");
		return mv;
	}

}
