/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.entities;

import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "observaciones", schema = "procesos")
@NamedQueries({
    @NamedQuery(name = "Observaciones.findAll", query = "SELECT o FROM Observaciones o"),
    @NamedQuery(name = "Observaciones.findById", query = "SELECT o FROM Observaciones o WHERE o.id = :id")})
public class Observaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private HistoricoTramites idTramite;
    @Column(name = "id_proceso")
    private BigInteger idProceso;
    @Column(name = "observacion", nullable = false)
    private String observacion;
    @Column(name = "tarea")
    private String tarea;
    @Column(name = "user_cre", nullable = false)
    private String userCre;
    @Column(name = "estado", nullable = false)
    private Boolean estado;
    @Column(name = "fec_cre", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;

    public Observaciones() {
    }

    public Observaciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoTramites getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
    }

    public BigInteger getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(BigInteger idProceso) {
        this.idProceso = idProceso;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Observaciones other = (Observaciones) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Observaciones{" + "id=" + id + ", observacion=" + observacion + ", tarea=" + tarea + ", userCre=" + userCre + ", estado=" + estado + '}';
    }

}
