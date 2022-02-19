/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.PlanNacionalEje;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.PlanNacionalEjeService;
import java.io.Serializable;
import java.text.ParseException;
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
@Named(value = "planNacionalEjeView")
@ViewScoped
public class PlanNacionalEjeController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession userSession;
    @Inject
    private PlanNacionalEjeService planNacionalEjeService;

    private PlanNacionalEje planNacionalEje;

    private PlanNacionalEje planSeleccionado;
    private LazyModel<PlanNacionalEje> lazy;

    @PostConstruct
    public void init() {
        this.planNacionalEje = new PlanNacionalEje();
        lazy = new LazyModel<>(PlanNacionalEje.class);
        lazy.getSorteds().put("codigo", "ASC");
        lazy.getFilterss().put("estado", true);
    }

    public void form(PlanNacionalEje eje) {
        if (eje != null) {
            planNacionalEje = eje;
        } else {
            planNacionalEje = new PlanNacionalEje();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update(":formPlan");
    }

    public boolean readOnlyElementForm() {
        boolean edit;
        return edit = planNacionalEje.getId() != null;
    }

    public void guardar() {
        try {
            //Formato para fecha actual-
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());

            boolean edit = planNacionalEje.getId() != null;
            PlanNacionalEje existeEje = planNacionalEjeService.existePlan(planNacionalEje);
            if (planNacionalEje.getId() == null && existeEje != null) {
                JsfUtil.addWarningMessage("Plan Nacional", "Eje " + planNacionalEje.getCodigo() + " se encuentra registrado en el sistema");
                return;
            }
            if (planNacionalEje.getId() != null && existeEje != null) {
                if (!Objects.equals(planNacionalEje.getId(), existeEje.getId())) {
                    JsfUtil.addWarningMessage("Plan Nacional", "Eje " + planNacionalEje.getCodigo() + " se encuentra registrado en el sistema");
                    return;
                }
            }
            if (edit) {
                //Mi FechaCaducidad es Anterior a mi fecha actual TRUE(planNacionalEstado = false)
                if (planNacionalEje.getFechaCaducidad().before(simpleDateFormat.parse(format))) {
                    planNacionalEje.setEstado(Boolean.FALSE);
                }
                planNacionalEje.setFechaModificacion(new Date());
                planNacionalEje.setUsuarioCreacion(userSession.getNameUser());
                planNacionalEjeService.edit(planNacionalEje);
            } else {
                if (planNacionalEje.getFechaCaducidad().before(simpleDateFormat.parse(format))) {
                    planNacionalEje.setEstado(Boolean.FALSE);
                }
                planNacionalEje.setUsuarioCreacion(userSession.getNameUser());
                planNacionalEje.setFechaCreacion(new Date());
                planNacionalEje = planNacionalEjeService.create(planNacionalEje);
            }
            PrimeFaces.current().executeScript("PF('planDialog').hide()");
            JsfUtil.addSuccessMessage("Plan Nacional", "Eje " + planNacionalEje.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
        } catch (ParseException e) {
            JsfUtil.addErrorMessage("Error", e.getMessage());
            PrimeFaces.current().ajax().update("messages");
        }
    }

    public void eliminar(PlanNacionalEje eje) {
        List<PlanNacionalObjetivo> objetivos = planNacionalEjeService.getObjetivosByEje(eje);
        if (objetivos != null) {
            if (!objetivos.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Plan Nacional", "Eje " + eje.getCodigo() + " tiene información asociada.");
                return;
            }
        }
        eje.setEstado(Boolean.FALSE);
        eje.setFechaCaducidad(new Date());
        planNacionalEjeService.edit(eje);
        JsfUtil.addSuccessMessage("Plan De Nacional", "Eje " + eje.getCodigo() + " Eliminado con éxito.");
        PrimeFaces.current().ajax().update("planes");
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formPlan");
    }

    public PlanNacionalEje getPlanNacionalEje() {
        return planNacionalEje;
    }

    public void setPlanNacionalEje(PlanNacionalEje planNacionalEje) {
        this.planNacionalEje = planNacionalEje;
    }

    public LazyModel<PlanNacionalEje> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<PlanNacionalEje> lazy) {
        this.lazy = lazy;
    }

    public PlanNacionalEje getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(PlanNacionalEje planSeleccionado) {
        this.planSeleccionado = planSeleccionado;
    }

}
