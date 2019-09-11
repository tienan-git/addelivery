package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DspCampaignInputForm {

	String campaignName;
	String startDatetime;
	String endDatetime;
	String startHour;
	String endHour;
	String startMin;
	String endMin;
	Integer budget;
	Integer deviceType;
	List<Integer> idList;
	Integer segmentId;
	Long templateId;

	String creativeName;
	MultipartFile image;
	String creativeName2;
	MultipartFile image2;
	String creativeName3;
	MultipartFile image3;
	String creativeName4;
	MultipartFile image4;
	String creativeName5;
	MultipartFile image5;
}
