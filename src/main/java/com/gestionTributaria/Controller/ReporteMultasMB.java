/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.Reportegeneralmultas;
import com.gestionTributaria.Services.CatPredioService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class ReporteMultasMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReporteMultasMB.class.getName());
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession ss;
    @Inject
    private CatPredioService predioService;
    @Inject
    private ClienteService clienteService;
    private int criterioBusquedad;
    private List<CatalogoItem> comisarias;
    private CatalogoItem comisaria;
    private Date fechaDesde;
    private Date fechaHasta;
    private Map<String, Object> paramt;
    private int estado;
    private CatalogoItem item;
    private int criterioPredio;
    private CatPredio predio;
    private String claveCatastral;
    private LazyModel<Reportegeneralmultas> contribuyentes;
    private Reportegeneralmultas contribuyente;
    private Cliente cliente;

    @PostConstruct

    public void init() {
        cliente = new Cliente();
        contribuyente = new Reportegeneralmultas();
        contribuyentes = new LazyModel<>(Reportegeneralmultas.class);
        claveCatastral = "";
        predio = new CatPredio();
        item = null;
        estado = 1;
        criterioBusquedad = 1;
        criterioPredio = 1;
        comisarias = new ArrayList<>();
        comisaria = new CatalogoItem();
        fechaDesde = new Date();
        fechaHasta = new Date();
        llenarListaComisaria();

    }

    public void reporteContribuyente(Reportegeneralmultas cont) {
        contribuyente = cont;
        generarReporteMulta();
    }

    public void generarReporteMulta() {
        ss.setNombreReporte("MultasMasivo");
        ss.setNombreSubCarpeta("/GestionTributatia/comisaria/");
        if (criterioBusquedad == 1) {
            ss.addParametro("criterio", criterioBusquedad);
            if (comisaria == null) {
                ss.addParametro("comisaria", null);
            } else {
                ss.addParametro("comisaria", comisaria.getId().intValue());
            }
            ss.addParametro("estadoliq", estado);
            ss.addParametro("fechaDesde", fechaDesde);
            ss.addParametro("fechaHasta", fechaHasta);
            ss.addParametro("id_predio", null);
            ss.addParametro("contribuyente", null);
        }
        if (criterioBusquedad == 2) {
            formarClave();
            if (predio == null) {
                JsfUtil.addInformationMessage("", "El predio no se encontró");
            } else {
                ss.addParametro("criterio", criterioBusquedad);
                if (comisaria == null) {
                    ss.addParametro("comisaria", null);
                } else {
                    ss.addParametro("comisaria", comisaria.getId().intValue());
                }
                ss.addParametro("estadoliq", estado);
                ss.addParametro("fechaDesde", fechaDesde);
                ss.addParametro("fechaHasta", fechaHasta);
                ss.addParametro("id_predio", predio.getId().intValue());
                ss.addParametro("contribuyente", null);

            }
        }
        if (criterioBusquedad == 3) {
            cliente = clienteService.verificarIdentificacionRuc(contribuyente.getIdentificacion());
            ss.addParametro("criterio", criterioBusquedad);
            if (comisaria == null) {
                ss.addParametro("comisaria", null);
            } else {
                ss.addParametro("comisaria", comisaria.getId().intValue());
            }
            System.out.println("Contrib" + cliente.getId().intValue());
            ss.addParametro("estadoliq", estado);
            ss.addParametro("fechaDesde", fechaDesde);
            ss.addParametro("fechaHasta", fechaHasta);
            ss.addParametro("id_predio", null);
            ss.addParametro("contribuyente", cliente.getId().intValue());
        }

        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void formarClave() {
        claveCatastral = "";
        claveCatastral = predio.getParroquia().intValue() == 3 ? "1." : "1.";
        claveCatastral = predio.getSector() == null ? claveCatastral + "0." : claveCatastral + predio.getSector() + ".";
        claveCatastral = predio.getMz() == null ? claveCatastral + "0." : claveCatastral + predio.getSector() + ".";
        claveCatastral = predio.getSolar() == null ? claveCatastral + "0." : claveCatastral + predio.getSolar() + ".";
        claveCatastral = predio.getDiv1() == null ? claveCatastral + "0." : claveCatastral + predio.getDiv1() + ".";
        claveCatastral = predio.getDiv2() == null ? claveCatastral + "0." : claveCatastral + predio.getDiv2() + ".";
        claveCatastral = predio.getDiv3() == null ? claveCatastral + "0." : claveCatastral + predio.getDiv3() + ".";
        claveCatastral = predio.getDiv4() == null ? claveCatastral + "0." : claveCatastral + predio.getDiv4() + ".";
        claveCatastral = predio.getPhv() == null ? claveCatastral + "0." : claveCatastral + predio.getPhv() + ".";
        claveCatastral = predio.getPhh() == null ? claveCatastral + "0" : claveCatastral + predio.getPhh() + ".";
        predio = (CatPredio) predioService.finByPredioClaveCatastral(claveCatastral);
    }

    public void buscarPredio(String claveCatastral) {
        predio = (CatPredio) predioService.finByPredioClaveCatastral(claveCatastral);
    }

    public void llenarListaComisaria() {
        comisarias = (List<CatalogoItem>) catalogoItemService.findCatalogoItems("GT_lista_comisarias");
    }

    public int getCriterioBusquedad() {
        return criterioBusquedad;
    }

    public void setCriterioBusquedad(int criterioBusquedad) {
        this.criterioBusquedad = criterioBusquedad;
    }

    public List<CatalogoItem> getComisarias() {
        return comisarias;
    }

    public void setComisarias(List<CatalogoItem> comisarias) {
        this.comisarias = comisarias;
    }

    public CatalogoItem getComisaria() {
        return comisaria;
    }

    public void setComisaria(CatalogoItem comisaria) {
        this.comisaria = comisaria;
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

    public CatalogoItem getItem() {
        return item;
    }

    public void setItem(CatalogoItem item) {
        this.item = item;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCriterioPredio() {
        return criterioPredio;
    }

    public void setCriterioPredio(int criterioPredio) {
        this.criterioPredio = criterioPredio;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public LazyModel<Reportegeneralmultas> getContribuyentes() {
        return contribuyentes;
    }

    public void setContribuyentes(LazyModel<Reportegeneralmultas> contribuyentes) {
        this.contribuyentes = contribuyentes;
    }

    public Reportegeneralmultas getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Reportegeneralmultas contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
