package jp.acepro.haishinsan.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadGoogleBannerAdCreateForm {

	// 広告名
	String imgAdName;

	// 画像ファイル01
	MultipartFile imageFile01;
	String imageData01;

	// 画像ファイル02
	MultipartFile imageFile02;
	String imageData02;

	// 画像ファイル03
	MultipartFile imageFile03;
	String imageData03;

	// 画像ファイル04
	MultipartFile imageFile04;
	String imageData04;
}
