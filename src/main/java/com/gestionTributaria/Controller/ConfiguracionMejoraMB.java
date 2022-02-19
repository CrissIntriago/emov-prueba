/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CenAvaluoMunicipal;
import com.gestionTributaria.Entities.Obra;
import com.gestionTributaria.Services.CatCiudadelasService;
import com.gestionTributaria.Services.CenAvaluoMunicipalService;
import com.gestionTributaria.Services.ObraServices;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class ConfiguracionMejoraMB implements Serializable {

    @Inject
    private ObraServices obrasService;
    @Inject
    private CenAvaluoMunicipalService cemAvaluoService;
    @Inject
    private CatCiudadelasService ciudadelasService;

    private List<Obra> obras;
    private List<CatalogoItem> configuraciones;
    private CatalogoItem configuracion;
    private List<CenAvaluoMunicipal> cemConfiguracion;
    private CenAvaluoMunicipal cemAvaluoMunicipal;
    private List<CatCiudadela> ciudadelas;
    private CatCiudadela ciudadela;
    private Obra obra;

    @PostConstruct
    public void initView() {
        obra = new Obra();
        ciudadelas = new ArrayList<>();
        ciudadela = new CatCiudadela();
        cemAvaluoMunicipal = new CenAvaluoMunicipal();
        configuracion = new CatalogoItem();
        cargarObras();
        cargarConfiguraciones();
        cargarCiudadelas();
    }

    public void cargarObras() {
        obras = obrasService.getObras();
    }

    public void cargarConfiguraciones() {
        configuraciones = obrasService.getConfiguracioness();
    }

    public void traerConfiguracionObra(Obra obra) {
        cemConfiguracion = obrasService.findConfiguracion(obra);
        this.obra = obra;
    }

    public void cargarCiudadelas() {
        ciudadelas = ciudadelasService.getAllCiudadelasTodas();
    }

    public void guardarConfiguracion() {
        if (configuracion.getCodigo().equals("cem_obra_sector_ciudadela_mz")) {
            cemAvaluoMunicipal.setCodigoCiudadela(ciudadela);
        }
        cemAvaluoMunicipal.setObra(BigInteger.valueOf(this.obra.getId()));
        cemAvaluoMunicipal.setCodigoConfiguracion(BigInteger.valueOf(configuracion.getId()));
        cemAvaluoMunicipal = cemAvaluoService.create(cemAvaluoMunicipal);
        cemConfiguracion.add(cemAvaluoMunicipal);
        cemAvaluoMunicipal = new CenAvaluoMunicipal();
    }

    public void eliminarConfiguracion(CenAvaluoMunicipal cemAvaluoMunicipal) {
        cemConfiguracion.remove(cemAvaluoMunicipal);
        cemAvaluoService.remove(cemAvaluoMunicipal);
    }
    //<editor-fold defaultstate="collapsed" desc="get and set">

    public List<Obra> getObras() {
        return obras;
    }

    public void setObras(List<Obra> obras) {
        this.obras = obras;
    }

    public List<CatalogoItem> getConfiguraciones() {
        return configuraciones;
    }

    public void setConfiguraciones(List<CatalogoItem> configuraciones) {
        this.configuraciones = configuraciones;
    }

    public CatalogoItem getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(CatalogoItem configuracion) {
        this.configuracion = configuracion;
    }

    public List<CenAvaluoMunicipal> getCemConfiguracion() {
        return cemConfiguracion;
    }

    public void setCemConfiguracion(List<CenAvaluoMunicipal> cemConfiguracion) {
        this.cemConfiguracion = cemConfiguracion;
    }

    public CenAvaluoMunicipal getCemAvaluoMunicipal() {
        return cemAvaluoMunicipal;
    }

    public void setCemAvaluoMunicipal(CenAvaluoMunicipal cemAvaluoMunicipal) {
        this.cemAvaluoMunicipal = cemAvaluoMunicipal;
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
//</editor-fold>

}
