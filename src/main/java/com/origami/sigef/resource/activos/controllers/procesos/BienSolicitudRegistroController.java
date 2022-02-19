/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers.procesos;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.activos.service.ResponsableAdquisicionService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.activos.entities.BienCatalogoBld;
import com.origami.sigef.resource.activos.entities.BienVidaUtil;
import com.origami.sigef.resource.activos.services.BienVidaUtilService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "bienSolicitudRegistroView")
@ViewScoped
public class BienSolicitudRegistroController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(BienSolicitudRegistroController.class.getName());

    private BienesItem bienesItem; //Para Grupo
    private BienesItem bienesItemRegistro; //Para un ItemBien
    private BienesItem bienesItemComponentes; //Para un componente
    private BienesItem bienesItemCrear;
    private BienesMovimiento bienesMovimiento;
    private BienCatalogoBld catalogoBLD;
    private ContCuentas cuentaContable;
    private Factura factura;
    private Adquisiciones adquisicionesOrden;
//    private OpcionBusqueda busqueda;
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
    private Long numeroTramite;
    int numero;
    private List<Adquisiciones> listaAdaqui;
    private Boolean botonAprobaciones = Boolean.TRUE;
    private Boolean botonRechazado = Boolean.TRUE;
    private Boolean botonContinuar = Boolean.TRUE;
    private Boolean botonCompletartarea = Boolean.TRUE;
    private String observaciones;
    private Boolean botonProcesos = Boolean.TRUE;
    private Boolean botonImprimir = Boolean.TRUE;
    private BienesMovimiento bienesAdicionales;
    private List<BienesMovimiento> listaBienes;
    private List<BienesItem> listaDetalleBen;

    private List<BienesMovimiento> listaAcata;

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
    private ClienteService clienteService;

    @Inject
    private FacturaService facturaService;

    @Inject
    private BienVidaUtilService vidaUtilService;

    @Inject
    private ActivoFijoServidorService activoFijoServService;

    @Inject
    private ActivoFijoCustodioService activoFijoCustodioService;

    @Inject
    private KatalinaService katalinaService;

    @Inject
    private ProcesoIngresoService procesoIngresoService;

    @Inject
    private ServletSession servletSess;

    @Inject
    private ResponsableAdquisicionService responsableAdquisicionService;

    private List<CatalogoItem> medidas;

    private String ruta; //ruta del codigo qr
    @Inject
    private ValoresService valoresService;
    //PARA LLAMAR AL WEB SERVICES

    @PostConstruct
    public void initView() {

        try {
            ruta = valoresService.findByCodigo("RUTA_CODIGO_QR");
            limpiarVariables();
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                medidas = catalogoItemService.findCatalogoItems("unidad_medida");
                listCatalogoItem = catalogoItemService.findCatalogoItems("tipo_bienes");

                Long tramiteNumero = tramite.getNumTramite();
                BienesMovimiento activoFijo = bienesMovimientoService.findMovimientoBienesByTramite(tramiteNumero);
//                if (activoFijo != null) {
//                    datosByProceso(activoFijo);
//
////                    this.habilitar = Boolean.TRUE;
//                    if (bienesMovimiento.getEstadoAdicional().equals("COMPLETO")) {
//                        this.habilitar = Boolean.TRUE;
//                        this.botonCompletartarea = Boolean.TRUE;
//                        this.botonProcesos = Boolean.FALSE;
//
//                    } else {
//
//                        this.habilitar = Boolean.FALSE;
//                        this.botonCompletartarea = Boolean.FALSE;
//                        this.botonProcesos = Boolean.TRUE;
//                    }
//
//                } else {
                /*Init Grupo Bien*/
                bienesItem.setGrupoPadre(new BienesItem());
                lazyGrupoBien = new LazyModel<>(BienesItem.class);
                lazyGrupoBien.getFilterss().put("estado", Boolean.TRUE);
                lazyGrupoBien.getFilterss().put("grupoPadre:equal", null);
                lazyGrupoBien.getSorteds().put("codigoBienAgrupado", "ASC");
                listBienesItemFilterCta = bienesItemService.getFilterCtaBienGrupo();
                listCuentaContableClasifGrupo = bienesMovimientoService.getClasificacionByTipoBien(3L, "14", "911", "6", "9", null, null);
                /* Termina init Grupo Bien*/

 /*New Bien*/
                catalogoBLD = new BienCatalogoBld();
                bienesMovimiento.setMotivoMovimiento(new CatalogoMovimiento());
                adquisicionesOrden = new Adquisiciones();

                distrib = new Distributivo();
                factura = new Factura();
                bienesItemComponentes = new BienesItem();
                activoFijoServidor = new ActivoFijoServidor();

                bienesMovimiento.setItemBien(bienesItemRegistro);

                bienesMovimiento.setFechaIngreso(new Date());
//                bienesMovimiento.setPeriodo(busqueda.getAnio());
                bienesMovimiento.setAdquisiciones(new Adquisiciones());
                bienesMovimiento.setFactura(new Factura());

                lazyCatalogoBLD = new LazyModel<>(BienCatalogoBld.class);
                lazyCatalogoBLD.getFilterss().put("estado", true);
                lazyCatalogoBLD.getSorteds().put("idBien", "ASC");
                lazyGestionAdquisicion = new LazyModel(Adquisiciones.class);
                lazyFactura = new LazyModel<>(Factura.class);
                lazyFactura.getFilterss().put("estado", Boolean.TRUE);
                this.listGestionAdquisiciones = new ArrayList<>();
                listEstadoBien = catalogoItemService.findCatalogoItems("estado_bien");
//                listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
                listGestionAdquisiciones = catalogoItemService.findCatalogoItems("tipo_proceso");
                listBienesComponentes = new ArrayList<>();
                listDatosAdicionales = catalogoItemService.findCatalogoItems("datos_adicionales_bienes");
                listVidaUtil = new ArrayList<>();
                listBienesItem = new ArrayList<>();
                listBienesOfActivoFijoGuard = new ArrayList<>();
                bienesItemRegistro.setGrupoPadre(new BienesItem());
                listMotivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("BIEN-ING", "FLU-PROCB");
                habilitar = Boolean.FALSE;
                docReferencia = Boolean.TRUE;
                vidaUtilBoolean = Boolean.TRUE;
                vidaUtilTempBoolean = Boolean.FALSE;
                habilitarVida = Boolean.TRUE;
                utpe = Boolean.FALSE;
                this.listaAcata = bienesMovimientoService.getTramitelistaBienes(tramite.getNumTramite());
                formNewBien();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void limpiarVariables() {
        cuentaContable = new ContCuentas();
        bienesItem = new BienesItem();
        bienesItemRegistro = new BienesItem();
        bienesMovimiento = new BienesMovimiento();
        this.listaBienes = new ArrayList<>();
        this.listaDetalleBen = new ArrayList<>();
        this.listaAcata = new ArrayList<>();
    }

    //<editor-fold defaultstate="collapsed" desc="CREACION DE UN GRUPO">
    public void formNewGrupo(BienesItem itemGrupo) {
        Subject subject = SecurityUtils.getSubject();
        habilitar = Boolean.TRUE;
        if (itemGrupo != null) {
            habilitar = Boolean.TRUE;
            this.bienesItem = itemGrupo;
            bienesItem.setFechaModificacion(new Date());
            bienesItem.setUsuarioModifica(subject.getPrincipal().toString());
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').show()");

        } else {
            bienesItem = new BienesItem();
            bienesItem.setCuentaContable(new ContCuentas());
            bienesItem.setUsuarioCreador(subject.getPrincipal().toString());
            bienesItem.setFechaCreacion(new Date());
//            Short anio = 2019;
//            bienesItem.setPeriodo(busqueda.getAnio());
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').show()");
            PrimeFaces.current().ajax().update("frmRegClasificacion");
        }
    }

    public void buscarCuentaGrupo() {
        if (bienesItem.getClasificTipoBien() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Clasificación");
            return;
        }
        if (bienesItem.getCuentaContable().getCodigo() != null) {
            ContCuentas cta = bienesItemService.getCuentaContable(bienesItem.getCuentaContable().getCodigo(), bienesItem.getClasificTipoBien().getCodigo());
            if (cta != null) {
                this.bienesItem.setCuentaContable(cta);
                Long orden = bienesItemService.getNivelOfGrupo(bienesItem.getCuentaContable());
                bienesItem.setOrden(orden);
                String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItem.getOrden() + "", 2);
                bienesItem.setCodigoBien(completarCadenaConCeros);
                PrimeFaces.current().ajax().update("fsetCuentaGrupo");
                PrimeFaces.current().ajax().update("codigoBien");
            } else {
                Map<String, List<String>> params = new HashMap<>();
                params.put("TIPOACTIVO", Arrays.asList("BIENES"));
//                params.put("PERIODO", Arrays.asList("" + busqueda.getAnio()));
                params.put("CODIGO", Arrays.asList(bienesItem.getClasificTipoBien().getCodigo()));
                Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", params);
            }
        } else {
            Map<String, List<String>> params = new HashMap<>();
            params.put("TIPOACTIVO", Arrays.asList("BIENES"));
//            params.put("PERIODO", Arrays.asList(busqueda.getAnio() + ""));
            params.put("CODIGO", Arrays.asList(bienesItem.getClasificTipoBien().getCodigo()));
            Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", params);
        }
    }

    public void limpiarCuentaGrupo() {
        bienesItem.setCuentaContable(new ContCuentas());
//        bienesItem.setOrden(null);
        bienesItem.setCodigoBien(null);
    }

    public void selectCuentaGrupo(SelectEvent event) {
        cuentaContable = (ContCuentas) event.getObject();
        bienesItem.setCuentaContable(cuentaContable);
        Long orden = bienesItemService.getNivelOfGrupo(bienesItem.getCuentaContable());
//            BigInteger ordenBig = BigInteger.valueOf(orden);
        bienesItem.setOrden(orden);
        String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItem.getOrden() + "", 2);
        bienesItem.setCodigoBien(completarCadenaConCeros);
        cuentaContable = new ContCuentas();
    }

    public void borrarBienGrupoLista(BienesItem grup) {
        grup.setEstado(Boolean.FALSE);
        bienesItemService.edit(grup);
        PrimeFaces.current().ajax().update(":dtGrupoBien");
    }

    public void cancelarGrupoBien() {
        bienesItem = new BienesItem();
        cuentaContable = new ContCuentas();
        habilitar = Boolean.FALSE;
        PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').hide()");
    }

    public void saveGrupoBien() {
        if (bienesItem.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Ingrese una descripción");
            return;
        }
        if (bienesItem.getCuentaContable() == null) {
            JsfUtil.addErrorMessage("ERROR", "Asigne una Cuenta Contable al Grupo");
            return;
        }
        if (bienesItem.getCodigoBienAgrupado() != null) {
            habilitar = Boolean.FALSE;
            bienesItemService.edit(bienesItem);
            JsfUtil.addInformationMessage("Editado", bienesItem.getCodigoBienAgrupado() + " Correctamente editado.");
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').hide()");
        } else {
            bienesItem.setEstado(Boolean.TRUE);
            habilitar = Boolean.FALSE;
            bienesItem.setItemBienBoolean(Boolean.FALSE);
            bienesItem.setComponente(Boolean.FALSE);
            bienesItem.setCodigoBienAgrupado(bienesItem.getCuentaContable().getCodigo() + bienesItem.getCodigoBien());
            bienesItemService.create(bienesItem);
            JsfUtil.addInformationMessage("Guardado", bienesItem.getCodigoBienAgrupado() + " Correctamente Guardado.");
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').hide()");
        }

    }
//</editor-fold>

    /*CREACION Y REGISTRO DE BIEN*/
    public void formNewBien() {

        bienesMovimiento = new BienesMovimiento();
        bienesItemRegistro = new BienesItem();
        bienesMovimiento.setFechaIngreso(new Date());
        this.anio = Utils.getAnio(bienesMovimiento.getFechaIngreso());
        bienesMovimiento.setPeriodo(anio.shortValue());
//            bienesMovimiento.setPeriodo(Short.valueOf("2019"));

        Long orden = bienesMovimientoService.getNivelBienesMov();
        bienesMovimiento.setOrden(orden);
        String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesMovimiento.getOrden() + "", 5);
        bienesMovimiento.setCodigo("BIEN-" + bienesMovimiento.getPeriodo() + "-" + completarCadenaConCeros);

        bienesMovimiento.setFechaCreacion(new Date());
        bienesItemCrear = new BienesItem();
        bienesMovimiento.setItemBien(new BienesItem());
        bienesMovimiento.setAdquisiciones(new Adquisiciones());
        bienesMovimiento.setFactura(new Factura());
        bienesItemComponentes = new BienesItem();
        cuentaContable = new ContCuentas();
        bienesItemRegistro.setGrupoPadre(new BienesItem());
//        bienesItemRegistro.setPeriodo(busqueda.getAnio());
        bienesItemRegistro.setEstadoBien(bienesItemService.getEstadoBien("bien_bueno", "estado_bien"));
        lote = Boolean.FALSE;
        habilitarVida = Boolean.FALSE;
        bienesItemRegistro.setCantidad(1);
        botonAprobaciones = Boolean.TRUE;
        botonContinuar = Boolean.TRUE;
        botonRechazado = Boolean.TRUE;

        codigoStart = "01";
        codigoEnd = "99";

        bienesMovimiento.setEstado(true);
        bienesItemRegistro.setEstado(Boolean.TRUE);

        if (!listMotivoMovimiento.isEmpty()) {
            bienesMovimiento.setMotivoMovimiento(listMotivoMovimiento.get(0));
        }
    }

    public void openDlgBienMEF() {
        PrimeFaces.current().executeScript("PF('dlgCatalogoBien').show()");
    }

    public void selectDataCatalogo(BienCatalogoBld evt) {
        catalogoBLD = evt;
        bienesItemRegistro.setCatalogoBienes(catalogoBLD);
        PrimeFaces.current().executeScript("PF('dlgCatalogoBien').hide()");
    }

    public void openDlgAdquisicion() {

        this.listaAdaqui = new ArrayList<>();
        this.listaAdaqui = bienesItemService.getListaAdquisiciones();
        if (!listaAdaqui.isEmpty()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "Informar a Contabilidad el registro de Contrato de Adquisición."));
            JsfUtil.addWarningMessage("AVISO!!", "Informar a Contabilidad el registro de Contrato de Adquisición.");
        }
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgAdquisicion");
    }

    public void closeSelectAdquisicion(Adquisiciones adq) {
        adquisicionesOrden = adq;
        bienesMovimiento.setAdquisiciones(adquisicionesOrden);
        //Setear el movimiento del bien
//        bienesRegReferencia.setMovimientoBien(bienesMovimiento);
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').hide()");
    }

    public void mostrarTodasAdquisiciones() {
        this.listaAdaqui = new ArrayList<>();
        this.listaAdaqui = bienesItemService.getTodasListaAdquisiciones();
    }

    public void buscarCuenta() {
        if (bienesItemRegistro.getTipoBien() == null) {
            JsfUtil.addWarningMessage("Error", "Ingrese un tipo de Bien");
            return;
        }
        if (bienesMovimiento.getMotivoMovimiento() == null) {
            JsfUtil.addWarningMessage("Error", "Ingrese un tipo de adquisición");
            return;
        }
        if (bienesItemRegistro.getClasificTipoBien() == null) {
            JsfUtil.addWarningMessage("Error", "Ingrese una clasificación para asignar Cuenta Contable");
            return;
        }
        if (bienesItemRegistro.getClasificTipoBien().getCodigo().equals("148")) {
            codigoStart = "01";
            codigoEnd = "98";
        }
        if (bienesItemRegistro.getClasificTipoBien().getCodigo().equals("911")) {
            codigoStart = "01";
            codigoEnd = "99";
        }

        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPOACTIVO", Arrays.asList("BIENES"));
//        params.put("PERIODO", Arrays.asList("" + bienesMovimiento.getPeriodo()));
        params.put("MOVIMIENTO", Arrays.asList(bienesMovimiento.getMotivoMovimiento().toString()));
        params.put("TIPOBIEN", Arrays.asList(bienesItemRegistro.getTipoBien().getCodigo()));
        params.put("CODIGO", Arrays.asList(bienesItemRegistro.getClasificTipoBien().getCodigo()));
        params.put("CODSTART", Arrays.asList(codigoStart));
        params.put("CODEND", Arrays.asList(codigoEnd));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", params);
    }

    public void obtenerVidaUtil() {
        listVidaUtil = vidaUtilService.getVidaUtilByCuenta(cuentaContable);
        if (listVidaUtil.isEmpty()) {
            vidaUtilBoolean = Boolean.TRUE;
            vidaUtilTempBoolean = Boolean.FALSE;
            habilitarVida = Boolean.FALSE;
            bienesItemRegistro.setMaterialBien(null);
            bienesItemRegistro.setVidaUtil(null);
        } else {
            if (listVidaUtil.size() > 1) {
                vidaUtilTempBoolean = Boolean.TRUE;
                vidaUtilBoolean = Boolean.FALSE;

                for (BienVidaUtil vidaUtilBien : listVidaUtil) {
                    if (vidaUtilBien.getVidaUtil() == null) {
                        habilitarVida = Boolean.FALSE;
                        utpe = Boolean.TRUE;
                    } else {
                        habilitarVida = Boolean.TRUE;
                    }
                    bienesItemRegistro.setMaterialBien(null);
                    if (vidaUtil != null) {
                        bienesItemRegistro.setVidaUtilBien(vidaUtil);
                        bienesItemRegistro.setVidaUtil(vidaUtil.getVidaUtil());
                        bienesItemRegistro.setMaterialBien(vidaUtil.getDescripcion());
                    }
                }
            } else {
                if (listVidaUtil.size() == 1) {
                    vidaUtilBoolean = Boolean.TRUE;
                    vidaUtilTempBoolean = Boolean.FALSE;
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
    }

    public void limpiarCuentaBienItem() {
        cuentaContable = new ContCuentas();
        bienesItemRegistro.setCuentaContable(new ContCuentas());
        bienesItemRegistro.setGrupoPadre(new BienesItem());
//        bienesItemRegistro.setOrden(null);
        bienesItemRegistro.setCodigoBien(null);
        bienesItemRegistro.setCodigoBienAgrupado(null);
    }

    public void buscarGrupoBien() {
        if (cuentaContable == null) {
            JsfUtil.addWarningMessage("Error", "Asigne una Cuenta Contable");
            PrimeFaces.current().ajax().update("messages");
        } else {
            listBienesItem = bienesItemService.getBienGrupoByCuenta(cuentaContable.getCodigo());
            PrimeFaces.current().executeScript("PF('dlgGrupoBien').show()");
            PrimeFaces.current().ajax().update(":frmItem");
        }
    }

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
            if (facturaBoolean) {

            }

            PrimeFaces.current().executeScript("PF('dlgGrupoBien').hide()");
            PrimeFaces.current().ajax().update(":asignarCtaGru");
        } else {
            JsfUtil.addErrorMessage("ERROR", "Error al asignar un SubGrupo");
        }

    }

    public void selectCuentaBien(SelectEvent event) {
        cuentaContable = (ContCuentas) event.getObject();
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

        String clasif = null;
        String codigo = null;

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

    public void borrarCompLista(BienesItem item) {
        listBienesComponentes.remove(item);
        PrimeFaces.current().ajax().update(":frmMain:dtComponentes");
    }

    public void openDialogFactura() {
        factura = new Factura();
        lazyFactura.getFilterss().put("inventarioRegistro:equal", null);
        PrimeFaces.current().executeScript("PF('dlgFact').show()");
        PrimeFaces.current().ajax().update("frmFact");
    }

    public void closeSelectFactura(Factura fact) {
        factura = fact;
        bienesMovimiento.setFactura(fact);
        bienesItemRegistro.setFechaAdquisicion(fact.getFechaFactura());
        PrimeFaces.current().executeScript("PF('dlgFact').hide();");
    }

    public void abrirDialogoFacturaCambios() {
        if (factura == null) {
            factura = new Factura();
        }
        PrimeFaces.current().executeScript("PF('dlgFacturas').show()");
    }

    public void guardarFactura() {
        factura.setEstado(Boolean.TRUE);
        facturaService.create(factura);
        PrimeFaces.current().executeScript("PF('dlgFacturas').hide();");
    }

    public void limpiarBienItem() {
        bienesItemRegistro = new BienesItem();
        bienesMovimiento = new BienesMovimiento();
        catalogoBLD = new BienCatalogoBld();
        adquisicionesOrden = new Adquisiciones();
//        bienesMovimiento.setItemBien(bienesItemRegistro);
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
        if (adquisicionesOrden.getId() == null) {
            JsfUtil.addErrorMessage("ERROR", "Debe seleccionar una adquisicion");
            return;
        }
        if (bienesItemRegistro.getCatalogoBienes() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un Catalogo de Bienes MEF");
            return;
        }
        if (factura.getId() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese Datos de Factura");
            return;
        }
        Servidor guardalmacenServidor = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        if (guardalmacenServidor == null) {
            JsfUtil.addErrorMessage("Advertencia", "Para guardar un bien, requiere que un funcionario haya sido asignado con el rol de Guardalmacén");
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
//            bienesItemRegistro.setBienesMovimiento(mov);
            bienesItemRegistro.setEstado(Boolean.TRUE);

            bienesItemRegistro.setComponente(Boolean.FALSE);
            bienesItemRegistro.setItemBienBoolean(Boolean.TRUE);
            generarCodigoQr();
            bienesItemCrear = bienesItemService.create(bienesItemRegistro);

            if (bienesItemCrear != null) {
                if (i == 0) {
//                    bienesMovimiento.setPeriodo(busqueda.getAnio());
                    bienesMovimiento.setEstado(Boolean.TRUE);
                    bienesMovimiento.setItemBien(bienesItemCrear);

                    bienesMovimiento.setFechaCreacion(new Date());
                    bienesMovimiento.setUsuarioOriginador(subject.getPrincipal().toString());
                    bienesMovimiento.setFactura(null);
                    if (!adquisicion) {
                    }
                    bienesMovimiento.setAdquisiciones(null);

                    bienesMovimiento.setNumeroTramite(tramite.getNumTramite());
                    bienesMovimiento.setEstadoAdicional("INCOMPLETO");
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
                                bienesItemComponentes = bienesItemService.create(bienesItemComponentes);
                            }
                        }

                    } else {
//                        System.out.println("No existe componentes...");
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
                        if (adquisicion) {
                            if (adquisicionesOrden != null) {
                                bienesMovimiento.setAdquisiciones(adquisicionesOrden);
                                bienesMovimiento.setProveedor(adquisicionesOrden.getProveedor());
                                bienesMovimientoService.edit(bienesMovimiento);
                                bienesItemCrear.setBienesMovimiento(bienesMovimiento);
                                bienesItemRegistro.setBienesMovimiento(bienesMovimiento);
                                bienesItemService.edit(bienesItemCrear);
                            } else {
                                JsfUtil.addErrorMessage("ERROR", "Seleccione una Orden de Adquisición");
                            }
                        }
                        if (facturaBoolean) {
                            if (factura.getId() != null) {
//                                factura.setEstado(Boolean.TRUE);
//                                Factura fact = facturaService.create(factura);
                                bienesMovimiento.setFactura(factura);
                                bienesMovimientoService.edit(bienesMovimiento);
                                bienesItemRegistro.setBienesMovimiento(bienesMovimiento);
                                bienesItemCrear.setBienesMovimiento(bienesMovimiento);
                                bienesItemService.edit(bienesItemCrear);
                            } else {
                                JsfUtil.addErrorMessage("ERROR", "Ingrese Datos de Factura");
                            }
                        }
                    }

                }
            }
            if (lote) {
                JsfUtil.addInformationMessage("Guardado", cant + " Registros Guardados Correctamente.");
            } else {
                JsfUtil.addInformationMessage("Guardado", bienesItemRegistro.getCodigoBienAgrupado() + " Registrado Correctamente.");
                listBienesOfActivoFijoGuard.add(bienesItemCrear);
            }
//            if (bienesItemCrear != null) {
//                activoFijoServidor.setEstado(Boolean.FALSE);
//                activoFijoServidor.setAsignado(Boolean.TRUE);
////                activoFijoServidor.setActivoFijoCustodio(bienesItemService.getGuardalmacen("GUARDALMACÉN"));
//                activoFijoServidor.setFechaAsignacion(new Date());
//                activoFijoServidor.setBienesItem(bienesItemCrear);
//                activoFijoServService.create(activoFijoServidor);
//            }

        }
        this.botonCompletartarea = Boolean.FALSE;
        this.botonGuardar = Boolean.TRUE;
        this.botonContinuar = Boolean.FALSE;

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
        guardalmacen.setServidor(clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen));
        guardalmacen.setDescripcion("ESTOS BIENES SON ASIGNADOS AL SERVIDOR CON CARGO DE GUARDALMACEN");
        List<BienesItem> bienes = bienesMovimientoService.getlistaBienesByTramite(tramite.getNumTramite());
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
            guardalmacen2 = new ActivoFijoServidor();
//            System.out.println("Guardalmacen " + bienes.size());
//            System.out.println("Guardalmacen " + bien.getCodigoBienAgrupado());
        }
        JsfUtil.addInformationMessage("GUARDALMACEN", "Se le asigno correctamente los bienes al guardalmacen");
    }

    public void datosByProceso(BienesMovimiento bien) {
        this.bienesMovimiento = bien;
        listMotivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("BIEN-ING", "FLU-PROCB");
//        busqueda = new OpcionBusqueda();
        this.bienesItemRegistro = new BienesItem();
        this.cuentaContable = new ContCuentas();
        this.listBienesItem = new ArrayList<>();
        this.listaAdaqui = new ArrayList<>();
        this.listVidaUtil = new ArrayList<>();
        factura = new Factura();
        listEstadoBien = catalogoItemService.findCatalogoItems("estado_bien");
//        listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listDatosAdicionales = catalogoItemService.findCatalogoItems("datos_adicionales_bienes");
        listCatalogoItem = catalogoItemService.findCatalogoItems("tipo_bienes");

//        this.proveedor = bien.getProveedor();
//        busqueda.setAnio(bienesMovimiento.getPeriodo());
        this.bienesItemRegistro = bienesMovimientoService.findBienesItemByMovimiento(bienesMovimiento);
//        List<InventarioItems> itemsDevueltos = inventarioItemsService.getItemByInventarioItems(inv);
        Adquisiciones adqui = bienesMovimiento.getAdquisiciones();
        this.listaAdaqui = bienesItemService.getListaAdquisiciones();
        cuentaContable = bienesItemRegistro.getCuentaContable();
        catalogoBLD = bienesItemRegistro.getCatalogoBienes();
        mostrarClasificacionCuenta();
        vidaUtilProcesos();
        if (bienesMovimiento.getFactura() != null) {
            factura = bienesMovimiento.getFactura();
        } else {
            factura = null;
            bienesMovimiento.setFactura(factura);
        }

        if (bienesMovimiento.getAdquisiciones() != null) {
            adquisicionesOrden = bienesMovimiento.getAdquisiciones();
        } else {
            adquisicionesOrden = null;
            bienesMovimiento.setAdquisiciones(adquisicionesOrden);
        }
        bienesAdicionales = new BienesMovimiento();
        bienesAdicionales = Utils.clone(bienesMovimiento);

//        Integer V = itemsDevueltos.size();
//        for (Integer w = 0; w < V; w++) {
//            detalleItem.setCodigo(itemsDevueltos.get(w).getDetalleItem().getCodigo());
//            detalleItem.setDescripcion(itemsDevueltos.get(w).getDetalleItem().getDescripcion());
//            detalleItem.setAsignarGrupo(itemsDevueltos.get(w).getDetalleItem().getAsignarGrupo());
//            detalleItem.setCuentaContable(itemsDevueltos.get(w).getDetalleItem().getCuentaContable());
//            detalleItem.setEstante(itemsDevueltos.get(w).getDetalleItem().getEstante());
//            detalleItem.setFila(itemsDevueltos.get(w).getDetalleItem().getFila());
//            detalleItem.setColumna(itemsDevueltos.get(w).getDetalleItem().getColumna());
//            detalleItem.setCajon(itemsDevueltos.get(w).getDetalleItem().getCajon());
//            detalleItem.setCuadrante(itemsDevueltos.get(w).getDetalleItem().getCuadrante());
//            detalleItem.setTipoMedida(itemsDevueltos.get(w).getDetalleItem().getTipoMedida());
//            detalleItem.setCantidadTemp(itemsDevueltos.get(w).getCantidad());
//            detalleItem.setPrecioTemp(itemsDevueltos.get(w).getPrecio());
//            detalleItem.setTotalTemp(itemsDevueltos.get(w).getTotal());
//            this.listDetalle.add(this.detalleItem);
//            detalleItem = new DetalleItem();
//        }
//        mostrarBtn();
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void vidaUtilProcesos() {
        listVidaUtil = vidaUtilService.getVidaUtilByCuenta(cuentaContable);
//        System.out.println("Size de lista Vida Util" + listVidaUtil.size());
        if (listVidaUtil.isEmpty()) {
//            System.out.println("Vacio");
            vidaUtilBoolean = Boolean.TRUE;
            vidaUtilTempBoolean = Boolean.FALSE;
            habilitarVida = Boolean.FALSE;
        } else {
//            System.out.println("Contienelista Vida Util");
            if (listVidaUtil.size() > 1) {
//                System.out.println("Mayor a 1");
                vidaUtilTempBoolean = Boolean.TRUE;
                vidaUtilBoolean = Boolean.FALSE;
                if (vidaUtil.getVidaUtil() == null) {
                    habilitarVida = Boolean.FALSE;
                    utpe = Boolean.TRUE;
                } else {
                    habilitarVida = Boolean.TRUE;
//                    System.out.println("Vida cuando es mayor a 0 " + vidaUtil.getVidaUtil());
                }

            } else {
                if (listVidaUtil.size() == 1) {
//                    System.out.println("Igual a 1");
                    vidaUtilBoolean = Boolean.TRUE;
                    vidaUtilTempBoolean = Boolean.FALSE;

                    for (BienVidaUtil vidaUtilBien : listVidaUtil) {
                        if (vidaUtilBien.getVidaUtil() == null) {
                            habilitarVida = Boolean.FALSE;
                            utpe = Boolean.TRUE;
                        } else {
                            habilitarVida = Boolean.TRUE;
                        }
//                        System.out.println("Vida ... " + bienesItemRegistro.getVidaUtil());
                    }
                }
            }
        }
    }

    public List<BienesItem> encontrandoDetalle(BienesMovimiento b) {

        return bienesMovimientoService.getListadetalle(b);

    }

    public void continuarProcesoEstados() {
        this.listaBienes = new ArrayList<>();
        this.listaBienes = bienesMovimientoService.getTramitelistaBienes(tramite.getNumTramite());

        if (listaBienes == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Adviertencia", "Realice un registro para poder terminar la tarea");
            return;
        }

        JsfUtil.executeJS("PF('dlgoBienesMostrar').show()");
        PrimeFaces.current().ajax().update(":fmDlogoMostrar");
        botonAprobaciones = Boolean.FALSE;
        botonContinuar = Boolean.TRUE;
        botonRechazado = Boolean.FALSE;

    }

    public void confirmarTerminoProcesos() {

        bienesMovimientoService.getAcualiizarEstadosSql("COMPLETO", tramite.getNumTramite());

        this.botonProcesos = Boolean.FALSE;
        this.botonCompletartarea = Boolean.TRUE;
        this.habilitar = Boolean.TRUE;
        asignarGuardalmacen();
        JsfUtil.executeJS("PF('dlgoBienesMostrar').hide()");
        PrimeFaces.current().ajax().update(":fmDlogoMostrar");

    }

    public void ingresarObservaciones(boolean aprobar) {

        List<BienesMovimiento> bienes = bienesMovimientoService.getTramitelistaBienes(tramite.getNumTramite());

        if (bienes.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("!", "No se puede aprobar xq o hay ningun registro");
        } else {

            for (BienesMovimiento data : bienes) {
                if (!"COMPLETO".equals(data.getEstadoAdicional())) {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("!", "tiene que terminar la tarea");
                    return;
                }
            }
        }

        if (aprobar) {
            this.botonAprobaciones = Boolean.FALSE;
            this.botonRechazado = Boolean.TRUE;

        } else {
            this.botonAprobaciones = Boolean.TRUE;
            this.botonRechazado = Boolean.FALSE;

            bienesAdicionales.setEstadoAdicional("RECHAZADO");
            bienesMovimientoService.edit(bienesAdicionales);

//            System.out.println("rechazado " + bienesAdicionales.getEstadoAdicional());
        }

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());

        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void aprobacionPrcoeso(int aprobado) {

        if (aprobado == 1) {
            this.botonImprimir = Boolean.FALSE;
            this.habilitar = Boolean.TRUE;
            this.botonProcesos = Boolean.TRUE;

        }

        observacion.setObservacion(observaciones);
        try {

            getParamts().put("aprobado", aprobado);
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.guardaAlmacen));

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void enviarCorreo(String email, String asunto, String userStart) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;text-align: justify;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA QUE " + tramite.getTipoTramite().getDescripcion().toUpperCase() + " HA SIDO APROBADO "
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
    }

    public void imprimir() {
        this.listaAcata = bienesMovimientoService.getTramitelistaBienes(tramite.getNumTramite());
        if (this.listaAcata.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No se puede imprimir");
            return;
        }
        for (int i = 0; i < listaAcata.size(); i++) {
            bienesAdicionales = listaAcata.get(0);
        }
        Servidor serv = responsableAdquisicionService.getAdministradorContrato(bienesAdicionales.getAdquisiciones());
        if (serv != null) {
            servletSess.addParametro("admin_nombre", serv.getPersona().getNombreCompleto());
            servletSess.addParametro("admin_cargo", serv.getDistributivo().getCargo().getNombreCargo());
        } else {
            servletSess.addParametro("admin_nombre", "");
            servletSess.addParametro("admin_cargo", "");
        }
        System.out.println("Ema " + bienesAdicionales.getUsuarioOriginador());
//        correo responsables
//        Cliente aux = procesoIngresoService.getEemailGuardalmacenmailGuardalmacen(bienesAdicionales.getUsuarioOriginador());
        Cliente emailRepresentanteLegal = new Cliente();
        Cliente emailGuardalmacen = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen).getPersona();
        Cliente emailProveedor = procesoIngresoService.getEmailProveedor(bienesAdicionales.getProveedor());
        if (bienesAdicionales.getProveedor().getContacto() != null) {
            emailRepresentanteLegal = procesoIngresoService.getEmailRepresentanteLegal(bienesAdicionales.getProveedor());
        }
        Cliente emailAdministradorContrato = procesoIngresoService.getEmailAdminContratoEnBienes(bienesAdicionales.getAdquisiciones());
        List<Cliente> correoTipo1 = new ArrayList<>(), correoTipo2 = new ArrayList<>();
        //LLENADO LISTA CORREO TIPO 1
        if (emailGuardalmacen != null) {
            correoTipo1.add(emailGuardalmacen);
        }
        if (emailProveedor != null) {
            correoTipo1.add(emailProveedor);
        }
        if (emailAdministradorContrato != null) {
            correoTipo1.add(emailAdministradorContrato);
        }
        //LLENADO LISTA CORREO TIPO 2
        if (emailGuardalmacen != null) {
            correoTipo2.add(emailGuardalmacen);
        }
        if (emailProveedor != null) {
            correoTipo2.add(emailProveedor);
        }
        if (emailAdministradorContrato != null) {
            correoTipo2.add(emailAdministradorContrato);
        }
        if (emailRepresentanteLegal != null) {
            correoTipo2.add(emailRepresentanteLegal);
        }
        if (bienesAdicionales.getProveedor().getCliente().getClasificacionProv() != null) {
            if (bienesAdicionales.getProveedor().getCliente().getClasificacionProv().getOrden() == 1) { //es natural
                correoTipo1.forEach((cli1) -> {
                    enviarCorreo(cli1.getEmail(), tramite.getTipoTramite().getDescripcion(), cli1.getNombreCompleto());
                });

            } else {
                correoTipo2.forEach((cli2) -> {
                    enviarCorreo(cli2.getEmail(), tramite.getTipoTramite().getDescripcion(), cli2.getNombreCompleto());
                });
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede determinar la clasificación del proveedor de la adquisición seleccionada, se recomienda editar el proveedor");
            return;
        }
        if (bienesAdicionales.getMotivoMovimiento().getOrden() == 2) {
            if (bienesAdicionales.getProveedor().getCliente().getClasificacionProv() == null) {
                JsfUtil.addWarningMessage("Advertencia", "Verificar que los datos del Proveedor " + bienesAdicionales.getProveedor().getCliente().getNombreCompleto() + " estén correctamente agregados.");
                return;
            }
            if (bienesAdicionales.getProveedor().getCliente().getClasificacionProv().getCodigo().equals("PER_NAT")) {
                servletSess.addParametro("tramite", tramite.getNumTramite());
                servletSess.setNombreReporte("actaEntregaRecepcBienesGuardaProvNatural");
            } else {
                servletSess.addParametro("tramite", tramite.getNumTramite());
                servletSess.setNombreReporte("actaEntregaRecepcBienesGuardaProv");
            }
            servletSess.setNombreSubCarpeta("reportesBienes");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

        }
    }

    public void completarFinalizarTarea() {
        try {

            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS & SETTERS">
    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

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

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
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

    public Boolean getBotonContinuar() {
        return botonContinuar;
    }

    public void setBotonContinuar(Boolean botonContinuar) {
        this.botonContinuar = botonContinuar;
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

    public BienVidaUtilService getVidaUtilService() {
        return vidaUtilService;
    }

    public void setVidaUtilService(BienVidaUtilService vidaUtilService) {
        this.vidaUtilService = vidaUtilService;
    }

    public ProcesoIngresoService getProcesoIngresoService() {
        return procesoIngresoService;
    }

    public void setProcesoIngresoService(ProcesoIngresoService procesoIngresoService) {
        this.procesoIngresoService = procesoIngresoService;
    }

    public ServletSession getServletSess() {
        return servletSess;
    }

    public void setServletSess(ServletSession servletSess) {
        this.servletSess = servletSess;
    }

    public ResponsableAdquisicionService getResponsableAdquisicionService() {
        return responsableAdquisicionService;
    }

    public void setResponsableAdquisicionService(ResponsableAdquisicionService responsableAdquisicionService) {
        this.responsableAdquisicionService = responsableAdquisicionService;
    }

    public List<CatalogoItem> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<CatalogoItem> medidas) {
        this.medidas = medidas;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
//</editor-fold>

}
