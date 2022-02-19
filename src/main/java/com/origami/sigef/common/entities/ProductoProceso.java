/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
 * @author ORIGAMI2
 */
@Entity
@Table(name = "producto_proceso")
@NamedQueries({
    @NamedQuery(name = "ProductoProceso.findAll", query = "SELECT p FROM ProductoProceso p"),
    @NamedQuery(name = "ProductoProceso.findById", query = "SELECT p FROM ProductoProceso p WHERE p.id = :id"),
    @NamedQuery(name = "ProductoProceso.findByNumetoTramite", query = "SELECT p FROM ProductoProceso p WHERE p.numetoTramite = :numetoTramite"),
    @NamedQuery(name = "ProductoProceso.findByFechaProceso", query = "SELECT p FROM ProductoProceso p WHERE p.fechaProceso = :fechaProceso")})
public class ProductoProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numeto_tramite")
    private BigInteger numetoTramite;
    @Column(name = "fecha_proceso")
    @Temporal(TemporalType.TIME)
    private Date fechaProceso;
    @Column(name = "usuario_solicita")
    private String usuarioSolicita;
    @JoinColumn(name = "estado_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoProceso;
    @JoinColumn(name = "producto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Producto producto;
    @Column(name = "monto_disponible")
    private BigDecimal montoDisponible;
    @Column(name = "solicitado")
    private BigDecimal solicitado;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem estado;

    public ProductoProceso() {
    }

    public ProductoProceso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioSolicita() {
        return usuarioSolicita;
    }

    public void setUsuarioSolicita(String usuarioSolicita) {
        this.usuarioSolicita = usuarioSolicita;
    }

    public BigInteger getNumetoTramite() {
        return numetoTramite;
    }

    public void setNumetoTramite(BigInteger numetoTramite) {
        this.numetoTramite = numetoTramite;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public CatalogoItem getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(CatalogoItem estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getMontoDisponible() {
        return montoDisponible;
    }

    public void setMontoDisponible(BigDecimal montoDisponible) {
        this.montoDisponible = montoDisponible;
    }

    public BigDecimal getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(BigDecimal solicitado) {
        this.solicitado = solicitado;
    }

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
        this.estado = estado;
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
        if (!(object instanceof ProductoProceso)) {
            return false;
        }
        ProductoProceso other = (ProductoProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ProductoProceso[ id=" + id + " ]";
    }

}
