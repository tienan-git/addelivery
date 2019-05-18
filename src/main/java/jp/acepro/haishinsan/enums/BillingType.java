package jp.acepro.haishinsan.enums;

/**
 * 審査フラグ 列挙クラス
 *
 *
 */
public enum BillingType implements CodeEnum<Integer> {

	CPM(0, "CPMクリック課金"), CPC(1, "CPCクリック課金");

	private BillingType(Integer value, String label) {
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

	public static BillingType of(Integer code) {
		return CodeEnum.of(BillingType.class, code);
	}
}
