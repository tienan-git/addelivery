package jp.acepro.haishinsan.controller.upload.google;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.UploadGoogleBannerAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleBannerTextAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleTextAdCreateForm;
import jp.acepro.haishinsan.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/upload/google")
public class GoogleUploadController {

	@Autowired
	HttpSession session;

	@Autowired
	ImageUtil imageUtil;

	// Select Ad Type
	@GetMapping("/adTypeSelection")
	public ModelAndView adTypeSelection(ModelAndView mv) {
		mv.setViewName("upload/google/adTypeSelection");
		return mv;
	}

	// Create Banner Ad
	@GetMapping("/bannerAd/create")
	public ModelAndView bannerAdCreate(ModelAndView mv) {
		UploadGoogleBannerAdCreateForm form = new UploadGoogleBannerAdCreateForm();
		mv.setViewName("upload/google/bannerAd/create");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerAd/confirm")
	public ModelAndView bannerAdConfirm(UploadGoogleBannerAdCreateForm form) throws IOException {
		log.debug(form.toString());
		session.setAttribute("bannerAdForm", form);
		try {
			if (!form.getImageFile01().isEmpty()) {
				form.setImageData01("data:image/jpeg;base64," + imageUtil.getImageBytes(form.getImageFile01(), MediaType.GOOGLEIMG.getValue()));
			}
			if (!form.getImageFile02().isEmpty()) {
				form.setImageData02("data:image/jpeg;base64," + imageUtil.getImageBytes(form.getImageFile02(), MediaType.GOOGLEIMG.getValue()));
			}
			if (!form.getImageFile03().isEmpty()) {
				form.setImageData03("data:image/jpeg;base64," + imageUtil.getImageBytes(form.getImageFile03(), MediaType.GOOGLEIMG.getValue()));
			}
			if (!form.getImageFile04().isEmpty()) {
				form.setImageData04("data:image/jpeg;base64," + imageUtil.getImageBytes(form.getImageFile04(), MediaType.GOOGLEIMG.getValue()));
			}
			log.debug(form.toString());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/bannerAd/confirm");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerAd/complete")
	public ModelAndView bannerAdComplete() {
		UploadGoogleBannerAdCreateForm form = (UploadGoogleBannerAdCreateForm) session.getAttribute("bannerAdForm");
		log.debug(form.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/textAd/complete");
		session.removeAttribute("bannerAdForm");
		return mv;
	}

	// Create Banner&Text Ad
	@GetMapping("/bannerTextAd/create")
	public ModelAndView bannerTextAdCreate(ModelAndView mv) {
		UploadGoogleBannerTextAdCreateForm form = new UploadGoogleBannerTextAdCreateForm();
		mv.setViewName("upload/google/bannerTextAd/create");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerTextAd/confirm")
	public ModelAndView bannerTextAdConfirm(UploadGoogleBannerTextAdCreateForm form) throws IOException {
		session.setAttribute("bannerTextAdForm", form);
		try {
			if (!form.getImageFile01().isEmpty()) {
				form.setImageData01("data:image/jpeg;base64," + imageUtil.getImageBytes(form.getImageFile01(), MediaType.GOOGLERES.getValue()));
			}
			if (!form.getImageFile02().isEmpty()) {
				form.setImageData02("data:image/jpeg;base64," + imageUtil.getImageBytes(form.getImageFile02(), MediaType.GOOGLERES.getValue()));
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/bannerTextAd/confirm");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerTextAd/complete")
	public ModelAndView bannerTextAdComplete() {
		UploadGoogleBannerTextAdCreateForm form = (UploadGoogleBannerTextAdCreateForm) session.getAttribute("bannerTextAdForm");
		log.debug(form.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/bannerTextAd/complete");
		session.removeAttribute("bannerTextAdForm");
		return mv;
	}

	// Create Text Ad
	@GetMapping("/textAd/create")
	public ModelAndView textAdCreate(ModelAndView mv) {
		UploadGoogleTextAdCreateForm form = new UploadGoogleTextAdCreateForm();
		mv.setViewName("upload/google/textAd/create");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/textAd/confirm")
	public ModelAndView textAdConfirm(UploadGoogleTextAdCreateForm form) {
		log.debug(form.toString());
		session.setAttribute("textAdForm", form);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/textAd/confirm");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/textAd/complete")
	public ModelAndView textAdComplete() {
		UploadGoogleTextAdCreateForm form = (UploadGoogleTextAdCreateForm) session.getAttribute("textAdForm");
		log.debug(form.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/textAd/complete");
		session.removeAttribute("textAdForm");
		return mv;
	}

}
