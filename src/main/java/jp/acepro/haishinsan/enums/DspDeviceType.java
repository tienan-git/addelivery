package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum DspDeviceType implements CodeEnum<Integer> {

	ALL(9, "全て"), PC(1, "パソコン"), MOBILE(2, "モバイル");

	private DspDeviceType(Integer value, String label) {
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

	public static DspDeviceType of(Integer code) {
		return CodeEnum.of(DspDeviceType.class, code);
	}
}
