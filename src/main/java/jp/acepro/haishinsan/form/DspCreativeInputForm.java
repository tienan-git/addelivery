package jp.acepro.haishinsan.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DspCreativeInputForm {

	String creativeName;
	MultipartFile image;
}
