/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "cat_edf_categ_prop", schema = "catastro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatEdfCategProp.findAll", query = "SELECT c FROM CatEdfCategProp c"),
    @NamedQuery(name = "CatEdfCategProp.findById", query = "SELECT c FROM CatEdfCategProp c WHERE c.id = :id"),
    @NamedQuery(name = "CatEdfCategProp.findByNombre", query = "SELECT c FROM CatEdfCategProp c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatEdfCategProp.findByGuiOrden", query = "SELECT c FROM CatEdfCategProp c WHERE c.guiOrden = :guiOrden"),
    @NamedQuery(name = "CatEdfCategProp.findByIsPorcentual", query = "SELECT c FROM CatEdfCategProp c WHERE c.isPorcentual = :isPorcentual"),
    @NamedQuery(name = "CatEdfCategProp.findByPeso", query = "SELECT c FROM CatEdfCategProp c WHERE c.peso = :peso"),
    @NamedQuery(name = "CatEdfCategProp.findByTipo", query = "SELECT c FROM CatEdfCategProp c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CatEdfCategProp.findByTipoEstruc", query = "SELECT c FROM CatEdfCategProp c WHERE c.tipoEstruc = :tipoEstruc"),
    @NamedQuery(name = "CatEdfCategProp.findByEstado", query = "SELECT c FROM CatEdfCategProp c WHERE c.estado = :estado")})
public class CatEdfCategProp implements Serializable {

    @Column(name = "codigo_clase")
    private Integer codigoClase;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gui_orden")
    private short guiOrden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_porcentual")
    private boolean isPorcentual;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "tipo")
    private Integer tipo;
    @Size(max = 255)
    @Column(name = "tipo_estruc")
    private String tipoEstruc;
    @Size(max = 2147483647)
    @Column(name = "estado")
    private String estado;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CatEdfProp> catEdfPropList;

    public CatEdfCategProp() {
    }

    public CatEdfCategProp(Long id) {
        this.id = id;
    }

    public CatEdfCategProp(Long id, String nombre, short guiOrden, boolean isPorcentual) {
        this.id = id;
        this.nombre = nombre;
        this.guiOrden = guiOrden;
        this.isPorcentual = isPorcentual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getGuiOrden() {
        return guiOrden;
    }

    public void setGuiOrden(short guiOrden) {
        this.guiOrden = guiOrden;
    }

    public boolean getIsPorcentual() {
        return isPorcentual;
    }

    public void setIsPorcentual(boolean isPorcentual) {
        this.isPorcentual = isPorcentual;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getTipoEstruc() {
        return tipoEstruc;
    }

    public void setTipoEstruc(String tipoEstruc) {
        this.tipoEstruc = tipoEstruc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    public List<CatEdfProp> getCatEdfPropList() {
        return catEdfPropList;
    }

    public void setCatEdfPropList(List<CatEdfProp> catEdfPropList) {
        this.catEdfPropList = catEdfPropList;
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
        if (!(object instanceof CatEdfCategProp)) {
            return false;
        }
        CatEdfCategProp other = (CatEdfCategProp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CatEdfCategProp[ id=" + id + " ]";
    }

    public Integer getCodigoClase() {
        return codigoClase;
    }

    public void setCodigoClase(Integer codigoClase) {
        this.codigoClase = codigoClase;
    }
    
}
