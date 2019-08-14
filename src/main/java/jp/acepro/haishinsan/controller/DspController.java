package jp.acepro.haishinsan.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dto.dsp.DspAdReportDto;
import jp.acepro.haishinsan.dto.dsp.DspCampaignDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingGraphDto;
import jp.acepro.haishinsan.dto.dsp.DspReportingListDto;
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspAdReportInputForm;
import jp.acepro.haishinsan.form.DspTemplateInputForm;
import jp.acepro.haishinsan.mapper.DspMapper;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;
import jp.acepro.haishinsan.util.Utf8BomUtil;
import jp.acepro.haishinsan.enums.ReportType;

@Controller
@RequestMapping("/dsp")
public class DspController {

	@Autowired
	DspApiService dspApiService;

	@Autowired
	HttpSession session;

	@Autowired
	DspCampaignService dspCampaignService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	OperationService operationService;

	@GetMapping("/createTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView createTemplate(@ModelAttribute DspTemplateInputForm dsptemplateInputForm) {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/createTemplate");
		return modelAndView;
	}

	@PostMapping("/completeTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView completeTemplate(@Validated DspTemplateInputForm dsptemplateInputForm, BindingResult result) {

		DspTemplateDto dspTemplateDto = DspMapper.INSTANCE.tempFormToDto(dsptemplateInputForm);
		DspTemplateDto newdspTemplateDto = null;
		try {
			newdspTemplateDto = dspApiService.createTemplate(dspTemplateDto);
		} catch (BusinessException e) {
			result.reject(e.getMessage());
			ModelAndView mv = new ModelAndView("dsp/createTemplate");
			mv.addObject("dsptemplateInputForm", dsptemplateInputForm);
			return mv;
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/completeTemplate");
		modelAndView.addObject("newdspTemplateDto", newdspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_CREATE.getValue(), String.valueOf(newdspTemplateDto.getTemplateId()));

		return modelAndView;
	}

	@GetMapping("/templateList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_VIEW + "')")
	public ModelAndView templateList() {

		List<DspTemplateDto> dspTemplateDtoList = dspApiService.templateList();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/templateList");
		modelAndView.addObject("dspTemplateDtoList", dspTemplateDtoList);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_LIST.getValue(), null);

		return modelAndView;
	}

	@GetMapping("/templateDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_VIEW + "')")
	public ModelAndView templateDetail(@RequestParam Long templateId) {

		DspTemplateDto dspTemplateDto = dspApiService.templateDetail(templateId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/templateDetail");
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));

		return modelAndView;

	}

	@PostMapping("/updateTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplate(@RequestParam Long templateId) {

		DspTemplateDto dspTemplateDto = dspApiService.templateDetail(templateId);
		DspTemplateInputForm dspTemplateInputForm = DspMapper.INSTANCE.tempDtoToForm(dspTemplateDto);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/updateTemplate");
		modelAndView.addObject("dspTemplateInputForm", dspTemplateInputForm);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));

		return modelAndView;

	}

	@PostMapping("/updateTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplateComplete(@Validated DspTemplateInputForm dspTemplateInputForm, BindingResult result) {

		DspTemplateDto dspTemplateDto = DspMapper.INSTANCE.tempFormToDto(dspTemplateInputForm);
		try {
			dspApiService.templateUpdate(dspTemplateDto);
		} catch (BusinessException e) {
			result.reject(e.getMessage());
			ModelAndView mv = new ModelAndView("dsp/updateTemplate");
			mv.addObject("dspTemplateInputForm", dspTemplateInputForm);
			return mv;
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/updateTemplateComplete");
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_UPDATE.getValue(), String.valueOf(dspTemplateInputForm.getTemplateId()));

		return modelAndView;

	}

	@PostMapping("/deleteTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView deleteTemplate(@RequestParam Long templateId) {

		DspTemplateDto dspTemplateDto = dspApiService.templateDelete(templateId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/deleteTemplate");
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_DELETE.getValue(), String.valueOf(templateId));

		return modelAndView;

	}

	@GetMapping("/reportingForBatch")
	public ModelAndView reportingForBatch() {

		dspApiService.getDspCampaignReporting();
		return null;
	}

	@GetMapping("/reporting")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ModelAndView reportingList(@ModelAttribute DspAdReportInputForm dspAdReportInputForm) {

		// システムDBに保存しているキャンペーンをすべて検索していく
		List<DspCampaignDto> dspCampaignDtoList = dspCampaignService.getCampaignList();

		// レポーティングを取得期間の初期値を付ける
		dspAdReportInputForm.setPeriod(PeriodSet.WHOLE.getValue());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/reporting");
		modelAndView.addObject("dspCampaignDtoList", dspCampaignDtoList);
		modelAndView.addObject("dspReportingGraphDto", new DspReportingGraphDto());
		modelAndView.addObject("dspReportingListDto", new DspReportingListDto());
		modelAndView.addObject("dspReportingDtoList", new ArrayList<DspReportingListDto>());

		return modelAndView;
	}

	@PostMapping("/reportingList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ModelAndView reporting(DspAdReportInputForm dspAdReportInputForm, @RequestParam Integer reportType) {

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
		if (PeriodSet.LIMITED.getValue().equals(dspAdReportInputForm.getPeriod())) {
			dspAdReportDto.setStartDate(dspAdReportInputForm.getStartDate());
			dspAdReportDto.setEndDate(dspAdReportInputForm.getEndDate());
		}
		dspAdReportDto.setReportType(reportType);

		// 検索したレポーティングをグラフとして、画面に表示する
		DspReportingGraphDto dspReportingGraphDto = dspApiService.getDspReportingGraph(dspAdReportDto);
		// 検索したレポーティングをリストとして、画面に表示する
		List<DspReportingListDto> dspReportingDtoList = dspApiService.getDspReportingList(dspAdReportDto);
		if (dspReportingDtoList != null) {
			// レポーティング情報の合計を取得、画面に表示する
			DspReportingListDto dspReportingListDto = dspApiService.getDspReportingSummary(dspAdReportDto);
			dspReportingDtoList.add(dspReportingListDto);
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/reporting");
		modelAndView.addObject("dspCampaignDtoList", dspCampaignDtoList);
		modelAndView.addObject("dspReportingGraphDto", dspReportingGraphDto);
		modelAndView.addObject("dspReportingDtoList", dspReportingDtoList);
		modelAndView.addObject("reportType", reportType);
		if (dspAdReportInputForm.getCampaignIdList() == null || dspAdReportInputForm.getCampaignIdList().size() == 0) {
			dspAdReportInputForm.setCampaignIdList(ids);
		}
		modelAndView.addObject("dspAdReportInputForm", dspAdReportInputForm);

		// オペレーションログ記録
		operationService.create(Operation.DSP_REPORT_VIEW.getValue(), null);

		return modelAndView;
	}

	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_REPORT_VIEW + "')")
	public ResponseEntity<byte[]> download(@ModelAttribute DspAdReportInputForm dspAdReportInputForm, @RequestParam Integer reportType) throws IOException {

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

}
