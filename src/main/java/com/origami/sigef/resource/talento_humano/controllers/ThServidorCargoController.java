/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargoRepository;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import com.origami.sigef.resource.talento_humano.services.ThCargoService;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoRepositoryService;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import com.origami.sigef.resource.talento_humano.services.ThServidorService;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
@Named(value = "thServidorCargoView")
@ViewScoped
public class ThServidorCargoController implements Serializable {

    @Inject
    private ThServidorCargoService thServidorCargoService;
    @Inject
    private ThServidorCargoRepositoryService thServidorCargoRepositoryService;
    @Inject
    private ThServidorService thServidorService;
    @Inject
    private ThCargoService thCargoService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession servletSession;

    private LazyModel<Servidor> thServidorLazy;
    private LazyModel<ThCargo> thCargoLazy;
    private LazyModel<ThServidorCargo> thServidorCargoLazy;

    private Servidor thServidor;
    private ThServidorCargo thServidorCargo;
    private OpcionBusqueda opcionBusqueda;
    private ThCargo thCargo;

    private List<ThServidorCargo> thServidorCargoList;
    private List<ThCargoRubros> thCargoRubroList;
    private List<Short> listaPeriodo;

    private Boolean view;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        //thServidorLazy
        listaPeriodo = catalogoItemService.getPeriodo();
        this.thServidorLazy = new LazyModel<>(Servidor.class);
        this.thServidorLazy.getSorteds().put("persona.apellido", "ASC");
        this.thServidorLazy.getFilterss().put("estado", true);
        this.thServidorLazy.setDistinct(false);
    }

    public void openDlgServidor(Servidor thServidor) {
        close();
        this.thServidor = thServidor;
        thServidorCargo.setIdServidor(thServidor);
        view = true;
        JsfUtil.executeJS("PF('thServidorDlg').show()");
        PrimeFaces.current().ajax().update("thServidorForm");
    }

    private void close() {
        this.thServidor = new Servidor();
        this.thServidorCargo = new ThServidorCargo();
        thServidorCargo.setFechaAsignacion(new Date());
        thCargoRubroList = new ArrayList<>();
    }

    public void openDlgCargo() {
        thCargoLazy = new LazyModel<>(ThCargo.class);
        thCargoLazy.getSorteds().put("codigo", "ASC");
        thCargoLazy.getFilterss().put("estado", true);
        thCargoLazy.getFilterss().put("activo", true);
        thCargoLazy.getFilterss().put("asignado", false);
        JsfUtil.executeJS("PF('thCargoDlg').show()");
        PrimeFaces.current().ajax().update("thCargoForm");
    }

    public void closeDlgCargo(ThCargo thCargo) {
        thServidorCargo.setIdCargo(thCargo);
        loadCargoPeriodos();
        PrimeFaces.current().ajax().update("fieldsetCargo");
        PrimeFaces.current().ajax().update("thCargoRubroTable");
        JsfUtil.executeJS("PF('thCargoDlg').hide()");
    }

    public void loadCargoPeriodos() {
        if (opcionBusqueda.getAnio() != null) {
            thCargoRubroList = thCargoRubrosService.findByCargoXanio(thServidorCargo.getIdCargo(), opcionBusqueda.getAnio());
        } else {
            thCargoRubroList = new ArrayList<>();
        }
    }

    public void save() {
        if (thServidorCargo.getIdCargo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un cargo antes de guardar");
            return;
        }
        thServidorCargo = thServidorCargoService.create(thServidorCargo);
        saveHistorico();
        //Editar la asignacion del cargo
        thServidorCargo.getIdCargo().setAsignado(Boolean.TRUE);
        thCargoService.edit(thServidorCargo.getIdCargo());
        close();
        JsfUtil.executeJS("PF('thServidorDlg').hide()");
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("INFO!!!", " Registrado con éxito");
    }

    private void saveHistorico() {
        ThServidorCargoRepository thServidorCargoRepository = new ThServidorCargoRepository();
        thServidorCargoRepository.setIdServidor(new BigInteger(thServidorCargo.getIdServidor().getId() + ""));
        thServidorCargo.getIdCargo().setThCargoRubrosList(thCargoRubroList);
        JsonUtil jsonUtil = new JsonUtil();
        String thCargoJson = jsonUtil.toJson(thServidorCargo.getIdCargo());
        thServidorCargoRepository.setCargo(thCargoJson);
        thServidorCargoRepository.setFechaInicio(thServidorCargo.getFechaAsignacion());
        thServidorCargoRepositoryService.create(thServidorCargoRepository);
    }

    public void fs_tempo() {
        List<ThServidorCargo> listaTemp = thServidorCargoService.findAll();
        for (ThServidorCargo item : listaTemp) {
            ThServidorCargoRepository thServidorCargoRepository = new ThServidorCargoRepository();
            thServidorCargoRepository.setIdServidor(new BigInteger(item.getIdServidor().getId() + ""));
            item.getIdCargo().setThCargoRubrosList(thCargoRubrosService.findByCargoXanio(item.getIdCargo(), opcionBusqueda.getAnio()));
            JsonUtil jsonUtil = new JsonUtil();
            String thCargoJson = jsonUtil.toJson(item.getIdCargo());
            thServidorCargoRepository.setCargo(thCargoJson);
            thServidorCargoRepository.setFechaInicio(item.getFechaAsignacion());
            thServidorCargoRepositoryService.create(thServidorCargoRepository);
        }
    }

    public void editar(Servidor thServidor) {
        close();
        this.thServidor = thServidor;
        this.thServidorCargo = thServidorCargoService.findByThServidor(thServidor);
        this.thCargoRubroList = thCargoRubrosService.findByCargoXanio(thServidorCargo.getIdCargo(), opcionBusqueda.getAnio());
        this.thCargo = thServidorCargo.getIdCargo();
        view = false;
        JsfUtil.executeJS("PF('thServidorDlg').show()");
        PrimeFaces.current().ajax().update("thServidorForm");
    }

    public void edit() {
        //Editamos la relacion
        thServidorCargoService.edit(thServidorCargo);
        //Editamos el historico
        ThServidorCargoRepository thServidorCargoRepository = thServidorCargoRepositoryService.findServidor(thServidor);
        thServidorCargo.getIdCargo().setThCargoRubrosList(thCargoRubroList);
        JsonUtil jsonUtil = new JsonUtil();
        String thCargoJson = jsonUtil.toJson(thServidorCargo.getIdCargo());
        thServidorCargoRepository.setCargo(thCargoJson);
        thServidorCargoRepository.setFechaInicio(thServidorCargo.getFechaAsignacion());
        thServidorCargoRepositoryService.edit(thServidorCargoRepository);
        //Editamos el antiguo cargo para que vuelva a estar disponible
        this.thCargo.setAsignado(Boolean.FALSE);
        thCargoService.edit(this.thCargo);
        //Editamos el nuevo cargo para que no este disponible
        thServidorCargo.getIdCargo().setAsignado(Boolean.TRUE);
        thCargoService.edit(thServidorCargo.getIdCargo());
        close();
        JsfUtil.executeJS("PF('thServidorDlg').hide()");
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("INFO!!!", " Editado con éxito");
    }

    public void quitar(Servidor thServidor) {
        close();
        thServidorCargo = thServidorCargoService.findByThServidor(thServidor);
        JsfUtil.executeJS("PF('thServidorQuitarDlg').show()");
        PrimeFaces.current().ajax().update("thServidorQuitarForm");
    }

    public void quitarCargo() {
        if (thServidorCargo.getFechaFinalizacion() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar la fecha de finalización");
            return;
        }
        //Editar la relacion servidor - cargo
        thServidorCargo.setActivo(Boolean.FALSE);
        thServidorCargoService.edit(thServidorCargo);
        //Editar el estado del cargo
        thServidorCargo.getIdCargo().setAsignado(Boolean.FALSE);
        thCargoService.edit(thServidorCargo.getIdCargo());
        //Editar la asignacion historica del cargo - servidor
        ThServidorCargoRepository thServidorCargoRepository = thServidorCargoRepositoryService.findServidor(thServidorCargo.getIdServidor());
        thServidorCargoRepository.setFechaFin(thServidorCargo.getFechaFinalizacion());
        thServidorCargoRepositoryService.edit(thServidorCargoRepository);
        JsfUtil.executeJS("PF('thServidorQuitarDlg').hide()");
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("INFO!!!", " Quitado con éxito");
    }

    public void printDocument(String tipo, Boolean accion, Servidor thServidor) {
        List<ThCargo> historicoSend = new ArrayList<>();
        servletSession.addParametro("id_servidor", thServidor.getId());
        servletSession.setContentType(tipo);
        if (accion) {
            List<ThServidorCargoRepository> historico = thServidorCargoRepositoryService.findServidorCargo(thServidor.getId());
            if (historico.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!!", "No existe ningun historico");
                return;
            }
            for (ThServidorCargoRepository item : historico) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                ThCargo cargo = gson.fromJson(item.getCargo(), ThCargo.class);
                //filed adicionales
                cargo.setFechaAsignacion(item.getFechaInicio());
                cargo.setFechaFinalizacion(item.getFechaFin());
                historicoSend.add(cargo);
            }
            servletSession.setNombreReporte("servidor_cargo");
            servletSession.setDataSource(historicoSend);
            servletSession.addParametro("thServidor", thServidor);
            servletSession.setNombreReporte("servidor_cargos_historico");
        } else {
            servletSession.setNombreReporte("servidor_cargo");
        }
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public LazyModel<Servidor> getThServidorLazy() {
        return thServidorLazy;
    }

    public void setThServidorLazy(LazyModel<Servidor> thServidorLazy) {
        this.thServidorLazy = thServidorLazy;
    }

    public LazyModel<ThCargo> getThCargoLazy() {
        return thCargoLazy;
    }

    public void setThCargoLazy(LazyModel<ThCargo> thCargoLazy) {
        this.thCargoLazy = thCargoLazy;
    }

    public LazyModel<ThServidorCargo> getThServidorCargoLazy() {
        return thServidorCargoLazy;
    }

    public void setThServidorCargoLazy(LazyModel<ThServidorCargo> thServidorCargoLazy) {
        this.thServidorCargoLazy = thServidorCargoLazy;
    }

    public Servidor getThServidor() {
        return thServidor;
    }

    public void setThServidor(Servidor thServidor) {
        this.thServidor = thServidor;
    }

    public List<ThServidorCargo> getThServidorCargoList() {
        return thServidorCargoList;
    }

    public void setThServidorCargoList(List<ThServidorCargo> thServidorCargoList) {
        this.thServidorCargoList = thServidorCargoList;
    }

    public ThServidorCargo getThServidorCargo() {
        return thServidorCargo;
    }

    public void setThServidorCargo(ThServidorCargo thServidorCargo) {
        this.thServidorCargo = thServidorCargo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<ThCargoRubros> getThCargoRubroList() {
        return thCargoRubroList;
    }

    public void setThCargoRubroList(List<ThCargoRubros> thCargoRubroList) {
        this.thCargoRubroList = thCargoRubroList;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
