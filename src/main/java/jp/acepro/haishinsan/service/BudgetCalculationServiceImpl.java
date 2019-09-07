package jp.acepro.haishinsan.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BudgetCalculationServiceImpl implements BudgetCalculationService {

	@Override
	public long calculateBudget(String startDateTime, String endDateTime, long budget, long costFee,
			LocalDateTime currentDateTime) {

		// TODO 一旦元予算+1のダミーデータ返す
		return budget + 1;
	}

}