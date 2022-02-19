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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "detalla_kardex", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallaKardex.findAll", query = "SELECT d FROM DetallaKardex d"),
    @NamedQuery(name = "DetallaKardex.findById", query = "SELECT d FROM DetallaKardex d WHERE d.id = :id"),
    @NamedQuery(name = "DetallaKardex.findByCantIngreso", query = "SELECT d FROM DetallaKardex d WHERE d.cantIngreso = :cantIngreso"),
    @NamedQuery(name = "DetallaKardex.findByPrecioIngreso", query = "SELECT d FROM DetallaKardex d WHERE d.precioIngreso = :precioIngreso"),
    @NamedQuery(name = "DetallaKardex.findByTotalIngreso", query = "SELECT d FROM DetallaKardex d WHERE d.totalIngreso = :totalIngreso"),
    @NamedQuery(name = "DetallaKardex.findByCantEgreso", query = "SELECT d FROM DetallaKardex d WHERE d.cantEgreso = :cantEgreso"),
    @NamedQuery(name = "DetallaKardex.findByPrecioEgreso", query = "SELECT d FROM DetallaKardex d WHERE d.precioEgreso = :precioEgreso"),
    @NamedQuery(name = "DetallaKardex.findByTotalEgreso", query = "SELECT d FROM DetallaKardex d WHERE d.totalEgreso = :totalEgreso"),
    @NamedQuery(name = "DetallaKardex.findByCantExistencia", query = "SELECT d FROM DetallaKardex d WHERE d.cantExistencia = :cantExistencia"),
    @NamedQuery(name = "DetallaKardex.findByPrecioExistencia", query = "SELECT d FROM DetallaKardex d WHERE d.precioExistencia = :precioExistencia"),
    @NamedQuery(name = "DetallaKardex.findByTotalExistencia", query = "SELECT d FROM DetallaKardex d WHERE d.totalExistencia = :totalExistencia")})
public class DetallaKardex implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cant_ingreso")
    private Integer cantIngreso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio_ingreso")
    private BigDecimal precioIngreso;
    @Column(name = "total_ingreso")
    private BigDecimal totalIngreso;
    @Column(name = "cant_egreso")
    private Integer cantEgreso;
    @Column(name = "precio_egreso")
    private BigDecimal precioEgreso;
    @Column(name = "total_egreso")
    private BigDecimal totalEgreso;
    @Column(name = "cant_existencia")
    private Integer cantExistencia;
    @Column(name = "precio_existencia")
    private BigDecimal precioExistencia;
    @Column(name = "total_existencia")
    private BigDecimal totalExistencia;

    @JoinColumn(name = "kardex", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Kardex kardex;

    @JoinColumn(name = "inventario_items", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private InventarioItems inventarioItems;

    public DetallaKardex() {
    }

    public DetallaKardex(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantIngreso() {
        return cantIngreso;
    }

    public void setCantIngreso(Integer cantIngreso) {
        this.cantIngreso = cantIngreso;
    }

    public BigDecimal getPrecioIngreso() {
        return precioIngreso;
    }

    public void setPrecioIngreso(BigDecimal precioIngreso) {
        this.precioIngreso = precioIngreso;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(BigDecimal totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public Integer getCantEgreso() {
        return cantEgreso;
    }

    public void setCantEgreso(Integer cantEgreso) {
        this.cantEgreso = cantEgreso;
    }

    public BigDecimal getPrecioEgreso() {
        return precioEgreso;
    }

    public void setPrecioEgreso(BigDecimal precioEgreso) {
        this.precioEgreso = precioEgreso;
    }

    public BigDecimal getTotalEgreso() {
        return totalEgreso;
    }

    public void setTotalEgreso(BigDecimal totalEgreso) {
        this.totalEgreso = totalEgreso;
    }

    public Integer getCantExistencia() {
        return cantExistencia;
    }

    public void setCantExistencia(Integer cantExistencia) {
        this.cantExistencia = cantExistencia;
    }

    public BigDecimal getPrecioExistencia() {
        return precioExistencia;
    }

    public void setPrecioExistencia(BigDecimal precioExistencia) {
        this.precioExistencia = precioExistencia;
    }

    public BigDecimal getTotalExistencia() {
        return totalExistencia;
    }

    public void setTotalExistencia(BigDecimal totalExistencia) {
        this.totalExistencia = totalExistencia;
    }

    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }

    public InventarioItems getInventarioItems() {
        return inventarioItems;
    }

    public void setInventarioItems(InventarioItems inventarioItems) {
        this.inventarioItems = inventarioItems;
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
        if (!(object instanceof DetallaKardex)) {
            return false;
        }
        DetallaKardex other = (DetallaKardex) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.curso2.inventario.entitiesBorrar.DetallaKardex[ id=" + id + " ]";
    }

}
