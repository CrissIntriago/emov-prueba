/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.OrdenRequisicionItemsService;
import com.origami.sigef.activos.service.OrdenRequisicionService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.OrdenRequisicion;
import com.origami.sigef.common.entities.OrdenRequisicionItems;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "aprobarSolicitudRequisicionView")
@ViewScoped
public class AprobarSolicitudRequisicionController extends BpmnBaseRoot implements Serializable {

    private OrdenRequisicion ordenRequisicion;
    private OrdenRequisicionItems ordenRequisicionItems;
    private Distributivo dis;
    private LazyModel<OrdenRequisicion> lazyOrdenRequisicion;
    private LazyModel<OrdenRequisicionItems> lazyOrdenRequisicionItems;
    private List<OrdenRequisicionItems> listOrdenRequisicionItems;
    private List<OrdenRequisicionItems> listOrdenRequisicionItemsByOR;

    private Boolean habilitar = Boolean.TRUE;
    private String observando;

    @Inject
    private OrdenRequisicionService ordenReqService;

    @Inject
    private OrdenRequisicionItemsService ordenRequisicionItemsService;

    @Inject
    private ClienteService clienteService;

    private static final Logger LOG = Logger.getLogger(AprobarSolicitudRequisicionController.class.getName());

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
                    lazyOrdenRequisicion = new LazyModel<>(OrdenRequisicion.class);
                    lazyOrdenRequisicion.getFilterss().put("codigo", ordenRequisicion.getCodigo());
                } else {
                    ordenRequisicion = new OrdenRequisicion();
                    ordenRequisicionItems = new OrdenRequisicionItems();
                    lazyOrdenRequisicion = new LazyModel<>(OrdenRequisicion.class);
                    lazyOrdenRequisicion.getFilterss().put("estado", Boolean.TRUE);
                    lazyOrdenRequisicionItems = new LazyModel<>(OrdenRequisicionItems.class);
                }
            } else {
                ordenRequisicion = new OrdenRequisicion();
                ordenRequisicionItems = new OrdenRequisicionItems();
                lazyOrdenRequisicion = new LazyModel<>(OrdenRequisicion.class);
                lazyOrdenRequisicion.getFilterss().put("estado", Boolean.TRUE);
                lazyOrdenRequisicionItems = new LazyModel<>(OrdenRequisicionItems.class);
            }
        } catch (Exception e) {

            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void aprobar(OrdenRequisicion orden) {
        Subject subject = SecurityUtils.getSubject();
        orden.setFechaAprobacion(new Date());
        orden.setEstadoSolicitud("APROBADO");
        orden.setEstadoOrden("COMPLETO");
        orden.setServidorRevisor(subject.getPrincipal().toString());
        habilitar = false;
        ordenReqService.edit(orden);
        JsfUtil.addInformationMessage("Información", "Orden de Requisición Aprobada Correctamente");
    }

    public void rechazar(OrdenRequisicion orden) {
        Subject subject = SecurityUtils.getSubject();
        orden.setFechaAprobacion(new Date());
        orden.setEstadoSolicitud("ANULADO");
        orden.setEstadoOrden("RECHAZADO");
        orden.setServidorRevisor(subject.getPrincipal().toString());
        habilitar = false;
        ordenReqService.edit(orden);
        JsfUtil.addInformationMessage("Información", "Orden de Requisición Rechazada Correctamente");

    }

    public void dlgObservaciones(int a, OrdenRequisicion r) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        ordenRequisicion = r;
        if (a == 1) {
            JsfUtil.executeJS("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
        } else {
            JsfUtil.executeJS("PF('dlgObservacionesR').show()");
            PrimeFaces.current().ajax().update(":frmDlgObserR");
        }
    }

    public List<OrdenRequisicionItems> getItemByOrdenRequisicion(OrdenRequisicion orden) {
        ordenRequisicion = orden;
        List<OrdenRequisicionItems> result = new ArrayList<>();
        List<OrdenRequisicionItems> items = ordenRequisicionItemsService.getItemsByOrden(ordenRequisicion);
        if (items != null && !items.isEmpty()) {
            items.stream().filter((i) -> (!result.contains(i))).forEachOrdered((i) -> {
                result.add(i);
            });
        }
        return result;

    }

    public void obsdrvacionesIngreso(int aprobado) {
        try {
            if (observando == null || observando.equals("")) {
                JsfUtil.addInformationMessage("Advertencia", "Ingrese una observación");
                return;
            }

            if (aprobado == 1) {
                aprobar(ordenRequisicion);
            } else {
                rechazar(ordenRequisicion);
            }

            getParamts().put("aprobado", aprobado);
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.guardaAlmacen));

            this.observacion.setObservacion(observando);
            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);

            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public OrdenRequisicion getOrdenRequisicion() {
        return ordenRequisicion;
    }

    public void setOrdenRequisicion(OrdenRequisicion ordenRequisicion) {
        this.ordenRequisicion = ordenRequisicion;
    }

    public OrdenRequisicionItems getOrdenRequisicionItems() {
        return ordenRequisicionItems;
    }

    public void setOrdenRequisicionItems(OrdenRequisicionItems ordenRequisicionItems) {
        this.ordenRequisicionItems = ordenRequisicionItems;
    }

    public Distributivo getDis() {
        return dis;
    }

    public void setDis(Distributivo dis) {
        this.dis = dis;
    }

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }

    public LazyModel<OrdenRequisicion> getLazyOrdenRequisicion() {
        return lazyOrdenRequisicion;
    }

    public void setLazyOrdenRequisicion(LazyModel<OrdenRequisicion> lazyOrdenRequisicion) {
        this.lazyOrdenRequisicion = lazyOrdenRequisicion;
    }

    public LazyModel<OrdenRequisicionItems> getLazyOrdenRequisicionItems() {
        return lazyOrdenRequisicionItems;
    }

    public void setLazyOrdenRequisicionItems(LazyModel<OrdenRequisicionItems> lazyOrdenRequisicionItems) {
        this.lazyOrdenRequisicionItems = lazyOrdenRequisicionItems;
    }

    public List<OrdenRequisicionItems> getListOrdenRequisicionItems() {
        return listOrdenRequisicionItems;
    }

    public void setListOrdenRequisicionItems(List<OrdenRequisicionItems> listOrdenRequisicionItems) {
        this.listOrdenRequisicionItems = listOrdenRequisicionItems;
    }

    public List<OrdenRequisicionItems> getListOrdenRequisicionItemsByOR() {
        return listOrdenRequisicionItemsByOR;
    }

    public void setListOrdenRequisicionItemsByOR(List<OrdenRequisicionItems> listOrdenRequisicionItemsByOR) {
        this.listOrdenRequisicionItemsByOR = listOrdenRequisicionItemsByOR;
    }
//</editor-fold>

    public String getObservando() {
        return observando;
    }

    public void setObservando(String observando) {
        this.observando = observando;
    }

}
