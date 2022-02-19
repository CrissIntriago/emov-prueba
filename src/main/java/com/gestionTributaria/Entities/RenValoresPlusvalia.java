/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_valores_plusvalia", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenValoresPlusvalia.findAll", query = "SELECT r FROM RenValoresPlusvalia r"),
    @NamedQuery(name = "RenValoresPlusvalia.findById", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.id = :id"),
    @NamedQuery(name = "RenValoresPlusvalia.findByBaseDesvalorizacion", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.baseDesvalorizacion = :baseDesvalorizacion"),
    @NamedQuery(name = "RenValoresPlusvalia.findByLiquidacion", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.liquidacion = :liquidacion"),
    @NamedQuery(name = "RenValoresPlusvalia.findByDesvalorizacionMonet", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.desvalorizacionMonet = :desvalorizacionMonet"),
    @NamedQuery(name = "RenValoresPlusvalia.findByDiferenciaNeta", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.diferenciaNeta = :diferenciaNeta"),
    @NamedQuery(name = "RenValoresPlusvalia.findByDiferenciaNeta2", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.diferenciaNeta2 = :diferenciaNeta2"),
    @NamedQuery(name = "RenValoresPlusvalia.findByFechaEscritaAnt", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.fechaEscritaAnt = :fechaEscritaAnt"),
    @NamedQuery(name = "RenValoresPlusvalia.findByFechaEscritaAct", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.fechaEscritaAct = :fechaEscritaAct"),
    @NamedQuery(name = "RenValoresPlusvalia.findByMejorasCemConst", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.mejorasCemConst = :mejorasCemConst"),
    @NamedQuery(name = "RenValoresPlusvalia.findByMejorasUrb", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.mejorasUrb = :mejorasUrb"),
    @NamedQuery(name = "RenValoresPlusvalia.findByPorcentajeRebaja", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.porcentajeRebaja = :porcentajeRebaja"),
    @NamedQuery(name = "RenValoresPlusvalia.findByRebajaGen", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.rebajaGen = :rebajaGen"),
    @NamedQuery(name = "RenValoresPlusvalia.findByUtilidadImponib", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.utilidadImponib = :utilidadImponib"),
    @NamedQuery(name = "RenValoresPlusvalia.findByVigencia", query = "SELECT r FROM RenValoresPlusvalia r WHERE r.vigencia = :vigencia")})
public class RenValoresPlusvalia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base_desvalorizacion")
    private BigDecimal baseDesvalorizacion;

    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @OneToOne(optional = false)
    private FinaRenLiquidacion liquidacion;
    @Column(name = "desvalorizacion_monet")
    private BigDecimal desvalorizacionMonet;
    @Column(name = "diferencia_neta")
    private BigDecimal diferenciaNeta;
    @Column(name = "diferencia_neta2")
    private BigDecimal diferenciaNeta2;
    @Column(name = "fecha_escrita_ant")
    @Temporal(TemporalType.DATE)
    private Date fechaEscritaAnt;
    @Column(name = "fecha_escrita_act")
    @Temporal(TemporalType.DATE)
    private Date fechaEscritaAct;
    @Column(name = "mejoras_cem_const")
    private BigDecimal mejorasCemConst;
    @Column(name = "mejoras_urb")
    private BigDecimal mejorasUrb;
    @Column(name = "porcentaje_rebaja")
    private Integer porcentajeRebaja;
    @Column(name = "rebaja_gen")
    private BigDecimal rebajaGen;
    @Column(name = "utilidad_imponib")
    private BigDecimal utilidadImponib;
    @Column(name = "vigencia")
    private Integer vigencia;
    @JoinColumn(name = "desvalorizacion", referencedColumnName = "id")
    @ManyToOne
    private RenDesvalorizacion desvalorizacion;
    @OneToMany(mappedBy = "valoresPlusvalia", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenDetallePlusvalia> renDetallePlusvaliaCollection;

    public RenValoresPlusvalia() {
        mejorasCemConst = BigDecimal.ZERO;
        diferenciaNeta = BigDecimal.ZERO;
        diferenciaNeta2 = BigDecimal.ZERO;
        porcentajeRebaja = 0;
        desvalorizacionMonet = BigDecimal.ZERO;
        rebajaGen = BigDecimal.ZERO;
        mejorasUrb = BigDecimal.ZERO;
        utilidadImponib = BigDecimal.ZERO;
    }

    public RenValoresPlusvalia(Long id) {
        this.id = id;
    }

    public RenValoresPlusvalia(Long id, FinaRenLiquidacion liquidacion) {
        this.id = id;
        this.liquidacion = liquidacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBaseDesvalorizacion() {
        return baseDesvalorizacion;
    }

    public void setBaseDesvalorizacion(BigDecimal baseDesvalorizacion) {
        this.baseDesvalorizacion = baseDesvalorizacion;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public BigDecimal getDesvalorizacionMonet() {
        return desvalorizacionMonet;
    }

    public void setDesvalorizacionMonet(BigDecimal desvalorizacionMonet) {
        this.desvalorizacionMonet = desvalorizacionMonet;
    }

    public BigDecimal getDiferenciaNeta() {
        return diferenciaNeta;
    }

    public void setDiferenciaNeta(BigDecimal diferenciaNeta) {
        this.diferenciaNeta = diferenciaNeta;
    }

    public BigDecimal getDiferenciaNeta2() {
        return diferenciaNeta2;
    }

    public void setDiferenciaNeta2(BigDecimal diferenciaNeta2) {
        this.diferenciaNeta2 = diferenciaNeta2;
    }

    public Date getFechaEscritaAnt() {
        return fechaEscritaAnt;
    }

    public void setFechaEscritaAnt(Date fechaEscritaAnt) {
        this.fechaEscritaAnt = fechaEscritaAnt;
    }

    public Date getFechaEscritaAct() {
        return fechaEscritaAct;
    }

    public void setFechaEscritaAct(Date fechaEscritaAct) {
        this.fechaEscritaAct = fechaEscritaAct;
    }

    public BigDecimal getMejorasCemConst() {
        return mejorasCemConst;
    }

    public void setMejorasCemConst(BigDecimal mejorasCemConst) {
        this.mejorasCemConst = mejorasCemConst;
    }

    public BigDecimal getMejorasUrb() {
        return mejorasUrb;
    }

    public void setMejorasUrb(BigDecimal mejorasUrb) {
        this.mejorasUrb = mejorasUrb;
    }

    public Integer getPorcentajeRebaja() {
        return porcentajeRebaja;
    }

    public void setPorcentajeRebaja(Integer porcentajeRebaja) {
        this.porcentajeRebaja = porcentajeRebaja;
    }

    public BigDecimal getRebajaGen() {
        return rebajaGen;
    }

    public void setRebajaGen(BigDecimal rebajaGen) {
        this.rebajaGen = rebajaGen;
    }

    public BigDecimal getUtilidadImponib() {
        return utilidadImponib;
    }

    public void setUtilidadImponib(BigDecimal utilidadImponib) {
        this.utilidadImponib = utilidadImponib;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public RenDesvalorizacion getDesvalorizacion() {
        return desvalorizacion;
    }

    public void setDesvalorizacion(RenDesvalorizacion desvalorizacion) {
        this.desvalorizacion = desvalorizacion;
    }

    public List<RenDetallePlusvalia> getRenDetallePlusvaliaCollection() {
        return renDetallePlusvaliaCollection;
    }

    public void setRenDetallePlusvaliaCollection(List<RenDetallePlusvalia> renDetallePlusvaliaCollection) {
        this.renDetallePlusvaliaCollection = renDetallePlusvaliaCollection;
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
        if (!(object instanceof RenValoresPlusvalia)) {
            return false;
        }
        RenValoresPlusvalia other = (RenValoresPlusvalia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenValoresPlusvalia[ id=" + id + " ]";
    }

}
