/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
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
 * @author Origami
 */
@Entity
@Table(name = "detalle_banco", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "DetalleBanco.findAll", query = "SELECT d FROM DetalleBanco d"),
    @NamedQuery(name = "DetalleBanco.findById", query = "SELECT d FROM DetalleBanco d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleBanco.findByEstadoCuenta", query = "SELECT d FROM DetalleBanco d WHERE d.estadoCuenta = :estadoCuenta"),
    @NamedQuery(name = "DetalleBanco.findByRegistro", query = "SELECT d FROM DetalleBanco d WHERE d.registro = :registro"),
    @NamedQuery(name = "DetalleBanco.findByTipoCuenta", query = "SELECT d FROM DetalleBanco d WHERE d.tipoCuenta = :tipoCuenta"),
    @NamedQuery(name = "DetalleBanco.findByCuentaBanco", query = "SELECT d FROM DetalleBanco d WHERE d.cuentaBanco = :cuentaBanco"),
    @NamedQuery(name = "DetalleBanco.findByCodigoList", query = "SELECT d FROM DetalleBanco d WHERE d.cuentaBanco = ?1"),
    @NamedQuery(name = "DetalleBanco.findListBancoByProveedor", query = "SELECT d FROM DetalleBanco d WHERE d.proveedor  = ?1 AND d.estado= true"),
    @NamedQuery(name = "DetalleBanco.findListBancoByServidor", query = "SELECT d FROM DetalleBanco d WHERE d.servidorPublico  = ?1 AND d.estado= true")})
public class DetalleBanco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado_cuenta")
    private Boolean estadoCuenta;
    @Size(max = 500)
    @Column(name = "registro")
    private String registro;
    @JoinColumn(name = "tipo_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoCuenta;
    @Column(name = "cuenta_banco")
    private String cuentaBanco;
//    @Column(name = "nombre_banco")
//    private BigInteger nombreBanco;
    @JoinColumn(name = "banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Banco banco;
    @JoinColumn(name = "servidor_publico", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidorPublico;

    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "cta_retenciones")
    private Boolean ctaRetenciones;
    
    public DetalleBanco() {
        this.estado = Boolean.TRUE;
        this.ctaRetenciones=Boolean.FALSE;
    }

    public DetalleBanco(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(Boolean estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public CatalogoItem getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(CatalogoItem tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(String cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }

    public Boolean getCtaRetenciones() {
        return ctaRetenciones;
    }

    public void setCtaRetenciones(Boolean ctaRetenciones) {
        this.ctaRetenciones = ctaRetenciones;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Servidor getServidorPublico() {
        return servidorPublico;
    }

    public void setServidorPublico(Servidor servidorPublico) {
        this.servidorPublico = servidorPublico;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
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
        if (!(object instanceof DetalleBanco)) {
            return false;
        }
        DetalleBanco other = (DetalleBanco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.curso.entities.DetalleBanco[ id=" + id + " ]";
    }

}
