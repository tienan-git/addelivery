package jp.acepro.haishinsan.enums;

/**
 * 審査フラグ 列挙クラス
 *
 *
 */
public enum AdvDestination implements CodeEnum<String> {

	IMAGE("2", "イメージ広告"), RESPONSIVE("1", "レスポンシブ広告"), LISTING("0", "リスティング");

	private AdvDestination(String value, String label) {
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

	public static AdvDestination of(String code) {
		return CodeEnum.of(AdvDestination.class, code);
	}
}
