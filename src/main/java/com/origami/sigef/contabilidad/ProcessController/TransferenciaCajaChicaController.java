/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.Transferencias;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.controller.TransferenciasController;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransferenciasService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.TransferenciasService;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "tranferenciaCajaChicaView")
@ViewScoped
public class TransferenciaCajaChicaController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(TransferenciasController.class.getName());

    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private TransferenciasService transferenciasService;
    @Inject
    private DetalleTransferenciasService detalleTransferenciasService;
    @Inject
    private BeneficiarioComprobantePagoService beneficiarioComprobantePagoService;
    @Inject
    private ComprobantePagoService comprobantePagoService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private AnticipoRemuneracionService anticipoService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private DetalleComprobantePagoService detalleComprobantePagoService;
    @Inject
    private DiarioGeneralService diarioService;

    private Transferencias transferencias;
    private ComprobantePago comprobantePagoBusqueda;
    private OpcionBusqueda opcionBusqueda;
    private BigInteger numTramite;
    private DetalleTransferencias detalleTransferencia;
    private Boolean ocultarTabla;
    private Boolean camposCorresponsal;
    private Boolean renderedBtn;
    private LazyModel<Transferencias> transferenciasLazyModel;
    private LazyModel<Servidor> servidorLazyModel;
    private LazyModel<ComprobantePago> comprobantePagoLazyModel;
    private List<Servidor> responsablesSeleccionados;
    private List<DetalleTransferencias> detalleTrasferencias;
    private List<ComprobantePago> comprobantePagosList;
    private List<UploadedFile> files;

    private CatalogoItem listaEspera;
    private CatalogoItem pagado;
    private AnticipoRemuneracion anticipo;
    private DiarioGeneral diario;
    private ComprobantePago comprobantePago;
    private int proceso;
    private BeneficiarioComprobantePago comprobanteTemporal;
    private boolean enabledIniciar;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + tramite.getNumTramite());
                comprobantePago = new ComprobantePago();
                switch (getTramite().getTipoTramite().getAbreviatura()) {
                    case "PAGORMU":
                        proceso = 1;
                        listaEspera = anticipoService.getEstadoAnticipo("EST_ANTI", (short) 9);
                        pagado = anticipoService.getEstadoAnticipo("EST_ANTI", (short) 3);
                        anticipo = anticipoService.findAnticipoByNTramite(numTramite);
                        break;
                    case "PPS_profesionales":
                        proceso = 2;
                        comprobantePago = diarioService.getComprobanteProcess(tramite.getNumTramite());
                        break;

                }
                this.transferenciasLazyModel = new LazyModel<>(Transferencias.class);
                this.comprobantePagoLazyModel = new LazyModel<>(ComprobantePago.class);
                this.transferenciasLazyModel.getFilterss().put("numeroTramite", numTramite);
                this.comprobantePagoLazyModel.getFilterss().put("numeroTramite", numTramite);
                this.comprobantePagoLazyModel.getSorteds().put("id", "ASC");
                this.comprobantePagoLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
                this.comprobantePagoLazyModel.getFilterss().put("estado", "REGISTRADO");
                this.transferenciasLazyModel.getSorteds().put("id", "ASC");
                this.transferenciasLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
            }
        }
        verificador(BigInteger.valueOf(tramite.getNumTramite()));
        vaciarFormulario("CONSTRUCTOR");
        this.detalleTransferencia = new DetalleTransferencias();
        comprobanteTemporal = new BeneficiarioComprobantePago();
        comprobanteTemporal = beneficiarioComprobantePagoService.getBeneficiarioComprobantePago(BigInteger.valueOf(tramite.getNumTramite()));
    }

    public void verificador(BigInteger num) {
        List<Transferencias> lista = transferenciasService.getlistaVerificadorTransferencias(num);
        if (lista.size() > 0) {
            enabledIniciar = true;
        } else {
            enabledIniciar = false;
        }
    }

    public boolean renderedAnulacion() {
        if (proceso == 1 || proceso == 2) {
            return true;
        }
        return false;
    }

    public void inicioProceso() {
        this.transferencias.setFechaAfectacion(new Date());
        this.transferencias.setPeriodo(opcionBusqueda.getAnio());
        Transferencias ultimaTransferencia = transferenciasService.getUltimaTransferencia(opcionBusqueda.getAnio());
        if (ultimaTransferencia != null) {
            transferencias.setNumReferencia(BigInteger.valueOf(ultimaTransferencia.getNumReferencia().longValue() + 1));
        } else {
            transferencias.setNumReferencia(BigInteger.valueOf(1));
        }
    }

    public void form(Transferencias transferencia) {
        if (transferencia != null) {
            this.transferencias = transferencia;
        } else {
            this.transferencias.setFechaAfectacion(new Date());
            this.transferencias.setPeriodo(opcionBusqueda.getAnio());
            this.transferencias.setDescripcion("PROCESO " + tramite.getTipoTramite().getDescripcion().toUpperCase() + ",TRÁMITE " + tramite.getNumTramite() + "-" + this.transferencias.getPeriodo() + ", ");
            Transferencias ultimaTransferencia = transferenciasService.getUltimaTransferencia(opcionBusqueda.getAnio());
            if (ultimaTransferencia != null) {
                transferencias.setNumReferencia(BigInteger.valueOf(ultimaTransferencia.getNumReferencia().longValue() + 1));
            } else {
                transferencias.setNumReferencia(BigInteger.valueOf(1));
            }
            ComprobantePago buscandoComprobantePago = transferenciasService.getComprobantePago(BigInteger.valueOf(tramite.getNumTramite()));
            seleccionarComprobantePago(buscandoComprobantePago);

        }
        this.ocultarTabla = Boolean.FALSE;
        PrimeFaces.current().ajax().update("mantenimientoTransferencia");
        PrimeFaces.current().ajax().update("formularioTransferencia");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void save() throws FileNotFoundException, UnsupportedEncodingException {
        boolean edit = transferencias.getId() != null;
        if (edit) {

        } else {
            if (transferencias.getDescripcion().equals("")) {
                JsfUtil.addWarningMessage("TRANSFERENCIA", "Ingrese la descripción de la Transferencia");
                return;
            }
            if (transferencias.getLocalidad().equals("")) {
                JsfUtil.addWarningMessage("LOCALIDAD", "Debe ingresar la localidad");
                return;
            }
            if (transferencias.getResponsable1() == null) {
                JsfUtil.addWarningMessage("RESPONSABLE", "Debe ingresar al menos 1 responsable ");
                return;
            }
            if (transferencias.getCtaCteBceIp().equals("") || transferencias.getNombreInstitucion().equals("")) {
                JsfUtil.addWarningMessage("INSTITUCIÓN PÚBLICA(IP)", "Debe ingresar la información completa de la institución");
                return;
            }
            if (transferencias.getCorresponsal()) {
                if (transferencias.getCtaRotativa().equals("") || transferencias.getCtaCteBceCorresponsal().equals("") || transferencias.getNombreCorresponsal().equals("")) {
                    JsfUtil.addWarningMessage("CORRESPONSAL", "Debe ingresar la información de Cta. Rotativa Pagos, Cta. Cte en BCE y Nombre Corresponsal");
                    return;
                }
            }
            if (detalleTrasferencias == null || detalleTrasferencias.isEmpty()) {
                JsfUtil.addWarningMessage("DETALLE TRANSFERENCIA", "No se puede guardar debido a que no exiten detalles de la tranferencia relacionado");
                return;
            }
            /*SACAMOS EL VALOR TOTAL A PAGAR*/
            double valorTotal = 0;
            for (DetalleTransferencias detalle : detalleTrasferencias) {
                valorTotal += detalle.getValor().doubleValue();
            }
            /*CREAMOS Y GUARDAMOS EL ENCABEZADO DE UNA TRANSFERENCIA*/
            transferencias.setDescripcion(transferencias.getDescripcion().toUpperCase());
            transferencias.setLocalidad(transferencias.getLocalidad().toUpperCase());
            transferencias.setNombreInstitucion(transferencias.getNombreInstitucion().toUpperCase());
            transferencias.setUsuarioCreacion(userSession.getNameUser());
            transferencias.setFechaCreacion(new Date());
            transferencias.setEstadoTransferencia("EMITIDO");
            transferencias.setValor(new BigDecimal(valorTotal));
            transferencias.setNumeroTramite(numTramite);
            transferencias = transferenciasService.create(transferencias);
            /*CREAMOS Y GUARDAMOS LOS DETALLES DE LA TRANSFERENCIA*/
            for (DetalleTransferencias detalle : detalleTrasferencias) {
                detalle.setTransferencia(transferencias);
                detalle.setEstado(transferencias.getEstadoTransferencia());
                detalleTransferenciasService.create(detalle);
            }
            switch (getTramite().getTipoTramite().getAbreviatura()) {
                case "PAGORMU":
                    anticipo.setEstadoAnticipo(listaEspera);
                    anticipoService.edit(anticipo);
                    break;

            }
            /*EDITAMOS LA INFORMACION DE LOS COMPROBANTES DE PAGOS*/
            for (ComprobantePago comprobante : comprobantePagosList) {
                comprobante.setEstado("TRANSFERENCIA GENERADA");
                comprobantePagoService.edit(comprobante);
            }
            /*DESCARGAMOS Y VEMOR EL DOCUMENTO A DESCARGAR */
            generarArchivosResumen(transferencias);
            if (transferencias.getCorresponsal()) {
                PrimeFaces.current().executeScript("PF('comprobanteTransferenciaDLG').show()");
            } else {
                vaciarFormulario("CANCELAR");
                JsfUtil.addInformationMessage("TRANSFERENCIA", "Se ha registrado correctamente");
            }
        }
        verificador(BigInteger.valueOf(tramite.getNumTramite()));
    }

    public void generarReporteVentanilla(Boolean estado) {
        if (estado) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            generarArchivosVentanilla(transferencias);
            PrimeFaces.current().executeScript("PF('comprobanteTransferenciaDLG').hide()");
        }
        vaciarFormulario("CANCELAR");
        JsfUtil.addInformationMessage("TRANSFERENCIA", "Se ha registrado correctamente");
    }

    public void dlogoObservaciones() {
        try {
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void completarTarea(int var) {

    }

    public void vaciarFormulario(String accion) {
        this.transferencias = new Transferencias();
        this.comprobantePagoBusqueda = new ComprobantePago();
        this.responsablesSeleccionados = new ArrayList<>();
        this.detalleTrasferencias = new ArrayList<>();
        this.comprobantePagosList = new ArrayList<>();
        this.ocultarTabla = Boolean.TRUE;
        this.camposCorresponsal = Boolean.TRUE;
        if (accion.equals("CANCELAR")) {
            PrimeFaces.current().ajax().update("mantenimientoTransferencia");
            PrimeFaces.current().ajax().update("formularioTransferencia");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void openDlgServidores() {
        this.servidorLazyModel = new LazyModel<>(Servidor.class);
        this.servidorLazyModel.getSorteds().put("id", "ASC");
        this.servidorLazyModel.getFilterss().put("estado", true);
        PrimeFaces.current().ajax().update("servidoresDLG");
        PrimeFaces.current().executeScript("PF('servidoresDLG').show()");
    }

    public void activarCorresponsal() {
        if (transferencias.getCorresponsal()) {
            this.camposCorresponsal = Boolean.FALSE;
            transferencias.setCtaCteBceCorresponsal("01820030");
            transferencias.setNombreCorresponsal("SPI-BANCO CENTRAL");
        } else {
            this.camposCorresponsal = Boolean.TRUE;
            transferencias.setCtaRotativa("");
            transferencias.setCtaCteBceCorresponsal("");
            transferencias.setNombreCorresponsal("");
        }
        PrimeFaces.current().ajax().update("corresponsalGrid");
    }

    public void añadirResponsables() {
        if (responsablesSeleccionados != null && !responsablesSeleccionados.isEmpty()) {
            if (responsablesSeleccionados.size() == 2) {
                int contador = 0;
                for (Servidor servidor : responsablesSeleccionados) {
                    if (contador == 0) {
                        transferencias.setResponsable1(servidor);
                    } else {
                        transferencias.setResponsable2(servidor);
                    }
                    contador += 1;
                }
            } else {
                JsfUtil.addWarningMessage("Aviso", "Se debe seleccionar un maximo de 2 responsables");
                return;
            }
        } else {
            JsfUtil.addWarningMessage("Aviso", "Debe seleccionar al 2 responsable");
            return;
        }
        this.responsablesSeleccionados = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('servidoresDLG').hide()");
        PrimeFaces.current().ajax().update("responsablesGrid");
    }

    public void buscarComprobantePago() {
        if (comprobantePagoBusqueda.getNumComprobante() != null) {
            ComprobantePago comprobantePago;
            if (transferencias.getCtaCteBceIp() != null) {
                comprobantePago = transferenciasService.getComprobantePagoSimilaresCuentasBanco(comprobantePagoBusqueda.getNumComprobante(), opcionBusqueda.getAnio(), "REGISTRADO", transferencias.getCtaCteBceIp());

            } else {
                comprobantePago = transferenciasService.getComprobantePago(comprobantePagoBusqueda.getNumComprobante(), opcionBusqueda.getAnio(), "REGISTRADO");
            }
            if (comprobantePago != null) {
                seleccionarComprobantePago(comprobantePago);
            } else {
                if (transferencias.getCtaCteBceIp() != null) {
                    this.comprobantePagoLazyModel.getFilterss().put("cuentaBancaria.numeroCuenta", transferencias.getCtaCteBceIp());
                }
                PrimeFaces.current().ajax().update("comprobantePagoForm");
                PrimeFaces.current().executeScript("PF('comprobantePagoDLG').show()");
            }
        } else {
            if (transferencias.getCtaCteBceIp() != null) {
                this.comprobantePagoLazyModel.getFilterss().put("cuentaBancaria.numeroCuenta", transferencias.getCtaCteBceIp());
            }
            PrimeFaces.current().ajax().update("comprobantePagoForm");
            PrimeFaces.current().executeScript("PF('comprobantePagoDLG').show()");
        }
        PrimeFaces.current().ajax().update("gridCuentaBancaria");
        PrimeFaces.current().ajax().update("formularioTransferencia");
        PrimeFaces.current().ajax().update("detalleTransferenciaTable");
    }

    public List<BeneficiarioComprobantePago> viewDetalleBeneficiarios(ComprobantePago comprobantePago) {
        List<BeneficiarioComprobantePago> resultado = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobantePago);
        return resultado;
    }

    public void seleccionarComprobantePago(ComprobantePago comprobanteP) {
        this.comprobantePagoBusqueda = new ComprobantePago();
        Boolean existe = Boolean.FALSE;
        if (!comprobantePagosList.isEmpty()) {
            for (ComprobantePago comprobante : comprobantePagosList) {
                if (comprobanteP.equals(comprobante)) {
                    existe = Boolean.TRUE;
                }
            }
        }
        if (existe) {
            JsfUtil.addErrorMessage("AVISO", "La información del comprobante seleccionado ya estan cargados en la lista");
        } else {
            List<BeneficiarioComprobantePago> resultado = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobanteP);
            if (resultado != null && !resultado.isEmpty()) {
                for (BeneficiarioComprobantePago beneficiario : resultado) {
                    DetalleTransferencias detalle = new DetalleTransferencias();
                    if (beneficiario.getTipoBeneficiario()) {
                        detalle.setIdentificacion(beneficiario.getBeneficiario().getIdentificacionCompleta());
                        detalle.setNombreBeneficiario(beneficiario.getBeneficiario().getNombre());
                    } else {
                        detalle.setIdentificacion(beneficiario.getBeneficiario().getIdentificacion());
                        detalle.setNombreBeneficiario(beneficiario.getBeneficiario().getNombreCompleto());
                    }
                    detalle.setReferencia(beneficiario.getNumeroTransferencia());
                    detalle.setInstitucionFinanciera(beneficiario.getDetalleBanco().getBanco());
                    detalle.setCuentaBcoBeneficiario(beneficiario.getDetalleBanco().getCuentaBanco());
                    detalle.setTipoCuenta(String.valueOf(beneficiario.getDetalleBanco().getTipoCuenta().getOrden()));
                    detalle.setValor(beneficiario.getValor());
                    detalle.setDetalle(beneficiario.getComprobantePago().getDetalle());
                    detalle.setComprobantePago(comprobanteP);
                    this.detalleTrasferencias.add(detalle);
                }
                if (!comprobantePagosList.contains(comprobanteP)) {
                    comprobantePagosList.add(comprobanteP);
                }
                String descripcionComprobantePago = "TRANSFERENCIA DEL(LOS) COMPROBANTE(S) DE PAGO NO." + comprobanteP.getNumComprobante() + "-" + Utils.getAnio(comprobanteP.getFechaComprobante()) + ", ";
                if (transferencias.getDescripcion() != null) {
                    transferencias.setDescripcion(transferencias.getDescripcion() + "NO." + comprobanteP.getNumComprobante() + "-" + Utils.getAnio(comprobanteP.getFechaComprobante()) + ", ");
                } else {
                    transferencias.setDescripcion(descripcionComprobantePago);
                }
                transferencias.setCtaCteBceIp(comprobanteP.getCuentaBancaria().getNumeroCuenta());
                transferencias.setNombreInstitucion(comprobanteP.getCuentaBancaria().getNombreCuentaBancaria());
                JsfUtil.addInformationMessage("Detalle de la Transferencia", "Se han cargado datos correctamente");
                PrimeFaces.current().ajax().update("detalleTransferenciaTable");
                PrimeFaces.current().ajax().update("gridDescripcion");
                PrimeFaces.current().executeScript("PF('comprobantePagoDLG').hide()");
            } else {
                JsfUtil.addWarningMessage("AVISO", "No hay beneficiarios relacionados a este comprobante de pago");
            }
        }
        PrimeFaces.current().ajax().update("formMain");
    }

    public void generarArchivosVentanilla(Transferencias transferencias) {
        String detalle = "Por medio de la presente, solicito a usted se sirva al Banco Central del Ecuador el detalle de los pagos contenidos"
                + " en el medio magnético que adjunto y que deberá ejecutarse el " + Utils.dateFormatPattern("dd/MM/yyyy", transferencias.getFechaAfectacion())
                + " a través del Sistema de Pagos Interbancarios SPI, con los recursos que el/la " + transferencias.getNombreInstitucion()
                + " mantiene en la cuenta corriente No. " + transferencias.getCtaCteBceIp() + " en el  Banco Central del Ecuador.";
        String piePagina = "Suscribimos este pedido en calidad de firma(s) autorizada(s) de la cuenta rotativa de pagos"
                + " No. " + transferencias.getCtaCteBceIp() + " que el/la " + transferencias.getNombreInstitucion() + " mantiene en el " + transferencias.getNombreCorresponsal();
        servletSession.addParametro("id_transferencia", transferencias.getId());
        servletSession.addParametro("detalle", detalle);
        servletSession.addParametro("piePagina", piePagina);
        servletSession.setNombreReporte("transferenciasVentanilla");
        servletSession.setNombreSubCarpeta("transferencias");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public void generarArchivosResumen(Transferencias transferencias) {
        servletSession.addParametro("id_transferencia", transferencias.getId());
        servletSession.setNombreReporte("transferenciasDetalle");
        servletSession.setNombreSubCarpeta("transferencias");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    /*VIZUALIZACION DE LOS DETALLES DE LA TRANSFERENCIA*/
    public List<DetalleTransferencias> viewDetalle(Transferencias transferencias) {
        List<DetalleTransferencias> detalleTransferenciasList = detalleTransferenciasService.getDetallesTransferencia(transferencias);
        return detalleTransferenciasList;
    }

    /*ACCIONES DE ANULACION, ACREDITACION*/
    public void subirArchivo(DetalleTransferencias detalle) {
        this.detalleTransferencia = detalle;
        files = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('comprobanteSPIDialog').show()");
    }

    public void handleFileUploadComprobanteSPI(FileUploadEvent event) {
        try {
            files.add(event.getFile());
            PrimeFaces.current().executeScript("PF('comprobanteSPIDialog').hide()");
            PrimeFaces.current().ajax().update("transferenciaTable");
            addArchivo();
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void addArchivo() {
        if (files != null) {
            uploadDoc.upload(this.detalleTransferencia, files);
            JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
        }
        Transferencias transferencia = detalleTransferencia.getTransferencia();
        /*EDITAMOS LA INFORMACION DE LOS DETALLE DE LA TRANSFERENCIA*/
        detalleTransferencia.setEstado("ACREDITADO");
        detalleTransferencia.setFechaAcreditacion(new Date());
        detalleTransferenciasService.edit(detalleTransferencia);
        /*EDITAMOS LA INFORMACION DEL ENCABEZADO DE LA TRANSFERENCIA*/
        int contador = 0;
        List<DetalleTransferencias> detallesTranferenciaList = detalleTransferenciasService.getDetallesTransferencia(transferencia);
        for (DetalleTransferencias detalle : detallesTranferenciaList) {
            if (detalle.getEstado().equals("ACREDITADO")) {
                contador += 1;
            }
        }
        if (contador == detallesTranferenciaList.size()) {
            transferencia.setEstadoTransferencia("ACREDITADO");
        } else {
            transferencia.setEstadoTransferencia("ACREDITADO-PARCIAL");
        }
        transferencia.setFechaAcreditacion(new Date());
        transferencia.setUsuarioModificacion(userSession.getNameUser());
        transferencia.setFechaModificacion(new Date());
        transferenciasService.edit(transferencia);
        /*EDITAMOS LA INFORMACION DEL DETALLE DE COMPROBANTE RELACIOANDO*/
        BeneficiarioComprobantePago beneficiarioComprobantePago = beneficiarioComprobantePagoService.getBeneficiarioComprobantePago(detalleTransferencia.getComprobantePago(), detalleTransferencia.getIdentificacion().substring(0, 10));
        beneficiarioComprobantePago.setEstadoBeneficiario("ACREDITADO");
        beneficiarioComprobantePagoService.edit(beneficiarioComprobantePago);
        /*EDITAMOS LA INFORMACION DEL COMPROBANTES DE PAGO */
        ComprobantePago comprobanteP = detalleTransferencia.getComprobantePago();
        List<BeneficiarioComprobantePago> beneficiarioComprobantePagoList = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobanteP);
        contador = 0;
        for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
            if (beneficiario.getEstadoBeneficiario().equals("ACREDITADO")) {
                contador += 1;
            }
        }
        if (contador == beneficiarioComprobantePagoList.size()) {
            comprobanteP.setEstado("TRANSFERENCIA ACREDITADA");
        } else {
            comprobanteP.setEstado("TRANSFERENCIA ACREDITADA-PARCIAL");
        }
        comprobanteP.setUsuarioModificacion(userSession.getNameUser());
        comprobanteP.setFechaModificacion(new Date());
        comprobantePagoService.edit(comprobanteP);
        this.detalleTransferencia = new DetalleTransferencias();
        PrimeFaces.current().ajax().update("transferenciaTable");
        String asunto;
        String mensaje;
        switch (getTramite().getTipoTramite().getAbreviatura()) {
            case "PAGORMU":
                asunto = "SOLICITUD DE ANTICIPO DE REMUNERACIÓN";
                mensaje = "<html lang=\"es\">\n"
                        + "<head>\n"
                        + "<meta charset=\"utf-8\"/>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<p style=\"width:200px;\">SR(a). " + anticipo.getServidor().getPersona().getNombreCompleto()
                        + "\n POR MEDIO DE LA PRESENTE SE LE INFORMA QUE SU SOLICITUD DE ANTICIPO DE REMUNERACIÓN AH SIDO ACREDITADA EN SU CUENTA DE AHORRO CORRESPONDIENTE "
                        + " SEGÚN EL NUMERO DE TRÁMITE N° " + anticipo.getNumTramite() + " </p>\n"
                        + "</body>\n"
                        + "</html>";
                anticipo.setEstadoAnticipo(pagado);
                anticipoService.edit(anticipo);
                getParamts().put("aprobado", 1);
                notificar(asunto, anticipo.getServidor().getEmailInstitucion(), mensaje, anticipo.getServidor().getPersona().getNombreCompleto());
                break;
            case "PPS_profesionales":
                asunto = "PAGO DE SERVICIOS PROFECIONALES";
                mensaje = "<html lang=\"es\">\n"
                        + "<head>\n"
                        + "<meta charset=\"utf-8\"/>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<p style=\"width:200px;\">SR(a). " + diario.getBeneficiario().getNombreCompleto()
                        + "\n POR MEDIO DE LA PRESENTE SE LE NOTIFICA QUE LE PAGO POR SUS SERVICIOS PROFECIONALES A SIDO ACREDITADA CON ÈXITO"
                        + " SEGÚN EL NUMERO DE TRÁMITE N° " + diario.getNumTramite() + " </p>\n"
                        + "</body>\n"
                        + "</html>";
                getParamts().put("aprobadoTe", 1);
                notificar(asunto, "jonathanchoez_94@hotmail.com", mensaje, diario.getBeneficiario().getNombreCompleto());
                break;

            case "proceso_af_caja_chica":
                Cliente c = comprobantePagoService.getClientecomprobantePago(BigInteger.valueOf(tramite.getNumTramite()));
                asunto = tramite.getTipoTramite().getDescripcion();
                mensaje = "<html lang=\"es\">\n"
                        + "<head>\n"
                        + "<meta charset=\"utf-8\"/>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<p style=\"width:200px;\">SR(a). " + c.getNombreCompleto()
                        + "\n POR MEDIO DE LA PRESENTE SE LE NOTIFICA QUE LA " + tramite.getTipoTramite().getDescripcion() + " HA SIDO APROBADA"
                        + " SEGÚN EL NUMERO DE TRÁMITE N° " + comprobanteP.getNumeroTramite() + " </p>\n"
                        + "</body>\n"
                        + "</html>";
                getParamts().put("aprobadoTe", 1);
                notificar(asunto, c.getEmail(), mensaje, c.getNombreCompleto());
                break;

        }
    }

    public void anularTransferenciaTotal(Transferencias transferencias) {
        transferencias.setEstadoTransferencia("ANULADO");
        transferencias.setFechaAnulacion(new Date());
        transferencias.setUsuarioModificacion(userSession.getNameUser());
        transferencias.setFechaModificacion(new Date());
        transferenciasService.edit(transferencias);
        List<DetalleTransferencias> detalleTransferenciasList = detalleTransferenciasService.getDetallesTransferencia(transferencias);
        for (DetalleTransferencias detalle : detalleTransferenciasList) {
            detalle.setEstado(transferencias.getEstadoTransferencia());
            detalle.setFechaAnulacion(new Date());
            detalleTransferenciasService.edit(detalle);
        }
        /*SE ANULA EL/LOS COMPROBANTE DE PAGO Y SUS DETALLES*/
        List<ComprobantePago> comprobantePagoList = transferenciasService.getComprobantesRegistrados(transferencias);
        for (ComprobantePago comprobante : comprobantePagoList) {
            List<BeneficiarioComprobantePago> beneficiarioCP = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobante);
            for (BeneficiarioComprobantePago beneficiario : beneficiarioCP) {
                beneficiario.setEstadoBeneficiario("TRANSFERENCIA ANULADA");
                beneficiario.setValor(new BigDecimal(beneficiario.getValor().doubleValue() * (-1)));
                beneficiarioComprobantePagoService.edit(beneficiario);
            }
            comprobante.setEstado("TRANSFERENCIA ANULADA");
            comprobante.setUsuarioModificacion(userSession.getNameUser());
            comprobante.setFechaModificacion(new Date());
            comprobantePagoService.edit(comprobante);
            List<DetalleComprobantePago> detalleCPList = detalleComprobantePagoService.getDetalleComprobantePago(comprobante);
            for (DetalleComprobantePago detalle : detalleCPList) {
                if (detalle.getDebe().doubleValue() > 0) {
                    detalle.setDebe(new BigDecimal(detalle.getDebe().doubleValue() * (-1)));
                    detalle.setEjecutado(new BigDecimal(detalle.getEjecutado().doubleValue() * (-1)));
                } else {
                    detalle.setHaber(new BigDecimal(detalle.getHaber().doubleValue() * (-1)));
                }
                detalleComprobantePagoService.edit(detalle);
            }
        }
        try {
            switch (getTramite().getTipoTramite().getAbreviatura()) {
                case "PAGORMU":
                    getParamts().put("aprobado", 0);
                    break;
                case "PPS_profesionales":
                    getParamts().put("aprobadoTe", 0);
                    getParamts().put("form", "/proceso/contabilidad/pagoServicios/comprobante-pago");
                    break;
                case "proceso_af_caja_chica":
                    getParamts().put("aprobadoTe", 0);
                    break;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
        PrimeFaces.current().ajax().update("transferenciaTable");
    }

    public void anularDetalleTransferencia(DetalleTransferencias detalleTransferencia) {
        /*GUARDA LA INFORMACION DEL USUARIO QUE LO MODIFICO*/
        Transferencias transferencia = detalleTransferencia.getTransferencia();
        List<DetalleTransferencias> detalleTransferenciasList = detalleTransferenciasService.getDetallesTransferencia(transferencia);
        if (detalleTransferenciasList.size() == 1) {
            transferencia.setEstadoTransferencia("ANULADO");
        } else {
            transferencia.setEstadoTransferencia("ACREEDITADO-PARCIAL");
        }
        transferencia.setUsuarioModificacion(userSession.getNameUser());
        transferencia.setFechaModificacion(new Date());
        transferenciasService.edit(transferencia);
        /*EDITA EL ESTADO Y LA FECHA DE ANULACION DEL DETALLE DE TRANFERENCIA QUE SE SELECCIONÓ*/
        detalleTransferencia.setEstado("ANULADO");
        detalleTransferencia.setFechaAnulacion(new Date());
        detalleTransferenciasService.edit(detalleTransferencia);
        /*ACTUALIZAMOS EL ESTADO DEL BENEFICIARIO QUE FUE ANULADO*/
        BeneficiarioComprobantePago beneficiarioComprobantePago = beneficiarioComprobantePagoService.getBeneficiarioComprobantePago(detalleTransferencia.getComprobantePago(), detalleTransferencia.getIdentificacion().substring(0, 10));
        beneficiarioComprobantePago.setEstadoBeneficiario("ANULADO");
        beneficiarioComprobantePagoService.edit(beneficiarioComprobantePago);
        /*ACTUALIZAMOS LA INFORMACION DEL COMPROBANTE DE PAGO QUE SI S ANULA UNA TRANSCCION DE BENEFICIARIO, ESTE SE VERA REFLEJADO EN EL COMPROBANTE DE PAGO*/
        ComprobantePago comprobante = beneficiarioComprobantePago.getComprobantePago();
        comprobante.setEstado(transferencia.getEstadoTransferencia());
        comprobantePagoService.edit(comprobante);
        PrimeFaces.current().ajax().update("transferenciaTable");
        try {
            switch (getTramite().getTipoTramite().getAbreviatura()) {
                case "PAGORMU":
                    getParamts().put("aprobado", 0);
                    break;

            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    public void notificar(String asunto, String email, String mensaje, String beneficiario) {
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje(mensaje);
//            List<CorreoArchivo> archivos = new ArrayList();
//            Map<String, Object> parametros = new HashMap();
//            parametros.put("tramite", impresion.getNumeroTramite());
//            String rutaArchivo = katalinaService.buildJasper(impresion.getId(), "\\activos\\actaEntregaRecepInventario", parametros);
//            CorreoArchivo archivo = new CorreoArchivo("Reporte", Utils.encodeFileToBase64Binary(rutaArchivo), "pdf");
//            archivos.add(archivo);
        c.setArchivos(null);
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de emai: " + email + " relacionada con: " + beneficiario);
//            this.anticipo.setEstadoAnticipo(pagado);
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public void verDocumentos(DetalleTransferencias detalle) {
        this.detalleTransferencia = detalle;
        PrimeFaces.current().executeScript("PF('viewDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
    }

    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public Transferencias getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(Transferencias transferencias) {
        this.transferencias = transferencias;
    }

    public boolean isEnabledIniciar() {
        return enabledIniciar;
    }

    public void setEnabledIniciar(boolean enabledIniciar) {
        this.enabledIniciar = enabledIniciar;
    }

    public BeneficiarioComprobantePago getComprobanteTemporal() {
        return comprobanteTemporal;
    }

    public void setComprobanteTemporal(BeneficiarioComprobantePago comprobanteTemporal) {
        this.comprobanteTemporal = comprobanteTemporal;
    }

    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public Boolean getRenderedBtn() {
        return renderedBtn;
    }

    public void setRenderedBtn(Boolean renderedBtn) {
        this.renderedBtn = renderedBtn;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public Boolean getOcultarTabla() {
        return ocultarTabla;
    }

    public void setOcultarTabla(Boolean ocultarTabla) {
        this.ocultarTabla = ocultarTabla;
    }

    public Boolean getCamposCorresponsal() {
        return camposCorresponsal;
    }

    public void setCamposCorresponsal(Boolean camposCorresponsal) {
        this.camposCorresponsal = camposCorresponsal;
    }

    public LazyModel<Transferencias> getTransferenciasLazyModel() {
        return transferenciasLazyModel;
    }

    public void setTransferenciasLazyModel(LazyModel<Transferencias> transferenciasLazyModel) {
        this.transferenciasLazyModel = transferenciasLazyModel;
    }

    public LazyModel<Servidor> getServidorLazyModel() {
        return servidorLazyModel;
    }

    public void setServidorLazyModel(LazyModel<Servidor> servidorLazyModel) {
        this.servidorLazyModel = servidorLazyModel;
    }

    public List<Servidor> getResponsablesSeleccionados() {
        return responsablesSeleccionados;
    }

    public void setResponsablesSeleccionados(List<Servidor> responsablesSeleccionados) {
        this.responsablesSeleccionados = responsablesSeleccionados;
    }

    public LazyModel<ComprobantePago> getComprobantePagoLazyModel() {
        return comprobantePagoLazyModel;
    }

    public void setComprobantePagoLazyModel(LazyModel<ComprobantePago> comprobantePagoLazyModel) {
        this.comprobantePagoLazyModel = comprobantePagoLazyModel;
    }

    public ComprobantePago getComprobantePagoBusqueda() {
        return comprobantePagoBusqueda;
    }

    public void setComprobantePagoBusqueda(ComprobantePago comprobantePagoBusqueda) {
        this.comprobantePagoBusqueda = comprobantePagoBusqueda;
    }

    public List<DetalleTransferencias> getDetalleTrasferencias() {
        return detalleTrasferencias;
    }

    public void setDetalleTrasferencias(List<DetalleTransferencias> detalleTrasferencias) {
        this.detalleTrasferencias = detalleTrasferencias;
    }

    public DetalleTransferencias getDetalleTransferencia() {
        return detalleTransferencia;
    }

    public void setDetalleTransferencia(DetalleTransferencias detalleTransferencia) {
        this.detalleTransferencia = detalleTransferencia;
    }

//</editor-fold>
}
