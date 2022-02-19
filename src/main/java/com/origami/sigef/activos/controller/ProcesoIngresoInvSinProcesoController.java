/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.activos.service.ConstatacionFisicaInvService;
import com.origami.sigef.activos.service.DetalleConstFisicaService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.activos.service.InventarioRegistroService;
import com.origami.sigef.activos.service.OficioService;
import com.origami.sigef.activos.service.OrdenCompraService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.administracionCompra.controller.Numero_Letras;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ConstatacionFisicaInventario;
import com.origami.sigef.common.entities.DetalleConstFisicaInventario;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.InventarioRegistro;
import com.origami.sigef.common.entities.Oficio;
import com.origami.sigef.common.entities.OrdenCompra;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "procesoIngresoInvSinProView")
@ViewScoped
public class ProcesoIngresoInvSinProcesoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvSinProcesoController.class.getName());
    private CatalogoMovimiento catalogoMovimiento;
    private List<CatalogoMovimiento> motivoMovimiento;
    private Inventario inventario;
    private Inventario impresion;
    private Inventario inventarioEgreso;
    private Proveedor proveedor;
    private Oficio oficio;
    private Oficio guiaRem;
    private OrdenCompra ordenCompra;
    private SolicitudOrdenCompra solicitudOrdenCompra;
    private Factura factura;
    private InventarioRegistro inventarioRegistro;
    private InventarioItems inventarioItems;
    private DetalleItem detalleItem;
    private List<InventarioItems> itemDevueltos; //lista que tiene todos los item del egreso devuelto
    private ArrayList<DetalleItem> listDetalle;
    private ArrayList<Oficio> listOficio;
//    private ArrayList<Oficio> listOficioGuiaR;
    private ArrayList<OrdenCompra> listOrdenCompra;
    private ArrayList<SolicitudOrdenCompra> listSolicitudOrdenCompras;
    private List<Factura> listFactura;
    private List<ConstatacionFisicaInventario> listCons;
    private List<DetalleConstFisicaInventario> listConsItem; //lista que trae todos los item de la constatacion
    private ConstatacionFisicaInventario constatacionFisicaInventario;
    private int numero;
    private int opcionAdquisicion = 0;

    //nuevo adquisiciones
    private List<Adquisiciones> listaAdaqui;
    private Adquisiciones adquisicionesOrden;
    private List<Adquisiciones> listAdq;

    private Boolean bandera = Boolean.TRUE;
    private Boolean habilitar = Boolean.TRUE;
    private Boolean mostrar = Boolean.FALSE;
    private Boolean mostrar2 = Boolean.TRUE;
    private Boolean mostrar3 = Boolean.TRUE;
    private Boolean mostrarAccObs = Boolean.TRUE;
    private Boolean mostrarAccObs1 = Boolean.TRUE;
    private Boolean rendOficio = Boolean.FALSE;
    private Boolean rendFactura = Boolean.TRUE;
    private Boolean rendCons = Boolean.FALSE;
    private Boolean rendOrden = Boolean.TRUE;
    private Boolean nuevo = Boolean.FALSE;
    private Boolean condContrato = Boolean.FALSE;
    private Boolean botonProcesos = Boolean.TRUE;
    private boolean botonImprimir = Boolean.TRUE;
    private Boolean bolImprimir = Boolean.TRUE;
    private Boolean guiaRemision = Boolean.FALSE;

    private String encabezado;

    private Boolean botonAprobaciones = Boolean.TRUE;
    private Boolean botonRechazado = Boolean.TRUE;
    private Boolean botonCompletartarea = Boolean.TRUE;
    private String observaciones;
    private Inventario inventarioOpcional;
    private Inventario inventarioEstado;
    private BigDecimal valorTotal = BigDecimal.ZERO, valorUnitarioTotal = BigDecimal.ZERO;
    private Integer cantidadTotal = 0;
    private Numero_Letras numeroLetra;

    @Inject
    private CatalogoMovimientoService catalogoMovimientoService;
    @Inject
    private GrupoNivelesService grupoService;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private ProcesoIngresoService procesoIngresoService;
    @Inject
    private InventarioItemsService inventarioItemsService;
    @Inject
    private InventarioRegistroService inventarioRegistroService;
    @Inject
    private OficioService oficioService;
    @Inject
    private DetalleConstFisicaService detalleConstFisicaService;
    @Inject
    private OrdenCompraService ordenCompraService;
    @Inject
    private FacturaService facturaService;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;
    //nuevo adquisiciones
    @Inject
    private BienesItemService bienesItemService;

    @Inject
    private ConstatacionFisicaInvService cfis;
    private Boolean consulta = false;

    @PostConstruct
    public void initView() {
        motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoEgresoInventario("INGINV", "INGINV");
//        motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("INGINV", "SIN-FLUJ");
        this.catalogoMovimiento = new CatalogoMovimiento();
        this.constatacionFisicaInventario = new ConstatacionFisicaInventario();
        this.proveedor = new Proveedor();
        this.proveedor.setCliente(new Cliente());
        this.inventario = new Inventario();
        this.inventarioEgreso = new Inventario();
        this.detalleItem = new DetalleItem();
        this.listDetalle = new ArrayList<>();
        this.listOficio = new ArrayList<>();
//        this.listOficioGuiaR = new ArrayList<>();
        adquisicionesOrden = new Adquisiciones();
        this.listaAdaqui = new ArrayList<>();
        this.listaAdaqui = bienesItemService.getTodasListaAdquisiciones();
        this.listAdq = new ArrayList<>();
        this.listCons = new ArrayList<>();
        this.listOrdenCompra = new ArrayList<>();
        this.listFactura = new ArrayList<>();
        this.listSolicitudOrdenCompras = new ArrayList<>();
        this.inventarioItems = new InventarioItems();
        this.oficio = new Oficio();
        this.guiaRem = new Oficio();
        this.ordenCompra = new OrdenCompra();
        this.factura = new Factura();
        this.inventarioRegistro = new InventarioRegistro();
//        inventario.setAnio(Utils.getAnio(new Date()).shortValue());
        rendOrden = Boolean.FALSE;
        rendFactura = Boolean.FALSE;

    }

    public void buscarProveedor(Boolean opc) {
        System.out.println("entroooo" + opc);
        try {
            if (opc) {
                if (factura.getProveedor().getCliente().getIdentificacion() != null) {
                    Proveedor p = proveedorService.findByIdentificacion(factura.getProveedor().getCliente().getIdentificacion());
                    if (p != null) {
                        factura.setProveedor(p);
                    } else {
                        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogProveedor", null);
                    }
                } else {
                    Utils.openDialog("/facelet/activos/inventario/Dialog/dialogProveedor", null);
                }
            } else {
                if (proveedor.getCliente().getIdentificacion() != null) {
                    Proveedor p = proveedorService.findByIdentificacion(proveedor.getCliente().getIdentificacion());
                    if (p != null) {
                        this.proveedor = p;
                    } else {
                        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogProveedor", null);
                    }
                } else {
                    Utils.openDialog("/facelet/activos/inventario/Dialog/dialogProveedor", null);
                }
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    //PARA ITEM
    public void abrirDialogo() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("INGRESO"));
//        params.put("PERIODO", Arrays.asList(inventario.getAnio().toString()));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogItemCta", params);
    }

    public void selectDatosItem(SelectEvent evt) {
        Boolean saber = false;
        detalleItem = (DetalleItem) evt.getObject();
        if (!listDetalle.isEmpty()) {
            for (int i = 0; i < listDetalle.size(); i++) {
                if (Objects.equals(detalleItem.getCodigoAgrupado(), listDetalle.get(i).getCodigoAgrupado())) {
                    saber = true;
                    break;
                }
            }
        }
        if (saber == true) {
            JsfUtil.addWarningMessage("Información", " Este Item ya está registrado.");
            return;
        }
        this.listDetalle.add(this.detalleItem);
    }

    public void borrarElementoLista(DetalleItem med) {
        listDetalle.remove(med);
    }

    // PARA OFICIO
    public void abrirDialogoOficio() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogOficio", null);
    }

    public void selectDatosOficio(SelectEvent evt) {
        oficio = (Oficio) evt.getObject();
        this.listOficio.add(this.oficio);

    }

    public void borrarElementoOficio(Oficio of) {
        listOficio.remove(of);
    }

    public void abrirDialogoOficioCambios(Oficio of) {
        if (of == null) {
            oficio = new Oficio();
//            proveedor = new Proveedor();
//            proveedor.setCliente(new Cliente());
            nuevo = true;
        } else {
            this.oficio = of;
//            proveedor = oficio.getProveedor();
            nuevo = false;
        }

        PrimeFaces.current().executeScript("PF('dlgOficio').show()");
    }

//    public void borrarElementoGuia(Oficio of) {
//        listOficioGuiaR.remove(of);
//    }
//    public void abrirDialogoGuiaCambios(Oficio of) {
//        if (of == null) {
//            guiaRem = new Oficio();
//            proveedor = new Proveedor();
//            proveedor.setCliente(new Cliente());
//            nuevo = true;
//        } else {
//            this.guiaRem = of;
//            proveedor = oficio.getProveedor();
//            nuevo = false;
//        }
//
//        PrimeFaces.current().executeScript("PF('dlgGuiaRemision').show()");
//    }
    //CONSTATACION FISICA
    public void abrirDialogCons() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("INGRESO"));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogConstatacion", params);
    }

    public void selectDatosCons(SelectEvent evt) {
        constatacionFisicaInventario = (ConstatacionFisicaInventario) evt.getObject();
        if (!listCons.contains(constatacionFisicaInventario)) {
            this.listCons.add(constatacionFisicaInventario);
            listConsItem = detalleConstFisicaService.getListDetalleItemCons(constatacionFisicaInventario);
            for (int w = 0; w < listConsItem.size(); w++) {
//                if (!listDetalle.contains(listConsItem.get(w))) {
                if (listConsItem.get(w).getDiferencia() > 0) {
                    detalleItem.setCodigo(listConsItem.get(w).getDetalleItem().getCodigo());
                    detalleItem.setCodigoAgrupado(listConsItem.get(w).getDetalleItem().getCodigoAgrupado());
                    detalleItem.setDescripcion(listConsItem.get(w).getDetalleItem().getDescripcion());
                    detalleItem.setAsignarGrupo(listConsItem.get(w).getDetalleItem().getAsignarGrupo());
                    detalleItem.setCuentaContable(listConsItem.get(w).getDetalleItem().getCuentaContable());
                    detalleItem.setEstante(listConsItem.get(w).getDetalleItem().getEstante());
                    detalleItem.setFila(listConsItem.get(w).getDetalleItem().getFila());
                    detalleItem.setColumna(listConsItem.get(w).getDetalleItem().getColumna());
                    detalleItem.setCajon(listConsItem.get(w).getDetalleItem().getCajon());
                    detalleItem.setCuadrante(listConsItem.get(w).getDetalleItem().getCuadrante());
                    detalleItem.setTipoMedida(listConsItem.get(w).getDetalleItem().getTipoMedida());
                    detalleItem.setCantidadTemp(listConsItem.get(w).getDiferencia());
                    detalleItem.setObservacionTemp(listConsItem.get(w).getObservacion());
                    detalleItem.setPrecioTemp(listConsItem.get(w).getDetalleItem().getPrecioCalculado());
                    detalleItem.setTotalTemp(BigDecimal.valueOf(listConsItem.get(w).getDiferencia()
                            * listConsItem.get(w).getDetalleItem().getPrecioCalculado().doubleValue()).setScale(4, RoundingMode.HALF_EVEN));
                    this.listDetalle.add(this.detalleItem);
                    detalleItem = new DetalleItem();
                }
//                }
            }
        }
        PrimeFaces.current().ajax().update("frmMain:outPanelCons");
        PrimeFaces.current().ajax().update("frmMain:dtItemIngreso");
    }

    public void buscarIngreso() {
        try {
            String codigo = inventario.getCodigo();
            if (Utils.isNum(inventario.getCodigo())) {
                String codigoGenerado = Utils.completarCadenaConCeros(inventario.getCodigo(), 5);
                codigo = "ING-" + codigoGenerado;
            }
            Inventario result = procesoIngresoService.findByCodigo(codigo);
            if (result != null) {
                inventario = result;
                listDetalle = new ArrayList<>();
                if (Utils.isNotEmpty(inventario.getInventarioItemsList())) {
                    for (InventarioItems invi : inventario.getInventarioItemsList()) {
                        DetalleItem di = invi.getDetalleItem();
                        di.setCantidadTemp(invi.getCantidad());
                        di.setPrecioTemp(invi.getPrecio());
                        listDetalle.add(di);
                    }
                }
                mostrarAccObs1 = false;
                bolImprimir = false;
                this.impresion = Utils.clone(inventario);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void borrarElementoCons(ConstatacionFisicaInventario cons) {
        listCons.remove(cons);
        List<DetalleConstFisicaInventario> detalleTem = cons.getDetalleConstFisicaInventarioList();
//        System.out.println("detalleTem " + detalleTem);
        for (DetalleItem d : listDetalle) {
            for (DetalleConstFisicaInventario dc : detalleTem) {
                if (d.equals(dc.getDetalleItem())) {
//                    System.out.println("entrooo");
                    listDetalle.remove(d);
                }
            }
        }
    }

    //PARA ORDEN DE COMPRA
    public void abrirDialogoOrden() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogOrdenCompra", null);
    }

    public void selectDatosOrden(SelectEvent evt) {
        ordenCompra = (OrdenCompra) evt.getObject();
        this.listOrdenCompra.add(this.ordenCompra);

    }

    public void borrarElementoOrden(OrdenCompra or) {
        listOrdenCompra.remove(or);
    }

    public void borrarElementoAdq(Adquisiciones adq) {
        listAdq.remove(adq);
    }

    public void abrirDialogoOrdenCambios(OrdenCompra orden) {
        if (orden == null) {
            ordenCompra = new OrdenCompra();
            proveedor = new Proveedor();
            proveedor.setCliente(new Cliente());
            nuevo = true;
        } else {
            this.ordenCompra = orden;
            nuevo = false;
        }

        PrimeFaces.current().executeScript("PF('dlgOrdenElectronico').show()");
    }

    //PARA FACTURA
    public void abrirDialogoFactura() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogFactura", null);
    }

    public void selectDatosFactura(SelectEvent evt) {
        factura = (Factura) evt.getObject();
        this.listFactura.add(this.factura);

    }

    public void borrarElementoFactura(int fac) {
        listFactura.remove(fac);
    }

    public void abrirDialogoFacturaCambios(Factura fac, int n) {
        if (fac == null) {
            factura = new Factura();
            if (proveedor != null) {
                factura.setProveedor(proveedor);
            } else {
                factura.setProveedor(new Proveedor(new Cliente()));
            }
            nuevo = true;
        } else {
            this.factura = fac;
            this.numero = n;
            nuevo = false;
        }

        PrimeFaces.current().executeScript("PF('dlgFactura').show()");
    }

    //PARA EGRESO
    public void abrirDialogoEgreso() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("EGRESO"));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogEgreso", params);
    }

    public void selectDatosEgreso(SelectEvent evt) {
        this.listDetalle = new ArrayList<>();
        inventarioEgreso = (Inventario) evt.getObject();
        itemDevueltos = inventarioItemsService.findItemByNombreEgreso(inventarioEgreso);
        Integer V = itemDevueltos.size();

        for (Integer w = 0; w < V; w++) {
            detalleItem.setCodigo(itemDevueltos.get(w).getDetalleItem().getCodigo());
            detalleItem.setCodigoAgrupado(itemDevueltos.get(w).getDetalleItem().getCodigoAgrupado());
            detalleItem.setDescripcion(itemDevueltos.get(w).getDetalleItem().getDescripcion());
            detalleItem.setAsignarGrupo(itemDevueltos.get(w).getDetalleItem().getAsignarGrupo());
            detalleItem.setCuentaContable(itemDevueltos.get(w).getDetalleItem().getCuentaContable());
            detalleItem.setEstante(itemDevueltos.get(w).getDetalleItem().getEstante());
            detalleItem.setFila(itemDevueltos.get(w).getDetalleItem().getFila());
            detalleItem.setColumna(itemDevueltos.get(w).getDetalleItem().getColumna());
            detalleItem.setCajon(itemDevueltos.get(w).getDetalleItem().getCajon());
            detalleItem.setCuadrante(itemDevueltos.get(w).getDetalleItem().getCuadrante());
            detalleItem.setTipoMedida(itemDevueltos.get(w).getDetalleItem().getTipoMedida());
            detalleItem.setCantidadTemp(itemDevueltos.get(w).getCantidad());
            detalleItem.setPrecioTemp(itemDevueltos.get(w).getDetalleItem().getPrecioCalculado());
            detalleItem.setTotalTemp(BigDecimal.valueOf(itemDevueltos.get(w).getCantidad()
                    * itemDevueltos.get(w).getDetalleItem().getPrecioCalculado().doubleValue()).setScale(4, RoundingMode.HALF_EVEN));
            this.listDetalle.add(this.detalleItem);
            detalleItem = new DetalleItem();
        }
        PrimeFaces.current().ajax().update("frmMain:outPanelCargarEgreso");
        PrimeFaces.current().ajax().update("frmMain:dtItemIngreso");
    }

    public void mostrarBtn() //<editor-fold defaultstate="collapsed" desc="Mostrar botones uso Boleanos">
    {
        try {
            rendOrden = Boolean.FALSE;
            rendFactura = Boolean.FALSE;
            if ((inventario.getMotivoMovimiento().getTexto().equals("DONACION"))) {
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.TRUE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrarAccObs1 = Boolean.TRUE;
                mostrar2 = Boolean.FALSE;
                mostrar3 = Boolean.FALSE;
                condContrato = Boolean.FALSE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR ANULACION"))) {
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.TRUE;
                mostrarAccObs = Boolean.FALSE;
                mostrarAccObs1 = Boolean.FALSE;
                mostrar2 = Boolean.TRUE;
                mostrar3 = Boolean.TRUE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE"))) {
                rendCons = Boolean.TRUE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrarAccObs1 = Boolean.FALSE;
                mostrar2 = Boolean.TRUE;
                mostrar3 = Boolean.TRUE;
            } else if (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION")) {
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.TRUE;
                mostrarAccObs = Boolean.FALSE;
                mostrarAccObs1 = Boolean.TRUE;
                mostrar2 = Boolean.TRUE;
                mostrar3 = Boolean.FALSE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("SALDO INICIAL"))) {
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrarAccObs1 = Boolean.TRUE;
                mostrar2 = Boolean.FALSE;
                mostrar3 = Boolean.FALSE;
            } else if (inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN")) {
                inventario.setSolicitudCompra(Boolean.FALSE);
                rendOrden = Boolean.TRUE;
                rendFactura = Boolean.TRUE;
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrar2 = Boolean.FALSE;
                mostrar3 = Boolean.FALSE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("OFICIO"))) {
                rendOrden = Boolean.FALSE;
                rendFactura = Boolean.TRUE;
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.TRUE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrarAccObs1 = Boolean.TRUE;
                mostrar2 = Boolean.FALSE;
                mostrar3 = Boolean.FALSE;
                condContrato = Boolean.TRUE;
            } else if (inventario.getMotivoMovimiento() == null) {
                mostrar2 = Boolean.TRUE;
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>

    public Boolean saldoMotivoAjuste(DetalleItem d) {
        if (inventario.getMotivoMovimiento().getTexto().equals("AJUSTE")) {
            if (d.getPrecioTemp().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return mostrar3;
        }
    }

    public GrupoNiveles obtenerNomArea(GrupoNiveles g) {
        if (g == null) {
            return new GrupoNiveles();
        }
        return grupoService.findByPadreGrupo(g);
    }

    public void nuevoValor() {
        Integer t = listDetalle.size();
        for (Integer i = 0; i < t; i++) {
            BigDecimal pre = listDetalle.get(i).getPrecioTemp();
            BigDecimal can = BigDecimal.valueOf(listDetalle.get(i).getCantidadTemp());
            BigDecimal result = pre.multiply(can);
            listDetalle.get(i).setTotalTemp(result);
        }
    }

    //PARA SOLICITUD ORDEN DE COMPRA
    public void abrirDlgOrdenCompra() {
        PrimeFaces.current().executeScript("PF('dlgOrdenCompra').show()");
        PrimeFaces.current().ajax().update(":frmOrdenCompra");
    }

    public void borrarElementoSolicCompra(SolicitudOrdenCompra solc) {
        listSolicitudOrdenCompras.remove(solc);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        try {
            if (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION")) {
                Integer b = 0;
                for (int i = 0; i < listDetalle.size(); i++) {
                    if ((itemDevueltos.get(i).getCantidad()) < listDetalle.get(i).getCantidadTemp()) {
                        listDetalle.get(i).setCantidadTemp(itemDevueltos.get(i).getCantidad());
                        b = b + 1;
                    }
                }

                if (b > 0) {
                    if (newValue != null && !newValue.equals(oldValue)) {
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor No Modificado", "Debe ser menor  o igual");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        b = 0;
                    }
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor Modificado", " Cantidad: " + newValue);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    nuevoValor();
                }
            } else {
                if (newValue != null && !newValue.equals(oldValue)) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor Modificado", "Cambio realizado");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    nuevoValor();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectDatos(SelectEvent evt) {
        proveedor = ((Proveedor) evt.getObject());
        proveedor.getCliente().setIdentificacion(proveedor.getCliente().getIdentificacion() + "001");
    }

    public void selectDatosFacturaProveedor(SelectEvent evt) {
        proveedor = ((Proveedor) evt.getObject());
        factura.setProveedor(proveedor);

//        proveedor.getCliente().setIdentificacion(proveedor.getCliente().getIdentificacion() + "001");
    }

//<editor-fold defaultstate="collapsed" desc="METODO LIMPIAR">
    public void limpiarTodo() {
        this.impresion = new Inventario();
        if (this.inventario != null) {
            this.impresion = Utils.clone(this.inventario);
        }
        inventario = new Inventario();
        inventarioEgreso = new Inventario();
        constatacionFisicaInventario = new ConstatacionFisicaInventario();
        listDetalle = new ArrayList<>();
        listCons = new ArrayList<>();
        proveedor = new Proveedor();
        proveedor.setCliente(new Cliente());
        oficio = new Oficio();
        guiaRem = new Oficio();
        ordenCompra = new OrdenCompra();
        factura = new Factura();
        listAdq = new ArrayList<>();
        listSolicitudOrdenCompras = new ArrayList<>();
        listOficio = new ArrayList<>();
//        listOficioGuiaR = new ArrayList<>();
        listOrdenCompra = new ArrayList<>();
        listFactura = new ArrayList<>();
        inventarioRegistro = new InventarioRegistro();
        bandera = Boolean.TRUE;
        habilitar = Boolean.TRUE;
        mostrar = Boolean.FALSE;
        rendOficio = Boolean.FALSE;
        rendCons = Boolean.FALSE;
        rendFactura = Boolean.TRUE;
        rendOrden = Boolean.TRUE;
        mostrarAccObs = Boolean.TRUE;
        mostrarAccObs1 = Boolean.TRUE;
        mostrar2 = Boolean.TRUE;
        mostrar3 = Boolean.TRUE;
        botonImprimir = Boolean.TRUE;
        botonProcesos = Boolean.TRUE;
        bolImprimir = Boolean.TRUE;
        guiaRemision = Boolean.FALSE;
        valorTotal = BigDecimal.ZERO;
        valorUnitarioTotal = BigDecimal.ZERO;
        cantidadTotal = 0;
    }
//</editor-fold>

    public void nuevoIngreso() {
        Subject subject = SecurityUtils.getSubject();
        if (bandera) {
            JsfUtil.addInformationMessage("INFORMACIÓN", "ESCOJA UN MOTIVO.");
            mostrar2 = Boolean.TRUE;
            habilitar = Boolean.FALSE;
            bolImprimir = Boolean.TRUE;
            this.inventario = new Inventario();
            inventario.setSolicitudCompra(Boolean.FALSE);
            //Long orden = procesoIngresoService.getOrderInventario("INGRESO");
//            inventario.setAnio(Utils.getAnio(new Date()).shortValue());
            //inventario.setOrden(orden);

            inventario.setEstado(Boolean.TRUE);
            inventario.setUsuarioOriginador(subject.getPrincipal().toString());
            inventario.setTipoMovimiento("INGRESO");
            inventario.setFechaMovimiento(new Date());
            bandera = Boolean.FALSE;
            guiaRemision = Boolean.FALSE;
//            listOficioGuiaR = new ArrayList<>();
            this.listDetalle = new ArrayList<>();
        } else {
            habilitar = Boolean.FALSE;
            JsfUtil.addWarningMessage("INFORMACIÓN", "TIENE UN INGRESO GENERADO, por favor CANCELAR para generar un nuevo ingreso.");
        }

    }

    public boolean validaciones() {
        if (inventario.getMotivoMovimiento() == null) {
            JsfUtil.addWarningMessage("ERROR", "DEBE SELECCIONAR UN MOTIVO.");
            return true;
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("OFICIO")) || (inventario.getMotivoMovimiento().getTexto().equals("DONACION"))) {
            if (listOficio.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Por favor escoja un oficio para este motivo");
                return true;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR ANULACION")) || (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION"))) {
            if (inventarioEgreso.getCodigo() == null) {
                JsfUtil.addWarningMessage("Error", "Por favor cargue un egreso para continuar");
                return true;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN"))) {
            if (opcionAdquisicion == 0 || opcionAdquisicion == 2) {
                if ((listAdq.isEmpty())) {
                    JsfUtil.addWarningMessage("Error", "Por favor adicione datos de adquisición para continuar");
                    return true;
                }
            } else {
                if (opcionAdquisicion == 1) {
                    if ((listSolicitudOrdenCompras.isEmpty())) {
                        JsfUtil.addWarningMessage("Error", "Por favor adicione datos de la solicitud de orden de compra para continuar");
                        return true;
                    }
                } else if (opcionAdquisicion == 3) {
                    if (listOficio.isEmpty()) {
                        JsfUtil.addWarningMessage("Error", "Por favor escoja un oficio para este motivo");
                        return true;
                    }
                }
            }
            if (!guiaRemision) {
                if ((listFactura.isEmpty())) {
                    JsfUtil.addWarningMessage("Error", "Por favor adicione facturas para continuar");
                    return true;
                }
            }
            if ((proveedor.getCliente().getIdentificacion() == null)) {
                JsfUtil.addWarningMessage("Error", "Seleccione el proveedor");
                return true;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE")) || (inventario.getMotivoMovimiento().getTexto().equals("SALDO INICIAL"))) {
            if (listDetalle.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Escoja los item para continuar");
                return true;
            }
        }
        if (listDetalle.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "Adicione Items para poder guardar");
            return true;
        }
        return false;
    }

    public void guardar() {
        if (validaciones()) {
            return;
        }
        Boolean aBol = Boolean.FALSE;
        for (int i = 0; i < listDetalle.size(); i++) {
            if ((listDetalle.get(i).getCantidadTemp() <= 0) || (listDetalle.get(i).getPrecioTemp().compareTo(BigDecimal.ZERO) < 0)) {
                aBol = Boolean.TRUE;
            }
        }
        if (aBol) {
            JsfUtil.addWarningMessage("Error", "Valores en cero, favor ingresar cantidad o precio diferente a 0");
            return;
        }
        if (inventario.getSolicitudCompra()) {
            if (Utils.isEmpty(listSolicitudOrdenCompras)) {
                JsfUtil.addWarningMessage("Error", "Ingrese una solicitud de Compra");
            }
        }
        inventario.setEstadoAdicional("COMPLETO");
        try {
            for (Integer w = 0; w < listDetalle.size(); w++) {
                if ((listDetalle.get(w).getCantidadTemp() <= 0) || (listDetalle.get(w).getPrecioTemp().doubleValue() == 0)) {
                    JsfUtil.addWarningMessage("Información", "Valores en cero, favor ingresar cantidad o precio diferente a 0");
                    return;
                }
            }
            if ((inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN"))) {
                inventario.setProveedor(proveedor);
            }
            if ((inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR ANULACION")) || (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION"))) {
                inventario.setIngresoEgresoRelacionado(inventarioEgreso);
            }
            if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE"))) {
                inventario.setConstatacionFisica(constatacionFisicaInventario);
                if (constatacionFisicaInventario.getRazon().equals("AMBOS")) {
                    constatacionFisicaInventario.setRazon("EGRESO");
                    constatacionFisicaInventario.setAjustado(Boolean.FALSE);
                } else {
                    constatacionFisicaInventario.setAjustado(Boolean.TRUE);
                }
                cfis.edit(constatacionFisicaInventario);
            }
            this.inventarioOpcional = new Inventario();
            this.inventarioEstado = new Inventario();
            if (inventario.getSolicitudCompra()) {
                if (Utils.isNotEmpty(listSolicitudOrdenCompras)) {
                    inventario.setSolicitudOrdenCompra(listSolicitudOrdenCompras.get(0));
                }
            }
            Long orden = procesoIngresoService.getOrderInventario("INGRESO");
//            inventario.setAnio(Utils.getAnio(new Date()).shortValue());
            inventario.setOrden(orden);
            String codigoGenerado = Utils.completarCadenaConCeros(inventario.getOrden() + "", 5);
            inventario.setCodigo("ING-" + codigoGenerado);
            inventario.setProveedor(proveedor != null ? proveedor : null);
            if (inventario.getId() == null) {
                inventario = procesoIngresoService.create(inventario);
            }
            if ((inventario.getMotivoMovimiento().getTexto().equals("OFICIO")) || (inventario.getMotivoMovimiento().getTexto().equals("DONACION")) || (inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN"))) {
                inventarioRegistro.setMovimiento(inventario);
                inventarioRegistro.setEstado(Boolean.TRUE);
                if (inventario.getId() != null) {
                    inventarioRegistroService.create(inventarioRegistro);
                }
            }

            //RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LOS DETALLE ITEM EN BASE DE DATOS
            for (Integer i = 0; i < listDetalle.size(); i++) {//actualizar precio cantidad en ITEM.
                inventarioItems = new InventarioItems();
//                String codigo = listDetalle.get(i).getCodigoAgrupado();
//                DetalleItem item = detalleItemService.findByCodigoList(codigo);
                DetalleItem item = detalleItemService.find(listDetalle.get(i).getId());
//                System.out.println("Item: " + item);
                if (item == null) {
                    item = new DetalleItem();
                }
                Integer canExistente = item.getCantidadExistente();
                Integer canIngresada = listDetalle.get(i).getCantidadTemp();
                BigDecimal prec = BigDecimal.ZERO;
                BigDecimal totalExistente = BigDecimal.ZERO;
                if (item.getTotalCalculado() != null) {
                    totalExistente = item.getTotalCalculado();
                }
                BigDecimal total = BigDecimal.ZERO;
                BigDecimal pre = listDetalle.get(i).getPrecioTemp();
                item.setCantidadExistente(canExistente + canIngresada);
                Integer can = canExistente + canIngresada;
                if (inventario.getIngresoEgresoRelacionado() == null) {
                    total = listDetalle.get(i).getTotalTemp();
                    totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                    item.setTotalCalculado(totalExistente);
                    if ((totalExistente.intValue() == 0) && (can == 0)) {
                        item.setPrecioCalculado(BigDecimal.ZERO);
                    } else {
                        item.setPrecioCalculado(BigDecimal.valueOf(totalExistente.doubleValue() / can).setScale(4, RoundingMode.HALF_EVEN));
                    }
                } else {
                    prec = item.getPrecioCalculado().setScale(4, RoundingMode.HALF_EVEN);
                    total = BigDecimal.valueOf(canIngresada * prec.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);
                    totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                    item.setTotalCalculado(totalExistente);
                    if ((totalExistente.intValue() == 0) && (can == 0)) {
                        item.setPrecioCalculado(BigDecimal.ZERO);
                    } else {
                        item.setPrecioCalculado(BigDecimal.valueOf(totalExistente.doubleValue() / can).setScale(4, RoundingMode.HALF_EVEN));
                    }
                }
                detalleItemService.edit(item);
                inventarioItems.setInventario(inventario);//guardar item transaccionados en inventario_item
                inventarioItems.setDetalleItem(item);
                inventarioItems.setEstante(listDetalle.get(i).getEstante());
                inventarioItems.setFila(listDetalle.get(i).getFila());
                inventarioItems.setColumna(listDetalle.get(i).getColumna());
                inventarioItems.setCajon(listDetalle.get(i).getCajon());
                inventarioItems.setCuadrante(listDetalle.get(i).getCuadrante());
                inventarioItems.setObservacion(listDetalle.get(i).getObservacionTemp());
                inventarioItems.setCantidad(canIngresada);
                if (inventario.getIngresoEgresoRelacionado() == null) {
                    inventarioItems.setPrecio(pre);
                } else {
                    inventarioItems.setPrecio(prec);
                }
                inventarioItems.setTotal(total);
                inventarioItems.setIva(listDetalle.get(i).getIvaTemp());
                inventarioItems.setFechaAdq(new Date());
                System.out.println("inventario items" + inventarioItems.getInventario().getId());
                inventarioItemsService.create(inventarioItems);
            } // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LOS OFICIOS EN BASE DE DATOS
            for (Integer i = 0; i < listOficio.size(); i++) {
                listOficio.get(i).setEstado(true);
                if (condContrato) {
                    listOficio.get(i).setTipoDocumento("OFICIO");
                } else {
                    listOficio.get(i).setTipoDocumento("DONACIÓN");
                }
                if (opcionAdquisicion == 3) {
                    listOficio.get(i).setTipoDocumento("ADQUISICIÓN");
                }
                oficioService.create(listOficio.get(i));   // AQUI VERIFIFCAR  OJO
                inventarioRegistro.setOficio(listOficio.get(i));
                inventarioRegistro.setMovimiento(inventario);
                inventarioRegistroService.edit(inventarioRegistro);
            }
            // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LOS OFICIOS EN BASE DE DATOS
//            for (Integer i = 0; i < listOficioGuiaR.size(); i++) {
//                listOficioGuiaR.get(i).setEstado(true);
//                listOficioGuiaR.get(i).setTipoDocumento("GUIA DE REMISIÓN");
//                InventarioRegistro invReg = new InventarioRegistro();
//                oficioService.create(listOficioGuiaR.get(i));   // AQUI VERIFIFCAR  OJO
//                invReg.setOficio(listOficioGuiaR.get(i));
//                invReg.setMovimiento(inventario);
//                invReg.setEstado(Boolean.TRUE);
//                inventarioRegistroService.create(invReg);
//            }
            // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE las adquisiciones  EN BASE DE DATOS
            for (Integer i = 0; i < listAdq.size(); i++) {
                inventarioRegistro.setAdquisiciones(listAdq.get(i));
                inventarioRegistro.setMovimiento(inventario);
                inventarioRegistroService.edit(inventarioRegistro);
            }
            // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LAS FACTURAS EN BASE DE DATOS
            for (Integer i = 0; i < listFactura.size(); i++) {
                listFactura.get(i).setEstado(true);
//                    listFactura.get(i).setProveedor(proveedor);
                listFactura.get(i).setInventarioRegistro(inventarioRegistro);
                facturaService.create(listFactura.get(i));    // ver si necesito new listFactura
            }
            //  guardado todo, procedemos a inicializar todo okidoki
//                    limpiarTodo();
            this.impresion = Utils.clone(this.inventario);
            bolImprimir = Boolean.FALSE;
            habilitar = Boolean.TRUE;
            mostrar2 = Boolean.TRUE;
            mostrar3 = Boolean.TRUE;
            mostrarAccObs1 = Boolean.FALSE;
            JsfUtil.addSuccessMessage("Información", "Ingreso N° " + inventario.getCodigo() + " se ha registrado con Éxito");
            this.botonCompletartarea = Boolean.FALSE;
            PrimeFaces.current().ajax().update("frmMain");

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void getTotalListItems() {
        valorTotal = BigDecimal.ZERO;
        valorUnitarioTotal = BigDecimal.ZERO;
        cantidadTotal = 0;
        if (Utils.isNotEmpty(listDetalle)) {
            cantidadTotal = listDetalle.stream().mapToInt(x -> x.getCantidadTemp() != null ? x.getCantidadTemp() : 0).sum();
            valorTotal = listDetalle.stream().filter(x -> x.getTotalTemp() != null).map(x -> x.getTotalTemp()).reduce(BigDecimal.ZERO, (b1, b2) -> b1.add(b2)).setScale(4, RoundingMode.HALF_EVEN);
            valorUnitarioTotal = listDetalle.stream().filter(x -> x.getPrecioTemp() != null).map(x -> x.getPrecioTemp()).reduce(BigDecimal.ZERO, (b1, b2) -> b1.add(b2)).setScale(4, RoundingMode.HALF_EVEN);
        }
//        System.out.println("cantidadTotal: "+ cantidadTotal +" valorTotal:"+valorTotal+" valorUnitarioTotal:"+valorUnitarioTotal);
    }

    public void continuarEstadoProcesos() {
        this.inventarioOpcional.setEstadoAdicional("COMPLETO");
        procesoIngresoService.edit(inventarioOpcional);
        botonCompletartarea = Boolean.TRUE;
        botonProcesos = Boolean.FALSE;
    }

    public void limpiarVariableOrdenCatalogoElec() {
        nuevo = Boolean.FALSE;
        ordenCompra = new OrdenCompra();
    }

    public void guardarOrdenCatalogoElec() {
        ordenCompra.setProveedor(proveedor);
        this.listOrdenCompra.add(this.ordenCompra);
        PrimeFaces.current().executeScript("PF('dlgOrdenElectronico').hide();");
        PrimeFaces.current().ajax().update("frmMain:outPanelMedidaCuatro");
    }

    public void editarOrdenCatalogoElec() {
        ordenCompra.setProveedor(proveedor);
        listOrdenCompra = new ArrayList<>();
        this.listOrdenCompra.add(this.ordenCompra);
        PrimeFaces.current().executeScript("PF('dlgOrdenElectronico').hide();");
        PrimeFaces.current().ajax().update("frmMain:outPanelMedidaCuatro");
    }

    public void limpiarVariableOficio() {
        nuevo = Boolean.FALSE;
        oficio = new Oficio();
    }

    public void guardarOficio() {
        this.listOficio.add(this.oficio);
        if (opcionAdquisicion == 3) {
            listOficio.get(0).setProveedor(proveedor);
//            System.out.println("Proveedor: " + proveedor);
        }
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
    }

    public void editarOficio() {
        listOficio = new ArrayList<>();
        this.listOficio.add(this.oficio);
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
    }

    public void limpiarVariableGuia() {
        nuevo = Boolean.FALSE;
        guiaRem = new Oficio();
    }

//    public void guardarGuia() {
//        if (listOficioGuiaR.size() < 1) {
//            this.listOficioGuiaR.add(this.guiaRem);
//            listOficioGuiaR.get(0).setProveedor(proveedor);
////            System.out.println("Proveedor: " + proveedor);
//        } else {
//            JsfUtil.addInformationMessage("Información", "No se pueden agregar más registros de Guía de Remisión");
//
//        }
//        PrimeFaces.current().executeScript("PF('dlgGuiaRemision').hide();");
//
//    }
//    public void editarGuia() {
//        listOficioGuiaR = new ArrayList<>();
//        this.listOficioGuiaR.add(this.guiaRem);
//        PrimeFaces.current().executeScript("PF('dlgGuiaRemision').hide();");
//    }
    public void limpiarVariableFactura() {
        nuevo = Boolean.FALSE;
        factura = new Factura();
    }

    public void guardarFactura() {
        if (Utils.isEmptyString(factura.getNumFactura())) {
            JsfUtil.addWarningMessage("Advertencia", "No se pueden agregar registro de Factura sin número de Facttura");
            return;
        }
        if (factura.getFechaFactura() == null) {
            JsfUtil.addWarningMessage("Advertencia", "No se pueden agregar registro de Factura sin fecha de Factura");
            return;
        }

        if (factura.getProveedor() == null || factura.getProveedor().getId() == null) {
            JsfUtil.addWarningMessage("Advertencia", "No se pueden agregar registro de Factura sin Proveedor");
            return;
        }
        System.out.println("factura " + factura.getProveedor());
        factura.setGuiaRemision(guiaRemision);
        this.listFactura.add(this.factura);
        PrimeFaces.current().executeScript("PF('dlgFactura').hide();");
    }

    public void editarFactura() {
        if (Utils.isEmptyString(factura.getNumFactura())) {
            JsfUtil.addWarningMessage("Advertencia", "No se pueden agregar registro de Factura sin número de Facttura");
            return;
        }
        if (factura.getFechaFactura() == null) {
            JsfUtil.addWarningMessage("Advertencia", "No se pueden agregar registro de Factura sin fecha de Factura");
            return;
        }
        System.out.println("factura " + factura.getProveedor());
        if (factura.getProveedor() == null || factura.getProveedor().getId() == null) {
            JsfUtil.addWarningMessage("Advertencia", "No se pueden agregar registro de Factura sin Proveedor");
            return;
        }
        this.listFactura.remove(numero);
        this.listFactura.add(numero, factura);
        PrimeFaces.current().executeScript("PF('dlgFactura').hide();");
    }

    //nuevo adquisicion
    public void mostrarTodasAdquisiciones() {
        this.listaAdaqui = new ArrayList<>();
        this.listaAdaqui = bienesItemService.getTodasListaAdquisiciones();
    }

    public void closeSelectAdquisicion(Adquisiciones adq) {
        adquisicionesOrden = adq;
        proveedor = adq.getProveedor();
        listAdq.add(adq);
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').hide()");
        PrimeFaces.current().ajax().update("frmMain:dtOrden");
    }

    public void openDlgAdquisicion() {
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgAdquisicion");
    }

    public void datosByProceso(Inventario inv) {
        this.inventario = inv;
        this.inventarioOpcional = inv;
        this.inventarioEstado = inv;
//        motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("INGINV", "SIN-FLUJ");
        motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoEgresoInventario("INGINV", "INGINV");
        this.detalleItem = new DetalleItem();
        this.listDetalle = new ArrayList<>();
        this.listOficio = new ArrayList<>();
//        this.listOficioGuiaR = new ArrayList<>();
        this.listAdq = new ArrayList<>();
        this.listCons = new ArrayList<>();
        this.proveedor = new Proveedor();
        this.proveedor.setCliente(new Cliente());
        this.listFactura = new ArrayList<>();
        this.proveedor = inv.getProveedor();
        this.inventarioEgreso = new Inventario();
        this.inventarioEgreso = inv.getIngresoEgresoRelacionado();
        List<InventarioItems> itemsDevueltos = inventarioItemsService.getItemByInventarioItems(inv);
        List<InventarioRegistro> invRegLista = inventarioRegistroService.findRegistroInvByNumInv(inv);
        List<Factura> listFact = facturaService.findFacturasByRegistro(invRegLista.get(0));
        listFactura = listFact;
        Oficio ofic = invRegLista.get(0).getOficio();
        Adquisiciones adqui = invRegLista.get(0).getAdquisiciones();
        this.listCons.add(inv.getConstatacionFisica());
        this.listAdq.add(adqui);
        this.listOficio.add(ofic);
        Integer V = itemsDevueltos.size();
        for (Integer w = 0; w < V; w++) {
            detalleItem.setCodigo(itemsDevueltos.get(w).getDetalleItem().getCodigo());
            detalleItem.setDescripcion(itemsDevueltos.get(w).getDetalleItem().getDescripcion());
            detalleItem.setAsignarGrupo(itemsDevueltos.get(w).getDetalleItem().getAsignarGrupo());
            detalleItem.setCuentaContable(itemsDevueltos.get(w).getDetalleItem().getCuentaContable());
            detalleItem.setEstante(itemsDevueltos.get(w).getDetalleItem().getEstante());
            detalleItem.setFila(itemsDevueltos.get(w).getDetalleItem().getFila());
            detalleItem.setColumna(itemsDevueltos.get(w).getDetalleItem().getColumna());
            detalleItem.setCajon(itemsDevueltos.get(w).getDetalleItem().getCajon());
            detalleItem.setCuadrante(itemsDevueltos.get(w).getDetalleItem().getCuadrante());
            detalleItem.setTipoMedida(itemsDevueltos.get(w).getDetalleItem().getTipoMedida());
            detalleItem.setCantidadTemp(itemsDevueltos.get(w).getCantidad());
            detalleItem.setPrecioTemp(itemsDevueltos.get(w).getPrecio());
            detalleItem.setTotalTemp(itemsDevueltos.get(w).getTotal());
            this.listDetalle.add(this.detalleItem);
            detalleItem = new DetalleItem();
        }
        mostrarBtn();
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void calcularIvaDetalle(DetalleItem item) {
//        System.out.println("getCantidadTemp "+ item.getCantidadTemp());
//        System.out.println("getPrecioTemp "+ item.getPrecioTemp());
//        System.out.println("getIvaTemp "+ item.getIvaTemp());
        //cambiar cantidad
        try {
            if (item.getCantidadTemp() != null && item.getCantidadTemp() > 0) {
                if (item.getPrecioTemp() != null && item.getPrecioTemp().doubleValue() > 0) {
                    if (item.getIvaTemp() != null && item.getIvaTemp().doubleValue() > 0) {
                        double neto = (item.getCantidadTemp() * item.getPrecioTemp().doubleValue());
                        double iva = neto * (item.getIvaTemp().doubleValue() / 100);
                        item.setTotalTemp(new BigDecimal(neto + iva));
                    } else {
                        item.setIvaTemp(BigDecimal.ZERO);
                        item.setTotalTemp(new BigDecimal(item.getCantidadTemp() * item.getPrecioTemp().doubleValue()));
                    }
                } else {
                    item.setTotalTemp(BigDecimal.ZERO);
                    item.setPrecioTemp(BigDecimal.ZERO);
                }
            } else {
                item.setCantidadTemp(0);
                item.setPrecioTemp(BigDecimal.ZERO);
                item.setTotalTemp(BigDecimal.ZERO);
            }
            getTotalListItems();
        } catch (Exception e) {
            item.setPrecioTemp(BigDecimal.ZERO);
            item.setTotalTemp(BigDecimal.ZERO);
            item.setCantidadTemp(0);
            e.printStackTrace();
        }
    }

    public void imprimirIngreso() {
        ss.addParametro("ing_Id", impresion.getId());
        ss.addParametro("guardalmacen", "");
//        ss.addParametro("guardalmacen", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
        ss.setNombreReporte("ingresoInventario");
        ss.setNombreSubCarpeta("activos");
        java.math.BigDecimal d = inventarioItemsService.getValorAcumuladoIngreso(impresion);
        String num = numeroLetra.convertir(d.setScale(2, java.math.RoundingMode.HALF_UP) + "", true);
        ss.addParametro("acumulado_lt", num);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        limpiarTodo();
    }

    public void seleccionarTipoAdquisicion() {
        inventario.setSolicitudCompra(Boolean.FALSE);
        proveedor = new Proveedor();
        proveedor.setCliente(new Cliente());
        if (opcionAdquisicion != 0) {
            switch (opcionAdquisicion) {
                case 1://Orden de Compra
                    inventario.setSolicitudCompra(Boolean.TRUE);
                    break;
                case 2://Contrato de Adquisición
                    break;
                case 3://Num. Referencia
                    break;
                default:
                    break;
            }
        }

    }

//<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Adquisiciones> getListaAdaqui() {
        return listaAdaqui;
    }

    public void setListaAdaqui(List<Adquisiciones> listaAdaqui) {
        this.listaAdaqui = listaAdaqui;
    }

    public CatalogoMovimiento getCatalogoMovimiento() {
        return catalogoMovimiento;
    }

    public void setCatalogoMovimiento(CatalogoMovimiento catalogoMovimiento) {
        this.catalogoMovimiento = catalogoMovimiento;
    }

    public List<CatalogoMovimiento> getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(List<CatalogoMovimiento> motivoMovimiento) {
        this.motivoMovimiento = motivoMovimiento;
    }

    public DetalleItem getDetalleItem() {
        return detalleItem;
    }

    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }

    public ArrayList<DetalleItem> getListDetalle() {
        return listDetalle;
    }

    public void setListDetalle(ArrayList<DetalleItem> listDetalle) {
        this.listDetalle = listDetalle;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public InventarioItems getInventarioItems() {
        return inventarioItems;
    }

    public void setInventarioItems(InventarioItems inventarioItems) {
        this.inventarioItems = inventarioItems;
    }

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }

    public Oficio getOficio() {
        return oficio;
    }

    public void setOficio(Oficio oficio) {
        this.oficio = oficio;
    }

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public InventarioRegistro getInventarioRegistro() {
        return inventarioRegistro;
    }

    public void setInventarioRegistro(InventarioRegistro inventarioRegistro) {
        this.inventarioRegistro = inventarioRegistro;
    }

    public ArrayList<Oficio> getListOficio() {
        return listOficio;
    }

    public void setListOficio(ArrayList<Oficio> listOficio) {
        this.listOficio = listOficio;
    }

    public ArrayList<OrdenCompra> getListOrdenCompra() {
        return listOrdenCompra;
    }

    public void setListOrdenCompra(ArrayList<OrdenCompra> listOrdenCompra) {
        this.listOrdenCompra = listOrdenCompra;
    }

    public List<Factura> getListFactura() {
        return listFactura;
    }

    public void setListFactura(List<Factura> listFactura) {
        this.listFactura = listFactura;
    }

    public Boolean getMostrar() {
        return mostrar;
    }

    public void setMostrar(Boolean mostrar) {
        this.mostrar = mostrar;
    }

    public Boolean getRendOficio() {
        return rendOficio;
    }

    public void setRendOficio(Boolean rendOficio) {
        this.rendOficio = rendOficio;
    }

    public Boolean getRendFactura() {
        return rendFactura;
    }

    public void setRendFactura(Boolean rendFactura) {
        this.rendFactura = rendFactura;
    }

    public Boolean getRendOrden() {
        return rendOrden;
    }

    public void setRendOrden(Boolean rendOrden) {
        this.rendOrden = rendOrden;
    }

    public Inventario getInventarioEgreso() {
        return inventarioEgreso;
    }

    public void setInventarioEgreso(Inventario inventarioEgreso) {
        this.inventarioEgreso = inventarioEgreso;
    }

    public List<InventarioItems> getItemDevueltos() {
        return itemDevueltos;
    }

    public void setItemDevueltos(List<InventarioItems> itemDevueltos) {
        this.itemDevueltos = itemDevueltos;
    }

    public Boolean getMostrarAccObs() {
        return mostrarAccObs;
    }

    public void setMostrarAccObs(Boolean mostrarAccObs) {
        this.mostrarAccObs = mostrarAccObs;
    }

    public Boolean getMostrarAccObs1() {
        return mostrarAccObs1;
    }

    public void setMostrarAccObs1(Boolean mostrarAccObs1) {
        this.mostrarAccObs1 = mostrarAccObs1;
    }

    public Boolean getMostrar2() {
        return mostrar2;
    }

    public void setMostrar2(Boolean mostrar2) {
        this.mostrar2 = mostrar2;
    }

    public Boolean getMostrar3() {
        return mostrar3;
    }

    public Boolean getBolImprimir() {
        return bolImprimir;
    }

    public void setBolImprimir(Boolean bolImprimir) {
        this.bolImprimir = bolImprimir;
    }

    public void setMostrar3(Boolean mostrar3) {
        this.mostrar3 = mostrar3;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public List<DetalleConstFisicaInventario> getListConsItem() {
        return listConsItem;
    }

    public void setListConsItem(List<DetalleConstFisicaInventario> listConsItem) {
        this.listConsItem = listConsItem;
    }

    public Boolean getRendCons() {
        return rendCons;
    }

    public void setRendCons(Boolean rendCons) {
        this.rendCons = rendCons;
    }

    public ConstatacionFisicaInventario getConstatacionFisicaInventario() {
        return constatacionFisicaInventario;
    }

    public void setConstatacionFisicaInventario(ConstatacionFisicaInventario constatacionFisicaInventario) {
        this.constatacionFisicaInventario = constatacionFisicaInventario;
    }

    public List<ConstatacionFisicaInventario> getListCons() {
        return listCons;
    }

    public void setListCons(List<ConstatacionFisicaInventario> listCons) {
        this.listCons = listCons;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public List<Adquisiciones> getListAdq() {
        return listAdq;
    }

    public void setListAdq(List<Adquisiciones> listAdq) {
        this.listAdq = listAdq;
    }

    public Boolean getCondContrato() {
        return condContrato;
    }

    public void setCondContrato(Boolean condContrato) {
        this.condContrato = condContrato;
    }

    public Adquisiciones getAdquisicionesOrden() {
        return adquisicionesOrden;
    }

    public void setAdquisicionesOrden(Adquisiciones adquisicionesOrden) {
        this.adquisicionesOrden = adquisicionesOrden;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Boolean getBotonProcesos() {
        return botonProcesos;
    }

    public void setBotonProcesos(Boolean botonProcesos) {
        this.botonProcesos = botonProcesos;
    }

    public boolean isBotonImprimir() {
        return botonImprimir;
    }

    public Inventario getImpresion() {
        return impresion;
    }

    public void setImpresion(Inventario impresion) {
        this.impresion = impresion;
    }

    public void setBotonImprimir(boolean botonImprimir) {
        this.botonImprimir = botonImprimir;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
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

    public Inventario getInventarioOpcional() {
        return inventarioOpcional;
    }

    public void setInventarioOpcional(Inventario inventarioOpcional) {
        this.inventarioOpcional = inventarioOpcional;
    }

    public SolicitudOrdenCompra getSolicitudOrdenCompra() {
        return solicitudOrdenCompra;
    }

    public void setSolicitudOrdenCompra(SolicitudOrdenCompra solicitudOrdenCompra) {
        this.solicitudOrdenCompra = solicitudOrdenCompra;
    }

    public ArrayList<SolicitudOrdenCompra> getListSolicitudOrdenCompras() {
        return listSolicitudOrdenCompras;
    }

    public void setListSolicitudOrdenCompras(ArrayList<SolicitudOrdenCompra> listSolicitudOrdenCompras) {
        this.listSolicitudOrdenCompras = listSolicitudOrdenCompras;
    }
//</editor-fold>

    public int getOpcionAdquisicion() {
        return opcionAdquisicion;
    }

    public void setOpcionAdquisicion(int opcionAdquisicion) {
        this.opcionAdquisicion = opcionAdquisicion;
    }

//    public ArrayList<Oficio> getListOficioGuiaR() {
//        return listOficioGuiaR;
//    }
//
//    public void setListOficioGuiaR(ArrayList<Oficio> listOficioGuiaR) {
//        this.listOficioGuiaR = listOficioGuiaR;
//    }
    public Boolean getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(Boolean guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public Oficio getGuiaRem() {
        return guiaRem;
    }

    public void setGuiaRem(Oficio guiaRem) {
        this.guiaRem = guiaRem;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorUnitarioTotal() {
        return valorUnitarioTotal;
    }

    public void setValorUnitarioTotal(BigDecimal valorUnitarioTotal) {
        this.valorUnitarioTotal = valorUnitarioTotal;
    }

    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

}
