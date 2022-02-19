/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.config;

import com.origami.sigef.auth.services.MantenimientoClaveService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AclLogin;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.AclLoginService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.entities.MantenimientoClave;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Dairon
 */
@Named
@ViewScoped
public class RecoverPasword implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RecoverPasword.class.getName());

    @Inject
    private UsuarioService usuarioService;
    @Inject
    private UserSession userSession;
    @Inject
    private AclLoginService aclLoginService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private MantenimientoClaveService passwordService;

    private String username;
    private String password;
    private String password1;

    private Usuarios usuario;
    private AclLogin aclLogin;
    private Boolean existe = false;
    private Boolean activarVerificacion;
    private String email;
    private String nombreServicio;
    private String codigoVerificacion;
    private String idenficacion;
    private MantenimientoClave cambiarPassword;
    private Boolean btnSeguir;
    private Boolean TOKEN;

    private Boolean tokenServicio;

    @PostConstruct
    public void initialize() {
        btnSeguir = true;
        if (userSession != null) { 
            if (userSession.getNameUser() != null) {
                username = userSession.getNameUser();
                tokenServicio = userSession.getServicioContrasenia();
            }
            usuario = usuarioService.findByUsuario(username);
            cambiarPassword = passwordService.findByUsuario(username, tokenServicio);
            if (cambiarPassword != null) {
                activarVerificacion = Boolean.TRUE;
                email = cambiarPassword.getCorreoCodificado();
            } else {
                activarVerificacion = Boolean.FALSE;
                cambiarPassword = new MantenimientoClave();

            }
        }
        definirNombreServicio();
        System.out.println("activarVerificacion: " + activarVerificacion);
    }

    private void definirNombreServicio() {
        if (tokenServicio) {
            nombreServicio = "CAMBIO DE CONTRASEÑA";
        } else {
            nombreServicio = "RECUPERACION DE CONTRASEÑA";
        }
    }

    public void validarCodigoVerificacion() {
        if (cambiarPassword.getCodigoVerificacion().equals(codigoVerificacion)) {
            JsfUtil.addSuccessMessage("Código Verificación", "El código de verificación es valido");
            TOKEN = Boolean.TRUE;
        } else {
            JsfUtil.addWarningMessage("Código Verificación", "El código de verificación es invalido");
            TOKEN = Boolean.FALSE;
            return;
        }
    }

    public void generarCodigoVerificacion() {
        String codigo = Utils.codigoVerificacion();
        /*determinar correo*/
        if (usuario.getFuncionario().getEmailInstitucion() != null) {
            email = usuario.getFuncionario().getEmailInstitucion();
        } else {
            email = usuario.getFuncionario().getPersona().getEmail();
        }
        /*Guardar Dato*/
        cambiarPassword = new MantenimientoClave(username, codigo, tokenServicio, new Date(), CONFIG.TIEMPO_EXPIRACION, email);
        passwordService.create(cambiarPassword);
        /*Enviar correo*/
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto("CÓDIGO DE VERIFICACION DE ORIGAMI-GT");
        c.setMensaje(formatoMensaje(usuario.getFuncionario().getPersona().getNombreCompleto(), codigo));
        c.setArchivos(null);
        katalinaService.enviarCorreo(c);
        /*actualizar paneles*/
        activarVerificacion = Boolean.TRUE;
        email = cambiarPassword.getCorreoCodificado();
    }

    private String formatoMensaje(String destinatario, String codigo) {
        return "<p>Estimado(a),</p>"
                + "<p><br></p>"
                + "<p><strong>" + destinatario + "&nbsp;</strong></p>"
                + "<p><br></p>"
                + "<p>Su clave de de verificacion es: <strong>" + codigo + "</strong></p>"
                + "<p><br></p><p><br></p>"
                + "<p><strong>NOTA:</strong> Este codigo de verificacion tiene vigencia durante " + CONFIG.TIEMPO_EXPIRACION + " hora, en caso de expirar debera generar una nueva clabe.</p><p><br></p>";
    }

    /**
     * Try and authenticate the user
     */
    public void doLogin() {
        Subject subject = SecurityUtils.getSubject();
        DefaultPasswordService passService = new DefaultPasswordService();
        //validar si los campos estan llenos
        if (password == null) {
            facesError("Debe ingresar la contraseña");
            return;
        }
        if (password1 == null) {
            facesError("Debe ingresar la contraseña");
            return;
        }
        //validar si las contraseñas son iguales
        if (!password.equals(password1)) {
            facesError("Las contraseñas no conciden");
            return;
        }
        if (passService.passwordsMatch(password, usuario.getContrasenia())) {
            facesError("Las contraseña ingresada es igual a la contraseña anterior");
            return;
        }
        if (barreraControl()) {
            JsfUtil.addErrorMessage("ERROR", "Al recuperar la contraseña, vuelva a intentarlo");
            subject.logout();
            JsfUtil.redirect(CONFIG.URL_APP + "login");
            return;
        }
        save();
    }

    public void doLoginRecuperar() {
        if (barreraControl()) {
            JsfUtil.addErrorMessage("ERROR", "Al cambiar la contraseña, vuelva a intentarlo");
            JsfUtil.redirect(CONFIG.URL_APP + "login");
            return;
        }
        password1 = Utils.contraseniaTemporal();
        save();
        String mensaje = "<p>Estimado(a),</p>"
                + "<p><br></p>"
                + "<p><strong>" + usuario.getFuncionario().getPersona().getNombreCompleto() + "&nbsp;</strong></p>"
                + "<p><br></p>"
                + "<p>Su contraseña temporal es: </strong>" + password1 + "</strong></p>"
                + "<p>Recuerde cambiar su contraseña lo mas antes posible</p>"
                + "<p><br></p><p><br></p>";
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto("RECUPERACIÓN DE CONTRASEÑA DE ORIGAMI-GT");
        c.setMensaje(mensaje);
        c.setArchivos(null);
        katalinaService.enviarCorreo(c);
        cambiarPassword.setActivo(Boolean.FALSE);
        passwordService.edit(cambiarPassword);
        JsfUtil.addSuccessMessage("Contraseña", "Se cambio y se envìo la contraseña correctamente");
        JsfUtil.redirect(CONFIG.URL_APP + "login");
    }

    public void cancelar() {
        userSession.setServicioContrasenia(false);
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    private Boolean barreraControl() {
        if (!idenficacion.equals(usuario.getFuncionario().getPersona().getIdentificacion())) {
            Correo c = new Correo();
            c.setDestinatario(email);
            c.setAsunto("ORDEN DE PAGO");
            c.setMensaje(formatoMensaje_2());
            c.setArchivos(null);
            katalinaService.enviarCorreo(c);
            cambiarPassword.setActivo(Boolean.FALSE);
            passwordService.edit(cambiarPassword);
            return true;
        } else {
            return false;
        }
    }

    private void save() {
        Subject subject = SecurityUtils.getSubject();
        DefaultPasswordService passService = new DefaultPasswordService();
        try {
            if (usuario != null) {
                usuario.setContrasenia(passService.encryptPassword(password1));
                usuario.setFechaCaducidad(Utils.sumarRestarDiasFecha(new Date(), usuario.getDiasCaducidad()));
                usuarioService.edit(usuario);
                cambiarPassword.setActivo(Boolean.FALSE);
                passwordService.edit(cambiarPassword);
                aclLogin = new AclLogin();
                aclLogin.setIpUserSession(userSession.getDatosEquipo());
                aclLogin.setUserSessionId(BigInteger.valueOf(usuario.getId()));
                aclLogin.setUserSessionName(userSession.getNameUser());
                aclLogin.setFechaDoLogin(new Date());
                aclLogin.setMacClient(userSession.getMACAddressEquipo());
                aclLogin.setEvento(nombreServicio + "WEB APP");
                aclLogin = aclLoginService.create(aclLogin);
                btnSeguir = true;
                if (tokenServicio) {
                    JsfUtil.addSuccessMessage("Contraseña", "Se cambio la contraseña correctamente");
                    subject.logout();
                    JsfUtil.redirect(CONFIG.URL_APP + "login");
                }
            } else {
                facesError("Usuario no encontrado.");
            }
        } catch (Exception ex) {
            facesError("Error: " + ex.getMessage());
            LOG.log(Level.SEVERE, username, ex);
        } finally {
        }
    }

    private String formatoMensaje_2() {
        return "<p>Estimado(a),</p>"
                + "<p><br></p>"
                + "<p><strong>" + usuario.getFuncionario().getPersona().getNombreCompleto() + "&nbsp;</strong></p>"
                + "<p><br></p>"
                + "<p>Estan intentando cambiar/recuperar su contraseña en el sistema de ORIGAMI-GT, desde la IP: " + userSession.getMACAddressEquipo() + "</p>"
                + "<p>Comuniquese con el Departamento de Sistema de su empresa o cambie la contraseña lo mas antes posible</p>"
                + "<p><br></p><p><br></p>";
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Boolean getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(Boolean TOKEN) {
        this.TOKEN = TOKEN;
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

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public Boolean getExiste() {
        return existe;
    }

    public void setExiste(Boolean existe) {
        this.existe = existe;
    }

    public Boolean getActivarVerificacion() {
        return activarVerificacion;
    }

    public void setActivarVerificacion(Boolean activarVerificacion) {
        this.activarVerificacion = activarVerificacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdenficacion() {
        return idenficacion;
    }

    public void setIdenficacion(String idenficacion) {
        this.idenficacion = idenficacion;
    }

    public Boolean getTokenServicio() {
        return tokenServicio;
    }

    public void setTokenServicio(Boolean tokenServicio) {
        this.tokenServicio = tokenServicio;
    }

}
