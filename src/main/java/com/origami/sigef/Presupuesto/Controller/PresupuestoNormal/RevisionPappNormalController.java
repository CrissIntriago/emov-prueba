/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.PresupuestoNormal;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.model.ReporteDeActividades;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
@Named(value = "revisionPappNormalView")
@ViewScoped
public class RevisionPappNormalController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ProductoService productoService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
    private LazyModel<VistaGeneralPlanAnual> lazy;
    private OpcionBusqueda opcionBusqueda;
    private ReporteDeActividades reporte;
    private Short periodo;
    private String observaciones;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);

                reporte = new ReporteDeActividades();
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                String anioActual = String.valueOf(year);
                this.opcionBusqueda = new OpcionBusqueda();
                this.opcionBusqueda.setAnio(Short.valueOf(anioActual));
                periodo = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                this.opcionBusqueda.setAnio(periodo);
                lazy = new LazyModel(VistaGeneralPlanAnual.class);
                lazy.getFilterss().put("periodoProducto:equal", periodo);
                lazy.getFilterss().put("codigoProducto:equal", null);
                lazy.getFilterss().put("codigoProductoTraspaso:equal", null);
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void verificadorDeValores() {
        ArrayList<ReporteDeActividades> list = new ArrayList<>();
        ArrayList<ReporteDeActividades> ListEquilibrado = new ArrayList<>();
        ArrayList<ReporteDeActividades> ListDesequilibrado = new ArrayList<>();
        ListEquilibrado = new ArrayList<>();
        List<ActividadOperativa> resultActividades = actividadService.getListActividades(opcionBusqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
        List<Producto> resultProductos = productoService.getListProductos(opcionBusqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
        if (resultActividades.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay Actividades Operativas registrados en el período " + opcionBusqueda.getAnio());
            return;
        } else {
            if (resultProductos.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO", "No hay Productos registrados en el período " + opcionBusqueda.getAnio());
                return;
            } else {
                for (ActividadOperativa actividad : resultActividades) {
                    BigDecimal totalProducto = productoService.getSumaProducto(actividad, opcionBusqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
                    if (!actividad.getMonto().equals(totalProducto)) {
                        /*Se iran agregando las actividades que la suma de sus productos no sean igual al monto asignado*/
                        reporte.setActividadOperativa(actividad);
                        reporte.setMontoTotalDeProductos(totalProducto);
                        reporte.setDiferencia(actividad.getMonto().subtract(totalProducto));
                        ListDesequilibrado.add(reporte);
                        reporte = new ReporteDeActividades();
                    } else {
                        reporte.setActividadOperativa(actividad);
                        reporte.setMontoTotalDeProductos(totalProducto);
                        reporte.setDiferencia(actividad.getMonto().subtract(totalProducto));
                        ListEquilibrado.add(reporte);
                        reporte = new ReporteDeActividades();
                    }
                }
                if (ListDesequilibrado.isEmpty()) {
                    list = ListEquilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductos').show()");
                    PrimeFaces.current().ajax().update("formActividadProductos");
                } else {
                    list = ListDesequilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductos').show()");
                    PrimeFaces.current().ajax().update("formActividadProductos");
                }
            }
        }
    }

    public void dlogoObservaciones() {

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("CPR", "2")
            getParamts().put("usuario",clienteService.getrolsUser(RolUsuario.presupuesto) ); //"2"
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
    
     public void imprimir(boolean excel) {
        if (opcionBusqueda == null) {
            JsfUtil.addWarningMessage("AVISO", "ELIGA UN PERIODO");
            return;
        }
        if (excel) {
            ss.setContentType("xlsx");
        }
        ss.addParametro("periodo", periodo);
        ss.setNombreReporte("papp");
        ss.setNombreSubCarpeta("presupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public LazyModel<VistaGeneralPlanAnual> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<VistaGeneralPlanAnual> lazy) {
        this.lazy = lazy;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

//</editor-fold>
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
