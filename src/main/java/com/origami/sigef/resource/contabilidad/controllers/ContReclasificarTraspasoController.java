/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ControlCuentaContableService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContReclasificarTraspaso;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContReclasificarTraspasoService;
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
@Named(value = "contReclasificarTraspasoView")
@ViewScoped
public class ContReclasificarTraspasoController implements Serializable {

    @Inject
    private ContReclasificarTraspasoService contReclasificarTraspasoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UserSession userSession;
    @Inject
    private ControlCuentaContableService controlCuentaContableService;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private ServletSession servletSession;

    private OpcionBusqueda busqueda, busqueda2;
    private ContReclasificarTraspaso contReclasificarTraspaso;

    private List<Short> listaPeriodo;

    private LazyModel<ContReclasificarTraspaso> reclasificacionLazy;
    private LazyModel<ContCuentas> contCuentasLazy;

    private Boolean edit, traspasar;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        busqueda2 = new OpcionBusqueda();
        busqueda.setAnio((short) (busqueda.getAnio() - 1));
        listaPeriodo = catalogoItemService.getPeriodo();
        edit = Boolean.FALSE;
        traspasar = Boolean.FALSE;
        updateLazyReclasificar();
    }

    public void loadPeriodos(Boolean accion) {
        if (accion) {
            busqueda2.setAnio((short) (busqueda.getAnio() + 1));
        } else {
            busqueda.setAnio((short) (busqueda2.getAnio() - 1));
        }
        updateLazyReclasificar();
    }

    public void loadSaldos() {
        if (busqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un periodo inicial a reclasificar");
            return;
        }
        if (busqueda2.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un periodo final a reclasificar");
            return;
        }
        if (busqueda2.getAnio().equals(busqueda.getAnio())) {
            JsfUtil.addWarningMessage("AVISO!!!", "El periodo final no debe ser igual al periodo inicial");
            return;
        }
        if (busqueda.getAnio() > busqueda2.getAnio()) {
            JsfUtil.addWarningMessage("AVISO!!!", "El periodo final no debe ser menor al periodo inicial");
            return;
        }
        if ((busqueda2.getAnio() - busqueda.getAnio()) > 1) {
            JsfUtil.addWarningMessage("AVISO!!!", "La diferencia entre periodos no debe ser mayor a 1 año");
            return;
        }
        if (controlCuentaContableService.getCierrePeriodo(busqueda.getAnio())) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe estar cerrado el periodo inicial seleccionado");
            return;
        }
        List<ContReclasificarTraspaso> temp = contReclasificarTraspasoService.findByNamedQuery("ContReclasificarTraspaso.findByPeriodos", busqueda.getAnio(), busqueda2.getAnio());
        if (temp.isEmpty()) {
            contReclasificarTraspasoService.loadData(userSession.getNameUser(), busqueda.getAnio(), busqueda2.getAnio());
            edit = Boolean.TRUE;
            JsfUtil.addInformationMessage("INFO!!", "Los saldos han sido cargados correctamente");
        } else {
            edit = Boolean.FALSE;
            JsfUtil.addInformationMessage("INFO!!", "Los saldos ya han sido traspasados correctamente");
        }
        updateLazyReclasificar();
        PrimeFaces.current().ajax().update("reclasificarTable");
    }

    public void updateLazyReclasificar() {
        reclasificacionLazy = new LazyModel<>(ContReclasificarTraspaso.class);
        reclasificacionLazy.getSorteds().put("idCuentaAnterior.codigo", "ASC");
        reclasificacionLazy.getFilterss().put("periodoInicial", busqueda.getAnio());
        reclasificacionLazy.getFilterss().put("periodoFinal", busqueda2.getAnio());
        reclasificacionLazy.setDistinct(false);
        List<ContReclasificarTraspaso> temp = contReclasificarTraspasoService.findByNamedQuery("ContReclasificarTraspaso.findByPeriodos", busqueda.getAnio(), busqueda2.getAnio());
        if (temp.isEmpty()) {
            edit = Boolean.TRUE;
        } else {
            edit = Boolean.FALSE;
        }
    }

    public void searchContCuenta(ContReclasificarTraspaso reclasificar) {
        contReclasificarTraspaso = reclasificar;
        if (contReclasificarTraspaso.getCodCuentaInsert() != null && !contReclasificarTraspaso.getCodCuentaInsert().equals("")) {
            contReclasificarTraspaso.setIdCuentaNueva(contCuentasService.findCodigo(contReclasificarTraspaso.getCodCuentaInsert()));
            if (contReclasificarTraspaso.getIdCuentaNueva() == null) {
                openDlg(true, reclasificar.getCodCuentaInsert());
            } else {
                if (contReclasificarTraspaso.getIdCuentaNueva().getMovimiento()) {
                    editRegistro();
                } else {
                    openDlg(true, reclasificar.getCodCuentaInsert());
                }
            }
        } else {
            openDlg(false, "");
        }
    }

    public void updateContCuentas(Boolean accion, String code) {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
        if (accion) {
            contCuentasLazy.getFilterss().put("codigo:startsWith", code);
        }
    }

    private void openDlg(Boolean accion, String code) {
        updateContCuentas(accion, code);
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
    }

    public void selectContCuenta(ContCuentas cuenta) {
        contReclasificarTraspaso.setIdCuentaNueva(cuenta);
        editRegistro();
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("reclasificarTable");
    }

    private void editRegistro() {
        contReclasificarTraspaso.setUsuarioModificacion(userSession.getNameUser());
        contReclasificarTraspaso.setFechaModificacion(new Date());
        contReclasificarTraspasoService.edit(contReclasificarTraspaso);
        contReclasificarTraspaso = new ContReclasificarTraspaso();
        JsfUtil.addInformationMessage("INFO!!", "Se ha cargado correctamente la cuenta contable");
    }

    public void consultar() {
        List<ContReclasificarTraspaso> temp = contReclasificarTraspasoService.findByNamedQuery("ContReclasificarTraspaso.findByPeriodos", busqueda.getAnio(), busqueda2.getAnio());
        if (temp.isEmpty()) {
            traspasar = Boolean.TRUE;
            updateLazyReclasificar();
            JsfUtil.addInformationMessage("INFO!!!", "Se puede traspasar los saldos dentro de los periodos seleccionados");
        } else {
            traspasar = Boolean.FALSE;
            JsfUtil.addWarningMessage("INFO!!!", "Los saldos ya se encuentran traspasados");
        }
        PrimeFaces.current().ajax().update("traspasarTable");
    }

    public void traspasar() {
        contReclasificarTraspasoService.getTraspasar(userSession.getNameUser(), busqueda.getAnio(), busqueda2.getAnio());
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha traspasado correctamente los saldos");
        PrimeFaces.current().ajax().update("traspasarTable");
    }

    public void generarReporte(String tipoArchivo) {
        if (busqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un periodo inicial a reclasificar");
            return;
        }
        if (busqueda2.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un periodo final a reclasificar");
            return;
        }
        servletSession.addParametro("periodo_inicial", busqueda.getAnio());
        servletSession.addParametro("periodo_final", busqueda2.getAnio());
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_contabilidad");
        servletSession.setNombreReporte("reclasificacion");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public OpcionBusqueda getBusqueda2() {
        return busqueda2;
    }

    public void setBusqueda2(OpcionBusqueda busqueda2) {
        this.busqueda2 = busqueda2;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public LazyModel<ContReclasificarTraspaso> getReclasificacionLazy() {
        return reclasificacionLazy;
    }

    public void setReclasificacionLazy(LazyModel<ContReclasificarTraspaso> reclasificacionLazy) {
        this.reclasificacionLazy = reclasificacionLazy;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

    public Boolean getTraspasar() {
        return traspasar;
    }

    public void setTraspasar(Boolean traspasar) {
        this.traspasar = traspasar;
    }

}
