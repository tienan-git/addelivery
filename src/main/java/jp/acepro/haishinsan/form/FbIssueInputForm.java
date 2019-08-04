package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FbIssueInputForm {

	List<String> idList;

	Long templateId;
	String unitPriceType;
	String campaignName;
	String campaignStatus;
	String checkStatus;

	String startDate;
	String endDate;
	Long dailyBudget;
	String arrangePlace;
	Long bidAmount;
	String pageId;
	
	// 地域
	List<Long> locationList;

	MultipartFile image;
	String linkMessage;
	Integer segmentId;
	String linkUrl;
	
	
	
}
