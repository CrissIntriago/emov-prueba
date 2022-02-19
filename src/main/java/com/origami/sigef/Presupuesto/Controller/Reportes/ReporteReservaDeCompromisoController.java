/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reporteReservaCompView")
@ViewScoped
public class ReporteReservaDeCompromisoController implements Serializable {

    private boolean segun;
    private String segunPor;
    private String estados;
    private Date fechadesde;
    private Date fechahasta;

    private OpcionBusqueda busqueda;
    private SolicitudReservaCompromiso reservaCompromiso;
    private CatalogoItem estadosReserva;

    private List<MasterCatalogo> periodos;
    private List<CatalogoItem> listaEstadoReserva;
    private List<SolicitudReservaCompromiso> listaReservasAproLiqui;

    @Inject
    private MasterCatalogoService masterCatalogoService;

    @Inject
    private ServletSession servlet;

    @Inject
    private ReservaCompromisoService reservaCompromisoService;

    @Inject
    private UserSession userSession;

    @PostConstruct
    public void initView() {
        busqueda = new OpcionBusqueda();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        this.listaEstadoReserva = reservaCompromisoService.getListaEstadosReserva();
        listaReservasAproLiqui = new ArrayList<>();
        listaReservasAproLiqui = reservaCompromisoService.getReservaCompromisoAprobadaLiquidada();
        reservaCompromiso = new SolicitudReservaCompromiso();
        estadosReserva = new CatalogoItem();
        this.segunPor = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        fechahasta = new Date();
    }

    public void imprimir(String isExcel) {
        if ("".equals(segunPor) || segunPor == null) {
            JsfUtil.addWarningMessage("Advertencia", "Elija una opción");
            return;
        }

        servlet.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            servlet.setOnePagePerSheet(false);
            servlet.setIsIgnorePaginator(true);
        }
        servlet.addParametro("fecha_desde", fechadesde);
        servlet.addParametro("fecha_hasta", fechahasta);
        servlet.addParametro("per", busqueda.getAnio());
        servlet.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        if (segunPor != null) {
            switch (segunPor) {
                case "E":
                    if (estadosReserva == null) {
                        JsfUtil.addWarningMessage("Advertencia", "Elija un estado de Reserva de Compromiso");
                        return;
                    }
                    servlet.addParametro("cod_estado", estadosReserva.getCodigo());
                    servlet.addParametro("estado_r", estadosReserva.getTexto());
                    servlet.setNombreReporte("reporteReservaDeCompromisoPorEstados");
                    break;
                case "EJEC":
                    servlet.setNombreReporte("reporteReservaDeCompromisoEjecutado");
                    break;
                case "EIND":
                    String beneficiario = "";
                    String identificacion = "";
                    if (reservaCompromiso == null) {
                        JsfUtil.addWarningMessage("Advertencia", "Elija una Reserva de Compromiso para su reporte individual");
                        return;
                    }

                    servlet.addParametro("reserva", reservaCompromiso.getId());
                    servlet.addParametro("codigo", reservaCompromiso.getSecuancialForFilter());
                    servlet.addParametro("descripcion_reserva", reservaCompromiso.getDescripcion());
                    servlet.addParametro("fecha_aprob", reservaCompromiso.getFechaAprobacion());
                    if (reservaCompromiso.getBeneficiario() != null) {
                        beneficiario = reservaCompromiso.getBeneficiario().getNombre() + " " + reservaCompromiso.getBeneficiario().getApellido();
                        identificacion = reservaCompromiso.getBeneficiario().getIdentificacionCompleta();
                    } else {
                        beneficiario = "PROCESO INICIAL";
                        identificacion = "0000000000";
                    }
                    servlet.addParametro("beneficiario_reserva", beneficiario);
                    servlet.addParametro("identificacion_reserva", identificacion);
                    servlet.addParametro("estado", reservaCompromiso.getEstado().getTexto());
                    servlet.setNombreReporte("reporteReservaDeCompromisoEjecutadoIndividual");
                    break;
                default:
                    break;
            }
            servlet.setNombreSubCarpeta("reportesPresupuesto");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            limpiar();
        }

    }

    public String completarConCeros(int secuencial) {
        return Utils.completarCadenaConCeros(secuencial + "", 5);
    }

    public void limpiar() {
        busqueda = new OpcionBusqueda();
        reservaCompromiso = new SolicitudReservaCompromiso();
        segun = Boolean.FALSE;
        segunPor = "";
        estados = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        fechahasta = new Date();
    }

    public boolean isSegun() {
        return segun;
    }

    public void setSegun(boolean segun) {
        this.segun = segun;
    }

    public String getSegunPor() {
        return segunPor;
    }

    public void setSegunPor(String segunPor) {
        this.segunPor = segunPor;
    }

    public String getEstados() {
        return estados;
    }

    public void setEstados(String estados) {
        this.estados = estados;
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

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getListaEstadoReserva() {
        return listaEstadoReserva;
    }

    public void setListaEstadoReserva(List<CatalogoItem> listaEstadoReserva) {
        this.listaEstadoReserva = listaEstadoReserva;
    }

    public List<SolicitudReservaCompromiso> getListaReservasAproLiqui() {
        return listaReservasAproLiqui;
    }

    public void setListaReservasAproLiqui(List<SolicitudReservaCompromiso> listaReservasAproLiqui) {
        this.listaReservasAproLiqui = listaReservasAproLiqui;
    }

    public CatalogoItem getEstadosReserva() {
        return estadosReserva;
    }

    public void setEstadosReserva(CatalogoItem estadosReserva) {
        this.estadosReserva = estadosReserva;
    }
    
    

}
