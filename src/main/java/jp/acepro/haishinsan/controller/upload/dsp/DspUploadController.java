package jp.acepro.haishinsan.controller.upload.dsp;

import java.io.IOException;
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

import jp.acepro.haishinsan.dto.dsp.DspCreativeDto;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.DspCreativeInputForm;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspCreativeService;
import jp.acepro.haishinsan.util.ImageUtil;

@Controller
@RequestMapping("/dsp")
public class DspUploadController {

	@Autowired
	HttpSession session;

	@Autowired
	DspCreativeService dspCreativeService;

	@Autowired
	ImageUtil imageUtil;

	@Autowired
	OperationService operationService;

	@GetMapping("/createCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CREATIVE_MANAGE + "')")
	public ModelAndView createCreative(@ModelAttribute DspCreativeInputForm dspCreativeInputForm) {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("campaign/dsp/createDspAd");
		return modelAndView;
	}

	@PostMapping("/confirmCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CREATIVE_MANAGE + "')")
	public ModelAndView confirmCreative(@Validated DspCreativeInputForm dspCreativeInputForm, BindingResult result) throws IOException {

		String imaBase64 = null;
		byte[] bytes = null;
		try {
			imaBase64 = imageUtil.getImageBytes(dspCreativeInputForm.getImage(), MediaType.DSP.getValue());
			bytes = dspCreativeInputForm.getImage().getBytes();
		} catch (BusinessException e) {
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView mv = new ModelAndView("dsp/createCreative");
			mv.addObject("dspCreativeInputForm", dspCreativeInputForm);
			return mv;
		}

		// FormからDtoまで変更
		DspCreativeDto dspCreativeDto = new DspCreativeDto();
		dspCreativeDto.setCreativeName(dspCreativeInputForm.getCreativeName());
		dspCreativeDto.setBytes(bytes);
		dspCreativeDto.setBase64Str(imaBase64);

		session.setAttribute("creativeName", dspCreativeDto.getCreativeName());
		session.setAttribute("imaBase64", dspCreativeDto.getBase64Str());
		session.setAttribute("bytes", bytes);

		StringBuffer data = new StringBuffer();
		data.append("data:image/jpeg;base64,");
		data.append(imaBase64);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("dspCreativeDto", dspCreativeDto);
		modelAndView.addObject("base64Data", data.toString());
		modelAndView.setViewName("campaign/dsp/confirmDspAd");
		return modelAndView;
	}

	@GetMapping("/completeCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CREATIVE_MANAGE + "')")
	public ModelAndView completeCreative() throws IOException {

		String imaBase64 = (String) session.getAttribute("imaBase64");
		byte[] bytes = (byte[]) session.getAttribute("bytes");
		String creativeName = (String) session.getAttribute("creativeName");
		// FormからDtoまで変更
		DspCreativeDto dspCreativeDto = new DspCreativeDto();
		dspCreativeDto.setCreativeName(creativeName);
		dspCreativeDto.setBytes(bytes);
		dspCreativeDto.setBase64Str(imaBase64);

		// creative作成
		DspCreativeDto newDspCreativeDto = dspCreativeService.createCreative(dspCreativeDto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("creative/uploadCreateSuccess");

		// オペレーションログ記録
		operationService.create(Operation.DSP_CREATIVE_CREATE.getValue(), String.valueOf(newDspCreativeDto.getCreativeId()));

		return modelAndView;
	}

	@GetMapping("/creativeList")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CREATIVE_VIEW + "')")
	public ModelAndView creativeList() {

		List<DspCreativeDto> dspCreativeDtoList = dspCreativeService.creativeList();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/creativeList");
		modelAndView.addObject("dspCreativeDtoList", dspCreativeDtoList);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CREATIVE_LIST.getValue(), null);

		return modelAndView;
	}

	@GetMapping("/creativeDetail")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CREATIVE_MANAGE + "')")
	public ModelAndView creativeDetail(@RequestParam Integer creativeId) {

		DspCreativeDto dspCreativeDto = dspCreativeService.creativeDetail(creativeId);
		session.setAttribute("dspCreativeDto", dspCreativeDto);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/creativeDetail");
		modelAndView.addObject("dspCreativeDto", dspCreativeDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CREATIVE_VIEW.getValue(), String.valueOf(creativeId));

		return modelAndView;

	}

	@PostMapping("/deleteCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.DSP_CREATIVE_MANAGE + "')")
	public ModelAndView deleteCreative(@RequestParam Integer creativeId) {

		DspCreativeDto dspCreativeDto = (DspCreativeDto) session.getAttribute("dspCreativeDto");
		session.removeAttribute("dspCeativeDto");

		dspCreativeService.creativeDelete(creativeId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dsp/deleteCreative");
		modelAndView.addObject("dspCreativeDto", dspCreativeDto);

		// オペレーションログ記録
		operationService.create(Operation.DSP_CREATIVE_DELETE.getValue(), String.valueOf(creativeId));

		return modelAndView;

	}
}
