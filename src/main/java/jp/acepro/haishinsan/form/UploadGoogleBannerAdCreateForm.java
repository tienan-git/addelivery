package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadGoogleBannerAdCreateForm {

	// 画像ファイル
	List<MultipartFile> imageAdImageFileList;
	List<byte[]> imageAdImageDataList;
}
