/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.WfUserSession;
import com.origami.sigef.common.entities.AclLogin;
import com.origami.sigef.common.entities.MenuRol;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Origami
 */
@Named
@SessionScoped
public class UserSession implements WfUserSession {

    /**
     *
     */
    @Inject
    private UnidadAdministrativaService unidadServices;
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(UserSession.class.getName());

    protected String nameUser;
    protected String actKey;
    protected String taskID;
    protected Long userId = (long) -1;
    protected String urlSolicitada;
    protected Boolean temp = false;
    protected String departamento;
    protected String nombrePersonaLogeada;
    protected Boolean esDirector;
    protected Object varTemp;
    protected String ipClient;
    protected String hostClient;
    protected String topPage;
    private String titlePage = "Sistema Administrativo";
    protected String perfil = "/resources/image/avatar.jpg";
    protected Boolean esSuperUser;
    protected List<UsuarioRol> roles = new ArrayList<>();
    protected List<Long> depts = new ArrayList<>();
    private AclLogin aclLogin = new AclLogin();
    private Long idAclLogin;
    private Pattern macpt = null;
    private Usuarios usuario;
    private Boolean servicioContrasenia;
    private FirmaElectronica firmaElectronica;
    private Long idDiario;
    private Long idComprobante;
    private Long idTransferencia;
    private Boolean viewDiario;
    private Boolean viewComprobante;
    private Boolean viewTransferencia;
    protected String token;
    
    public String getNombreBienvenida() {
        if (this.esLogueado()) {
            return this.getNombrePersonaLogeada();
        }
        return "invitado";
    }

    public Boolean esLogueado() {
        return userId != null && userId > 0L;
    }

    public String getDatosEquipo() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletRequest sr = (HttpServletRequest) ctx.getExternalContext().getRequest();
            String ip = sr.getHeader("X-Real-IP");
            if (null != ip && !"".equals(ip.trim())
                    && !"unknown".equalsIgnoreCase(ip)) {
                this.setIpClient(ip);
                return this.getIpClient();
            }
            sr.getHeader("X-FORWARDED-FOR");
            if (sr.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
                InetAddress localip = java.net.InetAddress.getLocalHost();
                this.setIpClient(localip.getHostAddress());
                this.hostClient = localip.getHostName();
            } else {
                this.setIpClient(sr.getRemoteAddr());
                this.hostClient = sr.getRemoteHost();
            }
//            try {
//                String[] HEADERS_TO_TRY = {
//                    "X-Forwarded-For",
//                    "Proxy-Client-IP",
//                    "WL-Proxy-Client-IP",
//                    "HTTP_X_FORWARDED_FOR",
//                    "HTTP_X_FORWARDED",
//                    "HTTP_X_CLUSTER_CLIENT_IP",
//                    "HTTP_CLIENT_IP",
//                    "HTTP_FORWARDED_FOR",
//                    "HTTP_FORWARDED",
//                    "HTTP_VIA",
//                    "REMOTE_ADDR"};
//                for (String header : HEADERS_TO_TRY) {
//                    System.out.println("//" + header + " = " + sr.getHeader(header));
//                }
//            } catch (Exception e) {
//                Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, e.getMessage());
//            }
            return this.getIpClient();
        } catch (UnknownHostException e) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, e.getMessage());
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String getMACAddressEquipo() {
        // Find OS and set command according to OS
        String OS = System.getProperty("os.name").toLowerCase();
        String[] cmd;
        if (OS.contains("win")) {
            // Windows
            macpt = Pattern.compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
            String[] a = {"arp", "-a", this.getIpClient()};
            cmd = a;
        } else {
            // Mac OS X, Linux
            macpt = Pattern.compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
            String[] a = {"arp", this.getIpClient()};
            cmd = a;
        }
        try {
            // Run command
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            // read output with BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            // Loop trough lines
//            System.out.println("Line " + line);
            while (line != null) {
                Matcher m = macpt.matcher(line);
                // when Matcher finds a Line then return it as result
                if (m.find()) {
                    //System.out.println("MAC: " + m.group(0));
                    return m.group(0);
                }
                line = reader.readLine();
            }
            if (line != null) {
                return line.toUpperCase();
            }
        } catch (IOException | InterruptedException e1) {
            LOG.log(Level.SEVERE, "", e1);
        }

        // Return empty string if no MAC is found
        return "";
    }

    public Boolean getServicioContrasenia() {
        return servicioContrasenia;
    }

    public void setServicioContrasenia(Boolean servicioContrasenia) {
        this.servicioContrasenia = servicioContrasenia;
    }

    public Long getIdDiario() {
        return idDiario;
    }

    public void setIdDiario(Long idDiario) {
        this.idDiario = idDiario;
    }

    public Long getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public Long getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(Long idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public Boolean getViewComprobante() {
        return viewComprobante;
    }

    public void setViewComprobante(Boolean viewComprobante) {
        this.viewComprobante = viewComprobante;
    }

    public Boolean getViewTransferencia() {
        return viewTransferencia;
    }

    public void setViewTransferencia(Boolean viewTransferencia) {
        this.viewTransferencia = viewTransferencia;
    }

    public Boolean getViewDiario() {
        return viewDiario;
    }

    public void setViewDiario(Boolean viewDiario) {
        this.viewDiario = viewDiario;
    }

    @Override
    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getActKey() {
        return actKey;
    }

    public void setActKey(String actKey) {
        this.actKey = actKey;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return nameUser;
    }

    @Override
    public String getUrlSolicitada() {
        return urlSolicitada;
    }

    public void setUrlSolicitada(String urlSolicitada) {
        this.urlSolicitada = urlSolicitada;
    }

    @Override
    public Boolean getTemp() {
        return temp;
    }

    public void setTemp(Boolean temp) {
        this.temp = temp;
    }

    @Override
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String getNombrePersonaLogeada() {
        if (nombrePersonaLogeada == null || nombrePersonaLogeada.isEmpty()) {
            nombrePersonaLogeada = "Admin";
        }
        return nombrePersonaLogeada;
    }

    public void setNombrePersonaLogeada(String nombrePersonaLogeada) {
        this.nombrePersonaLogeada = nombrePersonaLogeada;
    }

    @Override
    public Boolean getEsDirector() {
        return esDirector;
    }

    public void setEsDirector(Boolean esDirector) {
        this.esDirector = esDirector;
    }

    public Object getVarTemp() {
        return varTemp;
    }

    public void setVarTemp(Object varTemp) {
        this.varTemp = varTemp;
    }

    @Override
    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Boolean getEsSuperUser() {
        return esSuperUser;
    }

    public void setEsSuperUser(Boolean esSuperUser) {
        this.esSuperUser = esSuperUser;
    }

    @Override
    public List<Long> getRoles() {
        if (Utils.isNotEmpty(roles)) {
            List<Long> result = new ArrayList<>(roles.size());
            roles.forEach((rol) -> {
                result.add(rol.getId());
            });
            return result;
        }
        return null;
    }

    public List<String> getNameRoles() {
        if (Utils.isNotEmpty(roles)) {
            List<String> result = roles.stream().map(e -> e.getRol().getNombre()).collect(Collectors.toList());
            return result;
        }
        return null;
    }

    public List<UsuarioRol> getUserRoles() {
        return roles;
    }

    public void setRoles(List<UsuarioRol> roles) {
        this.roles = roles;
    }

    @Override
    public List<Long> getDepts() {

        return depts;
    }

    public void setDepts(List<Long> depts) {
        this.depts = depts;
    }

    public AclLogin getAclLogin() {
        return aclLogin;
    }

    public void setAclLogin(AclLogin aclLogin) {
        this.aclLogin = aclLogin;
    }

    public Long getIdAclLogin() {
        return idAclLogin;
    }

    public void setIdAclLogin(Long idAclLogin) {
        this.idAclLogin = idAclLogin;
    }

    public String getTopPage() {
        return topPage;
    }

    public void setTopPage(String topPage) {
        this.topPage = topPage;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public String getHostClient() {
        return hostClient;
    }

    public void setHostClient(String hostClient) {
        this.hostClient = hostClient;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public boolean hasRole(String nameRol) {
        Subject subject = SecurityUtils.getSubject();
        return subject.hasRole(nameRol);
    }

    public boolean[] hasRoles(List<String> nameRoles) {
        Subject subject = SecurityUtils.getSubject();
        return subject.hasRoles(nameRoles);
    }

    public boolean hasAnyRoles(List<String> nameRoles) {
        boolean[] hasRoles = hasRoles(nameRoles);
        for (boolean hasRol : hasRoles) {
            if (hasRol) {
                return hasRol;
            }
        }
        return false;
    }

    public MenuRol getMenuRol(String url) {
        if (roles == null) {
            return null;
        }
        if (Utils.isNotEmpty(roles)) {
            for (UsuarioRol usuarioRol : roles) {
                if (Utils.isNotEmpty(usuarioRol.getRol().getMenuRolList())) {
                    for (MenuRol menuRol : usuarioRol.getRol().getMenuRolList()) {
                        if (menuRol.getMenu() != null && menuRol.getMenu().getHrefUrl() != null) {
                            if (menuRol.getMenu().getHrefUrl().contains(url)) {
                                return menuRol;
                            }
                        }
                    }
                }
            }
            return null;
        }
        return null;
    }

    public UsuarioRol getRoles(MenuRol menuRol) {
        if (Utils.isNotEmpty(roles)) {
            for (UsuarioRol usuarioRol : roles) {
//                System.out.println("Rol " + usuarioRol);
                if (menuRol.getRol().equals(usuarioRol.getRol())) {
                    return usuarioRol;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "UserSession{" + "nameUser=" + nameUser + '}';
    }

    public String getUrlApp() {
        return CONFIG.URL_APP;
    }

    public String getIpAppTcWeb() {
        return CONFIG.URL_APP;
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
