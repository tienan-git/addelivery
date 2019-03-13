package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum SalesCheckFlag implements CodeEnum<String> {

	CHECK("1", "CHECK"), UNCHECK("0", "UNCHECK");

	private SalesCheckFlag(String value, String label) {
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

	public static SalesCheckFlag of(String code) {
		return CodeEnum.of(SalesCheckFlag.class, code);
	}
}
