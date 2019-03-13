package jp.acepro.haishinsan.dto.twitter;

import lombok.Data;

@Data
public class TwitterDisplayReportDto {

	String campaignId;
	String campaignName;
	String deviceType;
    String deviceName;
	String locationName;
	String day;
	
	Integer impressions;
	Integer follows;
	Integer clicks;
	Long costs;
	String ctr;
	Integer cpc;
	Integer cpm;

}
