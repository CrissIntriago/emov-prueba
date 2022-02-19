/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.entities.transporte.CooperativaSocio;
import com.origami.sigef.common.entities.transporte.CooperativaVehiculo;
import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.ordenes.services.CooperativaService;
import ec.com.trafficcontrol.ResultWsdl;
import ec.com.trafficcontrol.TrafficControlService;
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
public class PrimeraFaseCensoMb implements Serializable {

    private static final Logger LOG = Logger.getLogger(PrimeraFaseCensoMb.class.getName());

    private Cooperativa coop;
    private Boolean ver = true;
    @Inject
    private CooperativaService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession session;
    @Inject
    private TrafficControlService tcService;
    private Provincia provincia;
    private Cliente representanteLegal;
    private String placa;
    private String chasis;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            coop = new Cooperativa();
            coop.setEmpresa(new Cliente());
            nuevo(null);
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
        representanteLegal = new Cliente();
    }

    public void eliminarVehiculo(CooperativaVehiculo veh) {
        if (veh != null) {
            this.coop.getCooperativaVehiculoList().remove(veh);
        }
    }

    public void eliminarSocio(CooperativaSocio socio) {
        if (socio != null) {
            this.coop.getCooperativaSocioList().remove(socio);
        }
    }

    public void buscarVehiculo() {
        try {
            if (placa != null) {
                ResultWsdl.ResultData result = tcService.findVehiculo(placa);
                if ("0".equals(result.getCodigoerror())) {
                    Vehiculo c = tcService.processVehiculo(result);
                    if (Utils.isEmpty(this.coop.getCooperativaVehiculoList())) {
                        this.coop.setCooperativaVehiculoList(new ArrayList<>());
                    }
                    CooperativaVehiculo e = new CooperativaVehiculo();
                    e.setCooperativa(coop);
                    e.setEstado(CONFIG.ESTADO_ACTIVO_REG);
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

                    JsfUtil.update("viewCoop:dtVehiculos");
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

    public void guardar() {
        if (representanteLegal == null) {
            JsfUtil.addErrorMessage("Representante Legal", "Dede buscar el representente legal");
            return;
        }
        if (representanteLegal.getIdentificacion() == null) {
            JsfUtil.addErrorMessage("Representante Legal", "Dede buscar el representente legal");
            return;
        }
        if (coop.getEmpresa() != null) {
            coop.getEmpresa().changeRuc();
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
        if (representanteLegal != null) {
            representanteLegal.changeRuc();
            if (representanteLegal.getId() != null) {
                representanteLegal.setUsuarioModificacion(session.getNameUser());
                representanteLegal.setFechaModificacion(new Date());
                clienteService.edit(representanteLegal);
            } else {
                representanteLegal.setUsuarioCreacion(session.getNameUser());
                representanteLegal.setFechaCreacion(new Date());
                clienteService.create(representanteLegal);
            }
            coop.setRepresentanteLegal(representanteLegal);
        }
        if (coop.getTipo() == null) {
            coop.setTipo(coop.getTipoOperador().getDescripcion());
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
            e.setEstado(CONFIG.ESTADO_ACTIVO_REG);
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

    public void buscar() {
        try {
            Boolean tipoCliente = false;
            Cliente cliente = coop.getEmpresa();
            String ciRuc = cliente.getIdentificacion();
            if (verificarCooperativa(ciRuc)) {
                JsfUtil.addInformationMessage("Cooperativa", "ya existe agregado una cooperativa con ese número de identifiación.");
                return;
            }
            if (cliente.getTipoIdentificacion() == null) {
                JsfUtil.addErrorMessage("Tipo Identificación", "Debe seleccionar el tipo de identificación");
                return;
            } else {
                if (ciRuc == null) {
                    JsfUtil.addErrorMessage("Identificación", "Debe ingresar el número identificación");
                    return;
                } else {
                    tipoCliente = !cliente.getTipoIdentificacion().getCodigo().equalsIgnoreCase("C");
                    if (tipoCliente) {
                        if (ciRuc.length() < 13) {
                            JsfUtil.addErrorMessage("Identificación", "La cantidad de dígitos del RUC/RISE esta incompleto");
                            return;
                        }
                    } else {
                        if (ciRuc.length() < 10) {
                            JsfUtil.addErrorMessage("Identificación", "La cantidad de dígitos del No. Identificación esta incompleto");
                            return;
                        }
                    }
                }
            }
            Cliente temp = clienteService.buscarCliente(cliente, tipoCliente);
            if (temp != null) {
                if (temp.getId() != null) {
                    if (temp.getIdentificacion() != null && temp.getRuc() != null) {
                        temp.setIdentificacion(temp.getIdentificacion().concat(temp.getRuc()));
                    }
                    if (temp.getCanton() != null) {
                        provincia = temp.getCanton().getIdProvincia();
                    }
                    coop.setEmpresa(temp);
                }
            }
            PrimeFaces.current().ajax().update("pngDatosCoop");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "buscar", e);
        }
    }

    private Boolean verificarCooperativa(String ciRuc) {
        try {
            Cooperativa c = service.findByIdentificacion(ciRuc);
            if (c != null) {
                coop = c;
                representanteLegal = c.getRepresentanteLegal();
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, ciRuc, e);
            return false;
        }
    }

    public void buscarCliente() {
        try {
            if (representanteLegal.getIdentificacion() != null) {
                if (representanteLegal.getIdentificacion().length() == 0) {
                    Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
                    return;
                }
                Cliente temp = clienteService.buscarCliente(representanteLegal, false);
                if (temp != null) {
                    representanteLegal = temp;
                } else {
                    session.setVarTemp(1);
                    Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
                    return;
                }
                PrimeFaces.current().ajax().update("pngRepresLegal");
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

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Cliente getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Cliente representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

}
