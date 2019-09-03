package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum MediaType implements CodeEnum<Integer> {

	DSP(1, "DSP"), GOOGLERES1(21, "Googleレスポンシブ広告"), GOOGLERES2(22, "Googleレスポンシブ広告"), GOOGLEIMG1(31, "Googleイメージ広告"), GOOGLEIMG2(32, "Googleイメージ広告"), 
	GOOGLEIMG3(33, "Googleイメージ広告"), GOOGLEIMG4(34, "Googleイメージ広告"), FACEBOOK(4, "FACEBOOK"),
	TWITTER(5, "TWITTER"), YAHOORESPONSIVE(6, "Yahooレスポンシブ広告"), YAHOOIMAGE(7, "Yahooイメージ"), YOUTUBE(8, "Youtube"),
	YAHOOLISTING(9, "Yahooリスティング");

	private MediaType(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

	/** 値 */
	private Integer value;

	/** 名称 */
	private String label;

	public String getLabel() {
		return label;
	}

	public Integer getValue() {
		return value;
	}

	public static MediaType of(Integer code) {
		return CodeEnum.of(MediaType.class, code);
	}
}
