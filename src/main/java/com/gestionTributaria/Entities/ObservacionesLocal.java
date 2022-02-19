/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "observaciones_local", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObservacionesLocal.findAll", query = "SELECT o FROM ObservacionesLocal o"),
    @NamedQuery(name = "ObservacionesLocal.findById", query = "SELECT o FROM ObservacionesLocal o WHERE o.id = :id"),
    @NamedQuery(name = "ObservacionesLocal.findByEstado", query = "SELECT o FROM ObservacionesLocal o WHERE o.estado = :estado"),
    @NamedQuery(name = "ObservacionesLocal.findByFecCre", query = "SELECT o FROM ObservacionesLocal o WHERE o.fecCre = :fecCre"),
    @NamedQuery(name = "ObservacionesLocal.findByLocalComercial", query = "SELECT o FROM ObservacionesLocal o WHERE o.localComercial = :localComercial"),
    @NamedQuery(name = "ObservacionesLocal.findByObservacion", query = "SELECT o FROM ObservacionesLocal o WHERE o.observacion = :observacion"),
    @NamedQuery(name = "ObservacionesLocal.findByTarea", query = "SELECT o FROM ObservacionesLocal o WHERE o.tarea = :tarea"),
    @NamedQuery(name = "ObservacionesLocal.findByUserCre", query = "SELECT o FROM ObservacionesLocal o WHERE o.userCre = :userCre")})
public class ObservacionesLocal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fec_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;
    @JoinColumn(name = "local_comercial",referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalComercial localComercial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 100)
    @Column(name = "tarea")
    private String tarea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "user_cre")
    private String userCre;

    public ObservacionesLocal() {
    }

    public ObservacionesLocal(Long id) {
        this.id = id;
    }

    public ObservacionesLocal(Long id, String observacion, String userCre) {
        this.id = id;
        this.observacion = observacion;
        this.userCre = userCre;
    }

    
    public ObservacionesLocal(FinaRenLocalComercial localComercial, String observacion, String userCre, String tarea) {
        this.localComercial = localComercial;
        this.observacion = observacion;
        this.fecCre = new Date();
        this.userCre = userCre;
        this.tarea = tarea;
        this.estado = true;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
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
        if (!(object instanceof ObservacionesLocal)) {
            return false;
        }
        ObservacionesLocal other = (ObservacionesLocal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ObservacionesLocal[ id=" + id + " ]";
    }
    
}
