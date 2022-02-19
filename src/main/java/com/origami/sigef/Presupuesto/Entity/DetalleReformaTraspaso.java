/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.Producto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "detalle_reforma_traspaso", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleReformaTraspaso.findAll", query = "SELECT d FROM DetalleReformaTraspaso d"),
    @NamedQuery(name = "DetalleReformaTraspaso.findById", query = "SELECT d FROM DetalleReformaTraspaso d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleReformaTraspaso.findByProducto", query = "SELECT d FROM DetalleReformaTraspaso d WHERE d.producto = :producto"),
    @NamedQuery(name = "DetalleReformaTraspaso.findByMontoProducto", query = "SELECT d FROM DetalleReformaTraspaso d WHERE d.montoProducto = :montoProducto"),
    @NamedQuery(name = "DetalleReformaTraspaso.findByMontoReformada", query = "SELECT d FROM DetalleReformaTraspaso d WHERE d.montoReformada = :montoReformada")})
public class DetalleReformaTraspaso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "producto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Producto producto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_producto")
    private BigDecimal montoProducto;
    @Column(name = "reservas_aprobadas")
    private BigDecimal reservasAprobadas;
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    @Column(name = "traspaso_incremento")
    private BigDecimal traspasoIncremento;
    @Column(name = "traspaso_reduccion")
    private BigDecimal traspasoReduccion;
    @Column(name = "monto_reformada")
    private BigDecimal montoReformada;
    @Size(max = 10)
    @Column(name = "tipo_traspaso")
    private String tipoTraspaso;
    @JoinColumn(name = "reforma", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ReformaTraspaso reforma;

    public DetalleReformaTraspaso() {
        this.saldoDisponible=BigDecimal.ZERO;
        this.montoProducto=BigDecimal.ZERO;
        this.reservasAprobadas=BigDecimal.ZERO;
        this.traspasoIncremento=BigDecimal.ZERO;
        this.traspasoReduccion=BigDecimal.ZERO;
      

    }

    public DetalleReformaTraspaso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getMontoProducto() {
        return montoProducto;
    }

    public void setMontoProducto(BigDecimal montoProducto) {
        this.montoProducto = montoProducto;
    }

    public BigDecimal getReservasAprobadas() {
        return reservasAprobadas;
    }

    public void setReservasAprobadas(BigDecimal reservasAprobadas) {
        this.reservasAprobadas = reservasAprobadas;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public BigDecimal getTraspasoIncremento() {
        return traspasoIncremento;
    }

    public void setTraspasoIncremento(BigDecimal traspasoIncremento) {
        this.traspasoIncremento = traspasoIncremento;
    }

    public BigDecimal getTraspasoReduccion() {
        return traspasoReduccion;
    }

    public void setTraspasoReduccion(BigDecimal traspasoReduccion) {
        this.traspasoReduccion = traspasoReduccion;
    }

    public BigDecimal getMontoReformada() {
        return montoReformada;
    }

    public void setMontoReformada(BigDecimal montoReformada) {
        this.montoReformada = montoReformada;
    }

    public ReformaTraspaso getReforma() {
        return reforma;
    }

    public void setReforma(ReformaTraspaso reforma) {
        this.reforma = reforma;
    }

    public String getTipoTraspaso() {
        return tipoTraspaso;
    }

    public void setTipoTraspaso(String tipoTraspaso) {
        this.tipoTraspaso = tipoTraspaso;
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
        if (!(object instanceof DetalleReformaTraspaso)) {
            return false;
        }
        DetalleReformaTraspaso other = (DetalleReformaTraspaso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso[ id=" + id + " ]";
    }
    
}
