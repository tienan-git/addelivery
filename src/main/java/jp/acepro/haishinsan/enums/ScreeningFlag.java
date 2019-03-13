package jp.acepro.haishinsan.enums;

/**
 * クリエイティブ審査 列挙クラス
 *
 *
 */
public enum ScreeningFlag implements CodeEnum<Integer> {

	PASS(1, "審査済み"), FAIL(-1, "審査失敗"), OVERSIGHT(0, "審査中");

	private ScreeningFlag(Integer value, String label) {
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

	public static ScreeningFlag of(Integer code) {
		return CodeEnum.of(ScreeningFlag.class, code);
	}
}
