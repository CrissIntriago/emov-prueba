/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.RenTipoLiquidacionDepartamento;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author DEVELOPER
 */
@Named("tipoLiquidacionView")
@ViewScoped
public class TipoLiquidacionMb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession uSession;

    @Inject
    private ManagerService services;

    @Inject
    private ServletSession ss;
    @Inject
    private CatalogoService catalogoService;

    @Inject
    private UnidadAdministrativaService unidadService;
    private FinaRenTipoLiquidacion tipoLiquidacion, tipoLiquidacionSeleccionada, renTipoLiquidacionPadre;
    private LazyModel<FinaRenTipoLiquidacion> tiposLiquidaciones;
    private LazyModel<FinaRenTipoLiquidacion> lazy;

    private List<FinaRenRubrosLiquidacion> rubrosList;
    private UnidadAdministrativa departamento;
    private List<UnidadAdministrativa> departamentosList;

    protected List<Usuarios> usuarios, usuariosSelected, usuariosSelectedTemp;
    private Usuarios usuario;
    private List<CatalogoItem> listaTipo;

    @PostConstruct
    public void initView() {
        try {
            updateLazytipoLiquidaciones();
            departamentosList = unidadService.getlListaUnidades();
            listaTipo = new ArrayList<>();
            listaTipo = catalogoService.MostarTodoCatalogo("GT_TIPO_LIQUIDACION");
            //if (uSession.esLogueado()) {}
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void asignarRubros(FinaRenTipoLiquidacion tipoLiquidacion) {
        FinaRenTipoLiquidacion fina = Utils.clone(tipoLiquidacion);

        ss.borrarDatos();
        ss.borrarParametros();
        ss.instanciarParametros();
        ss.addParametro("idTipoLiquidacion", fina);
        System.out.println("tipoLiquidacion " + fina.getId());
        JsfUtil.redirect(CONFIG.URL_APP + "moduloGestionTributario/mantenimientos/_asignacionrubros.xhtml");

    }

    public void tipoLiquidacionEdit(FinaRenTipoLiquidacion tipoLiquidacion) {
        System.out.println("tipoLiquidacion" + tipoLiquidacion);
        tipoLiquidacionSeleccionada = new FinaRenTipoLiquidacion();
        if (tipoLiquidacion.getTransaccionPadre() != null) {
            this.renTipoLiquidacionPadre = services.find(FinaRenTipoLiquidacion.class, tipoLiquidacion.getTransaccionPadre());
            tipoLiquidacionSeleccionada = renTipoLiquidacionPadre;
        } else {
            renTipoLiquidacionPadre = new FinaRenTipoLiquidacion();
            tipoLiquidacionSeleccionada = new FinaRenTipoLiquidacion();
        }

        usuariosSelected = new ArrayList();
        usuariosSelectedTemp = new ArrayList();
        departamento = new UnidadAdministrativa();
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public void nuevoTipoLiquidacion() {
        renTipoLiquidacionPadre = new FinaRenTipoLiquidacion();
        usuariosSelected = new ArrayList();
        usuariosSelectedTemp = new ArrayList();
        departamento = new UnidadAdministrativa();
        tipoLiquidacionSeleccionada = new FinaRenTipoLiquidacion();
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        tipoLiquidacion.setEstado(true);
        tipoLiquidacion.setFechaIngreso(new Date());
        tipoLiquidacion.setUsuarioIngreso(uSession.getNameUser());
        tipoLiquidacion.setMostrarTransaccion(Boolean.TRUE);
    }

    public void eliminarTipoLiq(FinaRenTipoLiquidacion tipoLiq) {
        if (tipoLiq.getId() != null) {
            tipoLiq.setEstado(false);
            services.update(tipoLiq);
        }
        updateLazytipoLiquidaciones();
    }

    public void guardarNuevoTipoLiq() {
        try {
            if (tipoLiquidacion.getTipo() == null) {
                JsfUtil.addErrorMessage("Error", "Debe seleccionar un tipo...");
            }
            if (tipoLiquidacionSeleccionada != null) {
                tipoLiquidacion.setTransaccionPadre(tipoLiquidacionSeleccionada.getId());
            }
            System.out.println("Tipo>>" + tipoLiquidacion.getTipo());
            if (tipoLiquidacion.getTipo().equals("ESP")) {
                tipoLiquidacion.setEspecieValorada(Boolean.TRUE);
            }
            if (tipoLiquidacion.getNecesitaValidacionRentas() && tipoLiquidacion.getTransaccionPadre() == null) {
                tipoLiquidacion.setTransaccionPadre(0L);
            }
            FinaRenTipoLiquidacion rtl = (FinaRenTipoLiquidacion) services.save(tipoLiquidacion);

            if (rtl.getId() != null) {
                if (services.saveTypeLiquidationByUsers(rtl, usuariosSelected)) {
                    JsfUtil.addInformationMessage("Info", "Se creó correctamente el tipo de liquidación");
                } else {
                    JsfUtil.addErrorMessage("Error", "Hubo un problema al generar el tipo de liquidación");
                }
                JsfUtil.executeJS("PF('dlgNew').hide()");
            } else {
                JsfUtil.addErrorMessage("Error", "Hubo un problema al generar el tipo de liquidación");
            }
            tipoLiquidacionSeleccionada = new FinaRenTipoLiquidacion();
            JsfUtil.executeJS("PF('tableTr').filter();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarEdicionTipoLiq() {
        try {
            if (tipoLiquidacion.getTipo() == null) {
                JsfUtil.addErrorMessage("Error", "Debe seleccionar un tipo...");
            }
            System.out.println("tipo>>>" + tipoLiquidacion.getTipo());
            if (tipoLiquidacion.getTipo().equals("ESP")) {
                tipoLiquidacion.setEspecieValorada(Boolean.TRUE);
            }
            if (tipoLiquidacionSeleccionada != null) {
                tipoLiquidacion.setTransaccionPadre(tipoLiquidacionSeleccionada.getId());
            }
            if (tipoLiquidacion.getId() == null) {
                services.save(tipoLiquidacion);
            } else {
                services.update(tipoLiquidacion);
            }
            if (services.saveTypeLiquidationByUsers(tipoLiquidacion, usuariosSelected)) {
                JsfUtil.addInformationMessage("Info", "Se editó correctamente el tipo liquidación");
                departamento = new UnidadAdministrativa();
                usuariosSelected = new ArrayList();
                usuariosSelectedTemp = new ArrayList();
                usuarios = new ArrayList();
            } else {
                JsfUtil.addErrorMessage("Error", "Hubo un problema al editar el tipo liquidación");
            }
            JsfUtil.executeJS("PF('tableTr').filter();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarTitulo(FinaRenTipoLiquidacion tipoLiq) {
        boolean notEmpty = isNotEmpty(tipoLiq.getRenLiquidacionCollection());
        boolean eliminado = false;
        if (notEmpty) {
            JsfUtil.addInformationMessage("Info!", "El Titulo no puede ser eliminado porque tiene liquidaciones de este tipo. Solo se procedera a 'Inactivar'");
            tipoLiq.setEstado(false);
            eliminado = services.update(tipoLiq);
        } else {
            eliminado = services.eliminarTitulo(tipoLiq);
        }
        if (eliminado) {
            JsfUtil.addInformationMessage("Info!", "El Titulo fue eliminado correctamente.");
            updateLazytipoLiquidaciones();
        } else {
            JsfUtil.addErrorMessage("Info!", "Hubo un error al Eliminar Titulo.");
        }
    }

    public void updateLazytipoLiquidaciones() {
        lazy = new LazyModel<>(FinaRenTipoLiquidacion.class);
        lazy.getFilterss().put("estado", true);
        tiposLiquidaciones = new LazyModel<>(FinaRenTipoLiquidacion.class);
        tiposLiquidaciones.getFilterss().put("estado", true);
    }

    public void aplicaCoativa(FinaRenTipoLiquidacion f) {
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        tipoLiquidacion = services.find(FinaRenTipoLiquidacion.class, f.getId());
        if (tipoLiquidacion.getAplicaCoativa() != null) {
            if (tipoLiquidacion.getAplicaCoativa()) {
                System.out.println("entro false");
                f.setAplicaCoativa(Boolean.FALSE);
            } else {
                System.out.println("entro true");
                f.setAplicaCoativa(Boolean.TRUE);
            }
        } else {
            System.out.println("entro nulo true");
            f.setAplicaCoativa(Boolean.TRUE);
        }

        services.update(f);
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        JsfUtil.addInformationMessage("", "El Registro se actualizo correctamente");
    }

    public void addUser() {
        List<Usuarios> aclUsers = new ArrayList();

        if (usuariosSelected == null) {
            usuariosSelected = new ArrayList();
        }
        for (Usuarios aclUser : usuariosSelectedTemp) {
            if (!usuariosSelected.isEmpty()) {
                for (Usuarios acl : usuariosSelected) {
                    if (!acl.getId().equals(aclUser.getId())) {
                        aclUsers.add(aclUser);
                    }
                }
            } else {
                aclUsers.add(aclUser);
            }
        }
        usuariosSelected.addAll(aclUsers);
        usuariosSelected = usuariosSelected.stream().distinct().collect(Collectors.toList());

    }

    public void deleteUserLiquidacionAdd(int index) {
        usuariosSelected.remove(index);

        JsfUtil.addInformationMessage("Info!", "Eliminado Correctamente");
    }

    public void deleteUserLiquidacionSaved(int index) {
        RenTipoLiquidacionDepartamento rtld = (RenTipoLiquidacionDepartamento) new ArrayList(tipoLiquidacion.getRenTipoLiquidacionDepartamentosCollection()).get(index);
        rtld.setEstado(Boolean.FALSE);
        services.save(rtld);
        JsfUtil.addInformationMessage("Info!", "Eliminado Correctamente Recargue la pagina para ver sus cambios");
    }

    public Boolean eliminarRubros(List rubros) {
        if (isNotEmpty(rubros)) {
            return services.deleteList(rubros);
        }
        return true;
    }

    public void cargarUsuariosDepartamento() {
        try {
            usuarios = new ArrayList<>();
            List<Usuarios> usuariosTemp = new ArrayList<>();
            if (departamento != null) {
                usuariosTemp.addAll(services.getUser(departamento));
                System.out.println("usuariosTemp -> " + usuariosTemp.size());
                if (tipoLiquidacion.getRenTipoLiquidacionDepartamentosCollection() != null && !tipoLiquidacion.getRenTipoLiquidacionDepartamentosCollection().isEmpty()) {
                    for (RenTipoLiquidacionDepartamento rtld : tipoLiquidacion.getRenTipoLiquidacionDepartamentosCollection()) {
                        if (usuariosTemp.contains(rtld.getUsuario())) {
                            usuarios.add(rtld.getUsuario());
                        }
                    }
                    usuariosTemp.removeAll(usuarios);
                    usuarios = usuariosTemp;
                } else {
                    usuarios = usuariosTemp;
                }

            }

            usuarios.forEach(x -> System.out.println("x " + x.getUsuario()));
        } catch (Exception e) {
            System.err.println("e:v " + e);
        }
    }

    private boolean isNotEmpty(List<?> i) {
        return !i.isEmpty();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public UnidadAdministrativaService getUnidadService() {
        return unidadService;
    }

    public void setUnidadService(UnidadAdministrativaService unidadService) {
        this.unidadService = unidadService;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionSeleccionada() {
        return tipoLiquidacionSeleccionada;
    }

    public void setTipoLiquidacionSeleccionada(FinaRenTipoLiquidacion tipoLiquidacionSeleccionada) {
        this.tipoLiquidacionSeleccionada = tipoLiquidacionSeleccionada;
    }

    public FinaRenTipoLiquidacion getRenTipoLiquidacionPadre() {
        return renTipoLiquidacionPadre;
    }

    public void setRenTipoLiquidacionPadre(FinaRenTipoLiquidacion renTipoLiquidacionPadre) {
        this.renTipoLiquidacionPadre = renTipoLiquidacionPadre;
    }

    public LazyModel<FinaRenTipoLiquidacion> getTiposLiquidaciones() {
        return tiposLiquidaciones;
    }

    public void setTiposLiquidaciones(LazyModel<FinaRenTipoLiquidacion> tiposLiquidaciones) {
        this.tiposLiquidaciones = tiposLiquidaciones;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosList() {
        return rubrosList;
    }

    public void setRubrosList(List<FinaRenRubrosLiquidacion> rubrosList) {
        this.rubrosList = rubrosList;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public List<UnidadAdministrativa> getDepartamentosList() {
        return departamentosList;
    }

    public void setDepartamentosList(List<UnidadAdministrativa> departamentosList) {
        this.departamentosList = departamentosList;
    }

    public List<Usuarios> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuarios> getUsuariosSelected() {
        return usuariosSelected;
    }

    public void setUsuariosSelected(List<Usuarios> usuariosSelected) {
        this.usuariosSelected = usuariosSelected;
    }

    public List<Usuarios> getUsuariosSelectedTemp() {
        return usuariosSelectedTemp;
    }

    public void setUsuariosSelectedTemp(List<Usuarios> usuariosSelectedTemp) {
        this.usuariosSelectedTemp = usuariosSelectedTemp;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public LazyModel<FinaRenTipoLiquidacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FinaRenTipoLiquidacion> lazy) {
        this.lazy = lazy;
    }

    public List<CatalogoItem> getListaTipo() {
        return listaTipo;
    }

    public void setListaTipo(List<CatalogoItem> listaTipo) {
        this.listaTipo = listaTipo;
    }
//</editor-fold>

}
