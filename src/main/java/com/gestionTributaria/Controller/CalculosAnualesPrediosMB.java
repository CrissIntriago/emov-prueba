/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.CatPredioSumasAnualesUbicacion;
import com.asgard.Entity.CatUbicacion;
import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatParroquia;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CenAvaluoMunicipal;
import com.gestionTributaria.Entities.Obra;
import com.gestionTributaria.Entities.PredioAnio;
import com.gestionTributaria.Services.CatCiudadelasService;
import com.gestionTributaria.Services.CatPredioService;
import com.gestionTributaria.Services.CatPredioSumasAnualesUbicacionServices;
import com.gestionTributaria.Services.CatUbicacionService;
import com.gestionTributaria.Services.CenAvaluoMunicipalService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.PredioAnioService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
@Named(value = "calculosAnualesPredios")
@ViewScoped
public class CalculosAnualesPrediosMB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ManagerService manager;
    private Map<String, Object> parametros = new HashMap<>();
    @Inject
    private ServletSession servletSession;
    @Inject
    private CatUbicacionService ubicacionesService;
    @Inject
    private CatCiudadelasService ciudadelasService;
    private Obra obra;
    private LazyModel<Obra> obras;
    private List<CatUbicacion> ubicaciones;
    private List<CatCiudadela> ciudadelas;
    private CatCiudadela ciudadela;
    private Boolean bandera;
    private Integer anioActual;
    private Integer anio;

    @PostConstruct
    public void initView() {
        try {
            anioActual = Utils.getAnio(new Date());
            bandera = false;
            ciudadela = new CatCiudadela();
            obras = new LazyModel<>(Obra.class);
            ubicaciones = new ArrayList<>();
            ciudadelas = ciudadelasService.getAllCiudadelasTodas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarUbicacionesPorObra(Obra obra) {
        this.obra = obra;
        ubicaciones = ubicacionesService.getUbicacionesByObra(obra);
        if (ubicaciones == null) {
            ubicaciones = new ArrayList<>();
        }
    }

    public void anadirUbiacion() {
        CatUbicacion ubicacion = new CatUbicacion();
        ubicacion.setNombre(ciudadela.getNombre());
        ubicacion.setEstado(Boolean.TRUE);
        ubicacion.setFechaIngreso(new Date());
        ubicacion.setObra(this.obra);
        ubicacion.setCiudadela(ciudadela);
        ubicaciones.add(ubicacion);
        ubicacionesService.create(ubicacion);
    }

    public void anadirUbiacionTodoDuran() {
        CatUbicacion ubicacion = new CatUbicacion();
        ubicacion.setParroquia(new CatParroquia(3L));
        ubicacion.setFechaIngreso(new Date());
        ubicacion.setObra(this.obra);
        ubicacion.setEstado(Boolean.TRUE);
        ubicacion.setNombre("TODO DURÁN");
        ubicaciones.add(ubicacion);
        ubicacionesService.create(ubicacion);
    }

    public void eliminarUbicacion(CatUbicacion ubicacion) {
        ubicaciones.remove(ubicacion);
        ubicacionesService.remove(ubicacion);
    }

    public void calculo() {
        //se llama al metodo del calculo CEM

    }

    public void generarReporte() {
        servletSession.borrarDatos();
        servletSession.instanciarParametros();
//        servletSession.addParametro("ANIO", anioReporte.intValue());
        servletSession.setNombreReporte("calculosAnualesPrediales");
        servletSession.setNombreSubCarpeta("GestionTributatia/mejoras");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

    public Integer getAnioActual() {
        return anioActual;
    }

    public void setAnioActual(Integer anioActual) {
        this.anioActual = anioActual;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public ServletSession getServletSession() {
        return servletSession;
    }

    public void setServletSession(ServletSession servletSession) {
        this.servletSession = servletSession;
    }

//
    public LazyModel<Obra> getObras() {
        return obras;
    }

    public void setObras(LazyModel<Obra> obras) {
        this.obras = obras;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public List<CatUbicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<CatUbicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

//</editor-fold>
}
