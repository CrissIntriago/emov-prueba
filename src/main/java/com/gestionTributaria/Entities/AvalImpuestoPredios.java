/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@Table(name = "aval_impuesto_predios", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvalImpuestoPredios.findAll", query = "SELECT a FROM AvalImpuestoPredios a"),
    @NamedQuery(name = "AvalImpuestoPredios.findById", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.id = :id"),
    @NamedQuery(name = "AvalImpuestoPredios.findByAnioInicio", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.anioInicio = :anioInicio"),
    @NamedQuery(name = "AvalImpuestoPredios.findByAnioFin", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.anioFin = :anioFin"),
    @NamedQuery(name = "AvalImpuestoPredios.findByCobroBombero", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.cobroBombero = :cobroBombero"),
    @NamedQuery(name = "AvalImpuestoPredios.findByCobroMejoras", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.cobroMejoras = :cobroMejoras"),
    @NamedQuery(name = "AvalImpuestoPredios.findByEstado", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.estado = :estado"),
    @NamedQuery(name = "AvalImpuestoPredios.findByMz", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.mz = :mz"),
    @NamedQuery(name = "AvalImpuestoPredios.findByPagoSolarNoEdificado", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.pagoSolarNoEdificado = :pagoSolarNoEdificado"),
    @NamedQuery(name = "AvalImpuestoPredios.findByParroquia", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.parroquia = :parroquia"),
    @NamedQuery(name = "AvalImpuestoPredios.findBySector", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.sector = :sector"),
    @NamedQuery(name = "AvalImpuestoPredios.findBySolar", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.solar = :solar"),
    @NamedQuery(name = "AvalImpuestoPredios.findByZona", query = "SELECT a FROM AvalImpuestoPredios a WHERE a.zona = :zona")})
public class AvalImpuestoPredios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_inicio")
    private Integer anioInicio;
    @Column(name = "anio_fin")
    private Integer anioFin;
    @Column(name = "cobro_bombero")
    private Boolean cobroBombero;
    @Column(name = "cobro_mejoras")
    private Boolean cobroMejoras;
    @Size(max = 255)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mz")
    private short mz;
    @Column(name = "pago_solar_no_edificado")
    private Boolean pagoSolarNoEdificado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "parroquia")
    private short parroquia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sector")
    private short sector;
    @Basic(optional = false)
    @NotNull
    @Column(name = "solar")
    private short solar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona")
    private short zona;
    @JoinColumn(name = "banda_impositiva", referencedColumnName = "id")
    @ManyToOne
    private AvalBandaImpositiva bandaImpositiva;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idAvalImpuestoPredio")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AvalDetImpuestoPredios> avalDetImpuestoPrediosList;

    public AvalImpuestoPredios() {
    }

    public AvalImpuestoPredios(Long id) {
        this.id = id;
    }

    public AvalImpuestoPredios(Long id, short mz, short parroquia, short sector, short solar, short zona) {
        this.id = id;
        this.mz = mz;
        this.parroquia = parroquia;
        this.sector = sector;
        this.solar = solar;
        this.zona = zona;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Integer anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }

    public Boolean getCobroBombero() {
        return cobroBombero;
    }

    public void setCobroBombero(Boolean cobroBombero) {
        this.cobroBombero = cobroBombero;
    }

    public Boolean getCobroMejoras() {
        return cobroMejoras;
    }

    public void setCobroMejoras(Boolean cobroMejoras) {
        this.cobroMejoras = cobroMejoras;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public short getMz() {
        return mz;
    }

    public void setMz(short mz) {
        this.mz = mz;
    }

    public Boolean getPagoSolarNoEdificado() {
        return pagoSolarNoEdificado;
    }

    public void setPagoSolarNoEdificado(Boolean pagoSolarNoEdificado) {
        this.pagoSolarNoEdificado = pagoSolarNoEdificado;
    }

    public short getParroquia() {
        return parroquia;
    }

    public void setParroquia(short parroquia) {
        this.parroquia = parroquia;
    }

    public short getSector() {
        return sector;
    }

    public void setSector(short sector) {
        this.sector = sector;
    }

    public short getSolar() {
        return solar;
    }

    public void setSolar(short solar) {
        this.solar = solar;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public AvalBandaImpositiva getBandaImpositiva() {
        return bandaImpositiva;
    }

    public void setBandaImpositiva(AvalBandaImpositiva bandaImpositiva) {
        this.bandaImpositiva = bandaImpositiva;
    }

    
    public List<AvalDetImpuestoPredios> getAvalDetImpuestoPrediosList() {
        return avalDetImpuestoPrediosList;
    }

    public void setAvalDetImpuestoPrediosList(List<AvalDetImpuestoPredios> avalDetImpuestoPrediosList) {
        this.avalDetImpuestoPrediosList = avalDetImpuestoPrediosList;
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
        if (!(object instanceof AvalImpuestoPredios)) {
            return false;
        }
        AvalImpuestoPredios other = (AvalImpuestoPredios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AvalImpuestoPredios[ id=" + id + " ]";
    }
    
}
