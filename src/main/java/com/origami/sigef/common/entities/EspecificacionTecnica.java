/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "especificacion_tecnica", schema = "administracion")
@NamedQueries({
    @NamedQuery(name = "EspecificacionTecnica.findAll", query = "SELECT e FROM EspecificacionTecnica e")
    ,
    @NamedQuery(name = "EspecificacionTecnica.findById", query = "SELECT e FROM EspecificacionTecnica e WHERE e.id = :id")
    ,
    @NamedQuery(name = "EspecificacionTecnica.findByItem", query = "SELECT e FROM EspecificacionTecnica e WHERE e.item = :item")
    ,
    @NamedQuery(name = "EspecificacionTecnica.findByDetalle", query = "SELECT e FROM EspecificacionTecnica e WHERE e.detalle = :detalle")
    ,
    @NamedQuery(name = "EspecificacionTecnica.findByCantidad", query = "SELECT e FROM EspecificacionTecnica e WHERE e.cantidad = :cantidad")
    ,
    @NamedQuery(name = "EspecificacionTecnica.findByCaracteristicas", query = "SELECT e FROM EspecificacionTecnica e WHERE e.caracteristicas = :caracteristicas")})
public class EspecificacionTecnica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "item")
    private Short item;
    @Size(max = 2147483647)
    @Column(name = "detalle")
    private String detalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;
    @Column(name = "precio_total")
    private BigDecimal precioTotal;
    @Size(max = 2147483647)
    @Column(name = "caracteristicas")
    private String caracteristicas;
    @Size(max = 2147483647)
    @Column(name = "codigo_orden_compra")
    private String codigoOrdenCompra;
    @JoinColumn(name = "orden_compra", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudOrdenCompra ordenCompra;
    @JoinColumn(name = "unidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem unidad;
    @JoinColumn(name = "bien_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem bienItem;
    @JoinColumn(name = "inventario_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleItem inventarioItem;

    public EspecificacionTecnica() {
    }

    public EspecificacionTecnica(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getCodigoOrdenCompra() {
        return codigoOrdenCompra;
    }

    public void setCodigoOrdenCompra(String codigoOrdenCompra) {
        this.codigoOrdenCompra = codigoOrdenCompra;
    }

    public Short getItem() {
        return item;
    }

    public void setItem(Short item) {
        this.item = item;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public CatalogoItem getUnidad() {
        return unidad;
    }

    public void setUnidad(CatalogoItem unidad) {
        this.unidad = unidad;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public SolicitudOrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(SolicitudOrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public BienesItem getBienItem() {
        return bienItem;
    }

    public void setBienItem(BienesItem bienItem) {
        this.bienItem = bienItem;
    }

    public DetalleItem getInventarioItem() {
        return inventarioItem;
    }

    public void setInventarioItem(DetalleItem inventarioItem) {
        this.inventarioItem = inventarioItem;
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
        if (!(object instanceof EspecificacionTecnica)) {
            return false;
        }
        EspecificacionTecnica other = (EspecificacionTecnica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EspecificacionTecnica[ id=" + id + " ]";
    }

}
