package jp.acepro.haishinsan.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class S3UploadDto {

	/**
	 * ファイル（イメージなど）
	 */
	MultipartFile multipartFile;
	/**
	 * 媒体タイプ
	 */
	Integer mediaType;
	/**
	 * 拡張子
	 */
	String extension;
	/**
	 * ファイル名
	 */
	String name;
	/**
	 * ファイル全名 ファイルをアップロード、ダウンロードする時に使う 設定不可
	 */
	String totalName;

	private void setTotalName() {

	}

	public String getTotalName() {

		this.totalName = name + extension;

		return totalName;
	}

}
