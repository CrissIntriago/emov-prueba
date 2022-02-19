/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CitacionesDenunciado;
import com.gestionTributaria.Entities.CitacionesDenunciante;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
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
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class CitacionesGeneralesMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(CitacionesGeneralesMB.class.getName());
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession ss;
    private int tipoUsuario;
    private CatalogoItem comisaria;
    private List<CatalogoItem> comisarias;
    private CatalogoItem item;
    private LazyModel<CitacionesDenunciante> denunciante;
    private LazyModel<CitacionesDenunciado> denunciado;
    private Date fechaDesde;
    private Date fechaHasta;
    private BigInteger denuncianteid;

    @PostConstruct
    public void init() {
        denunciante = new LazyModel<>(CitacionesDenunciante.class);
        denunciado = new LazyModel<>(CitacionesDenunciado.class);
        item = new CatalogoItem();
        tipoUsuario = 1;
        comisarias = new ArrayList<>();
        comisaria = new CatalogoItem();
        llenarListaComisaria();
        fechaDesde = new Date();
        fechaHasta = new Date();
    }

    public void generarReporte() {
        ss.setNombreReporte("CITACIONESGENERALES");
        ss.setNombreSubCarpeta("/GestionTributatia/comisaria/");
        if (comisaria == null) {
            ss.addParametro("idComisaria", null);
        } else {
            ss.addParametro("idComisaria", comisaria.getId().intValue());
        }
        ss.addParametro("tipoPersona", tipoUsuario);
        ss.addParametro("fDesde", fechaDesde);
        ss.addParametro("fHasta", fechaHasta);
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void buscarPersonaDenunciante(CitacionesDenunciante client) {
        ss.addParametro("idPersona", client.getDenunciante().intValue());
        generarReporte();
    }

    public void buscarPersonaDenunciado(CitacionesDenunciado cliente) {
        ss.addParametro("idPersona", cliente.getDenunciado().intValue());
        generarReporte();
    }

    public void filtar() {
        if (comisaria == null) {
            denunciante.getFilterss().clear();
        } else {
            denunciante.getFilterss().put("comisaria", comisaria.getId());
            denunciado.getFilterss().put("comisaria", comisaria.getId());
        }
    }

    public void llenarListaComisaria() {
        comisarias = (List<CatalogoItem>) catalogoItemService.findCatalogoItems("GT_lista_comisarias");
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public CatalogoItem getComisaria() {
        return comisaria;
    }

    public void setComisaria(CatalogoItem comisaria) {
        this.comisaria = comisaria;
    }

    public List<CatalogoItem> getComisarias() {
        return comisarias;
    }

    public void setComisarias(List<CatalogoItem> comisarias) {
        this.comisarias = comisarias;
    }

    public CatalogoItem getItem() {
        return item;
    }

    public void setItem(CatalogoItem item) {
        this.item = item;
    }

    public LazyModel<CitacionesDenunciante> getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(LazyModel<CitacionesDenunciante> denunciante) {
        this.denunciante = denunciante;
    }

    public LazyModel<CitacionesDenunciado> getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(LazyModel<CitacionesDenunciado> denunciado) {
        this.denunciado = denunciado;
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

}
