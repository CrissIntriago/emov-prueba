/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.catastro.Entities.CatPredioEdificacionProp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "cat_edf_prop", schema = "catastro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatEdfProp.findAll", query = "SELECT c FROM CatEdfProp c"),
    @NamedQuery(name = "CatEdfProp.findById", query = "SELECT c FROM CatEdfProp c WHERE c.id = :id"),
    @NamedQuery(name = "CatEdfProp.findByEstado", query = "SELECT c FROM CatEdfProp c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatEdfProp.findByNombre", query = "SELECT c FROM CatEdfProp c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatEdfProp.findByOrden", query = "SELECT c FROM CatEdfProp c WHERE c.orden = :orden"),
    @NamedQuery(name = "CatEdfProp.findByPeso", query = "SELECT c FROM CatEdfProp c WHERE c.peso = :peso"),
    @NamedQuery(name = "CatEdfProp.findByTipo", query = "SELECT c FROM CatEdfProp c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CatEdfProp.findByTipoEstruc", query = "SELECT c FROM CatEdfProp c WHERE c.tipoEstruc = :tipoEstruc")})
public class CatEdfProp implements Serializable {

    @Size(max = 20)
    @Column(name = "codigo_ame")
    private String codigoAme;
    @Column(name = "codigo_tipo")
    private BigInteger codigoTipo;
    @OneToMany(mappedBy = "prop", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CatPredioEdificacionProp> catPredioEdificacionPropList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "orden")
    private Integer orden;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "tipo")
    private BigInteger tipo;
    @Size(max = 255)
    @Column(name = "tipo_estruc")
    private String tipoEstruc;
    @OneToMany(mappedBy = "espec", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AvalDepreciacionSolar> avalDepreciacionSolarList;
    @OneToMany(mappedBy = "categoriaConstruccion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AvalCoeficientesSuelo> avalCoeficientesSueloList;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CatEdfCategProp categoria;

    public CatEdfProp() {
    }

    public CatEdfProp(Long id) {
        this.id = id;
    }

    public CatEdfProp(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public String getTipoEstruc() {
        return tipoEstruc;
    }

    public void setTipoEstruc(String tipoEstruc) {
        this.tipoEstruc = tipoEstruc;
    }

    
    public List<AvalDepreciacionSolar> getAvalDepreciacionSolarList() {
        return avalDepreciacionSolarList;
    }

    public void setAvalDepreciacionSolarList(List<AvalDepreciacionSolar> avalDepreciacionSolarList) {
        this.avalDepreciacionSolarList = avalDepreciacionSolarList;
    }

    
    public List<AvalCoeficientesSuelo> getAvalCoeficientesSueloList() {
        return avalCoeficientesSueloList;
    }

    public void setAvalCoeficientesSueloList(List<AvalCoeficientesSuelo> avalCoeficientesSueloList) {
        this.avalCoeficientesSueloList = avalCoeficientesSueloList;
    }

    public CatEdfCategProp getCategoria() {
        return categoria;
    }

    public void setCategoria(CatEdfCategProp categoria) {
        this.categoria = categoria;
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
        if (!(object instanceof CatEdfProp)) {
            return false;
        }
        CatEdfProp other = (CatEdfProp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CatEdfProp[ id=" + id + " ]";
    }

    public String getCodigoAme() {
        return codigoAme;
    }

    public void setCodigoAme(String codigoAme) {
        this.codigoAme = codigoAme;
    }

    public BigInteger getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(BigInteger codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    @XmlTransient
    public List<CatPredioEdificacionProp> getCatPredioEdificacionPropList() {
        return catPredioEdificacionPropList;
    }

    public void setCatPredioEdificacionPropList(List<CatPredioEdificacionProp> catPredioEdificacionPropList) {
        this.catPredioEdificacionPropList = catPredioEdificacionPropList;
    }

}
