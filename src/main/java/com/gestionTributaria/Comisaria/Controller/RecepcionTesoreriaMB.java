package com.gestionTributaria.Comisaria.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Controller.RecaudacionGeneralMB;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.NotaDetalle;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.InteresesService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.NotaCreditoServices;
import com.gestionTributaria.Services.NotaDetalleService;
import com.gestionTributaria.Services.RemisionInteresService;
import com.gestionTributaria.Services.TipoLiquidacionService;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.PagoTituloReporteModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.ventanilla.Services.VentanillaTramiteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "recepcionTesoreriaMB")
@ViewScoped
public class RecepcionTesoreriaMB extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecaudacionGeneralMB.class.getName());
    @Inject
    private InteresesService interesService;
    @Inject
    private RemisionInteresService remisionInteres;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private FinaRenPagoService renPagoService;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private TipoLiquidacionService tipoLiquidacionService;
    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private KatalinaService correoServices;
    @Inject
    private ServletSession ss;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private VentanillaTramiteService ventanillaTramiteService;
    @Inject
    private FinaRenPagoService pagoService;
    @Inject
    private ManagerService manager;
    @Inject
    private NotaCreditoServices notaCreditoService;
    @Inject
    private NotaDetalleService notaDetalleService;

    private LazyModel<FinaRenLiquidacion> lazyTitulosGenerados;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenLiquidacion> liquidaciones;
    private PagoTituloReporteModel modelPago;
    protected Boolean variosPagos;
    private Cajero cajero;
    private BigInteger numeroComprobante;
//    private List<FinaRenTipoLiquidacion> tipoLiquidacionList;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);

                variosPagos = Boolean.FALSE;
                modelPago = new PagoTituloReporteModel();
//                tipoLiquidacionList = tipoLiquidacionService.getLiquidacionesGenerales();
                cajero = cajeroService.findByCajero(session.getNameUser());
                this.titulosGenerales();
//                if (cajero == null) {
//                    JsfUtil.addWarningMessage("Advertencia", "El usuario responsable de la tarea debe ser cajero, revisar permisos.");
//                    JsfUtil.redirectFaces("/procesos/bandeja-tareas");
//                    System.out.println("no es cajero el usuario el managebeans es recepcionTesoreriaMB");
//                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

//    public void onChanngeTab(TabChangeEvent event) {
//        try {
//            if (event.getTab().getId().equals("tituloCredito")) {
//                if (lazyTitulosGenerados == null) {
//                    this.titulosGenerales();
//                }
//            }
//            if (event.getTab().getId().equals("convenios")) {
//
//            }
//            if (event.getTab().getId().equals("convenioTerminado")) {
//
//            }
//            if (event.getTab().getId().equals("tituloCreditoPagado")) {
////                if (this.lazyTitulosPagados == null) {
////                    this.lazyTitulosPagados = new LazyModel<>(FinaRenLiquidacion.class);
////                    this.lazyTitulosPagados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(1L)));
////                    this.lazyTitulosPagados.getSorteds().put("numComprobante", "desc");
////                    JsfUtil.update("mainForm:tvRecaudaciones:dataTitulosPagados");
////                }
//            }
//            if (event.getTab().getId().equals("tituloCreditoAnulado")) {
////                if (this.lazyTitulosAnulados == null) {
////                    this.lazyTitulosAnulados = new LazyModel<>(FinaRenLiquidacion.class);
////                    this.lazyTitulosAnulados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(4L)));
////                    this.lazyTitulosAnulados.getSorteds().put("fechaIngreso", "desc");
////                    JsfUtil.update("mainForm:tvRecaudaciones:dataTitulosAnulados");
////                }
//            }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//    }
    public void titulosGenerales() {
        liquidacion = new FinaRenLiquidacion();
        liquidaciones = new ArrayList<>();

        if (!tramite.getTipoTramite().getAbreviatura().equals("PVPU")) {
            lazyTitulosGenerados = new LazyModel<>(FinaRenLiquidacion.class);
            lazyTitulosGenerados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(2L)));
            lazyTitulosGenerados.getFilterss().put("tipoLiquidacion:notEqual", Arrays.asList(new FinaRenTipoLiquidacion(3L), new FinaRenTipoLiquidacion(2L)));
//        System.out.println("BigInteger.valueOf(tramite.getId()) " + BigInteger.valueOf(tramite.getId()));
            lazyTitulosGenerados.getFilterss().put("tramite", BigInteger.valueOf(tramite.getId()));
            lazyTitulosGenerados.getSorteds().put("fechaIngreso", "desc");
        } else {
            lazyTitulosGenerados = new LazyModel<>(FinaRenLiquidacion.class);
            lazyTitulosGenerados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(2L)));
            lazyTitulosGenerados.getFilterss().put("tipoLiquidacion:notEqual", Arrays.asList(new FinaRenTipoLiquidacion(3L), new FinaRenTipoLiquidacion(2L)));
//        System.out.println("BigInteger.valueOf(tramite.getId()) " + BigInteger.valueOf(tramite.getId()));
            lazyTitulosGenerados.getFilterss().put("numTramite", tramite.getNumTramite());
            lazyTitulosGenerados.getSorteds().put("fechaIngreso", "desc");
        }
    }

    public void generarComprobante() {
        ss.addParametro("COMPROBANTE", numeroComprobante.longValue());
        ss.setNombreSubCarpeta("GestionTributatia/comprobantes");
        ss.setNombreReporte("comprobante");
//        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/") + "reportes/GestionTributatia/comprobantes/");
        ss.setImprimir(Boolean.TRUE);
        System.out.println("parametros--->>" + ss.getParametros());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void procesarPago() {
//        this.pagoRealizado = Boolean.FALSE;
        if (this.liquidacion != null) {
            liquidacion.setInteres(this.interes(liquidacion));
            //PARA LOSS PERMISOS AMBIENTALES GENERAR INTERES
//            if (this.liquidacion.getTipoLiquidacion() != null) {
//                if (this.liquidacion.getTipoLiquidacion().getTransaccionPadre() != null) {
//                    this.liquidacion.setInteres(this.interes(liquidacion));
//                }
//            }
            liquidacion.calcularPago();
            modelPago = new PagoTituloReporteModel(this.liquidacion.getPagoFinal(), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
            modelPago.setValorCobrar(liquidacion.getPagoFinal());
//            System.out.println("saldo--> " + modelPago.getValorSaldoPagoFinal());
        }

    }

    public BigDecimal interes(FinaRenLiquidacion liq) {
        Boolean aplicaRemision = remisionInteres.aplicaRemision(liq);
        if (aplicaRemision) {
            liq.setInteres(BigDecimal.ZERO);
        } else {
            liq.setInteres(interesService.interesesCalculado(liq, new Date()));
        }
        return liq.getInteres();
    }

    public void pago() {
        try {
            if (this.liquidacion != null && this.modelPago != null && this.liquidacion.getTipoLiquidacion().getId().equals(181L) && this.liquidacion.getSaldo().compareTo(this.modelPago.getValorTotal()) > 0) {
                JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser igual a la recaudación");
                return;
            }
//            this.generarLiquidacion();
            this.realizarPago();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarLiquidacion() {
        if (liquidacion != null && liquidacion.getId() != null) {
            liquidacion.calcularPago();
        }
        liquidacion.setIpUserSession(session.getIpClient());
        liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        liquidacion.setUsuarioIngreso(session.getNameUser());
//        liquidacion.getTipoLiquidacion().setRenRubrosLiquidacionCollection(rubrosSeleccionado);
        liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
        liquidacion.setValidada(Boolean.TRUE);
        liquidacion = liquidacionService.grabarLiquidacionPagada(liquidacion);
        JsfUtil.addWarningMessage("Mensaje", "Liquidacion: " + liquidacion.getIdLiquidacion() + " generada con exito");
    }

    public void realizarPago() {
        FinaRenPago p;
        FinaRenPago pagoCoactiva = null;
        if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
            liquidacion.calcularPago();
            try {
                FinaRenLiquidacion temp = null;
                if (liquidacion.getEstadoCoactiva() != null && liquidacion.getEstadoCoactiva() == 2) {
                    System.out.println("Tienen Coactiva..Falta desarrollar :v");
                }
                this.numeroComprobante = new BigInteger(manager.getNumComprobante().toString());

                liquidacion.calcularPago();
                if (liquidacion.getEstadoCoactiva() != null && liquidacion.getEstadoCoactiva() == 2) {
                    if (liquidacion.getValorCoactiva().doubleValue() == 0) {
                        liquidacion.calcularPago();
                    }
                }
                p = pagoService.realizarPago(liquidacion, getModelPago().realizarPago(liquidacion, this.numeroComprobante), cajero, true);
               
                //iniciar el tramite de la ventanilla.
                ventanillaTramiteService.iniciarTramiteVentanilla((p.getLiquidacion() != null
                        && !Utils.isEmptyString(p.getLiquidacion().getIdLiquidacion()))
                        ? p.getLiquidacion().getIdLiquidacion() : "");
                recaudacionService.emitirFactura(p);

                if (Utils.isNotEmpty(modelPago.getNotaCreditoMov())) {
                    for (NotasCredito nc : modelPago.getNotaCreditoMov()) {
                        NotaDetalle nd = new NotaDetalle();
                        nd.setIdNota(nc);
                        nd.setFechaIngreso(new Date());
                        nd.setFechaPago(new Date());
                        nd.setUsuarioCreacion(cajero.getUsuario());
                        nd.setValor(nc.getValor().subtract(nc.getSaldo()));
                        nd.setComprobante(numeroComprobante.toString());
                        notaCreditoService.edit(nc);
                        notaDetalleService.edit(nd);
                    }
                }

                JsfUtil.addSuccessMessage("Información", "Cobro conExito...");

                this.generarComprobante();

                modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
                inicializar();

                JsfUtil.update("mainForm");
            } catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
            }
        } else {
            JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));

            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void opendlg() {
        if (Utils.isNotEmpty(liquidacionService.liquidacionByTramite(BigInteger.valueOf(tramite.getId()), new FinaRenEstadoLiquidacion(2L)))) {
            JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Las liquidaciones generadas en este proceso deben ser pagadas.");
            return;
        }
//        System.out.println("quiero ver el estado de esta wea " + Utils.isNotEmpty(liquidacionService.liquidacionByTramite(BigInteger.valueOf(tramite.getId()), new FinaRenEstadoLiquidacion(2L))));
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            this.enviarCorreo();
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public LazyModel<FinaRenLiquidacion> getLazyTitulosGenerados() {
        return lazyTitulosGenerados;
    }

    public void setLazyTitulosGenerados(LazyModel<FinaRenLiquidacion> lazyTitulosGenerados) {
        this.lazyTitulosGenerados = lazyTitulosGenerados;
    }

    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public PagoTituloReporteModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoTituloReporteModel modelPago) {
        this.modelPago = modelPago;
    }
//</editor-fold>

}
