/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.RegimenLaboralService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "valoresParamView")
@ViewScoped
public class ValoresParametrizablesBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    private LazyModel<ParametrosTalentoHumano> parametrosLazy;
    private ParametrosTalentoHumano parametros;
    @Inject
    private ParametrizableService parametrizableSservice;
    @Inject
    private RegimenLaboralService regimenService;
    private List<CatalogoItem> listCatalogo;
    private List<CatalogoItem> listClasificacion;
    private List<CatalogoItem> listOrigen;
    private List<CatalogoItem> lisTipoEstado;
    @Inject
    private CatalogoItemService itemService;

    @Inject
    private UserSession userSession;
    private boolean OrigenBloquear;
    private Boolean requeridFechainicio;

    @PostConstruct
    public void inicializate() {
        requeridFechainicio = false;
        parametros = new ParametrosTalentoHumano();
        parametrosLazy = new LazyModel<>(ParametrosTalentoHumano.class);
        parametrosLazy.getFilterss().put("estado", true);
        parametrosLazy.getSorteds().put("clasificacion.texto", "DESC");
        parametrosLazy.setDistinct(false);;
        listClasificacion = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "tipo_presupuesto");
        listOrigen = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "origen_distributivo");
        lisTipoEstado = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "estado_valores");
        setOrigenBloquear(true);
    }

    public void form(ParametrosTalentoHumano parametro) {
        if (parametro != null) {
            this.parametros = parametro;
            if ("I".equals(parametros.getClasificacion().getCodigo())) {
                listCatalogo = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
                if (parametros.getTipo().getCodigo().equals("SBU")) {
                    setOrigenBloquear(true);
                } else {
                    setOrigenBloquear(false);
                }
            }
            if ("E".equals(parametros.getClasificacion().getCodigo())) {
                listCatalogo = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_egreso");
                setOrigenBloquear(true);
            }
        } else {
            parametros = new ParametrosTalentoHumano();
        }
        parametros.setFechaModificacion(new Date());
        parametros.setUsuarioModifica(userSession.getNameUser());
        parametros.setEstado(true);
        PrimeFaces.current().executeScript("PF('valorParamDialog').show()");
        PrimeFaces.current().ajax().update(":formValorParam");
    }

    public void guardarvalor() {
        boolean edit = parametros.getId() != null;
        if (parametros.getTipo() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Rubro.");
            return;
        }
        if (parametros.getClasificacion() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Clasificación del Rubro.");
            return;
        }
        if (parametros.getOrigen() == null && OrigenBloquear == false) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Origen del Rubro.");
            return;
        }
        if (parametros.getTipoEstado() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Estado del Rubro.");
            return;
        }
        if (parametros.getValor() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el valor del Rubro.");
            return;
        }
        if (parametros.getVigenciaDesde() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar la fecha de inicio.");
            return;
        }
        if (!edit) {
            ParametrosTalentoHumano pm;
            if (parametros.getTipo().getCodigo().equals("SBU")) {
                requeridFechainicio = true;
                List<ParametrosTalentoHumano> parList = parametrizableSservice.getlistaSBU("SBU");
                if (!parList.isEmpty()) {
                    JsfUtil.addWarningMessage("", "Verifique que Los SBU anteriores tengan fecha de Vigencia Finalizada");
                    return;
                } else {
                    pm = parametrizableSservice.getParametroFechaMax();
                    if (pm != null) {
                        if (pm.getVigenciaHasta().compareTo(parametros.getVigenciaDesde()) >= 0) {
                            JsfUtil.addWarningMessage("", "La fecha de Vigencia Desde no puede ser Menor/Igual a la fecha de Vigencia Hasta ya Registada Anteriormente");
                            return;
                        }
                    }
                }
            }
            parametros.setUsuarioCreacion(userSession.getNameUser());
            parametros.setFechaCreacion(new Date());
            parametros = parametrizableSservice.create(parametros);
        } else {
            parametrizableSservice.edit(parametros);
        }
        PrimeFaces.current().executeScript("PF('valorParamDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", parametros.getNombre() + (edit ? " editada" : " registrada") + " con éxito.");
    }

    public void eliminarvalor(ParametrosTalentoHumano param) {
        JsfUtil.addSuccessMessage("Información", param.getNombre() + " eliminada con éxito");
        param.setEstado(Boolean.FALSE);
        parametrizableSservice.edit(param);
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
    }

    public void updateTipo() {
        if (parametros.getClasificacion() != null) {
            if ("I".equals(parametros.getClasificacion().getCodigo())) {
                listCatalogo = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
                setOrigenBloquear(false);
            }
            if ("E".equals(parametros.getClasificacion().getCodigo())) {
                listCatalogo = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_egreso");
                setOrigenBloquear(true);
            }
        } else {
            listCatalogo = new ArrayList<>();
        }
    }

    public void validarRubros() {
        if (parametros.getTipo() != null) {
            if ("I".equals(parametros.getClasificacion().getCodigo())) {
                if (parametros.getTipo().getCodigo().equals("SBU")) {
                    setOrigenBloquear(true);
                    parametros.setOrigen(null);
                } else {
                    setOrigenBloquear(false);
                }
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get and Set">
    public LazyModel<ParametrosTalentoHumano> getParametrosLazy() {
        return parametrosLazy;
    }

    public void setParametrosLazy(LazyModel<ParametrosTalentoHumano> parametrosLazy) {
        this.parametrosLazy = parametrosLazy;
    }

    public Boolean getRequeridFechainicio() {
        return requeridFechainicio;
    }

    public void setRequeridFechainicio(Boolean requeridFechainicio) {
        this.requeridFechainicio = requeridFechainicio;
    }

    public ParametrosTalentoHumano getParametros() {
        return parametros;
    }

    public void setParametros(ParametrosTalentoHumano parametros) {
        this.parametros = parametros;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

    public List<CatalogoItem> getListClasificacion() {
        return listClasificacion;
    }

    public void setListClasificacion(List<CatalogoItem> listClasificacion) {
        this.listClasificacion = listClasificacion;
    }

    public List<CatalogoItem> getListOrigen() {
        return listOrigen;
    }

    public void setListOrigen(List<CatalogoItem> listOrigen) {
        this.listOrigen = listOrigen;
    }

    public List<CatalogoItem> getLisTipoEstado() {
        return lisTipoEstado;
    }

    public void setLisTipoEstado(List<CatalogoItem> lisTipoEstado) {
        this.lisTipoEstado = lisTipoEstado;
    }
    
    public boolean isOrigenBloquear() {
        return OrigenBloquear;
    }

    public void setOrigenBloquear(boolean OrigenBloquear) {
        this.OrigenBloquear = OrigenBloquear;
    }
    //</editor-fold>
}
