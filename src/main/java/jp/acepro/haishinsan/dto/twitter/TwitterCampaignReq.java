package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterCampaignReq {

	// funding_instrument_id
	String funding_instrument_id;

	// キャンペーン名
	String name;

	// 開始日
	String start_time;

	// 終了日
	String end_time;

	// 日予算
	String daily_budget_amount_local_micro;

	// 総予算
	String total_budget_amount_local_micro;

	// キャンペーンステータス
	String entity_status;

}
