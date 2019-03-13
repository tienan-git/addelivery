package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TwitterReport {
	
	String url;
	
	//reportDetail
	String id; //campaignId
	List<TwitterReportIdData> id_data;
	
}
