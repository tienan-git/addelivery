package jp.acepro.haishinsan.controller.template;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import jp.acepro.haishinsan.db.entity.FacebookCampaignManage;
import jp.acepro.haishinsan.dto.facebook.FbTemplateDto;
import jp.acepro.haishinsan.enums.DateFormatter;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.enums.PeriodSet;
import jp.acepro.haishinsan.enums.ReportType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.FbReportInputForm;
import jp.acepro.haishinsan.form.FbTemplateInputForm;
import jp.acepro.haishinsan.mapper.FacebookMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.util.ImageUtil;
import jp.acepro.haishinsan.util.Utf8BomUtil;

@Controller
@RequestMapping("/template/facebook")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FacebookTemplateController {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	OperationService operationService;

	@Autowired
	ImageUtil imageUtil;

	@GetMapping("/templateList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_VIEW + "')")
	public ModelAndView templateList() {

		// コードマスタをメモリへロード
		getFacebookAreaList();

		// テンプレートリストを取得
		List<FbTemplateDto> fbTemplateDtoList = facebookService.searchList();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("template/facebook/templateList");
		mv.addObject("fbTemplateDtoList", fbTemplateDtoList);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_LIST.getValue(), String.valueOf(""));
		return mv;
	}

	@GetMapping("/createTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView createTemplate(@ModelAttribute FbTemplateInputForm fbTempleteInputForm) {

		// コードマスタをメモリへロード
		getFacebookAreaList();

		// -------- 地域初期値設定 --------
		List<Long> locationList = new ArrayList<Long>();
		fbTempleteInputForm.setLocationList(locationList);

		// -------- 地域初期値設定 --------
		ModelAndView mv = new ModelAndView();
		mv.setViewName("template/facebook/createTemplate");
		return mv;
	}

	@PostMapping("/completeTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView completeTemplate(@Validated FbTemplateInputForm fbTemplateInputForm, BindingResult result) {

		FbTemplateDto fbTemplateDto = FacebookMapper.INSTANCE.map(fbTemplateInputForm);
		try {
			fbTemplateDto = facebookService.create(fbTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());

			ModelAndView mv = new ModelAndView();
			mv.setViewName("template/facebook/createTemplate");
			mv.addObject("fbTempleteInputForm", fbTemplateInputForm);
			return mv;
		}

		ModelAndView mv = new ModelAndView("template/facebook/completeTemplate");
		mv.addObject("fbTempleteInputForm", fbTemplateInputForm);
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_CREATE.getValue(),
				String.valueOf(fbTemplateDto.getTemplateId()));
		return mv;
	}

	@GetMapping("/templateDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_VIEW + "')")
	public ModelAndView templateDetail(@RequestParam Long templateId) {

		FbTemplateDto fbTemplateDto = facebookService.templateDetail(templateId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("template/facebook/templateDetail");
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));
		return mv;

	}

	@PostMapping("/updateTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplate(@RequestParam Long templateId) {

		FbTemplateDto fbTemplateDto = facebookService.templateDetail(templateId);
		FbTemplateInputForm fbTemplateInputForm = FacebookMapper.INSTANCE.mapDtoToForm(fbTemplateDto);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("template/facebook/updateTemplate");
		mv.addObject("fbTemplateInputForm", fbTemplateInputForm);
		return mv;

	}

	@PostMapping("/updateTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplateComplete(@Validated FbTemplateInputForm fbTemplateInputForm,
			BindingResult result) {

		FbTemplateDto fbTemplateDto = FacebookMapper.INSTANCE.mapFormToDto(fbTemplateInputForm);
		try {
			facebookService.templateUpdate(fbTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());

			ModelAndView mv = new ModelAndView();
			mv.setViewName("template/facebook/updateTemplate");
			mv.addObject("fbTempleteInputForm", fbTemplateInputForm);
			return mv;
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("template/facebook/updateTemplateComplete");
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_UPDATE.getValue(),
				String.valueOf(fbTemplateInputForm.getTemplateId()));
		return mv;

	}

	@PostMapping("/deleteTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_TEMPLATE_MANAGE + "')")
	public ModelAndView deleteTemplate(@RequestParam Long templateId) {

		FbTemplateDto fbTemplateDto = facebookService.templateDelete(templateId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("template/facebook/deleteTemplate");
		mv.addObject("fbTemplateDto", fbTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_TEMPLATE_DELETE.getValue(), String.valueOf(templateId));
		return mv;

	}

	@PostMapping("/download")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_REPORT_VIEW + "')")
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

	private void getFacebookAreaList() {
		if (CodeMasterServiceImpl.facebookAreaNameList == null) {
			codeMasterService.getFacebookAreaList();
		}
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
