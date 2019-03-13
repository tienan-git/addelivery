package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum PeriodSet implements CodeEnum<Integer> {

	WHOLE(0, "全期間"), LIMITED(1, "設定");

	private PeriodSet(Integer value, String label) {
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

	public static PeriodSet of(Integer code) {
		return CodeEnum.of(PeriodSet.class, code);
	}
}
