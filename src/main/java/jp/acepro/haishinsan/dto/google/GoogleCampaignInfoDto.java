package jp.acepro.haishinsan.dto.google;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleCampaignInfoDto {
	
	// キャンペインステータス
	String campaignStatus;
	
	// 審査状況
	String approvalFlag;
	
	// キャンペインID
	Long campaignId;
	
	// キャンペイン名
	String campaignName;
	
	// キャンペイン名
	String adType;
	
	// 配信開始日
	String startDate;
    public String getStartDateSlash() {
		return StringFormatter.formatToSlash(startDate);
	}
	
	// 配信終了日
	String endDate;
    public String getEndDateSlash() {
		return StringFormatter.formatToSlash(endDate);
	}
}
