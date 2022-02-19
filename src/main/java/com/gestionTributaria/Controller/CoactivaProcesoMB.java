/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import com.gestionTributaria.Entities.CoaMedidasCautelares;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.Entities.FnLiquidacionConvenio;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.CoaJuicioPredioServices;
import com.gestionTributaria.Services.CoaJuicioService;
import com.gestionTributaria.Services.FnConvenioPagoDetallerService;
import com.gestionTributaria.Services.FnConvenioPagoService;
import com.gestionTributaria.Services.FnLiquidacionConvenioService;
import com.gestionTributaria.Services.FnSolicitudExoneracionPrediosService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.MedidasCautelaresServices;
import com.gestionTributaria.Services.ResolucionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class CoactivaProcesoMB extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(CoactivaProcesoMB.class.getName());

    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private ManagerService services;
    @Inject
    private UserSession uSession;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private CoaJuicioService coaJuicioService;
    @Inject
    private CoaJuicioPredioServices coaJuicioPredioServices;
    @Inject
    private CatPredioPropietarioService catPredioPropietarioService;
    @Inject
    private ManagerService manageServices;
    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private MedidasCautelaresServices cautelaresServices;
    @Inject
    private ResolucionesService resolucionesService;
    @Inject
    private FnSolicitudExoneracionPrediosService solicitudExoneracionService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private FnLiquidacionConvenioService convenioService;
    @Inject
    private FnConvenioPagoService convenioPagoService;
    @Inject
    private FnConvenioPagoDetallerService convenioPagoDetallerService;
    private CoaJuicio juicio = new CoaJuicio();
    private List<CoaJuicioPredio> predioJuicios;
    private List<CatPredio> predios;
    private CatPredioPropietario propietario;
    private String observaciones;
    private Documentos documento;
    private List<Documentos> listaDocumentos;
    private List<CatalogoItem> medidasCautelares;
    private List<CatalogoItem> medidasCautelaresAplicadas;
    private FnResolucion resolucion;
    private FnLiquidacionConvenio liquidacionConvenio;
    private FnConvenioPago convenioPago;
    private List<FnConvenioPagoDetalle> detalleConvenio;
    private Long diferenciaDiasCuota;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private BigDecimal totalEmisiones;
    private FinaRenTipoLiquidacion tipoSelect;
    private BigDecimal sumaTotalCoactivaConv = new BigDecimal("0.00");
    private BigDecimal totalCoactiva;
    private FnSolicitudExoneracion exoneracion;
    private List<FnSolicitudExoneracionPredios> listaExoneraciones;
    private Boolean embargo = Boolean.FALSE;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                tipoLiquidacion = new FinaRenTipoLiquidacion();
                liquidacionConvenio = new FnLiquidacionConvenio();
                documento = new Documentos();
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                //buscando juicio
                juicio = coaJuicioService.findByNumeroTramite(tramite);
                ///BUSCANDO LOS PREDIOS DEL JUICIO se muestran en la tabla
                predioJuicios = coaJuicioPredioServices.findByIdJuicio(juicio.getId());
                tipoLiquidacion = predioJuicios.get(0).getLiquidacion().getTipoLiquidacion();
                predios = coaJuicioPredioServices.fynByPredioJuicio(juicio.getId());
                propietario = catPredioPropietarioService.findByPropietarioPredio(predioJuicios.get(0).getPredio());
                listaDocumentos = new ArrayList<>();
                medidasCautelares = catalogoService.findCatalogoItems("Medidas_Cautelares");
                medidasCautelaresAplicadas = cautelaresServices.findMedidasCautelares(juicio);
                listaExoneraciones = new ArrayList<>();
                if (tarea.getName().equals("Titulos en Convenio de Pago")) {
                    liquidacionConvenio = convenioService.findLiquidacionConvenioIdLiquidacion(predioJuicios.get(0).getLiquidacion());
                    detalleConvenio = convenioPagoDetallerService.findByDetallePago(liquidacionConvenio.getConvenio());
                    //sacar la fecha proxima de pago
                    int contador = 0;
                    for (FnConvenioPagoDetalle detalle : detalleConvenio) {
                        if (detalle.getEstado().equals(Boolean.FALSE)) {
                            contador++;
                        }
                    }
                    diferenciaDiasCuota = Long.valueOf(Utils.getDiasResta(new Date(), detalleConvenio.get(contador).getFechaMaximaPago()));
                    System.out.println("TOTAL: " + diferenciaDiasCuota);
                }
                medidasCautelaresAplicadas();
            } else {
                listaDocumentos = new ArrayList<>();
            }
        } catch (Exception ex) {
            System.out.println("Exception proceso coactiva" + ex.getMessage());
        }
    }

    public void enviarCorreo() {
        try {
            Correo c = new Correo();
            c.setDestinatario("david271998@gmail.com");
            c.setAsunto("CITACION PARA COACTIVA");
            c.setMensaje("<html lang=\"es\">\n"
                    + "<head>\n"
                    + "<meta charset=\"utf-8\"/>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<p style=\"width:200px;\">SR(a). _____________________________________ POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE SE LE ACABA DE APLICAR UN JUICIO DE COACTIVA \n"
                    + " SEGÚN EL NUMERO DE JUICIO N°" + juicio.getNumeroJuicio() + " </p>"
                    + "</body>\n"
                    + "</html>");
            System.out.println("mensaje>>>" + c.getMensaje());
            katalinaService.enviarCorreo(c);
            coaJuicioService.edit(juicio);
            JsfUtil.update("mainForm");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "ERROR ENVIAR CORREO>>>>", ex);
        }
    }

    public void verificarResolucion() {
        listaExoneraciones = solicitudExoneracionService.getExoneracionByPredio(predioJuicios.get(0).getPredio());
        if (!listaExoneraciones.isEmpty()) {
            JsfUtil.update("dlgResoluciones");
            JsfUtil.executeJS("PF('dlgResoluciones').show()");
            System.out.println("EL TAMAÑO DE LA LISTA" + listaExoneraciones.size());
        } else {
            JsfUtil.addInformationMessage("NO TIENE RESOLUCIONES APLICADAS AL PREDIO", "");
        }
    }

    public void verificarConvenioPago(String key) {
        Boolean bandera = Boolean.TRUE;
        //verificamos que todas las liquidaciones en coactiva están en la tabla de liquidaciones en convenioF
        for (CoaJuicioPredio juicio : predioJuicios) {
            liquidacionConvenio = convenioService.findLiquidacionConvenioIdLiquidacion(juicio.getLiquidacion());
            if (liquidacionConvenio != null) {
                bandera = Boolean.TRUE;
            } else {
                bandera = Boolean.FALSE;
                break;
            }
        }
        if (bandera.equals(Boolean.FALSE)) {
            JsfUtil.addInformationMessage("NO TIENE  UN CONVENIO DE PAGO", "");
        } else {
            convenioPago = convenioPagoService.findbyConvenioPago(liquidacionConvenio.getConvenio());
            if (convenioPago != null) {
                JsfUtil.addWarningMessage("TIENE UN CONVENIO DE PAGO APROBADO", "");
                continuarTarea("CONVENIO");
            } else {
                JsfUtil.addWarningMessage("TIENE UN CONVENIO DE PAGO, PERO NO ESTÁ APROBADO", "");
            }
        }
    }

    public void verDocumento(FnSolicitudExoneracion exoneracion) {
        System.out.println("ID DE LA EXONERACION " + exoneracion.getId());
        documento = (Documentos) services.documentoGestionTribtaria(exoneracion.getClass().getPackage().getName() + "." + exoneracion.getClass().getSimpleName(), exoneracion.getId());
        if (documento != null) {
            JsfUtil.executeJS("PF('dowloadDoc').show()");
        } else {
            JsfUtil.addInformationMessage("AUN NO SE SUBE EL DOCUMENTO", "");
        }
    }

    public void seleccionarExoneracion(FnSolicitudExoneracion exoneracion) {
        juicio.setSolicitudExoneracion(exoneracion);
        coaJuicioService.edit(juicio);
        JsfUtil.addInformationMessage("", "SELECCIONÓ LA RESOLUCIÓN: " + exoneracion.getExoneracionTipo().getDescripcion());
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            this.getParamts().put("abogado", session.getNameUser());
            this.getParamts().put("prioridad", 50);
            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void continuarTarea(String key) {
        try {
            boolean bandera = false;
            observacion.setObservacion(observaciones);
            if (!observacion.getObservacion().isEmpty()) {
                this.getParamts().put("juezcoactiva", session.getNameUser());
                this.getParamts().put("prioridad", 50);
                if (key.equals("MEDIDAS")) {
                    juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("EMC"));
                    coaJuicioService.edit(juicio);
                    this.getParamts().put("opcion", 1);
                    bandera = true;
                }
                if (key.equals("EXTINCION")) {
                    if (levantamientoMedidasCautelares()) {
                        if (validacionExtincion() == true) {
                            if (validarSubirDocumento() == true) {
                                juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("ED"));
                                coaJuicioService.edit(juicio);
                                this.getParamts().put("opcion", 2);
                                bandera = true;
                            } else {
                                JsfUtil.addInformationMessage("Debe subir un documento", "");
                            }
                        } else {
                            JsfUtil.addInformationMessage("Aun tiene deudas pendientes", "");
                            bandera = false;
                        }
                    } else {
                        JsfUtil.addInformationMessage("", "Debe levantar las medidas cautelares");
                    }
                }
                if (key.equals("RESOLUCION")) {
                    juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("BAC"));
                    coaJuicioService.edit(juicio);
                    this.getParamts().put("opcion", 2);
                    bandera = true;
                }
                if (key.equals("CONVENIO")) {
                    juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("CDP"));
                    coaJuicioService.edit(juicio);
                    this.getParamts().put("opcion", 1);
                    bandera = true;
                }
                //aqui terminan las condiciones
                if (saveTramite() == null) {
                    return;
                }
                if (bandera == true) {
                    this.session.setVarTemp(null);
                    super.completeTask((HashMap<String, Object>) getParamts());
                    JsfUtil.redirectFaces("/procesos/bandeja-tareas");
                }
            } else {
                JsfUtil.addWarningMessage("La Observación es necesaria", "");
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "completas tareas", ex);
        }

    }

    public boolean levantamientoMedidasCautelares() {
        List<CoaMedidasCautelares> medidasCautelares = new ArrayList<>();
        boolean bandera = false;
        try {
            medidasCautelares = cautelaresServices.findMedidasCautelaresJuicio(juicio);
            if (medidasCautelares.isEmpty()) {
                bandera = true;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al validar el levantamiento de medidas cautelares", ex);
        }
        return bandera;
    }

    public Boolean validarSubirDocumento() {
        Boolean bandera = false;
        if (!listaDocumentos.isEmpty()) {
            bandera = true;
        } else {
            bandera = false;
        }
        return bandera;
    }

    public boolean validacionExtincion() {
        boolean bandera = true;
        if (tarea.getName().equals("Titulos en Convenio de Pago")) {
            for (FnConvenioPagoDetalle cuota : detalleConvenio) {

            }
        } else {
            for (CoaJuicioPredio jp : predioJuicios) {
                if (jp.getLiquidacion().getEstadoLiquidacion().getCodigo().equals("pagado")) {
                    bandera = true;
                    System.out.println("valor bandera " + bandera);
                } else {
                    bandera = false;
                    System.out.println("valor bandera " + bandera);
                    break;
                }
            }
        }

        return bandera;
    }

    public void finalizarProceso() {
        try {
            Boolean bandera = true;
            if (listaDocumentos.size() > 0) {
                observacion.setObservacion(observaciones);
                juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("ED"));
                coaJuicioService.edit(juicio);
                bandera = true;
            }
            if (bandera == true) {
                if (saveTramite() == null) {
                    return;
                }
                this.session.setVarTemp(null);
                super.completeTask((HashMap<String, Object>) getParamts());
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            } else {
                JsfUtil.addInformationMessage("INFO;", "DEBE INGRESAR UN DOCUMENTO");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
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
            documento.setClaseNombre(juicio.getClass().getPackage().getName() + "." + juicio.getClass().getSimpleName());
            documento.setIdentificador(juicio.getId());
            listaDocumentos.add(documento);
            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            for (Documentos doc : listaDocumentos) {
                services.save(doc);
            }
            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
            guardarMedidasCautelares();
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subri el documento");
        }
    }

    public void handleFileUploadTramite(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            documento = new Documentos();
            documento.setFechaCreacion(new Date());
            documento.setDescripcion(event.getFile().getContentType());
            documento.setNombre(event.getFile().getFileName());
            listaDocumentos.add(documento);
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
            guardarMedidasCautelares();
            JsfUtil.executeJS("PF('doc').hide()");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void guardarMedidasCautelares() {
        CoaMedidasCautelares medidasCautelare;
        System.out.println("RESULTADO" + cautelaresServices.actualizarMedidasCautelares(juicio.getId()));
//        if (cautelaresServices.actualizarMedidasCautelares(juicio.getId()) > 1) {
        for (CatalogoItem medidas : medidasCautelaresAplicadas) {
            medidasCautelare = cautelaresServices.findMedidaCauleraExistente(juicio.getId(), medidas.getId());
            if (medidasCautelare.getId() != null) {
                //si es verdadera edita
                medidasCautelare.setEstado(Boolean.TRUE);
                cautelaresServices.edit(medidasCautelare);
            } else {
                //si es falso crea
                medidasCautelare = new CoaMedidasCautelares();
                medidasCautelare.setMedidaCautelar(medidas);
                medidasCautelare.setEstado(Boolean.TRUE);
                medidasCautelare.setJuicios(juicio);
                cautelaresServices.create(medidasCautelare);
            }
//            }
        }
    }

    public void calculoTotalPago(List<FinaRenLiquidacion> listado, Date fechaPago) {
        Boolean flag = true;
        totalEmisiones = new BigDecimal("0.00");
        for (FinaRenLiquidacion e : listado) {
            if (tipoSelect != null && (tipoSelect.getId().equals(2L) || tipoSelect.getId().equals(3L))) {
                // Pregunta por el año actual, si ya fue exonerado y si se encontró la solicitud de exoneración en el anterior método.
                if (Objects.equals(e.getAnio(), Utils.getAnio(new Date())) && e.getEstaExonerado() && exoneracion != null) {
                    exoneracion = null;
                }
                //

                if (flag && e.getEstadoLiquidacion().getId().compareTo(2L) == 0) {
                    if (e.getEstadoCoactiva() == 2) {
                        flag = false;
//                    JsfUtil.executeJS("PF('dlgMensaje').show();");
                    }
                }
//                System.out.println("lista ren_pago>>" + Utils.isNotEmpty(e.getRenPagoCollection()));
                if (e.getEstadoLiquidacion().getId().compareTo(2L) == 0) {
                    try {
                        //CALCULO DE DESCUENTO-RECARGO-INTERE
//                        liquidacionService.getEntityManager().detach(e);
                        long totalSum = 0;
                        long startTime = System.currentTimeMillis();
                        if (e.getTipoLiquidacion().getId().equals(2L) || e.getTipoLiquidacion().getId().equals(3L)) {
                            e = recaudacionService.realizarDescuentoRecargaInteresPredial(e, fechaPago);
                        }
                        long endTime = System.currentTimeMillis() - startTime;
                        //imprime tiempo transcurrido en ms
                        System.out.println("despues de ejecutar el metodo de calculos>>>Duración " + endTime + " milisegundos.");
                        e.calcularPagoConCoactiva();
                        totalEmisiones = totalEmisiones.add(e.getPagoFinal());
                        totalEmisiones = totalEmisiones.setScale(2, RoundingMode.HALF_UP);
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, null, e);
                    }
                }
            } else {
                System.out.println("generales interes");
                e = recaudacionService.getInteresesGenerales(e, new Date());
                e.calcularPago();
//                e.calcularPagoConCoactiva();
                totalEmisiones = totalEmisiones.add(e.getPagoFinal());
            }
            this.totalCoactiva = BigDecimal.ZERO;
            this.totalCoactiva = this.totalCoactiva.add(e.getValorCoactiva() != null ? e.getValorCoactiva() : BigDecimal.ZERO);
        }
    }

    public void inactivarJuicio() {
        juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("ED"));
    }

    public void aplicarMedidasCautelares() {
        juicio.setEstadoJuicio(coaJuicioPredioServices.findByEstado("EMC"));
    }

    public void medidasCautelaresAplicadas() {
        if (medidasCautelaresAplicadas.size() > 1) {
            embargo = Boolean.FALSE;
            for (CatalogoItem item : medidasCautelaresAplicadas) {
                if (item.getCodigo().equals("retencion_cuenta_bancaria")) {
                    embargo = Boolean.TRUE;
                }
            }
        }
    }



    //<editor-fold defaultstate="collapsed" desc="get and set">
    public CoaJuicio getJuicio() {
        return juicio;
    }

    public void setJuicio(CoaJuicio juicio) {
        this.juicio = juicio;
    }

    public List<FnConvenioPagoDetalle> getDetalleConvenio() {
        return detalleConvenio;
    }

    public void setDetalleConvenio(List<FnConvenioPagoDetalle> detalleConvenio) {
        this.detalleConvenio = detalleConvenio;
    }

    public FnResolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(FnResolucion resolucion) {
        this.resolucion = resolucion;
    }

    public FnLiquidacionConvenio getLiquidacionConvenio() {
        return liquidacionConvenio;
    }

    public void setLiquidacionConvenio(FnLiquidacionConvenio liquidacionConvenio) {
        this.liquidacionConvenio = liquidacionConvenio;
    }

    public List<CoaJuicioPredio> getPredioJuicios() {
        return predioJuicios;
    }

    public void setPredioJuicios(List<CoaJuicioPredio> predioJuicios) {
        this.predioJuicios = predioJuicios;
    }

    public List<CatPredio> getPredios() {
        return predios;
    }

    public void setPredios(List<CatPredio> predios) {
        this.predios = predios;
    }

    public CatPredioPropietario getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredioPropietario propietario) {
        this.propietario = propietario;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<CatalogoItem> getMedidasCautelares() {
        return medidasCautelares;
    }

    public void setMedidasCautelares(List<CatalogoItem> medidasCautelares) {
        this.medidasCautelares = medidasCautelares;
    }

    public List<CatalogoItem> getMedidasCautelaresAplicadas() {
        return medidasCautelaresAplicadas;
    }

    public void setMedidasCautelaresAplicadas(List<CatalogoItem> medidasCautelaresAplicadas) {
        this.medidasCautelaresAplicadas = medidasCautelaresAplicadas;
    }

    public Long getDiferenciaDiasCuota() {
        return diferenciaDiasCuota;
    }

    public void setDiferenciaDiasCuota(Long diferenciaDiasCuota) {
        this.diferenciaDiasCuota = diferenciaDiasCuota;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public List<FnSolicitudExoneracionPredios> getListaExoneraciones() {
        return listaExoneraciones;
    }

    public void setListaExoneraciones(List<FnSolicitudExoneracionPredios> listaExoneraciones) {
        this.listaExoneraciones = listaExoneraciones;
    }

    public Boolean getEmbargo() {
        return embargo;
    }

    public void setEmbargo(Boolean embargo) {
        this.embargo = embargo;
    }

//</editor-fold>
}
