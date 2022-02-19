/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.GarantiaService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Origami
 */
@Named(value = "polizabeans")
@ViewScoped
public class PolizaBeans implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private GarantiaService garantiaservice;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ClienteService clienteService;

    private Garantias garantia;
    private LazyModel<Garantias> garantiaLazy;
    private List<CatalogoItem> tiposdoc;
    private List<CatalogoItem> riesgoAsegurado;
    private List<Adquisiciones> adquisiciones;
    private Short dias;
    private Boolean view;
    private Boolean actualizarBoolean;
    private String tipoReporte;
    private LazyModel<ContCuentas> cuentaContableLazyModel;

    @PostConstruct
    public void inicializate() {
        loadModel();
        garantiaLazy = new LazyModel<>(Garantias.class);
        garantiaLazy.getFilterss().put("estado", true);
        garantiaLazy.getSorteds().put("adquisicion.numeroContrato", "ASC");
        garantiaLazy.setDistinct(false);
        tiposdoc = catalogoService.getTipoCuenta("tipo_doc", "ti_doc");
        riesgoAsegurado = catalogoService.getTipoCuenta("riesgo_asegurado", "FIEL_CUMPL");
        adquisiciones = adquisicionesService.findByNamedQuery("Adquisiciones.findByEstado", true);
    }

    public void loadModel() {
        view = Boolean.FALSE;
        actualizarBoolean = Boolean.FALSE;
        garantia = new Garantias();
        dias = 0;
        tipoReporte = "";
    }

    public void guardar() {
        boolean update = garantia.getId() != null;
        if (validar()) {
            if (!existsGarantia()) {
                if (garantia.getFechaHasta().compareTo(new Date()) < 0) {
                    JsfUtil.addWarningMessage("Informacíon", "Documento Vencido");
                } else {
                    JsfUtil.addInformationMessage("Informacíon", "Documento Vigente");
                }
                if (update) {
                    garantiaservice.create(loadGarantia());
                } else {
                    garantia.setFechaCreacion(new Date());
                    garantia.setDuracionDias(dias);
                    garantia.setUsuarioCreacion(userSession.getNameUser());
                    garantia = garantiaservice.create(garantia);
                }
                JsfUtil.addSuccessMessage("Poliza", "Garantía " + garantia.getNumPoliza() + (update ? " Actulizada" : " Registrada")
                        + " Con Éxito");
                loadModel();
            } else {
                JsfUtil.addErrorMessage("", "Ya existe una Garantía registrada con Contrato N° " + garantia.getAdquisicion().getNumeroContrato()
                        + ", Riesgo " + garantia.getRiesgoAsegurado().getTexto() + ", Fecha Desde " + garantia.getFechaDesde()
                        + " Fecha Hasta " + garantia.getFechaHasta());
            }
        }
    }

    private Garantias loadGarantia() {
        Garantias g = new Garantias(garantia.getTipoMoneda(), garantia.getNumPoliza(), garantia.getNumReferencia(),
                garantia.getFechaDesde(), garantia.getFechaHasta(), dias, garantia.getSuma(), garantia.getDetalle(),
                garantia.getRiesgoAsegurado(), garantia.getAdquisicion(), garantia.getTipoDocumento());
        g.setFechaCreacion(new Date());
        g.setDevolucion(Boolean.FALSE);
        g.setUsuarioCreacion(userSession.getNameUser());
        return g;
    }

    public Boolean existsGarantia() {
        return !garantiaservice.findAllGarantiasByAdquisicionAndRiesgoAseguradoAnFechas(garantia).isEmpty();
    }

    public Boolean validar() {
        if (garantia.getRiesgoAsegurado() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Seleccionar un Riesgo Asegurado");
            return false;
        }
        if (garantia.getTipoDocumento() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Seleccionar un Tipo de Documento");
            return false;
        }
        if (garantia.getSuma().intValue() <= 0) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar una Cantidad");
            return false;
        }
        if (garantia.getDetalle() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar un Detalle");
            return false;
        }
        return true;
    }

    public void actualizarVisualizarPoliza(Garantias g, boolean visualizar) {
        garantia = g;
        if (visualizar) {
            view = visualizar;
            actualizarBoolean = visualizar;
            calculoDias();
        } else {
            view = Boolean.TRUE;
            actualizarBoolean = visualizar;
            garantia.setFechaDesde(null);
            garantia.setFechaHasta(null);
        }
    }

    public void calculoDias() {
        this.dias = (TalentoHumano.diasDiferencia(garantia.getFechaDesde(), garantia.getFechaHasta())).shortValue();
    }

    public void upDialogdevolucionPoliza(Garantias g) {
        garantia = g;
        PrimeFaces.current().executeScript("PF('dialogDevolucion').show()");
    }

    public void devolucionPoliza() {
        if (garantia.getObservacionDevolucion().isEmpty()) {
            JsfUtil.addErrorMessage("", "Ingrese una observación");
            return;
        }
        garantia.setDevolucion(Boolean.TRUE);
        garantia.setFechaDevolucion(new Date());
        garantia.setObservacionDevolucion(garantia.getObservacionDevolucion().toUpperCase());
        garantiaservice.edit(garantia);
        JsfUtil.addSuccessMessage("", "Devolucion de Garantía " + garantia.getNumPoliza());
        PrimeFaces.current().executeScript("PF('dialogDevolucion').hide()");
        loadModel();
    }

    public Boolean polizaVigente(Date fechaHasta) {
        return fechaHasta.compareTo(new Date()) > 0;
    }

    public void generarReporte() {
        if (!tipoReporte.isEmpty() && tipoReporte != null) {
            List<Garantias> garantiaReporte = new ArrayList<>();
            servletSession.setNombreSubCarpeta("garantia");
            servletSession.setNombreReporte("garantiaPoliza");
            switch (tipoReporte) {
                case "TODOS":
                    garantiaReporte = garantiaservice.findAllGarantias();
                    break;
                case "VIGENTES": {
                    garantiaReporte = garantiaservice.findAllGarantiasByVigente(new Date());
                }
                break;
                case "VENCIDOS":
                    garantiaReporte = garantiaservice.findAllGarantiasByVencidas(new Date());
                    break;
                case "DEVUELTOS":
                    garantiaReporte = garantiaservice.findAllGarantiasByDevueltas();
                    break;
            }

//            servletSession.addParametro("elaborado", userSession.getUsuario().getFuncionario() != null ? userSession.getUsuario().getFuncionario().getPersona().getNombreCompleto() : userSession.getUsuario().getUsuario());
//            Servidor serv = clienteService.getUsuarioServidor(clienteService.getrolsUser(RolUsuario.tesorero)).getFuncionario();
//            servletSession.addParametro("nombre_tesorero", serv == null ? "" : serv.getPersona().getNombreCompleto());
            servletSession.addParametro("fecha_reporte", new Date());
            servletSession.setDataSource(garantiaReporte);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            loadModel();
            PrimeFaces.current().executeScript("PF('dialogReporte').hide()");
        } else {
            JsfUtil.addErrorMessage("", "Seleccione el tipo de roporte a Generar");
        }

    }

    public void openDlgCuentasContables() {
        this.cuentaContableLazyModel = new LazyModel<>(ContCuentas.class);
        this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
        this.cuentaContableLazyModel.getFilterss().put("estado", true);
        this.cuentaContableLazyModel.getFilterss().put("codigo:startsWith", "9");
        this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
    }

    public void aniadirCuentaContable() {
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
        PrimeFaces.current().ajax().update("gridCuentaContable");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            File file = FilesUtil.copyFileServer1(event, SisVars.RUTA_FILES_TEMP);
            System.out.println("file " + file.getAbsolutePath());
            garantia.setRutaArchivo(file.getAbsolutePath());
            JsfUtil.addSuccessMessage("INFORMACIÓN", "Valide su firma para firmar el Documento");

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo", "");
        }
    }

    public void showArchivoDevolucion(Garantias g) {
        servletSession.borrarDatos();
        servletSession.borrarParametros();
        servletSession.setContentType("application/pdf");
        servletSession.setNombreDocumento(g.getRutaArchivo());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/ViewSystemDocs");
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
//    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
//        return cuentaContableLazyModel;
//    }
//
//    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
//        this.cuentaContableLazyModel = cuentaContableLazyModel;
//    }

    public LazyModel<ContCuentas> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }

    public void setCuentaContableLazyModel(LazyModel<ContCuentas> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public List<Adquisiciones> getAdquisiciones() {
        return adquisiciones;
    }

    public void setAdquisiciones(List<Adquisiciones> adquisiciones) {
        this.adquisiciones = adquisiciones;
    }

    public Boolean getActualizarBoolean() {
        return actualizarBoolean;
    }

    public void setActualizarBoolean(Boolean actualizarBoolean) {
        this.actualizarBoolean = actualizarBoolean;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Garantias getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantias garantia) {
        this.garantia = garantia;
    }

    public LazyModel<Garantias> getGarantiaLazy() {
        return garantiaLazy;
    }

    public void setGarantiaLazy(LazyModel<Garantias> garantiaLazy) {
        this.garantiaLazy = garantiaLazy;
    }

    public List<CatalogoItem> getTiposdoc() {
        return tiposdoc;
    }

    public void setTiposdoc(List<CatalogoItem> tiposdoc) {
        this.tiposdoc = tiposdoc;
    }

    public List<CatalogoItem> getRiesgoAsegurado() {
        return riesgoAsegurado;
    }

    public void setRiesgoAsegurado(List<CatalogoItem> riesgoAsegurado) {
        this.riesgoAsegurado = riesgoAsegurado;
    }

    public Short getDias() {
        return dias;
    }

    public void setDias(Short dias) {
        this.dias = dias;
    }
//</editor-fold>
}
