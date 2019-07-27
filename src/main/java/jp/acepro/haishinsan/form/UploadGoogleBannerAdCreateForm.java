package jp.acepro.haishinsan.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadGoogleBannerAdCreateForm {

	// 画像ファイル01
	MultipartFile imageFile01;
	byte[] imageData01;

	// 画像ファイル02
	MultipartFile imageFile02;
	byte[] imageData02;

	// 画像ファイル03
	MultipartFile imageFile03;
	byte[] imageData03;

	// 画像ファイル04
	MultipartFile imageFile04;
	byte[] imageData04;

	// リタゲット配信
	String retargeting = "00";
}
