package jp.acepro.haishinsan.form;

import com.univocity.parsers.annotations.Trim;

import lombok.Data;

@Data
public class IssueSearchForm {

	// 店舗
	@Trim
	String shopName;
	// キャンペーン名
	@Trim
	String campaignName;
	// 配信媒体
	String media;
	// ステータス（アイコン）
	String status;
	// 配信開始日
	String startDate;
}
