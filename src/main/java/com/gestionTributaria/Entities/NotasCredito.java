/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "notas_credito", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasCredito.findAll", query = "SELECT n FROM NotasCredito n"),
    @NamedQuery(name = "NotasCredito.findById", query = "SELECT n FROM NotasCredito n WHERE n.id = :id"),
    @NamedQuery(name = "NotasCredito.findByCodigo", query = "SELECT n FROM NotasCredito n WHERE n.codigo = :codigo"),
    @NamedQuery(name = "NotasCredito.findByContribuyente", query = "SELECT n FROM NotasCredito n WHERE n.contribuyente = :contribuyente"),
    @NamedQuery(name = "NotasCredito.findByEstado", query = "SELECT n FROM NotasCredito n WHERE n.estado = :estado"),
    @NamedQuery(name = "NotasCredito.findByFechaIngreso", query = "SELECT n FROM NotasCredito n WHERE n.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "NotasCredito.findByLiquidacion", query = "SELECT n FROM NotasCredito n WHERE n.liquidacion = :liquidacion"),
    @NamedQuery(name = "NotasCredito.findByObservacion", query = "SELECT n FROM NotasCredito n WHERE n.observacion = :observacion"),
    @NamedQuery(name = "NotasCredito.findBySaldo", query = "SELECT n FROM NotasCredito n WHERE n.saldo = :saldo"),
    @NamedQuery(name = "NotasCredito.findByTipoNota", query = "SELECT n FROM NotasCredito n WHERE n.tipoNota = :tipoNota"),
    @NamedQuery(name = "NotasCredito.findByUsuarioIngreso", query = "SELECT n FROM NotasCredito n WHERE n.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "NotasCredito.findByValor", query = "SELECT n FROM NotasCredito n WHERE n.valor = :valor"),
    @NamedQuery(name = "NotasCredito.findByValorPagado", query = "SELECT n FROM NotasCredito n WHERE n.valorPagado = :valorPagado")})
public class NotasCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 20)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "resolucion")
    private String resolucion;
    @JoinColumn(name = "contribuyente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente contribuyente;
    @JoinColumn(name = "nota_credito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RenFactura notaCredito;
    @Column(name = "estado")
    private Short estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "tipo_nota")
    private BigInteger tipoNota;
    @Size(max = 25)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "valor_pagado")
    private BigDecimal valorPagado;
    @OneToMany(mappedBy = "idNota")
    private List<NotaDetalle> notaDetalleList;
    @OneToMany(mappedBy = "notaCredito")
    private List<NotasCreditoHistorico> notasCreditoHistoricoList;

    @Transient
    private BigDecimal valorDebitado;

    public NotasCredito() {
    }

    public NotasCredito(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Cliente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigInteger getTipoNota() {
        return tipoNota;
    }

    public void setTipoNota(BigInteger tipoNota) {
        this.tipoNota = tipoNota;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDebitado() {
        return valorDebitado;
    }

    public void setValorDebitado(BigDecimal valorDebitado) {
        this.valorDebitado = valorDebitado;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public RenFactura getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(RenFactura notaCredito) {
        this.notaCredito = notaCredito;
    }

    @XmlTransient
    public List<NotaDetalle> getNotaDetalleList() {
        return notaDetalleList;
    }

    public void setNotaDetalleList(List<NotaDetalle> notaDetalleList) {
        this.notaDetalleList = notaDetalleList;
    }

    @XmlTransient
    public List<NotasCreditoHistorico> getNotasCreditoHistoricoList() {
        return notasCreditoHistoricoList;
    }

    public void setNotasCreditoHistoricoList(List<NotasCreditoHistorico> notasCreditoHistoricoList) {
        this.notasCreditoHistoricoList = notasCreditoHistoricoList;
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
        if (!(object instanceof NotasCredito)) {
            return false;
        }
        NotasCredito other = (NotasCredito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.NotasCredito[ id=" + id + " ]";
    }

}
