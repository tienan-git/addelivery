package jp.acepro.haishinsan.dto.facebook;

import lombok.Data;

@Data
public class FbRegionReportDto {

	String campaign_id;;
	String campaign_name;
	String impressions;
	String clicks;
	String spend;
	String date_start;
	String date_stop;
	String region;

}
