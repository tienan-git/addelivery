package jp.acepro.haishinsan.enums;

/**
 * 広告媒体タイプ 列挙クラス
 *
 *
 */
public enum CreativeType implements CodeEnum<Integer> {

	DSP(1, "ファンへの配信"), GOOGLE(2, "Google"), FACEBOOK(3, "Facebook"), TWITTER(4, "Twitter");

	private CreativeType(Integer value, String label) {
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

	public static CreativeType of(Integer code) {
		return CodeEnum.of(CreativeType.class, code);
	}
}
