package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThPrestamoIess;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThConfigService;
import com.origami.sigef.resource.talento_humano.services.ThPrestamoIessService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Criss Intriago
 */
@Named(value = "thPrestamoQuirografarioView")
@ViewScoped
public class ThPrestamoQuirografarioController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThPrestamoIessService thPrestamoIessService;
    @Inject
    private ThConfigService thConfigService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRol;
    private ThConfig thConfig;

    private LazyModel<ThPrestamoIess> thPrestamoQuirografarioLazy;

    private List<Short> periodos;
    private List<ThTipoRol> tipoRolList;
    private List<CatalogoItem> listCatalogo;

    private Boolean btnCargarDatos;

    private DefaultStreamedContent downloadFormato;

    @PostConstruct
    public void init() {
        clean();
    }

    public void clean() {
        btnCargarDatos = true;
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        updateTipoRolList();
        listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_egreso");
    }

    public void updateTipoRolList() {
        if (opcionBusqueda.getAnio() != null) {
            tipoRolList = thInterfaces.tipoRol(opcionBusqueda.getAnio());
        } else {
            tipoRolList = new ArrayList<>();
        }
    }

    public void updateLazyModel() {
        if (thTipoRol != null) {
            btnCargarDatos = !thTipoRol.getAprobado();
            thPrestamoQuirografarioLazy = new LazyModel<>(ThPrestamoIess.class);
            thPrestamoQuirografarioLazy.getSorteds().put("servidor.persona.apellido", "ASC");
            thPrestamoQuirografarioLazy.getFilterss().put("estado", true);
            thPrestamoQuirografarioLazy.getFilterss().put("pq", true);
            thPrestamoQuirografarioLazy.getFilterss().put("idTipoRol", thTipoRol);
            thPrestamoQuirografarioLazy.setDistinct(false);
        } else {
            thPrestamoQuirografarioLazy = null;
            btnCargarDatos = true;
        }
    }

    public void download() throws Exception {
        downloadFormato = thInterfaces.docDownload("TH_FORMATO_PRESTAMO");
    }

    public void cargarFormato(FileUploadEvent event) {
        String msj = findThConfig(true);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            JsfUtil.executeJS("PF('loadingDlg').hide()");
            return;
        }
        List<ThPrestamoIess> temp = thInterfaces.loadData(event, Boolean.FALSE, Boolean.TRUE, thTipoRol, thConfig.getCodConfig());
        if (temp.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay datos cargados en el formato");
        } else {
            for (ThPrestamoIess item : temp) {
                thPrestamoIessService.create(item);
            }
            Integer respuesta = thPrestamoIessService.groupData(thTipoRol.getId(), Boolean.FALSE, Boolean.TRUE);
            updateLazyModel();
        }
        JsfUtil.executeJS("PF('subirdocu').hide()");
        JsfUtil.executeJS("PF('loadingDlg').hide()");
        PrimeFaces.current().ajax().update("prestamoTable");
    }

    public void edit(ThPrestamoIess prestamo) {
        if (prestamo != null) {
            thPrestamoIessService.edit(prestamo);
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha cambiado correctamente la información");
        PrimeFaces.current().ajax().update("prestamoTable");
    }

    public void delete(ThPrestamoIess prestamo) {
        prestamo.setEstado(Boolean.FALSE);
        thPrestamoIessService.edit(prestamo);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha eliminado correctamente la información");
        PrimeFaces.current().ajax().update("prestamoTable");
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thConfigService.findCode(CONFIG.CONFIG_PQ);
        if (thConfig == null) {
            mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
        } else {
            if (accion) {
                if (thConfig.getCodConfig() == null) {
                    mensaje = "No existe la configuración para cargar los rubros en el rol seleccionado ";
                }
            }
        }
        return mensaje;
    }

    public void openDlgConfig() {
        String msj = findThConfig(false);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        JsfUtil.executeJS("PF('thConfigDlg').show()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public void save() {
        thConfigService.edit(thConfig);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado la configuración");
        JsfUtil.executeJS("PF('thConfigDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public ThTipoRol getThTipoRol() {
        return thTipoRol;
    }

    public void setThTipoRol(ThTipoRol thTipoRol) {
        this.thTipoRol = thTipoRol;
    }

    public LazyModel<ThPrestamoIess> getThPrestamoQuirografarioLazy() {
        return thPrestamoQuirografarioLazy;
    }

    public void setThPrestamoQuirografarioLazy(LazyModel<ThPrestamoIess> thPrestamoQuirografarioLazy) {
        this.thPrestamoQuirografarioLazy = thPrestamoQuirografarioLazy;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public List<ThTipoRol> getTipoRolList() {
        return tipoRolList;
    }

    public void setTipoRolList(List<ThTipoRol> tipoRolList) {
        this.tipoRolList = tipoRolList;
    }

    public Boolean getBtnCargarDatos() {
        return btnCargarDatos;
    }

    public void setBtnCargarDatos(Boolean btnCargarDatos) {
        this.btnCargarDatos = btnCargarDatos;
    }

    public DefaultStreamedContent getDownloadFormato() {
        return downloadFormato;
    }

    public void setDownloadFormato(DefaultStreamedContent downloadFormato) {
        this.downloadFormato = downloadFormato;
    }

    public ThConfig getThConfig() {
        return thConfig;
    }

    public void setThConfig(ThConfig thConfig) {
        this.thConfig = thConfig;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

}
