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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMIEC
 */
@Entity
@Table(name = "cupo_presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CupoPresupuesto.findAll", query = "SELECT c FROM CupoPresupuesto c")
    , @NamedQuery(name = "CupoPresupuesto.findById", query = "SELECT c FROM CupoPresupuesto c WHERE c.id = :id")
    , @NamedQuery(name = "CupoPresupuesto.findByMontoCupo", query = "SELECT c FROM CupoPresupuesto c WHERE c.montoCupo = :montoCupo")
    , @NamedQuery(name = "CupoPresupuesto.findByOtros", query = "SELECT c FROM CupoPresupuesto c WHERE c.otros = :otros")
    , @NamedQuery(name = "CupoPresupuesto.findByPeriodo", query = "SELECT c FROM CupoPresupuesto c WHERE c.periodo = :periodo")})

public class CupoPresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_cupo")
    private BigDecimal montoCupo;
    @Column(name = "otros")
    private String otros;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "realizar_tarea")
    private String reliazarTarea;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @Column(name = "responsable")
    private String responsable;
    @Column(name = "username")
    private String userNameResponsable;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;

    public CupoPresupuesto() {
        this.montoCupo=BigDecimal.ZERO;
    }

    public CupoPresupuesto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontoCupo() {
        return montoCupo;
    }

    public void setMontoCupo(BigDecimal montoCupo) {
        this.montoCupo = montoCupo;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getReliazarTarea() {
        return reliazarTarea;
    }

    public void setReliazarTarea(String reliazarTarea) {
        this.reliazarTarea = reliazarTarea;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getUserNameResponsable() {
        return userNameResponsable;
    }

    public void setUserNameResponsable(String userNameResponsable) {
        this.userNameResponsable = userNameResponsable;
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
        if (!(object instanceof CupoPresupuesto)) {
            return false;
        }
        CupoPresupuesto other = (CupoPresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CupoPresupuesto[ id=" + id + " ]";
    }

}
