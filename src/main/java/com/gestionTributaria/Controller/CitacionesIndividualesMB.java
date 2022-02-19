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
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.presupuesto.procesos.services.CatalogoItemServices;
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
public class CitacionesIndividualesMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(CitacionesIndividualesMB.class.getName());
    @Inject
    private CatalogoItemServices catalogoItemService2;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession userSession;
    private int tipoUsuario;
    private CatalogoItem comisaria;
    private List<CatalogoItem> comisarias;
    private CatalogoItem item;
    private LazyModel<CitacionesDenunciante> denunciante;
    private LazyModel<CitacionesDenunciado> denunciado;
    private Date fechaDesde;
    private Date fechaHasta;
    private BigInteger denuncianteid;
    private List<UsuarioRol> usuarioRoles;
    private Boolean bandera;
    private CatalogoItem comisario;

    @PostConstruct
    public void init() {
        bandera = false;
        item = new CatalogoItem();
        tipoUsuario = 1;
        comisarias = new ArrayList<>();
        comisaria = new CatalogoItem();

        llenarListaComisaria();
        fechaDesde = new Date();
        fechaHasta = new Date();
        usuarioRoles = userSession.getUserRoles();
        for (UsuarioRol x : usuarioRoles) {
            CatalogoItem catalogo = x.getRol().getCategoria();
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("38")) {
                comisario = catalogoItemService2.traerCatalogo("CONSTRUCCION");
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
        }
        denunciante = new LazyModel<>(CitacionesDenunciante.class);
        denunciado = new LazyModel<>(CitacionesDenunciado.class);
        if (bandera == true) {
            denunciante.getFilterss().put("usuarioCreacion", userSession.getNameUser());
            denunciante.getFilterss().put("comisariaTexto", comisario.getCodigo());
            denunciado.getFilterss().put("usuarioCreacion", userSession.getNameUser());
            denunciado.getFilterss().put("comisariaTexto", comisario.getCodigo());
            System.out.println("USUARIO: " + userSession.getNameUser());
            System.out.println("codigo : " + comisario.getCodigo());
        } else {
        }
    }

    public void generarReporte() {
        ss.setNombreReporte("CITACIONESINDIVIDUALES");
        ss.setNombreSubCarpeta("/GestionTributatia/comisaria/");
        ss.addParametro("idComisaria", comisario.getId().intValue());
        ss.addParametro("tipoPersona", tipoUsuario);
        ss.addParametro("fDesde", fechaDesde);
        ss.addParametro("fHasta", fechaHasta);
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void reporteDenunciado(CitacionesDenunciado denunciado) {
        ss.addParametro("idPersona", denunciado.getDenunciado().intValue());
        generarReporte();
    }

    public void reporteDenunciante(CitacionesDenunciante denunciante) {
        ss.addParametro("idPersona", denunciante.getDenunciante().intValue());
        generarReporte();
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

    public BigInteger getDenuncianteid() {
        return denuncianteid;
    }

    public void setDenuncianteid(BigInteger denuncianteid) {
        this.denuncianteid = denuncianteid;
    }

}
