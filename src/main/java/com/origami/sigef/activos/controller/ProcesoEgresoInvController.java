/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.activos.service.DetalleConstFisicaService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.activos.service.InventarioRegistroService;
import com.origami.sigef.activos.service.OficioService;
import com.origami.sigef.activos.service.OrdenRequisicionItemsService;
import com.origami.sigef.activos.service.OrdenRequisicionService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
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
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
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
@Named(value = "procesoEgresoInvController")
@ViewScoped
public class ProcesoEgresoInvController extends BpmnBaseRoot implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES">
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
    private UnidadAdministrativa unidadAdministrativa;
    private ConstatacionFisicaInventario constatacionFisicaInventario;
    private List<ConstatacionFisicaInventario> listCons;
    private List<DetalleConstFisicaInventario> listConsItem; //lista que trae todos los item de la constatacion

    private Boolean habilitar = Boolean.TRUE;
    private Boolean bandera = Boolean.TRUE;
    private Boolean habilitar1 = Boolean.TRUE;
    private Boolean habilitarBtn = Boolean.FALSE;
    private Boolean habilitarBtnG = Boolean.FALSE;
    private Boolean habilitarBtnA = Boolean.TRUE;
    private Boolean habilitarBtnItem = Boolean.TRUE;
    private Boolean habilitarBtnSol = Boolean.TRUE;
    private Boolean habilitar2 = Boolean.FALSE;
    private Boolean mostrarIng = Boolean.FALSE;
    private Boolean mostrarOfi = Boolean.TRUE;
    private Boolean mostrarReq = Boolean.TRUE;
    private Boolean nuevo = Boolean.FALSE;
    private Boolean rendCons = Boolean.FALSE;

    private Boolean botonAprobaciones = Boolean.TRUE;
    private Boolean botonRechazado = Boolean.TRUE;
    private Boolean botonCompletartarea = Boolean.TRUE;
    private String observaciones;
    private Inventario inventarioOpcional;
    private Boolean botonProcesos = Boolean.TRUE;
    private boolean botonImprimir = Boolean.TRUE;
    private Boolean unidad = Boolean.TRUE;
    private Boolean departam = Boolean.TRUE;

    @Inject
    private KatalinaService katalinaService;
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
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
//</editor-fold>

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                System.out.println("// getTaskID " + this.session.getTaskID());
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                Long tramiteNumero = tramite.getNumTramite();
                System.out.println("tramite: " + tramiteNumero);
                OrdenRequisicion ordenR = ordenRequisicionService.findOrdenrByTramite(tramiteNumero);
                Inventario inv = procesoIngresoService.findInventarioByTramite(tramiteNumero);
                System.out.println("INVENTARIO: " + inv);
                this.impresion = new Inventario();
                this.impresion = Utils.clone(inv);
                if (inv != null) {
                    habilitarBtnG = Boolean.TRUE;
                    habilitarBtnA = Boolean.FALSE;
                    inventario = inv;
                } else {
                    Subject subject = SecurityUtils.getSubject();
                    inventario = new Inventario();
                    Long orden = procesoIngresoService.getOrderInventario("EGRESO");
                    inventario.setAnio(Utils.getAnio(new Date()).shortValue());
                    inventario.setOrden(orden);
                    inventario.setEstado(Boolean.TRUE);
                    inventario.setUsuarioOriginador(subject.getPrincipal().toString());
                    inventario.setTipoMovimiento("EGRESO");
                    inventario.setFechaMovimiento(new Date());
                    motivoMovimiento = catalogoMovimientoService.findMovimientoIngresoInventario("SALINV", "FLU-PROC");
                    inventario.setMotivoMovimiento(motivoMovimiento.get(0));
                }
                this.catalogoMovimiento = new CatalogoMovimiento();
                detalleItem = new DetalleItem();
                listDetalle = new ArrayList<>();
                inventarioIngreso = new Inventario();
                constatacionFisicaInventario = new ConstatacionFisicaInventario();
                inventarioRegistro = new InventarioRegistro();
                ordenRequisicion = new OrdenRequisicion();
                inventarioItems = new InventarioItems();
                servidor = new Servidor();
                servidor.setPersona(new Cliente());
                unidadAdministrativa = new UnidadAdministrativa();
                oficio = new Oficio();
                listOficio = new ArrayList<>();
                listCons = new ArrayList<>();
                listConsItem = new ArrayList<>();
                listOrdenRequisicion = new ArrayList<>();
                inventarioOpcional = new Inventario();
                selectDatosRequisicion(ordenR);
                PrimeFaces.current().ajax().update("frmMain");
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }
    // CARGAR SERVIDOR

    public void buscarServidor() {
        habilitar = Boolean.TRUE;
        System.out.println("Servidor CI: " + servidor.getPersona().getIdentificacion());
        if (servidor.getPersona().getIdentificacion() != null) {
            Servidor serv = procesoIngresoService.findByIdentificacionServ(servidor.getPersona().getIdentificacion());
            System.out.println("servidor " + serv);
            System.out.println("entro : " + habilitar);
            if (serv != null) {
                this.servidor = serv;
                this.unidadAdministrativa = serv.getDistributivo().getUnidadAdministrativa().getPadre().getPadre();
                if (unidadAdministrativa == null) {
                    unidad = Boolean.FALSE;
                    habilitar = Boolean.FALSE;
                    System.out.println("entro : " + habilitar);
                } else {
                    unidad = Boolean.TRUE;
                    habilitar = Boolean.TRUE;
                }
                setearUnidadAdministrativa();
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogServidor", null);
        }
    }

    public void setearUnidadAdministrativa() {
        if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("UN")) {
            inventario.setUnidad(servidor.getDistributivo().getUnidadAdministrativa().getNombre());
            inventario.setDepartamento(servidor.getDistributivo().getUnidadAdministrativa().getPadre().getNombre());
            UnidadAdministrativa unidads = ordenRequisicionService.findByPadreGrupo(servidor.getDistributivo().getUnidadAdministrativa().getPadre());
            inventario.setDireccion(unidads.getNombre());
            departam = Boolean.TRUE;
            if (unidadAdministrativa == null) {
                unidadAdministrativa = unidads;
                unidad = Boolean.TRUE;
            }
        } else if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("DE")) {
            inventario.setUnidad(null);
            inventario.setDepartamento(servidor.getDistributivo().getUnidadAdministrativa().getNombre());
            inventario.setDireccion(servidor.getDistributivo().getUnidadAdministrativa().getPadre().getNombre());
            departam = Boolean.TRUE;
            if (unidadAdministrativa == null) {
                unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre();
                unidad = Boolean.TRUE;
            }
        } else if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("DIR")) {
            inventario.setUnidad(null);
            inventario.setDepartamento(null);
            inventario.setDireccion(servidor.getDistributivo().getUnidadAdministrativa().getNombre());
            if (servidor.getDistributivo().getUnidadAdministrativa().getPadre() == null) {
                unidadAdministrativa = null;
                unidad = Boolean.FALSE;
                departam = Boolean.FALSE;
            } else if (servidor.getDistributivo().getUnidadAdministrativa().getPadre().getPadre() == null) {
                unidadAdministrativa = null;
                unidad = Boolean.FALSE;
                departam = Boolean.TRUE;
            } else if (unidadAdministrativa == null) {
                unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa();
                unidad = Boolean.TRUE;
            }

        }
        System.out.println("Direccion: " + inventario.getDireccion());
        System.out.println("Departamento: " + inventario.getDepartamento());
        System.out.println("Unidad: " + inventario.getUnidad());
    }

    public void selectDataServidor(SelectEvent evt) {
        servidor = ((Servidor) evt.getObject());
        try {
            unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre().getPadre();
            if (unidadAdministrativa == null) {
                habilitar = Boolean.FALSE;
            } else {
                habilitar = Boolean.TRUE;
            }
        } catch (Exception e) {
            habilitar = Boolean.FALSE;
        }
    }

    public void limpiarTodo() {
        this.impresion = new Inventario();
        this.impresion = Utils.clone(this.inventario);
        bandera = Boolean.TRUE;
        habilitar = Boolean.TRUE;
        habilitar1 = Boolean.TRUE;
        habilitar2 = Boolean.FALSE;
        mostrarIng = Boolean.FALSE;
        mostrarOfi = Boolean.TRUE;
        mostrarReq = Boolean.TRUE;
        habilitarBtn = Boolean.TRUE;
        habilitarBtnG = Boolean.TRUE;
        habilitarBtnA = Boolean.FALSE;
        habilitarBtnItem = Boolean.TRUE;
        habilitarBtnSol = Boolean.TRUE;
        rendCons = Boolean.FALSE;
        inventario = new Inventario();
        inventarioIngreso = new Inventario();
        constatacionFisicaInventario = new ConstatacionFisicaInventario();
        inventarioRegistro = new InventarioRegistro();
        ordenRequisicion = new OrdenRequisicion();
        listDetalle = new ArrayList<>();
        detalleItem = new DetalleItem();
        servidor = new Servidor();
        servidor.setPersona(new Cliente());
        unidadAdministrativa = new UnidadAdministrativa();
        oficio = new Oficio();
        listOficio = new ArrayList<>();
        listOrdenRequisicion = new ArrayList<>();
        listCons = new ArrayList<>();
        listConsItem = new ArrayList<>();
    }

    //CONSTATACION FISICA
    public void abrirDialogCons() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogConstatacion", null);
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
                System.out.println("multiplicacion por 1:" + porMenosUno);
                detalleItem.setCantidadTemp(porMenosUno);
                detalleItem.setCantidadTempSol(porMenosUno);
                detalleItem.setPrecioTemp(listConsItem.get(w).getDetalleItem().getPrecioCalculado());
                detalleItem.setTotalTemp(listConsItem.get(w).getDetalleItem().getTotalCalculado());
                this.listDetalle.add(this.detalleItem);
                detalleItem = new DetalleItem();
            }
        }
        PrimeFaces.current().ajax().update("frmMain");
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
            inventario = new Inventario();
            Long orden = procesoIngresoService.getOrderInventario("EGRESO");
            inventario.setAnio(Utils.getAnio(new Date()).shortValue());
            inventario.setOrden(orden);
            String codigoGenerado = Utils.completarCadenaConCeros(inventario.getOrden() + "", 5);
            inventario.setCodigo("EGR-" + codigoGenerado);
            inventario.setEstado(Boolean.TRUE);
            inventario.setUsuarioOriginador(subject.getPrincipal().toString());
            inventario.setTipoMovimiento("EGRESO");
            inventario.setFechaMovimiento(new Date());
            bandera = Boolean.FALSE;
        } else {
            habilitarBtnItem = Boolean.FALSE;
            habilitarBtnItem = Boolean.FALSE;
            habilitarBtn = Boolean.FALSE;
            habilitar = Boolean.FALSE;
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
            detalleItem.setDescripcion(itemDevueltos.get(i).getDetalleItem().getDescripcion());
            detalleItem.setTipoMedida(itemDevueltos.get(i).getDetalleItem().getTipoMedida());
            detalleItem.setCantidadExistente(itemDevueltos.get(i).getDetalleItem().getCantidadExistente());
            detalleItem.setCantidadTempSol(itemDevueltos.get(i).getCantidad());
            detalleItem.setCantidadTempDes(0);
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
        PrimeFaces.current().ajax().update("frmMain");
    }

    //TRAER OFICIO
    public void abrirDialogOficio() {
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogOficio", null);
    }

    public void selectDatosOficio(SelectEvent evt) {
        oficio = (Oficio) evt.getObject();
        this.listOficio.add(this.oficio);
        System.out.println("OFICIO GUARDADO");
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

    public void selectDatosRequisicion(OrdenRequisicion ordR) {
        ordenRequisicion = ordR;
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
            detalleItem.setCantidadTemp(listOrdenRequisicionItems.get(i).getCantidadSolicitada());
            this.listDetalle.add(detalleItem);
            detalleItem = new DetalleItem();
        }
        //SETEAMOS SOLICITANTE CON SI DIRECCION, DEPARTAMENTO Y UNIDAD
        servidor = ordenRequisicion.getServidorSolicitante();
        try {
            unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre().getPadre();
            if (unidadAdministrativa == null) {
                habilitar = Boolean.FALSE;
            } else {
                habilitar = Boolean.TRUE;
            }
        } catch (Exception e) {
            habilitar = Boolean.FALSE;
        }

        setearUnidadAdministrativa();

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
        this.listDetalle.add(this.detalleItem);
        System.out.println(listDetalle);
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
//            for (int i = 0; i < num; i++) {
            if (listDetalle.get(nn).getCantidadTempDes() > 0) {
                if ((listDetalle.get(nn).getCantidadTempDes() >= 1) && (listDetalle.get(nn).getCantidadTempDes() <= listDetalle.get(nn).getCantidadTempSol())) {
                    listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempDes());
                } else {
                    JsfUtil.addErrorMessage("Valor No Modificado", "Cantidad a despachar es mayor a solicitada");
                    listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempSol());
                    listDetalle.get(nn).setCantidadTempDes(0);
                }
            } else if (listDetalle.get(nn).getCantidadTempDes() <= 0) {
                listDetalle.get(nn).setCantidadTempDes(0);
                listDetalle.get(nn).setCantidadTemp(listDetalle.get(nn).getCantidadTempSol());
            }
//            }
        } else {
            Integer num = listDetalle.size();
            System.out.println("numero de lista: " + listDetalle.size());
//            for (int i = 0; i < num; i++) {

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
//            }
        }

    }

    public void guardar() {
        if (inventario.getMotivoMovimiento() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar un motivo");
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
            if (servidor.getPersona().getIdentificacion() == null) {
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
        for (int i = 0; i < co; i++) {
            if ((listDetalle.get(i).getCantidadExistente() < listDetalle.get(i).getCantidadTempSol())) {
                aBol = Boolean.TRUE;
            }
        }
        if (aBol) {
            JsfUtil.addWarningMessage("Error", "Cantidad a despachar de Items  debe ser menor al Stock, realice nuevo ingreso o corrija para continuar");
            return;
        }

        Integer n = listDetalle.size();
        Integer cond = 0;
        try {
            for (int i = 0; i < n; i++) {
                if (listDetalle.get(i).getCantidadTemp() <= 0) {
                    cond++;
                }
                if (cond != 0) {
                    JsfUtil.addWarningMessage("Información", "Valores en cero, favor ingresar cantidad o precio diferente a 0");
                } else {

                    if (inventario.getMotivoMovimiento().getTexto().equals("ANULACIÓN")) {
                        inventario.setIngresoEgresoRelacionado(inventarioIngreso);
                    }
                    inventario.setUsuarioSolicitante(servidor);
                    inventario.setUsuarioFinal(servidor);
                    inventario.setNumeroTramite(tramite.getNumTramite());
                    inventario.setEstadoAdicional("COMPLETO");
                    String codigoGenerado = Utils.completarCadenaConCeros(inventario.getOrden() + "", 5);
                    inventario.setCodigo("EGR-" + codigoGenerado);
                    procesoIngresoService.create(inventario);    // aqui ver cambios
                    inventarioOpcional = new Inventario();
                    inventarioOpcional = Utils.clone(inventario);

                    // REGISTRO PARA GUARDAR EL MOVIMIENTO CON SU REQUISICIÓN U OFICIO
                    inventarioRegistro.setMovimiento(inventario);
                    inventarioRegistro.setEstado(Boolean.TRUE);
                    inventarioRegistroService.create(inventarioRegistro);

                    //PARA ACTUALIZAR EXISTENCIA DE ITEM
                    Integer t = listDetalle.size();
                    for (int j = 0; j < t; j++) {
                        String codigo = listDetalle.get(j).getCodigo();  // error corregido: estaba i por la j.
//                        DetalleItem item = detalleItemService.findByCodigoList(codigo, inventario.getAnio());
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
                            total = BigDecimal.valueOf(canMenos * prec.doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
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
                        inventarioItemsService.create(inventarioItems);
                        System.out.println("Item transacionados guardados en tabla");
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
                    limpiarTodo();
                    //COMPLETADO SEGUIDO LIMPIAMOS E INICIALIZAMOS TODO
                    JsfUtil.addSuccessMessage("Información", "Egreso exitoso");
                }
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
                    habilitarBtnItem = Boolean.FALSE;
                    habilitarBtnSol = Boolean.FALSE;
                    rendCons = Boolean.FALSE;
                    break;
                case "ANULACIÓN":
                    mostrarReq = Boolean.FALSE;
                    mostrarOfi = Boolean.FALSE;
                    mostrarIng = Boolean.TRUE;
                    habilitarBtnItem = Boolean.TRUE;
                    habilitarBtnSol = Boolean.FALSE;
                    rendCons = Boolean.FALSE;
                    break;
                case "AJUSTE":
                    mostrarReq = Boolean.FALSE;
                    mostrarOfi = Boolean.FALSE;
                    mostrarIng = Boolean.FALSE;
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
        System.out.println("OFICIO GUARDADO");
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
    }

    public void editarOficio() {
        listOficio = new ArrayList<>();
        this.listOficio.add(this.oficio);
        System.out.println("OFICIO EDITADO");
        PrimeFaces.current().executeScript("PF('dlgOficio').hide();");
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
//            List<InventarioItems> invI = inventarioItemsService.getItemByInventarioItems(inventarioOpcional);
//            List<DetalleItem> listDet;
//            listDet = new ArrayList<>();
//            Integer z = invI.size();
//            for (int i = 0; i < z; i++) {
//                listDet.add(invI.get(i).getDetalleItem());
//            }
//            Integer t = listDet.size();
//            Integer ii = 0;
//            rechazarItems(ii, t, listDet, invI);
//            System.out.println("rechazado " + inventarioOpcional.getEstadoAdicional());
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

//        if(inventario.toString().length()>0){
//
//        System.out.println(inventario.toString());
//
//        }
//        else{
        System.out.println(inventario.toString());
        if (inventario == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No se ha registrado nada");

        } else {

            this.inventario.setEstadoAdicional("COMPLETO");

            procesoIngresoService.edit(inventario);
            botonCompletartarea = Boolean.TRUE;
            botonProcesos = Boolean.FALSE;
        }

    }

    public void aprobacionPrcoeso(int aprobado) {

        Inventario itemData = new Inventario();
        itemData = procesoIngresoService.getComprobarEstado(tramite.getNumTramite());

        if (itemData != null || "COMPLETO".equals(itemData.getEstadoAdicional())) {

            if (aprobado == 1) {
                this.botonImprimir = Boolean.FALSE;
                this.habilitar = Boolean.TRUE;
                this.botonProcesos = Boolean.TRUE;

            }

            observacion.setObservacion(observaciones);
            try {

                getParamts().put("aprobado", aprobado);
                getParamts().put("usuario", clienteService.getUSerLogeo("GUARDALMACÉN"));

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

        } else {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("!", "Debe dare clic al boton contiuar y terminar Tarea");
            return;

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
                + "<p style=\"width:200px;\"> Se le notifica que el trámite De orden de reuqisición ha sido generado\"\n"
                + "                    + \". Solicitamos se acerque a legalizar dicha acta. </p>\n"
                + "</body>\n"
                + "</html>");// codigo html
        katalinaService.enviarCorreo(c);
    }

    public void imprimir() {

        Inventario inv = procesoIngresoService.getComprobarEstado(tramite.getNumTramite());
        if (inv == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Realice algún registro para imprimir");
            return;
        }

        try {
            System.out.println("lista" + listDetalle.size());
//            System.out.println("Inv "+inventario.getUsuarioOriginador());
            Correo c = new Correo();
//            Cliente emailOriginador = procesoIngresoService.getEmailGuardalmacen(inventario.getUsuarioOriginador());
            Cliente emailOrigin = procesoIngresoService.getEmailGuardalmacen(clienteService.getrolsUser(RolUsuario.guardaAlmacen));
            ss.addParametro("egr_id", impresion.getId());
            ss.setNombreReporte("egresoInventario");
            ss.setNombreSubCarpeta("activos");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
//            System.out.println("COrig " + emailOriginador);
//            System.out.println("COrig Nombre " + emailOriginador.getNombreCompleto());
//            System.out.println("COrig Email" + emailOriginador.getEmail());
//            System.out.println("Cliente " + emailOrigin);
//            System.out.println("Cliente Nombre " + emailOrigin.getNombreCompleto());
//            System.out.println("Cliente Email" + emailOrigin.getEmail());
            if (emailOrigin != null) {
                System.out.println("Entro");
                enviarCorreo(emailOrigin.getEmail(), tramite.getTipoTramite().getDescripcion().toUpperCase(), emailOrigin.getNombreCompleto());
            }
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/consultaInventario");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error " + e);
        }
    }

    public void setPeriodoSeleccionado() {
        inventario.setAnio(Utils.getAnio(inventario.getFechaMovimiento()).shortValue());
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

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }

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

    public Boolean getBotonCompletartarea() {
        return botonCompletartarea;
    }

    public void setBotonCompletartarea(Boolean botonCompletartarea) {
        this.botonCompletartarea = botonCompletartarea;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Boolean getUnidad() {
        return unidad;
    }

    public void setUnidad(Boolean unidad) {
        this.unidad = unidad;
    }

    public Boolean getDepartam() {
        return departam;
    }

    public void setDepartam(Boolean departam) {
        this.departam = departam;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Inventario getInventarioOpcional() {
        return inventarioOpcional;
    }

    public Boolean getHabilitarBtnG() {
        return habilitarBtnG;
    }

    public Inventario getImpresion() {
        return impresion;
    }

    public void setImpresion(Inventario impresion) {
        this.impresion = impresion;
    }

    public void setHabilitarBtnG(Boolean habilitarBtnG) {
        this.habilitarBtnG = habilitarBtnG;
    }

    public Boolean getHabilitarBtnA() {
        return habilitarBtnA;
    }

    public void setHabilitarBtnA(Boolean habilitarBtnA) {
        this.habilitarBtnA = habilitarBtnA;
    }

    public void setInventarioOpcional(Inventario inventarioOpcional) {
        this.inventarioOpcional = inventarioOpcional;
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
//</editor-fold>

}
