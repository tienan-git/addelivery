package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.data.util.Pair;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleReportSearchForm {
	
	// キャンペーン名リスト
	List<Pair<Long, String>> campaignPairList;
	
	// キャンペーンIDリスト
	List<Long> campaignIdList;
	
	// 期間種別
	Integer period;

	// 開始日
	String startDate;
    public String getStartDateFormat() {
		return StringFormatter.dateFormat(startDate);
	}

	// 終了日
	String endDate;
    public String getEndDateFormat() {
		return StringFormatter.dateFormat(endDate);
	}
}
