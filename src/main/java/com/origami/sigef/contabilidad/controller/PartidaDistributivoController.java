/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.lazy.partidaDistribuidosLazy;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.talentohumano.services.DistributivoService;
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
@Named(value = "asignacionDistributivoView")
@ViewScoped
public class PartidaDistributivoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private PartidaDistributivoService partidasService;
    @Inject
    private UserSession user;
    @Inject
    private PlanProgramaticoService estructuraService;
    @Inject
    private CatalogoPresupuestoService itemService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private ClienteService clienteService;

    private List<MasterCatalogo> listaPeriodo;
    private List<Distributivo> listaDistributivo;
    private List<ValoresDistributivo> listaValoresDistributivo;
    private partidaDistribuidosLazy lazy;
    private Short periodo;
    private PartidasDistributivo partidaDistributivo;
    private List<PartidasDistributivo> listaRubros;
    private List<PlanProgramatico> listaEstructura;
    private List<CatalogoPresupuesto> listaItem;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PartidasDistributivo> listaVista;
    private boolean bloqueo;
    private List<UsuarioRol> usuario;
    private BigDecimal rmu;

    @Inject
    private ServletSession ss;
    private short anio;
    @Inject
    private UnidadAdministrativaService unidadService;
    private List<UnidadAdministrativa> listUnidades;
    private Long id;
    private List<Distributivo> distributivoListReport;
    @Inject
    private DistributivoService disService;
    private List<ValoresDistributivo> listValoresReport;
    @Inject
    private CatalogoService catalogoService;
    private BigDecimal totalCupo, cupoAsignado;
    private String estadoGeneral;
    private String observaciones;
    private Short periodoConsultado;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    private Distributivo mostrarData;
    private Distributivo mostrarDataview;

    @PostConstruct
    public void inicializador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodoConsultado = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "D", periodoConsultado});
                this.periodo = null;
                this.listaValoresDistributivo = new ArrayList<>();
                this.listaDistributivo = new ArrayList<>();
                this.listaRubros = new ArrayList<>();
                partidaDistributivo = new PartidasDistributivo();
                this.listaEstructura = new ArrayList<>();
                this.listaFuente = new ArrayList<>();
                this.listaItem = new ArrayList<>();
                this.listaVista = new ArrayList<>();
                this.bloqueo = true;
                usuario = new ArrayList<>();
                totalCupo = BigDecimal.ZERO;
                cupoAsignado = BigDecimal.ZERO;
                estadoGeneral = "";

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateCupoDistributivo() {
        totalCupo = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;
        estadoGeneral = "";
        totalCupo = cupoPresupuestoService.getCupoOtros("D", BigInteger.valueOf(tramite.getNumTramite()));
        cupoAsignado = cupoPresupuestoService.getCupoAsignadoDistributivo(periodo);
        estadoGeneral = cupoPresupuestoService.getestadoDistributivoNomral(periodo);
    }

    public void registrarDistributivo() {
        usuario = new ArrayList<>();
//        List<CatalogoProformaPresupuesto> li = new ArrayList<>();
        if (periodo == null) {
            this.listaDistributivo.clear();
//            li.clear();
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Eliga un Período");
            return;

        }

        this.listaValoresDistributivo = new ArrayList<>();
//        boolean verificar = true;

        listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
        CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RD");
        if (listaValoresDistributivo.size() > 0) {
            for (ValoresDistributivo item : listaValoresDistributivo) {
                partidaDistributivo = new PartidasDistributivo();
                partidaDistributivo.setDistributivo(item);
                partidaDistributivo.setEstado(Boolean.TRUE);
                partidaDistributivo.setPeriodo(periodo);
                partidaDistributivo.setUsuarioCreacion(user.getName());
                partidaDistributivo.setFechaCreacion(new Date());
                partidaDistributivo.setUsuarioModificacion(user.getName());
                partidaDistributivo.setFechaModificacion(new Date());
                partidaDistributivo.setEstadoPartida(estado);
                partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
                partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
                partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
                partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
                if (partidaDistributivo.getDistributivo().getValorResultante() == null) {
                    partidaDistributivo.getDistributivo().setValorResultante(BigDecimal.ZERO);
                }
                if (partidaDistributivo.getDistributivo().getValorResultante().setScale(2).compareTo(BigDecimal.ZERO.setScale(2)) == 0) {
                    partidaDistributivo.setMonto(BigDecimal.ZERO);
                    partidaDistributivo.setReformaCodificado(BigDecimal.ZERO);
                } else {
                    partidaDistributivo.setMonto(partidaDistributivo.getDistributivo().getValorResultante());
                    partidaDistributivo.setReformaCodificado(partidaDistributivo.getDistributivo().getValorResultante());
                }
                partidaDistributivo = partidasService.create(partidaDistributivo);

            }

        }
        if (periodo.intValue() > 0) {

            setBloqueo(proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(periodo, false, true));

//            li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, false, true);
            this.listaDistributivo = valoresService.getDistributivoFinal(periodo);
//            if (li.size() > 0) {
//                verificar = true;
//            } else {
//                verificar = false;
//            }
//            setBloqueo(verificar);

        } else {
            this.listaDistributivo = null;
        }
        //
        ////        else {
        ////            PrimeFaces.current().ajax().update("messages");
        ////            JsfUtil.addWarningMessage("Unidad Administrativa", "No hay Distributivo vigente para este anio");
        ////        }
        updateCupoDistributivo();
    }

    public void abriDlgoPartidasPresupuestariaDistributiov(Distributivo d) {
        mostrarData = new Distributivo();
        mostrarData = d;
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaRubros = valoresService.listaPresupuestoPartidas(d, periodo);
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, (short) 4, periodo);
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, (short) 3, periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
//        setRmu(valoresService.getRMu(d, periodo));
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivos').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivos");

    }

    public void editarCeldas(PartidasDistributivo p) {

        try {
            BigDecimal valorCupoAsignado = cupoPresupuestoService.getValorActualDisributivo(p);
            BigDecimal totalCu = cupoPresupuestoService.getCupoAsignadoDistributivo(periodo);
            BigDecimal total = cupoPresupuestoService.getCupoOtros("D", BigInteger.valueOf(tramite.getNumTramite()));
            if (totalCu.add(p.getDistributivo().getValorResultante()).doubleValue() > total.doubleValue()) {
                p.setFuenteAp(null);
                p.setFuenteDirecta(null);
                p.setPartidaAp(null);
                p.setItemAp(null);
                p.setEstructuraAp(null);
                partidasService.edit(p);
                updateCupoDistributivo();
                JsfUtil.addWarningMessage("Aviso", "No tiene suficiente cupo para asiganarle partida");
                PrimeFaces.current().ajax().update("formDlogopartidasDistributivos");
                return;
            }

            if (p.getFuenteAp() != null) {
                p.setFuenteDirecta(p.getFuenteAp().getTipoFuente());
            } else {
                p.setFuenteDirecta(null);
            }

            if (p.getItemAp() != null && p.getEstructuraAp() != null && p.getFuenteAp() != null) {
                p.setFuenteDirecta(p.getFuenteAp().getTipoFuente());
                p.setPartidaAp(p.getEstructuraAp().getCodigo() + p.getItemAp().getCodigo() + p.getFuenteDirecta().getOrden());
                p.setMonto(p.getDistributivo().getValorResultante());
                p.setReformaCodificado(p.getMonto());

            } else {
                p.setPartidaAp(null);

            }

            partidasService.edit(p);
            updateCupoDistributivo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("AVISO", "OCURRIO UUN ERROR CONTACTESE CON EL ADMINISTRADOR DEL SISTEMA");
        }

    }

    public void listaVisualizacion(Distributivo d) {
        mostrarDataview = new Distributivo();
        mostrarDataview = d;
//        setRmu(valoresService.getRMu(d, periodo));
        listaVista = valoresService.listaPresupuestoPartidas(d, periodo);
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivosvista");
    }

    public void opendlgPrint() {
        listUnidades = new ArrayList<>();
        listUnidades = unidadService.getUnidadesDistributivo();
        PrimeFaces.current().executeScript("PF('dlgPrint').show()");
    }

    public void deletePartida(PartidasDistributivo p) {
        try {
            p.setItemAp(null);
            p.setFuenteAp(null);
            p.setEstructuraAp(null);
            p.setPartidaAp(null);
            partidasService.edit(p);
            this.listaDistributivo = valoresService.getDistributivoFinal(periodo);
            updateCupoDistributivo();
            JsfUtil.addInformationMessage("Información", "Limpieza Correcto");
            PrimeFaces.current().ajax().update("formDlogopartidasDistributivos:lista");
            PrimeFaces.current().ajax().update("formDlogopartidasDistributivos");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "Ocurrio un Error, contactese con el administrador");

        }
    }

    public void printReport() {
        distributivoListReport = new ArrayList<>();
        if (anio == 0) {
            JsfUtil.addWarningMessage("Información", " Ingrese un año para generar Reporte.");
            return;
        }
        if (id == 0) {
            ss.addParametro("anio", anio);
            distributivoListReport = partidasService.getAllPartidaDistributivo(anio);
            calcularValorReporte(distributivoListReport);
            ss.setNombreReporte("partidaDistributivoAll");
            ss.setNombreSubCarpeta("Distributivos");
        } else {
            ss.addParametro("anio", anio);
            ss.addParametro("id", id);
            distributivoListReport = partidasService.getPartidaDistributivoXperiodoUnidad(anio, id);
            calcularValorReporte(distributivoListReport);
            ss.setNombreReporte("partidaDistributivo");
            ss.setNombreSubCarpeta("Distributivos");
        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        PrimeFaces.current().executeScript("PF('dlgPrint').hide()");

    }

    public void calcularValorReporte(List<Distributivo> listDistributivo) {
        BigDecimal aporteLosep = BigDecimal.ZERO;
        BigDecimal aporteCt = BigDecimal.ZERO;
        BigDecimal decimoT = BigDecimal.ZERO;
        BigDecimal decimoC = BigDecimal.ZERO;
        BigDecimal fondosr = BigDecimal.ZERO;
        BigDecimal rau = BigDecimal.ZERO;

        if (!listDistributivo.isEmpty()) {
            for (Distributivo item : listDistributivo) {
                listValoresReport = new ArrayList<>();
                listValoresReport = disService.findByListaValoresMostrar(item, anio);
                if (!listValoresReport.isEmpty()) {
                    for (ValoresDistributivo itemValor : listValoresReport) {
                        if ("ACT".equals(itemValor.getValoresParametrizados().getTipo().getCodigo())) {
                            aporteCt = aporteCt.add(itemValor.getValorResultante());
                        }
                        if ("ALOSEP".equals(itemValor.getValoresParametrizados().getTipo().getCodigo())) {
                            aporteLosep = aporteLosep.add(itemValor.getValorResultante());
                        }
                        if ("DT".equals(itemValor.getValoresParametrizados().getTipo().getCodigo())) {
                            decimoT = decimoT.add(itemValor.getValorResultante());
                        }
                        if ("DC".equals(itemValor.getValoresParametrizados().getTipo().getCodigo())) {
                            decimoC = decimoC.add(itemValor.getValorResultante());
                        }
                        if ("FR".equals(itemValor.getValoresParametrizados().getTipo().getCodigo())) {
                            fondosr = fondosr.add(itemValor.getValorResultante());
                        }
                        if ("RAU".equals(itemValor.getValoresParametrizados().getTipo().getCodigo())) {
                            rau = rau.add(itemValor.getValorResultante());
                        }
                    }
                }
            }
        }
        ss.addParametro("aporteC", aporteCt);
        ss.addParametro("aporteL", aporteLosep);
        ss.addParametro("decT", decimoT);
        ss.addParametro("decC", decimoC);
        ss.addParametro("fondosR", fondosr);
        ss.addParametro("rau", rau);
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
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto)); //PRESUPUESTO 2
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

    //<editor-fold defaultstate="collapsed" desc="setter and getter">
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Distributivo getMostrarData() {
        return mostrarData;
    }

    public void setMostrarData(Distributivo mostrarData) {
        this.mostrarData = mostrarData;
    }

    public Distributivo getMostrarDataview() {
        return mostrarDataview;
    }

    public void setMostrarDataview(Distributivo mostrarDataview) {
        this.mostrarDataview = mostrarDataview;
    }

    public List<UnidadAdministrativa> getListUnidades() {
        return listUnidades;
    }

    public void setListUnidades(List<UnidadAdministrativa> listUnidades) {
        this.listUnidades = listUnidades;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<Distributivo> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<Distributivo> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<ValoresDistributivo> getListaValoresDistributivo() {
        return listaValoresDistributivo;
    }

    public void setListaValoresDistributivo(List<ValoresDistributivo> listaValoresDistributivo) {
        this.listaValoresDistributivo = listaValoresDistributivo;
    }

    public partidaDistribuidosLazy getLazy() {
        return lazy;
    }

    public void setLazy(partidaDistribuidosLazy lazy) {
        this.lazy = lazy;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public PartidasDistributivo getPartidaDistributivo() {
        return partidaDistributivo;
    }

    public void setPartidaDistributivo(PartidasDistributivo partidaDistributivo) {
        this.partidaDistributivo = partidaDistributivo;
    }

    public List<PartidasDistributivo> getListaRubros() {
        return listaRubros;
    }

    public void setListaRubros(List<PartidasDistributivo> listaRubros) {
        this.listaRubros = listaRubros;
    }

    public List<PlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }

    public void setListaEstructura(List<PlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
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

    public List<PartidasDistributivo> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<PartidasDistributivo> listaVista) {
        this.listaVista = listaVista;
    }

    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
    }

//</editor-fold>
}
