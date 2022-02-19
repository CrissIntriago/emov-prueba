/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.FnEstadoExoneracion;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.ResolucionesService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
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
public class aprobacionRechazoResolucionMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ResolucionesService resolucionService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FnSolicitudExoneracionServices solicitudExoneracionService;
    private FnSolicitudExoneracion solicitudExoneracion;
    private Integer opcion;

    @PostConstruct
    public void initView() {
        if (this.session.getTaskID() != null) {
            solicitudExoneracion = new FnSolicitudExoneracion();
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
            buscarResolucion();
        }
    }

    public void buscarResolucion() {
        solicitudExoneracion = solicitudExoneracionService.findByTramiteSolicitudExoneracion(tramite.getId());
    }

    public void abrirDlgObservacion(Integer opc) {
        opcion = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");

    }

    public void actualizarEstado() {
        try {
            //aprobar
            if (opcion == 1) {
                solicitudExoneracion.setEstado(new FnEstadoExoneracion(1L));
                solicitudExoneracion.setFechaAprobacion(new Date());
                solicitudExoneracionService.edit(solicitudExoneracion);
                if (this.tramite.getServicio() != null) {
                    if (this.tramite.getServicio().getAbreviatura().equals("PI") || this.tramite.getServicio().getAbreviatura().equals("PE")) {
                        String usuario = clienteService.getrolsUser(RolUsuario.tesorero);
                        this.getParamts().put("tesoreria", usuario.equals("") ? "admin_1" : usuario);
                        this.getParamts().put("opcion", 2);
                    } else {
                        String usuario = clienteService.getrolsUser(RolUsuario.jefeRentas);
                        this.getParamts().put("rentas", usuario.equals("") ? "admin_1" : usuario);
                        this.getParamts().put("opcion", 1);
                    }
                } else {
                    if (this.tramite.getTipoTramite().getAbreviatura().equals("PRORESOLBT")) {
                        String usuario = clienteService.getrolsUser(RolUsuario.jefeRentas);
                        this.getParamts().put("rentas", usuario.equals("") ? "admin_1" : usuario);
                        this.getParamts().put("opcion", 1);
                    }
                }
            } else {
                //rechazar
                solicitudExoneracion.setEstado(new FnEstadoExoneracion(4L));
                solicitudExoneracion.setFechaAprobacion(new Date());
                solicitudExoneracionService.edit(solicitudExoneracion);
                this.getParamts().put("opcion", 0);
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
//            enviarCorreo();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "completas tareas", ex);
        }
    }

    public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

}
