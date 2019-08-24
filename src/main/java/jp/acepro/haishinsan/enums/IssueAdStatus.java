package jp.acepro.haishinsan.enums;

/**
 * 案件一覧：媒体ステータス 列挙クラス
 *
 *
 */
public enum IssueAdStatus implements CodeEnum<String> {

	WAIT("label wait", "配信待ち"), END("label stop", "配信済み"), ALIVE("label live", "配信中");

	private IssueAdStatus(String value, String label) {
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

	public static IssueAdStatus of(String code) {
		return CodeEnum.of(IssueAdStatus.class, code);
	}
}
