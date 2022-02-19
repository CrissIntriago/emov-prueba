/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.DetalleReporteMovimientoCuenta;
import com.origami.sigef.contabilidad.model.ReporteMovimientoCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;
import com.origami.sigef.resource.contabilidad.services.ContBeneficiarioComprobantePagoService;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralDetalleService;
import com.origami.sigef.resource.contabilidad.services.ContSaldoInicialService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
 * @author jesus
 */
@Named(value = "reporteMovimientoCuentaView")
@ViewScoped
public class ReporteMovimientoCuentaController implements Serializable {

    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private ContDiarioGeneralDetalleService contDiarioGeneralDetalleService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContSaldoInicialService contSaldoInicialService;
    @Inject
    private ContBeneficiarioComprobantePagoService contBeneficiarioComprobantePagoService;

    private List<Short> listaPeriodo;
    private List<ContCuentas> contCuentas;
    private final String CODIGOCUENTAITEM = "CC";
    private OpcionBusqueda opcionBusqueda;
    private ContCuentas cuentaDesde;
    private ContCuentas cuentaHasta;
    private Date fechaDesde;
    private Date fechaHasta;

    private List<ContDiarioGeneralDetalle> detallesTransacciones;
    private List<ContDiarioGeneralDetalle> detalleComprobantePago;
    private Date fechaDefaultDesde;

    @PostConstruct
    public void init() {
        loadModel();
        listaPeriodo = catalogoItemService.getPeriodo();
    }

    public void loadModel() {
        opcionBusqueda = new OpcionBusqueda();
        createFechaDesde();
        findCuentaContableUpdate();
        cuentaDesde = new ContCuentas();
        cuentaHasta = new ContCuentas();
        detallesTransacciones = new ArrayList<>();
        detalleComprobantePago = new ArrayList<>();
    }

    public void createFechaDesde() {
        Calendar fechaActual = new GregorianCalendar();
        int year = fechaActual.get(Calendar.YEAR);
        String fecha = "01-01-" + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechaHasta = new Date();
            fechaDesde = dateFormat.parse(fecha);
            fechaDefaultDesde = fechaDesde;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void findCuentaContableUpdate() {
//        Map<String, String> order = new HashMap<>();
//        order.put("codigo", "ASC");
//        contCuentas = contCuentasService.findAllOrder(order);
        contCuentas = contCuentasService.findAllByEstadoIsTRUE();
        try {
            fechaDefaultDesde = Utils.getPrimerDiaAnio(opcionBusqueda.getAnio().intValue());
            asignarRangoPeriodos();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    public void generarReporteCuenta(Boolean excel) {
        int aux = 0;
        int aux2 = 0;
        for (int i = 0; i < contCuentas.size(); i++) {
            if (contCuentas.get(i).equals(cuentaDesde)) {
                aux = i;
            }
            if (contCuentas.get(i).equals(cuentaHasta)) {
                aux2 = i;
            }
        }
        if (aux > aux2) {
            JsfUtil.addErrorMessage("", "Error al seleccionar el rango de Cuentas, La Cuenta desde es mayor a la cuenta hasta seleccionada");
            return;
        }
        if (fechaDesde.after(fechaDefaultDesde)) {
            // HACER EL PROCESO PARA FECHAS POSTERIORES A LA POR DEFAULT 01-01-XXXX
            List<ReporteMovimientoCuentas> cuentasAnteriores = llenarLisDetalleTransaccion(aux, aux2, fechaDefaultDesde, Utils.sumarRestarDiasFecha(fechaDesde, -1));
            List<ReporteMovimientoCuentas> cuentasNuevas = new ArrayList<>();
            if (!cuentasAnteriores.isEmpty()) {
                for (ReporteMovimientoCuentas r : cuentasAnteriores) {
                    cuentasNuevas.add(llenarListDetalleTransaccionPosterior(r, fechaDesde, fechaHasta));
                }
            }
            enviarReporte(cuentasNuevas, excel);
        } else {
            enviarReporte(llenarLisDetalleTransaccion(aux, aux2, fechaDesde, fechaHasta), excel);
        }
        loadModel();
    }

    public void comprobanteSinDiarioGeneral(Long idCuenta, Date fechaDesde, Date fechaHasta) {
        if (detalleComprobantePago.isEmpty()) {
            detalleComprobantePago = contDiarioGeneralDetalleService.findAllDiarioGeneralDetalleOfDetalleComprobantePagoByIdContCuentasAndFechaRegistroSinDiarioGeneral(idCuenta, fechaDesde, fechaHasta);
        } else {
            List<ContDiarioGeneralDetalle> d;
            List<ContDiarioGeneralDetalle> daux = new ArrayList<>();
            d = contDiarioGeneralDetalleService.findAllDiarioGeneralDetalleOfDetalleComprobantePagoByIdContCuentasAndFechaRegistroSinDiarioGeneral(idCuenta, fechaDesde, fechaHasta);
            if (d != null && !d.isEmpty()) {
                for (ContDiarioGeneralDetalle dpSinDiario : d) {
                    daux.add(dpSinDiario);
                }
                for (ContDiarioGeneralDetalle dpConDiario : detalleComprobantePago) {
                    daux.add(dpConDiario);
                }
                detalleComprobantePago = new ArrayList<>();
                detalleComprobantePago = daux;
            }
        }
    }

    public ReporteMovimientoCuentas llenarListDetalleTransaccionPosterior(ReporteMovimientoCuentas reporteAnterior, Date fechaDesde, Date fechaHasta) {
        ReporteMovimientoCuentas reporte = new ReporteMovimientoCuentas();
        BigDecimal saldoAnterior;
        if (reporteAnterior.getTotalSaldo() == null) {
            saldoAnterior = reporteAnterior.getTotalDebe().subtract(reporteAnterior.getTotalHaber());
        } else {
            saldoAnterior = reporteAnterior.getTotalSaldo();
        }
        for (ContCuentas cuenta : contCuentas) {
            if (cuenta.getCodigo().equals(reporteAnterior.getCodigoCuenta())) {
                detallesTransacciones = contDiarioGeneralDetalleService.findAllDiarioGeneralDetalleByIdContCuentasAndFechaRegistro(cuenta.getId(), fechaDesde, fechaHasta);
                detalleComprobantePago = contDiarioGeneralDetalleService.findAllDiarioGeneralDetalleOfDetalleComprobantePagoByIdContCuentasAndFechaRegistro(cuenta.getId(), fechaDesde, fechaHasta);

                //comprobanteSinDiarioGeneral(cuenta.getId(), fechaDesde, fechaHasta);
                reporte = new ReporteMovimientoCuentas(cuenta.getCodigo(), cuenta.getDescripcion(), saldoAnterior);
                reporte.setDetalleReporteMovimientoCuentas(new ArrayList<>());

                if (detallesTransacciones != null || detalleComprobantePago != null) {
                    if (detallesTransacciones.size() > 0) {
                        for (int y = 0; y < detallesTransacciones.size(); y++) {
                            if (y == 0) {
                                detallesTransacciones.get(y).setSaldoAnterior(saldoAnterior.add(detallesTransacciones.get(y).getDebe())
                                        .subtract(detallesTransacciones.get(y).getHaber()));
                            }
                            if (detallesTransacciones.get(y).getSaldoAnterior() == null) {
                                detallesTransacciones.get(y).setSaldoAnterior(detallesTransacciones.get(y - 1).getSaldoAnterior().add(
                                        detallesTransacciones.get(y).getDebe()).subtract(detallesTransacciones.get(y).getHaber()));
                            }
                            DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(BigInteger.valueOf(detallesTransacciones.get(y).getIdContDiarioGeneral().getNumRegistro()),
                                    detallesTransacciones.get(y).getIdContDiarioGeneral().getTipo().getDescripcion(),
                                    Utils.dateFormatPattern("dd-MM-YYYY", detallesTransacciones.get(y).getIdContDiarioGeneral().getFechaRegistro()), null, detallesTransacciones.get(y).getIdContDiarioGeneral().getBeneficiario(), detallesTransacciones.get(y).getIdContDiarioGeneral().getDescripcion(),
                                    detallesTransacciones.get(y).getDebe(), detallesTransacciones.get(y).getHaber(),
                                    detallesTransacciones.get(y).getSaldoAnterior());
                            reporte.setTotalDebe(reporte.getTotalDebe().add(detalle.getDebe()));
                            reporte.setTotalHaber(reporte.getTotalHaber().add(detalle.getHaber()));
                            reporte.setTotalSaldo(null);
                            reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                        }
                    }
                    if (detalleComprobantePago.size() > 0 && !reporte.getDetalleReporteMovimientoCuentas().isEmpty()) {
                        for (int y = 0; y < detalleComprobantePago.size(); y++) {
                            if (y == 0) {
                                detalleComprobantePago.get(y).setSaldoAnterior(
                                        reporte.getDetalleReporteMovimientoCuentas().get(reporte.getDetalleReporteMovimientoCuentas().size() - 1).getSaldo()
                                                .add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                                .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            if (detalleComprobantePago.get(y).getSaldoAnterior() == null) {
                                detalleComprobantePago.get(y).setSaldoAnterior(detalleComprobantePago.get(y - 1).getSaldoAnterior()
                                        .add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                        .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? null : BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getNumRegistro()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? "" : detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getTipo().getDescripcion(),
                                    Utils.dateFormatPattern("dd-MM-YYYY", detalleComprobantePago.get(y).getIdContComprobantePago().getFechaRegistro()),
                                    BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getNumRegistro()),
                                    //                                    contBeneficiarioComprobantePagoService.getNombresBeneficiariosComprobantesByIdComprobante(detalleComprobantePago.get(y).getIdContComprobantePago().getId()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getBeneficiarios(),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getDescripcion(),
                                    detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getDebe().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getHaber().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getSaldoAnterior().setScale(2, RoundingMode.HALF_EVEN));
                            reporte.setTotalDebe(reporte.getTotalDebe().add(detalle.getDebe()));
                            reporte.setTotalHaber(reporte.getTotalHaber().add(detalle.getHaber()));
                            reporte.setTotalSaldo(null);
                            reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                        }
                    } else if (detalleComprobantePago.size() > 0 && reporte.getDetalleReporteMovimientoCuentas().isEmpty()) {
                        for (int y = 0; y < detalleComprobantePago.size(); y++) {
                            if (y == 0) {
                                detalleComprobantePago.get(y).setSaldoAnterior(saldoAnterior.add(detalleComprobantePago.get(y).getDebe() == null
                                        ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                        .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            if (detalleComprobantePago.get(y).getSaldoAnterior() == null) {
                                detalleComprobantePago.get(y).setSaldoAnterior(detalleComprobantePago.get(y - 1).getSaldoAnterior()
                                        .add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                        .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? null : BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getNumRegistro()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? "" : detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getTipo().getDescripcion(),
                                    Utils.dateFormatPattern("dd-MM-YYYY", detalleComprobantePago.get(y).getIdContComprobantePago().getFechaRegistro()),
                                    BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getNumRegistro()),
                                    //                                    contBeneficiarioComprobantePagoService.getNombresBeneficiariosComprobantesByIdComprobante(detalleComprobantePago.get(y).getIdContComprobantePago().getId()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getDescripcion(),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getBeneficiarios(),
                                    detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getDebe().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getHaber().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getSaldoAnterior().setScale(2, RoundingMode.HALF_EVEN));
                            reporte.setTotalDebe(reporte.getTotalDebe().add(detalle.getDebe()));
                            reporte.setTotalHaber(reporte.getTotalHaber().add(detalle.getHaber()));
                            reporte.setTotalSaldo(null);
                            reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                        }
                    } else if (detalleComprobantePago.isEmpty() && reporte.getDetalleReporteMovimientoCuentas().isEmpty()) {
                        DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(null, "",
                                "", null, "", "",
                                BigDecimal.ZERO, BigDecimal.ZERO,
                                saldoAnterior);
                        reporte.setTotalSaldo(detalle.getSaldo());
                        reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                    }
                    return reporte;
                }
            }
        }
        return reporte;
    }

    public List<ReporteMovimientoCuentas> llenarLisDetalleTransaccion(int aux, int aux2, Date fechaDesde, Date fechaHasta) {
        List<ReporteMovimientoCuentas> reportesCuentas = new ArrayList<>();
        for (int x = aux; x < contCuentas.size() && (aux <= x && aux2 >= x); x++) {
            if (contCuentas.get(x).getMovimiento()) {
                detallesTransacciones = contDiarioGeneralDetalleService.findAllDiarioGeneralDetalleByIdContCuentasAndFechaRegistro(contCuentas.get(x).getId(), fechaDesde, fechaHasta);
                detalleComprobantePago = contDiarioGeneralDetalleService.findAllDiarioGeneralDetalleOfDetalleComprobantePagoByIdContCuentasAndFechaRegistro(contCuentas.get(x).getId(), fechaDesde, fechaHasta);
                ContSaldoInicial saldoInicial = contSaldoInicialService.findContSaldoInicialByIdCuentaAndPeriodo(contCuentas.get(x), opcionBusqueda.getAnio());
                BigDecimal saldoAnterior;
                if (saldoInicial != null) {
                    saldoAnterior = (saldoInicial.getSaldoDebe() == null ? BigDecimal.ZERO
                            : saldoInicial.getSaldoDebe()).subtract(saldoInicial.getSaldoHaber() == null ? BigDecimal.ZERO
                            : saldoInicial.getSaldoHaber());
                } else {
                    saldoAnterior = BigDecimal.ZERO;
                }
                ReporteMovimientoCuentas reporte = new ReporteMovimientoCuentas(contCuentas.get(x).getCodigo(),
                        contCuentas.get(x).getDescripcion(), saldoAnterior);
                reporte.setDetalleReporteMovimientoCuentas(new ArrayList<>());
                if (detallesTransacciones != null || detalleComprobantePago != null) {
                    if (detallesTransacciones.size() > 0) {
                        for (int y = 0; y < detallesTransacciones.size(); y++) {
                            if (y == 0) {
                                detallesTransacciones.get(y).setSaldoAnterior(saldoAnterior.add(detallesTransacciones.get(y).getDebe())
                                        .subtract(detallesTransacciones.get(y).getHaber()));
                            }
                            if (detallesTransacciones.get(y).getSaldoAnterior() == null) {
                                detallesTransacciones.get(y).setSaldoAnterior(detallesTransacciones.get(y - 1).getSaldoAnterior()
                                        .add(detallesTransacciones.get(y).getDebe()).subtract(detallesTransacciones.get(y).getHaber()));
                            }
//                            System.out.println("detalle transacción" + detallesTransacciones.get(y));
                            DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(BigInteger.valueOf(detallesTransacciones.get(y).getIdContDiarioGeneral().getNumRegistro()),
                                    detallesTransacciones.get(y).getIdContDiarioGeneral().getTipo().getDescripcion(),
                                    Utils.dateFormatPattern("dd-MM-YYYY", detallesTransacciones.get(y).getIdContDiarioGeneral().getFechaRegistro()), null, detallesTransacciones.get(y).getIdContDiarioGeneral().getBeneficiario(), detallesTransacciones.get(y).getIdContDiarioGeneral().getDescripcion(),
                                    detallesTransacciones.get(y).getDebe().setScale(2, RoundingMode.HALF_EVEN),
                                    detallesTransacciones.get(y).getHaber().setScale(2, RoundingMode.HALF_EVEN),
                                    detallesTransacciones.get(y).getSaldoAnterior().setScale(2, RoundingMode.HALF_EVEN));
                            reporte.setTotalDebe(reporte.getTotalDebe().add(detalle.getDebe()));
                            reporte.setTotalHaber(reporte.getTotalHaber().add(detalle.getHaber()));
                            reporte.setTotalSaldo(null);
                            reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                        }
                    }
                    if (detalleComprobantePago.size() > 0 && !reporte.getDetalleReporteMovimientoCuentas().isEmpty()) {
                        for (int y = 0; y < detalleComprobantePago.size(); y++) {
                            if (y == 0) {
                                detalleComprobantePago.get(y).setSaldoAnterior(
                                        reporte.getDetalleReporteMovimientoCuentas().get(reporte.getDetalleReporteMovimientoCuentas().size() - 1).getSaldo()
                                                .add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                                .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            if (detalleComprobantePago.get(y).getSaldoAnterior() == null) {
                                detalleComprobantePago.get(y).setSaldoAnterior(detalleComprobantePago.get(y - 1).getSaldoAnterior()
                                        .add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                        .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? null : BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getNumRegistro()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? "" : detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getTipo().getDescripcion(),
                                    Utils.dateFormatPattern("dd-MM-YYYY", detalleComprobantePago.get(y).getIdContComprobantePago().getFechaRegistro()),
                                    BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getNumRegistro()),
                                    //                                    contBeneficiarioComprobantePagoService.getNombresBeneficiariosComprobantesByIdComprobante(detalleComprobantePago.get(y).getIdContComprobantePago().getId()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getBeneficiarios(),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getDescripcion(),
                                    detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getDebe().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getHaber().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getSaldoAnterior());
                            reporte.setTotalDebe(reporte.getTotalDebe().add(detalle.getDebe()));
                            reporte.setTotalHaber(reporte.getTotalHaber().add(detalle.getHaber()));
                            reporte.setTotalSaldo(null);
                            reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                        }
                    } else if (detalleComprobantePago.size() > 0 && reporte.getDetalleReporteMovimientoCuentas().isEmpty()) {
                        for (int y = 0; y < detalleComprobantePago.size(); y++) {
                            if (y == 0) {
                                detalleComprobantePago.get(y).setSaldoAnterior(saldoAnterior.add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO
                                        : detalleComprobantePago.get(y).getDebe())
                                        .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            if (detalleComprobantePago.get(y).getSaldoAnterior() == null) {
                                detalleComprobantePago.get(y).setSaldoAnterior(detalleComprobantePago.get(y - 1).getSaldoAnterior()
                                        .add(detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getDebe())
                                        .subtract(detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO : detalleComprobantePago.get(y).getHaber()));
                            }
                            DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? null : BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getNumRegistro()),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral() == null
                                    ? "" : detalleComprobantePago.get(y).getIdContComprobantePago().getIdContDiarioGeneral().getTipo().getDescripcion(),
                                    Utils.dateFormatPattern("dd-MM-YYYY", detalleComprobantePago.get(y).getIdContComprobantePago().getFechaRegistro()),
                                    BigInteger.valueOf(detalleComprobantePago.get(y).getIdContComprobantePago().getNumRegistro()),
                                    //                                    contBeneficiarioComprobantePagoService.getNombresBeneficiariosComprobantesByIdComprobante(detalleComprobantePago.get(y).getIdContComprobantePago().getId()), 
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getDescripcion(),
                                    detalleComprobantePago.get(y).getIdContComprobantePago().getBeneficiarios(),
                                    detalleComprobantePago.get(y).getDebe() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getDebe().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getHaber() == null ? BigDecimal.ZERO
                                    : detalleComprobantePago.get(y).getHaber().setScale(2, RoundingMode.HALF_EVEN),
                                    detalleComprobantePago.get(y).getSaldoAnterior().setScale(2, RoundingMode.HALF_EVEN));
                            reporte.setTotalDebe(reporte.getTotalDebe().add(detalle.getDebe()));
                            reporte.setTotalHaber(reporte.getTotalHaber().add(detalle.getHaber()));
                            reporte.setTotalSaldo(null);
                            reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                        }
                    } else if (detalleComprobantePago.isEmpty() && reporte.getDetalleReporteMovimientoCuentas().isEmpty()) {
                        DetalleReporteMovimientoCuenta detalle = new DetalleReporteMovimientoCuenta(null, "",
                                "", null, "", "",
                                BigDecimal.ZERO, BigDecimal.ZERO,
                                saldoAnterior);
                        reporte.setTotalSaldo(detalle.getSaldo());
                        reporte.getDetalleReporteMovimientoCuentas().add(detalle);
                    }
                }
                reportesCuentas.add(reporte);
            }
        }
        return reportesCuentas;
    }

    public void enviarReporte(List<ReporteMovimientoCuentas> reportesCuentas, Boolean excel) {
        if (!reportesCuentas.isEmpty()) {
            BigDecimal totalDebe = BigDecimal.ZERO;
            BigDecimal totalHaber = BigDecimal.ZERO;
            //BigDecimal totalSaldo;
            List<ReporteMovimientoCuentas> reporteNew = new ArrayList<>();
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");

            for (ReporteMovimientoCuentas r : reportesCuentas) {
                if (!r.getTotalDebe().equals(BigDecimal.ZERO) || !r.getTotalHaber().equals(BigDecimal.ZERO)) {
                    if (!r.getTotalDebe().equals(0.00) || !r.getTotalHaber().equals(0.00)) {
                        totalDebe = totalDebe.add(r.getTotalDebe());
                        totalHaber = totalHaber.add(r.getTotalHaber());
                        if (Utils.isNotEmpty(r.getDetalleReporteMovimientoCuentas())) {
                            try {
                                Comparator<DetalleReporteMovimientoCuenta> c = (DetalleReporteMovimientoCuenta o1, DetalleReporteMovimientoCuenta o2) -> {
                                    try {
                                        return f.parse(o1.getFecha()).compareTo(f.parse(o2.getFecha()));
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ReporteMovimientoCuentaController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return 0;
                                };
                                Collections.sort(r.getDetalleReporteMovimientoCuentas(), c);
                            } catch (Exception e) {
                                Logger.getLogger(ReporteMovimientoCuentaController.class.getName()).log(Level.SEVERE, CODIGOCUENTAITEM, e);
                            }
                        }
                    }
                    reporteNew.add(r);
                }
            }
            if (excel) {
                servletSession.setContentType("xlsx");
            }
            //totalSaldo = totalDebe.subtract(totalHaber);
            servletSession.addParametro("desdeCuenta", cuentaDesde.getCodigo() + " - " + cuentaDesde.getDescripcion());
            servletSession.addParametro("hastaCuenta", cuentaHasta.getCodigo() + " - " + cuentaHasta.getDescripcion());
            servletSession.addParametro("fechaDesde", Utils.dateFormatPattern("dd/MM/YYYY", fechaDesde));
            servletSession.addParametro("fechaHasta", Utils.dateFormatPattern("dd/MM/YYYY", fechaHasta));
            servletSession.addParametro("totales_debe", totalDebe.setScale(2, RoundingMode.HALF_EVEN));
            servletSession.addParametro("totales_haber", totalHaber.setScale(2, RoundingMode.HALF_EVEN));
            servletSession.addParametro("totales_saldo", null);
            servletSession.setNombreSubCarpeta("movimientoCuentas");
            servletSession.setNombreReporte("reporteMovimientoCuenta");
            servletSession.setDataSource(reporteNew);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void asignarRangoPeriodos() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(opcionBusqueda.getAnio(), 0, 1);
        fechaDesde = calendar.getTime();
        if (opcionBusqueda.getAnio().intValue() == Utils.getAnio(new Date()).intValue()) {
            fechaHasta = new Date();
        } else {
            calendar.set(opcionBusqueda.getAnio(), 11, 31);
            fechaHasta = calendar.getTime();
        }
    }

    public void updateCuentaHasta() {
        cuentaHasta = Utils.clone(cuentaDesde);
    }

    public String styleNegrilla(ContCuentas cuenta) {
        if (cuenta.getMovimiento().equals(Boolean.FALSE)) {
            return "font-weight: bold";
        }
        return "";
    }

//<editor-fold defaultstate="collapsed" desc="GETTERS AND SETTERS">
    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public ContCuentas getCuentaDesde() {
        return cuentaDesde;
    }

    public void setCuentaDesde(ContCuentas cuentaDesde) {
        this.cuentaDesde = cuentaDesde;
    }

    public ContCuentas getCuentaHasta() {
        return cuentaHasta;
    }

    public void setCuentaHasta(ContCuentas cuentaHasta) {
        this.cuentaHasta = cuentaHasta;
    }
//
//    public List<CuentaContable> getCuentasContables() {
//        return cuentasContables;
//    }
//
//    public void setCuentasContables(List<CuentaContable> cuentasContables) {
//        this.cuentasContables = cuentasContables;
//    }

    public List<ContCuentas> getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(List<ContCuentas> contCuentas) {
        this.contCuentas = contCuentas;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
//</editor-fold>

}
