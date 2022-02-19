/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller.procesosBienes;

import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named(value = "reporteInventarioBeans")
@ViewScoped
public class ReportesInventario implements Serializable {

    private Date fechadesde;
    private Date fechahasta;
    private String unidadAdministrativs;
    private List<Servidor> listaUsuario;
    private Long solicitante;
    private String tipo;
    private String opcion;
    private List<DetalleItem> detalle;
    private String DetalleItem;
    private Boolean variable = false;
    private Boolean variable1 = false;
    private Boolean variable2 = false;
    private Boolean variable3 = false;
    private Boolean variable4 = false;

    @Inject
    private ServletSession ss;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private DetalleItemService detalleitemservice;
    @Inject
    private ServidorService servidorservice;
    @Inject
    private InventarioItemsService inventarioItemsService;

    private String valorSelectOneMenu;

    @PostConstruct
    public void inicializate() {
        valorSelectOneMenu = "";
        tipo = "";
        opcion = "";
        DetalleItem = "";
        try {
            fechadesde = Utils.getPrimerDiaAnio(Utils.getAnio(new Date()));
        } catch (ParseException parseException) {
        }
        fechahasta = new Date();
//        listaUsuario = servidorservice.findByNamedQuery("Servidor.findByServidorInventario");
        listaUsuario = inventarioItemsService.getServidoresSolicitanteMovimientos();
        detalle = detalleitemservice.findByNamedQuery("DetalleItem.findByEstadotrue");
    }

    public List<UnidadAdministrativa> getAllUnidadesAdministrativas() {
        return unidadService.getlListaUnidades();
    }

    public void btn() {
        if (valorSelectOneMenu.equals("periodo") || valorSelectOneMenu.equals("inventario")) {
            variable = true;
            variable1 = false;
            variable2 = false;
            variable3 = false;

        }
        if (valorSelectOneMenu.equals("unidad")) {
            variable1 = true;
            variable = false;
            variable2 = false;
            variable3 = false;

        }
        if (valorSelectOneMenu.equals("solicitante")) {
            variable2 = true;
            variable3 = false;
            variable1 = false;
            variable = false;

        }
        if (valorSelectOneMenu.equals("item5")) {
            variable3 = true;
            variable1 = false;
            variable = false;
            variable2 = false;

        }
        if (valorSelectOneMenu.equals("seleccione")) {
            variable4 = false;
            variable3 = false;
            variable1 = false;
            variable = false;
            variable2 = false;

        }
        try {
            fechadesde = Utils.getPrimerDiaAnio(Utils.getAnio(new Date()));
        } catch (ParseException parseException) {
        }
        fechahasta = new Date();
        tipo = "";
        solicitante = null;
        unidadAdministrativs = "";
    }

    public void imprimir(String tipoArchivo) {
        if (tipo.equals("EGRESO") && valorSelectOneMenu.equals("periodo")) {
            ss.addParametro("tipoEstado", false);
            ss.setNombreReporte("inventarioIngresoEgresoCompleto");
        }
        if (tipo.equals("INGRESO") && valorSelectOneMenu.equals("periodo")) {
            ss.addParametro("tipoEstado", true);
            ss.setNombreReporte("inventarioIngresoEgresoCompleto");
        }
        if (valorSelectOneMenu.equals("periodo") && tipo.equals("COMPLETO")) {
            ss.setNombreReporte("inventarioPeriodoCompleto");
        }
        if (tipo.equals("EGRESO") && valorSelectOneMenu.equals("unidad")) {
            ss.addParametro("EGRESO", tipo);
            ss.addParametro("unidad", unidadAdministrativs);
            ss.setNombreReporte("listado_egreso_unidad");
        }
        if (tipo.equals("EGRESO") && valorSelectOneMenu.equals("solicitante")) {
            ss.addParametro("EGRESO", tipo);
            ss.addParametro("id", solicitante);
            ss.setNombreReporte("listado_egreso_solicitante");
        }
        if (valorSelectOneMenu.equals("item5") && !DetalleItem.equals("") && (tipo.equals("INGRESO") || tipo.equals("EGRESO"))) {
            ss.addParametro("item", DetalleItem);
            ss.addParametro("INGRESO", tipo);
            ss.addParametro("EGRESO", tipo);
            ss.setNombreReporte("listado_ingreso_egreso_item");
        }
        if (valorSelectOneMenu.equals("item5") && tipo.equals("COMPLETO")) {
            ss.addParametro("item", DetalleItem);
            ss.addParametro("INGRESO", "INGRESO");
            ss.addParametro("EGRESO", "EGRESO");
            ss.setNombreReporte("listado_ingreso_egreso_item");
        }
        if (valorSelectOneMenu.equals("inventario") && DetalleItem.equals("") && (tipo.equals("INGRESO") || tipo.equals("EGRESO"))) {
            ss.addParametro("INGRESO", tipo);
            ss.addParametro("EGRESO", tipo);
            ss.setNombreReporte("inventario_ingreso_egreso");
        }
        if (valorSelectOneMenu.equals("inventario") && tipo.equals("COMPLETO")) {
            ss.addParametro("INGRESO", "INGRESO");
            ss.addParametro("EGRESO", "EGRESO");
            ss.setNombreReporte("inventario_ingreso_egreso");
        }
        if (valorSelectOneMenu.equals("cuentaContable")){ 
            System.out.println("entro");
           // ss.addParametro("EGRESO", tipo);
            //ss.addParametro("unidad", '');
            //ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/") + "reportes/inventario/");
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));   
            ss.setNombreReporte("inventarioPorCuentaContable");
        }
        if (fechadesde.compareTo(fechahasta) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha DESDE no puede ser mayor a la fecha HASTA");
            return;
        } else {
            ss.addParametro("fecha_desde", fechadesde);
            ss.addParametro("fecha_hasta", fechahasta);
            ss.setNombreSubCarpeta("inventario");
        }
        ss.setContentType(tipoArchivo);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getUnidadAdministrativs() {
        return unidadAdministrativs;
    }

    public void setUnidadAdministrativs(String unidadAdministrativs) {
        this.unidadAdministrativs = unidadAdministrativs;
    }

    public List<Servidor> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Servidor> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public Long getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Long solicitante) {
        this.solicitante = solicitante;
    }

    public List<DetalleItem> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleItem> detalle) {
        this.detalle = detalle;
    }

    public String getDetalleItem() {
        return DetalleItem;
    }

    public void setDetalleItem(String DetalleItem) {
        this.DetalleItem = DetalleItem;
    }

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
    }

    public String getValorSelectOneMenu() {
        return valorSelectOneMenu;
    }

    public void setValorSelectOneMenu(String valorSelectOneMenu) {
        this.valorSelectOneMenu = valorSelectOneMenu;
    }

    public Boolean getVariable1() {
        return variable1;
    }

    public void setVariable1(Boolean variable1) {
        this.variable1 = variable1;
    }

    public Boolean getVariable2() {
        return variable2;
    }

    public void setVariable2(Boolean variable2) {
        this.variable2 = variable2;
    }

    public Boolean getVariable3() {
        return variable3;
    }

    public void setVariable3(Boolean variable3) {
        this.variable3 = variable3;
    }

    public Boolean getVariable4() {
        return variable4;
    }

    public void setVariable4(Boolean variable4) {
        this.variable4 = variable4;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
//</editor-fold>

}
