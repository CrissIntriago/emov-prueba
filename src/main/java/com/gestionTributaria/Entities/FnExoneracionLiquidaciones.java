/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.entities.Usuarios;
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
@Table(name = "fn_exoneracion_liquidaciones", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnExoneracionLiquidaciones.findAll", query = "SELECT f FROM FnExoneracionLiquidaciones f")})
public class FnExoneracionLiquidaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAplicacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "liquidacion_ant")
    private BigInteger liquidacionAnt;

    @JoinColumn(name = "liquidacion_new", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @Column(name = "multas")
    private BigDecimal multas;
    @Column(name = "predio_rus_ant")
    private BigInteger predioRusAnt;
    @Column(name = "recargo")
    private BigDecimal recargo;
    @JoinColumn(name = "usuario_aplicacion", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioAplicacion;
    @Column(name = "predio_urb")
    private Long predioUrb;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnSolicitudExoneraciones exoneracion;

    public FnExoneracionLiquidaciones() {
    }

    public FnExoneracionLiquidaciones(Long id) {
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

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigInteger getLiquidacionAnt() {
        return liquidacionAnt;
    }

    public void setLiquidacionAnt(BigInteger liquidacionAnt) {
        this.liquidacionAnt = liquidacionAnt;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacionNew) {
        this.liquidacion = liquidacionNew;
    }

    public BigDecimal getMultas() {
        return multas;
    }

    public void setMultas(BigDecimal multas) {
        this.multas = multas;
    }

    public BigInteger getPredioRusAnt() {
        return predioRusAnt;
    }

    public void setPredioRusAnt(BigInteger predioRusAnt) {
        this.predioRusAnt = predioRusAnt;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public Usuarios getUsuarioAplicacion() {
        return usuarioAplicacion;
    }

    public void setUsuarioAplicacion(Usuarios usuarioAplicacion) {
        this.usuarioAplicacion = usuarioAplicacion;
    }

    public Long getPredioUrb() {
        return predioUrb;
    }

    public void setPredioUrb(Long predioUrb) {
        this.predioUrb = predioUrb;
    }

    public FnSolicitudExoneraciones getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(FnSolicitudExoneraciones exoneracion) {
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
        if (!(object instanceof FnExoneracionLiquidaciones)) {
            return false;
        }
        FnExoneracionLiquidaciones other = (FnExoneracionLiquidaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnExoneracionLiquidaciones[ id=" + id + " ]";
    }

}
