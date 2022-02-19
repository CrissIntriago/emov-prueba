/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "aval_depreciacion_solar", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvalDepreciacionSolar.findAll", query = "SELECT a FROM AvalDepreciacionSolar a"),
    @NamedQuery(name = "AvalDepreciacionSolar.findById", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.id = :id"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByAnio", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.anio = :anio"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByBueno", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.bueno = :bueno"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByCoeficiente", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.coeficiente = :coeficiente"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByMalo", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.malo = :malo"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByRegular", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.regular = :regular"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByObsoleto", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.obsoleto = :obsoleto"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByVidautildesde", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.vidautildesde = :vidautildesde"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByVidautilhasta", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.vidautilhasta = :vidautilhasta"),
    @NamedQuery(name = "AvalDepreciacionSolar.findByNuevo", query = "SELECT a FROM AvalDepreciacionSolar a WHERE a.nuevo = :nuevo")})
public class AvalDepreciacionSolar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "bueno")
    private BigDecimal bueno;
    @Column(name = "coeficiente")
    private BigDecimal coeficiente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "malo")
    private BigDecimal malo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "regular")
    private BigDecimal regular;
    @Column(name = "obsoleto")
    private BigDecimal obsoleto;
    @Column(name = "vidautildesde")
    private Long vidautildesde;
    @Column(name = "vidautilhasta")
    private Long vidautilhasta;
    @Column(name = "nuevo")
    private BigDecimal nuevo;
    @JoinColumn(name = "espec", referencedColumnName = "id")
    @ManyToOne
    private CatEdfProp espec;

    public AvalDepreciacionSolar() {
    }

    public AvalDepreciacionSolar(Long id) {
        this.id = id;
    }

    public AvalDepreciacionSolar(Long id, BigDecimal bueno, BigDecimal malo, BigDecimal regular) {
        this.id = id;
        this.bueno = bueno;
        this.malo = malo;
        this.regular = regular;
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

    public BigDecimal getBueno() {
        return bueno;
    }

    public void setBueno(BigDecimal bueno) {
        this.bueno = bueno;
    }

    public BigDecimal getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(BigDecimal coeficiente) {
        this.coeficiente = coeficiente;
    }

    public BigDecimal getMalo() {
        return malo;
    }

    public void setMalo(BigDecimal malo) {
        this.malo = malo;
    }

    public BigDecimal getRegular() {
        return regular;
    }

    public void setRegular(BigDecimal regular) {
        this.regular = regular;
    }

    public BigDecimal getObsoleto() {
        return obsoleto;
    }

    public void setObsoleto(BigDecimal obsoleto) {
        this.obsoleto = obsoleto;
    }

    public Long getVidautildesde() {
        return vidautildesde;
    }

    public void setVidautildesde(Long vidautildesde) {
        this.vidautildesde = vidautildesde;
    }

    public Long getVidautilhasta() {
        return vidautilhasta;
    }

    public void setVidautilhasta(Long vidautilhasta) {
        this.vidautilhasta = vidautilhasta;
    }

    public BigDecimal getNuevo() {
        return nuevo;
    }

    public void setNuevo(BigDecimal nuevo) {
        this.nuevo = nuevo;
    }

    public CatEdfProp getEspec() {
        return espec;
    }

    public void setEspec(CatEdfProp espec) {
        this.espec = espec;
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
        if (!(object instanceof AvalDepreciacionSolar)) {
            return false;
        }
        AvalDepreciacionSolar other = (AvalDepreciacionSolar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AvalDepreciacionSolar[ id=" + id + " ]";
    }
    
}
