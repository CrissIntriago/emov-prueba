/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.mantenimientos.controllers;

import com.origami.sigef.auth.services.MenuEjb;
import com.origami.sigef.auth.services.ModuloEjb;
import com.origami.sigef.auth.services.RolService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Menu;
import com.origami.sigef.common.entities.MenuRol;
import com.origami.sigef.common.entities.Modulo;
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
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.util.Variables;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.primefaces.PrimeFaces;
import org.primefaces.barcelona.domain.Document;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class MenuUsuarioAppBean implements Serializable {

    /**
     *
     */
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
    private List<Menu> menuSeleccionados;
    private List<MenuRol> menuRols;
    @Inject
    private RolService rolService;
    @Inject
    private ModuloEjb moduloEjb;
    @Inject
    private MenuEjb menuEjb;
    @Inject
    private ServidorService servidorEjb;
    // Usuarios
    private LazyModel<Usuarios> usersLazy;
    private LazyModel<Usuarios> usuariosLazy;
    private Usuarios usuario;
    private List<Usuarios> usuariosSeleccionados;
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

    private Converter docConverter = new Converter() {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Document orElse = permisos.stream().filter(p -> p.getName().equals(value)).findFirst().orElse(null);
            return orElse;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return (value != null)
                    ? ((Document) value).getName()
                    : null;
        }
    };

    private List<UploadedFile> files;
    @Inject
    private FileUploadDoc uploadDoc;

    @PostConstruct
    public void initView() {
        if (!PrimeFaces.current().isAjaxRequest()) {
            modulos = new LazyModel<>(Modulo.class);
            modulos.getSorteds().put("orden", "ASC");
            filtroActivosInactivos(3);
            rol = new Rol();
            modulo = new Modulo();
            menu = new Menu();
            datoAccion = new Document();
            esSubMenu = false;
            usuario = new Usuarios();
            sendMail = (valoresService.findByCodigo1(Variables.ENVIAR_MAIL_NUEVO_USUARIO).doubleValue() == BigDecimal.ONE.doubleValue());
            Map<String, String> ord = new HashMap<>(2);
            ord.put("nombre", "ASC");
//            roles = this.rolService.findAllOrder(ord);
            roles = this.rolService.findByNamedQuery("Rol.findByEstadOrderNombre", null);

        }
    }

    public void reloadMenus() {
        moduloEjb.clearReloadMenu();
        this.reloadConfig();
        JsfUtil.redirect(JsfUtil.getHostContextUrl() + "menu-roles");
    }

    public void filtroActivosInactivos(int aux) {
        //usuarios
        usersLazy = new LazyModel<>(Usuarios.class);
        usersLazy.getSorteds().put("usuario", "ASC");
        usersLazy.getSorteds().put("funcionario.id", "ASC");
        usersLazy.setDistinct(false);
        usersLazy.getFilterss().put("funcionario:notEqual", null);
        //roles
        rolesLazy = new LazyModel<>(Rol.class);
        rolesLazy.setDistinct(false);
        rolesLazy.getSorteds().put("nombre", "ASC");
        switch (aux) {
            case 1:
                usersLazy.getFilterss().put("estado", true);
                rolesLazy.getFilterss().put("estado", true);
                break;
            case 2:
                usersLazy.getFilterss().put("estado", false);
                rolesLazy.getFilterss().put("estado", false);
                break;
        }
        JsfUtil.update("tbMain:dtUsers");
        JsfUtil.update("tbMain:dtRoles");
    }

    //<editor-fold defaultstate="collapsed" desc="ROLES">
    public void nuevoRol(Rol rol) {
        if (rol == null) {
            this.rol = new Rol();
            this.rol.setEstado(true);
        } else {
            this.rol = rol;
        }
        if (this.rol.getUnidadAdministrativa() == null) {
            this.rol.setUnidadAdministrativa(new UnidadAdministrativa());
        }
        PrimeFaces.current().executeScript("PF('dlgRol').show();");
    }

    public void guardarRol() {
        if (rol.getNombre() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el nombre del rol");
            return;
        }
//        if (rol.getUnidadAdministrativa() != null) {
//            if (rol.getUnidadAdministrativa().getId() != null) {
//                rol.setUnidadAdministrativa(null);
//            }
//        }
        if (rolService.existe(rol)) {
            JsfUtil.addErrorMessage("Error", "Ya existe un rol con ese nombre");
            return;
        }
        Rol temp = rolService.save(rol);
        if (temp != null) {
            rol = new Rol();
            cerrarDialogo();
            PrimeFaces.current().executeScript("PF('dlgRol').hide();");
            JsfUtil.addSuccessMessage("Info", "Rol guardado correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al guardar el rol.");
        }
    }

    public void deshabilitar(Rol rol) {
        rol.setEstado(false);
        Rol temp = rolService.save(rol);
        if (temp != null) {
            JsfUtil.addSuccessMessage("Info", "Rol eliminado correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al eliminar el rol.");
        }
    }

    public void buscarUnidad() {
        Utils.openDialog("/facelet/talentoHumano/unidadAdministrativa/dialogUnidadAdministrativa", "55%", "500");
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
            LOG.log(Level.SEVERE, "Select Unidad", e);
        }
    }

    public void asignarMenu(Rol rol) {
        try {
            this.rol = rol;
            menuLazy = new LazyModel<>(Menu.class);
            menuLazy.getFilterss().put("prettyPattern:notEqual", null);
            menuLazy.getFilterss().put("esPublico", false);
            menuLazy.getSorteds().put("descripcion", "ASC");
            JsfUtil.executeJS("PF('dlgAsiganacionMenu').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "asignarMenu", e);
        }
    }

    public void asignarMasiva() {
        try {
            if (menuSeleccionados != null) {
                int count = 0;
                for (Menu ms : menuSeleccionados) {
                    if (ms.getMenuRolList() == null) {
                        ms.setMenuRolList(new ArrayList<>());
                    }
                    try {
                        boolean existe = false;
                        for (MenuRol mr : ms.getMenuRolList()) {
                            if (mr.getRol().equals(rol)) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            MenuRol mr = new MenuRol();
                            mr.setMenu(ms);
                            mr.setRol(rol);
                            ms.getMenuRolList().add(mr);
                            menuEjb.edit(ms);
                            count++;
//                            System.out.println("Agregando nuevo rol a menu " + ms.getDescripcion() + " >> " + ms.getPrettyPattern());
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Asignacion masiva de rol a menu " + ms.getId() + " rol " + rol.getId(), e);
                        continue;
                    }
                }
                JsfUtil.addSuccessMessage("Asignacion", "Rol agregago correctamante a " + count + " menu.");
                JsfUtil.executeJS("PF('dlgAsiganacionMenu').hide();");
                menuSeleccionados = new ArrayList<>();
                menuLazy = null;
                this.rol = new Rol();
            } else {
                JsfUtil.addSuccessMessage("Asignacion", "No se encontraron menu a asignar.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "asignarMasiva", e);
        }
    }

    public void asignarUsuario(Rol rol) {
        try {
            this.rol = rol;
            usuariosLazy = new LazyModel<>(Usuarios.class);
            usuariosLazy.getFilterss().put("estado", true);
            usuariosLazy.getSorteds().put("usuario", "ASC");
            JsfUtil.executeJS("PF('dlgAsiganacionUsuario').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "asignarMenu", e);
        }
    }

    public void asignarMasivaUsuario() {
        try {
            if (usuariosSeleccionados != null) {
                int count = 0;
                for (Usuarios ms : usuariosSeleccionados) {
                    if (ms.getRoles() == null) {
                        ms.setRoles(new ArrayList<>());
                    }
                    try {
                        boolean existe = false;
                        for (UsuarioRol mr : ms.getRoles()) {
                            if (mr.getRol().equals(rol)) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            UsuarioRol mr = new UsuarioRol();
                            mr.setUsuario(ms);
                            mr.setRol(rol);
                            ms.getRoles().add(mr);
                            usuarioService.edit(ms);
                            count++;
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Asignacion masiva de rol a menu " + ms.getId() + " rol " + rol.getId(), e);
                        continue;
                    }
                }
                JsfUtil.addSuccessMessage("Asignacion", "Rol agregago correctamante a " + count + " usuarios.");
                JsfUtil.executeJS("PF('dlgAsiganacionUsuario').hide();");
                usuariosSeleccionados = new ArrayList<>();
                usuariosLazy = null;
                this.rol = new Rol();
            } else {
                JsfUtil.addSuccessMessage("Asignacion", "No se encontraron usuarios a asignar.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "asignarMasiva", e);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MODULOS">
    public void nuevoModulo(Modulo modulo) {
        if (modulo == null) {
            this.modulo = new Modulo();
            this.modulo.setOrden(this.modulos.getRowCount() + 1);
        } else {
            this.modulo = modulo;
        }
        PrimeFaces.current().executeScript("PF('dlgModulo').show();");
    }

    public void guardarModulo() {
        if (modulo.getNombre() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el nombre del Módulo");
            return;
        }
        if (modulo.getIcono() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el Icono");
            return;
        }
        if (modulo.getWidgetVar() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el widgetvar");
            return;
        }
        Modulo temp = moduloEjb.save(modulo);
        if (temp != null) {
            modulo = new Modulo();
            cerrarDialogo();
            PrimeFaces.current().executeScript("PF('dlgModulo').hide();");
            JsfUtil.addSuccessMessage("Info", "Módulo guardado correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al guardar el Modulo.");
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MENU">
    public void nuevoMenu(Modulo modulo, Menu menu) {
        try {
            if (menu == null) {
                this.menu = new Menu();
                this.menu.setModulo(modulo);
                this.menu.setOrden(modulo.getMenuList().size() + 1);
                menuRols = new ArrayList<>();
            } else {
                this.menu = menu;
                menuRols = this.menu.getMenuRolList();
                esSubMenu = menu.getPrettyPattern() == null;
                if (this.menu.getPrettyId() != null) {
                    JsonUtil jsonUtil = new JsonUtil();
                    List<Document> acciones = (List<Document>) jsonUtil.fromJson(this.menu.getPrettyId(), Document[].class);
                    permisos = new ArrayList<>();
                    if (Utils.isNotEmpty(acciones)) {
                        permisos.addAll(acciones);
                    }
                }
            }
            datoAccion = new Document();
            if (rol == null) {
                rol = new Rol();
                if (rol.getUnidadAdministrativa() == null) {
                    rol.setUnidadAdministrativa(new UnidadAdministrativa());
                }
            }
            PrimeFaces.current().executeScript("PF('dlgMenu').show();");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "menu", e);
        }
    }

    public void editMenu(Long idMenu, Boolean ver) {
        try {
            this.ver = ver;
            this.menu = menuEjb.find(idMenu);
            esSubMenu = menu.getPrettyPattern() == null;
            if (menu.getPrettyPattern() != null) {
                esSubMenu = menu.getPrettyPattern().equals("/");
            }
            if (rol == null) {
                rol = new Rol();
                if (rol.getUnidadAdministrativa() == null) {
                    rol.setUnidadAdministrativa(new UnidadAdministrativa());
                }
            }
            permisos = new ArrayList<>();
            if (this.menu.getPrettyId() != null) {
                JsonUtil jsonUtil = new JsonUtil();
                List<Document> acciones = (List<Document>) jsonUtil.fromJson(this.menu.getPrettyId(), Document[].class);
                if (Utils.isNotEmpty(acciones)) {
                    permisos.addAll(acciones);
                }
            }
            menuRols = this.menu.getMenuRolList();
            PrimeFaces.current().executeScript("PF('dlgMenu').show();");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "menu", e);
        }
    }

    public void duplicarMenu(Menu menu) {
        try {
            this.ver = false;
            this.menu = Utils.clone(menu);
            this.menu.setId(null);
            esSubMenu = menu.getPrettyPattern() == null;
            if (menu.getPrettyPattern() != null) {
                esSubMenu = menu.getPrettyPattern().equals("/");
            }
            if (rol == null) {
                rol = new Rol();
                if (rol.getUnidadAdministrativa() == null) {
                    rol.setUnidadAdministrativa(new UnidadAdministrativa());
                }
            }
            permisos = new ArrayList<>();
            if (this.menu.getPrettyId() != null) {
                JsonUtil jsonUtil = new JsonUtil();
                List<Document> acciones = (List<Document>) jsonUtil.fromJson(this.menu.getPrettyId(), Document[].class);
                if (Utils.isNotEmpty(acciones)) {
                    permisos.addAll(acciones);
                }
            }
            if (Utils.isNotEmpty(this.menu.getMenuRolList())) {
                for (MenuRol mr : this.menu.getMenuRolList()) {
                    mr.setId(null);
                    mr.setMenu(this.menu);
                }
            }
            menuRols = this.menu.getMenuRolList();
            PrimeFaces.current().executeScript("PF('dlgMenu').show();");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "menu", e);
        }
    }

    public void eliminarMenu(Menu menu) {
        try {
            menuEjb.remove(menu);
            JsfUtil.addInformationMessage("Información", "Registro Eliminado correctamente.");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "Ocirrio un error al eliminar el menu");
            LOG.log(Level.SEVERE, "Eliminar menu", e);
        }
    }

    public void cambiarTipoMenu() {
        if (esSubMenu) {
            menu.setHrefUrl(null);
            menu.setPrettyId(null);
            menu.setPrettyPattern(null);
        } else {

        }
    }

    public void setOrderItem() {
        if (!esSubMenu) {
            if (menu.getMenuPadre() != null && menuRols != null) {
                menu.setOrden(menuRols.size() + 1);
            } else {
                menu.setOrden(1);
            }
        }
    }

    public void guardarMenu() {
        if (menu.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el nombre del Menu");
            return;
        }
        if (menu.getIcono() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el Icono del Menu");
            return;
        }
        if (!esSubMenu) {
            if (menu.getHrefUrl() == null) {
                JsfUtil.addErrorMessage("Error", "Debe ingresar la url absoluta de la paguina");
                return;
            }
            if (menu.getPrettyId() == null) {
                JsfUtil.addErrorMessage("Error", "Debe ingresar la url o el identificador de la pagina");
                return;
            }
            if (menu.getPrettyPattern() == null) {
                JsfUtil.addErrorMessage("Error", "Debe ingresar la url que se va mostrar");
                return;
            }
            if (menu.getMenuPadre() != null) {
                menu.setModulo(null);
            }
        }
        if (permisos != null) {
            JsonUtil jsonUtil = new JsonUtil();
            menu.setPrettyId(jsonUtil.toJsonNotSerializeNulls(permisos));
            if (Utils.isNotEmpty(menuRols)) {
                for (MenuRol menuRol : menuRols) {
                    if (menuRol.getPermisos() != null) {
                        menuRol.setOpciones(jsonUtil.toJsonNotSerializeNulls(menuRol.getPermisos()));
                    }
                }
            }
        }
        menu.setMenuRolList(menuRols);
        boolean nuevo = menu.getId() == null;
        Menu temp = menuEjb.save(menu, menuRols);
        if (temp != null) {
            cerrarDialogo();
//            JoinPath path = Join.path(temp.getPrettyPattern().trim());
//            Join rule = path.to(temp.getHrefUrl().trim());
//            if (nuevo) {
//                ConfigurationBuilder.begin().addRule(rule.withInboundCorrection());
//                UrlMapping mappingForUrl = PrettyContext.getCurrentInstance().getConfig().getMappingForUrl(URL.build(temp.getHrefUrl()));
//                PrettyContext.getCurrentInstance().getConfig().getGlobalRewriteRules().add(rule);
//            } else {
//                boolean contains = ConfigurationBuilder.begin().getRules().contains(rule);
//                System.out.println("Existe rule " + contains);
//                if (contains) {
//                    ConfigurationBuilder.begin().getRules().set(ConfigurationBuilder.begin().getRules().indexOf(rule), rule);
//                }
//            }
            menu = new Menu();
            esSubMenu = false;
            PrimeFaces.current().executeScript("PF('dlgMenu').hide();");
            JsfUtil.addSuccessMessage("Info", "Menu guardado correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al guardar el Menu.");
        }
    }

    public void agregarMenuRol() {
        if (rol == null || rol.getId() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar el Rol");
            return;
        }
        if (menuRols == null) {
            menuRols = new ArrayList<>();
        }
        if (Utils.isNotEmpty(menuRols)) {
            for (MenuRol mr : menuRols) {
                if (mr.getRol().equals(rol)) {
                    JsfUtil.addWarningMessage("Advertencia", "Rol ya existe agregado.");
                    return;
                }
            }
        }
        MenuRol ur = new MenuRol();
        ur.setRol(rol);
        ur.setMenu(menu);
        menuRols.add(ur);
        rol = new Rol();
    }

    public void elimarRolMenu(MenuRol menuRol, int index) {
        try {
            if (menuRol.getId() == null) {
                MenuRol remove = menuRols.remove(index);
                if (remove != null) {
                    JsfUtil.addInformationMessage("Info", "Rol eliminado correctamente");
                } else {
                    JsfUtil.addWarningMessage("Error", "Rol no puedo ser eliminado");
                }
            } else {
                boolean remove = menuEjb.removeMenuRol(menuRol);
                if (remove) {
                    menuRols.remove(menuRol);
//                    PrimeFaces.current().ajax().update("tdRolesMenu");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar rol", e);
        }
    }

    public TreeNode initMenus(Modulo mod) {
        if (mod.getNodeMenus() == null) {
            return menuEjb.createNode(mod);
        } else {
            return mod.getNodeMenus();
        }
    }

    public void agregarAcciones() {
        if (permisos == null) {
            permisos = new ArrayList<>();
        }
        if (datoAccion.getName() == null) {
            JsfUtil.addErrorMessage("Campo faltante", "Tipo componete");
            return;
        }
        if (datoAccion.getType() == null) {
            JsfUtil.addErrorMessage("Campo faltante", "Nombre componete");
            return;
        }
        try {
            permisos.add(datoAccion);
            datoAccion = new Document();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "agregarAcciones", e);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="USUARIO">
    public void nuevoUsuario(Usuarios usuario) {
        if (usuario == null) {
            this.usuario = new Usuarios();
            this.usuario.setEmpresaId(this.userSession.getUsuario().getEmpresaId());
        } else {
            this.usuario = usuario;
            if (Utils.isNotEmpty(this.usuario.getRoles())) {
                this.usuario.getRoles().size();
            }
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
                String ciRuc = usuario.getFuncionario().getPersona().getIdentificacion();
                Usuarios temp = usuarioService.findByIdentificacion(ciRuc);
                if (temp == null) {
                    Servidor servidor = servidorEjb.findByIdentificacion(ciRuc);
                    if (servidor != null) {
                        usuario.setFuncionario(servidor);
                        createNameUser();
                    } else {
                        Utils.openDialog("/facelet/talentoHumano/dialogServidor", null);
                    }
                } else {
                    usuario = temp;
                    if (usuario.getFuncionario() == null) {
                        usuario.setFuncionario(servidorEjb.findByIdentificacion(ciRuc));
                    }
                }
                PrimeFaces.current().ajax().update("@(.ui-panelgrid)");
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", null);
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
                if (pass1 == null || pass1.length() == 0) {
                    JsfUtil.addErrorMessage("Contraseña", "Debe ingresar la contraseña");
                    return;
                }
                usuario.setClaveTemp(pass1);
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

    public void deshabilitarUsuario(Usuarios usuario) {
        try {
            usuario.setEstado(false);
            usuario = usuarioService.save(usuario);
            if (usuario != null) {
                JsfUtil.addInformationMessage("Info", "Usuario Deshabilitado correctamente");
            } else {
                JsfUtil.addErrorMessage("Error", "Ocurrio un error al deshabilitar Usuario" + usuario.getUsuario());
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
    }

    public void cambiarClaveUsuario(Usuarios usuario) {
        try {
            setVer(false);
            pass1 = null;
            pass2 = null;
            this.usuario = usuario; // dlgCambioClave
            PrimeFaces.current().executeScript("PF('dlgCambioClave').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
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
            cerrarDialogo();
            JsfUtil.addInformationMessage("Usuario", "Datos almacenados correctamente");
        } else {
            JsfUtil.addErrorMessage("Usuario", "Ocurrio un error al guardar los datos del usuario");
        }
        this.usuario = new Usuarios();
        this.usuario.setFuncionario(new Servidor());
        this.usuario.getFuncionario().setPersona(new Cliente());
        pass1 = null;
        pass2 = null;
        PrimeFaces.current().executeScript("PF('dlgCambioClave').hide();");
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

    public void reporteAceso(String tipo) {
        ss.addParametro("FECHA", Utils.dateFormatPattern("yyyy-MM-dd", new Date()));
        ss.setNombreReporte("access");
        ss.setContentType(tipo);
        ss.setNombreSubCarpeta("AccessUser");
        if (tipo.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(true);
        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public void reporteUsuario(Usuarios usr) {
        try {
            ss.setNombreReporte("datosUsuario");
            ss.setNombreSubCarpeta("AccessUser");
            ss.setDataSource(Arrays.asList(usr));
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Generar reporte de usuario.", e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(event.getFile());
    }

//</editor-fold>
    public void cerrarDialogo() {
        ver = false;
    }

    public void reloadConfig() {
        this.moduloEjb.getCache().loadApplicationVariables();
    }

//<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

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

    public LazyModel<Menu> getMenuLazy() {
        return menuLazy;
    }

    public void setMenuLazy(LazyModel<Menu> menuLazy) {
        this.menuLazy = menuLazy;
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

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public Document getDatoAccion() {
        return datoAccion;
    }

    public void setDatoAccion(Document datoAccion) {
        this.datoAccion = datoAccion;
    }

    public List<Document> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Document> permisos) {
        this.permisos = permisos;
    }

    public List<String> getPermisosSeleccionados() {
        return permisosSeleccionados;
    }

    public void setPermisosSeleccionados(List<String> permisosSeleccionados) {
        this.permisosSeleccionados = permisosSeleccionados;
    }

    public Converter getDocConverter() {
        return docConverter;
    }

    public void setDocConverter(Converter docConverter) {
        this.docConverter = docConverter;
    }

    public List<MenuRol> getMenuRols() {
        return menuRols;
    }

    public void setMenuRols(List<MenuRol> menuRols) {
        this.menuRols = menuRols;
    }

    public List<Menu> getMenuSeleccionados() {
        return menuSeleccionados;
    }

    public void setMenuSeleccionados(List<Menu> menuSeleccionados) {
        this.menuSeleccionados = menuSeleccionados;
    }

    public LazyModel<Usuarios> getUsuariosLazy() {
        return usuariosLazy;
    }

    public void setUsuariosLazy(LazyModel<Usuarios> usuariosLazy) {
        this.usuariosLazy = usuariosLazy;
    }

    public List<Usuarios> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }

    public void setUsuariosSeleccionados(List<Usuarios> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }
//</editor-fold>

}
