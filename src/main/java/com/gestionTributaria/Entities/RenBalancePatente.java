/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import com.asgard.Entity.FinaRenPatente;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_balance_patente", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenBalancePatente.findAll", query = "SELECT r FROM RenBalancePatente r"),
    @NamedQuery(name = "RenBalancePatente.findById", query = "SELECT r FROM RenBalancePatente r WHERE r.id = :id"),
    @NamedQuery(name = "RenBalancePatente.findByAnioBalance", query = "SELECT r FROM RenBalancePatente r WHERE r.anioBalance = :anioBalance"),
    @NamedQuery(name = "RenBalancePatente.findByActivoTotal", query = "SELECT r FROM RenBalancePatente r WHERE r.activoTotal = :activoTotal"),
    @NamedQuery(name = "RenBalancePatente.findByActivoContingente", query = "SELECT r FROM RenBalancePatente r WHERE r.activoContingente = :activoContingente"),
    @NamedQuery(name = "RenBalancePatente.findByCapital", query = "SELECT r FROM RenBalancePatente r WHERE r.capital = :capital"),
    @NamedQuery(name = "RenBalancePatente.findByEstado", query = "SELECT r FROM RenBalancePatente r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenBalancePatente.findByFechaBalance", query = "SELECT r FROM RenBalancePatente r WHERE r.fechaBalance = :fechaBalance"),
    @NamedQuery(name = "RenBalancePatente.findByFechaIngreso", query = "SELECT r FROM RenBalancePatente r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RenBalancePatente.findByInteres", query = "SELECT r FROM RenBalancePatente r WHERE r.interes = :interes"),
    @NamedQuery(name = "RenBalancePatente.findByLiquidacion", query = "SELECT r FROM RenBalancePatente r WHERE r.liquidacion = :liquidacion"),
    @NamedQuery(name = "RenBalancePatente.findByLocalComercial", query = "SELECT r FROM RenBalancePatente r WHERE r.localComercial = :localComercial"),
    @NamedQuery(name = "RenBalancePatente.findByMesesInteres", query = "SELECT r FROM RenBalancePatente r WHERE r.mesesInteres = :mesesInteres"),
    @NamedQuery(name = "RenBalancePatente.findByPasivoTotal", query = "SELECT r FROM RenBalancePatente r WHERE r.pasivoTotal = :pasivoTotal"),
    @NamedQuery(name = "RenBalancePatente.findByPasivoContingente", query = "SELECT r FROM RenBalancePatente r WHERE r.pasivoContingente = :pasivoContingente"),
    @NamedQuery(name = "RenBalancePatente.findByPatente", query = "SELECT r FROM RenBalancePatente r WHERE r.patente = :patente"),
    @NamedQuery(name = "RenBalancePatente.findByPorcentajeIngreso", query = "SELECT r FROM RenBalancePatente r WHERE r.porcentajeIngreso = :porcentajeIngreso"),
    @NamedQuery(name = "RenBalancePatente.findByPorcentajeParticipacion", query = "SELECT r FROM RenBalancePatente r WHERE r.porcentajeParticipacion = :porcentajeParticipacion"),
    @NamedQuery(name = "RenBalancePatente.findByTipoExoneracion", query = "SELECT r FROM RenBalancePatente r WHERE r.tipoExoneracion = :tipoExoneracion"),
    @NamedQuery(name = "RenBalancePatente.findByUsuarioIngreso", query = "SELECT r FROM RenBalancePatente r WHERE r.usuarioIngreso = :usuarioIngreso")})
public class RenBalancePatente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_balance")
    private Integer anioBalance;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "activo_total")
    private BigDecimal activoTotal;
    @Column(name = "activo_contingente")
    private BigDecimal activoContingente;
    @Column(name = "capital")
    private BigDecimal capital;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_balance")
    @Temporal(TemporalType.DATE)
    private Date fechaBalance;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "interes")
    private BigDecimal interes;

    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)

    private FinaRenLocalComercial localComercial;
    @Column(name = "meses_interes")
    private Integer mesesInteres;
    @Column(name = "pasivo_total")
    private BigDecimal pasivoTotal;
    @Column(name = "pasivo_contingente")
    private BigDecimal pasivoContingente;
    @JoinColumn(name = "patente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenPatente patente;
    @Column(name = "porcentaje_ingreso")
    private BigDecimal porcentajeIngreso;
    @Column(name = "porcentaje_participacion")
    private BigDecimal porcentajeParticipacion;
    @Column(name = "tipo_exoneracion")
    private BigInteger tipoExoneracion;
    @Size(max = 50)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;

    public RenBalancePatente() {
        porcentajeParticipacion = new BigDecimal("100.00");
    }

    public RenBalancePatente(Long id) {
        this.id = id;
        porcentajeParticipacion = new BigDecimal("100.00");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioBalance() {
        return anioBalance;
    }

    public void setAnioBalance(Integer anioBalance) {
        this.anioBalance = anioBalance;
    }

    public BigDecimal getActivoTotal() {
        return activoTotal;
    }

    public void setActivoTotal(BigDecimal activoTotal) {
        this.activoTotal = activoTotal;
    }

    public BigDecimal getActivoContingente() {
        return activoContingente;
    }

    public void setActivoContingente(BigDecimal activoContingente) {
        this.activoContingente = activoContingente;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaBalance() {
        return fechaBalance;
    }

    public void setFechaBalance(Date fechaBalance) {
        this.fechaBalance = fechaBalance;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public Integer getMesesInteres() {
        return mesesInteres;
    }

    public void setMesesInteres(Integer mesesInteres) {
        this.mesesInteres = mesesInteres;
    }

    public BigDecimal getPasivoTotal() {
        return pasivoTotal;
    }

    public void setPasivoTotal(BigDecimal pasivoTotal) {
        this.pasivoTotal = pasivoTotal;
    }

    public BigDecimal getPasivoContingente() {
        return pasivoContingente;
    }

    public void setPasivoContingente(BigDecimal pasivoContingente) {
        this.pasivoContingente = pasivoContingente;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public void setPatente(FinaRenPatente patente) {
        this.patente = patente;
    }

    public BigDecimal getPorcentajeIngreso() {
        return porcentajeIngreso;
    }

    public void setPorcentajeIngreso(BigDecimal porcentajeIngreso) {
        this.porcentajeIngreso = porcentajeIngreso;
    }

    public BigDecimal getPorcentajeParticipacion() {
        return porcentajeParticipacion;
    }

    public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion) {
        this.porcentajeParticipacion = porcentajeParticipacion;
    }

    public BigInteger getTipoExoneracion() {
        return tipoExoneracion;
    }

    public void setTipoExoneracion(BigInteger tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
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
        if (!(object instanceof RenBalancePatente)) {
            return false;
        }
        RenBalancePatente other = (RenBalancePatente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenBalancePatente[ id=" + id + " ]";
    }

}
