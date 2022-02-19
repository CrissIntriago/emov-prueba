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

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "detalle_presupuesto", schema = "administracion")
@NamedQueries({
    @NamedQuery(name = "DetallePresupuesto.findAll", query = "SELECT d FROM DetallePresupuesto d")
    , @NamedQuery(name = "DetallePresupuesto.findById", query = "SELECT d FROM DetallePresupuesto d WHERE d.id = :id")
    , @NamedQuery(name = "DetallePresupuesto.findByDetalleReserva", query = "SELECT d FROM DetallePresupuesto d WHERE d.detalleReserva = :detalleReserva")
    , @NamedQuery(name = "DetallePresupuesto.findByValor", query = "SELECT d FROM DetallePresupuesto d WHERE d.valor = :valor")})
public class DetallePresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "orden_compra", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudOrdenCompra ordenCompra;
    @JoinColumn(name = "detalle_reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleSolicitudCompromiso detalleReserva;

    public DetallePresupuesto() {
    }

    public DetallePresupuesto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetalleSolicitudCompromiso getDetalleReserva() {
        return detalleReserva;
    }

    public void setDetalleReserva(DetalleSolicitudCompromiso detalleReserva) {
        this.detalleReserva = detalleReserva;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public SolicitudOrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(SolicitudOrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
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
        if (!(object instanceof DetallePresupuesto)) {
            return false;
        }
        DetallePresupuesto other = (DetallePresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetallePresupuesto[ id=" + id + " ]";
    }

}
