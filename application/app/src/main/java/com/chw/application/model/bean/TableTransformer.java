package com.chw.application.model.bean;

public class TableTransformer {

    /**
     * 表模板项id
     */
    private long id;

    /**
     * 表模板项对应值
     */
    private String value;

    /**
     * 表模板项是否必填字段
     */
    private boolean fill;

    public TableTransformer(long id, String value, boolean fill) {
        this.id = id;
        this.value = value;
        this.fill = fill;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }
}
