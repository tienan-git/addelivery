package jp.acepro.haishinsan.enums;

/**
 * 承認フラグ 列挙クラス
 *
 *
 */
public enum ApprovalFlag implements CodeEnum<String> {

	WAITING("0", "承認待ち"), COMPLETED("1", "承認済み");

	private ApprovalFlag(String value, String label) {
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

	public static ApprovalFlag of(String code) {
		return CodeEnum.of(ApprovalFlag.class, code);
	}
}
