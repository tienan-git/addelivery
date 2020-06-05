package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspCampaignCreateReq {

	Integer user_id;
	String name;
	Integer daily_budget;
	Integer monthly_budget;
	Integer is_scheduled;
	Integer billing_type;
	String start_datetime;
	String end_datetime;
	String reset_time;
	Integer status;
	DspCampaignKpi performance_indicator = new DspCampaignKpi();

}
