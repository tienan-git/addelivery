package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class DspCreateCreativeRes {
	Integer id;
	Integer user_id;
	Integer type;
	String src;
	String name;
	Integer screening;
	String size;
	String raw_size;
	String hash;
	Integer length;
	String html;
	Integer ad_cnt;
	Integer imagetype;

}
