package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;

@Data
public class TwitterAdsDto {
	
	Long twitter_campaign_manage_id; 
	String campaignId;
	String 	campaignName;
	String 	startTime;
	String 	endTime;
	long dailyBudget;
	long totalBudget;
	Integer objective;
	Integer location;
	
	List<String> tweetIdList;
	List<String> regions;
	
	List<TwitterTweet> websiteTweetList;
	
	List<TwitterTweet> followersTweetList;
	
	//List<TwitterTemplateDto> templateList;
	
}
