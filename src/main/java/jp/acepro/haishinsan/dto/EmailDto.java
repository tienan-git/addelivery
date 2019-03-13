package jp.acepro.haishinsan.dto;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EmailDto {
	Long issueId;
	List<EmailCampDetailDto> campaignList;
	List<MultipartFile> attachmentList;
	Integer templateType;

}