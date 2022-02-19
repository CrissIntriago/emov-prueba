/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.ResponsableAdquisicionService;
import com.origami.sigef.administracionCompra.service.SolicitudOrdenCompraService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.ResponsableAdquisicion;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.services.TipoTramiteService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Angel Navarro
 */
@Named(value = "procesoIngresoInvProView")
@ViewScoped
public class ProcesoIngresoInvProcesoController extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvProcesoController.class.getName());

    @Inject
    private SolicitudOrdenCompraService ordenCompraService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private ResponsableAdquisicionService responsableAdquisicionService;
    @Inject
    private TipoTramiteService tipoTramiteService;
    @Inject
    private SeqGenMan secuencia;
    @Inject
    private ClienteService clienteService;

    private SolicitudOrdenCompra solicitudOrdenCompra;

    private TipoTramite tipoTramite;
    private Boolean consulta = false;
    private Long secuenciaOrden;

    @PostConstruct
    public void initView() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                // Ingreso de
                solicitudOrdenCompra = ordenCompraService.getOrdenCompraXNumTramite(this.getTramite().getId());
                System.out.println("solicitud " + solicitudOrdenCompra);
            }
        }
    }

    public void completar() {
        if (solicitudOrdenCompra.getAdquisicion().getTipoAdquisicion() != null
                && solicitudOrdenCompra.getAdquisicion().getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_servicios")) {
            getParamts().put("servicio", 1);
        }
        if (solicitudOrdenCompra.getAdquisicion().getSubTipoAdquisicion() != null) {
            if (solicitudOrdenCompra.getAdquisicion().getSubTipoAdquisicion().getCodigo().equals("subtipo_adquisicion_inventario")) {
                tipoTramite = tipoTramiteService.find(4L);
                getParamts().put("subProcessBodega", "altaInventario");
                if (Utils.isNotEmpty(solicitudOrdenCompra.getAdquisicion().getResponsableAdquisicionList())) {
                    getParamts().put("servicio", 0);
                    getParamts().put("usuario_guardalmacen", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
                }
            } else {
                tipoTramite = tipoTramiteService.find(3L);
                getParamts().put("subProcessBodega", "procesoAltaBienes");
                if (Utils.isNotEmpty(solicitudOrdenCompra.getAdquisicion().getResponsableAdquisicionList())) {
                    getParamts().put("servicio", 0);
                    getParamts().put("usuario_guardalmacen", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
                }
            }
        }
        try {
            this.completeTask((HashMap<String, Object>) getParamts());
            if (solicitudOrdenCompra.getAdquisicion().getTipoAdquisicion() != null
                    && solicitudOrdenCompra.getAdquisicion().getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")) {
                secuenciaOrden = secuencia.getSecuencia("NUMERO_TRAMITE").longValue();
                JsfUtil.executeJS("PF('continuarDlg').show()");
                PrimeFaces.current().ajax().update("frmContinuar");
            } else {
                continuarProceso();
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

        //this.continuar();
    }

    public void continuarProceso() {
        JsfUtil.redirectFaces("/procesos/bandeja-tareas");
    }

    public Long secuencia() {
        return secuencia.getSecuencia("NUMERO_TRAMITE").longValue();
    }

    public void generalSolicitud() {
        Boolean metodologia = Boolean.FALSE;
        Boolean caracteristicas = Boolean.FALSE;
        Boolean catalogo = Boolean.TRUE;
        String nombreReporte = "ORDEN DE COMPRA";
        List<ResponsableAdquisicion> responsablesList = responsableAdquisicionService.getListaDeResponsablesActivo(solicitudOrdenCompra.getAdquisicion());
        ResponsableAdquisicion responsable = new ResponsableAdquisicion();
        for (ResponsableAdquisicion r : responsablesList) {
            if (r.getEstado() == true && r.getFechaFinalizacion() == null) {
                responsable = r;
                break;
            }
        }
        Adquisiciones adquisicion = new Adquisiciones();
        adquisicion = solicitudOrdenCompra.getAdquisicion();
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_combustible")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_alimento_bebidas")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_repuesto_acce"))) {
            caracteristicas = Boolean.TRUE;
            metodologia = Boolean.FALSE;
//            System.out.println("if 1");
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_servicios")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_seguros")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_mant_obras")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_arrendamiento"))) {
            caracteristicas = Boolean.FALSE;
            metodologia = Boolean.TRUE;
//            System.out.println("if 2");
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            catalogo = Boolean.FALSE;
            nombreReporte = "CATALOGO ELECTRONICO";
//            System.out.println("if 2");
        }
//        System.out.println("tipo 1 " + metodologia);
//        System.out.println("tipo 2 " + caracteristicas);
        serveltSession.addParametro("id_orden", solicitudOrdenCompra.getId());
        serveltSession.addParametro("METODOLOGIA_CARACTERISTICAS", metodologia);
        serveltSession.addParametro("NOMBRE_REPORTE", nombreReporte);
        serveltSession.addParametro("CARACTERISTICAS_BIEN", caracteristicas);
        serveltSession.addParametro("CATALOGO_ELECTRONICO", catalogo);
        serveltSession.addParametro("nombreCompleto", responsable.getResponsable().getPersona().getNombreCompleto());
        serveltSession.addParametro("cargo", responsable.getResponsable().getDistributivo().getCargo().getNombreCargo());
        serveltSession.addParametro("correo", responsable.getResponsable().getEmailInstitucion());
        serveltSession.addParametro("telefonos", responsable.getResponsable().getPersona().getCelular() + "-" + responsable.getResponsable().getPersona().getTelefono());
        serveltSession.setNombreReporte("OrdenCompra");
        serveltSession.setNombreSubCarpeta("SolicitudOC");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public SolicitudOrdenCompra getSolicitudOrdenCompra() {
        return solicitudOrdenCompra;
    }

    public void setSolicitudOrdenCompra(SolicitudOrdenCompra solicitudOrdenCompra) {
        this.solicitudOrdenCompra = solicitudOrdenCompra;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Long getSecuenciaOrden() {
        return secuenciaOrden;
    }

    public void setSecuenciaOrden(Long secuenciaOrden) {
        this.secuenciaOrden = secuenciaOrden;
    }

}
