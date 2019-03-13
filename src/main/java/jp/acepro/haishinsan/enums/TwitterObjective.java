package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum TwitterObjective implements CodeEnum<Integer> {

	WEBSITE(0, "ウェブサイトのクリックまたはコンバージョン"), FOLLOWER(1, "フォロワー");

	private TwitterObjective(Integer value, String label) {
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

	public static TwitterObjective of(Integer code) {
		return CodeEnum.of(TwitterObjective.class, code);
	}
}
