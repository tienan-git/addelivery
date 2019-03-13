package jp.acepro.haishinsan.enums;

/**
 * Google広告タイプ 列挙クラス
 *
 *
 */
public enum GoogleAdType implements CodeEnum<String> {

	IMAGE("02", "イメージ広告"), RESPONSIVE("01", "レスポンシブ広告"), TEXT("03", "リスティング広告");

	private GoogleAdType(String value, String label) {
		this.value = value;
		this.label = label;
	}

	/** 値 */
	private String value;

	/** 名称 */
	private String label;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public static GoogleAdType of(String code) {
		return CodeEnum.of(GoogleAdType.class, code);
	}
}
