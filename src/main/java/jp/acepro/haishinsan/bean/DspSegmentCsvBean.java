package jp.acepro.haishinsan.bean;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Data;

@Data
public class DspSegmentCsvBean {

	public static final String[] columnName = { "セグメント名", "セグメントID", "対象日", "累積UU", "累積UU(PC)", "累積UU(SP)" };

	// @Trim を付けると前後の半角スペースが除去される
	@Trim
	// CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
	@Parsed(field = "セグメント名")
	private String segmentName;

	@Trim
	@Parsed(field = "セグメントID")
	private Integer segmentId;

	@Trim
	@Parsed(field = "対象日")
	private String date;

	@Trim
	@Parsed(field = "累積UU")
	private Integer uunum;

	@Trim
	@Parsed(field = "累積UU(PC)")
	private Integer uunumPc;

	@Trim
	@Parsed(field = "累積UU(SP)")
	private Integer uunumSp;

}
