/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "revisionInicialView")
@ViewScoped
public class revisionInicialController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReservaCompromisoService reservaService;
    @Inject
    private DetalleReservaCompromisoService detalleReservaService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession user;
    @Inject
    private ClienteService clienteService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    private SolicitudReservaCompromiso solcitudReserva;
    private DetalleSolicitudCompromiso detalleReservaCompromiso;
    private LazyModel<SolicitudReservaCompromiso> lazy;
    private List<DetalleSolicitudCompromiso> solicitudesDetalles;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private Procedimiento procedimientoSeleccionado;
    private String observaciones;
    private String fileName;
    private List<CatalogoItem> estadoFiltro;
    private List<UnidadAdministrativa> unidadFiltros;

    private static final Logger LOG = Logger.getLogger(revisionCertificadosReservaCompromisoController.class.getName());

    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;
    private BigDecimal totalEjecutado;
    private BigDecimal totalLiquidado, totalLiberado;
    private List<DetalleTransaccion> detalleAcumulado;

    @PostConstruct
    public void incializador() {

        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.solcitudReserva = new SolicitudReservaCompromiso();
                this.detalleReservaCompromiso = new DetalleSolicitudCompromiso();
                lazy = new LazyModel(SolicitudReservaCompromiso.class);
                lazy.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                solicitudRequisitoList = new ArrayList<>();
                procedimientoRequisitoList = new ArrayList<>();
                procedimientoSeleccionado = new Procedimiento();
                estadoFiltro = new ArrayList<>();
                estadoFiltro = catalogoService.MostarTodoCatalogo("estado_solicitud");
                unidadFiltros = new ArrayList<>();
                unidadFiltros = reservaService.getListaUnidadesReservas();
                detalleAcumulado = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();

        return a;
    }

    public void openDlgoVisualizacion(SolicitudReservaCompromiso s) {
        this.solcitudReserva = new SolicitudReservaCompromiso();
        this.solcitudReserva = s;
        this.procedimientoSeleccionado = s.getProcedimiento();

        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }
        this.solicitudesDetalles = reservaService.getListaDetlleSolciitud(s);
        showEjecutadoReservasGlobal(s);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('DlgoVisualizacionRevision').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionRevision");

    }

    public void showEjecutadoReservasGlobal(SolicitudReservaCompromiso r) {

    }

    public void detallePartidasGlobal() {

    }

    public void enviarRevisionsolicitud(SolicitudReservaCompromiso s) {
        this.solcitudReserva = new SolicitudReservaCompromiso();
        this.solcitudReserva = s;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);

            if (tramite.getTipoTramite().getActivitykey().equals("solicitud_reserva_compromiso")) {
                if (tarea.getTaskDefinitionKey().equals("usertask2")) {

                    getParamts().put("usuario3", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                } else {
                    getParamts().put("usuario4", clienteService.getrolsUser(RolUsuario.presupuesto));
                }
            } else {
                getParamts().put("usuarioVerificacion", clienteService.getrolsUser(RolUsuario.presupuesto));
            }

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solcitad " + formatoCodigo(this.solcitudReserva.getSecuencial()) + "-" + this.solcitudReserva.getPeriodo() + " enviada con éxito");
            this.solcitudReserva = new SolicitudReservaCompromiso();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
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

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("forMain:datarevision");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("forMain:datarevision");
        }
    }

    private void calcularTotales() {
        totalSolicitado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        totalLiquidado = BigDecimal.ZERO;
        totalLiberado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : solicitudesDetalles) {
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
            totalEjecutado = totalEjecutado.add(detalle.getEjecutado());
            totalLiquidado = totalLiquidado.add(detalle.getLiquidado());
            if (detalle.getLiberado() != null) {
                totalLiberado = totalLiberado.add(detalle.getLiberado());
            }
        }
        totalSolicitado = totalSolicitado.add(totalLiberado).add(totalLiquidado);
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public List<CatalogoItem> getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(List<CatalogoItem> estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public BigDecimal getTotalLiberado() {
        return totalLiberado;
    }

    public void setTotalLiberado(BigDecimal totalLiberado) {
        this.totalLiberado = totalLiberado;
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

    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
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

    public SolicitudReservaCompromiso getSolcitudReserva() {
        return solcitudReserva;
    }

    public void setSolcitudReserva(SolicitudReservaCompromiso SolcitudReserva) {
        this.solcitudReserva = SolcitudReserva;
    }

    public List<DetalleSolicitudCompromiso> getSolicitudesDetalles() {
        return solicitudesDetalles;
    }

    public void setSolicitudesDetalles(List<DetalleSolicitudCompromiso> solicitudesDetalles) {
        this.solicitudesDetalles = solicitudesDetalles;
    }

    public DetalleSolicitudCompromiso getDetalleReservaCompromiso() {
        return detalleReservaCompromiso;
    }

    public void setDetalleReservaCompromiso(DetalleSolicitudCompromiso detalleReservaCompromiso) {
        this.detalleReservaCompromiso = detalleReservaCompromiso;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<SolicitudReservaCompromiso> lazy) {
        this.lazy = lazy;
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

//</editor-fold>  
}
