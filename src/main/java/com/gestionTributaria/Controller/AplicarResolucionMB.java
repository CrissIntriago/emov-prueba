/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPagoDetalle;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Services.FinaRenDetalleLiquidacionService;
import com.gestionTributaria.Services.FinaRenEstadoLiquidacionService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FnExoneracionLiquidacionService;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.NotaCreditoServices;
import com.gestionTributaria.Services.ResolucionesService;
import com.gestionTributaria.Services.fnResolucionPredioServices;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.implInterfaces.ImplContRegistroContable;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.api.UITree;

/**
 *
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class AplicarResolucionMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ResolucionesService resolucionService;
    @Inject
    private FinaRenDetalleLiquidacionService finaRenDetalleService;
    @Inject
    private fnResolucionPredioServices fnresolucionPrediosService;
    @Inject
    private FnSolicitudExoneracionServices solicitudExoneracionService;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private ManagerService manager;
    @Inject
    private FnExoneracionLiquidacionService exoneracionLiquidacionService;
    @Inject
    private FinaRenPagoService pagoService;
    @Inject
    private NotaCreditoServices notaCreditoService;
    @Inject
    private FinaRenLiquidacionService liquidacionesService;
    @Inject
    private FinaRenEstadoLiquidacionService finaRenEstadoLiquidacionService;
    @Inject
    private ManagerService managerService;
    @Inject
    private FinaRenDetalleLiquidacionService detalleLiquidacionService;
    @Inject
    private ContRegistroContable contRegistroContable;

    private FnSolicitudExoneracion solicitudExoneracion;
    private List<FinaRenLiquidacion> liquidaciones;
    private List<FinaRenPago> liquidacinesPagadas;
    private List<FinaRenPagoDetalle> liquidacionesPagadasDetalle;
    private NotasCredito notaCredito;
    private Cliente cliente;
    private LazyModel<Cliente> solicitantes;
    private FinaRenPago pago;
    private List<FnExoneracionLiquidacion> listaLiquidacionesExoneracion;

    @PostConstruct

    public void initView() {
        if (this.session.getTaskID() != null) {
            solicitudExoneracion = new FnSolicitudExoneracion();
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
            liquidaciones = new ArrayList<>();
            liquidacinesPagadas = new ArrayList<>();
            liquidacionesPagadasDetalle = new ArrayList<>();
            notaCredito = new NotasCredito();
            cliente = new Cliente();
            solicitantes = new LazyModel<>(Cliente.class);
            pago = new FinaRenPago();
            listaLiquidacionesExoneracion = new ArrayList<>();
            traerSolicitudExoneracion();
            buscarResolucion();
            traerLiquidaciones();
        }
    }

    public void traerSolicitudExoneracion() {
        solicitudExoneracion = solicitudExoneracionService.findByTramiteSolicitudExoneracion(this.tramite.getId());
    }

    public void seleccionarEnte(Cliente client) {
        cliente = client;
        JsfUtil.update("mainForm:tbView");
    }

    public void traerLiquidaciones() {
        List<FinaRenPago> pagosLiq;
        if (this.tramite.getServicio() != null) {
            //PAGO INDEBIDO Y PAGO EN EXCESO
            if (this.tramite.getServicio().getAbreviatura().equals("PI") || this.tramite.getServicio().getAbreviatura().equals("PE")) {
                liquidaciones = exoneracionLiquidacionService.traerLiquidacionesSolicitud(solicitudExoneracion);
                for (FinaRenLiquidacion liq : liquidaciones) {
                    pagosLiq = pagoService.finAllPago(liq);
                    if (!pagosLiq.isEmpty()) {
                        for (FinaRenPago p : pagosLiq) {
                            liquidacinesPagadas.add(p);
                        }
                    }
                }
            }
            //EXONERACIONES
            if (this.tramite.getServicio().getAbreviatura().equals("SETE")) {
                liquidaciones = exoneracionLiquidacionService.traerLiquidacionesSolicitud(solicitudExoneracion);
            }
        } else {
            //LIQUIDACIONES PARA BAJA DE TITULO
            liquidaciones = exoneracionLiquidacionService.traerLiquidacionesSolicitud(solicitudExoneracion);
        }
    }

    public void continuarTares() {
        try {
            Boolean bandera = false;
            if (this.tramite.getServicio() != null) {
                if (this.tramite.getServicio().getAbreviatura().equals("PI") || this.tramite.getServicio().getAbreviatura().equals("PE")) {
                    if (liquidacinesPagadas.isEmpty()) {
                        bandera = true;
                    }
                }
                if (this.tramite.getServicio().getAbreviatura().equals("SETE")) {
                    if (liquidacinesPagadas.isEmpty()) {
                        for (FinaRenLiquidacion liq : liquidaciones) {
                            bandera = true;
                            if (!liq.getEstadoLiquidacion().getCodigo().equals("baja_exoneracion")) {
                                bandera = false;
                                break;
                            }
                        }
                    }
                }
            } else {
                if (this.tramite.getTipoTramite().getAbreviatura().equals("PRORESOLBT")) {
                    for (FinaRenLiquidacion liq : liquidaciones) {
                        bandera = true;
                        if (!liq.getEstadoLiquidacion().getCodigo().equals("baja_emision")) {
                            bandera = false;
                            break;
                        }
                    }
                }
            }

            if (bandera == true) {
                if (saveTramite() == null) {
                    return;
                }
                this.session.setVarTemp(null);
                super.completeTask((HashMap<String, Object>) getParamts());
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            } else {
                JsfUtil.addInformationMessage("", "Debe aplicar la resolución");
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "completas tareas", ex);
        }
    }

    public void pagoSeleccionado(FinaRenPago pag) {
        pago = pag;
        JsfUtil.update("mainForm:tbView");
    }

    public void aplicarResolucion() {
        if (this.tramite.getServicio() != null) {
            //para pago indebido y pago en exceso
            if (this.tramite.getServicio().getAbreviatura().equals("PI") || this.tramite.getServicio().getAbreviatura().equals("PE")) {
                notaCredito.setContribuyente(cliente);
                notaCredito.setFechaIngreso(new Date());
                notaCredito.setLiquidacion(pago.getLiquidacion());
                notaCredito.setObservacion("");
                notaCredito.setSaldo(pago.getValor());
                notaCredito.setUsuarioIngreso(session.getNameUser());
                notaCredito.setValor(pago.getValor());
                notaCreditoService.create(notaCredito);
                pago.setValor(BigDecimal.ZERO);
                pago.setEstado(Boolean.FALSE);
                pagoService.edit(pago);
                liquidacinesPagadas.clear();
                traerLiquidaciones();
                notaCredito = new NotasCredito();
                pago = new FinaRenPago();
                JsfUtil.update("mainForm:tbView");
            }
            if (this.tramite.getServicio().getAbreviatura().equals("SETE")) {
                System.out.println("ES TERCERA EDAD");
                for (FinaRenLiquidacion liq : liquidaciones) {
                    aplicarExoneracion(liq);
                }
            }
            if (this.tramite.getServicio().getAbreviatura().equals("SED")) {
                if (solicitudExoneracion.getPorcentaje() == 100L) {
                    System.out.println("ES EL 100 PORCIENTE");
                    for (FinaRenLiquidacion liq : liquidaciones) {
                        aplicarExoneracion(liq);
                    }
                } else {
                    for (FinaRenLiquidacion liq : liquidaciones) {
                        exoneracionDiscapacidad(liq);
                    }
                }
            }
            if (this.tramite.getServicio().getAbreviatura().equals("SPPP")) {
                System.out.println("PRESCRIPCION DE TITULO");
                for (FinaRenLiquidacion liq : liquidaciones) {
                    contRegistroContable.anularPorPrescripcion(liq);
                }

            }
        } else {
            if (this.tramite.getTipoTramite().getAbreviatura().equals("PRORESOLBT")) {
                bajaTitulo();
            } else {
                System.out.println("mo entra al if");
            }
        }
    }

    public void aplicarExoneracion(FinaRenLiquidacion liquidacion) {
        FinaRenLiquidacion liquidaiconN = copiarLiquidacion(liquidacion);
        liquidaiconN = liquidacionesService.create(liquidaiconN);
        FnExoneracionLiquidacion exoli = exoneracionLiquidacionService.traerSolicitudesLiquidacionXExoneracionLiquidacion(solicitudExoneracion, liquidacion);
        exoli.setLiquidacionPosterior(liquidaiconN);
        exoneracionLiquidacionService.edit(exoli);
        List< FinaRenDetLiquidacion> detalleLiq = detalleLiquidacionService.findAllTipoLiquidacion(liquidacion);
        List<FinaRenDetLiquidacion> detalleLiqN = copiarDetalleLiquidacion(detalleLiq, liquidaiconN);
        Double salarioBasicox500 = managerService.getSalarioAnio(Utils.getAnio(new Date())).doubleValue() * 500;
        System.out.println("SALRAIO BASICO UNICICADO POR 500: " + salarioBasicox500);
        Double diferenciaAvaluo = liquidacion.getPredio().getAvaluoMunicipal().doubleValue() - salarioBasicox500;
        System.out.println("resta del avaluo municipal - los 500 salarios: " + diferenciaAvaluo);
        Double porcentajeDiferencia = (diferenciaAvaluo * 100) / liquidacion.getPredio().getAvaluoMunicipal().doubleValue();
        System.out.println("porcentaje diferencia: " + porcentajeDiferencia);
        Double ValorExoneraPorcentual = 100 - porcentajeDiferencia;
        System.out.println("porcentaje del valor a exonerar: " + ValorExoneraPorcentual);
        Double ValorExonerar = 0.00;
        for (FinaRenDetLiquidacion detLiq : detalleLiqN) {
            if (detLiq.getRubro().getId() == 713 || detLiq.getRubro().getId() == 716) {
                if (diferenciaAvaluo > 0) {
                    ValorExonerar = (detLiq.getValor().doubleValue() * ValorExoneraPorcentual) / 100;
                    detLiq.setValor(BigDecimal.valueOf(detLiq.getValor().doubleValue() - ValorExonerar));
                    Double valorSaldo = liquidaiconN.getSaldo().doubleValue() - ValorExonerar;
                    Double totalPago = liquidaiconN.getTotalPago().doubleValue() - ValorExonerar;
                    liquidaiconN.setSaldo(BigDecimal.valueOf(valorSaldo));
                    liquidaiconN.setTotalPago(BigDecimal.valueOf(totalPago));
                    liquidacionesService.edit(liquidaiconN);
                    System.out.println("TIENE DIFERENCIA");
                    System.out.println("EL VALOR A COBRAR ES: " + ValorExonerar);
                    break;
                } else {
                    ValorExonerar = (detLiq.getValor().doubleValue() * solicitudExoneracion.getPorcentaje().doubleValue()) / 100;
                    Double valorSaldo = liquidaiconN.getSaldo().doubleValue() - detLiq.getValor().doubleValue();
                    Double totalPago = liquidaiconN.getTotalPago().doubleValue() - detLiq.getValor().doubleValue();
                    liquidaiconN.setSaldo(BigDecimal.valueOf(valorSaldo));
                    liquidaiconN.setTotalPago(BigDecimal.valueOf(totalPago));
                    liquidacionesService.edit(liquidaiconN);
                    detLiq.setValor(BigDecimal.valueOf(detLiq.getValor().doubleValue() - ValorExonerar));
                    System.out.println("SIN DIFERENCIA");
                    System.out.println("EL VALOR A COBRAR ES: " + ValorExonerar);
                }
            }
            detalleLiquidacionService.create(detLiq);
        }
        JsfUtil.addInformationMessage("", "Las exoneracines se realizarón con éxito");
    }

    public void exoneracionDiscapacidad(FinaRenLiquidacion liquidacion) {
        FinaRenLiquidacion liquidaiconN = copiarLiquidacion(liquidacion);
        liquidaiconN = liquidacionesService.create(liquidaiconN);
        FnExoneracionLiquidacion exoli = exoneracionLiquidacionService.traerSolicitudesLiquidacionXExoneracionLiquidacion(solicitudExoneracion, liquidacion);
        exoli.setLiquidacionPosterior(liquidaiconN);
        exoneracionLiquidacionService.edit(exoli);
        List< FinaRenDetLiquidacion> detalleLiq = detalleLiquidacionService.findAllTipoLiquidacion(liquidacion);
        System.out.println("TAMAÑO LIQ ANTIGUA: " + detalleLiq.size());
        List<FinaRenDetLiquidacion> detalleLiqN = copiarDetalleLiquidacion(detalleLiq, liquidaiconN);
        Double salarioBasicox500 = managerService.getSalarioAnio(Utils.getAnio(new Date())).doubleValue() * 500;
        System.out.println("SALRAIO BASICO UNICICADO POR 500: " + salarioBasicox500);
        Double diferenciaAvaluo = liquidacion.getPredio().getAvaluoMunicipal().doubleValue() - salarioBasicox500;
        System.out.println("resta del avaluo municipal - los 500 salarios: " + diferenciaAvaluo);
        Double porcentajeDiferencia = (diferenciaAvaluo * 100) / liquidacion.getPredio().getAvaluoMunicipal().doubleValue();
        System.out.println("porcentaje diferencia: " + porcentajeDiferencia);
        Double ValorExoneraPorcentual = 100 - porcentajeDiferencia;
        System.out.println("porcentaje del valor a exonerar: " + ValorExoneraPorcentual);
        Double ValorExonerar = 0.00;
        for (FinaRenDetLiquidacion detLiq : detalleLiqN) {
            if (detLiq.getRubro().getId() == 713 || detLiq.getRubro().getId() == 716) {
                if (diferenciaAvaluo > 0) {
                    ValorExonerar = (detLiq.getValor().doubleValue() * ValorExoneraPorcentual) / 100;
                    detLiq.setValor(BigDecimal.valueOf(detLiq.getValor().doubleValue() - ValorExonerar));
                    Double valorSaldo = liquidaiconN.getSaldo().doubleValue() - ValorExonerar;
                    Double totalPago = liquidaiconN.getTotalPago().doubleValue() - ValorExonerar;
                    liquidaiconN.setSaldo(BigDecimal.valueOf(valorSaldo));
                    liquidaiconN.setTotalPago(BigDecimal.valueOf(totalPago));
                    liquidacionesService.edit(liquidaiconN);
                    System.out.println("EL VALOR A COBRAR ES: " + ValorExonerar);
                    break;
                } else {
                    ValorExonerar = 0.00;
                    Double valorSaldo = liquidaiconN.getSaldo().doubleValue() - detLiq.getValor().doubleValue();
                    Double totalPago = liquidaiconN.getTotalPago().doubleValue() - detLiq.getValor().doubleValue();
                    liquidaiconN.setSaldo(BigDecimal.valueOf(valorSaldo));
                    liquidaiconN.setTotalPago(BigDecimal.valueOf(totalPago));
                    liquidacionesService.edit(liquidaiconN);
                    System.out.println("EL VALOR A COBRAR ES: " + ValorExonerar);
                }
                detLiq.setValor(BigDecimal.valueOf(ValorExonerar));
            }
            detalleLiquidacionService.create(detLiq);
        }
        JsfUtil.addInformationMessage("", "Las exoneracines se realizarón con éxito");
    }

    public FinaRenLiquidacion copiarLiquidacion(FinaRenLiquidacion liquidacion) {
        FinaRenLiquidacion liqN = (FinaRenLiquidacion) Utils.clone(liquidacion);
        liqN.setId(null);
        liqN.setEstadoLiquidacion(finaRenEstadoLiquidacionService.findByCodigo("por_pagar"));
        liqN.setEstaExonerado(Boolean.TRUE);
        liquidacion.setEstadoLiquidacion(finaRenEstadoLiquidacionService.findByCodigo("baja_exoneracion"));
        liquidacionesService.edit(liquidacion);
        return liqN;
    }

    public List<FinaRenDetLiquidacion> copiarDetalleLiquidacion(List<FinaRenDetLiquidacion> detLiq, FinaRenLiquidacion liquidacionN) {
        List<FinaRenDetLiquidacion> detalleLiqN = new ArrayList<>();
        detalleLiqN = detLiq;
        for (FinaRenDetLiquidacion detLiquidacion : detalleLiqN) {
            detLiquidacion.setLiquidacion(liquidacionN);
            detLiquidacion.setId(null);
        }
        return detalleLiqN;
    }

    public void bajaTitulo() {
        listaLiquidacionesExoneracion = exoneracionLiquidacionService.traerSolicitudesLiquidacion(solicitudExoneracion);
        FinaRenLiquidacion liqN;
        //cambio de estado a la liquidaciones a baja de titulo
        for (FinaRenLiquidacion liq : liquidaciones) {
            liq.setEstadoLiquidacion(finaRenEstadoLiquidacionService.findByCodigo("baja_emision"));
            liquidacionesService.edit(liq);
            //aqui el metodo para dar de baja a la liquidacion, debo volver a emitir.
        }
        JsfUtil.addInformationMessage("", "Liquidaciones dadas de baja con éxito");
    }
//    public void aplicarExoneracionViviendaNueva() {
//        List< FinaRenLiquidacion> liquidaciones;
//        FinaRenLiquidacion liquidacionClonada;
//        resolucionesPredios = (List<FnResolucionPredio>) fnresolucionPrediosService.findByIdResolucion(resolucion);
//        for (FnResolucionPredio p : resolucionesPredios) {
//            liquidaciones = (List<FinaRenLiquidacion>) finaRenDetalleService.finbyLiquidacionByPredio(p.getPredio(), resolucion.getAnioDesde(), resolucion.getAnioHasta());
//            for (FinaRenLiquidacion liq : liquidaciones) {
//                //clonamos la resolucion
//                liquidacionClonada = copiarLiquidacion(liq);
//
//            }
//        }
//    }
//
//    public FinaRenLiquidacion copiarLiquidacion(FinaRenLiquidacion liq) {
//        //cambio de estado a la liquidacion actual
//        liq.setEstadoLiquidacion((FinaRenEstadoLiquidacion) manager.find(FinaRenEstadoLiquidacion.class, 4L));
//        finaRenLiquidacionService.edit(liq);
//        FinaRenLiquidacion liquidacionClonada = Utils.clone(liq);
//        liquidacionClonada.setId(null);
//        //guardamos la liquidacion clonada
//        finaRenLiquidacionService.create(liquidacionClonada);
//        //guardamos en ren_solicitudes_liquidaciones la liquidacion clonada y la antigua
//        RenSolicitudesLiquidacion solicitudLiquidacion = new RenSolicitudesLiquidacion();
//        solicitudLiquidacion.setEstado(Boolean.TRUE);
//        solicitudLiquidacion.setLiquidacion(liquidacionClonada);
////        solicitudLiquidacion.setSolExoneracion();
//        return liquidacionClonada;
//    }

    public void buscarResolucion() {
        solicitudExoneracion = solicitudExoneracionService.findByTramiteSolicitudExoneracion(tramite.getId());

    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

    public List<FinaRenPago> getLiquidacinesPagadas() {
        return liquidacinesPagadas;
    }

    public void setLiquidacinesPagadas(List<FinaRenPago> liquidacinesPagadas) {
        this.liquidacinesPagadas = liquidacinesPagadas;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<FinaRenPagoDetalle> getLiquidacionesPagadasDetalle() {
        return liquidacionesPagadasDetalle;
    }

    public void setLiquidacionesPagadasDetalle(List<FinaRenPagoDetalle> liquidacionesPagadasDetalle) {
        this.liquidacionesPagadasDetalle = liquidacionesPagadasDetalle;
    }

    public NotasCredito getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(NotasCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public FinaRenPago getPago() {
        return pago;
    }

    public void setPago(FinaRenPago pago) {
        this.pago = pago;
    }

}
