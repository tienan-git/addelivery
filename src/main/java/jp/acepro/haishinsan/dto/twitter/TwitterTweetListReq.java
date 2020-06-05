package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterTweetListReq {

	// キャンペーン目的（FOLLOWERS,WEBSITE_CLICKS）
	String objective;

	String count;

}
