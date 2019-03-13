package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspAdReportResDto {

	// キャンペーンID
	Integer campaign_id;
	// 広告グループID
	Integer adgroup_id;
	// 広告ID
	Integer ad_id;
	// 対象日
	String date;
	// 表示回数
	Integer impression_count;
	// クリック回数
	Integer click_count;
	// 広告費用
	Double price;
}
