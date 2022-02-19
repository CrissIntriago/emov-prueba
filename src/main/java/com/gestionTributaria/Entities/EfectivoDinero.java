/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "efectivo_dinero", schema = "tesoreria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EfectivoDinero.findAll", query = "SELECT e FROM EfectivoDinero e"),
    @NamedQuery(name = "EfectivoDinero.findById", query = "SELECT e FROM EfectivoDinero e WHERE e.id = :id"),
    @NamedQuery(name = "EfectivoDinero.findByUsuario", query = "SELECT e FROM EfectivoDinero e WHERE e.usuario = :usuario"),
    @NamedQuery(name = "EfectivoDinero.findByFechaRegistro", query = "SELECT e FROM EfectivoDinero e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EfectivoDinero.findByBillete100", query = "SELECT e FROM EfectivoDinero e WHERE e.billete100 = :billete100"),
    @NamedQuery(name = "EfectivoDinero.findByBillete50", query = "SELECT e FROM EfectivoDinero e WHERE e.billete50 = :billete50"),
    @NamedQuery(name = "EfectivoDinero.findByBillete20", query = "SELECT e FROM EfectivoDinero e WHERE e.billete20 = :billete20"),
    @NamedQuery(name = "EfectivoDinero.findByBillete10", query = "SELECT e FROM EfectivoDinero e WHERE e.billete10 = :billete10"),
    @NamedQuery(name = "EfectivoDinero.findByBillete5", query = "SELECT e FROM EfectivoDinero e WHERE e.billete5 = :billete5"),
    @NamedQuery(name = "EfectivoDinero.findByBilleteMoneda1", query = "SELECT e FROM EfectivoDinero e WHERE e.billeteMoneda1 = :billeteMoneda1"),
    @NamedQuery(name = "EfectivoDinero.findByMoneda50", query = "SELECT e FROM EfectivoDinero e WHERE e.moneda50 = :moneda50"),
    @NamedQuery(name = "EfectivoDinero.findByMoneda25", query = "SELECT e FROM EfectivoDinero e WHERE e.moneda25 = :moneda25"),
    @NamedQuery(name = "EfectivoDinero.findByMoneda10", query = "SELECT e FROM EfectivoDinero e WHERE e.moneda10 = :moneda10"),
    @NamedQuery(name = "EfectivoDinero.findByMoneda5", query = "SELECT e FROM EfectivoDinero e WHERE e.moneda5 = :moneda5"),
    @NamedQuery(name = "EfectivoDinero.findByMoneda1", query = "SELECT e FROM EfectivoDinero e WHERE e.moneda1 = :moneda1"),
    @NamedQuery(name = "EfectivoDinero.findByObservaciones", query = "SELECT e FROM EfectivoDinero e WHERE e.observaciones = :observaciones")})
public class EfectivoDinero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "billete_100")
    private BigInteger billete100;
    @Column(name = "billete_50")
    private BigInteger billete50;
    @Column(name = "billete_20")
    private BigInteger billete20;
    @Column(name = "billete_10")
    private BigInteger billete10;
    @Column(name = "billete_5")
    private BigInteger billete5;
    @Column(name = "billete_moneda_1")
    private BigInteger billeteMoneda1;
    @Column(name = "moneda_50")
    private BigInteger moneda50;
    @Column(name = "moneda_25")
    private BigInteger moneda25;
    @Column(name = "moneda_10")
    private BigInteger moneda10;
    @Column(name = "moneda_5")
    private BigInteger moneda5;
    @Column(name = "moneda_dolar")
    private BigInteger monedaDolar;
    @Column(name = "moneda_1")
    private BigInteger moneda1;
    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "caja", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cajero caja;

    public EfectivoDinero() {
        this.billeteMoneda1 = BigInteger.ZERO;
        this.billete5 = BigInteger.ZERO;
        this.billete10 = BigInteger.ZERO;
        this.billete20 = BigInteger.ZERO;
        this.billete50 = BigInteger.ZERO;
        this.billete100 = BigInteger.ZERO;
        this.moneda1 = BigInteger.ZERO;
        this.moneda5 = BigInteger.ZERO;
        this.moneda10 = BigInteger.ZERO;
        this.moneda25 = BigInteger.ZERO;
        this.moneda50 = BigInteger.ZERO;
        this.monedaDolar = BigInteger.ZERO;
    }

    public EfectivoDinero(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigInteger getBillete100() {
        return billete100;
    }

    public void setBillete100(BigInteger billete100) {
        this.billete100 = billete100;
    }

    public BigInteger getBillete50() {
        return billete50;
    }

    public void setBillete50(BigInteger billete50) {
        this.billete50 = billete50;
    }

    public BigInteger getBillete20() {
        return billete20;
    }

    public void setBillete20(BigInteger billete20) {
        this.billete20 = billete20;
    }

    public BigInteger getBillete10() {
        return billete10;
    }

    public void setBillete10(BigInteger billete10) {
        this.billete10 = billete10;
    }

    public BigInteger getBillete5() {
        return billete5;
    }

    public void setBillete5(BigInteger billete5) {
        this.billete5 = billete5;
    }

    public BigInteger getBilleteMoneda1() {
        return billeteMoneda1;
    }

    public void setBilleteMoneda1(BigInteger billeteMoneda1) {
        this.billeteMoneda1 = billeteMoneda1;
    }

    public BigInteger getMoneda50() {
        return moneda50;
    }

    public void setMoneda50(BigInteger moneda50) {
        this.moneda50 = moneda50;
    }

    public BigInteger getMoneda25() {
        return moneda25;
    }

    public void setMoneda25(BigInteger moneda25) {
        this.moneda25 = moneda25;
    }

    public BigInteger getMoneda10() {
        return moneda10;
    }

    public void setMoneda10(BigInteger moneda10) {
        this.moneda10 = moneda10;
    }

    public BigInteger getMoneda5() {
        return moneda5;
    }

    public void setMoneda5(BigInteger moneda5) {
        this.moneda5 = moneda5;
    }

    public BigInteger getMoneda1() {
        return moneda1;
    }

    public void setMoneda1(BigInteger moneda1) {
        this.moneda1 = moneda1;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigInteger getMonedaDolar() {
        return monedaDolar;
    }

    public void setMonedaDolar(BigInteger monedaDolar) {
        this.monedaDolar = monedaDolar;
    }

    public Cajero getCaja() {
        return caja;
    }

    public void setCaja(Cajero caja) {
        this.caja = caja;
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
        if (!(object instanceof EfectivoDinero)) {
            return false;
        }
        EfectivoDinero other = (EfectivoDinero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EfectivoDinero{" + "id=" + id + ", usuario=" + usuario + ", fechaRegistro=" + fechaRegistro + ", billete100=" + billete100 + ", billete50=" + billete50 + ", billete20=" + billete20 + ", billete10=" + billete10 + ", billete5=" + billete5 + ", billeteMoneda1=" + billeteMoneda1 + ", moneda50=" + moneda50 + ", moneda25=" + moneda25 + ", moneda10=" + moneda10 + ", moneda5=" + moneda5 + ", moneda1=" + moneda1 + ", observaciones=" + observaciones + '}';
    }

}
