package jp.acepro.haishinsan.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.service.api.IssueApiService;

@RestController
@RequestMapping("/api/issue")
public class IssueApiController {

	@Autowired
	IssueApiService issueApiService;

	@GetMapping("/triggerIssues")
	public void triggerIssues() {
		issueApiService.executeAsync();
	}

}