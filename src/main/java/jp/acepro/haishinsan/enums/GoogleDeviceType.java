package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum GoogleDeviceType implements CodeEnum<String> {

	PC("01", "パソコン"), MOBILE("02", "モバイル"), TABLET("03", "‎タブレット");

	private GoogleDeviceType(String value, String label) {
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

	public static GoogleDeviceType of(String code) {
		return CodeEnum.of(GoogleDeviceType.class, code);
	}
}
