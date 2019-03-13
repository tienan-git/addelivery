package jp.acepro.haishinsan.controller.google;

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
import jp.acepro.haishinsan.dto.google.GoogleCampaignInfoDto;
import jp.acepro.haishinsan.dto.google.GoogleReportDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.form.GoogleReportSearchForm;
import jp.acepro.haishinsan.mapper.GoogleMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.util.Utf8BomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/google")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GoogleReportController {

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	HttpSession session;

	@Autowired
	GoogleReportService googleReportService;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	ApplicationProperties applicationProperties;
	
	@Autowired
	OperationService operationService;

	@GetMapping("/reporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_REPORT_VIEW + "')")
	public ModelAndView reporting() {
		
		// ＦＯＲＭを初期化
		GoogleReportSearchForm googleReportSearchForm = new GoogleReportSearchForm();
		googleReportSearchForm.setCampaignPairList(getCampaignPairList());
		googleReportSearchForm.setPeriod(PeriodSet.WHOLE.getValue());
		
		// レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/reporting");
		modelAndView.addObject("googleReportSearchForm", googleReportSearchForm);
		
		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@GetMapping("/getReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_REPORT_VIEW + "')")
	public ModelAndView getReport() {

		// レポート取得（API経由）
		googleReportService.getReport();
		return null;
	}

	@PostMapping("/showDeviceReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_REPORT_VIEW + "')")
	public ModelAndView showDeviceReport(GoogleReportSearchForm googleReportSearchForm) {

		// ＦＯＲＭを読込
		GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();
		googleReportSearchDto = GoogleMapper.INSTANCE.map(googleReportSearchForm);

		// レポート表示（デバイス別）
		GoogleReportDto googleReportDto = googleReportService.showDeviceReport(googleReportSearchDto);
		googleReportSearchForm.setCampaignIdList(googleReportSearchDto.getCampaignIdList());
		
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/reporting");
		modelAndView.addObject("reportType", ReportType.DEVICE.getValue());
		googleReportSearchForm.setCampaignPairList(getCampaignPairList());
		modelAndView.addObject("googleReportSearchForm", googleReportSearchForm);
		modelAndView.addObject("googleReportDisplayDtoList", googleReportDto.getGoogleReportDisplayDtoList());
		modelAndView.addObject("googleReportGraphDtoList", googleReportDto.getGoogleReportGraphDtoList());
		
		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_DEVICE_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@PostMapping("/showLocationReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_REPORT_VIEW + "')")
	public ModelAndView showLocationReport(GoogleReportSearchForm googleReportSearchForm) {

		// ＦＯＲＭを読込
		GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();
		googleReportSearchDto = GoogleMapper.INSTANCE.map(googleReportSearchForm);

		// レポート表示（地域別）
		GoogleReportDto googleReportDto = googleReportService.showLocationReport(googleReportSearchDto);
		googleReportSearchForm.setCampaignIdList(googleReportSearchDto.getCampaignIdList());
		
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/reporting");
		modelAndView.addObject("reportType", ReportType.REGIONS.getValue());
		googleReportSearchForm.setCampaignPairList(getCampaignPairList());
		modelAndView.addObject("googleReportSearchForm", googleReportSearchForm);
		modelAndView.addObject("googleReportDisplayDtoList", googleReportDto.getGoogleReportDisplayDtoList());
		modelAndView.addObject("googleReportGraphDtoList", googleReportDto.getGoogleReportGraphDtoList());
		
		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_REGION_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@PostMapping("/showDailyReport")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_REPORT_VIEW + "')")
	public ModelAndView showDailyReport(GoogleReportSearchForm googleReportSearchForm) {

		// ＦＯＲＭを読込
		GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();
		googleReportSearchDto = GoogleMapper.INSTANCE.map(googleReportSearchForm);

		// レポート表示（日別）
		GoogleReportDto googleReportDto = googleReportService.showDailyReport(googleReportSearchDto);
		googleReportSearchForm.setCampaignIdList(googleReportSearchDto.getCampaignIdList());
		
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/reporting");
		modelAndView.addObject("reportType", ReportType.DATE.getValue());
		googleReportSearchForm.setCampaignPairList(getCampaignPairList());
		modelAndView.addObject("googleReportSearchForm", googleReportSearchForm);
		modelAndView.addObject("googleReportDisplayDtoList", googleReportDto.getGoogleReportDisplayDtoList());
		modelAndView.addObject("googleReportGraphDtoList", googleReportDto.getGoogleReportGraphDtoList());
		
		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_DATE_REPORT_VIEW.getValue(), String.valueOf(""));
		return modelAndView;
	}
	
	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_REPORT_VIEW + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute GoogleReportSearchForm googleReportSearchForm, @RequestParam Integer reportType) throws IOException {

		// ＦＯＲＭを読込
		GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();
		googleReportSearchDto = GoogleMapper.INSTANCE.map(googleReportSearchForm);
		googleReportSearchDto.setReportType(reportType);

		// ダウンロードファイルを作成
		String file = googleReportService.download(googleReportSearchDto);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "Google_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
		switch (ReportType.of(reportType)) {
		case DEVICE:
			fimeName = "Google_Device_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			break;
		case REGIONS:
			fimeName = "Google_Regions_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			break;
		case DATE:
			fimeName = "Google_Date_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			break;
		}
		httpHeaders.setContentDispositionFormData("filename", fimeName);
		
		// オペレーションログ記録
		switch (ReportType.of(reportType)) {
		case DEVICE:
			operationService.create(Operation.GOOGLE_DEVICE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case REGIONS:
			operationService.create(Operation.GOOGLE_REGION_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case DATE:
			operationService.create(Operation.GOOGLE_DATE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		}
		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}

	private List<Pair<Long, String>> getCampaignPairList() {

		// キャンペーン一覧情報取得
		List<GoogleCampaignInfoDto> googleCampaignInfoDtoList = new ArrayList<GoogleCampaignInfoDto>();
		googleCampaignInfoDtoList = googleCampaignService.getCampaignList();

		// キャンペーンペアリスト作成
		List<Pair<Long, String>> campaignList = new ArrayList<Pair<Long, String>>();
		if (googleCampaignInfoDtoList.size() > 0) {
			for (GoogleCampaignInfoDto googleCampaignInfoDto : googleCampaignInfoDtoList) {
				campaignList.add(Pair.of(googleCampaignInfoDto.getCampaignId(), googleCampaignInfoDto.getCampaignName()));
			}
		}
		return campaignList;
	}
}
