package jp.acepro.haishinsan.enums;

/**
 * フラグ列挙クラス
 *
 *
 */
public enum TwitterOperationType implements CodeEnum<Integer> {

    CREATE(0, "Create"), DELETE(1, "Delete"), UPDATE(2, "Update");

    private TwitterOperationType(Integer value, String label) {
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

    public static TwitterOperationType of(Integer code) {
        return CodeEnum.of(TwitterOperationType.class, code);
    }
}
