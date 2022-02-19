/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.TramiteService;
import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.entities.Tramite;
import com.origami.sigef.resource.procesos.services.TipoTramiteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.repository.ProcessDefinition;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Angel Navarro
 */
@Named
@ViewScoped
public class TramitesConfView implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TramitesConfView.class.getName());

    @Inject
    private BpmBaseEngine engine;
    private String nameProcess;
    private StreamedContent stream;
    private LazyModel<TipoTramite> tipoTramiteLazy;
    private TipoTramite tipoTramite;
    @Inject
    private TipoTramiteService tipoTramiteService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private TramiteService tramiteService;
    private List<String> rolCategoria;
    private List<CatalogoItem> categorias;
    private List<Tramite> tramites;
    
    @PostConstruct
    protected void iniView() {
        try {
            tipoTramiteLazy = new LazyModel<>(TipoTramite.class);
            this.tipoTramite = new TipoTramite();
            tramites = new ArrayList<>();
            tramites = tramiteService.findAllQuery("SELECT t FROM Tramite t WHERE estado=TRUE ORDER BY nombre ", null);
            
        } catch (Exception e) {
            Logger.getLogger(TramitesConfView.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void agregarProceso(TipoTramite tipoTramite) {
        rolCategoria = new ArrayList<>();
        categorias = catalogoItemService.findCatalogoItems("ROL-CATEGORIA");
        if (tipoTramite != null) {
            this.tipoTramite = tipoTramite;
            if (this.tipoTramite.getRolesAcceso() != null) {
                String x = this.tipoTramite.getRolesAcceso().substring(1);
                rolCategoria.addAll(Arrays.asList(Arrays.stream(x.split(":")).map(String::trim).toArray(String[]::new)));
            }
        } else {
            this.tipoTramite = new TipoTramite();
        }
        JsfUtil.update("frmProceso");
        JsfUtil.executeJS("PF('dlgProceso').show();");
    }

    public void duplicarProceso(TipoTramite tipoTramite) {
        if (tipoTramite != null) {
            this.tipoTramite = Utils.clone(tipoTramite);
            this.tipoTramite.setId(null);
            this.tipoTramite.setHistoricoTramiteRequisitoList(null);
        }
        JsfUtil.update("frmProceso");
        JsfUtil.executeJS("PF('dlgProceso').show();");
    }

    public void buscarUnidad() {
        Utils.openDialog("/facelet/talentoHumano/unidadAdministrativa/dialogUnidadAdministrativa", "550", "400");
    }

    public void selectUnidad(SelectEvent evt) {
        try {
            tipoTramite.setUnidadAdministrativa((UnidadAdministrativa) evt.getObject());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cliente Select", e);
        }
    }

    public void guardarProceso() {
//        if (tipoTramite.getUnidadAdministrativa() == null) {
//            JsfUtil.addErrorMessage("Unidad Administrativa", "Falta seleccionar campo");
//            return;
//        }
        if (tipoTramite.getTramite() == null || tipoTramite.getTramite().getId() == null ) {
            JsfUtil.addErrorMessage("Trámite", "Falta Seleccionar campo");
            return;
        }
        if (tipoTramite.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Descripción", "Falta Ingresar campo");
            return;
        }
        if (tipoTramite.getActivitykey() == null) {
            JsfUtil.addErrorMessage("Activiti Key", "Falta Ingresar campo");
            return;
        }
        if (tipoTramite.getArchivoBpmn() == null) {
            JsfUtil.addErrorMessage("Archivo", "Falta Ingresar campo");
            return;
        }
        if (Utils.isNotEmpty(rolCategoria)) {
            StringBuffer sb = new StringBuffer();
            for (String string : rolCategoria) {
                sb.append(":").append(string);
            }
            tipoTramite.setRolesAcceso(sb.toString());
        }
        tipoTramite = tipoTramiteService.save(tipoTramite);
        if (tipoTramite != null) {
            JsfUtil.addInformationMessage("Proceso", "Información guardada con éxito.");
            JsfUtil.update("formMain");
            JsfUtil.executeJS("PF('dlgProceso').hide();");
        } else {
            JsfUtil.addErrorMessage("Proceso", "Ocurrio un error al guardar la información.");
        }
    }

    public void showDlgDiagram(String id, String name) {
        try {
            stream = imagenProceso(id, name);
            JsfUtil.update("frmDiagrama");
            JsfUtil.executeJS("PF('dlgDiagrama').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private StreamedContent imagenProceso(String id, String name) {
        try {
            return new DefaultStreamedContent(engine.getProcessDiagram(id, name), "image/png");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public void startProcess(TipoTramite tipoTramite) {
        if (tipoTramite == null) {
            engine.loadSingleProcessByClassPath(nameProcess);
            JsfUtil.addInformationMessage(nameProcess, "Proceso desplegado con éxito");
        } else {
            System.out.println("TipoTramite.getArchivoBpmn()>>" + tipoTramite.getArchivoBpmn());
            engine.loadSingleProcessByClassPath(tipoTramite.getArchivoBpmn());
            JsfUtil.addInformationMessage(tipoTramite.getActivitykey(), "Proceso desplegado con éxito");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="LISTAR PROCESOS">
    public List<ProcessDefinition> getProcesosDesplegados() {
        return engine.getProcessDesployedList();
    }

    public long countStartProcess(String process) {
        return engine.getProcessDesployedLatestVersion().deploymentId(process).count();
    }

    public List<ProcessDefinition> getHistorialProcesosDesplegados() {
        return engine.getAllProcessDesployedList();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public String getNameProcess() {
        return nameProcess;
    }

    public void setNameProcess(String nameProcess) {
        this.nameProcess = nameProcess;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    public LazyModel<TipoTramite> getTipoTramiteLazy() {
        return tipoTramiteLazy;
    }

    public void setTipoTramiteLazy(LazyModel<TipoTramite> tipoTramiteLazy) {
        this.tipoTramiteLazy = tipoTramiteLazy;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<String> getRolCategoria() {
        return rolCategoria;
    }

    public void setRolCategoria(List<String> rolCategoria) {
        this.rolCategoria = rolCategoria;
    }

    public List<CatalogoItem> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CatalogoItem> categorias) {
        this.categorias = categorias;
    }

    public List<Tramite> getTramites() {
        return tramites;
    }

    public void setTramites(List<Tramite> tramites) {
        this.tramites = tramites;
    }
    
//</editor-fold>   
}
