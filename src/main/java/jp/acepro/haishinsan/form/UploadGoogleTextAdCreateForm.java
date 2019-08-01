package jp.acepro.haishinsan.form;

import lombok.Data;

@Data
public class UploadGoogleTextAdCreateForm {

	// 広告名
	String textAdName;

	// 広告見出し１
	String textAdTitle1;

	// 広告見出し２
	String textAdTitle2;

	// 説明文
	String textAdDescription;
}
