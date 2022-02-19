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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_estado_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenEstadoLiquidacion.findAll", query = "SELECT f FROM FinaRenEstadoLiquidacion f"),
    @NamedQuery(name = "FinaRenEstadoLiquidacion.findById", query = "SELECT f FROM FinaRenEstadoLiquidacion f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenEstadoLiquidacion.findByDescripcion", query = "SELECT f FROM FinaRenEstadoLiquidacion f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenEstadoLiquidacion.findByEstado", query = "SELECT f FROM FinaRenEstadoLiquidacion f WHERE f.estado = :estado"),
    @NamedQuery(name = "FinaRenEstadoLiquidacion.findByCodigo", query = "SELECT f FROM FinaRenEstadoLiquidacion f WHERE f.codigo = :codigo")})
public class FinaRenEstadoLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 255)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(mappedBy = "estadoLiquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLiquidacion> finaRenLiquidacionList;

    @Transient
    private String color;

    public FinaRenEstadoLiquidacion() {
    }

    public FinaRenEstadoLiquidacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public FinaRenEstadoLiquidacion(Long id) {
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<FinaRenLiquidacion> getFinaRenLiquidacionList() {
        return finaRenLiquidacionList;
    }

    public void setFinaRenLiquidacionList(List<FinaRenLiquidacion> finaRenLiquidacionList) {
        this.finaRenLiquidacionList = finaRenLiquidacionList;
    }

    public String getColor() {
        color = !Utils.isEmptyString(this.descripcion) ? this.descripcion.toLowerCase().equals("pagado") ? "#4CAF50" : "#8b0000" : "#c9c9c9";
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        if (!(object instanceof FinaRenEstadoLiquidacion)) {
            return false;
        }
        FinaRenEstadoLiquidacion other = (FinaRenEstadoLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenEstadoLiquidacion[ id=" + id + " ]";
    }

}
