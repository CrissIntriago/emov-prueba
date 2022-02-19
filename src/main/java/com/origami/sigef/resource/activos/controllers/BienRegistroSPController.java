/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.activos.entities.BienCatalogoBld;
import com.origami.sigef.resource.activos.entities.BienVidaUtil;
import com.origami.sigef.resource.activos.services.BienVidaUtilService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 * @author Luis Pozo Gonzabay
 */
@Named(value = "regBienesSinProcView")
@ViewScoped
public class BienRegistroSPController implements Serializable {

    private static final Logger LOG = Logger.getLogger(BienRegistroSPController.class.getName());

    private BienesItem bienesItemRegistro; //Para un ItemBien
    private BienesItem bienesItemComponentes; //Para un componente
    private BienesItem bienesItemCrear;
    private BienesMovimiento bienesMovimiento;
    private BienCatalogoBld catalogoBLD;
    private ContCuentas cuentaContable;
    private Factura factura;
    private Adquisiciones adquisicionesOrden;
    private Distributivo distrib;
    private BienVidaUtil vidaUtil;
    private ActivoFijoServidor activoFijoServidor;

    private Boolean habilitar = Boolean.FALSE;
    private Boolean bloqueoBtn = Boolean.FALSE;
    private Boolean adquisicion = Boolean.FALSE;
    private Boolean docReferencia = Boolean.TRUE;
    private Boolean facturaBoolean = Boolean.TRUE;
    private Boolean datosAdicionales = Boolean.TRUE;
    private Boolean lote = Boolean.FALSE;
    private Boolean vidaUtilBoolean;
    private Boolean vidaUtilTempBoolean;
    private Boolean habilitarVida;
    private Boolean nuevo;
    private Boolean utpe;
    private Boolean botonGuardar = Boolean.FALSE;

    private List<CatalogoMovimiento> listMotivoMovimiento;
    private List<CatalogoItem> listCatalogoItem;
    private List<ContCuentas> listCuentaContableClasifGrupo;
    private List<ContCuentas> listCuentaContable;
    private List<BienesItem> listBienesItem;
    private List<ContCuentas> listBienesItemFilterCta;
    private List<CatalogoItem> listGestionAdquisiciones;
    private List<CatalogoItem> listEstadoBien;
    private List<BienesItem> listBienesComponentes;
    private List<CatalogoItem> listDatosAdicionales;
    private List<BienVidaUtil> listVidaUtil;
    private List<BienesItem> listBienesOfActivoFijoGuard;

    private LazyModel<BienCatalogoBld> lazyCatalogoBLD;
    private LazyModel<BienesItem> lazyGrupoBien;
    private LazyModel<Adquisiciones> lazyGestionAdquisicion;
    private LazyModel<Factura> lazyFactura;

    private Integer anio;
    private String codigoStart;
    private String codigoEnd;
    private Short vidaUtilTemp;
    int numero;
    private List<Adquisiciones> listaAdaqui;
    private Boolean botonAprobaciones = Boolean.TRUE;
    private Boolean botonRechazado = Boolean.TRUE;
    private Boolean botonCompletartarea = Boolean.TRUE;
    private String observaciones;
    private Boolean botonProcesos = Boolean.TRUE;
    private Boolean botonImprimir = Boolean.TRUE;
    private BienesMovimiento bienesAdicionales;
    private List<BienesMovimiento> listaBienes;
    private List<BienesItem> listaDetalleBen;

    private List<BienesMovimiento> listaAcata;

    private List<CatalogoItem> medidas;

    @Inject
    private CatalogoMovimientoService catalogoMovimientoService;

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private BienesMovimientoService bienesMovimientoService;
    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private UserSession user;
    @Inject
    private FacturaService facturaService;
    @Inject
    private BienVidaUtilService vidaUtilService;
    @Inject
    private ActivoFijoServidorService activoFijoServService;
    @Inject
    private ActivoFijoCustodioService activoFijoCustodioService;
    @Inject
    private ClienteService clienteService;

    private String ruta; //ruta del codigo qr
    @Inject
    private ValoresService valoresService;
    //PARA LLAMAR AL WEB SERVICES
    @Inject
    private KatalinaService katalinaService;

    @PostConstruct
    public void initView() {
        ruta = valoresService.findByCodigo("RUTA_CODIGO_QR");

        /*New Bien*/
        catalogoBLD = new BienCatalogoBld();
        bienesMovimiento = new BienesMovimiento();
        adquisicionesOrden = new Adquisiciones();
        bienesItemRegistro = new BienesItem();
        distrib = new Distributivo();
        factura = new Factura();
        bienesItemComponentes = new BienesItem();
        activoFijoServidor = new ActivoFijoServidor();
        cuentaContable = new ContCuentas();
        bienesMovimiento.setMotivoMovimiento(new CatalogoMovimiento());
        bienesMovimiento.setItemBien(new BienesItem());
        bienesMovimiento.setFechaIngreso(new Date());
        bienesMovimiento.setAdquisiciones(new Adquisiciones());
        bienesMovimiento.setFactura(new Factura());
        bienesItemRegistro.setGrupoPadre(new BienesItem());

        lazyCatalogoBLD = new LazyModel<>(BienCatalogoBld.class);
        lazyCatalogoBLD.getSorteds().put("idBien", "ASC");
        lazyGestionAdquisicion = new LazyModel<>(Adquisiciones.class);
        lazyFactura = new LazyModel<>(Factura.class);
        lazyFactura.getFilterss().put("estado", Boolean.TRUE);
        this.listGestionAdquisiciones = new ArrayList<>();
        listEstadoBien = new ArrayList<>();
        listBienesComponentes = new ArrayList<>();
        listVidaUtil = new ArrayList<>();
        listBienesItem = new ArrayList<>();

        listEstadoBien = catalogoItemService.findCatalogoItems("estado_bien");
        listGestionAdquisiciones = catalogoItemService.findCatalogoItems("tipo_proceso");

        listDatosAdicionales = catalogoItemService.findCatalogoItems("datos_adicionales_bienes");

        listMotivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("BIEN-ING", "SIN-FLUJB");
        listCatalogoItem = catalogoItemService.findCatalogoItems("tipo_bienes");
        medidas = catalogoItemService.findCatalogoItems("unidad_medida");
        habilitar = Boolean.FALSE;
        docReferencia = Boolean.TRUE;
        vidaUtilBoolean = Boolean.TRUE;
        vidaUtilTempBoolean = Boolean.FALSE;
        habilitarVida = Boolean.TRUE;
        utpe = Boolean.FALSE;
        this.listaBienes = new ArrayList<>();
        this.listaDetalleBen = new ArrayList<>();
        listBienesOfActivoFijoGuard = new ArrayList<>();
        formNewBien();
    }

    /*CREACION Y REGISTRO DE BIEN*/
    public void formNewBien() {

        bienesMovimiento = new BienesMovimiento();
        bienesItemRegistro = new BienesItem();
        catalogoBLD = new BienCatalogoBld();
        bienesItemRegistro.setUso(Boolean.TRUE);
        bienesMovimiento.setFechaIngreso(new Date());
        this.anio = Utils.getAnio(bienesMovimiento.getFechaIngreso());
        bienesMovimiento.setPeriodo(anio.shortValue());

        Long orden = bienesMovimientoService.getNivelBienesMov();
        bienesMovimiento.setOrden(orden);
        String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesMovimiento.getOrden() + "", 5);
        bienesMovimiento.setCodigo("BIEN-" + bienesMovimiento.getPeriodo() + "-" + completarCadenaConCeros);

        bienesMovimiento.setFechaCreacion(new Date());
        bienesMovimiento.setItemBien(new BienesItem());
        bienesMovimiento.setAdquisiciones(new Adquisiciones());
        bienesMovimiento.setFactura(new Factura());
        bienesItemComponentes = new BienesItem();
        cuentaContable = new ContCuentas();
        bienesItemRegistro.setGrupoPadre(new BienesItem());
        lote = Boolean.FALSE;
        habilitarVida = Boolean.FALSE;
        bienesItemRegistro.setCantidad(1);
        bienesItemRegistro.setEstadoBien(bienesItemService.getEstadoBien("bien_bueno", "estado_bien"));

        codigoStart = "01";
        codigoEnd = "99";

        bienesMovimiento.setEstado(true);
    }

    public void openDlgBienMEF() {
        PrimeFaces.current().executeScript("PF('dlgCatalogoBien').show()");
    }

    public void selectDataCatalogo(BienCatalogoBld evt) {
        catalogoBLD = evt;
        bienesItemRegistro.setCatalogoBienes(catalogoBLD);
        PrimeFaces.current().executeScript("PF('dlgCatalogoBien').hide()");
        PrimeFaces.current().ajax().update("outPanelCatalogo");
    }

    public void openDlgAdquisicion() {
        this.listaAdaqui = new ArrayList<>();
        this.listaAdaqui = bienesItemService.getListaAdquisiciones();

        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgAdquisicion");
    }

    public void closeSelectAdquisicion(Adquisiciones adq) {
        adquisicionesOrden = adq;
        bienesMovimiento.setAdquisiciones(adquisicionesOrden);
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').hide()");
    }

    public void mostrarTodasAdquisiciones() {
        this.listaAdaqui = new ArrayList<>();
        this.listaAdaqui = bienesItemService.getTodasListaAdquisiciones();
    }

//    public void buscarCuenta() {
//        if (bienesItemRegistro.getTipoBien() == null) {
//            JsfUtil.addWarningMessage("Error", "Ingrese un tipo de Bien");
//            return;
//        }
//        if (bienesMovimiento.getMotivoMovimiento() == null) {
//            JsfUtil.addWarningMessage("Error", "Ingrese un tipo de adquisicion");
//            return;
//        }
//        if (bienesItemRegistro.getClasificTipoBien() == null) {
//            JsfUtil.addWarningMessage("Error", "Ingrese una clasificación para asignar Cuenta Contable");
//            return;
//        }
//
//        if (bienesItemRegistro.getClasificTipoBien().getCodigo().equals("148")) {
//            codigoStart = "01";
//            codigoEnd = "98";
//        }
//        if (bienesItemRegistro.getClasificTipoBien().getCodigo().equals("911")) {
//            codigoStart = "01";
//            codigoEnd = "99";
//        }
//
//        Map<String, List<String>> params = new HashMap<>();
//        params.put("TIPOACTIVO", Arrays.asList("BIENES"));
//        params.put("MOVIMIENTO", Arrays.asList(bienesMovimiento.getMotivoMovimiento().toString()));
//        params.put("TIPOBIEN", Arrays.asList(bienesItemRegistro.getTipoBien().getCodigo()));
//        params.put("CODIGO", Arrays.asList(bienesItemRegistro.getClasificTipoBien().getCodigo()));
//        params.put("CODSTART", Arrays.asList(codigoStart));
//        params.put("CODEND", Arrays.asList(codigoEnd));
//        params.put("MOVIMIENTOCUENTA", Arrays.asList("SI"));
//        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "60%", params);
//    }
    public void obtenerVidaUtil() {
        listVidaUtil = vidaUtilService.getVidaUtilByCuenta(cuentaContable);
        if (listVidaUtil.isEmpty()) {
            vidaUtilBoolean = Boolean.TRUE;
            vidaUtilTempBoolean = Boolean.FALSE;
            habilitarVida = Boolean.FALSE;
            bienesItemRegistro.setMaterialBien(null);
            bienesItemRegistro.setVidaUtil(null);
            bienesItemRegistro.setVidaUtilBien(null);
        } else {
            if (listVidaUtil.size() > 1) {
                vidaUtilTempBoolean = Boolean.TRUE;
                vidaUtilBoolean = Boolean.FALSE;
                if (vidaUtil == null) {
                    bienesItemRegistro.setMaterialBien(null);
                    bienesItemRegistro.setVidaUtil(null);
                    bienesItemRegistro.setVidaUtilBien(null);
                }
            } else {
                if (listVidaUtil.size() == 1) {
                    vidaUtilBoolean = Boolean.TRUE;
                    vidaUtilTempBoolean = Boolean.FALSE;
                }
            }
            if (vidaUtil != null) {
                bienesItemRegistro.setVidaUtilBien(vidaUtil);
                bienesItemRegistro.setVidaUtil(vidaUtil.getVidaUtil());
                bienesItemRegistro.setMaterialBien(vidaUtil.getDescripcion());
            } else {
                if (listVidaUtil.get(0).getVidaUtil() == null) {
                    habilitarVida = Boolean.FALSE;
                    utpe = Boolean.TRUE;
                    bienesItemRegistro.setVidaUtil(null);
                    bienesItemRegistro.setVidaUtilBien(listVidaUtil.get(0));
                } else {
                    habilitarVida = Boolean.TRUE;
                    bienesItemRegistro.setVidaUtil(listVidaUtil.get(0).getVidaUtil());
                    bienesItemRegistro.setMaterialBien(listVidaUtil.get(0).getDescripcion());
                    bienesItemRegistro.setVidaUtilBien(listVidaUtil.get(0));
                }
            }
        }
    }

    public void limpiarCuentaBienItem() {
        cuentaContable = new ContCuentas();
        bienesItemRegistro.setCuentaContable(new ContCuentas());
        bienesItemRegistro.setGrupoPadre(new BienesItem());
        bienesItemRegistro.setCodigoBien(null);
        bienesItemRegistro.setCodigoBienAgrupado(null);
    }

    public void buscarGrupoBien() {
//        if (cuentaContable == null) {
//            JsfUtil.addWarningMessage("Error", "Asigne una Cuenta Contable");
//            PrimeFaces.current().ajax().update("messages");
//        } else {
//        listBienesItem = bienesItemService.getBienGrupoByCuenta(cuentaContable.getCodigo());
        if (bienesItemRegistro.getClasificTipoBien() != null) {
            listBienesItem = bienesItemService.getBienGrupoByCuentaLike(bienesItemRegistro.getClasificTipoBien().getCodigo());
        } else {
            JsfUtil.addWarningMessage("Error", "Asigne la clasificación del bien");
            PrimeFaces.current().ajax().update("messages");
        }
        PrimeFaces.current().executeScript("PF('dlgGrupoBien').show()");
        PrimeFaces.current().ajax().update(":frmItem");
//        }
    }

//    metodos cambiadosx
    public void selectGrupoBien(BienesItem bienGrup) {
        if (bienGrup != null) {
            bienesItemRegistro.setGrupoPadre(bienGrup);
            //Setear Codigo Bien Item
            Long orden = bienesItemService.getNivelOrdenBienItem(bienesItemRegistro.getGrupoPadre());
            bienesItemRegistro.setOrden(orden);
            String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItemRegistro.getOrden() + "", 5);
            bienesItemRegistro.setCodigoBien(completarCadenaConCeros);
            bienesItemRegistro.setCodigoBienAgrupado(bienesItemRegistro.getGrupoPadre().getCodigoBienAgrupado() + bienesItemRegistro.getCodigoBien());
            bienesItemRegistro.setUso(Boolean.TRUE);

            selectCuentaBien(bienGrup.getCuentaContable());
            PrimeFaces.current().executeScript("PF('dlgGrupoBien').hide()");
            PrimeFaces.current().ajax().update(":asignarCtaGru");
        } else {
            JsfUtil.addErrorMessage("ERROR", "Error al asignar un SubGrupo");
        }
    }

    public void selectCuentaBien(ContCuentas cuenta) {
        cuentaContable = cuenta;
        bienesItemRegistro.setCuentaContable(cuentaContable);
        obtenerVidaUtil();
    }

    public void mostrarClasificacionCuenta() {
        String codigoBLD = null;
        String codigoBSC = null;
        String codigoNot1 = null;
        String codigoNot2 = null;
        String codigoNot3 = null;
        String codigoNot4 = null;
        Long orden = 3L;
        String clasif;
        String codigo;
        if (cuentaContable != null) {
            cuentaContable = new ContCuentas();
            bienesItemRegistro.setGrupoPadre(new BienesItem());
            bienesItemRegistro.setOrden(0);
            bienesItemRegistro.setCodigoBien(null);
            bienesItemRegistro.setCodigoBienAgrupado(null);
        }
        if (bienesItemRegistro.getTipoBien() == null) {
            JsfUtil.addWarningMessage("Información", "Elija un Tipo de Bien");
        } else {
            if (bienesMovimiento.getMotivoMovimiento() == null) {
                JsfUtil.addWarningMessage("Información", "Elija un Tipo de Movimiento");
            } else {
                if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                    codigoBSC = "911";
                }
                if (bienesItemRegistro.getTipoBien().getCodigo().equals("BLD")) {
                    codigoBLD = "14";
                    codigoNot1 = "6";
                    codigoNot2 = "9";
                }
                switch (bienesMovimiento.getMotivoMovimiento().getOrden()) {
                    case 1:
                        docReferencia = Boolean.FALSE;
                        facturaBoolean = Boolean.TRUE;
                        adquisicion = Boolean.FALSE;
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        }
                        break;
                    case 2:
                        facturaBoolean = Boolean.TRUE;
                        docReferencia = Boolean.FALSE;
                        adquisicion = Boolean.TRUE;
                        adquisicionesOrden = new Adquisiciones();
                        listGestionAdquisiciones = catalogoItemService.findCatalogoItems("tipo_proceso");
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 3:
                        facturaBoolean = Boolean.FALSE;
                        docReferencia = Boolean.TRUE;
                        adquisicion = Boolean.FALSE;
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Solo esta 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 4:
                        facturaBoolean = Boolean.TRUE;
                        adquisicion = Boolean.FALSE;
                        docReferencia = Boolean.TRUE;
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = null;
                            codigoBSC = null;
                            JsfUtil.addInformationMessage("Información", "No existe Cuenta Contable para " + bienesItemRegistro.getTipoBien().getTexto());
                        } else {
                            // Solo esta 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 5:
                        facturaBoolean = Boolean.TRUE;
                        adquisicion = Boolean.FALSE;
                        docReferencia = Boolean.TRUE;
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Solo esta 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 6:
                        facturaBoolean = Boolean.TRUE;
                        adquisicion = Boolean.FALSE;
                        docReferencia = Boolean.TRUE;
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Va el 141, 142
                            clasif = "141";
                            codigo = "142";
                            codigoNot3 = "3";
                            codigoNot4 = "8";
                        }
                        break;
                    case 7:
                        facturaBoolean = Boolean.TRUE;
                        adquisicion = Boolean.FALSE;
                        docReferencia = Boolean.TRUE;
                        //Incorporación
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = null;
                            codigoBSC = null;
                            JsfUtil.addInformationMessage("Información", "No existe Cuenta Contable para " + bienesItemRegistro.getTipoBien().getTexto());
                        } else {
                            //Solo va 143, 148
                            clasif = "143";
                            codigo = "148";
                            codigoNot3 = "1";
                            codigoNot4 = "2";
                        }
                        break;
                    case 8:
                        /*Acta de Entrega Recepción*/
                        facturaBoolean = Boolean.FALSE;
                        adquisicion = Boolean.FALSE;
                        docReferencia = Boolean.TRUE;
                        if (bienesItemRegistro.getTipoBien().getCodigo().equals("BSC")) {
                            clasif = "911";
                        } else {
                            // Todo menos 142, es decir Va el 141, 143, 148
                            clasif = "141";
                            codigo = "143";
                            codigoNot3 = "2";
                        }
                        break;
                }
            }
        }
        listCuentaContable = bienesMovimientoService.getClasificacionByTipoBien(orden, codigoBLD, codigoBSC, codigoNot1, codigoNot2, codigoNot3, codigoNot4);
    }

    public void newComponentes(BienesItem comp, int n) {
        Subject subject = SecurityUtils.getSubject();
        if (comp != null) {
            this.bienesItemComponentes = comp;
            this.numero = n;
            bienesItemComponentes.setFechaModificacion(new Date());
            bienesItemComponentes.setUsuarioModifica(subject.getPrincipal().toString());
        } else {
            bienesItemComponentes = new BienesItem();
            this.numero = n;
            Integer index = listBienesComponentes.size() + 1;
            Long orden = index.longValue();
            bienesItemComponentes.setOrden(orden);
            String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItemComponentes.getOrden() + "", 2);
            bienesItemComponentes.setCodigoBien(completarCadenaConCeros);
            bienesItemComponentes.setEstadoBien(bienesItemService.getEstadoBien("bien_bueno", "estado_bien"));
            bienesItemComponentes.setCantidad(1);
        }
        PrimeFaces.current().executeScript("PF('dlgComponentes').show()");
    }

    public void calcular() {
        if (bienesItemRegistro.getCostoAdquisicion() != null && bienesItemRegistro.getCantidad() != null) {
            BigDecimal cant = BigDecimal.valueOf(bienesItemRegistro.getCantidad());
            BigDecimal result = cant.multiply(bienesItemRegistro.getCostoAdquisicion());
            bienesItemRegistro.setValorTotal(result);
        }
    }

    public void saveComponenteOfBienItem() {
        Subject subject = SecurityUtils.getSubject();
        if (bienesItemComponentes.getEstado() == null) {
            bienesItemComponentes.setEstado(Boolean.TRUE);
            bienesItemComponentes.setFechaCreacion(new Date());
            bienesItemComponentes.setUsuarioCreador(subject.getPrincipal().toString());
            bienesItemComponentes.setPeriodo(bienesMovimiento.getPeriodo());
            bienesItemComponentes.setComponente(Boolean.TRUE);
            bienesItemComponentes.setItemBienBoolean(Boolean.FALSE);
            bienesItemComponentes.setFechaAdquisicion(bienesItemRegistro.getFechaAdquisicion());
            bienesItemComponentes.setCodigoBienAgrupado(bienesItemRegistro.getGrupoPadre().getCodigoBien() + bienesItemRegistro.getCodigoBien() + bienesItemComponentes.getCodigoBien());
            listBienesComponentes.add(bienesItemComponentes);

            for (BienesItem item : listBienesComponentes) {
                bienesItemComponentes.setEstado(Boolean.TRUE);
                bienesItemComponentes.setFechaCreacion(new Date());
                bienesItemComponentes.setUsuarioCreador(subject.getPrincipal().toString());
                bienesItemComponentes.setPeriodo(bienesMovimiento.getPeriodo());
                bienesItemComponentes.setComponente(Boolean.TRUE);
                bienesItemComponentes.setItemBienBoolean(Boolean.FALSE);
                bienesItemComponentes.setCodigoBienAgrupado(bienesItemRegistro.getGrupoPadre().getCodigoBien() + bienesItemRegistro.getCodigoBien() + bienesItemComponentes.getCodigoBien());
            }
        } else {

            this.listBienesComponentes.remove(numero);
            this.listBienesComponentes.add(numero, bienesItemComponentes);
        }
        PrimeFaces.current().executeScript("PF('dlgComponentes').hide()");
    }

    public void borrarCompLista(int item) {
        listBienesComponentes.remove(listBienesComponentes.get(item));
        PrimeFaces.current().ajax().update(":frmMain:dtComponentes");
    }

    public void openDialogFactura() {
        lazyFactura.getFilterss().put("inventarioRegistro:equal", null);
        PrimeFaces.current().ajax().update("frmFact");
        PrimeFaces.current().executeScript("PF('dlgFact').show()");
    }

    public void closeSelectFactura(Factura fact) {
        factura = fact;
        bienesMovimiento.setFactura(fact);
        bienesItemRegistro.setFechaAdquisicion(fact.getFechaFactura());
        PrimeFaces.current().executeScript("PF('dlgFact').hide();");
    }

    public void abrirDialogoFacturaCambios() {
        factura = new Factura();
        PrimeFaces.current().executeScript("PF('dlgFacturas').show()");
    }

    public void guardarFactura() {
        factura.setEstado(Boolean.TRUE);
        facturaService.create(factura);
        PrimeFaces.current().executeScript("PF('dlgFacturas').hide();");
    }

    public void limpiarBienItem() {
        bienesItemRegistro = new BienesItem();
        bienesItemCrear = new BienesItem();
        bienesMovimiento = new BienesMovimiento();
        catalogoBLD = new BienCatalogoBld();
        adquisicionesOrden = new Adquisiciones();
        cuentaContable = new ContCuentas();
        factura = new Factura();
        vidaUtil = new BienVidaUtil();
        listBienesOfActivoFijoGuard = new ArrayList<>();
    }

    public void saveBienItem() {
        Subject subject = SecurityUtils.getSubject();

        if (bienesItemRegistro.getTipoBien() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un Tipo de Bien");
            return;
        }
        if (bienesMovimiento.getMotivoMovimiento() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un motivo del Movimiento");
            return;
        }
        if (bienesItemRegistro.getCatalogoBienes() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un Catalogo de Bienes MEF");
            return;
        }
        if (docReferencia) {
            if (bienesMovimiento.getNumReferencia() == null || bienesMovimiento.getFechaReferencia() == null) {
                JsfUtil.addErrorMessage("ERROR", "Complete los datos del Oficio");
                return;
            }
        }
        Servidor guardalmacenServidor = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        if (guardalmacenServidor == null) {
            JsfUtil.addErrorMessage("Advertencia", "Para guardar un bien, requiere del rol de Guardalmacén");
            return;
        }
        //seteo el codigo del bien
        int cant = bienesItemRegistro.getCantidad();
        int i;
        for (i = 0; i < cant; i++) {
            if (i != 0) {
                bienesItemRegistro = new BienesItem();
                bienesItemRegistro.setPeriodo(bienesItemCrear.getPeriodo());
                bienesItemRegistro.setGrupoPadre(bienesItemCrear.getGrupoPadre());
                bienesItemRegistro.setCatalogoBienes(bienesItemCrear.getCatalogoBienes());
                bienesItemRegistro.setTipoBien(bienesItemCrear.getTipoBien());
                bienesItemRegistro.setClasificTipoBien(bienesItemCrear.getClasificTipoBien());
                bienesItemRegistro.setCuentaContable(bienesItemCrear.getCuentaContable());
                Long orden = bienesItemService.getNivelOrdenBienItem(bienesItemRegistro.getGrupoPadre());
                bienesItemRegistro.setOrden(orden);
                String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItemRegistro.getOrden() + "", 5);
                bienesItemRegistro.setCodigoBien(completarCadenaConCeros);
                bienesItemRegistro.setCodigoBienAgrupado(bienesItemRegistro.getGrupoPadre().getCodigoBienAgrupado() + bienesItemRegistro.getCodigoBien());
                /*Detalles del Bien*/
                bienesItemRegistro.setDescripcion(bienesItemCrear.getDescripcion());
                bienesItemRegistro.setMarca(bienesItemCrear.getMarca());
                bienesItemRegistro.setModelo(bienesItemCrear.getModelo());
                bienesItemRegistro.setColor1(bienesItemCrear.getColor1());
                bienesItemRegistro.setColor2(bienesItemCrear.getColor2());
                bienesItemRegistro.setSerie1(bienesItemCrear.getSerie1());
                bienesItemRegistro.setSerie2(bienesItemCrear.getSerie2());
                bienesItemRegistro.setAnioFabricacion(bienesItemCrear.getAnioFabricacion());
                bienesItemRegistro.setMaterialBien(bienesItemCrear.getMaterialBien());
                bienesItemRegistro.setEstadoBien(bienesItemCrear.getEstadoBien());
                bienesItemRegistro.setUso(bienesItemCrear.getUso());
                bienesItemRegistro.setTieneComponentes(bienesItemCrear.getTieneComponentes());
                bienesItemRegistro.setObservacion(bienesItemCrear.getObservacion());
                bienesItemRegistro.setAlto(bienesItemCrear.getAlto());
                bienesItemRegistro.setAncho(bienesItemCrear.getAncho());
                bienesItemRegistro.setLargo(bienesItemCrear.getLargo());
                bienesItemRegistro.setUnidadMedida(bienesItemCrear.getUnidadMedida());

                bienesItemRegistro.setCantidad(1);

                bienesItemRegistro.setValorTotal(bienesItemCrear.getValorTotal());

                if (bienesItemCrear.getTipoDatoAdicional() != null) {
                    bienesItemRegistro.setPlacaCodigocatastral(bienesItemCrear.getPlacaCodigocatastral());
                    bienesItemRegistro.setSerieMotor(bienesItemCrear.getSerieMotor());
                    bienesItemRegistro.setUbicacionNumchasis(bienesItemCrear.getUbicacionNumchasis());
                    bienesItemRegistro.setFechaInscripcionPredio(bienesItemCrear.getFechaInscripcionPredio());
                }

                bienesItemRegistro.setCostoAdquisicion(bienesItemCrear.getCostoAdquisicion());
                bienesItemRegistro.setFechaAdquisicion(bienesItemCrear.getFechaAdquisicion());
                bienesItemRegistro.setVidaUtil(bienesItemCrear.getVidaUtil());
                bienesItemRegistro.setUtpe(bienesItemCrear.getUtpe());
                bienesItemRegistro.setVidaUtilBien(bienesItemCrear.getVidaUtilBien());
                if (bienesMovimiento.getMotivoMovimiento().getOrden() == 1) {
                    bienesItemRegistro.setFechaUltimDepreciacion(bienesItemCrear.getFechaUltimDepreciacion());
                    bienesItemRegistro.setDepreciacionAcumulada(bienesItemCrear.getDepreciacionAcumulada());
                }

                bienesItemCrear = new BienesItem();
            }
            bienesItemRegistro.setUsuarioCreador(subject.getPrincipal().toString());
            bienesItemRegistro.setFechaCreacion(new Date());
            bienesItemRegistro.setEstado(Boolean.TRUE);
            bienesItemRegistro.setComponente(Boolean.FALSE);
            bienesItemRegistro.setItemBienBoolean(Boolean.TRUE);
//            generarCodigoQr();
            bienesItemCrear = bienesItemService.create(bienesItemRegistro);

            if (bienesItemCrear != null) {
                if (i == 0) {
                    bienesMovimiento.setEstado(Boolean.TRUE);
                    bienesMovimiento.setItemBien(bienesItemCrear);

                    bienesMovimiento.setFechaCreacion(new Date());
                    bienesMovimiento.setUsuarioOriginador(subject.getPrincipal().toString());
                    bienesMovimiento.setFactura(null);
                    if (!adquisicion) {
                        bienesMovimiento.setAdquisiciones(null);
                    }

                    bienesMovimiento = bienesMovimientoService.create(bienesMovimiento);
                    bienesAdicionales = new BienesMovimiento();
                    bienesAdicionales = Utils.clone(bienesMovimiento);
                }

                if (bienesItemCrear.getTieneComponentes()) {
                    if (listBienesComponentes.size() > 0) {
                        if (i == 0) {
                            for (BienesItem comp : listBienesComponentes) {
                                comp.setGrupoPadre(bienesItemCrear);
                                comp.setEstadoBien(bienesItemCrear.getEstadoBien());
                                comp.setUso(bienesItemCrear.getUso());
                                bienesItemComponentes = bienesItemService.create(comp);
                            }
                        } else {
                            for (BienesItem comp : listBienesComponentes) {
                                bienesItemComponentes = new BienesItem();
                                bienesItemComponentes.setOrden(comp.getOrden());
                                bienesItemComponentes.setGrupoPadre(bienesItemCrear);
                                bienesItemComponentes.setCodigoBien(comp.getCodigoBien());
                                bienesItemComponentes.setCodigoBienAgrupado(bienesItemCrear.getGrupoPadre().getCodigoBien() + bienesItemCrear.getCodigoBien() + comp.getCodigoBien());
                                bienesItemComponentes.setUso(bienesItemCrear.getUso());
                                bienesItemComponentes.setFechaCreacion(new Date());
                                bienesItemComponentes.setUsuarioCreador(subject.getPrincipal().toString());
                                bienesItemComponentes.setEstado(Boolean.TRUE);
                                bienesItemComponentes.setPeriodo(bienesMovimiento.getPeriodo());
                                bienesItemComponentes.setComponente(Boolean.TRUE);
                                bienesItemComponentes.setItemBienBoolean(Boolean.FALSE);
                                bienesItemComponentes.setDescripcion(comp.getDescripcion());
                                bienesItemComponentes.setMarca(comp.getMarca());
                                bienesItemComponentes.setSerie1(comp.getSerie1());
                                bienesItemComponentes.setModelo(comp.getModelo());
                                bienesItemComponentes.setCantidad(comp.getCantidad());
                                bienesItemComponentes.setEstadoBien(comp.getEstadoBien());
                                bienesItemComponentes.setObservacion(comp.getObservacion());
                                bienesItemComponentes.setFechaAdquisicion(comp.getFechaAdquisicion());
                                bienesItemComponentes = bienesItemService.create(bienesItemComponentes);
                            }
                        }
                    }
                }

                if (bienesMovimiento != null) {
                    if (i != 0) {
                        bienesItemCrear.setBienesMovimiento(bienesMovimiento);
                        bienesItemCrear.setCantidad(1);
                        if (bienesItemCrear.getCostoAdquisicion() != null && bienesItemCrear.getCantidad() != null) {
                            BigDecimal cantid = BigDecimal.valueOf(bienesItemCrear.getCantidad());
                            BigDecimal result = cantid.multiply(bienesItemCrear.getCostoAdquisicion());
                            bienesItemCrear.setValorTotal(result);
                        }
                        bienesItemService.edit(bienesItemCrear);
                    } else {
                        bienesItemCrear.setCantidad(1);
                        if (bienesItemCrear.getCostoAdquisicion() != null && bienesItemCrear.getCantidad() != null) {
                            BigDecimal cantid = BigDecimal.valueOf(bienesItemCrear.getCantidad());
                            BigDecimal result = cantid.multiply(bienesItemCrear.getCostoAdquisicion());
                            bienesItemCrear.setValorTotal(result);
                        }
                        bienesItemService.edit(bienesItemCrear);

                        if (facturaBoolean) {
                            if (factura.getId() != null) {
                                bienesMovimiento.setFactura(factura);
                                bienesMovimientoService.edit(bienesMovimiento);
                                bienesItemRegistro.setBienesMovimiento(bienesMovimiento);
                                bienesItemCrear.setBienesMovimiento(bienesMovimiento);
                                bienesItemService.edit(bienesItemCrear);
                            }
                        }
                        bienesItemRegistro.setBienesMovimiento(bienesMovimiento);
                        bienesItemCrear.setBienesMovimiento(bienesMovimiento);
                        bienesItemService.edit(bienesItemCrear);
                    }
                }
            }
            listBienesOfActivoFijoGuard.add(bienesItemCrear);
        }
        this.botonCompletartarea = Boolean.FALSE;
        if (lote) {
            JsfUtil.addInformationMessage("Guardado", cant + " Registros Guardados Correctamente.");
            asignarGuardalmacen();
            limpiarBienItem();
        } else {
            JsfUtil.addInformationMessage("Guardado", bienesItemRegistro.getCodigoBienAgrupado() + " " + bienesItemRegistro.getDescripcion() + " Registrado Correctamente.");
            asignarGuardalmacen();
            limpiarBienItem();
        }
        JsfUtil.redirectFaces("/activoFijos/mantenimientoBienes");
    }

    private void generarCodigoQr() {
        DetalleQr detalleQr = new DetalleQr(ruta, bienesItemRegistro.getCodigoBienAgrupado(),
                bienesItemRegistro.getMarca() != null ? bienesItemRegistro.getMarca() : "",
                bienesItemRegistro.getDescripcion() != null ? bienesItemRegistro.getDescripcion() : "", ruta + "plantilla.png",
                bienesItemRegistro.getNombreImagenQR() != null ? bienesItemRegistro.getNombreImagenQR() : "");
        DetalleQrResponse responseQr = katalinaService.generarCodigoQR(detalleQr);
        if (responseQr != null) {
            bienesItemRegistro.setIdAndCodigo(bienesItemRegistro.getCodigoBienAgrupado());
            bienesItemRegistro.setNombreImagenQR(responseQr.getNombreImagen());
            bienesItemRegistro.setUrlImagenQr(responseQr.getUrlImagen());
        }
    }

    public void asignarGuardalmacen() {
        ActivoFijoCustodio guardalmacen = new ActivoFijoCustodio();
        Servidor guardalmacenServidor = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        guardalmacen.setServidor(guardalmacenServidor);
        guardalmacen.setDescripcion("ESTOS BIENES SON ASIGNADOS AL SERVIDOR CON CARGO DE GUARDALMACEN");
        List<BienesItem> bienes = listBienesOfActivoFijoGuard;
        guardalmacen.setCantidadBienes((short) bienes.size());
        guardalmacen.setFechaCreacion(new Date());
        guardalmacen.setUsuarioCreacion(user.getNameUser());
        guardalmacen.setEstado(Boolean.TRUE);
        guardalmacen.setFechaEntrega(new Date());
        /*Traer el número de acta de la ultima acta registrada del guardalmacen*/
        ActivoFijoCustodio ultimaActa = activoFijoCustodioService.getUltimaActa(Boolean.TRUE);
        /*Creamos el activoFijoCustodio*/
        if (ultimaActa != null) {
            guardalmacen.setNumeroActa(BigInteger.valueOf(ultimaActa.getNumeroActa().longValue() + 1));
        } else {
            guardalmacen.setNumeroActa(BigInteger.valueOf(1));
        }
        guardalmacen.setActaGuardalmacen(Boolean.TRUE);
        guardalmacen = activoFijoCustodioService.create(guardalmacen);
        for (BienesItem bien : bienes) {
            ActivoFijoServidor guardalmacen2 = new ActivoFijoServidor();
            guardalmacen2.setActivoFijoCustodio(guardalmacen);
            guardalmacen2.setBienesItem(bien);
            guardalmacen2.setAsignado(Boolean.TRUE);
            guardalmacen2.setFechaAsignacion(new Date());
            guardalmacen2.setEstado(Boolean.FALSE);
            guardalmacen2.setEstadoBien(bien.getEstadoBien().getTexto());
            activoFijoServService.create(guardalmacen2);
        }
        JsfUtil.addInformationMessage("GUARDALMACEN", "Se le asigno correctamente los bienes al guardalmacen");
    }

    public List<BienesItem> encontrandoDetalle(BienesMovimiento b) {
        return bienesMovimientoService.getListadetalle(b);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public BienesItem getBienesItemRegistro() {
        return bienesItemRegistro;
    }

    public void setBienesItemRegistro(BienesItem bienesItemRegistro) {
        this.bienesItemRegistro = bienesItemRegistro;
    }

    public BienesItem getBienesItemComponentes() {
        return bienesItemComponentes;
    }

    public void setBienesItemComponentes(BienesItem bienesItemComponentes) {
        this.bienesItemComponentes = bienesItemComponentes;
    }

    public BienesItem getBienesItemCrear() {
        return bienesItemCrear;
    }

    public void setBienesItemCrear(BienesItem bienesItemCrear) {
        this.bienesItemCrear = bienesItemCrear;
    }

    public BienesMovimiento getBienesMovimiento() {
        return bienesMovimiento;
    }

    public void setBienesMovimiento(BienesMovimiento bienesMovimiento) {
        this.bienesMovimiento = bienesMovimiento;
    }

    public BienCatalogoBld getCatalogoBLD() {
        return catalogoBLD;
    }

    public void setCatalogoBLD(BienCatalogoBld catalogoBLD) {
        this.catalogoBLD = catalogoBLD;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Adquisiciones getAdquisicionesOrden() {
        return adquisicionesOrden;
    }

    public void setAdquisicionesOrden(Adquisiciones adquisicionesOrden) {
        this.adquisicionesOrden = adquisicionesOrden;
    }

    public Distributivo getDistrib() {
        return distrib;
    }

    public void setDistrib(Distributivo distrib) {
        this.distrib = distrib;
    }

    public BienVidaUtil getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(BienVidaUtil vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public ActivoFijoServidor getActivoFijoServidor() {
        return activoFijoServidor;
    }

    public void setActivoFijoServidor(ActivoFijoServidor activoFijoServidor) {
        this.activoFijoServidor = activoFijoServidor;
    }

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }

    public Boolean getBloqueoBtn() {
        return bloqueoBtn;
    }

    public void setBloqueoBtn(Boolean bloqueoBtn) {
        this.bloqueoBtn = bloqueoBtn;
    }

    public Boolean getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Boolean adquisicion) {
        this.adquisicion = adquisicion;
    }

    public Boolean getDocReferencia() {
        return docReferencia;
    }

    public void setDocReferencia(Boolean docReferencia) {
        this.docReferencia = docReferencia;
    }

    public Boolean getFacturaBoolean() {
        return facturaBoolean;
    }

    public void setFacturaBoolean(Boolean facturaBoolean) {
        this.facturaBoolean = facturaBoolean;
    }

    public Boolean getDatosAdicionales() {
        return datosAdicionales;
    }

    public void setDatosAdicionales(Boolean datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    public Boolean getLote() {
        return lote;
    }

    public void setLote(Boolean lote) {
        this.lote = lote;
    }

    public Boolean getVidaUtilBoolean() {
        return vidaUtilBoolean;
    }

    public void setVidaUtilBoolean(Boolean vidaUtilBoolean) {
        this.vidaUtilBoolean = vidaUtilBoolean;
    }

    public Boolean getVidaUtilTempBoolean() {
        return vidaUtilTempBoolean;
    }

    public void setVidaUtilTempBoolean(Boolean vidaUtilTempBoolean) {
        this.vidaUtilTempBoolean = vidaUtilTempBoolean;
    }

    public Boolean getHabilitarVida() {
        return habilitarVida;
    }

    public void setHabilitarVida(Boolean habilitarVida) {
        this.habilitarVida = habilitarVida;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getUtpe() {
        return utpe;
    }

    public void setUtpe(Boolean utpe) {
        this.utpe = utpe;
    }

    public Boolean getBotonGuardar() {
        return botonGuardar;
    }

    public void setBotonGuardar(Boolean botonGuardar) {
        this.botonGuardar = botonGuardar;
    }

    public List<CatalogoMovimiento> getListMotivoMovimiento() {
        return listMotivoMovimiento;
    }

    public void setListMotivoMovimiento(List<CatalogoMovimiento> listMotivoMovimiento) {
        this.listMotivoMovimiento = listMotivoMovimiento;
    }

    public List<CatalogoItem> getListCatalogoItem() {
        return listCatalogoItem;
    }

    public void setListCatalogoItem(List<CatalogoItem> listCatalogoItem) {
        this.listCatalogoItem = listCatalogoItem;
    }

    public List<ContCuentas> getListCuentaContableClasifGrupo() {
        return listCuentaContableClasifGrupo;
    }

    public void setListCuentaContableClasifGrupo(List<ContCuentas> listCuentaContableClasifGrupo) {
        this.listCuentaContableClasifGrupo = listCuentaContableClasifGrupo;
    }

    public List<ContCuentas> getListCuentaContable() {
        return listCuentaContable;
    }

    public void setListCuentaContable(List<ContCuentas> listCuentaContable) {
        this.listCuentaContable = listCuentaContable;
    }

    public List<BienesItem> getListBienesItem() {
        return listBienesItem;
    }

    public void setListBienesItem(List<BienesItem> listBienesItem) {
        this.listBienesItem = listBienesItem;
    }

    public List<ContCuentas> getListBienesItemFilterCta() {
        return listBienesItemFilterCta;
    }

    public void setListBienesItemFilterCta(List<ContCuentas> listBienesItemFilterCta) {
        this.listBienesItemFilterCta = listBienesItemFilterCta;
    }

    public List<CatalogoItem> getListGestionAdquisiciones() {
        return listGestionAdquisiciones;
    }

    public void setListGestionAdquisiciones(List<CatalogoItem> listGestionAdquisiciones) {
        this.listGestionAdquisiciones = listGestionAdquisiciones;
    }

    public List<CatalogoItem> getListEstadoBien() {
        return listEstadoBien;
    }

    public void setListEstadoBien(List<CatalogoItem> listEstadoBien) {
        this.listEstadoBien = listEstadoBien;
    }

    public List<BienesItem> getListBienesComponentes() {
        return listBienesComponentes;
    }

    public void setListBienesComponentes(List<BienesItem> listBienesComponentes) {
        this.listBienesComponentes = listBienesComponentes;
    }

    public List<CatalogoItem> getListDatosAdicionales() {
        return listDatosAdicionales;
    }

    public void setListDatosAdicionales(List<CatalogoItem> listDatosAdicionales) {
        this.listDatosAdicionales = listDatosAdicionales;
    }

    public List<BienVidaUtil> getListVidaUtil() {
        return listVidaUtil;
    }

    public void setListVidaUtil(List<BienVidaUtil> listVidaUtil) {
        this.listVidaUtil = listVidaUtil;
    }

    public List<BienesItem> getListBienesOfActivoFijoGuard() {
        return listBienesOfActivoFijoGuard;
    }

    public void setListBienesOfActivoFijoGuard(List<BienesItem> listBienesOfActivoFijoGuard) {
        this.listBienesOfActivoFijoGuard = listBienesOfActivoFijoGuard;
    }

    public LazyModel<BienCatalogoBld> getLazyCatalogoBLD() {
        return lazyCatalogoBLD;
    }

    public void setLazyCatalogoBLD(LazyModel<BienCatalogoBld> lazyCatalogoBLD) {
        this.lazyCatalogoBLD = lazyCatalogoBLD;
    }

    public LazyModel<BienesItem> getLazyGrupoBien() {
        return lazyGrupoBien;
    }

    public void setLazyGrupoBien(LazyModel<BienesItem> lazyGrupoBien) {
        this.lazyGrupoBien = lazyGrupoBien;
    }

    public LazyModel<Adquisiciones> getLazyGestionAdquisicion() {
        return lazyGestionAdquisicion;
    }

    public void setLazyGestionAdquisicion(LazyModel<Adquisiciones> lazyGestionAdquisicion) {
        this.lazyGestionAdquisicion = lazyGestionAdquisicion;
    }

    public LazyModel<Factura> getLazyFactura() {
        return lazyFactura;
    }

    public void setLazyFactura(LazyModel<Factura> lazyFactura) {
        this.lazyFactura = lazyFactura;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getCodigoStart() {
        return codigoStart;
    }

    public void setCodigoStart(String codigoStart) {
        this.codigoStart = codigoStart;
    }

    public String getCodigoEnd() {
        return codigoEnd;
    }

    public void setCodigoEnd(String codigoEnd) {
        this.codigoEnd = codigoEnd;
    }

    public Short getVidaUtilTemp() {
        return vidaUtilTemp;
    }

    public void setVidaUtilTemp(Short vidaUtilTemp) {
        this.vidaUtilTemp = vidaUtilTemp;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Adquisiciones> getListaAdaqui() {
        return listaAdaqui;
    }

    public void setListaAdaqui(List<Adquisiciones> listaAdaqui) {
        this.listaAdaqui = listaAdaqui;
    }

    public Boolean getBotonAprobaciones() {
        return botonAprobaciones;
    }

    public void setBotonAprobaciones(Boolean botonAprobaciones) {
        this.botonAprobaciones = botonAprobaciones;
    }

    public Boolean getBotonRechazado() {
        return botonRechazado;
    }

    public void setBotonRechazado(Boolean botonRechazado) {
        this.botonRechazado = botonRechazado;
    }

    public Boolean getBotonCompletartarea() {
        return botonCompletartarea;
    }

    public void setBotonCompletartarea(Boolean botonCompletartarea) {
        this.botonCompletartarea = botonCompletartarea;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getBotonProcesos() {
        return botonProcesos;
    }

    public void setBotonProcesos(Boolean botonProcesos) {
        this.botonProcesos = botonProcesos;
    }

    public Boolean getBotonImprimir() {
        return botonImprimir;
    }

    public void setBotonImprimir(Boolean botonImprimir) {
        this.botonImprimir = botonImprimir;
    }

    public BienesMovimiento getBienesAdicionales() {
        return bienesAdicionales;
    }

    public void setBienesAdicionales(BienesMovimiento bienesAdicionales) {
        this.bienesAdicionales = bienesAdicionales;
    }

    public List<BienesMovimiento> getListaBienes() {
        return listaBienes;
    }

    public void setListaBienes(List<BienesMovimiento> listaBienes) {
        this.listaBienes = listaBienes;
    }

    public List<BienesItem> getListaDetalleBen() {
        return listaDetalleBen;
    }

    public void setListaDetalleBen(List<BienesItem> listaDetalleBen) {
        this.listaDetalleBen = listaDetalleBen;
    }

    public List<BienesMovimiento> getListaAcata() {
        return listaAcata;
    }

    public void setListaAcata(List<BienesMovimiento> listaAcata) {
        this.listaAcata = listaAcata;
    }

    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

    public List<CatalogoItem> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<CatalogoItem> medidas) {
        this.medidas = medidas;
    }
//</editor-fold>

}
