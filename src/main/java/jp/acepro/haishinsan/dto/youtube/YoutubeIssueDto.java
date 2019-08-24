package jp.acepro.haishinsan.dto.youtube;

import java.util.List;

import org.springframework.data.util.Pair;

import lombok.Data;

@Data
public class YoutubeIssueDto {

	// 案件ID
	Long issueId;

	// キャンペーンID
	Long campaignId;

	// キャンペーン名
	String campaignName;

	// 広告タイプ
	String adType;

	// 予算
	Long budget;

	// 配信開始日
	String startDate;

	// 配信終了日
	String endDate;

	// 配信地域
	List<Pair<Long, String>> locationList;

	String area;

	// LP
	String lp;

	// 動画URL
	String videoUrl;

	// youtubeキャンペインmanage ID
	Long youtubeCampaignManageId;

}
