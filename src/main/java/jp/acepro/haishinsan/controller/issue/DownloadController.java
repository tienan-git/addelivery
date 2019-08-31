package jp.acepro.haishinsan.controller.issue;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.form.DspAdReportInputForm;
import jp.acepro.haishinsan.form.FbReportInputForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
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

	@PostMapping("/dsp")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.ISSUE_DETAIL + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute DspAdReportInputForm dspAdReportInputForm,
			@RequestParam Integer reportType) throws IOException {

		// システムDBに保存しているキャンペーンをすべて検索していく
		List<DspCampaignDto> dspCampaignDtoList = dspCampaignService.getCampaignList();
		List<Integer> ids = new ArrayList<Integer>();
		dspCampaignDtoList.forEach(dspCampaignDto -> ids.add(dspCampaignDto.getCampaignId()));

		// 検索条件を集める
		DspAdReportDto dspAdReportDto = new DspAdReportDto();
		dspAdReportDto.setCampaignIdList(dspAdReportInputForm.getCampaignIdList());
		if (dspAdReportInputForm.getCampaignIdList().isEmpty()) {
			dspAdReportDto.setCampaignIdList(ids);
		}
		dspAdReportDto.setStartDate(dspAdReportInputForm.getStartDate());
		dspAdReportDto.setEndDate(dspAdReportInputForm.getEndDate());
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

	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.ISSUE_DETAIL + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute FbReportInputForm fbReportInputForm,
			@RequestParam Integer reportType) throws IOException {

		// 該当店舗所有するキャンペーンリストを設定
		fbReportInputForm.setCampaignPairList(getFacebookCampaignManageList());
		String startDate = null;
		String endDate = null;
		if (PeriodSet.LIMITED.getValue().equals(fbReportInputForm.getPeriod())) {
			startDate = fbReportInputForm.getStartDate();
			endDate = fbReportInputForm.getEndDate();
		}
		// CSVファイル中身を取得し、文字列にする
		String file = facebookReportingService.download(fbReportInputForm.getCampaignIdList(), startDate, endDate,
				reportType);

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
			campaignPairList
					.add(Pair.of(facebookCampaignManage.getCampaignId(), facebookCampaignManage.getCampaignName()));
		}
		return campaignPairList;
	}
}
