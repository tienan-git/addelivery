package jp.acepro.haishinsan.dto.dsp;

import java.util.List;

import lombok.Data;

@Data
public class DspAdBitCreateReq {

	Integer user_id;
	Integer adgroup_id;
	Integer type;
	Integer price;
	Integer status;
	List<Segment> segments;
}
