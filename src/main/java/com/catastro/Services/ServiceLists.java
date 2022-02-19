/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatTipoConjunto;
import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatParroquia;
import com.gestionTributaria.Services.CatCiudadelasService;
import com.gestionTributaria.Services.CatParroquiaService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.UserSession;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ServiceLists implements Serializable {

    @Inject
    private CatastroServices catas;
    @Inject
    private UserSession session;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CantonService cantonService;
    @Inject
    private CatParroquiaService catParroquiaService;
    @Inject
    private CatTipoConjuntoServices catTipoConjuntoServices;
    @Inject
    private CatCiudadelasService catCiudadelasService;

    private List<CatTipoConjunto> tiposConjunto;

    private List<Provincia> provincias;

    public List<Provincia> getProvincias() {
        this.provincias = provinciaService.getProvincias();
        return this.provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<CatalogoItem> getListado(String argumento) {
        if (argumento == null) {
            return null;
        }
        return catalogoItemService.findByCatalogo(argumento);
    }

    public List<Canton> getCantones(Provincia prov) {
        if (prov == null) {
            return null;
        } else {
            return cantonService.getCantones(prov);
        }
    }

    public List<CatParroquia> getParroquiasxCanton(Canton canton) {
        if (canton == null) {
            return null;
        } else {;
            return catParroquiaService.findAllParroquiaByCanton(canton);
        }
    }

    public List<CatTipoConjunto> getTiposConjunto() {
        tiposConjunto = catTipoConjuntoServices.findAll();
        return tiposConjunto;
    }

    public void setTiposConjunto(List<CatTipoConjunto> tiposConjunto) {
        this.tiposConjunto = tiposConjunto;
    }

    public List<CatCiudadela> getCiudadelas() {
        return catCiudadelasService.findAll();
    }

}
