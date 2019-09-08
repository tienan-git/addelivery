package jp.acepro.haishinsan.enums;

/**
 * 審査ステータス 列挙クラス
 *
 *
 */
public enum CheckStatus implements CodeEnum<String> {

	PROCESSING("0", "審査中"), PREAPPROVED("1", "承認"), DISAPPROVED("2", "不承認");

	private CheckStatus(String value, String label) {
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

	public static CheckStatus of(String code) {
		return CodeEnum.of(CheckStatus.class, code);
	}
}
