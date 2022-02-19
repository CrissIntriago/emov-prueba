/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

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
 * @author Administrator
 */
@Entity
@Table(name = "cen_avaluo_municipal", schema = "sgm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CenAvaluoMunicipal.findAll", query = "SELECT c FROM CenAvaluoMunicipal c"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByAnioDesde", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.anioDesde = :anioDesde"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByAnioHasta", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.anioHasta = :anioHasta"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByAvaluoDesde", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.avaluoDesde = :avaluoDesde"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByAvaluoHasta", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.avaluoHasta = :avaluoHasta"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByTarifa", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.tarifa = :tarifa"),
    @NamedQuery(name = "CenAvaluoMunicipal.findById", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.id = :id"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByObra", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.obra = :obra"),
    @NamedQuery(name = "CenAvaluoMunicipal.findBySector", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.sector = :sector"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByCodigoCiudadela", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.codigoCiudadela = :codigoCiudadela"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByManzanaInicio", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.manzanaInicio = :manzanaInicio"),
    @NamedQuery(name = "CenAvaluoMunicipal.findByManzanaFin", query = "SELECT c FROM CenAvaluoMunicipal c WHERE c.manzanaFin = :manzanaFin")})
public class CenAvaluoMunicipal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "anio_desde")
    private Short anioDesde;
    @Column(name = "anio_hasta")
    private Short anioHasta;
    @Column(name = "avaluo_desde")
    private BigDecimal avaluoDesde;
    @Column(name = "avaluo_hasta")
    private BigDecimal avaluoHasta;
    @Column(name = "tarifa")
    private BigDecimal tarifa;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "obra")
    private BigInteger obra;
    @Column(name = "sector")
    private BigInteger sector;
    @JoinColumn(name = "codigo_ciudadela", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatCiudadela codigoCiudadela;
    @Column(name = "manzana_inicio")
    private BigInteger manzanaInicio;
    @Column(name = "manzana_fin")
    private BigInteger manzanaFin;
    @Column(name = "codigo_configuracion")
    private BigInteger codigoConfiguracion;

    public CenAvaluoMunicipal() {
    }

    public CenAvaluoMunicipal(Long id) {
        this.id = id;
    }

    public Short getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Short anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Short getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Short anioHasta) {
        this.anioHasta = anioHasta;
    }

    public BigDecimal getAvaluoDesde() {
        return avaluoDesde;
    }

    public void setAvaluoDesde(BigDecimal avaluoDesde) {
        this.avaluoDesde = avaluoDesde;
    }

    public BigDecimal getAvaluoHasta() {
        return avaluoHasta;
    }

    public void setAvaluoHasta(BigDecimal avaluoHasta) {
        this.avaluoHasta = avaluoHasta;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getObra() {
        return obra;
    }

    public void setObra(BigInteger obra) {
        this.obra = obra;
    }

    public BigInteger getSector() {
        return sector;
    }

    public void setSector(BigInteger sector) {
        this.sector = sector;
    }

    public CatCiudadela getCodigoCiudadela() {
        return codigoCiudadela;
    }

    public void setCodigoCiudadela(CatCiudadela codigoCiudadela) {
        this.codigoCiudadela = codigoCiudadela;
    }

    public BigInteger getManzanaInicio() {
        return manzanaInicio;
    }

    public void setManzanaInicio(BigInteger manzanaInicio) {
        this.manzanaInicio = manzanaInicio;
    }

    public BigInteger getManzanaFin() {
        return manzanaFin;
    }

    public void setManzanaFin(BigInteger manzanaFin) {
        this.manzanaFin = manzanaFin;
    }

    public BigInteger getCodigoConfiguracion() {
        return codigoConfiguracion;
    }

    public void setCodigoConfiguracion(BigInteger codigoConfiguracion) {
        this.codigoConfiguracion = codigoConfiguracion;
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
        if (!(object instanceof CenAvaluoMunicipal)) {
            return false;
        }
        CenAvaluoMunicipal other = (CenAvaluoMunicipal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CenAvaluoMunicipal[ id=" + id + " ]";
    }

}
