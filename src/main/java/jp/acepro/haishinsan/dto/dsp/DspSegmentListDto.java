package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspSegmentListDto {

	Long segmentManageId;
	Integer segmentId;
	String segmentName;
	String url;
	Integer is_enabled;
}
