/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Services.*;
import com.gestionTributaria.Entities.*;
import com.asgard.Entity.*;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.models.CertAdeudarDTO;
import com.gestionTributaria.models.CertificadoAdeudarDeta;
import com.gestionTributaria.models.ReportGenealModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
public class CertificadoNoAdeudarMB extends ReportGenealModel implements Serializable {

    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servlet;

    private List<FinaRenLiquidacion> listaLiquidaciones;
    private FinaRenLiquidacion liquidacion;
    private Cliente propieCliente;
    private LazyModel<FinaRenLiquidacion> lazy;
    private String esUrbano = "1";
    private Cliente propietario;
    private Long tipoBusqueda = 1L;
    private Integer tipoCons;
    private CatPredio predio;
    private List<CatPredio> prediosPropiestarios;
    private Cliente propietarioConsulta;
    private Map<String, Object> param;
    private LazyModel<FinaRenLocalComercial> lazyLcoal;

    @PostConstruct
    public void init() {
        listaLiquidaciones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        predio = new CatPredio();
        prediosPropiestarios = new ArrayList<>();
        propietarioConsulta = new Cliente();
        propietario = new Cliente();
    }

    public void consultar() {
        try {
            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    propietario = predio.getCatPredioPropietarioList().get(0).getEnte();
                }

            }

            switch (tipoCons) {

                case 1:

                    System.out.println("propietario " + propietario);
                    listaLiquidaciones = new ArrayList<>();
                    listaLiquidaciones = liquidacionService.busquedaPropietarioNoAdeuda(propietario, Arrays.asList("por_pagar"));
                    break;
                case 2:
                    break;
                case 3:
                    if (temp != null) {
                        listaLiquidaciones = new ArrayList<>();
                        listaLiquidaciones = liquidacionService.busquedaPredioNoAdeuda(predio, Arrays.asList("por_pagar"));
                    }
                    break;
                case 4:
                    if (temp != null) {
                        listaLiquidaciones = new ArrayList<>();
                        listaLiquidaciones = liquidacionService.busquedaPredioNoAdeuda(predio, Arrays.asList("por_pagar"));
                    }
                    break;
                case 5:
                    break;
                case 6:
                    lazyLcoal = new LazyModel<>(FinaRenLocalComercial.class);
                    JsfUtil.executeJS("PF('localComaericales').show()");
                    JsfUtil.update("fmLocales");
                    return;
            }

            if (listaLiquidaciones != null && !listaLiquidaciones.isEmpty()) {
                JsfUtil.addInformationMessage("", "Se encontraron dedudas");
            } else {
                JsfUtil.addWarningMessage("", "No se encontro ningú dato");
            }

            JsfUtil.update("mainForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportar(String tipo) {
        if (listaLiquidaciones != null && !listaLiquidaciones.isEmpty()) {

            List<CertificadoAdeudarDeta> result = new ArrayList<>();
            for (FinaRenLiquidacion item : listaLiquidaciones) {
                CertificadoAdeudarDeta res = new CertificadoAdeudarDeta();
                res.setIdLiquidacion(item.getIdLiquidacion());
                res.setAnio(item.getAnio());
                if (item.getPredio() != null) {
                    res.setClavePredial(item.getPredio().getClaveCat());
                }
                res.setFechaIngreso(item.getFechaIngreso());
                if (item.getComprador() != null) {
                    res.setIdentificacion(item.getComprador().getIdentificacionCompleta());
                    res.setSolicitante(item.getComprador().getNombreCompleto());
                }

                if (item.getLocalComercial() != null) {
                    res.setNumLocal(item.getLocalComercial().getNumLocal());
                }

                res.setValorEmitido(item.getTotalPago());
                if (item.getTipoLiquidacion() != null) {
                    res.setNombreTitulo(item.getTipoLiquidacion().getNombreTransaccion());
                }
                res.setUserLiquidador(item.getUsuarioIngreso());
                result.add(res);
            }

            CertAdeudarDTO data = new CertAdeudarDTO("", result);
            List<CertAdeudarDTO> list = new ArrayList<>();
            list.add(data);
            servlet.setDataSource(list);
            servlet.setContentType(tipo);
            servlet.addParametro("user_impresion", userSession.getNameUser());
            servlet.setNombreReporte("reporteCertificadoAdeudar");
            servlet.setNombreSubCarpeta("GestionTributatia/Transacciones");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

        }
    }

    public String getDetalleCategoria(FinaRenLocalComercial l) {
        String detalle = "";

        if (l.getCategoria() != null) {
            FinaRenLocalCategoria cat = service.find(FinaRenLocalCategoria.class,
                    l.getCategoria().getId());
            FinaRenLocalCategoria catPadre = service.find(FinaRenLocalCategoria.class,
                    cat.getPadre().longValue());
            detalle = catPadre.getDescripcion() + ": " + cat.getDescripcion();
            if (detalle == null) {
                detalle = "";
            }
        }
        return detalle;
    }

    public void calculosAdicionales(FinaRenLiquidacion fina) {
        liquidacion = new FinaRenLiquidacion();
        liquidacion = fina;
        if (this.liquidacion != null) {
            if (this.liquidacion.getTipoLiquidacion().getId() == 13L) {
                try {
                    this.liquidacion = service.realizarDescuentoRecargaInteresPredial(this.liquidacion, null);
                } catch (Exception ex) {
                    //Logger.get//Logger(LiquidacionGeneral.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //PARA LOSS PERMISOS AMBIENTALES
            if (this.liquidacion.getTipoLiquidacion().getTransaccionPadre() == 16L) {
                try {
                    Calendar fechaInteres = Calendar.getInstance();
                    fechaInteres.set(this.liquidacion.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
                    this.liquidacion.setInteres(service.generarInteres(this.liquidacion.getSaldo(), fechaInteres.getTime(), null));
                    if (this.liquidacion.getInteres() == null) {
                        this.liquidacion.setInteres(BigDecimal.ZERO);
                    }
                } catch (Exception ex) {
                    //Logger.get//Logger(LiquidacionGeneral.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            List<RenParametrosInteresMulta> parametrosInteresMultas = service.getListParametrosInteresMulta(liquidacion);
            if (parametrosInteresMultas != null && !parametrosInteresMultas.isEmpty()) {//VERIFICAR SI EMITE MULTA-INTERES
                for (RenParametrosInteresMulta interesMulta : parametrosInteresMultas) {
                    if (interesMulta.getTipo().equalsIgnoreCase("I")) {
                        try {
                            Calendar fecha = Calendar.getInstance();
                            fecha.set(Calendar.DAY_OF_MONTH, interesMulta.getDia().intValue());
                            fecha.set(Calendar.MONTH, interesMulta.getMes().intValue() - 1);
                            fecha.set(Calendar.YEAR, liquidacion.getAnio() + 1);
                            liquidacion.setInteres(service.generarInteres(liquidacion.getSaldo(), fecha.getTime(), null));
                        } catch (Exception ex) {
                            //Logger.get//Logger(LiquidacionGeneral.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
//                    if (interesMulta.getTipo().equalsIgnoreCase("M")) {
//                        liquidacion.setRecargo(manager.generarMultas(liquidacion, interesMulta));
//                    }
                }
            }
            this.liquidacion.calcularPago();
        }
    }

    public void seleccionarLocal(FinaRenLocalComercial local) {
        listaLiquidaciones = new ArrayList<>();
        listaLiquidaciones = liquidacionService.busquedaLocalNoAdeuda(local, Arrays.asList("por_pagar"));
        System.out.println("listaLiquidaciones " + listaLiquidaciones.size());
        JsfUtil.executeJS("PF('localComaericales').hide()");
        JsfUtil.update("fmLocales");
        JsfUtil.update("mainForm");
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
//                            JsfUtil.executeJS("PF('dlogoPpredioPropiestario').show()");
//                            JsfUtil.update("frmPrediosPropiestarios");
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
            case 6:
                System.out.println("local");
                break;
        }
        if (temp != null && temp.getId() != null) {

            return temp;
        } else {

            return null;
        }
    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = service.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public BigDecimal interes2(FinaRenLiquidacion l, Boolean aplicaRemision) {
        if (!aplicaRemision) {
            l.setInteres(service.interesCalculado(l, new Date()));
        } else {
            l.setInteres(BigDecimal.ZERO);
        }

        return l.getInteres();
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

    public FinaRenLiquidacionService getLiquidacionService() {
        return liquidacionService;
    }

    public void setLiquidacionService(FinaRenLiquidacionService liquidacionService) {
        this.liquidacionService = liquidacionService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<FinaRenLiquidacion> getListaLiquidaciones() {
        return listaLiquidaciones;
    }

    public void setListaLiquidaciones(List<FinaRenLiquidacion> listaLiquidaciones) {
        this.listaLiquidaciones = listaLiquidaciones;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Cliente getPropieCliente() {
        return propieCliente;
    }

    public void setPropieCliente(Cliente propieCliente) {
        this.propieCliente = propieCliente;
    }

    public LazyModel<FinaRenLiquidacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FinaRenLiquidacion> lazy) {
        this.lazy = lazy;
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

    public Long getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Long tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
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

    public List<CatPredio> getPrediosPropiestarios() {
        return prediosPropiestarios;
    }

    public void setPrediosPropiestarios(List<CatPredio> prediosPropiestarios) {
        this.prediosPropiestarios = prediosPropiestarios;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public LazyModel<FinaRenLocalComercial> getLazyLcoal() {
        return lazyLcoal;
    }

    public void setLazyLcoal(LazyModel<FinaRenLocalComercial> lazyLcoal) {
        this.lazyLcoal = lazyLcoal;
    }

    public Cliente getPropietarioConsulta() {
        return propietarioConsulta;
    }

    public void setPropietarioConsulta(Cliente propietarioConsulta) {
        this.propietarioConsulta = propietarioConsulta;
    }
//</editor-fold>         
}
