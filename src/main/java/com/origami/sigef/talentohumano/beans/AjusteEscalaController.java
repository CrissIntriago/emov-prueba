/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.EscalaSalarial;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.talentohumano.services.CargoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.EscalaSalarialService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "ajusteEscalaSalaView")
@ViewScoped
public class AjusteEscalaController implements Serializable {

    @Inject
    private EscalaSalarialService escalaService;
    @Inject
    private DistributivoEscalaService disEscalaService;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private ServletSession ss;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CargoService cargoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private UserSession userSession;

    private List<UnidadAdministrativa> listUnidad;
    private List<Cargo> listCargo;
    private LazyModel<Distributivo> lazy;
    private List<MasterCatalogo> listaPeriodo;
    private Distributivo distributivo;
    private Distributivo distributivoSeleccionado;
    private DistributivoEscala disEscala;
    private EscalaSalarial escalaSalarial;
    private OpcionBusqueda busqueda;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        distributivo = new Distributivo();
        distributivoSeleccionado = new Distributivo();
        disEscala = new DistributivoEscala();
        listaPeriodo = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listCargo = cargoService.findByNamedQuery("Cargo.findByEstado");
        listUnidad = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");
        this.lazy = new LazyModel<>(Distributivo.class);
        this.lazy.getFilterss().put("estado", true);
        this.lazy.getFilterss().put("servidorPublico:notEqual", null);
        this.lazy.setDistinct(false);
    }

    public void buscar() {
        this.lazy = new LazyModel<>(Distributivo.class);
        this.lazy.getFilterss().put("estado", true);
        this.lazy.getFilterss().put("periodo", busqueda.getAnio());
        this.lazy.getFilterss().put("servidorPublico:notEqual", null);
        this.lazy.setDistinct(false);
    }

    public void editar(Distributivo dis) {
        this.distributivo = dis;
        disEscala = disEscalaService.getEscalaDistributivoAnio(dis, busqueda);
        PrimeFaces.current().executeScript("PF('distributivoDialogo').show()");
        PrimeFaces.current().ajax().update(":panelDistributivo");
    }

    public void openDlgEscala() {
        Utils.openDialog("/facelet/talentoHumano/dialogEscala", "850", "400");
    }

    public void selectDataEscala(SelectEvent evt) {
        if (disEscala.getId() != null) {
            EscalaSalarial esc = (EscalaSalarial) evt.getObject();
            if (esc != null) {
                if (esc.getRemuneracionDolares().intValue() > disEscala.getRemuneracionDolares().intValue()) {
                    JsfUtil.addWarningMessage("Advertencia", "La asignación de una nueva Escala salarial no puede ser mayor al monto ya Registrado");
                    return;
                }
            }
        }
        disEscala.setEscalaSalarial((EscalaSalarial) evt.getObject());
        disEscala.setRemuneracionDolares(disEscala.getEscalaSalarial().getRemuneracionDolares());
    }

    public void guardar() {
        distributivo.setFechaModificacion(new Date());
        distributivo.setUsuarioModifica(userSession.getNameUser());
        distributivoService.edit(distributivo);
        disEscala.setDistributivo(distributivo);
        disEscalaService.edit(disEscala);
        PrimeFaces.current().executeScript("PF('distributivoDialogo').hide()");
        PrimeFaces.current().ajax().update(":panelDistributivo");
        newObject();
    }

    public void newObject() {
        this.disEscala = new DistributivoEscala();
        this.distributivo = new Distributivo();
    }

    public LazyModel<Distributivo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<Distributivo> lazy) {
        this.lazy = lazy;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public Distributivo getDistributivoSeleccionado() {
        return distributivoSeleccionado;
    }

    public void setDistributivoSeleccionado(Distributivo distributivoSeleccionado) {
        this.distributivoSeleccionado = distributivoSeleccionado;
    }

    public List<UnidadAdministrativa> getListUnidad() {
        return listUnidad;
    }

    public void setListUnidad(List<UnidadAdministrativa> listUnidad) {
        this.listUnidad = listUnidad;
    }

    public List<Cargo> getListCargo() {
        return listCargo;
    }

    public void setListCargo(List<Cargo> listCargo) {
        this.listCargo = listCargo;
    }

    public DistributivoEscala getDisEscala() {
        return disEscala;
    }

    public void setDisEscala(DistributivoEscala disEscala) {
        this.disEscala = disEscala;
    }

}
