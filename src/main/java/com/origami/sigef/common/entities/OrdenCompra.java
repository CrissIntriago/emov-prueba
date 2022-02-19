/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
@Table(name = "orden_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenCompra.findAll", query = "SELECT o FROM OrdenCompra o"),
    @NamedQuery(name = "OrdenCompra.findById", query = "SELECT o FROM OrdenCompra o WHERE o.id = :id"),
    @NamedQuery(name = "OrdenCompra.findByNumeroOrden", query = "SELECT o FROM OrdenCompra o WHERE o.numeroOrden = :numeroOrden"),
    @NamedQuery(name = "OrdenCompra.findByFechaOrden", query = "SELECT o FROM OrdenCompra o WHERE o.fechaOrden = :fechaOrden"),
    @NamedQuery(name = "OrdenCompra.findByProveedor", query = "SELECT o FROM OrdenCompra o WHERE o.proveedor = :proveedor"),
    @NamedQuery(name = "OrdenCompra.findByCondicionPago", query = "SELECT o FROM OrdenCompra o WHERE o.condicionPago = :condicionPago"),
    @NamedQuery(name = "OrdenCompra.findByTipoArchivo", query = "SELECT o FROM OrdenCompra o WHERE o.tipoArchivo = :tipoArchivo"),
    @NamedQuery(name = "OrdenCompra.findByTamanioArchivo", query = "SELECT o FROM OrdenCompra o WHERE o.tamanioArchivo = :tamanioArchivo"),
    @NamedQuery(name = "OrdenCompra.findByEstado", query = "SELECT o FROM OrdenCompra o WHERE o.estado = :estado")})
public class OrdenCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "numero_orden")
    private String numeroOrden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_orden")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOrden;

    @Column(name = "fecha_aceptacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAceptacion;

    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;

    @Size(max = 255)
    @Column(name = "condicion_pago")
    private String condicionPago;
    @Size(max = 255)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Size(max = 255)
    @Column(name = "tamanio_archivo")
    private String tamanioArchivo;
    @Column(name = "estado")
    private Boolean estado;

    @JoinColumn(name = "tipo_orden", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoOrden;


    public OrdenCompra() {
    }

    public OrdenCompra(Long id) {
        this.id = id;
    }

    public OrdenCompra(Long id, String numeroOrden, Date fechaOrden) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.fechaOrden = fechaOrden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Date getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        this.condicionPago = condicionPago;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(String tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(Date fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }

    public CatalogoItem getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(CatalogoItem tipoOrden) {
        this.tipoOrden = tipoOrden;
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
        if (!(object instanceof OrdenCompra)) {
            return false;
        }
        OrdenCompra other = (OrdenCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.OrdenCompra[ id=" + id + " ]";
    }

}
