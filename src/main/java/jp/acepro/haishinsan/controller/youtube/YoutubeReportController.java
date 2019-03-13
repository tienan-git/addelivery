package jp.acepro.haishinsan.controller.youtube;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dto.youtube.YoutubeCampaignInfoDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportSearchDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.form.YoutubeReportSearchForm;
import jp.acepro.haishinsan.mapper.YoutubeMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.youtube.YoutubeReportService;
import jp.acepro.haishinsan.util.Utf8BomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/youtube")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class YoutubeReportController {

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	HttpSession session;

	@Autowired
	YoutubeReportService youtubeReportService;
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@Autowired
	OperationService operationService;

	@GetMapping("/reporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YOUTUBE_REPORT_VIEW + "')")
	public ModelAndView reporting() {
		
		// ＦＯＲＭを初期化
		YoutubeReportSearchForm youtubeReportSearchForm = new YoutubeReportSearchForm();
		youtubeReportSearchForm.setCampaignPairList(getCampaignPairList());
		youtubeReportSearchForm.setPeriod(PeriodSet.WHOLE.getValue());
		
		// レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("youtube/reporting");
		modelAndView.addObject("youtubeReportSearchForm", youtubeReportSearchForm);
		
		// オペレーションログ記録
		operationService.create(Operation.YOUTUBE_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@GetMapping("/getReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YOUTUBE_REPORT_VIEW + "')")
	public ModelAndView getReport() {

		// レポート取得（API経由）
		youtubeReportService.getReport();
		return null;
	}

	@PostMapping("/showDeviceReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YOUTUBE_REPORT_VIEW + "')")
	public ModelAndView showDeviceReport(YoutubeReportSearchForm youtubeReportSearchForm) {

		// ＦＯＲＭを読込
		YoutubeReportSearchDto youtubeReportSearchDto = new YoutubeReportSearchDto();
		youtubeReportSearchDto = YoutubeMapper.INSTANCE.map(youtubeReportSearchForm);

		// レポート表示（デバイス別）
		YoutubeReportDto youtubeReportDto = youtubeReportService.showDeviceReport(youtubeReportSearchDto);
		youtubeReportSearchForm.setCampaignIdList(youtubeReportSearchDto.getCampaignIdList());
		
		
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("youtube/reporting");
		modelAndView.addObject("reportType", ReportType.DEVICE.getValue());
		youtubeReportSearchForm.setCampaignPairList(getCampaignPairList());
		modelAndView.addObject("youtubeReportSearchForm", youtubeReportSearchForm);
		modelAndView.addObject("youtubeReportDisplayDtoList", youtubeReportDto.getYoutubeReportDisplayDtoList());
		modelAndView.addObject("youtubeReportGraphDtoList", youtubeReportDto.getYoutubeReportGraphDtoList());
		
		// オペレーションログ記録
		operationService.create(Operation.YOUTUBE_DEVICE_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@PostMapping("/showLocationReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YOUTUBE_REPORT_VIEW + "')")
	public ModelAndView showLocationReport(YoutubeReportSearchForm youtubeReportSearchForm) {

		// ＦＯＲＭを読込
		YoutubeReportSearchDto youtubeReportSearchDto = new YoutubeReportSearchDto();
		youtubeReportSearchDto = YoutubeMapper.INSTANCE.map(youtubeReportSearchForm);

		// レポート表示（地域別）
		YoutubeReportDto youtubeReportDto = youtubeReportService.showLocationReport(youtubeReportSearchDto);
		youtubeReportSearchForm.setCampaignIdList(youtubeReportSearchDto.getCampaignIdList());
		
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("youtube/reporting");
		modelAndView.addObject("reportType", ReportType.REGIONS.getValue());
		youtubeReportSearchForm.setCampaignPairList(getCampaignPairList());
		modelAndView.addObject("youtubeReportSearchForm", youtubeReportSearchForm);
		modelAndView.addObject("youtubeReportDisplayDtoList", youtubeReportDto.getYoutubeReportDisplayDtoList());
		modelAndView.addObject("youtubeReportGraphDtoList", youtubeReportDto.getYoutubeReportGraphDtoList());
		
		// オペレーションログ記録
		operationService.create(Operation.YOUTUBE_REGION_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@PostMapping("/showDailyReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YOUTUBE_REPORT_VIEW + "')")
	public ModelAndView showDailyReport(YoutubeReportSearchForm youtubeReportSearchForm) {

		// ＦＯＲＭを読込
		YoutubeReportSearchDto youtubeReportSearchDto = new YoutubeReportSearchDto();
		youtubeReportSearchDto = YoutubeMapper.INSTANCE.map(youtubeReportSearchForm);

		// レポート表示（日別）
		YoutubeReportDto youtubeReportDto = youtubeReportService.showDailyReport(youtubeReportSearchDto);
		youtubeReportSearchForm.setCampaignIdList(youtubeReportSearchDto.getCampaignIdList());
		
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("youtube/reporting");
		modelAndView.addObject("reportType", ReportType.DATE.getValue());
		youtubeReportSearchForm.setCampaignPairList(getCampaignPairList());
		modelAndView.addObject("youtubeReportSearchForm", youtubeReportSearchForm);
		modelAndView.addObject("youtubeReportDisplayDtoList", youtubeReportDto.getYoutubeReportDisplayDtoList());
		modelAndView.addObject("youtubeReportGraphDtoList", youtubeReportDto.getYoutubeReportGraphDtoList());
		
		// オペレーションログ記録
		operationService.create(Operation.YOUTUBE_DATE_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}
	
	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.YOUTUBE_REPORT_VIEW + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute YoutubeReportSearchForm youtubeReportSearchForm, @RequestParam Integer reportType) throws IOException {

		// ＦＯＲＭを読込
		YoutubeReportSearchDto youtubeReportSearchDto = new YoutubeReportSearchDto();
		youtubeReportSearchDto = YoutubeMapper.INSTANCE.map(youtubeReportSearchForm);
		youtubeReportSearchDto.setReportType(reportType);

		// ダウンロードファイルを作成
		String file = youtubeReportService.download(youtubeReportSearchDto);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "Youtube_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
		switch (ReportType.of(reportType)) {
		case DEVICE:
			fimeName = "Youtube_Device_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			break;
		case REGIONS:
			fimeName = "Youtube_Regions_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			break;
		case DATE:
			fimeName = "Youtube_Date_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			break;
		}
		httpHeaders.setContentDispositionFormData("filename", fimeName);
		
		switch (ReportType.of(reportType)) {
		case DEVICE:
			operationService.create(Operation.YOUTUBE_DEVICE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case REGIONS:
			operationService.create(Operation.YOUTUBE_REGION_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case DATE:
			operationService.create(Operation.YOUTUBE_DATE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		}
		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}

	private List<Pair<Long, String>> getCampaignPairList() {

		// キャンペーン一覧情報取得
		List<YoutubeCampaignInfoDto> youtubeCampaignInfoDtoList = new ArrayList<YoutubeCampaignInfoDto>();
		youtubeCampaignInfoDtoList = youtubeReportService.getCampaignList();

		// キャンペーンペアリスト作成
		List<Pair<Long, String>> campaignList = new ArrayList<Pair<Long, String>>();
		if (youtubeCampaignInfoDtoList.size() > 0) {
			for (YoutubeCampaignInfoDto youtubeCampaignInfoDto : youtubeCampaignInfoDtoList) {
				campaignList.add(Pair.of(youtubeCampaignInfoDto.getCampaignId(), youtubeCampaignInfoDto.getCampaignName()));
			}
		}
		return campaignList;
	}
}
