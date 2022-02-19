/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.contabilidad.lazy.PlanProgramaticoLazy;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author CRISS INTRIAGO
 */
@Named("dlgPlanProg")
@ViewScoped
public class DlgPlanProg implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private PlanProgramaticoService planService;

    private PlanProgramaticoLazy lazy;

    @PostConstruct
    public void initView() {
        this.lazy = new PlanProgramaticoLazy();
    }

    public void seleccionar(PlanProgramatico plan) {
        PrimeFaces.current().dialog().closeDynamic(plan);
    }

    public PlanProgramaticoService getPlanService() {
        return planService;
    }

    public void setPlanService(PlanProgramaticoService planService) {
        this.planService = planService;
    }

    public PlanProgramaticoLazy getLazy() {
        return lazy;
    }

    public void setLazy(PlanProgramaticoLazy lazy) {
        this.lazy = lazy;
    }

}
