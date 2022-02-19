/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.SolicitudDocumento;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class GeneraLiquidacionVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    private FinaRenLiquidacion liquidacion;
    protected String observaciones;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private List<SolicitudDocumento> solicitudDocumentos;
    private SolicitudServicios solicitudServicios;
    private int tipo;
    private Boolean asignar;
    private List<UnidadAdministrativa> departamentos;
    private UnidadAdministrativa departamento;
    private Observaciones ultimaObservacion;
    private String codigoLiquidacion;

    @PostConstruct
    public void initView() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                Map<String, Object> params = new HashMap<>();
                params.put("id_tramite", tramite.getTipoTramite().getId());
                solicitudServicios = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByTramiteId", new Object[]{tramite.getId()});
                registroRequisitos = ventanillaService.findAllRegistroRequisitosBySolicitud(solicitudServicios);
                asignar = Boolean.FALSE;
                if (!Utils.isEmpty(tramite.getObservaciones())) {
                    ultimaObservacion = tramite.getObservaciones().get(tramite.getObservaciones().size() - 1);
                }
                liquidacion = new FinaRenLiquidacion();
                if (!Utils.isEmptyString(solicitudServicios.getReferenciaLiquidacionPago())) {
                    liquidacion = liquidacionService.getFinaRenLiquidacionByIdLiquidacion(solicitudServicios.getReferenciaLiquidacionPago());
                }
                codigoLiquidacion = "";
                buscarLiquidacion();
            }
        }
    }

    public void abriDlogo() {
        if (liquidacion == null || liquidacion.getId() == null) {
            JsfUtil.addErrorMessage("", "Debe vincular una liquidacion para continuar con el trámite");
            return;
        }
        if (Utils.isEmptyString(solicitudServicios.getReferenciaLiquidacionPago())) {
            JsfUtil.addErrorMessage("", "Debe vincular una liquidacion para continuar con el trámite");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            solicitudServicios.setPendientePago(Boolean.TRUE);
            ventanillaService.save(solicitudServicios);
            getParamts().put("usuario_6", session.getNameUser());
            observacion.setObservacion(observaciones);
            if (saveTramite() == null) {
                return;
            }
            enviarCorreo();
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    public void buscarLiquidacion() {
        if (!Utils.isEmptyString(codigoLiquidacion)) {
            liquidacion = liquidacionService.getFinaRenLiquidacionByIdLiquidacion(codigoLiquidacion);
            if (liquidacion == null || liquidacion.getId() == null) {
                JsfUtil.addErrorMessage("", "No existen datos con los parámetros ingresados");
                liquidacion = new FinaRenLiquidacion();
                return;
            }
            if (liquidacion.getCodigoVerificado() != null && liquidacion.getCodigoVerificado()) {
                JsfUtil.addErrorMessage("", "La liquidación ya ha sido verificada");
                liquidacion = new FinaRenLiquidacion();
                return;
            }
            if (liquidacion.getEstadoLiquidacion().getDescripcion().toLowerCase().equals("pagado")) {
                JsfUtil.addErrorMessage("", "Liquidación no disponible");
                return;
            }
            JsfUtil.addInformationMessage("", "Liquidación disponible");
        } else {
            JsfUtil.addErrorMessage("", "Ingrese un código de liquidación");
        }
    }

    public void vincularLiquidacion() {
        solicitudServicios.setEnObservacion(Boolean.TRUE);
        solicitudServicios.setReferenciaLiquidacionPago(codigoLiquidacion);
        ventanillaService.save(solicitudServicios);
        liquidacion.setCodigoVerificado(Boolean.TRUE);
        ventanillaService.save(liquidacion);
        codigoLiquidacion = "";
        JsfUtil.addSuccessMessage("", "Liquidación vinculado con éxito");
        JsfUtil.update("mainForm");
    }

    public void desvincularLiquidacion() {
        solicitudServicios.setEnObservacion(Boolean.FALSE);
        solicitudServicios.setReferenciaLiquidacionPago(null);
        ventanillaService.save(solicitudServicios);
        liquidacion.setCodigoVerificado(Boolean.FALSE);
        ventanillaService.save(liquidacion);
        codigoLiquidacion = "";
        liquidacion = new FinaRenLiquidacion();
        JsfUtil.addSuccessMessage("", "Liquidación desvinculada");
        JsfUtil.update("mainForm");
    }

    public void limpiarLiquidacion() {
        liquidacion = new FinaRenLiquidacion();
        codigoLiquidacion = "";
    }

    @Override
    public void enviarCorreo() {
        Correo c = new Correo();
        c.setDestinatario(solicitudServicios.getEnteSolicitante().getEmail());
        c.setAsunto("Notificación de pago");
        c.setMensaje(Utils.mailHtmlNotificacion("TRÁMITE N° " + tramite.getNumTramite() + " - " + tramite.getTipoTramite().getDescripcion(),
                "Estimad@ " + solicitudServicios.getEnteSolicitante().getNombreCompleto()
                + " se le notifica que debe acercarse a cancelar valores pendientes de pago por su trámite "
                + tramite.getTipoTramite().getDescripcion(),
                "Gracias por la Atención Brindada", "Este correo fue enviado de forma automática y no requiere respuesta."));
        correoService.enviarCorreo(c);
    }

    public Boolean renderedVincular() {
        if (liquidacion != null && liquidacion.getId() != null
                && liquidacion.getEstadoLiquidacion() != null
                && liquidacion.getEstadoLiquidacion().getDescripcion().toLowerCase().equals("pagado")) {
            return Boolean.FALSE;
        }
        if (Utils.isEmptyString(solicitudServicios.getReferenciaLiquidacionPago())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean renderedDesvincular() {
        if (liquidacion != null && liquidacion.getId() != null
                && liquidacion.getEstadoLiquidacion() != null
                && liquidacion.getEstadoLiquidacion().getDescripcion().toLowerCase().equals("pagado")) {
            return Boolean.FALSE;
        }
        if (!solicitudServicios.getPendientePago() && !Utils.isEmptyString(solicitudServicios.getReferenciaLiquidacionPago())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getCodigoLiquidacion() {
        return codigoLiquidacion;
    }

    public void setCodigoLiquidacion(String codigoLiquidacion) {
        this.codigoLiquidacion = codigoLiquidacion;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public List<SolicitudDocumento> getSolicitudDocumentos() {
        return solicitudDocumentos;
    }

    public void setSolicitudDocumentos(List<SolicitudDocumento> solicitudDocumentos) {
        this.solicitudDocumentos = solicitudDocumentos;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Boolean getAsignar() {
        return asignar;
    }

    public void setAsignar(Boolean asignar) {
        this.asignar = asignar;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }
//</editor-fold>
}
