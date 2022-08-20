package com.internal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "period", schema = "public", catalog = "internal")
public class Period {
    private Integer id;
    private String namePeriod;
    private String hours;
    private String day;
    private String text_p;
    @Basic
    @Column(name = "hours")
    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
    @Basic
    @Column(name = "day")
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    @Basic
    @Column(name = "text_p")
    public String getText_p() {
        return text_p;
    }

    public void setText_p(String text_p) {
        this.text_p = text_p;
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name_period")
    public String getNamePeriod() {
        return namePeriod;
    }

    public void setNamePeriod(String namePeriod) {
        this.namePeriod = namePeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(id, period.id) && Objects.equals(namePeriod, period.namePeriod) && Objects.equals(hours, period.hours) && Objects.equals(day, period.day) && Objects.equals(text_p, period.text_p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namePeriod, hours, day, text_p);
    }
}
