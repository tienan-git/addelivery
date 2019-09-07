package jp.acepro.haishinsan.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Service;

import jp.acepro.haishinsan.enums.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BudgetCalculationServiceImpl implements BudgetCalculationService {

	/**
	 * 計算ロジック：(総予算-既に使った予算)/広告残日数
	 */
	@Override
	public long calculateBudget(String startDateTime, String endDateTime, long budget, long costFee,
			LocalDateTime currentDateTime) {

		LocalDateTime startLocalDateTime = DateTimeFormatter.yyyyMMddHHmm_HAIFUN_COLON.parse(startDateTime);
		LocalDateTime endLocalDateTime = DateTimeFormatter.yyyyMMddHHmm_HAIFUN_COLON.parse(endDateTime);
		LocalDate startLocalDate = startLocalDateTime.toLocalDate();
		LocalDate endLocalDate = endLocalDateTime.toLocalDate();
		LocalDate currentLocalDate = currentDateTime.toLocalDate();
		int remainingDays;
		long newDailyBudget;

		// 広告残日数算出
		Period period = Period.between(currentLocalDate, endLocalDate);
		remainingDays = period.getDays() + 1;

		// 開始当日なら、計算しない
		if (startLocalDate.isEqual(currentLocalDate)) {
			newDailyBudget = 0;
		} else {
			// 新日予算計算
			newDailyBudget = (budget - costFee) / remainingDays;
		}

		log.info("日予算計算　総予算:{} 使った予算:{} 残日数:{} 新日予算:{}", budget, costFee, remainingDays, newDailyBudget);
		return newDailyBudget;
	}

	public static void main(String[] args) {
		BudgetCalculationService budgetCalculationService = new BudgetCalculationServiceImpl();
		budgetCalculationService.calculateBudget("2019-09-01 18:00", "2019-09-03 12:30", 5000, 1000,
				LocalDateTime.of(2019, 9, 1, 22, 00));
		

		budgetCalculationService.calculateBudget("2019-09-01 18:00", "2019-09-03 12:30", 5000, 1000,
				LocalDateTime.of(2019, 9, 2, 22, 00));
		

		budgetCalculationService.calculateBudget("2019-09-01 18:00", "2019-09-03 12:30", 5000, 1000,
				LocalDateTime.of(2019, 9, 3, 22, 00));

	}

}