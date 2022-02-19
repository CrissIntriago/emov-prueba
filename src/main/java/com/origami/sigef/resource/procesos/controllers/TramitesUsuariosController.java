/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.controllers;

import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.entities.TramitesUsuarios;
import com.origami.sigef.resource.procesos.services.TramitesUsuariosService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "tramitesUsuariosView")
@ViewScoped
public class TramitesUsuariosController implements Serializable {

    @Inject
    private TramitesUsuariosService tramitesUsuariosService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private UserSession userSession;

    private List<String> usuarios;
    private List<Usuarios> usuariosList;

    private List<TramitesUsuarios> tramitesUsuariosList;
    private List<TramitesUsuarios> tramitesUsuariosListDelete;

    private LazyModel<TipoTramite> tipoTramiteLazyModel;

    private TramitesUsuarios tramitesUsuarios;
    private TipoTramite tipoTamitesSeleccionado;

    private Boolean accion;

    @PostConstruct
    public void init() {
        cleanForm();
        tipoTamitesSeleccionado = new TipoTramite();
        this.tipoTramiteLazyModel = new LazyModel<>(TipoTramite.class);
        this.tipoTramiteLazyModel.getSorteds().put("descripcion", "ASC");
        this.tipoTramiteLazyModel.getFilterss().put("estado", true);
        usuariosList = usuarioService.findByNamedQuery("Usuario.findByUsuarioActivoParametre", true);
    }

    public void cleanForm() {
        tramitesUsuarios = new TramitesUsuarios();
        usuarios = new ArrayList<>();
        tramitesUsuariosList = new ArrayList<>();
        tramitesUsuariosListDelete = new ArrayList<>();
    }

    public void closeDlgUsuarios() {
        if (usuariosList != null && !usuariosList.isEmpty()) {
            usuarios = new ArrayList<>();
            for (Usuarios user : usuariosList) {
                usuarios.add(user.getUsuario());
            }
            JsfUtil.addSuccessMessage("INFO!!!", "Se han cargado los servidores");
            PrimeFaces.current().executeScript("PF('usuariosDlg').show()");
            PrimeFaces.current().ajax().update("usuariosTable");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar uno o varios usuarios ");
        }
    }

    public void openDlgConfiguracion(TipoTramite tipoTramite) {
        tipoTamitesSeleccionado = tipoTramite;
        tramitesUsuariosList = tramitesUsuariosService.findByNamedQuery("TramitesUsuarios.findByTipoTramite", tipoTamitesSeleccionado);
        if (tramitesUsuariosList.isEmpty()) {
            tramitesUsuariosList = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('tareasUsuariosDlg').show()");
    }

    public void formUserTramite(TramitesUsuarios tramite, Boolean accion) {
        this.accion = accion;
        if (tramite != null) {
            this.tramitesUsuarios = tramite;
            if (tramitesUsuarios.getVariosUsuarios()) {
                usuarios = new ArrayList<>(Arrays.asList(tramitesUsuarios.getNextUsers().split(";")));
            }
        } else {
            tramitesUsuarios = new TramitesUsuarios();
            usuarios = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('userTramiteFormularioDlg').show()");
        PrimeFaces.current().ajax().update("userTramiteFormularioDlg");
    }

    public void saveUsuarioTramite() {
        if (tramitesUsuarios.getUrl() == null || tramitesUsuarios.getUrl().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar la URL");
            return;
        }
        if (tramitesUsuarios.getUsersResponsable() == null || tramitesUsuarios.getUsersResponsable().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un usuario responsable");
            return;
        }
        if (tramitesUsuarios.getCondicion()) {
            if (tramitesUsuarios.getValorCondicion() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el valor de la condición");
                return;
            }
        }
        if (tramitesUsuarios.getVariosUsuarios()) {
            if (usuarios.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar a varios usuarios");
                return;
            } else {
                if (usuarios.size() == 1) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar mas de un usuario");
                    return;
                }
            }
        } else {
            if (tramitesUsuarios.getNextUsers() == null || tramitesUsuarios.getNextUsers().equals("")) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un usuario");
                return;
            }
        }
        tramitesUsuarios.setOrden(tramitesUsuariosList.size() + 1);
        tramitesUsuarios.setTipoTramite(tipoTamitesSeleccionado);
        if (tramitesUsuarios.getVariosUsuarios()) {
            getVariosUsuarios();
        }
        if (accion) {
            tramitesUsuariosList.add(tramitesUsuarios);
        }
        usuarios = new ArrayList<>();
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha añadido la asignación");
        PrimeFaces.current().executeScript("PF('userTramiteFormularioDlg').hide()");
        PrimeFaces.current().ajax().update("usuarioAsignadoTable");
    }

    public void getVariosUsuarios() {
        int aux = usuarios.size();
        int cont = 1;
        for (String item : usuarios) {
            if (aux == cont) {
                tramitesUsuarios.setNextUsers(tramitesUsuarios.getNextUsers().concat(item).concat(";"));
            } else {
                tramitesUsuarios.setNextUsers(tramitesUsuarios.getNextUsers().concat(item));
            }
            cont += 1;
        }
    }

    public void deleteList(TramitesUsuarios tramiteUsuario, int index) {
        if (tramiteUsuario.getId() != null) {
            tramitesUsuariosListDelete.add(tramiteUsuario);
            tramitesUsuariosList.remove(tramiteUsuario);
        } else {
            tramitesUsuariosList.remove(index);
        }
        int count = 1;
        for (TramitesUsuarios item : tramitesUsuariosList) {
            item.setOrden(count);
            count += 1;
        }
        PrimeFaces.current().ajax().update("usuarioAsignadoTable");
        JsfUtil.addWarningMessage("AVISO!!!", "Se ha eliminado correctamente");
    }

    public void save() {
        if (tramitesUsuariosList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay detalle para guardar");
        } else {
            for (TramitesUsuarios tramiteUsuario : tramitesUsuariosList) {
                if (tramiteUsuario.getId() != null) {
                    tramiteUsuario.setUsuarioCreacion(userSession.getNameUser());
                    tramiteUsuario.setFechaCreacion(new Date());
                    tramitesUsuariosService.edit(tramiteUsuario);
                } else {
                    tramiteUsuario.setTipoTramite(tipoTamitesSeleccionado);
                    tramiteUsuario.setUsuarioModificacion(userSession.getNameUser());
                    tramiteUsuario.setFechaModificacion(new Date());
                    tramitesUsuariosService.create(tramiteUsuario);
                }
            }
            if (tramitesUsuariosListDelete != null) {
                if (!tramitesUsuariosListDelete.isEmpty()) {
                    for (TramitesUsuarios tramiteUsuario : tramitesUsuariosListDelete) {
                        if (tramiteUsuario.getId() != null) {
                            tramiteUsuario.setUsuarioModificacion(userSession.getNameUser());
                            tramiteUsuario.setFechaModificacion(new Date());
                            tramiteUsuario.setEstado(Boolean.FALSE);
                            tramitesUsuariosService.edit(tramiteUsuario);
                        }
                    }
                }
            }
            cleanForm();
            JsfUtil.addSuccessMessage("INFO!!", "Se ha registrado correctamente");
            PrimeFaces.current().executeScript("PF('tareasUsuariosDlg').hide()");
        }
    }

    public TramitesUsuariosService getTramitesUsuariosService() {
        return tramitesUsuariosService;
    }

    public void setTramitesUsuariosService(TramitesUsuariosService tramitesUsuariosService) {
        this.tramitesUsuariosService = tramitesUsuariosService;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    public LazyModel<TipoTramite> getTipoTramiteLazyModel() {
        return tipoTramiteLazyModel;
    }

    public void setTipoTramiteLazyModel(LazyModel<TipoTramite> tipoTramiteLazyModel) {
        this.tipoTramiteLazyModel = tipoTramiteLazyModel;
    }

    public TramitesUsuarios getTramitesUsuarios() {
        return tramitesUsuarios;
    }

    public void setTramitesUsuarios(TramitesUsuarios tramitesUsuarios) {
        this.tramitesUsuarios = tramitesUsuarios;
    }

    public List<TramitesUsuarios> getTramitesUsuariosList() {
        return tramitesUsuariosList;
    }

    public void setTramitesUsuariosList(List<TramitesUsuarios> tramitesUsuariosList) {
        this.tramitesUsuariosList = tramitesUsuariosList;
    }

}
