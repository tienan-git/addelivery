package jp.acepro.haishinsan.enums;

/**
 * 重視タイプ 列挙クラス
 *
 *
 */
public enum GoogleCampaignStatus implements CodeEnum<String> {

	ENABLED("ENABLED", "有効"), PAUSED("PAUSED", "一時停止"), REMOVED("REMOVED", "削除済み"), UNKNOWN("UNKNOWN", "未知");

	private GoogleCampaignStatus(String value, String label) {
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

	public static GoogleCampaignStatus of(String code) {
		return CodeEnum.of(GoogleCampaignStatus.class, code);
	}
}
