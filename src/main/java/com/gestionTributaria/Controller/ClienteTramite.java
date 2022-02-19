/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Commons.VerCedulaUtils;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ClienteTramite implements Serializable {

    @Inject
    private UserSession user;
    @Inject
    private ManagerService services;
    protected String emailNew = "";
    protected String tlfnNew = "";
    protected VerCedulaUtils vcu = new VerCedulaUtils();
    protected Cliente persona = new Cliente();
    private Map<String, Object> param;

    public void inicializarVariables() {
        emailNew = "";
        tlfnNew = "";
        persona = new Cliente();
        param = new HashMap<>();
    }

    public Boolean existeEnte(String ciruc) {
        param = new HashMap<>();
        param.put("identificacion", ciruc);
        Cliente temp = (Cliente) services.findByParameter(Cliente.class, param);
        return temp != null;
    }

    public Boolean validarEnte(Cliente e) {
        if (e.getIdentificacion() != null) {
            if ("PER_NA".equals(e.getTipoProv().getCodigo())) {
                return ((e.getApellido() != null) && (e.getNombre() != null));
            } else {
                return e.getRazonSocial() != null;
            }
        } else {
            return false;
        }
    }

//    public Boolean guardarCliente() {
//        Boolean flag = false;
//        try {
//            if (this.validarEnte(persona)) {
//                if (persona.getEmail() != null) {
//                    persona.setUsuarioCreacion(user.getNameUser());
//                    persona.setFechaCreacion(new Date());
//                    persona = .guardarEnteCorreosTlfns(persona);
//                    flag = persona.getId() != null;
//                } else {
//                    JsfUtil.addErrorMessage("El usuario no tiene correcto", "");
//                }
//            } else {
//                JsfUtil.addErrorMessage("Los Campos no pueden estar vacios", "");
//            }
//        } catch (Exception e) {
//            flag = false;
//            System.err.println(e);
//        }
//        return flag;
//    }
//
//    public Boolean guardarClienteSinCorreo() {
//        Boolean flag = false;
//        try {
//            if (this.validarEnte(persona)) {
//                persona.setUserCre(session.getName_user());
//                persona.setFechaCre(new Date());
//                persona = manager.guardarEnteCorreosTlfns(persona);
//                flag = true;
//            } else {
//                JsfUti.messageError(null, Messages.faltanCampos, "");
//            }
//        } catch (Exception e) {
//            flag = false;
//            Logger.getLogger(ClienteTramite.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return flag;
//    }
//
//    public Boolean editarCliente() {
//        Boolean flag = false;
//        try {
//            if (this.validarEnte(persona)) {
//                if (!persona.getEnteCorreoCollection().isEmpty()) {
//                    persona.setUserMod(session.getName_user());
//                    persona.setFechaMod(new Date());
//                    flag = manager.editarEnteCorreosTlfns(persona);
//                } else {
//                    JsfUti.messageError(null, Messages.userSinCorreo, "");
//                }
//            } else {
//                JsfUti.messageError(null, Messages.faltanCampos, "");
//            }
//        } catch (Exception e) {
//            flag = false;
//            Logger.getLogger(ClienteTramite.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return flag;
//    }
//
//    public Boolean editarClienteSinCorreo() {
//        Boolean flag = false;
//        try {
//            if (this.validarEnte(persona)) {
//                persona.setUserMod(session.getName_user());
//                persona.setFechaMod(new Date());
//                flag = manager.editarEnteCorreosTlfns(persona);
//            } else {
//                JsfUti.messageError(null, Messages.faltanCampos, "");
//            }
//        } catch (Exception e) {
//            flag = false;
//            Logger.getLogger(ClienteTramite.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return flag;
//    }
//
//    public void buscarEnteDlg() {
//        Map<String, Object> options = new HashMap<>();
//        options.put("resizable", false);
//        options.put("draggable", false);
//        options.put("modal", true);
//        options.put("width", "75%");
//        options.put("position", "center");
//        options.put("closable", true);
//        options.put("closeOnEscape", true);
//        options.put("responsive", true);
//        options.put("contentWidth", "100%");
//        PrimeFaces.current().dialog().openDynamic("/resources/dialog/dialogEnte", options, null);
//    }

}
