package jp.acepro.haishinsan.dto;

import java.util.List;

import lombok.Data;

@Data
public class EmailDto {
    Long issueId;
    List<EmailCampDetailDto> campaignList;
    List<String> attachmentList;
    List<String> imageNameList;
    Integer templateType;

}