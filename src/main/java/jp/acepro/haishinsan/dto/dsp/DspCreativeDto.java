package jp.acepro.haishinsan.dto.dsp;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DspCreativeDto {

	Integer creativeId;
	byte[] bytes;
	String Base64Str;
	String creativeName;

	Integer creativeId2;
	byte[] bytes2;
	String Base64Str2;
	String creativeName2;

	Integer creativeId3;
	byte[] bytes3;
	String Base64Str3;
	String creativeName3;

	Integer creativeId4;
	byte[] bytes4;
	String Base64Str4;
	String creativeName4;

	Integer creativeId5;
	byte[] bytes5;
	String Base64Str5;
	String creativeName5;

	String url;
	Integer screening;
	LocalDateTime createdAt;
}
