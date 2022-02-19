/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLocalComercial;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_local_comercial_horario", schema = Utils.SCHEMA_SGM)
public class RenLocalComercialHorario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "lunes_desde")
    @Temporal(TemporalType.TIME)
    private Date lunesDesde;
    @Column(name = "lunes_hasta")
    @Temporal(TemporalType.TIME)
    private Date lunesHasta;
    @Column(name = "martes_desde")
    @Temporal(TemporalType.TIME)
    private Date martesDesde;
    @Column(name = "martes_hasta")
    @Temporal(TemporalType.TIME)
    private Date martesHasta;
    @Column(name = "miercoles_desde")
    @Temporal(TemporalType.TIME)
    private Date miercolesDesde;
    @Column(name = "miercoles_hasta")
    @Temporal(TemporalType.TIME)
    private Date miercolesHasta;
    @Column(name = "jueves_desde")
    @Temporal(TemporalType.TIME)
    private Date juevesDesde;
    @Column(name = "jueves_hasta")
    @Temporal(TemporalType.TIME)
    private Date juevesHasta;
    @Column(name = "viernes_desde")
    @Temporal(TemporalType.TIME)
    private Date viernesDesde;
    @Column(name = "viernes_hasta")
    @Temporal(TemporalType.TIME)
    private Date viernesHasta;
    @Column(name = "sabado_desde")
    @Temporal(TemporalType.TIME)
    private Date sabadoDesde;
    @Column(name = "sabado_hasta")
    @Temporal(TemporalType.TIME)
    private Date sabadoHasta;
    @Column(name = "domingo_desde")
    @Temporal(TemporalType.TIME)
    private Date domingoDesde;
    @Column(name = "domingo_hasta")
    @Temporal(TemporalType.TIME)
    private Date domingoHasta;
    @Column(name = "observacion_horario")
    private String observacionHorario;
    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private FinaRenLocalComercial renLocalComercial;
    
     public RenLocalComercialHorario() {
    }

    public RenLocalComercialHorario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLunesDesde() {
        return lunesDesde;
    }

    public void setLunesDesde(Date lunesDesde) {
        this.lunesDesde = lunesDesde;
    }

    public Date getLunesHasta() {
        return lunesHasta;
    }

    public void setLunesHasta(Date lunesHasta) {
        this.lunesHasta = lunesHasta;
    }

    public Date getMartesDesde() {
        return martesDesde;
    }

    public void setMartesDesde(Date martesDesde) {
        this.martesDesde = martesDesde;
    }

    public Date getMartesHasta() {
        return martesHasta;
    }

    public void setMartesHasta(Date martesHasta) {
        this.martesHasta = martesHasta;
    }

    public Date getMiercolesDesde() {
        return miercolesDesde;
    }

    public void setMiercolesDesde(Date miercolesDesde) {
        this.miercolesDesde = miercolesDesde;
    }

    public Date getMiercolesHasta() {
        return miercolesHasta;
    }

    public void setMiercolesHasta(Date miercolesHasta) {
        this.miercolesHasta = miercolesHasta;
    }

    public Date getJuevesDesde() {
        return juevesDesde;
    }

    public void setJuevesDesde(Date juevesDesde) {
        this.juevesDesde = juevesDesde;
    }

    public Date getJuevesHasta() {
        return juevesHasta;
    }

    public void setJuevesHasta(Date juevesHasta) {
        this.juevesHasta = juevesHasta;
    }

    public Date getViernesDesde() {
        return viernesDesde;
    }

    public void setViernesDesde(Date viernesDesde) {
        this.viernesDesde = viernesDesde;
    }

    public Date getViernesHasta() {
        return viernesHasta;
    }

    public void setViernesHasta(Date viernesHasta) {
        this.viernesHasta = viernesHasta;
    }

    public Date getSabadoDesde() {
        return sabadoDesde;
    }

    public void setSabadoDesde(Date sabadoDesde) {
        this.sabadoDesde = sabadoDesde;
    }

    public Date getSabadoHasta() {
        return sabadoHasta;
    }

    public void setSabadoHasta(Date sabadoHasta) {
        this.sabadoHasta = sabadoHasta;
    }

    public Date getDomingoDesde() {
        return domingoDesde;
    }

    public void setDomingoDesde(Date domingoDesde) {
        this.domingoDesde = domingoDesde;
    }

    public Date getDomingoHasta() {
        return domingoHasta;
    }

    public void setDomingoHasta(Date domingoHasta) {
        this.domingoHasta = domingoHasta;
    }

    public String getObservacionHorario() {
        return observacionHorario;
    }

    public void setObservacionHorario(String observacionHorario) {
        this.observacionHorario = observacionHorario;
    }

    public FinaRenLocalComercial getRenLocalComercial() {
        return renLocalComercial;
    }

    public void setRenLocalComercial(FinaRenLocalComercial renLocalComercial) {
        this.renLocalComercial = renLocalComercial;
    }

    
    
}
