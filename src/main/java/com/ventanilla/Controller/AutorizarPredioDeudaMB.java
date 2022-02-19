/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.google.gson.Gson;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.ventanilla.Entity.PredioAutorizar;
import com.ventanilla.Models.CatPredioModel;
import com.ventanilla.Models.DataPredioAutorizar;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class AutorizarPredioDeudaMB implements Serializable{
   @Inject
   private VentanillaService ventanillaService;
  
   @Inject
   private UserSession userSession;
   
   private LazyModel<PredioAutorizar> lazyPredios;
   
   private DataPredioAutorizar dataPredioAutorizar;
   
   private DataPredioAutorizar dataPredio;
   
   private PredioAutorizar predioAutorizar;
   
   private String observacion;
   
   @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {       
           this.lazyPredios = new LazyModel<>(PredioAutorizar.class);
           this.lazyPredios.getFilterss().put("autorizado", false);
           this.lazyPredios.getFilterss().put("cancelado", false);
           observacion = "";
           predioAutorizar = new PredioAutorizar();
           dataPredio = new DataPredioAutorizar();
            dataPredioAutorizar = new DataPredioAutorizar();
        }

    }
    
    public void getPredios(){
        this.lazyPredios = new LazyModel<>(PredioAutorizar.class);
        this.lazyPredios.getFilterss().put("autorizado", false);
        this.lazyPredios.getFilterss().put("cancelado", false);
        
         JsfUtil.redirect(JsfUtil.getHostContextUrl() + "ventanillaUnica/administracion/autorizarPredio");
    }
    
    public String getJsonData(String dato,String campo){     
        String respuesta = "";
        dataPredioAutorizar = new DataPredioAutorizar();
        Gson gson = new Gson();
        dataPredioAutorizar = gson.fromJson(dato, DataPredioAutorizar.class);
        
        switch(campo){
            case "concepto":
                respuesta = dataPredioAutorizar.getConcepto();
            break;
            
            case "predio":
                respuesta = dataPredioAutorizar.getCatPredio().getCodigoPredial().toString();                
                break;
        }      
        return respuesta;
    }
    
    
    public void abrirDlg(PredioAutorizar predio) {
        if (predio != null) {
            predioAutorizar = predio;
        }
        JsfUtil.executeJS("PF('dlgAutorizado').show();");
 
    }
    
//    public void abrirDlgPredio(String dato) {
//
//        if (dato != null) {
//            Gson gson = new Gson();
//            dataPredio = gson.fromJson(dato, DataPredioAutorizar.class);
//            System.out.println("dato predio autorizar:"+dataPredio.toString());
//        }
//        JsfUtil.executeJS("PF('dialogDeudas').show();");
// 
//    }
    
    public void autorizarPredio(){
        
        predioAutorizar.setAutorizado(true);
        predioAutorizar.setFechaAutorizacion(new Date());
        predioAutorizar.setObservacion_autoriza(observacion);
        predioAutorizar.setUsuarioAutoriza(userSession.getNameUser());
        
        
        ventanillaService.updateEntity(predioAutorizar);
        
        PrimeFaces.current().executeScript("PF('dlgAutorizado').hide()");
        PrimeFaces.current().ajax().update("dtAutorizarPredio");
        JsfUtil.addInformationMessage("Autorizar Predio", "Se actualizo el estado del predio correctamente");
    
    }
   
    
    public LazyModel<PredioAutorizar> getLazyPredios() {
        return lazyPredios;
    }

    public void setLazyPredios(LazyModel<PredioAutorizar> lazyPredios) {
        this.lazyPredios = lazyPredios;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public DataPredioAutorizar getDataPredioAutorizar() {
        return dataPredioAutorizar;
    }

    public void setDataPredioAutorizar(DataPredioAutorizar dataPredioAutorizar) {
        this.dataPredioAutorizar = dataPredioAutorizar;
    }

    public DataPredioAutorizar getDataPredio() {
        return dataPredio;
    }

    public void setDataPredio(DataPredioAutorizar dataPredio) {
        this.dataPredio = dataPredio;
    }
}
