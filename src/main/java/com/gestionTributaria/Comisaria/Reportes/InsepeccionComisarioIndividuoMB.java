/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Reportes;

import com.gestionTributaria.Comisaria.Entities.ViewComisariosInsp;
import com.gestionTributaria.Comisaria.Entities.ViewDelegadosInsp;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class InsepeccionComisarioIndividuoMB implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;
    private Date fechaDesde;
    private Date fechaHasta;
    private String origen;
    private int opcion;
    private int opcion2;
    private ViewDelegadosInsp delegado;
    private ViewComisariosInsp comisario;
    private LazyModel<ViewComisariosInsp> comisarios;
    private LazyModel<ViewDelegadosInsp> delegados;

    @PostConstruct
    public void init() {
        comisario = new ViewComisariosInsp();
        delegado = new ViewDelegadosInsp();
        opcion = 1;
        opcion2 = 5;

    }

    public void loadDelegados() {
        delegados = new LazyModel<>(ViewDelegadosInsp.class);
    }

    public void loadComisarios() {
        comisarios = new LazyModel<>(ViewComisariosInsp.class);
    }

    public void seleccionarDelegado(ViewDelegadosInsp c) {
        delegado = c;
    }

    public void seleccionarComisario(ViewComisariosInsp c) {
        comisario = c;
    }

    public void acciones() {
        if (opcion == 4) {
            loadComisarios();
        }
        if (opcion2 == 7) {
            loadDelegados();
        }
    }

    public void imprimir(String tipo) {
        /**
         * Parametros del reporte - comisaria, delegado ,fecha_desde,
         * fecha_hasta, origen, user_impresion,
         */

        ss.instanciarParametros();
        if (opcion == 1) {
            ss.addParametro("comisaria", userSession.getUsuario().getId());
        } else if (opcion == 4) {
            if (comisario != null) {
                System.out.println("comisario.getIdUser() " + comisario.getIdUser());
                ss.addParametro("comisaria", comisario.getIdUser());
            }
        }
        ss.addParametro("delegado", null);
        if (opcion == 2) {
            ss.addParametro("origen", origen);
        }

        ss.setContentType(tipo);
        ss.addParametro("fecha_desde", fechaDesde);
        ss.addParametro("fecha_hasta", fechaHasta);
        ss.addParametro("impresion", userSession.getNameUser());
        ss.setNombreReporte("inspeccionesPersonalizadas");
        ss.setNombreSubCarpeta("GestionTributatia/comisaria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        init();
    }

    public void imprimirDelegdo(String tipo) {
        /**
         * Parametros del reporte - comisaria, delegado ,fecha_desde,
         * fecha_hasta, origen, user_impresion,
         */

        ss.instanciarParametros();
        Long userId = null;
        if (userSession.getUsuario() != null && userSession.getUsuario().getFuncionario() != null && userSession.getUsuario().getFuncionario().getPersona() != null) {
            userId = userSession.getUsuario().getFuncionario().getPersona().getId();
        }
        if (opcion2 == 5) {
            ss.addParametro("delegado", userId);
        } else if (opcion2 == 7) {
            if (delegado != null) {
                System.out.println("delegado.getIdcliente() " + delegado.getIdcliente());
                ss.addParametro("delegado", delegado.getIdcliente());
            }
        }
        ss.addParametro("comisaria", null);
        if (opcion2 == 6) {
            ss.addParametro("origen", origen);
        }

        ss.setContentType(tipo);
        ss.addParametro("fecha_desde", fechaDesde);
        ss.addParametro("fecha_hasta", fechaHasta);
        ss.addParametro("impresion", userSession.getNameUser());
        ss.setNombreReporte("inspeccionesPersonalizadas");
        ss.setNombreSubCarpeta("GestionTributatia/comisaria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        init();
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public ViewDelegadosInsp getDelegado() {
        return delegado;
    }

    public void setDelegado(ViewDelegadosInsp delegado) {
        this.delegado = delegado;
    }

    public ViewComisariosInsp getComisario() {
        return comisario;
    }

    public void setComisario(ViewComisariosInsp comisario) {
        this.comisario = comisario;
    }

    public LazyModel<ViewComisariosInsp> getComisarios() {
        return comisarios;
    }

    public void setComisarios(LazyModel<ViewComisariosInsp> comisarios) {
        this.comisarios = comisarios;
    }

    public LazyModel<ViewDelegadosInsp> getDelegados() {
        return delegados;
    }

    public void setDelegados(LazyModel<ViewDelegadosInsp> delegados) {
        this.delegados = delegados;
    }

    public int getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(int opcion2) {
        this.opcion2 = opcion2;
    }

}
