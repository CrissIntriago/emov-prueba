/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller.SinProceso;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CmMultas;
import com.gestionTributaria.Services.FinaRenDetalleLiquidacionService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class MultaComisariaMB implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private ManagerService service;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private FinaRenDetalleLiquidacionService detalleLiquidacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private UsuarioService usuarioService;
    private List<CatalogoItem> listaComisarias;
    private FinaRenLiquidacion finaRenLiquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private List<FinaRenRubrosLiquidacion> rubro;
    private List<FinaRenDetLiquidacion> detalleLista;
    private FinaRenDetLiquidacion detalle;
    private BigDecimal sumaTotal = BigDecimal.ZERO;
    private Integer tipoCons;
    private CatPredio predio;
    private String esUrbano;
    private Cliente propietario;
    private List<CatPredio> prediosPropiestarios;
    private Cliente propietarioConsulta;
    private CmMultas multas;
    private CatalogoItem comisarias;
    private LazyModel<CmMultas> lazyMultas;

    @PostConstruct
    public void init() {
        resetear();
    }

    public void resetear() {
        listaComisarias = new ArrayList<>();
        listaComisarias = catalogoService.MostarTodoCatalogo("GT_lista_comisarias");
        finaRenLiquidacion = new FinaRenLiquidacion();
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        tipoLiquidacion = finaRenLiquidacionService.getTipoLiquidacionByPrefijo("COMI");
        comisarias = new CatalogoItem();
        multas = new CmMultas();
        detalleLista = new ArrayList<>();
        rubro = new ArrayList<>();
        rubro = finaRenLiquidacionService.listaRubros(tipoLiquidacion);
        prediosPropiestarios = new ArrayList<>();
        predio = new CatPredio();
        propietario = new Cliente();
        propietarioConsulta = new Cliente();
        sumaTotal = BigDecimal.ZERO;
        lazyMultas = new LazyModel<>(CmMultas.class);
        lazyMultas.getFilterss().put("usuarioIngreso", userSession.getNameUser());

    }

    public void suma() {
        sumaTotal = BigDecimal.ZERO;
        for (FinaRenDetLiquidacion finaRenDetLiquidacion : detalleLista) {
            if (finaRenDetLiquidacion.getCobrar()) {
                sumaTotal = sumaTotal.add(finaRenDetLiquidacion.getValor());
            }
        }
        finaRenLiquidacion.setTotalPago(sumaTotal);
    }

    public void cargarRubros() {
        detalleLista = new ArrayList<>();
        for (FinaRenRubrosLiquidacion item : rubro) {

            detalle = new FinaRenDetLiquidacion();
            detalle.setCantidad(1);
            detalle.setRubro(item);
            detalle.setCobrar(Boolean.FALSE);
//            detalle.setValor(BigDecimal.ZERO);
            detalleLista.add(detalle);
            JsfUtil.update("datosLista");
        }
    }

    public void editarLista(FinaRenDetLiquidacion li, int index) {
        this.detalleLista.add(this.detalleLista.indexOf(li), li);
        this.detalleLista.remove(this.detalleLista.indexOf(li));
        suma();
        JsfUtil.update("datosLista");
    }

    public void save() {
        if (comisarias == null) {
            JsfUtil.addWarningMessage("", "Tiene que el tipo de comisaria");
            return;
        }

        if (sumaTotal.compareTo(BigDecimal.ZERO) == 0) {
            JsfUtil.addWarningMessage("", "El total a pagar no puede ser igual a cero");
            return;
        }

        finaRenLiquidacion.setAnio(Utils.getAnio(new Date()));
        finaRenLiquidacion.setAvaluoMunicipal(predio.getAvaluoMunicipal());
        finaRenLiquidacion.setPredio(predio);

        finaRenLiquidacion.setFechaIngreso(new Date());
        finaRenLiquidacion.setFechaCreacionOriginal(new Date());
        finaRenLiquidacion.setTipoLiquidacion(tipoLiquidacion);
        finaRenLiquidacion.setComprador(propietario);
        finaRenLiquidacion.setValidada(Boolean.FALSE);
        this.finaRenLiquidacion.calcularPago();
        //SE AGREGA PARA SALDO DE PAGO
        this.generarLiquidacion();
    }

    public void generarLiquidacion() {
        finaRenLiquidacion.setUsuarioIngreso(userSession.getNameUser());
        finaRenLiquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
        finaRenLiquidacion.setComprador(propietario);
        finaRenLiquidacion.setNombreComprador(propietario.getNombreCompleto());
        finaRenLiquidacion.setIdentificacion(propietario.getIdentificacionCompleta());
        finaRenLiquidacion.setRentas(Boolean.TRUE);
        finaRenLiquidacion = finaRenLiquidacionService.crearLiquidacion(finaRenLiquidacion, detalleLista);

        multas = new CmMultas();
        multas.setCatastro(predio);
        multas.setContribuyente(propietario);
        Usuarios deman = usuarioService.findByUsuario(userSession.getNameUser());
        if (deman != null && deman.getFuncionario() != null && deman.getFuncionario().getPersona() != null) {
            multas.setDemandante(deman.getFuncionario().getPersona());
        }
        multas.setDemandado(propietario);
        multas.setFechaIngreso(new Date());
        multas.setObservacion(finaRenLiquidacion.getObservacion());
        multas.setUsuarioIngreso(userSession.getNameUser());
        multas.setLiquidacion(finaRenLiquidacion);
        multas.setComisaria(comisarias);
        multas = (CmMultas) service.updateEntity(multas);
        JsfUtil.executeJS("PF('dlogoNumLiquidacion').show()");
        JsfUtil.update("fmNumLiquidacion");
        JsfUtil.addInformationMessage("Mensaje", "Liquidacion: " + finaRenLiquidacion.getIdLiquidacion() + " genrada con exito");
    }

    public void close() {
        resetear();
        JsfUtil.update("fmNumLiquidacion");
        JsfUtil.executeJS("PF('dlogoNumLiquidacion').hide()");
    }

    public void consultar() {
        try {
            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    propietario = Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte();
                }
                cargarRubros();
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
            setPropietario(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
        }
        cargarRubros();
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
                    temp = service.findByParameter(CatPredio.class,
                            paramtr);
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

    public void viewLiquidacion(CmMultas c) {
        finaRenLiquidacion = new FinaRenLiquidacion();
        finaRenLiquidacion = c.getLiquidacion();
    }

//<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public CatalogoItem getComisarias() {
        return comisarias;
    }

    public void setComisarias(CatalogoItem comisarias) {
        this.comisarias = comisarias;
    }

    public BigDecimal getSumaTotal() {
        return sumaTotal;
    }

    public void setSumaTotal(BigDecimal sumaTotal) {
        this.sumaTotal = sumaTotal;
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

    public String getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(String esUrbano) {
        this.esUrbano = esUrbano;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public List<CatPredio> getPrediosPropiestarios() {
        return prediosPropiestarios;
    }

    public void setPrediosPropiestarios(List<CatPredio> prediosPropiestarios) {
        this.prediosPropiestarios = prediosPropiestarios;
    }

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public CmMultas getMultas() {
        return multas;
    }

    public void setMultas(CmMultas multas) {
        this.multas = multas;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public FinaRenLiquidacionService getFinaRenLiquidacionService() {
        return finaRenLiquidacionService;
    }

    public void setFinaRenLiquidacionService(FinaRenLiquidacionService finaRenLiquidacionService) {
        this.finaRenLiquidacionService = finaRenLiquidacionService;
    }

    public FinaRenDetalleLiquidacionService getDetalleLiquidacionService() {
        return detalleLiquidacionService;
    }

    public void setDetalleLiquidacionService(FinaRenDetalleLiquidacionService detalleLiquidacionService) {
        this.detalleLiquidacionService = detalleLiquidacionService;
    }

    public List<CatalogoItem> getListaComisarias() {
        return listaComisarias;
    }

    public void setListaComisarias(List<CatalogoItem> listaComisarias) {
        this.listaComisarias = listaComisarias;
    }

    public FinaRenLiquidacion getFinaRenLiquidacion() {
        return finaRenLiquidacion;
    }

    public void setFinaRenLiquidacion(FinaRenLiquidacion finaRenLiquidacion) {
        this.finaRenLiquidacion = finaRenLiquidacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public List<FinaRenDetLiquidacion> getDetalleLista() {
        return detalleLista;
    }

    public void setDetalleLista(List<FinaRenDetLiquidacion> detalleLista) {
        this.detalleLista = detalleLista;
    }

    public FinaRenDetLiquidacion getDetalle() {
        return detalle;
    }

    public void setDetalle(FinaRenDetLiquidacion detalle) {
        this.detalle = detalle;
    }

    public Cliente getPropietarioConsulta() {
        return propietarioConsulta;
    }

    public void setPropietarioConsulta(Cliente propietarioConsulta) {
        this.propietarioConsulta = propietarioConsulta;
    }

    public LazyModel<CmMultas> getLazyMultas() {
        return lazyMultas;
    }

    public void setLazyMultas(LazyModel<CmMultas> lazyMultas) {
        this.lazyMultas = lazyMultas;
    }

    public List<FinaRenRubrosLiquidacion> getRubro() {
        return rubro;
    }

    public void setRubro(List<FinaRenRubrosLiquidacion> rubro) {
        this.rubro = rubro;
    }

//</editor-fold>
}
