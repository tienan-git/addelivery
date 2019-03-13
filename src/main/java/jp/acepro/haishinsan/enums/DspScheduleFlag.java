package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum DspScheduleFlag implements CodeEnum<Integer> {

	SET(1, "設定"), UNSET(0, "未設定");

	private DspScheduleFlag(Integer value, String label) {
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

	public static DspScheduleFlag of(Integer code) {
		return CodeEnum.of(DspScheduleFlag.class, code);
	}
}
