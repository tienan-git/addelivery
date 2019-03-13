package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspSegmentUpdateReq {

	Integer id;
	Integer user_id;
	Integer is_enabled;
}
