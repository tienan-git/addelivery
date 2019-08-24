package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DspDateReportCsvBean {

	public static final String[] columnName = { "キャンペーン名", "キャンペーンID", "対象日", "表示回数", "クリック数", "費用", "CTR", "CPC",
			"CPM" };

	// @Trim を付けると前後の半角スペースが除去される
	@Trim
	// CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
	@Parsed(field = "キャンペーン名")
	private String campaignName;

	@Trim
	@Parsed(field = "キャンペーンID")
	private String campaignId;

	@Trim
	@Parsed(field = "対象日")
	private String date;

	@Trim
	@Parsed(field = "表示回数")
	private Integer impressionCount;

	@Trim
	@Parsed(field = "クリック数")
	private Integer clickCount;

	@Trim
	@Parsed(field = "費用")
	private Long price;

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
