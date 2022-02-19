/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.FnEstadoExoneracion;
import com.gestionTributaria.Entities.FnExoneracionClase;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
import com.gestionTributaria.Entities.PredioAnio;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.FinaRenDetalleLiquidacionService;
import com.gestionTributaria.Services.FinaRenEstadoLiquidacionService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FnExoneracionClaseService;
import com.gestionTributaria.Services.FnExoneracionLiquidacionService;
import com.gestionTributaria.Services.FnSolicitudExoneracionPrediosService;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.FnTipoExoneracionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.PredioAnioService;
import com.gestionTributaria.Services.SecuenciasServices;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.util.Lazy;

/**
 *
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class RegistroResolucionMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private FnExoneracionClaseService fnExoneracionClase;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FnTipoExoneracionService fnTipoExoneracionService;
    @Inject
    private CatPredioPropietarioService propietarioService;
    @Inject
    private VentanillaService servicioPredio;
    @Inject
    private FinaRenLiquidacionService liquidacionesService;
    @Inject
    private FinaRenEstadoLiquidacionService finaRenEstadoLiquidacionService;
    @Inject
    private FnSolicitudExoneracionServices fnSolicitudExoneracionService;
    @Inject
    private ContRegistroContable contRegistroContable;
    @Inject
    private ManagerService managerService;
    @Inject
    private FnSolicitudExoneracionPrediosService fnSolicitudExoneracionPrediosService;
    @Inject
    private PredioAnioService predioAnioService;
    @Inject
    private FnExoneracionLiquidacionService exoneracionLiquidaciones;
    @Inject
    private SecuenciasServices secuenciasService;
    @Inject
    private FinaRenDetalleLiquidacionService finaRenDetalleLiquidacionService;

    private List<FnExoneracionClase> claseExoneracion;
    private List<FnExoneracionTipo> tipoClaseExoneracion;
    private FnExoneracionClase claseExoneracionSelect;
    private FnExoneracionTipo tipoExoneracionSelect;
    private FnSolicitudExoneracion solicitudExoneracion;
    private FnSolicitudExoneracionPredios solicitudExoneracionPredioS;
    private Date fDesde;
    private Date fHasta;
    private List<FinaRenLiquidacion> liquidaciones;
    private List<CatPredio> predios;
    private List<CatPredio> predioSeleccionados;
    private CatPredioPropietario propietario;
    private SolicitudServicios solicitudServicio;
    private String porcentaje;
    private CatPredio predio;
    private List<FinaRenLiquidacion> liquidacionesSeleccionadas;
    private Double salarioContribuyente = 0.00;
    private FnExoneracionLiquidacion exoneracionLiquidacion;
    private int opcBusquedad = 1;
    private String criterio = "";
    private LazyModel<FinaRenLiquidacion> liquidacionesLazy;

    @PostConstruct
    public void initView() {
        if (this.session.getTaskID() != null) {
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
            cargarClasesExoneracion();
            tipoExoneracionSelect = new FnExoneracionTipo();
            solicitudExoneracion = new FnSolicitudExoneracion();
            solicitudExoneracionPredioS = new FnSolicitudExoneracionPredios();
            fDesde = new Date();
            fHasta = new Date();
            predio = new CatPredio();
            predios = new ArrayList<>();
            predioSeleccionados = new ArrayList<>();
            propietario = new CatPredioPropietario();
            liquidaciones = new ArrayList<>();
            liquidacionesSeleccionadas = new ArrayList<>();
            liquidacionesLazy = new LazyModel<>(FinaRenLiquidacion.class);
            liquidacionesLazy.getFilterss().put("estadoLiquidacion.id", 2);
            liquidacionesLazy.getFilterss().put("tipoLiquidacion.id", Arrays.asList(2L, 3L));
            traerPredio();
        }
    }

    public void buscarPredio() {
        liquidaciones.clear();
        if (opcBusquedad == 1) {
            predios = liquidacionesService.getListPredio(criterio);
        }
        if (opcBusquedad == 2) {
            liquidaciones = liquidacionesService.getLiquidacionesPagadas(opcBusquedad, criterio, "U");
        }
        if (opcBusquedad == 3) {
            liquidaciones = liquidacionesService.getLiquidacionesPagadas(opcBusquedad, criterio, "R");
        }
    }

    public void traerLiquidacionesPagadas() {
        if (predios.size() > 0) {
            liquidaciones = liquidacionesService.getLiquidacionesPagadas(opcBusquedad, predioSeleccionados.get(0).getClaveCat(), "U");
        } else {
            JsfUtil.addInformationMessage("", "Sin Resultados");
        }
    }

    public void traerPredio() {
        solicitudServicio = servicioPredio.findByIdTramite(this.tramite);
        if (solicitudServicio != null) {
            if (solicitudServicio.getPredio() != null) {
                predios.add(solicitudServicio.getPredio());
            }
        }
    }

    public void cargarClasesExoneracion() {
        claseExoneracion = new ArrayList<>();
        claseExoneracion = fnExoneracionClase.findAllClaseExoneracion();
    }

    public void actualizarTipoExoneracion() {
        tipoClaseExoneracion = new ArrayList<>();
        tipoClaseExoneracion = fnTipoExoneracionService.findAllExoneracionesByIdTipo(claseExoneracionSelect.getId());
    }

    public void actualuzarPorcentaje() {
        if (solicitudServicio.getServicioTipo().getServicio().getAbreviatura().equals("SETE")) {
            porcentaje = "100";
        }
    }

    public void cargarPropietario(CatPredio predio) {
        FinaRenTipoLiquidacion tipoLiquidacion = new FinaRenTipoLiquidacion();
        if (solicitudServicio.getPredio() != null) {
            System.out.println("ENTRA if: " + predio);
            propietario = propietarioService.findByPropietario(solicitudServicio.getPredio());
            System.out.println("propietario: " + propietario);
            predio = solicitudServicio.getPredio();
            if (solicitudServicio.getPredio().getTipoPredio().equals("U")) {
                tipoLiquidacion.setId(2L);
            } else {
                tipoLiquidacion.setId(3L);
            }
            liquidaciones = (List<FinaRenLiquidacion>) liquidacionesService.liquidacionesByPredio(solicitudServicio.getPredio(), tipoLiquidacion);
        } else {
            System.out.println("ENTRA else: " + predio);
            propietario = propietarioService.findByPropietario(predio);
            if (predio.getTipoPredio().equals("U")) {
                tipoLiquidacion.setId(2L);
            } else {
                tipoLiquidacion.setId(3L);
            }
            System.out.println("PREDIO SELECCIONADO: " + predio);
            liquidaciones = (List<FinaRenLiquidacion>) liquidacionesService.liquidacionesByPredioPagadas(predio, tipoLiquidacion);
        }
    }

    public void limpiar() {
        propietario = new CatPredioPropietario();
    }

    public void registrarResoluciones() {
        if (validarSeleccionarClaseTipoExoneracion() == true) {
            if (liquidacionesSeleccionadas.isEmpty() || liquidacionesSeleccionadas == null) {
                JsfUtil.addInformationMessage("", "DEBE SELECCIONAR LIQUIDACIONES");
                JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            } else {
                if (this.tramite.getServicio().getAbreviatura().equals("SETE") || this.tramite.getServicio().getAbreviatura().equals("D")) {
                    if (validarEdad() == false) {
                        JsfUtil.addInformationMessage("", "No cumple con los requisitos");
                    }
                }
                solicitudExoneracion.setPredio(liquidacionesSeleccionadas.get(0).getPredio());
                solicitudExoneracion.setExoneracionTipo(tipoExoneracionSelect);
                solicitudExoneracion.setFechaIngreso(new Date());
                solicitudExoneracion.setEstado(new FnEstadoExoneracion(2L));//EN PROCESO
                solicitudExoneracion.setNumResolucionSac(codigoResolucion());
                if (this.solicitud != null) {
                    solicitudExoneracion.setSolicitante(this.solicitud.getEnteSolicitante());
                }
                if (this.tramite.getServicio() != null) {
                    if (this.tramite.getServicio().getAbreviatura().equals("SETE") || this.tramite.getServicio().getAbreviatura().equals("D")) {
                        solicitudExoneracion.setPorcentaje(tipoExoneracionSelect.getPorcentaje().longValue());
                    }
                }
                solicitudExoneracion.setTramite(BigInteger.valueOf(this.tramite.getId()));
                solicitudExoneracion.setUsuarioCreacion(this.session.getNameUser());
                solicitudExoneracion = fnSolicitudExoneracionService.create(solicitudExoneracion);

                for (FinaRenLiquidacion liq : liquidacionesSeleccionadas) {
                    exoneracionLiquidacion = new FnExoneracionLiquidacion();
                    exoneracionLiquidacion = new FnExoneracionLiquidacion();
                    exoneracionLiquidacion.setEstado(Boolean.TRUE);
                    exoneracionLiquidacion.setExoneracion(solicitudExoneracion);
                    exoneracionLiquidacion.setFechaIngreso(new Date());
                    exoneracionLiquidacion.setLiquidacionOriginal(liq);
                    exoneracionLiquidacion = exoneracionLiquidaciones.create(exoneracionLiquidacion);
                }
                JsfUtil.addInformationMessage("", "Se registró con éxito");
                JsfUtil.executeJS("PF('dlgObservaciones').hide()");
                continuarProceso();
            }
        } else {
            JsfUtil.addInformationMessage("", "Debe seleccionar una clase y tipo de exoneración");
        }
    }

    public void continuarProceso() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.jefeFinanciero);
            getParamts().put("finanaciero_aprobacion", usuario.equals("") ? "admin_1" : usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "completas tareas", ex);
        }
    }

    public String codigoResolucion() {
        SecuenciaGeneral secuencia = secuenciasService.findNumberByCodigo("SECUENCIA_RESOLUCION");
        String codigoResolucion = "";
        if (this.tramite.getServicio() != null) {
            //SOLICITUD EXONERACION TERCERA EDAD
            if (this.tramite.getServicio().getAbreviatura().equals("SETE")) {
                codigoResolucion = "GADMCD-DGF-SR-TE-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia();
            }
            //DISCAPACIDAD
            if (this.tramite.getServicio().getAbreviatura().equals("D")) {
                codigoResolucion = "GADMCD-DGF-SR-D-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia();
            }
            //PAGO INDEBIDO
            if (this.tramite.getServicio().getAbreviatura().equals("PI")) {
                codigoResolucion = "GADMCD-DGF-SR-PI-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia();
            }
            //PAGO EN EXCESO
            if (this.tramite.getServicio().getAbreviatura().equals("PE")) {
                codigoResolucion = "GADMCD-DGF-SR-PE-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia();
            }
            //PRESCRIPCION DE TITULO
            if (this.tramite.getServicio().getAbreviatura().equals("PE")) {
                codigoResolucion = "GADMCD-DGF-SR-P-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia();
            }
        } else {
            //BAJA DE TITULO
            if (this.tramite.getTipoTramite().getAbreviatura().equals("PRORESOLBT")) {
                codigoResolucion = "GADMCD-DGF-SR-BTC-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia() + "-C";
            }
        }

        return codigoResolucion;
    }

    public void registrarExoneracion(List<FinaRenLiquidacion> liquidaciones) {
        FinaRenLiquidacion liquidacionN;
        FinaRenDetLiquidacion detLiquidacion;
        solicitudExoneracion.setPredio(liquidaciones.get(0).getPredio());
        solicitudExoneracion.setExoneracionTipo(tipoExoneracionSelect);
        solicitudExoneracion.setFechaIngreso(new Date());
        SecuenciaGeneral secuencia = secuenciasService.findNumberByCodigo("SECUENCIA_RESOLUCION");
        if (this.tramite.getServicio().getAbreviatura().equals("SETE")) {
            solicitudExoneracion.setNumResolucionSac("GADMCD-DGF-SR-TE-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia());
        } else {
            solicitudExoneracion.setNumResolucionSac("GADMCD-DGF-SR-D-" + Utils.getAnio(new Date()) + "-" + secuencia.getSecuencia());
        }
        solicitudExoneracion.setSolicitante(this.solicitud.getEnteSolicitante());
        solicitudExoneracion.setTramite(BigInteger.valueOf(this.tramite.getId()));
        solicitudExoneracion.setUsuarioCreacion(this.session.getNameUser());
        solicitudExoneracion = fnSolicitudExoneracionService.create(solicitudExoneracion);
        solicitudExoneracion.setPorcentaje(tipoExoneracionSelect.getPorcentaje().longValue());
        for (FinaRenLiquidacion liq : liquidaciones) {
            exoneracionLiquidacion = new FnExoneracionLiquidacion();
            exoneracionLiquidacion.setEstado(Boolean.TRUE);
            exoneracionLiquidacion.setExoneracion(solicitudExoneracion);
            exoneracionLiquidacion.setFechaIngreso(new Date());
            exoneracionLiquidacion.setLiquidacionOriginal(liq);
            //creo una nueva liquidacion
            liquidacionN = new FinaRenLiquidacion();
            liquidacionN = managerService.clonarRenLiquidacion(liq, solicitudExoneracion);
            exoneracionLiquidacion.setLiquidacionPosterior(liq);
            clonarDetLiquidacion(liq, liquidacionN);
            //calculo el valor de la exoneracion
            calcularExoneracion(liquidacionN);
            //cambio de estado a la liquidacion nueva
            liq.setEstaExonerado(Boolean.TRUE);
            liquidacionesService.edit(liq);
            exoneracionLiquidacion = exoneracionLiquidaciones.create(exoneracionLiquidacion);
        }
    }

    public Boolean validarEdad() {
        //--2016 17.02 --2017 25.52
        return true;
    }

    public Boolean validarSalario() {
        BigDecimal salarioActual = managerService.getSalarioAnio(Utils.getAnio(new Date()));
        Boolean bandera = false;
        if (salarioContribuyente > salarioActual.doubleValue()) {
            bandera = false;
        } else {
            bandera = true;
        }
        return true;
    }

    public Boolean validarPatrimonio() {
        Boolean bandera = false;
        BigDecimal salarioActualx500 = managerService.getSalarioAnio(Utils.getAnio(new Date()));
        if (predio.getAvaluoMunicipal().doubleValue() > salarioActualx500.doubleValue() * 500) {
            bandera = false;
        } else {
            bandera = true;
        }
        return bandera;
    }

    public void buscarLiquidacionesPendientesPagoExoneracion() {
        liquidaciones.clear();
        if (!predioSeleccionados.isEmpty()) {
            System.out.println("ENTRA 1");
            cargarPropietario(predioSeleccionados.get(0));
        }
    }

    public void bajaTituloPrescripcion(List<FinaRenLiquidacion> liquidaciones) {
        for (FinaRenLiquidacion liq : liquidaciones) {
            liq.setEstadoLiquidacion(finaRenEstadoLiquidacionService.findByCodigo("baja_prescripcion"));
            liquidacionesService.edit(liq);
            //aqui va el metodo que contabiliza
            contRegistroContable.anularPorPrescripcion(liq);
        }
    }

    public void clonarDetLiquidacion(FinaRenLiquidacion liqAnt, FinaRenLiquidacion liqAct) {
        List<FinaRenDetLiquidacion> detalle = finaRenDetalleLiquidacionService.findAllTipoLiquidacion(liqAnt);
        for (FinaRenDetLiquidacion det : detalle) {
            det.setLiquidacion(liqAct);
            det.setId(null);
            finaRenDetalleLiquidacionService.create(det);
            System.out.println("liq act: " + liqAct);
            System.out.println("liq ant: " + liqAnt);
        }
    }

    public void calcularExoneracion(FinaRenLiquidacion liq) {
        List<FinaRenDetLiquidacion> detalle = finaRenDetalleLiquidacionService.findAllTipoLiquidacion(liq);
        Double salarioBasicox500 = managerService.getSalarioAnio(Utils.getAnio(new Date())).doubleValue() * 500;
        Double salairoXTarifaImpositiva = 0.00;
        Double valorExonerar = 0.00;
        Double impuestoCobrar = 0.00;
        PredioAnio predioAnio = predioAnioService.findByAnioPredio(liq.getAnio(), liq.getPredio());
        if (predioAnio != null) {
            //encontro en predio año     
            System.out.println("ENTRA A TRUE");
            if (predioAnio.getAvaluoMunicipal().doubleValue() > salarioBasicox500 || this.tramite.getServicio().getAbreviatura().equals("SETE")) {
                //no se le puede sacar un porcentual, segun ordenanza se debe pagar la diferencia.
                salairoXTarifaImpositiva = salarioBasicox500 * predioAnio.getTarifaImpositiva().doubleValue();
                valorExonerar = salairoXTarifaImpositiva / 100;
            } else {
                //el avaluo es meno, se puede sacar un porcentaje
            }
        } else {
            System.out.println("ENTRA A FALSE");
            //no se encontro en predio año                 
            if (liq.getPredio().getAvaluoMunicipal().doubleValue() > salarioBasicox500 || this.tramite.getServicio().getAbreviatura().equals("SETE")) {
                //no se le puede sacar un porcentual, segun ordenanza se debe pagar la diferencia.
                salairoXTarifaImpositiva = salarioBasicox500 * liq.getPredio().getTarifaImpositiva().doubleValue();
                System.out.println("la tarifa impositiva: " + liq.getPredio().getTarifaImpositiva());
                valorExonerar = salairoXTarifaImpositiva / 100;
            } else {
                //el avaluo es meno, se puede sacar un porcentaje
            }
            System.out.println("EL SALARIO es: " + managerService.getSalarioAnio(Utils.getAnio(new Date())).doubleValue());
            System.out.println("EL SALARIO * 500 es: " + salarioBasicox500);
            System.out.println("EL SALARIO * 500 por ka tarifa impositiva es: " + salairoXTarifaImpositiva);
            System.out.println("Total Exonerar: " + valorExonerar);
            //busco el valor del rubro impuesto predial
            if (liq.getPredio().getTipoPredio().equals("U")) {
                for (FinaRenDetLiquidacion det : detalle) {
                    if (det.getRubro().getId() == 369L) {
                        impuestoCobrar = det.getValor().doubleValue() - valorExonerar;
                        if (impuestoCobrar <= 0) {
                            det.setValor(BigDecimal.ZERO);
                        } else {
                            det.setValor(BigDecimal.valueOf(impuestoCobrar));
                        }
                    }
                }
            } else {
                for (FinaRenDetLiquidacion det : detalle) {
                    if (det.getRubro().getId() == 394L) {
                        if (impuestoCobrar <= 0) {
                            det.setValor(BigDecimal.ZERO);
                        } else {
                            det.setValor(BigDecimal.valueOf(impuestoCobrar));
                        }
                    }
                }
            }

        }
    }

    public Boolean validarSeleccionarClaseTipoExoneracion() {
        Boolean estado = false;
        if (claseExoneracionSelect != null && tipoExoneracionSelect != null) {
            estado = true;
        } else {
            estado = false;
        }
        return estado;
    }

    //<editor-fold defaultstate="collapsed" desc="GET AND SET">
    public List<FnExoneracionClase> getClaseExoneracion() {
        return claseExoneracion;
    }

    public void setClaseExoneracion(List<FnExoneracionClase> claseExoneracion) {
        this.claseExoneracion = claseExoneracion;
    }

    public List<FnExoneracionTipo> getTipoClaseExoneracion() {
        return tipoClaseExoneracion;
    }

    public void setTipoClaseExoneracion(List<FnExoneracionTipo> tipoClaseExoneracion) {
        this.tipoClaseExoneracion = tipoClaseExoneracion;
    }

    public FnExoneracionClase getClaseExoneracionSelect() {
        return claseExoneracionSelect;
    }

    public void setClaseExoneracionSelect(FnExoneracionClase claseExoneracionSelect) {
        this.claseExoneracionSelect = claseExoneracionSelect;
    }

    public FnExoneracionTipo getTipoExoneracionSelect() {
        return tipoExoneracionSelect;
    }

    public void setTipoExoneracionSelect(FnExoneracionTipo tipoExoneracionSelect) {
        this.tipoExoneracionSelect = tipoExoneracionSelect;
    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

    public Date getfDesde() {
        return fDesde;
    }

    public void setfDesde(Date fDesde) {
        this.fDesde = fDesde;
    }

    public Date getfHasta() {
        return fHasta;
    }

    public void setfHasta(Date fHasta) {
        this.fHasta = fHasta;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<CatPredio> getPredios() {
        return predios;
    }

    public void setPredios(List<CatPredio> predios) {
        this.predios = predios;
    }

    public List<CatPredio> getPredioSeleccionados() {
        return predioSeleccionados;
    }

    public void setPredioSeleccionados(List<CatPredio> predioSeleccionados) {
        this.predioSeleccionados = predioSeleccionados;
    }

    public CatPredioPropietario getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredioPropietario propietario) {
        this.propietario = propietario;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public List<FinaRenLiquidacion> getLiquidacionesSeleccionadas() {
        return liquidacionesSeleccionadas;
    }

    public void setLiquidacionesSeleccionadas(List<FinaRenLiquidacion> liquidacionesSeleccionadas) {
        this.liquidacionesSeleccionadas = liquidacionesSeleccionadas;
    }

    public Double getSalarioContribuyente() {
        return salarioContribuyente;
    }

    public void setSalarioContribuyente(Double salarioContribuyente) {
        this.salarioContribuyente = salarioContribuyente;
    }

    public FnSolicitudExoneracionPredios getSolicitudExoneracionPredioS() {
        return solicitudExoneracionPredioS;
    }

    public void setSolicitudExoneracionPredioS(FnSolicitudExoneracionPredios solicitudExoneracionPredioS) {
        this.solicitudExoneracionPredioS = solicitudExoneracionPredioS;
    }

    public FnExoneracionLiquidacion getExoneracionLiquidacion() {
        return exoneracionLiquidacion;
    }

    public void setExoneracionLiquidacion(FnExoneracionLiquidacion exoneracionLiquidacion) {
        this.exoneracionLiquidacion = exoneracionLiquidacion;
    }

    public int getOpcBusquedad() {
        return opcBusquedad;
    }

    public void setOpcBusquedad(int opcBusquedad) {
        this.opcBusquedad = opcBusquedad;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidacionesLazy() {
        return liquidacionesLazy;
    }

    public void setLiquidacionesLazy(LazyModel<FinaRenLiquidacion> liquidacionesLazy) {
        this.liquidacionesLazy = liquidacionesLazy;
    }
//</editor-fold>

}
