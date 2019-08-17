package jp.acepro.haishinsan.dto;

import java.util.List;

import jp.acepro.haishinsan.dto.yahoo.YahooImageDto;
import lombok.Data;

@Data
public class EmailDto {
    Long issueId;
    List<EmailCampDetailDto> campaignList;
    List<YahooImageDto> attachmentList; 
    Integer templateType;

}