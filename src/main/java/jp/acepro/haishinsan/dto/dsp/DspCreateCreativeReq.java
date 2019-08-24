package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class DspCreateCreativeReq {

	private Integer user_id;

	private String name;

	private String body;

}
