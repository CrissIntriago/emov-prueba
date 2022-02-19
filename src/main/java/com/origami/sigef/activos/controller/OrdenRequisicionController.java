
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.OrdenRequisicionItemsService;
import com.origami.sigef.activos.service.OrdenRequisicionService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.OrdenRequisicion;
import com.origami.sigef.common.entities.OrdenRequisicionItems;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "ordenRequisicionView")
@ViewScoped
public class OrdenRequisicionController extends BpmnBaseRoot implements Serializable {

    private OrdenRequisicion ordenRequisicion;
    private OrdenRequisicionItems ordenRequisicionItems;
    private Servidor servidor;
    private DetalleItem detalleItem;
    private UnidadAdministrativa unidadAdministrativa;
    private UnidadAdministrativa direccion;
    private UnidadAdministrativa departamento;
    private UnidadAdministrativa unidadEnt;

    private List<DetalleItem> listDetalleItem;
    private List<OrdenRequisicionItems> listOrdenReqItems;
    private List<PlanAnualProgramaProyecto> listPlanAnualProgramaProyecto;
//    private Integer cont = 0;
    private Boolean nuevo = Boolean.FALSE;
    private Boolean bandera = Boolean.TRUE;
    private Boolean habilitar = Boolean.TRUE;
    private Boolean buscarServ = Boolean.FALSE;
    private Boolean unidad = Boolean.TRUE;
    private Boolean departam = Boolean.TRUE;
    private Boolean botonCompletarTarea = Boolean.TRUE;
    private Boolean botonGuardar = Boolean.TRUE;
    private Boolean botonCancelar = Boolean.FALSE;
    private Boolean adicionarItemB = Boolean.FALSE;
    private Boolean procesoTramBol = Boolean.FALSE;
    private Boolean botonNew = Boolean.FALSE;
    private LazyModel<OrdenRequisicion> lazyOrdenRequisicion;
    private String descripcionObservacion;
    private OrdenRequisicion ordenGlobal;

    @Inject
    private OrdenRequisicionService ordenReqService;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private OrdenRequisicionItemsService ordenReqItemsService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;

    private static final Logger LOG = Logger.getLogger(OrdenRequisicionController.class.getName());

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                Long tramiteNumero = tramite.getNumTramite();
                OrdenRequisicion ord = ordenReqService.findRequisicionBienesByTramite(tramiteNumero);
                if (ord != null) {
                    ordenRequisicion = ord;
                    servidor = ordenRequisicion.getServidorSolicitante();
                    listOrdenReqItems = ordenReqItemsService.getItemsByOrden(ordenRequisicion);
                    listDetalleItem = ordenReqItemsService.getListItemsByOrden(ordenRequisicion);
                    for (OrdenRequisicionItems listOrdenReqItem : listOrdenReqItems) {
                        for (DetalleItem detalleItem1 : listDetalleItem) {
                            if (detalleItem1.getCodigo().equals(listOrdenReqItem.getItem().getCodigo())) {
                                detalleItem1.setCantidadTemp(listOrdenReqItem.getCantidadSolicitada());
                            }
                        }
                    }
                    setearProcesoUnidadAdministrativa();
                    procesoTramBol = Boolean.TRUE;
                    botonNew = Boolean.TRUE;
                    adicionarItemB = Boolean.TRUE;
                    buscarServ = Boolean.TRUE;
                    botonCancelar = Boolean.TRUE;
                    this.botonCompletarTarea = Boolean.FALSE;
                    if (ordenRequisicion.getEstadoOrden().equals("COMPLETO")) {
                        botonCompletarTarea = Boolean.TRUE;
                    }
                    ordenGlobal = Utils.clone(ordenRequisicion);
                } else {
                    ordenRequisicion = new OrdenRequisicion();
                    ordenGlobal = new OrdenRequisicion();
                    ordenRequisicionItems = new OrdenRequisicionItems();
                    direccion = new UnidadAdministrativa();
                    departamento = new UnidadAdministrativa();
                    unidadEnt = new UnidadAdministrativa();
                    listDetalleItem = new ArrayList<>();
                    ordenRequisicion.setAnio(Utils.getAnio(new Date()).shortValue());
                    ordenRequisicion.setFechaEmision(new Date());
                    listPlanAnualProgramaProyecto = ordenReqService.getProyecto(ordenRequisicion.getAnio());
                    detalleItem = new DetalleItem();
                    botonNew = Boolean.FALSE;
                    botonGuardar = Boolean.FALSE;
                    botonCompletarTarea = Boolean.TRUE;
                    botonCancelar = Boolean.TRUE;
                    buscarServ = Boolean.TRUE;
                    adicionarItemB = Boolean.TRUE;
                    departam = Boolean.TRUE;
                    procesoTramBol = Boolean.FALSE;
                    newOrden();
                    servidor = clienteService.getUsuarioServidor(userSession.getNameUser()).getFuncionario();
                    setearProcesoUnidadAdministrativa();
                }
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {

            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void setearProcesoUnidadAdministrativa() {
        if (servidor.getDistributivo().getUnidadAdministrativa() != null) {
            if (servidor.getDistributivo().getUnidadAdministrativa().getPadre() != null) {
                if (servidor.getDistributivo().getUnidadAdministrativa().getPadre().getPadre() != null) {
                    this.unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre().getPadre();
                } else {
                    this.unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre();
                }
            } else {
                this.unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa();
            }
        }
        if (unidadAdministrativa == null) {
            unidad = Boolean.FALSE;
        } else {
            unidad = Boolean.TRUE;
        }
        if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("UN")) {
            UnidadAdministrativa unidads = ordenReqService.findByPadreGrupo(servidor.getDistributivo().getUnidadAdministrativa().getPadre());
            departam = Boolean.TRUE;
            if (unidadAdministrativa == null) {
                unidadAdministrativa = unidads;
                unidad = Boolean.TRUE;
            }
        } else if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("DE")) {
            departam = Boolean.TRUE;
            if (unidadAdministrativa == null) {
                unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre();
                unidad = Boolean.TRUE;
            }
        } else if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("DIR")) {
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
    }

    public void newOrden() {
        if (bandera) {
            unidad = Boolean.TRUE;
            habilitar = Boolean.FALSE;
            buscarServ = Boolean.FALSE;
            adicionarItemB = Boolean.FALSE;
            departam = Boolean.TRUE;
            botonCancelar = Boolean.FALSE;
            botonNew = Boolean.TRUE;
            ordenRequisicion = new OrdenRequisicion();
            ordenRequisicion.setAnio(Utils.getAnio(new Date()).shortValue());
            ordenRequisicion.setFechaEmision(new Date());
            Long ordenn = ordenReqService.getNivelOrdenRequisicion();
            ordenRequisicion.setOrden(ordenn);
            ordenRequisicion.setEstadoSolicitud("EN PROCESO");
            ordenRequisicion.setEstado(Boolean.TRUE);
            listDetalleItem = new ArrayList<>();
            bandera = Boolean.FALSE;
        } else {
            habilitar = Boolean.FALSE;
            buscarServ = Boolean.FALSE;
            JsfUtil.addWarningMessage("Información", "Tiene una orden de Requisición en proceso, por favor CANCELAR para generar un nuevo ingreso");
        }

    }

    public void saveRequisicion() {
        Boolean nuevo1 = Boolean.FALSE;
        ordenRequisicion.setServidorSolicitante(servidor);
        ordenRequisicion.setEstadoSolicitud("EMITIDO");
        ordenRequisicion.setUnidadAdministrativa(servidor.getDistributivo().getUnidadAdministrativa());
        ordenRequisicion.setNumeroTramite(tramite.getNumTramite());
        ordenRequisicion.setEstadoOrden("INCOMPLETO");
        String codigoRequisic = Utils.completarCadenaConCeros(ordenRequisicion.getOrden() + "", 5);
        ordenRequisicion.setCodigo("ORD-REQ-" + codigoRequisic);
//        ordenReqService.edit(ordenRequisicion);
        Integer t = listDetalleItem.size();
        for (Integer i = 0; i < t; i++) {
            //Guardar item en ordenRequisicionItems
            if (listDetalleItem.get(i).getCantidadTemp() > 0) {
                if (nuevo1.equals(Boolean.FALSE)) {
                    OrdenRequisicion ordenReq = ordenReqService.create(ordenRequisicion);
                    ordenGlobal = new OrdenRequisicion();
                    ordenGlobal = Utils.clone(ordenRequisicion);
                }

                String codigo = listDetalleItem.get(i).getCodigo();
                DetalleItem item = detalleItemService.findByCodigoList(codigo);
//                DetalleItem item = detalleItemService.findByCodigoList(codigo, ordenRequisicion.getAnio());
                detalleItemService.edit(item);
                ordenRequisicionItems.setOrdenRequisicion(ordenRequisicion);
                ordenRequisicionItems.setItem(item);
                ordenRequisicionItems.setCantidadSolicitada(listDetalleItem.get(i).getCantidadTemp());
                OrdenRequisicionItems ori = ordenReqItemsService.create(ordenRequisicionItems);
                nuevo1 = Boolean.TRUE;
                ordenRequisicionItems = new OrdenRequisicionItems();
            } else {
                JsfUtil.addWarningMessage("Información", "Ingrese una cantidad a Solicitar");
                return;
            }
        }

        if (ordenRequisicionItems != null && nuevo1.equals(Boolean.TRUE)) {
            JsfUtil.addSuccessMessage("ÉXITO", "Orden " + ordenRequisicion.getCodigo() + " Guardado Correctamente");
            //Inicializar todo

        }
        botonCompletarTarea = Boolean.FALSE;
        botonGuardar = Boolean.TRUE;
        buscarServ = Boolean.TRUE;
        adicionarItemB = Boolean.TRUE;
        botonCancelar = Boolean.TRUE;
        botonNew = Boolean.TRUE;
        ordenGlobal = Utils.clone(ordenRequisicion);
        habilitar = Boolean.TRUE;
    }

    public void terminarTarea() {
        try {
            this.observacion.setObservacion(descripcionObservacion);
            if (saveTramite() == null) {
                return;
            }
            if (ordenGlobal != null) {
                System.out.println("Unidad " + ordenGlobal.getUnidadAdministrativa());
            }
            this.session.setVarTemp(null);
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            limpiarTodo();
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }

    public boolean readOnlyElementForm(boolean bol) {
        if (bol) {
            return true;
        } else {
            return false;
        }
    }

    public void buscarServidor() {

        if (servidor.getPersona().getIdentificacion() != null) {
            Servidor serv = ordenReqService.findByIdentificacion(servidor.getPersona().getIdentificacion());

            if (serv != null) {
                this.servidor = serv;
                setearUnidadAdministrativa();

            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "456");
            }
        } else {

            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "456");
        }
        buscarServ = Boolean.FALSE;
    }

    public void selectData(SelectEvent evt) {
        servidor = ((Servidor) evt.getObject());

        buscarServ = Boolean.FALSE;
        setearUnidadAdministrativa();
    }

    public void setearUnidadAdministrativa() {
        try {
            this.unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre().getPadre();
            if (unidadAdministrativa == null) {
                unidad = Boolean.FALSE;
//                habilitar = Boolean.TRUE;
            } else {
                unidad = Boolean.TRUE;
//                habilitar = Boolean.TRUE;
//                    ordenRequisicion.setDireccion(unidadAdministrativa.getNombre());

            }
        } catch (Exception e) {
//            habilitar = Boolean.TRUE;
            unidad = Boolean.TRUE;
        }
        if (servidor.getDistributivo() == null) {
            servidor.setDistributivo(new Distributivo());
        }
        if (servidor.getDistributivo().getUnidadAdministrativa() == null) {
            servidor.getDistributivo().setUnidadAdministrativa(new UnidadAdministrativa());
        }
        if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("UN")) {
            ordenRequisicion.setUnidad(servidor.getDistributivo().getUnidadAdministrativa().getNombre());
            ordenRequisicion.setDepartamento(servidor.getDistributivo().getUnidadAdministrativa().getPadre().getNombre());
            UnidadAdministrativa unidads = ordenReqService.findByPadreGrupo(servidor.getDistributivo().getUnidadAdministrativa().getPadre());
            ordenRequisicion.setDireccion(unidads.getNombre());
            departam = Boolean.TRUE;
            if (unidadAdministrativa == null) {
                unidadAdministrativa = unidads;
                unidad = Boolean.TRUE;
            }
        } else if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("DE")) {
            ordenRequisicion.setUnidad(null);
            ordenRequisicion.setDepartamento(servidor.getDistributivo().getUnidadAdministrativa().getNombre());
            ordenRequisicion.setDireccion(servidor.getDistributivo().getUnidadAdministrativa().getPadre().getNombre());
            departam = Boolean.TRUE;
            if (unidadAdministrativa == null) {
                unidadAdministrativa = servidor.getDistributivo().getUnidadAdministrativa().getPadre();
                unidad = Boolean.TRUE;
            }
        } else if (servidor.getDistributivo().getUnidadAdministrativa().getTipoUnidad().getCodigo().equals("DIR")) {
            ordenRequisicion.setUnidad(null);
            ordenRequisicion.setDepartamento(null);
            ordenRequisicion.setDireccion(servidor.getDistributivo().getUnidadAdministrativa().getNombre());
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
    }

    public void abrirDialogo() {
//        Map<String, List<String>> params = new HashMap<>();
//        params.put("PERIODO", Arrays.asList(ordenRequisicion.getAnio().toString()));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogItemCta", null);
    }

    public void selectDatosItem(SelectEvent evt) {
        detalleItem = (DetalleItem) evt.getObject();
        for (DetalleItem detalleItem1 : listDetalleItem) {
            if (detalleItem1.getCodigo().equals(detalleItem.getCodigo())) {
                JsfUtil.addErrorMessage("ERROR", "El Item seleccionado ya está ingresado");
                return;
            }
        }
        this.listDetalleItem.add(this.detalleItem);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        int i = event.getRowIndex();
        Integer t = listDetalleItem.size();
        if (listDetalleItem.get(i).getCantidadTemp() > listDetalleItem.get(i).getCantidadExistente()) {

            FacesMessage sinStock = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", " La cantidad  " + newValue + " No hay en Stock");
            FacesContext.getCurrentInstance().addMessage(null, sinStock);
            listDetalleItem.get(i).setCantidadTemp(0);

            PrimeFaces.current().ajax().update(":frmMain:dtItem");
        } else {
            if (newValue != null && !newValue.equals(oldValue)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor Modificado", "Cantidad a Solicitar: " + listDetalleItem.get(i).getCantidadTemp());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimeFaces.current().ajax().update(":frmMain:dtItem");
            }
            PrimeFaces.current().ajax().update(":frmMain:dtItem");
        }
    }

    public void borrarItemLista(DetalleItem item) {
        listDetalleItem.remove(item);
        PrimeFaces.current().ajax().update(":frmMain:dtItem");
    }

    public void limpiarTodo() {
        ordenRequisicion = new OrdenRequisicion();
        ordenRequisicionItems = new OrdenRequisicionItems();
        listDetalleItem = new ArrayList<>();
        servidor = new Servidor();
        servidor.setPersona(new Cliente());
        ordenRequisicion.setAnio(Utils.getAnio(new Date()).shortValue());
        unidadAdministrativa = new UnidadAdministrativa();
        habilitar = Boolean.TRUE;
        bandera = Boolean.TRUE;
        unidad = Boolean.TRUE;
        botonNew = Boolean.FALSE;

    }

    public void observacionesIngreso() {

        OrdenRequisicion orden = ordenReqService.getOrdenEstados(tramite.getNumTramite());
        if (orden == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Realice un registro para poder terminar esta tarea");
            return;
        }

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());

        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

        //Requisición  Completada
        this.ordenGlobal.setEstadoOrden("COMPLETO");
        ordenReqService.edit(ordenGlobal);
        botonCompletarTarea = Boolean.TRUE;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public OrdenRequisicionItems getOrdenRequisicionItems() {
        return ordenRequisicionItems;
    }

    public void setOrdenRequisicionItems(OrdenRequisicionItems ordenRequisicionItems) {
        this.ordenRequisicionItems = ordenRequisicionItems;
    }

    public OrdenRequisicion getOrdenRequisicion() {
        return ordenRequisicion;
    }

    public void setOrdenRequisicion(OrdenRequisicion ordenRequisicion) {
        this.ordenRequisicion = ordenRequisicion;
    }

    public List<DetalleItem> getListDetalleItem() {
        return listDetalleItem;
    }

    public void setListDetalleItem(List<DetalleItem> listDetalleItem) {
        this.listDetalleItem = listDetalleItem;
    }

    public List<OrdenRequisicionItems> getListOrdenReqItems() {
        return listOrdenReqItems;
    }

    public void setListOrdenReqItems(List<OrdenRequisicionItems> listOrdenReqItems) {
        this.listOrdenReqItems = listOrdenReqItems;
    }

    public LazyModel<OrdenRequisicion> getLazyOrdenRequisicion() {
        return lazyOrdenRequisicion;
    }

    public void setLazyOrdenRequisicion(LazyModel<OrdenRequisicion> lazyOrdenRequisicion) {
        this.lazyOrdenRequisicion = lazyOrdenRequisicion;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public Boolean getBotonCompletarTarea() {
        return botonCompletarTarea;
    }

    public void setBotonCompletarTarea(Boolean botonCompletarTarea) {
        this.botonCompletarTarea = botonCompletarTarea;
    }

    public Boolean getBotonGuardar() {
        return botonGuardar;
    }

    public void setBotonGuardar(Boolean botonGuardar) {
        this.botonGuardar = botonGuardar;
    }

    public List<PlanAnualProgramaProyecto> getListPlanAnualProgramaProyecto() {
        return listPlanAnualProgramaProyecto;
    }

    public void setListPlanAnualProgramaProyecto(List<PlanAnualProgramaProyecto> listPlanAnualProgramaProyecto) {
        this.listPlanAnualProgramaProyecto = listPlanAnualProgramaProyecto;
    }

    public DetalleItem getDetalleItem() {
        return detalleItem;
    }

    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public UnidadAdministrativa getDireccion() {
        return direccion;
    }

    public void setDireccion(UnidadAdministrativa direccion) {
        this.direccion = direccion;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public UnidadAdministrativa getUnidadEnt() {
        return unidadEnt;
    }

    public void setUnidadEnt(UnidadAdministrativa unidadEnt) {
        this.unidadEnt = unidadEnt;
    }

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
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

    public Boolean getBuscarServ() {
        return buscarServ;
    }

    public void setBuscarServ(Boolean buscarServ) {
        this.buscarServ = buscarServ;
    }

    public Boolean getAdicionarItemB() {
        return adicionarItemB;
    }

    public void setAdicionarItemB(Boolean adicionarItemB) {
        this.adicionarItemB = adicionarItemB;
    }

    public Boolean getBotonCancelar() {
        return botonCancelar;
    }

    public void setBotonCancelar(Boolean botonCancelar) {
        this.botonCancelar = botonCancelar;
    }

    public Boolean getBotonNew() {
        return botonNew;
    }

    public void setBotonNew(Boolean botonNew) {
        this.botonNew = botonNew;
    }

    public Boolean getProcesoTramBol() {
        return procesoTramBol;
    }

    public void setProcesoTramBol(Boolean procesoTramBol) {
        this.procesoTramBol = procesoTramBol;
    }

    public String getDescripcionObservacion() {
        return descripcionObservacion;
    }

    public void setDescripcionObservacion(String descripcionObservacion) {
        this.descripcionObservacion = descripcionObservacion;
    }
//</editor-fold>

}
