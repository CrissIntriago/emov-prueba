/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "coa_medidas_cautelares", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaMedidasCautelares.findAll", query = "SELECT c FROM CoaMedidasCautelares c"),
    @NamedQuery(name = "CoaMedidasCautelares.findById", query = "SELECT c FROM CoaMedidasCautelares c WHERE c.id = :id"),
    @NamedQuery(name = "CoaMedidasCautelares.findByMedidaCautelar", query = "SELECT c FROM CoaMedidasCautelares c WHERE c.medidaCautelar = :medidaCautelar"),
    @NamedQuery(name = "CoaMedidasCautelares.findByEstado", query = "SELECT c FROM CoaMedidasCautelares c WHERE c.estado = :estado")})
public class CoaMedidasCautelares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "medida_cautelar", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem medidaCautelar;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "juicios", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CoaJuicio juicios;

    public CoaMedidasCautelares() {
    }

    public CoaMedidasCautelares(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getMedidaCautelar() {
        return medidaCautelar;
    }

    public void setMedidaCautelar(CatalogoItem medidaCautelar) {
        this.medidaCautelar = medidaCautelar;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CoaJuicio getJuicios() {
        return juicios;
    }

    public void setJuicios(CoaJuicio juicios) {
        this.juicios = juicios;
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
        if (!(object instanceof CoaMedidasCautelares)) {
            return false;
        }
        CoaMedidasCautelares other = (CoaMedidasCautelares) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CoaMedidasCautelares[ id=" + id + " ]";
    }

}
