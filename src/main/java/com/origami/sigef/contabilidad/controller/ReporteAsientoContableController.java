/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "reporteAcView")
@ViewScoped
public class ReporteAsientoContableController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContCuentasService contCuentasService;

    private Integer parametrosFecha;
    private Boolean cp;
    private Date desde, hasta;
    private int num_transaccion;
    private Short periodo;
    private ContCuentas cuenta;
    private List<Short> listaPeriodo;
    private List<ContCuentas> listCuentasContables;
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar;

    @PostConstruct
    public void inicializar() {
        try {
            listaPeriodo = catalogoItemService.getPeriodo();
            listCuentasContables = contCuentasService.findMovimientos(true, true);
            parametrosFecha = 1;
            loadInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInit() throws ParseException {
        calendar = Calendar.getInstance();
        cp = true;
        Short anio_sistema = Utils.getAnio(new Date()).shortValue();
        String fechaInicial = anio_sistema.toString() + "-01-01";
        periodo = anio_sistema;
        calendar.set(periodo, 0, 1);
        desde = formato.parse(fechaInicial);

        hasta = new Date();
        cuenta = new ContCuentas();
    }

    public void elegirPeriodo() {
        calendar.set(periodo, 0, 1);
        desde = calendar.getTime(); // new Date("01/01/" + busqueda.getAnio());        
        hasta = new Date();

    }

    public void imprimirDiarioGlobal(boolean excel) {
        if (excel) {
            servletSession.setContentType("xlsx");
        }
        int a;
        switch (parametrosFecha) {
            case 1:
                if (num_transaccion <= 0 || periodo == null || periodo <= 0) {
                    JsfUtil.addWarningMessage("AVISO", "NUMERO DE TRANSACCION INCORRECTO");
                    return;
                }
                if (!cp) {
                    servletSession.addParametro("num", num_transaccion);
                    servletSession.addParametro("periodo", periodo);
                    servletSession.setNombreReporte("AsientoContableCp");
                } else {
                    servletSession.addParametro("num", num_transaccion);
                    servletSession.addParametro("periodo", periodo);
                    servletSession.setNombreReporte("AsientoContableDiairio");
                }
                break;
            case 2:
                if (desde == null || hasta == null) {
                    JsfUtil.addWarningMessage("AVISO", "ELIGA LAS FECHAS");
                    return;
                }
                a = desde.compareTo(hasta);
                if (a > 0) {
                    JsfUtil.addWarningMessage("AVISO", "LA FECHA HASTA DEBER SERR IGUAL O MAYOR A LA FECHA DESDE");
                    return;
                }
                servletSession.addParametro("desde", desde);
                servletSession.addParametro("hasta", hasta);
                servletSession.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
                servletSession.setNombreReporte("asientosContables");
                break;
            case 3:
                if (desde == null || hasta == null) {
                    JsfUtil.addWarningMessage("AVISO", "ELIGA LAS FECHAS");
                    return;
                }
                a = desde.compareTo(hasta);
                if (a > 0) {
                    JsfUtil.addWarningMessage("AVISO", "LA FECHA HASTA DEBER SERR IGUAL O MAYOR A LA FECHA DESDE");
                    return;
                }
                servletSession.addParametro("desde", desde);
                servletSession.addParametro("hasta", hasta);
                servletSession.addParametro("codigoCuenta", cuenta != null ? cuenta.getCodigo() : "");
                servletSession.setNombreReporte("saldoCuentasAuxiliares");
                break;
        }
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

//<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public int getNum_transaccion() {
        return num_transaccion;
    }

    public void setNum_transaccion(int num_transaccion) {
        this.num_transaccion = num_transaccion;
    }

    public Integer getParametrosFecha() {
        return parametrosFecha;
    }

    public void setParametrosFecha(Integer parametrosFecha) {
        this.parametrosFecha = parametrosFecha;
    }

    public boolean getCp() {
        return cp;
    }

    public void setCp(boolean cp) {
        this.cp = cp;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

//</editor-fold>
    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public ContCuentas getCuenta() {
        return cuenta;
    }

    public void setCuenta(ContCuentas cuenta) {
        this.cuenta = cuenta;
    }

    public List<ContCuentas> getListCuentasContables() {
        return listCuentasContables;
    }

    public void setListCuentasContables(List<ContCuentas> listCuentasContables) {
        this.listCuentasContables = listCuentasContables;
    }

}
