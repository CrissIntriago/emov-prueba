/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
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
@Named(value = "revisionInformeIeView")
@ViewScoped
public class RevisionInformeReformaIngresoEgresoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaSuplementoIngresoService reformaService;
    @Inject
    private CuposService cuposService;
    private LazyModel<ReformaIngresoSuplemento> LazyReforma;
    private List<DetalleReformaIngresoSuplemento> lazyIngresos;
    private List<Cupos> lazyCupos;
    private BigDecimal totalPresupuestoInicial, totalSuplemeto, totalReduccion, totalCodificado;
    private List<ProformaIngreso> listaItem;
    private List<CatalogoItem> listaFiltroItem;
    private List<Nivel> listaFiltroNivel;
    private String observaciones;
    private ArrayList<String> usuariosScoma;
    private ReformaIngresoSuplemento reforma;

    @PostConstruct
    public void inicizalizar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                LazyReforma = new LazyModel(ReformaIngresoSuplemento.class);
                LazyReforma.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void actualizarTotales(ReformaIngresoSuplemento r) {
        totalPresupuestoInicial = BigDecimal.ZERO;
        totalSuplemeto = BigDecimal.ZERO;
        totalReduccion = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;
        totalSuplemeto = reformaService.getTotalSuplemento(r);
        totalReduccion = reformaService.getTotalReduccion(r);
        totalPresupuestoInicial = reformaService.getTotalReformaPinicial(r);
        totalCodificado = reformaService.getTotalReformaPcodificado(r);
    }

    public void revisionInforme(ReformaIngresoSuplemento r) {
        this.listaItem = reformaService.getListaCatalogoPresupuesto(r.getPeriodo(), true);
        this.listaFiltroNivel = reformaService.getListaNiveles(r.getPeriodo(), true);
        this.listaFiltroItem = reformaService.getListaItemFiltro(r.getPeriodo(), true);
        this.lazyIngresos = reformaService.getDetalleItem(r);
        this.lazyCupos = cuposService.getCuposDetalle(r);
        actualizarTotales(r);
        PrimeFaces.current().executeScript("PF('DlogoRevisionPlanificacion').show()");
        PrimeFaces.current().ajax().update(":fmDlogoRevision");
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
        traermeUnidadeasCupos();

    }

    public BigDecimal totalSuplemento(ReformaIngresoSuplemento r) {
        return reformaService.getTotalSuplemento(r);
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public void traermeUnidadeasCupos() {
        List<Cupos> entidadCupos = new ArrayList<>();

        usuariosScoma = new ArrayList<>();
        entidadCupos = cuposService.getCuposDetalle(reforma);

        for (Cupos item : entidadCupos) {
            if (!isNullOrEmpty(item.getUsuario())) {
                usuariosScoma.add(item.getUsuario());
            }
        }
//         usuarios = new ArrayList();
//        this.usuarios = cupoPresupuestoService.listaUsuarios();
//        List<String> username = new ArrayList();
//        for (Usuarios data : usuarios) {
//            username.add(data.getUsuario());
//        }
//        usuariosScoma = String.join(",", username);
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            traermeUnidadeasCupos();
            getParamts().put("usuario", this.session.getNameUser());
            this.getParamts().put("usuarios", usuariosScoma);

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            reforma = new ReformaIngresoSuplemento();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public LazyModel<ReformaIngresoSuplemento> getLazyReforma() {

        return LazyReforma;
    }

    public void setLazyReforma(LazyModel<ReformaIngresoSuplemento> LazyReforma) {
        this.LazyReforma = LazyReforma;
    }

    public BigDecimal getTotalPresupuestoInicial() {
        return totalPresupuestoInicial;
    }

    public void setTotalPresupuestoInicial(BigDecimal totalPresupuestoInicial) {
        this.totalPresupuestoInicial = totalPresupuestoInicial;
    }

    public BigDecimal getTotalSuplemeto() {
        return totalSuplemeto;
    }

    public void setTotalSuplemeto(BigDecimal totalSuplemeto) {
        this.totalSuplemeto = totalSuplemeto;
    }

    public BigDecimal getTotalReduccion() {
        return totalReduccion;
    }

    public void setTotalReduccion(BigDecimal totalReduccion) {
        this.totalReduccion = totalReduccion;
    }

    public BigDecimal getTotalCodificado() {
        return totalCodificado;
    }

    public void setTotalCodificado(BigDecimal totalCodificado) {
        this.totalCodificado = totalCodificado;
    }

    public List<ProformaIngreso> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<ProformaIngreso> listaItem) {
        this.listaItem = listaItem;
    }

    public List<CatalogoItem> getListaFiltroItem() {
        return listaFiltroItem;
    }

    public void setListaFiltroItem(List<CatalogoItem> listaFiltroItem) {
        this.listaFiltroItem = listaFiltroItem;
    }

    public List<Nivel> getListaFiltroNivel() {
        return listaFiltroNivel;
    }

    public void setListaFiltroNivel(List<Nivel> listaFiltroNivel) {
        this.listaFiltroNivel = listaFiltroNivel;
    }

    public ReformaSuplementoIngresoService getReformaService() {
        return reformaService;
    }

    public void setReformaService(ReformaSuplementoIngresoService reformaService) {
        this.reformaService = reformaService;
    }

    public List<DetalleReformaIngresoSuplemento> getLazyIngresos() {
        return lazyIngresos;
    }

    public void setLazyIngresos(List<DetalleReformaIngresoSuplemento> lazyIngresos) {
        this.lazyIngresos = lazyIngresos;
    }

    public List<Cupos> getLazyCupos() {
        return lazyCupos;
    }

    public void setLazyCupos(List<Cupos> lazyCupos) {
        this.lazyCupos = lazyCupos;
    }

//</editor-fold>
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
