package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FacebookRegionReportCsvBean {

	public static final String[] columnName = { "キャンペーンID", "キャンペーン名", "地域", "表示回数", "クリック数", "ご利用金額", "CTR", "CPC", "CPM" };

	// @Trim を付けると前後の半角スペースが除去される
	// CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
	@Trim
	@Parsed(field = "キャンペーンID")
	private String campaignId;
	
	@Trim
	@Parsed(field = "キャンペーン名")
	private String campaignName;

	@Trim
	@Parsed(field = "地域")
	private String locationName;

	@Trim
	@Parsed(field = "表示回数")
	private String impressions;

	@Trim
	@Parsed(field = "クリック数")
	private String clicks;

	@Trim
	@Parsed(field = "ご利用金額")
	private String costs;

	@Trim
	@Parsed(field = "CTR")
	private String ctr;

	@Trim
	@Parsed(field = "CPC")
	private String cpc;

	@Trim
	@Parsed(field = "CPM")
	private String cpm;

}
