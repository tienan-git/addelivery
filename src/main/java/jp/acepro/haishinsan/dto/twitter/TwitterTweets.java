package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterTweets {
	
		String created_at;
		String id_str;
		String text;
		TweetEntities entities;
		TweetUser user;
		
		
	
}
