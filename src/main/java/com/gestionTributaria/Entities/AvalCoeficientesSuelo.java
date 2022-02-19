/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "aval_coeficientes_suelo", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvalCoeficientesSuelo.findAll", query = "SELECT a FROM AvalCoeficientesSuelo a"),
    @NamedQuery(name = "AvalCoeficientesSuelo.findById", query = "SELECT a FROM AvalCoeficientesSuelo a WHERE a.id = :id"),
    @NamedQuery(name = "AvalCoeficientesSuelo.findByAnio", query = "SELECT a FROM AvalCoeficientesSuelo a WHERE a.anio = :anio"),
    @NamedQuery(name = "AvalCoeficientesSuelo.findByCategoriaSolar", query = "SELECT a FROM AvalCoeficientesSuelo a WHERE a.categoriaSolar = :categoriaSolar"),
    @NamedQuery(name = "AvalCoeficientesSuelo.findByValorCoeficiente", query = "SELECT a FROM AvalCoeficientesSuelo a WHERE a.valorCoeficiente = :valorCoeficiente"),
    @NamedQuery(name = "AvalCoeficientesSuelo.findByValorCoefInferior", query = "SELECT a FROM AvalCoeficientesSuelo a WHERE a.valorCoefInferior = :valorCoefInferior"),
    @NamedQuery(name = "AvalCoeficientesSuelo.findByValorCoefSuperior", query = "SELECT a FROM AvalCoeficientesSuelo a WHERE a.valorCoefSuperior = :valorCoefSuperior")})
public class AvalCoeficientesSuelo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "categoria_solar")
    private BigInteger categoriaSolar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_coeficiente")
    private BigDecimal valorCoeficiente;
    @Column(name = "valor_coef_inferior")
    private BigDecimal valorCoefInferior;
    @Column(name = "valor_coef_superior")
    private BigDecimal valorCoefSuperior;
    @JoinColumn(name = "categoria_construccion", referencedColumnName = "id")
    @ManyToOne
    private CatEdfProp categoriaConstruccion;

    public AvalCoeficientesSuelo() {
    }

    public AvalCoeficientesSuelo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigInteger getCategoriaSolar() {
        return categoriaSolar;
    }

    public void setCategoriaSolar(BigInteger categoriaSolar) {
        this.categoriaSolar = categoriaSolar;
    }

    public BigDecimal getValorCoeficiente() {
        return valorCoeficiente;
    }

    public void setValorCoeficiente(BigDecimal valorCoeficiente) {
        this.valorCoeficiente = valorCoeficiente;
    }

    public BigDecimal getValorCoefInferior() {
        return valorCoefInferior;
    }

    public void setValorCoefInferior(BigDecimal valorCoefInferior) {
        this.valorCoefInferior = valorCoefInferior;
    }

    public BigDecimal getValorCoefSuperior() {
        return valorCoefSuperior;
    }

    public void setValorCoefSuperior(BigDecimal valorCoefSuperior) {
        this.valorCoefSuperior = valorCoefSuperior;
    }

    public CatEdfProp getCategoriaConstruccion() {
        return categoriaConstruccion;
    }

    public void setCategoriaConstruccion(CatEdfProp categoriaConstruccion) {
        this.categoriaConstruccion = categoriaConstruccion;
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
        if (!(object instanceof AvalCoeficientesSuelo)) {
            return false;
        }
        AvalCoeficientesSuelo other = (AvalCoeficientesSuelo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AvalCoeficientesSuelo[ id=" + id + " ]";
    }
    
}
