/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.view.biotime;

import com.origami.sigef.talentohumano.model.MarcacionModel;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Criss
 */
@Entity
@Table(name = "marcacion")
@SqlResultSetMapping(name = "detalleMarcacionMaping",
        classes = @ConstructorResult(targetClass = MarcacionModel.class,
                columns = {
                    @ColumnResult(name = "emp_code", type = String.class)
                    ,@ColumnResult(name = "date", type = Date.class)
                    ,@ColumnResult(name = "hora_ingreso", type = Date.class)
                    ,@ColumnResult(name = "hora_salida", type = Date.class)
                    ,@ColumnResult(name = "ingreso_descanso", type = Date.class)
                    ,@ColumnResult(name = "salida_descanso", type = Date.class)
                    ,@ColumnResult(name = "total_hora", type = Date.class)
                    ,@ColumnResult(name = "total_hora_decanso", type = Date.class)
                    ,@ColumnResult(name = "horas_laboras", type = Date.class)
                    ,@ColumnResult(name = "horas_extras", type = Date.class)
                    ,@ColumnResult(name = "horas_extras_desc", type = Date.class)
                })
)
@SqlResultSetMapping(name = "detalleMarcacionLaboralMaping",
        classes = @ConstructorResult(targetClass = MarcacionModel.class,
                columns = {
                    @ColumnResult(name = "emp_code", type = String.class)
                    ,@ColumnResult(name = "date", type = Date.class)
                    ,@ColumnResult(name = "hora_ingreso", type = Date.class)
                    ,@ColumnResult(name = "hora_salida", type = Date.class)
                    ,@ColumnResult(name = "ingreso_descanso", type = Date.class)
                    ,@ColumnResult(name = "salida_descanso", type = Date.class)
                    ,@ColumnResult(name = "total_hora", type = Date.class)
                    ,@ColumnResult(name = "total_hora_decanso", type = Date.class)
                    ,@ColumnResult(name = "horas_laboras", type = Date.class)
                })
)
public class Marcacion implements Serializable {

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
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "punch_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date punchTime;
    @Column(name = "date_")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "time_")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "terminal_alias")
    private String terminalAlias;
    @Column(name = "area_alias")
    private String areaAlias;
    private String mobile;
    @Column(name = "punch_state")
    private String punchState;
    @Column(name = "event_name")
    private String eventName;

    public Marcacion() {
    }

    public Marcacion(Long id) {
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Date punchTime) {
        this.punchTime = punchTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTerminalAlias() {
        return terminalAlias;
    }

    public void setTerminalAlias(String terminalAlias) {
        this.terminalAlias = terminalAlias;
    }

    public String getAreaAlias() {
        return areaAlias;
    }

    public void setAreaAlias(String areaAlias) {
        this.areaAlias = areaAlias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPunchState() {
        return punchState;
    }

    public void setPunchState(String punchState) {
        this.punchState = punchState;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
        final Marcacion other = (Marcacion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Marcacion{" + "id=" + id + ", empCode=" + empCode + ", firstName=" + firstName + ", lastName=" + lastName + ", punchTime=" + punchTime + ", date=" + date + ", time=" + time + ", terminalAlias=" + terminalAlias + ", areaAlias=" + areaAlias + ", mobile=" + mobile + ", punchState=" + punchState + ", eventName=" + eventName + '}';
    }

    public String getNameEmployed() {
        return (firstName + (lastName == null ? "" : lastName));
    }

}
