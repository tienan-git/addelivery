package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TwitterDeviceReportCsvBean {

	public static final String[] columnName = { "キャンペーンID", "キャンペーン名", "デバイス", "表示回数", "クリック数", "フォロワー数", "ご利用金額",
			"CTR", "CPC", "CPM" };

	// @Trim を付けると前後の半角スペースが除去される

	@Trim
	@Parsed(field = "キャンペーンID")
	private String campaignId;

	@Trim
	// CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
	@Parsed(field = "キャンペーン名")
	private String campaignName;

	@Trim
	@Parsed(field = "デバイス")
	private String deviceType;

	@Trim
	@Parsed(field = "表示回数")
	private Integer impressionCount;

	@Trim
	@Parsed(field = "クリック数")
	private Integer clickCount;

	@Trim
	@Parsed(field = "フォロワー数")
	private Integer follows;

	@Trim
	@Parsed(field = "ご利用金額")
	private Long price;

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
