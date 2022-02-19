/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arturo
 */

@Named
@ViewScoped
public class ReimpresionComprobantesMB implements Serializable {
    
    @Inject
    private ServletSession ss;
    
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    
    protected List<FinaRenLiquidacion> emisionesPrediales;
    
    private FinaRenLiquidacion liquidacion;
    
    private FinaRenPago finaRenPago;
    private Boolean is_print;
    
    @PostConstruct
    public void init() {
        finaRenPago = new FinaRenPago();
        emisionesPrediales = new ArrayList<>();
        is_print = Boolean.FALSE;
    }
    
    
    public void buscarLiquidaciones( ){
        System.out.println("numero de comprobante:" +finaRenPago.getNumComprobante());
        this.is_print = Boolean.FALSE;
        if(finaRenPago.getNumComprobante() != null){
            emisionesPrediales = liquidacionService.liquidacionesByNumeroComprobante(finaRenPago.getNumComprobante());
             System.out.println("emisiones prediales:"+emisionesPrediales.toString());
            if(emisionesPrediales.size() > 0){
                PrimeFaces.current().ajax().update("mainForm:dtDatos");
                this.is_print = Boolean.TRUE;
                PrimeFaces.current().ajax().update("mainForm:btnImprimirComprobante");
            }else{
                JsfUtil.addWarningMessage("Comprobantes","No se han encontrado datos");
                emisionesPrediales =  new ArrayList<>();
                PrimeFaces.current().ajax().update("mainForm:dtDatos");
                this.is_print = Boolean.FALSE;
                PrimeFaces.current().ajax().update("mainForm:btnImprimirComprobante");
            }     
        }else{
            emisionesPrediales =  new ArrayList<>();
            JsfUtil.addWarningMessage("Comprobantes","No se han encontrado datos");
            PrimeFaces.current().ajax().update("mainForm:dtDatos");
            this.is_print = Boolean.FALSE;
            PrimeFaces.current().ajax().update("mainForm:btnImprimirComprobante");
        }
        System.out.println("is print:"+is_print);
    }
    
    public void verDetalle(FinaRenLiquidacion liq) {
        this.liquidacion = liq;
        System.out.println("liquidacion:"+liquidacion.toString());
        JsfUtil.executeJS("PF('dlgDetalle').show();");
        JsfUtil.update("formDetEmision");
    }
    
    @Asynchronous
    public void generarComprobante() {
        ss.addParametro("COMPROBANTE", finaRenPago.getNumComprobante().longValue());
        ss.setNombreSubCarpeta("GestionTributatia/comprobantes");
        ss.setNombreReporte("comprobante");
        ss.setImprimir(Boolean.TRUE);
        System.out.println("parametros--->>" + ss.getParametros());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public List<FinaRenLiquidacion> getEmisionesPrediales() {
        return emisionesPrediales;
    }

    public void setEmisionesPrediales(List<FinaRenLiquidacion> emisionesPrediales) {
        this.emisionesPrediales = emisionesPrediales;
    } 

    public FinaRenPago getFinaRenPago() {
        return finaRenPago;
    }

    public void setFinaRenPago(FinaRenPago finaRenPago) {
        this.finaRenPago = finaRenPago;
    }

    public Boolean getIs_print() {
        return is_print;
    }

    public void setIs_print(Boolean is_print) {
        this.is_print = is_print;
    }
    
    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
    
}
