/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_exoneracion_liquidacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnExoneracionLiquidacion.findAll", query = "SELECT f FROM FnExoneracionLiquidacion f")})
public class FnExoneracionLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "es_urbano")
    private Boolean esUrbano;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "liquidacion_posterior", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacionPosterior;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacionOriginal;
    @Size(max = 250)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnSolicitudExoneracion exoneracion;
  

    public FnExoneracionLiquidacion() {
    }

    public FnExoneracionLiquidacion(Long id) {
        this.id = id;
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

    public Boolean getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(Boolean esUrbano) {
        this.esUrbano = esUrbano;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public FinaRenLiquidacion getLiquidacionPosterior() {
        return liquidacionPosterior;
    }

    public void setLiquidacionPosterior(FinaRenLiquidacion liquidacionPosterior) {
        this.liquidacionPosterior = liquidacionPosterior;
    }

    public FinaRenLiquidacion getLiquidacionOriginal() {
        return liquidacionOriginal;
    }

    public void setLiquidacionOriginal(FinaRenLiquidacion liquidacionOriginal) {
        this.liquidacionOriginal = liquidacionOriginal;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public FnSolicitudExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(FnSolicitudExoneracion exoneracion) {
        this.exoneracion = exoneracion;
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
        if (!(object instanceof FnExoneracionLiquidacion)) {
            return false;
        }
        FnExoneracionLiquidacion other = (FnExoneracionLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnExoneracionLiquidacion[ id=" + id + " ]";
    }

}
