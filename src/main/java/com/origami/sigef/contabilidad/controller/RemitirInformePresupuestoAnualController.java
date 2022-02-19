/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CupoPresupuesto;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "remmitirInformeView")
@ViewScoped
public class RemitirInformePresupuestoAnualController extends BpmnBaseRoot implements Serializable {

    @Inject
    private KatalinaService katalinaService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    private List<Cliente> usuarioCorreos;
    private Short periodoConsultado;
    List<UnidadAdministrativa> unidadesConCupo;

    @PostConstruct
    public void inicializar() {

        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodoConsultado = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                unidadesConCupo = new ArrayList();
                unidadesConCupo = cupoPresupuestoService.getUnidadesConCupos(BigInteger.valueOf(tramite.getNumTramite()));
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public void enviarNotitifacion() {
        List<CupoPresupuesto> entidadCupos = new ArrayList<>();
        entidadCupos = cupoPresupuestoService.getEntidadConCupos(BigInteger.valueOf(tramite.getNumTramite()));
        List<String> userName = new ArrayList();
        for (CupoPresupuesto item : entidadCupos) {

            if (!isNullOrEmpty(item.getUserNameResponsable())) {
                userName.add(item.getUserNameResponsable());
            }

        }

        for (String data : userName) {
            Cliente c = new Cliente();
            c = reservaCompromisoService.devuelveClienteNotitfacion2(data);

            if (c != null && c.getId() != null && c.getEmail() != null) {
                enviarCorreo(c.getEmail(), "PRESUPUESTO ANUAL ", c.getNombreCompleto(), periodoConsultado);
            }

        }
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada a o los usuario(s): " + userName);
    }

    public void enviarCorreo(String email, String asunto, String userStart, Short anio) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE EL PRESUPUESTO DEL AÑO " + anio
                + " HA SIDO APROBADO CON EXITO"
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");

        katalinaService.enviarCorreo(c);

    }

    public void completarTarea() {
        try {
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            observacion.setObservacion("ok");
            if (saveTramite() == null) {
                return;
            }
            super.completeTask((HashMap<String, Object>) getParamts());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("INFORMACIÓN", "LA TARA HA FINALIZADO CORRECTAMENTE");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public List<Cliente> getUsuarioCorreos() {
        return usuarioCorreos;
    }

    public void setUsuarioCorreos(List<Cliente> usuarioCorreos) {
        this.usuarioCorreos = usuarioCorreos;
    }

    public Short getPeriodoConsultado() {
        return periodoConsultado;
    }

    public void setPeriodoConsultado(Short periodoConsultado) {
        this.periodoConsultado = periodoConsultado;
    }

    public List<UnidadAdministrativa> getUnidadesConCupo() {
        return unidadesConCupo;
    }

    public void setUnidadesConCupo(List<UnidadAdministrativa> unidadesConCupo) {
        this.unidadesConCupo = unidadesConCupo;
    }
//</editor-fold>

}
