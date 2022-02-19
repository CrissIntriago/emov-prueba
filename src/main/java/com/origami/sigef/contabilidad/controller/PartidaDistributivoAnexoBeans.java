/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author LuisPozoG
 */
@Named(value = "partidaDistributivoAnexoBeans")
@ViewScoped
public class PartidaDistributivoAnexoBeans extends BpmnBaseRoot implements Serializable {

    //catalogo master y busqueda
    private OpcionBusqueda busqueda;
    private List<MasterCatalogo> listaPeriodos;
    @Inject
    private MasterCatalogoService masterService;
    @Inject
    private UserSession user;
    @Inject
    private ClienteService clienteService;
    private Short periodo;
    //distributivo anexo
    private List<DistributivoAnexo> listaDistributivoAnexo;
    @Inject
    private DistributivoAnexoService distributivoAnexoService;
    //listas para sacar partida
    private List<CatalogoPresupuesto> listaItem;
    @Inject
    private CatalogoPresupuestoService itemService;
    private List<FuenteFinanciamiento> listaFuente;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    private List<PlanProgramatico> listaEstructura;
    @Inject
    private PlanProgramaticoService estructuraService;
    //lista partida distributivo
    private PartidasDistributivoAnexo partidaDistributivoAnexo;
    @Inject
    private PartidaDistributivoAnexoService PartidaDisAnexoService;
    private List<PartidasDistributivoAnexo> listaPartidasAnexo;//para mostrar
    @Inject
    private CatalogoService catalogoService;
    //bloqueo
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    private boolean bloqueo;

    @Inject
    private ServletSession ss;
    private short anio;

    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    private BigDecimal totalCupo, cupoAsignado;
    private String estadoGeneral;
    private String observaciones;
    private Short periodoConsultado;

    @PostConstruct
    public void inicializate() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodoConsultado = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                busqueda = new OpcionBusqueda();
                busqueda.setAnio(periodoConsultado);
                listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "D", periodoConsultado});
                this.listaDistributivoAnexo = new ArrayList<>();
                this.periodo = null;
                this.listaEstructura = new ArrayList<>();
                this.listaFuente = new ArrayList<>();
                this.listaItem = new ArrayList<>();
                partidaDistributivoAnexo = new PartidasDistributivoAnexo();
                this.bloqueo = true;
                totalCupo = BigDecimal.ZERO;
                cupoAsignado = BigDecimal.ZERO;
                estadoGeneral = "-";
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void updateCuposTotales() {
        totalCupo = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;
        estadoGeneral = "";
        totalCupo = cupoPresupuestoService.getCupoOtros("DA", BigInteger.valueOf(tramite.getNumTramite()));
        cupoAsignado = cupoPresupuestoService.getValorAsigandoDistributivoA(periodo);
        estadoGeneral = cupoPresupuestoService.getestadoDistributivoAnexoNomral(periodo);
    }

    public void registrarDistributivoAnexo() {

        if (periodo == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Eliga un Período");
            return;
        }

        cargarDatosGenerarPartida();
        listaDistributivoAnexo = new ArrayList<>();
        listaDistributivoAnexo = distributivoAnexoService.getDisAnexosNoExistInPartidaAnexo(periodo);
        CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RD");
        if (!listaDistributivoAnexo.isEmpty()) {
            for (DistributivoAnexo item : listaDistributivoAnexo) {
                partidaDistributivoAnexo = new PartidasDistributivoAnexo();
                partidaDistributivoAnexo.setDistributivoAnexo(item);
                partidaDistributivoAnexo.setEstado(Boolean.TRUE);
                partidaDistributivoAnexo.setPeriodo(periodo);
                partidaDistributivoAnexo.setUsuarioCreacion(user.getName());
                partidaDistributivoAnexo.setFechaCreacion(new Date());
                partidaDistributivoAnexo.setFechaModificacion(new Date());
                partidaDistributivoAnexo.setUsuarioModificacion(user.getName());
                partidaDistributivoAnexo.setReformaSuplemento(BigDecimal.ZERO);
                partidaDistributivoAnexo.setReformaReduccion(BigDecimal.ZERO);
                partidaDistributivoAnexo.setTraspasoReduccion(BigDecimal.ZERO);
                partidaDistributivoAnexo.setTraspasoIncremento(BigDecimal.ZERO);
                partidaDistributivoAnexo.setEstadoPartida(estado);
                partidaDistributivoAnexo.setMonto(item.getMonto());
                partidaDistributivoAnexo.setReformaCodificado(item.getMonto());
                partidaDistributivoAnexo = PartidaDisAnexoService.create(partidaDistributivoAnexo);
            }
        }
        listaPartidasAnexo = new ArrayList<>();
        listaPartidasAnexo = PartidaDisAnexoService.getDisAnexosEstadoFalse(periodo);
        if (!listaPartidasAnexo.isEmpty()) {
            for (PartidasDistributivoAnexo item : listaPartidasAnexo) {
                PartidaDisAnexoService.remove(item);
            }
        }

        if (periodo > 0) {
            setBloqueo(proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(periodo, false, true));
            listaPartidasAnexo = new ArrayList<>();
            listaPartidasAnexo = PartidaDisAnexoService.getPartidasDistributivoForYear(periodo);

        }
        updateCuposTotales();
    }

    public void cargarDatosGenerarPartida() {
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), periodo);
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);

    }

    public void EditCellAndCalculatePartida(PartidasDistributivoAnexo pa) {

        try {
            BigDecimal valorAsignado = cupoPresupuestoService.getValorAsigandoDistributivoA(periodo);
            BigDecimal valorActual = cupoPresupuestoService.getValorActualDA(pa);
            if (valorAsignado.add(pa.getMonto()).doubleValue() > totalCupo.doubleValue()) {
                pa.setItemApA(null);
                pa.setEstructuraApA(null);
                pa.setFuenteApA(null);
                pa.setFuenteDirectaA(null);
                pa.setPartidaAp(null);
                PartidaDisAnexoService.edit(pa);
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Aviso", "No hay sufuciente cupo");
                return;
            }

            if (pa.getFuenteApA() != null) {
                pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
            } else {
                pa.setFuenteDirectaA(null);
            }

            if (pa.getItemApA() != null && pa.getEstructuraApA() != null && pa.getFuenteApA() != null) {
                pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
                pa.setPartidaAp(pa.getEstructuraApA().getCodigo() + pa.getItemApA().getCodigo() + pa.getFuenteDirectaA().getOrden());
                if (PartidaDisAnexoService.validateIfExistPartidaInPeriodo(pa.getPartidaAp(), periodo)) {
                    pa.setFuenteDirectaA(null);
                    pa.setPartidaAp(null);
                    pa.setItemApA(null);
                    pa.setEstructuraApA(null);
                    pa.setFuenteApA(null);
                    // if (valorAsignado.add(pa.getMonto()).subtract(valorActual).doubleValue() > totalCupo.doubleValue()) {
                    pa.setMonto(BigDecimal.ZERO);
                    pa.setReformaCodificado(BigDecimal.ZERO);
                    JsfUtil.addWarningMessage("Advertencia", "La partida no puede ser igual a una partida ya ingresada en este periodo");

                } else {
//                    pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
//                    pa.setPartidaAp(pa.getEstructuraApA().getCodigo() + pa.getItemApA().getCodigo() + pa.getFuenteDirectaA().getOrden());
                    // if (valorAsignado.add(pa.getMonto()).subtract(valorActual).doubleValue() > totalCupo.doubleValue()) {
                    pa.setMonto(pa.getDistributivoAnexo().getMonto());
                    pa.setReformaCodificado(pa.getMonto());

                }

            } else {
                pa.setPartidaAp(null);
                pa.setMonto(pa.getDistributivoAnexo().getMonto());
                pa.setReformaCodificado(pa.getMonto());
            }
            PartidaDisAnexoService.edit(pa);
            updateCuposTotales();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deletePartida(PartidasDistributivoAnexo p) {
        try {
            p.setItemApA(null);
            p.setFuenteApA(null);
            p.setEstructuraApA(null);
            p.setPartidaAp(null);
            PartidaDisAnexoService.edit(p);
            listaPartidasAnexo = PartidaDisAnexoService.getPartidasDistributivoForYear(periodo);
            updateCuposTotales();
            JsfUtil.addSuccessMessage("Información", "Limpieza correcto");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "Ocurrio un Error, contactese con el administrador");
        }
    }

    public void opendlgPrint() {
        PrimeFaces.current().executeScript("PF('dlgPrint').show()");
    }

    public void printReport() {
        if (anio == 0) {
            JsfUtil.addWarningMessage("Información", " Ingrese un año para generar Reporte.");
            return;
        }
        boolean blockPrint = false;
        blockPrint = proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(anio, false, true);
        ss.addParametro("anio", anio);
        ss.addParametro("bloqueo", blockPrint);
        ss.setNombreReporte("PartidaDistributivoAnexo");
        ss.setNombreSubCarpeta("Distributivos");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        PrimeFaces.current().executeScript("PF('dlgPrint').hide()");

    }

    public void dlogoObservaciones() {
        if (periodo == null) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Eliga un Período");
            return;

        }

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
            getParamts().put("usuario", clienteService.getrolsUser("2"));
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

    //<editor-fold defaultstate="collapsed" desc="Get And Set">
    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }

    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }

    public String getEstadoGeneral() {
        return estadoGeneral;
    }

    public void setEstadoGeneral(String estadoGeneral) {
        this.estadoGeneral = estadoGeneral;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getListaPeriodos() {
        return listaPeriodos;
    }

    public void setListaPeriodos(List<MasterCatalogo> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public List<DistributivoAnexo> getListaDistributivoAnexo() {
        return listaDistributivoAnexo;
    }

    public void setListaDistributivoAnexo(List<DistributivoAnexo> listaDistributivoAnexo) {
        this.listaDistributivoAnexo = listaDistributivoAnexo;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public List<CatalogoPresupuesto> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<CatalogoPresupuesto> listaItem) {
        this.listaItem = listaItem;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<PlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }

    public void setListaEstructura(List<PlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
    }

    public List<PartidasDistributivoAnexo> getListaPartidasAnexo() {
        return listaPartidasAnexo;
    }

    public void setListaPartidasAnexo(List<PartidasDistributivoAnexo> listaPartidasAnexo) {
        this.listaPartidasAnexo = listaPartidasAnexo;
    }
//</editor-fold>

}
