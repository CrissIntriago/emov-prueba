/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
@Table(name = "fina_ren_tipo_valor", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenTipoValor.findAll", query = "SELECT f FROM FinaRenTipoValor f"),
    @NamedQuery(name = "FinaRenTipoValor.findById", query = "SELECT f FROM FinaRenTipoValor f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenTipoValor.findByDescripcion", query = "SELECT f FROM FinaRenTipoValor f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenTipoValor.findByEsPorcentaje", query = "SELECT f FROM FinaRenTipoValor f WHERE f.esPorcentaje = :esPorcentaje"),
    @NamedQuery(name = "FinaRenTipoValor.findByEstado", query = "SELECT f FROM FinaRenTipoValor f WHERE f.estado = :estado"),
    @NamedQuery(name = "FinaRenTipoValor.findByPrefijo", query = "SELECT f FROM FinaRenTipoValor f WHERE f.prefijo = :prefijo")})
public class FinaRenTipoValor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "es_porcentaje")
    private Boolean esPorcentaje;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 3)
    @Column(name = "prefijo")
    private String prefijo;
    @OneToMany(mappedBy = "tipoValor", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenRubrosLiquidacion> finaRenRubrosLiquidacionList;

    public FinaRenTipoValor() {
    }

    public FinaRenTipoValor(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsPorcentaje() {
        return esPorcentaje;
    }

    public void setEsPorcentaje(Boolean esPorcentaje) {
        this.esPorcentaje = esPorcentaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    
    public List<FinaRenRubrosLiquidacion> getFinaRenRubrosLiquidacionList() {
        return finaRenRubrosLiquidacionList;
    }

    public void setFinaRenRubrosLiquidacionList(List<FinaRenRubrosLiquidacion> finaRenRubrosLiquidacionList) {
        this.finaRenRubrosLiquidacionList = finaRenRubrosLiquidacionList;
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
        if (!(object instanceof FinaRenTipoValor)) {
            return false;
        }
        FinaRenTipoValor other = (FinaRenTipoValor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenTipoValor[ id=" + id + " ]";
    }
    
}
