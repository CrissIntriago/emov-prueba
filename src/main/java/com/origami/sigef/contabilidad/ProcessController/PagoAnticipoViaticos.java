/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "pagoAnticipoViaticosView")
@ViewScoped
public class PagoAnticipoViaticos extends BpmnBaseRoot implements Serializable {

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
    private CatalogoService catalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private AnticipoRemuneracionService anticipoService;
    @Inject
    private ClienteService clienteService;
    /*Objecto*/
    private ComprobantePago comprobantePago;
    private OpcionBusqueda opcionBusqueda;
    private DiarioGeneral diarioGeneral;
    private BeneficiarioComprobantePago beneficiarioComprobantePago;
    private CuentaBancaria cuentaBancariaSelecionada;
    private CatalogoItem enlaceSeleccionado;
    private CatalogoItem subEnlaceSeleccionado;
    private Servidor servidor;

    /*LazyModel*/
    private LazyModel<ComprobantePago> comprobantePagoLazy;
    private LazyModel<CuentaBancaria> cuentaBancariaLazyModel;

    /*Listas*/
    private List<DetalleTransaccion> detalleTransaccionList;
    private List<DetalleComprobantePago> detalleComprobantePagoList;
    private List<BeneficiarioComprobantePago> beneficiarioComprobantePagoList;
    private List<BeneficiarioComprobantePago> beneficiariosAnuladosSeleccionados;
    private List<CatalogoItem> enlacesComprobantePago;
    private List<CatalogoItem> subEnlaces;
    private List<CuentaContable> listaCuentaContables;

    /*Variables*/
    private Boolean ocultarTabla;
    private Boolean ocultarBotonesAcciones;
    private Boolean botonCuentaBanco;
    private Boolean fielsetDiarioGeneral;
    private Boolean fielsetEnlaces;
    private Boolean botonAcciones;
    private double totalDebe;
    private double totalHaber;
    private double totalValor;
    private double totalValorBeneficiarios;
    private String observasciones;
    private String nombreBeneficiario;
    private boolean btncuentacontable;
    private boolean btnCompletarTarea, btniniciar;

    @PostConstruct
    public void initialize() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.opcionBusqueda = new OpcionBusqueda();
                this.comprobantePagoLazy = new LazyModel<>(ComprobantePago.class);
                this.comprobantePagoLazy.getSorteds().put("id", "ASC");
                this.comprobantePagoLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
                this.comprobantePagoLazy.getFilterss().put("numeroTramite", tramite.getNumTramite());
                this.cuentaBancariaLazyModel = new LazyModel<>(CuentaBancaria.class);
                this.cuentaBancariaLazyModel.getSorteds().put("id", "ASC");
                this.cuentaBancariaLazyModel.getFilterss().put("estado", true);
                this.cuentaBancariaLazyModel.getFilterss().put("tipoCuenta", true);
                this.enlacesComprobantePago = catalogoService.getItemsByCatalogo("enlace_comprobante_pago");
                btncuentacontable = false;
                listaCuentaContables = new ArrayList<>();
                verificador(BigInteger.valueOf(tramite.getNumTramite()));
                verificadorInicializadorCP(BigInteger.valueOf(tramite.getNumTramite()));
                vaciarFormulario("REINICIAR");
                servidor = (Servidor) getTramiteService().getEntityManager().find(Class.forName(tramite.getNameReferencia()), tramite.getIdReferencia().longValue());
                addBeneficiario(servidor);
                nombreBeneficiario = servidor.getPersona().getNombreCompleto();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void addBeneficiario(Servidor beneficiario) {
        beneficiarioComprobantePagoList = new ArrayList<>();
        BeneficiarioComprobantePago beneficiarioComprobante = new BeneficiarioComprobantePago();
        beneficiarioComprobante.setBeneficiario(beneficiario.getPersona());
        beneficiarioComprobante.setTipoBeneficiario(Boolean.FALSE);
        DetalleBanco detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiarioComprobante.getBeneficiario(), beneficiarioComprobante.getTipoBeneficiario());
        if (detalleBanco != null) {
            beneficiarioComprobante.setDetalleBanco(detalleBanco);
        }
        beneficiarioComprobante.setNumeroTransferencia(BigInteger.valueOf(ultimoBeneficiarioComprobanteAnterior() + 1));
        beneficiarioComprobante.setValor(BigDecimal.ZERO);
        beneficiarioComprobantePagoList.add(beneficiarioComprobante);
    }

    public void verificador(BigInteger num) {
        List<ComprobantePago> lista = comprobantePagoService.getVerificadorComprobantePago(num, "");
        if (lista.isEmpty()) {
            btnCompletarTarea = true;
        } else {
            btnCompletarTarea = false;
        }
    }

    public void verificadorInicializadorCP(BigInteger num) {
        List<ComprobantePago> lista = comprobantePagoService.getVerificadorComprobantePago(num, "REGISTRADO");
        if (lista.isEmpty()) {
            btniniciar = false;
        } else {
            btniniciar = true;
        }
    }

    public void form(ComprobantePago comprobante, String accion) {
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
            this.comprobantePago.setPeriodo(opcionBusqueda.getAnio());
            this.comprobantePago.setFechaComprobante(new Date());
            this.comprobantePago.setDetalle("PROCESO " + tramite.getTipoTramite().getDescripcion().toUpperCase() + ",TRÁMITE " + tramite.getNumTramite() + "-" + comprobantePago.getPeriodo() + ", ");
            ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(opcionBusqueda.getAnio());
            if (ultimoComprobantePago != null) {
                comprobantePago.setNumComprobante(BigInteger.valueOf(ultimoComprobantePago.getNumComprobante().longValue() + 1));
            } else {
                comprobantePago.setNumComprobante(BigInteger.valueOf(1));
            }
            if ("NUEVO".equals(accion)) {
                this.ocultarTabla = Boolean.FALSE;
            } else {
                vaciarFormulario(accion);
            }
        }
        actualizarFormulario(true);
        PrimeFaces.current().ajax().update("formMain");
    }

    public void saveComprobante() {
        boolean edit = comprobantePago.getId() != null;
        if (edit) {

        } else {
            if (detalleComprobantePagoList.isEmpty()) {
                JsfUtil.addWarningMessage("COMPROBANTE DE PAGO", "Debe ingresar las cuentas del comprobante de pago antes de guardar");
                return;
            }
            if (comprobantePago.getDetalle().equals("")) {
                JsfUtil.addWarningMessage("COMPROBANTE DE PAGO", "Debe ingresar el detalle del comprobante de pago antes de guardar");
                return;
            }
            Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(comprobantePago.getFechaComprobante()), Utils.convertirMesALetra(Utils.getMes(comprobantePago.getFechaComprobante())));
            if (!periodoAbierto) {
                JsfUtil.addWarningMessage("AVISO", "Es imposible guardar porque la fecha seleccionada seleccionado esta cerrado");
                return;
            } else {
                if (totalDebe != totalHaber || totalDebe == 0 || totalHaber == 0) {
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
                comprobantePago.setPeriodo(opcionBusqueda.getAnio());
                comprobantePago.setDetalle(comprobantePago.getDetalle().toUpperCase());
                comprobantePago.setNumeroTramite(BigInteger.valueOf(tramite.getNumTramite()));
                if (enlaceSeleccionado.getId() != null) {
                    comprobantePago.setEnlace(enlaceSeleccionado);
                    if (subEnlaceSeleccionado.getId() != null) {
                        comprobantePago.setEnlace(subEnlaceSeleccionado);
                    }
                }
                comprobantePagoService.create(comprobantePago);
                /*GUARDAR EL DETALLE DEL COMPROBANTE DE PAGO*/
                for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                    detalleComprobante.setComprobantePago(comprobantePago);
                    detalleComprobantePagoService.create(detalleComprobante);
                }
                for (BeneficiarioComprobantePago beneficiarioComprobante : beneficiarioComprobantePagoList) {
                    beneficiarioComprobante.setComprobantePago(comprobantePago);
                    beneficiarioComprobante.setEstadoBeneficiario("REGISTRADO");
                    beneficiarioComprobantePagoService.create(beneficiarioComprobante);
                }
            }
            this.ocultarTabla = Boolean.TRUE;
            verificador(BigInteger.valueOf(tramite.getNumTramite()));
            verificadorInicializadorCP(BigInteger.valueOf(tramite.getNumTramite()));
            imprimirReporteComprobante(comprobantePago);
            vaciarFormulario("REINICIAR");
            PrimeFaces.current().ajax().update("comprobantePagoTable");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void anularComprobante(ComprobantePago comprobante) {
        this.totalDebe = 0;
        this.totalHaber = 0;
        /*EDITAMOS EL ESTADO DEL COMPROBANTE DE PAGO*/
        comprobante.setEstado("ANULADO");
        comprobante.setUsuarioModificacion(userSession.getNameUser());
        comprobante.setFechaModificacion(new Date());
        if (comprobante.getEnlace() != null) {
            if (comprobante.getEnlace().getCodigo().equals("")) {
                int indice = anticipoService.getUpdateAnticipo(comprobante);
            }
        }
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
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        if (ultimaActa != null) {
            diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(1));
        }
        /*CREAMOS UNA TRANSACCION EN EL LIBRO DIARIO*/
        diarioGeneral.setObservacion("ANULACIÓN CP No." + comprobante.getNumComprobante() + ", " + comprobante.getDetalle());
        diarioGeneral.setUsuarioCreacion(userSession.getNameUser());
        diarioGeneral.setFechaCreacion(new Date());
        diarioGeneral.setEstado(Boolean.TRUE);
        diarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneral.setEstadoTransaccion("CUADRADO");
        diarioGeneral.setFechaElaboracion(new Date());
        diarioGeneral = diarioGeneralService.create(diarioGeneral);
        /*CREAMOS LOS DETALLES DE LA TRANSACCION*/
        int contador = 0;
        for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
            DetalleTransaccion detalleTransaccion = new DetalleTransaccion();
            detalleTransaccion.setDiarioGeneral(diarioGeneral);
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

        diarioGeneral.setTotalDebe(new BigDecimal(totalDebe));
        diarioGeneral.setTotalHaber(new BigDecimal(totalHaber));
        diarioGeneralService.edit(diarioGeneral);
        /*IMPRIMIMOS EL REPORTE DEL COMPROBANTE DE PAGO*/
        imprimirReporteComprobante(comprobante);
        PrimeFaces.current().executeScript("PF('comprobanteDiarioGeneralDLG').show()");
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

    public void buscarDiarioGeneral(String accion) {
        if (accion.equals("BOTONBUSQUEDA")) {
            this.detalleComprobantePagoList = new ArrayList<>();
            this.beneficiarioComprobantePagoList = new ArrayList<>();
        }
        if (diarioGeneral.getNumeroTransaccion() != null) {
            DiarioGeneral diario = comprobantePagoService.getDiarioGeneral(diarioGeneral.getNumeroTransaccion(), opcionBusqueda.getAnio(), "CUADRADO", "clase_egreso", "tipo_financiero");
            if (diario != null) {
                DiarioGeneralSeleccionado(diario, "INGRESO");
            } else {
                PrimeFaces.current().ajax().update("diarioGeneralForm");
                PrimeFaces.current().executeScript("PF('diarioGeneralDLG').show()");
            }
        } else {
            PrimeFaces.current().ajax().update("diarioGeneralForm");
            PrimeFaces.current().executeScript("PF('diarioGeneralDLG').show()");
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
                                if (diferencia >= detalle2.getDebe().doubleValue()) {
                                    detalle2.setHaber(detalle2.getDebe());
                                    detalleComprobantePago.setEjecutado(detalle2.getDebe());
                                    diferencia = diferencia - detalleComprobantePago.getEjecutado().doubleValue();
                                    detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                    bandera = true;
                                } else {
                                    if (diferencia != 0) {
                                        detalleComprobantePago.setEjecutado(new BigDecimal(diferencia));
                                        diferencia = diferencia - detalleComprobantePago.getEjecutado().doubleValue();
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
                                    detalleComprobantePagoList.add(detalleComprobantePago);
                                }
                            }
                        }
                    }
                }
            }
            actualizarTotales();
            /*AGREGAR LOS BENEFICIARIOS*/
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
                    beneficiarioComprobantePago.setNumeroTransferencia(BigInteger.valueOf(contador + 1));
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
                beneficiarioComprobantePago.setValor(new BigDecimal(totalValor));
                beneficiarioComprobantePagoList.add(beneficiarioComprobantePago);
            }
            actualizarTotalBeneficiario();
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
            beneficiario.setValor(new BigDecimal(totalDebe));
            this.totalValorBeneficiarios = Math.round((totalValorBeneficiarios + beneficiario.getValor().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
        }
    }

    public void imprimirReporteComprobante(ComprobantePago comprobantePago) {
        servletSession.addParametro("id_comprobante_pago", comprobantePago.getId());
        servletSession.setNombreReporte("ComprobantePago_Registrado");
        servletSession.setNombreSubCarpeta("ComprobantePago");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public void imprimirReporteDiarioGeneral(DiarioGeneral diarioGeneral) {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public void vaciarFormulario(String accion) {
        this.ocultarTabla = Boolean.TRUE;
        this.botonAcciones = Boolean.TRUE;
        this.ocultarBotonesAcciones = Boolean.TRUE;
        this.botonCuentaBanco = Boolean.FALSE;
        this.fielsetDiarioGeneral = Boolean.FALSE;
        this.fielsetEnlaces = Boolean.FALSE;
        this.diarioGeneral = new DiarioGeneral();
        this.beneficiarioComprobantePago = new BeneficiarioComprobantePago();
        this.enlaceSeleccionado = new CatalogoItem();
        this.subEnlaceSeleccionado = new CatalogoItem();
        this.subEnlaces = new ArrayList<>();
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

    public void actualizarFormulario(Boolean accion) {
        if (accion) {
            this.fielsetDiarioGeneral = Boolean.FALSE;
            this.fielsetEnlaces = Boolean.TRUE;
            this.enlaceSeleccionado = catalogoService.getItemByCatalogoAndCodigo("enlace_comprobante_pago", "enlace_financiero");
            this.subEnlaceSeleccionado = catalogoItemService.getPadreCatalogoItem(enlaceSeleccionado, "cp_pago_anticipo_viaticos");
        } else {
            this.fielsetDiarioGeneral = Boolean.TRUE;
            this.fielsetEnlaces = Boolean.FALSE;
        }
        this.botonAcciones = Boolean.FALSE;
        PrimeFaces.current().ajax().update("enlacesFielset");
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
        ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(opcionBusqueda.getAnio());
        if (ultimoComprobantePago != null) {
            BeneficiarioComprobantePago beneficiarioComprobante = detalleComprobantePagoService.getUltimoNumeroReferencia(ultimoComprobantePago);
            if (beneficiarioComprobante != null) {
                contador = beneficiarioComprobante.getNumeroTransferencia().intValue();
            }
        }
        return contador;
    }

    public void dlogObser() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observasciones);
            PrimeFaces.current().executeScript("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            getParamts().put("usuarioTransferencia", clienteService.getrolsUser(RolUsuario.tesorero)); //TESORERIA 5
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            comprobantePago = new ComprobantePago();
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envio correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void showCuentasContables() {
        listaCuentaContables = new ArrayList<>();
        listaCuentaContables = comprobantePagoService.getlistaCuentasContables(comprobantePago.getPeriodo());
        PrimeFaces.current().executeScript("PF('DlogoCuentasContables').show()");
        PrimeFaces.current().ajax().update(":formcc");
    }

    public void aniadirCuentaContable(CuentaContable cuentaContable) {
        DetalleComprobantePago detalleCP = new DetalleComprobantePago();
        detalleCP.setLinea(BigInteger.valueOf(detalleComprobantePagoList.size() + 1));
        detalleCP.setCuentaContable(cuentaContable);
        detalleComprobantePagoList.add(detalleCP);
        PrimeFaces.current().executeScript("PF('DlogoCuentasContables').hide()");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
    }

    public void actualizarValoresDebeHaber(DetalleComprobantePago detalleCP, Boolean tipoIngreso) {
        if (tipoIngreso) {
            detalleCP.setHaber(BigDecimal.ZERO);
        } else {
            detalleCP.setDebe(BigDecimal.ZERO);
        }
        actualizarTotales();
        actualizarTotalBeneficiario();
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
        PrimeFaces.current().ajax().update("beneficiarioTable");
    }

    public void ingresoDebeHaber(DetalleComprobantePago detalleCP, Boolean tipoIngreso) {
        detalleCP.setTipoPago(null);
        detalleCP.setPartidaPresupuestaria(null);
        detalleCP.setEstructuraProgramatica(null);
        detalleCP.setFuente(null);
        detalleCP.setCedulaPresupuestaria(null);
        detalleCP.setEjecutado(BigDecimal.ZERO);
        if (tipoIngreso) {
            detalleCP.setHaber(BigDecimal.ZERO);
        } else {
            detalleCP.setDebe(BigDecimal.ZERO);
        }
        actualizarTotales();
        actualizarTotalBeneficiario();
    }

    /*Get - n- Set*/
    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public CatalogoItem getSubEnlaceSeleccionado() {
        return subEnlaceSeleccionado;
    }

    public void setSubEnlaceSeleccionado(CatalogoItem subEnlaceSeleccionado) {
        this.subEnlaceSeleccionado = subEnlaceSeleccionado;
    }

    public List<CatalogoItem> getSubEnlaces() {
        return subEnlaces;
    }

    public void setSubEnlaces(List<CatalogoItem> subEnlaces) {
        this.subEnlaces = subEnlaces;
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

    public boolean isBtncuentacontable() {
        return btncuentacontable;
    }

    public void setBtncuentacontable(boolean btncuentacontable) {
        this.btncuentacontable = btncuentacontable;
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

    public Boolean getFielsetDiarioGeneral() {
        return fielsetDiarioGeneral;
    }

    public void setFielsetDiarioGeneral(Boolean fielsetDiarioGeneral) {
        this.fielsetDiarioGeneral = fielsetDiarioGeneral;
    }

    public Boolean getFielsetEnlaces() {
        return fielsetEnlaces;
    }

    public void setFielsetEnlaces(Boolean fielsetEnlaces) {
        this.fielsetEnlaces = fielsetEnlaces;
    }

    public Boolean getBotonAcciones() {
        return botonAcciones;
    }

    public void setBotonAcciones(Boolean botonAcciones) {
        this.botonAcciones = botonAcciones;
    }

    public CatalogoItem getEnlaceSeleccionado() {
        return enlaceSeleccionado;
    }

    public void setEnlaceSeleccionado(CatalogoItem enlaceSeleccionado) {
        this.enlaceSeleccionado = enlaceSeleccionado;
    }

    public List<CatalogoItem> getEnlacesComprobantePago() {
        return enlacesComprobantePago;
    }

    public void setEnlacesComprobantePago(List<CatalogoItem> enlacesComprobantePago) {
        this.enlacesComprobantePago = enlacesComprobantePago;
    }

    public String getObservasciones() {
        return observasciones;
    }

    public void setObservasciones(String observasciones) {
        this.observasciones = observasciones;
    }

    public boolean isBtnCompletarTarea() {
        return btnCompletarTarea;
    }

    public void setBtnCompletarTarea(boolean btnCompletarTarea) {
        this.btnCompletarTarea = btnCompletarTarea;
    }

    public List<CuentaContable> getListaCuentaContables() {
        return listaCuentaContables;
    }

    public void setListaCuentaContables(List<CuentaContable> listaCuentaContables) {
        this.listaCuentaContables = listaCuentaContables;
    }

    public boolean isBtniniciar() {
        return btniniciar;
    }

    public void setBtniniciar(boolean btniniciar) {
        this.btniniciar = btniniciar;
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

//</editor-fold>
}
