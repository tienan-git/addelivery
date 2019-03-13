package jp.acepro.haishinsan.enums;

/**
 * 設備タイプ 列挙クラス。
 *
 *
 */
public enum UnitPriceType implements CodeEnum<String> {

	CLICK("01", "クリック重視"), DISPLAY("02", "表示重視");

	private UnitPriceType(String value, String label) {
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

	public static UnitPriceType of(String code) {
		return CodeEnum.of(UnitPriceType.class, code);
	}
}
