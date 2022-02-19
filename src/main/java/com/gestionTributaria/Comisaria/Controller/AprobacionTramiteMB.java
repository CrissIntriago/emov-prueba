/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class AprobacionTramiteMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;

    private String data;
    private Boolean opc = Boolean.FALSE;
    private ComisariaRegistros comisaria;

    private List<Usuarios> funcionario;
    private Usuarios servidor;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                comisaria = new ComisariaRegistros();

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarFuncionarios() {
        String codigo = "";
        if (data.equals("CONSTRUCCION")) {
            codigo = "38";
        }
        if (data.equals("AMBIENTE")) {
            codigo = "";
        }
        if (data.equals("VIA_PUBLICA")) {
            codigo = "42";
        }
        if (data.equals("INQUILINATO")) {
            codigo = "41";
        }
        if (data.equals("FUNCIONAMIENTO")) {
            codigo = "40";
        }
        if (data.equals("SOLARES_VACIOS")) {
            codigo = "39";
        }
        funcionario = clienteService.getListClientesByCodeRol(codigo);
        System.out.println("FUNCIONARIO: " + funcionario.toString());
    }

//    public void actualizarObservacion() {
//        observacion.setObservacion(data);
//        cargarFuncionarios();
//    }

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
            getParamts().put("aprobacion", opc ? 1 : 0);
            if (opc) {
                if (getData() == null || getData().isEmpty()) {
                    JsfUtil.addWarningMessage("Aviso", "Seleccione el tipo de tramite al que lo va rediriguir");
                    return;
                }
                getParamts().put("tipo_comisaria", data);
                //enviar al comisario asignado de data que tenga ese rol 
                String usuario = servidor.getUsuario();
                getParamts().put("usuarioComisario", usuario.equals("") ? "admin_1" : usuario);
            } else {
                Observaciones ob = tramite.getObservaciones().get(0);
                getParamts().put("usuario", ob.getUserCre());
            }
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

    public String getUserComisaria(String codigo) {
        int[] muList = {2, 2, 2};
        switch (codigo) {
            case "CONST":
                return clienteService.getrolsUser(RolUsuario.comisarioConstruccion);
            case "VIP":
                return clienteService.getrolsUser(RolUsuario.comisarioViaPublica);
            case "INQUI":
                return clienteService.getrolsUser(RolUsuario.comisarioInquilinato);
            case "PER_FUN":
                return clienteService.getrolsUser(RolUsuario.comisarioPermisoFuncionamiento);
            case "SOlVA":
                return clienteService.getrolsUser(RolUsuario.comisarioSolaresVacios);
        }
        return "";
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getOpc() {
        return opc;
    }

    public void setOpc(Boolean opc) {
        this.opc = opc;
    }

    public ComisariaRegistros getComisaria() {
        return comisaria;
    }

    public void setComisaria(ComisariaRegistros comisaria) {
        this.comisaria = comisaria;
    }

    public List<Usuarios> getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(List<Usuarios> funcionario) {
        this.funcionario = funcionario;
    }

    public Usuarios getServidor() {
        return servidor;
    }

    public void setServidor(Usuarios servidor) {
        this.servidor = servidor;
    }
//</editor-fold>

}
