/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.origami.sigef.common.util.Utils;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "det_rubro_mejoras", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetRubroMejoras.findAll", query = "SELECT d FROM DetRubroMejoras d"),
    @NamedQuery(name = "DetRubroMejoras.findById", query = "SELECT d FROM DetRubroMejoras d WHERE d.id = :id"),
    @NamedQuery(name = "DetRubroMejoras.findByRubroMejora", query = "SELECT d FROM DetRubroMejoras d WHERE d.rubroMejora = :rubroMejora"),
    @NamedQuery(name = "DetRubroMejoras.findByValor", query = "SELECT d FROM DetRubroMejoras d WHERE d.valor = :valor"),
    @NamedQuery(name = "DetRubroMejoras.findBySaldo", query = "SELECT d FROM DetRubroMejoras d WHERE d.saldo = :saldo"),
    @NamedQuery(name = "DetRubroMejoras.findByFechaIngreso", query = "SELECT d FROM DetRubroMejoras d WHERE d.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "DetRubroMejoras.findByFechaPago", query = "SELECT d FROM DetRubroMejoras d WHERE d.fechaPago = :fechaPago"),
    @NamedQuery(name = "DetRubroMejoras.findByEstado", query = "SELECT d FROM DetRubroMejoras d WHERE d.estado = :estado"),
    @NamedQuery(name = "DetRubroMejoras.findByValorTest", query = "SELECT d FROM DetRubroMejoras d WHERE d.valorTest = :valorTest")})
public class DetRubroMejoras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
 
    @JoinColumn(name = "rubro_mejora", referencedColumnName = "id")
    @ManyToOne
    private FinaRenDetLiquidacion rubroMejora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "valor_test")
    private BigDecimal valorTest;
    @JoinColumn(name = "ubicacion_obra", referencedColumnName = "id")
    @ManyToOne
    private ValoresObraUbicacion ubicacionObra;

    public DetRubroMejoras() {
    }

    public DetRubroMejoras(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenDetLiquidacion getRubroMejora() {
        return rubroMejora;
    }

    public void setRubroMejora(FinaRenDetLiquidacion rubroMejora) {
        this.rubroMejora = rubroMejora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getValorTest() {
        return valorTest;
    }

    public void setValorTest(BigDecimal valorTest) {
        this.valorTest = valorTest;
    }

    public ValoresObraUbicacion getUbicacionObra() {
        return ubicacionObra;
    }

    public void setUbicacionObra(ValoresObraUbicacion ubicacionObra) {
        this.ubicacionObra = ubicacionObra;
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
        if (!(object instanceof DetRubroMejoras)) {
            return false;
        }
        DetRubroMejoras other = (DetRubroMejoras) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DetRubroMejoras[ id=" + id + " ]";
    }
    
}
