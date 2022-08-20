package com.internal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Employees {
    private Integer id;
    private String pib;
    private String drfo;
    private String tabelNomer;

    @Id
    @SequenceGenerator(name="employees_id_seq",sequenceName="employees_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="employees_id_seq")
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pib")
    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    @Basic
    @Column(name = "drfo")
    public String getDrfo() {
        return drfo;
    }

    public void setDrfo(String drfo) {
        this.drfo = drfo;
    }

    @Basic
    @Column(name = "tabel_nomer")
    public String getTabelNomer() {
        return tabelNomer;
    }

    public void setTabelNomer(String tabelNomer) {
        this.tabelNomer = tabelNomer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employees employees = (Employees) o;
        return id == employees.id && Objects.equals(pib, employees.pib) && Objects.equals(drfo, employees.drfo) && Objects.equals(tabelNomer, employees.tabelNomer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pib, drfo, tabelNomer);
    }
}
