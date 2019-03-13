package jp.acepro.haishinsan.enums;

/**
 * Google広告タイプ 列挙クラス
 *
 *
 */
public enum YoutubeAdType implements CodeEnum<String> {

	INSTREAM ("04", "インストリーム広告"), BUMPER("05", "バンパー広告");

	private YoutubeAdType(String value, String label) {
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

	public static YoutubeAdType of(String code) {
		return CodeEnum.of(YoutubeAdType.class, code);
	}
}
