package jp.acepro.haishinsan.dto.google;

import java.util.HashMap;

import lombok.Data;

@Data
public class GoogleOtherLocationDto {

	Long campaignId;
	HashMap<String, GoogleReportDisplayDto> locationMap = new HashMap<String, GoogleReportDisplayDto>();
}
