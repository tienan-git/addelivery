package jp.acepro.haishinsan.enums;

/**
 * クリエイティブフォーマット 列挙クラス
 *
 *
 */
public enum CreativeFormat implements CodeEnum<Integer> {

	PICTURE(0, "イメージ"), ANIMATION(1, "アニメション");

	private CreativeFormat(Integer value, String label) {
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

	public static CreativeFormat of(Integer code) {
		return CodeEnum.of(CreativeFormat.class, code);
	}
}
