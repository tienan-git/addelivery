package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspReportingGraphDto {

	List<String> reportType;
	List<String> impressionCount;
	List<String> clickCount;
	List<String> price;
	List<String> cpc;
	List<String> cpm;
	List<String> ctr;
}
