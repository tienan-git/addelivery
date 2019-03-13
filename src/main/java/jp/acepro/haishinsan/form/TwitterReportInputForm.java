package jp.acepro.haishinsan.form;

import lombok.Data;

@Data
public class TwitterReportInputForm {
	
	// キャンペーンId
	String campaignIds;
	// 配信開始日
	String startDate;
	// 配信終了日
	String endDate;
	// レポートタイプ
	Integer reportType;
	
	Integer period;
	
}
