/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.activos.entities.BienCatalogoBld;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Sandra Arroba S
 * @author Luis Pozo Gonzabay
 */
@Named(value = "bienMantenimientoView")
@ViewScoped
public class BienMantenimientoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private BienesMovimientoService bienesMovService;

    private BienesItem bienesItem;
    private BienesItem bienesComponentes;
    private BienCatalogoBld catalogoBLD;
    private Boolean nuevo;
    private Boolean view;
    private Boolean componente;
    private Boolean saldoInicial;

    private LazyModel<BienesItem> lazyBienesItem;
    private LazyModel<BienCatalogoBld> lazyCatalogoBLD;
    private List<BienesItem> listBienesComponentes;
    private List<BienesItem> listNewBienesComponentes;
    private List<CatalogoItem> listTipoBien;
    private List<CatalogoItem> listTipoBienFiltro;
    private List<ContCuentas> listCuentaContable;
    private List<CatalogoItem> listEstadoBien;
    private List<CatalogoItem> listDatosAdicionales;
    private StreamedContent streamedContent;

    @PostConstruct
    public void initView() {
        bienesItem = new BienesItem();
        bienesComponentes = new BienesItem();
        lazyBienesItem = new LazyModel<>(BienesItem.class);
        listBienesComponentes = new ArrayList<>();
        listNewBienesComponentes = new ArrayList<>();
        listTipoBienFiltro = new ArrayList<>();
        listTipoBien = new ArrayList<>();
        listDatosAdicionales = new ArrayList<>();
        listTipoBien = catalogoItemService.findCatalogoItems("tipo_bienes");
        listEstadoBien = catalogoItemService.findCatalogoItems("estado_bien");
        listTipoBienFiltro = catalogoItemService.findCatalogoItems("tipo_bienes");
        listDatosAdicionales = catalogoItemService.findCatalogoItems("datos_adicionales_bienes");
        lazyBienesItem.getFilterss().put("itemBienBoolean", Boolean.TRUE);
        lazyBienesItem.getFilterss().put("componente", Boolean.FALSE);
        lazyBienesItem.getSorteds().put("codigoBienAgrupado", "ASC");
        lazyCatalogoBLD = new LazyModel<>(BienCatalogoBld.class);
        lazyCatalogoBLD.getSorteds().put("idBien", "ASC");
    }

    public void form(BienesItem item, Boolean views) {
        bienesItem = new BienesItem();
        if (item == null) {
            view = views;
            nuevo = Boolean.TRUE;
        } else {
            this.bienesItem = item;
            nuevo = Boolean.FALSE;
            view = views;
            saldoInicial = Boolean.FALSE;
            listBienesComponentes = bienesItemService.getListadoComponentes(item);
            componente = Boolean.TRUE;
            if (bienesItem.getBienesMovimiento() == null) {
                JsfUtil.addWarningMessage("Información", "No tiene asignado un Movimiento");
            } else if (bienesItem.getTipoBien() != null && bienesItem.getClasificTipoBien() != null && bienesItem.getCuentaContable() != null) {
                mostrarClasificacionCuenta();
            }
            if (bienesItem.getBienesMovimiento().getMotivoMovimiento().getOrden() == 1) {
                saldoInicial = Boolean.TRUE;
            }
            //Existen Asignaciones a Custodio, no permita editar
//            if (detalleItemService.existenRegistros(item)) {
//                view = Boolean.TRUE;
//                tipoInventario = Boolean.TRUE; //Deshabilitado
//                ubicacion = Boolean.FALSE;
//                adicional = Boolean.FALSE;
//                stock = Boolean.FALSE;
//            } else {
//                  estadoBoolean();
//            }
        }
        PrimeFaces.current().executeScript("PF('dlgBien').show()");
        PrimeFaces.current().ajax().update(":frmBien");
    }

    public void openDialogImage(String ruta) {
        streamedContent = null;
        if (ruta != null) {
            try {
                FileInputStream fis = new FileInputStream(ruta);
                streamedContent = new DefaultStreamedContent(fis, "images/png", bienesComponentes.getCodigoBien());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BienMantenimientoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PrimeFaces.current().executeScript("PF('dlgImages').show()");
        PrimeFaces.current().ajax().update(":frmShowDialog");
    }

    public void editar() {
        if (bienesItem.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
        if (bienesItem.getClasificTipoBien() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Clasificación");
            return;
        }
        if (bienesItem.getCuentaContable() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Cuenta Contable");
            return;
        }
        if (bienesItem.getGrupoPadre() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un SubGrupo");
            return;
        }
        bienesItemService.edit(bienesItem);

        if (!listBienesComponentes.isEmpty()) {
            bienesItem.setTieneComponentes(Boolean.TRUE);
            bienesItemService.edit(bienesItem);
        }
        JsfUtil.addSuccessMessage("Info", "Bien " + bienesItem.getCodigoBien() + " " + bienesItem.getDescripcion() + " editado correctamente");
        bienesItem = new BienesItem();
        PrimeFaces.current().executeScript("PF('dlgBien').hide();");

    }

    public void openDlgBienMEF() {
        PrimeFaces.current().executeScript("PF('dlgCatalogoBien').show()");
    }

    public void selectDataCatalogo(BienCatalogoBld evt) {
        catalogoBLD = evt;
        bienesItem.setCatalogoBienes(catalogoBLD);
        PrimeFaces.current().executeScript("PF('dlgCatalogoBien').hide()");
    }

    public void mostrarClasificacionCuenta() {
        String codigoBLD = null;
        String codigoBSC = null;
        String codigoNot1 = null;
        String codigoNot2 = null;
        String codigoNot3 = null;
        String codigoNot4 = null;
        Long orden = 3L;

        String clasif = null;
        String codigo = null;
        if (bienesItem.getTipoBien() == null) {
            JsfUtil.addWarningMessage("Información", "Elija un Tipo de Bien");
        } else {
            if (bienesItem.getBienesMovimiento().getMotivoMovimiento() == null) {
                JsfUtil.addWarningMessage("Información", "No tienes asignado un tipo de Movimiento");
            } else {
                if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                    codigoBSC = "911";
                }
                if (bienesItem.getTipoBien().getCodigo().equals("BLD")) {
                    codigoBLD = "14";
                    codigoNot1 = "6";
                    codigoNot2 = "9";
                }
                switch (bienesItem.getBienesMovimiento().getMotivoMovimiento().getOrden()) {
                    case 1:
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        }
                        break;
                    case 2:
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 3:
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Solo esta 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 4:
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = null;
                            codigoBSC = null;
                            JsfUtil.addInformationMessage("Información", "No existe Cuenta Contable para " + bienesItem.getTipoBien().getTexto());
                        } else {
                            // Solo esta 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 5:
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Solo esta 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 6:
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Va el 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 7:
                        //Incorporación
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = null;
                            codigoBSC = null;
                            JsfUtil.addInformationMessage("Información", "No existe Cuenta Contable para " + bienesItem.getTipoBien().getTexto());
                        } else {
                            //Solo va 143, 148
                            clasif = "143";
                            codigo = "148";
                            codigoNot3 = "1";
                            codigoNot4 = "2";
                        }
                        break;
                    case 8:
                        /*Acta de Entrega Recepción*/
                        if (bienesItem.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Todo menos 142, es decir Va el 141, 143, 148
                            clasif = "141";
                            codigo = "143";
                            codigoNot3 = "2";
                        }
                        break;
                }
            }
        }
        listCuentaContable = bienesMovService.getClasificacionByTipoBien(orden, codigoBLD, codigoBSC, codigoNot1, codigoNot2, codigoNot3, codigoNot4);
    }

    public void editComponentes(BienesItem comp) {
        Subject subject = SecurityUtils.getSubject();
        if (comp != null) {
            this.bienesComponentes = comp;
            bienesComponentes.setFechaModificacion(new Date());
            bienesComponentes.setUsuarioModifica(subject.getPrincipal().toString());
        } else {
            bienesComponentes = new BienesItem();
            bienesComponentes.setFechaCreacion(new Date());
            bienesComponentes.setUsuarioCreador(subject.getPrincipal().toString());
            Integer index = listBienesComponentes.size() + 1;
            Long orden = index.longValue();
            bienesComponentes.setOrden(orden);
            String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesComponentes.getOrden() + "", 2);
            bienesComponentes.setCodigoBien(completarCadenaConCeros);
        }
        PrimeFaces.current().executeScript("PF('dlgComponentes').show()");
    }

    public void saveComponenteOfBienItem() {
        Subject subject = SecurityUtils.getSubject();
        if (bienesComponentes.getEstado() != null) {
            bienesItemService.edit(bienesComponentes);
        } else {
            bienesComponentes.setEstado(Boolean.TRUE);
            bienesComponentes.setPeriodo(bienesItem.getPeriodo());
            bienesComponentes.setComponente(Boolean.TRUE);
            bienesComponentes.setItemBienBoolean(Boolean.FALSE);
            bienesComponentes.setCodigoBienAgrupado(bienesItem.getGrupoPadre().getCodigoBien() + bienesItem.getCodigoBien() + bienesComponentes.getCodigoBien());
            listNewBienesComponentes.add(bienesComponentes);
        }
        if (!listBienesComponentes.isEmpty()) {
            bienesItem.setTieneComponentes(Boolean.TRUE);
        }
        if (!listNewBienesComponentes.isEmpty()) {
            bienesItem.setTieneComponentes(Boolean.TRUE);
        }
        if (bienesItem.getTieneComponentes()) {
            if (listNewBienesComponentes.size() > 0) {
                for (BienesItem comp : listNewBienesComponentes) {
                    comp.setGrupoPadre(bienesItem);
                    comp.setUso(bienesItem.getUso());
                    bienesItemService.create(comp);
                    bienesComponentes = new BienesItem();
                    listBienesComponentes = bienesItemService.getListadoComponentes(bienesItem);
                }
            }
        }
        bienesComponentes = new BienesItem();
        PrimeFaces.current().executeScript("PF('dlgComponentes').hide()");
    }

    public void eliminar(BienesItem bien) {
        bienesItem = bien;
        if (bienesItem.getBienesMovimiento().getMotivoMovimiento().getOrden() != 1) {
            JsfUtil.addErrorMessage("Advertencia", "No puede inactivar registros");
            return;
        }
        if (bienesItemService.getListaMovimientoBienes(bienesItem) != null) {
            JsfUtil.addErrorMessage("Advertencia", "El bien está asignado a un Custodio, no puede inactivar el bien.");
            return;
        }
        bienesItem.setEstado(Boolean.FALSE);
        bienesItemService.edit(bienesItem);
        if (!listBienesComponentes.isEmpty()) {
            for (BienesItem componenteB : listBienesComponentes) {
                componenteB.setEstado(Boolean.FALSE);
                bienesItemService.edit(componenteB);
            }
        }
        JsfUtil.addInformationMessage("Información", "Registro inactivado correctamente");
    }

    public void resetValues() {
        nuevo = false;
        bienesItem = new BienesItem();
    }

    public void resetTipoDatoAdicional() {
        if (bienesItem.getTipoDatoAdicional() != null) {
            switch (bienesItem.getTipoDatoAdicional().getCodigo()) {
                case "AD-PREDI":
                    bienesItem.setSerieMotor(null);
                    break;
                case "AD-VEHIC":
                case "AD-EQCAM":
                    bienesItem.setFechaInscripcionPredio(null);
                    break;
                default:
                    break;
            }
        } else {
            bienesItem.setPlacaCodigocatastral(null);
            bienesItem.setSerieMotor(null);
            bienesItem.setUbicacionNumchasis(null);
            bienesItem.setFechaInscripcionPredio(null);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

    public BienesItem getBienesComponentes() {
        return bienesComponentes;
    }

    public void setBienesComponentes(BienesItem bienesComponentes) {
        this.bienesComponentes = bienesComponentes;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getComponente() {
        return componente;
    }

    public void setComponente(Boolean componente) {
        this.componente = componente;
    }

    public Boolean getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Boolean saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public LazyModel<BienesItem> getLazyBienesItem() {
        return lazyBienesItem;
    }

    public void setLazyBienesItem(LazyModel<BienesItem> lazyBienesItem) {
        this.lazyBienesItem = lazyBienesItem;
    }

    public LazyModel<BienCatalogoBld> getLazyCatalogoBLD() {
        return lazyCatalogoBLD;
    }

    public void setLazyCatalogoBLD(LazyModel<BienCatalogoBld> lazyCatalogoBLD) {
        this.lazyCatalogoBLD = lazyCatalogoBLD;
    }

    public List<BienesItem> getListBienesComponentes() {
        return listBienesComponentes;
    }

    public void setListBienesComponentes(List<BienesItem> listBienesComponentes) {
        this.listBienesComponentes = listBienesComponentes;
    }

    public List<CatalogoItem> getListTipoBien() {
        return listTipoBien;
    }

    public void setListTipoBien(List<CatalogoItem> listTipoBien) {
        this.listTipoBien = listTipoBien;
    }

    public List<ContCuentas> getListCuentaContable() {
        return listCuentaContable;
    }

    public void setListCuentaContable(List<ContCuentas> listCuentaContable) {
        this.listCuentaContable = listCuentaContable;
    }

    public List<CatalogoItem> getListEstadoBien() {
        return listEstadoBien;
    }

    public void setListEstadoBien(List<CatalogoItem> listEstadoBien) {
        this.listEstadoBien = listEstadoBien;
    }

    public List<BienesItem> getListNewBienesComponentes() {
        return listNewBienesComponentes;
    }

    public void setListNewBienesComponentes(List<BienesItem> listNewBienesComponentes) {
        this.listNewBienesComponentes = listNewBienesComponentes;
    }

    public List<CatalogoItem> getListTipoBienFiltro() {
        return listTipoBienFiltro;
    }

    public void setListTipoBienFiltro(List<CatalogoItem> listTipoBienFiltro) {
        this.listTipoBienFiltro = listTipoBienFiltro;
    }

    public List<CatalogoItem> getListDatosAdicionales() {
        return listDatosAdicionales;
    }

    public void setListDatosAdicionales(List<CatalogoItem> listDatosAdicionales) {
        this.listDatosAdicionales = listDatosAdicionales;
    }
//</editor-fold>

    public StreamedContent getStreamedContent() {
        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

}
