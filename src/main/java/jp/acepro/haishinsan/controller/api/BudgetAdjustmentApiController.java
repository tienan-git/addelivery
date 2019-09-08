package jp.acepro.haishinsan.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.acepro.haishinsan.service.api.BudgetAdjustmentApiService;
import jp.acepro.haishinsan.service.api.CreativeStatusApiService;
import jp.acepro.haishinsan.service.api.IssueApiService;

@RestController
@RequestMapping("/api/budgetAdjustment")
public class BudgetAdjustmentApiController {

	@Autowired
	BudgetAdjustmentApiService budgetAdjustmentApiService;

	@GetMapping("/adjustCampaignDailyBudget")
	public void adjustCampaignDailyBudget() {
		budgetAdjustmentApiService.executeAsync();
	}

}