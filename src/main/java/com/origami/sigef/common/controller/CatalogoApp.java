/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Pais;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.PaisService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ApplicationScoped
public class CatalogoApp implements Serializable {

    @Inject
    private CatalogoItemService items;
    @Inject
    private PaisService paisService;
    @Inject
    private CantonService cantonService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private UserSession session;

    public String comparar(Object nue, Object sys) {
        try {
            if (Utils.compararObjectos(nue, sys)) {
                return "blanco";
            } else {
                return "diferente";
            }
        } catch (Exception e) {
            Logger.getLogger(CatalogoApp.class.getName()).log(Level.SEVERE, "Completar con ceros", e);
        }
        return "blanco";
    }

    public List<CatalogoItem> itemFromCatalogo(String name) {
        return items.findCatalogoItems(name);
    }

    public List<CatalogoItem> itemFromCatalogo(String name, CatalogoItem itemPadre) {
        if (itemPadre == null) {
            return itemFromCatalogo(name);
        } else {
            return items.findCatalogoItems(name, itemPadre);
        }
    }

    public List<Canton> getCantones() {
        return cantonService.findAll();
    }

    public List<Canton> getCantones(Provincia pv) {
        if (pv == null) {
            return cantonService.findAll();
        } else {
            Canton c = new Canton();
            c.setIdProvincia(pv);
            List<Canton> findByExample = cantonService.findByExample(c);
            return findByExample;
        }
    }

    public List<Provincia> getProvincias() {
        return provinciaService.findAll();
    }

    public List<Pais> getPaises() {
        return paisService.findAll();
    }

    public List<UnidadAdministrativa> getAllUnidadesAdministrativas() {
        return unidadService.findAll();
    }

    public StreamedContent getMedia() {
        if (session.getVarTemp() != null) {
            UploadedFile f = (UploadedFile) session.getVarTemp();
            try {
                DefaultStreamedContent media = new DefaultStreamedContent(f.getInputstream(), f.getContentType(), f.getFileName(), Integer.valueOf(f.getSize() + ""));
                session.setVarTemp(null);
                return media;
            } catch (IOException ex) {
                Logger.getLogger(CatalogoApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
