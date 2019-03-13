package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspSegmentSearchDto {

	List<Integer> segmentIdList;
	String startDate;
	String endDate;
}
