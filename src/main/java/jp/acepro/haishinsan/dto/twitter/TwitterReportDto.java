package jp.acepro.haishinsan.dto.twitter;

import java.util.List;

import lombok.Data;

@Data
public class TwitterReportDto {
	
	List<String> campaignIdList;
	String campaignId;
	String startDate;
	String endDate;
	Integer reportType;
}
