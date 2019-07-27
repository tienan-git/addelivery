package jp.acepro.haishinsan.controller.upload.google;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/upload/google")
public class GoogleUploadController {

	// Select Ad Type
	@GetMapping("/adTypeSelection")
	public ModelAndView adTypeSelection(ModelAndView mv) {
		mv.setViewName("upload/google/adTypeSelection");
		return mv;
	}

	// Create Banner Ad
	@GetMapping("/bannerAd/create")
	public ModelAndView bannerAdCreate(ModelAndView mv) {
		mv.setViewName("upload/google/bannerAd/create");
		return mv;
	}

	// Create Banner&Text Ad
	@GetMapping("/bannerTextAd/create")
	public ModelAndView bannerTextAdCreate(ModelAndView mv) {
		mv.setViewName("upload/google/bannerTextAd/create");
		return mv;
	}

	// Create Text Ad
	@GetMapping("/textAd/create")
	public ModelAndView textAdCreate(ModelAndView mv) {
		mv.setViewName("upload/google/textAd/create");
		return mv;
	}

}
