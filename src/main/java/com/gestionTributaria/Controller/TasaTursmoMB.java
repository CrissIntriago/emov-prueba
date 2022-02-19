/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenActividadComercial;
import com.asgard.Entity.FinaRenLocalCategoria;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Entities.RenTasaTurismo;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.TipoTurismo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
@Named
@ViewScoped
public class TasaTursmoMB implements Serializable {

    private LazyModel<RenTasaTurismo> lazy;
    private RenTasaTurismo tasa;
    private Boolean esNuevo;
    private Long idCategoria;
    @Inject
    protected ManagerService manager;
    @Inject
    protected CatalogoService catalogoService;
    private Map<String, Object> param;
    private List<CatalogoItem> listaTasaTipoActividad;

    @PostConstruct
    protected void initView() {
        tasa = new RenTasaTurismo();
        lazy = new LazyModel<>(RenTasaTurismo.class);
        lazy.getSorteds().put("descripcion", "ASC");
        lazy.setDistinct(false);
        listaTasaTipoActividad = catalogoService.MostarTodoCatalogo("GT_TIPO_TASA_TURISITICA_ACT");

    }

    public List<CatalogoItem> getTipos() {
        return catalogoService.MostarTodoCatalogo("GT_TIPO_TASA_TURISMO");

    }

    public List<FinaRenLocalCategoria> getCategoriasHijas() {

        if (idCategoria != null) {
            param = new HashMap<>();
            param.put("padre", idCategoria);
            return manager.findAllQuery("SELECT rc FROM FinaRenLocalCategoria rc WHERE rc.padre = :padre", param);

        }
        return null;
    }

    public String getTipoDescripcion(Integer tipo) {
        switch (tipo) {
            case 1:
                return "POR HABITACION";
            case 2:
                return "POR PLAZA";
            case 3:
                return "POR MESAS";
            default:
                return "VALOR FIJO";
        }
    }

    public void nuevo() {
        tasa = new RenTasaTurismo();
        esNuevo = true;
        showHideDlg(esNuevo);
    }

    public void editar(RenTasaTurismo t) {
        tasa = t;
        idCategoria = tasa.getCategoria().getPadre();
        this.getCategoriasHijas();
        esNuevo = false;
        showHideDlg(true);
    }

    private void showHideDlg(Boolean p) {
        if (p) {
            JsfUtil.executeJS("PF('dlgTasa').show()");
            JsfUtil.update("frmtt");
        } else {
            JsfUtil.executeJS("PF('dlgTasa').hide()");
            JsfUtil.update("frmTasaTur");
        }
    }

    public List<FinaRenActividadComercial> getActividades() {
        return manager.findAllEasy("select f from FinaRenActividadComercial f order by f.descripcion asc");
    }

    public void guardar() {
        if (tasa.getCategoria() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar la Categoria");
            return;
        }
        if (tasa.getTipoItem() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el tipo de valor");
            return;
        }
        if (tasa.getValor() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el Valor");
            return;
        }
        FinaRenLocalCategoria cat = manager.find(FinaRenLocalCategoria.class, idCategoria);
        tasa.setDescripcion(cat.getDescripcion() + ": " + tasa.getCategoria().getDescripcion());
        if (esNuevo) {
            tasa.setEstado(true);
            tasa.setFechaIngreso(new Date());
        }
        //  tasa = manager.guardarTasaTurismo(tasa);
        if (tasa.getId() == null) {
            tasa = (RenTasaTurismo) manager.save(tasa);
        } else {
            manager.update(tasa);
        }
        if (tasa != null) {
            JsfUtil.addInformationMessage("Información", "Registro guardado correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al intentar guardar");
        }
        showHideDlg(false);
    }

    public List<FinaRenRubrosLiquidacion> getRubrosTurismo() {
        param = new HashMap<>();
        param.put("tipoLiq", Arrays.asList(72L));
        List<FinaRenRubrosLiquidacion> rubrosList = manager.findAllQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.tipoLiquidacion.id in (:tipoLiq) AND r.estado = true AND r.codigoRubro IS NOT NULL ORDER BY r.codigoRubro ASC",
                param);
        return rubrosList;
    }

    public List<FinaRenLocalCategoria> getCategorias() {
        return manager.findAllEasy("SELECT rc FROM FinaRenLocalCategoria rc WHERE rc.padre=0");
    }

    public LazyModel<RenTasaTurismo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RenTasaTurismo> lazy) {
        this.lazy = lazy;
    }

    public RenTasaTurismo getTasa() {
        return tasa;
    }

    public void setTasa(RenTasaTurismo tasa) {
        this.tasa = tasa;
    }

    public Boolean getEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(Boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public List<CatalogoItem> getListaTasaTipoActividad() {
        return listaTasaTipoActividad;
    }

    public void setListaTasaTipoActividad(List<CatalogoItem> listaTasaTipoActividad) {
        this.listaTasaTipoActividad = listaTasaTipoActividad;
    }

}
