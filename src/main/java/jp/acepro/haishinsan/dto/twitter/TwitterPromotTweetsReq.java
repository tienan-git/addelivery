package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterPromotTweetsReq {

	// グループID
	String line_item_id;

	// ツイートID
	String tweet_ids;

}
