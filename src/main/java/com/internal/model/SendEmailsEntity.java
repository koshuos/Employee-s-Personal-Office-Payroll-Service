package com.internal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "send_emails", schema = "public", catalog = "internal")
public class SendEmailsEntity {

    @Id
    @Column(name = "tab")
    private Integer tab;
    @Basic
    @Column(name = "period")
    private Integer period;

    public Integer getTab() {
        return tab;
    }

    public void setTab(Integer tab) {
        this.tab = tab;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendEmailsEntity that = (SendEmailsEntity) o;
        return Objects.equals(tab, that.tab) && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tab, period);
    }
}
