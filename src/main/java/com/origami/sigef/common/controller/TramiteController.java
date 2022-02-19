/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.TramiteService;
import com.origami.sigef.resource.procesos.entities.Tramite;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Doris
 */
@ViewScoped
@Named
public class TramiteController implements Serializable {

    @Inject
    private UserSession userSession;
    
    @Inject
    private TramiteService tramiteService;

    private Tramite tramite;

    private LazyModel<Tramite> tramiteLazy;

    private Boolean view;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        tramite = new Tramite();

        this.tramiteLazy = new LazyModel<>(Tramite.class);
        this.tramiteLazy.getFilterss().put("estado", true);

    }

    public void abrirDlg(Tramite data) {

        this.view = Boolean.FALSE;
        if (data != null) {
            System.out.println("data actualizar:" + data.toString());
            this.tramite = data;
        } else {
            this.tramite = new Tramite();
        }
        JsfUtil.executeJS("PF('dlgNuevoTramite').show();");
        JsfUtil.update("formNuevoTramite");
    }

    public void guardarItem() {
        try {
            boolean edit = tramite.getId() != null;
            if (Utils.isNotEmptyString(tramite.getNombre())) {

                if (edit) {
                    tramite.setUsuarioModifica(userSession.getNameUser());
                    tramite.setFechaModifica(new Date());
                    System.out.println("tramite edit :" + tramite.toString());
                    tramiteService.updateEntity(tramite);

                    PrimeFaces.current().executeScript("PF('dlgNuevoTramite').hide()");
                    PrimeFaces.current().ajax().update("dtDatos");
                    JsfUtil.addSuccessMessage("TRÁMITE", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                } else {

                    tramite.setUsuarioCreacion(userSession.getNameUser());
                    tramite.setFechaCreacion(new Date());
                    tramite.setEstado(true);
                    System.out.println("tramite add :" + tramite.toString());
                    tramiteService.updateEntity(tramite);

                    PrimeFaces.current().executeScript("PF('dlgNuevoTramite').hide()");
                    PrimeFaces.current().ajax().update("dtDatos");
                    JsfUtil.addSuccessMessage("TRÁMITE", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();

                }

            } else {
                JsfUtil.addErrorMessage("Error.", "Debe ingresar un nombre para el trámite");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error.", "La Transacción no se pudo completar");
            e.printStackTrace();
        }

    }

    public void delete(Tramite tramite) {
        tramite.setEstado(Boolean.FALSE);
        tramiteService.save(tramite);
        JsfUtil.addSuccessMessage("Trámite", tramite.getNombre() + " eliminado con éxito");
        PrimeFaces.current().ajax().update("dtDatos");
    }

    public void ver(Tramite t) {
        this.tramite = new Tramite();
        this.view = Boolean.TRUE;
        this.tramite = t;
        JsfUtil.executeJS("PF('dlgNuevoTramite').show();");
        JsfUtil.update("formNuevoTramite");
    }

    public void vaciarFormulario() {
        tramite = new Tramite();
        JsfUtil.update("formNuevoTramite");
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public Tramite getTramite() {
        return tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public LazyModel<Tramite> getTramiteLazy() {
        return tramiteLazy;
    }

    public void setTramiteLazy(LazyModel<Tramite> tramiteLazy) {
        this.tramiteLazy = tramiteLazy;
    }

}
