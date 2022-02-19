/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ReporteTurismoMB implements Serializable {

    @Inject
    private ManagerService service;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession ss;

    private Date desde;
    private Date hasta;
    private Boolean aprobadoRentas = null;
    private List<FinaRenEstadoLiquidacion> listaEstados;
    private FinaRenEstadoLiquidacion estado;
    private String numLiquidacion = null;
    private Map<String, Object> param;

    @PostConstruct
    public void init() {
        listaEstados=new ArrayList<>();
        listaEstados = service.getEstadoLiquidaciones(new String[]{"pagado,por_pagar,inactivo,baja_n"});
        desde = new Date();
        estado = new FinaRenEstadoLiquidacion();
    }

    public void imprimir() {

        Long id_estado = Long.MIN_VALUE;

        if (estado != null) {
            id_estado = estado.getId();
        } else {
            id_estado = null;
        }
        ss.addParametro("validado_", aprobadoRentas);
        ss.addParametro("estado_id_", id_estado);
        ss.addParametro("num_liqui_", numLiquidacion);
        ss.addParametro("desde", desde);
        ss.addParametro("hasta", hasta);
        ss.setNombreReporte("reporte_parametrizados");
        ss.setNombreSubCarpeta("GestionTributatia/turismo");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="setter and getter">
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Boolean getAprobadoRentas() {
        return aprobadoRentas;
    }

    public void setAprobadoRentas(Boolean aprobadoRentas) {
        this.aprobadoRentas = aprobadoRentas;
    }

    public List<FinaRenEstadoLiquidacion> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<FinaRenEstadoLiquidacion> listaEstados) {
        this.listaEstados = listaEstados;
    }

    public FinaRenEstadoLiquidacion getEstado() {
        return estado;
    }

    public void setEstado(FinaRenEstadoLiquidacion estado) {
        this.estado = estado;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public String getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(String numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }
//</editor-fold>

}
