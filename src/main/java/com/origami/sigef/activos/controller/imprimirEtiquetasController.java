/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.entities.Impresora;
import com.origami.sigef.common.entities.Zebra;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ImpresoraService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "imprimirEtiquetasview")
@ViewScoped
public class imprimirEtiquetasController implements Serializable {
    
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private ImpresoraService impresoraService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private GrupoNivelesService grupoService;
    
    private LazyModel<BienesItem> lazyModelBienes;
    private LazyModel<DetalleItem> lazyModelInventario;
    
    private List<BienesItem> itemsSeleccionados;
    private List<DetalleItem> itemsInventarioSeleccionados;
    private List<Impresora> impresoras;
    private List<File> fileList;
    
    private Impresora impresora;
    
    @PostConstruct
    public void init() {
        itemsSeleccionados = new ArrayList<>();
        itemsInventarioSeleccionados = new ArrayList<>();
        fileList = new ArrayList<>();
        cargarRegistros();
        impresoras = impresoraService.findAll();
        if (impresoras != null) {
            impresora = impresoras.get(0);
        }
    }
    
    public void cargarRegistros() {
        this.lazyModelBienes = new LazyModel<>(BienesItem.class);
        this.lazyModelBienes.getFilterss().put("estado", true);
        this.lazyModelBienes.getFilterss().put("itemBienBoolean", Boolean.TRUE);
        this.lazyModelBienes.getFilterss().put("componente", Boolean.FALSE);
        this.lazyModelBienes.getSorteds().put("codigoBienAgrupado", "ASC");
        //inventario
        this.lazyModelInventario = new LazyModel<>(DetalleItem.class);
        this.lazyModelInventario.getFilterss().put("estado", true);
        this.lazyModelInventario.getSorteds().put("codigoAgrupado", "ASC");
    }
    
    public void descargarEtiquetas() {
        String zipSPI = "";
        if (itemsSeleccionados.isEmpty() && itemsInventarioSeleccionados.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar uno o mas items");
            return;
        } else {
            int aux = 0;
            String name_zip = "";
            if (!itemsSeleccionados.isEmpty()) {
                for (BienesItem bien : itemsSeleccionados) {
                    if (bien.getUrlImagenQr() != null) {
                        File file = new File(bien.getUrlImagenQr());
                        if (file.exists()) {
                            fileList.add(file);
                            aux += 1;
                        }
                    }
                }
                name_zip = "etiquetas_bien";
            }
            if (!itemsInventarioSeleccionados.isEmpty()) {
                for (DetalleItem inventario : itemsInventarioSeleccionados) {
                    if (inventario.getUrlImagen() != null) {
                        File file = new File(inventario.getUrlImagen());
                        if (file.exists()) {
                            fileList.add(file);
                            aux += 1;
                        }
                    }
                }
                name_zip = "etiquetas_invetario";
            }
            if (fileList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!!", "GENERE PRIMERO LAS ETIQUETAS");
                return;
            }
            downloadZIP(fileList, name_zip);
            JsfUtil.addInformationMessage("INFO!!!", "Se han generado: " + aux + " de " + itemsSeleccionados.size() + " etiquetas");
            init();
        }
    }
    
    private void downloadZIP(List<File> fileList, String name_zip) {
        String zipSPI = katalinaService.addZIP(fileList, name_zip);
        servletSession.addParametro("download", true);
        servletSession.setNombreDocumento(zipSPI);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
    }
    
    public void generarItems() {
        System.out.println("size: " + itemsInventarioSeleccionados.size());
        if (impresora != null) {
            int aux = 0;
            String zipSPI = "";
            if (itemsSeleccionados.isEmpty() && itemsInventarioSeleccionados.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar uno o mas items");
                return;
            }
            //bienes
            if (!itemsSeleccionados.isEmpty()) {
                for (BienesItem item : itemsSeleccionados) {
                    DetalleQrResponse dqr = getDetalleQrResponse(item.detalleEtiquetaQR(), item.getCodigoBienAgrupado(),
                            item.getCuentaContable().getDescripcion(), item.getDescripcion(), item.getDescripcion(), "BIEN_" + item.getCodigoBienAgrupado() + ".png");
                    if (dqr != null) {
                        aux += 1;
                        item.setNombreImagenQR(dqr.getIdAndCodigo());
                        item.setUrlImagenQr(dqr.getRutaArchivo());
                        bienesItemService.edit(item);
                        //guardar etiqueta para descargar
                        File file = new File(dqr.getRutaArchivo());
                        if (file.exists()) {
                            fileList.add(file);
                        }
                    }
                }
                downloadZIP(fileList, "etiquetas_inventario");
                JsfUtil.addInformationMessage("INFO!!!", "Se han generado: " + aux + " de " + itemsSeleccionados.size() + " etiquetas");
            }
            //inventario
            System.out.println("ENTROO 0: " + itemsInventarioSeleccionados.size());
            if (!itemsInventarioSeleccionados.isEmpty()) {
                System.out.println("ENTROO 1 ");
                for (DetalleItem item : itemsInventarioSeleccionados) {
                    System.out.println("ITEM " + item);
                    DetalleQrResponse dqr = getDetalleQrResponse(item.detalleEtiquetaQR(), item.getCodigoAgrupado(),
                            item.getCuentaContable().getDescripcion(), item.getDescripcion(), item.getDescripcion(), "INVENTARIO_" + item.getCodigoAgrupado() + ".png");
                    System.out.println("dr: " + dqr);
                    if (dqr != null) {
                        aux += 1;
                        item.setCodigoQR(dqr.getIdAndCodigo());
                        item.setUrlImagen(dqr.getRutaArchivo());
                        detalleItemService.edit(item);
                        //guardar etiqueta para descargar 
                        File file = new File(dqr.getRutaArchivo());
                        if (file.exists()) {
                            fileList.add(file);
                        }
                    }
                }
                JsfUtil.addInformationMessage("INFO!!!", "Se han generado: " + aux + " de " + itemsInventarioSeleccionados.size() + " etiquetas");
                downloadZIP(fileList, "etiquetas_inventario");
            }
            init();
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No existe ninguna impresora configurada");
        }
    }
    
    private DetalleQrResponse getDetalleQrResponse(String detalleEtiquetaQR, String getCodigo, String getCuentaContable, String getDescripcion, String getSerie1, String nombreImagen) {
        DetalleQr detalleQr = new DetalleQr();
        detalleQr.setArchivo(SisVars.RUTA_IMAGES_QR);
        detalleQr.setIdAndCodigo(detalleEtiquetaQR);
        detalleQr.setCodigoItem("Código: " + getCodigo);
        detalleQr.setBodega("Grupo Contable: " + getCuentaContable);
        detalleQr.setTitulo("Descripción: " + getDescripcion);
        detalleQr.setDescripcion("Serie: " + getSerie1);
        detalleQr.setRutaArchivoPlantilla(SisVars.RUTA_IMAGES_QR + "plantilla.png");
        detalleQr.setNombreImagen(nombreImagen);
        detalleQr.setTipo("percha");
        return katalinaService.generarCodigoQR(detalleQr);
    }
    
    private void imprimir(String url, String code) {
        //imprimir etiqueta
        if (SisVars.IMPRIMIR_ETIQUETAS) {
            Zebra zebra = new Zebra(impresora.getNombre(), url, SisVars.CANTIDAD);
            zebra.setEstadoImpresion(Boolean.FALSE);
            zebra = (Zebra) katalinaService.methodPOST(zebra, SisVars.appQrService + "imprimir", Zebra.class);
            if (zebra != null) {
                if (!zebra.getEstadoImpresion()) {
                    JsfUtil.addErrorMessage("Intente nuevamente", "");
                }
            } else {
                JsfUtil.addErrorMessage("AVISO!!!", "No se imprimio la etiqueta " + code);
            }
        }
    }
    
    public GrupoNiveles obtenerNombreSub(GrupoNiveles g) {
        if (g == null) {
            return new GrupoNiveles();
        }
        return grupoService.findByPadreGrupo(g);
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public LazyModel<BienesItem> getLazyModelBienes() {
        return lazyModelBienes;
    }
    
    public void setLazyModelBienes(LazyModel<BienesItem> lazyModelBienes) {
        this.lazyModelBienes = lazyModelBienes;
    }
    
    public List<BienesItem> getItemsSeleccionados() {
        return itemsSeleccionados;
    }
    
    public void setItemsSeleccionados(List<BienesItem> itemsSeleccionados) {
        this.itemsSeleccionados = itemsSeleccionados;
    }
    
    public LazyModel<DetalleItem> getLazyModelInventario() {
        return lazyModelInventario;
    }
    
    public void setLazyModelInventario(LazyModel<DetalleItem> lazyModelInventario) {
        this.lazyModelInventario = lazyModelInventario;
    }
    
    public List<DetalleItem> getItemsInventarioSeleccionados() {
        return itemsInventarioSeleccionados;
    }
    
    public void setItemsInventarioSeleccionados(List<DetalleItem> itemsInventarioSeleccionados) {
        this.itemsInventarioSeleccionados = itemsInventarioSeleccionados;
    }
//</editor-fold>

}
