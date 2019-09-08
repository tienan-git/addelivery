package jp.acepro.haishinsan.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.enums.Operation;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspApiService;
import jp.acepro.haishinsan.service.google.GoogleReportService;
import jp.acepro.haishinsan.service.issue.FacebookReportingService;
import jp.acepro.haishinsan.service.issue.TwitterReportingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BudgetAdjustmentApiServiceImpl implements BudgetAdjustmentApiService {

	@Autowired
	OperationService operationService;

	@Autowired
	DspApiService dspApiService;

	@Autowired
	GoogleReportService googleReportService;

	@Autowired
	FacebookReportingService facebookReportingService;

	@Autowired
	TwitterReportingService twitterReportingService;

	@Async
	@Override
	@Transactional
	public void executeAsync() {

		// DSP
		try {
			// キャンペーン日別予算更新
			dspApiService.updateDailyBudget();
			// オペレーションログ記録
			operationService.createWithoutUser(Operation.DSP_CAMPAIGN_UPDATE.getValue(), "キャンペーン日別予算更新が成功しました。");
		} catch (Exception e) {
			log.error("キャンペーン日別予算更新中エラー発生", e);
			// オペレーションログ記録
			operationService.createWithoutUser(Operation.DSP_CAMPAIGN_UPDATE.getValue(), e.getMessage());
		}
		// Twitter
		twitterReportingService.changeBudget();

		// Facebook
		facebookReportingService.adjustDailyBudget();

		// Google

	}

}
