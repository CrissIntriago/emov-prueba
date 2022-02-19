/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.gestionTributaria.models.BusquedaPredios;
import com.origami.sigef.common.util.JsfUtil;
import java.math.BigDecimal;
import javax.inject.Inject;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class ConsultaLiquidacionesMB extends BusquedaPredios implements Serializable{
   
   protected List<FinaRenLiquidacion> liquidacionList;
    
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    
    private List<FinaRenLiquidacion> emisionesCobro;
    
    private FinaRenLiquidacion liquidacion;
    
    @PostConstruct
    public void init(){
        liquidacionList = new ArrayList<>();        
        emisionesCobro = new ArrayList<>();
    }
   
    
     public void liquidacionesdirectas() {
        this.consultarTipoLiquidacionesEmisiones();
        Boolean act = Boolean.FALSE;
        for (FinaRenLiquidacion l : emisionesPrediales) {
            if (!emisionesCobro.contains(l)) {
                emisionesCobro.add(l);
                this.calculoTotalPago(emisionesCobro, null);
                act = Boolean.TRUE;
                System.out.println("emisiones>>" + emisionesCobro);
            }
        }
        if (act) {
            this.getModelPago().setValorCobrar(BigDecimal.ZERO);
            this.getModelPago().setValorSaldoPagoFinal(BigDecimal.ZERO);
            this.getModelPago().setValorCobrar(getModelPago().getValorCobrar().add(totalEmisiones));
            this.getModelPago().setValorSaldoPagoFinal(getModelPago().getValorSaldoPagoFinal().add(totalEmisiones));
        }

        this.emisionesPrediales.clear();
    }
     
    public FnConvenioPagoDetalle getCuotaConvenioByLiquidacion(FinaRenLiquidacion liqui) {
        if (liqui != null || liqui.getId() != null) {
            return liquidacionService.getCuaotaByLiquidacion(liqui);
        }
        return null;
    }
    
    public void filterByEstado(int tipo){
        
        List<FinaRenLiquidacion> emisionesTemp = new ArrayList<>();
        switch(tipo){
            case 1: 
                System.out.println("entro caso 1");
                for (FinaRenLiquidacion l : emisionesPrediales) {
                    if (l.getEstadoLiquidacion().getId() == 1 ) {
                       
                       emisionesTemp.add(l);
                    }
                }
                
                break;
            case 2:
                System.out.println("entro caso 2");
                for (FinaRenLiquidacion l : emisionesPrediales) {
                    if (l.getEstadoLiquidacion().getId() == 2) {
                       
                       emisionesTemp.add(l);
                    }
                }
                
                break;
                
            case -1:
               
                this.consultarLiquidaciones();
                
                break;
        }
       
        emisionesPrediales = emisionesTemp;
        JsfUtil.update("mainForm:dataLiqEmi");  
    }
    
    public void verDetalle(FinaRenLiquidacion liq) {
        this.liquidacion = liq;
        System.out.println("liquidacion:"+liquidacion.toString());
        JsfUtil.executeJS("PF('dlgDetalle').show();");
        JsfUtil.update("formDetEmision");
    }


    public List<FinaRenLiquidacion> getLiquidacionList() {
        return liquidacionList;
    }

    public void setLiquidacionList(List<FinaRenLiquidacion> liquidacionList) {
        this.liquidacionList = liquidacionList;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

}
