/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenActividadComercial;
import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "hi_local_actividades", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
public class HiLocalActividades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    @JoinColumn(name = "local", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalComercial local;
    @JoinColumn(name = "actividad", referencedColumnName = "id")
    @ManyToOne
    private FinaRenActividadComercial actividad;

    public HiLocalActividades() {
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

    public FinaRenLocalComercial getLocal() {
        return local;
    }

    public void setLocal(FinaRenLocalComercial local) {
        this.local = local;
    }

    public FinaRenActividadComercial getActividad() {
        return actividad;
    }

    public void setActividad(FinaRenActividadComercial actividad) {
        this.actividad = actividad;
    }

}
