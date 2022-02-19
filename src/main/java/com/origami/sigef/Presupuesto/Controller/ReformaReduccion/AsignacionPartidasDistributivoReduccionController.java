/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Model.PartidasGlobalesDistributivo;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
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
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.lazy.partidaDistribuidosLazy;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
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
@Named(value = "asigParDistReduView")
@ViewScoped
public class AsignacionPartidasDistributivoReduccionController extends BpmnBaseRoot implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES">
    private boolean panel1;
    private boolean pane21;

    private LazyModel<ReformaIngresoSuplemento> lazyReformas;

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
    @Inject
    private CatalogoService catalogoService;

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
    private UserSession userSession;
    private List<Distributivo> copiaDiReforma;

    private BigInteger codigoReformaTotal;
    private boolean columnaSuplemento, columnaReduccion;
    private ReformaIngresoSuplemento reformaDistr;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    private ReformaCupoReduccionService cupoReduccionService;

    private BigDecimal totalCupoD;
    private BigDecimal cupoAsignado;

//</editor-fold>
    private List<Distributivo> distributivosNuevosList;
    private List<PartidasDistributivo> partidasDistributivosListNuevos;
    private boolean panel3;
    private boolean panel4;
    private List<Distributivo> distributivosListModificaacion;
    private List<PartidasDistributivo> partidasDistributivosListModificacion;
    private String observaciones;
    private Boolean enabledReforma;
    private Distributivo mostrarData;
    private Distributivo mostrarDataView;
    private PartidasGlobalesDistributivo partidasGlobales;
    private List<PartidasGlobalesDistributivo> listaPartidaGlobales;

    @PostConstruct
    public void inicializador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
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
                lazyReformas = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReformas.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                panel1 = false;
                pane21 = true;
                panel3 = false;
                panel4 = false;
                distributivosListModificaacion = new ArrayList<>();;
                partidasDistributivosListModificacion = new ArrayList<>();;
                copiaDiReforma = new ArrayList<>();
                distributivosNuevosList = new ArrayList<>();
                partidasDistributivosListNuevos = new ArrayList<>();
                enabledReforma = true;
                partidasGlobales = new PartidasGlobalesDistributivo();
                listaPartidaGlobales = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="LOGICA NEGOCIO">
    public void actualizandoValores() {
        CupoReduccion cupoReduccion = cupoReduccionService.getValoresCupo("D", reformaDistr);
        totalCupoD = cupoReduccion.getMotoDisponible();
        cupoAsignado = cupoReduccionService.retornaValorReducido(BigInteger.valueOf(reformaDistr.getId()), "PD");
    }

    public BigDecimal mostarValoresPrincipal(ReformaIngresoSuplemento r) {
        CupoReduccion cupoReduccion = cupoReduccionService.getValoresCupo("D", r);
        return cupoReduccion.getMotoDisponible();
    }

    public void showPanel1() {
        panel1 = false;
        pane21 = true;
        panel3 = false;
        panel4 = false;
        enabledReforma = true;

    }

    public void registrarDistributivo(ReformaIngresoSuplemento r) {
        //usuario = new ArrayList<>();
        this.reformaDistr = new ReformaIngresoSuplemento();
        this.reformaDistr = r;
        periodo = r.getPeriodo();
        this.listaValoresDistributivo = new ArrayList<>();
        copiaDiReforma = new ArrayList<>();
        List<PartidasDistributivo> verificarList = valoresService.verificarReformaDis(BigInteger.valueOf(r.getId()));
        if (verificarList.isEmpty()) {

            listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
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
                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setMonto(item.getValorResultante());
                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
                    partidaDistributivo = partidasService.create(partidaDistributivo);
                }
            }

            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(periodo, estadoAprobado);

            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");

            if (!copiaRubrosPartidasReforma.isEmpty()) {
                PartidasDistributivo item = new PartidasDistributivo();
                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {
//                
                    item.setDistributivo(data.getDistributivo());
                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
                    item.setCodigoReforma(BigInteger.valueOf(r.getId()));
                    item.setEstado(data.isEstado());
                    item.setEstadoPartida(estado);
                    item.setEstructuraAp(data.getEstructuraAp());
                    item.setItemAp(data.getItemAp());
                    item.setFechaCreacion(new Date());
                    item.setFechaModificacion(new Date());
                    item.setFuenteAp(data.getFuenteAp());
                    item.setFuenteDirecta(data.getFuenteDirecta());
                    item.setPartidaAp(data.getPartidaAp());
                    item.setPeriodo(data.getPeriodo());
                    item.setUsuarioCreacion(userSession.getNameUser());
                    item.setUsuarioModificacion(userSession.getNameUser());
                    item.setReformaSuplemento(BigDecimal.ZERO);
                    item.setReformaReduccion(BigDecimal.ZERO);

                    item.setReformaCodificado(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));

                    item.setMonto(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));

                    item = partidasService.create(item);
                    item = new PartidasDistributivo();
                }

            }
        }
        distributivosListModificaacion = new ArrayList<>();
        distributivosListModificaacion = valoresService.getDistributivoFinalReformaModificacion(periodo, BigInteger.valueOf(r.getId()));

        actualizandoValores();
        codigoReformaTotal = BigInteger.ZERO;
        codigoReformaTotal = BigInteger.valueOf(r.getId());
        panel1 = false;
        pane21 = false;
        panel3 = false;
        panel4 = true;
        enabledReforma = false;
    }

    public void registrarDistributivoNulos(ReformaIngresoSuplemento r) {
        //usuario = new ArrayList<>();
        this.reformaDistr = new ReformaIngresoSuplemento();
        this.reformaDistr = r;
        periodo = r.getPeriodo();
        copiaDiReforma = new ArrayList<>();
        List<PartidasDistributivo> verificarList = valoresService.verificarReformaDis(BigInteger.valueOf(r.getId()));
        if (verificarList.isEmpty()) {

            listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
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
                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setMonto(item.getValorResultante());
                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
                    partidaDistributivo = partidasService.create(partidaDistributivo);
                }
            }

            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(periodo, estadoAprobado);

            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");

            if (!copiaRubrosPartidasReforma.isEmpty()) {
                PartidasDistributivo item = new PartidasDistributivo();
                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {

                    item.setDistributivo(data.getDistributivo());
                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
                    item.setCodigoReforma(BigInteger.valueOf(r.getId()));
                    item.setEstado(data.isEstado());
                    item.setEstadoPartida(estado);
                    item.setEstructuraAp(data.getEstructuraAp());
                    item.setItemAp(data.getItemAp());
                    item.setFechaCreacion(new Date());
                    item.setFechaModificacion(new Date());
                    item.setFuenteAp(data.getFuenteAp());
                    item.setFuenteDirecta(data.getFuenteDirecta());
                    item.setPartidaAp(data.getPartidaAp());
                    item.setPeriodo(data.getPeriodo());
                    item.setUsuarioCreacion(userSession.getNameUser());
                    item.setUsuarioModificacion(userSession.getNameUser());
                    item.setReformaSuplemento(BigDecimal.ZERO);
                    item.setReformaReduccion(BigDecimal.ZERO);

                    item.setReformaCodificado(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));

                    item.setMonto(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));

                    item = partidasService.create(item);
                    item = new PartidasDistributivo();
                }

            }
        }

        distributivosNuevosList = new ArrayList<>();
        distributivosNuevosList = valoresService.getDistributivoFinalReformaNulos(periodo, BigInteger.valueOf(r.getId()));
        codigoReformaTotal = BigInteger.ZERO;
        codigoReformaTotal = BigInteger.valueOf(r.getId());
        actualizandoValores();
        panel1 = false;
        pane21 = false;
        panel3 = true;
        panel4 = false;
    }

    public void registrarDistributivoModificacion(ReformaIngresoSuplemento r) {
        //usuario = new ArrayList<>();
        this.reformaDistr = new ReformaIngresoSuplemento();
        this.reformaDistr = r;
        periodo = r.getPeriodo();
        copiaDiReforma = new ArrayList<>();
        List<PartidasDistributivo> verificarList = valoresService.verificarReformaDis(BigInteger.valueOf(r.getId()));
        if (verificarList.isEmpty()) {

            listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
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
                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setMonto(item.getValorResultante());
                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
                    partidaDistributivo = partidasService.create(partidaDistributivo);
                }
            }

            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(periodo, estadoAprobado);

            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");

            if (!copiaRubrosPartidasReforma.isEmpty()) {
                PartidasDistributivo item = new PartidasDistributivo();
                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {

                    item.setDistributivo(data.getDistributivo());
                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
                    item.setCodigoReforma(BigInteger.valueOf(r.getId()));
                    item.setEstado(data.isEstado());
                    item.setEstadoPartida(estado);
                    item.setEstructuraAp(data.getEstructuraAp());
                    item.setItemAp(data.getItemAp());
                    item.setFechaCreacion(new Date());
                    item.setFechaModificacion(new Date());
                    item.setFuenteAp(data.getFuenteAp());
                    item.setFuenteDirecta(data.getFuenteDirecta());
                    item.setPartidaAp(data.getPartidaAp());
                    item.setPeriodo(data.getPeriodo());
                    item.setUsuarioCreacion(userSession.getNameUser());
                    item.setUsuarioModificacion(userSession.getNameUser());
                    item.setReformaSuplemento(BigDecimal.ZERO);
                    item.setReformaReduccion(BigDecimal.ZERO);

                    item.setReformaCodificado(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));

                    item.setMonto(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));

                    item = partidasService.create(item);
                    item = new PartidasDistributivo();
                }

            }
        }

        distributivosListModificaacion = new ArrayList<>();
        distributivosListModificaacion = valoresService.getDistributivoFinalReformaModificacion(periodo, BigInteger.valueOf(r.getId()));

        codigoReformaTotal = BigInteger.ZERO;
        codigoReformaTotal = BigInteger.valueOf(r.getId());

        panel4 = true;
    }

    public void abriDlgoPartidasPresupuestariaDistributiov(Distributivo d) {
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaRubros = valoresService.listaPresupuestoPartidasReforma(d, codigoReformaTotal);
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), periodo);
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
//        setRmu(valoresService.getRMu(d, periodo));

        if (reformaDistr.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }

        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivos').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivos");

    }

    public void abriDlgoPartidasPresupuestariaDistributiovNuevos(Distributivo d) {
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.partidasDistributivosListNuevos = valoresService.listaPresupuestoPartidasReformaNulos(d, codigoReformaTotal);
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), periodo);
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
//        setRmu(valoresService.getRMu(d, periodo));

        if (reformaDistr.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }

        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosNuevos').show()");
        PrimeFaces.current().ajax().update(":DlogopartidasDistributivosNuevos");

    }

    public void abriDlgoPartidasPresupuestariaDistributivoModificacion(Distributivo d) {
        mostrarData = new Distributivo();
        mostrarData = d;
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.partidasDistributivosListModificacion = valoresService.listaPresupuestoPartidasReformaModificar(d, codigoReformaTotal);
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), periodo);
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
//        setRmu(valoresService.getRMu(d, periodo));

        if (reformaDistr.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }

        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivos').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivos");

    }

    public BigDecimal revisarReserva(PartidasDistributivo v) {

        //CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
        return valoresService.verReserva(v.getPartidaAp(), reformaDistr.getPeriodo());
    }

    public void editarPartidas(PartidasDistributivo p) {
        if (p.getItemAp() != null && p.getEstructuraAp() != null && p.getFuenteDirecta() != null) {

            p.setPartidaAp(p.getEstructuraAp().getCodigo() + p.getItemAp().getCodigo() + p.getFuenteDirecta().getOrden());

        } else {

            p.setPartidaAp(null);
        }

        partidasService.edit(p);

    }

    public void editarCeldas(PartidasDistributivo p) {
        PartidasDistributivo par = valoresService.verValorActual(p);

        BigDecimal valorAntiguo = BigDecimal.ZERO;//valoresService.getValorAntiguoSuplementoDistribuvtivo(p);
        BigDecimal valor = BigDecimal.ZERO; //valoresService.getSumaDistributivosSuplemento(BigInteger.valueOf(reformaDistr.getId())).add(p.getDistributivo().getValorResultante());

        try {

            if (p.getFuenteAp() != null) {
                p.setFuenteDirecta(p.getFuenteAp().getTipoFuente());
            } else {
                p.setFuenteDirecta(null);
            }

            if (p.getItemAp() != null && p.getEstructuraAp() != null && p.getFuenteAp() != null) {
                p.setFuenteDirecta(p.getFuenteAp().getTipoFuente());
                p.setPartidaAp(p.getEstructuraAp().getCodigo() + p.getItemAp().getCodigo() + p.getFuenteDirecta().getOrden());

                if (valor.subtract(valorAntiguo).doubleValue() > totalCupoD.doubleValue()) {
                    p.setReformaSuplemento(BigDecimal.ZERO);
                    p.setReformaReduccion(BigDecimal.ZERO);
                    p.setReformaCodificado(BigDecimal.ZERO);
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Error", "No hay suiciente cupo");

                } else {
                    if (reformaDistr.getTipo()) {
                        if (p.getReformaSuplemento().doubleValue() > 0) {

                            if (p.getMonto().doubleValue() == 0) {

                                p.setReformaReduccion(BigDecimal.ZERO);
                                p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()));

                            }

                        } else {

                            p.setReformaSuplemento(p.getMonto());
                            p.setReformaReduccion(BigDecimal.ZERO);
                            p.setReformaCodificado(p.getMonto());
                            p.setMonto(BigDecimal.ZERO);

                        }

                    } else {
                        p.setReformaSuplemento(BigDecimal.ZERO);
                        p.setReformaReduccion(p.getMonto());
                        p.setReformaCodificado(p.getMonto());
                        p.setMonto(BigDecimal.ZERO);

                    }
                }

            } else {
                if (p.getFuenteAp() == null) {
                    p.setFuenteDirecta(null);
                }
                p.setPartidaAp(null);
                p.setReformaCodificado(BigDecimal.ZERO);
            }

            partidasService.edit(p);

        } catch (Exception e) {

        }

    }

    public void suplementoEdit(PartidasDistributivo p) {
        PartidasDistributivo par = null; //valoresService.verValorActual(p);
        BigDecimal valor = BigDecimal.ZERO;//valoresService.getSumaDistributivosSuplemento(BigInteger.valueOf(reformaDistr.getId())).add(p.getReformaSuplemento()).subtract(par.getReformaSuplemento());
        if (valor.doubleValue() > totalCupoD.doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No tiene suficiente cupo");
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaReduccion(BigDecimal.ZERO);
            p.setReformaCodificado(p.getMonto());
            return;
        }

        if (p.getMonto().doubleValue() == 0) {
            p.setReformaSuplemento(par.getReformaSuplemento());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No puede aumentar a un registro que es nuevo");
            return;
        }

        if (reformaDistr.getTipo()) {

        }

        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()).subtract(p.getReformaReduccion()));
        partidasService.edit(p);

    }

    public void reduccion(PartidasDistributivo p) {
        BigDecimal resultado = partidasService.reservadoGlobal(p.getPartidaAp(), reformaDistr.getPeriodo(), 3, BigInteger.valueOf(reformaDistr.getId())).subtract(partidasService.reservadoGlobal(p.getPartidaAp(), reformaDistr.getPeriodo(), 1, BigInteger.valueOf(reformaDistr.getId())));
        BigDecimal sumaReforma = partidasService.presupuestoPDReduccion(p.getPartidaAp(), reformaDistr.getPeriodo(), 1, BigInteger.valueOf(reformaDistr.getId()));
        PartidasDistributivo valorActual = valoresService.verValorActual(p);

        System.out.println(p.getMonto().subtract(p.getReformaReduccion()).setScale(2).doubleValue() + "\t\t" + p.getMonto().setScale(2).doubleValue());
        System.out.println(sumaReforma.setScale(2).doubleValue() + "\t\t" + resultado.setScale(2).doubleValue());

        if (0 > p.getMonto().subtract(p.getReformaReduccion()).setScale(2).doubleValue() || 0.00 > p.getMonto().subtract(p.getReformaReduccion()).setScale(2).doubleValue()) {
            p.setReformaReduccion(valorActual.getReformaReduccion());
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaCodificado(valorActual.getReformaCodificado());
            partidasService.edit(p);
            JsfUtil.addErrorMessage("Error", "El valor esa mayor al valor original");
            return;
        }

        if (sumaReforma.setScale(2).subtract(valorActual.getReformaReduccion()).add(p.getReformaReduccion()).doubleValue() > resultado.setScale(2).doubleValue()) {

            p.setReformaReduccion(valorActual.getReformaReduccion());
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaCodificado(valorActual.getReformaCodificado());
            partidasService.edit(p);
            JsfUtil.addErrorMessage("Error", "No puede hacer esta reduccion, verifique las reservade compromisos realizadas que esten aprobada y liquidsdas");
            System.out.println("entrando");
            return;
        }

        p.setReformaSuplemento(BigDecimal.ZERO);
        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()).subtract(p.getReformaReduccion()));
        partidasService.edit(p);
        actualizandoValores();
        JsfUtil.addInformationMessage("Información", "Se realizo la reducción correctamente");

    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public void listaVisualizacion(Distributivo d) {
        mostrarDataView = new Distributivo();
        mostrarDataView = d;
        listaVista = valoresService.listaPresupuestoPartidasReforma(d, BigInteger.valueOf(reformaDistr.getId()));
//        setRmu(valoresService.getRMu(d, periodo));
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivosvista");
    }

    public void opendlgPrint() {
        listUnidades = new ArrayList<>();
        listUnidades = unidadService.getUnidadesDistributivo();
        PrimeFaces.current().executeScript("PF('dlgPrint').show()");
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
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
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

    public void abriDlogo(ReformaIngresoSuplemento r) {
        reformaDistr = new ReformaIngresoSuplemento();
        reformaDistr = r;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones.toUpperCase());
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaDistr = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void abrirDlogoPartidasGlobales() {
        List<String> lista = new ArrayList<>();
        lista = partidasService.partidasGlobales(BigInteger.valueOf(reformaDistr.getId()));
        partidasGlobales = new PartidasGlobalesDistributivo();
        listaPartidaGlobales = new ArrayList<>();
        for (String data : lista) {
            partidasGlobales = new PartidasGlobalesDistributivo();
            partidasGlobales.setPartida(data);
            partidasGlobales.setReservado(partidasService.reservadoGlobal(data, reformaDistr.getPeriodo(), 1, BigInteger.valueOf(reformaDistr.getId())));
            partidasGlobales.setComprometido(partidasService.reservadoGlobal(data, reformaDistr.getPeriodo(), 2, BigInteger.valueOf(reformaDistr.getId())));
            partidasGlobales.setMontoReformado(partidasService.reservadoGlobal(data, reformaDistr.getPeriodo(), 3, BigInteger.valueOf(reformaDistr.getId())));
            partidasGlobales.setSaldoDisponiblel(partidasGlobales.getMontoReformado().subtract(partidasGlobales.getReservado()));
            partidasGlobales.setMontoReducido(partidasService.presupuestoPDReformado(data, reformaDistr.getPeriodo(), 1, BigInteger.valueOf(reformaDistr.getId())));
            partidasGlobales.setReduccion(partidasService.presupuestoPDReduccion(data, reformaDistr.getPeriodo(), 1, BigInteger.valueOf(reformaDistr.getId())));
            listaPartidaGlobales.add(partidasGlobales);
        }

        PrimeFaces.current().executeScript("PF('dlogoPartidasGlobales').show()");
        PrimeFaces.current().ajax().update("frmPartidasGlobales");

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="setter and getter">
    public BigInteger getCodigoReformaTotal() {
        return codigoReformaTotal;
    }

    public void setCodigoReformaTotal(BigInteger codigoReformaTotal) {
        this.codigoReformaTotal = codigoReformaTotal;
    }

    public List<PartidasGlobalesDistributivo> getListaPartidaGlobales() {
        return listaPartidaGlobales;
    }

    public void setListaPartidaGlobales(List<PartidasGlobalesDistributivo> listaPartidaGlobales) {
        this.listaPartidaGlobales = listaPartidaGlobales;
    }

    public Boolean getEnabledReforma() {
        return enabledReforma;
    }

    public void setEnabledReforma(Boolean enabledReforma) {
        this.enabledReforma = enabledReforma;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Distributivo> getDistributivosNuevosList() {
        return distributivosNuevosList;
    }

    public void setDistributivosNuevosList(List<Distributivo> distributivosNuevosList) {
        this.distributivosNuevosList = distributivosNuevosList;
    }

    public List<PartidasDistributivo> getPartidasDistributivosListNuevos() {
        return partidasDistributivosListNuevos;
    }

    public void setPartidasDistributivosListNuevos(List<PartidasDistributivo> partidasDistributivosListNuevos) {
        this.partidasDistributivosListNuevos = partidasDistributivosListNuevos;
    }

    public boolean isPanel3() {
        return panel3;
    }

    public void setPanel3(boolean panel3) {
        this.panel3 = panel3;
    }

    public boolean isPanel4() {
        return panel4;
    }

    public void setPanel4(boolean panel4) {
        this.panel4 = panel4;
    }

    public List<Distributivo> getDistributivosListModificaacion() {
        return distributivosListModificaacion;
    }

    public void setDistributivosListModificaacion(List<Distributivo> distributivosListModificaacion) {
        this.distributivosListModificaacion = distributivosListModificaacion;
    }

    public List<PartidasDistributivo> getPartidasDistributivosListModificacion() {
        return partidasDistributivosListModificacion;
    }

    public void setPartidasDistributivosListModificacion(List<PartidasDistributivo> partidasDistributivosListModificacion) {
        this.partidasDistributivosListModificacion = partidasDistributivosListModificacion;
    }

    public boolean isPanel1() {
        return panel1;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPane21() {
        return pane21;
    }

    public void setPane21(boolean pane21) {
        this.pane21 = pane21;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyReformas() {
        return lazyReformas;
    }

    public void setLazyReformas(LazyModel<ReformaIngresoSuplemento> lazyReformas) {
        this.lazyReformas = lazyReformas;
    }

    public List<UsuarioRol> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<UsuarioRol> usuario) {
        this.usuario = usuario;
    }

    public List<UnidadAdministrativa> getListUnidades() {
        return listUnidades;
    }

    public void setListUnidades(List<UnidadAdministrativa> listUnidades) {
        this.listUnidades = listUnidades;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Distributivo> getDistributivoListReport() {
        return distributivoListReport;
    }

    public void setDistributivoListReport(List<Distributivo> distributivoListReport) {
        this.distributivoListReport = distributivoListReport;
    }

    public List<ValoresDistributivo> getListValoresReport() {
        return listValoresReport;
    }

    public void setListValoresReport(List<ValoresDistributivo> listValoresReport) {
        this.listValoresReport = listValoresReport;
    }

    public List<Distributivo> getCopiaDiReforma() {
        return copiaDiReforma;
    }

    public void setCopiaDiReforma(List<Distributivo> copiaDiReforma) {
        this.copiaDiReforma = copiaDiReforma;
    }

    public boolean isColumnaSuplemento() {
        return columnaSuplemento;
    }

    public void setColumnaSuplemento(boolean columnaSuplemento) {
        this.columnaSuplemento = columnaSuplemento;
    }

    public boolean isColumnaReduccion() {
        return columnaReduccion;
    }

    public void setColumnaReduccion(boolean columnaReduccion) {
        this.columnaReduccion = columnaReduccion;
    }

    public ReformaIngresoSuplemento getReformaDistr() {
        return reformaDistr;
    }

    public void setReformaDistr(ReformaIngresoSuplemento reformaDistr) {
        this.reformaDistr = reformaDistr;
    }

    public BigDecimal getTotalCupoD() {
        return totalCupoD;
    }

    public void setTotalCupoD(BigDecimal totalCupoD) {
        this.totalCupoD = totalCupoD;
    }

    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }

    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
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

    public Distributivo getMostrarData() {
        return mostrarData;
    }

    public void setMostrarData(Distributivo mostrarData) {
        this.mostrarData = mostrarData;
    }

    public Distributivo getMostrarDataView() {
        return mostrarDataView;
    }

    public void setMostrarDataView(Distributivo mostrarDataView) {
        this.mostrarDataView = mostrarDataView;
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
