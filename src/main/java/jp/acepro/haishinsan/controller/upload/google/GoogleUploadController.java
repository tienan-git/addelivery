package jp.acepro.haishinsan.controller.upload.google;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.form.UploadGoogleBannerAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleBannerTextAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleTextAdCreateForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/upload/google")
public class GoogleUploadController {

	@Autowired
	HttpSession session;

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
	public ModelAndView bannerAdConfirm(UploadGoogleBannerAdCreateForm form) {
		log.debug(form.toString());
		return null;
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
	public ModelAndView bannerTextAdConfirm(UploadGoogleBannerTextAdCreateForm form) {
		log.debug(form.toString());
		return null;
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
