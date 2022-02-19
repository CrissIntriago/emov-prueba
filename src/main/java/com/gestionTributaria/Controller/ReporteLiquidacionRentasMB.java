/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.TituloCredito;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.gestionTributaria.Services.TipoLiquidacionService;
import com.gestionTributaria.Services.TituloCreditoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.service.SecuenciaGeneralService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
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
public class ReporteLiquidacionRentasMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecaudacionMB.class.getName());
    @Inject
    private FinaRenTipoLiquidacionService tipoLiquidacionesService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private TipoLiquidacionService tipoLiquidacionService;
    @Inject
    private ServletSession ss;
    @Inject
    private SecuenciaGeneralService secuenciaService;
    @Inject
    private TituloCreditoService tituloService;
    private String criterioBusquedad;
    private String tipoPredio;
    private String criterioClaveCat;
    private CatPredio predio;
    private CatPredio predio2;
    private CatPredio predio3;
    private List<FinaRenLiquidacion> liquidaciones;
    private List<FinaRenTipoLiquidacion> tipoLiquidaciones = new ArrayList<>();
    private List<FinaRenTipoLiquidacion> tipoLiquidacionGeneral = new ArrayList<>();
    private List<FinaRenTipoLiquidacion> tipoLiquidacionesMasivo = new ArrayList<>();
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacion2;
    private FinaRenLiquidacion liquidacion;
    private Integer fechaDesde;
    private Integer fechaHasta;
    private SecuenciaGeneral secuencia;
    private String numLocal;
    private Integer anioDesde;
    private Integer aniohHasta;
    private Integer anio;
    private TituloCredito titulo;

    @PostConstruct
    public void init() {
        tipoLiquidacion2 = new FinaRenTipoLiquidacion();
        liquidaciones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        tipoLiquidacionesMasivo = tipoLiquidacionService.tipoliquidacionesLocalesMasivo();
        tipoLiquidaciones = tipoLiquidacionService.tipoliquidacionesLocales();
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        predio = new CatPredio();
        criterioBusquedad = "predio";
        tipoPredio = "urbano";
        tipoLiquidacionGeneral = tipoLiquidacionService.tipoliquidacionesGenerales();
        titulo = new TituloCredito();
    }

    public void buscarLiquidacion() {
        if (criterioBusquedad.equals("predio")) {
            if (tipoPredio.equals("urbano")) {
                tipoLiquidacion = tipoLiquidacionService.getTipoLiquidacionBYprefijo("PRU");
                liquidaciones = liquidacionService.getLiquidacionByClaveCatastral(formarClaveCatastral(), tipoLiquidacion);
            } else {
                tipoLiquidacion = tipoLiquidacionService.getTipoLiquidacionBYprefijo("PDR");
                liquidaciones = liquidacionService.getLiquidacionByClaveCatastral(formarClaveCatastral(), tipoLiquidacion);
            }
        }
        if (criterioBusquedad.equals("locales")) {
            liquidaciones = liquidacionService.getLiquidacionByClaveCatastralLocalComercial(formarClaveCatastral(), tipoLiquidacion, numLocal);
        }
        if (criterioBusquedad.equals("liquidaciones")) {

        }
        if (liquidaciones.isEmpty()) {
            JsfUtil.addInformationMessage("", "NO TIENE DEUDAS");
        }
    }

    public String formarClaveCatastral() {
        String clave = "";
        //urbano
        if ((criterioBusquedad.equals("predio") && tipoPredio.equals("urbano")) || (criterioBusquedad.equals("locales"))) {

            //parroquia
            clave = predio.getParroquia() == null ? clave + "1." : clave + predio.getParroquia() + ".";
            //sector
            clave = predio.getSector() == null ? clave + "0." : clave + predio.getSector() + ".";
            //manzana
            clave = predio.getMz() == null ? clave + "0." : clave + predio.getMz() + ".";
            //solar
            clave = predio.getSolar() == null ? clave + "0." : clave + predio.getSolar() + ".";
            //div 1
            clave = predio.getDiv1() == null ? clave + "0." : clave + predio.getDiv1() + ".";
            //div 2
            clave = predio.getDiv2() == null ? clave + "0." : clave + predio.getDiv2() + ".";
            //div 3
            clave = predio.getDiv3() == null ? clave + "0." : clave + predio.getDiv3() + ".";
            //div 4
            clave = predio.getDiv4() == null ? clave + "0." : clave + predio.getDiv4() + ".";
            //div phv
            clave = predio.getPhv() == null ? clave + "0." : clave + predio.getPhv() + ".";
            //div phh
            clave = predio.getPhh() == null ? clave + "0" : clave + predio.getPhh();
        }
        if (criterioBusquedad.equals("predio") && tipoPredio.equals("rural")) {
            //rural
            //parroquia
            clave = predio.getParroquia() == null ? clave + "1." : clave + predio.getParroquia() + ".";
            //sector
            clave = predio.getSector() == null ? clave + "0." : clave + predio.getSector() + ".";
            //codigo
            clave = predio.getNumPredio() == null ? clave + "0." : clave + predio.getNumPredio() + ".";
            //div 1
            clave = predio.getDiv1() == null ? clave + "0." : clave + predio.getDiv1() + ".";
            //div 2
            clave = predio.getDiv2() == null ? clave + "0" : clave + predio.getDiv2();
        }
        System.out.println("LA CLAVE" + clave);
        return clave;
    }

    public void limpiar() {
        predio = new CatPredio();
        liquidaciones = new ArrayList<>();
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        criterioBusquedad = "predio";
        tipoPredio = "urbano";
    }

    public void generarTodasLiquidacionesPendientePago() {
        if (!liquidaciones.isEmpty()) {
            ss.setNombreReporte("tituloCreditoUrbanoMasivo");
            ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
            ss.addParametro("codigo_predio", liquidaciones.get(0).getPredio().getId());
            ss.addParametro("tipo_liquidacion", tipoLiquidacion.getId());
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addInformationMessage("", "Debe realizar una busquedad para generar las notas de crédito");
        }
    }

    public void generarTodasLiquidacionesPendientePagoRural() {
        if (!liquidaciones.isEmpty()) {
            ss.setNombreReporte("tituloCreditoRuralMasivo");
            ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
            ss.addParametro("codigo_predio", liquidaciones.get(0).getPredio().getId());
            ss.addParametro("tipo_liquidacion", tipoLiquidacion.getId());
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addInformationMessage("", "Debe realizar una busquedad para generar las notas de crédito");
        }
    }

    public void generarTodasLiquidacionesPendienteLocales() {
        if (!liquidaciones.isEmpty()) {
            ss.setNombreReporte("tituloCreditoLocalesMasivo");
            ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
            ss.addParametro("codigo_predio", liquidaciones.get(0).getPredio().getId());
            ss.addParametro("tipo_liquidacion", tipoLiquidacion.getId());
            ss.addParametro("num_local", numLocal);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addInformationMessage("", "Debe realizar una busquedad para generar las notas de crédito");
        }
    }

    public void generarReporte(FinaRenLiquidacion liquidacion) {
        SecuenciaGeneral secuencia = secuenciaService.findByCodigoAndAmbiente("SECUENCIA_TITULO_CREDITO", 1L);
        if (!liquidaciones.isEmpty()) {
            if (criterioBusquedad.equals("predio")) {
                if (liquidacion != null) {
                    titulo = tituloService.findByLiquidacion(liquidacion);
                    if (titulo != null) {
                        ss.setNombreReporte("tituloCreditoUrbano");
                        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                        ss.addParametro("ID_LIQUIDACION", liquidacion.getId());
                        ss.addParametro("ANIO", "" + Utils.getAnio(new Date()));
                        ss.addParametro("CODIGOTITULO", titulo.getCodigo());
                    } else {
                        titulo = new TituloCredito();
                        titulo.setIdLiquidacion(BigInteger.valueOf(liquidacion.getId()));
                        titulo.setCodigo("" + secuencia.getSecuencia().toString() + "-" + liquidacion.getTipoLiquidacion().getId().toString() + "-" + Utils.getAnio(new Date()).toString());
                        tituloService.create(titulo);
                        secuencia.setSecuencia(BigInteger.valueOf(secuencia.getSecuencia().intValue() + 1));
                        secuenciaService.edit(secuencia);
                        ss.setNombreReporte("tituloCreditoUrbano");
                        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                        ss.addParametro("ID_LIQUIDACION", liquidacion.getId());
                        ss.addParametro("ANIO", "" + Utils.getAnio(new Date()));
                        ss.addParametro("CODIGOTITULO", titulo.getCodigo());
                        liquidacion.setValidada(Boolean.FALSE);
                        liquidacionService.edit(liquidacion);
                    }
                }
            }
            if (criterioBusquedad.equals("locales")) {
                if (liquidacion != null) {
                    titulo = tituloService.findByLiquidacion(liquidacion);
                    if (titulo != null) {
                        ss.setNombreReporte("tituloCreditoLocalComercial");
                        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                        ss.addParametro("num_predio", liquidacion.getPredio().getId());
                        ss.addParametro("idtipoliquidacion", tipoLiquidacion.getId());
                        ss.addParametro("num_local", numLocal);
                        ss.addParametro("idLiquidacion", liquidacion.getId());
                        ss.addParametro("CODIGOTITULO", titulo.getCodigo().toString());
                        ss.addParametro("FECHAIMPRESION", new Date());
                    } else {
                        titulo = new TituloCredito();
                        titulo.setIdLiquidacion(BigInteger.valueOf(liquidacion.getId()));
                        titulo.setCodigo("" + secuencia.getSecuencia().toString() + "-" + liquidacion.getCodigoLocal().toString() + "-" + liquidacion.getTipoLiquidacion().getId().toString() + "-" + Utils.getAnio(new Date()).toString());
                        tituloService.create(titulo);
                        secuencia.setSecuencia(BigInteger.valueOf(secuencia.getSecuencia().intValue() + 1));
                        secuenciaService.edit(secuencia);
                        ss.setNombreReporte("tituloCreditoLocalComercial");
                        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                        ss.addParametro("num_predio", liquidacion.getPredio().getId());
                        ss.addParametro("idtipoliquidacion", tipoLiquidacion.getId());
                        ss.addParametro("num_local", numLocal);
                        ss.addParametro("idLiquidacion", liquidacion.getId());
                        ss.addParametro("CODIGOTITULO", titulo.getCodigo());
                        ss.addParametro("FECHAIMPRESION", new Date());
                        liquidacion.setValidada(Boolean.FALSE);
                        liquidacionService.edit(liquidacion);
                    }
                }

            }
            ss.setImprimir(Boolean.FALSE);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
        if (liquidaciones.isEmpty()) {
            JsfUtil.addInformationMessage("", "Debe realizar una busquedad para generar las notas de crédito");
        }
    }

    public void generarReporteLqg() {
        if (tipoLiquidacion != null && anio != null) {
            ss.setNombreReporte("tituloCreditoLQG");
            ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
            ss.addParametro("idLiquidacion", tipoLiquidacion.getId());
            ss.addParametro("anio", anio);
            ss.addParametro("FECHAIMPRESION", new Date());
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addInformationMessage("", "Debe Seleccionar un Tipo de liquidacion");
        }
    }

    public void generarMasivo() {
        if (tipoLiquidacion2.getId() != null) {
            if (tipoLiquidacion2.getId().intValue() == 2 || tipoLiquidacion2.getId().intValue() == 3) {

            } else {
                ss.setNombreReporte("tituloCreditoMasivoRentas");
                ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                ss.addParametro("tipo_liquidacion", tipoLiquidacion2.getId());
                ss.addParametro("anioDesde", anioDesde);
                ss.addParametro("aniohasta", aniohHasta);
                ss.addParametro("FECHAIMPRESION", new Date());
            }
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addInformationMessage("", "Debe Seleccionar un Tipo de liquidacion");
        }

    }

    public void posibleTituloCredito() {
        if (tipoLiquidacion2.getId() != null) {
            if (anioDesde <= aniohHasta) {
                ss.setNombreReporte("posiblesTitulosCredito");
                ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                ss.addParametro("DESDE", anioDesde);
                ss.addParametro("HASTA", aniohHasta);
                ss.setImprimir(Boolean.FALSE);
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else {
                JsfUtil.addErrorMessage("URGENTE", "EL AÑO DESDE DEBE SER MAYOR O IGUAL AL AÑO HASTA");
            }

        } else {
            JsfUtil.addInformationMessage("", "Debe Seleccionar un Tipo de liquidacion");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public String getCriterioBusquedad() {
        return criterioBusquedad;
    }

    public void setCriterioBusquedad(String criterioBusquedad) {
        this.criterioBusquedad = criterioBusquedad;
    }

    public String getCriterioClaveCat() {
        return criterioClaveCat;
    }

    public void setCriterioClaveCat(String criterioClaveCat) {
        this.criterioClaveCat = criterioClaveCat;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
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

    public FinaRenTipoLiquidacionService getTipoLiquidacionesService() {
        return tipoLiquidacionesService;
    }

    public void setTipoLiquidacionesService(FinaRenTipoLiquidacionService tipoLiquidacionesService) {
        this.tipoLiquidacionesService = tipoLiquidacionesService;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Integer getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Integer fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Integer getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Integer fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public CatPredio getPredio2() {
        return predio2;
    }

    public void setPredio2(CatPredio predio2) {
        this.predio2 = predio2;
    }

    public CatPredio getPredio3() {
        return predio3;
    }

    public void setPredio3(CatPredio predio3) {
        this.predio3 = predio3;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getNumLocal() {
        return numLocal;
    }

    public void setNumLocal(String numLocal) {
        this.numLocal = numLocal;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidacionesMasivo() {
        return tipoLiquidacionesMasivo;
    }

    public void setTipoLiquidacionesMasivo(List<FinaRenTipoLiquidacion> tipoLiquidacionesMasivo) {
        this.tipoLiquidacionesMasivo = tipoLiquidacionesMasivo;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAniohHasta() {
        return aniohHasta;
    }

    public void setAniohHasta(Integer aniohHasta) {
        this.aniohHasta = aniohHasta;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion2() {
        return tipoLiquidacion2;
    }

    public void setTipoLiquidacion2(FinaRenTipoLiquidacion tipoLiquidacion2) {
        this.tipoLiquidacion2 = tipoLiquidacion2;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidacionGeneral() {
        return tipoLiquidacionGeneral;
    }

    public void setTipoLiquidacionGeneral(List<FinaRenTipoLiquidacion> tipoLiquidacionGeneral) {
        this.tipoLiquidacionGeneral = tipoLiquidacionGeneral;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
//</editor-fold>

}
