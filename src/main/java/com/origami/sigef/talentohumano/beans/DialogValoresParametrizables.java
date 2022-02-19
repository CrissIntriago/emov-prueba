/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "dialogValoresParametrizablesView")
@ViewScoped
public class DialogValoresParametrizables implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<ParametrosTalentoHumano> valoresMostrar;
    @Inject
    private ParametrizableService parametrizableService;

    @PostConstruct
    public void init() {
        String anio = JsfUtil.getRequestParameter("anio");
        String tipo = JsfUtil.getRequestParameter("TIPO");
        if (tipo.equals("ANEXO")) {
            valoresMostrar = parametrizableService.findByValores("I", "DIS-ANE");
        }
        if (tipo.equals("GEN")) {
            valoresMostrar = parametrizableService.findByValores("I", "DIS-GEN");
        }
        if (tipo.equals("EGRESO")) {
            valoresMostrar = parametrizableService.findByOnlyClasificacion("E");
        }
        if (tipo.equals("EGRESO-ANE")) {
            valoresMostrar = parametrizableService.findByValoresAnexoAprobados("DIS-ANE", Short.valueOf(anio));
        }

    }

    public void closePar(ParametrosTalentoHumano P) {
        PrimeFaces.current().dialog().closeDynamic(P);
    }

    public List<ParametrosTalentoHumano> getValoresMostrar() {
        return valoresMostrar;
    }

    public void setValoresMostrar(List<ParametrosTalentoHumano> valoresMostrar) {
        this.valoresMostrar = valoresMostrar;
    }

}
