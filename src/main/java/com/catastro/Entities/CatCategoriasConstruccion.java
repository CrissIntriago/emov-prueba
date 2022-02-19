/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_categorias_construccion", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatCategoriasConstruccion.findAll", query = "SELECT c FROM CatCategoriasConstruccion c"),
    @NamedQuery(name = "CatCategoriasConstruccion.findById", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.id = :id"),
    @NamedQuery(name = "CatCategoriasConstruccion.findByCodigoCategoria", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.codigoCategoria = :codigoCategoria"),
    @NamedQuery(name = "CatCategoriasConstruccion.findByDescripcion", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatCategoriasConstruccion.findByValorMt2", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.valorMt2 = :valorMt2"),
    @NamedQuery(name = "CatCategoriasConstruccion.findByUsuario", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CatCategoriasConstruccion.findByFechaIngreso", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CatCategoriasConstruccion.findByVidautil", query = "SELECT c FROM CatCategoriasConstruccion c WHERE c.vidautil = :vidautil")})
public class CatCategoriasConstruccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo_categoria")
    private Integer codigoCategoria;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_mt2")
    private BigDecimal valorMt2;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "vidautil")
    private Integer vidautil;
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CatPredioEdificacion> catPredioEdificacionList;

    public CatCategoriasConstruccion() {
    }

    public CatCategoriasConstruccion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(Integer codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorMt2() {
        return valorMt2;
    }

    public void setValorMt2(BigDecimal valorMt2) {
        this.valorMt2 = valorMt2;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getVidautil() {
        return vidautil;
    }

    public void setVidautil(Integer vidautil) {
        this.vidautil = vidautil;
    }

    @XmlTransient
    public List<CatPredioEdificacion> getCatPredioEdificacionList() {
        return catPredioEdificacionList;
    }

    public void setCatPredioEdificacionList(List<CatPredioEdificacion> catPredioEdificacionList) {
        this.catPredioEdificacionList = catPredioEdificacionList;
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
        if (!(object instanceof CatCategoriasConstruccion)) {
            return false;
        }
        CatCategoriasConstruccion other = (CatCategoriasConstruccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatCategoriasConstruccion[ id=" + id + " ]";
    }

}
