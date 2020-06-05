package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum TwitterCampaignStatus implements CodeEnum<Integer> {

	RESERVATION(0, "STARTS_IN_FUTURE"), ACTIVE(1, "ACTIVE"), PAUSED(2, "PAUSED"),
	PAUSEDBYADVERTISER(3, "PAUSED_BY_ADVERTISER"), EXPIRED(4, "EXPIRED");

	private TwitterCampaignStatus(Integer value, String label) {
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

	public static TwitterCampaignStatus of(Integer code) {
		return CodeEnum.of(TwitterCampaignStatus.class, code);
	}
}
