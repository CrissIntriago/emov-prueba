/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.catastro.Entities.CatTipoConjunto;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "cat_ciudadela", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatCiudadela.findAll", query = "SELECT c FROM CatCiudadela c"),
    @NamedQuery(name = "CatCiudadela.findById", query = "SELECT c FROM CatCiudadela c WHERE c.id = :id"),
    @NamedQuery(name = "CatCiudadela.findByCobraSolarNoEdificado", query = "SELECT c FROM CatCiudadela c WHERE c.cobraSolarNoEdificado = :cobraSolarNoEdificado"),
    @NamedQuery(name = "CatCiudadela.findByCodigo", query = "SELECT c FROM CatCiudadela c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CatCiudadela.findByCodTipoConjunto", query = "SELECT c FROM CatCiudadela c WHERE c.codTipoConjunto = :codTipoConjunto"),
    @NamedQuery(name = "CatCiudadela.findByCostoDirecto", query = "SELECT c FROM CatCiudadela c WHERE c.costoDirecto = :costoDirecto"),
    @NamedQuery(name = "CatCiudadela.findByNombre", query = "SELECT c FROM CatCiudadela c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatCiudadela.findByEstado", query = "SELECT c FROM CatCiudadela c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatCiudadela.findByEsMarginal", query = "SELECT c FROM CatCiudadela c WHERE c.esMarginal = :esMarginal"),
    @NamedQuery(name = "CatCiudadela.findByFondoTipo", query = "SELECT c FROM CatCiudadela c WHERE c.fondoTipo = :fondoTipo"),
    @NamedQuery(name = "CatCiudadela.findByFrenteTipo", query = "SELECT c FROM CatCiudadela c WHERE c.frenteTipo = :frenteTipo"),
    @NamedQuery(name = "CatCiudadela.findByUbicacion", query = "SELECT c FROM CatCiudadela c WHERE c.ubicacion = :ubicacion")})
public class CatCiudadela implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cobra_solar_no_edificado")
    private Boolean cobraSolarNoEdificado;
    @Column(name = "codigo")
    private Integer codigo;
    @JoinColumn(name = "cod_tipo_conjunto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatTipoConjunto codTipoConjunto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_directo")
    private BigDecimal costoDirecto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "es_marginal")
    private Boolean esMarginal;
    @Column(name = "fondo_tipo")
    private BigDecimal fondoTipo;
    @Column(name = "frente_tipo")
    private BigDecimal frenteTipo;
    @Column(name = "ubicacion")
    private BigInteger ubicacion;
    @JoinColumn(name = "cod_parroquia", referencedColumnName = "id")
    @ManyToOne
    private CatParroquia codParroquia;

    public CatCiudadela() {
    }

    public CatCiudadela(Long id) {
        this.id = id;
    }

    public CatCiudadela(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCobraSolarNoEdificado() {
        return cobraSolarNoEdificado;
    }

    public void setCobraSolarNoEdificado(Boolean cobraSolarNoEdificado) {
        this.cobraSolarNoEdificado = cobraSolarNoEdificado;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public CatTipoConjunto getCodTipoConjunto() {
        return codTipoConjunto;
    }

    public void setCodTipoConjunto(CatTipoConjunto codTipoConjunto) {
        this.codTipoConjunto = codTipoConjunto;
    }

    public BigDecimal getCostoDirecto() {
        return costoDirecto;
    }

    public void setCostoDirecto(BigDecimal costoDirecto) {
        this.costoDirecto = costoDirecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getEsMarginal() {
        return esMarginal;
    }

    public void setEsMarginal(Boolean esMarginal) {
        this.esMarginal = esMarginal;
    }

    public BigDecimal getFondoTipo() {
        return fondoTipo;
    }

    public void setFondoTipo(BigDecimal fondoTipo) {
        this.fondoTipo = fondoTipo;
    }

    public BigDecimal getFrenteTipo() {
        return frenteTipo;
    }

    public void setFrenteTipo(BigDecimal frenteTipo) {
        this.frenteTipo = frenteTipo;
    }

    public BigInteger getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(BigInteger ubicacion) {
        this.ubicacion = ubicacion;
    }

    public CatParroquia getCodParroquia() {
        return codParroquia;
    }

    public void setCodParroquia(CatParroquia codParroquia) {
        this.codParroquia = codParroquia;
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
        if (!(object instanceof CatCiudadela)) {
            return false;
        }
        CatCiudadela other = (CatCiudadela) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CatCiudadela[ id=" + id + " ]";
    }

}
