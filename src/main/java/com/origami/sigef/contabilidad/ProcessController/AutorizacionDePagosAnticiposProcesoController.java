/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "autDePagosAnticiposView")
@ViewScoped
public class AutorizacionDePagosAnticiposProcesoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private ProcesoService tramiteService;

    @Inject
    private ProcedimientoRequisitoService requisitosService;

    /*OBJECTOS*/
    private OpcionBusqueda busqueda;
    private Adquisiciones adquisicion;

    /*LISTAS*/
    private List<Adquisiciones> detalleAdquisiciones;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;

    /*CONTROLADORES DE BOTONES*/
    private Boolean formAdquisicion;
    private Boolean botonEnviar;
    private Boolean presupuestoTable;

    /*LAZYMODELS*/
    private LazyModel<Adquisiciones> adquisicionesLazyModel;

    private String observaciones;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                busqueda = new OpcionBusqueda();
                adquisicion = new Adquisiciones();

                formAdquisicion = true;
                adquisicionesLazyModel = new LazyModel<>(Adquisiciones.class);
                this.adquisicionesLazyModel.getFilterss().put("estado", true);
                adquisicionesLazyModel.getFilterss().put("id:equal", tramite.getIdReferencia().longValue());

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void visualizar(String codigo) {
        this.presupuestoTable = Boolean.FALSE;
        formAdquisicion = false;
        listRequisitos(codigo);
    }

    public void abriDlogo(Adquisiciones d) {
        this.adquisicion = new Adquisiciones();
        this.adquisicion = d;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void cancelar() {
        this.adquisicion = new Adquisiciones();
        this.adquisicion = null;
        formAdquisicion = true;
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("formAdquisicion");
        PrimeFaces.current().ajax().update("tableAdquisicion");

    }

    public void completarTarea(boolean aprob, String var) {
        try {
            int apro;
            if (aprob) {
                apro = 1;
            } else {
                apro = 0;
            }
            observacion.setObservacion(observaciones);
            getParamts().put("aprobado", apro);
            switch (tramite.getTipoTramite().getAbreviatura()) {
                case "PAG_ANTI_BIENES":
                case "PAG_ANTI_CONSULTORIA":
                case "PAG_ANTI_OBRAPUBLICA":
                case "PAG_ANTI_SERVICIOS":
                    if (var.equals("PAGO")) {
                        if (aprob) {
                            getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
                            getParamts().put("form", "/proceso/pagoAnticipo/poliza-contrato");
                        } else {
                            getParamts().put("usuarioFin", clienteService.getrolsUser(RolUsuario.financiero));
                        }
                    } else if (var.equals("TRANSF")) {
                        getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                    }
                    break;
                default:
                    if (var.equals("PAGO")) {
                        getParamts().put("usuarioContabilidad", clienteService.getrolsUser("3"));
                    } else if (var.equals("TRANSF")) {
                        getParamts().put("usuarioGeneracionCp", clienteService.getrolsUser("3"));
                    }
                    break;
            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    private void listRequisitos(String codigo) {
        List<ProcedimientoRequisito> listaRequisitos = tramiteService.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            if (codigo.equals("GASTO")) {
                for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                    ListarequisitosModel req = new ListarequisitosModel();
                    req.setRequisitos(tipoTramite);
                    if (!req.getRequisitos().getObligatorio()) {
                        if (req.getRequisitos().getIdRequisito().getNombre().toUpperCase().contains(codigo)) {
                            requisitosTramite.add(req);
                        }
                    }
                }
            } else {
                for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                    ListarequisitosModel req = new ListarequisitosModel();
                    req.setRequisitos(tipoTramite);
                    requisitosTramite.add(req);
                }
            }
        }
    }

    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    public void handleFileUploadCertificadoGerente(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(this.tramite.getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.edit(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.addInformationMessage("Información", "El archivo se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public List<Adquisiciones> getDetalleAdquisiciones() {
        return detalleAdquisiciones;
    }

    public void setDetalleAdquisiciones(List<Adquisiciones> detalleAdquisiciones) {
        this.detalleAdquisiciones = detalleAdquisiciones;
    }

    public Boolean getFormAdquisicion() {
        return formAdquisicion;
    }

    public void setFormAdquisicion(Boolean formAdquisicion) {
        this.formAdquisicion = formAdquisicion;
    }

    public Boolean getBotonEnviar() {
        return botonEnviar;
    }

    public void setBotonEnviar(Boolean botonEnviar) {
        this.botonEnviar = botonEnviar;
    }

    public Boolean getPresupuestoTable() {
        return presupuestoTable;
    }

    public void setPresupuestoTable(Boolean presupuestoTable) {
        this.presupuestoTable = presupuestoTable;
    }

    public LazyModel<Adquisiciones> getAdquisicionesLazyModel() {
        return adquisicionesLazyModel;
    }

    public void setAdquisicionesLazyModel(LazyModel<Adquisiciones> adquisicionesLazyModel) {
        this.adquisicionesLazyModel = adquisicionesLazyModel;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

}
