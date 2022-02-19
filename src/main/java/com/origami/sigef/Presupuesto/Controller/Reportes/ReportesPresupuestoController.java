/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
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
@Named(value = "reportesPresupuestoView")
@ViewScoped
public class ReportesPresupuestoController implements Serializable {

    private Date fechadesde;
    private Date fechahasta;
    private String presupuestoDe;
    private String tipoReporte;
    private boolean fechas;

    private Presupuesto presupuesto;
    private OpcionBusqueda busqueda;

    private List<MasterCatalogo> periodos;

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private ServletSession servlet;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;

    @PostConstruct
    public void initView() {
        presupuesto = new Presupuesto();
        busqueda = new OpcionBusqueda();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});

        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        fechahasta = new Date();
    }

    public void mostrar() {
        if (tipoReporte != null) {
            if (tipoReporte.equals("COD")) {
                fechas = Boolean.TRUE;
            } else {
                fechas = Boolean.FALSE;
            }
        } else {
            fechas = Boolean.FALSE;
        }
    }

    public void imprimir(String isExcel) {
        if (presupuestoDe == null) {
            JsfUtil.addErrorMessage("PRESUPUESTO DE", "Ingrese un tipo de movimiento");
            return;
        }
        if (tipoReporte == null) {
            JsfUtil.addErrorMessage(" TIPO REPORTE", "Ingrese un tipo de reporte");
            return;
        }

        servlet.addParametro("per", busqueda.getAnio());
        servlet.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        servlet.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            servlet.setOnePagePerSheet(false);
            servlet.setIsIgnorePaginator(true);
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.presupuesto));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        servlet.addParametro("ci_presupuesto", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servlet.addParametro("nombre_presupuesto", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        servlet.addParametro("cargo_presupuesto", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        servlet.addParametro("ci_director", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servlet.addParametro("nombre_director", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        servlet.addParametro("cargo_director", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        if (presupuestoDe.equals("I")) {
            if (tipoReporte.equals("COD")) {
                servlet.addParametro("fecha_desde", fechadesde);
                servlet.addParametro("fecha_hasta", fechahasta);
                servlet.setNombreReporte("reportePresupuestoCodificadoIngresos");
                servlet.setNombreSubCarpeta("reportesPresupuesto");

                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else if (tipoReporte.equals("PI")) {

                servlet.setNombreReporte("reportePresupuestoInicialIngresos");
                servlet.setNombreSubCarpeta("reportesPresupuesto");

                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
            limpiar();
        } else if (presupuestoDe.equals("E")) {
            if (tipoReporte.equals("COD")) {
                servlet.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
                servlet.addParametro("fecha_desde", fechadesde);
                servlet.addParametro("fecha_hasta", fechahasta);
                servlet.setNombreReporte("reportePresupuestoCodificadoEgresos");

                servlet.setNombreSubCarpeta("reportesPresupuesto");

                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else if (tipoReporte.equals("PI")) {
                servlet.setNombreReporte("reportePresupuestoInicialEgresos");
                servlet.setNombreSubCarpeta("reportesPresupuesto");

                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
            limpiar();
        }
    }

    public void limpiar() {
        busqueda = new OpcionBusqueda();
        tipoReporte = null;
        presupuestoDe = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        fechahasta = new Date();
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

    public String getPresupuestoDe() {
        return presupuestoDe;
    }

    public void setPresupuestoDe(String presupuestoDe) {
        this.presupuestoDe = presupuestoDe;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
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

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public boolean isFechas() {
        return fechas;
    }

    public void setFechas(boolean fechas) {
        this.fechas = fechas;
    }

}
