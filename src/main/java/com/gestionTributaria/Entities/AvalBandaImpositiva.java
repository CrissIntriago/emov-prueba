/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author DEVELOPER
 */
@Entity
@Table(name = "aval_banda_impositiva", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvalBandaImpositiva.findAll", query = "SELECT a FROM AvalBandaImpositiva a"),
    @NamedQuery(name = "AvalBandaImpositiva.findById", query = "SELECT a FROM AvalBandaImpositiva a WHERE a.id = :id"),
    @NamedQuery(name = "AvalBandaImpositiva.findByAnio", query = "SELECT a FROM AvalBandaImpositiva a WHERE a.anio = :anio"),
    @NamedQuery(name = "AvalBandaImpositiva.findByDesdeUs", query = "SELECT a FROM AvalBandaImpositiva a WHERE a.desdeUs = :desdeUs"),
    @NamedQuery(name = "AvalBandaImpositiva.findByEstado", query = "SELECT a FROM AvalBandaImpositiva a WHERE a.estado = :estado"),
    @NamedQuery(name = "AvalBandaImpositiva.findByHastaUs", query = "SELECT a FROM AvalBandaImpositiva a WHERE a.hastaUs = :hastaUs"),
    @NamedQuery(name = "AvalBandaImpositiva.findByMultiploImpuestoPredial", query = "SELECT a FROM AvalBandaImpositiva a WHERE a.multiploImpuestoPredial = :multiploImpuestoPredial")})
public class AvalBandaImpositiva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "desde_us")
    private BigDecimal desdeUs;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Column(name = "hasta_us")
    private BigDecimal hastaUs;
    @Column(name = "multiplo_impuesto_predial")
    private BigDecimal multiploImpuestoPredial;
    @Column(name = "predeterminada")
    private Boolean predeterminada = Boolean.FALSE;
    @OneToMany(mappedBy = "bandaImpositiva", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AvalImpuestoPredios> avalImpuestoPrediosList;
    @Column(name = "anio_inicio")
    private Integer anioInicio;
    @Column(name = "anio_fin")
    private Integer anioFin;
    @Column(name = "urbano")
    private Boolean urbano;
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;


    public AvalBandaImpositiva() {
        urbano = Boolean.TRUE;
    }

    public AvalBandaImpositiva(Long id) {
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

    public BigDecimal getDesdeUs() {
        return desdeUs;
    }

    public void setDesdeUs(BigDecimal desdeUs) {
        this.desdeUs = desdeUs;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getHastaUs() {
        return hastaUs;
    }

    public void setHastaUs(BigDecimal hastaUs) {
        this.hastaUs = hastaUs;
    }

    public BigDecimal getMultiploImpuestoPredial() {
        return multiploImpuestoPredial;
    }

    public void setMultiploImpuestoPredial(BigDecimal multiploImpuestoPredial) {
        this.multiploImpuestoPredial = multiploImpuestoPredial;
    }

    public Boolean getPredeterminada() {
        return predeterminada;
    }

    public void setPredeterminada(Boolean predeterminada) {
        this.predeterminada = predeterminada;
    }

    @XmlTransient
    public List<AvalImpuestoPredios> getAvalImpuestoPrediosList() {
        return avalImpuestoPrediosList;
    }

    public void setAvalImpuestoPrediosList(List<AvalImpuestoPredios> avalImpuestoPrediosList) {
        this.avalImpuestoPrediosList = avalImpuestoPrediosList;
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

    public Boolean getUrbano() {
        return urbano;
    }

    public void setUrbano(Boolean urbano) {
        this.urbano = urbano;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
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
        if (!(object instanceof AvalBandaImpositiva)) {
            return false;
        }
        AvalBandaImpositiva other = (AvalBandaImpositiva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AvalBandaImpositiva[ id=" + id + " ]";
    }

}
