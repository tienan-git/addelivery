package jp.acepro.haishinsan.controller.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/upload")
public class UploadComController {

	@GetMapping("/mediaSelection")
	public ModelAndView uploadMediaSelection(ModelAndView mv) {
		mv.setViewName("upload/mediaSelection");
		return mv;
	}

}
