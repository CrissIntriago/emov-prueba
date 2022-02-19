/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_tipo_transaccion", schema = Utils.SCHEMA_ASGARD)
public class FinaRenTipoTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "tipoTransaccion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenTipoLiquidacion> renTipoLiquidacionCollection;

    public FinaRenTipoTransaccion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<FinaRenTipoLiquidacion> getRenTipoLiquidacionCollection() {
        return renTipoLiquidacionCollection;
    }

    public void setRenTipoLiquidacionCollection(List<FinaRenTipoLiquidacion> renTipoLiquidacionCollection) {
        this.renTipoLiquidacionCollection = renTipoLiquidacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FinaRenTipoTransaccion other = (FinaRenTipoTransaccion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FinaRenTipoTransaccion{" + "id=" + id + ", estado=" + estado + ", descripcion=" + descripcion + ", renTipoLiquidacionCollection=" + renTipoLiquidacionCollection + '}';
    }

}
