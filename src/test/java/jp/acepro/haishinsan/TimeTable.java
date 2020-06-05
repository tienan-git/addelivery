package jp.acepro.haishinsan;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@Slf4j
public class TimeTable {

	public static void main(String[] args) {
		for (int i = 0; i < 24; i++) {
			getWeightRadio(i);
		}
	}

	static float getTotalWeight() {
		// total
		float total = 0f;
		for (Hour hour : hours) {
			total = total + hour.rate;
		}
		return total;
	}

	static float getWeightRadio(int currentHour) {
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
//		log.debug("[{}]radio:{} weight/total:{}/{}", currentHour, radio, weight, totalWeight);
		return radio;
	}

	public static List<Hour> hours = new ArrayList<Hour>();

	static {
		hours.add(new Hour(0, 2f));
		hours.add(new Hour(1, 1.5f));
		hours.add(new Hour(2, 0.5f));
		hours.add(new Hour(3, 0.5f));
		hours.add(new Hour(4, 0.5f));
		hours.add(new Hour(5, 0.5f));
		hours.add(new Hour(6, 0.5f));
		hours.add(new Hour(7, 1f));
		hours.add(new Hour(8, 1f));
		hours.add(new Hour(9, 1f));
		hours.add(new Hour(10, 1f));
		hours.add(new Hour(11, 1f));
		hours.add(new Hour(12, 1f));
		hours.add(new Hour(13, 1f));
		hours.add(new Hour(14, 1f));
		hours.add(new Hour(15, 0.5f));
		hours.add(new Hour(16, 0.5f));
		hours.add(new Hour(17, 1f));
		hours.add(new Hour(18, 1f));
		hours.add(new Hour(19, 1f));
		hours.add(new Hour(20, 1f));
		hours.add(new Hour(21, 1.5f));
		hours.add(new Hour(22, 1.5f));
		hours.add(new Hour(23, 2f));
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
