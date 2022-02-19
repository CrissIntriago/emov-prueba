/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.mantenimientos.controllers;

import com.origami.sigef.auth.services.MenuEjb;
import com.origami.sigef.auth.services.ModuloEjb;
import com.origami.sigef.auth.services.RolService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleRegistro;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Menu;
import com.origami.sigef.common.entities.Modulo;
import com.origami.sigef.common.entities.ProcesoServidor;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.util.Variables;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.ProcesoServidorService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import com.origami.sigef.talentohumano.services.detalleRegistroService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.primefaces.PrimeFaces;
import org.primefaces.barcelona.domain.Document;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class menuUsuarioNuevoBeans extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(MenuUsuarioAppBean.class.getName());

    // Roles
    private LazyModel<Rol> rolesLazy;
    private Rol rol;
    // Modulo y menu
    private LazyModel<Modulo> modulos;
    private Modulo modulo;
    private Menu menu;
    private Boolean esSubMenu;
    private Document datoAccion;
    private List<String> permisosSeleccionados;
    private List<Document> permisos;
    private LazyModel<Menu> menuLazy;
    @Inject
    private RolService rolService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private ModuloEjb moduloEjb;
    @Inject
    private MenuEjb menuEjb;
    @Inject
    private ServidorService servidorEjb;
    // Usuarios
    private LazyModel<Usuarios> usersLazy;
    private Usuarios usuario;
    @Inject
    private UserSession userSession;
    private String pass1;
    private String pass2;
    private Boolean sendMail = false;
    private Boolean ver = false;
//    private UnidadAdministrativa unidadAdministrativa;
    @Inject
    private ValoresService valoresService;
    @Inject
    private UsuarioService usuarioService;
    private List<Rol> roles;

    @Inject
    private ServletSession ss;
    private List<UploadedFile> files;
    @Inject
    private FileUploadDoc uploadDoc;
    private boolean flujo;

    @Inject
    private ProcesoServidorService procesoSerService;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private detalleBancoServices bancoService;
    @Inject
    private detalleRegistroService detalleRegistroService;
    @Inject
    private ServidorService servidorService;

    private String observaciones;

    @PostConstruct
    public void initView() {
        if (!PrimeFaces.current().isAjaxRequest()) {
            rolesLazy = new LazyModel<>(Rol.class);
            modulos = new LazyModel<>(Modulo.class);
            modulos.getSorteds().put("orden", "ASC");
            menuLazy = new LazyModel<>(Menu.class);
            menuLazy.getFilterss().put("orden", 1);
            usersLazy = new LazyModel<>(Usuarios.class);
            usersLazy.getSorteds().put("usuario", "ASC");
            usersLazy.getFilterss().put("funcionario:notEqual", null);
            rol = new Rol();
            modulo = new Modulo();
            menu = new Menu();
            datoAccion = new Document();
            esSubMenu = false;
            usuario = new Usuarios();
            sendMail = (valoresService.findByCodigo1(Variables.ENVIAR_MAIL_NUEVO_USUARIO).doubleValue() == BigDecimal.ONE.doubleValue());
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                if ("ASP".equals(tramite.getTipoTramite().getAbreviatura())) {
                    flujo = true;
                }
                if ("BSP".equals(tramite.getTipoTramite().getAbreviatura())) {
                    flujo = false;
                    Long tramiteNumero = tramite.getNumTramite();
                    ProcesoServidor procServidor = procesoSerService.findProcesoServidorByNTramite(BigInteger.valueOf(tramiteNumero));
                    usersLazy.getFilterss().put("funcionario", procServidor.getServidorP());

                }
            }
        }

    }

    public void nuevoUsuario(Usuarios usuario) {
        if (usuario == null) {
            this.usuario = new Usuarios();
            this.usuario.setEmpresaId(this.userSession.getUsuario().getEmpresaId());
        } else {
            this.usuario = usuario;
        }
        if (this.usuario.getFuncionario() == null) {
            this.usuario.setFuncionario(new Servidor());
            this.usuario.getFuncionario().setPersona(new Cliente());
        }
        if (rol == null) {
            rol = new Rol();
        }
        PrimeFaces.current().executeScript("PF('dlgUsuario').show();");
        PrimeFaces.current().ajax().update("frmUsuario");
    }

    public void buscarFuncionario() {
        try {
            if (usuario.getFuncionario().getPersona().getIdentificacion() != null) {
                Usuarios temp = usuarioService.findByIdentificacion(usuario.getFuncionario().getPersona().getIdentificacion());
                if (temp == null) {
                    Servidor servidor = servidorEjb.findByIdentificacion(usuario.getFuncionario().getPersona().getIdentificacion());
                    if (servidor != null) {
                        usuario.setFuncionario(servidor);
                        createNameUser();
                    } else {
                        Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "480");
                    }
                } else {
                    usuario = temp;
                }
                PrimeFaces.current().ajax().update("@(.ui-panelgrid)");
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "480");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, usuario.getFuncionario().getPersona().getIdentificacion(), e);
        }
    }

    private void createNameUser() {
        Servidor servidor = usuario.getFuncionario();
        String userTemp = servidor.getPersona().getNombre().subSequence(0, 1) + servidor.getPersona().getApellido().split(" ")[0];
        usuario.setUsuario(userTemp);
    }

    public void validarPass() {
        if (!pass1.equals(pass2)) {
            JsfUtil.addErrorMessage("Contraseña", "La contraseña no coinciden");
            return;
        }
    }

    public void saveUser() {
        try {
            if (usuario.getFuncionario() == null) {
                JsfUtil.addErrorMessage("Funcionario", "Debe buscar un funcionario");
                return;
            }
            if (usuario.getFuncionario().getId() == null) {
                JsfUtil.addErrorMessage("Funcionario", "Debe buscar un funcionario");
                return;
            }
            if (usuario.getUsuario() == null) {
                JsfUtil.addErrorMessage("Usuario", "Debe ingresar el usuario");
                return;
            }
            if (usuario.getId() == null) {
                if (!sendMail) {
                    if (!pass1.equals(pass2)) {
                        JsfUtil.addErrorMessage("Contraseña", "La contraseña no coinciden");
                        return;
                    }
                } else {
                    pass1 = RandomStringUtils.randomAlphanumeric(10);
                }
                DefaultPasswordService passwordService = new DefaultPasswordService();
                usuario.setContrasenia(passwordService.encryptPassword(pass1));
            }
            usuario = usuarioService.save(usuario);
            if (usuario != null) {
                if (files != null) {
                    uploadDoc.upload(usuario, files);
                }
                JsfUtil.addInformationMessage("Usuario", "Datos almacenados correctamente");
            } else {
                JsfUtil.addErrorMessage("Usuario", "Ocurrio un error al guardar los datos del usuario");
            }
            this.usuario = new Usuarios();
            this.usuario.setFuncionario(new Servidor());
            this.usuario.getFuncionario().setPersona(new Cliente());
            rol = new Rol();
            pass1 = null;
            pass2 = null;
            cerrarDialogo();
            PrimeFaces.current().executeScript("PF('dlgUsuario').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
    }

    public void cerrarDialogo() {
        ver = false;
    }

    public void ingObservacion() {
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

    public void deshabilitarUsuario(Usuarios usuario) {

        try {
            if (usuario != null) {
                if (usuario.getFuncionario().getFechaSalida() == null || usuario.getFuncionario().getFechaSalida().compareTo(new Date()) >= 0) {
                    JsfUtil.addErrorMessage("Error", "El servidor Público aún no se puede dar de baja");
                    JsfUtil.addInformationMessage("Información", "La fecha de salida del servidor Público es: " + usuario.getFuncionario().getFechaSalida());
                    return;
                }
                usuario.setEstado(false);
                usuario = usuarioService.save(usuario);
                List<DetalleBanco> listBancos = bancoService.findListBancoByServidor(usuario.getFuncionario());
                if (!listBancos.isEmpty()) {
                    for (DetalleBanco item : listBancos) {
                        item.setEstado(Boolean.FALSE);
                        bancoService.edit(item);
                    }
                }
                Servidor servidorSalida = new Servidor();
                servidorSalida = usuario.getFuncionario();
                Distributivo disSalida;
                disSalida = distributivoService.findByDistributivo(usuario.getFuncionario());
                disSalida.setServidorPublico(null);
                distributivoService.edit(disSalida);
                DetalleRegistro detalleRegistro = detalleRegistroService.getDetalleRegistroByServidor(usuario.getFuncionario());
                detalleRegistro.setEstado(false);
                detalleRegistroService.edit(detalleRegistro);
                usuario.getFuncionario().setEstado(Boolean.FALSE);
                servidorSalida.setEstado(false);
                servidorService.edit(servidorSalida);
                PrimeFaces.current().ajax().update("frmMain");
                JsfUtil.addInformationMessage("Info", "Usuario Deshabilitado correctamente");
            } else {
                JsfUtil.addErrorMessage("Error", "Ocurrio un error al deshabilitar Usuario" + usuario.getUsuario());
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
    }

    public void listener(SelectEvent evt) {
        try {
            usuario.setFuncionario((Servidor) evt.getObject());
            this.createNameUser();
            PrimeFaces.current().ajax().update("@(.ui-panelgrid)");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
    }

    public void buscarUnidad() {
        Utils.openDialog("/facelet/talentoHumano/unidadAdministrativa/dialogUnidadAdministrativa", "550", "400");
    }

    public void selectUnidad(SelectEvent evt) {
        try {
            if (rol == null) {
                rol = new Rol();
            }
            if (rol.getId() == null) {
                rol = new Rol();
            }
            rol.setUnidadAdministrativa((UnidadAdministrativa) evt.getObject());
            roles = this.rolService.rolesUnidadAdministrativa(rol.getUnidadAdministrativa());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
    }

    public void agregarRol() {
        if (rol == null || rol.getId() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar el Rol");
            return;
        }
        UsuarioRol ur = new UsuarioRol();
        ur.setRol(rol);
        ur.setUsuario(usuario);
        if (usuario.getRoles() == null) {
            usuario.setRoles(new ArrayList<>());
        }
        rol = new Rol();
        usuario.getRoles().add(ur);
        PrimeFaces.current().ajax().update("tdRolesUsuario");
        PrimeFaces.current().ajax().update("datosUnidadAdmRol", "pngRolesUser");
    }

    public void elimarRolUsuario(UsuarioRol ur, int index) {
        try {
            if (ur.getId() == null) {
                UsuarioRol remove = usuario.getRoles().remove(index);
                if (remove != null) {
                    JsfUtil.addInformationMessage("Info", "Rol eliminado correctamente");
                } else {
                    JsfUtil.addWarningMessage("Error", "Rol no puedo ser eliminado");
                }
            } else {
                boolean remove = usuarioService.removeUsuarioRol(ur);
                if (remove) {
                    usuario.getRoles().remove(ur);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar rol", e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(event.getFile());
    }
//<editor-fold defaultstate="collapsed" desc="getter and Setter">

    public LazyModel<Rol> getRolesLazy() {
        return rolesLazy;
    }

    public void setRolesLazy(LazyModel<Rol> rolesLazy) {
        this.rolesLazy = rolesLazy;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LazyModel<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(LazyModel<Modulo> modulos) {
        this.modulos = modulos;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Boolean getEsSubMenu() {
        return esSubMenu;
    }

    public void setEsSubMenu(Boolean esSubMenu) {
        this.esSubMenu = esSubMenu;
    }

    public Document getDatoAccion() {
        return datoAccion;
    }

    public void setDatoAccion(Document datoAccion) {
        this.datoAccion = datoAccion;
    }

    public List<String> getPermisosSeleccionados() {
        return permisosSeleccionados;
    }

    public void setPermisosSeleccionados(List<String> permisosSeleccionados) {
        this.permisosSeleccionados = permisosSeleccionados;
    }

    public List<Document> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Document> permisos) {
        this.permisos = permisos;
    }

    public LazyModel<Menu> getMenuLazy() {
        return menuLazy;
    }

    public void setMenuLazy(LazyModel<Menu> menuLazy) {
        this.menuLazy = menuLazy;
    }

    public RolService getRolService() {
        return rolService;
    }

    public void setRolService(RolService rolService) {
        this.rolService = rolService;
    }

    public ModuloEjb getModuloEjb() {
        return moduloEjb;
    }

    public void setModuloEjb(ModuloEjb moduloEjb) {
        this.moduloEjb = moduloEjb;
    }

    public LazyModel<Usuarios> getUsersLazy() {
        return usersLazy;
    }

    public void setUsersLazy(LazyModel<Usuarios> usersLazy) {
        this.usersLazy = usersLazy;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
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

    public Boolean getSendMail() {
        return sendMail;
    }

    public void setSendMail(Boolean sendMail) {
        this.sendMail = sendMail;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public FileUploadDoc getUploadDoc() {
        return uploadDoc;
    }

    public void setUploadDoc(FileUploadDoc uploadDoc) {
        this.uploadDoc = uploadDoc;
    }

    public boolean isFlujo() {
        return flujo;
    }

    public void setFlujo(boolean flujo) {
        this.flujo = flujo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//</editor-fold>

}
