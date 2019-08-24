package jp.acepro.haishinsan.enums;

/**
 * ロール 列挙クラス。
 *
 *
 */
public enum Role implements CodeEnum<Integer> {

	ADMIN(9, "管理"), CORPORATION(3, "法人"), SHOP(2, "店舗"), USER(1, "ユーザ"), AGENCY(4, "代理店"), SALES(8, "営業");

	private Role(Integer value, String label) {
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

	public static Role of(Integer long1) {
		return CodeEnum.of(Role.class, long1);
	}

}
