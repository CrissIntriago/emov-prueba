/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

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
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
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
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "aprobacionCertSinProcesoView")
@ViewScoped
public class AprobacionCertificadosSinProcesoController implements Serializable {

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
    private UserSession userSess;
    @Inject
    private PartidaDistributivoService partidaDistributivoService;
    @Inject
    private MasterCatalogoService periodoService;

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
    private boolean btnAprobar, btnRechazar;
    private boolean rederedSinRequisitos;
    private static final Logger LOG = Logger.getLogger(AprobacionCertificadosSinProcesoController.class.getName());
    private BigDecimal totalSolicitado;
    private BigDecimal totalDisponible;
    private BigDecimal totalPresupuesto;

    private OpcionBusqueda opcionBusqueda;
    private List<Short> listaPeriodo;

    @PostConstruct
    public void inicializar() {
        this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.beneficiario = new Cliente();
        this.detalleSolicitudReserva = new DetalleSolicitudCompromiso();
        opcionBusqueda = new OpcionBusqueda();
        listaPeriodo = solicitudService.listaAniosAprobados(Boolean.TRUE);
        if (listaPeriodo != null) {
            int indice = listaPeriodo.size();
            if (!listaPeriodo.isEmpty() && indice == 1) {
                opcionBusqueda.setAnio(listaPeriodo.get(0));
            } else {
                opcionBusqueda.setAnio(listaPeriodo.get(indice - 1));
            }
        }
        actualizarListadoReservas();
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
        btnAprobar = false;
        btnRechazar = false;
        rederedSinRequisitos = true;
    }

    public void actualizarListadoReservas() {
        if (opcionBusqueda.getAnio() != null) {
            //    String verificar = solicitudService.getCodigoRol(userSess.getNameUser(), RolUsuario.financiero);
            this.lazy = new LazyModel(SolicitudReservaCompromiso.class);
//            if (verificar.equals("")) {
//                lazy.getFilterss().put("estado.codigo", Arrays.asList("NO-"));
//                lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "AVISO!!", "No tiene permiso de Aprobar Reservas."));
//            } else {
            this.lazy.getFilterss().put("numTramite:equal", null);
            lazy.getFilterss().put("estado.codigo", Arrays.asList("E", "O"));
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            //  }
        } else {
            lazy = null;
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
        if (reservaCompromiso.getNumTramite() == null || reservaCompromiso.getProcedimiento() == null) {
            rederedSinRequisitos = false;
        } else {
            rederedSinRequisitos = true;
        }
        cargarDatosRegistrados(reservaCompromiso);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('DlgoVisualizacionAprobacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionAprobacion");
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
        if (reservaCompromiso.getNumTramite() == null) {
            rederedSinRequisitos = false;
        }
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
        if (reservaCompromiso.getNumTramite() == null) {
            rederedSinRequisitos = false;
        }
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

    public void editarFecha(SolicitudReservaCompromiso solicitud) {
        try {
            if (solicitud.getFechaAprobacion() != null) {
                solicitudService.edit(solicitud);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("ERROR!!", "No se pudo actualizar la fecha de aprobación");
        }
    }

    public void aprobarSolicitud(boolean a, SolicitudReservaCompromiso s) {
        solicitudReservaCompromiso = new SolicitudReservaCompromiso();
        this.solicitudReservaCompromiso = s;
        if (a) {
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
        }
        if (a) {
            BigDecimal valor = BigDecimal.ZERO;
            BigDecimal valor2 = BigDecimal.ZERO;
            int result = 0;
            int result2 = 0;
            this.solicitudReservaCompromiso.setComentario("");
            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("APRO", "estado_solicitud"));
            if (solicitudReservaCompromiso.getFechaAprobacion() == null) {
                this.solicitudReservaCompromiso.setFechaAprobacion(new Date());
            }
            solicitudService.edit(solicitudReservaCompromiso);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solicitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + " aprobada con éxito");
            short periodo = solicitudReservaCompromiso.getPeriodo();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            actualzandoReservaPartidasDistAndDa(periodo);

        } else {
            BigDecimal valor = BigDecimal.ZERO;
            BigDecimal valor2 = BigDecimal.ZERO;
            int result = 0;
            int result2 = 0;
            int resultado = 0;
            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("O", "estado_solicitud"));
            solicitudService.edit(solicitudReservaCompromiso);
            short periodo = solicitudReservaCompromiso.getPeriodo();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            actualzandoReservaPartidasDistAndDa(periodo);

        }
    }

    public void actualzandoReservaPartidasDistAndDa(Short periodo) {
        BigDecimal valor1 = BigDecimal.ZERO;
        BigDecimal valor2 = BigDecimal.ZERO;
        BigDecimal valor3 = BigDecimal.ZERO;
        List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);
        if (!listP.isEmpty()) {
            for (Producto pr : listP) {
                valor1 = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                int result = solicitudService.updateReservaProducto(valor1, periodo, pr.getId());
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
                valor3 = solicitudService.sumaTotalDeReservasDevengadoPd(periodo, li.getId());
                int result3 = solicitudService.actualizarReservaPresupuestoPd(valor2, periodo, li.getId());

                if (result3 > 0) {

                }
            }
        }

    }

    public void completarTarea(int aprobar) {

//        try {
//            observacion.setObservacion(observaciones);
        if (aprobar == 1) {
            BigDecimal valor = BigDecimal.ZERO;
            BigDecimal valor2 = BigDecimal.ZERO;
            int result = 0;
            int result2 = 0;

            this.solicitudReservaCompromiso.setComentario("");
            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("APRO", "estado_solicitud"));
            this.solicitudReservaCompromiso.setFechaAprobacion(new Date());
            solicitudService.edit(solicitudReservaCompromiso);

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solcitud" + formatoCodigo(this.solicitudReservaCompromiso.getSecuencial()) + "-" + this.solicitudReservaCompromiso.getPeriodo() + " aprobada con éxito");

            short periodo = solicitudReservaCompromiso.getPeriodo();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);

            if (!listP.isEmpty()) {
                for (Producto pr : listP) {

                    valor = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                    result = solicitudService.updateReservaProducto(valor, periodo, pr.getId());

                    if (result > 0) {

                    }
                }

            }
//            List<Presupuesto> lis = solicitudService.listaReservasSinRepetir(periodo);
//            if (!lis.isEmpty()) {
//
//                for (Presupuesto li : lis) {
//
//                    valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
//                    result2 = solicitudService.actualizarReservaPresupuesto(valor2, periodo, li.getId());
//
//                    if (result2 > 0) {
//
//                    }
//                }
//            }
            List<Presupuesto> lis = solicitudService.listaPresupuestoPDI(periodo);
            if (!lis.isEmpty()) {

                for (Presupuesto li : lis) {

                    valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
                    solicitudService.updatePDIReservas(valor2, periodo, li);

                    if (result2 > 0) {

                    }
                }
            }

        } else {

            BigDecimal valor = BigDecimal.ZERO;
            BigDecimal valor2 = BigDecimal.ZERO;
            int result = 0;
            int result2 = 0;
            int resultado = 0;

            this.solicitudReservaCompromiso.setEstado(solicitudService.getestados("RECHA", "estado_solicitud"));
            solicitudService.edit(solicitudReservaCompromiso);

            short periodo = solicitudReservaCompromiso.getPeriodo();
            this.solicitudReservaCompromiso = new SolicitudReservaCompromiso();

            List<Producto> listP = solicitudService.listaReservasSinRepetirProducto(periodo);

            for (Producto pr : listP) {

                valor = solicitudService.sumaTotalDeReservasProducto(periodo, pr.getId());
                result = solicitudService.updateReservaProducto(valor, periodo, pr.getId());

                if (result > 0) {

                }
            }

//            List<Presupuesto> lis = solicitudService.listaReservasSinRepetir(periodo);
//
//            for (Presupuesto li : lis) {
//
//                valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
//                result2 = solicitudService.actualizarReservaPresupuesto(valor2, periodo, li.getId());
//
//                if (result2 > 0) {
//
//                }
//            }
            List<Presupuesto> lis = solicitudService.listaPresupuestoPDI(periodo);
            if (!lis.isEmpty()) {
                for (Presupuesto li : lis) {
                    valor2 = solicitudService.sumaTotalDeReservasDevengado(periodo, li.getId());
                    solicitudService.updatePDIReservas(valor2, periodo, li);

                }
            }

        }

//            getParamts().put("aprobado", aprobar);
//            getParamts().put("usuario", clienteService.getUSerLogeo("GUARDALMACÉN"));
        JsfUtil.executeJS("PF('dlgObservaciones').hide()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

//            if (saveTramite() == null) {
//                return;
//            }
//            this.session.setVarTemp(null);
//            super.completeTask((HashMap<String, Object>) getParamts());
//            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "completas tareas", e);
//        }
    }

    public void viewDocumento(SolicitudReservaCompromiso s) {

        ss.borrarDatos();
        try {
            Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.financiero));
            Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.presupuesto));
            ss.addParametro("ci_financiero", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
            ss.addParametro("nombre_financiero", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
            ss.addParametro("ci_presupuesto", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
            ss.addParametro("nombre_presupuesto", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
            if ("LIQUI".equals(s.getEstado().getCodigo())) {
                ss.addParametro("reserva", s.getId());
                ss.setNombreReporte("certificacionLiquidacion");
                ss.setNombreSubCarpeta("CertificacionPresupuestaria");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
            } else {
                ss.addParametro("id", s.getId());
                ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
                ss.setNombreReporte("certificacionReservaCompromiso");
                ss.setNombreSubCarpeta("CertificacionPresupuestaria");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
        } catch (Exception e) {

        }

    }

    private void calcularTotales() {
        totalPresupuesto = BigDecimal.ZERO;
        totalDisponible = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaSolicitudes) {
            if (detalle.getActividadProducto() != null) {
                totalPresupuesto = totalPresupuesto.add(detalle.getActividadProducto().getMontoReformada());
            } else if (detalle.getRefDistributivo() != null) {
                totalPresupuesto = totalPresupuesto.add(detalle.getRefDistributivo().getReformaCodificado());
            }
            else if (detalle.getPartidasDirecta() != null) {
                totalPresupuesto = totalPresupuesto.add(detalle.getPartidasDirecta().getReformaCodificado());
            }
            totalDisponible = totalDisponible.add(detalle.getMontoDisponible());
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public BigDecimal getTotalDisponible() {
        return totalDisponible;
    }

    public void setTotalDisponible(BigDecimal totalDisponible) {
        this.totalDisponible = totalDisponible;
    }

    public BigDecimal getTotalPresupuesto() {
        return totalPresupuesto;
    }

    public void setTotalPresupuesto(BigDecimal totalPresupuesto) {
        this.totalPresupuesto = totalPresupuesto;
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

    public boolean isRederedSinRequisitos() {
        return rederedSinRequisitos;
    }

    public void setRederedSinRequisitos(boolean rederedSinRequisitos) {
        this.rederedSinRequisitos = rederedSinRequisitos;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
//</editor-fold> 
}
