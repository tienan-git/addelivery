package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class YoutubeDeviceReportCsvBean {

	public static final String[] columnName = { "キャンペーンID", "キャンペーン名", "デバイス", "表示回数", "クリック数", "ご利用金額", "CTR", "CPC", "CPM", "視聴回数", "視聴率", "視聴単価" };

	// @Trim を付けると前後の半角スペースが除去される
	// CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
	@Trim
	@Parsed(field = "キャンペーンID")
	private String campaignId;
	
	@Trim
	@Parsed(field = "キャンペーン名")
	private String campaignName;

	@Trim
	@Parsed(field = "デバイス")
	private String deviceName;

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
	
	@Trim
	@Parsed(field = "視聴回数")
    Long videoViews;

	@Trim
	@Parsed(field = "視聴率")
    String videoViewRate;

	@Trim
	@Parsed(field = "視聴単価")
	Integer avgCpv;
}
