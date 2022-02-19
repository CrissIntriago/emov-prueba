/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "beneficiario_solicitud_reserva", schema = "certificacion_presupuestaria_anual")
@NamedQueries({
    @NamedQuery(name = "BeneficiarioSolicitudReserva.findAll", query = "SELECT b FROM BeneficiarioSolicitudReserva b")
    , @NamedQuery(name = "BeneficiarioSolicitudReserva.findById", query = "SELECT b FROM BeneficiarioSolicitudReserva b WHERE b.id = :id")
    , @NamedQuery(name = "BeneficiarioSolicitudReserva.findByTipoBeneficiario", query = "SELECT b FROM BeneficiarioSolicitudReserva b WHERE b.tipoBeneficiario = :tipoBeneficiario")
    , @NamedQuery(name = "BeneficiarioSolicitudReserva.findByBeneficiario", query = "SELECT b FROM BeneficiarioSolicitudReserva b WHERE b.beneficiario = :beneficiario")})
public class BeneficiarioSolicitudReserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo_beneficiario")
    private Boolean tipoBeneficiario;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente beneficiario;
    @JoinColumn(name = "reserva_compromiso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso reservaCompromiso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;

    public BeneficiarioSolicitudReserva() {
    }

    public BeneficiarioSolicitudReserva(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof BeneficiarioSolicitudReserva)) {
            return false;
        }
        BeneficiarioSolicitudReserva other = (BeneficiarioSolicitudReserva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.BeneficiarioSolicitudReserva[ id=" + id + " ]";
    }

}
