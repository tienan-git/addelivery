package jp.acepro.haishinsan.controller.reporting;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jp.acepro.haishinsan.service.OperationService;

@Controller
@RequestMapping("/reporting")
public class ReportingController {

	@Autowired
	HttpSession session;

	@Autowired
	OperationService operationService;

	@GetMapping("/allReporting")
	public ModelAndView getReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/allReporting");
		return mv;
	}
	
	@GetMapping("/dspReporting")
	public ModelAndView getDspReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/dspReporting");
		return mv;
	}
	
	@GetMapping("/twitterReporting")
	public ModelAndView getTwitterReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/twitterReporting");
		return mv;
	}
	
	@GetMapping("/facebookReporting")
	public ModelAndView getFacebookReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/facebookReporting");
		return mv;
	}
	
	@GetMapping("/googleReporting")
	public ModelAndView getGoogleReporting() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reporting/googleReporting");
		return mv;
	}
}
