/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thPartidaPresupuestariaView")
@ViewScoped
public class ThPartidasPresupuestariaController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private PresPlanProgramaticoService preEstructuraProgramaticaService;
    @Inject
    private PresFuenteFinanciamientoService preFuenteFinanciamientoService;
    @Inject
    private PresCatalogoPresupuestarioService preCatalogoPresupuestoService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ThInterfaces thInterfaces;

    private LazyModel<ThCargo> thCargoLazyModel;

    private ThCargo thCargo;
    private OpcionBusqueda opcionBusqueda;
    private UnidadAdministrativa unidadFind;
    private CatalogoItem filtroContrato, filtroClasificacion;

    private List<ThCargoRubros> thCargoRubrosList;
    private List<Short> listaPeriodo;
    private List<CatalogoItem> listTipoCargo, tipoCargoList;
    private List<PresPlanProgramatico> preEstructuraProgramaticaList;
    private List<PresFuenteFinanciamiento> preFuenteList;
    private List<PresCatalogoPresupuestario> preCatalogoPresupuestoList;
    private List<String> thCargosList;
    private List<UnidadAdministrativa> unidadesAdministrativasList;

    private Boolean boolTramite;
    private String thCargoSeleccionado;

    @PostConstruct
    public void init() {
        closeForm();
        boolTramite = Boolean.FALSE;
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                boolTramite = Boolean.TRUE;
                opcionBusqueda.setAnio(cupoPresupuestoService.getAnio(tramite));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        actualizarLazy();
        listaPeriodo = catalogoItemService.getPeriodo();
        preEstructuraProgramaticaList = preEstructuraProgramaticaService.getEstructuraProgramatica("programatico_subprograma");
        preFuenteList = preFuenteFinanciamientoService.getFuenteFinanciamiento();
        thCargosList = thInterfaces.getCargosActivos();
        listTipoCargo = catalogoItemService.findByCatalogo("tipoCargo");
        tipoCargoList = catalogoItemService.findByCatalogo("th_tipo_contrato");
        unidadesAdministrativasList = thInterfaces.getUnidadesAdministrativas();
        closeDlg();
    }

    public void actualizarLazy() {
        //ThCargo
        thCargoLazyModel = new LazyModel<>(ThCargo.class);
        thCargoLazyModel.getSorteds().put("id", "ASC");
        thCargoLazyModel.getFilterss().put("estado", true);
        thCargoLazyModel.getFilterss().put("activo", true);
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void closeForm() {
        thCargo = new ThCargo();
        opcionBusqueda = new OpcionBusqueda();
        thCargoRubrosList = new ArrayList<>();
    }

    public void addCuenta(ThCargoRubros thCargoRubros) {
        if (thCargoRubros.getIdEstructura() != null && thCargoRubros.getIdPresupuesto() != null && thCargoRubros.getIdFuente() != null) {
            thCargoRubros.setPartidaPresupuestaria(thCargoRubros.getIdEstructura().getCodigo() + thCargoRubros.getIdPresupuesto().getCodigo() + thCargoRubros.getIdFuente().getCodFuente());
            thCargoRubros.setReformaCodificado(thCargoRubros.getMonto());
        }
        thCargoRubrosService.edit(thCargoRubros);
        JsfUtil.addSuccessMessage("INFO!!!", "Se registro la cuenta correctamente");
    }

    public void generarPartida(ThCargoRubros thCargoRubros) {
        if (thCargoRubros.getIdEstructura() != null && thCargoRubros.getIdPresupuesto() != null && thCargoRubros.getIdFuente() != null) {
            thCargoRubros.setPartidaPresupuestaria(thCargoRubros.getIdEstructura().getCodigo() + thCargoRubros.getIdPresupuesto().getCodigo() + thCargoRubros.getIdFuente().getCodFuente());
        }
    }

    public void addCuenta(ThRubro thRubro) {
        if (thRubro.getIdEstructura() != null && thRubro.getIdPresupuesto() != null && thRubro.getIdFuente() != null) {
            thRubro.setPartidaPresupuestaria(thRubro.getIdEstructura().getCodigo() + thRubro.getIdPresupuesto().getCodigo() + thRubro.getIdFuente().getCodFuente());

        }
    }

    public void loadCargo(ThCargo thCargo) {
        this.thCargo = thCargo;
        thCargoRubrosList = thCargoRubrosService.findByCargoXanioIngresos(this.thCargo, opcionBusqueda.getAnio());
        if (thCargo.getIdCatalogoItem() != null) {
            List<PresCatalogoPresupuestario> filterBy = preCatalogoPresupuestoService.findLikeCodigoPresupuesto(thCargo.getIdCatalogoItem().getCodigo());
            if (!filterBy.isEmpty()) {
                preCatalogoPresupuestoList = filterBy;
            }
        }
        JsfUtil.executeJS("PF('cargoDlg').show()");
        PrimeFaces.current().ajax().update("cargoForm");
    }

    public void rubrosXanio() {
        thCargoRubrosList = thCargoRubrosService.findByCargoXanioIngresos(this.thCargo, opcionBusqueda.getAnio());
    }

    public void openDlgParametros() {
        thCargoRubrosList = new ArrayList<>();
        closeDlg();
        JsfUtil.executeJS("PF('parametrosDlg').show()");
        PrimeFaces.current().ajax().update("parametrosForm");
    }

    public void findRubroXparametros() {
        if (opcionBusqueda.getAnio() != null && thCargoSeleccionado != null && !thCargoSeleccionado.equals("")) {
            thCargoRubrosList = thCargoRubrosService.findRubrosXParametros(opcionBusqueda.getAnio(), thCargoSeleccionado, unidadFind, filtroContrato, filtroClasificacion);
            System.out.println("filtroClasificacion: " + filtroClasificacion);
            if (filtroClasificacion != null && filtroClasificacion.getCodigo() != null) {
                preCatalogoPresupuestoList = preCatalogoPresupuestoService.presupuestoMovimiento(filtroClasificacion.getCodigo());
            } else {
                preCatalogoPresupuestoList = preCatalogoPresupuestoService.presupuestoMovimiento();
            }
            PrimeFaces.current().ajax().update("thrubrosTable");
        }
    }

    public void actualizarPartida() {
        System.out.println("1. " + thCargoRubrosList.isEmpty());
        System.out.println("2. " + opcionBusqueda.getAnio());
        System.out.println("3. " + thCargoSeleccionado);
        if (!thCargoRubrosList.isEmpty() && opcionBusqueda.getAnio() != null && thCargoSeleccionado != null && !thCargoSeleccionado.equals("")) {
            thInterfaces.getUpdateRubros(thCargoSeleccionado, thCargoRubrosList, true, opcionBusqueda.getAnio(), unidadFind, filtroContrato, filtroClasificacion);
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar los parametros de busqueda");
            return;
        }
        JsfUtil.executeJS("PF('parametrosDlg').hide()");
        closeDlg();
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("INFO!!!", "Se han actualizado los cargos correctamente");
    }

    private void closeDlg() {
        opcionBusqueda = new OpcionBusqueda();
        thCargoSeleccionado = null;
        unidadFind = null;
        filtroContrato = null;
        filtroClasificacion = null;
    }

    public void openDlg() {
        if (thCargoRubrosService.countPartidasAsignadas(opcionBusqueda.getAnio())) {
            JsfUtil.addWarningMessage("AVISO!!!", "Aun faltan partidas por asignar");
            return;
        }
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.titularTH)); //TALENTO HUMANO 14
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public LazyModel<ThCargo> getThCargoLazyModel() {
        return thCargoLazyModel;
    }

    public void setThCargoLazyModel(LazyModel<ThCargo> thCargoLazyModel) {
        this.thCargoLazyModel = thCargoLazyModel;
    }

    public ThCargo getThCargo() {
        return thCargo;
    }

    public void setThCargo(ThCargo thCargo) {
        this.thCargo = thCargo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<ThCargoRubros> getThCargoRubrosList() {
        return thCargoRubrosList;
    }

    public void setThCargoRubrosList(List<ThCargoRubros> thCargoRubrosList) {
        this.thCargoRubrosList = thCargoRubrosList;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<CatalogoItem> getListTipoCargo() {
        return listTipoCargo;
    }

    public void setListTipoCargo(List<CatalogoItem> listTipoCargo) {
        this.listTipoCargo = listTipoCargo;
    }

    public List<PresPlanProgramatico> getPreEstructuraProgramaticaList() {
        return preEstructuraProgramaticaList;
    }

    public void setPreEstructuraProgramaticaList(List<PresPlanProgramatico> preEstructuraProgramaticaList) {
        this.preEstructuraProgramaticaList = preEstructuraProgramaticaList;
    }

    public List<PresFuenteFinanciamiento> getPreFuenteList() {
        return preFuenteList;
    }

    public void setPreFuenteList(List<PresFuenteFinanciamiento> preFuenteList) {
        this.preFuenteList = preFuenteList;
    }

    public List<PresCatalogoPresupuestario> getPreCatalogoPresupuestoList() {
        return preCatalogoPresupuestoList;
    }

    public void setPreCatalogoPresupuestoList(List<PresCatalogoPresupuestario> preCatalogoPresupuestoList) {
        this.preCatalogoPresupuestoList = preCatalogoPresupuestoList;
    }

    public Boolean getBoolTramite() {
        return boolTramite;
    }

    public void setBoolTramite(Boolean boolTramite) {
        this.boolTramite = boolTramite;
    }

    public List<String> getThCargosList() {
        return thCargosList;
    }

    public void setThCargosList(List<String> thCargosList) {
        this.thCargosList = thCargosList;
    }

    public UnidadAdministrativa getUnidadFind() {
        return unidadFind;
    }

    public void setUnidadFind(UnidadAdministrativa unidadFind) {
        this.unidadFind = unidadFind;
    }

    public CatalogoItem getFiltroContrato() {
        return filtroContrato;
    }

    public void setFiltroContrato(CatalogoItem filtroContrato) {
        this.filtroContrato = filtroContrato;
    }

    public CatalogoItem getFiltroClasificacion() {
        return filtroClasificacion;
    }

    public void setFiltroClasificacion(CatalogoItem filtroClasificacion) {
        this.filtroClasificacion = filtroClasificacion;
    }

    public List<CatalogoItem> getTipoCargoList() {
        return tipoCargoList;
    }

    public void setTipoCargoList(List<CatalogoItem> tipoCargoList) {
        this.tipoCargoList = tipoCargoList;
    }

    public List<UnidadAdministrativa> getUnidadesAdministrativasList() {
        return unidadesAdministrativasList;
    }

    public void setUnidadesAdministrativasList(List<UnidadAdministrativa> unidadesAdministrativasList) {
        this.unidadesAdministrativasList = unidadesAdministrativasList;
    }

    public String getThCargoSeleccionado() {
        return thCargoSeleccionado;
    }

    public void setThCargoSeleccionado(String thCargoSeleccionado) {
        this.thCargoSeleccionado = thCargoSeleccionado;
    }

}
