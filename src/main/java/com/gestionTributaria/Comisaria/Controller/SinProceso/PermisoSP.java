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
import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.JvClasesPermisos;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.JvClasesPermisoServices;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PermisoSP implements Serializable {

    private static final Logger LOG = Logger.getLogger(PermisoSP.class.getName());

    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ComisariaServices comisariaServices;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private JvClasesPermisoServices servicesPermisos;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession viewReport;
    @Inject
    private ManagerService services;

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
    private Catalogo catalogo;
    private List<Catalogo> listaPermisos;
    private List<JvClasesPermisos> clasePermiso;
    private Long numPermiso;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenRubrosLiquidacion> detalle;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private BigDecimal valorCalculado = BigDecimal.ZERO;
    private Long tipoPermiso;
    private String observacion;

    @PostConstruct
    public void init() {
        try {
            resetear();
          //  consultarComisaria();
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    public void consultarComisaria() {
        lazy = new LazyModel<>(ComisariaRegistros.class);
        lazy.getFilterss().put("origen", "PER");
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
                    ciRucCobros = propietario.getIdentificacionCompleta();
                }

            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            JsfUtil.update("datosAdicion");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void seleccionarPredio(CatPredio p) {
        predio = p;
        if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
            comisaria.setEnte(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
            propietario = comisaria.getEnte();
        }
    }

    public void consulrarPersona() {
        if (ciRucCobros != null && !ciRucCobros.isEmpty() && ciRucCobros.length() > 0) {
            propietario = clienteService.buscarCliente(ciRucCobros);
        } else {
            JsfUtil.addWarningMessage("", "El campo idnetificación no puede ser nulo");
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
                if (predio.getNumPredio() == null) {
                    JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaNumPredio);
                    return null;
                }
                temp = service.getPredioNumPredio(predio.getNumPredio().longValue());
                break;
            case 4:
                if (predio.getClaveCat() == null) {
                    JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaClavePredio);
                    return null;
                }
                temp = service.getPredioByClaveCat(predio.getClaveCat());
                break;
            case 5:// Clave anterior
                if (predio.getPredialant() == null) {
                    JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.faltaClaveAnterior);
                    return null;
                }
                temp = service.getPredioByClaveCatAnt(predio.getPredialant());
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

    public void calcularArea() {

        if (comisaria.getLargo() != null && comisaria.getAncho() != null) {
            comisaria.setToalMetros(comisaria.getLargo().multiply(comisaria.getAncho()).setScale(2, RoundingMode.HALF_UP));
        }
    }

    public void verificacion() {
        System.out.println("tipoPermiso " + tipoPermiso);
    }

    public void save() {

        numPermiso = comisariaServices.numPermiso();

//            if (predio.getId() == null) {
//                JsfUtil.addWarningMessage("Advertencia", "Tiene que seleccionar el predio");
//                return;
//            }
        if (propietario == null) {
            JsfUtil.addWarningMessage("Advertencia", "Tiene que seleccionar un contribuyente");
            return;
        }

        comisaria.setEnte(propietario);

        if (catalogo != null) {
            comisaria.setTipoPermiso(new JvClasesPermisos(tipoPermiso));
            comisaria.setOrigen("PER");
            if (predio != null && predio.getId() != null) {
                comisaria.setPredio(predio.getId());
            }
            numPermiso = comisariaServices.numPermiso();
            comisaria.setNumPermiso(numPermiso);
            comisaria.setEnte(propietario);
            //  comisaria.setNumSolar(comisariaServices.numSolar());

            if (!calcularPermisoViaPublica()) {
                return;
            }
            comisaria.setObservacion(observacion);
            if (comisaria != null && comisaria.getId() == null) {
                comisaria = comisariaServices.create(comisaria);
            } else {
                comisariaServices.edit(comisaria);
            }
        }

        if (predio != null && predio.getId() != null) {
            liquidacion.setPredio(predio);
        }

        liquidacion.setComprador(propietario);
        generarLiquidacion();

        comisaria.setLiquidacion(liquidacion);
        comisariaServices.edit(comisaria);

        JsfUtil.addInformationMessage("", "Transacción exitosa");
        JsfUtil.executeJS("PF('dlogoNumPermiso').show()");
        JsfUtil.update("fmNumSolicitud");

    }

    public boolean calcularPermisoViaPublica() {
        switch (tipoLiquidacion.getId().intValue()) {
            case 13:
                if (comisaria.getToalMetros() == null && comisaria.getToalMetros().doubleValue() < 1) {
                    JsfUtil.addWarningMessage("", "Verifique los metros cuadrados");
                    return false;
                }
                break;
            case 10:
                if (comisaria.getTipoPermiso().getCodigoClsePermiso().equals(5L) || comisaria.getTipoPermiso().getCodigoClsePermiso().equals(8L) || comisaria.getTipoPermiso().getCodigoClsePermiso().equals(15L)) {
                    if (comisaria.getToalMetros() == null && comisaria.getToalMetros().doubleValue() < 1) {
                        JsfUtil.addWarningMessage("", "Verifique los metros cuadrados");
                        return false;
                    }
                }
                break;
            case 16:
                if (comisaria.getTipoPermiso().getCodigoClsePermiso().equals(7L) && comisaria.getTipoPermiso().getCodigoClsePermiso().equals(6L)) {
                    System.out.println("ok");
                } else {
                    if (comisaria.getToalMetros() == null && comisaria.getToalMetros().doubleValue() < 1) {
                        JsfUtil.addWarningMessage("", "Verifique los metros cuadrados");
                        return false;
                    }
                }
                break;

        }
        System.out.println(">>> " + tipoLiquidacion.getId() + " " + tipoPermiso + " " + comisaria.getToalMetros() + " " + comisaria.getDesde());
        valorCalculado = servicesPermisos.valorPermisoComisaraViaPublica(tipoLiquidacion.getId(), tipoPermiso.intValue(), comisaria.getToalMetros(), (Date) comisaria.getDesde());
        System.out.println("valorCalculado " + valorCalculado);
        return true;
    }

    public void generarLiquidacion() {
        if (session != null && session.getNameUser() != null && tipoLiquidacion != null && tipoLiquidacion.getId() != null) {
            liquidacion.setTipoLiquidacion(tipoLiquidacion);
            System.out.println("prefijo >>>" + liquidacion.getTipoLiquidacion().getPrefijo());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            // liquidacion.getTipoLiquidacion().setRenRubrosLiquidacionCollection(rubrosSeleccionado);
            liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            ///SI ES DEL REGISTRO PODRÁ COBRAR  =o
            if (liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas()) {
                liquidacion.setValidada(Boolean.FALSE);
            } else {
                liquidacion.setValidada(Boolean.TRUE);
            }
            liquidacion.setObservacion(observacion);
            liquidacion.setFechaIngreso(new Date());
            List<FinaRenDetLiquidacion> detalle = new ArrayList<>();
            List<FinaRenRubrosLiquidacion> rubros = service.getRubrosByTipoLiquidacionCodRubroASC(tipoLiquidacion.getId());
            BigDecimal total = BigDecimal.ZERO;
            if (rubros != null && !rubros.isEmpty()) {
                for (FinaRenRubrosLiquidacion item : rubros) {
                    FinaRenDetLiquidacion data = new FinaRenDetLiquidacion();
                    if (item.getCantidad() == null) {
                        item.setCantidad(1);
                    }
                    data.setCantidad(item.getCantidad());
                    data.setRubro(item);
                    data.setEstado(true);

                    if (item.getPrioridad().intValue() == 1 || item.getCodigoRubro().equals(1L)) {
                        data.setValor(valorCalculado);
                    } else {
                        data.setValor(item.getValor());
                    }

                    data.setValorRecaudado(BigDecimal.ZERO);
                    data.setValorSinDescuento(BigDecimal.ZERO);

                    total = total.add(data.getValor());
                    detalle.add(data);

                }

                liquidacion.setTotalPago(total);
                liquidacion = liquidacionService.crearLiquidacion(liquidacion, detalle);
                JsfUtil.addInformationMessage("Mensaje", "Liquidacion: " + liquidacion.getIdLiquidacion() + " Generada con exito");
                liquidacion.calcularPago();
            }

        } else {
            JsfUtil.addWarningMessage("", "Elija un tipo de permiso");
        }
    }

    public void close() {
        resetear();
        consultarComisaria();
    }

    public void resetear() {
        observacion = null;
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        liquidacion = new FinaRenLiquidacion();
        detalle = new ArrayList<>();
        ciRucCobros = null;
        catalogo = new Catalogo();
        listaPermisos = new ArrayList<>();
        listaPermisos = catalogoService.getItemCatalogo(Arrays.asList("GT_FUNCIONAMIENTO_VALLAS", "GT_FUNCIONAMIETNO_ANTENAS_PRIMERA", "GT_FUNCIONAMIETNO_ANTENAS_RENOVACION", "GT_VIA_PUBLIVA_ANUAL", "GT_VIA_PUBLICA_TMP", "GT_VIA_PUBLICA_SIN_FINES_LUCRO"));
        comisaria = new ComisariaRegistros();
        propietarioConsulta = new Cliente();
        tipoPermiso = null;
        predio = new CatPredio();
        propietario = new Cliente();
        comisaria.setDesde(new Date());
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_YEAR, 3);
        comisaria.setHasta(ca.getTime());
        propietario = new Cliente();
        ciRucCobros = null;
    }

    public BigDecimal interesReporte(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = services.valoresInteres(l, new Date());
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

    public void imprimirDistribuccion(FinaRenLiquidacion liq) {
        viewReport.borrarParametros();
        viewReport.instanciarParametros();
        viewReport.addParametro("id", liq.getId());
        viewReport.addParametro("descuento", liq.getDescuento());
        viewReport.addParametro("interes", interesReporte(liq));
        viewReport.addParametro("valor_exonerado", liq.getValorExoneracion());
        viewReport.addParametro("total", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.addParametro("recargo", liq.getRecargo());
        viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
        viewReport.addParametro("pagon_final", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.setNombreReporte("distribucionActivoTotales");
        viewReport.setNombreSubCarpeta("GestionTributatia/general");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimir(FinaRenLiquidacion liq) {
        System.out.println("liq " + liq.getId());

        interesReporte(liq);
        liq.setPagoFinal(liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));

        if (liq.getListDistribuciionCantones() != null && liq.getListDistribuciionCantones().size() > 0) {
            imprimirDistribuccion(liq);
        } else {

            viewReport.borrarParametros();
            viewReport.instanciarParametros();
            viewReport.addParametro("id", liq.getId());
            viewReport.addParametro("descuento", liq.getDescuento());
            viewReport.addParametro("interes", liq.getInteres());
            viewReport.addParametro("valor_exonerado", liq.getExoneracionSumValor());
            viewReport.addParametro("total", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.addParametro("recargo", liq.getRecargo());
            viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
            viewReport.addParametro("pagon_final", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.setNombreReporte("general");
            viewReport.setNombreSubCarpeta("GestionTributatia/general");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void cargarClasePermiso() {
        clasePermiso = new ArrayList<>();
        if (catalogo != null && catalogo.getId() != null) {
            System.out.println("catalogo " + catalogo);
            switch (catalogo.getCodigo()) {
                case "GT_FUNCIONAMIENTO_VALLAS":
                    tipoLiquidacion = service.find(FinaRenTipoLiquidacion.class, 13L);
                    break;
                case "GT_FUNCIONAMIETNO_ANTENAS_PRIMERA":
                    tipoLiquidacion = service.find(FinaRenTipoLiquidacion.class, 149L);
                    break;
                case "GT_FUNCIONAMIETNO_ANTENAS_RENOVACION":
                    tipoLiquidacion = service.find(FinaRenTipoLiquidacion.class, 10L);
                    break;
                case "GT_VIA_PUBLIVA_ANUAL":
                    tipoLiquidacion = service.find(FinaRenTipoLiquidacion.class, 16L);
                    break;
                case "GT_VIA_PUBLICA_SIN_FINES_LUCRO":
                    tipoLiquidacion = null;
                    break;
            }
            System.out.println("tipoLiquidacion " + tipoLiquidacion);
            clasePermiso = servicesPermisos.getItems(catalogo.getId());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<FinaRenRubrosLiquidacion> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FinaRenRubrosLiquidacion> detalle) {
        this.detalle = detalle;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public BigDecimal getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(BigDecimal valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public Long getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(Long tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
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

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public List<Catalogo> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<Catalogo> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public List<JvClasesPermisos> getClasePermiso() {
        return clasePermiso;
    }

    public void setClasePermiso(List<JvClasesPermisos> clasePermiso) {
        this.clasePermiso = clasePermiso;
    }

    public Long getNumPermiso() {
        return numPermiso;
    }

    public void setNumPermiso(Long numPermiso) {
        this.numPermiso = numPermiso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LazyModel<ComisariaRegistros> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ComisariaRegistros> lazy) {
        this.lazy = lazy;
    }
//</editor-fold> 
}
