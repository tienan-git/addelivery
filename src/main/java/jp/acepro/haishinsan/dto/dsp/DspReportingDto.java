package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DspReportingDto {

	List<DspReportingListDto> dspReportingDtoList = new ArrayList<DspReportingListDto>();
	DspReportingGraphDto dspReportingGraphDto = new DspReportingGraphDto();
}
