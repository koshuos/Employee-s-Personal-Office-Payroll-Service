package com.internal.model;

import java.io.Serializable;

public class Registration implements Serializable {

    private String tab_nomer;
    private String drfo;
    private String pay;
    private String email;
    private String password;

    public Registration() {
    }

    public String getTab_nomer() {
        return tab_nomer;
    }

    public void setTab_nomer(String tab_nomer) {
        this.tab_nomer = tab_nomer;
    }

    public String getDrfo() {
        return drfo;
    }

    public void setDrfo(String drfo) {
        this.drfo = drfo;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
