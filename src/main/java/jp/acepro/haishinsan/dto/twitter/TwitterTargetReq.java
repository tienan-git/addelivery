package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterTargetReq {
	
	List<TwitterTargetObjReq> request;
	
}
