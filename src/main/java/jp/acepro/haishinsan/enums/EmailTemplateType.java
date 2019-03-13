package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum EmailTemplateType implements CodeEnum<Integer> {

	CAMPAIGN(1, "キャンペーン"), ISSUEREQUEST(2, "案件依頼");

	private EmailTemplateType(Integer value, String label) {
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

	public static EmailTemplateType of(Integer code) {
		return CodeEnum.of(EmailTemplateType.class, code);
	}
}
