package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspAdCreateReq {

	Integer user_id;
	Integer adgroup_id;
	Integer creative_id;
	Integer status;
	String url;
	String review_landing_url;
	String start_time;
	String end_time;
}
