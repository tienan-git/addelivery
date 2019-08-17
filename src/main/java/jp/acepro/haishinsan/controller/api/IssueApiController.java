package jp.acepro.haishinsan.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.service.IssueApiService;
import jp.acepro.haishinsan.service.ReportApiService;

@RestController
@RequestMapping("/api")
public class IssueApiController {

	@Autowired
	IssueApiService issueApiService;

	@GetMapping("/updateFacebookIssue")
	public void updateFacebookIssue() {
		issueApiService.startFacebookIssueAsync();
	}

}