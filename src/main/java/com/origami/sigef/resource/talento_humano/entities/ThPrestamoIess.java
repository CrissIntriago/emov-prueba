/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
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
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_prestamo_iess", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThPrestamoIess.findAll", query = "SELECT t FROM ThPrestamoIess t"),
    @NamedQuery(name = "ThPrestamoIess.findById", query = "SELECT t FROM ThPrestamoIess t WHERE t.id = :id"),
    @NamedQuery(name = "ThPrestamoIess.findByPh", query = "SELECT t FROM ThPrestamoIess t WHERE t.ph = :ph"),
    @NamedQuery(name = "ThPrestamoIess.findByPq", query = "SELECT t FROM ThPrestamoIess t WHERE t.pq = :pq"),
    @NamedQuery(name = "ThPrestamoIess.findByIdCuenta", query = "SELECT t FROM ThPrestamoIess t WHERE t.idCuenta = :idCuenta"),
    @NamedQuery(name = "ThPrestamoIess.findByValor", query = "SELECT t FROM ThPrestamoIess t WHERE t.valor = :valor"),
    @NamedQuery(name = "ThPrestamoIess.findByServidor", query = "SELECT t FROM ThPrestamoIess t WHERE t.servidor = ?1 AND t.idTipoRol = ?2 AND t.estado = true"),
    @NamedQuery(name = "ThPrestamoIess.findByEstado", query = "SELECT t FROM ThPrestamoIess t WHERE t.estado = :estado")})
public class ThPrestamoIess implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "ph")
    private Boolean ph;
    @Column(name = "pq")
    private Boolean pq;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;
    @Column(name = "validado")
    private Boolean validado;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;

    public ThPrestamoIess() {
        this.estado = true;
        this.ph = false;
        this.pq = false;
        this.validado = false;
        this.valor = BigDecimal.ZERO;
    }

    public ThPrestamoIess(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPh() {
        return ph;
    }

    public void setPh(Boolean ph) {
        this.ph = ph;
    }

    public Boolean getPq() {
        return pq;
    }

    public void setPq(Boolean pq) {
        this.pq = pq;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
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
        if (!(object instanceof ThPrestamoIess)) {
            return false;
        }
        ThPrestamoIess other = (ThPrestamoIess) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThPrestamoIess[ id=" + id + " ]";
    }

}
