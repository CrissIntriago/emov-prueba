/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPagoDetalle;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Services.NotaCreditoServices;
import com.gestionTributaria.models.tipoPagos.OtrosFormas;
import com.gestionTributaria.models.tipoPagos.PagoCheque;
import com.gestionTributaria.models.tipoPagos.PagoNotaCredito;
import com.gestionTributaria.models.tipoPagos.PagoTarjetaCredito;
import com.gestionTributaria.models.tipoPagos.PagoTransferencia;
import com.origami.sigef.common.util.JsfUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author ORIGAMI2
 */
public class PagoTituloReporteModel {

    private static final Logger LOG = Logger.getLogger(PagoTituloReporteModel.class.getName());

    private static final long serialVersionUID = 1L;
    
    @EJB
    private NotaCreditoServices notaCreditoService;
    
    private BigDecimal valorRecibido;
    private BigDecimal valorCobrar;
    private BigDecimal valorMinimo;
    private BigDecimal valorMinimoPagar;
    private BigDecimal valorCambio;
    private BigDecimal valorSumado;
    private BigDecimal valorDebitado;

    private PagoCheque pagoCheque;
    private OtrosFormas otrasFormas;
    private PagoNotaCredito pagoNotaCredio;
    private PagoTarjetaCredito pagoTarjetaCredito;
    private PagoTransferencia pagoTransferencia;

    protected List<PagoCheque> listPagoCheque;
    protected List<PagoNotaCredito> listPagoNotaCredio;
    protected List<PagoTarjetaCredito> listPagoTarjetaCredito;
    protected List<PagoTransferencia> listPagoTransferencia;

    /*Notas de Credito*/
//    protected List<NotaCredito> listPagoNota;
//    private NotaCredito nota;
    protected List<NotasCredito> listPagoNota;
    protected List<NotasCredito> notaCreditoMov;
    private NotasCredito nota;

    private BigDecimal valorTotalEfectivo;
    private BigDecimal valorTotalCruceCuenta;
    private BigDecimal valorTotalNotasCredito;
    private BigDecimal valorTotalCheques;
    private BigDecimal valorTotalTarjetas;
    private BigDecimal valorTotalTransferencias;
    private BigDecimal valorTotal;
    //private BigDecimal valorCoactiva;

    private String observacion;
    private BigDecimal valorSaldoPago;
    private BigDecimal valorSaldoPagoFinal;

    public PagoTituloReporteModel() {
        this.valorRecibido = new BigDecimal("0.00");
        this.valorSumado = new BigDecimal("0.00");
        this.valorCobrar = new BigDecimal("0.00");
        this.valorCambio = new BigDecimal("0.00");
        this.valorDebitado = BigDecimal.ZERO;
        this.listPagoCheque = new ArrayList<>();
        this.listPagoNotaCredio = new ArrayList<>();
        this.listPagoTarjetaCredito = new ArrayList<>();
        this.listPagoTransferencia = new ArrayList<>();
        this.valorTotalEfectivo = new BigDecimal("0.00");
        this.valorTotalCruceCuenta = new BigDecimal("0.00");
        this.valorTotalNotasCredito = new BigDecimal("0.00");
        this.valorTotalCheques = new BigDecimal("0.00");
        this.valorTotalTarjetas = new BigDecimal("0.00");
        this.valorTotalTransferencias = new BigDecimal("0.00");
        this.valorTotal = new BigDecimal("0.00");
        this.valorSaldoPagoFinal = new BigDecimal("0.00");
        //this.valorCoactiva = new BigDecimal("0.00");
        this.pagoCheque = new PagoCheque();
        this.otrasFormas = new OtrosFormas();
        this.nota = new NotasCredito();
        this.pagoNotaCredio = new PagoNotaCredito();
        this.pagoTarjetaCredito = new PagoTarjetaCredito();
        this.pagoTransferencia = new PagoTransferencia();
        this.listPagoNota = new LinkedList<>();
        this.notaCreditoMov = new LinkedList<>();
    }

    public PagoTituloReporteModel(BigDecimal valorSaldoPago, Boolean variosPagos, PagoNotaCredito pagoNotaCredio, PagoCheque pagoCheque, PagoTarjetaCredito pagoTarjetaCredito, PagoTransferencia pagoTransferencia) {
        this.valorRecibido = new BigDecimal("0.00");
        this.valorDebitado = BigDecimal.ZERO;
        this.nota = new NotasCredito();
        this.valorCobrar = valorSaldoPago;
        this.valorCambio = new BigDecimal("0.00");
        this.listPagoCheque = new ArrayList<>();
        this.listPagoNotaCredio = new ArrayList<>();
        this.listPagoTarjetaCredito = new ArrayList<>();
        this.listPagoTransferencia = new ArrayList<>();
        this.valorTotalEfectivo = new BigDecimal("0.00");
        this.valorTotalNotasCredito = new BigDecimal("0.00");
        this.valorTotalCheques = new BigDecimal("0.00");
        this.valorTotalTarjetas = new BigDecimal("0.00");
        this.valorTotalTransferencias = new BigDecimal("0.00");
        this.valorTotal = new BigDecimal("0.00");
        //this.valorCoactiva = new BigDecimal("0.00");
        if (variosPagos) {
            this.pagoCheque = pagoCheque;
            this.otrasFormas = new OtrosFormas();
            this.pagoNotaCredio = pagoNotaCredio;
            this.pagoTarjetaCredito = pagoTarjetaCredito;
            this.pagoTransferencia = pagoTransferencia;
        } else {
            this.pagoCheque = new PagoCheque();
            this.otrasFormas = new OtrosFormas();
            this.pagoNotaCredio = new PagoNotaCredito();
            this.pagoTarjetaCredito = new PagoTarjetaCredito();
            this.pagoTransferencia = new PagoTransferencia();
            this.nota = new NotasCredito();
        }
        this.valorSaldoPago = valorSaldoPago;
        this.valorSaldoPagoFinal = valorSaldoPago;
        this.listPagoNota = new LinkedList<>();
        this.notaCreditoMov = new LinkedList<>();
        this.valorTotalCruceCuenta = new BigDecimal("0.00");
    }

    public void calcularEfectivo() {

        if (valorRecibido != null) {
            valorRecibido = valorRecibido.setScale(2, RoundingMode.UP);
        }
        if (valorCobrar != null) {
            valorCobrar = valorCobrar.setScale(2, RoundingMode.UP);
        }
        if (valorRecibido != null && valorCobrar != null) {
            if (valorRecibido.compareTo(valorCobrar) > 0) {
                valorTotalEfectivo = valorCobrar;
            } else {
                valorTotalEfectivo = valorRecibido;
            }
            valorCambio = valorRecibido.subtract(valorCobrar).setScale(2, RoundingMode.UP);
            if(valorCambio.compareTo(BigDecimal.ZERO)<0){
                valorCambio = BigDecimal.ZERO;
            }

        } else {
            valorTotalEfectivo = new BigDecimal("0.00");
            valorCambio = BigDecimal.ZERO;
        }
        this.calcularTotalPago();

    }

    public void calcularCruceCuenta() {
        if (valorTotalCruceCuenta != null) {
            valorTotalCruceCuenta = valorTotalCruceCuenta.setScale(2, RoundingMode.UP);
            valorCambio = valorTotalCruceCuenta.subtract(valorCobrar).setScale(2, RoundingMode.UP);
            if(valorCambio.compareTo(BigDecimal.ZERO)<0){
                valorCambio = BigDecimal.ZERO;
            }
        } else {
            valorTotalCruceCuenta = new BigDecimal("0.00");
        }
        this.calcularTotalPago();
    }

    public int cantidadTipoPagos() {
        int i = 0;
        if (valorTotalEfectivo.compareTo(new BigDecimal("0.00")) > 0) {
            i++;
        }
        if (valorTotalCheques.compareTo(new BigDecimal("0.00")) > 0) {
            i++;
        }
        if (valorTotalNotasCredito.compareTo(new BigDecimal("0.00")) > 0) {
            i++;
        }
        if (valorTotalTarjetas.compareTo(new BigDecimal("0.00")) > 0) {
            i++;
        }
        if (valorTotalTransferencias.compareTo(new BigDecimal("0.00")) > 0) {
            i++;
        }
        if (valorTotalCruceCuenta.compareTo(new BigDecimal("0.00")) > 0) {
            i++;
        }
        return i;
    }

    public void calcularTotalPago() {
        System.out.println("valor total>>"+valorTotal+" valor a cobrar>>"+valorCobrar);
        valorTotal = valorTotalEfectivo.add(valorTotalNotasCredito).add(valorTotalTarjetas).add(valorTotalCheques).add(valorTotalTransferencias).add(valorTotalCruceCuenta);
        System.out.println("valor total>>"+valorTotal+" valor a cobrar>>"+valorCobrar);
        if (valorSaldoPago == null) {
            valorSaldoPago = BigDecimal.ZERO;
        }
        if (valorSaldoPago.subtract(valorTotal).compareTo(new BigDecimal("0.00")) < 0) {
            valorSaldoPagoFinal = BigDecimal.ZERO.setScale(2, RoundingMode.UP);
        } else {
            valorSaldoPagoFinal = valorSaldoPago.subtract(valorTotal);
        }
//        if (valorTotalCheques.compareTo(new BigDecimal("0.00")) > 0 
//                || valorTotalNotasCredito.compareTo(new BigDecimal("0.00")) > 0
//                || valorTotalTarjetas.compareTo(new BigDecimal("0.00")) > 0 
//                || valorTotalTransferencias.compareTo(new BigDecimal("0.00")) > 0) {
//            valorCobrar = valorSaldoPagoFinal;
//        }

    }

    public void agregarPago(Integer tipoPago) {
        System.out.println("valor>>>"+pagoTarjetaCredito.getValor());
        switch (tipoPago) {
            case 2:
            case 9:
                if (pagoTarjetaCredito != null && pagoTarjetaCredito.getValor() != null 
                        && pagoTarjetaCredito.getValor().compareTo(new BigDecimal("0.00")) > 0) {
                    if (!listPagoTarjetaCredito.contains(pagoTarjetaCredito)) {
                        PagoTarjetaCredito ptc = new PagoTarjetaCredito();
                        ptc.setAutorizacion(pagoTarjetaCredito.getAutorizacion());
                        ptc.setBanco(pagoTarjetaCredito.getBanco());
                        ptc.setBaucher(pagoTarjetaCredito.getBaucher());
                        ptc.setFechaCaducidad(pagoTarjetaCredito.getFechaCaducidad());
                        ptc.setNombreTitular(pagoTarjetaCredito.getNombreTitular());
                        ptc.setNumTarjeta(pagoTarjetaCredito.getNumTarjeta());
                        ptc.setValor(pagoTarjetaCredito.getValor());
                        ptc.setTipoPago(tipoPago);
                        listPagoTarjetaCredito.add(ptc);
                        valorTotalTarjetas = valorTotalTarjetas.add(ptc.getValor());
                        pagoTarjetaCredito = new PagoTarjetaCredito();
                    } else {
                        JsfUtil.addWarningMessage("Verifique los datos.", "");
                    }
                } else {
                    JsfUtil.addWarningMessage("Para agregar el detalle, debe ingresar los parametros y el valor debe ser mayor a CERO.", "");
                }
                break;
            /*NOTAS DE CREDITO*/
            case 3:
                if (nota != null && this.valorDebitado != null && nota.getSaldo()!= null && this.valorDebitado.compareTo(nota.getSaldo()) <= 0) {
                    if (!listPagoNota.contains(nota)) {
                        nota.setSaldo(nota.getSaldo().subtract(this.valorDebitado));
                        nota.setValorDebitado(this.valorDebitado);
                        listPagoNota.add(nota);
                        valorTotalNotasCredito = valorTotalNotasCredito.add(this.valorDebitado);                    
                        nota = new NotasCredito();
                    }else{
                        JsfUtil.addWarningMessage("ERROR","Nota de Credito ya fue Agregada...");
                    }
                } else {
                    JsfUtil.addWarningMessage("Para agregar el detalle, debe ingresar los parametros y el valor debe ser mayor a CERO.", "");
                }
                break;
            case 4:
                if (pagoCheque != null && pagoCheque.getValor() != null && pagoCheque.getValor().compareTo(new BigDecimal("0.00")) > 0) {
                    if (!listPagoCheque.contains(pagoCheque)) {
                        PagoCheque pch = new PagoCheque();
                        pch.setBanco(pagoCheque.getBanco());
                        pch.setNumCheque(pagoCheque.getNumCheque());
                        pch.setNumCuenta(pagoCheque.getNumCuenta());
                        pch.setContribuyente(pagoCheque.getContribuyente());
                        pch.setValor(pagoCheque.getValor());
                        pch.setTipoPago(tipoPago);
                        listPagoCheque.add(pch);
                        valorTotalCheques = valorTotalCheques.add(pch.getValor());

                        pagoCheque = new PagoCheque();
                    } else {
                        JsfUtil.addWarningMessage("Verifique los datos.", "");
                    }
                } else {
                    JsfUtil.addWarningMessage("Para agregar el detalle, debe ingresar los parametros y el valor debe ser mayor a CERO.", "");
                }
                break;
            case 5:
                if (pagoTransferencia != null && pagoTransferencia.getValor() != null && pagoTransferencia.getValor().compareTo(new BigDecimal("0.00")) > 0) {
                    if (!listPagoTransferencia.contains(pagoTransferencia)) {
                        PagoTransferencia pt = new PagoTransferencia();
                        pt.setBanco(pagoTransferencia.getBanco());
                        pt.setFecha(pagoTransferencia.getFecha());
                        pt.setNumTransferencia(pagoTransferencia.getNumTransferencia());
                        pt.setTipoPago(tipoPago);
                        pt.setValor(pagoTransferencia.getValor());
                        listPagoTransferencia.add(pt);
                        valorTotalTransferencias = valorTotalTransferencias.add(pt.getValor());
                        //this.setValorCobrar(valorCobrar.subtract(valorTotalTransferencias));
                        pagoTransferencia = new PagoTransferencia();
                    } else {
                        JsfUtil.addWarningMessage("Verifique los datos.", "");
                    }
                } else {
                    JsfUtil.addWarningMessage("Para agregar el detalle, debe ingresar los parametros y el valor debe ser mayor a CERO.", "");
                }
                break;
            default:
                break;
        }
        this.calcularTotalPago();
    }

    public FinaRenPago realizarPago(FinaRenLiquidacion liquidacion, BigInteger numeroComprobante) {
        FinaRenPago pago = new FinaRenPago();
        FinaRenPagoDetalle detalle;
        List<FinaRenPagoDetalle> listDetPago = new ArrayList<>();
        BigDecimal valorPago = new BigDecimal("0.00");
        final BigDecimal valorX = valorTotalTransferencias;
        try {
            if (valorTotalEfectivo.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0
                    && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                detalle = new FinaRenPagoDetalle();
                if (liquidacion.getPagoFinal().compareTo(valorTotalEfectivo) >= 0) {
//                    System.out.println("/*** EL VALOR DE LA LIQUIDACION ES MAYOR O IGUAL AL VALOR RECAUDADO (ABONO)");
                    detalle.setValor(this.valorTotalEfectivo);
                } else {
//                    System.out.println("/*** EL VALOR DE LA RECAUDACION ES MAYOR AL DE LA LIQUIDACION (VARIOS PAGOS)");
                    //SE ACTULIZA EL VALOR TOTAL DEL MODELO DEL PAGO
                    detalle.setValor(liquidacion.getPagoFinal());//EL SALDO ES DEL VALOR ORIGINAL - DEBE EXITIR UN TRASIEN CON UN VALOR TOTAL
                }
//                System.out.println("entro a valores en efectibo");
                valorTotalEfectivo = valorTotalEfectivo.subtract(detalle.getValor());
                this.actualizarValorTotal(detalle.getValor());
                liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                detalle.setTipoPago(1);
                detalle.setTcTitular(liquidacion.getComprador() != null ? liquidacion.getComprador().getNombreCompleltoSql():liquidacion.getNombreComprador());
                listDetPago.add(detalle);
                //actualizarValoresTotal(detalle.getValor());
            }

            if (valorTotalCheques.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                for (PagoCheque pch : listPagoCheque) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(pch.getValor()) >= 0) {
                        detalle.setValor(pch.getValor());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalCheques = valorTotalCheques.subtract(detalle.getValor());
                    pch.setValor(pch.getValor().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setTcBanco(pch.getBanco());
                    detalle.setTcNumTarjeta(pch.getNumCheque());
                    detalle.setTcAutorizacion(pch.getNumCuenta());
                    detalle.setTipoPago(pch.getTipoPago());
                    detalle.setTcTitular(pch.getContribuyente());
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }
            }
            /*NOTAS DE CREDITO JC*/

            if (valorTotalNotasCredito.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                for (NotasCredito pnc : listPagoNota) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(pnc.getValorDebitado()) >= 0) {
                        detalle.setValor(pnc.getValorDebitado());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalNotasCredito = valorTotalNotasCredito.subtract(detalle.getValor());
                    pnc.setSaldo(pnc.getSaldo().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setTcNumTarjeta(pnc.getNotaCredito()!= null ? pnc.getNotaCredito().getCodigoComprobante():pnc.getCodigo());
                    detalle.setTcFechaCaducidad(pnc.getFechaIngreso());
                    detalle.setTcTitular(pnc.getContribuyente()!= null ? pnc.getContribuyente().getNombreCompleltoSql():"");
                    detalle.setTipoPago(3);
                    this.notaCreditoMov.add(pnc);
                    listDetPago.add(detalle);
//                    System.out.println("editando nota credito>>>>"+nc+" valor >>>"+nc.getValor()+" saldo>>"+nc.getSaldo());
//                    notaCreditoService.edit(nc);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }
            }
            if (valorTotalTarjetas.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                for (PagoTarjetaCredito ptc : listPagoTarjetaCredito) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(ptc.getValor()) >= 0) {
                        detalle.setValor(ptc.getValor());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalTarjetas = valorTotalTarjetas.subtract(detalle.getValor());
                    ptc.setValor(ptc.getValor().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setTcAutorizacion(ptc.getAutorizacion());
                    detalle.setTcBanco(ptc.getBanco());
                    detalle.setTcBaucher(ptc.getBaucher());
                    detalle.setTcFechaCaducidad(ptc.getFechaCaducidad());
                    detalle.setTcNumTarjeta(ptc.getNumTarjeta());
                    detalle.setTcTitular(ptc.getNombreTitular());
                    detalle.setTipoPago(ptc.getTipoPago());
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }
            }
            if (valorTotalTransferencias.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {

                for (PagoTransferencia pt : listPagoTransferencia) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(pt.getValor()) >= 0) {
                        detalle.setValor(pt.getValor());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalTransferencias = valorTotalTransferencias.subtract(detalle.getValor());
                    pt.setValor(pt.getValor().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setTcBanco(pt.getBanco());
                    detalle.setTcFechaCaducidad(pt.getFecha());
                    detalle.setTcNumTarjeta(pt.getNumTransferencia());
                    detalle.setTipoPago(pt.getTipoPago());
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }

            }
            if (valorTotalCruceCuenta.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                detalle = new FinaRenPagoDetalle();
                if (liquidacion.getPagoFinal().compareTo(valorTotalCruceCuenta) >= 0) {
                    //System.out.println("/*** EL VALOR DE LA LIQUIDACION ES MAYOR O IGUAL AL VALOR RECAUDADO (ABONO)");
                    detalle.setValor(this.valorTotalCruceCuenta);
                } else {
                    //System.out.println("/*** EL VALOR DE LA RECAUDACION ES MAYOR AL DE LA LIQUIDACION (VARIOS PAGOS)");
                    //SE ACTULIZA EL VALOR TOTAL DEL MODELO DEL PAGO
                    detalle.setValor(liquidacion.getPagoFinal());//EL SALDO ES DEL VALOR ORIGINAL - DEBE EXITIR UN TRASIEN CON UN VALOR TOTAL
                }
                valorTotalCruceCuenta = valorTotalCruceCuenta.subtract(detalle.getValor());
                this.actualizarValorTotal(detalle.getValor());
                liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                detalle.setTcNumTarjeta(otrasFormas.getDescripcion());
                detalle.setTipoPago(6);
                listDetPago.add(detalle);
                //actualizarValoresTotal(detalle.getValor());
            }
            for (FinaRenPagoDetalle d : listDetPago) {
                valorPago = valorPago.add(d.getValor());
            }
            pago.setNumComprobante(numeroComprobante);
            pago.setRenPagoDetalles(listDetPago);
            pago.setValor(valorPago);
            pago.setObservacion(observacion);
        } catch (Exception e) {
            Logger.getLogger(PagoTituloReporteModel.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return pago;
    }

    public FinaRenPago realizarPago(FinaRenLiquidacion liquidacion) {
        FinaRenPago pago = new FinaRenPago();
        FinaRenPagoDetalle detalle;
        List<FinaRenPagoDetalle> listDetPago = new ArrayList<>();
        BigDecimal valorPago = new BigDecimal("0.00");
        final BigDecimal valorX = valorTotalTransferencias;
        try {
            if (valorTotalEfectivo.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0
                    && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                detalle = new FinaRenPagoDetalle();
                if (liquidacion.getPagoFinal().compareTo(valorTotalEfectivo) >= 0) {
                    System.out.println("/*** EL VALOR DE LA LIQUIDACION ES MAYOR O IGUAL AL VALOR RECAUDADO (ABONO)");
                    detalle.setValor(this.valorTotalEfectivo);
                } else {
                    System.out.println("/*** EL VALOR DE LA RECAUDACION ES MAYOR AL DE LA LIQUIDACION (VARIOS PAGOS)");
                    //SE ACTULIZA EL VALOR TOTAL DEL MODELO DEL PAGO
                    detalle.setValor(liquidacion.getPagoFinal());//EL SALDO ES DEL VALOR ORIGINAL - DEBE EXITIR UN TRASIEN CON UN VALOR TOTAL
                }
                System.out.println("entro a valores en efectibo");
                valorTotalEfectivo = valorTotalEfectivo.subtract(detalle.getValor());
                this.actualizarValorTotal(detalle.getValor());
                liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                detalle.setTipoPago(1);
                listDetPago.add(detalle);
                //actualizarValoresTotal(detalle.getValor());
            }

            if (valorTotalCruceCuenta.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                detalle = new FinaRenPagoDetalle();
                if (liquidacion.getPagoFinal().compareTo(valorTotalCruceCuenta) >= 0) {
                    //System.out.println("/*** EL VALOR DE LA LIQUIDACION ES MAYOR O IGUAL AL VALOR RECAUDADO (ABONO)");
                    detalle.setValor(this.valorTotalCruceCuenta);
                } else {
                    //System.out.println("/*** EL VALOR DE LA RECAUDACION ES MAYOR AL DE LA LIQUIDACION (VARIOS PAGOS)");
                    //SE ACTULIZA EL VALOR TOTAL DEL MODELO DEL PAGO
                    detalle.setValor(liquidacion.getPagoFinal());//EL SALDO ES DEL VALOR ORIGINAL - DEBE EXITIR UN TRASIEN CON UN VALOR TOTAL
                }
                valorTotalCruceCuenta = valorTotalCruceCuenta.subtract(detalle.getValor());
                this.actualizarValorTotal(detalle.getValor());
                liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                detalle.setTipoPago(6);
                listDetPago.add(detalle);
                //actualizarValoresTotal(detalle.getValor());
            }

            if (valorTotalCheques.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                for (PagoCheque pch : listPagoCheque) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(pch.getValor()) >= 0) {
                        detalle.setValor(pch.getValor());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalCheques = valorTotalCheques.subtract(detalle.getValor());
                    pch.setValor(pch.getValor().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setChBanco(pch.getBanco());
                    detalle.setChNumCheque(pch.getNumCheque());
                    detalle.setChNumCuenta(pch.getNumCuenta());
                    detalle.setTipoPago(pch.getTipoPago());
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }
            }
            /*NOTAS DE CREDITO JC*/

            if (valorTotalNotasCredito.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                for (NotasCredito pnc : listPagoNota) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(pnc.getValorDebitado()) >= 0) {
                        detalle.setValor(pnc.getValorDebitado());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalNotasCredito = valorTotalNotasCredito.subtract(detalle.getValor());
                    pnc.setSaldo(pnc.getSaldo().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    System.out.println("editando nota credito>>>>"+pnc.getCodigo()+" valor >>>"+pnc.getValor()+" saldo>>"+pnc.getSaldo());
                    notaCreditoService.edit(pnc);
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setNcNumCredito(pnc.getId().toString());
                    detalle.setNcFecha(new Date());
                    detalle.setTipoPago(3);
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }
            }
            if (valorTotalTarjetas.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {
                for (PagoTarjetaCredito ptc : listPagoTarjetaCredito) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(ptc.getValor()) >= 0) {
                        detalle.setValor(ptc.getValor());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalTarjetas = valorTotalTarjetas.subtract(detalle.getValor());
                    ptc.setValor(ptc.getValor().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setTcAutorizacion(ptc.getAutorizacion());
                    detalle.setTcBanco(ptc.getBanco());
                    detalle.setTcBaucher(ptc.getBaucher());
                    detalle.setTcFechaCaducidad(ptc.getFechaCaducidad());
                    detalle.setTcNumTarjeta(ptc.getNumTarjeta());
                    detalle.setTcTitular(ptc.getNombreTitular());
                    detalle.setTipoPago(ptc.getTipoPago());
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }
            }
            if (valorTotalTransferencias.compareTo(new BigDecimal("0.00")) > 0 && valorTotal.compareTo(new BigDecimal("0.00")) > 0 && liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) > 0) {

                for (PagoTransferencia pt : listPagoTransferencia) {
                    detalle = new FinaRenPagoDetalle();
                    if (liquidacion.getPagoFinal().compareTo(pt.getValor()) >= 0) {
                        detalle.setValor(pt.getValor());
                    } else {
                        detalle.setValor(liquidacion.getPagoFinal());
                    }
                    valorTotalTransferencias = valorTotalTransferencias.subtract(detalle.getValor());
                    pt.setValor(pt.getValor().subtract(detalle.getValor()));
                    this.actualizarValorTotal(detalle.getValor());
                    liquidacion.setPagoFinal(liquidacion.getPagoFinal().subtract(detalle.getValor()));
                    detalle.setTrBanco(pt.getBanco());
                    detalle.setTrFecha(pt.getFecha());
                    detalle.setTrNumTransferencia(pt.getNumTransferencia());
                    detalle.setTipoPago(pt.getTipoPago());
                    listDetPago.add(detalle);
                    if (liquidacion.getPagoFinal().compareTo(new BigDecimal("0.00")) <= 0) {
                        break;
                    }
                }

            }
            System.out.println("lista de pago--> " + listDetPago);
            for (FinaRenPagoDetalle d : listDetPago) {
                valorPago = valorPago.add(d.getValor());
            }
//            pago.setNumComprobante(numeroComprobante);
            pago.setRenPagoDetalles(listDetPago);
            pago.setValor(valorPago);
            pago.setObservacion(observacion);
        } catch (Exception e) {
            Logger.getLogger(PagoTituloReporteModel.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return pago;
    }

    public void eliminarPagoNCOK(NotasCredito nc) {
        listPagoNota.remove(nc);
        valorTotalNotasCredito = valorTotalNotasCredito.subtract(nc.getValorDebitado());
        valorSaldoPagoFinal = valorSaldoPagoFinal.add(nc.getValorDebitado());
        this.calcularTotalPago();
    }

    public void eliminarPagoCh(PagoCheque ch) {
        listPagoCheque.remove(ch);
        valorTotalCheques = valorTotalCheques.subtract(ch.getValor());
        valorSaldoPagoFinal = valorSaldoPagoFinal.add(ch.getValor());
        this.calcularTotalPago();
    }

    public void eliminarPagoTransferencia(PagoTransferencia t) {
        listPagoTransferencia.remove(t);
        valorTotalTransferencias = valorTotalTransferencias.subtract(t.getValor());
        valorSaldoPagoFinal = valorSaldoPagoFinal.add(t.getValor());
        this.calcularTotalPago();
    }

    public void eliminarPagoTC(PagoTarjetaCredito tc) {
        listPagoTarjetaCredito.remove(tc);
        valorTotalTarjetas = valorTotalTarjetas.subtract(tc.getValor());
        valorSaldoPagoFinal = valorSaldoPagoFinal.add(tc.getValor());
        this.calcularTotalPago();
    }

    public void eliminarPagoNC(PagoNotaCredito nc) {
        listPagoNotaCredio.remove(nc);
        valorTotalNotasCredito = valorTotalNotasCredito.subtract(nc.getValor());
        valorSaldoPagoFinal = valorSaldoPagoFinal.add(nc.getValor());
        this.calcularTotalPago();
    }

    public void actualizarValorTotal(BigDecimal valor) {
        valorTotal = valorTotal.subtract(valor);
        valorSaldoPagoFinal = valorSaldoPago.add(valorTotal);
    }
//<editor-fold defaultstate="collapsed" desc="GET AND SET">

    public BigDecimal getValorRecibido() {
        return valorRecibido;
    }

    public void setValorRecibido(BigDecimal valorRecibido) {
        this.valorRecibido = valorRecibido;
    }

    public List<NotasCredito> getNotaCreditoMov() {
        return notaCreditoMov;
    }

    public void setNotaCreditoMov(List<NotasCredito> notaCreditoMov) {
        this.notaCreditoMov = notaCreditoMov;
    }

    public BigDecimal getValorCobrar() {
        return valorCobrar;
    }

    public void setValorCobrar(BigDecimal valorCobrar) {
        this.valorCobrar = valorCobrar;
    }

    public BigDecimal getValorCambio() {
        return valorCambio;
    }

    public void setValorCambio(BigDecimal valorCambio) {
        this.valorCambio = valorCambio;
    }

    public BigDecimal getValorSumado() {
        return valorSumado;
    }

    public void setValorSumado(BigDecimal valorSumado) {
        this.valorSumado = valorSumado;
    }

    public BigDecimal getValorDebitado() {
        return valorDebitado;
    }

    public void setValorDebitado(BigDecimal valorDebitado) {
        this.valorDebitado = valorDebitado;
    }

    public PagoCheque getPagoCheque() {
        return pagoCheque;
    }

    public void setPagoCheque(PagoCheque pagoCheque) {
        this.pagoCheque = pagoCheque;
    }

    public PagoNotaCredito getPagoNotaCredio() {
        return pagoNotaCredio;
    }

    public void setPagoNotaCredio(PagoNotaCredito pagoNotaCredio) {
        this.pagoNotaCredio = pagoNotaCredio;
    }

    public PagoTarjetaCredito getPagoTarjetaCredito() {
        return pagoTarjetaCredito;
    }

    public void setPagoTarjetaCredito(PagoTarjetaCredito pagoTarjetaCredito) {
        this.pagoTarjetaCredito = pagoTarjetaCredito;
    }

    public PagoTransferencia getPagoTransferencia() {
        return pagoTransferencia;
    }

    public void setPagoTransferencia(PagoTransferencia pagoTransferencia) {
        this.pagoTransferencia = pagoTransferencia;
    }

    public List<PagoCheque> getListPagoCheque() {
        return listPagoCheque;
    }

    public void setListPagoCheque(List<PagoCheque> listPagoCheque) {
        this.listPagoCheque = listPagoCheque;
    }

    public List<PagoNotaCredito> getListPagoNotaCredio() {
        return listPagoNotaCredio;
    }

    public void setListPagoNotaCredio(List<PagoNotaCredito> listPagoNotaCredio) {
        this.listPagoNotaCredio = listPagoNotaCredio;
    }

    public List<PagoTarjetaCredito> getListPagoTarjetaCredito() {
        return listPagoTarjetaCredito;
    }

    public void setListPagoTarjetaCredito(List<PagoTarjetaCredito> listPagoTarjetaCredito) {
        this.listPagoTarjetaCredito = listPagoTarjetaCredito;
    }

    public List<PagoTransferencia> getListPagoTransferencia() {
        return listPagoTransferencia;
    }

    public void setListPagoTransferencia(List<PagoTransferencia> listPagoTransferencia) {
        this.listPagoTransferencia = listPagoTransferencia;
    }

    public List<NotasCredito> getListPagoNota() {
        return listPagoNota;
    }

    public void setListPagoNota(List<NotasCredito> listPagoNota) {
        this.listPagoNota = listPagoNota;
    }

    public NotasCredito getNota() {
        return nota;
    }

    public void setNota(NotasCredito nota) {
        this.nota = nota;
    }

    public BigDecimal getValorTotalEfectivo() {
        return valorTotalEfectivo;
    }

    public void setValorTotalEfectivo(BigDecimal valorTotalEfectivo) {
        this.valorTotalEfectivo = valorTotalEfectivo;
    }

    public BigDecimal getValorTotalCruceCuenta() {
        return valorTotalCruceCuenta;
    }

    public void setValorTotalCruceCuenta(BigDecimal valorTotalCruceCuenta) {
        this.valorTotalCruceCuenta = valorTotalCruceCuenta;
    }

    public BigDecimal getValorTotalNotasCredito() {
        return valorTotalNotasCredito;
    }

    public void setValorTotalNotasCredito(BigDecimal valorTotalNotasCredito) {
        this.valorTotalNotasCredito = valorTotalNotasCredito;
    }

    public BigDecimal getValorTotalCheques() {
        return valorTotalCheques;
    }

    public void setValorTotalCheques(BigDecimal valorTotalCheques) {
        this.valorTotalCheques = valorTotalCheques;
    }

    public BigDecimal getValorTotalTarjetas() {
        return valorTotalTarjetas;
    }

    public void setValorTotalTarjetas(BigDecimal valorTotalTarjetas) {
        this.valorTotalTarjetas = valorTotalTarjetas;
    }

    public BigDecimal getValorTotalTransferencias() {
        return valorTotalTransferencias;
    }

    public void setValorTotalTransferencias(BigDecimal valorTotalTransferencias) {
        this.valorTotalTransferencias = valorTotalTransferencias;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getValorSaldoPago() {
        return valorSaldoPago;
    }

    public void setValorSaldoPago(BigDecimal valorSaldoPago) {
        this.valorSaldoPago = valorSaldoPago;
    }

    public BigDecimal getValorSaldoPagoFinal() {
        return valorSaldoPagoFinal;
    }

    public void setValorSaldoPagoFinal(BigDecimal valorSaldoPagoFinal) {
        this.valorSaldoPagoFinal = valorSaldoPagoFinal;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public BigDecimal getValorMinimoPagar() {
        return valorMinimoPagar;
    }

    public void setValorMinimoPagar(BigDecimal valorMinimoPagar) {
        this.valorMinimoPagar = valorMinimoPagar;
    }

    public OtrosFormas getOtrasFormas() {
        return otrasFormas;
    }

    public void setOtrasFormas(OtrosFormas otrasFormas) {
        this.otrasFormas = otrasFormas;
    }
    
//</editor-fold>

    @Override
    public String toString() {
        return "PagoTituloReporteModel{" + "valorRecibido=" + valorRecibido + ", valorCobrar=" + valorCobrar + ", valorCambio=" + valorCambio + ", valorSumado=" + valorSumado + ", valorDebitado=" + valorDebitado + ", pagoCheque=" + pagoCheque + ", pagoNotaCredio=" + pagoNotaCredio + ", pagoTarjetaCredito=" + pagoTarjetaCredito + ", pagoTransferencia=" + pagoTransferencia + ", listPagoCheque=" + listPagoCheque + ", listPagoNotaCredio=" + listPagoNotaCredio + ", listPagoTarjetaCredito=" + listPagoTarjetaCredito + ", listPagoTransferencia=" + listPagoTransferencia + ", listPagoNota=" + listPagoNota + ", nota=" + nota + ", valorTotalEfectivo=" + valorTotalEfectivo + ", valorTotalCruceCuenta=" + valorTotalCruceCuenta + ", valorTotalNotasCredito=" + valorTotalNotasCredito + ", valorTotalCheques=" + valorTotalCheques + ", valorTotalTarjetas=" + valorTotalTarjetas + ", valorTotalTransferencias=" + valorTotalTransferencias + ", valorTotal=" + valorTotal + ", observacion=" + observacion + ", valorSaldoPago=" + valorSaldoPago + ", valorSaldoPagoFinal=" + valorSaldoPagoFinal + '}';
    }

}
