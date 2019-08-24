package jp.acepro.haishinsan.controller.template;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import jp.acepro.haishinsan.dto.dsp.DspTemplateDto;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspTemplateInputForm;
import jp.acepro.haishinsan.mapper.DspMapper;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.dsp.DspCampaignService;

@Controller
@RequestMapping("/template/dsp")
public class DspTemplateController {

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
		modelAndView.setViewName("template/dsp/createTemplate");
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
			ModelAndView mv = new ModelAndView("template/dsp/createTemplate");
			mv.addObject("dsptemplateInputForm", dsptemplateInputForm);
			return mv;
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("template/dsp/completeTemplate");
		modelAndView.addObject("newdspTemplateDto", newdspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_CREATE.getValue(),
				String.valueOf(newdspTemplateDto.getTemplateId()));

		return modelAndView;
	}

	@GetMapping("/templateList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_VIEW + "')")
	public ModelAndView templateList() {

		List<DspTemplateDto> dspTemplateDtoList = dspApiService.templateList();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("template/dsp/templateList");
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
		modelAndView.setViewName("template/dsp/templateDetail");
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
		modelAndView.setViewName("template/dsp/updateTemplate");
		modelAndView.addObject("dspTemplateInputForm", dspTemplateInputForm);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_VIEW.getValue(), String.valueOf(templateId));

		return modelAndView;

	}

	@PostMapping("/updateTemplateComplete")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView updateTemplateComplete(@Validated DspTemplateInputForm dspTemplateInputForm,
			BindingResult result) {

		DspTemplateDto dspTemplateDto = DspMapper.INSTANCE.tempFormToDto(dspTemplateInputForm);
		try {
			dspApiService.templateUpdate(dspTemplateDto);
		} catch (BusinessException e) {
			result.reject(e.getMessage());
			ModelAndView mv = new ModelAndView("template/dsp/updateTemplate");
			mv.addObject("dspTemplateInputForm", dspTemplateInputForm);
			return mv;
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("template/dsp/updateTemplateComplete");
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_UPDATE.getValue(),
				String.valueOf(dspTemplateInputForm.getTemplateId()));

		return modelAndView;

	}

	@PostMapping("/deleteTemplate")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_TEMPLATE_MANAGE + "')")
	public ModelAndView deleteTemplate(@RequestParam Long templateId) {

		DspTemplateDto dspTemplateDto = dspApiService.templateDelete(templateId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("template/dsp/deleteTemplate");
		modelAndView.addObject("dspTemplateDto", dspTemplateDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_TEMPLATE_DELETE.getValue(), String.valueOf(templateId));

		return modelAndView;

	}

}
