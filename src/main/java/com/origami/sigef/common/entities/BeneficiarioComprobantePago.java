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
@Table(name = "beneficiario_comprobante_pago",schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "BeneficiarioComprobantePago.findAll", query = "SELECT b FROM BeneficiarioComprobantePago b")
    , @NamedQuery(name = "BeneficiarioComprobantePago.findById", query = "SELECT b FROM BeneficiarioComprobantePago b WHERE b.id = :id")
    , @NamedQuery(name = "BeneficiarioComprobantePago.findByDetalleBanco", query = "SELECT b FROM BeneficiarioComprobantePago b WHERE b.detalleBanco = :detalleBanco")
    , @NamedQuery(name = "BeneficiarioComprobantePago.findByBeneficiario", query = "SELECT b FROM BeneficiarioComprobantePago b WHERE b.beneficiario = :beneficiario")
    , @NamedQuery(name = "BeneficiarioComprobantePago.findByNumeroTransferencia", query = "SELECT b FROM BeneficiarioComprobantePago b WHERE b.numeroTransferencia = :numeroTransferencia")
    , @NamedQuery(name = "BeneficiarioComprobantePago.findByValor", query = "SELECT b FROM BeneficiarioComprobantePago b WHERE b.valor = :valor")})
public class BeneficiarioComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_transferencia")
    private BigInteger numeroTransferencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "comprobante_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ComprobantePago comprobantePago;
    @JoinColumn(name = "detalle_banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleBanco detalleBanco;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente beneficiario;
    @Column(name = "estado_beneficiario")
    private String estadoBeneficiario ;
    @Column(name = "tipo_beneficiario")
    private Boolean tipoBeneficiario ;

    public BeneficiarioComprobantePago() {
        
    }

    public BeneficiarioComprobantePago(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getNumeroTransferencia() {
        return numeroTransferencia;
    }

    public void setNumeroTransferencia(BigInteger numeroTransferencia) {
        this.numeroTransferencia = numeroTransferencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public DetalleBanco getDetalleBanco() {
        return detalleBanco;
    }

    public void setDetalleBanco(DetalleBanco detalleBanco) {
        this.detalleBanco = detalleBanco;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getEstadoBeneficiario() {
        return estadoBeneficiario;
    }

    public void setEstadoBeneficiario(String estadoBeneficiario) {
        this.estadoBeneficiario = estadoBeneficiario;
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
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
        if (!(object instanceof BeneficiarioComprobantePago)) {
            return false;
        }
        BeneficiarioComprobantePago other = (BeneficiarioComprobantePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.BeneficiarioComprobantePago[ id=" + id + " ]";
    }

}
