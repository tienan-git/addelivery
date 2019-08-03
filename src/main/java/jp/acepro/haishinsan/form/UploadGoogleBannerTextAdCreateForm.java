package jp.acepro.haishinsan.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadGoogleBannerTextAdCreateForm {

	// 広告名
	String resAdName;

	// 短い広告見出し
	String resAdShortTitle;

	// 説明文
	String resAdDescription;

	// 画像ファイル01
	MultipartFile imageFile01;
	String imageData01;

	// 画像ファイル02
	MultipartFile imageFile02;
	String imageData02;
}
