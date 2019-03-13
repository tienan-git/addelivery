package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspSegmentListReq {

	Integer user_id;
	Integer id;
	List<Integer> ids;
}
