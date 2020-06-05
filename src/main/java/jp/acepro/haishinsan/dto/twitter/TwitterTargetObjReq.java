package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterTargetObjReq {

	String operation_type;
	TwitterTargetParamsReq params;

}
