/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller.SinProceso;

import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
public class SolaresVaciosSP implements Serializable {

    /*AGREGADO*/
    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ComisariaServices comisariaServices;

    private Integer tipoCons;
    private CatPredio predio;
    private String ciRucCobros;
    private String observacionAnteriro;
    private String esUrbano;
    private Cliente contribuyenteConsulta;
    private String tabName;
    private String nombreContribuyente;
    private String identificacion;
    private Long tipoBusqueda = 1L;
    private Cliente propietario;
    private ComisariaRegistros comisaria;
    private LazyModel<ComisariaRegistros> lazy;
    private List<CatPredio> prediosPropiestarios;
    private Cliente propietarioConsulta;

    @PostConstruct
    public void init() {
        try {
            resetear();
            consultarComisaria();
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    public void consultarComisaria() {
        lazy = new LazyModel<>(ComisariaRegistros.class);
        lazy.getFilterss().put("origen", "SOlVA");
        JsfUtil.addInformationMessage("", "Actualizado");
    }

    public void consultar() {
        try {
            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    comisaria.setEnte(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
                    propietario = Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte();
                }
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            JsfUtil.update("mainForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarPredio(CatPredio p) {
        predio = p;
        if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
            comisaria.setEnte(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
        }
    }

    public CatPredio consultar(Integer tipoCons, CatPredio pred) {
        CatPredio temp = new CatPredio();
        CatPredio predio = pred;
        switch (tipoCons) {
            case 1: // Codigo Anterior
                System.out.println("propietarioConsulta.getIdentificacion() " + propietarioConsulta.getIdentificacion());
                propietario = new Cliente();
                propietario = clienteService.buscarCliente(propietarioConsulta.getIdentificacion());

                if (propietario != null) {
                    prediosPropiestarios = service.preidosPropietarios(propietario, "A");

                    if (prediosPropiestarios != null && !prediosPropiestarios.isEmpty()) {
                        if (prediosPropiestarios.size() == 1) {
                            temp = prediosPropiestarios.get(0);
                        } else {
                            JsfUtil.executeJS("PF('dlogoPpredioPropiestario').show()");
                            JsfUtil.update("frmPrediosPropiestarios");
                            return null;
                        }

                    }
                }
                break;
            case 2: // Codigo Nuevo                
                if (predio.getSector() > 0 || predio.getMz() > 0 || predio.getProvincia() > 0 || predio.getCanton() > 0
                        || predio.getParroquia() > 0 || predio.getZona() > 0 || predio.getSolar() > 0 || predio.getPiso() >= 0
                        || predio.getUnidad() >= 0 || predio.getBloque() >= 0) {
                    Map<String, Object> paramtr = new HashMap<>();
                    paramtr.put("estado", "A");
                    paramtr.put("sector", predio.getSector());
                    paramtr.put("mz", predio.getMz());
                    paramtr.put("provincia", predio.getProvincia());
                    paramtr.put("canton", predio.getCanton());
                    paramtr.put("parroquia", predio.getParroquia());
                    paramtr.put("zona", predio.getZona());
                    paramtr.put("solar", predio.getSolar());
                    paramtr.put("piso", predio.getPiso());
                    paramtr.put("unidad", predio.getUnidad());
                    paramtr.put("bloque", predio.getBloque());
                    if (esUrbano == "1") {
                        paramtr.put("tipoPredio", "U");
                    } else {
                        paramtr.put("tipoPredio", "R");
                    }
                    temp = service.findByParameter(CatPredio.class, paramtr);
                }
                break;

            case 3:// Numero de Predio

                if (predio.getNumPredio() != null) {
                    System.out.println("num predio");
                    temp = service.getPredioNumPredio(predio.getNumPredio().longValue(), esUrbano);
                }

                break;
            case 4:
                if (predio.getClaveCat() != null) {
                    System.out.println("clave catastral");
                    temp = service.getPredioByClaveCat(predio.getClaveCat(), esUrbano);
                }

                break;
            case 5:// Clave anterior
                if (predio.getPredialant() != null) {
                    temp = service.getPredioByClaveCatAnt(predio.getPredialant(), esUrbano);
                }

                break;

        }
        if (temp != null) {
            JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.predioEncontrado + temp.getClaveCat());
            return temp;
        } else {
            return null;
        }
    }

    public void save() {
        try {
            if (predio.getId() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Tiene que seleccionar el predio");
                return;
            }
            comisaria.setOrigen("SOlVA");
            comisaria.setPredio(predio.getId());
            comisaria.setEnte(propietario);
            comisaria.setNumSolar(comisariaServices.numSolar());
            if (comisaria != null && comisaria.getId() == null) {
                comisaria = comisariaServices.create(comisaria);
            } else {
                comisariaServices.edit(comisaria);
            }
            JsfUtil.addInformationMessage("", "Transacción exitosa");
            JsfUtil.executeJS("PF('dlogoNumSolar').show()");
            JsfUtil.update("fmNumSolicitud");           
        } catch (Exception e) {
            JsfUtil.addWarningMessage("", "La Transacción fue rechazada");
        }
    }

    public void close() {
        resetear();
        consultarComisaria();
    }

    public void resetear() {

        comisaria = new ComisariaRegistros();
        propietarioConsulta = new Cliente();
        predio = new CatPredio();
        propietario = new Cliente();

    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public ComisariaServices getComisariaServices() {
        return comisariaServices;
    }

    public void setComisariaServices(ComisariaServices comisariaServices) {
        this.comisariaServices = comisariaServices;
    }

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getCiRucCobros() {
        return ciRucCobros;
    }

    public void setCiRucCobros(String ciRucCobros) {
        this.ciRucCobros = ciRucCobros;
    }

    public String getObservacionAnteriro() {
        return observacionAnteriro;
    }

    public void setObservacionAnteriro(String observacionAnteriro) {
        this.observacionAnteriro = observacionAnteriro;
    }

    public String getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(String esUrbano) {
        this.esUrbano = esUrbano;
    }

    public Cliente getContribuyenteConsulta() {
        return contribuyenteConsulta;
    }

    public void setContribuyenteConsulta(Cliente contribuyenteConsulta) {
        this.contribuyenteConsulta = contribuyenteConsulta;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getNombreContribuyente() {
        return nombreContribuyente;
    }

    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Long getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Long tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public ComisariaRegistros getComisaria() {
        return comisaria;
    }

    public void setComisaria(ComisariaRegistros comisaria) {
        this.comisaria = comisaria;
    }

    public List<CatPredio> getPrediosPropiestarios() {
        return prediosPropiestarios;
    }

    public void setPrediosPropiestarios(List<CatPredio> prediosPropiestarios) {
        this.prediosPropiestarios = prediosPropiestarios;
    }

    public Cliente getPropietarioConsulta() {
        return propietarioConsulta;
    }

    public void setPropietarioConsulta(Cliente propietarioConsulta) {
        this.propietarioConsulta = propietarioConsulta;
    }

    public LazyModel<ComisariaRegistros> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ComisariaRegistros> lazy) {
        this.lazy = lazy;
    }
//</editor-fold>

}
