package com.internal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tabulagram_spr", schema = "public", catalog = "internal")
public class TabulagramSpr implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;
    private Long id;
    private Period period;
    private Integer kod;
    private Integer type;
    private String str;

    @Basic
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Id
    @SequenceGenerator(name="tabulagram_spr_id_seq",sequenceName="tabulagram_spr_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="tabulagram_spr_id_seq")
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   /* @OneToOne(targetEntity = Period.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "period", foreignKey = @ForeignKey(name = "tabulagram_spr_period_id_fk"))*/
    @ManyToOne
    @JoinColumn(name = "period")
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Basic
    @Column(name = "kod")
    public Integer getKod() {
        return kod;
    }

    public void setKod(Integer kod) {
        this.kod = kod;
    }

    @Basic
    @Column(name = "str")
    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabulagramSpr that = (TabulagramSpr) o;
        return Objects.equals(id, that.id) && Objects.equals(period, that.period) && Objects.equals(kod, that.kod) && Objects.equals(type, that.type) && Objects.equals(str, that.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, kod, type, str);
    }
}
