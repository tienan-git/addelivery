package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspSegmentRepReq {

	Integer user_id;
	List<Integer> segment_ids;
	String start_date;
	String end_date;
}
