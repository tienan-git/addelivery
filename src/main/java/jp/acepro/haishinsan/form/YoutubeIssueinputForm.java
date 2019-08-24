package jp.acepro.haishinsan.form;

import java.util.List;

import org.springframework.data.util.Pair;

import lombok.Data;

@Data
public class YoutubeIssueinputForm {

	// 案件ID
	Long issueId;

	// キャンペーン名
	String campaignName;

	// キャンペーン名
	Long campaignId;

	// 広告タイプ
	String adType;

	// 予算
	Long budget;

	// 配信開始日
	String startDate;

	// 配信終了日
	String endDate;

	// 配信地域
	String area;

	// LP
	String lp;

	// 動画URL
	String videoUrl;

	List<Long> locationIdList;

	List<Pair<Integer, String>> segmentList;

}
