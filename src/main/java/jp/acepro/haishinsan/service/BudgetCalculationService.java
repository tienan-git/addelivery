package jp.acepro.haishinsan.service;

import java.time.LocalDateTime;

public interface BudgetCalculationService {

	/**
	 * 予算自動計算
	 * 
	 * @param startDateTime   開始日時
	 * @param endDateTime     終了日時
	 * @param budget          元予算
	 * @param costFee         実際費用（使った分）
	 * @param currentDateTime レポート取得日時
	 * @return 計算後予算
	 */

	int calculateBudget(String startDateTime, String endDateTime, int budget, int costFee,
			LocalDateTime currentDateTime);

}
