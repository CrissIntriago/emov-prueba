/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.auth.services.ModuloEjb;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.FirmaElectronicaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.ws.IrisService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named
@ViewScoped
public class CuentaController implements Serializable {

    @Inject
    private IrisService irisService;
    @Inject
    private UserSession us;
    @Inject
    private FirmaElectronicaService firmaService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private ModuloEjb moduloEjb;
    private Usuarios usuario;

    //sw
    private FirmaElectronica firmaElectronica;
    private Boolean firmaValidada;
    private String claveFirma;
    private String pass1;
    private String pass2;

    @PostConstruct
    public void init() {
        claveFirma = "";
        if (us.getFirmaElectronica() != null) {
            firmaElectronica = us.getFirmaElectronica();
        } else {
            firmaElectronica = new FirmaElectronica();
        }
    }

    public void guardarActualizarFirmaElectronica() {
        if (firmaValidada) {
            firmaElectronica.setClave(Utils.encriptaEnMD5(claveFirma));
            firmaElectronica.setEstado(Boolean.TRUE);
            if (firmaElectronica.getTipoFirma() == null) {
                firmaElectronica.setTipoFirma("QR");
            }
            if (firmaElectronica.getId() == null) {
                firmaElectronica.setFechaCreacion(new Date());
                firmaElectronica.setUsuario(new Usuarios(us.getUsuario().getId()));
//                firmaElectronica = (FirmaElectronica) irisService.methodPOST(firmaElectronica, SisVars.ws + "firmaElectronicas/guardar", FirmaElectronica.class);
                firmaElectronica = firmaService.create(firmaElectronica);
            } else {
                firmaService.edit(firmaElectronica);
//                firmaElectronica = (FirmaElectronica) irisService.methodPUT(firmaElectronica, SisVars.ws + "firmaElectronicas/actualizar", FirmaElectronica.class);
            }
            JsfUtil.addInformationMessage("Información", "Datos actualizados correctamente");
            us.setFirmaElectronica(firmaElectronica);
            firmaValidada = Boolean.FALSE;
        } else {
            JsfUtil.addErrorMessage("ERROR", "Debe validar la firma para poder editarla");
        }
    }

    public void upload(FileUploadEvent files) {
        try {
            File f = FilesUtil.copyFileServer1(files, SisVars.rutaFirmasElectronicas);
            firmaElectronica.setArchivo(f.getAbsolutePath());
        } catch (IOException e) {
            JsfUtil.addErrorMessage("ERROR", "Ocurrió un error al subir el archivo de la Firma Electrónica");
        }
    }

    public void cambiarContraseña() {

        // setVer(false);
        pass1 = null;
        pass2 = null;
        this.usuario = us.getUsuario(); // dlgCambioClave
        JsfUtil.executeJS("PF('dlgCambioClave').show();");

        //userSession.setServicioContrasenia(true);
        // JsfUtil.redirectFaces("/recuperar/contrasenia");
    }

    public void validarFirmaElectronica() {
        if (firmaElectronica.getArchivo() != null && claveFirma != null && !claveFirma.isEmpty()) {
            firmaElectronica.setClave(claveFirma);
            try {
                FirmaElectronica firmaElectronicaValidar = new FirmaElectronica();
                firmaElectronicaValidar.setArchivo(firmaElectronica.getArchivo());
                firmaElectronicaValidar.setClave(claveFirma);
                firmaElectronicaValidar = (FirmaElectronica) irisService.methodPOST(firmaElectronicaValidar, SisVars.wsFirmaEC + "firmaElectronica/validar", FirmaElectronica.class);
                if (firmaElectronicaValidar.getUid() != null) {
                    firmaValidada = Boolean.TRUE;
                    firmaElectronica.setUbicacion("");
                    firmaElectronica.setUid(firmaElectronicaValidar.getUid());
                    firmaElectronica.setEstadoFirma(firmaElectronicaValidar.getEstadoFirma());
                    firmaElectronica.setCn(firmaElectronicaValidar.getCn());
                    firmaElectronica.setEmision(firmaElectronicaValidar.getEmision());
                    firmaElectronica.setFechaEmision(firmaElectronicaValidar.getFechaEmision());
                    firmaElectronica.setFechaExpiracion(firmaElectronicaValidar.getFechaExpiracion());
                    firmaElectronica.setIsuser(firmaElectronicaValidar.getIsuser());
                    JsfUtil.addInformationMessage("Información", "Firma electrónica validada");
                } else {
                    firmaValidada = Boolean.FALSE;
                    JsfUtil.addErrorMessage("Error", "No se pudo validar su firma electrónica");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsfUtil.addErrorMessage("Error", "Debe subir el archivo P12 y escribir su clave");
        }

    }

    public void reloadMenus() {
        moduloEjb.clearReloadMenu();
        this.reloadConfig();
        JsfUtil.redirect(JsfUtil.getHostContextUrl() + "user/account");
    }

    public void reloadConfig() {
        this.moduloEjb.getCache().loadApplicationVariables();
    }

    public void actualizarClave() {
        if (!pass1.equals(pass2)) {
            JsfUtil.addErrorMessage("Contraseña", "La contraseña no coinciden");
            return;
        }
        DefaultPasswordService passwordService = new DefaultPasswordService();
        usuario.setContrasenia(passwordService.encryptPassword(pass1));
        usuario = usuarioService.save(usuario);
        if (usuario != null) {
            JsfUtil.addInformationMessage("Usuario", "Datos almacenados correctamente");
        } else {
            JsfUtil.addErrorMessage("Usuario", "Ocurrio un error al guardar los datos del usuario");
        }
        this.usuario = new Usuarios();
        this.usuario.setFuncionario(new Servidor());
        this.usuario.getFuncionario().setPersona(new Cliente());
        pass1 = null;
        pass2 = null;
        reloadMenus();
        PrimeFaces.current().executeScript("PF('dlgCambioClave').hide();");
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public Boolean getFirmaValidada() {
        return firmaValidada;
    }

    public void setFirmaValidada(Boolean firmaValidada) {
        this.firmaValidada = firmaValidada;
    }

    public String getClaveFirma() {
        return claveFirma;
    }

    public void setClaveFirma(String claveFirma) {
        this.claveFirma = claveFirma;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

}
