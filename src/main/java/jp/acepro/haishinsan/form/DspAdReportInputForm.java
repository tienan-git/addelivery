package jp.acepro.haishinsan.form;

import java.util.List;

import lombok.Data;

@Data
public class DspAdReportInputForm {

	// キャンペーンIDリスト
	List<Integer> campaignIdList;
	// 全期間
	Integer period;
	// 開始日
	String startDate;
	// 終了日
	String endDate;
	// レポートタイプ
	Integer reportType;
}
