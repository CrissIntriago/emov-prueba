/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.activos.service.CatalogoMedidaService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoExistencias;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMedida;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "detalleItemView")
@ViewScoped
public class DetalleItemController implements Serializable {

    private DetalleItem detalleItem;
    private LazyModel<DetalleItem> lazyDetalleItem;
    private Boolean nuevo = false;
    private Boolean view = false;
    private Boolean tipoInventario = false;
    private Boolean stock = false;
    private Boolean ubicacion = false;
    private Boolean adicional = false;
    private List<ContCuentas> listTiposGastos;
    private List<CatalogoMedida> listCatalogoMedida;

    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private CatalogoMedidaService catalogoMedidaService;
    @Inject
    private GrupoNivelesService grupoService;

    @Inject
    private KatalinaService katalinaService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ServletSession servletSession;

    private List<UploadedFile> files;

    @PostConstruct
    public void initView() {
        this.detalleItem = new DetalleItem();

        //lista de gasto corregir el nativeQuery
        listTiposGastos = detalleItemService.getTipoGastos("13", Arrays.asList("15138", "15238"), "911");
        listCatalogoMedida = catalogoMedidaService.findByNamedQuery("CatalogoMedida.findByEstado", true);
        lazyDetalleItem = new LazyModel<>(DetalleItem.class);
        lazyDetalleItem.getSorteds().put("codigoAgrupado", "ASC");
        lazyDetalleItem.setDistinct(false);
        estadoBoolean();
    }

    public void form(DetalleItem item) {
        if (item == null) {
            detalleItem = new DetalleItem();
            detalleItem.setEstante(Short.valueOf("1"));
            detalleItem.setFila(Short.valueOf("1"));
            detalleItem.setColumna(Short.valueOf("1"));
            detalleItem.setCajon(Short.valueOf("1"));
            detalleItem.setCuadrante(Short.valueOf("1"));

            nuevo = Boolean.TRUE;
            estadoBoolean();
        } else {
            this.detalleItem = item;
            nuevo = Boolean.FALSE;
            if (detalleItemService.existenRegistros(item)) {
                view = Boolean.TRUE;
                tipoInventario = Boolean.TRUE; //Deshabilitado
                ubicacion = Boolean.FALSE;
                adicional = Boolean.FALSE;
                stock = Boolean.FALSE;
            } else {
                estadoBoolean();
            }
        }
        detalleItem.setEstado(true);
        PrimeFaces.current().executeScript("PF('dlgItems').show()");
        PrimeFaces.current().ajax().update(":frmItem");
    }

    public void estadoBoolean() {
        tipoInventario = Boolean.FALSE;
        adicional = Boolean.FALSE;
        stock = Boolean.FALSE;
        ubicacion = Boolean.FALSE;
        view = Boolean.FALSE;
    }

    public void openDlg() {
        Utils.openDialog("/facelet/activos/inventario/mantenimientoInventario/dialogGrupoNiveles", "60%", "305");
    }

    public void openDlgInvMEF() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCatalogoExistencias", "900", "520");
    }

    public void selectData(SelectEvent evt) {
        detalleItem.setAsignarGrupo((GrupoNiveles) evt.getObject());
//        System.out.println("Subgrupo max " + detalleItemService.getNivelOrdenSubgrupo(detalleItem.getAsignarGrupo()));
        //PrimeFaces.current().ajax().update("farea");
    }

    public void selectDataCatalogo(SelectEvent evt) {
        detalleItem.setCatalogoExistencias((CatalogoExistencias) evt.getObject());
        //PrimeFaces.current().ajax().update("mef");
    }

    public ContCuentas obtenerPadreOfCuentaTGastos(ContCuentas c) {
        if (c == null) {
            return new ContCuentas();
        }
        return detalleItemService.findByPadreOfTGastos(c);
    }

    public GrupoNiveles obtenerNombreSub(GrupoNiveles g) {
        if (g == null) {
            return new GrupoNiveles();
        }
        return grupoService.findByPadreGrupo(g);
    }

    public boolean verificarCuentaContable(DetalleItem item) {
        //Si tiene Cuenta Contable no se permitirá editar
        boolean verificar = true;
        Short cta = detalleItemService.getCtaContable(item);
        if (cta != 1) {
            verificar = Boolean.FALSE;
        } else {
            verificar = Boolean.TRUE;
        }

        return verificar;

    }

    public void saveItem() {
        if (detalleItem.getDescripcion() == null) {
            JsfUtil.addWarningMessage("Información", "Debe ingresar una nombre al item");
            return;
        }
        if (detalleItem.getTipoMedida() == null) {
            JsfUtil.addWarningMessage("Información", "Debe asignar un tipo de medida");
            return;
        }
        if (detalleItem.getAsignarGrupo() == null) {
            JsfUtil.addWarningMessage("Información", "Debe asignar un Grupo al Item");
            return;
        }

        CatalogoItem result = detalleItemService.getTipoActivoInv();
        detalleItem.setTipoActivo(result);

        Long orden = detalleItemService.getNivelOrdenSubgrupo(detalleItem.getAsignarGrupo());
        detalleItem.setOrden(orden);
        String completarCadenaConCeros = Utils.completarCadenaConCeros(detalleItem.getOrden() + "", 5);
        this.detalleItem.setCodigo(completarCadenaConCeros);

        detalleItem.setCodigoAgrupado(detalleItem.getAsignarGrupo().getPadre().getCodigo()
                + "-" + detalleItem.getAsignarGrupo().getCodigo()
                + "-" + detalleItem.getCodigo());
//        detalleItem.setCodigoAgrupado(grupoService.findByPadreGrupo(detalleItem.getAsignarGrupo().getPadre()).getCodigo()
//                + "-" + detalleItem.getAsignarGrupo().getPadre().getCodigo()
//                + "-" + detalleItem.getAsignarGrupo().getCodigo()
//                + "-" + detalleItem.getCodigo());

        detalleItem.setFechaIngresoSistema(new Date());
        detalleItem.setCantidadExistente(0);
//        generarCodigoQr(); AQUI NO SE GENERA EL CODIGO QR 
        DetalleItem item = detalleItemService.create(detalleItem);

        if (item != null) {
            if (files != null) {
                uploadDoc.upload(item, files);
            }
//            JsfUtil.addSuccessMessage("Info", "Item: " + codigoDescripcionItem(detalleItem) + " Guardado Correctamente");
            PrimeFaces.current().executeScript("PF('dlgItems').hide();");
            PrimeFaces.current().executeScript("PF('dlgInfo').show();");
            PrimeFaces.current().ajax().update("idFormDlgInfo");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al guardar Item");
        }
    }

    private void generarCodigoQr() {
        DetalleQr detalleQr = new DetalleQr(SisVars.RUTA_IMAGES_QR, detalleItem.getCodigoAgrupado(),
                detalleItem.getMarca(), detalleItem.getDescripcion(), SisVars.RUTA_IMAGES_QR + "plantilla.png",
                detalleItem.getCodigoQR() != null ? detalleItem.getCodigoQR() : "");
        DetalleQrResponse responseQr = katalinaService.generarCodigoQR(detalleQr);
        if (responseQr != null) {
            detalleItem.setIdAndCodigo(detalleQr.getIdAndCodigo());
            detalleItem.setCodigoQR(responseQr.getNombreImagen());
            detalleItem.setUrlImagen(responseQr.getUrlImagen());
        }
    }

    public void downloadCodeQr(DetalleItem item) {
        if (item.getCodigoQR() != null) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            servletSession.setNombreDocumento(SisVars.RUTA_IMAGES_QR + item.getCodigoQR());
            servletSession.setContentType("image/x-png");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        } else {
            JsfUtil.addInformationMessage("No existe un código Qr para " + item.getDescripcion(), "");
        }
    }

    public void limpiarVariable() {
        nuevo = false;
        detalleItem = new DetalleItem();
    }

    public void editar() {
        if (detalleItem.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
//        detalleItem.setCodigoAgrupado(grupoService.findByPadreGrupo(detalleItem.getAsignarGrupo().getPadre()).getCodigo()
//                + "-" + detalleItem.getAsignarGrupo().getPadre().getCodigo()
//                + "-" + detalleItem.getAsignarGrupo().getCodigo()
//                + "-" + detalleItem.getCodigo());
        detalleItem.setCodigoAgrupado(detalleItem.getAsignarGrupo().getPadre().getCodigo()
                + "-" + detalleItem.getAsignarGrupo().getCodigo()
                + "-" + detalleItem.getCodigo());
//        if (detalleItemService.verificarCambiosTipoInventario(detalleItem)) {
//            detalleItem.setCuentaContable(null);
//            JsfUtil.addInformationMessage("Información", "Al item: " + codigoDescripcionItem(detalleItem) + " Debe asignarle una Cuenta Contable");
//        }
//        generarCodigoQr();
        detalleItemService.edit(detalleItem);
        if (files != null) {
            uploadDoc.upload(detalleItem, files);
        }
//        JsfUtil.addSuccessMessage("Info", "Item: " + codigoDescripcionItem(detalleItem) + " editado correctamente");
        PrimeFaces.current().executeScript("PF('dlgItems').hide();");
        PrimeFaces.current().executeScript("PF('dlgInfo').show();");
        PrimeFaces.current().ajax().update("idFormDlgInfo");
    }

    public String codigoDescripcionItem(DetalleItem item) {
        return item.getCodigoAgrupado() + " " + item.getDescripcion();
    }

    public void visualizar(DetalleItem item) {
        if (item == null) {
            nuevo = Boolean.FALSE;
            estadoBoolean();
        } else {
            this.detalleItem = item;
            nuevo = Boolean.TRUE;
            view = Boolean.TRUE;
            adicional = Boolean.TRUE;
            stock = Boolean.TRUE;
            ubicacion = Boolean.TRUE;
        }
        detalleItem.setEstado(true);
        PrimeFaces.current().executeScript("PF('dlgItems').show()");
        PrimeFaces.current().ajax().update(":frmItem");
    }

    public void eliminar(DetalleItem item) {
//        if (detalleItemService.existenRegistros(item)) {
        if (item.getCantidadExistente() > 0) {
            JsfUtil.addWarningMessage("ERROR", "Item: " + codigoDescripcionItem(item) + " no se puede inactivar, tiene stock");
//            JsfUtil.addWarningMessage("ERROR", "No puede ser eliminado porque ya existen registros de INVENTARIOS de este item");
        } else {
            item.setEstado(Boolean.FALSE);
            detalleItemService.edit(item);
            JsfUtil.addSuccessMessage("INFO", "Item: " + codigoDescripcionItem(item) + " inactivado correctamente");
            PrimeFaces.current().ajax().update("frmMain");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(event.getFile());
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS & SETTERS DETALLE ITEM">
    public LazyModel<DetalleItem> getLazyDetalleItem() {
        return lazyDetalleItem;
    }

    public void setLazyDetalleItem(LazyModel<DetalleItem> lazyDetalleItem) {
        this.lazyDetalleItem = lazyDetalleItem;
    }

    public DetalleItem getDetalleItem() {
        return detalleItem;
    }

    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public List<ContCuentas> getListTiposGastos() {
        return listTiposGastos;
    }

    public void setListTiposGastos(List<ContCuentas> listTiposGastos) {
        this.listTiposGastos = listTiposGastos;
    }

    public List<CatalogoMedida> getListCatalogoMedida() {
        return listCatalogoMedida;
    }

    public void setListCatalogoMedida(List<CatalogoMedida> listCatalogoMedida) {
        this.listCatalogoMedida = listCatalogoMedida;
    }

//    public List<MasterCatalogo> getListaPeriodos() {
//        return listaPeriodos;
//    }
//
//    public void setListaPeriodos(List<MasterCatalogo> listaPeriodos) {
//        this.listaPeriodos = listaPeriodos;
//    }
    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getTipoInventario() {
        return tipoInventario;
    }

    public void setTipoInventario(Boolean tipoInventario) {
        this.tipoInventario = tipoInventario;
    }

    public Boolean getStock() {
        return stock;
    }

    public void setStock(Boolean stock) {
        this.stock = stock;
    }

    public Boolean getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Boolean ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getAdicional() {
        return adicional;
    }

    public void setAdicional(Boolean adicional) {
        this.adicional = adicional;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

//    public OpcionBusqueda getBusqueda() {
//        return busqueda;
//    }
//
//    public void setBusqueda(OpcionBusqueda busqueda) {
//        this.busqueda = busqueda;
//    }
//</editor-fold>
//    public LazyModel<GrupoNiveles> getLazyGrupoNiveles() {
//        return lazyGrupoNiveles;
//    }
//    
//    public void setLazyGrupoNiveles(LazyModel<GrupoNiveles> lazyGrupoNiveles) {
//        this.lazyGrupoNiveles = lazyGrupoNiveles;
//    }
}
