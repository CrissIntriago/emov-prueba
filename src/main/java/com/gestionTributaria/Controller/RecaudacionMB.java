/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.Entities.NotaDetalle;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FnConvenioPagoService;
import com.gestionTributaria.Services.InteresesService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.NotaCreditoServices;
import com.gestionTributaria.Services.NotaDetalleService;
import com.gestionTributaria.Services.RemisionInteresService;
import com.gestionTributaria.Services.SecuenciasServices;
import com.gestionTributaria.models.BusquedaPredios;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.PagoTituloReporteModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.BancoService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobanteTipo;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * @author ORIGAMI2
 */
@Named
@ViewScoped
public class RecaudacionMB extends BusquedaPredios implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecaudacionMB.class.getName());
    @Inject
    private InteresesService interesService;
    @Inject
    private RemisionInteresService remisionInteres;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private UsuarioService userService;
    @Inject
    private BancoService bancoService;
    @Inject
    private ServletSession ss;
    @Inject
    private SecuenciasServices secuenciasServices;
    @Inject
    private ManagerService manager;
    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private RenFacturaService facturaService;
    @Inject
    private FnConvenioPagoService convenioPagoService;
    @Inject
    private NotaCreditoServices notaCreditoService;
    @Inject
    private NotaDetalleService notaDetalleService;
    @Inject
    private ClienteService clienteService;
    
    private CatPredio predio;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenLiquidacion> liquidacionList;
    private List<FinaRenLiquidacion> emisionesACobrar;
    private List<FinaRenLiquidacion> emisionesCobro;
    private FinaRenPago pagoCoactiva;
    private Cajero cajero;
    protected List<RenFactura> listPagoNota;
    protected List<NotasCredito> notasCredito;
    private List<Banco> bancos;
    private List<Banco> tarjetas;
    private FnConvenioPago convenioPago;
    private BigInteger numeroComprobante;
    private String ciBeneficiario;
    private Cliente beneficiario;
    private Cliente comprador;
    private RenFactura factura;
    private LazyModel<Cliente> solicitantes;
    private Integer tipoSolicitante;
    private Integer tipoEnte;
    //Especies
    private FinaRenTipoLiquidacion especiesSeleccionada;
    private List<FinaRenTipoLiquidacion> especies;
    private List<FinaRenRubrosLiquidacion> rubrosEspecies;

    private List<FinaRenTipoLiquidacion> especiesSeleccionadas;
    private List<FinaRenDetLiquidacion> rubrosEspeciesSeleccionados;
    private List<FinaRenLiquidacion> especiesAgregadas;
    private List<FinaRenLiquidacion> especiesGeneradas;
    private List<FinaRenDetLiquidacion> rubrosEspeciesAgregadas;
    private FinaRenLiquidacion liquidacionMinima;
    private Boolean renderedDetalle, accesoComprobante;
    private BigDecimal interes;
    private BigDecimal recargo;
    private BigDecimal coactivaValor;
    private BigDecimal abono;
    private BigDecimal valorMinimo;
    private BigDecimal pagoMinimo;

    @PostConstruct
    public void init() {
        factura = new RenFactura();
        beneficiario = new Cliente();
        comprador = new Cliente();
        ciBeneficiario = "";        
        liquidacion = new FinaRenLiquidacion();
        liquidacionList = new ArrayList<>();
        emisionesCobro = new ArrayList<>();
        cajero = cajeroService.findByCajero(userSession.getNameUser());
        if (cajero != null) {
            accesoComprobante = Boolean.TRUE;
        } else {
            accesoComprobante = Boolean.FALSE;
        }
        emisionesACobrar = new ArrayList<>();
        this.bancos = bancoService.getBancoList();
        this.tarjetas = bancoService.getBancoList();
        renderedDetalle = Boolean.TRUE;
        variosPagos = Boolean.FALSE;
        especiesAgregadas = new ArrayList<>();
        rubrosEspeciesAgregadas = new ArrayList<>();
        especiesSeleccionada = new FinaRenTipoLiquidacion();
        especies = recaudacionService.getEspeciesFindAll();
    }

    public void addRubrosTipoLiquidacion() {
        if (especiesSeleccionada != null) {
            rubrosEspecies = new ArrayList();
            rubrosEspeciesAgregadas = new ArrayList();
            rubrosEspecies = recaudacionService.getRubrosByTipoLiquidacion(especiesSeleccionada);
            System.out.println("rubros>>" + rubrosEspecies.size());
            for (FinaRenRubrosLiquidacion r : rubrosEspecies) {
                FinaRenDetLiquidacion dl = new FinaRenDetLiquidacion();
//                dl.setId(Utils.idTemp());
                dl.setCantidad(1);
                dl.setEstado(Boolean.TRUE);
                dl.setRubro(r);
                dl.setValor(r.getValor());
                dl.setValorRecaudado(BigDecimal.ZERO);
                rubrosEspeciesAgregadas.add(dl);
            }
        }
    }

    public void consultaPredioEspecie() {
        this.consultarPrediosEmisiones();
        if (this.predioConsulta != null && this.predioConsulta.getId() != null) {
            comprador = Utils.get(predioConsulta.getCatPredioPropietarioList(), 0).getEnte();
            ciBeneficiario = comprador.getIdentificacionCompleta();
            JsfUtil.addSuccessMessage("Busqueda Exitosa...", "predio " + this.predioConsulta.getClaveCat() + " encontrado");

        }
    }

    public void addEspecies() {
        if (especiesSeleccionada == null || especiesSeleccionada.getId() == null) {
            JsfUtil.addWarningMessage("Error", "debe seleccionar un tipo de Especie...");
            return;
        }
        if (rubrosEspeciesSeleccionados == null || rubrosEspeciesSeleccionados.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "debe seleccionar una tasa como minimo...");
            return;
        }
        if (comprador == null || comprador.getId() == null) {
            JsfUtil.addWarningMessage("Error", "debe buscar una Presona...");
            return;
        }

        System.out.println("tipo>>" + especiesSeleccionada.getId() + " rubros>>" + rubrosEspeciesSeleccionados.size());
        BigDecimal total = rubrosEspeciesSeleccionados.stream().map(FinaRenDetLiquidacion::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        FinaRenLiquidacion liq = new FinaRenLiquidacion();
        liq.setTotalPago(total);
        liq.setSaldo(total);
        liq.setTipoLiquidacion(especiesSeleccionada);
        liq.setComprador(comprador);
        if (this.predioConsulta != null && this.predioConsulta.getId() != null) {
            liq.setPredio(predioConsulta);
        }
        liq.setFinaRenDetLiquidacionList(rubrosEspeciesSeleccionados);
        especiesAgregadas.add(liq);
        this.limpiarEspecies();
    }

    public void actualizarValorByCantidad(FinaRenDetLiquidacion rubro) {
        rubro.setValor(rubro.getRubro().getValor().multiply(new BigDecimal(rubro.getCantidad())));
    }

    public void limpiarEspecies() {
        especiesSeleccionada = new FinaRenTipoLiquidacion();
        rubrosEspeciesSeleccionados = new ArrayList<>();
        rubrosEspecies = new ArrayList<>();
        rubrosEspeciesAgregadas = new ArrayList<>();
    }

    public void eliminarEspecie(FinaRenLiquidacion liq) {
        especiesAgregadas.remove(liq);
    }

    public void buscarCliente() {
        try {
            if (ciBeneficiario != null && !ciBeneficiario.isEmpty()) {
                Map<String, Object> pm = new HashMap<>();
                pm.put("identificacion", ciBeneficiario);
                Cliente ve = clienteService.buscarCliente(ciBeneficiario);
                if(ve.getId() == null){
                    ve = clienteService.create(ve);
                }
                if (ve != null) {
                    comprador = new Cliente();
//                    factura.setSolicitante(ve);
                    comprador = ve;
                } else {
                    JsfUtil.addInformationMessage("Advertencia", "CLIENTE NO HAYADO");
                    comprador = new Cliente();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            comprador = new Cliente();
            JsfUtil.addInformationMessage("Advertencia", "CLIENTE NO HAYADO");
        }
    }

    public void consultaMercados() {
        if (this.mercado != null && this.mercado.getId() != null && this.nLocal != null) {
            this.consultarPrediosEmisiones();
            if (!emisionesPrediales.isEmpty()) {
                for (FinaRenLiquidacion em : emisionesPrediales) {
                    em.calcularPago();
                }
            }
            emisionesACobrar.clear();
            emisionesCobro.addAll(emisionesPrediales);
            emisionesPrediales.clear();
            BigDecimal total = emisionesCobro.stream().map(FinaRenLiquidacion::getPagoFinal).reduce(BigDecimal.ZERO, BigDecimal::add);
            this.initModelPago();
            getModelPago().setValorTotal(total);
            getModelPago().setValorCobrar(total);
            getModelPago().setValorSaldoPagoFinal(total);
            getModelPago().setValorSaldoPago(total);
            setTotalEmisiones(total);
        }
    }

    public void procesarEspecie() {
        if (especiesAgregadas == null || especiesAgregadas.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "debe Agregar una Liquidacion como minimo...");
            return;
        }
        emisionesCobro = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        especiesGeneradas = new ArrayList();
        for (FinaRenLiquidacion l : especiesAgregadas) {
            FinaRenLiquidacion liq = new FinaRenLiquidacion();
            liq = liquidacionService.crearLiquidacion(l, l.getFinaRenDetLiquidacionList());
            if (liq.getId() != null) {
                liq.calcularPago();
                total = liq.getPagoFinal().add(total);
                emisionesCobro.add(liq);
            }
        }
        this.initModelPago();
        getModelPago().setValorTotal(total);
        getModelPago().setValorCobrar(total);
        getModelPago().setValorSaldoPagoFinal(total);
        setTotalEmisiones(total);
        System.out.println("Total>>" + total);
        if (!emisionesCobro.isEmpty()) {
            this.procesarPago();
        }
        this.init();
        this.initModelPago();
    }

    public BigDecimal interes(FinaRenLiquidacion liq) {
        Boolean aplicaRemision = remisionInteres.aplicaRemision(liq);
        if (aplicaRemision) {
            liq.setInteres(BigDecimal.ZERO);
        } else {
            liq.setInteres(interesService.interesesCalculado(liq, new Date()));
        }
        return liq.getInteres();
    }

    public void liquidacionesdirectas() {
        this.consultarTipoLiquidacionesEmisiones();
//        this.emisionesCobro = this.emisionesPrediales;
//        emisionesCobro.addAll(emisionesPrediales);
        Boolean act = Boolean.FALSE;
        for (FinaRenLiquidacion l : emisionesPrediales) {
            if (!emisionesCobro.contains(l)) {
                emisionesCobro.add(l);
                this.calculoTotalPago(emisionesCobro, null);
                act = Boolean.TRUE;
            }
        }
        if (act) {
            this.getModelPago().setValorCobrar(BigDecimal.ZERO);
            this.getModelPago().setValorSaldoPagoFinal(BigDecimal.ZERO);
            this.getModelPago().setValorCobrar(getModelPago().getValorCobrar().add(totalEmisiones));
            this.getModelPago().setValorSaldoPagoFinal(getModelPago().getValorSaldoPagoFinal().add(totalEmisiones));
        }
        this.emisionesPrediales.clear();
    }

    public void emisionSeleccionada(FinaRenLiquidacion liq) {
        if (!emisionesCobro.contains(liq)) {
            emisionesACobrar.clear();
            totalEmisiones = BigDecimal.ZERO;
            this.getModelPago().setValorCobrar(BigDecimal.ZERO);
            this.getModelPago().setValorSaldoPagoFinal(BigDecimal.ZERO);
            for (int i = 0; i <= emisionesPrediales.indexOf(liquidacionList.get(0)); i++) {
                if (emisionesPrediales.get(i).getPagoFinal() == null || emisionesPrediales.get(i).getPagoFinal().compareTo(BigDecimal.ZERO) <= 0) {
                    emisionesPrediales.get(i).calcularPago();
                }
                emisionesACobrar.add(emisionesPrediales.get(i));
                totalEmisiones = totalEmisiones.add(emisionesPrediales.get(i).getPagoFinal());
                this.getModelPago().setValorCobrar(getModelPago().getValorCobrar().add(emisionesPrediales.get(i).getPagoFinal()));
                this.getModelPago().setValorSaldoPagoFinal(getModelPago().getValorSaldoPagoFinal().add(emisionesPrediales.get(i).getPagoFinal()));
            }
            if (liquidacionList.size() == 1) {
                this.setLiquidacion(new FinaRenLiquidacion());
                this.setLiquidacion(liquidacionList.get(0));
            }
            if (liquidacionList.size() != 1) {
                this.setLiquidacion(null);
            }
            Set<FinaRenLiquidacion> liquidacionCobro = new HashSet<>();
            liquidacionCobro.addAll(emisionesACobrar);
            emisionesACobrar.clear();
            emisionesACobrar.addAll(liquidacionCobro);
            this.addlistaCobro();
            JsfUtil.update("mainForm");
        }
    }
    
    public int columnsDetalle(){
        switch(this.tipoConsulta.intValue()){
            case 8:
            case 7:
            case 6:
                this.renderedDetalle = Boolean.FALSE;
                return 1;
            default:
                this.renderedDetalle = Boolean.TRUE;
                return 2;
        }
    }
    
    public String getColumnClasses(){
        switch(this.tipoConsulta.intValue()){
            case 8:
            case 7:
            case 6:
                return "ui-grid-col-12";
            default:
                return "ui-grid-col-4,ui-grid-col-8";
        }
    }
    
    public void addlistaCobro() {
        emisionesACobrar.stream().filter((em) -> (!emisionesCobro.contains(em))).forEachOrdered((em) -> {
            emisionesCobro.add(em);
        });
        emisionesCobro.sort(Comparator.comparing(FinaRenLiquidacion::getAnio));
    }

    public void eliminarList(FinaRenLiquidacion liq) {
        totalEmisiones = totalEmisiones.subtract(liq.getPagoFinal());
        this.getModelPago().setValorCobrar(totalEmisiones);
        this.getModelPago().setValorSaldoPagoFinal(totalEmisiones);
        emisionesCobro.remove(liq);
    }

    public Boolean renderedList(FinaRenLiquidacion liq) {
        if (liq.getTipoLiquidacion().getId().equals(2L) || liq.getTipoLiquidacion().getId().equals(3L)) {
            if (liq.equals(emisionesCobro.get(emisionesCobro.size() - 1))) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void generarProforma() {
        SecuenciaGeneral secuencia = secuenciasServices.findNumberByCodigo("_SEC_PROFORMA");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date fechaCad = c.getTime();
        String nameFile = "PROFORMA_" + Utils.completarCadenaConCeros(secuencia.getSecuencia().toString(), 6) + "_" + new Date().getTime() + ".pdf";
        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/") + "reportes/");
        ss.addParametro("ANIO", Utils.getAnio(new Date()));
        ss.setNombreSubCarpeta("GestionTributatia/proforma");
        ss.setNombreReporte("proformaEmisiones");
        ss.addParametro("NOMBREENTIDAD", userSession.getUsuario().getEmpresaId().getNombreEntidad());
        ss.addParametro("ABREV", userSession.getUsuario().getEmpresaId().getAbreviatura());
        ss.addParametro("DIRECCION", userSession.getUsuario().getEmpresaId().getDireccion());
        ss.addParametro("TELEFONO", userSession.getUsuario().getEmpresaId().getTelefono1());
        ss.addParametro("CELULAR", userSession.getUsuario().getEmpresaId().getMovil());
        ss.addParametro("EMAIL", userSession.getUsuario().getEmpresaId().getEmail());
        ss.addParametro("RUC", userSession.getUsuario().getEmpresaId().getRuc());
        ss.addParametro("CAJA", userSession.getNameUser());
        ss.addParametro("ANIO", Utils.getAnio(new Date()));
        ss.addParametro("CADUC", fechaCad);
//        ss.setSaveFile(Boolean.TRUE);
        ss.setImprimir(Boolean.FALSE);
        ss.setUrlServerFile("C:/proforma/");
        ss.setNombreDocumento(nameFile);
        ss.setDataSource(emisionesCobro);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void realizarPago() {
        List<FinaRenPago> pagos = new ArrayList<>();
        FinaRenPago pago;
        pagoCoactiva = null;
        try {
            if (getModelPago().getValorTotal().compareTo(getTotalEmisiones()) >= 0) {
                if (getModelPago().getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                    this.numeroComprobante = new BigInteger(manager.getNumComprobante().toString());
                    for (FinaRenLiquidacion l : emisionesCobro) {
                        l.calcularPago();
                        if (l.getEstadoCoactiva() != null && l.getEstadoCoactiva() == 2) {
                            if (l.getValorCoactiva().doubleValue() == 0) {
                                l.calcularPago();
                            }
                        }
                        pago = recaudacionService.realizarPagoLiquidacion(l, getModelPago().realizarPago(l, this.numeroComprobante), cajero, true);
                        recaudacionService.emitirFactura(pago);
                    }
                    if(Utils.isNotEmpty(modelPago.getNotaCreditoMov())){
                        for (NotasCredito nc : modelPago.getNotaCreditoMov()) {
                            NotaDetalle nd = new NotaDetalle();
                            nd.setIdNota(nc);
                            nd.setFechaIngreso(new Date());
                            nd.setFechaPago(new Date());
                            nd.setUsuarioCreacion(cajero.getUsuario());
                            nd.setValor(nc.getValor().subtract(nc.getSaldo()));
                            nd.setComprobante(numeroComprobante.toString());
                            notaCreditoService.edit(nc);
                            notaDetalleService.edit(nd);
                        }
                    }

                    JsfUtil.addSuccessMessage("Información", "Cobro conExito...");
                    setPagoRealizado(Boolean.TRUE);
                    this.generarComprobante();
                    emisionesCobro = new ArrayList<>();
                    modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
                    this.clearData();
                } else {
                    JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                }
            } else {
                if (getModelPago().getValorMinimoPagar().compareTo(BigDecimal.ZERO) > 0 && getTotalEmisiones().compareTo(getModelPago().getValorMinimoPagar()) >= 0) {
                    JsfUtil.executeJS("PF('dlgAbono').show();");
                    JsfUtil.update("dlgAbono");
                    JsfUtil.update("frmAbono");
                } else {
                    JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados deben ser mayor o igual al de la Recaudación");
                }

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarAbono() {
        try {
            FinaRenPago pago;
            if (modelPago.getValorTotal().compareTo(getModelPago().getValorMinimoPagar()) >= 0) {
                this.numeroComprobante = new BigInteger(manager.getNumComprobante().toString());
                for (FinaRenLiquidacion l : emisionesCobro) {
                    l.calcularPago();
                    if (l.getEstadoCoactiva() != null && l.getEstadoCoactiva() == 2) {
                        if (l.getValorCoactiva().doubleValue() == 0) {
                            l.calcularPago();
                        }
                    }
                    pago = recaudacionService.realizarPagoLiquidacion(l, getModelPago().realizarPago(l, this.numeroComprobante), cajero, true);
                    recaudacionService.emitirFactura(pago);
                }
                JsfUtil.addSuccessMessage("Información", "Abono Realizada con exito...");
                setPagoRealizado(Boolean.TRUE);
                this.generarComprobante();
                emisionesCobro = new ArrayList<>();
                modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
                this.clearData();
                JsfUtil.executeJS("PF('dlgAbono').hide();");
                JsfUtil.update("dlgAbono");
                JsfUtil.update("frmAbono");
            } else {
                JsfUtil.addSuccessMessage("Información", "Verifique los valores...");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarEnte() {
        try {
            if (ciBeneficiario != null && !ciBeneficiario.isEmpty()) {
                Map<String, Object> pm = new HashMap<>();
                pm.put("identificacion", ciBeneficiario);
                Cliente ve = this.manager.findByParameter(Cliente.class, pm);
                if (ve != null) {
                    factura.setSolicitante(new Cliente());
                    factura.setSolicitante(ve);
                    comprador = ve;
                } else {
                    factura.setSolicitante(new Cliente());
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            factura.setSolicitante(new Cliente());
            JsfUtil.addInformationMessage("Advertencia", "CLIENTE NO HAYADO");
        }
    }

    public void actualizarSolicitante() {
        if (comprador.getId() == null) {
            JsfUtil.addWarningMessage("Error", "No se encontro Solicitante para actualizar...");
            return;
        }
        manager.save(comprador);
        JsfUtil.addInformationMessage("Información", "Datos actualizados con exitos...");
    }

    public void seleccionar() {
        if (this.getComprador() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar un solicitante del listado");
        } else {
            this.ciBeneficiario = this.getComprador().getIdentificacion();
            if (tipoSolicitante == 1) {
                JsfUtil.update("formProcesar");
            } else if (tipoSolicitante == 2) {
                beneficiario = this.getComprador();
                JsfUtil.update("formProcesar");
            }
            JsfUtil.addInformationMessage("Mensaje", "Contribuyente seleccionado.");
            JsfUtil.executeJS("PF('dlgSolicitante').hide();");
        }
    }

    public void showDlgEntes(Integer codigo) {
        try {

            tipoSolicitante = codigo;
            if (this.ciBeneficiario != null && this.ciBeneficiario.length() >= 10) {
                if (this.comprador == null) {
                    this.comprador = new Cliente();
                }
                this.comprador.setIdentificacion(this.ciBeneficiario);
                this.existeCedula();
                if (this.comprador != null && (this.comprador.getId() != null || this.comprador.getIdentificacion() != null)) {
                    if (this.comprador.getId() == null) {
                        comprador.setUsuarioCreacion(userSession.getNameUser());
                        comprador.setFechaCreacion(new Date());
                        comprador = (Cliente) manager.save(comprador);
                    }
                    beneficiario = comprador;
                    this.factura.setSolicitante(comprador);
                } else {
                    solicitantes = new LazyModel<>(Cliente.class);
                    solicitantes.getSorteds().put("apellido", "ASC");
                    JsfUtil.update("frmSolicitante");
                    JsfUtil.executeJS("PF('dlgSolicitante').show();");
                }
            } else {
                solicitantes = new LazyModel<>(Cliente.class);
                solicitantes.getSorteds().put("apellido", "ASC");
                JsfUtil.update("frmSolicitante");
                JsfUtil.executeJS("PF('dlgSolicitante').show();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void existeCedula() {

        String identificacion = comprador.getIdentificacion();
        if (comprador.getIdentificacion() != null && comprador.getIdentificacion().length() > 0) {
            comprador = (Cliente) manager.find("select c from Cliente c where c.identificacion= :ciRuc", new String[]{"ciRuc"}, new Object[]{identificacion});
            if (comprador == null) {
                comprador = new Cliente();
                comprador.setIdentificacion(identificacion);
                if (tipoEnte == 1 || this.ciBeneficiario != null) {

                }
            }
        } else {
            comprador = new Cliente();
        }
    }

    public void limpiarDatosContribuyente() {
        if (this.comprador != null && this.comprador.getId() != null) {
            this.factura.setSolicitante(new Cliente());
            this.comprador = new Cliente();
            ciBeneficiario = "";
        }
    }

    private void generarComprobante() {
        ss.addParametro("COMPROBANTE", numeroComprobante.longValue());
        ss.setNombreSubCarpeta("GestionTributatia/comprobantes");
        ss.setNombreReporte("comprobante");
//        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/") + "reportes/GestionTributatia/comprobantes/");
        ss.setImprimir(Boolean.TRUE);
        System.out.println("parametros--->>" + ss.getParametros());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void procesarPago() {
        try {
            if (Utils.isNotEmpty(emisionesCobro)) {
                for (FinaRenLiquidacion l : emisionesCobro) {
                    if (l.getAnio() > Utils.getAnio(new Date())) {
                        JsfUtil.addWarningMessage("Error", "Solo se puede realizar pago de emisión hasta el año actual.");
                        return;
                    }
                }
                System.out.println("tamaño >>>" + emisionesCobro.size());
                this.listPagoNota = facturaService.getRenFacturaTipoByComprobante(ComprobanteTipo.COMPPROBANTERETENCION, ComprobanteTipo.ESTADOAUTORIZADO);
                this.notasCredito = notaCreditoService.getNotasCreditoList();
                comprador = emisionesCobro.get(0).getComprador();
                ciBeneficiario = emisionesCobro.get(0).getComprador().getIdentificacionCompleta();
                this.liquidacion = emisionesCobro.get(emisionesCobro.size() - 1);
                this.totalEmisiones = getModelPago().getValorCobrar();
                this.interes = emisionesCobro.get(emisionesCobro.size() - 1).getInteres();
                this.recargo = emisionesCobro.get(emisionesCobro.size() - 1).getRecargo();
                this.coactivaValor = emisionesCobro.get(emisionesCobro.size() - 1).getValorCoactiva();
                this.abono = emisionesCobro.get(emisionesCobro.size() - 1).abonado();
                this.valorMinimo = emisionesCobro.get(emisionesCobro.size() - 1).calculoMinimoPago();
                this.pagoMinimo = getModelPago().getValorCobrar().subtract(emisionesCobro.get(emisionesCobro.size() - 1).getPagoFinal()).add(valorMinimo);
                getModelPago().setValorMinimoPagar(pagoMinimo);
                JsfUtil.update("formProcesar");
            } else {
                JsfUtil.addWarningMessage("Mensaje", "No existen Emisiones a procesar...");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void openDlgConvenio() {

        Map<String, List<String>> params = new HashMap<>();
        List<String> p = new ArrayList<>();

        p = new ArrayList<>();
        p.add("1");
        params.put("nuevo", p);

        p = new ArrayList<>();
        p.add(totalEmisiones.toString());
        params.put("deudaInicial", p);
        p = new ArrayList<>();
        p.add("0");
        params.put("calculaInteres", p);
        if (!emisionesPredialesTemp.isEmpty()) {
            p = new ArrayList<>();
            if (emisionesPredialesTemp.get(0).getComprador() != null) {
                p.add(emisionesPredialesTemp.get(0).getComprador().getId().toString());
            } else {
                JsfUtil.addSuccessMessage("Error", "Debe Actualizar los Datos del Conribuyente de los Años Anteriores");
//                return;
            }
            params.put("contribuyente", p);
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "50%");
        options.put("height", "85%");
        options.put("closable", true);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic("/resources/dialog/dlgConvenioPago", options, params);

    }

    public void procesarConvenio(SelectEvent event) {
        convenioPago = (FnConvenioPago) event.getObject();
        convenioPago.setConvenioAgua(Boolean.FALSE);
        convenioPago.setPredio(emisionesPredialesTemp.get(0).getPredio());
        convenioPago.setCoactiva(totalCoactiva);
        convenioPagoService.edit(convenioPago);
        emisionesPredialesTemp.stream().map((l) -> {
            l.setValidada(Boolean.TRUE);
            return l;
        }).map((l) -> {
            l.setUsuarioValida(userSession.getNameUser());
            return l;
        }).map((l) -> {
            recaudacionService.saveLiquidacionConvenio(l, convenioPago);
            l.setConvenioPago(new FnConvenioPago());
            l.setConvenioPago(convenioPago);
            return l;
        }).map((l) -> {
            l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(7L));
            return l;
        }).forEachOrdered((l) -> {
            liquidacionService.edit(l);
        });
        emisionesPrediales = new ArrayList();
        emisionesPredialesTemp = new ArrayList();
        JsfUtil.addSuccessMessage("Info", "El convenio se ha elaborado con exito.");
        this.init();
        JsfUtil.update("mainForm");

    }

    public FnConvenioPagoDetalle getCuotaConvenioByLiquidacion(FinaRenLiquidacion liqui) {
        if (liqui != null || liqui.getId() != null) {
            return liquidacionService.getCuaotaByLiquidacion(liqui);
        }
        return null;
    }

    public void clearData() {
        this.init();
        predioModel = new CatPredioModel();
        tipoLiquidaciones = new ArrayList<>();
        tipoSelect = new FinaRenTipoLiquidacion();
        emisionesPrediales = new ArrayList<>();
        prediosConsulta = new ArrayList<>();
        predioConsulta = new CatPredio();
        identificacion = "";
        nombreContribuyente = "";

    }

    public void generarTotalesConvenio() {
        totalEmisiones = emisionesPredialesTemp.stream().map(FinaRenLiquidacion::getPagoFinal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void consultarPorNombreComprador(int tipo) {

    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<FinaRenLiquidacion> getLiquidacionList() {
        return liquidacionList;
    }

    public void setLiquidacionList(List<FinaRenLiquidacion> liquidacionList) {
        this.liquidacionList = liquidacionList;
    }

    public List<FinaRenLiquidacion> getEmisionesACobrar() {
        return emisionesACobrar;
    }

    public void setEmisionesACobrar(List<FinaRenLiquidacion> emisionesACobrar) {
        this.emisionesACobrar = emisionesACobrar;
    }

    public List<FinaRenLiquidacion> getEmisionesCobro() {
        return emisionesCobro;
    }

    public void setEmisionesCobro(List<FinaRenLiquidacion> emisionesCobro) {
        this.emisionesCobro = emisionesCobro;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public List<RenFactura> getListPagoNota() {
        return listPagoNota;
    }

    public void setListPagoNota(List<RenFactura> listPagoNota) {
        this.listPagoNota = listPagoNota;
    }

    public List<Banco> getBancos() {
        return bancos;
    }

    public void setBancos(List<Banco> bancos) {
        this.bancos = bancos;
    }

    public List<Banco> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Banco> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public String getCiBeneficiario() {
        return ciBeneficiario;
    }

    public void setCiBeneficiario(String ciBeneficiario) {
        this.ciBeneficiario = ciBeneficiario;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public FinaRenTipoLiquidacion getEspeciesSeleccionada() {
        return especiesSeleccionada;
    }

    public void setEspeciesSeleccionada(FinaRenTipoLiquidacion especiesSeleccionada) {
        this.especiesSeleccionada = especiesSeleccionada;
    }

    public List<FinaRenTipoLiquidacion> getEspecies() {
        return especies;
    }

    public void setEspecies(List<FinaRenTipoLiquidacion> especies) {
        this.especies = especies;
    }

    public List<FinaRenTipoLiquidacion> getEspeciesSeleccionadas() {
        return especiesSeleccionadas;
    }

    public void setEspeciesSeleccionadas(List<FinaRenTipoLiquidacion> especiesSeleccionadas) {
        this.especiesSeleccionadas = especiesSeleccionadas;
    }

    public List<FinaRenLiquidacion> getEspeciesAgregadas() {
        return especiesAgregadas;
    }

    public void setEspeciesAgregadas(List<FinaRenLiquidacion> especiesAgregadas) {
        this.especiesAgregadas = especiesAgregadas;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosEspecies() {
        return rubrosEspecies;
    }

    public void setRubrosEspecies(List<FinaRenRubrosLiquidacion> rubrosEspecies) {
        this.rubrosEspecies = rubrosEspecies;
    }

    public List<FinaRenDetLiquidacion> getRubrosEspeciesSeleccionados() {
        return rubrosEspeciesSeleccionados;
    }

    public void setRubrosEspeciesSeleccionados(List<FinaRenDetLiquidacion> rubrosEspeciesSeleccionados) {
        this.rubrosEspeciesSeleccionados = rubrosEspeciesSeleccionados;
    }

    public List<FinaRenDetLiquidacion> getRubrosEspeciesAgregadas() {
        return rubrosEspeciesAgregadas;
    }

    public void setRubrosEspeciesAgregadas(List<FinaRenDetLiquidacion> rubrosEspeciesAgregadas) {
        this.rubrosEspeciesAgregadas = rubrosEspeciesAgregadas;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public BigDecimal getPagoMinimo() {
        return pagoMinimo;
    }

    public void setPagoMinimo(BigDecimal pagoMinimo) {
        this.pagoMinimo = pagoMinimo;
    }

    public FinaRenLiquidacion getLiquidacionMinima() {
        return liquidacionMinima;
    }

    public void setLiquidacionMinima(FinaRenLiquidacion liquidacionMinima) {
        this.liquidacionMinima = liquidacionMinima;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getCoactivaValor() {
        return coactivaValor;
    }

    public void setCoactivaValor(BigDecimal coactivaValor) {
        this.coactivaValor = coactivaValor;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public List<NotasCredito> getNotasCredito() {
        return notasCredito;
    }

    public void setNotasCredito(List<NotasCredito> notasCredito) {
        this.notasCredito = notasCredito;
    }

    public List<FinaRenLiquidacion> getEspeciesGeneradas() {
        return especiesGeneradas;
    }

    public void setEspeciesGeneradas(List<FinaRenLiquidacion> especiesGeneradas) {
        this.especiesGeneradas = especiesGeneradas;
    }

    public Boolean getRenderedDetalle() {
        return renderedDetalle;
    }

    public void setRenderedDetalle(Boolean renderedDetalle) {
        this.renderedDetalle = renderedDetalle;
    }

    public Boolean getAccesoComprobante() {
        return accesoComprobante;
    }

    public void setAccesoComprobante(Boolean accesoComprobante) {
        this.accesoComprobante = accesoComprobante;
    }

}
