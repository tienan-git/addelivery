package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum FacebookCampaignStatus implements CodeEnum<String> {

	ACTIVE("ACTIVE", "有効"), PAUSED("PAUSED", "停止");

	private FacebookCampaignStatus(String value, String label) {
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

	public static FacebookCampaignStatus of(String code) {
		return CodeEnum.of(FacebookCampaignStatus.class, code);
	}
}
