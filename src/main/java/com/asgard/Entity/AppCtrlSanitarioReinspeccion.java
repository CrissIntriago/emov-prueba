/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_ctrl_sanitario_reinspeccion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppCtrlSanitarioReinspeccion.findAll", query = "SELECT a FROM AppCtrlSanitarioReinspeccion a"),
    @NamedQuery(name = "AppCtrlSanitarioReinspeccion.findById", query = "SELECT a FROM AppCtrlSanitarioReinspeccion a WHERE a.id = :id"),
    @NamedQuery(name = "AppCtrlSanitarioReinspeccion.findByFecha", query = "SELECT a FROM AppCtrlSanitarioReinspeccion a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AppCtrlSanitarioReinspeccion.findByPermisoFuncionamiento", query = "SELECT a FROM AppCtrlSanitarioReinspeccion a WHERE a.permisoFuncionamiento = :permisoFuncionamiento")})
public class AppCtrlSanitarioReinspeccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "permiso_funcionamiento")
    private Boolean permisoFuncionamiento;
    @JoinColumn(name = "ctrl_sanitario_mejoras", referencedColumnName = "id")
    @ManyToOne
    private AppCtrlSanitarioMejoras ctrlSanitarioMejoras;

    public AppCtrlSanitarioReinspeccion() {
    }

    public AppCtrlSanitarioReinspeccion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getPermisoFuncionamiento() {
        return permisoFuncionamiento;
    }

    public void setPermisoFuncionamiento(Boolean permisoFuncionamiento) {
        this.permisoFuncionamiento = permisoFuncionamiento;
    }

    public AppCtrlSanitarioMejoras getCtrlSanitarioMejoras() {
        return ctrlSanitarioMejoras;
    }

    public void setCtrlSanitarioMejoras(AppCtrlSanitarioMejoras ctrlSanitarioMejoras) {
        this.ctrlSanitarioMejoras = ctrlSanitarioMejoras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppCtrlSanitarioReinspeccion)) {
            return false;
        }
        AppCtrlSanitarioReinspeccion other = (AppCtrlSanitarioReinspeccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppCtrlSanitarioReinspeccion[ id=" + id + " ]";
    }
    
}
