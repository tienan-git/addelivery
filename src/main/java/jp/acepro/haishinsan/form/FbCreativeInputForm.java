package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FbCreativeInputForm {

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

}
