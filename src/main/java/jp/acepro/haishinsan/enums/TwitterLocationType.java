package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum TwitterLocationType implements CodeEnum<Integer> {

	ALLCITY(0, "全ての国と地域"), JAPAN(1, "日本"), REGION(2, "都道府県");

	private TwitterLocationType(Integer value, String label) {
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

	public static TwitterLocationType of(Integer code) {
		return CodeEnum.of(TwitterLocationType.class, code);
	}
}
