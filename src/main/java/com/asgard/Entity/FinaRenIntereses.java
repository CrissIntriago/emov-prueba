/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_intereses", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenIntereses.findAll", query = "SELECT f FROM FinaRenIntereses f"),
    @NamedQuery(name = "FinaRenIntereses.findById", query = "SELECT f FROM FinaRenIntereses f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenIntereses.findByDesde", query = "SELECT f FROM FinaRenIntereses f WHERE f.desde = :desde"),
    @NamedQuery(name = "FinaRenIntereses.findByHasta", query = "SELECT f FROM FinaRenIntereses f WHERE f.hasta = :hasta"),
    @NamedQuery(name = "FinaRenIntereses.findByPorcentaje", query = "SELECT f FROM FinaRenIntereses f WHERE f.porcentaje = :porcentaje"),
    @NamedQuery(name = "FinaRenIntereses.findByDias", query = "SELECT f FROM FinaRenIntereses f WHERE f.dias = :dias"),
    @NamedQuery(name = "FinaRenIntereses.findByAnio", query = "SELECT f FROM FinaRenIntereses f WHERE f.anio = :anio")})
public class FinaRenIntereses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date desde;
    @Column(name = "hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hasta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "dias")
    private Integer dias;
    @Column(name = "anio")
    private Integer anio;

    public FinaRenIntereses() {
    }

    public FinaRenIntereses(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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
        if (!(object instanceof FinaRenIntereses)) {
            return false;
        }
        FinaRenIntereses other = (FinaRenIntereses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenIntereses[ id=" + id + " ]";
    }
    
}
