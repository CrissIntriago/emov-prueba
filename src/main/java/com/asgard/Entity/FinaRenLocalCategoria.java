/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.RenTasaTurismo;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_local_categoria", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenLocalCategoria.findAll", query = "SELECT f FROM FinaRenLocalCategoria f"),
    @NamedQuery(name = "FinaRenLocalCategoria.findById", query = "SELECT f FROM FinaRenLocalCategoria f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenLocalCategoria.findByDescripcion", query = "SELECT f FROM FinaRenLocalCategoria f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenLocalCategoria.findByEstado", query = "SELECT f FROM FinaRenLocalCategoria f WHERE f.estado = :estado"),
    @NamedQuery(name = "FinaRenLocalCategoria.findByPadre", query = "SELECT f FROM FinaRenLocalCategoria f WHERE f.padre = :padre"),
    @NamedQuery(name = "FinaRenLocalCategoria.findByCodigo", query = "SELECT f FROM FinaRenLocalCategoria f WHERE f.codigo = :codigo")})
public class FinaRenLocalCategoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "padre")
    private Long padre;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLocalComercial> finaRenLocalComercialList;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenTasaTurismo> renTasaTurismoLsit;

    public FinaRenLocalCategoria() {
    }

    public FinaRenLocalCategoria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getPadre() {
        return padre;
    }

    public void setPadre(Long padre) {
        this.padre = padre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<FinaRenLocalComercial> getFinaRenLocalComercialList() {
        return finaRenLocalComercialList;
    }

    public void setFinaRenLocalComercialList(List<FinaRenLocalComercial> finaRenLocalComercialList) {
        this.finaRenLocalComercialList = finaRenLocalComercialList;
    }

    public List<RenTasaTurismo> getRenTasaTurismoLsit() {
        return renTasaTurismoLsit;
    }

    public void setRenTasaTurismoLsit(List<RenTasaTurismo> renTasaTurismoLsit) {
        this.renTasaTurismoLsit = renTasaTurismoLsit;
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
        if (!(object instanceof FinaRenLocalCategoria)) {
            return false;
        }
        FinaRenLocalCategoria other = (FinaRenLocalCategoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenLocalCategoria[ id=" + id + " ]";
    }

}
