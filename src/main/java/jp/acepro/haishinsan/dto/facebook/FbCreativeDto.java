package jp.acepro.haishinsan.dto.facebook;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.enums.Flag;
import lombok.Data;

@Data
public class FbCreativeDto {

	Long templateId;
	String unitPriceType;
	String pageId;
	String arrangePlace;
	String creativeName;
	// 地域
	List<Long> locationList;
	MultipartFile image;
	String linkMessage;
	Integer segmentId;
	String linkUrl;
	
	// image
	byte[] bytes;
	String Base64Str;
	File imageFile;


	
}
