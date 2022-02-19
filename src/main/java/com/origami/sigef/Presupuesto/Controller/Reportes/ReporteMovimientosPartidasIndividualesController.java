/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.Presupuesto.Service.EjecucionPresupuestariaService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reporteMovPartidasIndividualesView")
@ViewScoped
public class ReporteMovimientosPartidasIndividualesController implements Serializable {

    private boolean movimiento;
    private String partida;
    private Date fechadesde;
    private Date fechahasta;

    private Presupuesto presupuesto;
    private OpcionBusqueda busqueda;

    private List<MasterCatalogo> periodos;
    private List<Presupuesto> listPartidas;

    @Inject
    private MasterCatalogoService masterCatalogoService;

    @Inject
    private ServletSession servlet;

    @Inject
    private PresupuestoService presupuestoService;

    @Inject
    private EjecucionPresupuestariaService ejecPresupuestariaService;

    @Inject
    private UserSession userSession;

    @PostConstruct
    public void initView() {
        movimiento = true;
        presupuesto = new Presupuesto();
        busqueda = new OpcionBusqueda();
        listPartidas = new ArrayList<>();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        listPartidas = presupuestoService.getPartidaPresupuestoIngresosEgresos(busqueda.getAnio(), movimiento);
        asignarRangoPeriodos();
    }

    public void asignarRangoPeriodos() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        if (busqueda.getAnio().intValue() == Utils.getAnio(new Date()).intValue()) {
            fechahasta = new Date();
        } else {
            calendar.set(busqueda.getAnio(), 11, 31);
            fechahasta = calendar.getTime();
        }
    }

    public void cambioOpcionReporte() {
        partida = null;
        presupuesto = new Presupuesto();
        listPartidas = presupuestoService.getPartidaPresupuestoIngresosEgresos(busqueda.getAnio(), movimiento);
    }

    public void imprimir(String isExcel) {
        BigDecimal reformas = BigDecimal.ZERO;
        BigDecimal codificado = BigDecimal.ZERO;
        servlet.addParametro("per", busqueda.getAnio());
        servlet.addParametro("fecha_desde", fechadesde);
        servlet.addParametro("fecha_hasta", fechahasta);
        servlet.addParametro("diaDesde", Utils.getDia(fechadesde));
        servlet.addParametro("mesDesde", TalentoHumano.getMesXInt(Utils.getMes(fechadesde)));
        servlet.addParametro("diaHasta", Utils.getDia(fechahasta));
        servlet.addParametro("mesHasta", TalentoHumano.getMesXInt(Utils.getMes(fechahasta)));
        servlet.addParametro("presup_inicial", presupuesto.getPresupuestoInicial());
        servlet.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        servlet.addParametro("item_presupuestario", presupuesto.getItemNew().getDescripcion());
        servlet.addParametro("fuente", presupuesto.getFuenteNew().getCodFuente() + ".-" + presupuesto.getFuenteNew().getNombre());
        servlet.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            servlet.setOnePagePerSheet(false);
            servlet.setIsIgnorePaginator(true);
        }
        if (movimiento) {
            reformas = ejecPresupuestariaService.getReformaByPartidaFechaDesdeHastaIngresos(busqueda.getAnio(), fechadesde, fechahasta, presupuesto.getPartida(), "MOV", "", "");
            codificado = presupuesto.getPresupuestoInicial().add(reformas);
            servlet.addParametro("reforma", reformas);
            servlet.addParametro("preCodificado", codificado);
            servlet.addParametro("partida", presupuesto.getPartida());
            servlet.setNombreReporte("reporteMovimientosPartidasIndividualesIngresos");
            servlet.setNombreSubCarpeta("reportesPresupuesto");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            limpiar();
        } else {
            String codigo = ejecPresupuestariaService.identificarPartida(busqueda.getAnio(), presupuesto.getPartida());
            switch (codigo) {
                case "PAPP":
                    reformas = ejecPresupuestariaService.getReformaByPartidaFechaDesdeHastaPAPP(busqueda.getAnio(), fechadesde, fechahasta, presupuesto.getPartida(), "MOV", "", "", null);
                    break;
                case "PD":
                    reformas = ejecPresupuestariaService.getReformaByPartidaFechaDesdeHastaPD(busqueda.getAnio(), fechadesde, fechahasta, presupuesto.getPartida(), "MOV", "", "", null);
                    break;
                case "PDI":
                    reformas = ejecPresupuestariaService.getReformaByPartidaFechaDesdeHastaPDI(busqueda.getAnio(), fechadesde, fechahasta, presupuesto.getPartida(), "MOV", "", "", null);
                    break;
                case "PDA":
                    reformas = ejecPresupuestariaService.getReformaByPartidaFechaDesdeHastaPDA(busqueda.getAnio(), fechadesde, fechahasta, presupuesto.getPartida(), "MOV", "", "");
                    break;
                default:
                    break;
            }
            codificado = presupuesto.getPresupuestoInicial().add(reformas);
            servlet.addParametro("reforma", reformas);
            servlet.addParametro("preCodificado", codificado);
            servlet.addParametro("partida", presupuesto.getPartida());

            servlet.setNombreReporte("reporteMovimientosPartidasIndividualesEgresos");
            servlet.setNombreSubCarpeta("reportesPresupuesto");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            limpiar();
        }
    }

    public void limpiar() {
        listPartidas = new ArrayList<>();
        busqueda = new OpcionBusqueda();
        movimiento = true;
        listPartidas = presupuestoService.getPartidaPresupuestoIngresosEgresos(busqueda.getAnio(), movimiento);
        presupuesto = new Presupuesto();
        asignarRangoPeriodos();
    }

    public boolean isMovimiento() {
        return movimiento;
    }

    public void setMovimiento(boolean movimiento) {
        this.movimiento = movimiento;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
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

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<Presupuesto> getListPartidas() {
        return listPartidas;
    }

    public void setListPartidas(List<Presupuesto> listPartidas) {
        this.listPartidas = listPartidas;
    }

}
