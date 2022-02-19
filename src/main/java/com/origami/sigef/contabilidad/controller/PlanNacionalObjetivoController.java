/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.PlanNacionalEje;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.PlanNacionalEjeService;
import com.origami.sigef.contabilidad.service.PlanNacionalObjetivoService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Origami
 */
@Named(value = "planNacionalObjetivoView")
@ViewScoped
public class PlanNacionalObjetivoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private UserSession userSession;
    @Inject
    private PlanNacionalObjetivoService planObjetivoService;
    @Inject
    private PlanNacionalEjeService planEjeService;

    private PlanNacionalObjetivo planObjetivo;
    private PlanNacionalEje planEje;
    private List<PlanNacionalEje> ejes;

    private LazyModel<PlanNacionalObjetivo> lazy;
    private PlanNacionalObjetivo ObjetivoSeleccionado;

    @PostConstruct
    public void init() {
        this.planObjetivo = new PlanNacionalObjetivo();
        this.ejes = planEjeService.findByNamedQuery("PlanNacionalEje.findByEstado");
        lazy = new LazyModel<>(PlanNacionalObjetivo.class);
        lazy.getSorteds().put("id", "ASC");
        lazy.getFilterss().put("estado", true);
    }

    public void form(PlanNacionalObjetivo objetivo) {
        if (objetivo != null) {
            planObjetivo = objetivo;
        } else {
            planObjetivo = new PlanNacionalObjetivo();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update(":formPlan");
    }

    public boolean readOnlyElementForm() {
        boolean edit;
        return edit = planObjetivo.getId() != null;
    }

    public void guardar() {
        try {
            //Formato para fecha actual-
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());

            boolean edit = planObjetivo.getId() != null;
            PlanNacionalObjetivo existeObjetivo = planObjetivoService.existePlan(planObjetivo);
            if (planObjetivo.getId() == null && existeObjetivo != null) {
                JsfUtil.addWarningMessage("Plan Nacional", "Objetivo " + planObjetivo.getCodigo() + " se encuentra registrado en el sistema");
                return;
            }
            if (planObjetivo.getId() != null && existeObjetivo != null) {
                if (!Objects.equals(planObjetivo.getId(), existeObjetivo.getId())) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("Plan Nacional", "Objetivo" + planObjetivo.getCodigo() + "Del Eje" + planObjetivo.getEje() + " se encuentra registrado en el sistema");
                    return;
                }
            }
            if (edit) {
                if (planObjetivo.getFechaCaducidad().before(simpleDateFormat.parse(format))) {
                    planObjetivo.setEstado(Boolean.FALSE);
                }
                planObjetivo.setFechaModificacion(new Date());
                planObjetivo.setUsuarioModifica(userSession.getNameUser());
                planObjetivoService.edit(planObjetivo);
            } else {
                if (planObjetivo.getFechaCaducidad().before(simpleDateFormat.parse(format))) {
                    planObjetivo.setEstado(Boolean.FALSE);
                }
                planObjetivo.setUsuarioCreacion(userSession.getNameUser());
                planObjetivo.setFechaCreacion(new Date());
                planObjetivo = planObjetivoService.create(planObjetivo);
            }
            PrimeFaces.current().executeScript("PF('planDialog').hide()");
            PrimeFaces.current().ajax().update("planes");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addSuccessMessage("Plan Nacional", "Objetivo " + planObjetivo.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error", e.getMessage());
            PrimeFaces.current().ajax().update("messages");
        }
    }

    public void eliminar(PlanNacionalObjetivo objetivo) {
        List<PlanNacionalPolitica> politicas = planObjetivoService.getPoliticasByObjetivo(objetivo);
        if (politicas != null) {
            if (!politicas.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Plan Nacional", "Objetivo " + objetivo.getCodigo() + " tiene información asociada.");
                return;
            }
        }
        objetivo.setEstado(Boolean.FALSE);
        objetivo.setFechaCaducidad(new Date());
        planObjetivoService.edit(objetivo);
        JsfUtil.addSuccessMessage("Plan De Nacional", "Objetivo " + objetivo.getCodigo() + " Eliminado con éxito.");
        PrimeFaces.current().ajax().update("planes");
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formPlan");
    }

    public void botonCancelar() {
        planObjetivo = new PlanNacionalObjetivo();
        planObjetivo.setDescripcion("");
        PrimeFaces.current().executeScript("PF('planDialog').hide()");
    }

    public PlanNacionalObjetivo getPlanObjetivo() {
        return planObjetivo;
    }

    public PlanNacionalObjetivo getObjetivoseleccionado() {
        return ObjetivoSeleccionado;
    }

    public void setObjetivoseleccionado(PlanNacionalObjetivo Objetivoseleccionado) {
        this.ObjetivoSeleccionado = Objetivoseleccionado;
    }

    public void setPlanObjetivo(PlanNacionalObjetivo planObjetivo) {
        this.planObjetivo = planObjetivo;
    }

    public PlanNacionalEje getPlanEje() {
        return planEje;
    }

    public void setPlanEje(PlanNacionalEje planEje) {
        this.planEje = planEje;
    }

    public List<PlanNacionalEje> getEjes() {
        return ejes;
    }

    public void setListEje(List<PlanNacionalEje> ejes) {
        this.ejes = ejes;
    }

    public LazyModel<PlanNacionalObjetivo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<PlanNacionalObjetivo> lazy) {
        this.lazy = lazy;
    }

}
