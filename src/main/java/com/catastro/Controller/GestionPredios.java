/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Controller;

import com.catastro.Models.EstadosPredio;
import com.catastro.Services.CatPredioServices;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class GestionPredios implements Serializable {

    //servicio user sesion
    @Inject
    private UserSession sess;
    @Inject
    private ServletSession ss;
    @Inject
    private AppConfig appconfig;
    @Inject
    private CatPredioServices catPredioServices;
    //lazy para predios
    private LazyModel<CatPredio> predios;
    //objeto predio
    private CatPredio predio = new CatPredio();
    //tipo predio
    private String tipoPredio = null;
    //el tipo de consulta
    private Integer optFiltro;
    private Long numPredios = 0L, numPrediosActivos = 0L, numPrediosInactivos = 0L, numPrediosHistorico = 0L, numPrediosPrivados = 0L, numPrediosPublicos = 0L, conteoPrediosUrbanos = 0L, conteoPrediosRurales = 0L;
    private BigDecimal avaluosTerrenos, avaluosConstruccion, avaluosPropiedad;
    private Boolean filtro, act = false;
    private CatPredioPropietario propietario;
    private Cliente contribuyenteConsulta, responsable;
    private LazyModel<Cliente> clientes;

    @PostConstruct
    public void initView() {
        try {
            predio = new CatPredio();
            clientes = new LazyModel<>(Cliente.class);
            clientes.getFilterss().put("estado", true);
            optFiltro = 4;
            iniciarListaPredios();
            getTotalesPredios();

        } catch (Exception ex) {
            Logger.getLogger(GestionPredios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filtrar() {
        filtro = optFiltro == 1;
        predios.addFilter("tipoPredio", tipoPredio);
    }

    private void iniciarListaPredios() {
        predios = new LazyModel<>(CatPredio.class);
        predios.getSorteds().put("canton", "ASC");
        predios.getSorteds().put("parroquia", "ASC");
        predios.getSorteds().put("zona", "ASC");
        predios.getSorteds().put("sector", "ASC");
        predios.getSorteds().put("mz", "ASC");
        predios.getSorteds().put("solar", "ASC");
        predios.getSorteds().put("bloque", "ASC");
        predios.getSorteds().put("piso", "ASC");
        predios.setDistinct(false);
        predios.getSorteds().put("unidad", "ASC");
        predios.getFilterss().put("zona:gte", 0);
    }

    public void updateValor(SelectEvent e) {
        predio = (CatPredio) e.getObject();
    }

    public void ver(boolean edt) {
        if (predio != null) {
            System.out.println("predio: " + predio);
//            ss.addParametro("numPredio", predio.getNumPredio());
            ss.addParametro("idPredio", predio.getId());
            ss.addParametro("edit", edt);
            if (edt) {
                if (!predioActivo() && !this.sess.getEsSuperUser()) {
                    return;
                }
            }
            JsfUtil.redirectNewTab("/origamigt/catastro/fichaPredial.xhtml");
        }
    }

    public void limpiarVariablesConsulta() {
        predio = new CatPredio();
    }

    public void getTotalesPredios() {
        numPredios = catPredioServices.CountPredios();
        numPrediosActivos = catPredioServices.PrediosActivos();
        numPrediosInactivos = catPredioServices.PrediosInactivos();
        numPrediosHistorico = catPredioServices.numPrediosHistorico();
        conteoPrediosUrbanos = catPredioServices.TotalesPrediosUrbanosPorTipoPredio("'U'");
        conteoPrediosRurales = catPredioServices.TotalesPrediosUrbanosPorTipoPredio("'R'");
        numPrediosPrivados = catPredioServices.TotalesPrediosPrivados("'PRIVADO%'");
        numPrediosPublicos = catPredioServices.TotalesPrediosPublicos("'PUBLICO%'");
        avaluosTerrenos = catPredioServices.TotalesAvaluosTerrenos();
        avaluosConstruccion = catPredioServices.TotalesAvaluosConstruccion();
        avaluosPropiedad = catPredioServices.TotalesAvaluosPropiedad();
    }

    public void loadResponsablesPredio() {
        try {
            Utils.openDialog("/resources/dialog/Ciudadanos", "", "");
        } catch (Exception e) {
            System.out.println("Error: loasResposable Predio" + e);
        }
    }

    public void selectedResponsableCiudadano(SelectEvent event) {
        responsable = (Cliente) event.getObject();
        if (responsable != null) {
            predios.getFilterss().put("catPredioPropietarioCollection.ente", responsable);
        } else {
            JsfUtil.addErrorMessage("Error", "Ciudadano no seleccionado.");
        }
    }

    public boolean predioActivo() {
        if (!predio.getEstado().equalsIgnoreCase(EstadosPredio.ACTIVO)) {
            JsfUtil.addWarningMessage("Advertencia", "El predio no se encuentra Activo.");
            return false;
        }
        if (appconfig.isLocked(sess.getIpClient() + ":" + sess.getNameUser(), predio.getId())) {
            JsfUtil.addWarningMessage("Advertencia", "El predio está siendo editado por otro usuario");
            return false;
        }
        appconfig.lockPredio(sess.getIpClient() + ":" + sess.getNameUser(), predio.getId());
        return true;
    }

    public String nombresPropietarios(CatPredio pt) {
        if (pt == null) {
            return null;
        }
        String nombres = "";
        StringBuilder sb = new StringBuilder();
        int num = 0;
        if (pt.getCatPredioPropietarioCollection() != null && !pt.getCatPredioPropietarioCollection().isEmpty()) {
            for (CatPredioPropietario cpp : pt.getCatPredioPropietarioCollection()) {
                num++;
                if (cpp.getNombresCompletos() != null) {
                    nombres = cpp.getNombresCompletos();
                    sb.append(nombres).append(" - ");
                } else {
                    sb.append(nombres).append(" - ");
                }
            }
        }
        if (sb.length() >= 3) {
            sb.delete(sb.length() - 3, sb.length() - 1);
        }
        return sb.toString().toUpperCase();
    }

    public Boolean seleccionarContribuyente() {
        if (propietario != null) {
            if (propietario.getEstado().equalsIgnoreCase("A")) {
                setContribuyenteConsulta(propietario.getEnte());
                predio.setNumPredio(propietario.getPredio().getNumPredio());
                return true;
            } else {
                JsfUtil.addWarningMessage("Advertencia!", "El propietario referenciado se encuentra inactivo");
                return false;
            }
        }
        return false;
    }

    public void consultar() {
        try {
            switch (optFiltro) {
                case 1:
                    if (predio.getNumPredio() != null) {
                        predios.addFilter("numPredio", predio.getNumPredio());
                    } else {
                        JsfUtil.addWarningMessage("Advertenia!", "La matricula inmobiliaria del predio es obligatoria");
                    }
                    break;
                case 2:
                    if (seleccionarContribuyente()) {
                    } else {
                        JsfUtil.addWarningMessage("Advertenia!", "El contribuyente seleccionado no cuenta con predios registrados, o se encuentra inactivo");
                    }
                    break;
                case 3:
                    if (predio.getPredialant() != null) {
                        predios.addFilter("predialantant", predio.getPredialant());
                    } else {
                        JsfUtil.addWarningMessage("Advertenia!", "La Clave anterior del predio es obligatoria");
                    }
                    break;
                case 4:
                    if (predio.getClaveCat() != null) {
                        predios.addFilter("claveCat", predio.getClaveCat());
                    } else {
                        JsfUtil.addWarningMessage("Advertenia!", "La Clave Actual del predio es obligatoria");
                    }
                    break;
                case 5:
                    if (predio.getPredialantAnt() != null) {
                        predios.addFilter("predialant", predio.getPredialantAnt());
                    } else {
                        JsfUtil.addWarningMessage("Advertenia!", "La matricula inmobiliaria del predio es obligatoria");
                    }
                    break;
                case 6:
                    predios.addFilter("propiedadHorizontal", true);
            }
            predios.addSorted("canton", "ASC");
            predios.addSorted("parroquia", "ASC");
            predios.addSorted("zona", "ASC");
            predios.addSorted("sector", "ASC");
            predios.addSorted("mz", "ASC");
            predios.addSorted("solar", "ASC");
            predios.addSorted("bloque", "ASC");
            predios.addSorted("piso", "ASC");
            predios.addSorted("unidad", "ASC");
        } catch (Exception e) {
            Logger.getLogger(GestionPredios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void onRowSelect(SelectEvent event) {

        JsfUtil.closeDialog((Cliente) event.getObject());
    }

    //<editor-fold defaultstate="collapsed" desc="get an set">
    public LazyModel<CatPredio> getPredios() {
        return predios;
    }

    public void setPredios(LazyModel<CatPredio> predios) {
        this.predios = predios;
    }

    public Long getNumPrediosActivos() {
        return numPrediosActivos;
    }

    public void setNumPrediosActivos(Long numPrediosActivos) {
        this.numPrediosActivos = numPrediosActivos;
    }

    public Long getNumPrediosInactivos() {
        return numPrediosInactivos;
    }

    public void setNumPrediosInactivos(Long numPrediosInactivos) {
        this.numPrediosInactivos = numPrediosInactivos;
    }

    public AppConfig getAppconfig() {
        return appconfig;
    }

    public void setAppconfig(AppConfig appconfig) {
        this.appconfig = appconfig;
    }

    public Long getNumPredios() {
        return numPredios;
    }

    public void setNumPredios(Long numPredios) {
        this.numPredios = numPredios;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public Integer getOptFiltro() {
        return optFiltro;
    }

    public void setOptFiltro(Integer optFiltro) {
        this.optFiltro = optFiltro;
    }

    public Long getNumPrediosHistorico() {
        return numPrediosHistorico;
    }

    public void setNumPrediosHistorico(Long numPrediosHistorico) {
        this.numPrediosHistorico = numPrediosHistorico;
    }

    public Long getNumPrediosPrivados() {
        return numPrediosPrivados;
    }

    public void setNumPrediosPrivados(Long numPrediosPrivados) {
        this.numPrediosPrivados = numPrediosPrivados;
    }

    public Long getNumPrediosPublicos() {
        return numPrediosPublicos;
    }

    public void setNumPrediosPublicos(Long numPrediosPublicos) {
        this.numPrediosPublicos = numPrediosPublicos;
    }

    public Long getConteoPrediosUrbanos() {
        return conteoPrediosUrbanos;
    }

    public void setConteoPrediosUrbanos(Long conteoPrediosUrbanos) {
        this.conteoPrediosUrbanos = conteoPrediosUrbanos;
    }

    public Long getConteoPrediosRurales() {
        return conteoPrediosRurales;
    }

    public void setConteoPrediosRurales(Long conteoPrediosRurales) {
        this.conteoPrediosRurales = conteoPrediosRurales;
    }

    public void setAvaluosTerrenos(BigDecimal avaluosTerrenos) {
        this.avaluosTerrenos = avaluosTerrenos;
    }

    public BigDecimal getAvaluosConstruccion() {
        return avaluosConstruccion;
    }

    public void setAvaluosConstruccion(BigDecimal avaluosConstruccion) {
        this.avaluosConstruccion = avaluosConstruccion;
    }

    public BigDecimal getAvaluosPropiedad() {
        return avaluosPropiedad;
    }

    public void setAvaluosPropiedad(BigDecimal avaluosPropiedad) {
        this.avaluosPropiedad = avaluosPropiedad;
    }

    public BigDecimal getAvaluosTerrenos() {
        return avaluosTerrenos;
    }

    public Boolean getFiltro() {
        return filtro;
    }

    public void setFiltro(Boolean filtro) {
        this.filtro = filtro;
    }

    public Boolean getAct() {
        return act;
    }

    public void setAct(Boolean act) {
        this.act = act;
    }

    public CatPredioPropietario getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredioPropietario propietario) {
        this.propietario = propietario;
    }

    public Cliente getContribuyenteConsulta() {
        return contribuyenteConsulta;
    }

    public void setContribuyenteConsulta(Cliente contribuyenteConsulta) {
        this.contribuyenteConsulta = contribuyenteConsulta;
    }

    public Cliente getResponsable() {
        return responsable;
    }

    public void setResponsable(Cliente responsable) {
        this.responsable = responsable;
    }

    public LazyModel<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(LazyModel<Cliente> clientes) {
        this.clientes = clientes;
    }

}
//</editor-fold>

