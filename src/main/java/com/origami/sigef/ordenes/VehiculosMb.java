/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.ordenes.services.VehiculoService;
import ec.com.trafficcontrol.ResultWsdl;
import ec.com.trafficcontrol.TrafficControlService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class VehiculosMb implements Serializable {

    private LazyModel<Vehiculo> vehiculoLazy;
    private Vehiculo vehiculo;
    private Boolean ver = true;
    @Inject
    private VehiculoService service;
    @Inject
    private TrafficControlService tcService;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            vehiculoLazy = new LazyModel<>(Vehiculo.class);
            vehiculo = new Vehiculo();
        }
    }

    public void nuevo(Vehiculo veh) {
        if (veh == null) {
            this.vehiculo = new Vehiculo();
            ver = false;
        } else {
            this.vehiculo = veh;
        }
        if (this.vehiculo.getPropietario() == null) {
            this.vehiculo.setPropietario(new Cliente());
        }
        JsfUtil.executeJS("PF('dlgOrden').show()");
    }

    public void guardar() {
        vehiculo = service.save(vehiculo);
        if (vehiculo != null) {
            JsfUtil.executeJS("PF('dlgOrden').hide()");
        }
    }

    public void cerrarDialogo() {
        JsfUtil.executeJS("PF('dlgOrden').hide()");
        ver = true;
    }

    public void eliminar(Vehiculo veh) {

    }

    public void buscar() {
        try {
            String placa = vehiculo.getPlacaActual();
            if (placa != null) {
                ResultWsdl.ResultData result = tcService.findVehiculo(placa);
                if ("0".equals(result.getCodigoerror())) {
                    Vehiculo c = tcService.processVehiculo(result);
                    if (c != null) {
                        this.vehiculo = c;
                        if (this.vehiculo.getPropietario() == null) {
                            this.vehiculo.setPropietario(new Cliente());
                        }
                        JsfUtil.addInformationMessage("Información", "Busqueda realizada con exito");
                        JsfUtil.update("frmOrden");
                    } else {
                        JsfUtil.addErrorMessage("Vehículo", "No se encontro información");
                    }
                    if (this.vehiculo.getPropietario() == null) {
                        this.vehiculo.setPropietario(new Cliente());
                    }
                } else {
                    JsfUtil.addErrorMessage("Vehículo", result.getMensajeerror());
                }
            } else {
                JsfUtil.addInformationMessage("Información", "Debe ingresa la placa o el número de chasis a buscar");
            }
        } catch (Exception e) {
            System.out.println("Test WSDl...");
        }
    }

    public LazyModel<Vehiculo> getVehiculoLazy() {
        return vehiculoLazy;
    }

    public void setVehiculoLazy(LazyModel<Vehiculo> vehiculoLazy) {
        this.vehiculoLazy = vehiculoLazy;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

}
