/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author LuisPozoG
 */
@Entity
@Table(name = "distributivo_escala", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "DistributivoEscala.findAll", query = "SELECT d FROM DistributivoEscala d")})
public class DistributivoEscala implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "remuneracion_dolares", precision = 15, scale = 2)
    private BigDecimal remuneracionDolares;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "anio")
    private short anio;

    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Distributivo distributivo;
    @JoinColumn(name = "escala_salarial", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private EscalaSalarial escalaSalarial;
    
    @OneToMany(mappedBy = "rmu")
    private List<LiquidacionRol> lrmuLista;

    public DistributivoEscala() {
        this.estado = Boolean.TRUE;
    }

    public DistributivoEscala(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRemuneracionDolares() {
        return remuneracionDolares;
    }

    public void setRemuneracionDolares(BigDecimal remuneracionDolares) {
        this.remuneracionDolares = remuneracionDolares;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public EscalaSalarial getEscalaSalarial() {
        return escalaSalarial;
    }

    public void setEscalaSalarial(EscalaSalarial escalaSalarial) {
        this.escalaSalarial = escalaSalarial;
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
        if (!(object instanceof DistributivoEscala)) {
            return false;
        }
        DistributivoEscala other = (DistributivoEscala) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entitites.DistributivoEscala[ id=" + id + " ]";
    }

}
