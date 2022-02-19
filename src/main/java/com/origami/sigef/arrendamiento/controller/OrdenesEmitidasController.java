/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.controller;

import com.origami.sigef.arrendamiento.entities.OrdenesEmitidas;
import com.origami.sigef.arrendamiento.service.OrdenesEmitidasService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "ordenesEmitidasView")
@ViewScoped
public class OrdenesEmitidasController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private OrdenesEmitidasService ordenesEmitidasService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private FileUploadDoc uploadDoc;

    private LazyModel<OrdenesEmitidas> ordenesEmitidasLazyModel;

    private int anioSeleccionado;
    private CatalogoItem mesSeleccionado;
    private OrdenesEmitidas ordenesEmitidas;
    private String mesPresente;
    private double montoAbonado;
    private boolean abonoTotal;
    private boolean tipoReporte;
    private static final int DIAS_MAXIMOS = 3;

    private int codigoParametro;
    private int codigoReporte;

    private List<UploadedFile> files;
    private List<CatalogoItem> mesList;

    @PostConstruct
    public void initialize() {
        lazyModelParametros();
        anioSeleccionado = Utils.getAnio(new Date());
        mesPresente = Utils.convertirMesALetra(Utils.getMes(new Date()));
        this.ordenesEmitidasLazyModel.getFilterss().put("mes", Utils.getMes(new Date()));
        this.ordenesEmitidasLazyModel.getFilterss().put("anio", Utils.getAnio(new Date()));
        this.ordenesEmitidasLazyModel.getFilterss().put("estado", true);
        this.mesList = catalogoService.getItemsByCatalogo("meses_anio");
    }

    private void lazyModelParametros() {
        this.ordenesEmitidasLazyModel = new LazyModel<>(OrdenesEmitidas.class);
        this.ordenesEmitidasLazyModel.getSorteds().put("id", "ASC");
        this.ordenesEmitidasLazyModel.getFilterss().put("estado", true);
    }

    public void parametroFiltro() {
        lazyModelParametros();
        if (mesSeleccionado != null && anioSeleccionado > 0) {
            mesPresente = mesSeleccionado.getTexto();
            this.ordenesEmitidasLazyModel.getFilterss().put("mes", mesSeleccionado.getOrden());
            this.ordenesEmitidasLazyModel.getFilterss().put("anio", anioSeleccionado);
        } else {
            this.ordenesEmitidasLazyModel.getFilterss().put("mes", Utils.getMes(new Date()));
            this.ordenesEmitidasLazyModel.getFilterss().put("anio", Utils.getAnio(new Date()));
        }
    }

    public void generarOrden(OrdenesEmitidas orden) {
        orden.setFechaEmision(new Date());
        ordenesEmitidasService.edit(orden);
        if (ordenesEmitidasService.volverGenerarOrdenPago(orden)) {
            JsfUtil.addSuccessMessage("INFO!!", "Se volvio a genenar la orden de pago");
        } else {
            JsfUtil.addSuccessMessage("ERROR!!", "No se pudo genenar la orden de pago");
        }
    }

    public void anularOrdenDePago(OrdenesEmitidas orden) {
        if (ordenesEmitidasService.anularOrdenPago(orden)) {
            orden.setEstado(Boolean.FALSE);
            ordenesEmitidasService.edit(orden);
            JsfUtil.addSuccessMessage("INFO!!", "Se Anulo correctamente la orden de pago");
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "No se pudo generar la anulación de la orden de pago");
        }
    }

    public void openDlgReporte(Boolean accion) {
        codigoParametro = 0;
        tipoReporte = accion;
        PrimeFaces.current().executeScript("PF('parametrosReporteDLG').show()");
        PrimeFaces.current().ajax().update("parametrosReporteForm");
    }

    public void imprimirReportes() {
        if (codigoParametro == 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar uno de los parámetros mostrados");
            return;
        }
        if (tipoReporte) {
            imprimirReporteMesual();
        } else {
            imprimirReporteGeneral();
        }
        PrimeFaces.current().executeScript("PF('parametrosReporteDLG').hide()");
        PrimeFaces.current().ajax().update("parametrosReporteForm");
    }

    private void imprimirReporteMesual() {
        if (mesSeleccionado != null && mesSeleccionado.getOrden() != null) {
            servletSession.addParametro("mes", mesSeleccionado.getOrden().intValue());
            servletSession.addParametro("mes_letra", Utils.convertirMesALetra(mesSeleccionado.getOrden().intValue()).toUpperCase() + " - " + anioSeleccionado);
        } else {
            servletSession.addParametro("mes", Utils.getMes(new Date()));
            servletSession.addParametro("mes_letra", mesPresente + " - " + (short) anioSeleccionado);
        }
        servletSession.addParametro("anio", (short) anioSeleccionado);
        servletSession.addParametro("codigo", codigoParametro);
        servletSession.setNombreReporte("ReporteArrendamientos");
        servletSession.setNombreSubCarpeta("Arrendamiento");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private void imprimirReporteGeneral() {
        servletSession.addParametro("anio", (short) anioSeleccionado);
        servletSession.addParametro("codigo", codigoParametro);
        servletSession.setNombreReporte("ReporteArrendamientosGeneral");
        servletSession.setNombreSubCarpeta("Arrendamiento");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void visualizarArchivo(OrdenesEmitidas orden) {
        this.ordenesEmitidas = orden;
        PrimeFaces.current().executeScript("PF('viewDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
        PrimeFaces.current().ajax().update("docArriendo");
    }

    public void subirRecibo(OrdenesEmitidas orden) {
        this.ordenesEmitidas = orden;
        this.montoAbonado = 0;
        files = new ArrayList<>();
        PrimeFaces.current().ajax().update("subirDocForm");
        PrimeFaces.current().executeScript("PF('subirDocDialog').show()");
    }

    public void cargarArchivo(FileUploadEvent event) {
        if (!abonoTotal) {
            if (montoAbonado > ordenesEmitidas.getMontoPagar().doubleValue()) {
                JsfUtil.addWarningMessage("AVISO!!!", "El monto ingresado de abono es mayor al monto a pagar");
                return;
            }
            if (montoAbonado < (montoAbonado + ordenesEmitidas.getMontoPagado().doubleValue())) {
                JsfUtil.addWarningMessage("AVISO!!!", "El monto ingresado de abono es mayor a la suma del abono actual con el anterior");
                return;
            }

        }
        files.add(event.getFile());
        if (files != null) {
            uploadDoc.upload(this.ordenesEmitidas, files);
            JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
        }
        if (abonoTotal) {
            ordenesEmitidas.setMontoPagado(ordenesEmitidas.getMontoPagar());
        } else {
            ordenesEmitidas.setMontoPagado(ordenesEmitidas.getMontoPagado().add(new BigDecimal(montoAbonado)));
        }
        if (ordenesEmitidas.getMontoPagar().doubleValue() == ordenesEmitidas.getMontoPagado().doubleValue()) {
            ordenesEmitidas.setOrdenPagada(Boolean.TRUE);
        }
        ordenesEmitidasService.edit(ordenesEmitidas);
        PrimeFaces.current().executeScript("PF('subirDocDialog').hide()");
        PrimeFaces.current().ajax().update("ordenesEmitidasTable");
    }

    public void handleClose(CloseEvent event) {
        this.ordenesEmitidas = new OrdenesEmitidas();
        PrimeFaces.current().executeScript("PF('subirDocDialog').hide()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
        PrimeFaces.current().ajax().update("docArriendo");
    }

    public void openDlgReporteContabilidad() {
        codigoReporte = 0;
        PrimeFaces.current().executeScript("PF('reporteContabilizado').show()");
        PrimeFaces.current().ajax().update("reporteContabilizadoForm");
    }

    public void imprimirReporteContabilizado() {
        if (mesSeleccionado != null && mesSeleccionado.getOrden() != null) {
            servletSession.addParametro("mes", mesSeleccionado.getOrden().intValue());
            servletSession.addParametro("mes_letra", Utils.convertirMesALetra(mesSeleccionado.getOrden().intValue()).toUpperCase() + " - " + anioSeleccionado);
        } else {
            servletSession.addParametro("mes", Utils.getMes(new Date()));
            servletSession.addParametro("mes_letra", mesPresente + " - " + (short) anioSeleccionado);
        }
        servletSession.addParametro("anio", (short) anioSeleccionado);
        servletSession.addParametro("codigo", codigoReporte);
        servletSession.setNombreReporte("ReporteArrendamientosContabilizados");
        servletSession.setNombreSubCarpeta("Arrendamiento");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        PrimeFaces.current().executeScript("PF('reporteContabilizado').hide()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
        PrimeFaces.current().ajax().update("docArriendo");
    }

    public LazyModel<OrdenesEmitidas> getOrdenesEmitidasLazyModel() {
        return ordenesEmitidasLazyModel;
    }

    public void setOrdenesEmitidasLazyModel(LazyModel<OrdenesEmitidas> ordenesEmitidasLazyModel) {
        this.ordenesEmitidasLazyModel = ordenesEmitidasLazyModel;
    }

    public int getAnioSeleccionado() {
        return anioSeleccionado;
    }

    public void setAnioSeleccionado(int anioSeleccionado) {
        this.anioSeleccionado = anioSeleccionado;
    }

    public CatalogoItem getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(CatalogoItem mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

    public List<CatalogoItem> getMesList() {
        return mesList;
    }

    public void setMesList(List<CatalogoItem> mesList) {
        this.mesList = mesList;
    }

    public String getMesPresente() {
        return mesPresente;
    }

    public void setMesPresente(String mesPresente) {
        this.mesPresente = mesPresente;
    }

    public double getMontoAbonado() {
        return montoAbonado;
    }

    public void setMontoAbonado(double montoAbonado) {
        this.montoAbonado = montoAbonado;
    }

    public boolean isAbonoTotal() {
        return abonoTotal;
    }

    public void setAbonoTotal(boolean abonoTotal) {
        this.abonoTotal = abonoTotal;
    }

    public OrdenesEmitidas getOrdenesEmitidas() {
        return ordenesEmitidas;
    }

    public void setOrdenesEmitidas(OrdenesEmitidas ordenesEmitidas) {
        this.ordenesEmitidas = ordenesEmitidas;
    }

    public int getCodigoParametro() {
        return codigoParametro;
    }

    public void setCodigoParametro(int codigoParametro) {
        this.codigoParametro = codigoParametro;
    }

    public int getCodigoReporte() {
        return codigoReporte;
    }

    public void setCodigoReporte(int codigoReporte) {
        this.codigoReporte = codigoReporte;
    }

}
