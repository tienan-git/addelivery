package jp.acepro.haishinsan.enums;

/**
 * セグメントフラグ 列挙クラス。
 *
 *
 */
public enum SegmentFlag implements CodeEnum<Integer> {

	ON(1, "有効"), OFF(0, "無効");

	private SegmentFlag(Integer value, String label) {
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

	public static SegmentFlag of(Integer long1) {
		return CodeEnum.of(SegmentFlag.class, long1);
	}

}
