/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.AsignacionTitulo;
import com.gestionTributaria.Services.AsignarComprobanteService;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI2
 */
@Named
@ViewScoped
public class AsignarComprobantesMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(AsignarComprobantesMB.class.getName());

    @Inject
    private CajeroService cajeroService;
    @Inject
    private UserSession userSession;
    @Inject
    private AsignarComprobanteService asiganarComprobanteService;

    private LazyModel<AsignacionTitulo> lazy;
    private AsignacionTitulo comprobante;
    private List<Usuarios> usuarios;

    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(AsignacionTitulo.class);
        lazy.getFilterss().put("estado", Boolean.TRUE);
        lazy.getSorteds().put("fechaCreacion", "desc");
        comprobante = new AsignacionTitulo();
        comprobante.setUsuario(new Usuarios());
        this.actualizarListadoUsuarios();
    }

    private void actualizarListadoUsuarios() {
        usuarios = cajeroService.getListUsuarios();
    }

    public void guardarEditar() {
        if (comprobante.getTotalTitulo().intValue() <= 0) {
            JsfUtil.addWarningMessage("Error", "Verifique que la cantidad de comprobantes asignados sea mayor a cero...");
            return;
        }
        if (comprobante.getId() != null) {
            comprobante.setFechaModificacion(new Date());
            comprobante.setUsuarioModificacion(userSession.getNameUser());
            asiganarComprobanteService.edit(comprobante);
        } else {
            comprobante.setFechaCreacion(new Date());
            comprobante.setUsuarioCreacion(userSession.getNameUser());
            asiganarComprobanteService.create(comprobante);
        }
        this.init();
        JsfUtil.update("mainForm");
    }

    public void calcularTitulosAsignados() {
        try {
            if (comprobante.getSecInicio().intValue() > comprobante.getSecFin().intValue()) {
                JsfUtil.addWarningMessage("Error", "Verifique que la secuencia inicial no se mayo a la final");
                return;
            }
            if (comprobante.getSecFin() != null && comprobante.getSecFin().intValue() > 0) {
                comprobante.setTotalTitulo(comprobante.getSecFin().subtract(comprobante.getSecInicio()));
            }
//            System.out.println("total titulos--> " + comprobante.getTotalTitulo());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        JsfUtil.addSuccessMessage("Registro", "Asignación de Comprobantes se realizó correctamente...");
        JsfUtil.update("datosAsignacion");
    }

    public void eliminar(AsignacionTitulo object) {
        if (object.getTotalTituloUsado().intValue() > 0) {
            JsfUtil.addWarningMessage("Error", "Se registro movimiento en la emision de Comprobantes...");
            return;
        }
        object.setEstado(Boolean.FALSE);
        comprobante.setFechaModificacion(new Date());
        comprobante.setUsuarioModificacion(userSession.getNameUser());
        asiganarComprobanteService.edit(object);
        JsfUtil.addSuccessMessage("Registro", "La asignación se eliminó correctamente...");
        JsfUtil.update("mainForm");
    }

    public LazyModel<AsignacionTitulo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<AsignacionTitulo> lazy) {
        this.lazy = lazy;
    }

    public AsignacionTitulo getComprobante() {
        return comprobante;
    }

    public void setComprobante(AsignacionTitulo comprobante) {
        this.comprobante = comprobante;
    }

    public List<Usuarios> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }

}
