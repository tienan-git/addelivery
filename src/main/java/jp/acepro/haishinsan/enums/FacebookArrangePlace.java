package jp.acepro.haishinsan.enums;

/**
 * Facebookの広告配置場所 列挙クラス。
 *
 *
 */
public enum FacebookArrangePlace implements CodeEnum<String> {

	BOTH("01", "両方"), FACEBOOK("02", "Facebook"), INSTAGRAM("03", "Instagram");

	private FacebookArrangePlace(String value, String label) {
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

	public static FacebookArrangePlace of(String code) {
		return CodeEnum.of(FacebookArrangePlace.class, code);
	}
}
