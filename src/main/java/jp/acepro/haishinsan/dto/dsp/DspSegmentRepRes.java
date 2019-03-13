package jp.acepro.haishinsan.dto.dsp;

import java.util.ArrayList;
import java.util.List;

import jp.acepro.haishinsan.dto.DspSegmentRepDto;
import lombok.Data;

@Data
public class DspSegmentRepRes {

	List<DspSegmentRepDto> result = new ArrayList<DspSegmentRepDto>();
}
