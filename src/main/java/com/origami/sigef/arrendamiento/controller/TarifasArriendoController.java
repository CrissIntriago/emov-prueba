/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.controller;

import com.origami.sigef.arrendamiento.service.TarifasArriendoService;
import com.origami.sigef.arrendamiento.entities.TarifasArriendo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "tarifasArriendoView")
@ViewScoped
public class TarifasArriendoController implements Serializable {
    
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private TarifasArriendoService tarifasArriendoService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private PresupuestoService presupuestoService;
    
    private TarifasArriendo tarifasArriendo;
    private OpcionBusqueda opcionBusqueda;
    private ContCuentas cuentaContableSeleccionado;
    
    private Boolean ver;
    private int codigoAccion;
    private Short periodoSeleccionado;
    
    private List<CatalogoItem> canonArrendamientoList;
    private List<Presupuesto> partidas;
    private List<MasterCatalogo> listaPeriodo;
    
    private LazyModel<TarifasArriendo> tarifasArriendoLazyModel;
    private LazyModel<CuentaContable> cuentaContableLazyModel;
    
    @PostConstruct
    public void initialize() {
        this.tarifasArriendo = new TarifasArriendo();
        this.opcionBusqueda = new OpcionBusqueda();
        this.tarifasArriendoLazyModel = new LazyModel<>(TarifasArriendo.class);
        this.tarifasArriendoLazyModel.getSorteds().put("codigo", "ASC");
        this.tarifasArriendoLazyModel.getFilterss().put("estado", true);
        this.canonArrendamientoList = catalogoService.getItemsByCatalogo("canon_arrendamiento");
    }
    
    public void form(TarifasArriendo tarifasArriendo, Boolean accion) {
        this.ver = accion;
        this.tarifasArriendo = new TarifasArriendo();
        this.partidas = new ArrayList<>();
        if (tarifasArriendo != null) {
            this.tarifasArriendo = tarifasArriendo;
            if (tarifasArriendo.getCuentaIngreso() != null) {
                codigoAccion = 2;
                cuentaContableSeleccionado = tarifasArriendo.getCuentaIngreso();
                aniadirCuentaContable();
            }
        }
        PrimeFaces.current().executeScript("PF('tarifasArriendoDlg').show()");
        PrimeFaces.current().ajax().update("formTarifasArriendo");
    }
    
    public void saveEdit() {
        if ((tarifasArriendo.getAreaDesde() != null && tarifasArriendo.getAreaHasta() == null) || (tarifasArriendo.getAreaDesde() == null && tarifasArriendo.getAreaHasta() != null)) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe llenar el campo faltante en los rangos de área");
            return;
        }
        if (tarifasArriendo.getAreaDesde().doubleValue() > tarifasArriendo.getAreaHasta().doubleValue()) {
            JsfUtil.addWarningMessage("AVISO!!!", "El área desde no debe ser mayor al del área hasta");
            return;
        }
        if (tarifasArriendo.getCuentaCobro() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una cuenta por cobrar");
            return;
        }
        if (tarifasArriendo.getCuentaIngreso() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una cuenta de ingreso");
            return;
        }
        if (tarifasArriendo.getCuentaBanco() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una cuenta de banco");
            return;
        }
        if (tarifasArriendo.getItemPresupuesto() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una partida");
            return;
        }
        boolean edit = tarifasArriendo.getId() != null;
        if (edit) {
            edit(tarifasArriendo);
        } else {
            tarifasArriendo.setUsuarioCreacion(userSession.getNameUser());
            tarifasArriendo = tarifasArriendoService.create(tarifasArriendo);
        }
        PrimeFaces.current().executeScript("PF('tarifasArriendoDlg').hide()");
        PrimeFaces.current().ajax().update("tarifasArriendoTable");
        JsfUtil.addSuccessMessage("Tarifa de Arrendamiento ", (edit ? "Editado" : " Registrado") + " con éxito.");
    }
    
    public void delete(TarifasArriendo tarifasArriendo) {
        if (tarifasArriendoService.findRelacionados(tarifasArriendo)) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede eliminar porque tiene relaciones con los locales");
        }
        tarifasArriendo.setEstado(Boolean.FALSE);
        edit(tarifasArriendo);
        JsfUtil.addSuccessMessage("Arrendamiento", "Eliminada con éxito");
    }
    
    public void edit(TarifasArriendo tarifasArriendo) {
        tarifasArriendo.setFechaModificacion(new Date());
        tarifasArriendo.setUsuarioModificacion(userSession.getNameUser());
        tarifasArriendoService.edit(tarifasArriendo);
    }
    
    public void openDlgCuentaContable(int codigo) {
        //this.tipoCuenta = accion;
        this.codigoAccion = codigo;
        this.cuentaContableLazyModel = new LazyModel<>(CuentaContable.class);
        this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
        this.cuentaContableLazyModel.getFilterss().put("estado", true);
        this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        switch (codigoAccion) {
            case 1:
                this.cuentaContableLazyModel.getFilterss().put("titulo", 1);
                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 3);
                break;
            case 2:
                this.cuentaContableLazyModel.getFilterss().put("titulo", 6);
                this.cuentaContableLazyModel.getFilterss().put("grupo", 2);
                break;
            case 4:
                this.cuentaContableLazyModel.getFilterss().put("titulo", 1);
                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 2);
                break;
            case 5:
                this.cuentaContableLazyModel.getFilterss().put("titulo", 2);
                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 2);
                break;
        }
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
        PrimeFaces.current().ajax().update("cuentasPresupuestarioTable");
    }
    
    public void aniadirCuentaContable() {
        switch (codigoAccion) {
            case 1:
                tarifasArriendo.setCuentaCobro(cuentaContableSeleccionado);
                break;
            case 2:
                partidas.clear();
                tarifasArriendo.setCuentaIngreso(cuentaContableSeleccionado);
//                Presupuesto catalogo = catalogoPresupuestoService.getpartida(cuentaContableSeleccionado);
//                partidas.add(catalogo);
                PrimeFaces.current().ajax().update("partida");
                break;
            case 3:
                tarifasArriendo.setCuentaBanco(cuentaContableSeleccionado);
                break;
            case 4:
                tarifasArriendo.setCuentaIvaIngreso(cuentaContableSeleccionado);
                break;
            case 5:
                tarifasArriendo.setCuentaIvaEgreso(cuentaContableSeleccionado);
                break;
        }
        cuentaContableSeleccionado = new ContCuentas();
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
    }
    
    public void openDlgActualizar() {
        this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        if (listaPeriodo != null) {
            int indice = listaPeriodo.size();
            if (!listaPeriodo.isEmpty() && indice == 1) {
                periodoSeleccionado = listaPeriodo.get(0).getAnio();
            } else {
                periodoSeleccionado = listaPeriodo.get(indice - 1).getAnio();
            }
        } else {
            this.periodoSeleccionado = opcionBusqueda.getAnio();
        }
        PrimeFaces.current().ajax().update("listPeriodos");
        PrimeFaces.current().executeScript("PF('actualizarDlg').show()");
    }
    
    public void closeDlgActualizar() {
        if (periodoSeleccionado == 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un período");
            return;
        }
        List<TarifasArriendo> temp = tarifasArriendoService.getTarifasList();
        for (TarifasArriendo index : temp) {
//            index.setCuentaCobro(getCuenta(index.getCuentaCobro()));
//            index.setCuentaBanco(getCuenta(index.getCuentaBanco()));
//            index.setCuentaIngreso(getCuenta(index.getCuentaIngreso()));
//            index.setCuentaIvaIngreso(getCuenta(index.getCuentaIvaIngreso()));
//            index.setCuentaIvaEgreso(getCuenta(index.getCuentaIvaEgreso()));
            index.setItemPresupuesto(presupuestoService.findPresupuestoByCodigoAndPerido(index.getItemPresupuesto().getPartida(), periodoSeleccionado));
            tarifasArriendoService.edit(index);
        }
        PrimeFaces.current().ajax().update("cuentasPresupuestarioTable");
        PrimeFaces.current().executeScript("PF('actualizarDlg').hide()");
        JsfUtil.addSuccessMessage("INFO!!!", "Se actualizaron los registros de las tarifas de arriendo");
    }
    
    private CuentaContable getCuenta(CuentaContable cuenta) {
        if (cuenta != null) {
            return cuentaContableService.findCuentaContableByCodigoAndPerido(cuenta.getCodigo(), periodoSeleccionado);
        } else {
            return null;
        }
    }
    
    public TarifasArriendo getTarifasArriendo() {
        return tarifasArriendo;
    }
    
    public void setTarifasArriendo(TarifasArriendo tarifasArriendo) {
        this.tarifasArriendo = tarifasArriendo;
    }
    
    public Boolean getVer() {
        return ver;
    }
    
    public void setVer(Boolean ver) {
        this.ver = ver;
    }
    
    public List<CatalogoItem> getCanonArrendamientoList() {
        return canonArrendamientoList;
    }
    
    public void setCanonArrendamientoList(List<CatalogoItem> canonArrendamientoList) {
        this.canonArrendamientoList = canonArrendamientoList;
    }
    
    public LazyModel<TarifasArriendo> getTarifasArriendoLazyModel() {
        return tarifasArriendoLazyModel;
    }
    
    public void setTarifasArriendoLazyModel(LazyModel<TarifasArriendo> tarifasArriendoLazyModel) {
        this.tarifasArriendoLazyModel = tarifasArriendoLazyModel;
    }
    
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }
    
    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
    
    public ContCuentas getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }
    
    public void setCuentaContableSeleccionado(ContCuentas cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }
    
    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }
    
    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }
    
    public List<Presupuesto> getPartidas() {
        return partidas;
    }
    
    public void setPartidas(List<Presupuesto> partidas) {
        this.partidas = partidas;
    }
    
    public Short getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }
    
    public void setPeriodoSeleccionado(Short periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }
    
    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }
    
    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
    
}
