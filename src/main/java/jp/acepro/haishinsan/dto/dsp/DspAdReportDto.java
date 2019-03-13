package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspAdReportDto {

	List<Integer> campaignIdList;
	String startDate;
	String endDate;
	Integer reportType;
}
