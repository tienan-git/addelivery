package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspAdGroupListReq {

	Integer user_id;
	// キャンペーンID
	Integer campaign_id;
	// 広告グループID
	Integer id;
}
