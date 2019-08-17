package jp.acepro.haishinsan.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BudgetCalculationServiceImpl implements BudgetCalculationService {

	@Value("${time-weight.weight}")
	private List<Float> weights;

	public List<Hour> hours = new ArrayList<Hour>();

	@PostConstruct
	void initHours() {
		for (int i = 0; i < weights.size(); i++) {
			hours.add(new Hour(i, weights.get(i)));
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
	public int calculateBudget(int currentHour, int budget, int costFee) {
		log.debug("hours:{} ", hours);

		// 時間割でウエイト算出
		float weightRadio = getWeightRadio(currentHour);
		float leftWeightRadio = 1.0f - weightRadio;

		// 残予算
		int leftFee = budget - costFee;

		// 調整後予算
		int adjustBudget = (int) (leftFee / leftWeightRadio);

		log.debug("currentHour:{} budget:{} costFee:{} weightRadio:{}　→　adjustBudget[{}]", currentHour, budget, costFee,
				weightRadio, adjustBudget);

		return adjustBudget;
	}

	float getTotalWeight() {
		// total
		float total = 0f;
		for (Hour hour : hours) {
			total = total + hour.rate;
		}
		return total;
	}

	float getWeightRadio(int currentHour) {
		// total
		float totalWeight = getTotalWeight();
		// 実際
		float weight = 0f;

		for (Hour hour : hours) {
			if (currentHour >= hour.hour) {
				weight = weight + hour.rate;
			}
		}

		float radio = weight / totalWeight;
		return radio;
	}

}

@Data
@AllArgsConstructor
@ToString
class Hour {
	// 時(0~23)
	int hour;

	// 時間割
	float rate;
}