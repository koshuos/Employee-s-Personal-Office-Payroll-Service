package com.internal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "last_pay", schema = "public", catalog = "internal")
public class LastPay {
    private Long id;
    private Double pay;
    private String employees;

    @Id
    @SequenceGenerator(name="last_pay_id_seq",sequenceName="last_pay_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="last_pay_id_seq")
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pay")
    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    @Basic
    @Column(name = "employees")
    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastPay lastPay = (LastPay) o;
        return id == lastPay.id && Objects.equals(pay, lastPay.pay) && Objects.equals(employees, lastPay.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pay, employees);
    }
}
