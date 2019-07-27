package jp.acepro.haishinsan.controller.upload.google;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/upload/google")
public class GoogleUploadController {

	@GetMapping("/adTypeSelection")
	public ModelAndView adTypeSelection(ModelAndView mv) {
		mv.setViewName("upload/google/adTypeSelection");
		return mv;
	}
}
