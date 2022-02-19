/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "documentacionPagoServiciosView")
@ViewScoped
public class DocuemntacionPagoServiciosController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ProcedimientoRequisitoService requisitosService;
    @Inject
    private UserSession user;
    @Inject
    private ClienteService clienteService;

    private String observaciones;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private Boolean renderBtnObservacion;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                procedimientoRequisitoList = requisitosService.getListaRequisitos(tramite.getTipoTramite().getId());
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        getRenderBtn();
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void getRenderBtn() {
        switch (tramite.getTipoTramite().getAbreviatura()) {
            case "PPPI":
                renderBtnObservacion = true;
                break;
            default:
                renderBtnObservacion = false;
                break;
        }
    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);
            if (null != tramite.getTipoTramite().getAbreviatura()) {
                switch (tramite.getTipoTramite().getAbreviatura()) {
                    case "PPPI":
                        getParamts().put("aprobado", aprobar);
                        if (aprobar == 0) {
                            getParamts().put("usuarioTTHH", clienteService.getrolsUser(RolUsuario.analista));
                        } else {
                            getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                        }
                        break;
                    default:
                        getParamts().put("usuarioCtl", clienteService.getrolsUser(RolUsuario.controlPrevio));
                        break;
                }
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

    public Boolean getRenderBtnObservacion() {
        return renderBtnObservacion;
    }

    public void setRenderBtnObservacion(Boolean renderBtnObservacion) {
        this.renderBtnObservacion = renderBtnObservacion;
    }

}
