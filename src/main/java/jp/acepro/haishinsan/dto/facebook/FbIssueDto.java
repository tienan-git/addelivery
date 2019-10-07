package jp.acepro.haishinsan.dto.facebook;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.enums.Flag;
import lombok.Data;

@Data
public class FbIssueDto {

	Long templateId;
	String unitPriceType;
	String campaignId;
	String campaignName;
	Integer campaignStatus;
	String campaignDisplayStatus;
	String checkStatus;

	// キャンペーンの上限予算（一万円以上）
	Long spendCap;

    String startTime;
    String endTime;
    String startHour;
    String startMin;
    String endHour;
    String endMin;

	String createdDate;
	String updatedDate;

	Long dailyBudget;
	Long bidAmount;
	String arrangePlace;
	String pageId;
	// 地域
	List<Long> locationList;

	// image
	String imageFileName;
	byte[] imageBytes;
	MultipartFile image;
	byte[] bytes;
	String Base64Str;
	File imageFile;

	String linkMessage;
	Integer segmentId;
	String linkUrl;
	String imageUrl;

	public boolean getSelected() {
		if (Flag.ON.getValue().equals(campaignStatus)) {
			return true;
		} else {
			return false;
		}
	}

}
