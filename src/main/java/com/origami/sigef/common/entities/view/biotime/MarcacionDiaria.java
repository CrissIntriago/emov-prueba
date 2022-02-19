/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.view.biotime;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Criss
 */
@Entity
@Table(name = "marcacion_diaria")
//@NamedQueries(
//        @NamedQuery(name = "MarcacionDiaria.findByEmpCode", query = "SELECT m FROM MarcacionDiaria m WHERE m.empCode = 1?"))
public class MarcacionDiaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column
    private Long id;
    @Column(name = "emp_code")
    private String empCode;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "start_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column(name = "start_time")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date endTime;
    @Column(name = "wt_second")
    private Integer wtSecond;
    @Transient
    @Formula(value = "wt_second / 60")
    private Integer wtMinute;
    @Transient
    @Formula(value = "(wt_second / 60) / 60")
    private Integer wtHoras;

    public MarcacionDiaria() {
    }

    public MarcacionDiaria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getWtSecond() {
        return wtSecond;
    }

    public void setWtSecond(Integer wtSecond) {
        this.wtSecond = wtSecond;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MarcacionDiaria other = (MarcacionDiaria) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MarcacionDiaria{" + "id=" + id + ", empCode=" + empCode + ", firstName=" + firstName + ", startDate=" + startDate + ", startTime=" + startTime + ", endTime="
                + endTime + ", wt_second=" + wtSecond + ", wtMinute=" + getWtMinute() + '}';
    }

    public Integer getWtMinute() {
        if (wtMinute == null) {
            if (getWtSecond() != null) {
                wtMinute = wtSecond / 60;
                wtHoras = wtMinute / 60;
            } else {
                wtMinute = 0;
            }
        }
        return wtMinute;
    }

    public void setWtMinute(Integer wtMinute) {
        this.wtMinute = wtMinute;
    }

    public Integer getWtHoras() {
        if (getWtHoras() == null) {
            wtHoras = getWtMinute() / 60;
        }
        return wtHoras;
    }

    public void setWtHoras(Integer wtHoras) {
        this.wtHoras = wtHoras;
    }

}
