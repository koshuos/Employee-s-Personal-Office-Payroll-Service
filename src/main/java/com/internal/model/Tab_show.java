package com.internal.model;

public class Tab_show {
    private String name;
    private String value;
    private Integer kod;
    private Integer type;

    public Tab_show(String name, String value, Integer kod, Integer type) {
        this.name = name;
        this.value = value;
        this.kod = kod;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getKod() {
        return kod;
    }

    public void setKod(Integer kod) {
        this.kod = kod;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
