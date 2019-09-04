package jp.acepro.haishinsan.service;

import java.time.LocalDateTime;

public interface BudgetCalculationService {

	/**
	 * 予算自動計算
	 * 
	 * ・終了日時過ぎた場合、予算更新しない
	 * 
	 * ・最小予算以下にならないこと
	 * 
	 * ・最大予算は1,000,000超えないこと
	 * 
	 * @param startDateTime   開始日時(2019-08-12 12:30)
	 * @param endDateTime     終了日時(2019-08-16 03:00)
	 * @param budget          元予算
	 * @param currentDateTime レポート取得日時
	 * @param costFee         実際費用（使った分）
	 * @return 計算後予算
	 */

	long calculateBudget(String startDateTime, String endDateTime, long budget, LocalDateTime currentDateTime,
			long costFee);

}
