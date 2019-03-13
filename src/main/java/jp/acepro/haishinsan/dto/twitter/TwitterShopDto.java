package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;

@Data
public class TwitterShopDto {
	
	Long shopId;
	String jobId;
	String twitterAccountId;
	String twitterApiKey;
	String twitterApiSecretKey;
	String twitterAccessToken;
	String twitterAccessTokenSecret;

}
