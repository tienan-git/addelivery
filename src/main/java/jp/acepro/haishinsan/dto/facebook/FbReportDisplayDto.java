package jp.acepro.haishinsan.dto.facebook;

import lombok.Data;

@Data
public class FbReportDisplayDto {

	String campaignId;;
	String campaignName;
	String date;
	String region;
	String device;
	Long impressions;
	Long clicks;
	Long spend;
	String ctr;
	Integer cpc;
	Integer cpm;

}
