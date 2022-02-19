/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Where;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_local_tipo_accesorio", schema = Utils.SCHEMA_SGM)
public class RenLocalTipoAccesorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Size(max = 50)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;

    @OneToMany(mappedBy = "tipoAccesorio", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    private List<RenLocalCantidadAccesorios> cantidadAccesoriosCollection;

    public RenLocalTipoAccesorio() {
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public List<RenLocalCantidadAccesorios> getCantidadAccesoriosCollection() {
        return cantidadAccesoriosCollection;
    }

    public void setCantidadAccesoriosCollection(List<RenLocalCantidadAccesorios> cantidadAccesoriosCollection) {
        this.cantidadAccesoriosCollection = cantidadAccesoriosCollection;
    }

}
