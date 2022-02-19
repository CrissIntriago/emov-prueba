/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.models.BusquedaPredios;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class GestionCobroPrevioCoactivaMB extends BusquedaPredios implements Serializable {

    @Inject
    private CatPredioPropietarioService catPredioPropietarioService;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private ServletSession ss;
    private String criterio;
    private CatPredio predio;
    private List<CatPredio> propiedades;
    private CatPredio propietario;
    private List<FinaRenLiquidacion> liquidaciones;
    private FinaRenLiquidacion liquidacion;

    @PostConstruct
    public void initView() {
        propietario = new CatPredio();
        predio = new CatPredio();
        liquidacion = new FinaRenLiquidacion();
    }

    public void consultarLiquidaciones() {
        propiedades = catPredioPropietarioService.findByIdentificacionPropietario(criterio);
        System.out.println("LAS PROPIEDADES ENCONTRADAS SON " + propiedades);
        if (propiedades.isEmpty()) {
            JsfUtil.addInformationMessage("Búsqueda", "Contribuyente no encontrado");
        }
        else if (!propiedades.isEmpty()) {
            JsfUtil.addInformationMessage("Búsqueda", "Contribuyente fue encontrado");
            System.out.println("el id del predio es --->" +propiedades);
        }else {
            System.out.println("viene null");
        }
            
    }

    public void buscarLiquidacion(SelectEvent evn) {
        propietario = (CatPredio) evn.getObject();
        liquidaciones = (List<FinaRenLiquidacion>) finaRenLiquidacionService.getLiquidacionesByIdPredioEstadoPendientePago(propietario);
//        System.out.println("las liquidaciones son ---> "+liquidaciones);
        if (liquidaciones.isEmpty()) {
            JsfUtil.addInformationMessage("", "No tiene liquidaciones pendientes de pago o ya se encuentran en coactiva");
        } else {
            JsfUtil.update("mainForm:dtLiquidaciones");
        }
    }

    public void seleccionLiquidacion(FinaRenLiquidacion liquidacion) {
        List<FinaRenLiquidacion> lista = new ArrayList<>();
        lista.add(liquidacion);
        calculoTotalPago(lista, null);
        lista.get(0).calcularPagoConCoactiva();
        this.liquidacion = lista.get(0);

        JsfUtil.update("dlgDetalle");
        JsfUtil.executeJS("PF('dlgDetalle').show();");
    }

    public void generearaReporte(FinaRenLiquidacion liquidacion) {
        System.out.println("el id de la liquidacion es--->" +liquidacion.getId());
            ss.instanciarParametros();
            ss.addParametro("ID", liquidacion.getId());
            ss.setNombreSubCarpeta("GestionTributatia/Recaudacion");
            ss.setNombreReporte("sPreviaCoactiva");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<CatPredio> getPropiedades() {
        return propiedades;
    }

    public void setPropietarios(List<CatPredio> propiedades) {
        this.propiedades = propiedades;
    }

    public CatPredio getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredio propietario) {
        this.propietario = propietario;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

}
