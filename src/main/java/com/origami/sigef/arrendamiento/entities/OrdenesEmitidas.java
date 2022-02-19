/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.tesoreria.entities.Liquidacion;
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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "ordenes_emitidas", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "OrdenesEmitidas.findAll", query = "SELECT o FROM OrdenesEmitidas o")
    ,
    @NamedQuery(name = "OrdenesEmitidas.findById", query = "SELECT o FROM OrdenesEmitidas o WHERE o.id = :id")
    ,
    @NamedQuery(name = "OrdenesEmitidas.findByIdSolicitud", query = "SELECT o FROM OrdenesEmitidas o WHERE o.idSolicitud = ?1")
    ,
    @NamedQuery(name = "OrdenesEmitidas.findByMes", query = "SELECT o FROM OrdenesEmitidas o WHERE o.mes = :mes")
    ,
    @NamedQuery(name = "OrdenesEmitidas.findByAnio", query = "SELECT o FROM OrdenesEmitidas o WHERE o.anio = :anio")})
public class OrdenesEmitidas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "mes")
    private BigInteger mes;
    @Column(name = "anio")
    private Short anio;
    @JoinColumn(name = "id_arrendamiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Arrendamiento idArrendamiento;
    @Column(name = "monto_pagar")
    private BigDecimal montoPagar;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Column(name = "notificar_orden")
    private Boolean notificarOrden;
    @JoinColumn(name = "id_arrendatario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Arrendatarios idArrendatario;
    @Column(name = "orden_pagada")
    private Boolean ordenPagada;
    @Column(name = "monto_pagado")
    private BigDecimal montoPagado;
    @Column(name = "mes_letra")
    private String mesLetra;
    @Column(name = "diario_cobrar")
    private Boolean diarioCobrar;
    @Column(name = "id_diario_cobrar")
    private BigInteger idDiarioCobrar;
    @Column(name = "diario_pagar")
    private Boolean diarioPagar;
    @Column(name = "id_diario_pagar")
    private BigInteger idDiarioPagar;
    @Column(name = "id_solicitud")
    private String idSolicitud;
//    @Column(name = "js_pago")
//    private String jsPago;
    @JoinColumn(name = "id_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Liquidacion idLiquidacion;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
//    @Column(name = "fecha_pago")
//    @Temporal(TemporalType.DATE)
//    private Date fechaPago;

    public OrdenesEmitidas() {
        this.estado = Boolean.TRUE;
        this.fechaEmision = new Date();
        this.notificarOrden = Boolean.TRUE;
        this.ordenPagada = Boolean.FALSE;
        this.montoPagado = BigDecimal.ZERO;
        this.diarioCobrar = Boolean.FALSE;
        this.diarioPagar = Boolean.FALSE;
    }

    public OrdenesEmitidas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getMes() {
        return mes;
    }

    public void setMes(BigInteger mes) {
        this.mes = mes;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public Arrendamiento getIdArrendamiento() {
        return idArrendamiento;
    }

    public void setIdArrendamiento(Arrendamiento idArrendamiento) {
        this.idArrendamiento = idArrendamiento;
    }

    public BigDecimal getMontoPagar() {
        return montoPagar;
    }

    public void setMontoPagar(BigDecimal montoPagar) {
        this.montoPagar = montoPagar;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Boolean getNotificarOrden() {
        return notificarOrden;
    }

    public void setNotificarOrden(Boolean notificarOrden) {
        this.notificarOrden = notificarOrden;
    }

    public Arrendatarios getIdArrendatario() {
        return idArrendatario;
    }

    public void setIdArrendatario(Arrendatarios idArrendatario) {
        this.idArrendatario = idArrendatario;
    }

    public Boolean getOrdenPagada() {
        return ordenPagada;
    }

    public void setOrdenPagada(Boolean ordenPagada) {
        this.ordenPagada = ordenPagada;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMesLetra() {
        return mesLetra;
    }

    public void setMesLetra(String mesLetra) {
        this.mesLetra = mesLetra;
    }

    public Boolean getDiarioCobrar() {
        return diarioCobrar;
    }

    public void setDiarioCobrar(Boolean diarioCobrar) {
        this.diarioCobrar = diarioCobrar;
    }

    public Boolean getDiarioPagar() {
        return diarioPagar;
    }

    public void setDiarioPagar(Boolean diarioPagar) {
        this.diarioPagar = diarioPagar;
    }

    public BigInteger getIdDiarioCobrar() {
        return idDiarioCobrar;
    }

    public void setIdDiarioCobrar(BigInteger idDiarioCobrar) {
        this.idDiarioCobrar = idDiarioCobrar;
    }

    public BigInteger getIdDiarioPagar() {
        return idDiarioPagar;
    }

    public void setIdDiarioPagar(BigInteger idDiarioPagar) {
        this.idDiarioPagar = idDiarioPagar;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Liquidacion getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Liquidacion idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
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
        if (!(object instanceof OrdenesEmitidas)) {
            return false;
        }
        OrdenesEmitidas other = (OrdenesEmitidas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.arrendamiento.entities.OrdenesEmitidas[ id=" + id + " ]";
    }

}
