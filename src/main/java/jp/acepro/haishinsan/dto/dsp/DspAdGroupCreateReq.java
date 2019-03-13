package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspAdGroupCreateReq {

	Integer user_id;
	Integer campaign_id;
	String name;
	Integer device;
	Integer daily_freq_cap;
	Integer rotation_method;
	Integer status;
}
