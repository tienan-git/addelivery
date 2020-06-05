package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterCampaignIdReq {

	String id;
	String line_item_ids;
	String line_item_id;
	String campaign_ids;

}
