/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatParroquia;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatSectores;
import com.gestionTributaria.Services.CatCiudadelasService;
import com.gestionTributaria.Services.CatParroquiaService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.SectoresService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
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
public class CarteraVencidaMB implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private CatParroquiaService services;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private CatCiudadelasService ciudadelasService;
    @Inject
    private SectoresService sectoresService;
    private List<CatParroquia> catParroquias;
    private Canton canton;
    private CatPredio predio;
    private CatPredio predio2;
    private CatParroquia parroquia;
    private String criterioFiltro;
    private Date fechaDesde;
    private Date fechaHasta;
    private List<FinaRenLiquidacion> cartera;
    private BigDecimal recaudado;
    private BigDecimal recaudar;
    private BigDecimal totalRecaudar;
    private List<CatCiudadela> ciudadelas;
    private CatCiudadela ciudadela;
    private List<CatSectores> sectoresLista;
    private CatSectores sector;
    private String opcionBusquedad;
    private String opcionBusquedadRural;

    @PostConstruct
    public void initView() {
        try {
            opcionBusquedad = "1";
            opcionBusquedadRural = "1";
            ciudadela = new CatCiudadela();
            fechaDesde = new Date();
            fechaHasta = new Date();
            parroquia = new CatParroquia();
            predio = new CatPredio();
            predio2 = new CatPredio();
            canton = new Canton(81L);
            catParroquias = services.findAllParroquiaByCanton(canton);
            cartera = new ArrayList<>();
            recaudado = new BigDecimal("0.00");
            recaudar = new BigDecimal("0.00");
            totalRecaudar = new BigDecimal("0.00");
            ciudadelas = ciudadelasService.getAllCiudadelas();
            sectoresLista = sectoresService.getAllSectores();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Logger.getLogger(CarteraVencidaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void reporteCarteraVencida(int opc, boolean excel) {
        if (excel) {
                ss.setOnePagePerSheet(false);
                ss.setContentType("xlsx");
            }
        try {
            if (opc == 1) {
                ss.setNombreReporte("carteraVencida");
                ss.setNombreSubCarpeta("GestionTributatia/Coactiva/CarteraVencida");
                ss.addParametro("opcion", Integer.parseInt(opcionBusquedad));
                if (opcionBusquedad.equals("1")) {
                    ss.addParametro("parroquia", predio.getParroquia().intValue() == 1 ? 3 : predio.getParroquia().intValue());
                    ss.addParametro("sector", predio.getSector() == null ? null : predio.getSector().intValue());
                    ss.addParametro("manzana", predio.getMz() == null ? null : predio.getMz().intValue());
                }
                if (opcionBusquedad.equals("3")) {
                    ss.addParametro("numeroPredio", predio.getNumPredio().intValue());
                }
                if (opcionBusquedad.equals("4")) {
                    ss.addParametro("codigoAnt", predio.getPredialant());
                }
                if (opcionBusquedad.equals("5")) {
                    ss.addParametro("idCiudadela", ciudadela.getId().intValue());
                }
            }
            if (opc == 2) {
                ss.setNombreReporte("carteraVencidaRural");
                ss.setNombreSubCarpeta("GestionTributatia/Coactiva/CarteraVencida");
                ss.addParametro("OPC", Integer.parseInt(opcionBusquedadRural));
                if (opcionBusquedadRural.equals("1")) {
                    System.out.println("VALOR PARROQUA" + predio2.getParroquia().intValue());
                    System.out.println("VALOR SECTOR" + predio2.getSector());
                    ss.addParametro("parroquia", predio2.getParroquia().intValue() == 1 ? 3 : predio2.getParroquia().intValue());
                    ss.addParametro("sector", predio2.getSector() == null ? null : predio2.getSector().intValue());
                }
                if (opcionBusquedadRural.equals("2")) {
                    ss.addParametro("codigoPredial", predio2.getNumPredio().intValue());
                }
                if (opcionBusquedadRural.equals("3")) {
                    ss.addParametro("idSector", sector.getSector().intValue());
                }
            }
            ss.setImprimir(Boolean.FALSE);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } catch (Exception e) {
            Logger.getLogger(CarteraVencidaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void recaudacionCarteraVencida() {
        try {
            cartera = finaRenLiquidacionService.bucarCorteFecha(fechaDesde, fechaHasta);
            recaudado = finaRenLiquidacionService.recaudado(fechaDesde, fechaHasta);
            recaudar = finaRenLiquidacionService.recaudar(fechaDesde, fechaHasta);
            totalRecaudar = finaRenLiquidacionService.totalRecaudar(fechaDesde, fechaHasta);
        } catch (Exception ex) {
            Logger.getLogger(CarteraVencidaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get and set">
    public List<CatParroquia> getCatParroquias() {
        return catParroquias;
    }

    public void setCatParroquias(List<CatParroquia> catParroquias) {
        this.catParroquias = catParroquias;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public String getCriterioFiltro() {
        return criterioFiltro;
    }

    public void setCriterioFiltro(String criterioFiltro) {
        this.criterioFiltro = criterioFiltro;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<FinaRenLiquidacion> getCartera() {
        return cartera;
    }

    public void setCartera(List<FinaRenLiquidacion> cartera) {
        this.cartera = cartera;
    }

    public BigDecimal getRecaudado() {
        return recaudado;
    }

    public void setRecaudado(BigDecimal recaudado) {
        this.recaudado = recaudado;
    }

    public BigDecimal getRecaudar() {
        return recaudar;
    }

    public void setRecaudar(BigDecimal recaudar) {
        this.recaudar = recaudar;
    }

    public BigDecimal getTotalRecaudar() {
        return totalRecaudar;
    }

    public void setTotalRecaudar(BigDecimal totalRecaudar) {
        this.totalRecaudar = totalRecaudar;
    }

    public List<CatCiudadela> getCiudadelas() {
        return ciudadelas;
    }

    public void setCiudadelas(List<CatCiudadela> ciudadelas) {
        this.ciudadelas = ciudadelas;
    }

    public CatCiudadela getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(CatCiudadela ciudadela) {
        this.ciudadela = ciudadela;
    }

    public List<CatSectores> getSectoresLista() {
        return sectoresLista;
    }

    public void setSectoresLista(List<CatSectores> sectoresLista) {
        this.sectoresLista = sectoresLista;
    }

    public CatSectores getSector() {
        return sector;
    }

    public void setSector(CatSectores sector) {
        this.sector = sector;
    }

    public String getOpcionBusquedad() {
        return opcionBusquedad;
    }

    public void setOpcionBusquedad(String opcionBusquedad) {
        this.opcionBusquedad = opcionBusquedad;
    }

    public String getOpcionBusquedadRural() {
        return opcionBusquedadRural;
    }

    public void setOpcionBusquedadRural(String opcionBusquedadRural) {
        this.opcionBusquedadRural = opcionBusquedadRural;
    }

    public CatPredio getPredio2() {
        return predio2;
    }

    public void setPredio2(CatPredio predio2) {
        this.predio2 = predio2;
    }
//</editor-fold>

}
