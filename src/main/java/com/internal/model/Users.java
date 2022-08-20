package com.internal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Users {
    private Integer userid;
    private String username;
    private String password;
    private Integer enabled;
    private String email;
    private Integer show_admin_tab;

    @Basic
    @Column(name = "show_admin_tab")
    public Integer getShow_admin_tab() {
        return show_admin_tab;
    }

    public void setShow_admin_tab(Integer show_admin_tab) {
        this.show_admin_tab = show_admin_tab;
    }

    @Id
    @Column(name = "userid")
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "enabled")
    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return userid == users.userid && Objects.equals(username, users.username) && Objects.equals(password, users.password) && Objects.equals(enabled, users.enabled) && Objects.equals(email, users.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, username, password, enabled, email);
    }
}
