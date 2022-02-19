/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.controller;

import com.origami.sigef.activos.service.ResponsableAdquisicionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.ResponsableAdquisicion;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "ordenCompraEmitidaView")
@ViewScoped
public class OrdenCompraEmitidaController implements Serializable {

    @Inject
    private ServletSession serveltSession;
    @Inject
    private ResponsableAdquisicionService responsableAdquisicionService;

    private LazyModel<SolicitudOrdenCompra> lazy;
    private Adquisiciones adquisicion;

    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(SolicitudOrdenCompra.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("adquisicion.estado", true);
        lazy.getFilterss().put("adquisicion.tipoProceso.codigo:notEqual", "tipo_proceso_catalogo");
    }

    public void generalSolicitud(SolicitudOrdenCompra sol) {
        Boolean metodologia = Boolean.FALSE;
        Boolean caracteristicas = Boolean.FALSE;
        Boolean catalogo = Boolean.TRUE;
        String nombreReporte = "ORDEN DE COMPRA";
        List<ResponsableAdquisicion> responsablesList = responsableAdquisicionService.getListaDeResponsablesActivo(sol.getAdquisicion());
        ResponsableAdquisicion responsable = new ResponsableAdquisicion();
        for (ResponsableAdquisicion r : responsablesList) {
            if (r.getEstado() == true && r.getFechaFinalizacion() == null) {
                responsable = r;
                break;
            }
        }
        adquisicion = new Adquisiciones();
        adquisicion = sol.getAdquisicion();
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_combustible")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_alimento_bebidas")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_repuesto_acce"))) {
            caracteristicas = Boolean.TRUE;
            metodologia = Boolean.FALSE;
//            System.out.println("if 1");
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_servicios")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_seguros")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_mant_obras")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_arrendamiento"))) {
            caracteristicas = Boolean.FALSE;
            metodologia = Boolean.TRUE;
//            System.out.println("if 2");
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            catalogo = Boolean.FALSE;
            nombreReporte = "CATALOGO ELECTRONICO";
//            System.out.println("if 2");
        }
//        System.out.println("tipo 1 " + metodologia);
//        System.out.println("tipo 2 " + caracteristicas);
        serveltSession.addParametro("id_orden", sol.getId());
        serveltSession.addParametro("METODOLOGIA_CARACTERISTICAS", metodologia);
        serveltSession.addParametro("NOMBRE_REPORTE", nombreReporte);
        serveltSession.addParametro("CARACTERISTICAS_BIEN", caracteristicas);
        serveltSession.addParametro("CATALOGO_ELECTRONICO", catalogo);
        serveltSession.addParametro("nombreCompleto", responsable.getResponsable().getPersona().getNombreCompleto());
        serveltSession.addParametro("cargo", responsable.getResponsable().getDistributivo().getCargo().getNombreCargo());
        serveltSession.addParametro("correo", responsable.getResponsable().getEmailInstitucion());
        serveltSession.addParametro("telefonos", responsable.getResponsable().getPersona().getCelular() + "-" + responsable.getResponsable().getPersona().getTelefono());
        serveltSession.setNombreReporte("OrdenCompra");
        serveltSession.setNombreSubCarpeta("SolicitudOC");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public LazyModel<SolicitudOrdenCompra> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<SolicitudOrdenCompra> lazy) {
        this.lazy = lazy;
    }

}
