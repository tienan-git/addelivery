package jp.acepro.haishinsan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.service.OperationService;

@Controller
@RequestMapping("/campaign")
public class CampaignController {

	@Autowired
	OperationService operationService;

	@GetMapping("/mediaSelection")
	public ModelAndView mediaSelection(ModelAndView mv) {

		mv.setViewName("campaign/mediaSelection");
		return mv;
	}

}