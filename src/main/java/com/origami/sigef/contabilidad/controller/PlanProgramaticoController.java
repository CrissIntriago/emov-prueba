/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.NivelService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.PlanProgramaticoLazy;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
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
@Named(value = "planProgramaticoView")
@ViewScoped
public class PlanProgramaticoController implements Serializable {

    /**
     *
     */
    @Inject
    private UserSession userSession;

    private static final long serialVersionUID = 1L;
    @Inject
    private PlanProgramaticoService planService;
    @Inject
    private NivelService nivelService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoService catalogoService;

    private OpcionBusqueda opcionBusqueda;
    private PlanProgramaticoLazy lazy;
    private PlanProgramatico plan;
    private PlanProgramatico planSeleccionado;
    private List<Nivel> niveles;
    private List<MasterCatalogo> periodos;
    private List<CatalogoItem> clasificaciones;
    private int cantColumnas;
    private String columnClass;
    private List<PlanProgramatico> fechaCaducidad;

    @PostConstruct
    public void init() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.lazy = new PlanProgramaticoLazy(opcionBusqueda);
        this.niveles = nivelService.findByNamedQuery("Nivel.findByCatalogoAndCodigo", new Object[]{"plan_programatico", "NIVELES"});
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        this.plan = new PlanProgramatico();
        this.clasificaciones = catalogoService.getItemsByCatalogo("tipo_plan");
        cantColumnas = 4;
        columnClass = "ui-grid-col-3";
    }

    public void buscar() {
        lazy = new PlanProgramaticoLazy(opcionBusqueda);
    }

    public void cancelar() {
        opcionBusqueda = new OpcionBusqueda();
        lazy = new PlanProgramaticoLazy(opcionBusqueda);
    }

    public void form(PlanProgramatico p, boolean edit) {
        if (edit) {
            plan = p;

        } else {
            if (p.getNivel().getOrden() == 3) {
                JsfUtil.addWarningMessage("Cuenta Nivel " + p.getNivel().getOrden(), "No puede Asignar cuenta a " + p.getDescripcion());
                return;
            }
            plan = new PlanProgramatico();
            plan.setPadre(p);
            plan.setNivel(nivelService.getProximoNivel(p.getNivel()));
            plan.setPeriodo(p.getPeriodo());
            plan.setFechaVigente(fechaVigente());

            switch (p.getNivel().getOrden()) {
                case 1:
                    plan.setFuncion(p.getFuncion());
                    plan.setClasificacion((CatalogoItem) catalogoService.getTipoItem("PROG"));
                    plan.setPrograma(planService.getMaxValueForChild(p, false));
                    break;
                case 2:
                    plan.setFuncion(p.getFuncion());
                    plan.setPrograma(p.getPrograma());
                    plan.setClasificacion((CatalogoItem) catalogoService.getTipoItem("SUBPROG"));
                    plan.setSubprograma(planService.getMaxValueForChild(p, false));
            }
        }
        actulizarInfoGridFom();
        PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
        PrimeFaces.current().ajax().update(":formCuenta");
    }

    public void eliminar(PlanProgramatico plan) {

        List<PlanProgramatico> hijos = planService.getHijosByPadre(plan);

        if (hijos != null) {
            if (!hijos.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Plan Programatico", plan.getCodigo() + " tiene cuentas de movimientos asociadas.");
                return;
            }
        }
        JsfUtil.addSuccessMessage("Plan Programatico", plan.getCodigo() + " eliminada con éxito");
        plan.setEstado(Boolean.FALSE);
        planService.edit(plan);
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("messages");
    }

    public void formAdd() {

        try {
            plan = new PlanProgramatico();
            plan.setFechaVigente(fechaVigente());
            plan.setClasificacion((CatalogoItem) catalogoService.getTipoItem("FUNC"));
            plan.setNivel(nivelService.getFirstNivel("plan_programatico", "NIVELES"));
            plan.setPeriodo(opcionBusqueda.getAnio());
            plan.setFuncion(planService.getMaxValueForChild(plan, true));

            actulizarInfoGridFom();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
        PrimeFaces.current().ajax().update(":formCuenta");
    }

    public void guardar() {
        boolean edit = plan.getId() != null;
        try {
            plan.setCodigo(generarCodigo(plan));
            PlanProgramatico existeplan = planService.existeCuenta(plan);
            if (plan.getId() == null && existeplan != null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Plan Programatico", plan.getCodigo() + " se ecuentra registrado en el sistema.");
                return;
            }
            if (plan.getId() != null && existeplan != null) {
                if (!Objects.equals(plan.getId(), existeplan.getId())) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("Plan Programatico", plan.getCodigo() + " se ecuentra registrado en el sistema.");
                    return;
                }
            }
            if (plan.getId() == null) {
                plan.setUsuarioCreacion(userSession.getNameUser());
                plan.setFechaCreacion(new Date());
                planSeleccionado = planService.create(plan);
            } else {
                plan.setUsuarioModifica(userSession.getNameUser());
                plan.setFechaModificacion(new Date());
                planService.edit(plan);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('cuentaDialog').hide()");
        PrimeFaces.current().ajax().update(":cuentas");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Plan Programatico", plan.getCodigo() + (edit ? " editado" : " registrado") + " con éxito.");
    }

    public void handleCloseForm(CloseEvent event) {
//        PrimeFaces.current().ajax().update("mostrarColumnas");
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("formCuenta");
    }

    private String generarCodigo(PlanProgramatico p) {

        if (p.getPadre() != null) {
            return p.getPadre().getCodigo() + getSubCodigo(p);
        }

        return getSubCodigo(p);
    }

    private void actulizarInfoGridFom() {

        switch (plan.getNivel().getOrden()) {
            case 1:
                cantColumnas = 2;
                columnClass = "ui-grid-col-2";
                break;
            case 2:
            case 3:
                cantColumnas = 2;
                columnClass = "ui-grid-col-2";
                break;
        }

    }

    private String getSubCodigo(PlanProgramatico p) {

        String format = "%0" + p.getNivel().getLongitud() + "d";
        switch (p.getNivel().getOrden()) {

            case 1: {
                return String.format(format, p.getFuncion());
            }
            case 2: {
                return String.format(format, p.getPrograma());
            }
            case 3: {
                return String.format(format, p.getSubprograma());
            }
        }
        return "";
    }

    /**
     *
     * @return
     */
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
        boolean edit = plan.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    public boolean renderElementForm(PlanProgramatico pp, int position) {

        if (pp.getNivel() != null) {
            return position <= pp.getNivel().getOrden();
        }
        return false;
    }

    public boolean readOnlyElementForm(PlanProgramatico pp, int position) {

        return position != pp.getNivel().getOrden();

    }

    public NivelService getNivelService() {
        return nivelService;
    }

    public void setNivelService(NivelService nivelService) {
        this.nivelService = nivelService;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<PlanProgramatico> getLazy() {
        return lazy;
    }

    public void setLazy(PlanProgramaticoLazy lazy) {
        this.lazy = lazy;
    }

    public PlanProgramatico getPlan() {
        return plan;
    }

    public void setPlan(PlanProgramatico plan) {
        this.plan = plan;
    }

    public PlanProgramatico getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(PlanProgramatico planSeleccionado) {
        this.planSeleccionado = planSeleccionado;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<CatalogoItem> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public int getCantColumnas() {
        return cantColumnas;
    }

    public void setCantColumnas(int cantColumnas) {
        this.cantColumnas = cantColumnas;
    }

    public String getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public PlanProgramaticoService getPlanService() {
        return planService;
    }

    public void setPlanService(PlanProgramaticoService planService) {
        this.planService = planService;
    }

    public MasterCatalogoService getMasterCatalogoService() {
        return masterCatalogoService;
    }

    public void setMasterCatalogoService(MasterCatalogoService masterCatalogoService) {
        this.masterCatalogoService = masterCatalogoService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

}
