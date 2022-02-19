/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_predio_obra_interna", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioObraInterna.findAll", query = "SELECT c FROM CatPredioObraInterna c"),
    @NamedQuery(name = "CatPredioObraInterna.findById", query = "SELECT c FROM CatPredioObraInterna c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioObraInterna.findByTipo", query = "SELECT c FROM CatPredioObraInterna c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CatPredioObraInterna.findByMaterial", query = "SELECT c FROM CatPredioObraInterna c WHERE c.material = :material"),
    @NamedQuery(name = "CatPredioObraInterna.findByCantidad", query = "SELECT c FROM CatPredioObraInterna c WHERE c.cantidad = :cantidad"),
    @NamedQuery(name = "CatPredioObraInterna.findByConservacion", query = "SELECT c FROM CatPredioObraInterna c WHERE c.conservacion = :conservacion"),
    @NamedQuery(name = "CatPredioObraInterna.findByEdad", query = "SELECT c FROM CatPredioObraInterna c WHERE c.edad = :edad"),
    @NamedQuery(name = "CatPredioObraInterna.findByModificado", query = "SELECT c FROM CatPredioObraInterna c WHERE c.modificado = :modificado"),
    @NamedQuery(name = "CatPredioObraInterna.findByEstado", query = "SELECT c FROM CatPredioObraInterna c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatPredioObraInterna.findByUsuario", query = "SELECT c FROM CatPredioObraInterna c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CatPredioObraInterna.findByFecha", query = "SELECT c FROM CatPredioObraInterna c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CatPredioObraInterna.findByPredio", query = "SELECT c FROM CatPredioObraInterna c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioObraInterna.findByArea", query = "SELECT c FROM CatPredioObraInterna c WHERE c.area = :area"),
    @NamedQuery(name = "CatPredioObraInterna.findByObservaciones", query = "SELECT c FROM CatPredioObraInterna c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "CatPredioObraInterna.findByAltura", query = "SELECT c FROM CatPredioObraInterna c WHERE c.altura = :altura"),
    @NamedQuery(name = "CatPredioObraInterna.findByAvaluo", query = "SELECT c FROM CatPredioObraInterna c WHERE c.avaluo = :avaluo"),
    @NamedQuery(name = "CatPredioObraInterna.findByEtapaConstruccion", query = "SELECT c FROM CatPredioObraInterna c WHERE c.etapaConstruccion = :etapaConstruccion"),
    @NamedQuery(name = "CatPredioObraInterna.findByTipoGrafico", query = "SELECT c FROM CatPredioObraInterna c WHERE c.tipoGrafico = :tipoGrafico")})
public class CatPredioObraInterna implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo")
    private BigInteger tipo;
    @Column(name = "material")
    private BigInteger material;
    @Column(name = "cantidad")
    private BigInteger cantidad;
    @Column(name = "conservacion")
    private BigInteger conservacion;
    @Column(name = "edad")
    private BigInteger edad;
    @Size(max = 20)
    @Column(name = "modificado")
    private String modificado;
    @Column(name = "estado")
    private Character estado;
    @Size(max = 100)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "predio")
    private BigInteger predio;
    @Column(name = "area")
    private BigInteger area;
    @Size(max = 5000)
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "altura")
    private BigInteger altura;
    @Column(name = "avaluo")
    private BigInteger avaluo;
    @Column(name = "etapa_construccion")
    private BigInteger etapaConstruccion;
    @Column(name = "tipo_grafico")
    private BigInteger tipoGrafico;

    public CatPredioObraInterna() {
    }

    public CatPredioObraInterna(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public BigInteger getMaterial() {
        return material;
    }

    public void setMaterial(BigInteger material) {
        this.material = material;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public BigInteger getConservacion() {
        return conservacion;
    }

    public void setConservacion(BigInteger conservacion) {
        this.conservacion = conservacion;
    }

    public BigInteger getEdad() {
        return edad;
    }

    public void setEdad(BigInteger edad) {
        this.edad = edad;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getPredio() {
        return predio;
    }

    public void setPredio(BigInteger predio) {
        this.predio = predio;
    }

    public BigInteger getArea() {
        return area;
    }

    public void setArea(BigInteger area) {
        this.area = area;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigInteger getAltura() {
        return altura;
    }

    public void setAltura(BigInteger altura) {
        this.altura = altura;
    }

    public BigInteger getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigInteger avaluo) {
        this.avaluo = avaluo;
    }

    public BigInteger getEtapaConstruccion() {
        return etapaConstruccion;
    }

    public void setEtapaConstruccion(BigInteger etapaConstruccion) {
        this.etapaConstruccion = etapaConstruccion;
    }

    public BigInteger getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(BigInteger tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
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
        if (!(object instanceof CatPredioObraInterna)) {
            return false;
        }
        CatPredioObraInterna other = (CatPredioObraInterna) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioObraInterna[ id=" + id + " ]";
    }

}
