/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
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
@Named(value = "asignacionPdirectasReduccionView")
@ViewScoped
public class AsignacionPartidasDireReduccionController extends BpmnBaseRoot implements Serializable {
//<editor-fold defaultstate="collapsed" desc="VARIABLES">

    @Inject
    private ReformaCupoReduccionService cupoReduccionService;
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
    private ClienteService clienteService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    @Inject
    private UserSession userSession;
    private ProformaPresupuestoPlanificado proformaPresupuesto;
    private List<ProformaPresupuestoPlanificado> proformaList;
    private LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;
    private ReformaIngresoSuplemento reformaGlobal;
    private BigDecimal totalCupoPDI;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
//</editor-fold>
    private boolean btnnuevo;
    private BigDecimal cupoAsignado;
    private String observaciones;
    private Boolean enabledReformas;

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
                lazyReformas.getFilterss().put("tipo:equal", false);
                proformaList = new ArrayList<>();
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
                reformaGlobal = new ReformaIngresoSuplemento();
                columnaSuplmento = false;
                columnaReduccion = false;
                enabledReformas = true;
                listaPartidasDirectas = new ArrayList<>();
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
                pro.setEstructuraProgramatica(item.getEstructuraProgramatica());
                pro.setItemPresupuestario(item.getItemPresupuestario());
                pro.setFechaCreacion(item.getFechaCreacion());
                pro.setFechaModificacion(item.getFechaModificacion());
                pro.setFuente(item.getFuente());
                pro.setFuenteDirecta(item.getFuenteDirecta());
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

        List<ProformaPresupuestoPlanificado> lista = proformaService.getPdRefroma(BigInteger.valueOf(r.getId()));
        listaPartidasDirectas = new ArrayList<>();
        for (ProformaPresupuestoPlanificado item : lista) {
            ProformaPresupuestoPlanificado prof = new ProformaPresupuestoPlanificado();
            prof = Utils.clone(item);
            prof.setReserva(proformaService.getMontoReservado(item.getPartidaPresupuestaria(), r.getPeriodo()));
            listaPartidasDirectas.add(prof);
        }

        //this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findEstadoValido", true, r.getPeriodo());
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
        this.listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), r.getPeriodo());
        this.listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), r.getPeriodo());

        cupoAsignado = BigDecimal.ZERO;
        actulizarValores();
        if (r.getTipo()) {
            columnaSuplmento = true;
            columnaReduccion = false;

            btnnuevo = false;

        } else {
            columnaSuplmento = false;
            columnaReduccion = true;
            btnnuevo = true;
        }
        panelReforma = false;
        panelData = true;
        enabledReformas = false;

    }

    public void actulizarValores() {
        totalCupoPDI = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;
        CupoReduccion c = cupoReduccionService.getValoresCupo("PD", reformaGlobal);
        totalCupoPDI = c.getMotoDisponible();
        cupoAsignado = cupoReduccionService.retornaValorReducido(BigInteger.valueOf(reformaGlobal.getId()), "PDI");

    }

    public BigDecimal mostrarValorPrincipal(ReformaIngresoSuplemento r) {
        CupoReduccion c = cupoReduccionService.getValoresCupo("PD", r);
        return c.getMotoDisponible();
    }

    public void showPanel1() {
        panelReforma = true;
        panelData = false;
        enabledReformas = true;
    }

    public void savePdReforma() {
        if (proformaPresupuesto.getEstructuraProgramatica() == null && proformaPresupuesto.getFuente() == null && proformaPresupuesto.getItemPresupuestario() == null && (proformaPresupuesto.getValor() == null || proformaPresupuesto.getValor() == BigDecimal.ZERO)) {
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
        proformaPresupuesto.setPartidaPresupuestaria(proformaPresupuesto.getEstructuraProgramatica().getCodigo() + proformaPresupuesto.getItemPresupuestario().getCodigo() + proformaPresupuesto.getFuente().getTipoFuente().getOrden());
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

        String comprobandoPartida = proformaPresupuesto.getEstructuraProgramatica().getCodigo()
                + proformaPresupuesto.getItemPresupuestario().getCodigo() + proformaPresupuesto.getFuente().getTipoFuente().getOrden();

        BigDecimal result = proformaService.totalSuplemento(BigInteger.valueOf(reformaGlobal.getId()));
        //BigDecimal valorActual = proformaService.totalSuplementoActual(proformaPresupuesto);
        BigDecimal confirma = result.add(proformaPresupuesto.getValor());

        if (confirma.doubleValue() > totalCupoPDI.doubleValue()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No tiene suficiente Cupo");
            return;
        }

        proformaPresupuesto.setPartidaPresupuestaria(proformaPresupuesto.getEstructuraProgramatica().getCodigo() + proformaPresupuesto.getItemPresupuestario().getCodigo() + proformaPresupuesto.getFuente().getTipoFuente().getOrden());
        proformaPresupuesto.setFuenteDirecta(proformaPresupuesto.getFuente().getTipoFuente());
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

        }
    }

    public void editarPartidaDirectaValor(ProformaPresupuestoPlanificado p) {
        proformaPresupuesto = new ProformaPresupuestoPlanificado();
        proformaPresupuesto = p;
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");

    }

    public void cellEditPartidas(ProformaPresupuestoPlanificado p) {
        if (proformaPresupuesto.getEstructuraProgramatica() == null && proformaPresupuesto.getFuente() == null && proformaPresupuesto.getItemPresupuestario() == null) {
            p.setPartidaPresupuestaria(null);
        } else {
            p.setPartidaPresupuestaria(p.getEstructuraProgramatica().getCodigo() + p.getItemPresupuestario().getCodigo() + p.getFuente().getTipoFuente().getOrden());
            p.setFuenteDirecta(p.getFuente().getTipoFuente());
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

        }

    }

    public void cellEditReduccion(ProformaPresupuestoPlanificado p) {
        BigDecimal resultado = BigDecimal.ZERO;
        resultado = p.getValor().subtract(p.getReserva());
        ProformaPresupuestoPlanificado valorActual = proformaService.getActual(p);

        if (p.getReformaReduccion().doubleValue() > resultado.doubleValue()) {
            p.setReformaCodificado(valorActual.getReformaCodificado());
            p.setReformaReduccion(valorActual.getReformaReduccion());
            proformaService.edit(p);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "El valor de reduccion no puede ser maoyor al valor de original");
            return;
        }
        p.setReformaCodificado(p.getValor().subtract(p.getReformaReduccion()));
        proformaService.edit(p);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
        actulizarValores();

    }

    public void abriDlgoPartidas() {
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");
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

    public Boolean getEnabledReformas() {
        return enabledReformas;
    }

    public void setEnabledReformas(Boolean enabledReformas) {
        this.enabledReformas = enabledReformas;
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

    public List<ProformaPresupuestoPlanificado> getListaPartidasDirectas() {
        return listaPartidasDirectas;
    }

    public void setListaPartidasDirectas(List<ProformaPresupuestoPlanificado> listaPartidasDirectas) {
        this.listaPartidasDirectas = listaPartidasDirectas;
    }
    
    

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }
    
    
//</editor-fold>

}
