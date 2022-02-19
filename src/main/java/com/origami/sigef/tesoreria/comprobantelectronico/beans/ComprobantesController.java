/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ComprobanteService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named(value = "comprobanteView")
@ViewScoped
public class ComprobantesController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private ComprobanteService comprobanteService;

    private LazyModel<Comprobantes> comprobantes;
    private Comprobantes comprobante;
    private Boolean editar;

    @PostConstruct
    public void init() {
        comprobantes = new LazyModel<>(Comprobantes.class);
        comprobantes.addFilter("estado", Boolean.TRUE);
        comprobante = new Comprobantes();
        editar = Boolean.FALSE;

    }

    public void guardarComprobante() {
        if (comprobante.getId() == null) {
            if (comprobanteService.findByCodigo(comprobante.getCodigo()) == null) {
                comprobanteService.create(comprobante);
                comprobante = new Comprobantes();
            } else {
                //EXISTE UN AMBIENTE IGUAL
                JsfUtil.addErrorMessage("Advertencia", "Existe un Comprobante con los mismos datos");
            }
        } else {
            comprobanteService.edit(comprobante);
            comprobante = new Comprobantes();
            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", "");
        }
    }

    public void editarComprobante(Comprobantes c) {
        editar = Boolean.TRUE;
        comprobante = c;
    }

    public void cancelar() {
        editar = Boolean.FALSE;
        comprobante = new Comprobantes();
    }

    public LazyModel<Comprobantes> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(LazyModel<Comprobantes> comprobantes) {
        this.comprobantes = comprobantes;
    }

    public Comprobantes getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobantes comprobante) {
        this.comprobante = comprobante;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

}
