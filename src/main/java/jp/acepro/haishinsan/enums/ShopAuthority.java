package jp.acepro.haishinsan.enums;

/**
 * 店舗権限 列挙クラス
 *
 *
 */
public enum ShopAuthority implements CodeEnum<String> {

	MANAGE("SHOPLIST_MANAGE", "管理権限"), AGENCY("SHOPLIST_AGENCY", "代理店権限"),
	CORPORATION("SHOPLIST_CORPORATION", "法人権限"), SHOP("SHOPLIST_SHOP", "店舗権限");

	private ShopAuthority(String value, String label) {
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

	public static ShopAuthority of(String code) {
		return CodeEnum.of(ShopAuthority.class, code);
	}
}
