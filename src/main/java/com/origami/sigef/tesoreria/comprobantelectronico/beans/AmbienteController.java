/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Ambiente;
import com.origami.sigef.tesoreria.comprobantelectronico.service.AmbienteService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named(value = "ambienteView")
@ViewScoped
public class AmbienteController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private AmbienteService ambienteService;

    private LazyModel<Ambiente> ambientes;
    private Ambiente ambiente;
    private Boolean editar;

    @PostConstruct
    public void init() {
        ambientes = new LazyModel<>(Ambiente.class);
        ambiente = new Ambiente();
        editar = Boolean.FALSE;

    }

    public void guardarAmbiente() {
        if (ambiente.getId() == null) {
            if (ambienteService.findByCodigo(ambiente.getCodigo()) == null) {
                ambienteService.create(ambiente);
                ambiente = new Ambiente();
            } else {
                //EXISTE UN AMBIENTE IGUAL
                JsfUtil.addErrorMessage("Advertencia", "Existe un Cajero con los mismos datos");
            }
        } else {
            ambienteService.edit(ambiente);
            ambiente = new Ambiente();
            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", "");
        }
    }

    public void editarAmbiente(Ambiente a) {
        editar = Boolean.TRUE;
        ambiente = a;
    }

    public void cancelar() {
        editar = Boolean.FALSE;
        ambiente = new Ambiente();
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public LazyModel<Ambiente> getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(LazyModel<Ambiente> ambientes) {
        this.ambientes = ambientes;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
    }

}
