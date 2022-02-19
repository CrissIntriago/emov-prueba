/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BalanceComprobacion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.controller.configArchivosSinafip.ArchivosSinafipDatos;
import com.origami.sigef.contabilidad.model.EstructuraArchivosPlanos;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralDetalleService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralService;
import com.origami.sigef.resource.contabilidad.services.ContSaldoInicialService;
import com.origami.sigef.resource.presupuesto.procesos.services.ClienteServices;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.BalanceComprobacionService;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jesus
 */
@Named(value = "archivoSinafipView")
@ViewScoped
public class ArchivosSinafipController extends ArchivosSinafipDatos implements Serializable {

    @Inject
//    private DiarioGeneralService diarioGeneralService;
    private ContDiarioGeneralService diarioGeneralService;
    @Inject
//    private DetalleTransaccionService detalleTransaccionService;
    private ContDiarioGeneralDetalleService detalleTransaccionService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
//    @Inject
//    private CuentaContableService cuentaContableService;
    @Inject
    private ContCuentasService cuentaContableService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ProveedorService proveedorSercice;
    @Inject
    private DatosGeneralesEntidadService datosGeneralesEntidadService;
    @Inject
    private BalanceComprobacionService balanceService;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private PlanCuentasService planCuentasService;
    @Inject
    private ContSaldoInicialService contSaldoInicialService;
    @Inject
    private ClienteService clienteService;
    private BalanceComprobacion balanceComprobación;

    private CatalogoItem claseCatalogoItem;
    private CatalogoItem aperturaCatalogoItem;
    private CatalogoItem tipoCierreItem;
    private List<CatalogoItem> cuentasTransaccionesItems;
//    private List<MasterCatalogo> periodos;
    private List<Short> listaPeriodo;
    private OpcionBusqueda opcionBusqueda;

    private String tipoArchivo;
    private Boolean claseDiarioApertura;

    private final String CODIGO_BALANCE_INICIAL = "BI";
    private final String CODIGO_BALANCE_COMPROBACION = "BCM";
    private final String CODIGO_TRANSACCIONES_RECIPROCAS = "TRM";
    private Integer mes;

    private List<CatalogoItem> mesesAnio;
    private Boolean renderedMeses;
//    private DiarioGeneral diarioApertura;
    private ContDiarioGeneral diarioApertura;
    private Boolean mensajeCuentaRendered;
    private String tipoDiario;
    private Boolean mostrarDatosComprobacion;
    private Boolean mostrarDatosComprobacionSinCierre;
    private List<EstructuraArchivosPlanos> estructuraArchivos;
    private String fechaDesde;
    private String fechaHasta;

    private List<ContDiarioGeneralDetalle> listDetalleTransaccionesReciprocas;
    private Boolean mostrarDatatableTransacccionesR;
    private List<Proveedor> proveedores;
    private List<Proveedor> aggProveedores;
    List<BalanceComprobacion> listBalance;
    private List<PlanCuentas> listNiveles;

//    private DatosGeneralesEntidad datosGeneralesEntidad;
    @PostConstruct
    public void init() {
        loadModel();
        mesesAnio = catalogoItemService.findByNamedQuery("CatalogoItem.findByCatalogo", "meses_anio");
        claseCatalogoItem = catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario");
        aperturaCatalogoItem = catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_apertura");
//        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listaPeriodo = catalogoItemService.getPeriodo();
        cuentasTransaccionesItems = catalogoItemService.findByNamedQuery("CatalogoItem.findByCatalogo", "transacciones_reciprocas");
//        datosGeneralesEntidad = datosGeneralesEntidadService.find(1L);
        proveedores = proveedorSercice.findByNamedQuery("Proveedor.findAll");
        listNiveles = planCuentasService.getNivelesList(CONFIG.PLAN_CUENTA_CONTABLE, false);

    }

    public void loadModel() {
        claseDiarioApertura = Boolean.FALSE;
        opcionBusqueda = new OpcionBusqueda();
        tipoArchivo = "";
        mes = 0;
        renderedMeses = Boolean.FALSE;
        mensajeCuentaRendered = Boolean.FALSE;
        diarioApertura = new ContDiarioGeneral();
        mostrarDatosComprobacion = Boolean.FALSE;
        mostrarDatosComprobacionSinCierre = Boolean.FALSE;
        estructuraArchivos = new ArrayList<>();
        fechaDesde = "";
        fechaHasta = "";
        tipoDiario = "";
        listDetalleTransaccionesReciprocas = new ArrayList<>();
        mostrarDatatableTransacccionesR = Boolean.FALSE;
        aggProveedores = new ArrayList<>();
    }

    public void generarArchivoPlano() {
        System.out.println("entro al metodo");
        List<ContCuentas> cuentasGobiernoPadre = cuentaContableService.findAllAgrupacionAndGobierno(true);
        String ruta = "";
        if (tipoArchivo != null) {
            switch (tipoArchivo) {
                case CODIGO_BALANCE_INICIAL:
                    if (!cuentasGobiernoPadre.isEmpty()) {
                        ruta = valoresService.findByCodigo("RUTA_ARCHIVO_BALANCE_INICIAL") + "BalanceInicial_" + opcionBusqueda.getAnio() + ".txt";
                        if (diarioApertura != null) {
                            for (ContCuentas padre : cuentasGobiernoPadre) {
                                ContDiarioGeneralDetalle padreDetalleDB = detalleTransaccionService.findByPadreDiarioGeneral(diarioApertura.getId(), padre.getId());
                                ContDiarioGeneralDetalle detalleCuentaHija = consultaHijasByDiarioGeneral(returnDetalleCuentaHijas(padre), diarioApertura);
                                if (padreDetalleDB.getDebe() != null && detalleCuentaHija.getDebe() == null) {
                                    if (!padreDetalleDB.getHaber().equals(0.00) || !padreDetalleDB.getDebe().equals(0.00)) {
                                        estructuraArchivos.add(setEstructuraBalanceInicial(padre, padreDetalleDB));
                                    }
                                } else if (padreDetalleDB.getDebe() == null && detalleCuentaHija.getDebe() != null) {
                                    if (!detalleCuentaHija.getDebe().equals(BigDecimal.ZERO) || !detalleCuentaHija.getHaber().equals(BigDecimal.ZERO)) {
                                        estructuraArchivos.add(setEstructuraBalanceInicial(padre, detalleCuentaHija));
                                    }
                                } else if (padreDetalleDB.getDebe() != null && detalleCuentaHija.getDebe() != null) {
                                    padreDetalleDB.setDebe(padreDetalleDB.getDebe().add(detalleCuentaHija.getDebe()));
                                    padreDetalleDB.setHaber(padreDetalleDB.getHaber().add(detalleCuentaHija.getHaber()));
                                    if (!padreDetalleDB.getHaber().equals(0.00) || !padreDetalleDB.getDebe().equals(0.00)) {
                                        estructuraArchivos.add(setEstructuraBalanceInicial(padre, padreDetalleDB));
                                    }
                                }
                            }
                        } else {
                            //aqui viene en el caso q no exista un diario tipo apertura .... consultar directamente desde cuenta contable saldo inicial debe, 
                            //saldo inicial haber
                            for (ContCuentas padre : cuentasGobiernoPadre) {
                                //saldos iniciales
                                ContSaldoInicial padreSaldosIniciales = contSaldoInicialService.findContSaldoInicialByIdCuentaAndPeriodo(padre, opcionBusqueda.getAnio());
                                if (padreSaldosIniciales == null) {
                                    padreSaldosIniciales = new ContSaldoInicial();
                                }
                                //other object
                                Map<String, BigDecimal> valoresInicialesHijas = consultaHijasNoExisteDiarioAperturaByComprobanteMensualHas(returnDetalleCuentaHijas(padre), opcionBusqueda.getAnio());
                                BigDecimal inicialDebe = valoresInicialesHijas.get("inicialDebe"), inicialHaber = valoresInicialesHijas.get("inicialHaber");
//                                ContCuentas hijasCuenta = consultaHijasNoExisteDiarioAperturaByComprobanteMensual(returnDetalleCuentaHijas(padre));
                                if (inicialDebe != null && inicialHaber != null) {
                                    if (padreSaldosIniciales.getSaldoDebe() != null && padreSaldosIniciales.getSaldoHaber() != null) {
                                        padreSaldosIniciales.getSaldoDebe().add(inicialDebe);
                                        padreSaldosIniciales.getSaldoHaber().add(inicialHaber);
                                        estructuraArchivos.add(setEstructuraBalanceInicialCuentaContable(padre, opcionBusqueda.getAnio(), padreSaldosIniciales.getSaldoDebe().doubleValue(), padreSaldosIniciales.getSaldoHaber().doubleValue()));
                                    } else {
                                        if (!inicialDebe.equals(BigDecimal.ZERO) || !inicialHaber.equals(BigDecimal.ZERO)) {
                                            padreSaldosIniciales.setSaldoDebe(inicialDebe);
                                            padreSaldosIniciales.setSaldoHaber(inicialHaber);
                                            estructuraArchivos.add(setEstructuraBalanceInicialCuentaContable(padre, opcionBusqueda.getAnio(), padreSaldosIniciales.getSaldoDebe().doubleValue(), padreSaldosIniciales.getSaldoHaber().doubleValue()));
                                        }
                                    }
                                } else {
                                    if (padreSaldosIniciales.getSaldoDebe() != null && padreSaldosIniciales.getSaldoHaber() != null) {
                                        estructuraArchivos.add(setEstructuraBalanceInicialCuentaContable(padre, opcionBusqueda.getAnio(), padreSaldosIniciales.getSaldoDebe().doubleValue(), padreSaldosIniciales.getSaldoHaber().doubleValue()));
                                    }
                                }
                            }
                        }
                    } else {
                        JsfUtil.addInformationMessage("", "No existen registros de Cuenta Contable de Catalogo para el período " + opcionBusqueda.getAnio());
                    }
                    break;
                case CODIGO_BALANCE_COMPROBACION:
                    ruta = valoresService.findByCodigo("RUTA_ARCHIVO_BALANCE_COMPROBACION") + "BalanceComprobacion_" + fechaDesde + ".txt";
                    Date fechaI = inicioDate(1, opcionBusqueda.getAnio());
                    Date fechaF = finDate(mes, opcionBusqueda.getAnio());
                    Boolean cierre;
                    switch (tipoDiario) {
                        case "CT":
                            cierre = true;
                            break;
                        case "ST":
                            cierre = false;
                            break;
                        default:
                            cierre = true;
                            break;
                    }
                    listBalance = new ArrayList<>();
                    List<ContCuentas> lisDetalleCT;
                    lisDetalleCT = balanceService.getCuentasIfOrNotGobierno(fechaI, fechaF, opcionBusqueda.getAnio(), true);
                    if (!lisDetalleCT.isEmpty()) {
                        lisDetalleCT.stream().map((item) -> {
                            SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioNoAcum(item.getCodigo(), fechaI, fechaF, opcionBusqueda.getAnio());
                            SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoNoAcum(item.getCodigo(), fechaI, fechaF, opcionBusqueda.getAnio(), cierre);
                            balanceComprobación = new BalanceComprobacion();
                            balanceComprobación.setCuentaContable(item);
                            System.out.println("setCuentaContable || >> " + balanceComprobación.getCuentaContable().getCodigo());
//                            System.out.println("item: " + item.getCodigo() + " id " + item.getId());
                            System.out.println("inicial " + inicial.toString());
                            System.out.println("flujo " + flujo.toString());
                            balanceComprobación.setSaldoInicialDebe(inicial.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
                            balanceComprobación.setSaldoInicialHaber(inicial.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
                            balanceComprobación.setFlujoDebe(flujo != null ? flujo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                            balanceComprobación.setFlujoHaber(flujo != null ? flujo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                            return flujo;
                        }).map((_item) -> {
                            balanceComprobación.setAcumuladoDebe(balanceComprobación.getSaldoInicialDebe().add(balanceComprobación.getFlujoDebe()));
                            return _item;
                        }).map((_item) -> {
                            balanceComprobación.setAcumuladoHaber(balanceComprobación.getSaldoInicialHaber().add(balanceComprobación.getFlujoHaber()));
                            return _item;
                        }).map((_item) -> {
                            balanceComprobación.setTotalDebe(totalDebe(balanceComprobación));
                            return _item;
                        }).map((_item) -> {
                            balanceComprobación.setTotalHaber(totalHaber(balanceComprobación));
                            return _item;
                        }).filter((_item) -> (balanceComprobación.getAcumuladoDebe().doubleValue() > 0 || balanceComprobación.getAcumuladoHaber().doubleValue() > 0)).forEachOrdered((_item) -> {
                            System.out.println("Cta Gobierno >> " + balanceComprobación.getCuentaContable().getCodigo());
                            listBalance.add(balanceComprobación);
                        });
                    }
                    //agregando cuentas no gobierno y buscando el catalogo
                    List<ContCuentas> listDetalleCTnoCatalogo;
                    List<ContCuentas> listDetalleCTA = new ArrayList<>();
                    listDetalleCTnoCatalogo = balanceService.getCuentasIfOrNotGobierno(fechaI, fechaF, opcionBusqueda.getAnio(), false);
                    if (!listDetalleCTnoCatalogo.isEmpty()) {
                        listDetalleCTnoCatalogo.stream().map((item) -> {
                            ContCuentas recorrer;
                            recorrer = getCuentaGobierno(item);
                            return recorrer;
                        }).forEachOrdered((recorrer) -> {
                            boolean con = false;
                            boolean var = false;
                            for (ContCuentas i : lisDetalleCT) {
                                if (recorrer.getId() != null && recorrer.getCodigo().equals(i.getCodigo())) {
                                    con = true;
                                }
                            }
                            for (int i = 0; i < listDetalleCTA.size(); i++) {
                                System.out.println("Cta Gobierno: " + recorrer.getCodigo());
                                if (recorrer.getId() != null && recorrer.getCodigo().equals(listDetalleCTA.get(i).getCodigo())) {
                                    var = true;
                                }
                            }
                            if (con == false && var == false) {
                                System.out.println("add >> " + recorrer.getCodigo());
                                listDetalleCTA.add(recorrer);
                            }
                        });
                    }
                    if (!listDetalleCTA.isEmpty()) {
                        for (ContCuentas item : listDetalleCTA) {
                            SaldoDebeHaberDTO inicial = balanceService.getSaldosDebeHaberInicioAcum(item.getCodigo(), fechaI, fechaF, opcionBusqueda.getAnio());
                            SaldoDebeHaberDTO flujo = balanceService.getSaldosDebeHaberFlujoAcum(item.getCodigo(), fechaI, fechaF, opcionBusqueda.getAnio(), true);
                            balanceComprobación.setCuentaContable(item);
                            balanceComprobación.setSaldoInicialDebe(totalDebeInicial(inicial).setScale(2, RoundingMode.HALF_UP));
                            balanceComprobación.setSaldoInicialHaber(totalHaberInicial(inicial).setScale(2, RoundingMode.HALF_UP));
                            balanceComprobación.setFlujoDebe(flujo != null ? flujo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                            balanceComprobación.setFlujoHaber(flujo != null ? flujo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                            balanceComprobación.setAcumuladoDebe(balanceComprobación.getSaldoInicialDebe().add(balanceComprobación.getFlujoDebe()));
                            balanceComprobación.setAcumuladoHaber(balanceComprobación.getSaldoInicialHaber().add(balanceComprobación.getFlujoHaber()));
                            balanceComprobación.setTotalDebe(totalDebe(balanceComprobación));
                            balanceComprobación.setTotalHaber(totalHaber(balanceComprobación));
                            System.out.println("cuentaContable >> " + balanceComprobación.getCuentaContable());
                            System.out.println("Cta Gobierno >> " + balanceComprobación.getCuentaContable().getCodigo());
                            if (listBalance.contains(balanceComprobación)) {
                                System.out.println("eliminando registro >> " + listBalance.contains(balanceComprobación));
                                listBalance.remove(balanceComprobación);
                            }
                            listBalance.add(balanceComprobación);
//                            balanceService.create(balanceComprobación);
                            balanceComprobación = new BalanceComprobacion();
                        }
                    }
                    break;

                case CODIGO_TRANSACCIONES_RECIPROCAS:
                    ruta = valoresService.findByCodigo("RUTA_ARCHIVO_TRANSACCION_RECIPROCA") + "TransaccionesReciprocas_" + fechaDesde + ".txt";
                    if (!listDetalleTransaccionesReciprocas.isEmpty()) {
                        for (ContDiarioGeneralDetalle d : listDetalleTransaccionesReciprocas) {
                            if (d.getIdContDiarioGeneral().getOtorgante() == null || d.getIdContDiarioGeneral().getReceptor() == null) {
                                JsfUtil.addErrorMessage("", "Todas los registros debe asignarle un otorgante y un receptor");
                                return;
                            }
                        }
                        List<ContDiarioGeneralDetalle> newLista = unirListaSemejantesDetalleTransaccion(listDetalleTransaccionesReciprocas);
                        for (ContDiarioGeneralDetalle detalle : newLista) {
                            if (detalle.getIdContCuentas().getIsHija()) {
                                estructuraArchivos.add(setEstructuraTransaccionesReciprocas(detalle.getIdContCuentas().getPadre(), detalle, mes));
                            } else {
                                estructuraArchivos.add(setEstructuraTransaccionesReciprocas(detalle.getIdContCuentas(), detalle, mes));
                            }
                        }

                    } else {
                        if (!cuentasTransaccionesItems.isEmpty()) {
                            for (CatalogoItem item : cuentasTransaccionesItems) {
                                estructuraArchivos.add(setEstructuraTransaccionesReciprocasEmpty(item, mes));
                            }
                        }
                    }
                    break;
            }
            System.out.println("ruta " + ruta);
            if (createFileArchivoText(ruta, estructuraArchivos, tipoArchivo) && !tipoArchivo.equals("BCM")) {
                //aqui viene la descarga
                downloadFile(ruta);
                JsfUtil.addSuccessMessage("", "Archivo Generado Correctamente");
            }
            if (tipoArchivo.equals("BCM") && createFileArchivoText(ruta, listBalance)) {
                downloadFile(ruta);
                JsfUtil.addSuccessMessage("", "Archivo Generado Correctamente");
            }
        }
    }

    public void actualizarDatosDiario() {
        mostrarDatosComprobacion = Boolean.FALSE;
        mostrarDatosComprobacionSinCierre = Boolean.FALSE;
        listDetalleTransaccionesReciprocas = new ArrayList<>();
        if (tipoArchivo != null) {
            if (tipoDiario != null) {
                if (tipoDiario.equals("CT")) {
                    mostrarDatosComprobacion = Boolean.TRUE;
                    tipoCierreItem = new CatalogoItem();
                } else {
                    tipoCierreItem = catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_cierre");
                    mostrarDatosComprobacionSinCierre = Boolean.TRUE;
                }
            }
            if (tipoArchivo.equals(CODIGO_TRANSACCIONES_RECIPROCAS)) {
                for (CatalogoItem cuentaItem : cuentasTransaccionesItems) {
//                    ContCuentas cuentaContable = cuentaContableService.findCuentaContableByCodigoAndPerido(cuentaItem.getCodigo(), opcionBusqueda.getAnio());
                    ContCuentas cuentaContable = cuentaContableService.findContCuentasByCodigoAndEstadoIsTrue(cuentaItem.getCodigo());
                    if (cuentaContable != null) {

                        List<ContDiarioGeneralDetalle> padres = detalleTransaccionService.findAllDetalleByCuentaContablePadreAndFecha(
                                fechaDesde, cuentaContable.getId(), tipoCierreItem.getId());

                        List<ContDiarioGeneralDetalle> hijos = detalleTransaccionService.findAllDetalleByCuentaContableHijasAndFecha(
                                fechaDesde, cuentaContable.getId(), tipoCierreItem.getId());

                        if (padres != null) {
                            for (ContDiarioGeneralDetalle padreDetalle : padres) {
                                ContDiarioGeneralDetalle detalleDB = detalleTransaccionService.findDetalleTransaccionById(padreDetalle.getId());
                                asignarReceptorADetalleTransaccion(detalleDB);
                                detalleDB.getIdContCuentas().setIsHija(Boolean.FALSE);
                                listDetalleTransaccionesReciprocas.add(detalleDB);
                            }
                        }
                        if (hijos != null) {
                            for (ContDiarioGeneralDetalle hijasDetalle : hijos) {
                                ContDiarioGeneralDetalle detalleDB = detalleTransaccionService.findDetalleTransaccionById(hijasDetalle.getId());
                                asignarReceptorADetalleTransaccion(detalleDB);
                                detalleDB.getIdContCuentas().setIsHija(Boolean.TRUE);
                                listDetalleTransaccionesReciprocas.add(detalleDB);
                            }
                        }
                        mostrarDatatableTransacccionesR = Boolean.TRUE;
                    }
                }
            }
        } else {
            JsfUtil.addInformationMessage("", "Seleccione el tipo de Archivo a Generar");
        }
    }

    public void asignarReceptorADetalleTransaccion(ContDiarioGeneralDetalle detalleDB) {

        if ((Objects.equals(detalleDB.getIdContDiarioGeneral().getCodModulo(), MOD_CONTABILIDAD.MOD_CERTIFICACION))
                && (detalleDB.getIdContDiarioGeneral().getIdentificacion() != null && !detalleDB.getIdContDiarioGeneral().getIdentificacion().equals(""))) {
            Cliente c = clienteService.buscarCliente(detalleDB.getIdContDiarioGeneral().getIdentificacion());
            detalleDB.getIdContDiarioGeneral().setReceptor(c);
        } else {
            detalleDB.getIdContDiarioGeneral().setReceptor(new Cliente());
        }
        detalleDB.setCuentaMonetaria(0);
    }

    public void actualizarDatosView() {
        diarioApertura = new ContDiarioGeneral();
        claseDiarioApertura = Boolean.FALSE;
        mensajeCuentaRendered = Boolean.FALSE;
        renderedMeses = Boolean.FALSE;
        mostrarDatosComprobacion = Boolean.FALSE;
        mostrarDatosComprobacionSinCierre = Boolean.FALSE;
        mostrarDatatableTransacccionesR = Boolean.FALSE;
        if (tipoArchivo != null) {
            if (tipoArchivo.equals(CODIGO_BALANCE_INICIAL)) {
                diarioApertura = diarioGeneralService.findAllDiarioGeneralClaseAndTipoAndPeriodo(
                        claseCatalogoItem.getId(), aperturaCatalogoItem.getId(), opcionBusqueda.getAnio());
                if (diarioApertura != null) {
                    claseDiarioApertura = Boolean.TRUE;
                } else {
                    mensajeCuentaRendered = Boolean.TRUE;
                }
            }
            if (tipoArchivo.equals(CODIGO_BALANCE_COMPROBACION)) {
                diarioApertura = diarioGeneralService.findAllDiarioGeneralClaseAndTipoAndPeriodo(
                        claseCatalogoItem.getId(), aperturaCatalogoItem.getId(), opcionBusqueda.getAnio());
                renderedMeses = Boolean.TRUE;
            }
            if (tipoArchivo.equals(CODIGO_TRANSACCIONES_RECIPROCAS)) {
                renderedMeses = Boolean.TRUE;
            }
        }
    }

    private Boolean createFileArchivoText(String ruta, List<EstructuraArchivosPlanos> estructuras, String tipo) {
        try {
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator('.');
            DecimalFormat format = new DecimalFormat("#0.00", simbolo);
            if (!estructuras.isEmpty()) {
                if (tipo.equals(CODIGO_BALANCE_INICIAL)) {
                    try (PrintWriter writer = new PrintWriter(ruta, "UTF-8")) {
                        estructuras.forEach((e) -> {
                            writer.println(e.getPeriodo() + "|" + e.getCuentaMayor() + "|" + e.getCuentaNivel1() + "|" + e.getCuentaNivel2()
                                    + "|" + format.format(e.getSaldoInicialDeudor()) + "|" + format.format(e.getSaldoInicialAcreedor()));
                        });
                    }
                    return true;
                }
                if (tipo.equals(CODIGO_BALANCE_COMPROBACION)) {
                    try (PrintWriter writer = new PrintWriter(ruta, "UTF-8")) {
                        estructuras.forEach((e) -> {
                            writer.println(e.getPeriodo() + "|" + e.getCuentaMayor() + "|" + e.getCuentaNivel1() + "|" + e.getCuentaNivel2()
                                    + "|" + format.format(e.getSaldoInicialDeudor()) + "|" + format.format(e.getSaldoInicialAcreedor()) + "|" + format.format(e.getFlujoDeudor())
                                    + "|" + format.format(e.getFlujoAcreedor()) + "|" + format.format(e.getSumasAcumuladoDeudor()) + "|" + format.format(e.getSumasAculadoAcreedor())
                                    + "|" + format.format(e.getSaldoFinalDeudor()) + "|" + format.format(e.getSaldoFinalAcreedor()));
                        });
                    }
                    return true;
                }
                if (tipo.equals(CODIGO_TRANSACCIONES_RECIPROCAS)) {
                    try (PrintWriter writer = new PrintWriter(ruta, "UTF-8")) {
                        estructuras.forEach((e) -> {
                            writer.println(e.getPeriodo() + "|" + e.getCuentaMayor() + "|" + e.getCuentaNivel1() + "|" + e.getCuentaNivel2()
                                    + "|" + e.getRucReceptor() + "|" + e.getRucOtorgante()
                                    + "|" + format.format(e.getFlujoDeudor())
                                    + "|" + format.format(e.getFlujoAcreedor())
                                    + "|" + e.getCuentaMonetaria());
                        });
                    }
                    return true;
                }
            } else {
                try (PrintWriter writer = new PrintWriter(ruta, "UTF8")) {
                    writer.print("");
                }
                return true;
            }
            JsfUtil.addInformationMessage("", "No existen datos que generar");
            return false;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void downloadFile(String ruta) {
        servletSession.borrarDatos();
        servletSession.borrarParametros();
        servletSession.setNombreDocumento(ruta);
        servletSession.setContentType("text/plain");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        loadModel();
    }

    public void formatFecha() {
        if (mes != null && opcionBusqueda.getAnio() != null) {
            String mesString;
            if (mes.toString().length() == 1) {
                mesString = "0" + mes;
            } else {
                mesString = "" + mes;
            }
            fechaDesde = opcionBusqueda.getAnio() + "-" + mesString;
            fechaHasta = opcionBusqueda.getAnio() + "-01";
            if (tipoDiario != null && !tipoDiario.isEmpty()) {
                actualizarDatosDiario();
            }
        }
    }

    public BigDecimal totalDebeInicial(SaldoDebeHaberDTO saldo) {
        BigDecimal result;
        saldo.setSaldoDebe(saldo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
        saldo.setSaldoHaber(saldo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
        System.out.println("D Debe Ini: " + saldo.getSaldoDebe() + " Haber Ini: " + saldo.getSaldoHaber());
        result = saldo.getSaldoDebe().subtract(saldo.getSaldoHaber());
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalHaberInicial(SaldoDebeHaberDTO saldo) {
        BigDecimal result;
        saldo.setSaldoDebe(saldo.getSaldoDebe().setScale(2, RoundingMode.HALF_UP));
        saldo.setSaldoHaber(saldo.getSaldoHaber().setScale(2, RoundingMode.HALF_UP));
        System.out.println("H Debe Ini: " + saldo.getSaldoDebe() + " Haber Ini: " + saldo.getSaldoHaber());
        result = saldo.getSaldoDebe().subtract(saldo.getSaldoHaber());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            return result.abs();
        } else {
            return BigDecimal.ZERO;
        }
    }

//<editor-fold defaultstate="collapsed" desc="Luis Pozo G metodos que ayudan a ejecutar el Archivo plano mensualizado :3">
    private Boolean createFileArchivoText(String ruta, List<BalanceComprobacion> listBalance) {
        try {
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator('.');
            DecimalFormat format = new DecimalFormat("#0.00", simbolo);
            if (!listBalance.isEmpty()) {
                try (PrintWriter writer = new PrintWriter(ruta, "UTF-8")) {
                    Collections.sort(listBalance, (x, z) -> x.getCuentaContable().getCodigo().compareToIgnoreCase(z.getCuentaContable().getCodigo()));
                    listBalance.forEach((e) -> {
                        Map<String, String> codigoSeparado = getNivelesCuenta(e.getCuentaContable());
                        writer.println(String.format("%02d", mes) + "|"
                                + codigoSeparado.get("cuentaMayor") + "|"
                                + codigoSeparado.get("nivel1") + "|"
                                + codigoSeparado.get("nivel2") + "|"
                                + format.format(e.getSaldoInicialDebe()) + "|"
                                + format.format(e.getSaldoInicialHaber()) + "|"
                                + format.format(e.getFlujoDebe()) + "|"
                                + format.format(e.getFlujoHaber()) + "|"
                                + format.format(e.getAcumuladoDebe()) + "|"
                                + format.format(e.getAcumuladoHaber()) + "|"
                                + format.format(e.getTotalDebe()) + "|"
                                + format.format(e.getTotalHaber()));
                    });
                }
                return true;
            } else {
                try (PrintWriter writer = new PrintWriter(ruta, "UTF8")) {
                    writer.print("");
                }
                JsfUtil.addInformationMessage("", "No existen datos que generar");
                return true;
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            return false;
        }
    }

    public ContCuentas getCuentaGobierno(ContCuentas c) {
        ContCuentas resultCuenta = new ContCuentas();
        if (c.getGobierno() == true) {
            return c;
        } else {
            Integer i = c.getCodigo().length();
            for (int j = 1; j <= i; j++) {
                ContCuentas cuenta = contCuentasService.findContCuentasByCodigo(c.getCodigo().substring(0, i - j));
                if (cuenta != null) {
                    if (cuenta.getGobierno() == true) {
                        resultCuenta = cuenta;
                        break;
                    }
                }
            }
            return resultCuenta;
        }
    }

    public BigDecimal totalDebe(BalanceComprobacion b) {
        BigDecimal result = BigDecimal.ZERO;
        b.setAcumuladoDebe(b.getAcumuladoDebe().setScale(2, RoundingMode.HALF_UP));
        b.setAcumuladoHaber(b.getAcumuladoHaber().setScale(2, RoundingMode.HALF_UP));
        result = b.getAcumuladoDebe().subtract(b.getAcumuladoHaber());
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalHaber(BalanceComprobacion b) {
        BigDecimal result = BigDecimal.ZERO;
        b.setAcumuladoDebe(b.getAcumuladoDebe().setScale(2, RoundingMode.HALF_UP));
        b.setAcumuladoHaber(b.getAcumuladoHaber().setScale(2, RoundingMode.HALF_UP));
        result = b.getAcumuladoDebe().subtract(b.getAcumuladoHaber());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            return result.abs();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public Date inicioDate(int mes, Short anio) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(anio, mes - 1, 1);
        return calendar.getTime();

    }

    public Date finDate(int mes, Short anio) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(anio, mes - 1, TalentoHumano.obtenerUltimoDiaMes(anio.intValue(), mes - 1));
        return calendar.getTime();
    }

    private String codigoIsNull(Short c) {
        if (c == null) {
            return "00";
        }
        return "" + c;
    }

    private String completarCeros(Short c) {
        if (c == null) {
            return "00";
        }
        if (c.toString().length() == 1) {
            return "0" + c;
        }
        return "" + c;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public List<Proveedor> getAggProveedores() {
        return aggProveedores;
    }

    public void setAggProveedores(List<Proveedor> aggProveedores) {
        this.aggProveedores = aggProveedores;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public List<ContDiarioGeneralDetalle> getListDetalleTransaccionesReciprocas() {
        return listDetalleTransaccionesReciprocas;
    }

    public void setListDetalleTransaccionesReciprocas(List<ContDiarioGeneralDetalle> listDetalleTransaccionesReciprocas) {
        this.listDetalleTransaccionesReciprocas = listDetalleTransaccionesReciprocas;
    }

    public Boolean getMostrarDatatableTransacccionesR() {
        return mostrarDatatableTransacccionesR;
    }

    public void setMostrarDatatableTransacccionesR(Boolean mostrarDatatableTransacccionesR) {
        this.mostrarDatatableTransacccionesR = mostrarDatatableTransacccionesR;
    }

    public Boolean getMostrarDatosComprobacionSinCierre() {
        return mostrarDatosComprobacionSinCierre;
    }

    public void setMostrarDatosComprobacionSinCierre(Boolean mostrarDatosComprobacionSinCierre) {
        this.mostrarDatosComprobacionSinCierre = mostrarDatosComprobacionSinCierre;
    }

    public Boolean getMostrarDatosComprobacion() {
        return mostrarDatosComprobacion;
    }

    public void setMostrarDatosComprobacion(Boolean mostrarDatosComprobacion) {
        this.mostrarDatosComprobacion = mostrarDatosComprobacion;
    }

    public String getTipoDiario() {
        return tipoDiario;
    }

    public void setTipoDiario(String tipoDiario) {
        this.tipoDiario = tipoDiario;
    }

    public Boolean getMensajeCuentaRendered() {
        return mensajeCuentaRendered;
    }

    public void setMensajeCuentaRendered(Boolean mensajeCuentaRendered) {
        this.mensajeCuentaRendered = mensajeCuentaRendered;
    }

    public ContDiarioGeneral getDiarioApertura() {
        return diarioApertura;
    }

    public void setDiarioApertura(ContDiarioGeneral diarioApertura) {
        this.diarioApertura = diarioApertura;
    }

    public Boolean getRenderedMeses() {
        return renderedMeses;
    }

    public void setRenderedMeses(Boolean renderedMeses) {
        this.renderedMeses = renderedMeses;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public List<CatalogoItem> getMesesAnio() {
        return mesesAnio;
    }

    public void setMesesAnio(List<CatalogoItem> mesesAnio) {
        this.mesesAnio = mesesAnio;
    }

    public Boolean getClaseDiarioApertura() {
        return claseDiarioApertura;
    }

    public void setClaseDiarioApertura(Boolean claseDiarioApertura) {
        this.claseDiarioApertura = claseDiarioApertura;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

//    public List<MasterCatalogo> getPeriodos() {
//        return periodos;
//    }
//
//    public void setPeriodos(List<MasterCatalogo> periodos) {
//        this.periodos = periodos;
//    }
//</editor-fold>
    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
}
