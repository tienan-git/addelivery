package jp.acepro.haishinsan;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BudgetTest {

	@Test
	public void calcute() throws Exception {

		int budget = 6000;
		int cost = 1000;

		for (int i = 0; i < 24; i++) {
			calculateBudget(i, budget, cost);
		}
	}

	/**
	 * 予算決定ロジック予算算出
	 * 
	 * @param currentHour 実際費用取得の時
	 * @param budget      元々の予算
	 * @param costFee     使った費用
	 * @return 計算後予算（再設定値）
	 */
	int calculateBudget(int currentHour, int budget, int costFee) {

		// 時間割でウエイト算出
		float weightRadio = TimeTable.getWeightRadio(currentHour);
		float leftWeightRadio = 1.0f - weightRadio;

		// 残予算
		int leftFee = budget - costFee;

		// 調整後予算
		int adjustBudget = (int) (leftFee / leftWeightRadio);

		log.debug("currentHour:{} budget:{} costFee:{} weightRadio:{}　→　adjustBudget[{}]", currentHour, budget, costFee,
				weightRadio, adjustBudget);

//		System.out.println(currentHour + "," + budget + "," + costFee + "," + weightRadio + "," + adjustBudget);

		return adjustBudget;
	}

}
