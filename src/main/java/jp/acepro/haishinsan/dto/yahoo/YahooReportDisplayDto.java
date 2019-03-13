package jp.acepro.haishinsan.dto.yahoo;

import lombok.Data;

@Data
public class YahooReportDisplayDto {

	String campaignId;;
	String campaignName;
	String date;
	String region;
	String device;
	String impressions;
	String clicks;
	String spend;
	String ctr;
	String cpc;
	String cpm;

}
