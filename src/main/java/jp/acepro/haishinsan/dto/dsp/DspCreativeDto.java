package jp.acepro.haishinsan.dto.dsp;

import lombok.Data;

@Data
public class DspCreativeDto {

	Integer creativeId;
	String creativeName;
	byte[] bytes;
	
	String src;
	String Base64Str;
	Integer screening;
}
