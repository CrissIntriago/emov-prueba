/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.Presupuesto.Service.PlanificacionPlanService;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.PlanLocalPoliticaLazy;
import com.origami.sigef.contabilidad.service.PlanLocalObjetivoService;
import com.origami.sigef.contabilidad.service.PlanLocalPoliticaService;
import com.origami.sigef.contabilidad.service.PlanLocalSistemaService;
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
 * @author ORIGAMI2
 */
@Named(value = "planPoliticaView")
@ViewScoped
public class PlanLocalPoliticaController implements Serializable {

    /**
     *
     */
    @Inject
    private UserSession userSession;

    private static final long serialVersionUID = 1L;
    @Inject
    private PlanLocalPoliticaService planPoliticaService;
    @Inject
    private PlanLocalObjetivoService planObjetivoService;
    @Inject
    private PlanLocalSistemaService planSistemaService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private PlanificacionPlanService planifiPlanService;

    private PlanLocalPolitica planPolitica;
    private PlanLocalPolitica planSeleccionado;
    private PlanLocalPoliticaLazy lazy;
    private List<MasterCatalogo> periodos;
    private PlanLocalSistema sistema;
    private List<PlanLocalSistema> sistemas;
    private List<PlanLocalObjetivo> objetivos;
    private OpcionBusqueda opcionBusqueda;

    @PostConstruct
    public void init() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.lazy = new PlanLocalPoliticaLazy(opcionBusqueda);
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        //this.objetivos = planObjetivoService.findAll();
        sistema = new PlanLocalSistema();
        sistemas = planSistemaService.findByNamedQuery("PlanLocalSistema.findByEstado");
        planPolitica = new PlanLocalPolitica();
    }

    public void form(PlanLocalPolitica politica) {
        try {
            if (politica != null) {
                sistema = politica.getObjetivo().getSistema();
                if (sistema != null) {
                    objetivos = planObjetivoService.findByNamedQuery("PlanLocalObjetivo.findByEstado", sistema.getId());
                }
                this.planPolitica = politica;
            } else {
                sistemas = planSistemaService.findByNamedQuery("PlanLocalSistema.findByEstado");
                this.planPolitica = new PlanLocalPolitica();
                this.planPolitica.setPeriodo(opcionBusqueda.getAnio());
                this.planPolitica.setFechaCreacion(fechaVigente());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update(":formPlan");
    }

    public void guardar() {
        try {
            boolean edit = planPolitica.getId() != null;
            PlanLocalPolitica existeCuenta = planPoliticaService.existeCuenta(planPolitica);
            if (planPolitica.getId() == null && existeCuenta != null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Plan Local Politica", planPolitica.getCodigo() + " se ecuentra registrada en el sistema.");
                return;
            }
            if (planPolitica != null && existeCuenta != null) {
                if (!Objects.equals(planPolitica.getId(), existeCuenta.getId())) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("Plan Local Politicas", planPolitica.getCodigo() + " se ecuentra registrada en el sistema.");
                    return;
                }
            }
            if (edit) {
                planPolitica.setFechaModificacion(new Date());
                planPolitica.setUsuarioModifica(userSession.getNameUser());
                planPoliticaService.edit(planPolitica);
            } else {
                if (planPolitica.getCodigo() == null) {/////////////////////////////////// NUEVO ENRIQUE
                    String idPoliticaLocal = planifiPlanService.getIdObjetivoLocal(planPolitica.getObjetivo());
                    if (idPoliticaLocal == null) {
                        idPoliticaLocal = "1";
                    } else {
                        Integer val = Integer.parseInt(idPoliticaLocal) + 1;
                        idPoliticaLocal = val.toString();
                    }
                    planPolitica.setCodigo(idPoliticaLocal);
                }
                planPolitica.setUsuarioCreacion(userSession.getNameUser());
                planPolitica.setFechaCreacion(new Date());
                planPolitica = planPoliticaService.create(planPolitica);
            }
            PrimeFaces.current().executeScript("PF('planDialog').hide()");
            PrimeFaces.current().ajax().update("planes");
            PrimeFaces.current().ajax().update("formMain");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Plan Politica", planPolitica.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error", e.getMessage());
            PrimeFaces.current().ajax().update("messages");
        }
        sistemas = null;
    }

    public void actualizarObjetivos() {
        if (sistema != null) {
            objetivos = planObjetivoService.findByNamedQuery("PlanLocalObjetivo.findByEstado", sistema.getId());
        } else {
            objetivos = null;
        }
    }

    public void eliminar(PlanLocalPolitica plan) {
        List<PlanLocalProgramaProyecto> politica = planPoliticaService.getObjetivosByEje(plan);
        if (politica != null) {
            if (!politica.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Plan Nacional", "Política " + plan.getCodigo() + " tiene información asociada.");
                return;
            }
        }
        plan.setEstado(Boolean.FALSE);
        plan.setFechaCaducidad(new Date());
        planPoliticaService.edit(plan);
        JsfUtil.addSuccessMessage("Plan De Nacional", "Política " + plan.getCodigo() + " Eliminado con éxito.");
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
        boolean edit = planPolitica.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    public void handleCloseForm(CloseEvent event) {
        sistema = new PlanLocalSistema();
        PrimeFaces.current().ajax().update("planes");
        PrimeFaces.current().ajax().update("formPlan");
    }

    public PlanLocalPolitica getPlanPolitica() {
        return planPolitica;
    }

    public void setPlanPolitica(PlanLocalPolitica planPolitica) {
        this.planPolitica = planPolitica;
    }

    public PlanLocalPolitica getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(PlanLocalPolitica planSeleccionado) {
        this.planSeleccionado = planSeleccionado;
    }

    public LazyModel<PlanLocalPolitica> getLazy() {
        return lazy;
    }

    public void setLazy(PlanLocalPoliticaLazy lazy) {
        this.lazy = lazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<PlanLocalObjetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<PlanLocalObjetivo> objetivos) {
        this.objetivos = objetivos;
    }

    public MasterCatalogoService getMasterCatalogoService() {
        return masterCatalogoService;
    }

    public void setMasterCatalogoService(MasterCatalogoService masterCatalogoService) {
        this.masterCatalogoService = masterCatalogoService;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public PlanLocalSistema getSistema() {
        return sistema;
    }

    public void setSistema(PlanLocalSistema sistema) {
        this.sistema = sistema;
    }

    public List<PlanLocalSistema> getSistemas() {
        return sistemas;
    }

    public void setSistemas(List<PlanLocalSistema> sistemas) {
        this.sistemas = sistemas;
    }
}
