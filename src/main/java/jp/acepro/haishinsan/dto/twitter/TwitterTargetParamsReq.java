package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterTargetParamsReq {

	// グループID
	String line_item_id;

	// ターゲットタイプ
	String targeting_type;

	// targeting_value
	String targeting_value;

}
