/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.activos.lazy.ProveedorLazy;
import com.origami.sigef.certificacion_presupuesto_anual.model.DocumentosModel;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "revisionCertificadosView")
@ViewScoped
public class revisionRequisitosController extends BpmnBaseRoot implements Serializable {

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
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private UserSession userSession;
    @Inject
    private ThCargoRubrosService servicioThCargos;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaEgresosService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ServletSession ss;
    @Inject
    private ProcedimientoService procedimientoService;

    private SolicitudReservaCompromiso solicitudReservaCompromiso;
    private DetalleSolicitudCompromiso detalleSolicitudReserva;
    private LazyModel<SolicitudReservaCompromiso> lazy;
    private List<DetalleSolicitudCompromiso> listaSolicitudes;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private List<UploadedFile> files;
    private Procedimiento procedimientoSeleccionado;
    private String fileName;
    private Cliente beneficiario;
    private boolean panelProveedor, panelServidor;
    private ProveedorLazy proveedorLazy;
    private ServidorLazy servidorLazy;
    private List<CatalogoItem> estadoFiltro;
    private List<UnidadAdministrativa> unidadFiltros;
    private String observaciones;
    private boolean btnAprobar, btnRechazar;
    private boolean panel1, panel2, ocultarbutton, mostrarButton;
    private static final Logger LOG = Logger.getLogger(AprobacionCertificadosController.class.getName());

    private BigDecimal totalPresupuesto;
    private BigDecimal totalDisponible;
    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido, totalEjecuato, totalLiquidado;
    private List<DetalleTransaccion> detalleAcumulado;

    private UnidadAdministrativa unidadAdministrativa;
    private DetalleSolicitudCompromiso detalleSolicitud;
    private Producto producto;
    private ProcedimientoRequisito procedimientoRequisito;
    private DocumentosModel data;
    private DocumentosModel archivo;
    private UploadedFile file;
    private List<Producto> listaProductos;
    private List<ThCargoRubros> listaDistributivo;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private List<Procedimiento> procedimientoList;
    private List<DetalleSolicitudCompromiso> removerDetalle;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicPartidasDirectas;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos;
    private List<DetalleSolicitudCompromiso> listaGuardar;
    private List<SolicitudReservaCompromiso> listaVerificadora;
    private List<DocumentosModel> documentosListas;
    private List<DetalleSolicitudCompromiso> listaeditable1;
    private List<DetalleSolicitudCompromiso> listaeditable2;
    private List<DetalleSolicitudCompromiso> listaeditable3;
    private BigDecimal totalDisponiblePDI = BigDecimal.ZERO;
    private BigDecimal totalComprometidoPDI = BigDecimal.ZERO;
    private BigDecimal totalSolicitadoPDI = BigDecimal.ZERO;
    private BigDecimal totalSolicitadoPD;
    private BigDecimal totalComprometidoPD;
    private BigDecimal totalDisponiblePD;
    private BigDecimal valorAnteriorPresupuesto;
    private double num1, num2;
    private double result1, result2;
    private String cbTipo;
    private boolean bolSistemaAntiguo = false;

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

                this.lazy.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                this.listaSolicitudes = new ArrayList<>();
                this.solicitudRequisitoList = new ArrayList<>();
                this.procedimientoRequisitoList = new ArrayList<>();
                this.procedimientoList = new ArrayList<>();
                this.procedimientoSeleccionado = new Procedimiento();
                procedimientoList = procedimientoService.getProcedimientos("SOL_RC");
                data = new DocumentosModel();
                this.valorAnteriorPresupuesto = BigDecimal.ZERO;
                panelProveedor = true;
                panelServidor = false;
                estadoFiltro = new ArrayList<>();
                estadoFiltro = catalogoService.MostarTodoCatalogo("estado_solicitud");
                unidadFiltros = new ArrayList<>();
                unidadFiltros = solicitudService.getListaUnidadesReservas();
                documentosListas = new ArrayList<>();
                btnAprobar = false;
                btnRechazar = false;
                totalComprometido = BigDecimal.ZERO;
                totalEjecuato = BigDecimal.ZERO;
                totalLiquidado = BigDecimal.ZERO;
                detalleAcumulado = new ArrayList<>();
                panel1 = true;
                panel2 = false;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
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

    public void showEjecutadoReservasGlobal(SolicitudReservaCompromiso r) {

    }

    public void detallePartidasGlobal() {

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

//    public void observacion() {
//    
//    }
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

    public void aprobarSolicitud(boolean a, SolicitudReservaCompromiso s) {
        solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.solicitudReservaCompromiso = s;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (a) {
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
        }

        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea(int aprobar) {

        try {
            observacion.setObservacion(observaciones);

            BigDecimal valor = BigDecimal.ZERO;
            BigDecimal valor2 = BigDecimal.ZERO;
            int result = 0;
            int result2 = 0;

            short periodo = solicitudReservaCompromiso.getPeriodo();
            System.out.println("clienteService.getrolsUser(RolUsuario.financiero) >> " + clienteService.getrolsUser(RolUsuario.financiero));
            System.out.println("clienteService.getrolsUser(RolUsuario.asistenteDireccion) >> " + clienteService.getrolsUser(RolUsuario.asistenteDireccion));

            if (tramite.getTipoTramite().getActivitykey().equals("solicitud_reserva_compromiso")) {
                if (aprobar == 1) {
//                    getParamts().put("usuario2", clienteService.getrolsUser(RolUsuario.financiero));
                    getParamts().put("usuario2", clienteService.getrolsUser(RolUsuario.asistenteDireccion));
                }
                getParamts().put("apro", aprobar);

            } else {
                getParamts().put("aprobado", aprobar);
                System.out.println("Aprobar: " + aprobar);
                if (aprobar == 1) {
                    getParamts().put("usuariofinal", clienteService.getrolsUser(RolUsuario.financiero));
                }
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            if (aprobar == 1) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solicitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + " aprobado con éxito");
            } else {
                solicitudReservaCompromiso.setEstado(solicitudService.getestados("RECHA", "estado_solicitud"));
                solicitudService.edit(solicitudReservaCompromiso);
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solicitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + " Rechazado con éxito");
            }
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    private void calcularTotales() {
        totalPresupuesto = BigDecimal.ZERO;
        totalDisponible = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        List<DetalleSolicitudCompromiso> detalle_temp;
        detalle_temp = new ArrayList<>();
        if (Utils.isNotEmpty(listaSolicitudes)) {
            detalle_temp = listaSolicitudes;
        } else if (Utils.isNotEmpty(listaGlobalDetalleSolicitudCompromisos)) {
            detalle_temp = listaSolicitudes;
        }
        if (Utils.isNotEmpty(detalle_temp)) {
            for (DetalleSolicitudCompromiso detalle : listaSolicitudes) {
                if (detalle.getActividadProducto() != null) {
                    totalPresupuesto = totalPresupuesto.add(detalle.getActividadProducto().getMontoReformada());
                } else if (detalle.getRefDistributivo() != null) {
                    totalPresupuesto = totalPresupuesto.add(detalle.getRefDistributivo().getReformaCodificado());
                } else if (detalle.getPartidasDirecta() != null) {
                    totalPresupuesto = totalPresupuesto.add(detalle.getPartidasDirecta().getReformaCodificado());
                }
                totalDisponible = totalDisponible.add(detalle.getMontoDisponible());
                totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            }
        }
    }

    /*actualizar reserva*/
    public void consultarMostrarAndOcultarPaneles() {
        panel1 = true;
        panel2 = false;
    }

    public void edicionSolicitud(SolicitudReservaCompromiso s) {
        solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        ocultarbutton = true;
        mostrarButton = false;
        listaGuardar = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        removerDetalle = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        listaeditable3 = new ArrayList<>();
        listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
        this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.solicitudReservaCompromiso = s;

//        if (!"APRO".equals(reservaCompromiso.getEstado().getCodigo())) {
//            PrimeFaces.current().ajax().update("messages");
//            JsfUtil.addWarningMessage("Advertencia", "La solicitud debe estar aprobada para poder actualizarla");
//
//            return;
//        }
//
        this.procedimientoSeleccionado = s.getProcedimiento();

        listaeditable1 = solicitudService.getEdicionDetalleProducto(s);
        listaeditable2 = solicitudService.getEdicionDetallePDAndD(s);
        listaeditable3 = solicitudService.getEdicionDetallePD(s);
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }

        listaeditable1.forEach((detalle1) -> {
            this.listaGlobalDetalleSolicitudCompromisos.add(detalle1);
        });

        listaeditable2.forEach((detalle2) -> {
            this.listaGlobalDetalleSolicitudPartidas.add(detalle2);
        });

        listaeditable3.forEach((detalle2) -> {
            this.listaGlobalDetalleSolicPartidasDirectas.add(detalle2);
        });

        panel1 = false;
        panel2 = true;

        listaGlobalDetalleSolicitudCompromisos.forEach((lista1) -> {
            lista1.setMontoDisponible(lista1.getActividadProducto().getMontoReformada().subtract(solicitudService.getSumaEdicionProductos(lista1.getActividadProducto())).add(lista1.getMontoSolicitado()));
        });
        listaGlobalDetalleSolicitudPartidas.forEach((lis) -> {
            lis.setMontoDisponible(lis.getRefDistributivo().getReformaCodificado().subtract(solicitudService.getSumaEdicionPresupuesto(lis.getRefDistributivo().getId(), true)).add(lis.getMontoSolicitado()));
        });

        listaGlobalDetalleSolicPartidasDirectas.forEach((lis) -> {
            lis.setMontoDisponible(lis.getPartidasDirecta().getReformaCodificado().subtract(solicitudService.getSumaEdicionPresupuesto(lis.getPartidasDirecta().getId(), false)).add(lis.getMontoSolicitado()));
        });

        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update("formMain");
    }

    private void calcularTotalesPD() {
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudPartidas) {
            totalDisponiblePD = totalDisponiblePD.add(detalle.getMontoDisponible());
            totalComprometidoPD = totalComprometidoPD.add(detalle.getMontoComprometido());
            totalSolicitadoPD = totalSolicitadoPD.add(detalle.getMontoSolicitado());
        }
        totalDisponiblePDI = BigDecimal.ZERO;
        totalComprometidoPDI = BigDecimal.ZERO;
        totalSolicitadoPDI = BigDecimal.ZERO;
        if (listaGlobalDetalleSolicPartidasDirectas != null) {
            for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicPartidasDirectas) {
                totalDisponiblePDI = totalDisponiblePDI.add(detalle.getMontoDisponible());
                totalComprometidoPDI = totalComprometidoPDI.add(detalle.getMontoComprometido());
                totalSolicitadoPDI = totalSolicitadoPDI.add(detalle.getMontoSolicitado());
            }
        }
    }

    public void seleccionPresupuesto(ThCargoRubros p) {
        //if (!listaGlobalDetaSolicCompr.isEmpty()) {
        if (listaGlobalDetalleSolicitudPartidas != null) {
            for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudPartidas) {
                if (p.equals(data.getRefDistributivo())) {
                    JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                    return;
                }
            }
        }
        DetalleSolicitudCompromiso detSoliComp = new DetalleSolicitudCompromiso();
        detSoliComp.setSolicitud(solicitudReservaCompromiso);
        detSoliComp.setRefDistributivo(p);
        detSoliComp.setEstado(true);
        detSoliComp.setFechaCreacion(new Date());
        detSoliComp.setFechaModifcacion(new Date());
        detSoliComp.setUsuarioCreacion(userSession.getName());
        detSoliComp.setUsuarioModificacion(userSession.getName());
        detSoliComp.setPeriodo(solicitudReservaCompromiso.getPeriodo());
        BigDecimal maximo_resultado = BigDecimal.ZERO;
        BigDecimal resultado_valor_disponble = (BigDecimal) solicitudService.getSueldoDisponibleCargosRubros(p, solicitudReservaCompromiso.getPeriodo());
        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detSoliComp.setMontoDisponible(p.getReformaCodificado());
        } else {
            maximo_resultado = detSoliComp.getRefDistributivo().getReformaCodificado().subtract(resultado_valor_disponble);
            detSoliComp.setMontoDisponible(maximo_resultado);
        }

        listaGlobalDetalleSolicitudPartidas.add(detSoliComp);
        sumaTotales();
        //calcularTotales();
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        JsfUtil.addInformationMessage("", "EL REGISTRO QUE SELECCION FUE EXITOSO");
    }

    public void sumaTotales() {
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        totalDisponiblePDI = BigDecimal.ZERO;
        totalComprometidoPDI = BigDecimal.ZERO;
        totalSolicitadoPDI = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudPartidas) {
            totalDisponiblePD = totalDisponiblePD.add(detalle.getMontoDisponible());
            totalComprometidoPD = totalComprometidoPD.add(detalle.getMontoComprometido());
            totalSolicitadoPD = totalSolicitadoPD.add(detalle.getMontoSolicitado());
        }

        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicPartidasDirectas) {
            totalDisponiblePDI = totalDisponiblePDI.add(detalle.getMontoDisponible());
            totalComprometidoPDI = totalComprometidoPDI.add(detalle.getMontoComprometido());
            totalSolicitadoPDI = totalSolicitadoPDI.add(detalle.getMontoSolicitado());
        }
    }

    public void accionComponentes() throws ParseException {
        if (!solicitudService.getPeriodoAprobado(solicitudReservaCompromiso.getPeriodo())) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }
        if (solicitudReservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }
        if (solicitudReservaCompromiso.getPeriodo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");
        } else {
//            tabla1 = true;
//            tabla2 = false;
//            tabla3 = false;
            //cargoUnidadUser
            unidadAdministrativa = clienteService.getUnidadPrincipalUSer(userSession.getNameUser());
            System.out.println("unidadAdministrativa " + unidadAdministrativa.toString());
            solicitudReservaCompromiso.setUnidadRequiriente(unidadAdministrativa);
            //Distributivo d = clienteService.getuusuarioLogeado(user.getNameUser());
            List<Producto> listaAnaidir = new ArrayList<>();
            if (unidadAdministrativa != null) {
                listaAnaidir = solicitudService.listadoProductoActividades(solicitudReservaCompromiso.getPeriodo(), unidadAdministrativa);
            }

            if (!listaAnaidir.isEmpty()) {
                listaProductos = listaAnaidir;
            }
            if (listaProductos.isEmpty()) {

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No existe datos");
                return;
            }
            PrimeFaces.current().executeScript("PF('Dlgo2').show()");
            PrimeFaces.current().ajax().update(":formDlgo2");
        }
    }

    public void accionComponentesDistributivoPartida(Boolean distributivo) {
//        listaPresupuesto = new ArrayList<>();
        if (!solicitudService.getPeriodoAprobado(solicitudReservaCompromiso.getPeriodo())) {
            //if (false) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }
        if (solicitudReservaCompromiso.getDescripcion().length() == 1) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }
        if (solicitudReservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }
        solicitudReservaCompromiso.setUnidadRequiriente(unidadAdministrativa);
        if (solicitudService.getPeriodoAprobado(solicitudReservaCompromiso.getPeriodo())) {
            if (solicitudReservaCompromiso.getPeriodo() == null && solicitudReservaCompromiso.getUnidadRequiriente() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Elija una fecha y una unidad");
            } else {
                if (distributivo) {
                    obtieneListaDistributivo(solicitudReservaCompromiso);
                    JsfUtil.executeJS("PF('Dlogo3').show()");
                    JsfUtil.update(":formDlogo3");
                } else {
                    obtenerPartidasDirectas();
                    JsfUtil.executeJS("PF('dlogoPartidasDirectas').show()");
                    JsfUtil.update("fromPartidasDirectas");
                }

            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "No hay aprobacion " + solicitudReservaCompromiso.getPeriodo());
        }

    }

    public void añadiendoFormato(Producto p) {
        BigDecimal maximo_resultado = BigDecimal.ZERO;

        for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudCompromisos) {
            if (data.getActividadProducto().equals(p)) {
                JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                return;
            }
        }

        detalleSolicitud = new DetalleSolicitudCompromiso();
        producto = new Producto();
        producto = p;

        detalleSolicitud.setActividadProducto(producto);
        detalleSolicitud.setEstado(true);
        detalleSolicitud.setFechaCreacion(new Date());
        detalleSolicitud.setFechaModifcacion(new Date());
        detalleSolicitud.setUsuarioCreacion(userSession.getName());
        detalleSolicitud.setUsuarioModificacion(userSession.getName());
        detalleSolicitud.setPeriodo(solicitudReservaCompromiso.getPeriodo());

        BigDecimal resultado_valor_disponble = solicitudService.getSueldoDisponible(producto, solicitudReservaCompromiso.getPeriodo());

        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detalleSolicitud.setMontoDisponible(producto.getMontoReformada());

        } else {

            maximo_resultado = (detalleSolicitud.getActividadProducto().getMontoReformada()).subtract(resultado_valor_disponble);

            detalleSolicitud.setMontoDisponible(maximo_resultado);

        }

        detalleSolicitud.setMontoComprometido(BigDecimal.ZERO);
        listaGlobalDetalleSolicitudCompromisos.add(detalleSolicitud);

        calcularTotales();
        JsfUtil.addInformationMessage("INFORMACION", "PRODUCTO SELECCIONADO CORRECTAMENTE");
        producto = new Producto();
        detalleSolicitud = new DetalleSolicitudCompromiso();
    }

    public void solicitarReservas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();
        if (detalleSolicitud.getMontoComprometido() != null) {
            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudCompromisos.add(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudCompromisos.remove(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud));
            calcularTotales();
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void solicitarReservasPD(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();
        if (detalleSolicitud.getMontoComprometido() != null) {
            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicPartidasDirectas.add(this.listaGlobalDetalleSolicPartidasDirectas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicPartidasDirectas.remove(this.listaGlobalDetalleSolicPartidasDirectas.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('Dlgo').hide()");
            PrimeFaces.current().ajax().update(":formDlgo");
        }
        calcularTotalesPD();
    }

    public void solicitarReservasPartidas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudPartidas.add(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudPartidas.remove(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud));
            calcularTotalesPD();
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void obtieneListaDistributivo(SolicitudReservaCompromiso p) {
        List<ThCargoRubros> tmp = servicioThCargos.distributivoCompleto(p.getPeriodo());
        listaDistributivo = new ArrayList<>();
        System.out.println("tmp " + tmp.size());
        if (tmp != null && !tmp.isEmpty()) {

            for (ThCargoRubros item : tmp) {
                listaDistributivo.add(item);
            }
        }
    }

    public void obtenerPartidasDirectas() {
        listaPartidasDirectas = new ArrayList<>();
        listaPartidasDirectas = proformaEgresosService.getProformaPresupuestoPartidaDirecta(solicitudReservaCompromiso.getPeriodo(), "PDI");
    }

    public void eliminadoMemoriaDetalleSOlicitud(int index) {
        removerDetalle.add(listaGlobalDetalleSolicitudCompromisos.get(index));
        listaGlobalDetalleSolicitudCompromisos.remove(index);
        calcularTotales();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void eliminarPArtidasDirectasAndDistributivo(int index) {
        removerDetalle.add(listaGlobalDetalleSolicitudPartidas.get(index));
        listaGlobalDetalleSolicitudPartidas.remove(index);
        calcularTotalesPD();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void cargarRequisitos() {
        this.procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);

        PrimeFaces.current().ajax().update("requisitosTabla");
    }

    public void requisitoSeleccionado(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void eliminarPDF(ProcedimientoRequisito procedimientoRequisito) {
        DocumentosModel item = new DocumentosModel();
        boolean verificar = true;

        if (Utils.isNotEmpty(documentosListas)) {
            for (DocumentosModel data : documentosListas) {
                if (data.getRequisito().equals(procedimientoRequisito)) {
                    if (data.getUrl() == null || "".equals(data.getUrl())) {

                        JsfUtil.addErrorMessage("Error", "No se puede eliminar porque no hay ningún archivos");
                        return;
                    } else {
                        item = new DocumentosModel();
                        item = data;
                        verificar = false;
                        break;
                    }

                }
            }

            if (!verificar) {
                documentosListas.remove(item);
                files.remove(item.getArchivo());
                JsfUtil.addInformationMessage("Información", "Eliminado Correctamente");
            }
        } else {
            JsfUtil.addWarningMessage("Advertencia", "No hay archivos");

        }

        //        Boolean auxiliar = true;
        //        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
        //            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        //        } else {
        //            for (SolicitudRequisito pr : solicitudRequisitoList) {
        //                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
        //                    solicitudRequisitoList.remove(pr);
        //                    break;
        //                } else {
        //                    auxiliar = false;
        //                }
        //            }
        //            if (auxiliar) {
        //                JsfUtil.addInformationMessage("INFORMACIÓN", "DOCUMENTO ADJUNTO ELIMINADO CORRECTAMENTE");
        //            } else {
        //                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
        //            }
        //        }
    }

    public Boolean documentosObligatorios() {
        boolean verificacion = false;
        int contado1 = 0;
        int contador2 = 0;

        if (!procedimientoRequisitoList.isEmpty()) {
            if (!documentosListas.isEmpty()) {

                List<ProcedimientoRequisito> lista1 = procedimientoRequisitoList.stream().filter(data1 -> data1.getObligatorio() == true).collect(Collectors.toList());
                List<DocumentosModel> lista2 = documentosListas.stream().filter(data -> data.getRequisito().getObligatorio() == true).collect(Collectors.toList());

                if (lista1.size() == lista2.size()) {
                    verificacion = false;
                } else {
                    verificacion = true;
                }
                for (DocumentosModel data : documentosListas) {
                    if (data.getRequisito().getObligatorio()) {
                        if (data.getUrl() == null || "".equals(data.getUrl())) {
                            verificacion = true;
                            break;
                        }
                    }
                }
            } else {
                for (ProcedimientoRequisito item : procedimientoRequisitoList) {
                    if (item.getObligatorio()) {
                        verificacion = true;
                        break;
                    }
                }
            }
        } else {
            verificacion = true;
        }
        return verificacion;
    }

    public void viewFile(int index, ProcedimientoRequisito req) {
        try {
            data = new DocumentosModel();
            if (Utils.isNotEmpty(documentosListas)) {
                for (DocumentosModel documentosLista : documentosListas) {
                    if (documentosLista.getRequisito().equals(req)) {
                        data = documentosLista;
                    }
                }
//                data = documentosListas.get(index);
                if (data.getArchivo() != null) {
                    ss.setContentType(data.getArchivo().getContentType());
                    ss.setNombreDocumento(data.getArchivo().getFileName());
                    ss.setTempData(data.getArchivo().getInputstream());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (Utils.isNotEmpty(documentosListas)) {
                for (DocumentosModel item : documentosListas) {
                    if (procedimientoRequisito == item.getRequisito()) {
                        if (item.getUrl().length() > 0) {
                            JsfUtil.addErrorMessage("Error", "Ya subió este requisito, si quiere subir uno nuevo primero borrelo y después suba de nuevo");
                            return;
                        }
                    }
                }
            }
            file = null;
            file = event.getFile();
            archivo = new DocumentosModel();
            archivo.setRequisito(procedimientoRequisito);
            archivo.setUrl(file.getFileName());
            archivo.setArchivo(file);
            if (files == null) {
                files = new ArrayList<>();
            }
            files.add(event.getFile());
            this.documentosListas.add(archivo);
            System.out.println("Archivo: " + archivo.getArchivo());
            System.out.println("Arc_url: " + archivo.getUrl());
            this.procedimientoRequisito = new ProcedimientoRequisito();
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("requisitoDialogForm");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Su archivo se subió exitosamente");
        } catch (Exception e) {
            System.out.println("Error al subir el archivo " + e);
        }
    }

    public void resetSolicitud() {
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
//        ListadetalleSolicitud = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        procedimientoSeleccionado = new Procedimiento();
    }

    public void editarReservaCompromiso() {
        listaGuardar.clear();
        listaGuardar = new ArrayList<>();
        List<DetalleSolicitudCompromiso> listaValoresAnteriores;
        if (procedimientoSeleccionado == null) {
            JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
            return;
        }
        boolean verificar = true;
        boolean pararMetodo = true;
        boolean pararMetodoPdi = true;
        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty() || !listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {

            if (!listaGlobalDetalleSolicitudCompromisos.isEmpty()) {
                listaGlobalDetalleSolicitudCompromisos.forEach((l) -> {
                    listaGuardar.add(l);
                });
            }
            if (!listaGlobalDetalleSolicitudPartidas.isEmpty()) {
                listaGlobalDetalleSolicitudPartidas.forEach((item) -> {
                    listaGuardar.add(item);
                });
            }
            if (listaGlobalDetalleSolicPartidasDirectas != null && !listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {
                listaGlobalDetalleSolicPartidasDirectas.forEach((item) -> {
                    listaGuardar.add(item);
                });

            }
        }
        if (listaGuardar.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No hay datos");
            return;
        }
        listaValoresAnteriores = listaGuardar;
        for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
            num1 = 0;
            result1 = 0;
            num1 = solicitudService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();
            BigDecimal valorActual = solicitudService.getValorActual(itemsuma);
            if (itemsuma.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result1 = num1 + itemsuma.getMontoSolicitado().doubleValue() - valorActual.doubleValue();
            }

            if (result1 > itemsuma.getActividadProducto().getMontoReformada().doubleValue()) {
                verificar = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo para su reserva solicitada");
                break;
            }
        }
        if (!verificar) {
            return;
        }
        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicitudPartidas) {
            num2 = 0;
            result2 = 0;
            num2 = solicitudService.getSumaEdicionPresupuesto(itemverificar.getRefDistributivo().getId(), true).doubleValue();
            BigDecimal valorActual = solicitudService.getValorActual(itemverificar);
            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue() - valorActual.doubleValue();
            }
            if (result2 > itemverificar.getRefDistributivo().getReformaCodificado().doubleValue()) {
                pararMetodo = false;

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }
        }
        if (!pararMetodo) {
            return;
        }

        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicPartidasDirectas) {
            num2 = 0;
            result2 = 0;
            num2 = solicitudService.getSumaEdicionPresupuesto(itemverificar.getPartidasDirecta().getId(), false).doubleValue();
            BigDecimal valorActual = solicitudService.getValorActual(itemverificar);
            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue() - valorActual.doubleValue();
            }

            if (result2 > itemverificar.getPartidasDirecta().getReformaCodificado().doubleValue()) {
                pararMetodoPdi = false;

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }
        }

        if (!pararMetodoPdi) {
            return;
        }

        solicitudReservaCompromiso.setUsuarioModificacion(userSession.getName());
        solicitudReservaCompromiso.setFechaModificacion(new Date());
        solicitudService.edit(solicitudReservaCompromiso);
        if (files != null) {
            uploadDoc.upload(solicitudReservaCompromiso, files);
        }
        for (DetalleSolicitudCompromiso li : listaGuardar) {
            if (li.getId() != null) {
                detalleSolicitud = new DetalleSolicitudCompromiso();
                detalleSolicitud.setId(li.getId());
                detalleSolicitud.setSolicitud(solicitudReservaCompromiso);
                detalleSolicitud.setActividadProducto(li.getActividadProducto());
                detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
                detalleSolicitud.setEstado(true);
                detalleSolicitud.setRefDistributivo(li.getRefDistributivo());
                detalleSolicitud.setFechaCreacion(new Date());
                detalleSolicitud.setFechaModifcacion(new Date());
                detalleSolicitud.setUsuarioCreacion(userSession.getName());
                detalleSolicitud.setUsuarioModificacion(userSession.getName());
                detalleSolicitud.setPeriodo(li.getPeriodo());
                detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
                detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());
                detalleSolicitud.setMontoComprometido(valorAnteriorPresupuesto);
                for (DetalleSolicitudCompromiso lli : listaValoresAnteriores) {
                    if (lli.equals(li)) {
                        detalleSolicitud.setMontoComprometido(lli.getMontoComprometido());
                    }
                }
                detalleSolicitudService.edit(detalleSolicitud);
            } else {
                detalleSolicitud = new DetalleSolicitudCompromiso();
                detalleSolicitud = Utils.clone(li);
                detalleSolicitud.setSolicitud(solicitudReservaCompromiso);
                detalleSolicitud.setActividadProducto(li.getActividadProducto());
                detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
                detalleSolicitud.setEstado(true);
                detalleSolicitud.setRefDistributivo(li.getRefDistributivo());
                detalleSolicitud.setFechaCreacion(new Date());
                detalleSolicitud.setFechaModifcacion(new Date());
                detalleSolicitud.setUsuarioCreacion(userSession.getName());
                detalleSolicitud.setUsuarioModificacion(userSession.getName());
                detalleSolicitud.setPeriodo(li.getPeriodo());
                detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
                detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());
                detalleSolicitud.setMontoComprometido(BigDecimal.ZERO);
                detalleSolicitud = detalleSolicitudService.create(detalleSolicitud);
            }
        }
        if (!removerDetalle.isEmpty()) {
            for (DetalleSolicitudCompromiso detalle : removerDetalle) {
                if (detalle.getId() != null) {
                    detalleSolicitudService.getRemove(detalle);
                }
            }
        }

        short periodo = solicitudReservaCompromiso.getPeriodo();
        List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);
        BigDecimal valor = BigDecimal.ZERO, valor2 = BigDecimal.ZERO;
        if (listP != null && !listP.isEmpty()) {
            for (Producto pr : listP) {

                valor = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                int result = solicitudService.updateReservaProducto(valor, periodo, pr.getId());

                if (result > 0) {

                }
            }
        }
        List<ThCargoRubros> lis = solicitudService.listaReservasSinRepetir(periodo);
        if (lis != null && !lis.isEmpty()) {
            for (ThCargoRubros li : lis) {
                valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
                int result2 = solicitudService.actualizarReservaPresupuesto(valor2, periodo, li.getId());

                if (result2 > 0) {

                }
            }
        }

        List<ProformaPresupuestoPlanificado> tmpPd = solicitudService.listaReservasPdSinRepetir(periodo);

        if (tmpPd != null && !tmpPd.isEmpty()) {
            for (ProformaPresupuestoPlanificado li : tmpPd) {
                valor2 = solicitudService.sumaTotalDeReservasDevengadoPd(periodo, li.getId());
                int result2 = solicitudService.actualizarReservaPresupuestoPd(valor2, periodo, li.getId());

                if (result2 > 0) {

                }
            }
        }
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Editado correctamente");
        resetSolicitud();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        consultarMostrarAndOcultarPaneles();
        listaVerificadora = new ArrayList<>();
        listaVerificadora = solicitudService.listaSolicitud(BigInteger.valueOf(tramite.getNumTramite()));
//        if (listaVerificadora.size() > 0) {
//            registraNuevoVerificar = true;
//        } else {
//            registraNuevoVerificar = false;
//        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public boolean isBtnAprobar() {
        return btnAprobar;
    }

    public void setBtnAprobar(boolean btnAprobar) {
        this.btnAprobar = btnAprobar;
    }

    public boolean isBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public BigDecimal getTotalPresupuesto() {
        return totalPresupuesto;
    }

    public void setTotalPresupuesto(BigDecimal totalPresupuesto) {
        this.totalPresupuesto = totalPresupuesto;
    }

    public BigDecimal getTotalDisponible() {
        return totalDisponible;
    }

    public void setTotalDisponible(BigDecimal totalDisponible) {
        this.totalDisponible = totalDisponible;
    }

    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
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
//</editor-fold>    

    public boolean isPanel1() {
        return panel1;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPanel2() {
        return panel2;
    }

    public void setPanel2(boolean panel2) {
        this.panel2 = panel2;
    }

    public boolean isOcultarbutton() {
        return ocultarbutton;
    }

    public void setOcultarbutton(boolean ocultarbutton) {
        this.ocultarbutton = ocultarbutton;
    }

    public boolean isMostrarButton() {
        return mostrarButton;
    }

    public void setMostrarButton(boolean mostrarButton) {
        this.mostrarButton = mostrarButton;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<ThCargoRubros> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<ThCargoRubros> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidasDirectas() {
        return listaPartidasDirectas;
    }

    public void setListaPartidasDirectas(List<ProformaPresupuestoPlanificado> listaPartidasDirectas) {
        this.listaPartidasDirectas = listaPartidasDirectas;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudPartidas() {
        return listaGlobalDetalleSolicitudPartidas;
    }

    public void setListaGlobalDetalleSolicitudPartidas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas) {
        this.listaGlobalDetalleSolicitudPartidas = listaGlobalDetalleSolicitudPartidas;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicPartidasDirectas() {
        return listaGlobalDetalleSolicPartidasDirectas;
    }

    public void setListaGlobalDetalleSolicPartidasDirectas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicPartidasDirectas) {
        this.listaGlobalDetalleSolicPartidasDirectas = listaGlobalDetalleSolicPartidasDirectas;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudCompromisos() {
        return listaGlobalDetalleSolicitudCompromisos;
    }

    public void setListaGlobalDetalleSolicitudCompromisos(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos) {
        this.listaGlobalDetalleSolicitudCompromisos = listaGlobalDetalleSolicitudCompromisos;
    }

    public List<Procedimiento> getProcedimientoList() {
        return procedimientoList;
    }

    public void setProcedimientoList(List<Procedimiento> procedimientoList) {
        this.procedimientoList = procedimientoList;
    }

    public List<DetalleSolicitudCompromiso> getRemoverDetalle() {
        return removerDetalle;
    }

    public void setRemoverDetalle(List<DetalleSolicitudCompromiso> removerDetalle) {
        this.removerDetalle = removerDetalle;
    }

    public List<DocumentosModel> getDocumentosListas() {
        return documentosListas;
    }

    public void setDocumentosListas(List<DocumentosModel> documentosListas) {
        this.documentosListas = documentosListas;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public List<DetalleSolicitudCompromiso> getListaGuardar() {
        return listaGuardar;
    }

    public void setListaGuardar(List<DetalleSolicitudCompromiso> listaGuardar) {
        this.listaGuardar = listaGuardar;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalEjecuato() {
        return totalEjecuato;
    }

    public void setTotalEjecuato(BigDecimal totalEjecuato) {
        this.totalEjecuato = totalEjecuato;
    }

    public BigDecimal getTotalLiquidado() {
        return totalLiquidado;
    }

    public void setTotalLiquidado(BigDecimal totalLiquidado) {
        this.totalLiquidado = totalLiquidado;
    }

    public BigDecimal getTotalDisponiblePDI() {
        return totalDisponiblePDI;
    }

    public void setTotalDisponiblePDI(BigDecimal totalDisponiblePDI) {
        this.totalDisponiblePDI = totalDisponiblePDI;
    }

    public BigDecimal getTotalComprometidoPDI() {
        return totalComprometidoPDI;
    }

    public void setTotalComprometidoPDI(BigDecimal totalComprometidoPDI) {
        this.totalComprometidoPDI = totalComprometidoPDI;
    }

    public BigDecimal getTotalSolicitadoPDI() {
        return totalSolicitadoPDI;
    }

    public void setTotalSolicitadoPDI(BigDecimal totalSolicitadoPDI) {
        this.totalSolicitadoPDI = totalSolicitadoPDI;
    }

    public BigDecimal getTotalSolicitadoPD() {
        return totalSolicitadoPD;
    }

    public void setTotalSolicitadoPD(BigDecimal totalSolicitadoPD) {
        this.totalSolicitadoPD = totalSolicitadoPD;
    }

    public BigDecimal getTotalComprometidoPD() {
        return totalComprometidoPD;
    }

    public void setTotalComprometidoPD(BigDecimal totalComprometidoPD) {
        this.totalComprometidoPD = totalComprometidoPD;
    }

    public BigDecimal getTotalDisponiblePD() {
        return totalDisponiblePD;
    }

    public void setTotalDisponiblePD(BigDecimal totalDisponiblePD) {
        this.totalDisponiblePD = totalDisponiblePD;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ProcedimientoRequisito getProcedimientoRequisito() {
        return procedimientoRequisito;
    }

    public void setProcedimientoRequisito(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
    }

    public boolean isBolSistemaAntiguo() {
        return bolSistemaAntiguo;
    }

    public void setBolSistemaAntiguo(boolean bolSistemaAntiguo) {
        this.bolSistemaAntiguo = bolSistemaAntiguo;
    }

    public double getNum1() {
        return num1;
    }

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public double getNum2() {
        return num2;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    public double getResult1() {
        return result1;
    }

    public void setResult1(double result1) {
        this.result1 = result1;
    }

    public double getResult2() {
        return result2;
    }

    public void setResult2(double result2) {
        this.result2 = result2;
    }

    public String getCbTipo() {
        return cbTipo;
    }

    public void setCbTipo(String cbTipo) {
        this.cbTipo = cbTipo;
    }

    public DetalleSolicitudCompromiso getDetalleSolicitud() {
        return detalleSolicitud;
    }

    public void setDetalleSolicitud(DetalleSolicitudCompromiso detalleSolicitud) {
        this.detalleSolicitud = detalleSolicitud;
    }

    public List<SolicitudReservaCompromiso> getListaVerificadora() {
        return listaVerificadora;
    }

    public void setListaVerificadora(List<SolicitudReservaCompromiso> listaVerificadora) {
        this.listaVerificadora = listaVerificadora;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable1() {
        return listaeditable1;
    }

    public void setListaeditable1(List<DetalleSolicitudCompromiso> listaeditable1) {
        this.listaeditable1 = listaeditable1;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable2() {
        return listaeditable2;
    }

    public void setListaeditable2(List<DetalleSolicitudCompromiso> listaeditable2) {
        this.listaeditable2 = listaeditable2;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable3() {
        return listaeditable3;
    }

    public void setListaeditable3(List<DetalleSolicitudCompromiso> listaeditable3) {
        this.listaeditable3 = listaeditable3;
    }

    public BigDecimal getValorAnteriorPresupuesto() {
        return valorAnteriorPresupuesto;
    }

    public void setValorAnteriorPresupuesto(BigDecimal valorAnteriorPresupuesto) {
        this.valorAnteriorPresupuesto = valorAnteriorPresupuesto;
    }

    public DocumentosModel getData() {
        return data;
    }

    public void setData(DocumentosModel data) {
        this.data = data;
    }

}
