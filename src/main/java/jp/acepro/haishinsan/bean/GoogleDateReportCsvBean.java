package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoogleDateReportCsvBean {

	public static final String[] columnName = { "キャンペーンID", "キャンペーン名", "日付", "表示回数", "クリック数", "ご利用金額", "CTR", "CPC",
			"CPM" };

	// @Trim を付けると前後の半角スペースが除去される
	// CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
	@Trim
	@Parsed(field = "キャンペーンID")
	private String campaignId;

	@Trim
	@Parsed(field = "キャンペーン名")
	private String campaignName;

	@Trim
	@Parsed(field = "日付")
	private String dateSlash;

	@Trim
	@Parsed(field = "表示回数")
	private Long impressions;

	@Trim
	@Parsed(field = "クリック数")
	private Long clicks;

	@Trim
	@Parsed(field = "ご利用金額")
	private Long costs;

	@Trim
	@Parsed(field = "CTR")
	private String ctr;

	@Trim
	@Parsed(field = "CPC")
	private Integer cpc;

	@Trim
	@Parsed(field = "CPM")
	private Integer cpm;

}
