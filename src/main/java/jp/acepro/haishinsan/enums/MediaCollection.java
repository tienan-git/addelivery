package jp.acepro.haishinsan.enums;

/**
 * メディア 列挙クラス。
 *
 *
 */
public enum MediaCollection implements CodeEnum<Integer> {

	DSP(1, "/dsp/campaignList", "DSP"), GOOGLE(2, "/google/listCampaign", "Google"),
	FACEBOOK(3, "/facebook/campaignList", "Facebook"), TWITTER(4, "/twitter/adsList", "Twitter"),
	YAHOO(5, "/yahoo/issueList", "Yahoo!"), YOUTUBE(6, "/youtube/list", "YouTube");

	private MediaCollection(Integer value, String path, String label) {
		this.value = value;
		this.path = path;
		this.label = label;
	}

	/** 値 */
	private Integer value;

	/** パース */
	private String path;

	/** 名称 */
	private String label;

	public String getLabel() {
		return label;
	}

	public String getPath() {
		return path;
	}

	public Integer getValue() {
		return value;
	}

	public static MediaCollection of(Integer long1) {
		return CodeEnum.of(MediaCollection.class, long1);
	}

}
