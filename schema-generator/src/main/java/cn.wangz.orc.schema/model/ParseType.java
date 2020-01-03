package cn.wangz.orc.schema.model;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public enum ParseType {

    CREATETABLE("CREATETABLE"),
    STRUCTSCHEMA("STRUCTSCHEMA");

    public String value;
    ParseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
