/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThGrupoServidor;
import com.origami.sigef.resource.talento_humano.entities.ThGrupoServidorDetalle;
import com.origami.sigef.resource.talento_humano.services.ThGrupoServidorDetalleService;
import com.origami.sigef.resource.talento_humano.services.ThGrupoServidorService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thGrupoServidorDetalleView")
@ViewScoped
public class ThGrupoServidorDetalleController implements Serializable {

    @Inject
    private ThGrupoServidorService thGrupoServidorService;
    @Inject
    private ThGrupoServidorDetalleService thGrupoServidorDetalleService;

    private LazyModel<ThGrupoServidorDetalle> thGrupoServidorDetalleLazy;

    private ThGrupoServidor thGrupoServidor;
    private List<ThGrupoServidor> thGrupoServidorList;

    private List<Servidor> servidorList, servidoresSeleccionados;

    @PostConstruct
    public void init() {
        thGrupoServidor = new ThGrupoServidor();
        thGrupoServidorList = thGrupoServidorService.findByNamedQuery("ThGrupoServidor.findByEstado", true);
        servidoresSeleccionados = new ArrayList<>();
    }

    public void updateLazy() {
        if (thGrupoServidor != null && thGrupoServidor.getId() != null) {
            thGrupoServidorDetalleLazy = new LazyModel<>(ThGrupoServidorDetalle.class);
            thGrupoServidorDetalleLazy.getSorteds().put("idServidor.persona.apellido", "ASC");
            thGrupoServidorDetalleLazy.getFilterss().put("estado", true);
            thGrupoServidorDetalleLazy.getFilterss().put("idGrupo", true);
            thGrupoServidorDetalleLazy.setDistinct(false);
        } else {
            thGrupoServidorDetalleLazy = null;
        }
        PrimeFaces.current().ajax().update("thGrupoServidorDetalleTable");
    }

    public void openDlgServidores() {
        if (thGrupoServidor == null || thGrupoServidor.getId() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleecionar un grupo");
            return;
        }
        servidorList = thGrupoServidorDetalleService.getServidores();
        servidoresSeleccionados = new ArrayList<>();
        JsfUtil.executeJS("PF('thServidorDlg').show()");
        PrimeFaces.current().ajax().update("thServidoresTable");
    }

    public void close() {
        if (!servidoresSeleccionados.isEmpty()) {
            actualizarGrupo();
            JsfUtil.executeJS("PF('thServidorDlg').hide()");
            updateLazy();
            servidoresSeleccionados = new ArrayList<>();
            JsfUtil.addInformationMessage("INFO!!!", "Se han cargado correctamente los datos");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar minimo un servidor");
        }
    }

    public void delete(ThGrupoServidorDetalle thGrupoServidorDetalle) {
        thGrupoServidorDetalle.setEstado(Boolean.FALSE);
        thGrupoServidorDetalleService.edit(thGrupoServidorDetalle);
        actualizarGrupo();
        JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public void actualizarGrupo() {
        if (thGrupoServidor != null) {
            if (thGrupoServidor.getId() != null) {
                thGrupoServidor.setCantidad(thGrupoServidorDetalleService.getCantidad(thGrupoServidor));
                thGrupoServidorService.edit(thGrupoServidor);
            }
        }
    }

    public LazyModel<ThGrupoServidorDetalle> getThGrupoServidorDetalleLazy() {
        return thGrupoServidorDetalleLazy;
    }

    public void setThGrupoServidorDetalleLazy(LazyModel<ThGrupoServidorDetalle> thGrupoServidorDetalleLazy) {
        this.thGrupoServidorDetalleLazy = thGrupoServidorDetalleLazy;
    }

    public ThGrupoServidor getThGrupoServidor() {
        return thGrupoServidor;
    }

    public void setThGrupoServidor(ThGrupoServidor thGrupoServidor) {
        this.thGrupoServidor = thGrupoServidor;
    }

    public List<ThGrupoServidor> getThGrupoServidorList() {
        return thGrupoServidorList;
    }

    public void setThGrupoServidorList(List<ThGrupoServidor> thGrupoServidorList) {
        this.thGrupoServidorList = thGrupoServidorList;
    }

    public List<Servidor> getServidorList() {
        return servidorList;
    }

    public void setServidorList(List<Servidor> servidorList) {
        this.servidorList = servidorList;
    }

    public List<Servidor> getServidoresSeleccionados() {
        return servidoresSeleccionados;
    }

    public void setServidoresSeleccionados(List<Servidor> servidoresSeleccionados) {
        this.servidoresSeleccionados = servidoresSeleccionados;
    }

}
