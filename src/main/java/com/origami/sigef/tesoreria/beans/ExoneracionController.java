/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.beans;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.tesoreria.entities.Exoneracion;
import com.origami.sigef.tesoreria.service.ExoneracionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author gutya
 */
@Named(value = "exoneracionView")
@ViewScoped
public class ExoneracionController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession userSession;

    @Inject
    private ExoneracionService exoneracionService;

    private LazyModel<Exoneracion> exoneraciones;
    private Exoneracion exoneracion;
    private Boolean editar;

    @PostConstruct
    public void init() {
        editar = Boolean.FALSE;
        exoneracion = new Exoneracion();
        exoneracion.setEstado(Boolean.TRUE);
        exoneraciones = new LazyModel<>(Exoneracion.class);
        exoneraciones.getFilterss().put("estado", true);
    }

    public void guardar() {

        if (exoneracion.getId() == null) {
            exoneracion.setUserIngreso(userSession.getNameUser());
            exoneracion.setFechaIngreso(new Date());
            exoneracion.setConcepto(exoneracion.getConcepto().toUpperCase());
            exoneracionService.create(exoneracion);
        } else {
            exoneracion.setUserEdicion(userSession.getNameUser());
            exoneracion.setFechaEdicion(new Date());
            exoneracion.setConcepto(exoneracion.getConcepto().toUpperCase());
            exoneracionService.edit(exoneracion);
            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", "");
        }
        exoneracion = new Exoneracion();
        editar = Boolean.FALSE;
    }

    public void eliminar(Exoneracion rt) {
        rt.setEstado(Boolean.FALSE);
        exoneracionService.edit(rt);
    }

    public void editar(Exoneracion rt) {
        editar = Boolean.TRUE;
        exoneracion = rt;
    }

    public void cancelar() {
        editar = Boolean.FALSE;
        exoneracion = new Exoneracion();
    }
    
    public void restriccionValor(){
        if(exoneracion.getValor().doubleValue()>1){
            JsfUtil.addWarningMessage("AVISO!!", "El valor ingresado debe ser igual o menor a 1, debido a que es un valor porcentual");
            exoneracion.setValor(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("datos");
        }
    }

    public LazyModel<Exoneracion> getExoneraciones() {
        return exoneraciones;
    }

    public void setExoneraciones(LazyModel<Exoneracion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }

    public Exoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(Exoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

}
