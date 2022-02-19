/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ProcesoServidor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.ProcesoServidorService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.ObservacionesService;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Origami
 */
@Named(value = "InfoBajaServidorView")
@ViewScoped
public class InfoBajaServidor extends BpmnBaseRoot implements Serializable {

    @Inject
    private ServidorService servidorService;
    private Servidor servidor;
    @Inject
    private CatalogoItemService catalogoItemService;
    private List<CatalogoItem> tipoMotivo;

    @Inject
    private ProcesoServidorService procesoServidorService;
    private ProcesoServidor procesoServidor;

    @Inject
    private ObservacionesService observacionSevice;

    private String observaciones;

    @Inject
    private ClienteService clienteService;

    @PostConstruct
    public void inicializate() {
        if (!JsfUtil.isAjaxRequest()) {
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
            //getTramite().getNumTramite()
//             servidorServices.findByNumTramite(getTramite().getNumTramite());
//            Long variable = (Long) this.getEngine().getVariableByProcessInstance(
//                    this.getEngine().getTaskDataByTaskID(session.getTaskID()).getProcessInstanceId(), "idServidor");

        }
        servidor = new Servidor();
        tipoMotivo = new ArrayList<>();
        tipoMotivo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "MOTIVO-SERVIDOR");
        procesoServidor = new ProcesoServidor();
    }

    public void buscarServidor() {
        Utils.openDialog("/facelet/talentoHumano/dialogServidor", "860", "410");
    }

    public void selectDataSer(SelectEvent evt) {
        servidor = new Servidor();
        servidor = (Servidor) evt.getObject();
//        System.out.println(servidor.getPersona().getNombreCompleto());
        procesoServidor.setServidorP(servidor);
        procesoServidor.setnTramite(new BigInteger("" + tramite.getNumTramite()));

    }

    public void ingObservacion() {
        if (servidor.getId() == null) {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar un Servidor Público para Dar de baja.");
            return;
        }
        if (servidor.getFechaSalida() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar Fecha de Baja de servidor publico.");
            return;
        }
        if (servidor.getFechaSalida().compareTo(new Date()) >= 0) {

            JsfUtil.addErrorMessage("Error", "La Fecha de Salida no debe ser menor a la fecha Actual.");
            return;
        }

        if (procesoServidor.getMotivoSalida() == null) {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar un Motivo para baja del Servidor Público.");
            return;
        }
        if (procesoServidor.getFechaDoc() == null) {
            JsfUtil.addErrorMessage("Error", "Debe Dar Fecha Del Documento Cargado para baja del Servidor Público.");
            return;
        }
        if (procesoServidor.getFechaDoc().compareTo(servidor.getFechaIngreso()) <= 0) {
            JsfUtil.addErrorMessage("Error", "La Fecha de Salida del Documento no debe ser menor a la fecha de Ingreso del Servidor.");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        observacion.setIdTramite(tramite);

        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completar() {
        try {
            observacion.setObservacion(observaciones);
            servidor.setUsuarioModifica(session.getName());
            servidor.setFechaModifica(new Date());
            servidorService.edit(servidor);
            procesoServidor.setEstadoProceso("COMPLETO");
            procesoServidorService.create(procesoServidor);
            getParamts().put("usuarioGuardaAlmacen", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
            getParamts().put("idServidor", session.getUserId());
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }
    //<editor-fold defaultstate="collapsed" desc="get and set">

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public List<CatalogoItem> getTipoMotivo() {
        return tipoMotivo;
    }

    public void setTipoMotivo(List<CatalogoItem> tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
    }

    public ProcesoServidor getProcesoServidor() {
        return procesoServidor;
    }

    public void setProcesoServidor(ProcesoServidor procesoServidor) {
        this.procesoServidor = procesoServidor;
    }
//</editor-fold>

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
