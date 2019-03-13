package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspCampaignListDto {

	Integer id;
	String name;
	Integer daily_budget;
	Integer monthly_budget;
	Integer billing_type;
	String start_datetime;
	String end_datetime;
	Integer status;

}
