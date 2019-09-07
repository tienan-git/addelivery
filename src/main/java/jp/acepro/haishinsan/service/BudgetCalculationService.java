package jp.acepro.haishinsan.service;

import java.time.LocalDateTime;

public interface BudgetCalculationService {

	/**
	 * 予算自動計算
	 * 
	 * ・配信中（開始〜終了の間）広告のみ調整する
	 * 
	 * @param startDateTime   広告開始日時(2019-08-12 12:30)
	 * @param endDateTime     広告終了日時(2019-08-16 03:00)
	 * @param budget          総予算
	 * @param costFee         実際費用（前日まで使った分）
	 * @param currentDateTime レポート取得日時
	 * @return 計算後日予算
	 */

	long calculateBudget(String startDateTime, String endDateTime, long budget, long costFee,
			LocalDateTime currentDateTime);

}
