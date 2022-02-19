/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import java.io.Serializable;
import java.math.BigInteger;
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
 * @author alexa
 */
@Named(value = "aprobacionControlPrevio")
@ViewScoped
public class AprobacionControlPrevioController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ProcedimientoRequisitoService requisitosService;
    @Inject
    private UserSession user;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ProcesoService tramiteServiceu;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private FileUploadDoc uploadDoc;

    private String observaciones;
    private boolean btnAprobar, btnRechazar;
    private boolean btnOtro;
    private boolean btnRendered;
    private int tipo;
    private String nombreButton;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                procedimientoRequisitoList = requisitosService.getListaRequisitos(tramite.getTipoTramite().getId());
                btnOtro = false;
                nombreButton = "";
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
            listRequisitos();
            getBtnObservacion();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void listRequisitos() {
        List<ProcedimientoRequisito> listaRequisitos = tramiteServiceu.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                ListarequisitosModel req = new ListarequisitosModel();
                req.setRequisitos(tipoTramite);
                requisitosTramite.add(req);
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
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void getBtnObservacion() {
        switch (tramite.getTipoTramite().getAbreviatura()) {
            case "proceso_af_caja_chica":
                if (tarea.getName().equals("Autorización de Pago")) {
                    btnRendered = false;
                } else {
                    btnRendered = true;
                }
                break;
            default:
                btnRendered = true;
                break;
        }
    }

    public void abriDlogo(boolean aprobar) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        nombreButton = "APROBAR";
        if (aprobar) {
            if ("procesos_pagos_noClasficados".equals(tramite.getTipoTramite().getAbreviatura())) {
                btnOtro = true;
                nombreButton = "CON CARGO";
                tipo = 500;
            } else {
                btnOtro = false;
                tipo = 300;
            }
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
            btnOtro = false;
        }
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);
            switch (tramite.getTipoTramite().getAbreviatura()) {
                case "procesp_fc_liquidacion":
                case "procesos_rf_caja_chica":
                case "proceso_jgvis_servicios":
                    getParamts().put("aprobado", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
                    }
                    break;
                case "proceso_af_caja_chica":
                    getParamts().put("aprobado", aprobar);
                    if (aprobar == 1) {
                        if (tarea.getName().equals("Autorización de Pago")) {
                            getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                        } else {
                            getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                        }
                    }
                    break;
                case "PAG_ANTI_VIATICOS":
                    getParamts().put("aprobado", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioTTHH", clienteService.getrolsUser(RolUsuario.titularTH));
                    }
                    break;
                case "RET_JUD":
                case "PAG_DEC":
                case "PPRM":
                    getParamts().put("aprobado", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioMax", clienteService.getrolsUser(RolUsuario.maximaAutoridad));
                    } else {
                        getParamts().put("usuarioATTHH", clienteService.getrolsUser(RolUsuario.analista));
                    }
                    break;
                case "PPS_profesionales":
                case "procesos_pagos_noClasficados":
                    switch (aprobar) {
                        case 0:
                            getParamts().put("aprobado", aprobar);
                            break;
                        case 1:
                            getParamts().put("aprobado", aprobar);
                            getParamts().put("usuarioMax", clienteService.getrolsUser(RolUsuario.maximaAutoridad));
                            break;
                        case 2:
                            boolean validar = determinarTipoBeneficiario();
                            if (validar) {
                                JsfUtil.addWarningMessage("AVISO!!!", "No puede seleccionar sin cargo a presupuesto ya que el Beneficiario seleccionado es un Servidor.");
                                return;
                            }
                            getParamts().put("aprobado", aprobar);
                            getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                            break;
                    }
                    break;
                default:
                    getParamts().put("aprobado", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioMax", clienteService.getrolsUser(RolUsuario.maximaAutoridad));
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

    public Boolean determinarTipoBeneficiario() {
        boolean res = false;
        int intIndex = tramite.getNameReferencia().toUpperCase().indexOf("PROVEEDOR");
        if (intIndex == -1) {
            res = true;
        }
        return res;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isBtnAprobar() {
        return btnAprobar;
    }

    public void setBtnAprobar(boolean btnAprobar) {
        this.btnAprobar = btnAprobar;
    }

    public boolean isBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public boolean isBtnOtro() {
        return btnOtro;
    }

    public void setBtnOtro(boolean btnOtro) {
        this.btnOtro = btnOtro;
    }

    public String getNombreButton() {
        return nombreButton;
    }

    public void setNombreButton(String nombreButton) {
        this.nombreButton = nombreButton;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

    public boolean isBtnRendered() {
        return btnRendered;
    }

    public void setBtnRendered(boolean btnRendered) {
        this.btnRendered = btnRendered;
    }

}
