/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.gestionTributaria.Commons.SisVars;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Font;
import com.origami.sigef.activos.lazy.ProveedorLazy;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.model.Imagenes;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import com.origami.sigef.ws.IrisService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "documentoReservaView")
@ViewScoped
public class DocumentoReservaCompromisoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReservaCompromisoService solicitudService;
    @Inject
    private DetalleReservaCompromisoService detalleSolicitudService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession user;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private PappProcesoService procesoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private UserSession us;
    @Inject
    private IrisService irisService;

    @Inject
    private FileUploadDoc uploadDoc;

    private UploadedFile uploadedFile;
    private boolean showOptionFirmar;
    private String clave;
    private Boolean existenImagenes;
    private List<Imagenes> imagenesPdfs;
    private FirmaElectronica firmaElectronica;

    private SolicitudReservaCompromiso solicitudReservaCompromiso;
    private DetalleSolicitudCompromiso detalleSolicitudReserva;
    private LazyModel<SolicitudReservaCompromiso> lazy;
    private List<DetalleSolicitudCompromiso> listaSolicitudes;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private Procedimiento procedimientoSeleccionado;
    private String fileName;
    private Cliente beneficiario;
    private boolean panelProveedor, panelServidor;
    private ProveedorLazy proveedorLazy;
    private ServidorLazy servidorLazy;
    private List<CatalogoItem> estadoFiltro;
    private List<UnidadAdministrativa> unidadFiltros;
    private String observaciones;
    private String email;
    private Cliente clienteNotifacacion;

    private static final Logger LOG = Logger.getLogger(DocumentoReservaCompromisoController.class.getName());
    public static final String DEST = "C:\\";

    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;
    private BigDecimal totalEjecutado;
    private BigDecimal totalLiquidado;
    private List<DetalleTransaccion> detalleAcumulado;

    //NUEVO
    boolean firmaAnulacion = false, firmaReserva = false, firmaLiquidacion = false, firmaLiberacion = false, firmaComprometida = false, firmaModificacion = false;
    private String rutaArchivoMostrat;
    private boolean firmaSolicutd;
    private Boolean ocultarDocumente;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
                this.beneficiario = new Cliente();
                this.detalleSolicitudReserva = new DetalleSolicitudCompromiso();
                this.lazy = new LazyModel(SolicitudReservaCompromiso.class);
                rutaArchivoMostrat = "";
                this.lazy.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                this.listaSolicitudes = new ArrayList<>();
                this.solicitudRequisitoList = new ArrayList<>();
                this.procedimientoRequisitoList = new ArrayList<>();
                this.procedimientoSeleccionado = new Procedimiento();
                panelProveedor = true;
                panelServidor = false;
                estadoFiltro = new ArrayList<>();
                estadoFiltro = catalogoService.MostarTodoCatalogo("estado_solicitud");
                unidadFiltros = new ArrayList<>();
                unidadFiltros = solicitudService.getListaUnidadesReservas();
                clienteNotifacacion = new Cliente();
                detalleAcumulado = new ArrayList<>();
                firmaSolicutd = false;
                ocultarDocumente = Boolean.FALSE;
                if (us.getFirmaElectronica() != null) {
                    showOptionFirmar = false;
                    firmaElectronica = us.getFirmaElectronica();
                    existenImagenes = Boolean.FALSE;
                    imagenesPdfs = new ArrayList<>();
                    firmaElectronica.setMotivo("Firmado Electrónicamente por FirmaEC");
                    firmaElectronica.setUrlQr("");
                } else {

                    JsfUtil.addWarningMessage("ADVERTENCIA", "AGREGUE FIRMA ELECTRONICA AL SISTEMA PARA PODER UTILIZAR ESTÁ OPCIÓN");
                }

                if (tarea.getTaskDefinitionKey().equals("documentoReservaLegalizacion")) {
                    firmaSolicutd = true;
                }

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
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

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:dataaprobacion");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formMain:dataaprobacion");
        }
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();
        return a;
    }

    private void cargarDatosRegistrados(SolicitudReservaCompromiso reservaCompromiso) {
        this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.solicitudReservaCompromiso = reservaCompromiso;
        this.procedimientoSeleccionado = reservaCompromiso.getProcedimiento();
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(reservaCompromiso);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((reserva) -> {
                solicitudRequisitoList.add(reserva);
            });
        }
        this.listaSolicitudes = solicitudService.getListaDetlleSolciitud(reservaCompromiso);
    }

    public void visualizarSolicitudes(SolicitudReservaCompromiso reservaCompromiso) {
        cargarDatosRegistrados(reservaCompromiso);
        showEjecutadoReservasGlobal(reservaCompromiso);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('DlgoVisualizacionAprobacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionAprobacion");
    }

    public void aprobarSolicitud(boolean aprobar, SolicitudReservaCompromiso s) {

        BigDecimal valor = BigDecimal.ZERO;
        BigDecimal valor2 = BigDecimal.ZERO;
        BigDecimal valor3 = BigDecimal.ZERO;
        int result = 0;
        int result2 = 0;
        int result3 = 0;

        this.solicitudReservaCompromiso = s;
        this.solicitudReservaCompromiso.setComentario("");

        if (aprobar) {
            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("APRO", "estado_solicitud"));
            this.solicitudReservaCompromiso.setFechaAprobacion(new Date());
            solicitudService.edit(solicitudReservaCompromiso);

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solcitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + " aprobada con éxito");

            short periodo = solicitudReservaCompromiso.getPeriodo();
            List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);
            valor = BigDecimal.ZERO;
            valor2 = BigDecimal.ZERO;
            valor3 = BigDecimal.ZERO;
            if (listP != null && !listP.isEmpty()) {
                for (Producto pr : listP) {

                    valor = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                    result = solicitudService.updateReservaProducto(valor, periodo, pr.getId());

                    if (result > 0) {

                    }
                }
            }
            List<ThCargoRubros> lis = solicitudService.listaReservasSinRepetir(periodo);
            if (lis != null && !lis.isEmpty()) {
                for (ThCargoRubros li : lis) {
                    valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
                    result2 = solicitudService.actualizarReservaPresupuesto(valor2, periodo, li.getId());

                    if (result2 > 0) {

                    }
                }
            }

            List<ProformaPresupuestoPlanificado> tmpPd = solicitudService.listaReservasPdSinRepetir(periodo);

            if (tmpPd != null && !tmpPd.isEmpty()) {
                for (ProformaPresupuestoPlanificado li : tmpPd) {
                    valor3 = solicitudService.sumaTotalDeReservasDevengadoPd(periodo, li.getId());
                    result3 = solicitudService.actualizarReservaPresupuestoPd(valor2, periodo, li.getId());

                    if (result3 > 0) {

                    }
                }
            }

        } else {
            PrimeFaces.current().executeScript("PF('DlgoObservacion').show()");
            PrimeFaces.current().ajax().update(":formObservacion");
        }

    }

    public void vizualizarPDF(ProcedimientoRequisito procedimientoRequisito) {
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                    setFileName("/resources/demo/media/" + pr.getUrl());
                    break;
                } else {
                    setFileName("");
                }
            }
            if (getFileName().equals("")) {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public void observacion() {
        BigDecimal valor = BigDecimal.ZERO;
        BigDecimal valor2 = BigDecimal.ZERO;
        BigDecimal valor3 = BigDecimal.ZERO;
        int result = 0;
        int result2 = 0;
        int result3 = 0;
        int resultado = 0;

        if (this.solicitudReservaCompromiso.getComentario().length() > 0) {

            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("RECHA", "estado_solicitud"));
            solicitudService.edit(solicitudReservaCompromiso);
            PrimeFaces.current().executeScript("PF('DlgoObservacion').hide()");
            PrimeFaces.current().ajax().update(":formObservacion");

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solcitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + "se ha notificada con extio");

            short periodo = solicitudReservaCompromiso.getPeriodo();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);

            for (Producto pr : listP) {

                valor = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                result = solicitudService.updateReservaProducto(valor, periodo, pr.getId());

                if (result > 0) {

                }
            }

            List<ThCargoRubros> lis = solicitudService.listaReservasSinRepetir(periodo);
            if (lis != null && !lis.isEmpty()) {
                for (ThCargoRubros li : lis) {
                    valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
                    result2 = solicitudService.actualizarReservaPresupuesto(valor2, periodo, li.getId());

                    if (result2 > 0) {

                    }
                }
            }

            List<ProformaPresupuestoPlanificado> tmpPd = solicitudService.listaReservasPdSinRepetir(periodo);
            if (tmpPd != null && !tmpPd.isEmpty()) {
                for (ProformaPresupuestoPlanificado li : tmpPd) {
                    valor3 = solicitudService.sumaTotalDeReservasDevengadoPd(periodo, li.getId());
                    result3 = solicitudService.actualizarReservaPresupuestoPd(valor2, periodo, li.getId());
                    if (result3 > 0) {

                    }
                }
            }

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "No deje los campos vacios");
        }

    }

    public void actualizarSolicitudDeReservaAprobada(SolicitudReservaCompromiso reservaCompromiso) {

        cargarDatosRegistrados(reservaCompromiso);
        this.solicitudReservaCompromiso.setFechaActualizacion(new Date());
        PrimeFaces.current().executeScript("PF('DlgoActualizacionReserva').show()");
        PrimeFaces.current().ajax().update(":formActualizacionReserva");

    }

    public void saveActualizacionSolicitud() {

        if (solicitudReservaCompromiso.getBeneficiario() != null) {
            if (detalleSolicitudReserva.getMontoComprometido().doubleValue() > 0) {

                for (DetalleSolicitudCompromiso data : listaSolicitudes) {
                    detalleSolicitudService.edit(data);
                }

                PrimeFaces.current().executeScript("PF('DlgoActualizacionReserva').hide()");
                JsfUtil.addSuccessMessage("INFORMACIÓN", "La solicitud de reserva de compromiso ha sido actualizada correctamente");
            } else {
                JsfUtil.addWarningMessage("INFORMACIÓN", "El monto comprometido debe ser mayor a $0,00");
            }
        } else {
            JsfUtil.addWarningMessage("INFORMACIÓN", "Añadir un beneficiario antes de guardar");
        }

    }

    public void vizualizarSolicitudActualizada(SolicitudReservaCompromiso reservaCompromiso) {

        cargarDatosRegistrados(reservaCompromiso);
        PrimeFaces.current().executeScript("PF('DlgoVisualizarActualizacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionActualizacion");
    }

    public void showEjecutadoReservasGlobal(SolicitudReservaCompromiso r) {

    }

    public void detallePartidasGlobal() {

    }

    public void CargarDatosBeneficiario() {
        if (solicitudReservaCompromiso.getTipoBeneficiario() != null) {
            //si reservaCompromiso.getTipoBeneficiario() es igual a TRUE entonces es PROVEEDOR
            if (solicitudReservaCompromiso.getTipoBeneficiario()) {
                panelProveedor = true;
                panelServidor = false;
                proveedorLazy = new ProveedorLazy();
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                servidorLazy = new ServidorLazy();
            }
        } else {
            PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        }
    }

    public void AñadirBeneficiarioServidor(Servidor servidor) {
        solicitudReservaCompromiso.setBeneficiario(servidor.getPersona());
        solicitudReservaCompromiso.setTipoBeneficiario(Boolean.FALSE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + solicitudReservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void Dloobservaciones(SolicitudReservaCompromiso s) {
        this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.solicitudReservaCompromiso = s;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void forEmail() {
        email = "";
        PrimeFaces.current().executeScript("PF('dlgEmail').show()");
        PrimeFaces.current().ajax().update(":frmDlgEmail");
    }

    public void completarTarea(SolicitudReservaCompromiso s) {
        try {

            if (tramite.getTipoTramite().getActivitykey().equals("liquidacionReservaCompromisoProcess") || tramite.getTipoTramite().getActivitykey().equals("liberacionReserva")) {
                clienteNotifacacion = new Cliente();
                String usuario = (String) this.getEngine().getvariable(this.session.getTaskID(), "usuario");
                System.out.println("Task: " + this.session.getTaskID() + "   : " + usuario);
                clienteNotifacacion = solicitudService.devuelveClienteNotitfacion2(usuario);
                if (clienteNotifacacion != null && clienteNotifacacion.getEmail() != null) {
                    enviarCorreo(clienteNotifacacion.getEmail(), "Solicitud Reserva Compromiso", formatoCodigo(s.getSecuencial()), clienteNotifacacion.getNombre() + " " + clienteNotifacacion.getApellido());
                }
            } else {
                getParamts().put("user_firma", clienteService.getrolsUser(RolUsuario.presupuesto));
            }
            super.completeTask((HashMap<String, Object>) getParamts());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("INFORMACIÓN", "LA TAREA SE HA COMPLETADO CORRECTAMENTE");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void enviarCorreo(String email, String asunto, String mensaje, String userStart) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;text-align: justify;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE SU RESERVA No." + mensaje + " HA SIDO APROBADO "
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        System.out.println("correo " + email);
        System.out.println("mensaje " + c.getMensaje());
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + email + " relacionada con: " + clienteNotifacacion.getNombre() + " " + clienteNotifacacion.getApellido());

    }

    public void insertarComprometido(DetalleSolicitudCompromiso detalle) {
        this.detalleSolicitudReserva = detalle;

        if (detalleSolicitudReserva.getMontoComprometido().doubleValue() > detalleSolicitudReserva.getMontoSolicitado().doubleValue()) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "El monto comprometido es mayor al monto solicitado");
            detalleSolicitudReserva.setMontoComprometido(BigDecimal.ZERO);
        }

    }

    public void AñadirBeneficiarioProveedor(Proveedor provedor) {
        solicitudReservaCompromiso.setBeneficiario(provedor.getCliente());
        solicitudReservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + solicitudReservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void armarDocumento(SolicitudReservaCompromiso s) {
        ss.borrarDatos();
        try {
            Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.financiero));
            Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.presupuesto));
            ss.addParametro("ci_financiero", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
            ss.addParametro("nombre_financiero", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
            ss.addParametro("ci_presupuesto", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
            ss.addParametro("nombre_presupuesto", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
            if ("liquidacionReservaCompromisoProcess".equals(tramite.getTipoTramite().getActivitykey()) || "liberacionReserva".equals(tramite.getTipoTramite().getActivitykey())) {
                ss.addParametro("reserva", s.getId());
                ss.setNombreReporte("certificacionLiquidacion");
                ss.setNombreSubCarpeta("CertificacionPresupuestaria");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else {
                ss.addParametro("id", s.getId());
                ss.addParametro("SUBREPORT_ENCABEZADO", JsfUtil.getRealPath("/reportes/"));
                ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
                ss.setNombreReporte("certificacionReservaCompromiso");
                ss.setNombreSubCarpeta("CertificacionPresupuestaria");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
        } catch (Exception e) {

        }
    }

    public void pdfManual(SolicitudReservaCompromiso s) throws IOException, DocumentException {

        List<DetalleSolicitudCompromiso> listaDetalle = solicitudService.getListaDetlleSolciitud(s);

//se crear el documento
        Document documento = new Document();
        //fichero donde creariamos
        // FileOutputStream ficheroPdf = new FileOutputStream("C:\\Users\\Public\\Downloads\reserva-" + s.getSecuencial() + ".pdf");
        OutputStream ficheroPdf = new FileOutputStream(new File("Test.pdf"));
        PdfWriter.getInstance(documento, ficheroPdf);
        documento.open();

        Paragraph titulo = new Paragraph("RESERVA DE COMPROMISO",
                FontFactory.getFont("arial", 14, Font.BOLD, BaseColor.BLACK)
        );

        Paragraph estado = new Paragraph("Estado: " + s.getEstado().getTexto());

        documento.add(titulo);
        documento.add(estado);

        //creamos la cabecera de la primera tabla
        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell("Partida");
        tabla.addCell("Item");
        tabla.addCell("Estructura Programatica");
        tabla.addCell("Monto Solicitado");

        //cuerpo de la primera tabla
        for (DetalleSolicitudCompromiso item : listaDetalle) {
            if (item.getActividadProducto() != null) {
                tabla.addCell(item.getActividadProducto().getCodigoPresupuestario());
                tabla.addCell(item.getActividadProducto().getItemPresupuestario().getDescripcion());
                tabla.addCell(item.getActividadProducto().getEstructuraProgramatica().getDescripcion());
                tabla.addCell(item.getMontoSolicitado().toString());
            } else {
                tabla.addCell(item.getPresupuesto().getPartida());
                tabla.addCell(item.getPresupuesto().getItem().getDescripcion());
                tabla.addCell(item.getPresupuesto().getEstructura().getDescripcion());
                tabla.addCell(item.getMontoSolicitado().toString());
            }
        }

        documento.add(tabla);
        //creamos la 2da tabla
        documento.close();

    }

    private void calcularTotales() {
        BigDecimal totalLiberado = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        totalLiquidado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaSolicitudes) {
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
            totalEjecutado = totalEjecutado.add(detalle.getEjecutado());
            totalLiquidado = totalLiquidado.add(detalle.getLiquidado());
            totalLiberado = totalLiberado.add(totalLiberado);
        }
        totalSolicitado = totalSolicitado.add(totalLiberado).add(totalLiquidado);
    }

    public boolean isFirmaSolicutd() {
        return firmaSolicutd;
    }

    public void setFirmaSolicutd(boolean firmaSolicutd) {
        this.firmaSolicutd = firmaSolicutd;
    }

//    public void handleFileUpload(FileUploadEvent event) {
//        try {
//
//            uploadedFile = event.getFile();
//            existenImagenes = Boolean.TRUE;
//            File file = FilesUtil.copyFileServer1(event, SisVars.RUTA_FILES_PRESUPUESTO);
//            firmaElectronica.setArchivoFirmar(file.getAbsolutePath());
//            JsfUtil.addSuccessMessage("INFORMACIÓN", "Valide su firma para firmar el Documento");
//
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo", "");
//        }
//    }

    public void validarFirmaImagenPDF() {
        if (validarFirma()) {
            imagenesPdfs = obtenerImagenesDesdePDF(firmaElectronica.getArchivoFirmar());
            if (Utils.isNotEmpty(imagenesPdfs)) {
                existenImagenes = Boolean.TRUE;
            }
        }
    }

    public void validarFirmarDocumento() {
//        if (validarFirma()) {
        firmaElectronica.setClave(clave);
        FirmaElectronica archivoGenerado = generarFirmaElectronica(firmaElectronica, SisVars.wsFirmaEC);
        firmaElectronica.setClave(Utils.encriptaEnMD5(clave));
        firmaElectronica.setUrlQr("");
        firmaElectronica.setMotivo("Firmado Electrónicamente por FirmaEC");
        if (archivoGenerado != null) {
            JsfUtil.addInformationMessage("", "Elemento guardado con éxito.");
        } else {
            JsfUtil.addInformationMessage("", "No se pudo firmar el documento");
        }
//        PrimeFaces.current().dialog().closeDynamic(archivoGenerado);
//        }
    }

    private Boolean validarFirma() {
        if (!clave.isEmpty()) {
            if (us.getFirmaElectronica() != null) {
                if (new Date().before(us.getFirmaElectronica().getFechaExpiracion())) {
                    if (Utils.encriptaEnMD5(clave).equals(us.getFirmaElectronica().getClave())) {
                        return Boolean.TRUE;
                    } else {
                        JsfUtil.addErrorMessage("", "Clave incorrecta.");
                        return Boolean.FALSE;
                    }
                } else {
                    JsfUtil.addErrorMessage("", "Su firma electrónica esta caducada");
                    return Boolean.FALSE;
                }
            } else {
                JsfUtil.addErrorMessage("", "Suba su firma electrónica, primero");
                return Boolean.FALSE;
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese su clave ");
            return Boolean.FALSE;
        }
    }

    public void previoFirmar(SolicitudReservaCompromiso s) {
        this.ocultarDocumente = Boolean.TRUE;
        solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        solicitudReservaCompromiso = s;
        rutaArchivoMostrat = "";
        System.out.println("tarea.getTaskDefinitionKey()>>>: " + tarea.getTaskDefinitionKey());
        switch (tarea.getTaskDefinitionKey()) {
            case "documentoReservaLegalizacion":
                if (solicitudReservaCompromiso.getRutaReserva() == null) {
                    JsfUtil.addWarningMessage("No se encontro archivo relacionado al proceso", "");
                    return;
                }
                rutaArchivoMostrat = solicitudReservaCompromiso.getRutaReserva();
                break;
            case "documento_legalizacion":
                if (solicitudReservaCompromiso.getRutaModificada() == null) {
                    JsfUtil.addWarningMessage("No se encontro archivo relacionado al proceso", "");
                    return;
                }
                rutaArchivoMostrat = solicitudReservaCompromiso.getRutaModificada();
                break;
            case "documento_a":
                if (solicitudReservaCompromiso.getRutaAnulada() == null) {
                    JsfUtil.addWarningMessage("No se encontro archivo relacionado al proceso", "");
                    return;
                }
                rutaArchivoMostrat = solicitudReservaCompromiso.getRutaAnulada();
                break;
            case "documento_l":
                if (solicitudReservaCompromiso.getRutaLiquidada() == null) {
                    JsfUtil.addWarningMessage("No se encontro archivo relacionado al proceso", "");
                    return;
                }
                rutaArchivoMostrat = solicitudReservaCompromiso.getRutaLiquidada();
                break;
            case "documento_liber":
                if (solicitudReservaCompromiso.getRutaLiberada() == null) {
                    JsfUtil.addWarningMessage("No se encontro archivo relacionado al proceso", "");
                    return;
                }
                rutaArchivoMostrat = solicitudReservaCompromiso.getRutaLiberada();
                break;
            default:
                rutaArchivoMostrat = "";
                JsfUtil.addWarningMessage("ADVERTENCIA", "NO SE PUEDO ENVIAR EL CORREO");
                break;

        }
    }

    public Void selectEndListener(ImageAreaSelectEvent e) {
        if (e.getWidth() == 150 && e.getHeight() == 50) {
            String pagina = e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", "");
            firmaElectronica.setClave(clave);
            firmaElectronica.setNumeroPagina(Integer.valueOf(pagina) + 1);
            firmaElectronica.setPosicionX1("" + (e.getX1()));
            firmaElectronica.setPosicionX2("" + (e.getX2()));
            firmaElectronica.setPosicionY1(((e.getImgHeight() - e.getY1())) + "");
            firmaElectronica.setPosicionY2(((e.getImgHeight() - e.getY2())) + "");
            firmaElectronica.setUsuario(new Usuarios(session.getUserId()));
            FirmaElectronica archivoGenerado = firmarElectronicamente(firmaElectronica);
            firmaElectronica.setClave(Utils.encriptaEnMD5(clave));//SE LA ENCRIPTA NUEVAMENTE
            //Eliminar Archivos
            if (archivoGenerado != null) {

                String rutaTmp = archivoGenerado.getArchivoFirmado().replace("\\", "/");

                switch (tarea.getTaskDefinitionKey()) {
                    case "documentoReservaLegalizacion":
                        solicitudReservaCompromiso.setRutaReserva(rutaTmp);
                        break;
                    case "documento_legalizacion":
                        solicitudReservaCompromiso.setRutaModificada(rutaTmp);
                        break;
                    case "documento_a":
                        solicitudReservaCompromiso.setRutaAnulada(rutaTmp);
                        break;
                    case "documento_l":
                        solicitudReservaCompromiso.setRutaLiquidada(rutaTmp);
                        break;
                    case "documento_liber":
                        solicitudReservaCompromiso.setRutaLiberada(rutaTmp);
                        break;
                    default:
                        solicitudReservaCompromiso.setRutaOtros(rutaTmp);
                        break;

                }

                solicitudService.edit(solicitudReservaCompromiso);
                previoFirmar(solicitudReservaCompromiso);
            }
            existenImagenes = false;
            imagenesPdfs = new ArrayList<>();
            clave = "";
            PrimeFaces.current().ajax().update("formMain");
        } else {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar el tamaño completo del rectángulo para la firma electrónica");
        }
        return null;
    }

    public FirmaElectronica generarFirmaElectronica(FirmaElectronica firmaElectrica, String tipo) {
        FirmaElectronica archivoGenerado = new FirmaElectronica();
        if (!firmaElectrica.getClave().isEmpty()) {
            if (Utils.encriptaEnMD5(firmaElectrica.getClave()).equals(us.getFirmaElectronica().getClave())) {
                firmaElectrica.setIsuser(us.getUsuario().getNameUsuario());
                firmaElectrica.setClave(firmaElectrica.getClave());
                //LLAMANDO DIRECTAMENTE AL METODO GENERAR FIRMA AL WS
                archivoGenerado = (FirmaElectronica) irisService.methodPOST(firmaElectrica, tipo + "firmaElectronica/generar", FirmaElectronica.class);
                firmaElectrica.setClave(Utils.encriptaEnMD5(firmaElectrica.getClave()));
                if (archivoGenerado != null) {
                    JsfUtil.addInformationMessage("", "Elemento guardado con éxito.");
                } else {
                    JsfUtil.addSuccessMessage("", "No se pudo firmar el documento");
                }

            } else {
                JsfUtil.addErrorMessage("", "Clave incorrecta");
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese su clave ");
        }

        return archivoGenerado;
    }

    public FirmaElectronica firmarElectronicamente(FirmaElectronica firmaElectronica) {
        return (FirmaElectronica) irisService.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/generar", FirmaElectronica.class);
    }

    public List<Imagenes> obtenerImagenesDesdePDF(String ruta) {
        String urlFile = Utils.getFilterRuta(ruta);
        return (List<Imagenes>) irisService.methodListGET(SisVars.appQrService + "resource/pdfImagenes/" + urlFile, Imagenes[].class);
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public Boolean getOcultarDocumente() {
        return ocultarDocumente;
    }

    public void setOcultarDocumente(Boolean ocultarDocumente) {
        this.ocultarDocumente = ocultarDocumente;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(BigDecimal totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public BigDecimal getTotalLiquidado() {
        return totalLiquidado;
    }

    public void setTotalLiquidado(BigDecimal totalLiquidado) {
        this.totalLiquidado = totalLiquidado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFirmaAnulacion() {
        return firmaAnulacion;
    }

    public void setFirmaAnulacion(boolean firmaAnulacion) {
        this.firmaAnulacion = firmaAnulacion;
    }

    public boolean isFirmaReserva() {
        return firmaReserva;
    }

    public void setFirmaReserva(boolean firmaReserva) {
        this.firmaReserva = firmaReserva;
    }

    public boolean isFirmaLiquidacion() {
        return firmaLiquidacion;
    }

    public void setFirmaLiquidacion(boolean firmaLiquidacion) {
        this.firmaLiquidacion = firmaLiquidacion;
    }

    public boolean isFirmaLiberacion() {
        return firmaLiberacion;
    }

    public void setFirmaLiberacion(boolean firmaLiberacion) {
        this.firmaLiberacion = firmaLiberacion;
    }

    public boolean isFirmaComprometida() {
        return firmaComprometida;
    }

    public void setFirmaComprometida(boolean firmaComprometida) {
        this.firmaComprometida = firmaComprometida;
    }

    public boolean isFirmaModificacion() {
        return firmaModificacion;
    }

    public void setFirmaModificacion(boolean firmaModificacion) {
        this.firmaModificacion = firmaModificacion;
    }

    public String getRutaArchivoMostrat() {
        return rutaArchivoMostrat;
    }

    public void setRutaArchivoMostrat(String rutaArchivoMostrat) {
        this.rutaArchivoMostrat = rutaArchivoMostrat;
    }

    public List<CatalogoItem> getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(List<CatalogoItem> estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public List<UnidadAdministrativa> getUnidadFiltros() {
        return unidadFiltros;
    }

    public void setUnidadFiltros(List<UnidadAdministrativa> unidadFiltros) {
        this.unidadFiltros = unidadFiltros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public SolicitudReservaCompromiso getSolicitudReservaCompromiso() {
        return solicitudReservaCompromiso;
    }

    public void setSolicitudReservaCompromiso(SolicitudReservaCompromiso solicitudReservaCompromiso) {
        this.solicitudReservaCompromiso = solicitudReservaCompromiso;
    }

    public DetalleSolicitudCompromiso getDetalleSolicitudReserva() {
        return detalleSolicitudReserva;
    }

    public void setDetalleSolicitudReserva(DetalleSolicitudCompromiso detalleSolicitudReserva) {
        this.detalleSolicitudReserva = detalleSolicitudReserva;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<SolicitudReservaCompromiso> lazy) {
        this.lazy = lazy;
    }

    public List<DetalleSolicitudCompromiso> getListaSolicitudes() {
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<DetalleSolicitudCompromiso> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public List<SolicitudRequisito> getSolicitudRequisitoList() {
        return solicitudRequisitoList;
    }

    public void setSolicitudRequisitoList(List<SolicitudRequisito> solicitudRequisitoList) {
        this.solicitudRequisitoList = solicitudRequisitoList;
    }

    public Procedimiento getProcedimientoSeleccionado() {
        return procedimientoSeleccionado;
    }

    public void setProcedimientoSeleccionado(Procedimiento procedimientoSeleccionado) {
        this.procedimientoSeleccionado = procedimientoSeleccionado;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public boolean isPanelProveedor() {
        return panelProveedor;
    }

    public void setPanelProveedor(boolean panelProveedor) {
        this.panelProveedor = panelProveedor;
    }

    public boolean isPanelServidor() {
        return panelServidor;
    }

    public void setPanelServidor(boolean panelServidor) {
        this.panelServidor = panelServidor;
    }

    public ProveedorLazy getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(ProveedorLazy proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public ServidorLazy getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(ServidorLazy servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public ReservaCompromisoService getSolicitudService() {
        return solicitudService;
    }

    public void setSolicitudService(ReservaCompromisoService solicitudService) {
        this.solicitudService = solicitudService;
    }

    public DetalleReservaCompromisoService getDetalleSolicitudService() {
        return detalleSolicitudService;
    }

    public void setDetalleSolicitudService(DetalleReservaCompromisoService detalleSolicitudService) {
        this.detalleSolicitudService = detalleSolicitudService;
    }

    public ProcedimientoRequisitoService getProcedimientoRequisitoService() {
        return procedimientoRequisitoService;
    }

    public void setProcedimientoRequisitoService(ProcedimientoRequisitoService procedimientoRequisitoService) {
        this.procedimientoRequisitoService = procedimientoRequisitoService;
    }

    public SolicitudRequisitoService getSolicitudRequisitoService() {
        return solicitudRequisitoService;
    }

    public void setSolicitudRequisitoService(SolicitudRequisitoService solicitudRequisitoService) {
        this.solicitudRequisitoService = solicitudRequisitoService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

    public KatalinaService getKatalinaService() {
        return katalinaService;
    }

    public void setKatalinaService(KatalinaService katalinaService) {
        this.katalinaService = katalinaService;
    }

    public PappProcesoService getProcesoService() {
        return procesoService;
    }

    public void setProcesoService(PappProcesoService procesoService) {
        this.procesoService = procesoService;
    }

    public DiarioGeneralService getDiarioGeneralService() {
        return diarioGeneralService;
    }

    public void setDiarioGeneralService(DiarioGeneralService diarioGeneralService) {
        this.diarioGeneralService = diarioGeneralService;
    }

    public UserSession getUs() {
        return us;
    }

    public void setUs(UserSession us) {
        this.us = us;
    }

    public IrisService getIrisService() {
        return irisService;
    }

    public void setIrisService(IrisService irisService) {
        this.irisService = irisService;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public boolean isShowOptionFirmar() {
        return showOptionFirmar;
    }

    public void setShowOptionFirmar(boolean showOptionFirmar) {
        this.showOptionFirmar = showOptionFirmar;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getExistenImagenes() {
        return existenImagenes;
    }

    public void setExistenImagenes(Boolean existenImagenes) {
        this.existenImagenes = existenImagenes;
    }

    public List<Imagenes> getImagenesPdfs() {
        return imagenesPdfs;
    }

    public void setImagenesPdfs(List<Imagenes> imagenesPdfs) {
        this.imagenesPdfs = imagenesPdfs;
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public Cliente getClienteNotifacacion() {
        return clienteNotifacacion;
    }

    public void setClienteNotifacacion(Cliente clienteNotifacacion) {
        this.clienteNotifacacion = clienteNotifacacion;
    }

    public List<DetalleTransaccion> getDetalleAcumulado() {
        return detalleAcumulado;
    }

    public void setDetalleAcumulado(List<DetalleTransaccion> detalleAcumulado) {
        this.detalleAcumulado = detalleAcumulado;
    }

//</editor-fold>    
}
