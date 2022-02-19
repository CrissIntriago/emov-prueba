/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "aval_det_impuesto_predios", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvalDetImpuestoPredios.findAll", query = "SELECT a FROM AvalDetImpuestoPredios a"),
    @NamedQuery(name = "AvalDetImpuestoPredios.findById", query = "SELECT a FROM AvalDetImpuestoPredios a WHERE a.id = :id"),
    @NamedQuery(name = "AvalDetImpuestoPredios.findByIdRubroCobrar", query = "SELECT a FROM AvalDetImpuestoPredios a WHERE a.idRubroCobrar = :idRubroCobrar")})
public class AvalDetImpuestoPredios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_rubro_cobrar")
    private long idRubroCobrar;
    @JoinColumn(name = "id_aval_impuesto_predio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AvalImpuestoPredios idAvalImpuestoPredio;

    public AvalDetImpuestoPredios() {
    }

    public AvalDetImpuestoPredios(Long id) {
        this.id = id;
    }

    public AvalDetImpuestoPredios(Long id, long idRubroCobrar) {
        this.id = id;
        this.idRubroCobrar = idRubroCobrar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdRubroCobrar() {
        return idRubroCobrar;
    }

    public void setIdRubroCobrar(long idRubroCobrar) {
        this.idRubroCobrar = idRubroCobrar;
    }

    public AvalImpuestoPredios getIdAvalImpuestoPredio() {
        return idAvalImpuestoPredio;
    }

    public void setIdAvalImpuestoPredio(AvalImpuestoPredios idAvalImpuestoPredio) {
        this.idAvalImpuestoPredio = idAvalImpuestoPredio;
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
        if (!(object instanceof AvalDetImpuestoPredios)) {
            return false;
        }
        AvalDetImpuestoPredios other = (AvalDetImpuestoPredios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AvalDetImpuestoPredios[ id=" + id + " ]";
    }
    
}
