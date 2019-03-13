package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterCampaignData {
	
	 //Campaigns
	 String id;
	 String name;
	 String start_time;
	 String end_time;
	 Long daily_budget_amount_local_micro;
	 Long total_budget_amount_local_micro;
	 List<String> reasons_not_servable;
	 String entity_status;
	 String servable;
	 Integer location;
	 
	// キャンペインステータス
	 String campaignStatus;
	 
	// 審査状況
	String approvalFlag;
	 
	 //lineItems
	 String line_item_id;
	 String objective;
	 
	 //targets
	 List<String> targets;
	 Integer target;
	 
	 //tweets
	 List<TwitterTweet> tweetList;
}
