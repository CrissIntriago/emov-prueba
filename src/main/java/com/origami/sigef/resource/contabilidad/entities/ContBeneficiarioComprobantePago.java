/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
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
@Table(name = "cont_beneficiario_comprobante_pago", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findAll", query = "SELECT c FROM ContBeneficiarioComprobantePago c"),
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findById", query = "SELECT c FROM ContBeneficiarioComprobantePago c WHERE c.id = :id"),
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findByIdCliente", query = "SELECT c FROM ContBeneficiarioComprobantePago c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findByTipoBeneficiario", query = "SELECT c FROM ContBeneficiarioComprobantePago c WHERE c.tipoBeneficiario = :tipoBeneficiario"),
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findByNumRegistro", query = "SELECT c FROM ContBeneficiarioComprobantePago c WHERE c.numRegistro = :numRegistro"),
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findByComprobantePago", query = "SELECT c FROM ContBeneficiarioComprobantePago c WHERE c.idComprobantePago = ?1 ORDER BY c.numRegistro ASC"),
    @NamedQuery(name = "ContBeneficiarioComprobantePago.findByIdDetalleBanco", query = "SELECT c FROM ContBeneficiarioComprobantePago c WHERE c.idDetalleBanco = :idDetalleBanco")})
public class ContBeneficiarioComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente idCliente;
    @Column(name = "tipo_beneficiario")
    private Boolean tipoBeneficiario;
    @Column(name = "num_registro")
    private Integer numRegistro;
    @JoinColumn(name = "id_detalle_banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleBanco idDetalleBanco;
    @JoinColumn(name = "id_comprobante_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContComprobantePago idComprobantePago;
    @Column(name = "monto")
    private BigDecimal monto;

    public ContBeneficiarioComprobantePago() {
        monto = BigDecimal.ZERO;
    }

    public ContBeneficiarioComprobantePago(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public DetalleBanco getIdDetalleBanco() {
        return idDetalleBanco;
    }

    public void setIdDetalleBanco(DetalleBanco idDetalleBanco) {
        this.idDetalleBanco = idDetalleBanco;
    }

    public ContComprobantePago getIdComprobantePago() {
        return idComprobantePago;
    }

    public void setIdComprobantePago(ContComprobantePago idComprobantePago) {
        this.idComprobantePago = idComprobantePago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
        if (!(object instanceof ContBeneficiarioComprobantePago)) {
            return false;
        }
        ContBeneficiarioComprobantePago other = (ContBeneficiarioComprobantePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago[ id=" + id + " ]";
    }

}
