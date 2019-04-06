package jp.acepro.haishinsan.dto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class NonTwitterAdDto {

	Long adId;
	String adImageName;
	String adImageSize;
	ArrayList<String> adImageUrlList = new ArrayList<String>();
	String adText;
	String adReviewStatus;
	String adCreateDate;
	String adReviewDate;
	String adIssue;
	String adIcon;
}
