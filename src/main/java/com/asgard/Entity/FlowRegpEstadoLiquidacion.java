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
@Table(name = "flow_regp_estado_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowRegpEstadoLiquidacion.findAll", query = "SELECT f FROM FlowRegpEstadoLiquidacion f"),
    @NamedQuery(name = "FlowRegpEstadoLiquidacion.findById", query = "SELECT f FROM FlowRegpEstadoLiquidacion f WHERE f.id = :id"),
    @NamedQuery(name = "FlowRegpEstadoLiquidacion.findByCode", query = "SELECT f FROM FlowRegpEstadoLiquidacion f WHERE f.code = :code")})
public class FlowRegpEstadoLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "estadoLiquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FlowRegpLiquidacion> flowRegpLiquidacionList;

    public FlowRegpEstadoLiquidacion() {
    }

    public FlowRegpEstadoLiquidacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    
    public List<FlowRegpLiquidacion> getFlowRegpLiquidacionList() {
        return flowRegpLiquidacionList;
    }

    public void setFlowRegpLiquidacionList(List<FlowRegpLiquidacion> flowRegpLiquidacionList) {
        this.flowRegpLiquidacionList = flowRegpLiquidacionList;
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
        if (!(object instanceof FlowRegpEstadoLiquidacion)) {
            return false;
        }
        FlowRegpEstadoLiquidacion other = (FlowRegpEstadoLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowRegpEstadoLiquidacion[ id=" + id + " ]";
    }
    
}
