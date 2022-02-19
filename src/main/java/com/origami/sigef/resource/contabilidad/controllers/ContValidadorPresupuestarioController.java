/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contValidadorPresupuestarioView")
@ViewScoped
public class ContValidadorPresupuestarioController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private PresCatalogoPresupuestarioService presCatalogoPresupuestarioService;

    private Date fecha_inicio, fecha_fin;

    private String grupo;

    private Boolean parametro_1, parametro_2;

    private List<PresCatalogoPresupuestario> gruposPresupuestarios;

    private OpcionBusqueda opcionBusqueda;

    @PostConstruct
    public void init() {
        gruposPresupuestarios = presCatalogoPresupuestarioService.findByNamedQuery("PresCatalogoPresupuestario.findByNivel", 2);
        cleanForm();
    }

    public void cleanForm() {
        opcionBusqueda = new OpcionBusqueda();
        Calendar calendar = Calendar.getInstance();
        calendar.set(opcionBusqueda.getAnio(), 0, 1);
        fecha_inicio = calendar.getTime();
        fecha_fin = new Date();
        grupo = "";
        parametro_1 = Boolean.TRUE;
        parametro_2 = Boolean.TRUE;
        PrimeFaces.current().ajax().update("formMain");
    }

    public void generarReporte(String tipoArchivo) {
        if (parametro_1) {
            grupo = "";
        } else {
            if (grupo == null || grupo.equals("")) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un grupo");
                return;
            }
        }
        if (parametro_2) {
            servletSession.setNombreReporte("contable_presupuesto");
        } else {
//            servletSession.setNombreReporte("contable_presupuesto");
        }
        servletSession.addParametro("grupo", grupo);
        servletSession.addParametro("fecha_inicio", fecha_inicio);
        servletSession.addParametro("fecha_inicio_string", Utils.convertirFechaLetra(fecha_inicio));
        servletSession.addParametro("fecha_fin", fecha_fin);
        servletSession.addParametro("fecha_fin_string", Utils.convertirFechaLetra(fecha_fin));
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_contabilidad");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        cleanForm();
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Boolean getParametro_1() {
        return parametro_1;
    }

    public void setParametro_1(Boolean parametro_1) {
        this.parametro_1 = parametro_1;
    }

    public Boolean getParametro_2() {
        return parametro_2;
    }

    public void setParametro_2(Boolean parametro_2) {
        this.parametro_2 = parametro_2;
    }

    public List<PresCatalogoPresupuestario> getGruposPresupuestarios() {
        return gruposPresupuestarios;
    }

    public void setGruposPresupuestarios(List<PresCatalogoPresupuestario> gruposPresupuestarios) {
        this.gruposPresupuestarios = gruposPresupuestarios;
    }

}
