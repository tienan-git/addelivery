package jp.acepro.haishinsan.controller.upload.facebook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.facebook.FbCreativeDto;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.FbCreativeInputForm;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.facebook.FacebookService;
import jp.acepro.haishinsan.util.ImageUtil;

@Controller
@RequestMapping("/upload/facebook")
public class FacebookUploadController {

	@Autowired
	HttpSession session;

	@Autowired
	ImageUtil imageUtil;

	@Autowired
	CodeMasterService codeMasterService;

	@Autowired
	FacebookService facebookService;

	@Autowired
	DspSegmentService dspSegmentService;

	@Autowired
	OperationService operationService;

	@Autowired
	MessageSource msg;

	@GetMapping("/createFacebookCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView createFacebookCreative(@ModelAttribute FbCreativeInputForm fbCreativeInputForm) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/facebook/createCreative");
		return mv;

	}

	@PostMapping("/confirmFacebookCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView confirmFacebookCreative(@Validated FbCreativeInputForm fbCreativeInputForm,
			BindingResult result) throws IOException {

		String imaBase64 = null;
		byte[] bytes = null;
		List<DspSegmentListDto> dspSegmentDtoList = null;
		try {
			LocalDateTime dateTime = LocalDateTime.now();
			// 日付によるセグメント情報を取得
			dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(dateTime);
			if (dspSegmentDtoList == null || dspSegmentDtoList.size() == 0) {
				// セグメントのURLが存在しない。
				throw new BusinessException(ErrorCodeConstant.E00012);
			}
			imaBase64 = imageUtil.getImageBytes(fbCreativeInputForm.getImage(), MediaType.FACEBOOK.getValue());
			bytes = fbCreativeInputForm.getImage().getBytes();
		} catch (BusinessException e) {
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView mv = new ModelAndView("upload/facebook/createCreative");
			mv.addObject("fbCreativeInputForm", fbCreativeInputForm);
			return mv;
		}

		// FormからDtoまで変更
		FbCreativeDto fbCreativeDto = new FbCreativeDto();
		fbCreativeDto.setCreativeName(fbCreativeInputForm.getCreativeName());
		fbCreativeDto.setBytes(bytes);
		fbCreativeDto.setBase64Str(imaBase64);
		fbCreativeDto.setImage(fbCreativeInputForm.getImage());

		session.setAttribute("creativeName", fbCreativeDto.getCreativeName());
		session.setAttribute("imaBase64", fbCreativeDto.getBase64Str());
		session.setAttribute("bytes", bytes);
		session.setAttribute("image", fbCreativeDto.getImage());
		session.setAttribute("dspSegmentDtoList", dspSegmentDtoList);

		StringBuffer data = new StringBuffer();
		data.append("data:image/jpeg;base64,");
		data.append(imaBase64);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("fbCreativeDto", fbCreativeDto);
		modelAndView.addObject("base64Data", data.toString());
		modelAndView.setViewName("upload/facebook/confirmCreative");
		return modelAndView;

	}

	@GetMapping("/completeFacebookCreative")
	@PreAuthorize("hasAuthority('" + jp.acepro.haishinsan.constant.AuthConstant.FACEBOOK_CAMPAIGN_MANAGE + "')")
	public ModelAndView completeFacebookCreative() throws IOException {

		String imaBase64 = (String) session.getAttribute("imaBase64");
		byte[] bytes = (byte[]) session.getAttribute("bytes");
		String creativeName = (String) session.getAttribute("creativeName");
		MultipartFile image = (MultipartFile) session.getAttribute("image");
		List<DspSegmentListDto> dspSegmentDtoList = (List<DspSegmentListDto>) session.getAttribute("dspSegmentDtoList");

		FbCreativeDto fbCreativeDto = new FbCreativeDto();
		fbCreativeDto.setCreativeName(creativeName);
		fbCreativeDto.setBytes(bytes);
		fbCreativeDto.setBase64Str(imaBase64);

		File imageFile = new File(image.getOriginalFilename());
		FileOutputStream fo = new FileOutputStream(imageFile);
		fo.write(image.getBytes());
		fo.close();
		fbCreativeDto.setImageFile(imageFile);

		facebookService.createCreative(fbCreativeDto, dspSegmentDtoList);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("creative/uploadCreateSuccess");

		session.removeAttribute("creativeName");
		session.removeAttribute("imaBase64");
		session.removeAttribute("bytes");
		session.removeAttribute("image");
		session.removeAttribute("dspSegmentDtoList");

		// オペレーションログ記録
		operationService.create(Operation.FACEBOOK_CAMPAIGN_CREATE.getValue(), null);

		return modelAndView;
	}

}
