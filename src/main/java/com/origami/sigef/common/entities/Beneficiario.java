/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ARTURO404
 */
@Entity
@Table(name = "beneficiario")
@NamedQueries({
    @NamedQuery(name = "Beneficiario.findAll", query = "SELECT b FROM Beneficiario b"),
    @NamedQuery(name = "Beneficiario.findById", query = "SELECT b FROM Beneficiario b WHERE b.id = :id"),
    @NamedQuery(name = "Beneficiario.findByTipoBeneficiario", query = "SELECT b FROM Beneficiario b WHERE b.tipoBeneficiario = :tipoBeneficiario"),
    @NamedQuery(name = "Beneficiario.findByIdentificacion", query = "SELECT b FROM Beneficiario b WHERE b.identificacion = :identificacion"),
    @NamedQuery(name = "Beneficiario.findByPeriodo", query = "SELECT b FROM Beneficiario b WHERE b.periodo = :periodo")})
public class Beneficiario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "tipo_beneficiario")
    private String tipoBeneficiario;
    @Size(max = 10)
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "periodo")
    private Short periodo;
    @JoinColumn(name = "cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem cuenta;
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente cliente;

    public Beneficiario() {
    }

    public Beneficiario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(String tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public CatalogoItem getCuenta() {
        return cuenta;
    }

    public void setCuenta(CatalogoItem cuenta) {
        this.cuenta = cuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        if (!(object instanceof Beneficiario)) {
            return false;
        }
        Beneficiario other = (Beneficiario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.proyecto.auxiliar.Beneficiario[ id=" + id + " ]";
    }

}
