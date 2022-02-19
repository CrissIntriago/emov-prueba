/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_contabilidad_modulo", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContContabilidadModulo.findAll", query = "SELECT c FROM ContContabilidadModulo c"),
    @NamedQuery(name = "ContContabilidadModulo.findById", query = "SELECT c FROM ContContabilidadModulo c WHERE c.id = :id"),
    @NamedQuery(name = "ContContabilidadModulo.findByIdContDiarioGeneral", query = "SELECT c FROM ContContabilidadModulo c WHERE c.idContDiarioGeneral = :idContDiarioGeneral AND c.estado= true"),
    @NamedQuery(name = "ContContabilidadModulo.findByClass1", query = "SELECT c FROM ContContabilidadModulo c WHERE c.class1 = :class1")})
public class ContContabilidadModulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "class")
    private String class1;
    @JoinColumn(name = "id_cont_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral idContDiarioGeneral;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "cod_modulo")
    private Integer codModulo;
    @Column(name = "id_modulo")
    private BigInteger idModulo;

    public ContContabilidadModulo() {
        this.estado = true;
    }

    public ContContabilidadModulo(Long id) {
        this.id = id;
    }

    public ContContabilidadModulo(ContDiarioGeneral idContDiarioGeneral, String class1) {
        this.idContDiarioGeneral = idContDiarioGeneral;
        this.class1 = class1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public ContDiarioGeneral getIdContDiarioGeneral() {
        return idContDiarioGeneral;
    }

    public void setIdContDiarioGeneral(ContDiarioGeneral idContDiarioGeneral) {
        this.idContDiarioGeneral = idContDiarioGeneral;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getCodModulo() {
        return codModulo;
    }

    public void setCodModulo(Integer codModulo) {
        this.codModulo = codModulo;
    }

    public BigInteger getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(BigInteger idModulo) {
        this.idModulo = idModulo;
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
        if (!(object instanceof ContContabilidadModulo)) {
            return false;
        }
        ContContabilidadModulo other = (ContContabilidadModulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContContabilidadModulo[ id=" + id + " ]";
    }

}
