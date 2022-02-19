/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
@Table(name = "retenciones_registradas",schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "RetencionesRegistradas.findAll", query = "SELECT r FROM RetencionesRegistradas r")
    , @NamedQuery(name = "RetencionesRegistradas.findById", query = "SELECT r FROM RetencionesRegistradas r WHERE r.id = :id")
    , @NamedQuery(name = "RetencionesRegistradas.findByPartidaPresupuestaria", query = "SELECT r FROM RetencionesRegistradas r WHERE r.partidaPresupuestaria = :partidaPresupuestaria")
    , @NamedQuery(name = "RetencionesRegistradas.findByMonto", query = "SELECT r FROM RetencionesRegistradas r WHERE r.monto = :monto")})
public class RetencionesRegistradas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    @Column(name = "monto")
    private BigDecimal monto;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;

    public RetencionesRegistradas() {
    }

    public RetencionesRegistradas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
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
        if (!(object instanceof RetencionesRegistradas)) {
            return false;
        }
        RetencionesRegistradas other = (RetencionesRegistradas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RetencionesRegistradas[ id=" + id + " ]";
    }

}
