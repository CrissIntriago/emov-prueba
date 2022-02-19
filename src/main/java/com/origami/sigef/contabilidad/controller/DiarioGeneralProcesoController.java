/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.GarantiaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named(value = "diarioGeneralProcesoGarantiaView")
@ViewScoped
public class DiarioGeneralProcesoController extends BpmnBaseRoot implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Inject">
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private CatalogoItemService catalogoItemService;
//</editor-fold>

    /*OBJECTOS*/
    private OpcionBusqueda opcionBusqueda;
    private DiarioGeneral diarioGeneral;
    private CuentaContable cuentaContableSeleccionado;
    private CatalogoMovimiento catalogoMovimientoSeleccionado;
    private DetalleTransferencias transferenciaAnuladaSeleccionada;
    private CatalogoItem tipoSeleccionado;
    private DetalleTransaccion detalleTransaccion;
    private Presupuesto presupuestoSeleccionado;

    /*LISTAS*/
    private List<DetalleTransaccion> detalleDiarioGeneral;
    private List<CatalogoItem> clasesDiarioGeneral;
    private List<CatalogoItem> tiposDiarioGeneral;
    private List<CatalogoItem> modulosDeEnlaces;
    private List<CatalogoMovimiento> motivoMovimientosList;
    private List<CuentaContablePresupuestoModel> cuentaContablePresupuestoModelList;
    private List<CuentaContablePresupuestoModel> cuentasConPartida;
    private List<CuentaContable> relacionCuentasContables;
    private List<DetalleTransaccion> partidaEstructura;
    private List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas;
    private List<Presupuesto> presupuestoRelacionado;
    private List<String> periodosFiscales;

    /*CONTROLADORES DE BOTONES*/
    private Boolean formDiarioGeneral;
    private Boolean botonCuentasContables;
    private Boolean botonNuevoEnlace;
    private Boolean botonEnlaces;
    private Boolean disableClaseDiarioGeneral;
    private Boolean disableTipoDiarioGeneral;
    private Boolean botonPartidasPresupuestarias;
    private Boolean detalleDiarioTableView;
    private Boolean detalleDiarioTableEdit;

    /*TOTALES*/
    private double totalComprometido;
    private double totalDevengado;
    private double totalEjecutado;
    private double totalLiquidacion;

    /*LAZYMODELS*/
    private LazyModel<DiarioGeneral> diarioGeneralLazyModel;
    private LazyModel<Recaudacion> recaudacionlLazyModel;
    private LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel;
    private LazyModel<CuentaContable> cuentaContableLazyModel;
    private LazyModel<Inventario> inventarioLazyModel;
    private LazyModel<DetalleTransferencias> detalleTransferenciaLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<Presupuesto> presupuestoLazyModel;
    private LazyModel<Adquisiciones> adquisicionesLazyModel;
    private TareasActivas tareasActivas;

    @Inject
    private GarantiaService garantiaService;

    @PostConstruct
    public void initialize() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
            }
        }
        this.opcionBusqueda = new OpcionBusqueda();
        this.diarioGeneralLazyModel = new LazyModel<>(DiarioGeneral.class);
        this.diarioGeneralLazyModel.getSorteds().put("id", "ASC");
        this.diarioGeneralLazyModel.getFilterss().put("estado", true);
        tareasActivas = new TareasActivas();
        if (this.session.getVarTemp() instanceof TareasActivas) {
            tareasActivas = (TareasActivas) this.session.getVarTemp();
            this.diarioGeneralLazyModel.getFilterss().put("numTramite", tareasActivas.getNumTramite());
            this.diarioGeneralLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        }
        this.clasesDiarioGeneral = catalogoService.getItemsByCatalogo("diario_general_clases");
        this.modulosDeEnlaces = catalogoService.getItemsByCatalogo("diario_general_modulos_enlace");
        reiniciarBotones();
    }

    public void form(DiarioGeneral diarioGeneralIntegrado, String accion) {
        this.diarioGeneral = new DiarioGeneral();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.formDiarioGeneral = Boolean.FALSE;
        this.detalleDiarioTableView = Boolean.FALSE;
        this.detalleDiarioTableEdit = Boolean.TRUE;
        if (diarioGeneralIntegrado != null) {
            this.diarioGeneral = diarioGeneralIntegrado;
            Boolean accion_detalle = false;
            if (accion.equals("visualizar")) {
                this.detalleDiarioTableView = Boolean.TRUE;
                this.detalleDiarioTableEdit = Boolean.FALSE;
                accion_detalle = false;
            }
            if (accion.equals("editar")) {
                this.botonCuentasContables = Boolean.TRUE;
                if (diarioGeneral.getClase().getCodigo().equals("clase_diario") && diarioGeneral.getTipo().getCodigo().equals("tipo_presupuesto")) {
                    this.detalleDiarioTableView = Boolean.FALSE;
                    this.detalleDiarioTableEdit = Boolean.FALSE;
                } else {
                    this.detalleDiarioTableView = Boolean.FALSE;
                    this.detalleDiarioTableEdit = Boolean.TRUE;
                }
                accion_detalle = true;
            }
            List<DetalleTransaccion> auxiliar = detalleTransaccionService.getDetalleTransaccion(diarioGeneral);
            aggDetalle(accion_detalle, auxiliar);
            this.botonNuevoEnlace = Boolean.FALSE;
            actualizarTipoDiarioGeneral();
            calcularTotalesDetalleDiarioGeneral();
        } else {
            registrarDiarioGeneral();
        }
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
        PrimeFaces.current().ajax().update("tableDiarioGeneral");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralView");
    }

    private void aggDetalle(Boolean accion, List<DetalleTransaccion> auxiliar) {
        for (DetalleTransaccion detalleDG : auxiliar) {
            if (accion) {
                if (detalleDG.getCedulaPresupuestaria() != null) {
                    List<CuentaContable> auxiliarList = diarioGeneralService.getCuentasContables(detalleDG.getPartidaPresupuestaria(), opcionBusqueda.getAnio());
                    if (auxiliarList != null) {
                        detalleDG.setSeleccionarCuentaContable(auxiliarList);
                    } else {
                        detalleDG.setSeleccionarCuentaContable(new ArrayList<>());
                    }
                    if (!detalleDG.getSeleccionarCuentaContable().contains(detalleDG.getCuentaContable())) {
                        detalleDG.getSeleccionarCuentaContable().add(detalleDG.getCuentaContable());
                    }
                }
            }
            detalleDiarioGeneral.add(detalleDG);
        }
    }

    public void updialogObservacion(DiarioGeneral d) {
        diarioGeneral = d;
        observacion.setEstado(Boolean.TRUE);
        observacion.setFecCre(new Date());
        observacion.setUserCre(this.session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTask() {
        try {
            getParamts().put("contabilidadR", clienteService.getrolsUser(RolUsuario.contador));
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception ex) {
            Logger.getLogger(DiarioGeneralProcesoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void datosPrecargadosDiarioGeneral() {
        this.diarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        this.diarioGeneral.setFechaElaboracion(new Date());
        this.diarioGeneral.setTotalDebe(BigDecimal.ZERO);
        this.diarioGeneral.setTotalHaber(BigDecimal.ZERO);
    }

    private void numActaDiarioGeneral() {
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        if (ultimaActa != null) {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(1));
        }
    }

    public void save() {
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion())));
        if (!periodoAbierto) {
            JsfUtil.addWarningMessage("AVISO", "Es imposible guardar porque la fecha seleccionada esta cerrado");
            return;
        }
        if (diarioGeneral.getObservacion() == null || diarioGeneral.getObservacion().isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "Debe ingresar el detalle del diario general");
            return;
        }
        if (detalleDiarioGeneral.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay detalle en el asiento contable");
            return;
        }
        if (diarioGeneral.getClase() == null || diarioGeneral.getTipo() == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar un tipo y una clase para el diario General");
            return;
        }
        diarioGeneral.setObservacion(diarioGeneral.getObservacion().toUpperCase());
        if (diarioGeneral.getId() != null) {
            diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
            diarioGeneral.setFechaModificacion(new Date());
            if (botonPartidasPresupuestarias) {
                diarioGeneral.setTotalDebe(BigDecimal.ZERO);
                diarioGeneral.setTotalHaber(BigDecimal.ZERO);
            }
            diarioGeneralService.edit(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                if (transaccion.getId() != null) {
                    detalleTransaccionService.edit(transaccion);
                } else {
                    transaccion.setDiarioGeneral(diarioGeneral);
                    transaccion.setDatoCargado(Boolean.TRUE);
                    detalleTransaccionService.create(transaccion);
                }
            }
        } else {
            if ((diarioGeneral.getClase().getCodigo().equals("clase_egreso") || diarioGeneral.getClase().getCodigo().equals("clase_diario")) && diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                diarioGeneral.setRetencion(Boolean.TRUE);
                diarioGeneral.setRetenido(Boolean.FALSE);
            }
            diarioGeneral.setUsuarioCreacion(userSession.getNameUser());
            diarioGeneral.setFechaCreacion(new Date());
            diarioGeneral.setEstado(Boolean.TRUE);
            diarioGeneral.setObservacion(diarioGeneral.getObservacion().toUpperCase());
            diarioGeneral.setEstadoDiario("REGISTRADO");
            if (botonPartidasPresupuestarias) {
                diarioGeneral.setTotalDebe(BigDecimal.ZERO);
                diarioGeneral.setTotalHaber(BigDecimal.ZERO);
            }
            diarioGeneral.setNumTramite(tareasActivas.getNumTramite() == null ? null : tareasActivas.getNumTramite());
            numActaDiarioGeneral();
            diarioGeneral = diarioGeneralService.create(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                transaccion.setDiarioGeneral(diarioGeneral);
                transaccion.setDatoCargado(Boolean.TRUE);
                if (botonPartidasPresupuestarias) {
                    transaccion.setHaber(BigDecimal.ZERO);
                }
                detalleTransaccionService.create(transaccion);
            }
        }
        imprimirDiarioGeneral();
        cancelar();
    }

    public void cancelar() {
        reiniciarBotones();
        reiniciarValoresTotales();
        reiniciarCuentaContable();
        this.diarioGeneral = new DiarioGeneral();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.tiposDiarioGeneral = new ArrayList<>();
        this.cuentasConPartida = new ArrayList<>();
        this.inventarioLazyModel = null;
        this.corteOrdenCobroLazyModel = null;
        this.recaudacionlLazyModel = null;
        this.detalleTransferenciaLazyModel = null;
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("tableDiarioGeneral");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralView");
    }

    public void imprimirDiarioGeneral() {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void quitarDetalleDiarioGeneral(DetalleTransaccion detalleDG) {
        int index = 0;
        for (DetalleTransaccion detalle : detalleDiarioGeneral) {
            if (detalle.getContador() == detalleDG.getContador()) {
                break;
            }
            index = index + 1;
        }
        detalleDiarioGeneral.remove(index);
        int contador = 0;
        for (DetalleTransaccion detalle : detalleDiarioGeneral) {
            contador = contador + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            detalle.setContador(bigInteger);
        }
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
    }

    public void registrarDiarioGeneral() {
        this.botonNuevoEnlace = Boolean.FALSE;
        datosPrecargadosDiarioGeneral();
        diarioGeneral.setClase(catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("diario_general_clases", "clase_diario"));
        diarioGeneral.setTipo(catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("diario_general_tipos", "tipo_ajuste"));
        // SI VIENE POR PROCESO DE GARANTIA SE TRAE LA CUENTA CONTABLE CON SU VALORES Y CONCEPTO. 
        actualizarTipoDiarioGeneral();
        loadGarantiaProceso();
        this.disableClaseDiarioGeneral = Boolean.FALSE;
        this.disableTipoDiarioGeneral = Boolean.FALSE;
        this.botonCuentasContables = Boolean.TRUE;
    }

    public void loadGarantiaProceso() {
        if (tareasActivas != null && tareasActivas.getNumTramite() != null) {
            Garantias garantia = garantiaService.findGarantiaByNumTramite(tareasActivas.getNumTramite());
            if (garantia != null && garantia.getId() != null) {
                DetalleTransaccion detalleDG = new DetalleTransaccion();
                int contador = detalleDiarioGeneral.size() + 1;
                BigInteger bigInteger = BigInteger.valueOf(contador);
                detalleDG.setContador(bigInteger);
//                detalleDG.setCuentaContable(garantia.getCuentaContable());
                detalleDG.setDebe(garantia.getSuma());
                detalleDG.setHaber(BigDecimal.ZERO);

                //PARA LA OBSERVACION DEL DIARIO GENERAL 
                diarioGeneral.setObservacion(garantia.getTipoDocumento().getTexto() + " Para " + garantia.getRiesgoAsegurado().getTexto()
                        + " " + (garantia.getAdquisicion() != null ? garantia.getAdquisicion().getNumeroContrato() : " ") + " " + garantia.getDetalle());

                detalleDiarioGeneral.add(detalleDG);
                calcularTotalesDetalleDiarioGeneral();
                this.detalleTransaccion = new DetalleTransaccion();
                PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
                PrimeFaces.current().ajax().update("observacionDiarioGeneral");
            }
        }
    }

    public void actualizarTipoDiarioGeneral() {
        if (diarioGeneral.getClase() != null) {
            this.tiposDiarioGeneral = catalogoService.getTiposDiarioGeneral(diarioGeneral.getClase(), "diario_general_tipos");
        } else {
            this.tiposDiarioGeneral = new ArrayList<>();
        }
    }

    public void actualizarBotonCuentasContables() {
        if (diarioGeneral.getClase() != null && diarioGeneral.getTipo() != null) {
            if (diarioGeneral.getClase().getCodigo().equals("clase_diario") && diarioGeneral.getTipo().getCodigo().equals("tipo_presupuesto")) {
                this.botonCuentasContables = Boolean.FALSE;
                this.botonPartidasPresupuestarias = Boolean.TRUE;
            } else {
                this.botonCuentasContables = Boolean.TRUE;
                this.botonPartidasPresupuestarias = Boolean.FALSE;
            }
        } else {
            this.botonCuentasContables = Boolean.FALSE;
            this.botonPartidasPresupuestarias = Boolean.FALSE;
        }
    }

    private void reiniciarBotones() {
        this.formDiarioGeneral = Boolean.TRUE;
        this.botonCuentasContables = Boolean.FALSE;
        this.botonNuevoEnlace = Boolean.TRUE;
        this.botonEnlaces = Boolean.FALSE;
        this.disableClaseDiarioGeneral = Boolean.TRUE;
        this.disableTipoDiarioGeneral = Boolean.TRUE;
        this.detalleDiarioTableView = Boolean.FALSE;
        this.detalleDiarioTableEdit = Boolean.TRUE;
        this.botonPartidasPresupuestarias = Boolean.FALSE;
    }

    private void calcularTotalesDetalleDiarioGeneral() {
        reiniciarValoresTotales();
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            this.diarioGeneral.setTotalDebe(new BigDecimal(redondearDosDecimales(this.diarioGeneral.getTotalDebe().doubleValue() + detalleDG.getDebe().doubleValue())));
            this.diarioGeneral.setTotalHaber(new BigDecimal(redondearDosDecimales(this.diarioGeneral.getTotalHaber().doubleValue() + detalleDG.getHaber().doubleValue())));
            this.totalComprometido = redondearDosDecimales(totalComprometido + detalleDG.getComprometido().doubleValue());
            this.totalDevengado = redondearDosDecimales(totalDevengado + detalleDG.getDevengado().doubleValue());
            this.totalEjecutado = redondearDosDecimales(totalEjecutado + detalleDG.getEjecutado().doubleValue());
        }
        if (this.diarioGeneral.getTotalDebe().doubleValue() != this.diarioGeneral.getTotalHaber().doubleValue()) {
            this.diarioGeneral.setEstadoTransaccion("DESCUADRADO");
        } else {
            this.diarioGeneral.setEstadoTransaccion("CUADRADO");
        }
    }

    private double redondearDosDecimales(double valor) {
        return Math.round(valor * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    private void reiniciarValoresTotales() {
        this.diarioGeneral.setTotalDebe(BigDecimal.ZERO);
        this.diarioGeneral.setTotalHaber(BigDecimal.ZERO);
        this.totalComprometido = 0;
        this.totalDevengado = 0;
        this.totalEjecutado = 0;
    }

    public void openDlgCuentasContables() {
        reiniciarCuentaContable();
        this.cuentaContableLazyModel = new LazyModel<>(CuentaContable.class);
        this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
        this.cuentaContableLazyModel.getFilterss().put("estado", true);
        this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
    }

    private Boolean calcularHaberTotalPartidasRepetidas() {
        double sumatoriaHaber = 0;
        double sumatoriaDebe = 0;
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            if (detalleDG.getPartidaPresupuestaria() != null) {
                if (detalleDG.getPartidaPresupuestaria().getCodigo().substring(0, 2).equals(detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                    sumatoriaDebe = sumatoriaDebe + detalleDG.getDebe().doubleValue();
                }
            }
            if (detalleDG.getCuentaContable().getCodigo().substring(3, 5).equals(detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                sumatoriaHaber = sumatoriaHaber + detalleDG.getHaber().doubleValue();
            }
        }
        double total = sumatoriaDebe - sumatoriaHaber;
        detalleTransaccion.setHaber(new BigDecimal(total));
        return total == 0;
    }

    public void añadirCuentaContable() {
        DetalleTransaccion detalleDG = new DetalleTransaccion();
        int contador = detalleDiarioGeneral.size() + 1;
        BigInteger bigInteger = BigInteger.valueOf(contador);
        detalleDG.setContador(bigInteger);
        detalleDG.setCuentaContable(cuentaContableSeleccionado);
        detalleDG.setDebe(BigDecimal.ZERO);
        if (detalleTransaccion != null) {
            if (detalleTransaccion.getHaber() != null) {
                detalleDG.setHaber(detalleTransaccion.getHaber());
            } else {
                detalleDG.setHaber(BigDecimal.ZERO);
            }
        } else {
            detalleDG.setHaber(BigDecimal.ZERO);
        }
        detalleDiarioGeneral.add(detalleDG);
        calcularTotalesDetalleDiarioGeneral();
        this.detalleTransaccion = new DetalleTransaccion();
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
    }

    private void reiniciarCuentaContable() {
        this.cuentaContableSeleccionado = new CuentaContable();
    }

    public void consultarPeriodoElegido() {
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion())));
        if (!periodoAbierto) {
            JsfUtil.addWarningMessage("AVISO", "El mes de la fecha seleccionada no se encuentra abierto");
        }
    }

    public void quitarFiltros() {
        openDlgCuentasContables();
        PrimeFaces.current().ajax().update("cuentasPresupuestarioTable");
        PrimeFaces.current().executeScript("PF('cuentasPresupuestarioTable').clearFilters()");
    }

    //<editor-fold defaultstate="collapsed" desc="Metodo de anulación del diario general">
    public void anulacionDiarioGeneral() {
        DiarioGeneral diarioG = new DiarioGeneral();
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        if (ultimaActa != null) {
            diarioG.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            diarioG.setNumeroTransaccion(BigInteger.valueOf(1));
        }
        diarioG.setCertificacionesPresupuestario(diarioGeneral.getCertificacionesPresupuestario());
        diarioG.setClase(diarioGeneral.getClase());
        diarioG.setTipo(diarioGeneral.getTipo());
        diarioG.setObservacion("ANULACIÓN " + diarioGeneral.getObservacion());
        diarioG.setEstadoDiario("REGISTRADO");
        diarioG.setUsuarioCreacion(userSession.getNameUser());
        diarioG.setFechaCreacion(new Date());
        diarioG.setEstado(Boolean.TRUE);
        diarioG.setPeriodo(opcionBusqueda.getAnio());
        diarioG.setEstadoTransaccion(diarioGeneral.getEstadoTransaccion());
        diarioG.setFechaElaboracion(new Date());
        diarioG.setRetencion(diarioGeneral.getRetencion());
        diarioG.setRetenido(diarioGeneral.getRetenido());
        diarioG.setTotalDebe(new BigDecimal(diarioGeneral.getTotalDebe().doubleValue() * (-1)));
        diarioG.setTotalHaber(new BigDecimal(diarioGeneral.getTotalHaber().doubleValue() * (-1)));
        if (diarioGeneral.getVariosBeneficiarios() != null) {
            diarioG.setVariosBeneficiarios(diarioGeneral.getVariosBeneficiarios());
        }
        if (diarioGeneral.getBeneficiario() != null) {
            diarioG.setBeneficiario(diarioGeneral.getBeneficiario());
        }
        if (diarioGeneral.getTipoBeneficiario() != null) {
            diarioG.setTipoBeneficiario(diarioGeneral.getTipoBeneficiario());
        }
        if (diarioGeneral.getNumTramite() != null) {
            diarioG.setNumTramite(diarioGeneral.getNumTramite());
        }
        diarioG = diarioGeneralService.create(diarioG);
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            DetalleTransaccion detalleT = new DetalleTransaccion();
            detalleT.setCuentaContable(detalleDG.getCuentaContable());
            if (detalleDG.getDebe().doubleValue() > 0) {
                detalleT.setDebe(new BigDecimal(detalleDG.getDebe().doubleValue() * (-1)));
            } else {
                detalleT.setDebe(detalleDG.getDebe());
            }
            if (detalleDG.getHaber().doubleValue() > 0) {
                detalleT.setHaber(new BigDecimal(detalleDG.getHaber().doubleValue() * (-1)));
            } else {
                detalleT.setHaber(detalleDG.getHaber());
            }
            if (detalleDG.getTipoTransaccion() != null) {
                detalleT.setTipoTransaccion(detalleDG.getTipoTransaccion());
            }
            if (detalleDG.getPartidaPresupuestaria() != null) {
                detalleT.setPartidaPresupuestaria(detalleDG.getPartidaPresupuestaria());
            }
            if (detalleDG.getEstructuraProgramatica() != null) {
                detalleT.setEstructuraProgramatica(detalleDG.getEstructuraProgramatica());
            }
            if (detalleDG.getComprometido().doubleValue() > 0) {
                detalleT.setComprometido(new BigDecimal(detalleDG.getComprometido().doubleValue() * (-1)));
            } else {
                detalleT.setComprometido(detalleDG.getComprometido());
            }
            if (detalleDG.getDevengado().doubleValue() > 0) {
                detalleT.setDevengado(new BigDecimal(detalleDG.getDevengado().doubleValue() * (-1)));
            } else {
                detalleT.setDevengado(detalleDG.getDevengado());
            }
            if (detalleDG.getEjecutado().doubleValue() > 0) {
                detalleT.setEjecutado(new BigDecimal(detalleDG.getEjecutado().doubleValue() * (-1)));
            } else {
                detalleT.setEjecutado(detalleDG.getEjecutado());
            }
            if (detalleDG.getCedulaPresupuestaria() != null) {
                detalleT.setCedulaPresupuestaria(detalleDG.getCedulaPresupuestaria());
            }
            if (detalleDG.getFuente() != null) {
                detalleT.setFuente(detalleDG.getFuente());
            }
            if (detalleDG.getTipoDevengado() != null) {
                detalleT.setTipoDevengado(detalleDG.getTipoDevengado());
            }
            if (detalleDG.getComprobantePago() != null) {
                detalleT.setComprobantePago(detalleDG.getComprobantePago());
            }
            detalleT.setContador(detalleDG.getContador());
            detalleT.setDiarioGeneral(diarioG);
            detalleTransaccionService.create(detalleT);
        }
        /*Actualizar Diario Anulado*/
        diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
        diarioGeneral.setFechaModificacion(new Date());
        diarioGeneral.setFechaAnulacion(new Date());
        diarioGeneral.setEstadoDiario("ANULADO");
        diarioGeneralService.edit(diarioGeneral);
        this.diarioGeneral = diarioG;
        imprimirDiarioGeneral();
        cancelar();
    }
//</editor-fold>

    /*METODOS SOLO PARA LA PARTE PRESUPUESTARIA CUANDO ESTE SEA DIARIO - FINANCIERO*/
    //<editor-fold defaultstate="collapsed" desc="Parte Presupuestaria">
    public void openDlgPartidasPresupuestarias() {
        this.detalleDiarioTableView = Boolean.FALSE;
        this.detalleDiarioTableEdit = Boolean.FALSE;
        this.presupuestoLazyModel = new LazyModel<>(Presupuesto.class);
        this.presupuestoLazyModel.getSorteds().put("partida", "ASC");
        this.presupuestoLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        PrimeFaces.current().executeScript("PF('presupuestoDlg').show()");
        PrimeFaces.current().ajax().update("presupuestoForm");
    }

    public void añadirPartidasPresupuestarias(Presupuesto presupuesto) {
        this.disableClaseDiarioGeneral = Boolean.TRUE;
        this.disableTipoDiarioGeneral = Boolean.TRUE;
        DetalleTransaccion detalleDG = new DetalleTransaccion();
        int contador = detalleDiarioGeneral.size() + 1;
        BigInteger bigInteger = BigInteger.valueOf(contador);
        detalleDG.setContador(bigInteger);
        detalleDG.setDebe(BigDecimal.ZERO);
        detalleDG.setDatoCargado(presupuesto.getTipo());
        detalleDG.setCedulaPresupuestaria(presupuesto.getPartida());
        if (presupuesto.getItem() != null) {
            detalleDG.setPartidaPresupuestaria(presupuesto.getItem());
        }
        if (presupuesto.getEstructura() != null) {
            detalleDG.setEstructuraProgramatica(presupuesto.getEstructura());
        }
        if (presupuesto.getFuenteDirecta() != null) {
            detalleDG.setFuente(presupuesto.getFuenteDirecta());
        }
        /*Calculamos el saldo disponible de la partida seleccionada*/
        double comprometidoRegistrado = 0;
        if (detalleDiarioGeneral.isEmpty()) {
            for (DetalleTransaccion detalle : detalleDiarioGeneral) {
                if (detalle.getCedulaPresupuestaria().equals(detalleDG.getCedulaPresupuestaria())) {
                    comprometidoRegistrado = comprometidoRegistrado + detalle.getComprometido().doubleValue();
                }
            }
        }
        BigDecimal saldoReservasAprobadas = diarioGeneralService.getSaldoReservas(presupuesto.getPartida(), opcionBusqueda.getAnio(), "APRO");
        BigDecimal saldoReservasLiquidadas = diarioGeneralService.getSaldoReservas(presupuesto.getPartida(), opcionBusqueda.getAnio(), "LIQUI");
        BigDecimal saldoDevengado = diarioGeneralService.getsaldoDevengado(presupuesto.getPartida(), opcionBusqueda.getAnio());
        double saldoDisponible = presupuesto.getCodificado().doubleValue() - comprometidoRegistrado - saldoReservasAprobadas.doubleValue() - saldoReservasLiquidadas.doubleValue() - saldoDevengado.doubleValue();
        if (!detalleDG.getDatoCargado()) {
            detalleDG.setHaber(new BigDecimal(presupuesto.getCodificado().doubleValue() - saldoDevengado.doubleValue()));
        } else {
            detalleDG.setHaber(new BigDecimal(saldoDisponible));
        }
        this.detalleDiarioGeneral.add(detalleDG);
        PrimeFaces.current().executeScript("PF('presupuestoDlg').hide()");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    public void validarMontosPresupueto(DetalleTransaccion detalleDG, String accion) {
        switch (accion) {
            case "1":
                if (detalleDG.getHaber().doubleValue() < detalleDG.getComprometido().doubleValue()) {
                    JsfUtil.addWarningMessage("AVISO", "El monto del comprometido es mayor al saldo disponible");
                    detalleDG.setComprometido(BigDecimal.ZERO);
                }
                break;
            case "2":
                if (!detalleDG.getDatoCargado()) {
                    if (detalleDG.getComprometido() != null) {
                        if (detalleDG.getComprometido().doubleValue() < detalleDG.getDevengado().doubleValue()) {
                            JsfUtil.addWarningMessage("AVISO", "El monto del comprometido es mayor al saldo disponible");
                            detalleDG.setDevengado(BigDecimal.ZERO);
                        }
                    } else {
                        JsfUtil.addWarningMessage("AVISO", "Primero debe ingresar el monto comprometido");
                        detalleDG.setDevengado(BigDecimal.ZERO);
                    }
                }
                break;
            case "3":
                if (detalleDG.getDevengado() != null) {
                    if (detalleDG.getDevengado().doubleValue() < detalleDG.getEjecutado().doubleValue()) {
                        JsfUtil.addWarningMessage("AVISO", "El monto del comprometido es mayor al saldo disponible");
                        detalleDG.setEjecutado(BigDecimal.ZERO);
                    }
                } else {
                    JsfUtil.addWarningMessage("AVISO", "Primero debe ingresar el monto devengado");
                    detalleDG.setEjecutado(BigDecimal.ZERO);
                }
                break;
        }
        calcularTotalesDetalleDiarioGeneral();
    }
//</editor-fold>

    /*Determina su relacion presupuestaria al momento de ingresar el valor en el debe o en el haber*/
    //<editor-fold defaultstate="collapsed" desc="Establecer la relación presupuestaria en el ingreso del Debe - Haber">
    public void determinarRelacionPresupuestaria(DetalleTransaccion detalleDG, Boolean tipoIngreso) {
        reiniciarValoresDinamicos();
        Boolean accion = Boolean.FALSE;
        if (!detalleDG.getDatoCargado()) {
            detalleDG.setTipoTransaccion(null);
            detalleDG.setPartidaPresupuestaria(null);
            detalleDG.setEstructuraProgramatica(null);
            detalleDG.setFuente(null);
            detalleDG.setCedulaPresupuestaria(null);
            detalleDG.setComprometido(BigDecimal.ZERO);
            detalleDG.setDevengado(BigDecimal.ZERO);
            detalleDG.setEjecutado(BigDecimal.ZERO);
            CatalogoPresupuesto partidaPresupuestaria = null;
            if (tipoIngreso) {
                detalleDG.setHaber(BigDecimal.ZERO);
                if (!detalleDG.getDatoCargado()) {
                    if (diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                        if (detalleDG.getCuentaContable().getTitulo() == 1 && detalleDG.getCuentaContable().getGrupo() == 1 && detalleDG.getCuentaContable().getSubGrupo() == 3) {
                            calcularTotalesDetalleDiarioGeneral();
                            return;
                        }
                        if (detalleDG.getCuentaContable().getCtaPagarCobrar()) {
                            partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleDG.getCuentaContable());
                            if (partidaPresupuestariaRelacionadas != null) {
                                for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                                    añadirRelacionItemPresupuesto(catPresupuesto);
                                }
                            }
                        } else {
                            partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleDG.getCuentaContable(), true);
                            if (partidaPresupuestaria != null) {
                                añadirRelacionItemPresupuesto(partidaPresupuestaria);
                            }
                        }
                        if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                            if (detalleDG.getCuentaContable().getTitulo() == 2) {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                detalleDG.setEjecutado(detalleDG.getDebe());
                            } else {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                                detalleDG.setDevengado(detalleDG.getDebe());
                                detalleDG.setComprometido(detalleDG.getDebe());
                            }
                            accion = Boolean.TRUE;
                        }
                    }
                }
            } else {
                detalleDG.setDebe(BigDecimal.ZERO);
                if (!detalleDG.getDatoCargado()) {
                    if (diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                        if (detalleDG.getCuentaContable().getTitulo() == 2 && detalleDG.getCuentaContable().getGrupo() == 1 && detalleDG.getCuentaContable().getSubGrupo() == 3) {
                            calcularTotalesDetalleDiarioGeneral();
                            return;
                        }
                        if (detalleDG.getCuentaContable().getCtaPagarCobrar()) {
                            partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleDG.getCuentaContable());
                            if (partidaPresupuestariaRelacionadas != null) {
                                for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                                    añadirRelacionItemPresupuesto(catPresupuesto);
                                }
                            }
                        } else {
                            partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleDG.getCuentaContable(), false);
                            if (partidaPresupuestaria != null) {
                                añadirRelacionItemPresupuesto(partidaPresupuestaria);
                            }
                        }
                        if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                            if (detalleDG.getCuentaContable().getTitulo() == 1) {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                detalleDG.setEjecutado(detalleDG.getHaber());
                            } else {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                                detalleDG.setDevengado(detalleDG.getHaber());
                            }
                            accion = Boolean.TRUE;
                        }
                    }
                }
            }
            if (accion) {
                this.detalleTransaccion = detalleDG;
                if (presupuestoRelacionado.size() == 1) {
                    for (Presupuesto presupuesto : presupuestoRelacionado) {
                        this.presupuestoSeleccionado = presupuesto;
                        guardarRelacionesPresupuestarias();
                    }
                } else {
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                    PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
                }
            }
        }
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().ajax().update("diarioGeneralTable");

    }

    private void añadirRelacionItemPresupuesto(CatalogoPresupuesto catPresupuesto) {
        List<Presupuesto> auxiliarList = diarioGeneralService.getListadoPresupuesto(catPresupuesto, opcionBusqueda.getAnio());
        if (auxiliarList != null) {
            for (Presupuesto presupuesto : auxiliarList) {
                presupuestoRelacionado.add(presupuesto);
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            if (detalleDG.getContador().equals(detalleTransaccion.getContador())) {
                detalleDG.setPartidaPresupuestaria(presupuestoSeleccionado.getItem());
                detalleDG.setCedulaPresupuestaria(presupuestoSeleccionado.getPartida());
                detalleDG.setFuente(presupuestoSeleccionado.getFuenteDirecta());
                detalleDG.setEstructuraProgramatica(presupuestoSeleccionado.getEstructura());
            }
        }
        reiniciarValoresDinamicos();
        PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
    }

    private void reiniciarValoresDinamicos() {
        this.detalleTransaccion = new DetalleTransaccion();
        this.partidaPresupuestariaRelacionadas = new ArrayList<>();
        this.presupuestoRelacionado = new ArrayList<>();
        this.presupuestoSeleccionado = new Presupuesto();
        this.partidaEstructura = new ArrayList<>();
    }
//</editor-fold>

    /*GET - SET*/
    //<editor-fold defaultstate="collapsed" desc="Get - Set">
    public LazyModel<Adquisiciones> getAdquisicionesLazyModel() {
        return adquisicionesLazyModel;
    }

    public void setAdquisicionesLazyModel(LazyModel<Adquisiciones> adquisicionesLazyModel) {
        this.adquisicionesLazyModel = adquisicionesLazyModel;
    }

    public List<String> getPeriodosFiscales() {
        return periodosFiscales;
    }

    public void setPeriodosFiscales(List<String> periodosFiscales) {
        this.periodosFiscales = periodosFiscales;
    }

    public Presupuesto getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public DetalleTransaccion getDetalleTransaccion() {
        return detalleTransaccion;
    }

    public void setDetalleTransaccion(DetalleTransaccion detalleTransaccion) {
        this.detalleTransaccion = detalleTransaccion;
    }

    public void setPresupuestoSeleccionado(Presupuesto presupuestoSeleccionado) {
        this.presupuestoSeleccionado = presupuestoSeleccionado;
    }

    public List<CatalogoPresupuesto> getPartidaPresupuestariaRelacionadas() {
        return partidaPresupuestariaRelacionadas;
    }

    public void setPartidaPresupuestariaRelacionadas(List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas) {
        this.partidaPresupuestariaRelacionadas = partidaPresupuestariaRelacionadas;
    }

    public List<Presupuesto> getPresupuestoRelacionado() {
        return presupuestoRelacionado;
    }

    public void setPresupuestoRelacionado(List<Presupuesto> presupuestoRelacionado) {
        this.presupuestoRelacionado = presupuestoRelacionado;
    }

    public CatalogoItem getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(CatalogoItem tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<CuentaContable> getRelacionCuentasContables() {
        return relacionCuentasContables;
    }

    public void setRelacionCuentasContables(List<CuentaContable> relacionCuentasContables) {
        this.relacionCuentasContables = relacionCuentasContables;
    }

    public List<CatalogoItem> getModulosDeEnlaces() {
        return modulosDeEnlaces;
    }

    public void setModulosDeEnlaces(List<CatalogoItem> modulosDeEnlaces) {
        this.modulosDeEnlaces = modulosDeEnlaces;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public List<DetalleTransaccion> getDetalleDiarioGeneral() {
        return detalleDiarioGeneral;
    }

    public void setDetalleDiarioGeneral(List<DetalleTransaccion> detalleDiarioGeneral) {
        this.detalleDiarioGeneral = detalleDiarioGeneral;
    }

    public List<CatalogoItem> getClasesDiarioGeneral() {
        return clasesDiarioGeneral;
    }

    public void setClasesDiarioGeneral(List<CatalogoItem> clasesDiarioGeneral) {
        this.clasesDiarioGeneral = clasesDiarioGeneral;
    }

    public List<CatalogoItem> getTiposDiarioGeneral() {
        return tiposDiarioGeneral;
    }

    public void setTiposDiarioGeneral(List<CatalogoItem> tiposDiarioGeneral) {
        this.tiposDiarioGeneral = tiposDiarioGeneral;
    }

    public Boolean getFormDiarioGeneral() {
        return formDiarioGeneral;
    }

    public void setFormDiarioGeneral(Boolean formDiarioGeneral) {
        this.formDiarioGeneral = formDiarioGeneral;
    }

    public Boolean getBotonCuentasContables() {
        return botonCuentasContables;
    }

    public void setBotonCuentasContables(Boolean botonCuentasContables) {
        this.botonCuentasContables = botonCuentasContables;
    }

    public Boolean getBotonNuevoEnlace() {
        return botonNuevoEnlace;
    }

    public void setBotonNuevoEnlace(Boolean botonNuevoEnlace) {
        this.botonNuevoEnlace = botonNuevoEnlace;
    }

    public Boolean getBotonEnlaces() {
        return botonEnlaces;
    }

    public void setBotonEnlaces(Boolean botonEnlaces) {
        this.botonEnlaces = botonEnlaces;
    }

    public Boolean getDisableClaseDiarioGeneral() {
        return disableClaseDiarioGeneral;
    }

    public void setDisableClaseDiarioGeneral(Boolean disableClaseDiarioGeneral) {
        this.disableClaseDiarioGeneral = disableClaseDiarioGeneral;
    }

    public Boolean getDisableTipoDiarioGeneral() {
        return disableTipoDiarioGeneral;
    }

    public void setDisableTipoDiarioGeneral(Boolean disableTipoDiarioGeneral) {
        this.disableTipoDiarioGeneral = disableTipoDiarioGeneral;
    }

    public double getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(double totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public double getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(double totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public double getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(double totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public LazyModel<DiarioGeneral> getDiarioGeneralLazyModel() {
        return diarioGeneralLazyModel;
    }

    public void setDiarioGeneralLazyModel(LazyModel<DiarioGeneral> diarioGeneralLazyModel) {
        this.diarioGeneralLazyModel = diarioGeneralLazyModel;
    }

    public LazyModel<Recaudacion> getRecaudacionlLazyModel() {
        return recaudacionlLazyModel;
    }

    public void setRecaudacionlLazyModel(LazyModel<Recaudacion> recaudacionlLazyModel) {
        this.recaudacionlLazyModel = recaudacionlLazyModel;
    }

    public LazyModel<CorteOrdenCobro> getCorteOrdenCobroLazyModel() {
        return corteOrdenCobroLazyModel;
    }

    public void setCorteOrdenCobroLazyModel(LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel) {
        this.corteOrdenCobroLazyModel = corteOrdenCobroLazyModel;
    }

    public CuentaContable getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(CuentaContable cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }

    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }

    public Boolean getBotonPartidasPresupuestarias() {
        return botonPartidasPresupuestarias;
    }

    public void setBotonPartidasPresupuestarias(Boolean botonPartidasPresupuestarias) {
        this.botonPartidasPresupuestarias = botonPartidasPresupuestarias;
    }

    public CatalogoMovimiento getCatalogoMovimientoSeleccionado() {
        return catalogoMovimientoSeleccionado;
    }

    public void setCatalogoMovimientoSeleccionado(CatalogoMovimiento catalogoMovimientoSeleccionado) {
        this.catalogoMovimientoSeleccionado = catalogoMovimientoSeleccionado;
    }

    public List<CatalogoMovimiento> getMotivoMovimientosList() {
        return motivoMovimientosList;
    }

    public void setMotivoMovimientosList(List<CatalogoMovimiento> motivoMovimientosList) {
        this.motivoMovimientosList = motivoMovimientosList;
    }

    public LazyModel<Inventario> getInventarioLazyModel() {
        return inventarioLazyModel;
    }

    public void setInventarioLazyModel(LazyModel<Inventario> inventarioLazyModel) {
        this.inventarioLazyModel = inventarioLazyModel;
    }

    public DetalleTransferencias getTransferenciaAnuladaSeleccionada() {
        return transferenciaAnuladaSeleccionada;
    }

    public void setTransferenciaAnuladaSeleccionada(DetalleTransferencias transferenciaAnuladaSeleccionada) {
        this.transferenciaAnuladaSeleccionada = transferenciaAnuladaSeleccionada;
    }

    public LazyModel<DetalleTransferencias> getDetalleTransferenciaLazyModel() {
        return detalleTransferenciaLazyModel;
    }

    public void setDetalleTransferenciaLazyModel(LazyModel<DetalleTransferencias> detalleTransferenciaLazyModel) {
        this.detalleTransferenciaLazyModel = detalleTransferenciaLazyModel;
    }

    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoModelList() {
        return cuentaContablePresupuestoModelList;
    }

    public void setCuentaContablePresupuestoModelList(List<CuentaContablePresupuestoModel> cuentaContablePresupuestoModelList) {
        this.cuentaContablePresupuestoModelList = cuentaContablePresupuestoModelList;
    }

    public double getTotalLiquidacion() {
        return totalLiquidacion;
    }

    public void setTotalLiquidacion(double totalLiquidacion) {
        this.totalLiquidacion = totalLiquidacion;
    }

    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }

    public List<CuentaContablePresupuestoModel> getCuentasConPartida() {
        return cuentasConPartida;
    }

    public void setCuentasConPartida(List<CuentaContablePresupuestoModel> cuentasConPartida) {
        this.cuentasConPartida = cuentasConPartida;
    }

    public List<DetalleTransaccion> getPartidaEstructura() {
        return partidaEstructura;
    }

    public void setPartidaEstructura(List<DetalleTransaccion> partidaEstructura) {
        this.partidaEstructura = partidaEstructura;
    }

    public Boolean getDetalleDiarioTableView() {
        return detalleDiarioTableView;
    }

    public void setDetalleDiarioTableView(Boolean detalleDiarioTableView) {
        this.detalleDiarioTableView = detalleDiarioTableView;
    }

    public Boolean getDetalleDiarioTableEdit() {
        return detalleDiarioTableEdit;
    }

    public void setDetalleDiarioTableEdit(Boolean detalleDiarioTableEdit) {
        this.detalleDiarioTableEdit = detalleDiarioTableEdit;
    }

    public LazyModel<Presupuesto> getPresupuestoLazyModel() {
        return presupuestoLazyModel;
    }

    public void setPresupuestoLazyModel(LazyModel<Presupuesto> presupuestoLazyModel) {
        this.presupuestoLazyModel = presupuestoLazyModel;
    }
//</editor-fold>
}
