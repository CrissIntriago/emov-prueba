/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThDiasLaborados;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThDiasLaboradosService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thDiasLaboradosView")
@ViewScoped
public class ThDiasLaboradosController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThDiasLaboradosService thDiasLaboradosService;

    private OpcionBusqueda opcionBusqueda;
    private ThDiasLaborados thDiasLaborados;
    private ThTipoRol thTipoRol;

    private LazyModel<ThDiasLaborados> thDiasLaboradosLazy;

    private List<ThDiasLaborados> thDiasLaboradosDeleteList;
    private List<Short> periodos;
    private List<ThTipoRol> tipoRolList;

    private Boolean btnCargarDatos;

    private DefaultStreamedContent downloadFormato;

    @PostConstruct
    public void init() {
        clean();
    }

    public void clean() {
        btnCargarDatos = Boolean.TRUE;
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        thDiasLaborados = new ThDiasLaborados();
        updateTipoRolList();
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
            thDiasLaboradosLazy = new LazyModel<>(ThDiasLaborados.class);
            thDiasLaboradosLazy.getSorteds().put("servidor.persona.apellido", "ASC");
            thDiasLaboradosLazy.getFilterss().put("estado", true);
            thDiasLaboradosLazy.getFilterss().put("idTipoRol", thTipoRol);
            thDiasLaboradosLazy.setDistinct(false);
        } else {
            thDiasLaboradosLazy = null;
            btnCargarDatos = Boolean.FALSE;
        }
    }

    public void download() throws Exception {
        downloadFormato = thInterfaces.docDownload("TH_FORMATO_ASISTENCIA");
    }

    public void cargarFormato(FileUploadEvent event) {
        List<ThDiasLaborados> temp = thInterfaces.loadDataDiasLaborados(event, Boolean.FALSE, Boolean.TRUE, thTipoRol);
        if (temp.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se han podido cargar los datos debido a que hubo un error el tipo de dato del formato subido, o los datos ya estan cargados");
        } else {
            JsfUtil.addSuccessMessage("AVISO!!!", "Se ha cargado correctamente los datos");
            updateLazyModel();
        }
        JsfUtil.executeJS("PF('subirdocu').hide()");
        JsfUtil.executeJS("PF('loadingDlg').hide()");
        PrimeFaces.current().ajax().update("thDiasLaboradosTable");
    }

    public void edit(ThDiasLaborados diasLaborados) {
        if (diasLaborados != null) {
            Integer dias = thInterfaces.getDiasTalentoHumano();
            if (diasLaborados.getDiasLaborados() > dias) {
                JsfUtil.addWarningMessage("AVISO!!", "Los dias ingresados son mayor a " + dias + " dias");
                return;
            }
            thDiasLaboradosService.edit(diasLaborados);
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha cambiado correctamente la información");
        PrimeFaces.current().ajax().update("thDiasLaboradosTable");
    }

    public void delete(ThDiasLaborados diasLaborados) {
        diasLaborados.setUsuarioModificacion(thInterfaces.getUser());
        diasLaborados.setFechaModificacion(new Date());
        diasLaborados.setEstado(Boolean.FALSE);
        thDiasLaboradosService.edit(diasLaborados);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha eliminado correctamente la información");
        PrimeFaces.current().ajax().update("thDiasLaboradosTable");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public ThDiasLaborados getThDiasLaborados() {
        return thDiasLaborados;
    }

    public void setThDiasLaborados(ThDiasLaborados thDiasLaborados) {
        this.thDiasLaborados = thDiasLaborados;
    }

    public ThTipoRol getThTipoRol() {
        return thTipoRol;
    }

    public void setThTipoRol(ThTipoRol thTipoRol) {
        this.thTipoRol = thTipoRol;
    }

    public LazyModel<ThDiasLaborados> getThDiasLaboradosLazy() {
        return thDiasLaboradosLazy;
    }

    public void setThDiasLaboradosLazy(LazyModel<ThDiasLaborados> thDiasLaboradosLazy) {
        this.thDiasLaboradosLazy = thDiasLaboradosLazy;
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

    public DefaultStreamedContent getDownloadFormato() {
        return downloadFormato;
    }

    public void setDownloadFormato(DefaultStreamedContent downloadFormato) {
        this.downloadFormato = downloadFormato;
    }

    public Boolean getBtnCargarDatos() {
        return btnCargarDatos;
    }

    public void setBtnCargarDatos(Boolean btnCargarDatos) {
        this.btnCargarDatos = btnCargarDatos;
    }

}
