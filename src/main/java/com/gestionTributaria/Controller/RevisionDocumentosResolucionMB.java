/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class RevisionDocumentosResolucionMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FinaRenLiquidacionService liquidacionesService;
    @Inject
    private VentanillaService servicioPredio;
    private List<FinaRenLiquidacion> liquidaciones;
    private SolicitudServicios solicitudServicio;
    private FinaRenTipoLiquidacion tipoLiquidacion;

    @PostConstruct

    public void initView() {
        if (this.session.getTaskID() != null) {
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
            tipoLiquidacion = new FinaRenTipoLiquidacion();
            traerPredio();
            if (!"PI".equals(this.tramite.getServicio().getAbreviatura()) && !"PE".equals(this.tramite.getServicio().getAbreviatura())) {
                traerLiquidaciones();
            }
        }
    }

    public void traerPredio() {
        solicitudServicio = servicioPredio.findByIdTramite(this.tramite);
    }

    public void traerLiquidaciones() {
        if (solicitudServicio.getPredio().getTipoPredio().equals("U")) {
            tipoLiquidacion.setId(2L);
        } else {
            tipoLiquidacion.setId(3L);
        }
        liquidaciones = (List<FinaRenLiquidacion>) liquidacionesService.liquidacionesByPredio(solicitudServicio.getPredio(), tipoLiquidacion);
    }

    public void continuarTares() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.abogadoResoucion2);
            getParamts().put("resolucion", usuario.equals("") ? "admin_1" : usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "completas tareas", ex);
        }
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public SolicitudServicios getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(SolicitudServicios solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }

}
