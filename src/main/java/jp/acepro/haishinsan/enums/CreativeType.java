package jp.acepro.haishinsan.enums;

/**
 * 広告媒体タイプ 列挙クラス
 *
 *
 */
public enum CreativeType implements CodeEnum<String> {

	DSP("1", "ファンへの配信"), GOOGLE("2", "Google"), FACEBOOK("3", "Facebook"), INSTAGRAM("4", "Instagram"),
	TWITTER("5", "Twitter");

	private CreativeType(String value, String label) {
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

	public static CreativeType of(String code) {
		return CodeEnum.of(CreativeType.class, code);
	}
}
