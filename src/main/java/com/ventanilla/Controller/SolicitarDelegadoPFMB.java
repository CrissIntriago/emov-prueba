/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Comisaria.Service.OrdernesService;
import com.gestionTributaria.Entities.Ordenes;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "solicitarDelegadoPFMB")
@ViewScoped
public class SolicitarDelegadoPFMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private OrdernesService ordenesService;
    private Boolean opc = Boolean.FALSE;
    private UploadedFile file;
    private List<Usuarios> listClientes;
    private Usuarios clienteSelect;
    private Ordenes ordenes;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listClientes = clienteService.getListClientesByCodeRol(RolUsuario.presupuesto);

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void openDialogUpload() {
        PrimeFaces.current().executeScript("PF('dlgoUpload').show()");
        PrimeFaces.current().ajax().update("formUpload");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        uploadDoc.upload(tramite, file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        PrimeFaces.current().executeScript("PF('dlgoUpload').hide()");
    }

    public void opendlg(Boolean opc) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            Boolean bandera = false;
            String usuario = "";
            if (opc) {//continuar
                ordenes = ordenesService.findByOrden(tramite.getNumTramite());
//                if (ordenes == null) {
//                    bandera = false;
//                    JsfUtil.addInformationMessage("", "Debe generar una Orden");
//                } else {
                    bandera = true;
                    usuario = "";
                    usuario = clienteService.getrolsUser(RolUsuario.jefeDelegado);
                    getParamts().put("usuarioJefeDelegado", usuario.equals("") ? "admin_1" : usuario);
                    getParamts().put("tipo", 0);
//                }
            } else {//correccion
                bandera = true;
                usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
                getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
                getParamts().put("tipo", 1);
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (bandera == true) {
                if (saveTramite() == null) {
                    return;
                }
                this.session.setVarTemp(null);
                super.completeTask((HashMap<String, Object>) getParamts());
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public Boolean getOpc() {
        return opc;
    }

    public void setOpc(Boolean opc) {
        this.opc = opc;
    }

    public List<Usuarios> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Usuarios> listClientes) {
        this.listClientes = listClientes;
    }

    public Usuarios getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Usuarios clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

}
