package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspAdReportDto {

	List<Integer> campaignIdList;
	Integer campaignId;
	String startDate;
	String endDate;
	Integer reportType;
}
