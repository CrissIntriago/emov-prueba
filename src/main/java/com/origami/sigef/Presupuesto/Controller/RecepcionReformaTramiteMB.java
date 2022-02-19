/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class RecepcionReformaTramiteMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    private LazyModel<ReformaIngresoSuplemento> lazyAsignacion;
    private String observaciones;
    private ReformaIngresoSuplemento reformaSuplemento;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                reformaSuplemento = new ReformaIngresoSuplemento();
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                lazyAsignacion = new LazyModel(ReformaIngresoSuplemento.class);
                lazyAsignacion.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")
            getParamts().put("usuariofinanciero", clienteService.getrolsUser(RolUsuario.asistenteFinanciero));

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

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public LazyModel<ReformaIngresoSuplemento> getLazyAsignacion() {
        return lazyAsignacion;
    }

    public void setLazyAsignacion(LazyModel<ReformaIngresoSuplemento> lazyAsignacion) {
        this.lazyAsignacion = lazyAsignacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ReformaIngresoSuplemento getReformaSuplemento() {
        return reformaSuplemento;
    }

    public void setReformaSuplemento(ReformaIngresoSuplemento reformaSuplemento) {
        this.reformaSuplemento = reformaSuplemento;
    }
//</editor-fold>        
}
