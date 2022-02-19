/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.entities.transporte.CooperativaSocio;
import com.origami.sigef.common.entities.transporte.CooperativaVehiculo;
import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.ordenes.services.CooperativaService;
import ec.com.trafficcontrol.IntegracionPortalWS;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class CooperativasMb implements Serializable {

    private static final Logger LOG = Logger.getLogger(CooperativasMb.class.getName());

    private LazyModel<Cooperativa> cooperativaLazy;
    private Cooperativa coop;
    private Boolean ver = true;
    @Inject
    private CooperativaService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession session;
    @Inject
    private IntegracionPortalWS portalWS;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            cooperativaLazy = new LazyModel<>(Cooperativa.class);
//            cooperativaLazy.getFilterss().put("estado", "AC");
            coop = new Cooperativa();
            coop.setEmpresa(new Cliente());
            try {
                String consultaVehiculoSRI = portalWS.getMetodosPort().consultaVehiculoSRI("GTE-4416");

                System.out.println("ConsultaVehiculoSRI >> " + consultaVehiculoSRI);
            } catch (Exception e) {
                System.out.println("Test WSDl...");
            }
        }
    }

    public void nuevo(Cooperativa coop) {
        if (coop == null) {
            this.coop = new Cooperativa();
            ver = false;
        } else {
            this.coop = coop;
        }
        if (this.coop.getEmpresa() == null) {
            this.coop.setEmpresa(new Cliente());
        }
        JsfUtil.executeJS("PF('dlgOrden').show()");
        PrimeFaces.current().ajax().update("frmOrden");
    }

    public void eliminarVehiculo(CooperativaVehiculo veh) {
        if (veh != null) {
            this.coop.getCooperativaVehiculoList().remove(veh);
            // metodo para eliminar de la base
        }
    }

    public void eliminarSocio(CooperativaSocio socio) {
        if (socio != null) {
            this.coop.getCooperativaSocioList().remove(socio);
            // metodo para eliminar de la base
        }
    }

    public void guardar() {
        if (coop.getEmpresa() != null) {
            if (coop.getEmpresa().getTipoIdentificacion().getCodigo().equals("RUC")) {
                coop.getEmpresa().setIdentificacion(coop.getEmpresa().getIdentificacion().substring(0, 10));
            }
            if (coop.getEmpresa().getId() != null) {
                coop.getEmpresa().setUsuarioModificacion(session.getNameUser());
                coop.getEmpresa().setFechaModificacion(new Date());
                clienteService.edit(coop.getEmpresa());
            } else {
                coop.getEmpresa().setUsuarioCreacion(session.getNameUser());
                coop.getEmpresa().setFechaCreacion(new Date());
                clienteService.create(coop.getEmpresa());
            }
        }
        coop = service.save(coop);
        if (coop != null) {
            cerrarDialogo();
            JsfUtil.addInformationMessage("Info", "Datos Actualizados correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al actualizar los datos");
        }
    }

    public void cerrarDialogo() {
        JsfUtil.executeJS("PF('dlgOrden').hide()");
        ver = true;
    }

    public void eliminar(Cooperativa coop) {

    }

    public void openDlg() {
        Utils.openDialog("/facelet/vehiculos/dlgVehiculos", null);
    }

    public void returnDialoProcess(SelectEvent evt) {
        Vehiculo c = (Vehiculo) evt.getObject();
        if (c != null) {
            if (Utils.isEmpty(this.coop.getCooperativaVehiculoList())) {
                this.coop.setCooperativaVehiculoList(new ArrayList<>());
            }
            CooperativaVehiculo e = new CooperativaVehiculo();
            e.setCooperativa(coop);
            e.setEstado("AC");
            e.setFechaCre(new Date());
            e.setUsuarioCre(session.getNameUser());
            e.setFechaMod(new Date());
            e.setUsuarioMod(session.getNameUser());
            e.setVehiculo(c);
            Boolean existe = false;
            for (CooperativaVehiculo cv : this.coop.getCooperativaVehiculoList()) {
                if (cv.getVehiculo().equals(c)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                this.coop.getCooperativaVehiculoList().add(e);
            } else {
                JsfUtil.addErrorMessage("Vehículo", "Ya existe agregado el vehículo seleccionado");
            }
        }
        JsfUtil.update("viewCoop:dtVehiculos");
    }

    public void buscarCliente() {
        try {
            if (coop.getEmpresa().getIdentificacion() != null) {
                if (coop.getEmpresa().getIdentificacion().length() == 0) {
                    Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
                    return;
                }
                String ciRuc = coop.getEmpresa().getIdentificacion();
                Boolean tipo = (coop.getEmpresa().getTipoIdentificacion() != null ? coop.getEmpresa().getTipoIdentificacion().getCodigo().equals("C") : false);
                Cliente temp = clienteService.buscarCliente(coop.getEmpresa(), tipo);
                if (temp != null) {
                    if (temp.getTipoIdentificacion().getCodigo().equals("RUC")) {
                        temp.setIdentificacion(temp.getIdentificacion().concat(temp.getRuc()));
                    }
                    coop.setEmpresa(temp);
                }
                PrimeFaces.current().ajax().update("pngDatosCoop");
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "buscarCliente", e);
        }
    }

    public void returnDialoProcessCliente(SelectEvent evt) {
        try {
            Cliente temp = (Cliente) evt.getObject();
            coop.setEmpresa(temp);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "returnDialoProcessCliente", e);
        }
    }

    public LazyModel<Cooperativa> getCooperativaLazy() {
        return cooperativaLazy;
    }

    public void setCooperativaLazy(LazyModel<Cooperativa> cooperativaLazy) {
        this.cooperativaLazy = cooperativaLazy;
    }

    public Cooperativa getCoop() {
        return coop;
    }

    public void setCoop(Cooperativa coop) {
        this.coop = coop;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

}
