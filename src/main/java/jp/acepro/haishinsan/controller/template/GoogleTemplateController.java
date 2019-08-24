package jp.acepro.haishinsan.controller.template;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.GoogleTemplateForm;
import jp.acepro.haishinsan.mapper.GoogleMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.service.google.GoogleTemplateService;
import jp.acepro.haishinsan.util.ContextUtil;

@Controller
@RequestMapping("/template/google")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GoogleTemplateController {

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	HttpSession session;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	GoogleTemplateService googleTemplateService;

	@Autowired
	OperationService operationService;

	@Autowired
	DspSegmentService dspSegmentService;

	@GetMapping("/createTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_MANAGE + "')")
	public ModelAndView createTemplate() {

		// コードマスタを読込
		getGoogleAreaList();

		// 入力ＦＯＲＭを初期化
		GoogleTemplateForm googleTemplateForm = new GoogleTemplateForm();

		// ＤＳＰＵＲＬを読込
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/createTemplate");
		modelAndView.addObject("googleTemplateForm", googleTemplateForm);
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		return modelAndView;
	}

	@PostMapping("/createTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_MANAGE + "')")
	public ModelAndView createTemplateComplete(@Validated GoogleTemplateForm googleTemplateForm, BindingResult result) {

		// 入力ＦＯＲＭを読込
		GoogleTemplateDto googleTemplateDto = GoogleMapper.INSTANCE.map(googleTemplateForm);

		// テンプレートを作成
		try {
			googleTemplateService.createTemplate(googleTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());

			// ＤＳＰＵＲＬを読込
			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("google/createTemplate");
			modelAndView.addObject("googleTemplateForm", googleTemplateForm);
			modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
			return modelAndView;
		}

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/createTemplateComplete");
		modelAndView.addObject("googleTemplateDto", googleTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_TEMPLATE_CREATE.getValue(),
				String.valueOf(googleTemplateDto.getTemplateId()));
		return modelAndView;
	}

	@GetMapping("/listTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_VIEW + "')")
	public ModelAndView listTemplate() {

		// コードマスタを読込
		getGoogleAreaList();

		// テンプレートを検索
		List<GoogleTemplateDto> googleTemplateDtoList = googleTemplateService
				.getTemplateList(ContextUtil.getCurrentShop().getShopId());

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/listTemplate");
		modelAndView.addObject("googleTemplateDtoList", googleTemplateDtoList);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_TEMPLATE_LIST.getValue(), String.valueOf(""));
		return modelAndView;
	}

	@GetMapping("/detailTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_VIEW + "')")
	public ModelAndView detailTemplate(@RequestParam Long templateId) {

		// コードマスタを読込
		getGoogleAreaList();

		// テンプレートを検索
		GoogleTemplateDto googleTemplateDto = googleTemplateService.getTemplate(templateId);

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/detailTemplate");
		modelAndView.addObject("googleTemplateDto", googleTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));
		return modelAndView;
	}

	@PostMapping("/updateTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplate(@RequestParam Long templateId) {

		// テンプレートを検索
		GoogleTemplateDto googleTemplateDto = googleTemplateService.getTemplate(templateId);
		GoogleTemplateForm googleTemplateForm = GoogleMapper.INSTANCE.mapDtoToForm(googleTemplateDto);

		// ＤＳＰＵＲＬを読込
		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/updateTemplate");
		modelAndView.addObject("googleTemplateForm", googleTemplateForm);
		modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
		return modelAndView;
	}

	@PostMapping("/updateTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplateComplete(@Validated GoogleTemplateForm googleTemplateForm, BindingResult result) {

		// 入力ＦＯＲＭを読込
		GoogleTemplateDto googleTemplateDto = GoogleMapper.INSTANCE.map(googleTemplateForm);

		// テンプレートを更新
		try {
			googleTemplateService.updateTemplate(googleTemplateDto);
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage());

			// ＤＳＰＵＲＬを読込
			List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("google/updateTemplate");
			modelAndView.addObject("googleTemplateForm", googleTemplateForm);
			modelAndView.addObject("dspSegmentDtoList", dspSegmentDtoList);
			return modelAndView;
		}

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/updateTemplateComplete");
		modelAndView.addObject("googleTemplateDto", googleTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_TEMPLATE_UPDATE.getValue(),
				String.valueOf(googleTemplateForm.getTemplateId()));
		return modelAndView;
	}

	@PostMapping("/deleteTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.GOOGLE_TEMPLATE_MANAGE + "')")
	public ModelAndView deleteTemplateComplete(@RequestParam Long templateId) {

		// テンプレートを削除
		googleTemplateService.deleteTemplate(templateId);

		// テンプレートを取得
		GoogleTemplateDto googleTemplateDto = googleTemplateService.getTemplate(templateId);

		// 正常時レスポンスを作成
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("google/deleteTemplateComplete");
		modelAndView.addObject("googleTemplateDto", googleTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.GOOGLE_TEMPLATE_UPDATE.getValue(), String.valueOf(templateId));
		return modelAndView;
	}

	private void getGoogleAreaList() {
		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}
	}
}
