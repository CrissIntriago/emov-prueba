/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThConfLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "thCuentasContablesView")
@ViewScoped
public class ThCuentasContablesController implements Serializable {

    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ThInterfaces thInterfaces;

    private LazyModel<ThServidorCargo> thServidorCargoLazyModel;
    private LazyModel<ContCuentas> contCuentasLazy;
    private LazyModel<ThRubro> thRubroLazyModel;

    private ThServidorCargo thServidorCargo;
    private OpcionBusqueda opcionBusqueda;
    private CatalogoItem clasificacion, contrato;
    private ThRegimenLaboral regimen;
    private ThConfLiquidacionRol rubro;

    private List<ThConfLiquidacionRol> thCargoRubrosList, thRubrosList;
    private List<Short> listaPeriodo;
    private List<CatalogoItem> clasificacionList, contratoList;
    private List<ThRegimenLaboral> regimenList;

    @PostConstruct
    public void init() {
        closeForm();
        actualizarLazy();
        listaPeriodo = catalogoItemService.getPeriodo();
        clasificacionList = catalogoItemService.findByCatalogo("tipoCargo");
        contratoList = catalogoItemService.findByCatalogo("th_tipo_contrato");
        regimenList = thInterfaces.getRegimenList();
        closeDlg();
    }

    public void actualizarLazy() {
        thServidorCargoLazyModel = new LazyModel<>(ThServidorCargo.class);
        thServidorCargoLazyModel.getSorteds().put("idServidor.persona.apellido", "ASC");
        thServidorCargoLazyModel.getFilterss().put("activo", true);
        thServidorCargoLazyModel.setDistinct(false);
    }

    public void closeForm() {
        thServidorCargo = new ThServidorCargo();
        opcionBusqueda = new OpcionBusqueda();
        thCargoRubrosList = new ArrayList<>();
    }

    public void addCuenta(ThCargoRubros thCargoRubros) {
        thCargoRubrosService.edit(thCargoRubros);
        JsfUtil.addSuccessMessage("INFO!!!", "Se registro la cuenta correctamente");
    }

    public void loadCargo(ThServidorCargo thServidorCargo) {
        this.thServidorCargo = thServidorCargo;
        thCargoRubrosList = thInterfaces.getCuentasRubroEgreso(thServidorCargo, opcionBusqueda.getAnio());
        JsfUtil.executeJS("PF('cargoDlg').show()");
        PrimeFaces.current().ajax().update("cargoForm");
    }

    public void openDlgParametros() {
        thCargoRubrosList = new ArrayList<>();
        closeDlg();
        JsfUtil.executeJS("PF('parametrosDlg').show()");
        PrimeFaces.current().ajax().update("parametrosForm");
    }

    private void closeDlg() {
        opcionBusqueda = new OpcionBusqueda();
        thServidorCargo = null;
        contrato = null;
        clasificacion = null;
        regimen = null;
        thRubrosList = new ArrayList<>();
    }

    public void rubrosXanio() {
        thCargoRubrosList = thInterfaces.getCuentasRubroEgreso(thServidorCargo, opcionBusqueda.getAnio());
    }

    public void updateContCuentas(Boolean accion) {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
        if (accion) {
            if (thServidorCargo != null) {
                if (thServidorCargo.getIdCargo() != null) {
                    if (thServidorCargo.getIdCargo().getIdCatalogoItem() != null) {
                        if (thServidorCargo.getIdCargo().getIdCatalogoItem().getCodigo() != null) {
                            contCuentasLazy.getFilterss().put("codigo:startsWith", "213".concat(thServidorCargo.getIdCargo().getIdCatalogoItem().getCodigo()));
                        }
                    }
                }
            }
        }

    }

    public void openDlgCuenta(ThConfLiquidacionRol thConfLiquidacionRol) {
        rubro = thConfLiquidacionRol;
        updateContCuentas(true);
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("contCuentasTable");
    }

    public void closeDlgCuenta(ContCuentas contCuentas) {
        rubro.setIdCuenta(contCuentas);
        rubro = new ThConfLiquidacionRol();
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("thCargosRubroTable");
        PrimeFaces.current().ajax().update("thRubroEgresoTable");
    }

    public void actualizarCuenta() {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo");
            return;
        }
        if (contrato == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un contrato");
            return;
        }
        if (clasificacion == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un tipo de clasificacion");
            return;
        }
        if (regimen == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar el regimen laboral");
            return;
        }
        for (ThConfLiquidacionRol item : thRubrosList) {
            if (item.getIdCuenta() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "El rubro " + item.getIdRubro().getNombre() + ", no tiene una cuenta contable asociado");
                return;
            }
        }
        for (ThConfLiquidacionRol item : thRubrosList) {
            thInterfaces.getUpdateRubroCuenta(item.getIdRubro(), item.getIdCuenta(), opcionBusqueda.getAnio(), contrato, clasificacion, regimen);
        }
        closeDlg();
        JsfUtil.executeJS("PF('parametrosDlg').hide()");
        JsfUtil.addSuccessMessage("INFO!!!", "Se actualizo correctamente");
    }

    public void openDlgRubro() {
        thRubroLazyModel = new LazyModel<>(ThRubro.class);
        thRubroLazyModel.getSorteds().put("nombre", "ASC");
        thRubroLazyModel.getFilterss().put("estado", true);
        thRubroLazyModel.getFilterss().put("activo", true);
        thRubroLazyModel.getFilterss().put("ingreso", false);
        JsfUtil.executeJS("PF('rubrosDlg').show()");
        PrimeFaces.current().ajax().update("rubroTable");
    }

    public void closeDlgRubro(ThRubro thRubro) {
        if (thRubrosList == null) {
            thRubrosList = new ArrayList<>();
        }
        ThConfLiquidacionRol rubro = new ThConfLiquidacionRol();
        rubro.setIdRubro(thRubro);
        thRubrosList.add(rubro);
        JsfUtil.executeJS("PF('rubrosDlg').hide()");
        PrimeFaces.current().ajax().update("thRubroEgresoTable");
    }

    public void guardarDetalle() {
        if (thCargoRubrosList != null) {
            if (thCargoRubrosList.isEmpty()) {
                JsfUtil.addWarningMessage("INFO!!", "Sin detalle para guardar");
                return;
            }
            for (ThConfLiquidacionRol item : thCargoRubrosList) {
                if (item.getIdCuenta() != null) {
                    thInterfaces.editThConfLiquidacionRol(item);
                }
            }
            JsfUtil.executeJS("PF('cargoDlg').hide()");
            closeDlg();
            JsfUtil.addSuccessMessage("INFO!!", "Datos cargados correctamente");
            thCargoRubrosList = new ArrayList<>();
        } else {
            JsfUtil.addWarningMessage("INFO!!", "Sin detalle para guardar");
        }
    }

    public void deleteRubro(int index) {
        thRubrosList.remove(index);
        JsfUtil.addSuccessMessage("INFO!!", "Se elimino correctamente");
        PrimeFaces.current().ajax().update("thRubroEgresoTable");
    }

    public LazyModel<ThServidorCargo> getThServidorCargoLazyModel() {
        return thServidorCargoLazyModel;
    }

    public void setThServidorCargoLazyModel(LazyModel<ThServidorCargo> thServidorCargoLazyModel) {
        this.thServidorCargoLazyModel = thServidorCargoLazyModel;
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

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

    public List<ThConfLiquidacionRol> getThCargoRubrosList() {
        return thCargoRubrosList;
    }

    public void setThCargoRubrosList(List<ThConfLiquidacionRol> thCargoRubrosList) {
        this.thCargoRubrosList = thCargoRubrosList;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public CatalogoItem getContrato() {
        return contrato;
    }

    public void setContrato(CatalogoItem contrato) {
        this.contrato = contrato;
    }

    public ThRegimenLaboral getRegimen() {
        return regimen;
    }

    public void setRegimen(ThRegimenLaboral regimen) {
        this.regimen = regimen;
    }

    public List<CatalogoItem> getClasificacionList() {
        return clasificacionList;
    }

    public void setClasificacionList(List<CatalogoItem> clasificacionList) {
        this.clasificacionList = clasificacionList;
    }

    public List<CatalogoItem> getContratoList() {
        return contratoList;
    }

    public void setContratoList(List<CatalogoItem> contratoList) {
        this.contratoList = contratoList;
    }

    public List<ThRegimenLaboral> getRegimenList() {
        return regimenList;
    }

    public void setRegimenList(List<ThRegimenLaboral> regimenList) {
        this.regimenList = regimenList;
    }

    public LazyModel<ThRubro> getThRubroLazyModel() {
        return thRubroLazyModel;
    }

    public void setThRubroLazyModel(LazyModel<ThRubro> thRubroLazyModel) {
        this.thRubroLazyModel = thRubroLazyModel;
    }

    public List<ThConfLiquidacionRol> getThRubrosList() {
        return thRubrosList;
    }

    public void setThRubrosList(List<ThConfLiquidacionRol> thRubrosList) {
        this.thRubrosList = thRubrosList;
    }

    public ThServidorCargo getThServidorCargo() {
        return thServidorCargo;
    }

    public void setThServidorCargo(ThServidorCargo thServidorCargo) {
        this.thServidorCargo = thServidorCargo;
    }

}
