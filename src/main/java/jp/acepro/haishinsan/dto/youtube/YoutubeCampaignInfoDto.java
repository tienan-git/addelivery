package jp.acepro.haishinsan.dto.youtube;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class YoutubeCampaignInfoDto {

	// キャンペインステータス
	String campaignStatus;

	// 審査状況
	String approvalFlag;

	// キャンペインID
	Long campaignId;

	// キャンペイン名
	String campaignName;

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
