package jp.acepro.haishinsan.form;

import java.util.List;

import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import lombok.Data;

@Data
public class TwitterAdsInputForm {
	
	Long templateId;
	
	//入力データ
	Long twitter_campaign_manage_id; 
	String 	campaignId;
	String 	campaignName;
	String 	startTime;
	String 	endTime;
	Integer dailyBudget;
	Integer totalBudget;
	Integer objective;
	Integer location;
	List<String> regions;
	List<String> tweetIdList;
	
	
	//初期値の為
	
	List<TwitterTweet> websiteTweetList;
	
	List<TwitterTweet> followersTweetList;
	
	List<TwitterTemplateDto> templateList;
	
	
}
