/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.procesos.pagos;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Transferencias;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.controller.TransferenciasController;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DetalleTransferenciasService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.TransferenciasService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import java.io.File;
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
 * @author jintr
 */
@Named(value = "archivoSPIView")
@ViewScoped
public class ArchivoSPIController extends BpmnBaseRoot implements Serializable {

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
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ValoresService valoresService;

    private Transferencias transferencias;
    private ComprobantePago comprobantePagoBusqueda;
    private OpcionBusqueda opcionBusqueda;
    private BigInteger numTramite;
    private DetalleTransferencias detalleTransferencia;
    private CatalogoItem listaEspera;
    private CatalogoItem pagado;
    private AnticipoRemuneracion anticipo;
    private DiarioGeneral diario;

    private Boolean ocultarTabla;
    private Boolean camposCorresponsal;
    private Boolean renderedBtn;
    private Boolean accionAnular;

    private LazyModel<Transferencias> transferenciasLazyModel;
    private LazyModel<ComprobantePago> comprobantePagoLazyModel;
    private List<Servidor> responsablesSeleccionados;
    private List<DetalleTransferencias> detalleTrasferencias;
    private List<ComprobantePago> comprobantePagosList;
    private List<UploadedFile> files;
    private List<CatalogoItem> conceptoTransferencia;
    private ComprobantePago comprobantePago;
    private int proceso;
    private BeneficiarioComprobantePago comprobanteTemporal;
    private boolean enabledIniciar;
    private Boolean btnCompletarTarea;
    private String zipSPI;
    private File archivoProvee;
    private Boolean btn_1;
    private Boolean btn_2;
    private Boolean btn_3;
    private Boolean btn_4;
    private CatalogoItem conceptoSeleccionado;
    private Boolean nuevo = true;
    private Date fechaAcreditacion;
    private Integer progress2;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + tramite.getNumTramite());
                conceptoTransferencia = catalogoService.getItemsByCatalogo("concepto_transferencia");
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
                        comprobantePago = diarioGeneralService.getComprobanteProcess(tramite.getNumTramite());
                        break;
                    case "PAG_ANTI_LIQUI_HABER":
                        proceso = 2;
                        comprobantePago = diarioGeneralService.getComprobanteProcess(tramite.getNumTramite());
                        break;
                    case "PAG_SERV_NOTARIALES":
                        proceso = 2;
                        comprobantePago = diarioGeneralService.getComprobanteProcess(tramite.getNumTramite());
                        break;
                    case "PPPI":
                        comprobantePago = diarioGeneralService.getComprobanteProcess(tramite.getNumTramite());
                        verificador(numTramite);
                        if (enabledIniciar == false) {
                            vaciarFormulario("CONSTRUCTOR");
                            form(null);
                            seleccionarComprobantePago(comprobantePago);
                            try {
                                guardar();
                            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        comprobantePago = diarioGeneralService.getComprobanteProcess(tramite.getNumTramite());
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
    }

    public void verificador(BigInteger num) {
        List<Transferencias> lista = transferenciasService.getlistaVerificadorTransferencias(num);
        if (lista.size() > 0) {
            enabledIniciar = true;
        } else {
            enabledIniciar = false;
        }
        renderBtnCompletarTarea(lista);
    }

    /*METODO PARA APARECER EL BOTON DE COMPLETAR TAREA SI HAY VARIOS BENEFICIARIO*/
    private void renderBtnCompletarTarea(List<Transferencias> lista) {
        if (!lista.isEmpty()) {
            for (Transferencias transf : lista) {
                determinarValorBtnCompletarTarea(transf);
            }
        }
    }

    private void determinarValorBtnCompletarTarea(Transferencias transf) {
        if (!transf.getEstadoTransferencia().equals("ANULADO")) {
            List<DetalleTransferencias> detalleList = detalleTransferenciasService.getDetallesTransferencia(transf);
            if (detalleList.size() > 1) {
                btnCompletarTarea = detalleTransferenciasService.getCompletarTarea(transf);
            }
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
    }

    public void form(Transferencias transferencia) {
        if (transferencia != null) {
            this.transferencias = transferencia;
        } else {
            this.transferencias.setFechaAfectacion(new Date());
            this.transferencias.setPeriodo(opcionBusqueda.getAnio());
            this.transferencias.setDescripcion("PROCESO " + tramite.getTipoTramite().getDescripcion().toUpperCase() + ",TRAMITE " + tramite.getNumTramite() + "-" + this.transferencias.getPeriodo() + ", ");
            ComprobantePago buscandoComprobantePago = transferenciasService.getComprobantePago(BigInteger.valueOf(tramite.getNumTramite()));
            seleccionarComprobantePago(buscandoComprobantePago);
        }
        transferencias.setResponsable1(clienteService.getResponsableTransferencia(RolUsuario.maximaAutoridad));
        transferencias.setResponsable2(clienteService.getResponsableTransferencia(RolUsuario.tesorero));
        if (!getTramite().getTipoTramite().getAbreviatura().equals("PPPI")) {
            this.ocultarTabla = Boolean.FALSE;
        }
        PrimeFaces.current().ajax().update("idtabView:mantenimientoTransferencia");
        PrimeFaces.current().ajax().update("idtabView:formularioTransferencia");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void save() throws FileNotFoundException, UnsupportedEncodingException {
        /*SACAMOS EL VALOR TOTAL A PAGAR*/
        // Variable para el proceso de generacion de SPI-SP.md5
        StringBuffer data = new StringBuffer();
        StringBuilder archivoProveedor = new StringBuilder();
        double valorTotal = 0;
        for (DetalleTransferencias detalle : detalleTrasferencias) {
            if (detalle.getConcepto() == null) {
                JsfUtil.addErrorMessage("Concepto", "Debe seleccionar el concepto");
                return;
            }
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
        System.out.println("ultimaa");
        Transferencias ultimaTransferencia = transferenciasService.getUltimaTransferencia(opcionBusqueda.getAnio());
        if (ultimaTransferencia != null) {
            transferencias.setNumReferencia(BigInteger.valueOf(ultimaTransferencia.getNumReferencia().longValue() + 1));
        } else {
            transferencias.setNumReferencia(BigInteger.valueOf(1));
        }
        transferencias = transferenciasService.create(transferencias);
        /*CREAMOS Y GUARDAMOS LOS DETALLES DE LA TRANSFERENCIA*/
        Integer sumCuenta = 0;
        for (DetalleTransferencias detalle : detalleTrasferencias) {
            // Metodo para generar el archivo de transferencia bancaria
            data.append(detalleTransferenciasService.getRowTransferencia(detalle)).append("\n");
            archivoProveedor.append(detalleTransferenciasService.getRowTransferenciaAP(detalle)).append("\n");
            detalle.setTransferencia(transferencias);
            detalle.setEstado(transferencias.getEstadoTransferencia());
            detalleTransferenciasService.create(detalle);
            sumCuenta += Integer.valueOf(detalle.getInstitucionFinanciera().getCuentaCorriente());
        }
        // INSERTAMOS LA CABEZERA DEL ARCHIVO
        data.insert(0, detalleTransferenciasService.getHeaderTransferecia(transferencias, detalleTrasferencias.size(), sumCuenta, 0, 0) + "\n");
        archivoProveedor.insert(0, detalleTransferenciasService.getHeaderTransfereciaAP(transferencias) + "\n");
        /*EDITAMOS LA INFORMACION DE LOS COMPROBANTES DE PAGOS*/
        for (ComprobantePago comprobante : comprobantePagosList) {
            comprobante.setEstado("TRANSFERENCIA GENERADA");
            comprobantePagoService.edit(comprobante);
        }
        // CREAMOS EL ARCHIVO SPI-SP
        zipSPI = detalleTransferenciasService.createFileSPI(data);
        archivoProvee = detalleTransferenciasService.createTxt(valoresService.findByCodigo("SPI_FILE_APR"), archivoProveedor.toString());
    }

    public void guardar() throws FileNotFoundException, UnsupportedEncodingException {
        boolean edit = transferencias.getId() != null;
        if (edit) {

        } else {
            /*SACAMOS EL VALOR TOTAL A PAGAR*/
            double valorTotal = 0;
            for (DetalleTransferencias detalle : detalleTrasferencias) {
                valorTotal += detalle.getValor().doubleValue();
            }
            if (tramite.getTipoTramite().getAbreviatura().equals("PPPI")) {
                Transferencias ultimaTransferencia = transferenciasService.getUltimaTransferencia(opcionBusqueda.getAnio());
                if (ultimaTransferencia != null) {
                    transferencias.setNumReferencia(BigInteger.valueOf(ultimaTransferencia.getNumReferencia().longValue() + 1));
                } else {
                    transferencias.setNumReferencia(BigInteger.valueOf(1));
                }
            }
            /*CREAMOS Y GUARDAMOS EL ENCABEZADO DE UNA TRANSFERENCIA*/
            transferencias.setDescripcion(transferencias.getDescripcion().toUpperCase());
            transferencias.setNombreInstitucion(transferencias.getNombreInstitucion().toUpperCase());
            transferencias.setUsuarioCreacion(userSession.getNameUser());
            transferencias.setFechaCreacion(new Date());
            transferencias.setEstadoTransferencia("EMITIDO");
            transferencias.setValor(new BigDecimal(valorTotal));
            transferencias.setNumeroTramite(numTramite);
            transferencias.setLocalidad("DURÁN");
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
            this.btn_1 = Boolean.TRUE;
            this.btn_2 = Boolean.TRUE;
            this.btn_3 = Boolean.FALSE;
            if (transferencias.getCorresponsal() != null && transferencias.getCorresponsal()) {
                this.btn_4 = Boolean.FALSE;
            }
            if (!tramite.getTipoTramite().getAbreviatura().equals("PPPI")) {
                PrimeFaces.current().ajax().update("menuArchivosForm");
                PrimeFaces.current().executeScript("PF('menuArchivosDlg').show()");
            }
        }
    }

    public void OpenMenuArchivosDlg(Boolean accion) {
        if (accion) {
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
            for (DetalleTransferencias detalle : detalleTrasferencias) {
                if (detalle.getConcepto() == null) {
                    JsfUtil.addErrorMessage("Concepto", "Debe seleccionar el concepto");
                    return;
                }
            }
        } else {
            generarArchivo();
        }
        this.btn_1 = Boolean.FALSE;
        PrimeFaces.current().ajax().update("menuArchivosForm");
        PrimeFaces.current().executeScript("PF('menuArchivosDlg').show()");
    }

    public void generarArchivo() {
        try {
            // Variable para el proceso de generacion de SPI-SP.md5
            StringBuffer data = new StringBuffer();
            StringBuffer archivoProveedor = new StringBuffer();
            Integer sumCuenta = 0;
            for (DetalleTransferencias detalle : detalleTrasferencias) {
                if (detalle.getConcepto() == null) {
                    JsfUtil.addErrorMessage("Concepto", "Debe seleccionar el concepto");
                    return;
                }
                detalleTransferenciasService.edit(detalle);
                // Método para generar el archivo de transferencia bancaria
                data.append(detalleTransferenciasService.getRowTransferencia(detalle)).append("\n");
                archivoProveedor.append(detalleTransferenciasService.getRowTransferenciaAP(detalle)).append("\n");
                sumCuenta += Integer.valueOf(detalle.getInstitucionFinanciera().getCuentaCorriente());
            }
            // INSERTAMOS LA CABEZERA DEL ARCHIVO
            data.insert(0, detalleTransferenciasService.getHeaderTransferecia(transferencias, detalleTrasferencias.size(), sumCuenta, 0, 0) + "\n");
            archivoProveedor.insert(0, detalleTransferenciasService.getHeaderTransfereciaAP(transferencias) + "\n");
            // CREAMOS EL ARCHIVO SPI-SP
            zipSPI = detalleTransferenciasService.createFileSPI(data);
            archivoProvee = detalleTransferenciasService.createTxt(valoresService.findByCodigo("SPI_FILE_APR"), archivoProveedor.toString());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Generar Archivo", e);
        }
    }

    public void dlogoObservaciones(Transferencias tranferencia) {
        try {
            if (tranferencia != null) {
                this.transferencias = tranferencia;
                accionAnular = true;
            } else {
                accionAnular = false;
            }
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

    public void vaciarFormulario(String accion) {
        this.transferencias = new Transferencias();
        this.comprobantePagoBusqueda = new ComprobantePago();
        this.responsablesSeleccionados = new ArrayList<>();
        this.detalleTrasferencias = new ArrayList<>();
        this.comprobantePagosList = new ArrayList<>();
        this.ocultarTabla = Boolean.TRUE;
        this.camposCorresponsal = Boolean.TRUE;
        this.btn_1 = Boolean.TRUE;
        this.btn_2 = Boolean.TRUE;
        this.btn_3 = Boolean.TRUE;
        this.btn_4 = Boolean.TRUE;
        this.nuevo = true;
        this.conceptoSeleccionado = null;
        if (accion.equals("CANCELAR")) {
            this.btnCompletarTarea = Boolean.FALSE;
            PrimeFaces.current().ajax().update("idtabView:mantenimientoTransferencia");
            PrimeFaces.current().ajax().update("idtabView:formularioTransferencia");
            PrimeFaces.current().ajax().update("formMain");
        }
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
        PrimeFaces.current().ajax().update("idtabView:corresponsalGrid");
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
        PrimeFaces.current().ajax().update("idtabView:gridCuentaBancaria");
        PrimeFaces.current().ajax().update("idtabView:formularioTransferencia");
        PrimeFaces.current().ajax().update("idtabView:detalleTransferenciaTable");
    }

    public List<BeneficiarioComprobantePago> viewDetalleBeneficiarios(ComprobantePago comprobantePago) {
        List<BeneficiarioComprobantePago> resultado = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobantePago);
        return resultado;
    }

    public void seleccionarComprobantePago(ComprobantePago comprobanteP) {
        this.comprobantePagoBusqueda = new ComprobantePago();
        Boolean existe = Boolean.FALSE;
        if (comprobantePagosList != null && !comprobantePagosList.isEmpty()) {
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
                        detalle.setTipoBeneficiario("2");
                        detalle.setIdentificacion(beneficiario.getBeneficiario().getIdentificacionCompleta());
                        detalle.setNombreBeneficiario(beneficiario.getBeneficiario().getNombre());
                    } else {
                        detalle.setTipoBeneficiario("1");
                        detalle.setIdentificacion(beneficiario.getBeneficiario().getIdentificacion());
                        detalle.setNombreBeneficiario(beneficiario.getBeneficiario().getNombreCompleto());
                    }
                    detalle.setReferencia(beneficiario.getNumeroTransferencia());
                    detalle.setInstitucionFinanciera(beneficiario.getDetalleBanco().getBanco());
                    detalle.setCuentaBcoBeneficiario(beneficiario.getDetalleBanco().getCuentaBanco());
                    if (beneficiario.getDetalleBanco().getTipoCuenta() != null) {
                        detalle.setTipoCuenta(String.valueOf(beneficiario.getDetalleBanco().getTipoCuenta().getOrden()));
                    }
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
                PrimeFaces.current().ajax().update("idtabView:detalleTransferenciaTable");
                PrimeFaces.current().ajax().update("idtabView:gridDescripcion");
                PrimeFaces.current().executeScript("PF('comprobantePagoDLG').hide()");
            } else {
                JsfUtil.addWarningMessage("AVISO", "No hay beneficiarios relacionados a este comprobante de pago");
            }
        }
        PrimeFaces.current().ajax().update("formMain");
    }

    public void generarArchivosDescargas(int accion) throws FileNotFoundException {
        switch (accion) {
            case 1:
                if (transferencias.getId() == null) {
                    try {
                        save();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(TransferenciasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.btn_2 = Boolean.FALSE;
                this.btn_3 = Boolean.FALSE;
                if (transferencias.getCorresponsal()) {
                    this.btn_4 = Boolean.FALSE;
                }
                servletSession.addParametro("download", true);
                servletSession.setNombreDocumento(zipSPI);
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
                break;
            case 2:
                servletSession.setNombreDocumento(archivoProvee.getAbsolutePath());
                servletSession.setContentType("text/plain");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
                break;
            case 3:
                // Para generar el pdf
                servletSession.addParametro("id_transferencia", transferencias.getId());
                servletSession.addParametro("num_control", formatoNumControl((String) this.userSession.getVarTemp()));// Optiene el hash del archivo md5F
                servletSession.setNombreReporte("transferenciasDetalle");
                servletSession.setNombreSubCarpeta("transferencias");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                break;
            case 4:
                String detalle = "Por medio de la presente, solicito a usted se sirva al Banco Central del Ecuador el detalle de los pagos contenidos"
                        + " en el medio magnético que adjunto y que deberá ejecutarse el " + Utils.dateFormatPattern("dd/MM/yyyy", transferencias.getFechaAfectacion())
                        + " a través del Sistema de Pagos Interbancarios SPI, con los recursos que el/la " + transferencias.getNombreInstitucion()
                        + " mantiene en la cuenta corriente No. " + transferencias.getCtaCteBceIp() + " en el  Banco Central del Ecuador.";
                String piePagina = "Suscribimos este pedido en calidad de firma(s) autorizada(s) de la cuenta rotativa de pagos"
                        + " No. " + transferencias.getCtaCteBceIp() + " que el/la " + transferencias.getNombreInstitucion() + " mantiene en el " + transferencias.getNombreCorresponsal();
                servletSession.addParametro("id_transferencia", transferencias.getId());
                servletSession.addParametro("num_control", formatoNumControl((String) this.userSession.getVarTemp()));// Optiene el hash del archivo md5
                servletSession.addParametro("detalle", detalle);
                servletSession.addParametro("piePagina", piePagina);
                servletSession.setNombreReporte("transferenciasVentanilla");
                servletSession.setNombreSubCarpeta("transferencias");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                break;
            default:
                PrimeFaces.current().executeScript("PF('menuArchivosDlg').hide()");
                vaciarFormulario("CANCELAR");
                break;
        }
        enabledIniciar = Boolean.TRUE;
        PrimeFaces.current().ajax().update("menuArchivosForm");
        PrimeFaces.current().ajax().update("formMain");
    }

    public String formatoNumControl(String cadena) {
        int contador = 0;
        String codigo = "";
        while (contador < cadena.length()) {
            codigo = codigo.concat(cadena.substring(contador, contador + 4));
            contador += 4;
            if (contador != cadena.length()) {
                codigo = codigo.concat(" - ");
            }
        }
        return codigo;
    }

    public void uploadDocGeneral(Transferencias tranferencia) {
        this.transferencias = tranferencia;
        files = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('comprobanteSPIDialog_2').show()");
        PrimeFaces.current().ajax().update("comprobanteSPIForm_2");
    }

    public void subirDocGeneral(FileUploadEvent event) {
        files.add(event.getFile());
        guardarDatos();
        PrimeFaces.current().executeScript("PF('comprobanteSPIDialog_2').hide()");
    }

    public void guardarDatos() {
        List<DetalleTransferencias> temp = viewDetalle(transferencias);
        progress2 = 0;
        Integer aux = temp.size();
        Integer aux_1 = 100;
        Integer count = 1;
        for (DetalleTransferencias item : temp) {
            if (files != null) {
                progress2 = (count * aux_1) / aux;
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                }
                uploadDoc.upload(item, files);
                item.setFechaAcreditacion(fechaAcreditacion);
                item.setEstado("ACREDITADO");
                detalleTransferenciasService.edit(item);
                /*EDITAMOS LA INFORMACION DEL DETALLE DE COMPROBANTE RELACIOANDO*/
                BeneficiarioComprobantePago beneficiarioComprobantePago = beneficiarioComprobantePagoService.getBeneficiarioComprobantePago(item.getComprobantePago(), item.getIdentificacion().substring(0, 10));
                beneficiarioComprobantePago.setEstadoBeneficiario("ACREDITADO");
                beneficiarioComprobantePagoService.edit(beneficiarioComprobantePago);
                /*EDITAMOS LA INFORMACION DEL COMPROBANTES DE PAGO */
                ComprobantePago comprobanteP = item.getComprobantePago();
                comprobanteP.setEstado("TRANSFERENCIA ACREDITADA");
                comprobanteP.setUsuarioModificacion(userSession.getNameUser());
                comprobanteP.setFechaModificacion(new Date());
                comprobantePagoService.edit(comprobanteP);
                detalleTransferencia = item;
                datosNotificacion(detalleTransferencia.getIdentificacion());
                count += 1;
            }
        }
        transferencias.setEstadoTransferencia("ACREDITADO");
        transferencias.setFechaAcreditacion(fechaAcreditacion);
        transferencias.setUsuarioModificacion(userSession.getNameUser());
        transferencias.setFechaModificacion(new Date());
        transferenciasService.edit(transferencias);
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
        PrimeFaces.current().ajax().update("comprobanteSPIForm");
    }

    public void handleFileUploadComprobanteSPI(FileUploadEvent event) {
        try {
            files.add(event.getFile());
            if (files != null) {
                uploadDoc.upload(this.detalleTransferencia, files);
                JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
            }
            PrimeFaces.current().executeScript("PF('comprobanteSPIDialog').hide()");
            PrimeFaces.current().ajax().update("idtabView:transferenciaTable");
            addArchivo();
        } catch (Exception e) {
        }
    }

    public void addArchivo() {
        try {
            Transferencias transferencia = detalleTransferencia.getTransferencia();
            /*EDITAMOS LA INFORMACION DE LOS DETALLE DE LA TRANSFERENCIA*/
            detalleTransferencia.setEstado("ACREDITADO");
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
            transferencia.setFechaAcreditacion(detalleTransferencia.getFechaAcreditacion());
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
            PrimeFaces.current().ajax().update("idtabView:transferenciaTable");
            datosNotificacion(detalleTransferencia.getIdentificacion());
        } catch (Exception e) {
        }
    }

    public void datosNotificacion(String identificacion) {
        String mensaje;
        Cliente c = clienteService.getFindCliente(identificacion);
        String asunto = getTramite().getTipoTramite().getDescripcion().toUpperCase();
        switch (getTramite().getTipoTramite().getAbreviatura()) {
            case "PPS_profesionales":
            case "PAG_DEC":
                mensaje = mensajeEmail("SR(@)", "POR MEDIO DE LA PRESENTE SE LE NOTIFICA QUE EL " + getTramite().getTipoTramite().getDescripcion().toUpperCase() + " A SIDO ACREDITADO CON ÉXITO", c.getNombreCompleto());
                getParamts().put("aprobado", 1);
                notificar(asunto, c.getEmail(), mensaje, c.getNombreCompleto());
                break;
            case "PAGORMU":
                mensaje = mensajeEmail("Sr(a)", "POR MEDIO DE LA PRESENTE SE LE IN  FORMA QUE SU SOLICITUD DE ANTICIPO DE REMUNERACIÓN AH SIDO ACREDITADA EN SU CUENTA CORRESPONDIENTE", anticipo.getServidor().getPersona().getNombreCompleto());
                anticipo.setEstadoAnticipo(pagado);
                anticipoService.edit(anticipo);
                getParamts().put("aprobado", 1);
                notificar(asunto, anticipo.getServidor().getEmailInstitucion(), mensaje, anticipo.getServidor().getPersona().getNombreCompleto());
                break;
            default:
                mensaje = mensajeEmail("SR(@)", "POR MEDIO DE LA PRESENTE SE LE NOTIFICA QUE EL " + getTramite().getTipoTramite().getDescripcion().toUpperCase() + " A SIDO ACREDITADO CON ÉXITO", c.getNombreCompleto());
                getParamts().put("aprobado", 1);
                notificar(asunto, c.getEmail(), mensaje, c.getNombreCompleto());
                break;
        }
    }

    public String mensajeEmail(String sigla, String mensaje, String beneficiario) {
        return "<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;\">" + sigla + ". " + beneficiario
                + "\n" + mensaje
                + " SEGÚN EL NUMERO DE TRAMITE N° " + tramite.getNumTramite() + " </p>\n"
                + "</body>\n"
                + "</html>";
    }

    public void anularTransferenciaTotal(Transferencias transferencias) {
        System.out.println("anulacion total");
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
            anularComprobante(comprobante, transferencias);
        }
        caseCompletarTarea();
    }

    private void caseCompletarTarea() {
        try {
            getParamts().put("aprobado", 0);
            getParamts().put("usuarioPago", clienteService.getrolsUser(RolUsuario.autorizacionPago));
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
        PrimeFaces.current().ajax().update("idtabView:transferenciaTable");
    }

    public void anularDetalleTransferencia(DetalleTransferencias detalleTransferencia) {
        /*GUARDA LA INFORMACION DEL USUARIO QUE LO MODIFICO*/
        Transferencias transferencia = detalleTransferencia.getTransferencia();
        if (!transferencia.getEstadoTransferencia().equals("ACREDITADO") && !transferencia.getEstadoTransferencia().equals("ACREDITADO-PARCIAL")) {
            transferencia.setEstadoTransferencia("ANULADO - PARCIAL");
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
        Servidor RolContador = clienteService.getResponsableTransferencia(RolUsuario.contador);
        String mensaje = mensajeEmail("SR(@)", "POR MEDIO DE LA PRESENTE SE LE NOTIFICA QUE LA TRANFERENCIA DEL " + getTramite().getTipoTramite().getDescripcion().toUpperCase() + " DEL Sr(@)." + detalleTransferencia.getNombreBeneficiario() + " CON C.I. " + detalleTransferencia.getIdentificacion() + ", A SIDO ANULADO", RolContador.getPersona().getNombreCompleto());
        notificar_2("ANULACIÓN DE TRANSFERENCIA", RolContador.getPersona().getEmail(), mensaje);
        determinarValorBtnCompletarTarea(detalleTransferencia.getTransferencia());
        PrimeFaces.current().ajax().update("idtabView:transferenciaTable");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void notificar(String asunto, String email, String mensaje, String beneficiario) {
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje(mensaje);
        c.setArchivos(null);
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + email + " relacionada con: " + beneficiario);
        List<DetalleTransferencias> detalleTrasnf = detalleTransferenciasService.getDetallesTransferencia(detalleTransferencia.getTransferencia());
        if (detalleTrasnf.size() > 1) {
            determinarValorBtnCompletarTarea(detalleTransferencia.getTransferencia());
        } else {
            completarTarea(false);
        }
        detalleTransferencia = new DetalleTransferencias();
        PrimeFaces.current().ajax().update("formMain");
    }

    public Boolean verificarTamanio(DetalleTransferencias detalleSeleccionado) {
        Boolean resultado = Boolean.FALSE;
        if (detalleSeleccionado != null) {
            List<DetalleTransferencias> detalleList = detalleTransferenciasService.getDetallesTransferencia(detalleSeleccionado.getTransferencia());
            if (detalleList.size() > 1) {
                resultado = Boolean.TRUE;
            }
        }
        return resultado;
    }

    public void completarTarea(Boolean accion) {
        getParamts().put("aprobado", 1);
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public void notificar_2(String asunto, String email, String mensaje) {
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje(mensaje);
        c.setArchivos(null);
        katalinaService.enviarCorreo(c);
    }

    public void verDocumentos(DetalleTransferencias detalle) {
        this.detalleTransferencia = detalle;
        PrimeFaces.current().executeScript("PF('viewDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
    }

    public void anularComprobante(ComprobantePago comprobante, Transferencias transferencia) {
        double totalDebe = 0;
        double totalHaber = 0;
        /*TRAEMOS LOS DETALLES DEL COMPROBANTE DE PAGO*/
        List<DetalleComprobantePago> detalleComprobantePagoList = detalleComprobantePagoService.getDetalleComprobantePago(comprobante);
        /*BUSCAMOS LA ULTIMA TRANSACCION EN EL LIBRO DIARIO*/
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        DiarioGeneral diarioGeneralAnulacion = new DiarioGeneral();
        if (ultimaActa != null) {
            diarioGeneralAnulacion.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            diarioGeneralAnulacion.setNumeroTransaccion(BigInteger.valueOf(1));
        }
        /*CREAMOS UNA TRANSACCION EN EL LIBRO DIARIO*/
        if (comprobante.getDiarioGeneral() != null) {
            diarioGeneralAnulacion.setRetenido(comprobante.getDiarioGeneral().getRetenido());
            if (comprobante.getDiarioGeneral().getVariosBeneficiarios() != null) {
                diarioGeneralAnulacion.setVariosBeneficiarios(comprobante.getDiarioGeneral().getVariosBeneficiarios());
            }
            if (comprobante.getDiarioGeneral().getBeneficiario() != null) {
                diarioGeneralAnulacion.setBeneficiario(comprobante.getDiarioGeneral().getBeneficiario());
            }
            if (comprobante.getDiarioGeneral().getTipoBeneficiario() != null) {
                diarioGeneralAnulacion.setTipoBeneficiario(comprobante.getDiarioGeneral().getTipoBeneficiario());
            }
            if (comprobante.getDiarioGeneral().getEnlace() != null) {
                diarioGeneralAnulacion.setEnlace(comprobante.getDiarioGeneral().getEnlace());
            }
            if (comprobante.getDiarioGeneral().getCertificacionesPresupuestario() != null) {
                diarioGeneralAnulacion.setCertificacionesPresupuestario(comprobante.getDiarioGeneral().getCertificacionesPresupuestario());
            }
            diarioGeneralAnulacion.setComprobantePago(Boolean.TRUE);
            /*ACTUALIZAMOS EL ESTADO DE COMPROBANTE DEL DIARIO GENERAL PARA QUE VUELVA A ESTAR ACTIVO Y PODER REGISTRARLO*/
            comprobante.getDiarioGeneral().setComprobantePago(Boolean.FALSE);
            diarioGeneralService.edit(comprobante.getDiarioGeneral());
        } else {
            diarioGeneralAnulacion.setRetenido(Boolean.FALSE);
        }
        diarioGeneralAnulacion.setNumTramite(transferencia.getNumeroTramite().longValue());
        diarioGeneralAnulacion.setObservacion("ANULACIÓN CP No." + comprobante.getNumComprobante() + ", " + comprobante.getDetalle());
        diarioGeneralAnulacion.setUsuarioCreacion(userSession.getNameUser());
        diarioGeneralAnulacion.setFechaCreacion(new Date());
        diarioGeneralAnulacion.setEstado(Boolean.TRUE);
        diarioGeneralAnulacion.setPeriodo(opcionBusqueda.getAnio());
        diarioGeneralAnulacion.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
        diarioGeneralAnulacion.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneralAnulacion.setEstadoTransaccion("CUADRADO");
        diarioGeneralAnulacion.setFechaElaboracion(new Date());
        diarioGeneralAnulacion.setEstadoDiario("REGISTRADO");
        diarioGeneralAnulacion = diarioGeneralService.create(diarioGeneralAnulacion);
        /*CREAMOS LOS DETALLES DE LA TRANSACCION*/
        int contador = 0;
        for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
            DetalleTransaccion detalleTransaccion = new DetalleTransaccion();
            detalleTransaccion.setDiarioGeneral(diarioGeneralAnulacion);
            contador = contador + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            detalleTransaccion.setContador(bigInteger);
            detalleTransaccion.setCuentaContable(detalleComprobante.getCuentaContable());
            if (detalleComprobante.getDebe() != null) {
                double debe = detalleComprobante.getDebe().doubleValue() * (-1);
                detalleTransaccion.setDebe(new BigDecimal(debe));
            }
            if (detalleComprobante.getHaber() != null) {
                double haber = detalleComprobante.getHaber().doubleValue() * (-1);
                detalleTransaccion.setHaber(new BigDecimal(haber));
            }
            if (detalleComprobante.getTipoPago() != null) {
                detalleTransaccion.setTipoTransaccion(detalleComprobante.getTipoPago());
            }
            if (detalleComprobante.getEjecutado() != null) {
                double ejecutado = detalleComprobante.getEjecutado().doubleValue() * (-1);
                detalleTransaccion.setEjecutado(new BigDecimal(ejecutado));
            } else {
                detalleTransaccion.setEjecutado(BigDecimal.ZERO);
            }
            if (detalleComprobante.getPartidaPresupuestaria() != null) {
                detalleTransaccion.setPartidaPresupuestaria(detalleComprobante.getPartidaPresupuestaria());
            }
            if (detalleComprobante.getEstructuraProgramatica() != null) {
                detalleTransaccion.setEstructuraProgramatica(detalleComprobante.getEstructuraProgramatica());
            }
            if (detalleComprobante.getFuente() != null) {
                detalleTransaccion.setFuente(detalleComprobante.getFuente());
            }
            if (detalleComprobante.getCedulaPresupuestaria() != null) {
                detalleTransaccion.setCedulaPresupuestaria(detalleComprobante.getCedulaPresupuestaria());
            }
            detalleTransaccion.setDevengado(BigDecimal.ZERO);
            detalleTransaccion.setComprometido(BigDecimal.ZERO);
            if (detalleTransaccion.getDebe() != null) {
                totalDebe = Math.round((totalDebe + detalleTransaccion.getDebe().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
            if (detalleTransaccion.getHaber() != null) {
                totalHaber = Math.round((totalHaber + detalleTransaccion.getHaber().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
            detalleTransaccionService.create(detalleTransaccion);
        }
        /*ACTUALIZAMOS EL TOTAL DE LOS DEBES Y HABER DEL DETALLE DEL LIBRO DIARIO*/
        diarioGeneralAnulacion.setTotalDebe(new BigDecimal(totalDebe));
        diarioGeneralAnulacion.setTotalHaber(new BigDecimal(totalHaber));
        diarioGeneralService.edit(diarioGeneralAnulacion);
        /*IMPRIMIMOS EL REPORTE DEL COMPROBANTE DE PAGO*/
        imprimirReporteDiarioGeneral(diarioGeneralAnulacion);
    }

    public void imprimirReporteDiarioGeneral(DiarioGeneral diarioGeneral) {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void anularProcesoTransferencia() {
        ComprobantePago buscandoComprobantePago = transferenciasService.getComprobantePago(BigInteger.valueOf(tramite.getNumTramite()));
        List<BeneficiarioComprobantePago> beneficiarioCP = beneficiarioComprobantePagoService.getBeneficiarioComprobante(buscandoComprobantePago);
        for (BeneficiarioComprobantePago beneficiario : beneficiarioCP) {
            beneficiario.setEstadoBeneficiario("TRANSFERENCIA ANULADA");
            beneficiario.setValor(new BigDecimal(beneficiario.getValor().doubleValue() * (-1)));
            beneficiarioComprobantePagoService.edit(beneficiario);
        }
        buscandoComprobantePago.setEstado("TRANSFERENCIA ANULADA");
        buscandoComprobantePago.setUsuarioModificacion(userSession.getNameUser());
        buscandoComprobantePago.setFechaModificacion(new Date());
        comprobantePagoService.edit(buscandoComprobantePago);
        Transferencias tranferenciaTemp = new Transferencias(numTramite);
        anularComprobante(buscandoComprobantePago, tranferenciaTemp);
        caseCompletarTarea();
    }

    public void closeObservacionDlg() {
        if (accionAnular) {
            anularTransferenciaTotal(transferencias);
        } else {
            anularProcesoTransferencia();
        }
    }

    public void actualizarTabla() {
        if (detalleTrasferencias != null) {
            if (!detalleTrasferencias.isEmpty()) {
                for (DetalleTransferencias detalleT : detalleTrasferencias) {
                    if (conceptoSeleccionado != null) {
                        detalleT.setConcepto(conceptoSeleccionado.getCodigo());
                    } else {
                        detalleT.setConcepto(null);
                    }
                }
            }
        }
    }

    public void verTransferencia(Transferencias t) {
        this.transferencias = t;
        DetalleTransferencias d = new DetalleTransferencias();
        d.setTransferencia(this.transferencias);
        this.detalleTrasferencias = detalleTransferenciasService.getDetallesTransferencia(transferencias);
        this.ocultarTabla = Boolean.FALSE;
        this.nuevo = false;
        int cont = ultimoBeneficiarioComprobanteAnterior();
        System.out.println("COMPRIOBANTE: " + comprobantePago);
        PrimeFaces.current().ajax().update("idtabView:mantenimientoTransferencia");
        PrimeFaces.current().ajax().update("idtabView:formularioTransferencia");
        PrimeFaces.current().ajax().update("formMain");
    }

    private int ultimoBeneficiarioComprobanteAnterior() {
        int contador = 0;
        ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(opcionBusqueda.getAnio());
        if (ultimoComprobantePago != null) {
            BeneficiarioComprobantePago beneficiarioComprobante = detalleComprobantePagoService.getUltimoNumeroReferencia(ultimoComprobantePago);
            if (beneficiarioComprobante != null) {
                contador = beneficiarioComprobante.getNumeroTransferencia().intValue();
            }
        }
        int auxContador = comprobantePagoService.getUltimaTransferencia(opcionBusqueda.getAnio());
        if (contador >= auxContador) {
            return contador;
        } else {
            return auxContador;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public CatalogoItem getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(CatalogoItem conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    public List<CatalogoItem> getConceptoTransferencia() {
        return conceptoTransferencia;
    }

    public void setConceptoTransferencia(List<CatalogoItem> conceptoTransferencia) {
        this.conceptoTransferencia = conceptoTransferencia;
    }

    public Boolean getBtn_1() {
        return btn_1;
    }

    public void setBtn_1(Boolean btn_1) {
        this.btn_1 = btn_1;
    }

    public Boolean getBtn_2() {
        return btn_2;
    }

    public void setBtn_2(Boolean btn_2) {
        this.btn_2 = btn_2;
    }

    public Boolean getBtn_3() {
        return btn_3;
    }

    public void setBtn_3(Boolean btn_3) {
        this.btn_3 = btn_3;
    }

    public Boolean getBtn_4() {
        return btn_4;
    }

    public void setBtn_4(Boolean btn_4) {
        this.btn_4 = btn_4;
    }

    public Boolean getBtnCompletarTarea() {
        return btnCompletarTarea;
    }

    public void setBtnCompletarTarea(Boolean btnCompletarTarea) {
        this.btnCompletarTarea = btnCompletarTarea;
    }

    public Transferencias getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(Transferencias transferencias) {
        this.transferencias = transferencias;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public boolean isEnabledIniciar() {
        return enabledIniciar;
    }

    public void setEnabledIniciar(boolean enabledIniciar) {
        this.enabledIniciar = enabledIniciar;
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

    public BeneficiarioComprobantePago getComprobanteTemporal() {
        return comprobanteTemporal;
    }

    public void setComprobanteTemporal(BeneficiarioComprobantePago comprobanteTemporal) {
        this.comprobanteTemporal = comprobanteTemporal;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Integer getProgress2() {
        return progress2;
    }

    public void setProgress2(Integer progress2) {
        this.progress2 = progress2;
    }
//</editor-fold>
}
