/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.Presupuesto.Service.EjecucionPresupuestariaService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AsientosContables;
import com.origami.sigef.common.entities.EjecucionPresupuestaria;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.AsientosContablesService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.Serializable;
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
@Named(value = "reporteEjecucionPresPAPPView")
@ViewScoped
public class ReporteEjecucionPresupuestariaPAPPController implements Serializable {

    private OpcionBusqueda busqueda;
    private EjecucionPresupuestaria ejecucionPresupuestaria;
    private AsientosContables asientosContables;
    private UnidadAdministrativa unidad;

    private List<MasterCatalogo> periodos;
    private List<UnidadAdministrativa> unidades;

    private String tipoReporte;
    private String clasificado;
    private Date fechadesde;
    private Date fechahasta;
    private boolean porUnidad;

    @Inject
    private MasterCatalogoService masterCatalogoService;

    @Inject
    private ServletSession servlet;

    @Inject
    private EjecucionPresupuestariaService ejecucionPresService;

    @Inject
    private AsientosContablesService asientosContablesService;

    @Inject
    private UserSession usser;

    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            busqueda = new OpcionBusqueda();
            periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
            unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
            asignarRangoPeriodos();
        }
    }

    public void cambiarOpcion() {
        usser.getNameUser();
        if (clasificado == null) {
            porUnidad = Boolean.FALSE;
        } else if (clasificado.equals("UR")) {
            porUnidad = Boolean.TRUE;
        } else {
            porUnidad = Boolean.FALSE;
        }
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

    public void imprimir(String isExcel) {
        if (tipoReporte == null) {
            JsfUtil.addErrorMessage("TIPO REPORTE", "Ingrese un tipo de reporte");
            return;
        }
        if (clasificado == null) {
            JsfUtil.addErrorMessage("CLASIFICACION", "Seleccione la clasificación");
            return;
        }
        servlet.addParametro("fecha_desde", fechadesde);
        servlet.addParametro("fecha_hasta", fechahasta);
        servlet.addParametro("per", busqueda.getAnio());
        servlet.addParametro("ENTIDAD", usser.getUsuario().getEmpresaId());
        servlet.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            servlet.setOnePagePerSheet(false);
        }
        if (tipoReporte.equals("C")) {
            if (clasificado.equals("ALL")) {
                servlet.setNombreReporte("reporteEjecucionPAPPCompletoTodo");
                servlet.setNombreSubCarpeta("reportesPresupuesto");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else if (clasificado.equals("UR")) {
                servlet.addParametro("unidad_responsable", unidad.getNombre());
                servlet.setNombreReporte("reporteEjecucionPAPPCompletoPorUnidad");
                servlet.setNombreSubCarpeta("reportesPresupuesto");
                System.out.println("id unidad>> " + unidad.getId());
                if (unidad != null && unidad.getId() != null) {
                    servlet.addParametro("id_unidad", unidad.getId());
                } else {
                    servlet.addParametro("id_unidad", 0L);
                }
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
            limpiar();
        } else if (tipoReporte.equals("R")) {
            if (clasificado.equals("ALL")) {
                servlet.setNombreReporte("reporteEjecucionPAPPResumido");
                servlet.setNombreSubCarpeta("reportesPresupuesto");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else if (clasificado.equals("UR")) {
                servlet.addParametro("unidad_responsable", unidad.getNombre());
                servlet.setNombreReporte("reporteEjecucionPAPPResumidoPorUnidad");
                servlet.setNombreSubCarpeta("reportesPresupuesto");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
            limpiar();
        } else if (tipoReporte.equals("L")) {
            if (clasificado.equals("ALL")) {
                servlet.setNombreReporte("reporteEjecucionPAPPComparativoTodo");
                servlet.setNombreSubCarpeta("reportesPresupuesto");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else if (clasificado.equals("UR")) {
                servlet.addParametro("unidad_responsable", unidad.getNombre());
                servlet.setNombreReporte("reporteEjecucionPAPPComparativoPorUnidad");
                servlet.setNombreSubCarpeta("reportesPresupuesto");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
            limpiar();
        }
    }

    public void limpiar() {
        busqueda = new OpcionBusqueda();
        tipoReporte = null;
        clasificado = null;
        porUnidad = Boolean.FALSE;
        asignarRangoPeriodos();
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

    public EjecucionPresupuestaria getEjecucionPresupuestaria() {
        return ejecucionPresupuestaria;
    }

    public void setEjecucionPresupuestaria(EjecucionPresupuestaria ejecucionPresupuestaria) {
        this.ejecucionPresupuestaria = ejecucionPresupuestaria;
    }

    public AsientosContables getAsientosContables() {
        return asientosContables;
    }

    public void setAsientosContables(AsientosContables asientosContables) {
        this.asientosContables = asientosContables;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getClasificado() {
        return clasificado;
    }

    public void setClasificado(String clasificado) {
        this.clasificado = clasificado;
    }

    public boolean isPorUnidad() {
        return porUnidad;
    }

    public void setPorUnidad(boolean porUnidad) {
        this.porUnidad = porUnidad;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

}
