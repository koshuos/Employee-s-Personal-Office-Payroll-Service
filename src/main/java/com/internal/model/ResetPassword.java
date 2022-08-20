package com.internal.model;

import java.io.Serializable;

public class ResetPassword implements Serializable {

    private String email;
    private String tab_n;

    public ResetPassword() {

    }

    public String getTab_n() {
        return tab_n;
    }

    public void setTab_n(String tab_n) {
        this.tab_n = tab_n;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
