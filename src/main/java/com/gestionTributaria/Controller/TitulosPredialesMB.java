/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnEstadoExoneracion;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
import com.gestionTributaria.Services.DocumentosServiceRS;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.ReportGenealModel;
import com.gestionTributaria.models.RubrosPorTipoLiq;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.osgi.application.ApplicationContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named(value = "titulosPrediales")
@ViewScoped
public class TitulosPredialesMB extends ReportGenealModel implements Serializable {

    @Inject
    private ManagerService services;
    @Inject
    private UserSession uSession;
    @Inject
    private ServletSession ss;
    @Inject
    private FinaRenPagoService finaRenPagoService;
    @Inject
    private FnSolicitudExoneracionServices fnSolicitudExoneracionServices;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private DocumentosServiceRS documentosServiceRS;
    @Inject
    private ContRegistroContable contableRegistro;

    private BigDecimal cemParquesPlazas;
    private BigDecimal cemAlcantarillado;

    private List<FinaRenLiquidacion> liquidacionseleccionadas;
    private LazyModel<FinaRenLiquidacion> liquidacionesLazy;
    private FinaRenLiquidacion original, posterior, cemLiquidacion;
    private List<FnExoneracionLiquidacion> exoneraciones;
    private List<RubrosPorTipoLiq> detList1, detList2;
    private FnExoneracionLiquidacion exoneracion;
    private List<FinaRenDetLiquidacion> detLiq;
    private String observacion;
    private Map<String, Object> param;
    private Documentos documento;
    private Documentos documentoDescargar;
    private LazyModel<Documentos> documentos;
    private List<Documentos> listaDocumentos;

    @PostConstruct
    public void initView() {

        documentoDescargar = new Documentos();
        listaDocumentos = new ArrayList<>();
        documento = new Documentos();
        param = new HashMap<>();
        liquidacionseleccionadas = new ArrayList<>();
        cemParquesPlazas = BigDecimal.ZERO;
        cemAlcantarillado = BigDecimal.ZERO;
        liquidacionesLazy = new LazyModel<>(FinaRenLiquidacion.class);
        liquidacionesLazy.getSorteds().put("fechaIngreso", "DESC");
        liquidacionesLazy.getFilterss().put("tipoLiquidacion.id", Arrays.asList(2L, 3L));
        liquidacionesLazy.getFilterss().put("estadoLiquidacion.id", Arrays.asList(2L, 4L));
        //         geDocumentosList = new ArrayList();

    }

    public void openDialog() {
        try {
            listaDocumentos = new ArrayList<>();
            if (liquidacionseleccionadas.isEmpty()) {
                JsfUtil.addWarningMessage("Información", "Debe seleccionar emisiones");
                return;
            } else {
                JsfUtil.executeJS("PF('dlgAnular').show()");
            }
        } catch (Exception e) {
            JsfUtil.addWarningMessage("Información", "Debe seleccionar emisiones");
        }
    }

    public void generarComprobante(FinaRenLiquidacion liquidacion) {

        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.borrarParametros();
        ss.instanciarParametros();
        ss.setNombreSubCarpeta("Emision");
        ss.addParametro("LOGO", path + SisVars.logoReportes);
        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/").concat("/Emision/"));
        ss.setNombreReporte("emisionPredioUrbanoAnulado");
        //ss.setTieneDatasource(Boolean.TRUE);
        ss.addParametro("ID_LIQUIDACION", liquidacion.getId());
        JsfUtil.redirectNewTab("/sgmEE/Documento");

    }

    public void openDialogCem(FinaRenLiquidacion liquidacion) {
        this.cemLiquidacion = liquidacion;
        JsfUtil.executeJS("PF('dlgAddCem').show()");
        JsfUtil.update("frmCEMValor");
    }

    public void addCem() {
        BigDecimal totalPago = this.cemLiquidacion.getTotalPago();
        System.out.println("cemAlcantarillado " + this.cemAlcantarillado);
        System.out.println("cemParquesPlazas " + this.cemParquesPlazas);

        this.cemLiquidacion.setTotalPago(totalPago.add(this.cemAlcantarillado).add(this.cemParquesPlazas));
        this.cemLiquidacion.setSaldo(totalPago.add(this.cemAlcantarillado).add(this.cemParquesPlazas));
        System.out.println("totalPago" + totalPago);
        services.save(this.cemLiquidacion);
        FinaRenDetLiquidacion detLiquidacion = new FinaRenDetLiquidacion(cemParquesPlazas, this.cemLiquidacion, 640L);
        services.save(detLiquidacion);
        detLiquidacion = new FinaRenDetLiquidacion(cemParquesPlazas, this.cemLiquidacion, 641L);
        services.save(detLiquidacion);
    }

    public void anularLiquidacion() {
        if (listaDocumentos.size() == 0) {
            JsfUtil.addErrorMessage("Información", "Debe Subir un Documento Para Continuar");
            return;
        }
        if (observacion == null || observacion.equals("")) {
            JsfUtil.addErrorMessage("Información", "Las observaciones son obligatorias");
            return;
        }
        FinaRenTipoLiquidacion tipoLiq = null;
        for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
            if (contableRegistro.isEmisionContabilizada(rl)) {
                JsfUtil.update("La Emisión " + rl.getIdLiquidacion() + " ya se encuentra contabilizada");
                return;
            } else {
                contableRegistro.anularEmision(rl);
            }

            if (tipoLiq == null) {
                tipoLiq = rl.getTipoLiquidacion();
            } else {
                if (tipoLiq != null) {
                    if (!tipoLiq.equals(rl.getTipoLiquidacion())) {
                        JsfUtil.addErrorMessage("Error", "Solo Puede dar de Baja a Emisiones Del mismo tipo");
                        return;
                    }
                }
            }
            if (bajaAnioActual()) {
                if (rl.getAnio() < Utils.getAnio(new Date())) {
                    JsfUtil.addErrorMessage("Información", "Solo Puede dar de Baja a Emisiones del Año Actual");
                    return;
                }

            }
        }
        for (FinaRenLiquidacion data : liquidacionseleccionadas) {
            FinaRenLiquidacion item = Utils.clone(data);
            item.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(4L));
            item.setFechaAnulacion(new Date());
            item.setObservacion(observacion);
            finaRenLiquidacionService.edit(item);
        }

//        Boolean prediosDiferentes = Boolean.FALSE;
//        //SE ORDERNA LA ,LISTA PARA OBTENER EL MENOR DE LOS ANIOS
//        Collections.sort(liquidacionseleccionadas, (FinaRenLiquidacion rl1, FinaRenLiquidacion rl2)
//                -> new Integer(rl1.getAnio())
//                        .compareTo(rl2.getAnio()));
//        CatPredioModel predio = null;
//        if (tipoLiq.getId().equals(13l) || tipoLiq.getId().equals(7l)) {
//            List<FinaRenLiquidacion> liquidacionesTemp = new ArrayList();
//            List<CatPredioModel> prediosUrbanos = new ArrayList();
//            FnSolicitudExoneracionPredios exoneracionPredios = null;
//            List<FnSolicitudExoneracionPredios> prediosEnSolicitud = new ArrayList<>();
//            for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
//                exoneracionPredios = new FnSolicitudExoneracionPredios();
//                exoneracionPredios.setPredio(rl.getPredio());
//                CatPredioModel busquedaPredio = services.buscarPredio(rl.getPredio().getId());
//                prediosUrbanos.add(busquedaPredio);
//                if (!prediosUrbanos.isEmpty()) {
//                    for (CatPredioModel cp : prediosUrbanos) {
////                        if (!rl.getPredio().equals(cp.getId())) {
////                            prediosDiferentes = Boolean.TRUE;
////                            break;
////                        }
//                    }
//                }
//
//                rl.setObservacion(observacion);
//                prediosEnSolicitud.add(exoneracionPredios);
//                liquidacionesTemp.add(rl);
//            }
//            if (prediosDiferentes) {
//                JsfUtil.addErrorMessage("Información", "Las anulaciones deben de ser un mismo predio");
//                return;
//
//            }
//            FnExoneracionTipo tipo = services.find(FnExoneracionTipo.class, 40L);
//            List<FinaRenPago> pagos;
//            if (!prediosUrbanos.isEmpty()) {
//                predio = prediosUrbanos.get(0);
//                if (tipo.getId() != 40L) {
//                    for (CatPredioModel pu : prediosUrbanos) {
//                        pagos = finaRenPagoService.getPagosByPredioTipoLiquidacionAnio(pu.getId(), null, new FinaRenTipoLiquidacion(2L),
//                                liquidacionseleccionadas.get(0).getAnio(), liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getAnio());
//                        if (pagos != null && !pagos.isEmpty()) {
//                            JsfUtil.addInformationMessage("Información", "Solo procede las Emisiones Pendientes de Pago");
//                            return;
//                        }
//                    }
//                } else {
//                    for (CatPredioModel pu : prediosUrbanos) {
//                        pagos = services.getPagosByPredioTipoLiquidacionAnioPagada(pu.getId(), null, new FinaRenTipoLiquidacion(2L),
//                                liquidacionseleccionadas.get(0).getAnio(), liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getAnio());
//                        if (pagos != null && !pagos.isEmpty()) {
//                            JsfUtil.addInformationMessage("Información", "Solo procede las Emisiones Pendientes de Pago");
//                            return;
//                        }
//                    }
//                }
//            }
//
//            for (FinaRenLiquidacion r : liquidacionesTemp) {
//                if (r.getId() != null) {
//                    finaRenLiquidacionService.edit(r);
//                } else {
//                    finaRenLiquidacionService.create(r);
//                }
//            }
//            FnSolicitudExoneracion solicitud = new FnSolicitudExoneracion();
//            solicitud.setExoneracionTipo(tipo);
//            solicitud.setFechaIngreso(new Date());
//            solicitud.setUsuarioCreacion(uSession.getNameUser());
//            solicitud.setSolicitante(liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getComprador());
//            solicitud.setPredio(liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getPredio());
//            solicitud.setAnioInicio(liquidacionseleccionadas.get(0).getAnio());
//            solicitud.setAnioFin(liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getAnio());
//
//            solicitud.setEstado(new FnEstadoExoneracion(2L));
//            if (solicitud.getId() != null) {
//                solicitud = (FnSolicitudExoneracion) fnSolicitudExoneracionServices.create(solicitud);
//            } else {
//                fnSolicitudExoneracionServices.edit(solicitud);
//            }
//
//            fnSolicitudExoneracionServices.registarDatoSolicitudExoneracion(solicitud, null, prediosEnSolicitud);
//            exoneraciones = services.aplicarExoneracion(null, solicitud, uSession.getNameUser());
//
//            if (exoneraciones != null && !exoneraciones.isEmpty()) {
//                detList1 = new ArrayList();
//                detList2 = new ArrayList();
//                FinaRenRubrosLiquidacion rubro;
//                exoneracion = exoneraciones.get(0);
//                if (exoneracion.getLiquidacionOriginal() != null) {
//                    original = exoneracion.getLiquidacionOriginal();
//                    param = new HashMap<>();
//                    param.put("liquidacion", original);
//                    detLiq = services.findAllQuery("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion ORDER BY r.rubro ASC", param);
//
//                    for (FinaRenDetLiquidacion temp : detLiq) {
//                        rubro = (FinaRenRubrosLiquidacion) services.find(FinaRenRubrosLiquidacion.class,
//                                temp.getRubro());
//                        detList1.add(new RubrosPorTipoLiq(rubro, temp.getValor()));
//                    }
//                }
//                if (exoneracion.getLiquidacionPosterior() != null) {
//                    posterior = exoneracion.getLiquidacionPosterior();
//                    posterior.setTotalPago(posterior.getTotalPago().setScale(2, RoundingMode.HALF_UP));
//                    posterior.setUsuarioIngreso(uSession.getNameUser());
//                    if (posterior.getId() != null) {
//                        finaRenLiquidacionService.edit(posterior);
//                    } else {
//                        posterior = (FinaRenLiquidacion) services.save(posterior);
//                    }
//
//                    posterior = (FinaRenLiquidacion) services.find(FinaRenLiquidacion.class, posterior.getId());
//                    param = new HashMap<>();
//                    param.put("liquidacion", posterior);
//                    detLiq = services.findAllQuery("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion ORDER BY r.rubro ASC", param);
//
//                    for (FinaRenDetLiquidacion temp2 : detLiq) {
//                        rubro = (FinaRenRubrosLiquidacion) services.find(FinaRenRubrosLiquidacion.class, temp2.getRubro());
//                        detList2.add(new RubrosPorTipoLiq(rubro, temp2.getValor()));
//                    }
//                }
//                JsfUtil.update("frmExoAp");
//                JsfUtil.executeJS("PF('dlgAnular').hide()");
//                JsfUtil.addInformationMessage("Info", "Exoneracion aplicada correctamente");
//                exoneraciones = new ArrayList();
//                exoneracion = new FnExoneracionLiquidacion();
//                solicitud = new FnSolicitudExoneracion();
//                posterior = new FinaRenLiquidacion();
//                original = new FinaRenLiquidacion();
//
//            } else {
//                JsfUtil.addInformationMessage("Info", "Hubo un problema al aplicar la exoneracion, Revise las emisiones realizadas con anterioridad");
//            }
//        } else {
//            for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
//                if (!rl.getEstadoLiquidacion().equals(new FinaRenEstadoLiquidacion(2L))) {
//                    JsfUtil.addInformationMessage("Advertencia", "Solo se permite dar de baja a liquidacion pendientes de pago.");
//                    return;
//                }
//                List<FinaRenPago> pagos = finaRenPagoService.obtenerPagos(rl, true);
//                if (Utils.isNotEmpty(pagos)) {
//                    JsfUtil.addInformationMessage("Advertencia", "Una de las liquidaciones seleccionadas tiene pagos realizaizados.");
//                    return;
//                }
//            }
//            for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
//                rl.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(4L));
//                rl.setObservacion(observacion);
//                finaRenLiquidacionService.edit(rl);
//            }
//            JsfUtil.executeJS("PF('dlgAnular').hide()");
//            JsfUtil.addInformationMessage("Info", "Baja de titulo realizado correctamente");
//        }
//
        for (Documentos doc : listaDocumentos) {
            if (doc.getId() != null) {
                documentosServiceRS.edit(doc);
            } else {
                documentosServiceRS.create(doc);
            }

        }
        observacion = null;
        listaDocumentos = new ArrayList<>();
        liquidacionseleccionadas = new ArrayList();
        liquidacionesLazy = new LazyModel(FinaRenLiquidacion.class);
        liquidacionesLazy.getFilterss().put("estadoLiquidacion.id", Arrays.asList(2L, 4L));
        liquidacionesLazy.getSorteds().put("fechaIngreso", "DESC");
        JsfUtil.update("mainForm");

    }

    public void anularLiquidacionPagoIndebido() {
        if (observacion == null || observacion.equals("")) {
            JsfUtil.addWarningMessage("Información", "Las observaciones son obligatorias");
            return;
        }
        if (liquidacionseleccionadas == null || liquidacionseleccionadas.size() > 1) {
            JsfUtil.addWarningMessage("Información", "Pago Indebido se realiza individualmente");
            return;
        }
        if (liquidacionseleccionadas.isEmpty()) {
            JsfUtil.addWarningMessage("Información", "Debe seleccionar una Emisión");
            return;
        }
        FinaRenLiquidacion l = liquidacionseleccionadas.get(0);
        if (l.getEstadoLiquidacion().getId().equals(1L)) {
            param = new HashMap<>();
            param.put("liquidacion", l);
            List<FinaRenPago> pagos = services.findAllQuery("SELECT p FROM FinaRenPago p WHERE p.liquidacion=:liquidacion AND p.estado=true AND p.pagoIndebido=false", param);
            if (pagos != null && pagos.size() >= 1) {
                FinaRenPago p = pagos.get(0);
                p.setObservacion(p.getObservacion() != null ? (p.getObservacion() + " " + observacion) : observacion);
                p.setPagoIndebido(Boolean.TRUE);
                services.save(p);
                l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                param = new HashMap<>();
                param.put("liquidacion", l);
                List<FinaRenDetLiquidacion> detalles = services.findAllQuery("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion ORDER BY r.rubro ASC", param);
                for (FinaRenDetLiquidacion d : detalles) {
                    d.setValorRecaudado(BigDecimal.ZERO);
                    services.save(d);
                }
                l.setSaldo(l.getTotalPago());
                services.save(l);
                //

                liquidacionesLazy = new LazyModel(FinaRenLiquidacion.class);
                liquidacionesLazy.addFilter("estadoLiquidacion:ne", 3L);
                liquidacionesLazy.addSorted("anio", "DESC");
                liquidacionesLazy.addSorted("fechaIngreso", "DESC");
                JsfUtil.redirect(CONFIG.URL_APP + "rentas/bajasTitulos");
            } else {
                JsfUtil.addInformationMessage("Información", "Proceden solo Pagos Totales");
            }
        } else {
            JsfUtil.addInformationMessage("Información", "Proceden solo las Emisiones pagadas");
        }
    }

    public Boolean bajaAnioActual() {
//        if (session.getIsAdmin()) {
//            return false;
//        } else {
//            if (session.getDepts().contains(2L)) {
//                if (session.getRoles().contains(66L)) {
//                    return true;
//                } else if (session.getRoles().contains(68L)) {
//                    return true;
//                } else if (session.getRoles().contains(174L)) {
//                    return true;
//                }
//            } else {
//                return false;
//            }
//        }
        return false;
    }

    public void handleFileDocumentBySave(FileUploadEvent event) throws ClassNotFoundException {
        try {
            String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            documento = new Documentos();
            documento.setDepartamento(uSession.getDepartamento());
            documento.setRutaDocumento(ruta);
            documento.setFechaCreacion(new Date());
            documento.setNombre(event.getFile().getFileName());
            documento.setDescripcion(event.getFile().getContentType());
            documento.setEstado(Boolean.TRUE);
            for (FinaRenLiquidacion item : liquidacionseleccionadas) {
                documento.setClaseNombre(item.getClass().getPackage().getName() + "." + item.getClass().getSimpleName());
                documento.setIdentificador(item.getId());
                System.out.println("->" + documento.getClaseNombre());

                listaDocumentos.add(documento);
            }

            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subri el documento");
        }
    }

    public void descargarDocumento(FinaRenLiquidacion r) throws ClassNotFoundException {
        original = new FinaRenLiquidacion();
        original = r;
        documentoDescargar = new Documentos();
        documentoDescargar = (Documentos) services.documentoGestionTribtaria(r.getClass().getPackage().getName() + "." + r.getClass().getSimpleName(), r.getId());
        System.out.println("documentoDescargar " + documentoDescargar.getRutaDocumento());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "descargaDoc?ruta=" + documentoDescargar.getRutaDocumento());

    }

    public void viewDocumento(FinaRenLiquidacion r) throws ClassNotFoundException {
        original = new FinaRenLiquidacion();
        original = r;
        documentoDescargar = new Documentos();
        documentoDescargar = (Documentos) services.documentoGestionTribtaria(r.getClass().getPackage().getName() + "." + r.getClass().getSimpleName(), r.getId());
        System.out.println("documentoDescargar " + documentoDescargar.getRutaDocumento());

    }

    public void inactivarDocumento(Documentos doc, int index) {
        doc = listaDocumentos.get(index);
        if (doc.getId() != null) {
            doc.setEstado(Boolean.FALSE);
            services.update(doc);
        }

        listaDocumentos.remove(index);
        JsfUtil.addInformationMessage("", "El documento se inactivo con exito");
    }

    //<editor-fold defaultstate="collapsed" desc="setter and getter">
    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public UserSession getuSession() {
        return uSession;
    }

    public void setuSession(UserSession uSession) {
        this.uSession = uSession;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public BigDecimal getCemParquesPlazas() {
        return cemParquesPlazas;
    }

    public void setCemParquesPlazas(BigDecimal cemParquesPlazas) {
        this.cemParquesPlazas = cemParquesPlazas;
    }

    public BigDecimal getCemAlcantarillado() {
        return cemAlcantarillado;
    }

    public void setCemAlcantarillado(BigDecimal cemAlcantarillado) {
        this.cemAlcantarillado = cemAlcantarillado;
    }

    public List<FinaRenLiquidacion> getLiquidacionseleccionadas() {
        return liquidacionseleccionadas;
    }

    public void setLiquidacionseleccionadas(List<FinaRenLiquidacion> liquidacionseleccionadas) {
        this.liquidacionseleccionadas = liquidacionseleccionadas;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidacionesLazy() {
        return liquidacionesLazy;
    }

    public void setLiquidacionesLazy(LazyModel<FinaRenLiquidacion> liquidacionesLazy) {
        this.liquidacionesLazy = liquidacionesLazy;
    }

    public FinaRenLiquidacion getOriginal() {
        return original;
    }

    public void setOriginal(FinaRenLiquidacion original) {
        this.original = original;
    }

    public FinaRenLiquidacion getPosterior() {
        return posterior;
    }

    public void setPosterior(FinaRenLiquidacion posterior) {
        this.posterior = posterior;
    }

    public FinaRenLiquidacion getCemLiquidacion() {
        return cemLiquidacion;
    }

    public void setCemLiquidacion(FinaRenLiquidacion cemLiquidacion) {
        this.cemLiquidacion = cemLiquidacion;
    }

    public List<FnExoneracionLiquidacion> getExoneraciones() {
        return exoneraciones;
    }

    public void setExoneraciones(List<FnExoneracionLiquidacion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }

    public List<RubrosPorTipoLiq> getDetList1() {
        return detList1;
    }

    public void setDetList1(List<RubrosPorTipoLiq> detList1) {
        this.detList1 = detList1;
    }

    public List<RubrosPorTipoLiq> getDetList2() {
        return detList2;
    }

    public void setDetList2(List<RubrosPorTipoLiq> detList2) {
        this.detList2 = detList2;
    }

    public FnExoneracionLiquidacion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(FnExoneracionLiquidacion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public List<FinaRenDetLiquidacion> getDetLiq() {
        return detLiq;
    }

    public void setDetLiq(List<FinaRenDetLiquidacion> detLiq) {
        this.detLiq = detLiq;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public LazyModel<Documentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(LazyModel<Documentos> documentos) {
        this.documentos = documentos;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Documentos getDocumentoDescargar() {
        return documentoDescargar;
    }

    public void setDocumentoDescargar(Documentos documentoDescargar) {
        this.documentoDescargar = documentoDescargar;
    }

//</editor-fold>
}
