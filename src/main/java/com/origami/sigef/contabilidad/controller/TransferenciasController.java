/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Transferencias;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DetalleTransferenciasService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.TransferenciasService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
 * @author Criss Intriago
 */
@Named(value = "transferenciasView")
@ViewScoped
public class TransferenciasController implements Serializable {

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
    private CatalogoService catalogoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private DetalleComprobantePagoService detalleComprobantePagoService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private ValoresService valoresService;

    private Transferencias transferencias;
    private ComprobantePago comprobantePagoBusqueda;
    private OpcionBusqueda opcionBusqueda;
    private DetalleTransferencias detalleTransferencia;
    private Boolean ocultarTabla;
    private Boolean camposCorresponsal;
    private LazyModel<Transferencias> transferenciasLazyModel;
    private LazyModel<ComprobantePago> comprobantePagoLazyModel;
    private List<Servidor> responsablesSeleccionados;
    private List<DetalleTransferencias> detalleTrasferencias;
    private List<ComprobantePago> comprobantePagosList;
    private List<UploadedFile> files;
    private List<CatalogoItem> conceptoTransferencia;
    private Boolean nuevo = true;
    private String zipSPI;
    private File archivoProvee;
    private boolean tipoResponsable;
    private List<Servidor> servidoresList;
    private Short periodo;
    private List<MasterCatalogo> listaPeriodo;
    private Boolean activarAcciones;
    private CatalogoItem conceptoSeleccionado;
    private Date fechaAcreditacion;
    private Integer progress2;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.comprobantePagoLazyModel = new LazyModel<>(ComprobantePago.class);
        this.comprobantePagoLazyModel.getSorteds().put("id", "ASC");
        this.comprobantePagoLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        this.comprobantePagoLazyModel.getFilterss().put("estado", "REGISTRADO");
        this.detalleTransferencia = new DetalleTransferencias();
        this.conceptoTransferencia = catalogoService.getItemsByCatalogo("concepto_transferencia");
        this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        this.periodo = opcionBusqueda.getAnio();
        cargarRegistros();
        vaciarFormulario("CONSTRUCTOR");
    }

    public void form(Transferencias transferencia) {
        if (periodo != null) {
            if (periodo.equals(opcionBusqueda.getAnio())) {
                if (transferencia != null) {
                    this.transferencias = transferencia;
                } else {
                    this.transferencias.setFechaAfectacion(new Date());
                    this.transferencias.setPeriodo(opcionBusqueda.getAnio());
                    transferencias.setResponsable1(clienteService.getResponsableTransferencia(RolUsuario.maximaAutoridad));
                    transferencias.setResponsable2(clienteService.getResponsableTransferencia(RolUsuario.tesorero));
                    conceptoSeleccionado = null;
                }
                this.ocultarTabla = Boolean.FALSE;
                PrimeFaces.current().ajax().update("mantenimientoTransferencia");
                PrimeFaces.current().ajax().update("formularioTransferencia");
                PrimeFaces.current().ajax().update("formMain");
            } else {
                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
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

    public void cargarRegistros() {
        if (periodo != null) {
            this.transferenciasLazyModel = new LazyModel<>(Transferencias.class);
            this.transferenciasLazyModel.getSorteds().put("id", "ASC");
            this.transferenciasLazyModel.getFilterss().put("periodo", this.periodo);
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
    }

    public void buscarResponsables(Boolean tipoResponsable) {
        this.tipoResponsable = tipoResponsable;
        if (tipoResponsable) {
            servidoresList = clienteService.getListServidoresRol(RolUsuario.maximaAutoridad);
        } else {
            servidoresList = clienteService.getListServidoresRol(RolUsuario.tesorero);
        }
        PrimeFaces.current().executeScript("PF('servidoresDlg').show()");
    }

    public void servidorSeleccionado(Servidor servidor) {
        if (tipoResponsable) {
            transferencias.setResponsable1(servidor);
        } else {
            transferencias.setResponsable2(servidor);
        }
        PrimeFaces.current().executeScript("PF('servidoresDlg').hide()");
        PrimeFaces.current().ajax().update("responsablesGrid");
        PrimeFaces.current().ajax().update("responsablesGrid2");
    }

    public void onDateSelect() {
        PrimeFaces.current().ajax().update("gridMes");
    }

    public void verTransferencia(Transferencias t) {
        this.transferencias = t;
        DetalleTransferencias d = new DetalleTransferencias();
        d.setTransferencia(this.transferencias);
        this.detalleTrasferencias = detalleTransferenciasService.getDetallesTransferencia(transferencias);
        this.ocultarTabla = Boolean.FALSE;
        PrimeFaces.current().ajax().update("mantenimientoTransferencia");
        PrimeFaces.current().ajax().update("formularioTransferencia");
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
                    JsfUtil.addErrorMessage("DETALLE TRANSFERENCIA", "Debe seleccionar el concepto");
                    return;
                }
            }
        } else {
            generarArchivo();
        }
        PrimeFaces.current().executeScript("PF('menuArchivosDlg').show()");
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
                activarAcciones = Boolean.FALSE;
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
        PrimeFaces.current().ajax().update("menuArchivosForm");
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

    public void generarArchivo() {
        try {
            // Variable para el proceso de generacion de SPI-SP.md5
            StringBuffer data = new StringBuffer();
            StringBuffer archivoProveedor = new StringBuffer();
            Integer sumCuenta = 0;
//            int contador = ultimoBeneficiarioComprobanteAnterior() + 1;
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

    private int ultimoBeneficiarioComprobanteAnterior() {
        int contador = 0;
        ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(periodo);
        if (ultimoComprobantePago != null) {
            BeneficiarioComprobantePago beneficiarioComprobante = detalleComprobantePagoService.getUltimoNumeroReferencia(ultimoComprobantePago);
            if (beneficiarioComprobante != null) {
                contador = beneficiarioComprobante.getNumeroTransferencia().intValue();
            }
        }
        int auxContador = comprobantePagoService.getUltimaTransferencia(periodo);
        if (contador >= auxContador) {
            System.out.println("1. " + contador);
            return contador;
        } else {
            System.out.println("2. " + auxContador);
            return auxContador;
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
        this.activarAcciones = Boolean.TRUE;
        this.nuevo = true;
        this.userSession.setVarTemp(null); // UNA VES RECUPERADA LA VARIABLE BORRAR LE VARIABLE TEMPORAL
        if (accion.equals("CANCELAR")) {
            PrimeFaces.current().ajax().update("mantenimientoTransferencia");
            PrimeFaces.current().ajax().update("formularioTransferencia");
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
        PrimeFaces.current().ajax().update("corresponsalGrid");
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
        PrimeFaces.current().ajax().update("formularioTransferencia");
        PrimeFaces.current().ajax().update("detalleTransferenciaTable");
        PrimeFaces.current().ajax().update("formMain");
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
                    detalle.setTipoCuenta(String.valueOf(beneficiario.getDetalleBanco().getTipoCuenta().getOrden()));
                    detalle.setValor(beneficiario.getValor());
                    if (beneficiario.getComprobantePago().getDetalle().length() > 60) {
                        detalle.setDetalle(beneficiario.getComprobantePago().getDetalle().substring(0, 60));
                    } else {
                        detalle.setDetalle(beneficiario.getComprobantePago().getDetalle());
                    }
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
                if (comprobanteP.getCuentaBancaria() != null) {
                    transferencias.setCtaCteBceIp(comprobanteP.getCuentaBancaria().getNumeroCuenta());
                } else {
                    JsfUtil.addWarningMessage("AVISO!!", "");
                    vaciarFormulario("CANCELAR");
                    return;
                }
                transferencias.setNombreInstitucion(comprobanteP.getCuentaBancaria().getNombreCuentaBancaria());
                JsfUtil.addInformationMessage("Detalle de la Transferencia", "Se han cargado datos correctamente");
                PrimeFaces.current().ajax().update("detalleTransferenciaTable");
                PrimeFaces.current().ajax().update("gridDescripcion");
                PrimeFaces.current().ajax().update("formMain");
                PrimeFaces.current().executeScript("PF('comprobantePagoDLG').hide()");
            } else {
                JsfUtil.addWarningMessage("AVISO", "No hay beneficiarios relacionados a este comprobante de pago");
            }
        }
    }

    /*VIZUALIZACION DE LOS DETALLES DE LA TRANSFERENCIA*/
    public List<DetalleTransferencias> viewDetalle(Transferencias transferencias) {
        List<DetalleTransferencias> detalleTransferenciasList = detalleTransferenciasService.getDetallesTransferencia(transferencias);
        return detalleTransferenciasList;
    }

    /*ACCIONES DE ANULACION, ACREDITACION*/
    public void subirArchivo(DetalleTransferencias detalle) {
        if (periodo != null) {
            if (periodo.equals(opcionBusqueda.getAnio())) {
                this.detalleTransferencia = detalle;
                files = new ArrayList<>();
                PrimeFaces.current().ajax().update("comprobanteSPIForm");
                PrimeFaces.current().executeScript("PF('comprobanteSPIDialog').show()");
            } else {
                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
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
        if (periodo != null) {
            if (periodo.equals(opcionBusqueda.getAnio())) {
                if (files != null) {
                    uploadDoc.upload(this.detalleTransferencia, files);
                    JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
                }
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
                this.detalleTransferencia = new DetalleTransferencias();
                PrimeFaces.current().ajax().update("transferenciaTable");
            } else {
                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
    }

    public void anularTransferenciaTotal(Transferencias transferencias) {
        if (periodo != null) {
            if (periodo.equals(opcionBusqueda.getAnio())) {
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
                        beneficiarioComprobantePagoService.edit(beneficiario);
                    }
                    comprobante.setEstado("TRANSFERENCIA ANULADA");
                    comprobante.setUsuarioModificacion(userSession.getNameUser());
                    comprobante.setFechaModificacion(new Date());
                    comprobantePagoService.edit(comprobante);
                    anularComprobante(comprobante, transferencias);
                }
                PrimeFaces.current().ajax().update("transferenciaTable");
            } else {
                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
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
        if (transferencia.getNumeroTramite() != null) {
            diarioGeneralAnulacion.setNumTramite(transferencia.getNumeroTramite().longValue());
        }
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
        /*Habilitamos el diarioGeneral para que se muestre nuevamente en el listado de las transacciones en el comprobante de pago*/
        comprobante.getDiarioGeneral().setComprobantePago(Boolean.FALSE);
        diarioGeneralService.edit(comprobante.getDiarioGeneral());
        /*IMPRIMIMOS EL REPORTE DEL COMPROBANTE DE PAGO*/
        imprimirReporteDiarioGeneral(diarioGeneralAnulacion);
    }

    public void imprimirReporteDiarioGeneral(DiarioGeneral diarioGeneral) {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void anularDetalleTransferencia(DetalleTransferencias detalleTransferencia) {
        if (periodo != null) {
            if (periodo.equals(opcionBusqueda.getAnio())) {
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
                String mensaje = mensajeEmail("SR(@)", "POR MEDIO DE LA PRESENTE SE LE NOTIFICA QUE LA TRANFERENCIA CON NO. " + detalleTransferencia.getTransferencia().getNumReferencia() + " DEL Sr(@)." + detalleTransferencia.getNombreBeneficiario() + " CON C.I. " + detalleTransferencia.getIdentificacion() + ", A SIDO ANULADO", RolContador.getPersona().getNombreCompleto());
                notificar_2("ANULACIÓN DE TRANSFERENCIA", RolContador.getPersona().getEmail(), mensaje);
                PrimeFaces.current().ajax().update("transferenciaTable");
            } else {
                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
    }

    private String mensajeEmail(String sigla, String mensaje, String beneficiario) {
        return "<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;\">" + sigla + ". " + beneficiario
                + "\n" + mensaje + " </p>\n"
                + "</body>\n"
                + "</html>";
    }

    private void notificar_2(String asunto, String email, String mensaje) {
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

    public CatalogoItem getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(CatalogoItem conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
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
        PrimeFaces.current().ajax().update("transferenciaTable");
        JsfUtil.addSuccessMessage("INFO!!", "Documento agregado correctamente");
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
                count += 1;
            }
        }
        transferencias.setEstadoTransferencia("ACREDITADO");
        transferencias.setFechaAcreditacion(fechaAcreditacion);
        transferencias.setUsuarioModificacion(userSession.getNameUser());
        transferencias.setFechaModificacion(new Date());
        transferenciasService.edit(transferencias);
    }

    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public Integer getProgress2() {
        return progress2;
    }

    public void setProgress2(Integer progress2) {
        this.progress2 = progress2;
    }

    public Boolean getActivarAcciones() {
        return activarAcciones;
    }

    public void setActivarAcciones(Boolean activarAcciones) {
        this.activarAcciones = activarAcciones;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public List<ComprobantePago> getComprobantePagosList() {
        return comprobantePagosList;
    }

    public void setComprobantePagosList(List<ComprobantePago> comprobantePagosList) {
        this.comprobantePagosList = comprobantePagosList;
    }

    public List<Servidor> getServidoresList() {
        return servidoresList;
    }

    public void setServidoresList(List<Servidor> servidoresList) {
        this.servidoresList = servidoresList;
    }

    public List<CatalogoItem> getConceptoTransferencia() {
        return conceptoTransferencia;
    }

    public void setConceptoTransferencia(List<CatalogoItem> conceptoTransferencia) {
        this.conceptoTransferencia = conceptoTransferencia;
    }

    public Transferencias getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(Transferencias transferencias) {
        this.transferencias = transferencias;
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

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }
//</editor-fold>

}
