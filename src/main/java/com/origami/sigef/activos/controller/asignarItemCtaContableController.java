/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Luis Pozo Gonzabay
 * @author Sandra Arroba
 */
@Named(value = "asignarItemCtaContableView")
@ViewScoped
public class asignarItemCtaContableController implements Serializable {

    private static final Logger LOG = Logger.getLogger(asignarItemCtaContableController.class.getName());

    private DetalleItem detalleItem;
    private LazyModel<DetalleItem> lazyDetalleItem;
    private ContCuentas cuentaContable, contraCuenta;
    private List<ContCuentas> listTiposGastos;
    private List<DetalleItem> listItemsSeleccionados, listItems;
    private Boolean agregarCuenta = false;
    private Boolean visualizar, tipoRegistro;
    private Integer tipoBusqueda;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private ServletSession ss;

    @PostConstruct
    public void initView() {
        this.detalleItem = new DetalleItem();
        this.detalleItem.setCuentaContable(new ContCuentas());
        this.detalleItem.setContraCuenta(new ContCuentas());

        lazyDetalleItem = new LazyModel<>(DetalleItem.class);
        cuentaContable = new ContCuentas();
        contraCuenta = new ContCuentas();
        lazyDetalleItem.getFilterss().put("estado", Boolean.TRUE);
        lazyDetalleItem.getFilterss().put("cuentaContable:noequal", null);
        lazyDetalleItem.getSorteds().put("codigo", "ASC");
        visualizar = Boolean.TRUE;
        listTiposGastos = detalleItemService.getTipoGastos("13", Arrays.asList("15138", "15238"), "911");
        listItemsSeleccionados = new ArrayList<>();
    }

    public void buscarItem() {
        try {
            if (detalleItem.getCodigoAgrupado()!= null) {
                DetalleItem item = detalleItemService.getItem(detalleItem.getCodigoAgrupado());
                if (item != null) {
                    this.detalleItem = item;
                } else {
                    JsfUtil.addWarningMessage("Información", "Ya tiene cuenta asignada o no existe Item.");
                    Utils.openDialog("/facelet/activos/inventario/mantenimientoInventario/dialogItem", "65%", "55%");
                }
            } else {
                Utils.openDialog("/facelet/activos/inventario/mantenimientoInventario/dialogItem", "65%", "55%");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarCuenta(boolean contracuenta, boolean item) {
        try {
            if (item) {
                if (detalleItem.getId() == null) {
                    JsfUtil.addWarningMessage("Advertencia", "Ingrese el item");
                    return;
                }
            }
            if (contracuenta) {//CONTRA CUENTA
                if (detalleItem.getCuentaContable() == null) {
                    JsfUtil.addWarningMessage("Advertencia", "Ingrese la cuenta contable.");
                    return;
                }
                if (contraCuenta.getCodigo() != null) {
                    ContCuentas cta = null;
                    if (cuentaContable.getCodigo().startsWith("131")) {
                        if (contraCuenta.getCodigo().startsWith("63408") || contraCuenta.getCodigo().startsWith("63450")) {
                            cta = detalleItemService.getCuentaContableGastos(contraCuenta.getCodigo());
                            if (cta != null) {
                                JsfUtil.addInformationMessage("Información", "Cuenta contable con código " + cta.getCodigo() + " encontrada con éxito");
                                detalleItem.setContraCuenta(cta);
                            } else {
                                Map<String, List<String>> params = new HashMap<>();
                                params.put("CODIGO", Arrays.asList("63408", "63450"));
                                params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
                                Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "45%", params);
                            }
                        } else {
                            Map<String, List<String>> params = new HashMap<>();
                            params.put("CODIGO", Arrays.asList("63408", "63450"));
                            params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
                            Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "45%", params);
                        }
                    } else {
                        cta = detalleItemService.getCuentaContableGastos(contraCuenta.getCodigo());
                        if (cta != null) {
                            JsfUtil.addInformationMessage("Información", "Cuenta contable con código " + cta.getCodigo() + " encontrada con éxito");
                            detalleItem.setContraCuenta(cta);
                        } else {
                            Map<String, List<String>> params = new HashMap<>();
                            params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
                            Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "45%", params);
                        }
                    }
                } else {
                    Map<String, List<String>> params = new HashMap<>();
                    if (cuentaContable.getCodigo().startsWith("131")) {
                        params.put("CODIGO", Arrays.asList("63408", "63450"));
                    }
                    params.put("CODIGO", Arrays.asList(""));
                    params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
                    Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "45%", params);
                }
            } else {//CUENTA CONTABLE
                if (detalleItem.getTipoGasto() == null) {
                    JsfUtil.addWarningMessage("Advertencia", "Ingrese el tipo de gasto");
                    return;
                }
                if (cuentaContable.getCodigo() != null) {
                    ContCuentas cta = detalleItemService.getCuentaContable(cuentaContable.getCodigo(), detalleItem.getTipoGasto().getCodigo());
                    if (cta != null) {
                        JsfUtil.addInformationMessage("Información", "Cuenta contable con código " + cta.getCodigo() + " encontrada con éxito");
                        this.detalleItem.setCuentaContable(cta);
                    } else {
                        Map<String, List<String>> params = new HashMap<>();
                        params.put("CODIGO", Arrays.asList(detalleItem.getTipoGasto().getCodigo()));
                        if (detalleItem.getTipoGasto().getCodigo().equals("911")) {
                            params.put("CODEND", Arrays.asList("17"));
                        }
                        params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
                        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "45%", params);
                    }
                } else {
                    Map<String, List<String>> params = new HashMap<>();
                    params.put("CODIGO", Arrays.asList(detalleItem.getTipoGasto() != null ? detalleItem.getTipoGasto().getCodigo() : ""));
                    if (detalleItem.getTipoGasto() != null) {
                        if (detalleItem.getTipoGasto().getCodigo().equals("911")) {
                            params.put("CODEND", Arrays.asList("17"));
                        }
                    }
                    params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
                    Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "45%", params);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ajaxTipoInventario() {
        if (detalleItem != null) {
            if (detalleItem.getTipoGasto() != null) {
                JsfUtil.addInformationMessage("Información", "Asigne Cuenta Contable y Contracuenta.");
            } else {
                JsfUtil.addWarningMessage("Advertencia", "Seleccione un tipo de gasto.");
            }
            detalleItem.setCuentaContable(null);
            detalleItem.setContraCuenta(null);
            cuentaContable = new ContCuentas();
            contraCuenta = new ContCuentas();
        }
    }

    public void openDialogItems() {
        detalleItem = new DetalleItem();
        cuentaContable = new ContCuentas();
        contraCuenta = new ContCuentas();
        tipoRegistro = Boolean.TRUE;
        JsfUtil.executeJS("PF('dlgItemsContable').show()");
        PrimeFaces.current().ajax().update("form");

    }

    public void openDialogListItems() {
        tipoBusqueda = 1;
        listItemsSeleccionados = new ArrayList<>();
        detalleItem = new DetalleItem();
        cuentaContable = new ContCuentas();
        contraCuenta = new ContCuentas();
        loadTipoBusquedaItems();
        JsfUtil.executeJS("PF('dlgListItemsContable').show()");
        PrimeFaces.current().ajax().update("formItems");

    }

    public void loadTipoBusquedaItems() {
        switch (tipoBusqueda) {
            case 1:
                listItems = detalleItemService.getItemsByCuentaContable();
                break;
            case 2:
                listItems = detalleItemService.getItemsByCuentaContableIsNull();
                break;
            case 3:
                listItems = detalleItemService.getItemsByCuentaContableIsNOtNull();
                break;
        }
    }

    public void openDialogListItemsSelect() {
        if (listItemsSeleccionados.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "Seleccione Items para Agregarle Tipo de Inv., Cuenta y Contra Cuenta Contable.z");
            return;
        }
//        System.out.println("listItemsSeleccionados " + listItemsSeleccionados);
        JsfUtil.executeJS("PF('dlgListItemsSelect').show()");
        PrimeFaces.current().ajax().update("formItemsSelect");
    }

    public void selectItem(SelectEvent evt) {
        this.detalleItem = ((DetalleItem) evt.getObject());
    }

    public void selectCuenta(SelectEvent evt) {
        cuentaContable = (ContCuentas) evt.getObject();
        detalleItem.setCuentaContable(cuentaContable);
    }

    public void selectContraCuenta(SelectEvent evt) {
        contraCuenta = (ContCuentas) evt.getObject();
        detalleItem.setContraCuenta(contraCuenta);

    }

    public void guardarGrupo() {
        for (DetalleItem di : listItemsSeleccionados) {
            di.setTipoGasto(detalleItem.getTipoGasto());
            di.setCuentaContable(cuentaContable.getId() != null ? cuentaContable : null);
            di.setContraCuenta(contraCuenta.getId() != null ? contraCuenta : null);
            detalleItemService.edit(di);
        }
        JsfUtil.addInformationMessage("Información", "Cuentas Contables Agregadas Correctamente.");
        JsfUtil.executeJS("PF('dlgListItemsContable').hide()");
        JsfUtil.executeJS("PF('dlgListItemsSelect').hide()");
    }

    public void guardar() {
//        boolean edit = detalleItem.getId() != null;
        if (detalleItem.getCuentaContable() == null) {
            JsfUtil.addInformationMessage("Información", "Llene los campos requeridos.");
            return;
        }
//        if (detalleItem.getContraCuenta() == null) {
//            JsfUtil.addInformationMessage("Información", "Llene los campos requeridos.");
//            return;
//        }
        detalleItemService.edit(detalleItem);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Item " + detalleItem.getCodigoAgrupado() + " " + detalleItem.getDescripcion() + ", Cuenta asignada con éxito.");
        resetValues();

    }

    public void eliminar(DetalleItem i) {
        if (detalleItemService.existenRegistros(i)) {
            JsfUtil.addWarningMessage("ERROR", "No puede ser eliminado porque ya existen registros de INVENTARIOS del item");
        } else {
            i.setCuentaContable(null);
            i.setContraCuenta(null);
            detalleItem = i;
            detalleItemService.edit(detalleItem);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Item " + detalleItem.getCodigoAgrupado() + " " + detalleItem.getDescripcion() + ", Cuenta eliminada.");
        }

    }

    public void eliminarItemSeleccionado(DetalleItem s) {
        listItemsSeleccionados.remove(s);
        JsfUtil.addInformationMessage("Información", "Item " + s.getCodigoAgrupado() + " " + s.getDescripcion() + ", eliminado de la lista Seleccionada.");
    }

    public void visualizarDetalleAsignacion(DetalleItem i) {
        cuentaContable = i.getCuentaContable() != null ? i.getCuentaContable() : new ContCuentas();
        contraCuenta = i.getContraCuenta() != null ? i.getContraCuenta() : new ContCuentas();
        detalleItem = i;
        visualizar = Boolean.FALSE;
        tipoRegistro = Boolean.FALSE;
        JsfUtil.executeJS("PF('dlgItemsContable').show()");
        PrimeFaces.current().ajax().update("form");
    }

    public void editarItemCuentaContable(DetalleItem i) {
        cuentaContable = i.getCuentaContable() != null ? i.getCuentaContable() : new ContCuentas();
        contraCuenta = i.getContraCuenta() != null ? i.getContraCuenta() : new ContCuentas();
        detalleItem = i;
        visualizar = Boolean.TRUE;
        tipoRegistro = Boolean.FALSE;
        JsfUtil.executeJS("PF('dlgItemsContable').show()");
        PrimeFaces.current().ajax().update("form");

    }

    public void resetValues() {
        detalleItem = new DetalleItem();
        cuentaContable = new ContCuentas();
        contraCuenta = new ContCuentas();
        visualizar = Boolean.TRUE;
        JsfUtil.executeJS("PF('dlgItemsContable').hide()");
    }

    /**
     * @param tipo true sin cuentas false con cuentas
     */
    public void generarPdf(Boolean tipo) {
        if (tipo) {
            ss.setNombreReporte("itemsCuentasContables");
            ss.setNombreSubCarpeta("activos");
        } else {
        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public DetalleItem getDetalleItem() {
        return detalleItem;
    }

    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }

    public LazyModel<DetalleItem> getLazyDetalleItem() {
        return lazyDetalleItem;
    }

    public void setLazyDetalleItem(LazyModel<DetalleItem> lazyDetalleItem) {
        this.lazyDetalleItem = lazyDetalleItem;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public ContCuentas getContraCuenta() {
        return contraCuenta;
    }

    public void setContraCuenta(ContCuentas contraCuenta) {
        this.contraCuenta = contraCuenta;
    }

    public Boolean getAgregarCuenta() {
        return agregarCuenta;
    }

    public void setAgregarCuenta(Boolean agregarCuenta) {
        this.agregarCuenta = agregarCuenta;
    }

    public Boolean getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }

    public List<ContCuentas> getListTiposGastos() {
        return listTiposGastos;
    }

    public void setListTiposGastos(List<ContCuentas> listTiposGastos) {
        this.listTiposGastos = listTiposGastos;
    }

    public List<DetalleItem> getListItemsSeleccionados() {
        return listItemsSeleccionados;
    }

    public void setListItemsSeleccionados(List<DetalleItem> listItemsSeleccionados) {
        this.listItemsSeleccionados = listItemsSeleccionados;
    }

    public List<DetalleItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<DetalleItem> listItems) {
        this.listItems = listItems;
    }

//</editor-fold>
    public Boolean getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Boolean tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

}
