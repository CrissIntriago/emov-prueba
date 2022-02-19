/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Services.CatPredioService;
import com.gestionTributaria.Services.CoaJuicioPredioServices;
import com.gestionTributaria.Services.FinaRenLocalComercialService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class ConsultaPrediosMB implements Serializable {

    @Inject
    private CoaJuicioPredioServices coaJuicioService;
    @Inject
    private CatPredioService prediosService;
    @Inject
    private CoaJuicioPredioServices juicioPredioService;
    @Inject
    private FinaRenLocalComercialService localComercialService;
    private CatPredio predio;
    private String opcionBusquedad = "";
    private String div1 = "", div2 = "", div3 = "", div4 = "", local = "";
    private String clave_catastral = "";
    private List<CatPredio> listaPredios;
    private List<CoaJuicio> juicios;
    private List<FinaRenLiquidacion> historialPagos;
    private FinaRenLiquidacion liquidacionSeleccionada;
    private String urlFoto;
    private String url = "http://";
    private String urlip = "172.17.50.49:8341/";
    private String urlContext = "origamigt/images/";
    private String urlConmpleta = null;
    private FinaRenLocalComercial localComercial;

    @PostConstruct
    public void init() {
        predio = new CatPredio();
        opcionBusquedad = "U";
        listaPredios = new ArrayList<>();
        juicios = new ArrayList<>();
        historialPagos = new ArrayList<>();
        liquidacionSeleccionada = new FinaRenLiquidacion();
        localComercial = new FinaRenLocalComercial();
    }

    public void formandoClaveCatastra(int campo) {
        int i = 0;
        clave_catastral = "";
        //parroquia
        clave_catastral = "".equals(predio.getParroquia()) ? clave_catastral + "1." : clave_catastral + predio.getParroquia() + ".";
        for (i = 0; i < campo; i++) {
            if (opcionBusquedad.equals("U") || opcionBusquedad.equals("L")) {
                //sector
                if (i == 0) {
                    if ("".equals(predio.getSector()) || predio.getSector() == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + predio.getSector() + ".";
                    }
                }
                //manzana
                if (i == 1) {
                    if ("".equals(predio.getMz()) || predio.getMz() == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + predio.getMz() + ".";
                    }
                }
                //solar
                if (i == 2) {
                    if ("".equals(predio.getSolar()) || predio.getSolar() == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + predio.getSolar() + ".";
                    }
                }
                //div 1
                if (i == 3) {
                    if ("".equals(div1)) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div1 + ".";
                    }
                }
                //div 2
                if (i == 4) {
                    if ("".equals(div2)) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div2 + ".";
                    }
                }
                //div 3
                if (i == 5) {
                    if ("".equals(div3)) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div3 + ".";
                    }
                }
                //div 4
                if (i == 6) {
                    if ("".equals(div4)) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div4 + ".";
                    }
                }
                //div phv
                if (i == 7) {
                    if ("".equals(predio.getBloque()) || predio.getBloque() == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + predio.getBloque() + ".";
                    }
                }
                //div phh
                if (i == 8) {
                    if ("".equals(predio.getPiso()) || predio.getPiso() == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + predio.getPiso();
                    }
                }
            } else {
                if (i == 0) {
                    if ("".equals(predio.getSector()) || predio.getSector() == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + predio.getSector() + ".";
                    }
                }
                if (i == 1) {
                    if ("".equals(div3) || div3 == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div3 + ".";
                    }
                }
                //solar
                if (i == 2) {
                    if ("".equals(div1) || div1 == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div1 + ".";
                    }
                }
                //div 1
                if (i == 3) {
                    if ("".equals(div2) || div2 == null) {
                        break;
                    } else {
                        clave_catastral = clave_catastral + div2 + ".";
                    }
                }
            }
        }
        if (opcionBusquedad.equals("L")) {
            clave_catastral = clave_catastral.replaceFirst(".$", "");
            for (clave_catastral.length(); i < 9; i++) {
                clave_catastral = clave_catastral + ".0";
            }
            // se encuentra el predio
            listaPredios = prediosService.findByClaveCatastral(clave_catastral, "U");
            //buscamos los datos del local
            localComercial = localComercialService.findByNumPredio(listaPredios.get(0).getNumPredio().longValue(), local).get(0);
            System.out.println("EL LOICAL COMERCUIAL " + localComercial);
        } else {
            listaPredios(clave_catastral);
        }
    }

    public List<CatPredio> listaPredios(String claveCatastral) {
        listaPredios = prediosService.findByClaveCatastral(claveCatastral, opcionBusquedad);
        if (listaPredios != null) {
            if (listaPredios.size() < 1) {
                JsfUtil.addInformationMessage("Sin resultados", "");
            }
            return listaPredios;
        } else {
            JsfUtil.addInformationMessage("Sin resultados", "");
            return listaPredios = new ArrayList<>();
        }
    }

    public void predioSeleccionado(CatPredio predio) {
        this.predio = predio;
        juicios = juicioPredioService.findByPredio(this.predio);
        historialPagos = coaJuicioService.HistorialPagos(this.predio);
        System.out.println("LOS JUCIIOS SON: " + juicios);
        ConsultarFoto(predio);
    }

    public void capturarLiquidacionDetalle(FinaRenLiquidacion liquidacion) {
        liquidacion.calcularPagoConCoactiva();
        liquidacionSeleccionada = liquidacion;
    }

    public void ConsultarFoto(CatPredio predio) {
        urlConmpleta = url + urlip + urlContext + predio.getNumPredio().toString();
        System.out.println("URL COMPLETA: " + urlConmpleta);
        JsfUtil.update("mainForm:fotoPredio");
    }

//<editor-fold defaultstate="collapsed" desc="GET AND SET">
    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public List<CoaJuicio> getJuicios() {
        return juicios;
    }

    public void setJuicios(List<CoaJuicio> juicios) {
        this.juicios = juicios;
    }

    public String getOpcionBusquedad() {
        return opcionBusquedad;
    }

    public void setOpcionBusquedad(String opcionBusquedad) {
        this.opcionBusquedad = opcionBusquedad;
    }

    public String getDiv1() {
        return div1;
    }

    public void setDiv1(String div1) {
        this.div1 = div1;
    }

    public String getDiv2() {
        return div2;
    }

    public void setDiv2(String div2) {
        this.div2 = div2;
    }

    public String getDiv3() {
        return div3;
    }

    public void setDiv3(String div3) {
        this.div3 = div3;
    }

    public String getDiv4() {
        return div4;
    }

    public void setDiv4(String div4) {
        this.div4 = div4;
    }

    public String getClave_catastral() {
        return clave_catastral;
    }

    public void setClave_catastral(String clave_catastral) {
        this.clave_catastral = clave_catastral;
    }

    public List<CatPredio> getListaPredios() {
        return listaPredios;
    }

    public void setListaPredios(List<CatPredio> listaPredios) {
        this.listaPredios = listaPredios;
    }

    public List<FinaRenLiquidacion> getHistorialPagos() {
        return historialPagos;
    }

    public void setHistorialPagos(List<FinaRenLiquidacion> historialPagos) {
        this.historialPagos = historialPagos;
    }

    public FinaRenLiquidacion getLiquidacionSeleccionada() {
        return liquidacionSeleccionada;
    }

    public void setLiquidacionSeleccionada(FinaRenLiquidacion liquidacionSeleccionada) {
        this.liquidacionSeleccionada = liquidacionSeleccionada;
    }

    public String getUrlConmpleta() {
        return urlConmpleta;
    }

    public void setUrlConmpleta(String urlConmpleta) {
        this.urlConmpleta = urlConmpleta;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
    
     public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }
    
//</editor-fold>

   

}
