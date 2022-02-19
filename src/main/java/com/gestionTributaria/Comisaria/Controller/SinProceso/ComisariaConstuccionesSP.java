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
import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ComisariaConstuccionesSP implements Serializable {

    /*AGREGADO*/
    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ComisariaServices comisariaServices;

    protected List<String> user = new ArrayList<>();
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
    private List<CatalogoItem> tipos;
    private ComisariaRegistros comisaria;
    private LazyModel<ComisariaRegistros> lazy;
    private CatPredioModel predioModel;

    @PostConstruct
    public void initView() {
        try {
            predio = new CatPredio();
            tipos = new ArrayList<>();
            tipos = catalogoService.MostarTodoCatalogo("GT_tipo_construccion_comisaria");
            comisaria = new ComisariaRegistros();
            predioModel = new CatPredioModel();
            consultaComisaria();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);

        }
    }

    public void consultaComisaria() {
        lazy = new LazyModel<>(ComisariaRegistros.class);
        lazy.getFilterss().put("origen", "CONST");
        JsfUtil.addInformationMessage("", "Actualizado");
    }

    public void onTabChange(TabChangeEvent event) {
        if (event.getTab().getId().equals("consulta")) {
            consultaComisaria();
        } else {
            resetear();
        }
        JsfUtil.update(event.getTab().getId());
    }

    public void consultar() {
        System.out.println("predios " + esUrbano + "\t\t" + tipoCons + "\t\t" + predio.getNumPredio() + " \t\t" + predio.getClaveCat());
        try {
            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    setPropietario(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
                }
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            JsfUtil.update("mainForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CatPredio consultar(Integer tipoCons, CatPredio pred) {
        CatPredio temp = new CatPredio();
        CatPredio predio = pred;
        switch (tipoCons) {
            case 1: // Codigo Anterior

                break;
            case 2: // Codigo Nuevo
                System.out.println("estructura predial");
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
            JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            return null;
        }
    }

    public void validarClaveOtroCanton() {
        predio = new CatPredio();
//        if (cobrosGenerales.getTipoLiquidacion() == null) {
//            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar el tipo de liquidación a realizar");
//            return;
//        }
        try {
            if (predioModel.getProvincia() > 0 && predioModel.getCanton() > 0
                    && predioModel.getParroquiaShort() > 0 && predioModel.getZona() > 0 && predioModel.getSector() > 0
                    && predioModel.getMz() > 0 && predioModel.getLote() > 0) {

            } else {
                JsfUtil.addWarningMessage("Advertencia", "Verifique los datos ingresados");
            }

            JsfUtil.update("frmAlcPlus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            if (predio.getId() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Tiene que seleccionar el predio");
                return;
            }
            if (comisaria.getObservacion().isEmpty() || comisaria.getObservacion() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Tiene que llenar el campo observación");
                return;
            }

            comisaria.setEnte(propietario);
            comisaria.setOrigen("CONST");
            comisaria.setPredio(predio.getId());
            if (comisaria != null && comisaria.getId() == null) {
                comisaria = comisariaServices.create(comisaria);
            } else {
                comisariaServices.edit(comisaria);
            }
            JsfUtil.addInformationMessage("", "Transacción exitosa");
            resetear();
        } catch (Exception e) {
            JsfUtil.addWarningMessage("", "La Transacción fue rechazada");
        }
    }

    public void resetear() {
        comisaria = new ComisariaRegistros();
        tipos = new ArrayList<>();
        tipos = catalogoService.MostarTodoCatalogo("GT_tipo_construccion_comisaria");
        predio = new CatPredio();
        propietario = new Cliente();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
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

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public String getObservacionAnteriro() {
        return observacionAnteriro;
    }

    public void setObservacionAnteriro(String observacionAnteriro) {
        this.observacionAnteriro = observacionAnteriro;
    }

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

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
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

    public List<CatalogoItem> getTipos() {
        return tipos;
    }

    public void setTipos(List<CatalogoItem> tipos) {
        this.tipos = tipos;
    }

    public ComisariaRegistros getComisaria() {
        return comisaria;
    }

    public void setComisaria(ComisariaRegistros comisaria) {
        this.comisaria = comisaria;
    }

    public LazyModel<ComisariaRegistros> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ComisariaRegistros> lazy) {
        this.lazy = lazy;
    }

    public CatPredioModel getPredioModel() {
        return predioModel;
    }

    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }

//</editor-fold>
}
