package jp.acepro.haishinsan.enums;

/**
 * 案件一覧：媒体タイプ 列挙クラス
 *
 */
public enum IssueAdtype implements CodeEnum<String> {

	GOOGLE("label google", "Google"), FACEBOOK("label faceBook", "FaceBook"), DSP("label dsp", "FreakOut"),
	TWITTER("label twitter", "Twitter"), YAHOO("label yahoo", "Yahoo"), YOUTUBE("label youtube", "Youtube");

	private IssueAdtype(String value, String label) {
		this.value = value;
		this.label = label;
	}

	/** icon */
	private String value;

	/** 名称 */
	private String label;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public static IssueAdtype of(String code) {
		return CodeEnum.of(IssueAdtype.class, code);
	}
}
