/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.activos.service.DetalleConstFisicaService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.activos.service.InventarioRegistroService;
import com.origami.sigef.activos.service.OficioService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.activos.service.ResponsableAdquisicionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
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
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "procesoIngresoInvView")
@ViewScoped
public class ProcesoIngresoInvController extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvController.class.getName());
    private CatalogoMovimiento catalogoMovimiento;
    private List<CatalogoMovimiento> motivoMovimiento;
    private Inventario inventario;
    private Inventario impresion;
    private Inventario inventarioEgreso;
    private Proveedor proveedor;
    private Oficio oficio;
    private OrdenCompra ordenCompra;
    private Factura factura;
    private InventarioRegistro inventarioRegistro;
    private InventarioItems inventarioItems;
    private DetalleItem detalleItem;
    private List<InventarioItems> itemDevueltos; //lista que tiene todos los item del egreso devuelto
    private ArrayList<DetalleItem> listDetalle;
    private ArrayList<Oficio> listOficio;
    private ArrayList<OrdenCompra> listOrdenCompra;
    private List<Factura> listFactura;
    private List<ConstatacionFisicaInventario> listCons;
    private List<DetalleConstFisicaInventario> listConsItem; //lista que trae todos los item de la constatacion
    private ConstatacionFisicaInventario constatacionFisicaInventario;
    private int numero;
    private Integer VV;

    //nuevo adquisiciones
    private List<Adquisiciones> listaAdaqui;
    private Adquisiciones adquisicionesOrden;
    private List<Adquisiciones> listAdq;

    private Boolean bandera = Boolean.TRUE;
    private Boolean habilitar = Boolean.TRUE;
    private Boolean habilitar1 = Boolean.TRUE;
    private Boolean mostrar = Boolean.FALSE;
    private Boolean mostrar2 = Boolean.TRUE;
    private Boolean mostrar3 = Boolean.TRUE;
    private Boolean mostrarAccObs = Boolean.TRUE;
    private Boolean rendOficio = Boolean.TRUE;
    private Boolean rendFactura = Boolean.FALSE;
    private Boolean rendCons = Boolean.FALSE;
    private Boolean rendOrden = Boolean.FALSE;
    private Boolean nuevo = Boolean.FALSE;
    private Boolean condContrato = Boolean.FALSE;
    private Boolean botonProcesos = Boolean.TRUE;
    private boolean botonImprimir = Boolean.TRUE;
    private Boolean botonNew = Boolean.FALSE;
    private Boolean genOrConIngreso = Boolean.TRUE;

    private String encabezado;

    private Boolean botonAprobaciones = Boolean.TRUE;
    private Boolean botonRechazado = Boolean.TRUE;
    private Boolean botonCompletartarea = Boolean.TRUE;
    private String observaciones;
    private Inventario inventarioOpcional;
    private Inventario inventarioEstado;

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
    private FacturaService facturaService;

    @Inject
    private DetalleItemService detalleItemService;

    @Inject
    private ServletSession ss;

    @Inject
    private KatalinaService katalinaService;

    @Inject
    private UserSession userSes;

    //nuevo adquisiciones
    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ResponsableAdquisicionService responsableAdquisicionService;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                this.observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                Long tramiteNumero = tramite.getNumTramite();
                Inventario inv = procesoIngresoService.findInventarioByTramite(tramiteNumero);
                this.impresion = new Inventario();
                this.impresion = Utils.clone(inv);
                if (inv != null) {
                    datosByProceso(inv);
                    botonNew = Boolean.TRUE;
                    this.habilitar = Boolean.TRUE;
                    this.habilitar1 = Boolean.FALSE;
                    this.botonCompletartarea = Boolean.FALSE;
                    this.genOrConIngreso = Boolean.FALSE;
                    if (inv.getEstadoAdicional().equals("COMPLETO")) {
                        botonCompletartarea = Boolean.TRUE;
                        botonProcesos = Boolean.FALSE;
                        this.habilitar1 = Boolean.TRUE;
                    }

                } else {
                    motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("INGINV", "FLU-PROC");
                    this.catalogoMovimiento = new CatalogoMovimiento();
                    this.constatacionFisicaInventario = new ConstatacionFisicaInventario();
                    this.proveedor = new Proveedor();
                    this.proveedor.setCliente(new Cliente());
                    this.inventario = new Inventario();
                    this.inventarioEgreso = new Inventario();
                    this.detalleItem = new DetalleItem();
                    this.listDetalle = new ArrayList<>();
                    this.listOficio = new ArrayList<>();
                    adquisicionesOrden = new Adquisiciones();
                    this.listaAdaqui = new ArrayList<>();
                    this.listaAdaqui = bienesItemService.getListaAdquisicionesInventario();
                    this.listAdq = new ArrayList<>();
                    this.listCons = new ArrayList<>();
                    this.listOrdenCompra = new ArrayList<>();
                    this.listFactura = new ArrayList<>();
                    this.inventarioItems = new InventarioItems();
                    this.oficio = new Oficio();
                    this.ordenCompra = new OrdenCompra();
                    this.factura = new Factura();
                    this.inventarioRegistro = new InventarioRegistro();
                    inventario.setAnio(Utils.getAnio(new Date()).shortValue());
                    inventario.setMotivoMovimiento(motivoMovimiento.get(0));
                    setEncabezado(tarea.getName() + "-" + tramite.getNumTramite().toString());
                }
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void setPeriodoSeleccionado() {
        inventario.setAnio(Utils.getAnio(inventario.getFechaMovimiento()).shortValue());
    }

    public void buscarProveedor() {
        try {
            if (proveedor.getCliente().getIdentificacion() != null) {
                Proveedor p = proveedorService.findByIdentificacion(proveedor.getCliente().getIdentificacion());
                if (p != null) {
//                    p.getCliente().setIdentificacion(identificacion);
                    this.proveedor = p;
                } else {
                    Utils.openDialog("/facelet/activos/inventario/Dialog/dialogProveedor", null);
                }
            } else {
                Utils.openDialog("/facelet/activos/inventario/Dialog/dialogProveedor", null);
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
                if (Objects.equals(detalleItem.getCodigo(), listDetalle.get(i).getCodigo())) {
                    saber = true;
                    break;
                }
            }
        }
        if (saber == true) {
            JsfUtil.addWarningMessage("Información", " Este Item ya está registrado.");
            return;
        }
        detalleItem = (DetalleItem) evt.getObject();
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
            nuevo = true;
        } else {
            this.oficio = of;
            nuevo = false;
        }

        PrimeFaces.current().executeScript("PF('dlgOficio').show()");
    }

    //CONSTATACION FISICA
    public void abrirDialogCons() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("EGRESO"));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogEgreso", params);
    }

    public void selectDatosCons(SelectEvent evt) {
        constatacionFisicaInventario = (ConstatacionFisicaInventario) evt.getObject();
        this.listCons.add(constatacionFisicaInventario);
        listConsItem = detalleConstFisicaService.getListDetalleItemCons(constatacionFisicaInventario);
        Integer c = listConsItem.size();
        for (int w = 0; w < c; w++) {
            if (listConsItem.get(w).getDiferencia() > 0) {
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
                detalleItem.setPrecioTemp(listConsItem.get(w).getDetalleItem().getPrecioCalculado());
                detalleItem.setTotalTemp(BigDecimal.valueOf(listConsItem.get(w).getDiferencia()
                        * listConsItem.get(w).getDetalleItem().getPrecioCalculado().doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN));
                this.listDetalle.add(this.detalleItem);
                detalleItem = new DetalleItem();
            }
        }
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void borrarElementoCons(ConstatacionFisicaInventario cons) {
        listCons.remove(cons);
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
        inventarioEgreso = (Inventario) evt.getObject();
        itemDevueltos = inventarioItemsService.findItemByNombreEgreso(inventarioEgreso);
        Integer V = itemDevueltos.size();
        for (Integer w = 0; w < V; w++) {
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
                    * itemDevueltos.get(w).getDetalleItem().getPrecioCalculado().doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN));
            this.listDetalle.add(this.detalleItem);
            detalleItem = new DetalleItem();
        }
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void mostrarBtn() //<editor-fold defaultstate="collapsed" desc="Mostrar botones uso Boleanos">
    {
        try {
            if ((inventario.getMotivoMovimiento().getTexto().equals("DONACION"))) {
                rendOrden = Boolean.FALSE;
                rendCons = Boolean.FALSE;
                rendFactura = Boolean.FALSE;
                rendOficio = Boolean.TRUE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrar2 = Boolean.FALSE;
                mostrar3 = Boolean.FALSE;
                condContrato = Boolean.FALSE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR ANULACION"))) {
                rendOrden = Boolean.FALSE;
                rendFactura = Boolean.FALSE;
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.TRUE;
                mostrarAccObs = Boolean.FALSE;
                mostrar2 = Boolean.TRUE;
                mostrar3 = Boolean.TRUE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE"))) {
                rendOrden = Boolean.FALSE;
                rendFactura = Boolean.FALSE;
                rendCons = Boolean.TRUE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.FALSE;
                mostrar2 = Boolean.TRUE;
                mostrar3 = Boolean.TRUE;
            } else if (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION")) {
                rendOrden = Boolean.FALSE;
                rendFactura = Boolean.FALSE;
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.TRUE;
                mostrarAccObs = Boolean.FALSE;
                mostrar2 = Boolean.TRUE;
                mostrar3 = Boolean.FALSE;
            } else if ((inventario.getMotivoMovimiento().getTexto().equals("SALDO INICIAL"))) {
                rendOrden = Boolean.FALSE;
                rendFactura = Boolean.FALSE;
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.FALSE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
                mostrar2 = Boolean.FALSE;
                mostrar3 = Boolean.FALSE;
            } else if (inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN")) {
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
                rendFactura = Boolean.FALSE;
                rendCons = Boolean.FALSE;
                rendOficio = Boolean.TRUE;
                mostrar = Boolean.FALSE;
                mostrarAccObs = Boolean.TRUE;
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

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        try {
            if (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION")) {
                Integer b = 0;
                Integer num = listDetalle.size();
                for (int i = 0; i < num; i++) {
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
        }

    }

    public void selectDatos(SelectEvent evt) {
        proveedor = ((Proveedor) evt.getObject());
        proveedor.getCliente().setIdentificacion(proveedor.getCliente().getIdentificacion() + "001");
    }

//<editor-fold defaultstate="collapsed" desc="METODO LIMPIAR">
    public void limpiarTodo() {
        inventario = new Inventario();
        inventarioEgreso = new Inventario();
        constatacionFisicaInventario = new ConstatacionFisicaInventario();
        listDetalle = new ArrayList<>();
        listCons = new ArrayList<>();
        proveedor = new Proveedor();
        proveedor.setCliente(new Cliente());
        oficio = new Oficio();
        ordenCompra = new OrdenCompra();
        factura = new Factura();
        listAdq = new ArrayList<>();
        listOficio = new ArrayList<>();
        listOrdenCompra = new ArrayList<>();
        listFactura = new ArrayList<>();
        inventarioRegistro = new InventarioRegistro();
        bandera = Boolean.TRUE;
        habilitar = Boolean.TRUE;
        mostrar = Boolean.FALSE;
        rendOficio = Boolean.FALSE;
        rendCons = Boolean.FALSE;
        rendFactura = Boolean.FALSE;
        rendOrden = Boolean.FALSE;
        mostrarAccObs = Boolean.TRUE;
        mostrar2 = Boolean.TRUE;
        mostrar3 = Boolean.TRUE;
        botonImprimir = Boolean.TRUE;
        botonProcesos = Boolean.TRUE;
        botonNew = Boolean.TRUE;

    }
//</editor-fold>

    public void nuevoIngreso() {
        Subject subject = SecurityUtils.getSubject();
        if (bandera) {
            mostrar2 = Boolean.TRUE;
            habilitar = Boolean.FALSE;
            habilitar1 = Boolean.FALSE;
            this.inventario = new Inventario();
            inventario.setAnio(Utils.getAnio(new Date()).shortValue());

            inventario.setEstado(Boolean.TRUE);
            inventario.setUsuarioOriginador(subject.getPrincipal().toString());
            inventario.setTipoMovimiento("INGRESO");
            inventario.setFechaMovimiento(new Date());
            inventario.setMotivoMovimiento(motivoMovimiento.get(0));
            bandera = Boolean.FALSE;
            mostrarBtn();
            PrimeFaces.current().ajax().update("frmMain");
        } else {
            habilitar = Boolean.FALSE;
            JsfUtil.addWarningMessage("Información", "Tiene un ingreso generado, por favor CANCELAR para generar un nuevo ingreso");
        }

    }

    public void guardar() {
        if (inventario.getMotivoMovimiento() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar un motivo");
            return;
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("OFICIO")) || (inventario.getMotivoMovimiento().getTexto().equals("DONACION"))) {
            if (listOficio.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Por favor escoja un oficio para este motivo");
                return;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR ANULACION")) || (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION"))) {
            if (inventarioEgreso.getCodigo() == null) {
                JsfUtil.addWarningMessage("Error", "Por favor cargue un egreso para continuar");
                return;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN"))) {
            if ((listAdq.isEmpty())) {
                JsfUtil.addWarningMessage("Error", "Por favor adicione datos de adquisición para continuar");
                return;
            }
            if ((listFactura.isEmpty())) {
                JsfUtil.addWarningMessage("Error", "Por favor adicione facturas para continuar");
                return;
            }
            if ((proveedor.getCliente().getIdentificacion() == null)) {
                JsfUtil.addWarningMessage("Error", "Seleccione el proveedor");
                return;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE")) || (inventario.getMotivoMovimiento().getTexto().equals("SALDO INICIAL"))) {
            if (listDetalle.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Escoja los item para continuar");
                return;
            }
        }

        if (listDetalle.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "Adicione Items para poder guardar");
            return;
        }

        inventario.setEstadoAdicional("INCOMPLETO");

        Integer d = listDetalle.size();

        Integer cond = 0;
        try {
            for (Integer w = 0; w < d; w++) {
                if ((listDetalle.get(w).getCantidadTemp() <= 0)) {
                    cond++;
                }
                if (cond == 0) {
                    if ((inventario.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN"))) {
//                        proveedor.getCliente().setIdentificacion(proveedor.getCliente().getIdentificacion().substring(0, 10));
                        inventario.setProveedor(proveedor);
                    }
                    if ((inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR ANULACION")) || (inventario.getMotivoMovimiento().getTexto().equals("INGRESO POR DEVOLUCION"))) {
                        inventario.setIngresoEgresoRelacionado(inventarioEgreso);
                    }
                    if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE"))) {
                        inventario.setConstatacionFisica(constatacionFisicaInventario);
                    }
                    inventario.setNumeroTramite(tramite.getNumTramite());
                    this.inventarioOpcional = new Inventario();
                    this.inventarioEstado = new Inventario();
                    Long orden = procesoIngresoService.getOrderInventario("INGRESO");
                    inventario.setOrden(orden);
                    String codigoGenerado = Utils.completarCadenaConCeros(inventario.getOrden() + "", 5);
                    inventario.setCodigo(codigoGenerado);
                    inventario.setCodigo("ING-" + codigoGenerado);
                    Inventario inve = procesoIngresoService.create(inventario);
                    this.inventarioOpcional = Utils.clone(this.inventario);
                    this.inventarioEstado = Utils.clone(this.inventario);
                    inventarioRegistro.setMovimiento(inventario);
                    inventarioRegistro.setEstado(Boolean.TRUE);
                    InventarioRegistro reg = inventarioRegistroService.create(inventarioRegistro);

                    //RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LOS DETALLE ITEM EN BASE DE DATOS
                    Integer t = listDetalle.size();
                    Integer ii = 0;
                    actualizarItemAnadirInvItems(ii, t);

                    // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LOS OFICIOS EN BASE DE DATOS
                    Integer a = listOficio.size();
                    for (Integer i = 0; i < a; i++) {
                        listOficio.get(i).setEstado(true);

                        if (condContrato) {
                            listOficio.get(i).setTipoDocumento("OFICIO");
                        } else {
                            listOficio.get(i).setTipoDocumento("DONACIÓN");
                        }
                        oficioService.create(listOficio.get(i));   // AQUI VERIFIFCAR  OJO
                        inventarioRegistro.setOficio(listOficio.get(i));
                        inventarioRegistroService.edit(inventarioRegistro);
                    }

                    // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE las adquisiciones  EN BASE DE DATOS
                    Integer b = listAdq.size();
                    for (Integer i = 0; i < b; i++) {
                        inventarioRegistro.setAdquisiciones(listAdq.get(i));
                        inventarioRegistroService.edit(inventarioRegistro);
                    }

                    // RECORRIDO PARA GUARDAR LO DEL ARRAYLIST DE LAS FACTURAS EN BASE DE DATOS
                    Integer c = listFactura.size();
                    for (Integer i = 0; i < c; i++) {
                        listFactura.get(i).setEstado(true);
                        listFactura.get(i).setProveedor(proveedor);
                        listFactura.get(i).setInventarioRegistro(inventarioRegistro);
                        facturaService.create(listFactura.get(i));    // ver si necesito new listFactura
                    }

                    //  guardado todo, procedemos a inicializar todo
                    limpiarTodo();
                    JsfUtil.addSuccessMessage("Información", "Ingreso exitoso");
                    this.botonCompletartarea = Boolean.FALSE;

                } else {
                    JsfUtil.addWarningMessage("Información", "Valores en cero, favor ingresar cantidad o precio diferente a 0");
                }

            }

        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void ingresarObservaciones(boolean aprobar) {

        if (aprobar) {
            this.botonAprobaciones = Boolean.FALSE;
            this.botonRechazado = Boolean.TRUE;

        } else {
            this.botonAprobaciones = Boolean.TRUE;
            this.botonRechazado = Boolean.FALSE;

            inventarioOpcional.setEstadoAdicional("RECHAZADO");
            procesoIngresoService.edit(inventarioOpcional);
            List<InventarioItems> invI = inventarioItemsService.getItemByInventarioItems(inventarioOpcional);
            List<DetalleItem> listDet;
            listDet = new ArrayList<>();
            Integer z = invI.size();
            for (int i = 0; i < z; i++) {
                listDet.add(invI.get(i).getDetalleItem());
            }
            Integer t = listDet.size();
            Integer ii = 0;
            rechazarItems(ii, t, listDet, invI);
        }

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        observacion.setIdTramite(tramite);

        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void continuarEstadoProcesos() {

        this.inventarioOpcional.setEstadoAdicional("COMPLETO");

        procesoIngresoService.edit(inventarioOpcional);
        botonCompletartarea = Boolean.TRUE;
        botonProcesos = Boolean.FALSE;

        JsfUtil.addInformationMessage("Información", "Tarea terminada, a continuación apruebe o rechace el Registro");

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

    public void rechazarItems(Integer i, Integer t, List<DetalleItem> listDetalle, List<InventarioItems> invII) {
        for (; i < t; i++) {
            //actualizar precio cantidad en ITEM.
            String codigo = listDetalle.get(i).getCodigo();;
//            DetalleItem item = detalleItemService.findByCodigoList(codigo, inventarioOpcional.getAnio());
            DetalleItem item = detalleItemService.findByCodigoList(codigo);
            Integer canExistente = item.getCantidadExistente();
            Integer canIngresada = invII.get(i).getCantidad();
            BigDecimal pre = invII.get(i).getPrecio();
            item.setCantidadExistente(canExistente - canIngresada);
            Integer cant = canExistente - canIngresada;
            item.setPrecioCalculado(pre);
            BigDecimal total = BigDecimal.valueOf(cant * pre.doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
            item.setTotalCalculado(total);
            detalleItemService.edit(item);
        }
    }

    public void imprimir() {
        if ((impresion.getMotivoMovimiento().getTexto().equals("ADQUISICIÓN"))) {
            /*Obtener Administrador de Contrato de Gestión de Adquisiciones*/
            Adquisiciones adquisicion = inventarioRegistroService.getAdquisicionByInventarioRegistro(impresion);
            Servidor serv = responsableAdquisicionService.getAdministradorContrato(adquisicion);
            if (serv != null) {
                ss.addParametro("admin_nombre", serv.getPersona().getNombreCompleto());
                ss.addParametro("admin_cargo", serv.getDistributivo().getCargo().getNombreCargo());
            } else {
                ss.addParametro("admin_nombre", "");
                ss.addParametro("admin_cargo", "");
            }
            ss.addParametro("periodo", inventario.getAnio());
            ss.addParametro("fecha_actual", Utils.convertirFechaLetra(new Date()));
            if (impresion.getProveedor().getCliente().getClasificacionProv().getOrden() == 1) {  // es natural
                ss.addParametro("tramite", impresion.getNumeroTramite());
                ss.setNombreReporte("actaEntregaRecepInventario");
                ss.setNombreSubCarpeta("activos");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else {
                ss.addParametro("tramite", impresion.getNumeroTramite());
                ss.setNombreReporte("actaEntregaRecepInventarioJuridico");
                ss.setNombreSubCarpeta("activos");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
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
                + "<p style=\"width:200px;\"> Se le notifica que el trámite Alta de Inventario ha sido generado. "
                + "Solicitamos se acerque a legalizar dicha acta. </p>\n"
                + "</body>\n"
                + "</html>");// codigo html
        katalinaService.enviarCorreo(c);
    }

    public void culimnarTarea() {
        try {
            System.out.println("impresion " + impresion);
            if (impresion != null) {
                Correo c = new Correo();
                Cliente emailRepresentanteLegal = new Cliente();
                Cliente emailGuardalmacen = procesoIngresoService.getEmailGuardalmacen(impresion.getUsuarioOriginador());
                Cliente emailProveedor = procesoIngresoService.getEmailProveedor(impresion.getProveedor());
                if (impresion.getProveedor().getContacto() != null) {
                    emailRepresentanteLegal = procesoIngresoService.getEmailRepresentanteLegal(impresion.getProveedor());
                }
                Long idRegistro = inventarioRegistroService.findIdRegistroInvByNumInv(impresion);
                Cliente emailAdministradorContrato = procesoIngresoService.getEmailAdminContrato(idRegistro);
                List<Cliente> correoTipo1 = new ArrayList<>(), correoTipo2 = new ArrayList<>();
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

                if (impresion.getProveedor().getCliente().getClasificacionProv().getOrden() == 1) { //es natural
                    correoTipo1.forEach((cli1) -> {
                        enviarCorreo(cli1.getEmail(), tramite.getTipoTramite().getDescripcion(), cli1.getNombreCompleto());
                    });
                } else {
                    correoTipo2.forEach((cli2) -> {
                        enviarCorreo(cli2.getEmail(), tramite.getTipoTramite().getDescripcion(), cli2.getNombreCompleto());
                    });
                }
            }
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            // JsfUtil.executeJS("PF('continuarDlg').show()");
            super.completeTask((HashMap<String, Object>) getParamts());
        } catch (Exception e) {
            System.out.println("A ocurrido un error " + e);
        }
    }

    public void irBandejatareas() {
        JsfUtil.redirectFaces("/procesos/bandeja-tareas");
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
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
        PrimeFaces.current().ajax().update("frmMain:outPanelMedidaCuatro");
    }

    public void editarOficio() {
        listOficio = new ArrayList<>();
        this.listOficio.add(this.oficio);
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
        PrimeFaces.current().ajax().update("frmMain:outPanelMedidaCuatro");
    }

    public void limpiarVariableFactura() {
        nuevo = Boolean.FALSE;
        factura = new Factura();
    }

    public void guardarFactura() {
        if (factura.getNumFactura() == null) {
            JsfUtil.addErrorMessage("FACTURA", "Ingrese un número de Factura");
            return;
        }
        this.listFactura.add(this.factura);
        PrimeFaces.current().executeScript("PF('dlgFactura').hide();");
    }

    public void editarFactura() {
//        this.listFactura.add(this.factura);
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
        listAdq.clear();
        listAdq.add(adq);

        PrimeFaces.current().ajax().update("frmMain:outPanelMedidaCuatro");
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').hide()");
        PrimeFaces.current().ajax().update("frmMain:dtOrden");
    }

    public void openDlgAdquisicion() {
        PrimeFaces.current().executeScript("PF('dlgAdquisiciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgAdquisicion");
    }

    public void datosByProceso(Inventario inv) {
        this.inventario = new Inventario();
        this.inventario = inv;
        this.inventarioOpcional = inv;
        this.inventarioEstado = inv;
        motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("INGINV", "FLU-PROC");
        this.detalleItem = new DetalleItem();
        this.listDetalle = new ArrayList<>();
        this.listOficio = new ArrayList<>();
        this.listAdq = new ArrayList<>();
        this.listCons = new ArrayList<>();
        this.proveedor = new Proveedor();
        this.proveedor.setCliente(new Cliente());
        this.listFactura = new ArrayList<>();
        this.proveedor = inv.getProveedor();
        this.inventarioEgreso = new Inventario();
        this.inventarioEgreso = inv.getIngresoEgresoRelacionado();
        this.inventarioItems = new InventarioItems();

        List<InventarioItems> itemsDevueltos = inventarioItemsService.getItemByInventarioItems(inv);
        List<InventarioRegistro> invRegLista = inventarioRegistroService.findRegistroInvByNumInv(inv);
        List<Factura> listFact = facturaService.findFacturasByRegistro(invRegLista.get(0));

        listFactura = listFact;

        Oficio ofic = invRegLista.get(0).getOficio();
        Adquisiciones adqui = invRegLista.get(0).getAdquisiciones();
        this.listCons.add(inv.getConstatacionFisica());
        this.listAdq.add(adqui);
        this.listOficio.add(ofic);
        VV = itemsDevueltos.size();
        for (Integer w = 0; w < VV; w++) {
            detalleItem.setCodigoAgrupado(itemsDevueltos.get(w).getDetalleItem().getCodigoAgrupado());
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

    public void actualizarItemAnadirInvItems(Integer i, Integer t) {
        for (; i < t; i++) {
            //actualizar precio cantidad en ITEM.
            String codigo = listDetalle.get(i).getCodigo();
//            DetalleItem item = detalleItemService.findByCodigoList(codigo, inventario.getAnio());
            DetalleItem item = detalleItemService.findByCodigoList(codigo);
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
                totalExistente = totalExistente.add(total).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                item.setTotalCalculado(totalExistente);
                if ((totalExistente.intValue() == 0) && (can == 0)) {
                    item.setPrecioCalculado(BigDecimal.ZERO);
                } else {
                    item.setPrecioCalculado(BigDecimal.valueOf(totalExistente.doubleValue() / can).setScale(4, BigDecimal.ROUND_HALF_EVEN));
                }
            } else {
                prec = item.getPrecioCalculado().setScale(4, BigDecimal.ROUND_HALF_EVEN);
                total = BigDecimal.valueOf(canIngresada * prec.doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                totalExistente = totalExistente.add(total).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                item.setTotalCalculado(totalExistente);
                if ((totalExistente.intValue() == 0) && (can == 0)) {
                    item.setPrecioCalculado(BigDecimal.ZERO);
                } else {
                    item.setPrecioCalculado(BigDecimal.valueOf(totalExistente.doubleValue() / can).setScale(4, BigDecimal.ROUND_HALF_EVEN));
                }
            }
            detalleItemService.edit(item);
            //guardar item transaccionados en inventario_item
            inventarioItems.setInventario(inventario);
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
            InventarioItems inv = inventarioItemsService.create(inventarioItems);
            inventarioItems = new InventarioItems();
        }
    }

    public void calcularIvaDetalle(DetalleItem item) {
        //cambiar cantidad
        if (item.getCantidadTemp().intValue() > 0) {
            if (item.getPrecioTemp().doubleValue() > 0) {
                if (item.getIvaTemp().doubleValue() > 0) {
                    double neto = (item.getCantidadTemp().intValue() * item.getPrecioTemp().doubleValue());
                    double iva = neto * (item.getIvaTemp().doubleValue() / 100);
                    item.setTotalTemp(new BigDecimal(neto + iva));
                } else {
                    item.setTotalTemp(new BigDecimal(item.getCantidadTemp().intValue() * item.getPrecioTemp().doubleValue()));
                }
            } else {
                item.setTotalTemp(BigDecimal.ZERO);
            }
        } else {
            item.setPrecioTemp(BigDecimal.ZERO);
            item.setTotalTemp(BigDecimal.ZERO);
        }
    }

    public void guardarAdicional() {

        Integer t = listDetalle.size();
        Integer a = VV;
        Integer cond = 0;
        for (Integer w = 0; w < t; w++) {
            if ((listDetalle.get(w).getCantidadTemp() <= 0)) {
                cond++;
            }
        }
        if (cond == 0) {
            if (!Objects.equals(a, t)) {
                actualizarItemAnadirInvItems(a, t);
                JsfUtil.addSuccessMessage("Información", "Ingreso exitoso");
            } else {
                JsfUtil.addWarningMessage("Información", "No existen items adicionales añadidos, puede añadir o terminar tarea");
            }
        } else {
            JsfUtil.addWarningMessage("Información", "Valores en cero, favor ingresar cantidad o precio diferente a 0");
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

    public Boolean getMostrar2() {
        return mostrar2;
    }

    public void setMostrar2(Boolean mostrar2) {
        this.mostrar2 = mostrar2;
    }

    public Boolean getMostrar3() {
        return mostrar3;
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

//</editor-fold>
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

    public Boolean getBotonNew() {
        return botonNew;
    }

    public Integer getVV() {
        return VV;
    }

    public void setVV(Integer VV) {
        this.VV = VV;
    }

    public Boolean getHabilitar1() {
        return habilitar1;
    }

    public Inventario getImpresion() {
        return impresion;
    }

    public void setImpresion(Inventario impresion) {
        this.impresion = impresion;
    }

    public void setHabilitar1(Boolean habilitar1) {
        this.habilitar1 = habilitar1;
    }

    public Boolean getGenOrConIngreso() {
        return genOrConIngreso;
    }

    public void setGenOrConIngreso(Boolean genOrConIngreso) {
        this.genOrConIngreso = genOrConIngreso;
    }

    public void setBotonNew(Boolean botonNew) {
        this.botonNew = botonNew;
    }

    public Inventario getInventarioOpcional() {
        return inventarioOpcional;
    }

    public void setInventarioOpcional(Inventario inventarioOpcional) {
        this.inventarioOpcional = inventarioOpcional;
    }

}
