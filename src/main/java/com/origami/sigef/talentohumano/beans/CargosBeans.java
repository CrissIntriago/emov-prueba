/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.CargoService;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

///**
// *
// * @author OrigamiEC
// */
@Named(value = "cargosBeans")
@ViewScoped
public class CargosBeans implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private Cargo cargos;
    private LazyModel<Cargo> cargoLazy;
    @Inject
    private CargoService cargoService;
    @Inject
    private UserSession userSession;

    @PostConstruct
    public void inicializate() {
        this.cargos = new Cargo();
        cargoLazy = new LazyModel<>(Cargo.class);
        cargoLazy.getFilterss().put("estado", true);
        cargoLazy.getSorteds().put("nombreCargo", "ASC");

    }

    public void nuevo(Cargo c) {
        if (c != null) {
            this.cargos = c;
        } else {
            cargos = new Cargo();
        }
        cargos.setFechaModificacion(new Date());
        cargos.setUsuarioModifica(userSession.getNameUser());
        cargos.setEstado(true);
        PrimeFaces.current().executeScript("PF('cargoDialog').show()");
        PrimeFaces.current().ajax().update("frmCargo");
    }

    public void guardarCargos() {
        boolean edit = cargos.getId() != null;
        if (cargos.getNombreCargo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", " Debe Ingresar un Cargo");
        }
        Cargo existeCargo = cargoService.existeCargo(cargos);
        if (cargos.getId() == null && existeCargo != null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Cargo", cargos.getNombreCargo() + " se ecuentra resitrado/a en el sistema");
            return;
        }
        if (cargos.getId() == null && existeCargo != null) {
            if (cargos.getNombreCargo().equals(existeCargo.getNombreCargo())) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Cargo", cargos.getNombreCargo() + " se ecuentra resitrado/a en el sistema");
                return;
            }
        }
        if (!edit) {
            cargos.setUsuarioCreacion(userSession.getNameUser());
            cargos.setFechaCreacion(new Date());
            cargos = cargoService.create(cargos);
        } else {
            cargos.setUsuarioCreacion(userSession.getNameUser());
            cargos.setFechaCreacion(new Date());
            cargoService.edit(cargos);
        }
        PrimeFaces.current().executeScript("PF('cargoDialog').hide()");
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":formMain");
        JsfUtil.addSuccessMessage("Información", cargos.getNombreCargo() + (edit ? " editada" : " registrada") + " con éxito.");

    }

    public void eliminar(Cargo c) {
        c.setEstado(Boolean.FALSE);
        cargoService.edit(c);
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Info", "Cargo eliminada correctamente");
    }

    public Cargo getCargos() {
        return cargos;
    }

    public void setCargos(Cargo cargos) {
        this.cargos = cargos;
    }

    public LazyModel<Cargo> getCargoLazy() {
        return cargoLazy;
    }

    public void setCargoLazy(LazyModel<Cargo> cargoLazy) {
        this.cargoLazy = cargoLazy;
    }

}
