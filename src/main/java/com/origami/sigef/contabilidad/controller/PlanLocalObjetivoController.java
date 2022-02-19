/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.PlanLocalObjetivoLazy;
import com.origami.sigef.contabilidad.service.PlanLocalObjetivoService;
import com.origami.sigef.contabilidad.service.PlanLocalSistemaService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "planObjetivoView")
@ViewScoped
public class PlanLocalObjetivoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private PlanLocalObjetivoService planObjetivoService;
    @Inject
    private PlanLocalSistemaService planSistemaService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private UserSession userSession;

    private PlanLocalObjetivo planObjetivo;
    private PlanLocalObjetivo PlanSeleccionado;
    private PlanLocalObjetivoLazy lazy;
    private List<MasterCatalogo> periodos;
    private List<PlanLocalSistema> sistemas;
    private PlanLocalSistema planSistema;
    private OpcionBusqueda opcionBusqueda;

    @PostConstruct
    public void init() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.lazy = new PlanLocalObjetivoLazy(opcionBusqueda);
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        this.planObjetivo = new PlanLocalObjetivo();
        this.sistemas = planSistemaService.getPlanSistema();
    }

    public void form(PlanLocalObjetivo objetivo) {
        try {
            if (objetivo != null) {
                this.planObjetivo = objetivo;
            } else {
                this.planObjetivo = new PlanLocalObjetivo();
                this.planObjetivo.setPeriodo(opcionBusqueda.getAnio());
                this.planObjetivo.setFechaCreacion(fechaVigente());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update(":formPlan");
    }

    public void guardar() {
        try {

//            PlanLocalObjetivo existeCuenta = planObjetivoService.existeCuenta(planObjetivo);
//            if (planObjetivo.getId() == null && existeCuenta != null) {
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addWarningMessage("Plan Local Objetivo", planObjetivo.getCodigo() + " se ecuentra registrada en el sistema para dicho perido");
//                return;
//            }
            boolean edit = planObjetivo.getId() != null;
            if (edit) {
                planObjetivo.setFechaModificacion(new Date());
                planObjetivo.setUsuarioModifica(userSession.getNameUser());
                planObjetivoService.edit(planObjetivo);
            } else {
                planObjetivo.setUsuarioCreacion(userSession.getNameUser());
                planObjetivo.setFechaCreacion(new Date());
                planObjetivo = planObjetivoService.create(planObjetivo);
            }
            PrimeFaces.current().executeScript("PF('planDialog').hide()");
            PrimeFaces.current().ajax().update("planes");
            PrimeFaces.current().ajax().update("formMain");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Plan Local", planObjetivo.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error", e.getMessage());
            PrimeFaces.current().ajax().update("messages");
        }
    }

    public void eliminar(PlanLocalObjetivo plan) {
        System.out.println("plan.getId() "+plan.getId());
        List<PlanLocalPolitica> objetivo = planObjetivoService.getObjetivosByEje(plan);
        if (objetivo != null) {
            if (!objetivo.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Plan Local", "Objetivo " + plan.getCodigo() + " tiene información asociada.");
                return;
            }
        }
        plan.setEstado(Boolean.FALSE);
        plan.setFechaCaducidad(new Date());
        planObjetivoService.edit(plan);
        JsfUtil.addSuccessMessage("Plan Local", "Objetivo " + plan.getCodigo() + " Eliminado con éxito.");
        PrimeFaces.current().ajax().update("planes");
    }

    public Date fechaVigente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Short anio = opcionBusqueda.getAnio();
        String dia = "01/01/" + anio;
        try {
            fecha = sdf.parse(dia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public Boolean editable() {
        boolean edit = planObjetivo.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formPlan");
    }

    public PlanLocalObjetivo getPlanObjetivo() {
        return planObjetivo;
    }

    public void setPlanObjetivo(PlanLocalObjetivo planObjetivo) {
        this.planObjetivo = planObjetivo;
    }

    public PlanLocalObjetivo getPlanSeleccionado() {
        return PlanSeleccionado;
    }

    public void setPlanSeleccionado(PlanLocalObjetivo PlanSeleccionado) {
        this.PlanSeleccionado = PlanSeleccionado;
    }

    public LazyModel<PlanLocalObjetivo> getLazy() {
        return lazy;
    }

    public void setLazy(PlanLocalObjetivoLazy lazy) {
        this.lazy = lazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBuesqueda) {
        this.opcionBusqueda = opcionBuesqueda;
    }

    public List<PlanLocalSistema> getSistemas() {
        return sistemas;
    }

    public void setSistemas(List<PlanLocalSistema> sistemas) {
        this.sistemas = sistemas;
    }

    public PlanLocalSistema getPlanSistema() {
        return planSistema;
    }

    public void setPlanSistema(PlanLocalSistema planSistema) {
        this.planSistema = planSistema;
    }

}
