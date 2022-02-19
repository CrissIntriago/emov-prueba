/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.CatPredioService;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
public class TituloCreditoMB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private CatPredioService prediosService;
    @Inject
    private ServletSession ss;
    @Inject
    private FinaRenTipoLiquidacionService tipoliquidaicones;
    //PARA TITULOS DE CREDITO
    private List<CatPredio> listaObtenerTituloCredito;
    private String TipoSeleccionTituloCredito = "";
    private String claveCatastralBuscar = "";
    private String parroquiaClave = "", sector = "", manzana = "", solar = "", piso = "", bloque = "";
    private String div1 = "", div2 = "", div3 = "", div4 = "";
    private List<FinaRenTipoLiquidacion> tipoLiquidaciones;
    private FinaRenTipoLiquidacion tipoLiquidacion;

    @PostConstruct
    public void initView() {
        TipoSeleccionTituloCredito = "U";
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        tipoLiquidaciones = tipoliquidaicones.findTipoLiquidacionAplicaCoactiva();
    }

    public List<CatPredio> listaPredios(String claveCatastral) {
        listaObtenerTituloCredito = prediosService.findByClaveCatastral(claveCatastral, TipoSeleccionTituloCredito);
        if (listaObtenerTituloCredito != null) {
            return listaObtenerTituloCredito;
        } else {
            System.out.println("NO TIENE RESULTADOS");
            return listaObtenerTituloCredito = new ArrayList<>();
        }
    }

    public void formandoClaveCatastra(int campo) {
        int i = 0;
        claveCatastralBuscar = "";
        //parroquia
        claveCatastralBuscar = "".equals(parroquiaClave) ? claveCatastralBuscar + "1." : claveCatastralBuscar + sector + ".";
        for (i = 0; i < campo; i++) {
            //sector
            if (i == 0) {
                if ("".equals(sector)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + sector + ".";
                }
            }
            //manzana
            if (i == 1) {
                if ("".equals(manzana)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + manzana + ".";
                }
            }
            //solar
            if (i == 2) {
                if ("".equals(solar)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + solar + ".";
                }
            }
            //div 1
            if (i == 3) {
                if ("".equals(div1)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + div1 + ".";
                }
            }
            //div 2
            if (i == 4) {
                if ("".equals(div2)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + div2 + ".";
                }
            }
            //div 3
            if (i == 5) {
                if ("".equals(div3)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + div3 + ".";
                }
            }
            //div 4
            if (i == 6) {
                if ("".equals(div4)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + div4 + ".";
                }
            }
            //div phv
            if (i == 7) {
                if ("".equals(bloque)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + bloque + ".";
                }
            }
            //div phh
            if (i == 8) {
                if ("".equals(piso)) {
                    break;
                } else {
                    claveCatastralBuscar = claveCatastralBuscar + piso;
                }
            }
        }
        System.out.println("LA CLAVE CATASATRAL ES: " + claveCatastralBuscar);
        listaPredios(claveCatastralBuscar);
    }

    public void generarTituloCredito(CatPredio predio) {
        ss.setNombreReporte("tituloCreditoCoactiva");
        ss.setNombreSubCarpeta("/Coactiva/TituloCredito/");
        ss.addParametro("SECUENCIA_TITULO_CREDITO_COACTIVA", 1);
        ss.addParametro("TIPO_TITULO_CREDITO", tipoLiquidacion.getNombreTransaccion());
        ss.addParametro("DIA_TITULO", Utils.getDia(new Date()));
        ss.addParametro("MES_TITULO", Utils.getMes(new Date()));
        ss.addParametro("ANIO_TITULO", Utils.getAnio(new Date()));
        ss.addParametro("DIRECCION_MUNICIPIO", "CDLA. ABEL GILBERT III ETAPA BLOQUE 1 Y 2");
        ss.addParametro("NOMBRE_CONTRIBUYENTE", predio.getNombrePropietarios());
        ss.addParametro("TITULO_ANIOO", Utils.getAnio(new Date()));
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }
//<editor-fold defaultstate="collapsed" desc="generarGetAndSet">

    public List<CatPredio> getListaObtenerTituloCredito() {
        return listaObtenerTituloCredito;
    }

    public void setListaObtenerTituloCredito(List<CatPredio> listaObtenerTituloCredito) {
        this.listaObtenerTituloCredito = listaObtenerTituloCredito;
    }

    public String getTipoSeleccionTituloCredito() {
        return TipoSeleccionTituloCredito;
    }

    public void setTipoSeleccionTituloCredito(String TipoSeleccionTituloCredito) {
        this.TipoSeleccionTituloCredito = TipoSeleccionTituloCredito;
    }

    public String getClaveCatastralBuscar() {
        return claveCatastralBuscar;
    }

    public void setClaveCatastralBuscar(String claveCatastralBuscar) {
        this.claveCatastralBuscar = claveCatastralBuscar;
    }

    public String getParroquiaClave() {
        return parroquiaClave;
    }

    public void setParroquiaClave(String parroquiaClave) {
        this.parroquiaClave = parroquiaClave;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getSolar() {
        return solar;
    }

    public void setSolar(String solar) {
        this.solar = solar;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
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

    public List<FinaRenTipoLiquidacion> getTipoLiquidaciones() {
        return tipoLiquidaciones;
    }

    public void setTipoLiquidaciones(List<FinaRenTipoLiquidacion> tipoLiquidaciones) {
        this.tipoLiquidaciones = tipoLiquidaciones;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }
//</editor-fold>

}
