/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cat_predio_clasific_rural", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioClasificRural.findAll", query = "SELECT c FROM CatPredioClasificRural c"),
    @NamedQuery(name = "CatPredioClasificRural.findById", query = "SELECT c FROM CatPredioClasificRural c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioClasificRural.findBySectorHomogeneo", query = "SELECT c FROM CatPredioClasificRural c WHERE c.sectorHomogeneo = :sectorHomogeneo"),
    @NamedQuery(name = "CatPredioClasificRural.findByCalidadSuelo", query = "SELECT c FROM CatPredioClasificRural c WHERE c.calidadSuelo = :calidadSuelo"),
    @NamedQuery(name = "CatPredioClasificRural.findBySuperficie", query = "SELECT c FROM CatPredioClasificRural c WHERE c.superficie = :superficie"),
    @NamedQuery(name = "CatPredioClasificRural.findByUsoPredio", query = "SELECT c FROM CatPredioClasificRural c WHERE c.usoPredio = :usoPredio"),
    @NamedQuery(name = "CatPredioClasificRural.findByModificado", query = "SELECT c FROM CatPredioClasificRural c WHERE c.modificado = :modificado"),
    @NamedQuery(name = "CatPredioClasificRural.findByEstado", query = "SELECT c FROM CatPredioClasificRural c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatPredioClasificRural.findByUsuario", query = "SELECT c FROM CatPredioClasificRural c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CatPredioClasificRural.findByFecha", query = "SELECT c FROM CatPredioClasificRural c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CatPredioClasificRural.findByPredio", query = "SELECT c FROM CatPredioClasificRural c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioClasificRural.findByObservaciones", query = "SELECT c FROM CatPredioClasificRural c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "CatPredioClasificRural.findByValorUnitarioHectareaTerreno", query = "SELECT c FROM CatPredioClasificRural c WHERE c.valorUnitarioHectareaTerreno = :valorUnitarioHectareaTerreno"),
    @NamedQuery(name = "CatPredioClasificRural.findByValorTerreno", query = "SELECT c FROM CatPredioClasificRural c WHERE c.valorTerreno = :valorTerreno")})
public class CatPredioClasificRural implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "sector_homogeneo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem sectorHomogeneo;
    @JoinColumn(name = "calidad_suelo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem calidadSuelo;
    @Column(name = "superficie")
    private BigInteger superficie;
    @JoinColumn(name = "uso_predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem usoPredio;
    @Size(max = 20)
    @Column(name = "modificado")
    private String modificado;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 100)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "predio")
    private BigInteger predio;
    @Size(max = 5000)
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "valor_unitario_hectarea_terreno")
    private BigInteger valorUnitarioHectareaTerreno;
    @Column(name = "valor_terreno")
    private BigInteger valorTerreno;

    public CatPredioClasificRural() {
    }

    public CatPredioClasificRural(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getSectorHomogeneo() {
        return sectorHomogeneo;
    }

    public void setSectorHomogeneo(CatalogoItem sectorHomogeneo) {
        this.sectorHomogeneo = sectorHomogeneo;
    }

    public CatalogoItem getCalidadSuelo() {
        return calidadSuelo;
    }

    public void setCalidadSuelo(CatalogoItem calidadSuelo) {
        this.calidadSuelo = calidadSuelo;
    }

    public CatalogoItem getUsoPredio() {
        return usoPredio;
    }

    public void setUsoPredio(CatalogoItem usoPredio) {
        this.usoPredio = usoPredio;
    }

    public BigInteger getSuperficie() {
        return superficie;
    }

    public void setSuperficie(BigInteger superficie) {
        this.superficie = superficie;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigInteger getValorUnitarioHectareaTerreno() {
        return valorUnitarioHectareaTerreno;
    }

    public void setValorUnitarioHectareaTerreno(BigInteger valorUnitarioHectareaTerreno) {
        this.valorUnitarioHectareaTerreno = valorUnitarioHectareaTerreno;
    }

    public BigInteger getValorTerreno() {
        return valorTerreno;
    }

    public void setValorTerreno(BigInteger valorTerreno) {
        this.valorTerreno = valorTerreno;
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
        if (!(object instanceof CatPredioClasificRural)) {
            return false;
        }
        CatPredioClasificRural other = (CatPredioClasificRural) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioClasificRural[ id=" + id + " ]";
    }

}
