/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Recaudacion;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPagoDetalle;
import com.asgard.Entity.FinaRenPagoRubro;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnLiquidacionConvenio;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Entities.RenSolicitudesLiquidacion;
import com.gestionTributaria.Services.CatPredioService;
import com.gestionTributaria.Services.FinaRenDetalleLiquidacionService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.gestionTributaria.Services.FnConvenioPagoDetallerService;
import com.gestionTributaria.Services.FnLiquidacionConvenioService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.RenRubroLiquidacionService;
import com.gestionTributaria.models.InteresRecargoDescuento;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Detalle;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetallePago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetalleRubro;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author ORIGAMI2
 */
@Stateless(name = "RecaudacionEjb")
@javax.enterprise.context.Dependent
public class RecaudacionEjb extends AccesosComprobanteElectronico implements RecaudacionInteface {

    private static final Logger LOG = Logger.getLogger(RecaudacionEjb.class.getName());

    @Inject
    private ManagerService manager;
    @Inject
    private UserSession session;
    @Inject
    private FinaRenPagoService pagoService;
    @Inject
    private FinaRenLiquidacionService renLiquidacionService;
    @Inject
    private FinaRenDetalleLiquidacionService renDetLiquidacionService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private RenFacturaService facturaService;
    @Inject
    private FnLiquidacionConvenioService liquidacionConvenioService;
    @Inject
    private FinaRenTipoLiquidacionService tipoLiquidacionService;
    @Inject
    private RenRubroLiquidacionService rubroLiquidacionService;
    @Inject
    private FnConvenioPagoDetallerService cuotasService;
    @Inject
    private CatPredioService predioService;

    @Override
    public FinaRenPago realizarPagoLiquidacion(FinaRenLiquidacion liquidacion, FinaRenPago pago, Cajero cajero, Boolean isSac) {
        return pagoService.realizarPago(liquidacion, pago, cajero, Boolean.TRUE);
    }

    @Override
    public FinaRenPago realizarPago(FinaRenLiquidacion liquidacion, FinaRenPago pago, Cajero cajero) {

        Long numComprobante;
//        List<ConsolidacionBanco> listCB;
        // COMPROBANTE DEL SGM
        numComprobante = manager.getNumComprobante();
       // System.out.println("secuencia-->" + manager.getNumComprobante());
        List<FinaRenPagoDetalle> detallePago;
        BigDecimal valorLiquidacion;
        FinaRenPagoRubro pagoRubro;
        BigDecimal valorRecaudacion;
//        MejPagoRubroMejora pagoRubroMejora;
        BigDecimal valorRecaudacionMejora;
        Map<String, Object> parametros;

//           // System.out.println("lista de deatalle --> " + pago.getRenPagoDetalles());
        try {

            detallePago = (List<FinaRenPagoDetalle>) pago.getRenPagoDetalles();
            pago.setFechaPago(new Date());
            pago.setEstado(true);
            pago.setNumComprobante(new BigInteger(numComprobante.toString()));
            pago.setLiquidacion(liquidacion);
            pago.setCajero(cajero);
            pago.setDescuento(liquidacion.getDescuento());
            pago.setRecargo(liquidacion.getRecargo());
            pago.setContribuyente(liquidacion.getComprador());
            pago.setNombreContribuyente(liquidacion.getNombreComprador());
            pago.setInteres(liquidacion.getInteres());
            if (session.getNameUser() != null) {
                pago.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
                pago.setIpUserSession(session.getIpClient());
            }
            manager.save(pago);
            liquidacion.setNumComprobante(pago.getNumComprobante());
            liquidacion.setSaldo(liquidacion.getSaldo().subtract(pago.getValor().subtract(pago.getInteres()).subtract(pago.getRecargo()).add(pago.getDescuento())));
           // System.out.println("liquidacion--> " + liquidacion.getSaldo());
            if (liquidacion.getSaldo().compareTo(BigDecimal.ZERO) < 1) {
                liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(1L));
                liquidacion.setSaldo(BigDecimal.ZERO);
                /*Anyelo*/
                if (liquidacion.getEstadoCoactiva() != null && liquidacion.getEstadoCoactiva() == 2) {
                    liquidacion.setEstadoCoactiva(3);
                }
//                pago.setValor(pago.getValor().subtract(pago.getDescuento()));
            }
            //VALOR DE LIQUIDACION
            valorLiquidacion = pago.getValor().subtract(pago.getInteres()).subtract(pago.getRecargo()).add(pago.getDescuento());
//           // System.out.println("lista detalle liquidacion-->" + liquidacion.getRenDetLiquidacionCollection());
            for (FinaRenDetLiquidacion rubro : liquidacion.getRenDetLiquidacionCollection()) {
                //RUBROS QUE ESTAN PENDIENTES DE RECAUDAR
                if (rubro != null) {
                    if (rubro.getValor() != null) {
                        if (!liquidacion.getTipoLiquidacion().getId().equals(13L)) {
                            rubro.setValorRecaudado(BigDecimal.ZERO.setScale(2));
                        }
                        if (rubro.getValor().compareTo(rubro.getValorRecaudado()) > 0) {
                            pagoRubro = new FinaRenPagoRubro();
                            BigDecimal valorRecaudar = rubro.getValor().subtract(rubro.getValorRecaudado());
                            if (valorLiquidacion.compareTo(valorRecaudar) >= 0) {//PAGO TOTAL DEL RUBRO / COMPLETA EL PAGO
                                //SE VERIFICA CUANTO SE VA A MENORAR DEL DINERO RECIBIDO
                                if (rubro.getValorRecaudado().compareTo(new BigDecimal("0.00")) <= 0) {
                                    valorRecaudacion = rubro.getValor();
                                } else {
                                    valorRecaudacion = rubro.getValor().subtract(rubro.getValorRecaudado());
                                }
                                rubro.setValorRecaudado(rubro.getValor());

                            } else {//PAGO PARCIAL DEL RUBRO
                                rubro.setValorRecaudado(rubro.getValorRecaudado().add(valorLiquidacion));
                                valorRecaudacion = valorLiquidacion;
                            }
                            manager.save(rubro);
                            //REGISTRO VALOR RECAUDADO POR RUBRO
                            pagoRubro.setPago(pago);
                            pagoRubro.setRubro(new FinaRenRubrosLiquidacion(rubro.getRubro().getId()));
                            pagoRubro.setValor(valorRecaudacion);
                            manager.save(pagoRubro);
                            //ACTUALIZACION EN RUBROS MEJORAS
                            //REGISTRO DE RUBROS DE MEJORAS
                            valorLiquidacion = valorLiquidacion.subtract(valorRecaudar);
                            if (valorLiquidacion.compareTo(new BigDecimal("0.00")) <= 0) {
                                break;
                            }
                        }
                    }

                }

            }
            manager.save(liquidacion);
            // OBSERVACION DEL SALDO DE UNA LIQUIDACION
            if (liquidacion.getEstadoLiquidacion().getId() == 2L) {
                pago.setObservacion("Saldo: " + liquidacion.getTotalPago().subtract(pago.getValor()) + ".");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Realizar Cobro>>>", e);
            return null;
        }
        return pago;
    }

    @Override
    public FinaRenLiquidacion realizarDescuentoRecargaInteresPredial(FinaRenLiquidacion emision, Date fechaPago) {
        try {
            InteresRecargoDescuento interesRecDesc = new InteresRecargoDescuento();
            Date fecha = new Date();
            if (fechaPago != null) {
                fecha = fechaPago;
            }
            Integer dia = Utils.getDateValues("D", fecha);
            Integer mes = Utils.getDateValues("M", fecha);
            //SE REALIZA UNA SOLO VEZ EL RECARGO O EL DESCUENTO
            emision.setRecargo(new BigDecimal("0.00"));
            emision.setDescuento(new BigDecimal("0.00"));
            emision.setInteres(new BigDecimal("0.00"));
            OpcionBusqueda op = new OpcionBusqueda();
            interesRecDesc = renLiquidacionService.getInteresRecargoDescuento(emision, op.getAnio(), mes, dia);
            if (interesRecDesc != null) {
                emision.setRecargo(interesRecDesc.getRecargo());
                emision.setDescuento(interesRecDesc.getDescuento());
                emision.setInteres(interesRecDesc.getInteres());
            }
            return emision;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public FinaRenLiquidacion getInteresesGenerales(FinaRenLiquidacion emision, Date fechaPago) {
        try {
            Date fecha = new Date();
            if (fechaPago != null) {
                fecha = fechaPago;
            }
            Integer dia = Utils.getDateValues("D", fecha);
            Integer mes = Utils.getDateValues("M", fecha);
            Integer anio = Utils.getDateValues("Y", fecha);
            RenParametrosInteresMulta parametrosInteresMulta;
            parametrosInteresMulta = (RenParametrosInteresMulta) manager.find("SELECT p FROM RenParametrosInteresMulta p WHERE p.tipoLiquidacion.id =:tipoLiquidacion AND p.tipo =:tipo and p.estado=:estado", new String[]{"tipoLiquidacion", "tipo", "estado"}, new Object[]{emision.getTipoLiquidacion().getId(), "I", true});
           // System.out.println("parametros intereses>>" + parametrosInteresMulta);
            if (parametrosInteresMulta != null) {
                if (mes >= parametrosInteresMulta.getMes() || anio > emision.getAnio()) {
                    Calendar fechaInteres = Calendar.getInstance();
                    fechaInteres.set(emision.getAnio(), parametrosInteresMulta.getMes(), parametrosInteresMulta.getDia(), 0, 0, 0);
                    emision.setInteres(this.generarInteres(emision.getSaldo(), fechaInteres.getTime(), fechaPago));
                } else {
                    emision.setInteres(BigDecimal.ZERO);
                }
            } else {
                emision.setInteres(BigDecimal.ZERO);
            }
            return emision;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public BigDecimal generarInteres(BigDecimal valor, Date fecha, Date fechaPago) {
        BigDecimal interes, a;
        BigDecimal interesValor = new BigDecimal("0.00");
        Calendar fechaHasta = Calendar.getInstance();
        if (fechaPago != null) {
            fechaHasta.setTime(fechaPago);
        }
        try {
            interes = this.getRenIntereses(fecha, fechaPago);
           // System.out.println("fecha ini >>" + fecha + " fecha pago>>" + fechaPago);
           // System.out.println("intereses>>" + interes + " valor >>>" + valor);
            // a = (BigDecimal) manager.getNativeQuery(QuerysFinanciero.getInteresNativo, new Object[]{new SimpleDateFormat("dd-MM-YYYY").format(fecha), new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime())});
            if (interes != null) {
                if (valor != null) {
                    interesValor = interes.multiply(valor).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                }
            } else {
                JsfUtil.addWarningMessage("Error", "Verifique en el Mantenimiento de Intereses este agreado el procentaje de interes correspondiente al " + Utils.getAnio(fecha));
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return interesValor;
    }

    public BigDecimal getRenIntereses(Date fechaInicio, Date fechafin) {
        BigDecimal interes;
        interes = BigDecimal.ZERO;
        try {
            interes = (BigDecimal) manager.getEntityManager().createNativeQuery("SELECT sum(r.porcentaje) FROM asgard.fina_ren_intereses r WHERE \n"
                    + "( r.hasta <= TO_DATE(:fechafin,'DD-MM-YYYY') OR (r.hasta >=  TO_DATE(:fechafin,'DD-MM-YYYY') \n"
                    + "AND DATE_PART('month', r.hasta) = DATE_PART('month', TO_DATE(:fechafin,'DD-MM-YYYY')) \n"
                    + "AND DATE_PART('YEAR', r.hasta) <= DATE_PART('year', TO_DATE(:fechafin,'DD-MM-YYYY')))) \n"
                    + "AND r.id IN(SELECT id FROM asgard.fina_ren_intereses i WHERE i.desde >=  TO_DATE(:fechaini,'DD-MM-YYYY') \n"
                    + "OR (i.desde <=  TO_DATE(:fechaini,'DD-MM-YYYY') AND DATE_PART('month', i.desde) = DATE_PART('month', TO_DATE(:fechaini,'DD-MM-YYYY')) "
                    + "AND DATE_PART('YEAR', i.desde) >= DATE_PART('year', TO_DATE(:fechaini,'DD-MM-YYYY'))));")
                    .setParameter("fechaini", new SimpleDateFormat("dd-MM-YYYY").format(fechaInicio))
                    .setParameter("fechafin", new SimpleDateFormat("dd-MM-YYYY").format(fechafin)).getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return BigDecimal.ZERO;
        }
        return interes;
    }

    public BigDecimal getValorImpuestoPrioritario(FinaRenLiquidacion emision) {
        for (FinaRenDetLiquidacion rubro : emision.getRenDetLiquidacionCollection()) {
            if (rubro.getRubro() != null) {
                if (rubro.getRubro().getCodigoRubro() != null && rubro.getRubro().getCodigoRubro().equals(1L)) {
                    return rubro.getValor();
                }

            }
        }
        return BigDecimal.ZERO;
    }

    public Boolean getClienteAplicaExoneracion(FinaRenLiquidacion liq, Cliente propietario) {
        int anionacimiento = Utils.getAnio(propietario.getFechaNacimiento());
        int aniodiferen = liq.getAnio() - anionacimiento;
        if (aniodiferen > TalentoHumano.ANIOS_MAYOR_EDAD) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<FnExoneracionLiquidacion> aplicarExoneracion(List<CatPredio> predios, FnSolicitudExoneracion solicitud, Map<String, Object> params) {
        solicitud = this.registarDatoSolicitudExoneracion(solicitud, this.registroSolicitudExoneracionPredios(predios));
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        FinaRenLiquidacion liquidacion = new FinaRenLiquidacion();
        BigDecimal totalPagoExneracion = BigDecimal.ZERO;
        FinaRenLiquidacion liquidacionExonerada = new FinaRenLiquidacion();
        List<FnExoneracionLiquidacion> exoneraciones = new ArrayList();
        Cliente propietario = (Cliente) params.get("PROPIETARIO");
        for (CatPredio predio : predios) {
            FinaRenTipoLiquidacion tipo = null;
            if (predio.getTipoPredio().equals("U")) {
                tipo = new FinaRenTipoLiquidacion(2L);
            } else {
                tipo = new FinaRenTipoLiquidacion(3L);
            }
            List<FinaRenLiquidacion> listaLiquidacion = renLiquidacionService.liquidacionesConsultaByTipoPredio(predio, new FinaRenEstadoLiquidacion(2L), tipo);
            for (FinaRenLiquidacion rl : listaLiquidacion) {
                if (this.getClienteAplicaExoneracion(rl, propietario)) {
                    liquidacion = rl;
                    liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud);
//                    if (solicitud.getExoneracionTipo().getId().intValue() == 37L) {
//                       // System.out.println("exoneracionTipo.getId().intValue() == 37L" + solicitud.getExoneracionTipo().getId().intValue());
//                        liquidacionExonerada.setBandaImpositiva(predio.getAvaluoMunicipal().divide(new BigDecimal("2")));
//                    } else {
//                        liquidacionExonerada.setBandaImpositiva(predio.getAvaluoMunicipal());
//                    }
                    totalPagoExneracion = this.getGenerarDetalleLiquidacionObtenerTotal(solicitud, liquidacion, liquidacionExonerada, params);
                    liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
                    liquidacionExonerada.setTotalPago(totalPagoExneracion);
                    liquidacionExonerada.setSaldo(totalPagoExneracion);
                    liquidacionExonerada.setValorExoneracion(liquidacion.getTotalPago().subtract(totalPagoExneracion));
                    liquidacionExonerada.setExoneracionDescripcion("EXONERACIÒN: " + solicitud.getExoneracionTipo().getDescripcion());
                    liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                    manager.save(liquidacionExonerada);
                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, liquidacionExonerada, solicitud));
                }
            }
        }
        return exoneraciones;
    }

    @Override
    public void aplicarExoneracionAlcabala(FinaRenLiquidacion liqui, FnSolicitudExoneracion solicitud, Map<String, Object> params) {

        //solicitud = this.registarDatoSolicitudExoneracion(solicitud, this.registroSolicitudExoneracionPredios(predios));
        //List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        FinaRenLiquidacion liquidacion = new FinaRenLiquidacion();
        BigDecimal totalPagoExneracion = BigDecimal.ZERO;
        FinaRenLiquidacion liquidacionExonerada = new FinaRenLiquidacion();
        List<FnExoneracionLiquidacion> exoneraciones = new ArrayList();
        BigDecimal maxSalario = (BigDecimal) params.get("SALARIOMAX");
        Cliente propietario = (Cliente) params.get("PROPIETARIO");
        FinaRenTipoLiquidacion tipo = null;

        liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud);
//                    if (solicitud.getExoneracionTipo().getId().intValue() == 37L) {
//                       // System.out.println("exoneracionTipo.getId().intValue() == 37L" + solicitud.getExoneracionTipo().getId().intValue());
//                        liquidacionExonerada.setBandaImpositiva(predio.getAvaluoMunicipal().divide(new BigDecimal("2")));
//                    } else {
//                        liquidacionExonerada.setBandaImpositiva(predio.getAvaluoMunicipal());
//                    }
        //totalPagoExneracion = this.getGenerarDetalleLiquidacionObtenerTotal(solicitud, liqui, liquidacionExonerada);
        liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
        liquidacionExonerada.setTotalPago(liqui.getTotalPago());
        liquidacionExonerada.setSaldo(totalPagoExneracion);
        // liquidacionExonerada.setValorExoneracion(solicitud.getValor());
        //liquidacionExonerada.setExoneracionDescripcion("EXONERACIÒN: " + solicitud.getExoneracionTipo().getDescripcion());
        liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        manager.save(liquidacionExonerada);
        //exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liqui, liquidacionExonerada, solicitud));

        //return exoneraciones;
    }

    public BigDecimal getGenerarDetalleLiquidacionObtenerTotal(FnSolicitudExoneracion solicitud, FinaRenLiquidacion liquidacionOriginal, FinaRenLiquidacion liquidacionPosterior, Map<String, Object> params) {
        BigDecimal totalPagoExoneracion = new BigDecimal("0.00");
        FinaRenDetLiquidacion detalleExonerado;
        BigDecimal maxSalario = (BigDecimal) params.get("SALARIOMAX");
        BigDecimal valorExcedente = (BigDecimal) params.get("VALOREXCEDENTE"), diferenciaExcedente;
        Boolean excedente = (Boolean) params.get("EXCEDENTE");
        diferenciaExcedente = valorExcedente.subtract(maxSalario).setScale(2, RoundingMode.HALF_UP);
        try {
            List<FinaRenDetLiquidacion> listDet = renDetLiquidacionService.findByidLiquidacion(liquidacionOriginal);
            for (FinaRenDetLiquidacion dl : listDet) {
                detalleExonerado = new FinaRenDetLiquidacion();
                detalleExonerado.setLiquidacion(liquidacionPosterior);
                detalleExonerado.setRubro(dl.getRubro());
                detalleExonerado.setValor(dl.getValor());
                if (dl.getRubro().getAplicaExoneracion() != null && dl.getRubro().getAplicaExoneracion()) {
                    if (excedente) {
                        Double valorCobro = (diferenciaExcedente.doubleValue() * dl.getValor().doubleValue()) / valorExcedente.doubleValue();
                       // System.out.println("valor cobro>>" + valorCobro);
                        detalleExonerado.setValorRecaudado(dl.getValor().subtract(new BigDecimal(valorCobro).setScale(2, RoundingMode.HALF_UP)));
                        detalleExonerado.setValor(new BigDecimal(valorCobro).setScale(2, RoundingMode.HALF_UP));
                    } else {
                        detalleExonerado.setValorRecaudado(dl.getValor().multiply(solicitud.getValor()).divide(new BigDecimal("100")));
                        detalleExonerado.setValor(dl.getValor().subtract(dl.getValor().multiply(solicitud.getValor()).divide(new BigDecimal("100"))));
                    }
                }
                totalPagoExoneracion = totalPagoExoneracion.add(detalleExonerado.getValor());
                manager.save(detalleExonerado);
            }
            return totalPagoExoneracion;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FnSolicitudExoneracionPredios> registroSolicitudExoneracionPredios(List<CatPredio> predios) {
        List<FnSolicitudExoneracionPredios> prediosEnSolicitud = new ArrayList<>();
        for (CatPredio prediosUrbano : predios) {
            FnSolicitudExoneracionPredios predioSolicitud = new FnSolicitudExoneracionPredios();
            predioSolicitud.setPredio(prediosUrbano);
            prediosEnSolicitud.add(predioSolicitud);
        }
        return null;
    }

    public FnSolicitudExoneracion registarDatoSolicitudExoneracion(FnSolicitudExoneracion solicitud, List<FnSolicitudExoneracionPredios> prediosSolicitud) {
        try {
            if (prediosSolicitud != null && !prediosSolicitud.isEmpty()) {
                for (FnSolicitudExoneracionPredios prediosSolicitud1 : prediosSolicitud) {
                    prediosSolicitud1.setSolicitudExoneracion(solicitud);
                    manager.save(prediosSolicitud1);
                }
            }
            //HiberUtil.flushAndCommit();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "DATOS SOLICITUD EXONERACION", e);
        }
        return solicitud;
    }

    public Boolean getAplicaExoneracion(FinaRenLiquidacion liq, Cliente propietario) {
        int anionacimiento = Utils.getAnio(propietario.getFechaNacimiento());
        int aniodiferen = liq.getAnio() - anionacimiento;
        if (aniodiferen > TalentoHumano.ANIOS_MAYOR_EDAD) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public FinaRenLiquidacion clonarRenLiquidacion(FinaRenLiquidacion liquidacion, FnSolicitudExoneracion solicitud) {
        FinaRenLiquidacion liquidacion2 = null;

        try {
            liquidacion.setEstadoLiquidacion((FinaRenEstadoLiquidacion) manager.find(FinaRenEstadoLiquidacion.class, 4L));
            manager.save(liquidacion);
            liquidacion2 = (FinaRenLiquidacion) Utils.clone(liquidacion);
            liquidacion2.setId(null);
            liquidacion2.setComprador(liquidacion.getComprador());
            liquidacion2.setTramite(liquidacion.getTramite());
            liquidacion2.setTipoLiquidacion(liquidacion.getTipoLiquidacion());
            liquidacion2.setPredio(liquidacion.getPredio());
            liquidacion2.setPredioRustico(liquidacion.getPredioRustico());
            liquidacion2.setRuralExcel(liquidacion.getRuralExcel());
            liquidacion2.setLocalComercial(liquidacion.getLocalComercial());
            liquidacion2.setRenValoresPlusvalia(liquidacion.getRenValoresPlusvalia());
            liquidacion2.setVendedor(liquidacion.getVendedor());
            liquidacion2.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            liquidacion2.setAvaluoConstruccion(liquidacion.getAvaluoConstruccion());
            liquidacion2.setAvaluoMunicipal(liquidacion.getAvaluoMunicipal());
            liquidacion2.setAvaluoSolar(liquidacion.getAvaluoSolar());
            liquidacion2.setAreaTotal(liquidacion.getAreaTotal());
            liquidacion2.setFechaIngreso(new Date());
            liquidacion2.setEstaExonerado(Boolean.TRUE);
            liquidacion2.setBombero(Boolean.FALSE);
            liquidacion2.setNombreComprador(liquidacion.getNombreComprador());
            liquidacion2.setCoactiva(liquidacion.getCoactiva());
            liquidacion2.setEstadoCoactiva(liquidacion.getEstadoCoactiva());
            liquidacion2 = (FinaRenLiquidacion) manager.save(liquidacion2);

            if (solicitud != null) {
                RenSolicitudesLiquidacion solsLiq = new RenSolicitudesLiquidacion();
                solsLiq.setEstado(Boolean.TRUE);
                solsLiq.setSolExoneracion(solicitud);
                solsLiq.setLiquidacion(liquidacion);
                manager.save(solsLiq);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }

        return liquidacion2;
    }

    public FnExoneracionLiquidacion registrarRelacionLiquidacionDadaBajaExoneracion(FinaRenLiquidacion liquidacionOriginal, FinaRenLiquidacion liquidacionPosterior, FnSolicitudExoneracion solicitud) {
        FnExoneracionLiquidacion exoneracionLiquidacion = new FnExoneracionLiquidacion();
        List<CoaJuicioPredio> juicioPredios;
        CoaJuicioPredio jp;
        try {
            exoneracionLiquidacion.setFechaIngreso(new Date());
            exoneracionLiquidacion.setLiquidacionOriginal(liquidacionOriginal);
            exoneracionLiquidacion.setLiquidacionPosterior(liquidacionPosterior);
            exoneracionLiquidacion.setExoneracion(solicitud);
            exoneracionLiquidacion.setUsuarioIngreso(session.getNameUser());
            exoneracionLiquidacion.setEstado(Boolean.TRUE);
            exoneracionLiquidacion.setEsUrbano(Boolean.TRUE);
            manager.save(exoneracionLiquidacion);
            if (liquidacionOriginal != null && liquidacionPosterior != null) {
                juicioPredios = manager.findByNamedQuery1("CoaJuicioPredio.findByLiquidacion", liquidacionOriginal);
                if (juicioPredios != null && !juicioPredios.isEmpty()) {
                    for (CoaJuicioPredio juicioPredio : juicioPredios) {
                        if (juicioPredio.getLiquidacion().getId().equals(liquidacionOriginal.getId())) {
                            jp = new CoaJuicioPredio();
                            jp.setAbogadoJuicio(juicioPredio.getAbogadoJuicio());
                            jp.setAnioDesde(juicioPredio.getAnioDesde());
                            jp.setAnioDeuda(juicioPredio.getAnioDeuda());
                            jp.setAnioHasta(juicioPredio.getAnioHasta());
                            jp.setEstado(Boolean.TRUE);
                            jp.setJuicio(juicioPredio.getJuicio());
                            jp.setLiquidacion(liquidacionPosterior);
                            jp.setObservacion(juicioPredio.getObservacion());
                            jp.setPredio(juicioPredio.getPredio());
                            jp.setValorDeuda(liquidacionPosterior.getTotalPago());
                            manager.save(jp);
                            juicioPredio.setEstado(Boolean.FALSE);
                            manager.save(juicioPredio);
                            if (juicioPredio.getJuicio().getTotalDeuda() != null) {
                                juicioPredio.getJuicio().setTotalDeuda(juicioPredio.getJuicio().getTotalDeuda().subtract(liquidacionOriginal.getTotalPago()).add(liquidacionPosterior.getTotalPago()));
                                manager.save(juicioPredio.getJuicio());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            exoneracionLiquidacion = null;
            LOG.log(Level.SEVERE, "GENERACION DETALLE EXONERACION", e);
        }
        return exoneracionLiquidacion;
    }

    @Asynchronous
    public void rubrosAdicionales(Long numComprobante) {
        renLiquidacionService.procedureRubrosAdicionales(numComprobante);
    }

    @Override
    public void emitirFactura(FinaRenPago pago) {
        if (pago.getCajero() == null) {
           // System.out.println("ERROR!!! No existe cajero");
            return;
        }
        List<Detalle> detalles = new ArrayList<>();
        RenFactura fac = new RenFactura();
        fac.setSolicitante(pago.getContribuyente());
        fac.setNumComprobante(pago.getNumComprobante().longValue());
        fac.setSubTotal(pago.getLiquidacion().getTotalPago());
        fac.setTotalPagar(pago.getValor());
        fac.setFechaEmision(new Date());
        fac.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(pago.getCajero().getVariableSecuenciaFacturas()));
        fac.setCodigoComprobante(pago.getCajero().getEntidad().getEstablecimiento() + "-" + pago.getCajero().getPuntoEmision() + "-" + String.format("%09d", fac.getNumeroComprobante()));
        fac.setCaja(pago.getCajero());
        fac.setComprobante(katalinaService.findComprobante(ComprobantesCode.FACTURA));
        fac = facturaService.create(fac);
        pago.setFactura(fac);
        pagoService.edit(pago);
        fac.setPagosList(pagoService.getRenPagoByLiquidacion(pago.getLiquidacion()));
       // System.out.println("lista >>" + fac.getPagosList());
        ComprobanteElectronico comprobanteElectronico = initComprobanteElectronico(fac, pago.getCajero());
        comprobanteElectronico.setDetalle(setDetalle(fac.getPagosList()));
        comprobanteElectronico.setTramite(fac.getNumTramite());
        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.FACTURA);
        comprobanteElectronico.setDetallePagos(this.getDetallePago(fac));
        comprobanteElectronico.setRubroMunicipios(getRubros(pago, Boolean.TRUE));
        comprobanteElectronico.setRubroTercero(getRubros(pago, Boolean.FALSE));
       // System.out.println("rubro M>>" + comprobanteElectronico.getRubroMunicipios().size() + " rubro TC>>" + comprobanteElectronico.getRubroTercero().size());
        comprobanteElectronicaService.enviarFacturaElectronicaSRI(comprobanteElectronico);
        this.rubrosAdicionales(fac.getNumeroComprobante().longValue());
    }

    public List<DetalleRubro> getRubros(FinaRenPago pago, Boolean muni) {
        List<DetalleRubro> list = new ArrayList();
        for (FinaRenPagoRubro r : pagoService.getRubrosbyPago(pago, muni)) {
            DetalleRubro rubro = new DetalleRubro();
            rubro.setDescripcion(r.getRubro().getDescripcion());
            rubro.setValor(r.getValor());
            list.add(rubro);
        }
        return list;
    }

    @Override
    public void reenviarFactura(RenFactura fac, Cajero caja) {
        ComprobanteElectronico comprobanteElectronico = initComprobanteElectronico(fac, caja);
        comprobanteElectronico.setDetalle(setDetalle(fac.getPagosList()));
        comprobanteElectronico.setTramite(fac.getNumTramite());
        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.FACTURA);
        comprobanteElectronico.setDetallePagos(this.getDetallePago(fac));
        comprobanteElectronicaService.enviarFacturaElectronicaSRI(comprobanteElectronico);
    }

    private List<Detalle> setDetalle(List<FinaRenPago> pagos) {
        Detalle detalle;
        List<Detalle> detalleList = new ArrayList<>();
        for (FinaRenPago p : pagos) {
            detalle = new Detalle();
            if (p.getLiquidacion().getPredio() != null && p.getLiquidacion().getPredio().getId() != null) {
                detalle.setCodigoPrincipal(p.getLiquidacion().getPredio().getClaveCat());
            } else if (p.getLiquidacion().getCodigoLocal() != null && !p.getLiquidacion().getCodigoLocal().isEmpty()) {
                detalle.setCodigoPrincipal(p.getLiquidacion().getCodigoLocal());
            } else {
                detalle.setCodigoPrincipal(p.getLiquidacion().getTipoLiquidacion().getPrefijo());
            }
           // System.out.println("codigo Principal>>" + detalle.getCodigoPrincipal() + " tipo>>" + p.getLiquidacion().getTipoLiquidacion().getPrefijo());
            detalle.setDescripcion(p.getLiquidacion().getTipoLiquidacion().getNombreTitulo());
            detalle.setValorUnitario(p.getLiquidacion().getTotalPago().doubleValue());
            detalle.setValorTotal(p.getValor().doubleValue());
            detalle.setCantidad(1);
            detalle.setDescuento(p.getDescuento().doubleValue());
            detalle.setInteres(p.getInteres().doubleValue());
            detalle.setRecargo(p.getRecargo().doubleValue());
            detalle.setCoactiva(p.getValorCoactiva().doubleValue());
            detalle.setCodigoTarifa("6");
            detalle.setIva(0.00);
            detalle.setAnio(p.getLiquidacion().getAnio());
            detalleList.add(detalle);
        }
        return detalleList;
    }

    public List<DetallePago> getDetallePago(RenFactura fac) {
        DetallePago pago;
        List<DetallePago> pagos = new ArrayList<>();
        pago = new DetallePago();
        pago.setFormaPago("01");
        pago.setTotal(fac.getTotalPagar().doubleValue());
        pagos.add(pago);
        return pagos;
    }

    @Override
    @Asynchronous
    public void saveLiquidacionConvenio(FinaRenLiquidacion liq, FnConvenioPago convenio) {
        FnLiquidacionConvenio lc = new FnLiquidacionConvenio();
        lc.setConvenio(new FnConvenioPago());
        lc.setConvenio(convenio);
        lc.setLiquidacion(liq);
        lc.setCoactiva(liq.getValorCoactiva());
        lc.setFechaRegistro(new Date());
        lc.setRecargo(liq.getRecargo());
        lc.setTotalDeuda(liq.getPagoFinal());
        lc.setInteres(liq.getInteres());
        liquidacionConvenioService.create(lc);
    }

    public Boolean getAnularPagoByNotaCredito(FinaRenPago pago) {
        try {
            pago.setEstado(Boolean.FALSE);
            for (FinaRenPagoRubro rr : pago.getFinaRenPagoRubroList()) {

            }
            return Boolean.TRUE;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return Boolean.FALSE;
        }
    }

    @Override
    public List<FinaRenTipoLiquidacion> getEspeciesFindAll() {
        return tipoLiquidacionService.getEspeciesFindAll();
    }

    @Override
    public List<FinaRenRubrosLiquidacion> getRubrosByTipoLiquidacion(FinaRenTipoLiquidacion tipo) {
        return rubroLiquidacionService.getRubrosBytipoLiquidacion(tipo);
    }

    @Override
    public Boolean anularPagoLiquidacion(RenFactura fac) {
        try {
            FinaRenPago pago = pagoService.getPagoByFactura(fac);
            if (pago != null) {
                pago.setEstado(Boolean.FALSE);
                pago.setPagoIndebido(Boolean.TRUE);
               // System.out.println("pago.getLiquidacion().getFinaRenDetLiquidacionList().size()>>" + pago.getLiquidacion().getFinaRenDetLiquidacionList().size());
                for (FinaRenDetLiquidacion dt : pago.getLiquidacion().getFinaRenDetLiquidacionList()) {
                    dt.setValorRecaudado(BigDecimal.ZERO);
                    renDetLiquidacionService.edit(dt);
                }
            }
            pago.getLiquidacion().setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            pago.getLiquidacion().setSaldo(pago.getLiquidacion().getTotalPago());
            if (pago.getLiquidacion().getEstadoCoactiva() != null && pago.getLiquidacion().getEstadoCoactiva() == 3) {
                pago.getLiquidacion().setEstadoCoactiva(2);
            }
            renLiquidacionService.edit(pago.getLiquidacion());
            return Boolean.TRUE;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUtil.addWarningMessage("ERROR|", "Error de aplicación...");
            return Boolean.FALSE;
        }
    }

    @Override
    public CatPredio findPredioByTipoAndNumPredio(String tipoPredio, String numPredio) {
        try {
            List<CatPredio> predios = predioService.findByNamedQuery("CatPredio.findByCodigoPredioAndTipo", new Object[]{numPredio, tipoPredio});
            return predios.get(0);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "buscar predio en recaudaciones EJB", e);
            return null;
        }
    }

    @Override
    public List<FinaRenLiquidacion> findLiquidacionesByPrediosAndAnio(CatPredio predio, Integer anio, Long estado) {
        return renLiquidacionService.getObtenerLiquidacionPrediosUOR(predio, Integer.SIZE, Long.MIN_VALUE);
    }

    public Boolean pagoConvenios(FinaRenLiquidacion liq) {
        try {
            FnConvenioPagoDetalle cuota = cuotasService.getCuotaDetalle(liq);
            if (cuota != null) {
                cuota.setEstado(Boolean.FALSE);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return Boolean.FALSE;
        }
    }

}
