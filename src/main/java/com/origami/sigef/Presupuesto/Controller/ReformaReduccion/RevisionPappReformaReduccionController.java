/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author Luis Suarez
 */
@Named(value = "revisionPappReormaReduccionView")
@ViewScoped
public class RevisionPappReformaReduccionController extends BpmnBaseRoot implements Serializable {

    //SERVICIOS
    @Inject
    private UserSession user;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private CuposService cupoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CuposService cuposService;
    @Inject
    private ReformaCupoReduccionService cupoReduccionService;
    @Inject
    private ClienteService clienteService;

//LAZY
    private LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy;
    private LazyModel<ReformaIngresoSuplemento> lazyReformas;
    //OBJETOS
    private ReformaIngresoSuplemento reformaSuplemento;
    private CatalogoItem estado;
    //BOOLEAN
    private boolean filtroDatosNullTablaGeneralPlanes;
    private boolean panel1, panel2;
    //LOCAL
    private BigInteger reforma;
    private CatalogoItem estadoMostrar;
    private List<CatalogoItem> estadoFiltro;
    private String observaciones;
    private boolean btnAprobar, btnRechazar;
    private List<String> usuariosScoma;
    private Boolean enabledReforma;

    @PostConstruct
    public void inicialiador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                lazyReformas = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReformas.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyReformas.getFilterss().put("tipo:equal", false);
                filtroDatosNullTablaGeneralPlanes = true;
                panel1 = true;
                panel2 = false;
                estadoFiltro = new ArrayList<>();
                estadoFiltro = catalogoService.MostarTodoCatalogo("estado_solicitud");
                usuariosScoma = new ArrayList<>();
                enabledReforma = true;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarPapp(ReformaIngresoSuplemento r) {
        reforma = BigInteger.valueOf(r.getId());
        reformaSuplemento = new ReformaIngresoSuplemento();
        reformaSuplemento = r;
        vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", reforma);

        panel1 = false;
        panel2 = true;
        mostrEstado(reforma);
        enabledReforma = false;
    }

    public void mostrEstado(BigInteger b) {
        estadoMostrar = new CatalogoItem();
        estadoMostrar = suplementoIngresoService.getEstadoNombre(b);

    }

    public void aprobarPapp() {

        if (reformaSuplemento.getTipo()) {
            BigDecimal result1 = suplementoIngresoService.getSuplementoPapp(BigInteger.valueOf(reformaSuplemento.getId()));

            if (result1.doubleValue() == 0) {
                JsfUtil.addErrorMessage("Error", "No hay monto alterado");
                return;
            }

            BigDecimal r1 = cupoService.cuposPapp(reformaSuplemento);
            BigDecimal r2 = suplementoIngresoService.getSuplementoPapp(BigInteger.valueOf(reformaSuplemento.getId()));

            if (r1.doubleValue() == r2.doubleValue()) {

                estado = new CatalogoItem();
                estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AR");
                suplementoIngresoService.actualizarEstadoPapp(estado, BigInteger.valueOf(reformaSuplemento.getId()));
                mostrEstado(reforma);
            } else {
                JsfUtil.addErrorMessage("Error", "El monto del PAPP no coincide con el cupo que se le asigno");
            }

        } else {
            BigDecimal resul2 = suplementoIngresoService.getReduccionPapp(BigInteger.valueOf(reformaSuplemento.getId()));

            if (resul2.doubleValue() == 0) {
                JsfUtil.addErrorMessage("Error", "No hay monto alterado");
                return;
            }

            estado = new CatalogoItem();
            estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AR");
            suplementoIngresoService.actualizarEstadoPapp(estado, BigInteger.valueOf(reformaSuplemento.getId()));
            mostrEstado(reforma);
        }

        JsfUtil.addInformationMessage("Información", "SE HA APROBADO EL PAPP CON EXITO");

    }

    public void rechazarPapp() {

        estado = new CatalogoItem();
        estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RRE");
        suplementoIngresoService.actualizarEstadoPapp(estado, BigInteger.valueOf(reformaSuplemento.getId()));
        mostrEstado(reforma);
        JsfUtil.addInformationMessage("Información", "SE HA RECHAZADO EL PAPP");

    }

    public void showPanel1() {
        panel1 = true;
        panel2 = false;
    }

    public void filtroDeDatosNullVistaGeneralPlanAnual() {
        if (!filtroDatosNullTablaGeneralPlanes) {
            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
            vistaGeneralPlanAnualLazy.getFilterss().put("nombrePlanLocalProgramaProyecto:equal", null);
            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", reforma);
            PrimeFaces.current().ajax().update("formTablaMain");
        } else {
            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", reforma);
            PrimeFaces.current().ajax().update("formTablaMain");
        }
    }

    public BigDecimal cupoSuplmeneto(ReformaIngresoSuplemento r) {
        return cupoService.cuposPapp(r);
    }

    public void abriDlogo(boolean aprobar) {

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (aprobar) {
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
        }
        traermeUnidadeasCupos();
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public void traermeUnidadeasCupos() {
        List<CupoReduccion> entidadCupos = new ArrayList<>();
        usuariosScoma = new ArrayList<>();
        entidadCupos = cupoReduccionService.getCuposDetalle(reformaSuplemento);
        for (CupoReduccion item : entidadCupos) {
            if (!isNullOrEmpty(item.getUsuario())) {
                usuariosScoma.add(item.getUsuario());
            }
        }
    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones.toUpperCase());
            //clienteService.getUnidadUserCodigo("CPR","2")
            if (aprobar == 1) {
                getParamts().put("aprobado", aprobar);
                getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));

                aprobarPapp();
            } else {

                getParamts().put("aprobado", aprobar);
                getParamts().put("usuario", this.session.getNameUser());
                this.getParamts().put("usuarios", usuariosScoma);
                rechazarPapp();
            }

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaSuplemento = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public List<CatalogoItem> getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(List<CatalogoItem> estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public CatalogoItem getEstadoMostrar() {
        return estadoMostrar;
    }

    public void setEstadoMostrar(CatalogoItem estadoMostrar) {
        this.estadoMostrar = estadoMostrar;
    }

    public boolean isFiltroDatosNullTablaGeneralPlanes() {
        return filtroDatosNullTablaGeneralPlanes;
    }

    public void setFiltroDatosNullTablaGeneralPlanes(boolean filtroDatosNullTablaGeneralPlanes) {
        this.filtroDatosNullTablaGeneralPlanes = filtroDatosNullTablaGeneralPlanes;
    }

    public boolean isPanel1() {
        return panel1;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPanel2() {
        return panel2;
    }

    public void setPanel2(boolean panel2) {
        this.panel2 = panel2;
    }

    public BigInteger getReforma() {
        return reforma;
    }

    public void setReforma(BigInteger reforma) {
        this.reforma = reforma;
    }

    public LazyModel<VistaGeneralPlanAnual> getVistaGeneralPlanAnualLazy() {
        return vistaGeneralPlanAnualLazy;
    }

    public void setVistaGeneralPlanAnualLazy(LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy) {
        this.vistaGeneralPlanAnualLazy = vistaGeneralPlanAnualLazy;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyReformas() {
        return lazyReformas;
    }

    public void setLazyReformas(LazyModel<ReformaIngresoSuplemento> lazyReformas) {
        this.lazyReformas = lazyReformas;
    }

    public ReformaIngresoSuplemento getReformaSuplemento() {
        return reformaSuplemento;
    }

    public void setReformaSuplemento(ReformaIngresoSuplemento reformaSuplemento) {
        this.reformaSuplemento = reformaSuplemento;
    }

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
        this.estado = estado;
    }
//</editor-fold>

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

    public Boolean getEnabledReforma() {
        return enabledReforma;
    }

    public void setEnabledReforma(Boolean enabledReforma) {
        this.enabledReforma = enabledReforma;
    }

}
