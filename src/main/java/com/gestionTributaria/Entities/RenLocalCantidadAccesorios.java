/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_local_cantidad_accesorios", schema = Utils.SCHEMA_SGM)
public class RenLocalCantidadAccesorios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Size(max = 50)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;

    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalComercial localComercial;

    @JoinColumn(name = "tipo_accesorio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenLocalTipoAccesorio tipoAccesorio;

    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatalogoItem tipo;

    public RenLocalCantidadAccesorios() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public RenLocalTipoAccesorio getTipoAccesorio() {
        return tipoAccesorio;
    }

    public void setTipoAccesorio(RenLocalTipoAccesorio tipoAccesorio) {
        this.tipoAccesorio = tipoAccesorio;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }
    
    

}
