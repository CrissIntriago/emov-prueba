/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.Producto;

/**
 *
 * @author Criss
 */
public class PlanAnualDTO {

    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private ActividadOperativa actividadOperativa;
    private Producto producto;

    public PlanAnualDTO() {

    }

    public PlanAnualDTO(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanAnualDTO(PlanAnualPoliticaPublica planAnualPoliticaPublica, PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public PlanAnualDTO(PlanAnualPoliticaPublica planAnualPoliticaPublica, PlanAnualProgramaProyecto planAnualProgramaProyecto, ActividadOperativa actividadOperativa) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
        this.actividadOperativa = actividadOperativa;
    }

    public PlanAnualDTO(PlanAnualPoliticaPublica planAnualPoliticaPublica, PlanAnualProgramaProyecto planAnualProgramaProyecto, ActividadOperativa actividadOperativa, Producto producto) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
        this.actividadOperativa = actividadOperativa;
        this.producto = producto;
    }

    public PlanAnualPoliticaPublica getPlanAnualPoliticaPublica() {
        return planAnualPoliticaPublica;
    }

    public void setPlanAnualPoliticaPublica(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanAnualProgramaProyecto getPlanAnualProgramaProyecto() {
        return planAnualProgramaProyecto;
    }

    public void setPlanAnualProgramaProyecto(PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public ActividadOperativa getActividadOperativa() {
        return actividadOperativa;
    }

    public void setActividadOperativa(ActividadOperativa actividadOperativa) {
        this.actividadOperativa = actividadOperativa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
