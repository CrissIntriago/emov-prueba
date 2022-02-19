/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "comprobantePagoView")
@ViewScoped
public class ComprobantePagoController implements Serializable {

    /*Inject*/
    @Inject
    private UserSession userSession;
    @Inject
    private ComprobantePagoService comprobantePagoService;
    @Inject
    private DetalleComprobantePagoService detalleComprobantePagoService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private BeneficiarioComprobantePagoService beneficiarioComprobantePagoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private BeneficiarioSolicitudReservaService beneficiarioSolicitudReservaService;
    @Inject
    private MasterCatalogoService periodoService;

    /*Objecto*/
    private ComprobantePago comprobantePago;
    private OpcionBusqueda opcionBusqueda;
    private DiarioGeneral diarioGeneral;
    private BeneficiarioComprobantePago beneficiarioComprobantePago;
    private CuentaBancaria cuentaBancariaSelecionada;

    /*LazyModel*/
    private LazyModel<ComprobantePago> comprobantePagoLazy;
    private LazyModel<CuentaBancaria> cuentaBancariaLazyModel;

    /*Listas*/
    private List<DetalleTransaccion> detalleTransaccionList;
    private List<DetalleComprobantePago> detalleComprobantePagoList;
    private List<BeneficiarioComprobantePago> beneficiarioComprobantePagoList;
    private List<BeneficiarioComprobantePago> beneficiariosAnuladosSeleccionados;
    private List<DiarioGeneral> diarioGeneralList;
    private List<MasterCatalogo> listaPeriodo;

    /*Variables*/
    private Boolean ocultarTabla;
    private Boolean ocultarBotonesAcciones;
    private Boolean botonCuentaBanco;
    private Boolean botonAcciones;
    private Boolean accion;
    private double totalDebe;
    private double totalHaber;
    private double totalValor;
    private double totalValorBeneficiarios;
    private Short periodo;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.cuentaBancariaLazyModel = new LazyModel<>(CuentaBancaria.class);
        this.cuentaBancariaLazyModel.getSorteds().put("id", "ASC");
        this.cuentaBancariaLazyModel.getFilterss().put("estado", true);
        this.cuentaBancariaLazyModel.getFilterss().put("tipoCuenta", true);
        this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        this.periodo = opcionBusqueda.getAnio();
        cargarRegistros();
        vaciarFormulario("REINICIAR");
    }

    public void cargarRegistros() {
        if (periodo != null) {
            this.comprobantePagoLazy = new LazyModel<>(ComprobantePago.class);
            this.comprobantePagoLazy.getSorteds().put("id", "ASC");
            this.comprobantePagoLazy.getFilterss().put("periodo", periodo);
            this.comprobantePagoLazy.getFilterss().put("periodo", periodo);
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
    }

    public void form(ComprobantePago comprobante, String accion) {
        if (periodo != null) {
//            if (periodo.equals(periodo) || accion.equals("visualizar")) {
            if (comprobante != null) {
                this.comprobantePago = comprobante;
                this.diarioGeneral = comprobante.getDiarioGeneral();
                this.ocultarTabla = Boolean.FALSE;
                this.ocultarBotonesAcciones = Boolean.FALSE;
                this.detalleComprobantePagoList = detalleComprobantePagoService.getDetalleComprobantePago(comprobantePago);
                this.beneficiarioComprobantePagoList = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobantePago);
                actualizarTotales();
                actualizarTotalBeneficiario();
            } else {
                this.comprobantePago = new ComprobantePago();
                this.comprobantePago.setPeriodo(periodo);
                this.comprobantePago.setFechaComprobante(new Date());
                if ("NUEVO".equals(accion)) {
                    this.ocultarTabla = Boolean.FALSE;
                } else {
                    vaciarFormulario(accion);
                }
            }
            PrimeFaces.current().ajax().update("formMain");
//            } else {
//                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
//            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }

    }

    public void saveComprobante() {
        if (comprobantePago.getDetalle().equals("")) {
            JsfUtil.addWarningMessage("COMPROBANTE DE PAGO", "Debe ingresar el detalle del comprobante de pago antes de guardar");
            return;
        }
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(comprobantePago.getFechaComprobante()), Utils.convertirMesALetra(Utils.getMes(comprobantePago.getFechaComprobante())));
        if (!periodoAbierto) {
            JsfUtil.addWarningMessage("AVISO", "Es imposible guardar porque la fecha seleccionada seleccionado esta cerrado");
            return;
        } else {
            if (detalleComprobantePagoList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!", "No hay detalle del comprobante a guardar");
                return;
            }
            if (totalDebe != totalValor || totalDebe != totalHaber) {
                JsfUtil.addWarningMessage("AVISO ", "La suma de los montos del Comprobante de Pago esta descuadrado");
                return;
            }
            if (detalleComprobantePagoList == null && detalleComprobantePagoList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO ", "No hay cuentas relacionadas, se recomienda verificar la relación Cuenta Contable - Catálogo Presupuesto");
                return;
            }
            int auxiliar = 0;
            if (beneficiarioComprobantePagoList == null && beneficiarioComprobantePagoList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO ", "No existen datos del beneficiario");
                return;
            } else {
                for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
                    if (beneficiario.getDetalleBanco() != null && beneficiario.getBeneficiario() != null) {
                        auxiliar = auxiliar + 1;
                    }
                }
            }
            if (auxiliar < beneficiarioComprobantePagoList.size()) {
                JsfUtil.addWarningMessage("AVISO ", "Revise la información del beneficiario");
                return;
            }
            /*GUARDAR EL ENCABEZADO DEL COMPROBANTE DE PAGO*/
            comprobantePago.setUsuarioCreacion(userSession.getNameUser());
            comprobantePago.setFechaCreacion(new Date());
            comprobantePago.setEstado("REGISTRADO");
            comprobantePago.setPeriodo(periodo);
            comprobantePago.setDetalle(comprobantePago.getDetalle().toUpperCase());
            ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(periodo);
            if (ultimoComprobantePago != null) {
                comprobantePago.setNumComprobante(BigInteger.valueOf(ultimoComprobantePago.getNumComprobante().longValue() + 1));
            } else {
                comprobantePago.setNumComprobante(BigInteger.valueOf(1));
            }
            int contador = ultimoBeneficiarioComprobanteAnterior();
            comprobantePagoService.create(comprobantePago);
            /*GUARDAR EL DETALLE DEL COMPROBANTE DE PAGO*/
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                detalleComprobante.setComprobantePago(comprobantePago);
                detalleComprobantePagoService.create(detalleComprobante);
            }
            for (BeneficiarioComprobantePago beneficiarioComprobante : beneficiarioComprobantePagoList) {
                beneficiarioComprobante.setComprobantePago(comprobantePago);
                contador += 1;
                beneficiarioComprobante.setNumeroTransferencia(BigInteger.valueOf(contador));
                beneficiarioComprobante.setEstadoBeneficiario("REGISTRADO");
                beneficiarioComprobantePagoService.create(beneficiarioComprobante);
            }
        }
        /*ACTUALIZAMOS EL ESTADO DEL DIARIO GENERAL */
        comprobantePago.getDiarioGeneral().setComprobantePago(Boolean.TRUE);
        diarioGeneralService.edit(comprobantePago.getDiarioGeneral());
        this.ocultarTabla = Boolean.TRUE;
        imprimirReporteComprobante(comprobantePago);
        vaciarFormulario("REINICIAR");
        PrimeFaces.current().ajax().update("comprobantePagoTable");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void anularComprobante(ComprobantePago comprobante) {
        if (periodo.equals(periodo)) {
            this.totalDebe = 0;
            this.totalHaber = 0;
            /*EDITAMOS EL ESTADO DEL COMPROBANTE DE PAGO*/
            comprobante.setEstado("ANULADO");
            comprobante.setDetalle(comprobante.getDetalle() + " (ANULADO)");
            comprobante.setUsuarioModificacion(userSession.getNameUser());
            comprobante.setFechaModificacion(new Date());
            comprobantePagoService.edit(comprobante);
            /*TRAEMOS LOS DETALLES DEL COMPROBANTE DE PAGO*/
            detalleComprobantePagoList = detalleComprobantePagoService.getDetalleComprobantePago(comprobante);
            /*TRAEMOS LOS BENEFICIAROS DEL COMPROBANTE DE PAGO*/
            beneficiarioComprobantePagoList = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobante);
            /*MODIFICAMOS EL ESTADO DE LOS BENEFICIARIOS*/
            for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
                beneficiario.setEstadoBeneficiario(comprobante.getEstado());
                beneficiarioComprobantePagoService.edit(beneficiario);
            }
            /*BUSCAMOS LA ULTIMA TRANSACCION EN EL LIBRO DIARIO*/
            DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(periodo);
            DiarioGeneral diarioG = new DiarioGeneral();
            if (ultimaActa != null) {
                diarioG.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
            } else {
                diarioG.setNumeroTransaccion(BigInteger.valueOf(1));
            }
            if (comprobante.getNumeroTramite() != null) {
                diarioG.setNumTramite(comprobante.getNumeroTramite().longValue());
            }
            /*CREAMOS UNA TRANSACCION EN EL LIBRO DIARIO*/
            if (comprobante.getDiarioGeneral() != null) {
                diarioG.setRetenido(comprobante.getDiarioGeneral().getRetenido());
                if (comprobante.getDiarioGeneral().getVariosBeneficiarios() != null) {
                    diarioG.setVariosBeneficiarios(comprobante.getDiarioGeneral().getVariosBeneficiarios());
                }
                if (comprobante.getDiarioGeneral().getBeneficiario() != null) {
                    diarioG.setBeneficiario(comprobante.getDiarioGeneral().getBeneficiario());
                }
                if (comprobante.getDiarioGeneral().getTipoBeneficiario() != null) {
                    diarioG.setTipoBeneficiario(comprobante.getDiarioGeneral().getTipoBeneficiario());
                }
                if (comprobante.getDiarioGeneral().getEnlace() != null) {
                    diarioG.setEnlace(comprobante.getDiarioGeneral().getEnlace());
                }
                if (comprobante.getDiarioGeneral().getCertificacionesPresupuestario() != null) {
                    diarioG.setCertificacionesPresupuestario(comprobante.getDiarioGeneral().getCertificacionesPresupuestario());
                }
                diarioG.setComprobantePago(Boolean.TRUE);
                /*ACTUALIZAMOS EL ESTADO DE COMPROBANTE DEL DIARIO GENERAL PARA QUE VUELVA A ESTAR ACTIVO Y PODER REGISTRARLO*/
                comprobante.getDiarioGeneral().setComprobantePago(Boolean.FALSE);
                diarioGeneralService.edit(comprobante.getDiarioGeneral());
            } else {
                diarioG.setRetenido(Boolean.FALSE);
            }
            diarioG.setObservacion("ANULACIÓN CP No." + comprobante.getNumComprobante() + ", " + comprobante.getDetalle());
            diarioG.setUsuarioCreacion(userSession.getNameUser());
            diarioG.setFechaCreacion(comprobante.getFechaComprobante());
            diarioG.setEstado(Boolean.TRUE);
            diarioG.setPeriodo(periodo);
            diarioG.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
            diarioG.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
            diarioG.setEstadoTransaccion("CUADRADO");
            diarioG.setFechaElaboracion(comprobante.getFechaComprobante());
            diarioG.setEstadoDiario("REGISTRADO");
            diarioG = diarioGeneralService.create(diarioG);
            /*CREAMOS LOS DETALLES DE LA TRANSACCION*/
            int contador = 0;
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                DetalleTransaccion detalleTransaccion = new DetalleTransaccion();
                detalleTransaccion.setDiarioGeneral(diarioG);
                contador = contador + 1;
                BigInteger bigInteger = BigInteger.valueOf(contador);
                detalleTransaccion.setContador(bigInteger);
                detalleTransaccion.setCuentaContable(detalleComprobante.getCuentaContable());
                if (detalleComprobante.getDebe() != null) {
                    double debe = detalleComprobante.getDebe().doubleValue() * (-1);
                    detalleTransaccion.setDebe(new BigDecimal(debe));
                }
                if (detalleComprobante.getHaber() != null) {
                    double haber = detalleComprobante.getHaber().doubleValue() * (-1);
                    detalleTransaccion.setHaber(new BigDecimal(haber));
                }
                if (detalleComprobante.getTipoPago() != null) {
                    detalleTransaccion.setTipoTransaccion(detalleComprobante.getTipoPago());
                }
                if (detalleComprobante.getEjecutado() != null) {
                    double ejecutado = detalleComprobante.getEjecutado().doubleValue() * (-1);
                    detalleTransaccion.setEjecutado(new BigDecimal(ejecutado));
                } else {
                    detalleTransaccion.setEjecutado(BigDecimal.ZERO);
                }
                if (detalleComprobante.getPartidaPresupuestaria() != null) {
                    detalleTransaccion.setPartidaPresupuestaria(detalleComprobante.getPartidaPresupuestaria());
                }
                if (detalleComprobante.getEstructuraProgramatica() != null) {
                    detalleTransaccion.setEstructuraProgramatica(detalleComprobante.getEstructuraProgramatica());
                }
                if (detalleComprobante.getFuente() != null) {
                    detalleTransaccion.setFuente(detalleComprobante.getFuente());
                }
                if (detalleComprobante.getCedulaPresupuestaria() != null) {
                    detalleTransaccion.setCedulaPresupuestaria(detalleComprobante.getCedulaPresupuestaria());
                }
                if (detalleComprobante.getIdDetalleReserva() != null) {
                    detalleTransaccion.setIdDetalleReserva(detalleComprobante.getIdDetalleReserva());
                }
                detalleTransaccion.setDevengado(BigDecimal.ZERO);
                detalleTransaccion.setComprometido(BigDecimal.ZERO);
                if (detalleTransaccion.getDebe() != null) {
                    totalDebe = Math.round((totalDebe + detalleTransaccion.getDebe().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
                }
                if (detalleTransaccion.getHaber() != null) {
                    totalHaber = Math.round((totalHaber + detalleTransaccion.getHaber().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
                }
                detalleTransaccionService.create(detalleTransaccion);
            }
            /*ACTUALIZAMOS EL TOTAL DE LOS DEBES Y HABER DEL DETALLE DEL LIBRO DIARIO*/
            diarioG.setTotalDebe(new BigDecimal(totalDebe));
            diarioG.setTotalHaber(new BigDecimal(totalHaber));
            diarioGeneralService.edit(diarioG);
            diarioGeneral = diarioG;
            /*ACTUALIZAMOS EL ESTADO DE COMPROBANTE DEL DIARIO GENERAL PARA QUE VUELVA A ESTAR ACTIVO Y PODER REGISTRARLO*/
            if (comprobante.getDiarioGeneral() != null) {
                comprobante.getDiarioGeneral().setComprobantePago(Boolean.FALSE);
                diarioGeneralService.edit(comprobante.getDiarioGeneral());
            }
            /*IMPRIMIMOS EL REPORTE DEL COMPROBANTE DE PAGO*/
            imprimirReporteComprobante(comprobante);
            PrimeFaces.current().executeScript("PF('comprobanteDiarioGeneralDLG').show()");
        } else {
            JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
        }
    }

    public void generarArchivoDiarioGeneral(Boolean estado) {
        if (estado) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            imprimirReporteDiarioGeneral(diarioGeneral);
        }
        vaciarFormulario("REINICIAR");
        PrimeFaces.current().executeScript("PF('comprobanteDiarioGeneralDLG').hide()");
        PrimeFaces.current().ajax().update("comprobantePagoTable");
    }

    private void openDlgDiarioGeneral() {
        PrimeFaces.current().ajax().update("diarioGeneralForm");
        PrimeFaces.current().executeScript("PF('diarioGeneralDLG').show()");
    }

    public void buscarDiarioGeneral(String accion) {
        diarioGeneralList = comprobantePagoService.getListDiarioGeneral(periodo);
        if (accion.equals("BOTONBUSQUEDA")) {
            this.detalleComprobantePagoList = new ArrayList<>();
            this.beneficiarioComprobantePagoList = new ArrayList<>();
        }
        if (diarioGeneral.getNumeroTransaccion() != null) {
            DiarioGeneral diario = comprobantePagoService.getDiarioGeneral(diarioGeneral.getNumeroTransaccion(), periodo);
            if (diario != null) {
                if ((diario.getTipo().getCodigo().equals("tipo_financiero") && diario.getClase().getCodigo().equals("clase_egreso")) || diario.getEnlace().getCodigo().equals("modulo_anulacion_cp_transferencia")) {
                    DiarioGeneralSeleccionado(diario, "INGRESO");
                } else {
                    openDlgDiarioGeneral();
                }
            } else {
                openDlgDiarioGeneral();
            }
        } else {
            openDlgDiarioGeneral();
        }
        this.botonCuentaBanco = Boolean.TRUE;
        PrimeFaces.current().ajax().update("formComprobantePagos");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
        PrimeFaces.current().ajax().update("beneficiarioTable");
    }

    public void openDlgCuentaBanco() {
        if (comprobantePago.getCuentaBancaria() != null) {
            JsfUtil.addWarningMessage("AVISO", "Solo puede agregar una cuenta bancaria");
        } else {
            PrimeFaces.current().ajax().update("cuentaBancoDLG");
            PrimeFaces.current().ajax().update("cuentaBancoForm");
            PrimeFaces.current().executeScript("PF('cuentaBancoDLG').show()");
        }
    }

    public void añadirCuentaBanco() {
        if (cuentaBancariaSelecionada != null) {
            DetalleComprobantePago comprobante = new DetalleComprobantePago();
            int contador = detalleComprobantePagoList.size() + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            comprobante.setLinea(bigInteger);
            comprobante.setCuentaContable(cuentaBancariaSelecionada.getCuentaMovimiento());
            double sumaDebe = 0;
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                if (detalleComprobante.getDebe() != null) {
                    sumaDebe = sumaDebe + detalleComprobante.getDebe().doubleValue();
                }
            }
            comprobante.setHaber(new BigDecimal(sumaDebe));
            comprobante.setDebe(BigDecimal.ZERO);
            comprobante.setEjecutado(BigDecimal.ZERO);
            detalleComprobantePagoList.add(comprobante);
        }
        comprobantePago.setCuentaBancaria(cuentaBancariaSelecionada);
        actualizarTotales();
        actualizarTotalBeneficiario();
        PrimeFaces.current().executeScript("PF('cuentaBancoDLG').hide()");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
    }

    //<editor-fold defaultstate="collapsed" desc="Seleccionar Diario General">
    public void DiarioGeneralSeleccionado(DiarioGeneral diarioGeneral, String accion) {
        this.diarioGeneral = diarioGeneral;
        this.comprobantePago.setDiarioGeneral(diarioGeneral);
        if (diarioGeneral.getCertificacionesPresupuestario() != null) {
            this.comprobantePago.setReservaCompromiso(diarioGeneral.getCertificacionesPresupuestario());
        }
        this.comprobantePago.setDetalle(diarioGeneral.getObservacion());
        /*TRAE LAS CUENTAS CON LAS PARTIDAS PRESUPUESTARIAS*/
        List<DetalleTransaccion> detallesTransacciones = detalleTransaccionService.getDetalleTransaccionDevengados(diarioGeneral);
        /*TRAE LAS CUENTA QUE ESTAN RELACIONADAS*/
        this.detalleTransaccionList = comprobantePagoService.getDetalleTransaccion(diarioGeneral);
        /*LISTAS AUXILIARES*/
        List<DetalleComprobantePago> detallesComprobantePago_1 = new ArrayList<>();
        List<DetalleComprobantePago> detallesComprobantePago_2 = new ArrayList<>();
        /*COMENZAMOS AÑADIR DATOS A LA LISTA*/
        if (detalleTransaccionList != null && !detalleTransaccionList.isEmpty()) {
            if (diarioGeneral.getEnlace() != null) {
                if (diarioGeneral.getEnlace().getCodigo().equals("modulo_anulacion_cp_transferencia")) {
                    this.accion = Boolean.TRUE;
                    int in = 1;
                    for (DetalleTransaccion detalle : detalleTransaccionList) {
                        DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                        detalleComprobantePago.setCuentaContable(detalle.getCuentaContable());
                        detalleComprobantePago.setDebe(detalle.getHaber());
                        detalleComprobantePago.setLinea(BigInteger.valueOf(in));
                        detalleComprobantePagoList.add(detalleComprobantePago);
                        in += 1;
                    }
                }
            } else {
                /*GUARDAMOS SOLO LA CUENTA CONTABLE  EL VALOR AL DEBE EN detallesComprobantePago_1*/
                for (DetalleTransaccion detalle : detalleTransaccionList) {
                    DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                    detalleComprobantePago.setCuentaContable(detalle.getCuentaContable());
                    detalleComprobantePago.setDebe(detalle.getHaber());
                    detallesComprobantePago_1.add(detalleComprobantePago);
                }
                /*GUARDAMOS LAS PARTIDAS PRESUPUESTARIAS Y LA ESTRUCTURA PROGRAMATICA EN detallesComprobantePago_2*/
                for (DetalleTransaccion detalle : detallesTransacciones) {
                    if (detalle.getTipoTransaccion().getCodigo().equals("diario_general_devengado")) {
                        DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                        detalleComprobantePago.setPartidaPresupuestaria(detalle.getPartidaPresupuestaria());
                        detalleComprobantePago.setEstructuraProgramatica(detalle.getEstructuraProgramatica());
                        detalleComprobantePago.setFuente(detalle.getFuente());
                        detalleComprobantePago.setCedulaPresupuestaria(detalle.getCedulaPresupuestaria());
                        detalleComprobantePago.setIdDetalleReserva(detalle.getIdDetalleReserva());
                        /*EL VALOR DEL COMPROMETIDO LO GUARDAMOS MOMENTANEMENTE EN LA VARIABLE DEL HABER*/
                        detalleComprobantePago.setDebe(detalle.getComprometido());
                        detalleComprobantePago.setHaber(BigDecimal.ZERO);
                        for (DetalleTransaccion detalleTransaccion : detallesTransacciones) {
                            if (detalle.getPartidaPresupuestaria().equals(detalleTransaccion.getPartidaPresupuestaria())
                                    && detalle.getTipoTransaccion().getCodigo().equals("diario_general_devengado") && detalleTransaccion.getTipoTransaccion().getCodigo().equals("diario_general_ejecucion")) {
                                detalleComprobantePago.setHaber(detalleTransaccion.getEjecutado());
                            }
                        }
                        detallesComprobantePago_2.add(detalleComprobantePago);
                    }
                }
                /*UNIR LAS 2 LISTAS*/
                if (detallesComprobantePago_2.size() == 1) {
                    for (DetalleComprobantePago detalle1 : detallesComprobantePago_1) {
                        DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                        int contador = detalleComprobantePagoList.size() + 1;
                        BigInteger bigInteger = BigInteger.valueOf(contador);
                        detalleComprobantePago.setLinea(bigInteger);
                        detalleComprobantePago.setCuentaContable(detalle1.getCuentaContable());
                        detalleComprobantePago.setDebe(detalle1.getDebe());
                        detalleComprobantePago.setHaber(BigDecimal.ZERO);
                        for (DetalleComprobantePago detalle2 : detallesComprobantePago_2) {
                            detalleComprobantePago.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                            detalleComprobantePago.setPartidaPresupuestaria(detalle2.getPartidaPresupuestaria());
                            detalleComprobantePago.setEstructuraProgramatica(detalle2.getEstructuraProgramatica());
                            detalleComprobantePago.setFuente(detalle2.getFuente());
                            detalleComprobantePago.setCedulaPresupuestaria(detalle2.getCedulaPresupuestaria());
                            detalleComprobantePago.setIdDetalleReserva(detalle2.getIdDetalleReserva());
                        }
                        detalleComprobantePago.setEjecutado(detalleComprobantePago.getDebe());
                        detalleComprobantePagoList.add(detalleComprobantePago);
                    }
                } else {
                    for (DetalleComprobantePago detalle1 : detallesComprobantePago_1) {
                        double diferencia = detalle1.getDebe().doubleValue();
                        for (DetalleComprobantePago detalle2 : detallesComprobantePago_2) {
                            if (detalle1.getCuentaContable().getCodigo().substring(3, 5).equals(detalle2.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                                /*VERIFICAMOS SI LA ASOCIACION CUENTA CONTABLE - CATALOGO PRESUPUESTO ESTA CORRECTA*/
                                Boolean relacionContable = comprobantePagoService.getRelacionCuentaContable(detalle1.getCuentaContable(), detalle2.getPartidaPresupuestaria());
                                if (relacionContable) {
                                    /*Condicion para solo registrar una vez la cuenta contable*/
                                    Boolean registrado = true;
                                    Boolean bandera = false;
                                    for (DetalleComprobantePago detComprobante : detalleComprobantePagoList) {
                                        if (detalle1.getCuentaContable().equals(detComprobante.getCuentaContable())) {
                                            registrado = false;
                                        }
                                    }
                                    /*CREAMOS UN NUEVO OBJETO PARA AÑADIRLO EN LA LISTA A PRESENTAR Y GUARDAR*/
                                    DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                                    int contador = detalleComprobantePagoList.size() + 1;
                                    BigInteger bigInteger = BigInteger.valueOf(contador);
                                    detalleComprobantePago.setLinea(bigInteger);
                                    if (registrado) {
                                        detalleComprobantePago.setCuentaContable(detalle1.getCuentaContable());
                                        detalleComprobantePago.setDebe(detalle1.getDebe());
                                        detalleComprobantePago.setHaber(BigDecimal.ZERO);
                                    }
                                    /*DEBE = COMPROMETIDO Y HABER = EJECUTADO  y EJECUTADO = SALDO*/
                                    detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                    if (diferencia >= detalle2.getDebe().doubleValue()) {
                                        detalle2.setHaber(detalle2.getDebe().add(detalle2.getHaber()));
                                        detalleComprobantePago.setEjecutado(detalle2.getDebe());
                                        diferencia = diferencia - detalleComprobantePago.getEjecutado().doubleValue();
                                        detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                        bandera = true;
                                    } else {
                                        if ((diferencia > 0) && detalle2.getEjecutado().doubleValue() > 0) {
                                            detalleComprobantePago.setEjecutado(new BigDecimal(diferencia));
                                            detalle2.setHaber(new BigDecimal(detalle2.getHaber().doubleValue() + diferencia));
                                            detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - diferencia));
                                            diferencia = diferencia - detalleComprobantePago.getHaber().doubleValue();
                                            bandera = true;
                                        } else {
                                            bandera = false;
                                        }
                                    }
                                    if (bandera) {
                                        detalleComprobantePago.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                        detalleComprobantePago.setPartidaPresupuestaria(detalle2.getPartidaPresupuestaria());
                                        detalleComprobantePago.setEstructuraProgramatica(detalle2.getEstructuraProgramatica());
                                        detalleComprobantePago.setFuente(detalle2.getFuente());
                                        detalleComprobantePago.setCedulaPresupuestaria(detalle2.getCedulaPresupuestaria());
                                        detalleComprobantePago.setIdDetalleReserva(detalle2.getIdDetalleReserva());
                                        detalleComprobantePagoList.add(detalleComprobantePago);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            actualizarTotales();
            /*AGREGAR LOS BENEFICIARIOS*/
            if (diarioGeneral.getVariosBeneficiarios() != null) {
                if (diarioGeneral.getVariosBeneficiarios()) {
                    List<BeneficiarioSolicitudReserva> beneficiariosReservaList = beneficiarioSolicitudReservaService.getBeneficiarioReservasComprometidas(diarioGeneral.getCertificacionesPresupuestario());
                    int contador = ultimoBeneficiarioComprobanteAnterior();
                    for (BeneficiarioSolicitudReserva beneficiarioReserva : beneficiariosReservaList) {
                        beneficiarioComprobantePago = new BeneficiarioComprobantePago();
                        beneficiarioComprobantePago.setBeneficiario(beneficiarioReserva.getBeneficiario());
                        beneficiarioComprobantePago.setTipoBeneficiario(beneficiarioReserva.getTipoBeneficiario());
                        DetalleBanco detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiarioReserva.getBeneficiario(), beneficiarioReserva.getTipoBeneficiario());
                        if (detalleBanco != null) {
                            beneficiarioComprobantePago.setDetalleBanco(detalleBanco);
                        }
                        contador += 1;
                        beneficiarioComprobantePago.setNumeroTransferencia(BigInteger.valueOf(contador));
                        beneficiarioComprobantePago.setValor(beneficiarioReserva.getValor());
                        beneficiarioComprobantePagoList.add(beneficiarioComprobantePago);
                    }
                } else {
                    DetalleBanco detalleBanco;
                    beneficiarioComprobantePago = new BeneficiarioComprobantePago();
                    beneficiarioComprobantePago.setBeneficiario(diarioGeneral.getBeneficiario());
                    beneficiarioComprobantePago.setTipoBeneficiario(diarioGeneral.getTipoBeneficiario());
                    detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiarioComprobantePago.getBeneficiario(), beneficiarioComprobantePago.getTipoBeneficiario());
                    if (detalleBanco != null) {
                        beneficiarioComprobantePago.setDetalleBanco(detalleBanco);
                    }
                    beneficiarioComprobantePago.setNumeroTransferencia(BigInteger.valueOf(ultimoBeneficiarioComprobanteAnterior() + 1));
                    if (this.accion) {
                        beneficiarioComprobantePago.setValor(new BigDecimal(totalDebe));
                    } else {
                        beneficiarioComprobantePago.setValor(new BigDecimal(totalValor));
                    }
                    beneficiarioComprobantePagoList.add(beneficiarioComprobantePago);
                }
                actualizarTotalBeneficiario();
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "No hay cuentas seleccionadas para generar el comprobante");
        }
        if (accion.equals("DIALOG")) {
            PrimeFaces.current().executeScript("PF('diarioGeneralDLG').hide()");
            PrimeFaces.current().ajax().update("formComprobantePagos");
            PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
            PrimeFaces.current().ajax().update("beneficiarioTable");
        }
    }
//</editor-fold>

    private void actualizarTotales() {
        this.totalDebe = 0;
        this.totalHaber = 0;
        this.totalValor = 0;
        for (DetalleComprobantePago comprobante : detalleComprobantePagoList) {
            if (comprobante.getDebe() != null) {
                this.totalDebe = Math.round((totalDebe + comprobante.getDebe().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
            if (comprobante.getHaber() != null) {
                this.totalHaber = Math.round((totalHaber + comprobante.getHaber().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
            if (comprobante.getEjecutado() != null) {
                this.totalValor = Math.round((totalValor + comprobante.getEjecutado().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
        }
    }

    private void actualizarTotalBeneficiario() {
        this.totalValorBeneficiarios = 0;
        for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
            if (beneficiario.getValor() == null) {
                beneficiario.setValor(BigDecimal.ZERO);
            }
            this.totalValorBeneficiarios = Math.round((totalValorBeneficiarios + beneficiario.getValor().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
        }
    }

    public void imprimirReporteComprobante(ComprobantePago comprobantePago) {
        servletSession.addParametro("id_comprobante_pago", comprobantePago.getId());
        servletSession.setNombreReporte("ComprobantePago_Registrado");
        servletSession.setNombreSubCarpeta("ComprobantePago");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirReporteDiarioGeneral(DiarioGeneral diarioGeneral) {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void vaciarFormulario(String accion) {
        this.ocultarTabla = Boolean.TRUE;
        this.botonAcciones = Boolean.TRUE;
        this.ocultarBotonesAcciones = Boolean.TRUE;
        this.botonCuentaBanco = Boolean.FALSE;
        this.accion = Boolean.FALSE;
        this.diarioGeneral = new DiarioGeneral();
        this.beneficiarioComprobantePago = new BeneficiarioComprobantePago();
        this.detalleTransaccionList = new ArrayList<>();
        this.detalleComprobantePagoList = new ArrayList<>();
        this.beneficiarioComprobantePagoList = new ArrayList<>();
        this.beneficiariosAnuladosSeleccionados = new ArrayList<>();
        if (accion.equals("CANCELAR")) {
            PrimeFaces.current().ajax().update("dataTableComprobantePagos");
            PrimeFaces.current().ajax().update("formComprobantePagos");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void removerCuenta(DetalleComprobantePago detalleCP) {
        detalleComprobantePagoList.remove(detalleCP);
        int contador = 0;
        if (detalleComprobantePagoList.isEmpty()) {
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                contador += 1;
                detalleComprobante.setLinea(BigInteger.valueOf(contador + 1));
            }
            actualizarTotales();
        }
    }

    public void removerBeneficiario(BeneficiarioComprobantePago beneficiarioCP) {
        beneficiarioComprobantePagoList.remove(beneficiarioCP);
        int contador = 0;
        if (beneficiarioComprobantePagoList.isEmpty()) {
            for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
                contador = ultimoBeneficiarioComprobanteAnterior();
                beneficiario.setNumeroTransferencia(BigInteger.valueOf(contador + 1));
            }
            actualizarTotalBeneficiario();
        }
    }

    private int ultimoBeneficiarioComprobanteAnterior() {
        int contador = 0;
        ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(periodo);
        if (ultimoComprobantePago != null) {
            BeneficiarioComprobantePago beneficiarioComprobante = detalleComprobantePagoService.getUltimoNumeroReferencia(ultimoComprobantePago);
            if (beneficiarioComprobante != null) {
                contador = beneficiarioComprobante.getNumeroTransferencia().intValue();
            }
        }
        return contador;
    }

    public void openDlgEdit(ComprobantePago comprobante) {
        this.comprobantePago = comprobante;
        PrimeFaces.current().executeScript("PF('comprobanteEditDLG').show()");
        PrimeFaces.current().ajax().update("comprobanteEditFormDLG");
    }

    public void getPeriodoSelecionado() {
        comprobantePago.setPeriodo(Utils.getAnio(comprobantePago.getFechaComprobante()).shortValue());
        PrimeFaces.current().ajax().update("EditPeriodo");
    }

    public void opnDlgSaveEdit() {
        if (comprobantePago.getPeriodo() != periodo) {
            JsfUtil.addWarningMessage("AVISO!!!", "El período del comprobante no es igual el período general seleccionado");
            return;
        }
        comprobantePagoService.edit(comprobantePago);
        JsfUtil.addSuccessMessage("AVISO!!!", "Se ha editado correctamente el comprobante de pago No." + comprobantePago.getNumComprobante());
        vaciarFormulario("REINICIAR");
        PrimeFaces.current().executeScript("PF('comprobanteEditDLG').hide()");
        PrimeFaces.current().ajax().update("comprobanteEditFormDLG");
    }

    /*Get - n- Set*/
    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public LazyModel<CuentaBancaria> getCuentaBancariaLazyModel() {
        return cuentaBancariaLazyModel;
    }

    public void setCuentaBancariaLazyModel(LazyModel<CuentaBancaria> cuentaBancariaLazyModel) {
        this.cuentaBancariaLazyModel = cuentaBancariaLazyModel;
    }

    public CuentaBancaria getCuentaBancariaSelecionada() {
        return cuentaBancariaSelecionada;
    }

    public void setCuentaBancariaSelecionada(CuentaBancaria cuentaBancariaSelecionada) {
        this.cuentaBancariaSelecionada = cuentaBancariaSelecionada;
    }

    public List<DetalleComprobantePago> getDetalleComprobantePagoList() {
        return detalleComprobantePagoList;
    }

    public void setDetalleComprobantePagoList(List<DetalleComprobantePago> detalleComprobantePagoList) {
        this.detalleComprobantePagoList = detalleComprobantePagoList;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public Boolean getOcultarTabla() {
        return ocultarTabla;
    }

    public void setOcultarTabla(Boolean ocultarTabla) {
        this.ocultarTabla = ocultarTabla;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ComprobantePago> getComprobantePagoLazy() {
        return comprobantePagoLazy;
    }

    public void setComprobantePagoLazy(LazyModel<ComprobantePago> comprobantePagoLazy) {
        this.comprobantePagoLazy = comprobantePagoLazy;
    }

    public List<DetalleTransaccion> getDetalleTransaccionList() {
        return detalleTransaccionList;
    }

    public void setDetalleTransaccionList(List<DetalleTransaccion> detalleTransaccionList) {
        this.detalleTransaccionList = detalleTransaccionList;
    }

    public double getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(double totalDebe) {
        this.totalDebe = totalDebe;
    }

    public double getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(double totalHaber) {
        this.totalHaber = totalHaber;
    }

    public double getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(double totalValor) {
        this.totalValor = totalValor;
    }

    public BeneficiarioComprobantePago getBeneficiarioComprobantePago() {
        return beneficiarioComprobantePago;
    }

    public void setBeneficiarioComprobantePago(BeneficiarioComprobantePago beneficiarioComprobantePago) {
        this.beneficiarioComprobantePago = beneficiarioComprobantePago;
    }

    public List<BeneficiarioComprobantePago> getBeneficiarioComprobantePagoList() {
        return beneficiarioComprobantePagoList;
    }

    public void setBeneficiarioComprobantePagoList(List<BeneficiarioComprobantePago> beneficiarioComprobantePagoList) {
        this.beneficiarioComprobantePagoList = beneficiarioComprobantePagoList;
    }

    public List<DiarioGeneral> getDiarioGeneralList() {
        return diarioGeneralList;
    }

    public void setDiarioGeneralList(List<DiarioGeneral> diarioGeneralList) {
        this.diarioGeneralList = diarioGeneralList;
    }

    public Boolean getOcultarBotonesAcciones() {
        return ocultarBotonesAcciones;
    }

    public void setOcultarBotonesAcciones(Boolean ocultarBotonesAcciones) {
        this.ocultarBotonesAcciones = ocultarBotonesAcciones;
    }

    public Boolean getBotonCuentaBanco() {
        return botonCuentaBanco;
    }

    public void setBotonCuentaBanco(Boolean botonCuentaBanco) {
        this.botonCuentaBanco = botonCuentaBanco;
    }

    public double getTotalValorBeneficiarios() {
        return totalValorBeneficiarios;
    }

    public void setTotalValorBeneficiarios(double totalValorBeneficiarios) {
        this.totalValorBeneficiarios = totalValorBeneficiarios;
    }

    public List<BeneficiarioComprobantePago> getBeneficiariosAnuladosSeleccionados() {
        return beneficiariosAnuladosSeleccionados;
    }

    public void setBeneficiariosAnuladosSeleccionados(List<BeneficiarioComprobantePago> beneficiariosAnuladosSeleccionados) {
        this.beneficiariosAnuladosSeleccionados = beneficiariosAnuladosSeleccionados;
    }

    public Boolean getBotonAcciones() {
        return botonAcciones;
    }

    public void setBotonAcciones(Boolean botonAcciones) {
        this.botonAcciones = botonAcciones;
    }
//</editor-fold>
}
