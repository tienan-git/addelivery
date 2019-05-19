package jp.acepro.haishinsan.form;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CreateIssueForm {

	// 案件名
	String issueName;

	// 日別予算
	Long dayBudget;

	// 配信開始日
	String issueStartDate;

	// 配信終了日
	String issueEndDate;

	// セグメントID（リンク先）
	Integer segmentId;

	// セグメントURL（リンク先）
	String segmentUrl;

	// Twitter以外広告Idリスト
	List<Long> nonTwitterAdIdList = new ArrayList<Long>();

	// Twitter広告Idリスト
	List<Long> twitterAdIdList = new ArrayList<Long>();
}
