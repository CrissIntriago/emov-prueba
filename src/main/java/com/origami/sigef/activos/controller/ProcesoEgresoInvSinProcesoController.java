/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.activos.service.ConstatacionFisicaInvService;
import com.origami.sigef.activos.service.DetalleConstFisicaService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.activos.service.InventarioRegistroService;
import com.origami.sigef.activos.service.OficioService;
import com.origami.sigef.activos.service.OrdenRequisicionItemsService;
import com.origami.sigef.activos.service.OrdenRequisicionService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.administracionCompra.controller.Numero_Letras;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ConstatacionFisicaInventario;
import com.origami.sigef.common.entities.DetalleConstFisicaInventario;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.InventarioRegistro;
import com.origami.sigef.common.entities.Oficio;
import com.origami.sigef.common.entities.OrdenRequisicion;
import com.origami.sigef.common.entities.OrdenRequisicionItems;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
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
@Named(value = "procesoEgresoInvSinProView")
@ViewScoped
public class ProcesoEgresoInvSinProcesoController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvController.class.getName());
    private CatalogoMovimiento catalogoMovimiento;
    private List<CatalogoMovimiento> motivoMovimiento;
    private List<InventarioItems> itemDevueltos;  //lista que tiene todos los item del ingreso relacionado
    private List<OrdenRequisicionItems> listOrdenRequisicionItems; // lista que trae los item de la requisicion
    private ArrayList<Oficio> listOficio;
    private ArrayList<OrdenRequisicion> listOrdenRequisicion;
    private ArrayList<DetalleItem> listDetalle;
    private Inventario inventario;
    private Inventario impresion;
    private InventarioItems inventarioItems;
    private InventarioRegistro inventarioRegistro;
    private DetalleItem detalleItem;
    private OrdenRequisicionItems ordenRequisicionItems;
    private Inventario inventarioIngreso;
    private Servidor servidor;
    private Oficio oficio;
    private OrdenRequisicion ordenRequisicion;
    private Numero_Letras numeroLetra;
//    private UnidadAdministrativa unidadAdministrativa;
    private ConstatacionFisicaInventario constatacionFisicaInventario;
    private List<ConstatacionFisicaInventario> listCons;
    private List<DetalleConstFisicaInventario> listConsItem; //lista que trae todos los item de la constatacion

//    private Boolean habilitar = Boolean.TRUE;
    private Boolean bandera = Boolean.TRUE;
    private Boolean habilitar1 = Boolean.TRUE;
    private Boolean habilitarBtn = Boolean.TRUE;
    private Boolean habilitarBtnItem = Boolean.TRUE;
    private Boolean habilitarBtnSol = Boolean.TRUE;
    private Boolean habilitar2 = Boolean.FALSE;
    private Boolean mostrarIng = Boolean.FALSE;
    private Boolean mostrarOfi = Boolean.FALSE;
    private Boolean mostrarReq = Boolean.TRUE;
    private Boolean nuevo = Boolean.FALSE;
    private Boolean rendCons = Boolean.FALSE;
    private Boolean bolImprimir = Boolean.TRUE;
//    private Boolean unidad = Boolean.TRUE;
//    private Boolean departam = Boolean.TRUE;
    private Boolean mostrar2 = Boolean.TRUE;

    private Boolean botonAprobaciones = Boolean.TRUE;
    private Boolean botonRechazado = Boolean.TRUE;
    private Boolean botonCompletartarea = Boolean.TRUE;
    private String observaciones;
    private Inventario inventarioOpcional;
    private Boolean botonProcesos = Boolean.TRUE;
    private boolean botonImprimir = Boolean.TRUE;
    private ThServidorCargo cargoSelectData;
    private ThServidorCargo cargoAutorizadorSelectData;

    @Inject
    private ProcesoIngresoService procesoIngresoService;
    @Inject
    private CatalogoMovimientoService catalogoMovimientoService;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private InventarioItemsService inventarioItemsService;
    @Inject
    private OrdenRequisicionItemsService ordenRequisicionItemsService;
    @Inject
    private OrdenRequisicionService ordenRequisicionService;
    @Inject
    private InventarioRegistroService inventarioRegistroService;
    @Inject
    private OficioService oficioService;
    @Inject
    private DetalleConstFisicaService detalleConstFisicaService;
//    @Inject
//    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
    @Inject
    private ConstatacionFisicaInvService cfis;
    @Inject
    private ThServidorCargoService thServidorCargoService;

    @PostConstruct
    public void initView() {
        try {

            motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("SALINV", "SIN-FLUJ");
            this.catalogoMovimiento = new CatalogoMovimiento();
            detalleItem = new DetalleItem();
            listDetalle = new ArrayList<>();
            inventario = new Inventario();
            inventarioIngreso = new Inventario();
            constatacionFisicaInventario = new ConstatacionFisicaInventario();
            inventarioRegistro = new InventarioRegistro();
            ordenRequisicion = new OrdenRequisicion();
            this.inventario = new Inventario();
            inventarioItems = new InventarioItems();
            servidor = new Servidor();
            servidor.setPersona(new Cliente());
//            unidadAdministrativa = new UnidadAdministrativa();
            inventario.setUsuarioAutorizador(new Servidor());
            oficio = new Oficio();
            listOficio = new ArrayList<>();
            listCons = new ArrayList<>();
            listConsItem = new ArrayList<>();
            listOrdenRequisicion = new ArrayList<>();
            inventarioOpcional = new Inventario();
            numeroLetra = new Numero_Letras();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarEgreso() {
        try {
            cargoSelectData = new ThServidorCargo();
            String codigo = inventario.getCodigo();
            if (Utils.isNum(inventario.getCodigo())) {
                String codigoGenerado = Utils.completarCadenaConCeros(inventario.getCodigo(), 5);
                codigo = "EGR-" + codigoGenerado;
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
                        di.setCantidadTempSol(invi.getCantidad());
                        listDetalle.add(di);
                    }
                }
                InventarioRegistro ir = new InventarioRegistro();
                ir.setMovimiento(inventario);
                List<InventarioRegistro> ofs = inventarioRegistroService.findByExample(ir);
                if (Utils.isNotEmpty(ofs)) {
                    for (InventarioRegistro of : ofs) {
                        if (of.getOficio() != null) {
                            listOficio.add(of.getOficio());
                        }
                    }
                    this.mostrarOfi = true;
                }
                if (inventario.getIngresoEgresoRelacionado() != null) {
                    inventarioIngreso = inventario.getIngresoEgresoRelacionado();
                    mostrarIng = true;
                }
                this.bolImprimir = false;
                this.habilitar1 = false;
                this.impresion = Utils.clone(inventario);
                servidor = inventario.getUsuarioSolicitante();
                if (servidor != null) {
                    cargoSelectData = thServidorCargoService.findByThServidor(servidor);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    // CARGAR SERVIDOR

    public void buscarServidor(boolean varSolic) {
//        habilitar = Boolean.TRUE;

        if (varSolic) {
            cargoSelectData = new ThServidorCargo();
            if (servidor.getPersona().getIdentificacion() != null) {
                Servidor serv = procesoIngresoService.findByIdentificacionServ(servidor.getPersona().getIdentificacion());
                if (serv != null) {

                    this.servidor = serv;
                    cargoSelectData = thServidorCargoService.findByThServidor(servidor);
                    inventario.setUnidad(cargoSelectData.getIdCargo().getIdUnidad().getNombre());

                } else {
                    Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "456");
                }
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "456");
            }
        } else {
            cargoAutorizadorSelectData = new ThServidorCargo();
            if (inventario.getUsuarioAutorizador().getPersona().getIdentificacion() != null) {
                Servidor serv = procesoIngresoService.findByIdentificacionServ(inventario.getUsuarioAutorizador().getPersona().getIdentificacion());
                if (serv != null) {
                    inventario.setUsuarioAutorizador(serv);
                    cargoAutorizadorSelectData = thServidorCargoService.findByThServidor(serv);
                    inventario.setDireccion(cargoAutorizadorSelectData.getIdCargo().getIdUnidad().getNombre());
                } else {
                    Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "456");
                }
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "456");
            }
        }
    }

    public void selectDataServidor(SelectEvent evt) {
        servidor = ((Servidor) evt.getObject());
        cargoSelectData = thServidorCargoService.findByThServidor(servidor);
        inventario.setUnidad(cargoSelectData.getIdCargo().getIdUnidad().getNombre());
    }

    public void selectDataServidorAutorizador(SelectEvent evt) {
        inventario.setUsuarioAutorizador((Servidor) evt.getObject());
        cargoAutorizadorSelectData = thServidorCargoService.findByThServidor(inventario.getUsuarioAutorizador());
        inventario.setDireccion(cargoAutorizadorSelectData.getIdCargo().getIdUnidad().getNombre());
    }

    public void limpiarTodo() {
        this.impresion = new Inventario();
        if (inventario != null) {
            this.impresion = Utils.clone(this.inventario);
        }
        bandera = Boolean.TRUE;
//        habilitar = Boolean.TRUE;
        habilitar1 = Boolean.TRUE;
        habilitar2 = Boolean.FALSE;
        mostrarIng = Boolean.FALSE;
        mostrarOfi = Boolean.TRUE;
        mostrarReq = Boolean.TRUE;
        habilitarBtn = Boolean.TRUE;
        habilitarBtnItem = Boolean.TRUE;
        habilitarBtnSol = Boolean.TRUE;
        bolImprimir = Boolean.TRUE;
        rendCons = Boolean.FALSE;
        mostrar2 = Boolean.TRUE;
        inventario = new Inventario();
        inventarioIngreso = new Inventario();
        constatacionFisicaInventario = new ConstatacionFisicaInventario();
        inventarioRegistro = new InventarioRegistro();
        ordenRequisicion = new OrdenRequisicion();
        listDetalle = new ArrayList<>();
        detalleItem = new DetalleItem();
        servidor = new Servidor();
        servidor.setPersona(new Cliente());
//        unidadAdministrativa = new UnidadAdministrativa();
        oficio = new Oficio();
        listOficio = new ArrayList<>();
        listOrdenRequisicion = new ArrayList<>();
        listCons = new ArrayList<>();
        listConsItem = new ArrayList<>();
        cargoSelectData = new ThServidorCargo();
    }

    //CONSTATACION FISICA
    public void abrirDialogCons() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("EGRESO"));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogConstatacion", params);
    }

    public void selectDatosCons(SelectEvent evt) {
        constatacionFisicaInventario = (ConstatacionFisicaInventario) evt.getObject();
        this.listCons.add(constatacionFisicaInventario);
        listConsItem = detalleConstFisicaService.getListDetalleItemCons(constatacionFisicaInventario);
        Integer c = listConsItem.size();
        habilitar1 = Boolean.FALSE;
        for (int w = 0; w < c; w++) {
            if (listConsItem.get(w).getDiferencia() < 0) {
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
                detalleItem.setCantidadExistente(listConsItem.get(w).getDetalleItem().getCantidadExistente());
                Integer porMenosUno = ((listConsItem.get(w).getDiferencia()) * (-1));
                detalleItem.setCantidadTemp(porMenosUno);
                detalleItem.setCantidadTempSol(porMenosUno);
                detalleItem.setObservacionTemp(listConsItem.get(w).getObservacion());
                detalleItem.setPrecioTemp(listConsItem.get(w).getDetalleItem().getPrecioCalculado());
                detalleItem.setTotalTemp(listConsItem.get(w).getDetalleItem().getTotalCalculado());
                this.listDetalle.add(this.detalleItem);
                detalleItem = new DetalleItem();
            }
        }
        PrimeFaces.current().ajax().update("frmMain:outPanelCons");
        PrimeFaces.current().ajax().update("frmMain:dtItemEgreso");
    }

    public void borrarElementoCons(ConstatacionFisicaInventario cons) {
        listCons.remove(cons);
    }

    public void nuevoEgreso() {
        Subject subject = SecurityUtils.getSubject();
        if (bandera) {
            JsfUtil.addInformationMessage("Información", "Escoja un motivo");
            habilitarBtnItem = Boolean.FALSE;
            habilitarBtnSol = Boolean.FALSE;
            habilitarBtn = Boolean.FALSE;
            bolImprimir = Boolean.TRUE;
            inventario = new Inventario();
            inventario.setUsuarioAutorizador(new Servidor());
            inventario.getUsuarioAutorizador().setPersona(new Cliente());
            //Long orden = procesoIngresoService.getOrderInventario("EGRESO");
//            inventario.setAnio(Utils.getAnio(new Date()).shortValue());
            //inventario.setOrden(orden);
            inventario.setEstado(Boolean.TRUE);
            inventario.setUsuarioOriginador(subject.getPrincipal().toString());
            inventario.setTipoMovimiento("EGRESO");
            inventario.setFechaMovimiento(new Date());
            inventario.setEstadoAdicional("COMPLETO");
            bandera = Boolean.FALSE;
        } else {
            habilitarBtnItem = Boolean.FALSE;
            habilitarBtnItem = Boolean.FALSE;
            habilitarBtn = Boolean.FALSE;
//            habilitar = Boolean.FALSE;
            JsfUtil.addWarningMessage("Información", "Tiene un Egreso generado, por favor CANCELAR para generar un nuevo Egreso");
        }
    }

    // TRAER INGRESO RELACIONADO
    public void abrirDialogIngreso() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("INGRESO"));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogEgreso", params);
    }

    public void selectDatosIngreso(SelectEvent evt) {
        listDetalle = new ArrayList<>();
        habilitar1 = Boolean.TRUE;
        Integer cont = 0;
        inventarioIngreso = (Inventario) evt.getObject();
        itemDevueltos = inventarioItemsService.findItemByNombreEgreso(inventarioIngreso);
        Integer a = itemDevueltos.size();
        for (int i = 0; i < a; i++) {
            detalleItem.setCodigo(itemDevueltos.get(i).getDetalleItem().getCodigo());
            detalleItem.setCodigoAgrupado(itemDevueltos.get(i).getDetalleItem().getCodigoAgrupado());
            detalleItem.setDescripcion(itemDevueltos.get(i).getDetalleItem().getDescripcion());
            detalleItem.setTipoMedida(itemDevueltos.get(i).getDetalleItem().getTipoMedida());
            detalleItem.setCantidadExistente(itemDevueltos.get(i).getDetalleItem().getCantidadExistente());
            detalleItem.setCantidadTempSol(itemDevueltos.get(i).getCantidad());
            detalleItem.setCantidadTempDes(0);
            detalleItem.setPrecioTemp(itemDevueltos.get(i).getPrecio());
            detalleItem.setCantidadTemp(itemDevueltos.get(i).getCantidad());
            if (itemDevueltos.get(i).getCantidad() > itemDevueltos.get(i).getDetalleItem().getCantidadExistente()) {
                cont++;
            }
            this.listDetalle.add(this.detalleItem);
            detalleItem = new DetalleItem();
        }
        if (cont > 0) {
            habilitarBtn = Boolean.TRUE;
            habilitar1 = Boolean.FALSE;
            JsfUtil.addErrorMessage("Error", "Items del ingreso " + inventarioIngreso.getCodigo() + ","
                    + " ya fueron entregados a usuario final. Por favor escoja otro ingreso.");
        }
        PrimeFaces.current().ajax().update("frmMain:outPanelCargarIngreso");
        PrimeFaces.current().ajax().update("frmMain:dtItemEgreso");
    }

    //TRAER OFICIO
    public void abrirDialogOficio() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogOficio", null);
    }

    public void selectDatosOficio(SelectEvent evt) {
        oficio = (Oficio) evt.getObject();
        this.listOficio.add(this.oficio);
    }

    public void borrarElementoOficio(Oficio of) {
        listOficio.remove(of);
    }

    public void abrirDialogOficioCambios(Oficio of) {
        if (of == null) {
            oficio = new Oficio();
            nuevo = true;
        } else {
            this.oficio = of;
            nuevo = false;
        }
        PrimeFaces.current().executeScript("PF('dlgOficio').show()");
    }

    //TRAER ORDEN DE REQUISICIÓN
    public void abrirDialogRequisicion() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogOrdenRequisicion", null);
    }

    public void selectDatosRequisicion(SelectEvent evt) {
        ordenRequisicion = (OrdenRequisicion) evt.getObject();
        this.listOrdenRequisicion.add(this.ordenRequisicion);
        listOrdenRequisicionItems = ordenRequisicionItemsService.findByIdCodigo(ordenRequisicion);
        Integer V = listOrdenRequisicionItems.size();
        for (int i = 0; i < V; i++) {
            detalleItem.setCodigo(listOrdenRequisicionItems.get(i).getItem().getCodigo());
            detalleItem.setCodigoAgrupado(listOrdenRequisicionItems.get(i).getItem().getCodigoAgrupado());
            detalleItem.setDescripcion(listOrdenRequisicionItems.get(i).getItem().getDescripcion());
            detalleItem.setTipoMedida(listOrdenRequisicionItems.get(i).getItem().getTipoMedida());
            detalleItem.setCantidadExistente(listOrdenRequisicionItems.get(i).getItem().getCantidadExistente());
            detalleItem.setCantidadTempSol(listOrdenRequisicionItems.get(i).getCantidadSolicitada());
            detalleItem.setCantidadTempDes(0);
            detalleItem.setCantidadTemp(0);
            this.listDetalle.add(detalleItem);
            detalleItem = new DetalleItem();
        }
        //SETEAMOS SOLICITANTE CON SI DIRECCION, DEPARTAMENTO Y UNIDAD
        servidor = ordenRequisicion.getServidorSolicitante();
        if (servidor != null) {
            cargoSelectData = thServidorCargoService.findByThServidor(servidor);
        }
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void borrarElementoRequisicion(OrdenRequisicion ord) {
        listOrdenRequisicion.remove(ord);
    }

    // PARA ITEM
    public void abrirDialogItem() {
//        Map<String, List<String>> params = new HashMap<>();
//        params.put("PERIODO", Arrays.asList(inventario.getAnio().toString()));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogItemCta", null);
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
        PrimeFaces.current().ajax().update("frmMain:dtItemEgreso:uno");
    }

    public void borrarElementoLista(DetalleItem egr) {
        listDetalle.remove(egr);
    }

    //CAMBIOS DE VALORES EN CELDAS DE ITEM
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        int nn = event.getRowIndex();
        if (inventario.getMotivoMovimiento().getTexto().equals("REQUISICIÓN")) {
            Integer num = listDetalle.size();
            if ((listDetalle.get(nn).getCantidadTempDes() >= 1) && (listDetalle.get(nn).getCantidadTempDes() <= listDetalle.get(nn).getCantidadTempSol())) {
                listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempDes());
            } else {
                JsfUtil.addErrorMessage("Valor No Modificado", "No existe esa cantidad en Stock o Cantidad a despachar es mayor a solicitada");
                listDetalle.get(nn).setCantidadTemp(0);
                listDetalle.get(nn).setCantidadTempDes(0);
            }
        } else {
            Integer num = listDetalle.size();
            if ((listDetalle.get(nn).getCantidadExistente() < listDetalle.get(nn).getCantidadTempSol())) {
                JsfUtil.addErrorMessage("Valor No Modificado", "Cantidad debe ser menor al Stock");
                listDetalle.get(nn).setCantidadTempSol(0);
                listDetalle.get(nn).setCantidadTemp(0);
            } else {
                if (listDetalle.get(nn).getCantidadTempDes() <= 0) {
                    listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempSol());
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor Modificado", "Valor a cambiado");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }

            if (listDetalle.get(nn).getCantidadTempDes() > 0) {

                if ((listDetalle.get(nn).getCantidadTempDes() >= 1) && (listDetalle.get(nn).getCantidadTempDes() <= listDetalle.get(nn).getCantidadTempSol())) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor Modificado", "Valor a cambiado");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempDes());
                } else {
                    JsfUtil.addErrorMessage("Valor No Modificado", "Cantidad debe ser menor a cantidad solicitada");
                    listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempSol());
                    listDetalle.get(nn).setCantidadTempDes(0);
                }
            }
        }

    }

    public void guardar() {
        if (inventario.getMotivoMovimiento() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar un motivo");
            return;
        }
        if (inventario.getUsuarioAutorizador().getId() == null) {
            JsfUtil.addWarningMessage("Error", "Por favor seleccione al usuario autorizador");
            return;
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("REQUISICIÓN"))) {
            if (listOrdenRequisicion.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Por favor escoja una orden de Requisición");
                return;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("OFICIO"))) {
            if (listOficio.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Por favor escoja un Oficio");
                return;
            }
            if (servidor.getId() == null) {
                JsfUtil.addWarningMessage("Error", "Por favor seleccione un solicitante");
                return;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("ANULACIÓN"))) {
            if (servidor.getPersona().getIdentificacion() == null) {
                JsfUtil.addWarningMessage("Error", "Por favor seleccione un solicitante");
                return;
            }
            if (inventarioIngreso.getCodigo() == null) {
                JsfUtil.addWarningMessage("Error", "Por favor cargue un ingreso para continuar");
                return;
            }
        }
        if ((inventario.getMotivoMovimiento().getTexto().equals("AJUSTE"))) {
            if (servidor.getPersona().getIdentificacion() == null) {
                JsfUtil.addWarningMessage("Error", "Por favor seleccione un solicitante");
                return;
            }
        }
        if (listDetalle.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "Adicione Items para poder guardar");
            return;
        }
        Integer co = listDetalle.size();
        Boolean aBol = Boolean.FALSE;
        Boolean eBol = Boolean.FALSE;
        for (int i = 0; i < co; i++) {
            if ((listDetalle.get(i).getCantidadExistente() < listDetalle.get(i).getCantidadTempSol())) {
                aBol = Boolean.TRUE;
            }
            if ((listDetalle.get(i).getCantidadTempSol() == 0)) {
                eBol = Boolean.TRUE;
            }
        }
        if (aBol) {
            JsfUtil.addWarningMessage("Error", "Cantidad a despachar de Items  debe ser menor al Stock, realice nuevo ingreso o corrija para continuar");
            return;
        }
        if (eBol) {
            JsfUtil.addWarningMessage("Error", "Cantidad solicitada en 0, favor ingrese valores para continar con el egreso");
            return;
        }
        Integer n = listDetalle.size();
        Integer cond = 0;
        try {
            for (DetalleItem detalleItem1 : listDetalle) {
                if (detalleItem1.getCantidadTemp() <= 0) {
                    cond++;
                }
            }
            if (cond != 0) {
                JsfUtil.addWarningMessage("Información", "Valores en cero, favor ingresar cantidad o precio diferente a 0");
            } else {

                if (inventario.getMotivoMovimiento().getTexto().equals("ANULACIÓN")) {
                    inventario.setIngresoEgresoRelacionado(inventarioIngreso);
                }
                if (inventario.getMotivoMovimiento().getTexto().equals("AJUSTE")) {
                    if (constatacionFisicaInventario.getRazon().equals("AMBOS")) {
                        constatacionFisicaInventario.setRazon("INGRESO");
                        constatacionFisicaInventario.setAjustado(Boolean.FALSE);
                    } else {
                        constatacionFisicaInventario.setAjustado(Boolean.TRUE);
                    }
                    cfis.edit(constatacionFisicaInventario);
                }
                inventario.setUsuarioSolicitante(servidor);
                inventario.setUsuarioFinal(servidor);
                Long orden = procesoIngresoService.getOrderInventario("EGRESO");
//              inventario.setAnio(Utils.getAnio(new Date()).shortValue());
                inventario.setOrden(orden);
                String codigoGenerado = Utils.completarCadenaConCeros(inventario.getOrden() + "", 5);
                inventario.setCodigo("EGR-" + codigoGenerado);
                inventario = procesoIngresoService.create(inventario);    // aqui ver cambios
                // REGISTRO PARA GUARDAR EL OFICIO
                if (inventario.getMotivoMovimiento().getTexto().equals("OFICIO")) {
                    inventarioRegistro.setMovimiento(inventario);
                    inventarioRegistro.setEstado(Boolean.TRUE);
                    inventarioRegistro = inventarioRegistroService.create(inventarioRegistro);
                }
                //PARA ACTUALIZAR EXISTENCIA DE ITEM
                Integer t = listDetalle.size();
                for (int j = 0; j < t; j++) {
                    String codigo = listDetalle.get(j).getCodigoAgrupado();  // error corregido: estaba i por la j.
                    DetalleItem item = detalleItemService.findByCodigoList(codigo);
                    Integer canExistente = item.getCantidadExistente();
                    Integer canMenos = listDetalle.get(j).getCantidadTemp();
                    BigDecimal prec = BigDecimal.ZERO;
                    BigDecimal pre = item.getPrecioCalculado();
                    BigDecimal totalExistente = item.getTotalCalculado();
                    BigDecimal total = BigDecimal.ZERO;
                    item.setCantidadExistente(canExistente - canMenos);
                    Integer can = canExistente - canMenos;
                    if (inventario.getIngresoEgresoRelacionado() == null) {
                        total = BigDecimal.valueOf(canMenos * pre.doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                        totalExistente = totalExistente.subtract(total).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                        item.setTotalCalculado(totalExistente);
                        if ((totalExistente.intValue() == 0) && (can == 0)) {
                            item.setPrecioCalculado(BigDecimal.ZERO);
                        } else {
                            item.setPrecioCalculado(BigDecimal.valueOf(totalExistente.doubleValue() / can).setScale(4, BigDecimal.ROUND_HALF_EVEN));
                        }
                    } else {
                        prec = listDetalle.get(j).getPrecioTemp();
                        for (InventarioItems item_1 : inventario.getIngresoEgresoRelacionado().getInventarioItemsList()) {
                            if (item_1.getDetalleItem().getId().equals(item.getId())) {
                                if (item_1.getIva() != null) {
                                    total = BigDecimal.valueOf(canMenos * prec.doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                                    total = BigDecimal.valueOf(total.doubleValue() + (total.doubleValue() * (item_1.getIva().doubleValue() / 100))).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                                } else {
                                    total = BigDecimal.valueOf(canMenos * prec.doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                                }
                            }
                        }
                        totalExistente = totalExistente.subtract(total).setScale(4, BigDecimal.ROUND_HALF_EVEN);
                        item.setTotalCalculado(totalExistente);
                        if ((totalExistente.intValue() == 0) && (can == 0)) {
                            item.setPrecioCalculado(BigDecimal.ZERO);
                        } else {
                            item.setPrecioCalculado(BigDecimal.valueOf(totalExistente.doubleValue() / can).setScale(4, BigDecimal.ROUND_HALF_EVEN));
                        }
                    }
                    detalleItemService.edit(item);
                    //SETEAMOS LA CANTIDAD DESPACHADA DE CADA ITEM DE LA ORDEN DE REQUISICIÓN
                    if ((inventario.getMotivoMovimiento().getTexto().equals("REQUISICIÓN"))) {
                        listOrdenRequisicionItems.get(j).setCantidadDespachada(listDetalle.get(j).getCantidadTemp());
                        ordenRequisicionItemsService.edit(listOrdenRequisicionItems.get(j));

                        //SETEAMOS LA FECHA , ESTADO , USUARIO EN REQUISICIÓN
                        Subject subject = SecurityUtils.getSubject();
                        ordenRequisicion.setEstadoSolicitud("DESPACHADO");
                        ordenRequisicion.setFechaDespachado(inventario.getFechaMovimiento());
                        ordenRequisicion.setServidorDespachador(subject.getPrincipal().toString());
                        ordenRequisicionService.edit(ordenRequisicion);
                    }
                    //GUARDAR ITEM TRANSACCIONADOS EN INVENTARIO ITEMS
                    inventarioItems.setInventario(inventario);
                    inventarioItems.setDetalleItem(item);
                    inventarioItems.setObservacion(listDetalle.get(j).getObservacionTemp());
                    inventarioItems.setCantidad(canMenos);
                    if (inventario.getIngresoEgresoRelacionado() == null) {
                        inventarioItems.setPrecio(pre);
                    } else {
                        inventarioItems.setPrecio(prec);
                    }
                    inventarioItems.setTotal(total);
                    inventarioItems = inventarioItemsService.create(inventarioItems);
                    inventarioItems = new InventarioItems();
                }
                // RECORRRIDO PARA GUARDAR LO DEL ARRAYLIST DE LA REQUISICIÓN EN BASE DE DATOS
                Integer a = listOrdenRequisicion.size();
                for (int j = 0; j < a; j++) {
                    inventarioRegistro.setRequisicion(listOrdenRequisicion.get(j));
                    inventarioRegistroService.edit(inventarioRegistro);
                }
                // RECORRRIDO PARA GUARDAR LO DEL ARRAYLIST QUE CONTIENE EL OFICIO EN BASE DE DATOS
                Integer b = listOficio.size();
                for (int j = 0; j < b; j++) {
                    listOficio.get(j).setEstado(true);
                    oficioService.create(listOficio.get(j));
                    inventarioRegistro.setOficio(listOficio.get(j));
                    inventarioRegistroService.edit(inventarioRegistro);
                }
                this.impresion = Utils.clone(this.inventario);
                habilitarBtn = true;
                mostrar2 = true;
                bolImprimir = Boolean.FALSE;
                bolImprimir = Boolean.FALSE;
                //COMPLETADO SEGUIDO LIMPIAMOS E INICIALIZAMOS TODO
                JsfUtil.addSuccessMessage("Información", "Egreso N° " + inventario.getCodigo() + " se ha registrado con Éxito");
            }
        } catch (IndexOutOfBoundsException e) {
        }

    }

    public void mostrarBtn() {
        try {
            switch (inventario.getMotivoMovimiento().getTexto()) {
                case "REQUISICIÓN":
                    habilitar2 = Boolean.TRUE;
                    mostrarReq = Boolean.TRUE;
                    mostrarOfi = Boolean.FALSE;
                    mostrarIng = Boolean.FALSE;
                    habilitarBtnItem = Boolean.TRUE;
                    habilitarBtnSol = Boolean.TRUE;
                    rendCons = Boolean.FALSE;
                    break;
                case "OFICIO":
                    mostrarReq = Boolean.FALSE;
                    mostrarOfi = Boolean.TRUE;
                    mostrarIng = Boolean.FALSE;
                    mostrar2 = Boolean.FALSE;
                    habilitarBtnItem = Boolean.FALSE;
                    habilitarBtnSol = Boolean.FALSE;
                    rendCons = Boolean.FALSE;
                    break;
                case "ANULACIÓN":
                    mostrarReq = Boolean.FALSE;
                    mostrarOfi = Boolean.FALSE;
                    mostrarIng = Boolean.TRUE;
                    mostrar2 = Boolean.TRUE;
                    habilitarBtnItem = Boolean.TRUE;
                    habilitarBtnSol = Boolean.FALSE;
                    rendCons = Boolean.FALSE;
                    break;
                case "AJUSTE":
                    mostrarReq = Boolean.FALSE;
                    mostrarOfi = Boolean.FALSE;
                    mostrarIng = Boolean.FALSE;
                    mostrar2 = Boolean.TRUE;
                    habilitarBtnItem = Boolean.TRUE;
                    habilitarBtnSol = Boolean.FALSE;
                    rendCons = Boolean.TRUE;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void limpiarVariableOficio() {
        nuevo = Boolean.FALSE;
        oficio = new Oficio();
    }

    public void guardarOficio() {
        this.listOficio.add(this.oficio);
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
    }

    public void editarOficio() {
        listOficio = new ArrayList<>();
        this.listOficio.add(this.oficio);
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
    }

    public void imprimirEgreso() {
        ss.addParametro("egr_id", impresion.getId());
        ss.setNombreReporte("egresoInventario");
        ss.setNombreSubCarpeta("activos");
        BigDecimal d = inventarioItemsService.getValorAcumulado(impresion);
        String num = numeroLetra.convertir(d.setScale(2, java.math.RoundingMode.HALF_UP) + "", true);
        ss.addParametro("acumulado_lt", num);
        limpiarTodo();
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

//<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
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

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

//    public UnidadAdministrativa getUnidadAdministrativa() {
//        return unidadAdministrativa;
//    }
//
//    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
//        this.unidadAdministrativa = unidadAdministrativa;
//    }
//
//    public Boolean getHabilitar() {
//        return habilitar;
//    }
//
//    public void setHabilitar(Boolean habilitar) {
//        this.habilitar = habilitar;
//    }
    public Inventario getInventarioIngreso() {
        return inventarioIngreso;
    }

    public void setInventarioIngreso(Inventario inventarioIngreso) {
        this.inventarioIngreso = inventarioIngreso;
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public DetalleItem getDetaleItem() {
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

    public InventarioItems getInventarioItems() {
        return inventarioItems;
    }

    public void setInventarioItems(InventarioItems inventarioItems) {
        this.inventarioItems = inventarioItems;
    }

    public ArrayList<Oficio> getListOficio() {
        return listOficio;
    }

    public void setListOficio(ArrayList<Oficio> listOficio) {
        this.listOficio = listOficio;
    }

    public Oficio getOficio() {
        return oficio;
    }

    public void setOficio(Oficio oficio) {
        this.oficio = oficio;
    }

    public List<InventarioItems> getItemDevueltos() {
        return itemDevueltos;
    }

    public List<ConstatacionFisicaInventario> getListCons() {
        return listCons;
    }

    public void setListCons(List<ConstatacionFisicaInventario> listCons) {
        this.listCons = listCons;
    }

    public List<DetalleConstFisicaInventario> getListConsItem() {
        return listConsItem;
    }

    public void setListConsItem(List<DetalleConstFisicaInventario> listConsItem) {
        this.listConsItem = listConsItem;
    }

    public void setItemDevueltos(List<InventarioItems> itemDevueltos) {
        this.itemDevueltos = itemDevueltos;
    }

    public ArrayList<OrdenRequisicion> getListOrdenRequisicion() {
        return listOrdenRequisicion;
    }

    public void setListOrdenRequisicion(ArrayList<OrdenRequisicion> listOrdenRequisicion) {
        this.listOrdenRequisicion = listOrdenRequisicion;
    }

    public OrdenRequisicion getOrdenRequisicion() {
        return ordenRequisicion;
    }

    public void setOrdenRequisicion(OrdenRequisicion ordenRequisicion) {
        this.ordenRequisicion = ordenRequisicion;
    }

    public List<OrdenRequisicionItems> getListOrdenRequisicionItems() {
        return listOrdenRequisicionItems;
    }

    public void setListOrdenRequisicionItems(List<OrdenRequisicionItems> listOrdenRequisicionItems) {
        this.listOrdenRequisicionItems = listOrdenRequisicionItems;
    }

    public OrdenRequisicionItems getOrdenRequisicionItems() {
        return ordenRequisicionItems;
    }

    public void setOrdenRequisicionItems(OrdenRequisicionItems ordenRequisicionItems) {
        this.ordenRequisicionItems = ordenRequisicionItems;
    }

    public Boolean getHabilitar1() {
        return habilitar1;
    }

    public void setHabilitar1(Boolean habilitar1) {
        this.habilitar1 = habilitar1;
    }

    public Boolean getHabilitar2() {
        return habilitar2;
    }

    public void setHabilitar2(Boolean habilitar2) {
        this.habilitar2 = habilitar2;
    }

    public Boolean getMostrarIng() {
        return mostrarIng;
    }

    public void setMostrarIng(Boolean mostrarIng) {
        this.mostrarIng = mostrarIng;
    }

    public Boolean getMostrarOfi() {
        return mostrarOfi;
    }

    public void setMostrarOfi(Boolean mostrarOfi) {
        this.mostrarOfi = mostrarOfi;
    }

    public Boolean getMostrarReq() {
        return mostrarReq;
    }

    public void setMostrarReq(Boolean mostrarReq) {
        this.mostrarReq = mostrarReq;
    }

    public Boolean getHabilitarBtn() {
        return habilitarBtn;
    }

    public void setHabilitarBtn(Boolean habilitarBtn) {
        this.habilitarBtn = habilitarBtn;
    }

    public Boolean getHabilitarBtnItem() {
        return habilitarBtnItem;
    }

    public void setHabilitarBtnItem(Boolean habilitarBtnItem) {
        this.habilitarBtnItem = habilitarBtnItem;
    }

    public Boolean getHabilitarBtnSol() {
        return habilitarBtnSol;
    }

//    public Boolean getUnidad() {
//        return unidad;
//    }
//
//    public void setUnidad(Boolean unidad) {
//        this.unidad = unidad;
//    }
//    public Boolean getDepartam() {
//        return departam;
//    }
//
//    public void setDepartam(Boolean departam) {
//        this.departam = departam;
//    }
    public void setHabilitarBtnSol(Boolean habilitarBtnSol) {
        this.habilitarBtnSol = habilitarBtnSol;
    }

    public Boolean getRendCons() {
        return rendCons;
    }

    public void setRendCons(Boolean rendCons) {
        this.rendCons = rendCons;
    }

    public InventarioRegistro getInventarioRegistro() {
        return inventarioRegistro;
    }

    public ConstatacionFisicaInventario getConstatacionFisicaInventario() {
        return constatacionFisicaInventario;
    }

    public void setConstatacionFisicaInventario(ConstatacionFisicaInventario constatacionFisicaInventario) {
        this.constatacionFisicaInventario = constatacionFisicaInventario;
    }

    public void setInventarioRegistro(InventarioRegistro inventarioRegistro) {
        this.inventarioRegistro = inventarioRegistro;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
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

    public Boolean getBolImprimir() {
        return bolImprimir;
    }

    public void setBolImprimir(Boolean bolImprimir) {
        this.bolImprimir = bolImprimir;
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

    public Inventario getImpresion() {
        return impresion;
    }

    public Boolean getMostrar2() {
        return mostrar2;
    }

    public void setMostrar2(Boolean mostrar2) {
        this.mostrar2 = mostrar2;
    }

    public void setImpresion(Inventario impresion) {
        this.impresion = impresion;
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

    public ThServidorCargo getCargoSelectData() {
        return cargoSelectData;
    }

    public void setCargoSelectData(ThServidorCargo cargoSelectData) {
        this.cargoSelectData = cargoSelectData;
    }

    public ThServidorCargo getCargoAutorizadorSelectData() {
        return cargoAutorizadorSelectData;
    }

    public void setCargoAutorizadorSelectData(ThServidorCargo cargoAutorizadorSelectData) {
        this.cargoAutorizadorSelectData = cargoAutorizadorSelectData;
    }
//</editor-fold>

}
