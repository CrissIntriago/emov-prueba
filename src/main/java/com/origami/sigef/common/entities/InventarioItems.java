/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "inventario_items", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventarioItems.findAll", query = "SELECT i FROM InventarioItems i")
    ,
    @NamedQuery(name = "InventarioItems.findById", query = "SELECT i FROM InventarioItems i WHERE i.id = :id")
    ,

    @NamedQuery(name = "InventarioItems.findItemByNombreEgreso", query = "SELECT i FROM InventarioItems i JOIN i.detalleItem d WHERE i.inventario = ?1")
    ,

    @NamedQuery(name = "InventarioItems.findByEstante", query = "SELECT i FROM InventarioItems i WHERE i.estante = :estante")
    ,
    @NamedQuery(name = "InventarioItems.findByFila", query = "SELECT i FROM InventarioItems i WHERE i.fila = :fila")
    ,
    @NamedQuery(name = "InventarioItems.findByColumna", query = "SELECT i FROM InventarioItems i WHERE i.columna = :columna")
    ,
    @NamedQuery(name = "InventarioItems.findByCajon", query = "SELECT i FROM InventarioItems i WHERE i.cajon = :cajon")
    ,
    @NamedQuery(name = "InventarioItems.findByCuadrante", query = "SELECT i FROM InventarioItems i WHERE i.cuadrante = :cuadrante")
    ,
    @NamedQuery(name = "InventarioItems.findByCantidad", query = "SELECT i FROM InventarioItems i WHERE i.cantidad = :cantidad")
    ,
    @NamedQuery(name = "InventarioItems.findByPrecio", query = "SELECT i FROM InventarioItems i WHERE i.precio = :precio")
    ,
    @NamedQuery(name = "InventarioItems.findByTotal", query = "SELECT i FROM InventarioItems i WHERE i.total = :total")
    ,
    @NamedQuery(name = "InventarioItems.getItemByInventarioItems", query = "SELECT i FROM InventarioItems i JOIN i.detalleItem d WHERE i.inventario = ?1")
    ,
    @NamedQuery(name = "InventarioItems.findByFechaAdq", query = "SELECT i FROM InventarioItems i WHERE i.fechaAdq = :fechaAdq")})
public class InventarioItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "estante")
    private Short estante;
    @Column(name = "fila")
    private Short fila;
    @Column(name = "columna")
    private Short columna;
    @Column(name = "cajon")
    private Short cajon;
    @Column(name = "cuadrante")
    private Short cuadrante;

    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio", nullable = false, precision = 19, scale = 2)
    private BigDecimal precio;
    @Column(name = "total", nullable = false, precision = 19, scale = 2)
    private BigDecimal total;
    @Column(name = "fecha_adq")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAdq;

    @JoinColumn(name = "detalle_item", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleItem detalleItem;

    @JoinColumn(name = "inventario", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Inventario inventario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventarioItems")
    private List<DetallaKardex> detallaKardexs;

    @Column(name = "iva", nullable = false, precision = 19, scale = 2)
    private BigDecimal iva;

    public InventarioItems() {
    }

    public InventarioItems(Long id) {
        this.id = id;
    }

    public InventarioItems(Long id, Integer cantidad, BigDecimal precio, BigDecimal total) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getEstante() {
        return estante;
    }

    public void setEstante(Short estante) {
        this.estante = estante;
    }

    public Short getFila() {
        return fila;
    }

    public void setFila(Short fila) {
        this.fila = fila;
    }

    public Short getColumna() {
        return columna;
    }

    public void setColumna(Short columna) {
        this.columna = columna;
    }

    public Short getCajon() {
        return cajon;
    }

    public void setCajon(Short cajon) {
        this.cajon = cajon;
    }

    public Short getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(Short cuadrante) {
        this.cuadrante = cuadrante;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getFechaAdq() {
        return fechaAdq;
    }

    public void setFechaAdq(Date fechaAdq) {
        this.fechaAdq = fechaAdq;
    }

    public DetalleItem getDetalleItem() {
        return detalleItem;
    }

    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<DetallaKardex> getDetallaKardexs() {
        return detallaKardexs;
    }

    public void setDetallaKardexs(List<DetallaKardex> detallaKardexs) {
        this.detallaKardexs = detallaKardexs;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
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
        if (!(object instanceof InventarioItems)) {
            return false;
        }
        InventarioItems other = (InventarioItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.InventarioItems[ id=" + id + " ]";
    }

}
