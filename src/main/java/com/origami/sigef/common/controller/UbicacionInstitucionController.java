/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.entities.UbicacionInstitucion;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UbicacionInstitucionService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named
@ViewScoped
public class UbicacionInstitucionController implements Serializable {

    @Inject
    private UbicacionInstitucionService ubicacionInstitucionService;
    @Inject
    private UserSession us;

    private LazyModel<UbicacionInstitucion> lazyUbicacion;
    private UbicacionInstitucion ubicacionInstitucion;
    private Boolean edit;

    @PostConstruct
    public void init() {
        lazyUbicacion = new LazyModel<>(UbicacionInstitucion.class);
        lazyUbicacion.setDistinct(false);
        lazyUbicacion.getFilterss().put("estado", true);
        ubicacionInstitucion = new UbicacionInstitucion();
        edit = Boolean.FALSE;
    }

    public void editar(UbicacionInstitucion ubi) {
        edit = Boolean.TRUE;
        ubicacionInstitucion = ubi;
    }

    public void guardar() {
        if (ubicacionInstitucion.getDescripcion() == null || ubicacionInstitucion.getDescripcion().isEmpty()) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "Agregue la descripción del registro.");
            return;
        }
        if (ubicacionInstitucion.getDireccion() == null || ubicacionInstitucion.getDireccion().isEmpty()) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "Agregue la Ubicación del registro.");
            return;
        }
        if (!edit) {
            ubicacionInstitucion.setEstado(Boolean.TRUE);
            ubicacionInstitucion.setFechaCreacion(new Date());
            ubicacionInstitucion.setUserCreacion(us.getNameUser());
            ubicacionInstitucionService.create(ubicacionInstitucion);
        } else {
            ubicacionInstitucion.setFechaModificacion(new Date());
            ubicacionInstitucion.setUserModificacion(us.getNameUser());
            ubicacionInstitucionService.edit(ubicacionInstitucion);
        }
        JsfUtil.addInformationMessage("Datos " + (edit ? "Editados" : "Registrados") + " Correctamente", "");
        PrimeFaces.current().ajax().update("formMain");
        cancelar();
    }

    public void inactivar(UbicacionInstitucion ubi) {
        if (ubicacionInstitucionService.existenRegistros(ubi)) {
            JsfUtil.addErrorMessage("ERROR", "No se puede eliminar la Ubicación, una Unidad Administrativa activa la usa.");

        } else {
            ubi.setEstado(Boolean.FALSE);
            ubicacionInstitucionService.edit(ubi);
        }
        PrimeFaces.current().ajax().update("formMain");
    }

    public void cancelar() {
        edit = Boolean.FALSE;
        ubicacionInstitucion = new UbicacionInstitucion();
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModel<UbicacionInstitucion> getLazyUbicacion() {
        return lazyUbicacion;
    }

    public void setLazyUbicacion(LazyModel<UbicacionInstitucion> lazyUbicacion) {
        this.lazyUbicacion = lazyUbicacion;
    }

    public UbicacionInstitucion getUbicacionInstitucion() {
        return ubicacionInstitucion;
    }

    public void setUbicacionInstitucion(UbicacionInstitucion ubicacionInstitucion) {
        this.ubicacionInstitucion = ubicacionInstitucion;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

//</editor-fold>
}
