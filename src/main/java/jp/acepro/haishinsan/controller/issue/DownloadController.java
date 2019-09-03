package jp.acepro.haishinsan.controller.issue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.db.entity.YahooCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.google.GoogleReportSearchDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeReportSearchDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.form.FbReportInputForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.service.yahoo.YahooService;
import jp.acepro.haishinsan.service.youtube.YoutubeReportService;
import jp.acepro.haishinsan.util.Utf8BomUtil;

@Controller
@RequestMapping("/issue/download")
public class DownloadController {

	@Autowired
	DspApiService dspApiService;

	@Autowired
	HttpSession session;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	FacebookService facebookService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	OperationService operationService;

	@Autowired
	GoogleReportService googleReportService;
	
	@Autowired
	YahooService yahooService;
	
	@Autowired
	YoutubeReportService youtubeReportService;

	@PostMapping("/dspDownload")
	public ResponseEntity<byte[]> dspDownload(@RequestParam Integer campaignId, @RequestParam Integer reportType) throws IOException {

		// 検索条件を集める
		DspAdReportDto dspAdReportDto = new DspAdReportDto();
		dspAdReportDto.setCampaignIdList(Arrays.asList(campaignId));
		dspAdReportDto.setStartDate(null);
		dspAdReportDto.setEndDate(null);
		dspAdReportDto.setReportType(reportType);

		// CSVファイル中身を取得し、文字列にする
		String file = dspApiService.download(dspAdReportDto);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "DSP_REPORT" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
		httpHeaders.setContentDispositionFormData("filename", fimeName);

		// オペレーションログ記録
		operationService.create(Operation.DSP_REPORT_DOWNLOAD.getValue(), null);

		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/googleDownload")
	public ResponseEntity<byte[]> googleDownload(@RequestParam Long campaignId, @RequestParam Integer reportType) throws IOException {

		// ＦＯＲＭを読込
		GoogleReportSearchDto googleReportSearchDto = new GoogleReportSearchDto();
		googleReportSearchDto.setCampaignIdList(Arrays.asList(campaignId));
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

	@PostMapping("/yahooDownload")
	public ResponseEntity<byte[]> yahooDownload(@RequestParam String campaignId, @RequestParam Integer reportType) throws IOException {

		List<String> campaignIdList = new ArrayList<String>();

		List<Pair<String, String>> campaignPairList = getCampaignPairList();

		campaignIdList.add(campaignId);
		String startDate = null;
		String endDate = null;

		// ダウンロードファイルを作成
		String file = yahooService.download(campaignIdList, startDate, endDate, reportType);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "Yahoo_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";

		switch (ReportType.of(reportType)) {
		case DEVICE:
			fimeName = "Yahoo_Device_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			// オペレーションログ記録
			operationService.create(Operation.YAHOO_DEVICE_REPORT_DOWNLOAD.getValue(), "ファイル名：" + fimeName);
			break;
		case REGIONS:
			fimeName = "Yahoo_Region_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			// オペレーションログ記録
			operationService.create(Operation.YAHOO_REGION_REPORT_DOWNLOAD.getValue(), "ファイル名：" + fimeName);
			break;
		case DATE:
			fimeName = "Yahoo_Date_Report_" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
			// オペレーションログ記録
			operationService.create(Operation.YAHOO_DATE_REPORT_DOWNLOAD.getValue(), "ファイル名：" + fimeName);
			break;

		default:
			break;
		}
		httpHeaders.setContentDispositionFormData("filename", fimeName);

		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}
	
	@PostMapping("/youtubeDownload")
	public ResponseEntity<byte[]> youtubeDownload(@RequestParam Long campaignId, @RequestParam Integer reportType) throws IOException {

		// ＦＯＲＭを読込
		YoutubeReportSearchDto youtubeReportSearchDto = new YoutubeReportSearchDto();
		youtubeReportSearchDto.setCampaignId(campaignId);
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

	
	private List<Pair<String, String>> getCampaignPairList() {

        List<YahooCampaignManage> yahooCampaignManageList = yahooService.searchYahooCampaignManageList();
        List<Pair<String, String>> campaignPairList = new ArrayList<Pair<String, String>>();

        for (YahooCampaignManage yahooCampaignManage : yahooCampaignManageList) {
            if (yahooCampaignManage.getCampaignId() != null) {
                campaignPairList.add(
                        Pair.of(yahooCampaignManage.getCampaignId().toString(), yahooCampaignManage.getCampaignName()));
            }
        }

        return campaignPairList;
    }


	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.ISSUE_DETAIL + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute FbReportInputForm fbReportInputForm, @RequestParam Integer reportType) throws IOException {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());
		String startDate = null;
		String endDate = null;
		if (PeriodSet.LIMITED.getValue().equals(fbReportInputForm.getPeriod())) {
			startDate = fbReportInputForm.getStartDate();
			endDate = fbReportInputForm.getEndDate();
		}
		// CSVファイル中身を取得し、文字列にする
		String file = facebookReportingService.download(fbReportInputForm.getCampaignIdList(), startDate, endDate, reportType);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", applicationProperties.getContentTypeCsvDownload());
		String fimeName = "FACEBOOK_REPORT" + DateFormatter.yyyyMMdd.format(LocalDate.now()) + ".csv";
		httpHeaders.setContentDispositionFormData("filename", fimeName);

		// オペレーションログ記録
		switch (ReportType.of(reportType)) {
		case DEVICE:
			operationService.create(Operation.FACEBOOK_DEVICE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case REGIONS:
			operationService.create(Operation.FACEBOOK_REGION_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		case DATE:
			operationService.create(Operation.FACEBOOK_DATE_REPORT_DOWNLOAD.getValue(), String.valueOf(""));
			break;
		}
		return new ResponseEntity<>(Utf8BomUtil.utf8ToWithBom(file), httpHeaders, HttpStatus.OK);
	}

	private List<Pair<String, String>> getFacebookCampaignManageList() {
		// 該当店舗所有するキャンペーンリストを取得
		List<FacebookCampaignManage> facebookCampaignManageList = facebookService.searchFacebookCampaignManageList();
		List<Pair<String, String>> campaignPairList = new ArrayList<Pair<String, String>>();
		for (FacebookCampaignManage facebookCampaignManage : facebookCampaignManageList) {
			campaignPairList.add(Pair.of(facebookCampaignManage.getCampaignId(), facebookCampaignManage.getCampaignName()));
		}
		return campaignPairList;
	}
}
