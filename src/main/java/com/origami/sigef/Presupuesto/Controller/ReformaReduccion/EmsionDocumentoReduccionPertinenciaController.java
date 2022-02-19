/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Model.FormularioEmisionDatos;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "emisionPertinecniaReduccionView")
@ViewScoped
public class EmsionDocumentoReduccionPertinenciaController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession user;
    @Inject
    private ReformaSuplementoIngresoService reformaService;
    @Inject
    private ServletSession ss;
    @Inject
    private ValoresService valoresService;

    private LazyModel<ReformaIngresoSuplemento> lazyReforma;
    private ReformaIngresoSuplemento reforma;
    private String observaciones;
    private FormularioEmisionDatos formulario;

    @PostConstruct
    public void inicalizar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                reforma = new ReformaIngresoSuplemento();
                lazyReforma = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReforma.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyReforma.getFilterss().put("tipo:equal", false);
                formulario = new FormularioEmisionDatos();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void realizarFormulario(ReformaIngresoSuplemento r) {
        reforma = new ReformaIngresoSuplemento();
        reforma = r;
        formulario = new FormularioEmisionDatos();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formulario.setFecha(simpleDateFormat.format(reforma.getFechaOficioReforma()));
        reforma.setInformacion(valoresService.findByCodigo("HTML_INFORME_PERTINENCIA"));
        if (r.getTipo()) {
            formulario.setNum("No." + r.getNumeracion() + "-SUPLEMENTO-" + r.getPeriodo());
        } else {
            formulario.setNum("No." + r.getNumeracion() + "-REDUCCIÓN-" + r.getPeriodo());
        }
        PrimeFaces.current().executeScript("PF('dlgFormulario').show()");
        PrimeFaces.current().ajax().update(":frmFormulario");

    }

    public void imprimirInformePertinencia() {

        ss.addParametro("num", formulario.getNum());
        ss.addParametro("fecha", formulario.getFecha());
        ss.addParametro("parrafocinco", reforma.getInformacion());
        ss.setNombreReporte("refromaGeneralPertinencia");
        reformaService.edit(reforma);

//        ss.addParametro("NOMBRE_REPORTE", "PROFORMA INGRESOS");
//        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public void imprimirotros(int a) {

        switch (a) {
            case 1:
                ss.addParametro("id", reforma.getId());
                ss.setNombreReporte("reformaIngreosAnexoUno");
                break;
            case 2:
                ss.addParametro("id", reforma.getId());
                ss.setNombreReporte("reformaEgresosAnexoDos");
                break;
            default:
                ss.addParametro("id", reforma.getId());
                ss.setNombreReporte("refromaPlanAnualProgramaProyecto");
                break;
        }

//        ss.addParametro("NOMBRE_REPORTE", "PROFORMA INGRESOS");
//        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    public void abriDlogo(ReformaIngresoSuplemento r) {
        reforma = new ReformaIngresoSuplemento();
        reforma = r;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public BigDecimal getSuplemento(ReformaIngresoSuplemento r) {
        return reformaService.getTotalSuplemento(r);
    }

    public BigDecimal getReduccion(ReformaIngresoSuplemento r) {

        return reformaService.getTotalReduccion(r);
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones.toUpperCase());
            getParamts().put("usuario_aprobacion_fi", clienteService.getrolsUser(RolUsuario.financiero));
            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reforma = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public LazyModel<ReformaIngresoSuplemento> getLazyReforma() {
        return lazyReforma;
    }

    public void setLazyReforma(LazyModel<ReformaIngresoSuplemento> lazyReforma) {
        this.lazyReforma = lazyReforma;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
    }

    
    
    public FormularioEmisionDatos getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioEmisionDatos formulario) {
        this.formulario = formulario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//</editor-fold>

}
