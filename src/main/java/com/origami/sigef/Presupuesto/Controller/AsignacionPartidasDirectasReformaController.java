/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
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
 * @author ORIGAMIEC
 */
@Named(value = "reformaDirectasView")
@ViewScoped
public class AsignacionPartidasDirectasReformaController extends BpmnBaseRoot implements Serializable {
//<editor-fold defaultstate="collapsed" desc="VARIABLES">

    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private PlanProgramaticoService estrucPlanProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPrespuestoService;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    private ValoresDistributivoService valoresService;
    private boolean panelReforma, panelData, columnaSuplmento, columnaReduccion;
    private LazyModel<ReformaIngresoSuplemento> lazyReformas;

    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    private ProformaPresupuestoPlanificado proformaPresupuesto;

    private List<ProformaPresupuestoPlanificado> proformaList;
    private LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy;
    @Inject
    private UserSession userSession;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;
    private ReformaIngresoSuplemento reformaGlobal;
    private BigDecimal totalCupoPDI;

//</editor-fold>
    private boolean btnnuevo;
    private BigDecimal cupoAsignado;
    private String observaciones;
    private boolean enabledReforma;
    @Inject
    private ClienteService clienteService;
    //NUEVO
    @Inject
    private PresCatalogoPresupuestarioService catalogoServiceNew;
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private PresPlanProgramaticoService estructuraServiceNew;
    private List<PresPlanProgramatico> listEstructura;
    private List<PresCatalogoPresupuestario> listItem;
    private List<PresFuenteFinanciamiento> listFuenteNew;

    @PostConstruct
    public void inicializador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                panelReforma = true;
                panelData = false;
                lazyReformas = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReformas.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyReformas.getFilterss().put("tipo:equal", true);
                proformaList = new ArrayList<>();
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
                reformaGlobal = new ReformaIngresoSuplemento();
                columnaSuplmento = false;
                columnaReduccion = false;
                enabledReforma = true;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="LOGICA DE NEGOCIO">
    public void copiaPartidaDirecta(ReformaIngresoSuplemento r) {
        reformaGlobal = new ReformaIngresoSuplemento();
        reformaGlobal = r;
        proformaList = new ArrayList<>();

        List<ProformaPresupuestoPlanificado> listVerificar = proformaService.getListaVerificacion(BigInteger.valueOf(r.getId()));

        if (listVerificar.isEmpty()) {
            proformaList = proformaService.getPratidasDirectasReformas(r.getPeriodo(), "PDI");
            ProformaPresupuestoPlanificado pro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado item : proformaList) {
                pro.setCodigo(item.getCodigo());
                pro.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                pro.setCodigoReforma(BigInteger.valueOf(r.getId()));
                pro.setCondicion(item.getCondicion());
                pro.setEstado(item.getEstado());
                pro.setFuenteNew(item.getFuenteNew());
                pro.setEstructruaNew(item.getEstructruaNew());
                pro.setItemNew(item.getItemNew());
                pro.setFechaCreacion(item.getFechaCreacion());
                pro.setFechaModificacion(item.getFechaModificacion());
                pro.setGenerado(pro.getGenerado());
                pro.setCondicion(item.getCondicion());
                pro.setValor(item.getReformaCodificado());
                pro.setReformaSuplemento(BigDecimal.ZERO);
                pro.setReformaReduccion(BigDecimal.ZERO);
                pro.setTraspasoIncremento(BigDecimal.ZERO);
                pro.setTraspasoReduccion(BigDecimal.ZERO);
                pro.setUsuarioModificacion(userSession.getNameUser());
                pro.setPartidaPresupuestaria(item.getPartidaPresupuestaria());
                pro.setReformaCodificado(item.getValor().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
                pro.setPeriodo(r.getPeriodo());
                pro.setUsuarioCreacion(item.getUsuarioCreacion());
                pro.setGenerado(item.getGenerado());
                pro = proformaService.create(pro);
                pro = new ProformaPresupuestoPlanificado();

            }
        }

        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        proformaPresupuestoLazy.getFilterss().put("periodo:equal", r.getPeriodo());
        proformaPresupuestoLazy.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(r.getId()));
        //this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findEstadoValido", true, r.getPeriodo());
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
        this.listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), r.getPeriodo());
        this.listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), r.getPeriodo());

        cupoAsignado = BigDecimal.ZERO;

        totalCupoPDI = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;
        if (r.getTipo()) {
            columnaSuplmento = true;
            columnaReduccion = false;
            totalCupoPDI = valoresService.getCupoDistributivo(r, "PD");
            btnnuevo = false;
            totalCupoAsignado(BigInteger.valueOf(reformaGlobal.getId()));
        } else {
            columnaSuplmento = false;
            columnaReduccion = true;
            btnnuevo = true;
        }
        panelReforma = false;
        panelData = true;
        enabledReforma = false;
    }

    public void totalCupoAsignado(BigInteger b) {

        cupoAsignado = proformaService.totalSuplemento(b);
    }

    public void showPanel1() {
        panelReforma = true;
        panelData = false;
        enabledReforma = true;
    }

    public void savePdReforma() {
        if (proformaPresupuesto.getEstructruaNew() == null && proformaPresupuesto.getFuenteNew() == null && proformaPresupuesto.getItemNew() == null && (proformaPresupuesto.getValor() == null || proformaPresupuesto.getValor() == BigDecimal.ZERO)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Los Campos no deben estar vacios");
            return;

        }

        if (proformaPresupuesto.getValor() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Ingrese un valor ");
            return;
        }

        if (proformaPresupuesto.getValor().doubleValue() < 1) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Ingrese un valor mayor a cero");
            return;
        }

        List<ProformaPresupuestoPlanificado> listaverificandoPrueba = proformaService.getPdRefroma(BigInteger.valueOf(reformaGlobal.getId()));
        boolean bandera = false;
        proformaPresupuesto.setPartidaPresupuestaria(proformaPresupuesto.getEstructruaNew().getCodigo() + proformaPresupuesto.getItemNew().getCodigo() + proformaPresupuesto.getFuenteNew().getCodFuente());
        if (!listaverificandoPrueba.isEmpty()) {
            for (ProformaPresupuestoPlanificado v : listaverificandoPrueba) {
                if (proformaPresupuesto.getPartidaPresupuestaria().equals(v.getPartidaPresupuestaria())) {
                    bandera = true;
                }
            }
        }

        if (bandera) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Esta Partida ya existe");
            return;
        }

        String comprobandoPartida = proformaPresupuesto.getEstructruaNew().getCodigo() + proformaPresupuesto.getItemNew().getCodigo() + proformaPresupuesto.getFuenteNew().getCodFuente();

        BigDecimal result = proformaService.totalSuplemento(BigInteger.valueOf(reformaGlobal.getId()));
        //BigDecimal valorActual = proformaService.totalSuplementoActual(proformaPresupuesto);
        BigDecimal confirma = result.add(proformaPresupuesto.getValor());

        if (confirma.doubleValue() > totalCupoPDI.doubleValue()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No tiene suficiente Cupo");
            return;
        }

        proformaPresupuesto.setPartidaPresupuestaria(proformaPresupuesto.getEstructruaNew().getCodigo() + proformaPresupuesto.getItemNew().getCodigo() + proformaPresupuesto.getFuenteNew().getCodFuente());
        proformaPresupuesto.setCodigoReforma(BigInteger.valueOf(reformaGlobal.getId()));
        proformaPresupuesto.setCondicion(false);
        proformaPresupuesto.setGenerado(true);
        proformaPresupuesto.setCodigo("PDI");
        proformaPresupuesto.setEstado(true);
        proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
        proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
        proformaPresupuesto.setFechaCreacion(new Date());
        proformaPresupuesto.setPeriodo(reformaGlobal.getPeriodo());
        proformaPresupuesto.setFechaModificacion(new Date());
        proformaPresupuesto.setReformaSuplemento(proformaPresupuesto.getValor());
        proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
        proformaPresupuesto.setValor(BigDecimal.ZERO);
        proformaPresupuesto = proformaService.create(proformaPresupuesto);
        proformaPresupuesto = new ProformaPresupuestoPlanificado();
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Exitoso", "El registro se realizo correctamente");
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').hide()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");

        if (reformaGlobal.getTipo()) {
            totalCupoAsignado(BigInteger.valueOf(reformaGlobal.getId()));
        }
    }

    public void editarPartidaDirectaValor(ProformaPresupuestoPlanificado p) {
        proformaPresupuesto = new ProformaPresupuestoPlanificado();
        proformaPresupuesto = p;
        listItem = new ArrayList<>();
        listItem = catalogoServiceNew.findTipoPresupuesto(false);
        listEstructura = new ArrayList<>();
        listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
        listFuenteNew = new ArrayList<>();
        listFuenteNew = fuenteServiceNew.getFuenteFinanciamiento();
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");

    }

    public void cellEditPartidas(ProformaPresupuestoPlanificado p) {
        if (proformaPresupuesto.getEstructruaNew() == null && proformaPresupuesto.getFuenteNew() == null && proformaPresupuesto.getItemNew() == null) {
            p.setPartidaPresupuestaria(null);
        } else {
            p.setPartidaPresupuestaria(p.getEstructruaNew().getCodigo() + p.getItemNew().getCodigo() + p.getFuenteNew().getCodFuente());
            //p.setFuenteDirecta(p.getFuente().getTipoFuente());
        }
        proformaService.edit(p);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
    }

    public void cellEditSuplemento(ProformaPresupuestoPlanificado p) {

        BigDecimal result = proformaService.totalSuplemento(BigInteger.valueOf(reformaGlobal.getId()));
        BigDecimal valorActual = proformaService.totalSuplementoActual(p);
        BigDecimal confirma = result.subtract(valorActual).add(p.getReformaSuplemento());

        if (confirma.doubleValue() > totalCupoPDI.doubleValue()) {
            p.setReformaCodificado(p.getValor());
            p.setReformaSuplemento(BigDecimal.ZERO);
            proformaService.edit(p);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No tiene suficiente cupo");
        } else {

            p.setReformaCodificado(p.getValor().add(p.getReformaSuplemento()));
            proformaService.edit(p);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
        }

        if (reformaGlobal.getTipo()) {
            totalCupoAsignado(BigInteger.valueOf(reformaGlobal.getId()));
        }

    }

    public void cellEditReduccion(ProformaPresupuestoPlanificado p) {
        if (p.getReformaReduccion().doubleValue() > p.getValor().doubleValue()) {
            p.setReformaCodificado(BigDecimal.ZERO);
            p.setReformaReduccion(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "El valor de reduccion no puede ser maoyor al valor de original");
            return;
        }
        p.setReformaCodificado(p.getValor().subtract(p.getReformaReduccion()));
        proformaService.edit(p);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
    }

    public void abriDlgoPartidas() {
        proformaPresupuesto = new ProformaPresupuestoPlanificado();
        listItem = new ArrayList<>();
        listItem = catalogoServiceNew.findTipoPresupuesto(false);
        listEstructura = new ArrayList<>();
        listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
        listFuenteNew = new ArrayList<>();
        listFuenteNew = fuenteServiceNew.getFuenteFinanciamiento();
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");
    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }
//</editor-fold>

    public void abriDlogo(ReformaIngresoSuplemento r) {
        reformaGlobal = new ReformaIngresoSuplemento();
        reformaGlobal = r;

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
            //clienteService.getUnidadUserCodigo("JA", "6")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaGlobal = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public boolean isBtnnuevo() {
        return btnnuevo;
    }

    public void setBtnnuevo(boolean btnnuevo) {
        this.btnnuevo = btnnuevo;
    }

    public boolean isEnabledReforma() {
        return enabledReforma;
    }

    public void setEnabledReforma(boolean enabledReforma) {
        this.enabledReforma = enabledReforma;
    }

    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }

    public BigDecimal getTotalCupoPDI() {
        return totalCupoPDI;
    }

    public void setTotalCupoPDI(BigDecimal totalCupoPDI) {
        this.totalCupoPDI = totalCupoPDI;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuesto() {
        return proformaPresupuesto;
    }

    public void setProformaPresupuesto(ProformaPresupuestoPlanificado proformaPresupuesto) {
        this.proformaPresupuesto = proformaPresupuesto;
    }

    public boolean isColumnaSuplmento() {
        return columnaSuplmento;
    }

    public void setColumnaSuplmento(boolean columnaSuplmento) {
        this.columnaSuplmento = columnaSuplmento;
    }

    public boolean isColumnaReduccion() {
        return columnaReduccion;
    }

    public void setColumnaReduccion(boolean columnaReduccion) {
        this.columnaReduccion = columnaReduccion;
    }

    public boolean isPanelReforma() {
        return panelReforma;
    }

    public void setPanelReforma(boolean panelReforma) {
        this.panelReforma = panelReforma;
    }

    public boolean isPanelData() {
        return panelData;
    }

    public void setPanelData(boolean panelData) {
        this.panelData = panelData;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyReformas() {
        return lazyReformas;
    }

    public void setLazyReformas(LazyModel<ReformaIngresoSuplemento> lazyReformas) {
        this.lazyReformas = lazyReformas;
    }

    public ProformaPresupuestoPlanificado getProfroma() {
        return proformaPresupuesto;
    }

    public void setProfroma(ProformaPresupuestoPlanificado profroma) {
        this.proformaPresupuesto = profroma;
    }

    public List<ProformaPresupuestoPlanificado> getProformaList() {
        return proformaList;
    }

    public void setProformaList(List<ProformaPresupuestoPlanificado> proformaList) {
        this.proformaList = proformaList;
    }

    public LazyModel<ProformaPresupuestoPlanificado> getProformaPresupuestoLazy() {
        return proformaPresupuestoLazy;
    }

    public void setProformaPresupuestoLazy(LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy) {
        this.proformaPresupuestoLazy = proformaPresupuestoLazy;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }
//</editor-fold>

    public List<PresPlanProgramatico> getListEstructura() {
        return listEstructura;
    }

    public void setListEstructura(List<PresPlanProgramatico> listEstructura) {
        this.listEstructura = listEstructura;
    }

    public List<PresCatalogoPresupuestario> getListItem() {
        return listItem;
    }

    public void setListItem(List<PresCatalogoPresupuestario> listItem) {
        this.listItem = listItem;
    }

    public List<PresFuenteFinanciamiento> getListFuenteNew() {
        return listFuenteNew;
    }

    public void setListFuenteNew(List<PresFuenteFinanciamiento> listFuenteNew) {
        this.listFuenteNew = listFuenteNew;
    }

}
