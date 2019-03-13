package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum Flag implements CodeEnum<Integer> {

	OFF(0, "OFF"), ON(1, "ON");

	private Flag(Integer value, String label) {
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

	public static Flag of(Integer code) {
		return CodeEnum.of(Flag.class, code);
	}
}
