package jp.acepro.haishinsan.dto.yahoo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YahooImageDto {

	// 画像名
	String imageName;

	// 画像データ
	String imageData;

	// 画像タイプ
	String contentType;
}
