package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspReportingListDto {

	String campaignId;
	String campaignName;
	Integer creativeId;
	String creativeName;
	String startDate;
	String endDate;
	String date;
	Integer deviceType;
	Integer impressionCount;
	Integer clickCount;
	Long price;
	String ctr;
	Integer cpc;
	Integer cpm;

}
