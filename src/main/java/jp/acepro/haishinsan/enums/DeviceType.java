package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum DeviceType implements CodeEnum<String> {

	ALL("99", "全て"), PC("01", "パソコン"), MOBILE("02", "モバイル");

	private DeviceType(String value, String label) {
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

	public static DeviceType of(String code) {
		return CodeEnum.of(DeviceType.class, code);
	}
}
