package jp.acepro.haishinsan.form;

import java.util.List;

import jp.acepro.haishinsan.util.StringFormatter;
import lombok.Data;

@Data
public class GoogleTextAdIssueForm {

	// 広告名
	String textAdName;

	// 広告見出し１
	String textAdTitle1;

	// 広告見出し２
	String textAdTitle2;

	// 説明文
	String textAdDescription;
	
	// テンプレートID
	Long templateId;

	// テンプレート名
	String templateName;

	// セグメントID
	Integer segmentId;

	// セグメントURL
	String url;

	// キャンペイン名
	String campaignName;

	// 地域
	List<Long> locationList;

	// 予算
	Long budget;

	// 単価タイプ
	String unitPriceType;

	// 開始日
	String startDate;

	public String getStartDateSlash() {
		return StringFormatter.dateHyphenToSlash(startDate);
	}

	// 終了日
	String endDate;

	public String getEndDateSlash() {
		return StringFormatter.dateHyphenToSlash(endDate);
	}
	
    String startTime;
    String endTime;
    String startHour;
    String startMin;
    String endHour;
    String endMin;

	// デバイスタイプ
	String deviceType;

	// 広告タイプ
	String adType;
}
