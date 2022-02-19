/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.config;

import com.origami.sigef.common.entities.AclLogin;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AclLoginService;
import com.origami.sigef.common.service.FirmaElectronicaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import ec.gob.duran.ws.DuranService;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dairon
 */
@Named
@ViewScoped
public class ShiroAuthoritySecurity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ShiroAuthoritySecurity.class);

    @Inject
    private UsuarioService usuarioService;
    @Inject
    private UserSession userSession;
    @Inject
    private AclLoginService aclLoginService;
    @Inject
    private FirmaElectronicaService firmaService;
    @Inject
    private DuranService duranService;

    private String username;
    private String password;
    private boolean rememberMe;
   

    private Usuarios usuario;
    private AclLogin aclLogin;

    public ShiroAuthoritySecurity() {

    }

    /**
     * Try and authenticate the user
     */
    public void doLogin() {

        UsernamePasswordToken token = null;
        try {
            Subject subject = SecurityUtils.getSubject();
//            DefaultPasswordService passwordService = new DefaultPasswordService();
//            System.out.println("pass: " + passwordService.encryptPassword(pass));
            token = new UsernamePasswordToken(getUsername(), getPassword());
            token.setRememberMe(isRememberMe());

            subject.login(token);

            if (subject.getPrincipal() != null && !subject.getPrincipal().toString().isEmpty()) {
//                String tokenWs = ""; //duranService.autenticate(username, password);
//                userSession.setToken(tokenWs);
//                System.out.println("token ws login:" + tokenWs);
                usuario = usuarioService.findByUsuario(subject.getPrincipal().toString());
                if (usuario != null) {
                    userSession.setNameUser(usuario.getUsuario());
                    userSession.setUserId(usuario.getId());
                    userSession.setRoles(usuario.getRoles());
                    if (usuario.getCaducarClave() == null) {
                        usuario.setCaducarClave(false);
                    }
                    if (usuario.getCaducarClave()) {
                        if (usuario.getFechaCaducidad() == null) {
                            usuario.setFechaCaducidad(Utils.sumarRestarDiasFecha(usuario.getFechaIngreso(), usuario.getDiasCaducidad()));
                        }
                        if (usuario.getFechaCaducidad().compareTo(new Date()) < 0) {
                            facesError("Su contraseña ha caducado, acérquse donde el administrador del sistema para solicitar contraseña temporal.");
                            JsfUtil.redirectFaces("/recuperar/contrasenia");
                            subject.logout();
                            System.out.println("subject.getSession().getHost() " + subject.getSession().getHost());
                            return;
                        }
                    }
                    if (usuario.getFuncionario() != null && usuario.getFuncionario().getPersona() != null) {
                        if (usuario.getFuncionario().getFechaSalida() != null) {
                            if (usuario.getFuncionario().getFechaSalida().compareTo(new Date()) < 0) {
                                facesError("Usuario se encuentra inactivo por fecha de salida registrada.");
                                subject.logout();
                                return;
                            }
                        }
                        userSession.setNombrePersonaLogeada(usuario.getFuncionario().getPersona().getNombreCompleto());
                    } else {
                        userSession.setNombrePersonaLogeada("");
                    }
                    aclLogin = new AclLogin();
                    aclLogin.setIpUserSession(userSession.getDatosEquipo());
                    aclLogin.setUserSessionId(BigInteger.valueOf(usuario.getId()));
                    aclLogin.setUserSessionName(userSession.getNameUser());
                    aclLogin.setFechaDoLogin(new Date());
                    aclLogin.setMacClient(userSession.getMACAddressEquipo());
                    aclLogin.setEvento("Login Web App");
                    try {
                        aclLogin = aclLoginService.create(aclLogin);
                        userSession.setAclLogin(aclLogin);
                    } catch (Exception e) {
                        System.out.println("Error al crear acllogin " + e.getMessage());
                    }
                    if (usuario.getEmpresaId() == null) {
                        usuario.setEmpresaId(this.usuarioService.findEmpresa(usuario));
                    }
                    Utils.isNotEmpty(usuario.getRoles());
                    userSession.setUsuario(usuario);
                    FirmaElectronica firma = firmaService.getFirmaByUsuario(usuario);
                    userSession.setFirmaElectronica(firma);
                }
                // Para renderizar los componente en las vistas
                JsfUtil.setSessionBean("userSession", userSession);
            }

            FacesContext.getCurrentInstance().getExternalContext().redirect("procesos/bandeja-tareas");

        } catch (UnknownAccountException ex) {
            facesError("Usuario Incorrecto");
        } catch (IncorrectCredentialsException ex) {
            facesError("Contraseña Incorrecta");
        } catch (LockedAccountException ex) {
            facesError("Usuario Bloqueado");
        } catch (AuthenticationException | IOException ex) {
            facesError("Error: " + ex.getMessage());
        } finally {
            if (token != null) {
                token.clear();
            }
        }
    }

    public void cambiarContraseña() {

//        // setVer(false);
//        pass1 = null;
//        pass2 = null;
//        this.usuario = userSession.getUsuario(); // dlgCambioClave
//        JsfUtil.executeJS("PF('dlgCambioClave').show();");

        //userSession.setServicioContrasenia(true);
        // JsfUtil.redirectFaces("/recuperar/contrasenia");
    }

    public void recuperarContraseña() {
        if (username.equals("")) {
            JsfUtil.addErrorMessage("ERROR!!!", "Ingrese el nombre de su usuario");
            return;
        }
        Usuarios usuario = usuarioService.findByUsuario(username);
        //valida si el usuario existe
        if (usuario == null) {
            JsfUtil.addErrorMessage("ERROR!!!", "No existe el usuario con el que intenta ingresar " + username);
            return;
        }
        //validar si esta activo el servidor publico
        if (usuario.getFuncionario() != null && usuario.getFuncionario().getPersona() != null) {
            if (usuario.getFuncionario().getFechaSalida() != null) {
                if (usuario.getFuncionario().getFechaSalida().compareTo(new Date()) < 0) {
                    facesError("Usuario se encuentra inactivo por fecha de salida registrada.");
                    return;
                }
            }
            userSession.setNombrePersonaLogeada(usuario.getFuncionario().getPersona().getNombreCompleto());
        }
        userSession.setServicioContrasenia(false);
        userSession.setNameUser(username);
//        System.out.println("ESTE: " + userSession.getServicioContrasenia());
//        JsfUtil.redirect(CONFIG.URL_APP + "/recuperar/contrasenia");

        JsfUtil.redirectFaces("/recuperar/contrasenia");
    }

    private void facesError(String message) {
        JsfUtil.addWarningMessage("Advertencia", message);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String senha) {
        this.password = senha;
    }    
    
    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void logout(String url) {
        Subject currentUser = SecurityUtils.getSubject();
        if (userSession.getAclLogin() != null) {
            userSession.getAclLogin().setFechaDoLogout(new Date());
            if (userSession.getAclLogin().getId() != null) {
                aclLoginService.edit(userSession.getAclLogin());
            }
        }
        currentUser.logout();
        JsfUtil.redirectFaces(url);
    }
}
