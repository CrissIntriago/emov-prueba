/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.certificacion_presupuesto_anual.service.RequisitoService;
import com.origami.sigef.common.entities.Requisito;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "requisitoView")
@ViewScoped
public class RequisitoController implements Serializable {

    @Inject
    private RequisitoService requisitoService;
    @Inject
    private UserSession userSession;

    private Requisito requisito;

    private LazyModel<Requisito> requisitoLazyModel;

    @PostConstruct
    public void initialize() {
        requisito = new Requisito();
        requisitoLazyModel = new LazyModel<>(Requisito.class);
        requisitoLazyModel.getFilterss().put("estado", true);
        requisitoLazyModel.getSorteds().put("nombre", "ASC");
    }

    /*FUNCIONES PARA EL MANTENIMIENTO DE REQUISTO*/
    //Llamar formulario para el registro de requisito
    public void formRequisito(Requisito requisito) {
        if (requisito != null) {
            this.requisito = requisito;
        } else {
            this.requisito = new Requisito();
        }
        PrimeFaces.current().executeScript("PF('DlgRegistroRequisito2').show()");
        PrimeFaces.current().ajax().update("DlgRegistroRequisito2");
    }

    //Guardar nuevo requisito y en caso de haber sido editado, guarda los cambios
    public void saveRequisito() {
        boolean edit = requisito.getId() != null;
        if (edit) {
            requisito.setUsuarioModificacion(userSession.getNameUser());
            requisito.setFechaModificacion(new Date());
            requisitoService.edit(requisito);
        } else {
            requisito.setEstado(Boolean.TRUE);
            requisito.setUsuarioCreacion(userSession.getNameUser());
            requisito.setFechaCreacion(new Date());
            requisito.setUsuarioModificacion(userSession.getNameUser());
            requisito.setFechaModificacion(new Date());
            requisito = requisitoService.create(requisito);
        }
        PrimeFaces.current().executeScript("PF('DlgRegistroRequisito2').hide()");
        JsfUtil.addSuccessMessage("Requisito", requisito.getNombre() + (edit ? " editada" : " registrada") + " con éxito.");
    }

    //Eliminar requisito
    public void deleteRequisito(Requisito requisito) {
        Boolean consultar = requisitoService.getTramiteAsociado(requisito);
        if (consultar) {
            JsfUtil.addErrorMessage("REQUISITO", "No se puede eliminar debido a que esta relacionado a un Tramite ");
        } else {
            requisito.setEstado(Boolean.FALSE);
            requisitoService.edit(requisito);
            PrimeFaces.current().ajax().update("requisitosTable2");
            JsfUtil.addSuccessMessage("Requisito", "SE ELIMINO CORRECTAMENTE");
        }
    }

    /*FIN DE LAS FUNCIONES DE REQUISITOS*/
    //Llama y presenta a un Dialog donde se vizualizara los requisitos que tenemos registrados, ademas dando opcion
    //a crear uno nuevo
    public void callTableRequito() {
        PrimeFaces.current().executeScript("PF('callTableRequisito').show()");
    }

    //Cierra el formulario y lo actualiza
    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("mainForm");
        PrimeFaces.current().ajax().update("formRegistroProcedimiento");
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public LazyModel<Requisito> getRequisitoLazyModel() {
        return requisitoLazyModel;
    }

    public void setRequisitoLazyModel(LazyModel<Requisito> requisitoLazyModel) {
        this.requisitoLazyModel = requisitoLazyModel;
    }
//</editor-fold>

}
