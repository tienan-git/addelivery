package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterReportMetrics {
	
	List<String> impressions;
	List<String> billed_charge_local_micro;
	List<String> follows;
	List<String> retweets;
	List<String> likes;
	List<String> engagements;
	List<String> clicks;
	List<String> url_clicks;
	List<String> billed_engagements;
	
}
