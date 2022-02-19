/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "ctlg_descuento_emision", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CtlgDescuentoEmision.findAll", query = "SELECT c FROM CtlgDescuentoEmision c"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByNumMes", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.numMes = :numMes"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByNumQuincena", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.numQuincena = :numQuincena"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByPorcentaje", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.porcentaje = :porcentaje"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByFechaIngreso", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByUsuarioIngreso", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByFechaModificacion", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "CtlgDescuentoEmision.findByUsuarioModificacion", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "CtlgDescuentoEmision.findById", query = "SELECT c FROM CtlgDescuentoEmision c WHERE c.id = :id")})
public class CtlgDescuentoEmision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "num_mes")
    private BigInteger numMes;
    @Column(name = "num_quincena")
    private BigInteger numQuincena;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 40)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 40)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    public CtlgDescuentoEmision() {
    }

    public CtlgDescuentoEmision(Long id) {
        this.id = id;
    }

    public BigInteger getNumMes() {
        return numMes;
    }

    public void setNumMes(BigInteger numMes) {
        this.numMes = numMes;
    }

    public BigInteger getNumQuincena() {
        return numQuincena;
    }

    public void setNumQuincena(BigInteger numQuincena) {
        this.numQuincena = numQuincena;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof CtlgDescuentoEmision)) {
            return false;
        }
        CtlgDescuentoEmision other = (CtlgDescuentoEmision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CtlgDescuentoEmision[ id=" + id + " ]";
    }
    
}
