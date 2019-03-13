package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterGroupReq {

	//キャンペーンID
	String campaign_id;
	
	//キャンペーン目的（FOLLOWERS,WEBSITE_CLICKS）
	String objective;
	
	//placement (ALL_ON_TWITTER)
	String placements;
	
	//product_type (PROMOTED_ACCOUNT)
	String product_type;
	
	//..
	String bid_type;
	
	//..
	String entity_status;
	
	//
	String bid_amount_local_micro;
}
