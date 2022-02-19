package com.origami.sigef.tesoreria.comprobantelectronico.service.ws;

import com.google.gson.Gson;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.service.MsgFormatoNotificacionService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.interfaces.AsynchronousService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Variables;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Ambiente;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.ClaveAcceso;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Entidad;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.PorcentajeImpuesto;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.TipoEmision;
import com.origami.sigef.tesoreria.comprobantelectronico.service.AmbienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ClaveAccesoService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ComprobanteSRIService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ComprobanteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.DirectorioService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.EntidadService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.FormaPagoService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ImpuestosAsignadosRetencionService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.PorcentajeImpuestoService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.RespuestaComprobanteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.RespuestaSolicitudService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.TipoEmisionService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.TipoIdentificacionService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteDetalleSRI;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobantePagoSRI;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteSRI;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Detalle;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetallePago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.FirmaDocElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.FirmaElectronica;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.Calculos;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.InfoTributaria;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.factura.Factura;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notacredito.NotaCredito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notacredito.TotalConImpuestos;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notadebito.NotaDebito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.retencion.ComprobanteRetencion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.RespuestaSolicitud;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.Constantes;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.DocumentosUtil;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import com.origami.sigef.tesoreria.service.RubroService;
import com.origami.sigef.tesoreria.service.RubroTipoService;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;

//@Path(value = "comprobanteElectronico")
//@Produces(value = {MediaType.APPLICATION_JSON})
@Stateless(name = "ComprobanteElectronicaService")
@javax.enterprise.context.Dependent
//@ApplicationScoped
//@Stateless @javax.enterprise.context.Dependent
public class ComprobanteElectronicosWS implements ComprobanteElectronicaService {

    private static final Logger LOG = Logger.getLogger(ComprobanteElectronicosWS.class.getName());

    @EJB
    //@Inject
    private AsynchronousService asynchronousService;
    @EJB
    //@Inject
    private MsgFormatoNotificacionService msgFormatoNotificacionService;
    @EJB
    //@Inject
    private DirectorioService directorioService;
    @EJB
    //@Inject
    private RespuestaSolicitudService respuestaSolicitudService;
    @EJB
    //@Inject
    private RespuestaComprobanteService respuestaComprobanteService;
    @EJB
    //@Inject
    private FormaPagoService formaPagoService;
    @EJB
    //@Inject
    private ImpuestosAsignadosRetencionService impuestosAsignadosRetencionService;
    @EJB
    //@Inject
    private ComprobanteSRIService comprobanteSRIService;
    @EJB
    //@Inject
    private TipoIdentificacionService tipoIdentificacionService;
    @EJB
    //@Inject
    private ClaveAccesoService claveAccesoService;
    @EJB
    //@Inject
    private PorcentajeImpuestoService porcentajeImpuestoService;
    @EJB
    //@Inject
    private CajeroService cajeroService;
    @EJB
    //@Inject
    private EntidadService entidadService;
    @EJB
    //@Inject
    private ComprobanteService comprobanteService;
    @EJB
    //@Inject
    private AmbienteService ambienteService;
    @EJB
    //@Inject
    private TipoEmisionService tipoEmisionService;
    @EJB
    //@Inject
    private SeqGenMan seqGenManService;
    @EJB
    //@Inject
    private CatalogoItemService catalogoItemService;
    @EJB
    private RubroService rubroService;
    @EJB
    private RubroTipoService rubroTipoService;
    @EJB
    private RenFacturaService renFacturaService;

    private RenFactura liquidacion;

    private ComprobanteSRI comprobanteSRI = null;

    private String archivoACrear, claveAcceso, secuencial;

    private Boolean continuar = Boolean.TRUE;

    private Gson gson;


    /*   public List<ComprobanteSRI> consultarComprobanteContribuyentes( String identificacion) {
        return comprobanteSRIRepository
                .findByContribuyente_IdemtificacionAndNumAutorizacionIsNotNullOrderByFechaAutorizacionDesc(
                        identificacion);
    }*/
    @Override
    public void enviarCorreoFacturaElectronicaSRI(ComprobanteSRI sri) {
        asynchronousService.sendMailFactura(msgFormatoNotificacionService.findByMsgFormatoNotificacionTipo(Variables.correoFactura),
                sri.getXmlPath(), sri.getPdfPath(),
                sri.getContribuyente().getEmail(), sri.getId());
    }

    //@POST
    //@Path(value = "/facturaElectronica")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Override
    @Asynchronous
    public void enviarFacturaElectronicaSRI(ComprobanteElectronico comprobanteElectronico) {
        System.out.println("comprobante>>"+comprobanteElectronico.toString());
        FirmaDocElectronico firmaDocElectronico = validarComprobanteRest(comprobanteElectronico);
        if (firmaDocElectronico != null) {
            Factura factura = createXML(firmaDocElectronico, comprobanteElectronico);
            if (factura != null) {
                archivoACrear = directorioService.findByCodigo(1).getRuta()
                        + factura.getInfoTributaria().getClaveAcceso() + ".xml";
                if (DocumentosUtil.crearArchivo(factura, archivoACrear)) {
                    claveAcceso = factura.getInfoTributaria().getClaveAcceso();
                    secuencial = factura.getInfoTributaria().getSecuencial();
                    liquidacion = renFacturaService.find(comprobanteElectronico.getIdLiquidacion());
                    enviarSRIAutoriacion(firmaDocElectronico, comprobanteElectronico);
                }
            }
        } else {
            JsfUtil.addWarningMessage("Error", "Validación de Firma...");
        }
    }

    @Override
    @Asynchronous
    public void reenviarFacturaElectronicaSRI(ComprobanteElectronico comprobanteElectronico) {
        FirmaDocElectronico firmaDocElectronico = validarComprobanteRest(comprobanteElectronico);
        if (firmaDocElectronico != null) {
            Factura factura = createXML(firmaDocElectronico, comprobanteElectronico);
            if (factura != null) {
                archivoACrear = directorioService.findByCodigo(1).getRuta()
                        + factura.getInfoTributaria().getClaveAcceso() + ".xml";
                if (DocumentosUtil.crearArchivo(factura, archivoACrear)) {
                    claveAcceso = factura.getInfoTributaria().getClaveAcceso();
                    secuencial = factura.getInfoTributaria().getSecuencial();
                    enviarSRIAutoriacion(firmaDocElectronico, comprobanteElectronico);
                }
            }
        }
    }

    @Override
    @Asynchronous
    public void enviarNotaCreditoSRI(ComprobanteElectronico comprobanteElectronico) {
        FirmaDocElectronico firmaDocElectronico = validarComprobanteRest(comprobanteElectronico);
//        System.out.println("firma electronica--> " + firmaDocElectronico);
        if (firmaDocElectronico != null) {
            liquidacion = renFacturaService.find(comprobanteElectronico.getIdLiquidacion());
            NotaCredito notaCredito = createXMLNotaCredito(firmaDocElectronico, comprobanteElectronico);
            this.archivoACrear = directorioService.findByCodigo(1).getRuta()
                    + notaCredito.getInfoTributaria().getClaveAcceso() + ".xml";
            if (DocumentosUtil.crearArchivo(notaCredito, archivoACrear)) {
                claveAcceso = notaCredito.getInfoTributaria().getClaveAcceso();
                secuencial = notaCredito.getInfoTributaria().getSecuencial();
                enviarSRIAutoriacion(firmaDocElectronico, comprobanteElectronico);
            }
        }
    }

    @Override
    @Asynchronous
    public void enviarNotaDebitoSRI(ComprobanteElectronico comprobanteElectronico) {
        FirmaDocElectronico firmaDocElectronico = validarComprobanteRest(comprobanteElectronico);
        if (firmaDocElectronico != null) {
            liquidacion = renFacturaService.find(comprobanteElectronico.getIdLiquidacion());
            NotaDebito notaDebito = crearXMLNotaDebito(firmaDocElectronico, comprobanteElectronico);
            this.archivoACrear = directorioService.findByCodigo(1).getRuta()
                    + notaDebito.getInfoTributaria().getClaveAcceso() + ".xml";
            if (DocumentosUtil.crearArchivo(notaDebito, archivoACrear)) {
                claveAcceso = notaDebito.getInfoTributaria().getClaveAcceso();
                secuencial = notaDebito.getInfoTributaria().getSecuencial();
                enviarSRIAutoriacion(firmaDocElectronico, comprobanteElectronico);
                //        return new ResponseEntity<>(this.comprobanteSRI, HttpStatus.OK);
            }
        }
        //  return null;
    }

    @Override
    @Asynchronous
    public void enviarComprobanteRentencionSRI(ComprobanteElectronico comprobanteElectronico) {
        FirmaDocElectronico firmaDocElectronico = validarComprobanteRest(comprobanteElectronico);
        if (firmaDocElectronico != null) {
            System.out.println("enviarComprobanteRentencionSRI: true");
            liquidacion = renFacturaService.find(comprobanteElectronico.getIdLiquidacion());
            ComprobanteRetencion comprobanteRetencion = crearXMLComprobanteRetencion(firmaDocElectronico,
                    comprobanteElectronico);
            this.archivoACrear = directorioService.findByCodigo(1).getRuta()
                    + comprobanteRetencion.getInfoTributaria().getClaveAcceso() + ".xml";
            if (DocumentosUtil.crearArchivo(comprobanteRetencion, archivoACrear)) {
                claveAcceso = comprobanteRetencion.getInfoTributaria().getClaveAcceso();
                secuencial = comprobanteRetencion.getInfoTributaria().getSecuencial();
                enviarSRIAutoriacion(firmaDocElectronico, comprobanteElectronico);
            }
        } else {
            System.out.println("enviarComprobanteRentencionSRI: false");
        }
    }

    private FirmaDocElectronico validarComprobanteRest(ComprobanteElectronico comprobanteElectronico) {
        if (Calculos.validarCamposComprobanteElectronico(comprobanteElectronico)) {
            FirmaDocElectronico firmaDocElectronico = getFirmaDocElectronico(comprobanteElectronico);
            if (firmaDocElectronico != null) {
                if (DocumentosUtil.validarPasswordCertificado(firmaDocElectronico)) {
                    return firmaDocElectronico;
                }
            }
        } else {
            System.out.println("validarCamposComprobanteElectronico: false");
        }
        return null;
    }

    private FirmaDocElectronico getFirmaDocElectronico(ComprobanteElectronico comprobanteElectronico) {
        Cajero cajero = cajeroService.findByPuntoEmision(comprobanteElectronico.getPuntoEmision());
        Entidad entidad = entidadService.findByRuc(comprobanteElectronico.getRucEntidad());
        Comprobantes cElecotronico
                = comprobanteService.findByCodigo(comprobanteElectronico.getComprobanteCodigo());
        Ambiente ambiente = ambienteService.findByCodigo(comprobanteElectronico.getAmbiente());
        Cajero c = cajeroService.findByCajero(cajero.getUsuario());
        FirmaElectronica firmaElectronica = new FirmaElectronica(c, c.getArchivo(), c.getClave(), "A");
        TipoEmision tipoEmision = tipoEmisionService.findByOnline(Boolean.FALSE);
        tipoEmision.setEsOnline(comprobanteElectronico.getIsOnline());
        return new FirmaDocElectronico(firmaElectronica, entidad,
                cElecotronico, tipoEmision, ambiente, cajero);
    }

    void enviarSRIAutoriacion(FirmaDocElectronico firmaDocElectronico,
            com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        continuar = Boolean.TRUE;
        RespuestaSolicitud rs = getRespuestaSolicitud(firmaDocElectronico);
        RespuestaComprobante rc = null;

        if (rs != null && rs.getEstado() != null) {
            if (rs.getEstado().equals(Variables.RECIBIDA)) {
                rc = getRespuestaComprobante(firmaDocElectronico);
                if (comprobanteElectronico.getReenvioVerificacion()) {
                    updateReenvioLiquidacion("REENVIADA");
                }
            } else if (rs.getEstado().equals(Variables.SIN_CONEXION)) {
                continuar = Boolean.FALSE;
                updateReenvioLiquidacion(Variables.SIN_CONEXION);
            } else {
                if (rs != null && rs.getCodigoError() != null) {
                    if (rs.getCodigoError().equals(Variables.CODE_CLAVE_ACCESO_REGISTRADA)) {
                        if (!comprobanteElectronico.getReenvioVerificacion()) {
                            rs.setEstado("RECIBIDA");
                            rc = getRespuestaComprobante(firmaDocElectronico);
                            continuar = Boolean.TRUE;
                        } else {
                            continuar = Boolean.FALSE;
                            updateReenvioLiquidacion("REVISADA");
                        }
                    }
                }
            }
        }
        if (continuar) {
            facturaSRIRespuest(firmaDocElectronico, rs, rc, comprobanteElectronico);
        }
    }

    private RespuestaSolicitud getRespuestaSolicitud(FirmaDocElectronico firmaDocElectronico) {
        gson = new Gson();

        try {
            RespuestaSolicitud rsRespuesta = DocumentosUtil.firmarXMLRecepcion(
                    directorioService.findByCodigo(-1).getRuta(), archivoACrear,
                    directorioService.findByCodigo(2).getRuta(), firmaDocElectronico, claveAcceso,
                    firmaDocElectronico.getTipoEmision().getEsOnline());
            if (rsRespuesta != null) {
                liquidacion.setClaveAcceso(claveAcceso);
                renFacturaService.edit(liquidacion);
                rsRespuesta.setResponse(gson.toJson(rsRespuesta));
                rsRespuesta.setFechaIngreso(new Date());
                rsRespuesta.setTramite(liquidacion.getId());
                respuestaSolicitudService.create(rsRespuesta);
            }
            return rsRespuesta;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERROR en getRespuestaSolicitud", e);
            return null;
        }
    }

    private RespuestaComprobante getRespuestaComprobante(FirmaDocElectronico firmaDocElectronico) {
        gson = new Gson();
        RespuestaComprobante respuestaComprobante = DocumentosUtil.autorizacionXMLSRI(claveAcceso,
                directorioService.findByCodigo(3).getRuta(),
                directorioService.findByCodigo(6).getRuta(),
                firmaDocElectronico.getAmbiente().getWsUrlAutorizacion(),
                firmaDocElectronico.getTipoEmision().getEsOnline());
        System.out.println("respuestaComprobante " + respuestaComprobante);
        if (respuestaComprobante != null) {
            respuestaComprobante.setResponse(gson.toJson(respuestaComprobante));
            respuestaComprobante.setFechaIngreso(new Date());
            respuestaComprobanteService.create(respuestaComprobante);
        }
        return respuestaComprobante;

    }

    private void facturaSRIRespuest(FirmaDocElectronico firmaDocElectronico, RespuestaSolicitud respuestaSolicitud,
            RespuestaComprobante respuestaComprobante, com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        this.comprobanteSRI = new ComprobanteSRI();
        this.comprobanteSRI
                .setTipoComprobante(firmaDocElectronico.getComprobantes().getDescripcion());
        this.comprobanteSRI
                .setCodigoTipoComprobante(firmaDocElectronico.getComprobantes().getCodigo());
        if (respuestaSolicitud != null) {
            if (respuestaSolicitud.getEstado() != null
                    && !respuestaSolicitud.getEstado().equals(Variables.SIN_CONEXION)) {
                this.comprobanteSRI
                        .setRespuestaSolicitudSRI(DocumentosUtil.loadValuesRespuestaSolicitudSRI(respuestaSolicitud));
            }
        }

        if (respuestaComprobante != null) {
            this.comprobanteSRI.setRespuestaAutorizacionSRI(
                    DocumentosUtil.loadValuesRespuestaComprobanteSRI(respuestaComprobante));
        }

        // SETEAR OS DATOS DE LA ENTIDAD QUE EMITE LA FACTURA
        this.comprobanteSRI.getEntidad()
                .setIdentificacion(firmaDocElectronico.getEntidad().getRucEntidad());
        this.comprobanteSRI.getEntidad()
                .setNombres(firmaDocElectronico.getEntidad().getNombreEntidad());
        this.comprobanteSRI.getEntidad()
                .setNombreComercial(firmaDocElectronico.getEntidad().getNombreComercial());
        this.comprobanteSRI.getEntidad()
                .setObligadoContabilidad(firmaDocElectronico.getEntidad().getContabilidad());
        this.comprobanteSRI.getEntidad().setContribuyenteEspecial(
                firmaDocElectronico.getEntidad().getContribuyenteEspecial());
        this.comprobanteSRI.getEntidad()
                .setDireccionMatriz(firmaDocElectronico.getEntidad().getDireccion());
        this.comprobanteSRI.getEntidad()
                .setDireccionSucursal(firmaDocElectronico.getEntidad().getDireccionSucursal());
        this.comprobanteSRI.getEntidad().setLogo(firmaDocElectronico.getEntidad().getLogo());

        /// SET DATOS DE LA PERSONA QUE COMPRA
        this.comprobanteSRI.getContribuyente().setIdentificacion(comprobanteElectronico.getCabecera().getCedulaRuc());
        this.comprobanteSRI.getContribuyente().setNombres(comprobanteElectronico.getCabecera().getPropietario());
        this.comprobanteSRI.getContribuyente().setDireccion(comprobanteElectronico.getCabecera().getDireccion());
        this.comprobanteSRI.getContribuyente().setTelefono(comprobanteElectronico.getCabecera().getTelefono());
        this.comprobanteSRI.getContribuyente().setEmail(comprobanteElectronico.getCabecera().getCorreo());
        //this.comprobanteSRI.setDescuentoAdicional(comprobanteElectronico.getDescuentoAdicional());

        // SET DATOS FACTURAS
        // PARA NO REPETIR CODIGO
        if (!comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            Map<String, Double> totalesFactura = Calculos.totalesFactura(comprobanteElectronico);
            this.comprobanteSRI.setSubTotalNoObjetoIva(new BigDecimal(totalesFactura.get("subTotalNoOnjetoIva")).setScale(2, BigDecimal.ROUND_HALF_UP));
            this.comprobanteSRI.setSubTotalSinImpuetos(new BigDecimal(totalesFactura.get("totalSinImpuestos")).setScale(2, BigDecimal.ROUND_HALF_UP));
            this.comprobanteSRI.setDescuento(new BigDecimal(totalesFactura.get("totalDescuento")).setScale(2, BigDecimal.ROUND_HALF_UP));
            this.comprobanteSRI.setIva(new BigDecimal(totalesFactura.get("totalIva")).setScale(2, BigDecimal.ROUND_HALF_UP));
            this.comprobanteSRI.setValorTotal(new BigDecimal(totalesFactura.get("importeTotalimporteTotal")).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)) {
                this.comprobanteSRI.setSubTotalNoObjetoIva(Calculos.totalSinImpuestoNotaDebito(comprobanteElectronico));
                //this.comprobanteSRI.setIva(comprobanteElectronico.getImpuestoNotaDebito().getValor());
                // this.comprobanteSRI
                //        .setValorTotal(this.comprobanteSRI.getSubTotalNoObjetoIva().add(this.comprobanteSRI.getIva
                //        ()));
            }
        }

        this.comprobanteSRI.setNumFactura(
                firmaDocElectronico.getCajero().getEntidad().getEstablecimiento() + firmaDocElectronico.getCajero().getPuntoEmision() + secuencial);

        this.comprobanteSRI.setNumFacturaFormato(firmaDocElectronico.getCajero().getEntidad().getEstablecimiento() + "-"
                + firmaDocElectronico.getCajero().getPuntoEmision() + "-" + secuencial);

        if (respuestaComprobante != null && !respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
            this.comprobanteSRI.setNumAutorizacion(
                    respuestaComprobante.getAutorizaciones().getAutorizacion().get(0).getNumeroAutorizacion());
            XMLGregorianCalendar fechaAutorizacion = respuestaComprobante.getAutorizaciones().getAutorizacion().get(0)
                    .getFechaAutorizacion();
            if (fechaAutorizacion != null) {
                this.comprobanteSRI.setFechaAutorizacion(
                        fechaAutorizacion.getYear() + "-" + String.format("%02d", fechaAutorizacion.getMonth()) + "-"
                        + fechaAutorizacion.getDay() + " " + fechaAutorizacion.getHour() + ":"
                        + fechaAutorizacion.getMinute() + ":" + fechaAutorizacion.getSecond());

                this.comprobanteSRI.setFechaAutorizacion(String.format("%02d", fechaAutorizacion.getDay()) + "-"
                        + String.format("%02d", fechaAutorizacion.getMonth()) + "-" + fechaAutorizacion.getYear() + " "
                        + String.format("%02d", fechaAutorizacion.getHour()) + ":"
                        + String.format("%02d", fechaAutorizacion.getMinute()) + ":"
                        + String.format("%02d", fechaAutorizacion.getSecond()));

            }
        }

        this.comprobanteSRI.setAmbiente(firmaDocElectronico.getAmbiente().getDescripcion());
        this.comprobanteSRI.setEmision(firmaDocElectronico.getTipoEmision().getDescripcion());
        this.comprobanteSRI.setClaveAcceso(claveAcceso);
        this.comprobanteSRI.setFechaEmision(comprobanteElectronico.getCabecera().getFechaEmision());

        // AL SER ENTIDA PUBLICA SE LE SETEA ZERO
        this.comprobanteSRI.setSubTotal12(BigDecimal.ZERO);
        this.comprobanteSRI.setSubTotalIva(BigDecimal.ZERO);
        this.comprobanteSRI.setSubTotalExcentoIva(BigDecimal.ZERO);
        this.comprobanteSRI.setIce(BigDecimal.ZERO);
        this.comprobanteSRI.setIrbpnr(BigDecimal.ZERO);
        this.comprobanteSRI.setPropina(BigDecimal.ZERO);
        this.comprobanteSRI.setPropina(BigDecimal.ZERO);
        this.comprobanteSRI.setValorSinSubSidio(BigDecimal.ZERO);
        this.comprobanteSRI.setAhorroPorSubSidio(BigDecimal.ZERO);

        // SET DATOS DETALLE FACTURA
        if (!comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            ComprobanteDetalleSRI comprobanteDetalleSRI = null;
            for (Detalle detalle : comprobanteElectronico.getDetalle()) {
                comprobanteDetalleSRI = new ComprobanteDetalleSRI();
                comprobanteDetalleSRI.setCodigoPrincipal(detalle.getCodigoPrincipal());
                comprobanteDetalleSRI.setCodigoAuxiliar(detalle.getCodigoAuxiliar());
                comprobanteDetalleSRI.setCantidad(detalle.getCantidad());
                comprobanteDetalleSRI.setDescripcion(detalle.getDescripcion());
                comprobanteDetalleSRI.setPrecioUnitario(new BigDecimal(detalle.getValorUnitario() + detalle.getRecargo()));
                comprobanteDetalleSRI.setSubsidio(BigDecimal.ZERO);
                comprobanteDetalleSRI.setPrecioSinSubsidio(BigDecimal.ZERO);
                comprobanteDetalleSRI.setDescuento(new BigDecimal(detalle.getDescuento()));
                comprobanteDetalleSRI.setPrecioTotal(new BigDecimal(detalle.getValorTotal()));
                comprobanteDetalleSRI.setRecargo(new BigDecimal(detalle.getRecargo()));
                comprobanteDetalleSRI.setInteres(new BigDecimal(detalle.getInteres()));
                comprobanteDetalleSRI.setCoactiva(new BigDecimal(detalle.getCoactiva()));
                comprobanteDetalleSRI.setAnio(detalle.getAnio().shortValue());
                this.comprobanteSRI.getDetalleFactura().add(comprobanteDetalleSRI);
            }
            comprobanteDetalleSRI = null;
        }

        /// SET DATOS PAGOS
        ComprobantePagoSRI comprobantePagoSRI = null;
        if (comprobanteElectronico.getDetallePagos() != null) {
            for (DetallePago detallePago : comprobanteElectronico.getDetallePagos()) {
                comprobantePagoSRI = new ComprobantePagoSRI();
                comprobantePagoSRI.setDescripcion(formaPagoService.findByCodigo(detallePago.getFormaPago()).getDescripcion());
                comprobantePagoSRI.setValor(new BigDecimal(detallePago.getTotal()).setScale(2, RoundingMode.HALF_UP));
                this.comprobanteSRI.getPagoDetalle().add(comprobantePagoSRI);
            }
            comprobantePagoSRI = null;
        }

        if (comprobanteElectronico.getRubroMunicipios() != null && !comprobanteElectronico.getRubroMunicipios().isEmpty()) {
//            System.out.println("////R MUC");
            this.comprobanteSRI.setRubrosMunicipio(comprobanteElectronico.getRubroMunicipios());
        }
        if (comprobanteElectronico.getRubroTercero() != null && !comprobanteElectronico.getRubroTercero().isEmpty()) {
//            System.out.println("////R TC");
            this.comprobanteSRI.setRubrosTercero(comprobanteElectronico.getRubroTercero());
        }

        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTACREDITO)
                || comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)) {
            this.comprobanteSRI.setTipoDocumentoModifica(comprobanteElectronico.getTipoDocumentoModifica());
            this.comprobanteSRI.setNumComprobanteModifica(comprobanteElectronico.getNumComprobanteModifica());
            this.comprobanteSRI.setMotivoNotaCredito(comprobanteElectronico.getMotivoNotaCredito());
            this.comprobanteSRI
                    .setFechaEmisionDocumentoModifica(comprobanteElectronico.getFechaEmisionDocumentoModifica());
            String compModif = "";
            if (comprobanteElectronico.getTipoDocumentoModifica().length() <= 1) {
                compModif = String.format("%02d", comprobanteElectronico.getTipoDocumentoModifica());
            } else {
                compModif = comprobanteElectronico.getTipoDocumentoModifica();
            }
            this.comprobanteSRI.setDescripcionComprobanteModifica(
                    getFirmaDocElectronico(comprobanteElectronico).getComprobantes().getDescripcion());

        }
        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)) {
            this.comprobanteSRI.setMotivosNotaDebito(comprobanteElectronico.getMotivosNotaDebito());
        }

        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {

            String mesPeriodoF = "";
            if (comprobanteElectronico.getMes().length() == 1) {
                mesPeriodoF = "0" + comprobanteElectronico.getMes();
            } else {
                mesPeriodoF = comprobanteElectronico.getMes();
            }
            this.comprobanteSRI.setPeriodo(mesPeriodoF + "/" + comprobanteElectronico.getAnio());

            BigDecimal totalRetenido = BigDecimal.ZERO, totalRetenidoAcum = BigDecimal.ZERO;
            this.comprobanteSRI.setImpuestoComprobanteRetencion(new ArrayList<>());
            for (ImpuestoComprobanteElectronico impuestoComprobanteElectronico : comprobanteElectronico
                    .getImpuestoComprobanteRetencion()) {
                //AQUI SE MODIFICO PARA EL DETALLE DEL REPORTE POR QUE SALIA QUEMADO EL COMPROBANTE_RETENCION 
                /* ESTO ESTABA DENTRO DE SETDESCRIPCIONMODIFICADO()
                getFirmaDocElectronico(comprobanteElectronico).getComprobantes().getDescripcion()
                 */
//                impuestoComprobanteElectronico.setDescripcionDocumentoModificado("");
                System.out.println("codigo comprobante>>>>"+impuestoComprobanteElectronico.getCodigo());
                impuestoComprobanteElectronico.setDescripcionImpuestoRetenido(impuestosAsignadosRetencionService
                        .findByCodigo(impuestoComprobanteElectronico.getCodigo()).getDescripcion());
                totalRetenido = impuestoComprobanteElectronico.getBaseImponible()
                        .multiply(impuestoComprobanteElectronico.getPorcentajeRetencion())
                        .setScale(2, RoundingMode.HALF_UP);
                totalRetenido = totalRetenido.divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                totalRetenidoAcum = totalRetenidoAcum.add(totalRetenido);
                impuestoComprobanteElectronico.setValor(totalRetenido);
                this.comprobanteSRI.getImpuestoComprobanteRetencion().add(impuestoComprobanteElectronico);
            }
            this.comprobanteSRI.setValorTotal(totalRetenidoAcum);
        }
        this.comprobanteSRI.setTramite(comprobanteElectronico.getTramite());
        /// SI EXISTE NUMERO DE AUTORIZACION
        if (this.comprobanteSRI.getNumAutorizacion() != null) {
            String pathAutorizados = directorioService.findByCodigo(3).getRuta();

            /// RNOMBRA EL ARCHIVO XML PORQUE ASI ESTA EN PORTOVIEJO =( </3
            String initNombreReporte = "", xmlPath;

            if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTACREDITO)) {
                initNombreReporte = "notacredito_";
            } else if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.FACTURA)) {
                initNombreReporte = "factura_";
            } else if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
                initNombreReporte = "retencion_";
            } else if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)) {
                initNombreReporte = "notadebito_";
            }

            xmlPath = pathAutorizados + initNombreReporte + this.comprobanteSRI.getNumFactura() + ".xml";

            renameFileXMLComprobante(pathAutorizados + claveAcceso + ".xml", xmlPath);
            this.comprobanteSRI.getEntidad().setLogo(firmaDocElectronico.getEntidad().getLogo());

            this.comprobanteSRI.setXmlPath(xmlPath);
            this.comprobanteSRI
                    .setPdfPath(pathAutorizados + initNombreReporte + this.comprobanteSRI.getNumFactura() + ".pdf");
            DocumentosUtil.generarPDFFacturacionElectronica(comprobanteSRI,
                    directorioService.findByCodigo(-2).getRuta());
            /// SEND MAILT O USER}
            //  System.out.println("send maiSystel not");
            if (comprobanteElectronico.getCabecera().getCorreo() != null
                    && comprobanteElectronico.getCabecera().getCorreo().length() > 0) {
                asynchronousService.sendMailFactura(msgFormatoNotificacionService.findByMsgFormatoNotificacionTipo(Variables.correoFactura),
                        this.comprobanteSRI.getXmlPath(), this.comprobanteSRI.getPdfPath(),
                        comprobanteElectronico.getCabecera().getCorreo(),
                        comprobanteElectronico.getIdLiquidacion());
            }

        }
        this.comprobanteSRI.setNumComprobante(comprobanteElectronico.getNumComprobante());
        updateAutorizacion();
        gson = new Gson();
        this.comprobanteSRI.setResponse(gson.toJson(comprobanteSRI));
        this.comprobanteSRI.setFecha(new Date());
        this.comprobanteSRI.setIdOrdenPago(comprobanteElectronico.getIdLiquidacion());
        comprobanteSRIService.create(this.comprobanteSRI);
    }

    private void renameFileXMLComprobante(String fileNameOld, String fileNameNew) {
        File fileOld = new File(fileNameOld);
        File fileNew = new File(fileNameNew);
        fileOld.renameTo(fileNew);
        if (fileOld.exists()) {
            fileOld.delete();
        }
    }

    private Factura createXML(FirmaDocElectronico firmaDocElectronico, com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        Factura factura = null;
        String secuencialComprobante = secuencialComprobante(comprobanteElectronico, firmaDocElectronico.getCajero());
        Factura.InfoFactura infoFactura = Calculos.loadInfoFactura(firmaDocElectronico, comprobanteElectronico);
        if (!comprobanteElectronico.getCabecera().getEsPasaporte()) {
            infoFactura.setTipoIdentificacionComprador(tipoIdentificacionService
                    .findByTamanio(comprobanteElectronico.getCabecera().getCedulaRuc().length()).getCodigo());
        } else {
            infoFactura.setTipoIdentificacionComprador("06");
        }
        infoFactura.setTotalConImpuestos(generaTotalesImpuesto(comprobanteElectronico));
        // GENERA EL DETALLE DE LA FACTURA
        Factura.Detalles detalles = generarDetalleFactura(comprobanteElectronico);
        Factura.InfoAdicional informacion = Calculos.generarInformacionAdicionalFactura(comprobanteElectronico);
        factura = new Factura();
        factura.setInfoTributaria(
                getInfoTributaria(secuencialComprobante, firmaDocElectronico, comprobanteElectronico));
        factura.setInfoFactura(infoFactura);
        if (detalles != null) {
            factura.setDetalles(detalles);
        }
        if (informacion.getCampoAdicional().size() > 0) {
            factura.setInfoAdicional(informacion);
        }
        factura.setVersion(Constantes.VERSION);
        factura.setId(Constantes.ID);
        return factura;

    }

    private NotaCredito createXMLNotaCredito(FirmaDocElectronico firmaDocElectronico,
            com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        NotaCredito notaCredito = new NotaCredito();

        String secuencialComprobante = secuencialComprobante(comprobanteElectronico, firmaDocElectronico.getCajero());
        //secuencialComprobante = comprobanteElectronico.getNumComprobanteModifica().substring(6);

        NotaCredito.Detalles detalles = generarDetalleNotaCredito(comprobanteElectronico);
        NotaCredito.InfoNotaCredito infoNotaCredito = Calculos.loadInfoNotaCredito(firmaDocElectronico,
                comprobanteElectronico);
        NotaCredito.InfoAdicional infoAdicional = Calculos
                .generarInformacionAdicionalNotaCredito(comprobanteElectronico);
        infoNotaCredito.setTipoIdentificacionComprador(tipoIdentificacionService
                .findByTamanio(comprobanteElectronico.getCabecera().getCedulaRuc().length()).getCodigo());
        infoNotaCredito.setTotalConImpuestos(generaTotalesImpuestoNotaCredito(comprobanteElectronico));
        notaCredito.setInfoNotaCredito(infoNotaCredito);
        notaCredito.setInfoTributaria(
                getInfoTributaria(secuencialComprobante, firmaDocElectronico, comprobanteElectronico));
        notaCredito.setInfoAdicional(infoAdicional);
        notaCredito.setDetalles(detalles);
        notaCredito.setVersion(Constantes.VERSION);
        notaCredito.setId(Constantes.ID);
        return notaCredito;
    }

    private NotaDebito crearXMLNotaDebito(FirmaDocElectronico firmaDocElectronico,
            ComprobanteElectronico comprobanteElectronico) {
        NotaDebito notaDebito = new NotaDebito();
        notaDebito.setVersion(Constantes.VERSION);
        notaDebito.setId(Constantes.ID);

        String sc = secuencialComprobante(comprobanteElectronico, firmaDocElectronico.getCajero());
        notaDebito.setInfoTributaria(
                getInfoTributaria(sc, firmaDocElectronico, comprobanteElectronico));

        NotaDebito.InfoNotaDebito infoNotaDebito = Calculos.loadInfoNotaDebito(firmaDocElectronico,
                comprobanteElectronico);
        infoNotaDebito.setTipoIdentificacionComprador(tipoIdentificacionService
                .findByTamanio(comprobanteElectronico.getCabecera().getCedulaRuc().length()).getCodigo());
        NotaDebito.InfoAdicional infoAdicional = Calculos.generarInformacionAdicionalNotaDebito(comprobanteElectronico);
        NotaDebito.InfoNotaDebito.Impuestos impuestos = new NotaDebito.InfoNotaDebito.Impuestos();
        for (ImpuestoComprobanteElectronico iNotaDebito : comprobanteElectronico.getImpuestosNotaDebitos()) {
            impuestos.getImpuesto().add(Calculos.impuestoNotaDebito(iNotaDebito));
        }

        infoNotaDebito.setImpuestos(impuestos);
        notaDebito.setMotivos(Calculos.obtenerMotivos(comprobanteElectronico));
        notaDebito.setInfoAdicional(infoAdicional);
        notaDebito.setInfoNotaDebito(infoNotaDebito);

        return notaDebito;
    }

    private ComprobanteRetencion crearXMLComprobanteRetencion(FirmaDocElectronico firmaDocElectronico,
            com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        ComprobanteRetencion comprobanteRetencion = new ComprobanteRetencion();
        comprobanteRetencion.setVersion(Constantes.VERSION);
        comprobanteRetencion.setId(Constantes.ID);
        System.out.println("crearXMLComprobanteRetencion");
        String secuencialComprobante = secuencialComprobante(comprobanteElectronico, firmaDocElectronico.getCajero());

        ComprobanteRetencion.InfoCompRetencion infoCompRetencion = Calculos.infoCompRetencion(firmaDocElectronico,
                comprobanteElectronico);
        infoCompRetencion.setTipoIdentificacionSujetoRetenido(tipoIdentificacionService
                .findByTamanio(comprobanteElectronico.getCabecera().getCedulaRuc().length()).getCodigo());
        System.out.println("infoCompRetencion.setIdentificacionSujetoRetenido "
                + infoCompRetencion.getIdentificacionSujetoRetenido());
        comprobanteRetencion.setInfoTributaria(
                getInfoTributaria(secuencialComprobante, firmaDocElectronico, comprobanteElectronico));
        comprobanteRetencion.setInfoCompRetencion(infoCompRetencion);
        comprobanteRetencion.setImpuestos(Calculos.obtenerImpuestoComprobanteRetencion(comprobanteElectronico));
        comprobanteRetencion
                .setInfoAdicional(Calculos.generarInformacionAdicionalCompobanteRetencion(comprobanteElectronico));
        return comprobanteRetencion;
    }

    private String secuencialComprobante(ComprobanteElectronico comprobanteElectronico, Cajero cajero) {
        if (comprobanteElectronico.getNumComprobante() == null) {
            switch (comprobanteElectronico.getComprobanteCodigo()) {
                case "01": //FACTURA
                    comprobanteElectronico.setNumComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaFacturas()));
                    break;
                case "04": //NOTA CREDITO
                    comprobanteElectronico.setNumComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaNotaCredito()));
                    break;
                case "05": //NOTA DEBITO
//                    System.out.println("cajero: " + cajero.getVariableSecuenciaNotaDebito());
                    comprobanteElectronico.setNumComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaNotaDebito()));
                    break;
                case "06": //GUIA REMISION
                    comprobanteElectronico.setNumComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaGuiaRemision()));
                    break;
                case "07": //COMPROBANTE RETENCION
                    comprobanteElectronico.setNumComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaRetencion()));
                    break;

            }
        }
        return String.format("%09d", comprobanteElectronico.getNumComprobante());
    }

    private InfoTributaria getInfoTributaria(String secuencialComprobante, FirmaDocElectronico firmaDocElectronico,
            com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        InfoTributaria infoTributaria = Calculos.loadInfoTributaria(secuencialComprobante, firmaDocElectronico);
        if (comprobanteElectronico.getClaveAcceso() != null) {
            infoTributaria.setClaveAcceso(comprobanteElectronico.getClaveAcceso());
        } else {
            infoTributaria
                    .setClaveAcceso(claveAcceso(firmaDocElectronico, comprobanteElectronico, secuencialComprobante));
            if (infoTributaria.getClaveAcceso() == null) {
                return null;
            }
        }
        return infoTributaria;
    }

    private String claveAcceso(FirmaDocElectronico firmaDocElectronico, com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico,
            String secuencialComprobante) {
        gson = new Gson();
        String claveAccesoTemp = DocumentosUtil.generarClaveAcceso(firmaDocElectronico, comprobanteElectronico,
                secuencialComprobante);
//        System.out.println("claveAccesoTemp: " + claveAccesoTemp);
        if (claveAccesoTemp != null) {
            ClaveAcceso byClaveAcceso = claveAccesoService
                    .findByClaveAcceso(claveAccesoTemp);
            do {
                if (byClaveAcceso == null) {
                    byClaveAcceso = new ClaveAcceso();
                    byClaveAcceso.setDataModel(gson.toJson(comprobanteElectronico));
                    byClaveAcceso.setClaveAcceso(claveAccesoTemp);
                    byClaveAcceso.setEstado(Constantes.ESTADO_ACTIVO);
                    byClaveAcceso.setFecha(new Date());
                    claveAccesoService.create(byClaveAcceso);
                    /// break;
                } else {
                    claveAcceso = DocumentosUtil.generarClaveAcceso(firmaDocElectronico, comprobanteElectronico,
                            secuencialComprobante);
                    byClaveAcceso = claveAccesoService.findByClaveAcceso(claveAccesoTemp);
                }
            } while (byClaveAcceso == null);
            return byClaveAcceso.getClaveAcceso();
        } else {
            return null;
        }
    }

    private Factura.Detalles generarDetalleFactura(com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        Factura.Detalles detalles = new Factura.Detalles();
        Factura.Detalles.Detalle detalle;
        /*
         * Marca Chevrolet = cHEVROLET - Modelo = 2012 etc --detalles
         * adicionalesFactura.Detalles.Detalle.DetallesAdicionales detallesAdicionales =
         * new Factura.Detalles.Detalle.DetallesAdicionales();Factura.Detalles.Detalle.
         * DetallesAdicionales.DetAdicional detAdicional;
         */

        for (Detalle d : comprobanteElectronico.getDetalle()) {
            detalle = new Factura.Detalles.Detalle();
            detalle.setCantidad(new BigDecimal(d.getCantidad()));
            if (d.getCodigoAuxiliar() != null) {
                detalle.setCodigoAuxiliar(d.getCodigoAuxiliar());
            }
            if (d.getCodigoPrincipal() != null) {
                detalle.setCodigoPrincipal(d.getCodigoPrincipal());
            }
            if (d.getDescripcion() != null) {
                detalle.setDescripcion(d.getDescripcion());
            }

            detalle.setDescuento(new BigDecimal(d.getDescuento()).setScale(2, RoundingMode.HALF_UP));

            if (comprobanteElectronico.getTipoLiquidacion() != null && comprobanteElectronico.getTipoLiquidacion().equals("TC")) {
                detalle.setPrecioTotalSinImpuesto(
                        new BigDecimal(d.getValorTotal()).setScale(2, RoundingMode.HALF_UP));
//                System.out.println("detalle.getPrecioTotalSinImpuesto(): " + detalle.getPrecioTotalSinImpuesto());
                detalle.setPrecioUnitario(new BigDecimal(d.getValorUnitario()).setScale(2,
                        RoundingMode.HALF_UP));
            } else {
                detalle.setPrecioTotalSinImpuesto(
                        new BigDecimal(((d.getValorUnitario() + d.getRecargo() + d.getCoactiva() + d.getInteres())) - d.getDescuento()).setScale(2, RoundingMode.HALF_UP));
                detalle.setPrecioUnitario(new BigDecimal((d.getValorUnitario() + d.getRecargo() + d.getCoactiva() + d.getInteres())
                        - d.getDescuento()).setScale(2, RoundingMode.HALF_UP));
            }
//            System.out.println("valor sin imp>>"+detalle.getPrecioTotalSinImpuesto());
//            System.out.println("valor Unitario>>"+detalle.getPrecioUnitario());
            detalle.setImpuestos(Calculos.obtenerImpuestosFactura(d,
                    porcentajeImpuestoService.findByCodigoAndValorPorcentaje(d.getCodigoTarifa(), d.getIva())));

            detalles.getDetalle().add(detalle);
        }
        return detalles;
    }

    private NotaCredito.Detalles generarDetalleNotaCredito(com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        NotaCredito.Detalles detalles = new NotaCredito.Detalles();
        NotaCredito.Detalles.Detalle detalle;
        /*
         * Marca Chevrolet = cHEVROLET - Modelo = 2012 etc --detalles
         * adicionalesFactura.Detalles.Detalle.DetallesAdicionales detallesAdicionales =
         * new Factura.Detalles.Detalle.DetallesAdicionales();Factura.Detalles.Detalle.
         * DetallesAdicionales.DetAdicional detAdicional;
         */

        for (Detalle d : comprobanteElectronico.getDetalle()) {
            detalle = new NotaCredito.Detalles.Detalle();
            detalle.setCantidad(new BigDecimal(d.getCantidad()));
            if (d.getCodigoAuxiliar() != null) {
                detalle.setCodigoAdicional(d.getCodigoAuxiliar());
            }
            if (d.getCodigoPrincipal() != null) {
                detalle.setCodigoInterno(d.getCodigoPrincipal());
            }
            if (d.getDescripcion() != null) {
                detalle.setDescripcion(d.getDescripcion());
            }
            detalle.setPrecioUnitario(new BigDecimal(d.getValorUnitario()).setScale(2, RoundingMode.HALF_UP));
            detalle.setDescuento(new BigDecimal(d.getDescuento()).setScale(2, RoundingMode.HALF_UP));
            detalle.setPrecioTotalSinImpuesto(
                    new BigDecimal(d.getValorTotal() - d.getDescuento()).setScale(2, RoundingMode.HALF_UP));
            detalle.setImpuestos(Calculos.obtenerImpuestosNotaCredito(d,
                    porcentajeImpuestoService.findByCodigoAndValorPorcentaje(d.getCodigoTarifa(), d.getIva())));
            detalles.getDetalle().add(detalle);
        }
        return detalles;
    }

    private Factura.InfoFactura.TotalConImpuestos generaTotalesImpuesto(com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {

        Boolean llevaIva = Boolean.FALSE;

        PorcentajeImpuesto porcentajeImpuesto;

        List<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto> totalImpuestoList = new ArrayList<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto>();

//        System.out.println("comprobanteElectronico.getDetalles().getDetalle().size "
//                + comprobanteElectronico.getDetalle().size());
        /// MODIFICAR PAARA CUANDO EL IVVA EXISTA O EXISTA ALGUN TIPO DE ICE
        for (Detalle d : comprobanteElectronico.getDetalle()) {
            if (!llevaIva) {
                if (d.getIva() > 0) {
                    llevaIva = Boolean.TRUE;
                }
            }
            porcentajeImpuesto = porcentajeImpuestoService.findByCodigoAndValorPorcentaje(d.getCodigoTarifa(), d.getIva());
            totalImpuestoList.add(Calculos.generaTotalImpuesto(d, porcentajeImpuesto));
        }

        String codigo = "";
        String codigoPorcentaje = "";
        BigDecimal tarifa = BigDecimal.ZERO;
        BigDecimal baseImponible = BigDecimal.ZERO;
        BigDecimal valor = BigDecimal.ZERO;

        Factura.InfoFactura.TotalConImpuestos totalConImpuestos = new Factura.InfoFactura.TotalConImpuestos();

        if (!llevaIva) {
            for (Factura.InfoFactura.TotalConImpuestos.TotalImpuesto t : totalImpuestoList) {
                codigo = t.getCodigo();
                codigoPorcentaje = t.getCodigoPorcentaje();
                baseImponible = baseImponible.add(t.getBaseImponible());
                valor = valor.add(t.getValor());
                tarifa = tarifa.add(t.getTarifa());
            }
            Factura.InfoFactura.TotalConImpuestos.TotalImpuesto t = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
            t.setBaseImponible(baseImponible);
            t.setValor(valor);
            t.setTarifa(tarifa);
            t.setCodigo(codigo);
            t.setCodigoPorcentaje(codigoPorcentaje);

            totalImpuestoList.clear();
            // totalImpuestoList.add(t);

            totalConImpuestos.getTotalImpuesto().add(t);
        } else {
            totalConImpuestos.getTotalImpuesto().addAll(totalImpuestoList);
        }

//        System.out.println("size " + totalConImpuestos.getTotalImpuesto().size());
        return totalConImpuestos;
    }

    private TotalConImpuestos generaTotalesImpuestoNotaCredito(com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico comprobanteElectronico) {
        PorcentajeImpuesto porcentajeImpuesto;
        TotalConImpuestos totalConImpuestos = new TotalConImpuestos();

        List<TotalConImpuestos.TotalImpuesto> totalImpuestoList = new ArrayList();

        String codigo = "";
        String codigoPorcentaje = "";

        BigDecimal baseImponible = BigDecimal.ZERO;
        BigDecimal valor = BigDecimal.ZERO;

        /// MODIFICAR PAARA CUANDO EL IVVA EXISTA O EXISTA ALGUN TIPO DE ICE
        for (Detalle d : comprobanteElectronico.getDetalle()) {
            porcentajeImpuesto = porcentajeImpuestoService.findByCodigoAndValorPorcentaje(d.getCodigoTarifa(), d.getIva());
            totalImpuestoList.add(Calculos.generaTotalImpuestoNotaCredito(d, porcentajeImpuesto));
            // totalConImpuestos.getTotalImpuesto().add(Calculos.generaTotalImpuestoNotaCredito(d,
            // porcentajes));
        }

        for (TotalConImpuestos.TotalImpuesto t : totalImpuestoList) {
            codigo = t.getCodigo();
            codigoPorcentaje = t.getCodigoPorcentaje();
            baseImponible = baseImponible.add(t.getBaseImponible());
            valor = valor.add(t.getValor());
        }
        TotalConImpuestos.TotalImpuesto t = new TotalConImpuestos.TotalImpuesto();
        t.setBaseImponible(baseImponible);
        t.setValor(valor);
        t.setCodigo(codigo);
        t.setCodigoPorcentaje(codigoPorcentaje);

        totalImpuestoList.clear();

        totalConImpuestos.getTotalImpuesto().add(t);

        return totalConImpuestos;
    }

    public Rubro crearRubro(Detalle dt, String tipo) {
        Rubro rubro = new Rubro();
        rubro.setDescripcion(dt.getDescripcion());
        rubro.setCodigo(dt.getCodigoPrincipal());
        rubro.setTipoImpuesto(dt.getCodigoPrincipal() + tipo);
        if (tipo.equals("-TT")) {
            rubro.setRubroTipo(rubroTipoService.getRubroTipoCodigo("TASAS"));
        } else {
            rubro.setRubroTipo(rubroTipoService.getRubroTipoCodigo("PROD"));
        }
        rubro.setEstado(Boolean.FALSE);
        rubro.setValor(dt.getValorUnitario());
        rubro.setPorcentualSbu(Boolean.FALSE);
        rubro.setPorcentaje(BigDecimal.ZERO);
        rubro.setPredeterminado(Boolean.FALSE);
        rubro.setVigente(true);
        rubro.setPorcentajeRetencion(BigDecimal.ZERO);
        rubro.setPorcentajeLibre(Boolean.FALSE);
        rubro = rubroService.create(rubro);
        return rubro;
    }

    private Map<String, String> getDatos(String valor) {
        Map<String, String> map = new HashMap<>();
        String apellidos = "";
        String nombres = "";
        String[] datos = valor.split(" ");
        for (int i = 0; i < datos.length; i++) {
            switch (i) {
                case 0:
                case 1:
                    apellidos = apellidos + datos[i] + " ";
                    break;
                default:
                    nombres = nombres + datos[i] + " ";
            }
        }
        map.put("nombre", nombres.trim());
        map.put("apellido", apellidos.trim());
        return map;
    }

    private Map<String, String> getNumeroContacto(String numero) {
        Map<String, String> map = new HashMap<>();
        String celular = "", telefono = "";
//        char[] separadores = {';'};
//        String numeroAll = numero.replaceAll("[^\\d.]", ";");
//        String[] numeros = numero.split(";");
        String[] numeros = numero.split("\\D+");
        if (numeros.length > 1) {
            if (numeros[0].length() < 10) {
                map.put("telf", numeros[0]);
                map.put("cel", numeros[1]);
            } else {
                map.put("cel", numeros[0]);
                map.put("telf", numeros[1]);
            }
        } else {
            if (numeros[0].length() > 9) {
                map.put("cel", numeros[0]);
                map.put("telf", " ");
            } else {
                map.put("telf", numeros[0]);
                map.put("cel", " ");
            }
        }

        return map;
    }

    private void updateReenvioLiquidacion(String valor) {
        if (liquidacion != null) {
            System.out.println("update>>" + valor);
            liquidacion.setVerificadaWs(valor);
            renFacturaService.edit(liquidacion);
        }
    }

    private void updateAutorizacion() {
        if (liquidacion != null) {
            try {
                Map<String, Object> datosAutorizacion = getDatosAutorizacion();
                liquidacion.setEstadoWs(datosAutorizacion.get("estadoWS").toString());
                liquidacion.setMensajeWs(datosAutorizacion.get("mensajeWS").toString());
                liquidacion.setNumeroAutorizacion(comprobanteSRI.getNumAutorizacion());
                liquidacion.setClaveAcceso(claveAcceso);
                liquidacion.setEstadoLiquidacion(liquidacion.getNumeroAutorizacion() != null && liquidacion.getClaveAcceso() != null
                        ? catalogoItemService.getCatalogoI("estado_liquidacion", "aceptada")
                        : liquidacion.getEstadoLiquidacion()); //ACEPTADA-INGRESADA-INCOMPLETA-ANULADA
                if (comprobanteSRI.getFechaAutorizacion() != null) {
                    liquidacion.setFechaAutorizacion(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(comprobanteSRI.getFechaAutorizacion()));
                }

                renFacturaService.edit(liquidacion);
            } catch (ParseException ex) {
                Logger.getLogger(ComprobanteElectronicosWS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Map getDatosAutorizacion() {
        Map<String, Object> datosAutorizacion = new HashMap<String, Object>();

        String estado = "", mensaje = "";

        if (comprobanteSRI.getRespuestaSolicitudSRI() != null && !comprobanteSRI.getRespuestaSolicitudSRI().isEmpty()) {
            estado = (comprobanteSRI.getRespuestaSolicitudSRI().get(0).getEstado() != null
                    ? comprobanteSRI.getRespuestaSolicitudSRI().get(0).getEstado() : "");

            mensaje = (comprobanteSRI.getRespuestaSolicitudSRI().get(0).getMensaje() != null
                    ? comprobanteSRI.getRespuestaSolicitudSRI().get(0).getMensaje() : "");

        }
        if (comprobanteSRI.getRespuestaAutorizacionSRI() != null && !comprobanteSRI.getRespuestaAutorizacionSRI().isEmpty()) {

            estado = estado + ";" + (comprobanteSRI.getRespuestaAutorizacionSRI().get(0).getEstado() != null
                    ? comprobanteSRI.getRespuestaAutorizacionSRI().get(0).getEstado() : "");

            mensaje = mensaje + ";" + (comprobanteSRI.getRespuestaAutorizacionSRI().get(0).getMensaje() != null
                    ? comprobanteSRI.getRespuestaAutorizacionSRI().get(0).getMensaje() : "");
        }
        datosAutorizacion.put("estadoWS", estado);
        datosAutorizacion.put("mensajeWS", mensaje);

        return datosAutorizacion;
    }

    @Override
    public String verificarServiciosSRI() {
        Ambiente ambiente = ambienteService.findByCodigo(ComprobantesCode.AMBIENTE);
        try {
            String result = "";
            Boolean result_1 = VerificarUrl(ambiente.getWsUrlRecepcion(), 0);
            Boolean result_2 = VerificarUrl(ambiente.getWsUrlAutorizacion(), 1);
            result = "Servicio de recepción: " + (result_1 ? "EN LINEA" : "FUERA DE LINEA") + " - Servicio de autorización: " + (result_2 ? "EN LINEA" : "FUERA DE LINEA");
            return result;
        } catch (Exception ex) {
            return "";
        }
    }

    public boolean VerificarUrl(String url, int recEnv) {
        String claveAcceso = "";
        String cadenaBytes = "";
        String xmlEnviar = "";
        String xmlRecibir = "";
        String xml = "";
        boolean retorno = false;

        try {
            cadenaBytes = DatatypeConverter.printBase64Binary(cadenaBytes.getBytes("UTF-8"));
            xmlEnviar = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ec='http://ec.gob.sri.ws.recepcion'>"
                    + "<soapenv:Header/>"
                    + "<soapenv:Body>"
                    + "<ec:validarComprobante>"
                    + "<xml>" + cadenaBytes + "</xml>"
                    + "</ec:validarComprobante>"
                    + "</soapenv:Body>"
                    + "</soapenv:Envelope>";

            xmlRecibir = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ec='http://ec.gob.sri.ws.autorizacion'>"
                    + "<soapenv:Header/>"
                    + "<soapenv:Body>"
                    + "<ec:autorizacionComprobante>"
                    + "<claveAccesoComprobante>" + claveAcceso + "</claveAccesoComprobante>"
                    + "</ec:autorizacionComprobante>"
                    + "</soapenv:Body>"
                    + "</soapenv:Envelope>";
            if (recEnv <= 0) {
                xml = xmlEnviar;
            } else {
                xml = xmlRecibir;
            }
            if (RevisarEstadoSw(url, xml)) {
                System.out.println("EN LINEA URL-->" + url);
                retorno = true;
            } else {
                System.out.println("FUERA DE LINEA URL-->" + url);
                retorno = false;
            }
        } catch (Exception ex) {
            retorno = false;
        }
        return retorno;
    }

    public boolean RevisarEstadoSw(String url, String xml) {
        URL oURL;
        boolean retorno;
        try {
            oURL = new URL(url);
            HttpURLConnection con = null;

            con = (HttpURLConnection) oURL.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", "");
            con.setRequestProperty("Host", "celcer.sri.gob.ec");

            OutputStream reqStreamOut = con.getOutputStream();
            reqStreamOut.write(xml.getBytes());
            System.out.println("Codito HTTP--> " + String.valueOf(con.getResponseCode()) + " Mensaje--> " + con.getResponseMessage());
            if (con.getResponseCode() > 200) {
                return false;
            }
            return true;
        } catch (MalformedURLException e) {
            retorno = false;
        } catch (Exception ex) {
            retorno = false;
        }
        return retorno;
    }
}
