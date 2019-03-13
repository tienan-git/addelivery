package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum ReportType implements CodeEnum<Integer> {

	DEVICE(1, "デバイス"), DATE(2, "日付"), REGIONS(3, "地域"), CREATIVE(4, "クリエイティブ");

	private ReportType(Integer value, String label) {
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

	public static ReportType of(Integer code) {
		return CodeEnum.of(ReportType.class, code);
	}
}
