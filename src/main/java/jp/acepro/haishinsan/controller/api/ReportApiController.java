package jp.acepro.haishinsan.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.service.ReportApiService;

@RestController
@RequestMapping("/api/report")
public class ReportApiController {

	@Autowired
	ReportApiService reportApiService;

	@GetMapping("/getReport")
	public void getReport() {
		reportApiService.executeAsync();
	}

}