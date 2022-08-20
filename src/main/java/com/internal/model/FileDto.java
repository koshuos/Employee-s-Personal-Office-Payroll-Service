package com.internal.model;

public class FileDto {

    private String data_spr;
    private String data_data;
    private Period period;
    private Boolean email;

    public FileDto(String data_spr, String data_data, Period period, Boolean email) {
        this.data_spr = data_spr;
        this.data_data = data_data;
        this.period = period;
        this.email = email;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public String getData_spr() {
        return data_spr;
    }

    public void setData_spr(String data_spr) {
        this.data_spr = data_spr;
    }

    public String getData_data() {
        return data_data;
    }

    public void setData_data(String data_data) {
        this.data_data = data_data;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
