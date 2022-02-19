/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.CatalogoProyecto;
import com.origami.sigef.Presupuesto.Service.CatalogoProyectoService;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.entities.PlanNacionalEje;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.PlanLocalObjetivoService;
import com.origami.sigef.contabilidad.service.PlanLocalPoliticaService;
import com.origami.sigef.contabilidad.service.PlanLocalSistemaService;
import com.origami.sigef.contabilidad.service.PlanNacionalEjeService;
import com.origami.sigef.contabilidad.service.PlanNacionalObjetivoService;
import com.origami.sigef.contabilidad.service.PlanNacionalPoliticaService;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "catalogoProyectoView")
@ViewScoped
public class CatalogoProyectoController implements Serializable {

    /*Inject Services*/
    @Inject
    private CatalogoProyectoService catalogoProyectoService;
    @Inject
    private PlanLocalSistemaService planLocalSistemaService;
    @Inject
    private PlanLocalObjetivoService planLocalObjetivoService;
    @Inject
    private PlanLocalPoliticaService planLocalPoliticaService;
    @Inject
    private PlanNacionalEjeService planNacionalEjeService;
    @Inject
    private PlanNacionalObjetivoService planNacionalObjetivoService;
    @Inject
    private PlanNacionalPoliticaService planNacionalPoliticaService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private UserSession userSession;

    /*Objetos*/
    private OpcionBusqueda opcionBusqueda;
    private CatalogoProyecto catalogoProyecto;
    private PlanLocalSistema planLocalSistemaSeleccionado;
    private PlanLocalObjetivo planLocalObjetivoSeleccionado;
    private PlanLocalPolitica planLocalPoliticaSeleccionado;
    private PlanNacionalEje planNacionalEjeSeleccionado;
    private PlanNacionalObjetivo planNacionalObjetivoSeleccionado;
    private PlanNacionalPolitica planNacionalPoliticaSeleccionado;

    /*Lista de Objetos*/
    private List<PlanLocalSistema> planLocalSistemaList;
    private List<PlanLocalObjetivo> planLocalObjetivoList;
    private List<PlanLocalPolitica> planLocalPoliticaList;
    private List<PlanNacionalEje> planNacionalEjeList;
    private List<PlanNacionalObjetivo> planNacionalObjetivoList;
    private List<PlanNacionalPolitica> planNacionalPoliticaList;
    private List<MasterCatalogo> periodos;

    /*Lazy Model*/
    private LazyModel<CatalogoProyecto> catalogoProyectoLazy;

    /*Constructor Inicializador*/
    @PostConstruct
    public void initializate() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.catalogoProyecto = new CatalogoProyecto();
        this.planLocalSistemaSeleccionado = new PlanLocalSistema();
        this.planLocalObjetivoSeleccionado = new PlanLocalObjetivo();
        this.planLocalPoliticaSeleccionado = new PlanLocalPolitica();
        this.planNacionalEjeSeleccionado = new PlanNacionalEje();
        this.planNacionalObjetivoSeleccionado = new PlanNacionalObjetivo();
        this.planNacionalPoliticaSeleccionado = new PlanNacionalPolitica();
        this.catalogoProyectoLazy = new LazyModel<>(CatalogoProyecto.class);
        this.catalogoProyectoLazy.getSorteds().put("id", "ASC");
        catalogoProyectoLazy.getFilterss().put("estado", true);
        catalogoProyectoLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        this.planLocalSistemaList = planLocalSistemaService.findByNamedQuery("PlanLocalSistema.findByEstado");
        this.planNacionalEjeList = planNacionalEjeService.findByNamedQuery("PlanNacionalEje.findByEstado");
    }

    /*Formulario*/
    public void form(CatalogoProyecto catalogo) throws ParseException {
        boolean existe = catalogoProyectoService.consultarExitenciaMasterCatalogo(opcionBusqueda.getAnio());
        if (existe) {
            if (catalogo != null) {
                /*Edita*/
                this.catalogoProyecto = catalogo;
            } else {
                /*Nuevo*/
                this.catalogoProyecto = new CatalogoProyecto();
                catalogoProyecto.setFechaVigencia(new Date());
                catalogoProyecto.setPeriodo(opcionBusqueda.getAnio());
            }
            PrimeFaces.current().executeScript("PF('catalogoProyectoDlg').show()");
            PrimeFaces.current().ajax().update("formEstructuraProgramatica");
        } else {
            JsfUtil.addWarningMessage("AVISO", "No esta creado un Catalogo de Presupuesto para el período " + opcionBusqueda.getAnio());
        }
    }

    /*Guardar*/
    public void save() {
        boolean edit = catalogoProyecto.getId() != null;
        if (edit) {
            /*Edita*/
            catalogoProyecto.setPlanLocal(planLocalPoliticaSeleccionado);
            catalogoProyecto.setPlanNacional(planNacionalPoliticaSeleccionado);
            catalogoProyecto.setUsuarioModificacion(userSession.getNameUser());
            catalogoProyecto.setFechaModificacion(new Date());
            catalogoProyectoService.edit(catalogoProyecto);
        } else {
            /*Guarda*/
            catalogoProyecto.setPlanLocal(planLocalPoliticaSeleccionado);
            catalogoProyecto.setPlanNacional(planNacionalPoliticaSeleccionado);
            catalogoProyecto.setUsuarioCreacion(userSession.getNameUser());
            catalogoProyecto.setFechaCreacion(new Date());
            catalogoProyecto = catalogoProyectoService.create(catalogoProyecto);
        }
        PrimeFaces.current().executeScript("PF('catalogoProyectoDlg').hide()");
        PrimeFaces.current().ajax().update("catalogoProyectoTable");
        JsfUtil.addSuccessMessage("Proyecto", catalogoProyecto.getDescripcion() + (edit ? " editado" : " registrado") + " con éxito.");
    }

    /*Eliminar*/
    public void delete(CatalogoProyecto catalogo) {
        catalogo.setEstado(Boolean.FALSE);
        catalogoProyectoService.edit(catalogo);
        JsfUtil.addSuccessMessage("Proyecto", catalogo.getDescripcion() + " eliminado con éxito");
        PrimeFaces.current().ajax().update("catalogoProyectoTable");
    }

    /*Actualizar plan local objetivo*/
    public void actualizarPlanLocalObjetivo() {
        this.planLocalObjetivoList = planLocalObjetivoService.findByNamedQuery("PlanLocalObjetivo.findByObjetivo", new Object[]{planLocalSistemaSeleccionado});
    }

    /*Actualizar plan local politica*/
    public void actualizarPlanLocalPolitica() {
        this.planLocalPoliticaList = planLocalPoliticaService.findByNamedQuery("PlanLocalPolitica.findByPolitica", new Object[]{planLocalObjetivoSeleccionado});
    }

    /*Actualizar plan nacional objetivo*/
    public void actualizarPlanNacionalObjetivo() {
        this.planNacionalObjetivoList = planNacionalObjetivoService.findByNamedQuery("PlanNacionalObjetivo.findByFiltroPolitica", new Object[]{planNacionalEjeSeleccionado});
    }

    /*Actualizar plan nacional politica*/
    public void actualizarPlanNacionalPolitica() {
        this.planNacionalPoliticaList = planNacionalPoliticaService.findByNamedQuery("PlanNacionalPolitica.findFiltrarPolitica", new Object[]{planNacionalObjetivoSeleccionado});
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public CatalogoProyecto getCatalogoProyecto() {
        return catalogoProyecto;
    }

    public void setCatalogoProyecto(CatalogoProyecto catalogoProyecto) {
        this.catalogoProyecto = catalogoProyecto;
    }

    public PlanLocalSistema getPlanLocalSistemaSeleccionado() {
        return planLocalSistemaSeleccionado;
    }

    public void setPlanLocalSistemaSeleccionado(PlanLocalSistema planLocalSistemaSeleccionado) {
        this.planLocalSistemaSeleccionado = planLocalSistemaSeleccionado;
    }

    public PlanLocalObjetivo getPlanLocalObjetivoSeleccionado() {
        return planLocalObjetivoSeleccionado;
    }

    public void setPlanLocalObjetivoSeleccionado(PlanLocalObjetivo planLocalObjetivoSeleccionado) {
        this.planLocalObjetivoSeleccionado = planLocalObjetivoSeleccionado;
    }

    public PlanLocalPolitica getPlanLocalPoliticaSeleccionado() {
        return planLocalPoliticaSeleccionado;
    }

    public void setPlanLocalPoliticaSeleccionado(PlanLocalPolitica planLocalPoliticaSeleccionado) {
        this.planLocalPoliticaSeleccionado = planLocalPoliticaSeleccionado;
    }

    public PlanNacionalEje getPlanNacionalEjeSeleccionado() {
        return planNacionalEjeSeleccionado;
    }

    public void setPlanNacionalEjeSeleccionado(PlanNacionalEje planNacionalEjeSeleccionado) {
        this.planNacionalEjeSeleccionado = planNacionalEjeSeleccionado;
    }

    public PlanNacionalObjetivo getPlanNacionalObjetivoSeleccionado() {
        return planNacionalObjetivoSeleccionado;
    }

    public void setPlanNacionalObjetivoSeleccionado(PlanNacionalObjetivo planNacionalObjetivoSeleccionado) {
        this.planNacionalObjetivoSeleccionado = planNacionalObjetivoSeleccionado;
    }

    public PlanNacionalPolitica getPlanNacionalPoliticaSeleccionado() {
        return planNacionalPoliticaSeleccionado;
    }

    public void setPlanNacionalPoliticaSeleccionado(PlanNacionalPolitica planNacionalPoliticaSeleccionado) {
        this.planNacionalPoliticaSeleccionado = planNacionalPoliticaSeleccionado;
    }

    public List<PlanLocalSistema> getPlanLocalSistemaList() {
        return planLocalSistemaList;
    }

    public void setPlanLocalSistemaList(List<PlanLocalSistema> planLocalSistemaList) {
        this.planLocalSistemaList = planLocalSistemaList;
    }

    public List<PlanLocalObjetivo> getPlanLocalObjetivoList() {
        return planLocalObjetivoList;
    }

    public void setPlanLocalObjetivoList(List<PlanLocalObjetivo> planLocalObjetivoList) {
        this.planLocalObjetivoList = planLocalObjetivoList;
    }

    public List<PlanLocalPolitica> getPlanLocalPoliticaList() {
        return planLocalPoliticaList;
    }

    public void setPlanLocalPoliticaList(List<PlanLocalPolitica> planLocalPoliticaList) {
        this.planLocalPoliticaList = planLocalPoliticaList;
    }

    public List<PlanNacionalEje> getPlanNacionalEjeList() {
        return planNacionalEjeList;
    }

    public void setPlanNacionalEjeList(List<PlanNacionalEje> planNacionalEjeList) {
        this.planNacionalEjeList = planNacionalEjeList;
    }

    public List<PlanNacionalObjetivo> getPlanNacionalObjetivoList() {
        return planNacionalObjetivoList;
    }

    public void setPlanNacionalObjetivoList(List<PlanNacionalObjetivo> planNacionalObjetivoList) {
        this.planNacionalObjetivoList = planNacionalObjetivoList;
    }

    public List<PlanNacionalPolitica> getPlanNacionalPoliticaList() {
        return planNacionalPoliticaList;
    }

    public void setPlanNacionalPoliticaList(List<PlanNacionalPolitica> planNacionalPoliticaList) {
        this.planNacionalPoliticaList = planNacionalPoliticaList;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public LazyModel<CatalogoProyecto> getCatalogoProyectoLazy() {
        return catalogoProyectoLazy;
    }

    public void setCatalogoProyectoLazy(LazyModel<CatalogoProyecto> catalogoProyectoLazy) {
        this.catalogoProyectoLazy = catalogoProyectoLazy;
    }
//</editor-fold>
}
