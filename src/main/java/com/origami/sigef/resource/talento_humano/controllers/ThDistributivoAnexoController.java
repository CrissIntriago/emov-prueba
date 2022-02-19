/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
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
@Named(value = "thDistributivoAnexoView")
@ViewScoped
public class ThDistributivoAnexoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaPresupuestoService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private UserSession userSession;
    @Inject
    private PresPlanProgramaticoService preEstructuraProgramaticaService;
    @Inject
    private PresFuenteFinanciamientoService preFuenteFinanciamientoService;
    @Inject
    private PresCatalogoPresupuestarioService preCatalogoPresupuestoService;
    @Inject
    private ThInterfaces thInterfaces;

    private OpcionBusqueda opcionBusqueda;
    private ThCargoRubros thCargoRubros;

    private List<Short> listaPeriodo;
    private List<PresPlanProgramatico> preEstructuraProgramaticaList;
    private List<PresFuenteFinanciamiento> preFuenteList;
    private List<PresCatalogoPresupuestario> preCatalogoPresupuestoList;

    private LazyModel<ThCargoRubros> thCargoRubroLazyModel;
    private LazyModel<ThCargoRubros> thCargoRubroViewLazyModel;
    private LazyModel<ThRubro> thRubroLazyModel;
    private LazyModel<ContCuentas> contCuentasLazy;

    private Boolean aprobado, view, editTable;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
        actualizarDataTable();
        updateDataTableView(true);
        closeForm();
        determinarCambios();
        thRubroLazyModel = new LazyModel<>(ThRubro.class);
        thRubroLazyModel.getSorteds().put("nombre", "ASC");
        thRubroLazyModel.getFilterss().put("ingreso", true);
        thRubroLazyModel.getFilterss().put("origen", false);
        thRubroLazyModel.getFilterss().put("estado", true);
        preEstructuraProgramaticaList = preEstructuraProgramaticaService.getEstructuraProgramatica("programatico_subprograma");
        preFuenteList = preFuenteFinanciamientoService.getFuenteFinanciamiento();
        preCatalogoPresupuestoList = preCatalogoPresupuestoService.presupuestoMovimiento();
    }

    public void determinarCambios() {
        consultarAprobacion(opcionBusqueda.getAnio());
        if (aprobado) {
            JsfUtil.addWarningMessage("AVISO!!!", "El presupuesto del periodo seleccionado ya se encuentra aprobado, por lo tanto no puede hacer modificaciones");
            editTable = false;
        } else {
            editTable = true;
        }
    }

    public void closeForm() {
        thCargoRubros = new ThCargoRubros();
        view = false;
    }

    public void updateDataTableView(Boolean accion) {
        if (opcionBusqueda.getAnio() != null) {
            thCargoRubroViewLazyModel = new LazyModel<>(ThCargoRubros.class);
            thCargoRubroViewLazyModel.getSorteds().put("descripcion", "ASC");
            thCargoRubroViewLazyModel.getFilterss().put("idRubro.ingreso", true);
            thCargoRubroViewLazyModel.getFilterss().put("idRubro.origen", false);
            thCargoRubroViewLazyModel.getFilterss().put("estado", true);
            thCargoRubroViewLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
            if (accion) {
                determinarCambios();
            }
        } else {
            thCargoRubroViewLazyModel = null;
        }
    }

    public void actualizarDataTable() {
        if (opcionBusqueda.getAnio() != null) {
            thCargoRubroLazyModel = new LazyModel<>(ThCargoRubros.class);
            thCargoRubroLazyModel.getSorteds().put("descripcion", "ASC");
            thCargoRubroLazyModel.getFilterss().put("idRubro.ingreso", true);
            thCargoRubroLazyModel.getFilterss().put("idRubro.origen", false);
            thCargoRubroLazyModel.getFilterss().put("estado", true);
            thCargoRubroLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        } else {
            thCargoRubroLazyModel = null;
        }
    }

    public void generarPartida(ThCargoRubros thCargoRubros) {
        if (thCargoRubros.getIdEstructura() != null && thCargoRubros.getIdPresupuesto() != null && thCargoRubros.getIdFuente() != null) {
            thCargoRubros.setPartidaPresupuestaria(thCargoRubros.getIdEstructura().getCodigo() + thCargoRubros.getIdPresupuesto().getCodigo() + thCargoRubros.getIdFuente().getCodFuente());
        }
        thCargoRubrosService.edit(thCargoRubros);
    }

    public void save() {
        if (thCargoRubros.getIdRubro() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un rubro");
            return;
        }
        if (thCargoRubros.getDescripcion() == null || thCargoRubros.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripción");
            return;
        }
        if (thCargoRubros.getProyeccion() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una proyeccion");
            return;
        }
        if (thCargoRubros.getMonto() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un monto");
            return;
        }
        Boolean edit = thCargoRubros.getId() != null;
        if (edit) {
            thCargoRubros.setUsuarioModificacion(userSession.getNameUser());
            thCargoRubros.setFechaModificacion(new Date());
            thCargoRubrosService.edit(thCargoRubros);
        } else {
            thCargoRubros.setUsuarioCreacion(userSession.getNameUser());
            thCargoRubros.setFechaCreacion(new Date());
            thCargoRubros.setPeriodo(opcionBusqueda.getAnio());
            thCargoRubrosService.create(thCargoRubros);
        }
        JsfUtil.executeJS("PF('thDistributivoAnexoDlg').hide()");
        PrimeFaces.current().ajax().update("thDistributivoAnexoForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void openDlgAnexo(ThCargoRubros thCargoRubros, Boolean view) {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
            return;
        }
        if (!view) {
            consultarAprobacion(opcionBusqueda.getAnio());
            if (aprobado) {
                JsfUtil.addWarningMessage("AVISO!!!", "No puede agregar un rubro en este periodo debido a que ya esta aprobado el presupuesto del periodo seleccionado");
                return;
            }
        }
        closeForm();
        this.view = view;
        if (thCargoRubros != null) {
            this.thCargoRubros = thCargoRubros;
        }
        JsfUtil.executeJS("PF('thDistributivoAnexoDlg').show()");
        PrimeFaces.current().ajax().update("thDistributivoAnexoForm");
    }

    public void delete(ThCargoRubros thCargoRubros) {
        consultarAprobacion(opcionBusqueda.getAnio());
        if (aprobado) {
            JsfUtil.addWarningMessage("AVISO!!!", "No puede agregar un rubro en este periodo debido a que ya esta aprobado el presupuesto del periodo seleccionado");
            return;
        }
        thCargoRubros.setUsuarioModificacion(userSession.getNameUser());
        thCargoRubros.setFechaModificacion(new Date());
        thCargoRubrosService.edit(thCargoRubros);
    }

    private void consultarAprobacion(Short periodo) {
        aprobado = catalogoProformaPresupuestoService.presupuestoEgresoAprobado(periodo);
    }

    public void openDlgRubro() {
        JsfUtil.executeJS("PF('thRubroDlg').show()");
        PrimeFaces.current().ajax().update("thRubroForm");
    }

    public void closeDlgRubro(ThRubro thRubro) {
        thCargoRubros.setIdRubro(thRubro);
        JsfUtil.executeJS("PF('thRubroDlg').hide()");
        PrimeFaces.current().ajax().update("thRubroForm");
        PrimeFaces.current().ajax().update("fieldsetRubro");
        JsfUtil.addSuccessMessage("AVISO!!!", "Se ha agregado correctamente");
    }

    public void openDlgCuenta(ThCargoRubros thCargoRubros) {
        this.thCargoRubros = thCargoRubros;
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
        PrimeFaces.current().ajax().update("contCuentasTable");
    }

    public void closeDlgCuenta(ContCuentas contCuentas) {
        this.thCargoRubros.setIdCuenta(contCuentas);
        thCargoRubrosService.edit(thCargoRubros);
        this.thCargoRubros = new ThCargoRubros();
        JsfUtil.addSuccessMessage("AVISO!!!", "Se ha agregado correctamente");
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("thDistributivoAnexoTable");
    }

    public void quitarCuenta(ThCargoRubros thCargoRubros) {
        if (thCargoRubros.getIdCuenta() != null) {
            thCargoRubros.setIdCuenta(null);
            thCargoRubrosService.edit(thCargoRubros);
            JsfUtil.addSuccessMessage("AVISO!!!", "Se ha quitado correctamente");
        } else {
            JsfUtil.addSuccessMessage("AVISO!!!", "No hay cuenta contable");
        }
        PrimeFaces.current().ajax().update("thDistributivoAnexoTable");
    }

    public void actualizarCuenta(ThCargoRubros thCargoRubros) {
        boolean accion = thInterfaces.getCuentaPartidaRubro(thCargoRubros);
        if (accion) {
            JsfUtil.addSuccessMessage("AVISO!!!", "Se ha actualizado correctamente");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se encontro cuenta contable asociada a la partida, comuniquese con contabilidad");
        }
        PrimeFaces.current().ajax().update("thDistributivoAnexoTable");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public LazyModel<ThCargoRubros> getThCargoRubroLazyModel() {
        return thCargoRubroLazyModel;
    }

    public void setThCargoRubroLazyModel(LazyModel<ThCargoRubros> thCargoRubroLazyModel) {
        this.thCargoRubroLazyModel = thCargoRubroLazyModel;
    }

    public ThCargoRubros getThCargoRubros() {
        return thCargoRubros;
    }

    public void setThCargoRubros(ThCargoRubros thCargoRubros) {
        this.thCargoRubros = thCargoRubros;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public LazyModel<ThRubro> getThRubroLazyModel() {
        return thRubroLazyModel;
    }

    public void setThRubroLazyModel(LazyModel<ThRubro> thRubroLazyModel) {
        this.thRubroLazyModel = thRubroLazyModel;
    }

    public LazyModel<ThCargoRubros> getThCargoRubroViewLazyModel() {
        return thCargoRubroViewLazyModel;
    }

    public void setThCargoRubroViewLazyModel(LazyModel<ThCargoRubros> thCargoRubroViewLazyModel) {
        this.thCargoRubroViewLazyModel = thCargoRubroViewLazyModel;
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

    public Boolean getEditTable() {
        return editTable;
    }

    public void setEditTable(Boolean editTable) {
        this.editTable = editTable;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

}
