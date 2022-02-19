/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "inventario_registro", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventarioRegistro.findAll", query = "SELECT i FROM InventarioRegistro i"),
    @NamedQuery(name = "InventarioRegistro.findById", query = "SELECT i FROM InventarioRegistro i WHERE i.id = :id"),
    @NamedQuery(name = "InventarioRegistro.findByOficio", query = "SELECT i FROM InventarioRegistro i WHERE i.oficio = :oficio"),

    @NamedQuery(name = "InventarioRegistro.findRegistroInvByNumInv", query = "SELECT i FROM InventarioRegistro i JOIN  i.movimiento m WHERE i.movimiento = ?1"), // erwin
    @NamedQuery(name = "InventarioRegistro.findIdRegistroInvByNumInv", query = "SELECT i.id FROM InventarioRegistro i JOIN  i.movimiento m WHERE i.movimiento = ?1"), // erwin
    @NamedQuery(name = "InventarioRegistro.findByObservacion", query = "SELECT i FROM InventarioRegistro i WHERE i.observacion = :observacion"),
    @NamedQuery(name = "InventarioRegistro.findByEstado", query = "SELECT i FROM InventarioRegistro i WHERE i.estado = :estado")})
public class InventarioRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "oficio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Oficio oficio;
    @JoinColumn(name = "requisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private OrdenRequisicion requisicion;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Inventario movimiento;
    @JoinColumn(name = "movimiento_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesMovimiento movimientoBien;
    @JoinColumn(name = "adquisiciones", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Adquisiciones adquisiciones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventarioRegistro")
    private List<Factura> facturaList;

    public InventarioRegistro() {
    }

    public InventarioRegistro(Long id) {
        this.id = id;
    }

    public InventarioRegistro(Long id, Oficio oficio) {
        this.id = id;
        this.oficio = oficio;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Inventario getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Inventario movimiento) {
        this.movimiento = movimiento;
    }

    public Oficio getOficio() {
        return oficio;
    }

    public void setOficio(Oficio oficio) {
        this.oficio = oficio;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    public OrdenRequisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(OrdenRequisicion requisicion) {
        this.requisicion = requisicion;
    }

    public BienesMovimiento getMovimientoBien() {
        return movimientoBien;
    }

    public void setMovimientoBien(BienesMovimiento movimientoBien) {
        this.movimientoBien = movimientoBien;
    }

    public Adquisiciones getAdquisiciones() {
        return adquisiciones;
    }

    public void setAdquisiciones(Adquisiciones adquisiciones) {
        this.adquisiciones = adquisiciones;
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
        if (!(object instanceof InventarioRegistro)) {
            return false;
        }
        InventarioRegistro other = (InventarioRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.InventarioRegistro[ id=" + id + " ]";
    }

}
