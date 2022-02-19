/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
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
@Named(value = "notificacionReformaReducView")
@ViewScoped
public class NotificacionReformaReduccionController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaCupoReduccionService cupoReduccionService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    private List<Cliente> usuarioCorreos;

    private List<UnidadAdministrativa> unidadesConCupo;
    private ReformaIngresoSuplemento r = new ReformaIngresoSuplemento();

    @PostConstruct
    public void inicializar() {

        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);

                unidadesConCupo = new ArrayList();
                r = new ReformaIngresoSuplemento();
                r = cupoReduccionService.getReforma(BigInteger.valueOf(tramite.getNumTramite()));
                unidadesConCupo = cupoReduccionService.getCuposDetalleUnidades(r);
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
        List<CupoReduccion> entidadCupos = new ArrayList<>();

        List<String> userName = new ArrayList<>();
        entidadCupos = cupoReduccionService.getCuposDetalleActual(r);

        for (CupoReduccion item : entidadCupos) {

            if (!isNullOrEmpty(item.getUsuario())) {
                userName.add(item.getUsuario());
            }

        }

        for (String data : userName) {
            Cliente c = new Cliente();
            c = reservaCompromisoService.devuelveClienteNotitfacion2(data);
            if (c.getEmail() != null) {
                enviarCorreo(c.getEmail(), "REFORMA DE TIPO " + (r.getTipo() ? "SUPLEMENTO" : "REDUCCIÓN"), c.getNombreCompleto(), r.getPeriodo());
            }
        }

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
                + "<p style=\"width:200px;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE LA REFORMA PARA EL AÑO" + anio
                + " HA SIDO APROBADO CON EXITO"
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + email + " relacionada con: " + userStart);

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

    public List<UnidadAdministrativa> getUnidadesConCupo() {
        return unidadesConCupo;
    }

    public void setUnidadesConCupo(List<UnidadAdministrativa> unidadesConCupo) {
        this.unidadesConCupo = unidadesConCupo;
    }
//</editor-fold>

}
