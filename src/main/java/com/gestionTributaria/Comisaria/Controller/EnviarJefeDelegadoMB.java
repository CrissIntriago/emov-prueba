/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.CatalogoService;
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
@Named(value = "enviarJefeDelegadoMB")
@ViewScoped
public class EnviarJefeDelegadoMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private CatalogoService catalogoService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ClienteService clienteService;

    private CatalogoItem comisariaSelect;
    private UploadedFile file;
    private List<Usuarios> listClientes;
    private Boolean permisoFuncionamiento = Boolean.FALSE;
    private Usuarios clienteSelect;
    private Integer opc = 0;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                comisariaSelect = catalogoService.getItemByCatalogoAndCodigo("GT_lista_comisarias", (String) getVariable("tipo_comisaria"));
                //poner codigo de delegados de comisaria permiso de funcionamiento 
//                listClientes = clienteService.getListClientesByCodeRol(RolUsuario.presupuesto);
//                permisoFuncionamiento = comisariaSelect.getCodigo().equals("FUNCIONAMIENTO");
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

    public void opendlg(Integer opc) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void enviarJefeDelegado() {

    }

    public void completarTarea() {
        try {
            if (opc == 0) {
                String comisariaPermisoFuncionamiento = clienteService.getrolsUser(RolUsuario.comisarioPermisoFuncionamiento);
                getParamts().put("usuarioDelegado", comisariaPermisoFuncionamiento.equals("") ? "admin_1" : comisariaPermisoFuncionamiento);
                getParamts().put("irJefeDelegado", 0);

            }
            if (opc == 1) {
                String usuario = clienteService.getrolsUser(RolUsuario.jefeDelegado);
                getParamts().put("usuarioJefeDelegado", usuario.equals("") ? "admin_1" : usuario);
                getParamts().put("irJefeDelegado", 1);
            }
            if (opc == 2) {
                getParamts().put("irJefeDelegado", 2);
                Observaciones ob = tramite.getObservaciones().get(0);
                getParamts().put("usuario", ob.getUserCre());
            }

//            if (clienteSelect == null) {
//                JsfUtil.addWarningMessage("Información", "Seleccionar un delegado.");
//                return;
//            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public CatalogoItem getComisariaSelect() {
        return comisariaSelect;
    }

    public void setComisariaSelect(CatalogoItem comisariaSelect) {
        this.comisariaSelect = comisariaSelect;
    }

    public List<Usuarios> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Usuarios> listClientes) {
        this.listClientes = listClientes;
    }

    public Boolean getPermisoFuncionamiento() {
        return permisoFuncionamiento;
    }

    public void setPermisoFuncionamiento(Boolean permisoFuncionamiento) {
        this.permisoFuncionamiento = permisoFuncionamiento;
    }

    public Usuarios getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Usuarios clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

    public Integer getOpc() {
        return opc;
    }

    public void setOpc(Integer opc) {
        this.opc = opc;
    }

}
