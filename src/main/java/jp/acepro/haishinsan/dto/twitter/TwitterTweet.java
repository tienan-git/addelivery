package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterTweet {

	String tweetId;
	String tweetTitle;
	String tweetBody;
	String previewUrl;
}
