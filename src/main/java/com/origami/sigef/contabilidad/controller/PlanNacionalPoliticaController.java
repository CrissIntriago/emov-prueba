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
import com.origami.sigef.contabilidad.service.PlanNacionalPoliticaService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@Named(value = "planNacionalPoliticaView")
@ViewScoped
public class PlanNacionalPoliticaController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private UserSession userSession;
    @Inject
    private PlanNacionalPoliticaService planPoliticaService;
    @Inject
    private PlanNacionalObjetivoService planObjetivoService;
    @Inject
    private PlanNacionalEjeService planEjeService;

    private PlanNacionalPolitica planPolitica;
    private LazyModel<PlanNacionalPolitica> lazy;
    private List<PlanNacionalObjetivo> objetivos;
    private PlanNacionalEje eje;
    private List<PlanNacionalEje> ejes;
    private PlanNacionalPolitica politicaSeleccionada;

    @PostConstruct
    public void init() {
        eje = new PlanNacionalEje();
        planPolitica = new PlanNacionalPolitica();
        lazy = new LazyModel<>(PlanNacionalPolitica.class);
        lazy.getSorteds().put("id", "ASC");
        lazy.getFilterss().put("estado", true);
        ejes = planEjeService.findByNamedQuery("PlanNacionalEje.findByEstado");
    }

    public void form(PlanNacionalPolitica politica) {
        if (politica != null) {
            eje = politica.getObjetivo().getEje();
            if (eje != null) {
                objetivos = planObjetivoService.findByNamedQuery("PlanNacionalObjetivo.findByEstado", eje.getId());
            }
            planPolitica = politica;
        } else {
            planPolitica = new PlanNacionalPolitica();
            objetivos = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update(":formPlan");
    }

    public boolean readOnlyElementForm() {
        boolean edit;
        return edit = planPolitica.getId() != null;
    }

    public void updateObjetivos() {
        try {
            if (eje != null) {
                objetivos = planObjetivoService.findByNamedQuery("PlanNacionalObjetivo.findByEstado", eje.getId());
                planPolitica.setObjetivo(null);
            } else {
                resetValues();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetValues() {
        eje = new PlanNacionalEje();
        ejes = new ArrayList<>();
        objetivos = new ArrayList<>();
        planPolitica.setObjetivo(null);
    }

    public void guardar() {
        try {
            //Formato para fecha actual-
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());

            boolean edit = planPolitica.getId() != null;
            PlanNacionalPolitica existePolitica = planPoliticaService.existePlan(planPolitica);
            if (planPolitica.getId() == null && existePolitica != null) {
                JsfUtil.addWarningMessage("Plan Nacional", "Política " + planPolitica.getCodigo() + " Del Objetivo " + planPolitica.getObjetivo().getCodigo() + " se encuentra registrado en el sistema");
                return;
            }
            if (planPolitica.getId() != null && existePolitica != null) {
                if (!Objects.equals(planPolitica.getId(), existePolitica.getId())) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("Plan Nacional", "Política " + planPolitica.getCodigo() + " se encuentra registrado en el sistema");
                    return;
                }
            }
            if (edit) {
                if (planPolitica.getFechaCaducidad().before(simpleDateFormat.parse(format))) {
                    planPolitica.setEstado(Boolean.FALSE);
                }
                planPolitica.setFechaModificacion(new Date());
                planPolitica.setUsuarioModifica(userSession.getNameUser());
                planPoliticaService.edit(planPolitica);
            } else {
                if (planPolitica.getFechaCaducidad().before(simpleDateFormat.parse(format))) {
                    planPolitica.setEstado(Boolean.FALSE);
                }
                planPolitica.setUsuarioCreacion(userSession.getNameUser());
                planPolitica.setFechaCreacion(new Date());
                planPolitica = planPoliticaService.create(planPolitica);
            }
            PrimeFaces.current().executeScript("PF('planDialog').hide()");
            PrimeFaces.current().ajax().update("planes");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addSuccessMessage("Plan Nacional", "Política " + planPolitica.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error", e.getMessage());
            PrimeFaces.current().ajax().update("messages");
        }
    }

    public void eliminar(PlanNacionalPolitica politica) {
        politica.setEstado(Boolean.FALSE);
        politica.setFechaCaducidad(new Date());
        planPoliticaService.edit(politica);
        JsfUtil.addSuccessMessage("Plan De Nacional", "Politica " + politica.getCodigo() + " Eliminado con éxito.");
        PrimeFaces.current().ajax().update("planes");

    }

    public void handleCloseForm(CloseEvent event) {
        eje = new PlanNacionalEje();
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formPlan");
    }

    public PlanNacionalPolitica getPoliticaSeleccionada() {
        return politicaSeleccionada;
    }

    public void setPoliticaSeleccionada(PlanNacionalPolitica politicaSeleccionada) {
        this.politicaSeleccionada = politicaSeleccionada;
    }

    public PlanNacionalPoliticaService getPlanPoliticaService() {
        return planPoliticaService;
    }

    public void setPlanPoliticaService(PlanNacionalPoliticaService planPoliticaService) {
        this.planPoliticaService = planPoliticaService;
    }

    public PlanNacionalPolitica getPlanPolitica() {
        return planPolitica;
    }

    public void setPlanPolitica(PlanNacionalPolitica planPolitica) {
        this.planPolitica = planPolitica;
    }

    public LazyModel<PlanNacionalPolitica> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<PlanNacionalPolitica> lazy) {
        this.lazy = lazy;
    }

    public List<PlanNacionalObjetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<PlanNacionalObjetivo> objetivos) {
        this.objetivos = objetivos;
    }

    public List<PlanNacionalEje> getEjes() {
        return ejes;
    }

    public void setEjes(List<PlanNacionalEje> ejes) {
        this.ejes = ejes;
    }

    public PlanNacionalEje getEje() {
        return eje;
    }

    public void setEje(PlanNacionalEje eje) {
        this.eje = eje;
    }

}
