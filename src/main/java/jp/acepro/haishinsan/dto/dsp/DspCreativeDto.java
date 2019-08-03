package jp.acepro.haishinsan.dto.dsp;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DspCreativeDto {

	Integer creativeId;
	String creativeName;
	byte[] bytes;
	
	String src;
	String Base64Str;
	Integer screening;
	LocalDateTime createdAt;
}
