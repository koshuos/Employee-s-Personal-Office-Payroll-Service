package com.internal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tabulagram_data", schema = "public", catalog = "internal")
public class TabulagramData implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;
    private Long id;
    private Integer tabN;
    private Integer kod;
    private String data;
    private Period period;

    @Id
    @SequenceGenerator(name="tabulagram_data_id_seq",sequenceName="tabulagram_data_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="tabulagram_data_id_seq")
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tab_n")
    public Integer getTabN() {
        return tabN;
    }

    public void setTabN(Integer tabN) {
        this.tabN = tabN;
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
    @Column(name = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @ManyToOne
   //@JoinColumn(nullable = false, name = "period", foreignKey = @ForeignKey(name = "tabulagram_data_period_id_fk"))
    @JoinColumn(name = "period")
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabulagramData that = (TabulagramData) o;
        return Objects.equals(id, that.id) && Objects.equals(tabN, that.tabN) && Objects.equals(kod, that.kod) && Objects.equals(data, that.data) && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tabN, kod, data, period);
    }
}
