/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
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
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
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
@Named(value = "asignacionDistributivoReformaView")
@ViewScoped
public class AsignacionDistributivoReformaController extends BpmnBaseRoot implements Serializable {

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
    ;
    
    private Short periodo;
    private PartidasDistributivo partidaDistributivo;
    private List<ThCargoRubros> listaRubros;
    private List<PresPlanProgramatico> listaEstructura;
    private List<PresCatalogoPresupuestario> listaItem;
    private List<PresFuenteFinanciamiento> listaFuente;
    private List<ThCargoRubros> listaVista;
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
    private List<ThCargo> copiaDiReforma;
    private List<ThCargoRubros> copiaDiReformaAnexo;

    private BigInteger codigoReformaTotal;
    private boolean columnaSuplemento, columnaReduccion;
    private ReformaIngresoSuplemento reformaDistr;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    private BigDecimal totalCupoD;
    private BigDecimal cupoAsignado;
    private BigDecimal totalCupoDA;
    private BigDecimal cupoAsignadoDA;
    @Inject
    private CuposService cupoService;
//</editor-fold>

    private List<ThCargo> distributivosNuevosList;
    private List<ThCargoRubros> partidasDistributivosListNuevos;
    private List<ThCargoRubros> partidasDistributivosListNuevosAnexo;
    private boolean panel3;
    private boolean panel4;
    private List<ThCargo> distributivosListModificaacion;
    private List<ThCargoRubros> partidasDistributivosListModificacion;
    private List<ThCargoRubros> partidasDistributivosListModificacionAnexo;
    private String observaciones;
    private boolean enabledReformas;
    private ThCargo mostrarData, dataView;
    @Inject
    private PresCatalogoPresupuestarioService catalogoServiceNew;
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private PresPlanProgramaticoService estructuraServiceNew;

    @Inject
    private ThCargoRubrosService thCargoRubrosService;

    @PostConstruct
    public void inicializador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
                this.periodo = null;
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
                lazyReformas.getFilterss().put("tipo:equal", true);
                panel1 = false;
                pane21 = true;
                panel3 = false;
                panel4 = false;
                distributivosListModificaacion = new ArrayList<>();;
                partidasDistributivosListModificacion = new ArrayList<>();;
                enabledReformas = true;
                copiaDiReforma = new ArrayList<>();
                distributivosNuevosList = new ArrayList<>();
                partidasDistributivosListNuevos = new ArrayList<>();
                mostrarData = new ThCargo();
                dataView = new ThCargo();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="LOGICA NEGOCIO">
    public BigDecimal cupoSuplemento(ReformaIngresoSuplemento r) {
        if (r.getTipo()) {
            Cupos c = cupoService.getCodigoCupo(r, "D");

            if (c == null) {
                return BigDecimal.ZERO;
            } else {
                return c.getMontoCupo();
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void showPanel1() {
        panel1 = false;
        pane21 = true;
        panel3 = false;
        panel4 = false;
        enabledReformas = true;
    }

    public void registrarDistributivo(ReformaIngresoSuplemento r) {
        //usuario = new ArrayList<>();
        this.reformaDistr = new ReformaIngresoSuplemento();
        this.reformaDistr = r;
        periodo = r.getPeriodo();

        copiaDiReforma = new ArrayList<>();

        totalCupoD = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;

        if (r.getTipo()) {
            totalCupoD = valoresService.getCupoDistributivo(r, "D");
            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
            totalcupoAsignado();
            totalcupoAsignadoDa();
        } else {

            totalCupoD = BigDecimal.ZERO;
            cupoAsignado = BigDecimal.ZERO;
            totalCupoDA = BigDecimal.ZERO;
            cupoAsignadoDA = BigDecimal.ZERO;
        }
        copiaDiReforma = valoresService.getDistributivoReforma(periodo, BigInteger.valueOf(r.getId()));

        copiaDiReformaAnexo = new ArrayList<>();
        copiaDiReformaAnexo = valoresService.getDistributivoAnexoReforma(periodo, BigInteger.valueOf(r.getId()));
        codigoReformaTotal = BigInteger.ZERO;
        codigoReformaTotal = BigInteger.valueOf(r.getId());
        panel1 = true;
        pane21 = false;
        panel3 = false;
        panel4 = false;
        enabledReformas = false;
    }

    public void registrarDistributivoNulos(ReformaIngresoSuplemento r) {
        //usuario = new ArrayList<>();
        this.reformaDistr = new ReformaIngresoSuplemento();
        this.reformaDistr = r;
        periodo = r.getPeriodo();
        copiaDiReforma = new ArrayList<>();
//        List<PartidasDistributivo> verificarList = valoresService.verificarReformaDis(BigInteger.valueOf(r.getId()));
//        if (verificarList.isEmpty()) {
//
//            listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
//            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            if (listaValoresDistributivo.size() > 0) {
//                for (ValoresDistributivo item : listaValoresDistributivo) {
//                    partidaDistributivo = new PartidasDistributivo();
//                    partidaDistributivo.setDistributivo(item);
//                    partidaDistributivo.setEstado(Boolean.TRUE);
//                    partidaDistributivo.setPeriodo(periodo);
//                    partidaDistributivo.setUsuarioCreacion(user.getName());
//                    partidaDistributivo.setFechaCreacion(new Date());
//                    partidaDistributivo.setUsuarioModificacion(user.getName());
//                    partidaDistributivo.setFechaModificacion(new Date());
//                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
//                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
//                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
//                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
//                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
//                    partidaDistributivo.setMonto(item.getValorResultante());
//                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
//                    partidaDistributivo = partidasService.create(partidaDistributivo);
//                }
//            }
//
//            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(periodo, estadoAprobado);
//
//            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");
//
//            if (!copiaRubrosPartidasReforma.isEmpty()) {
//                PartidasDistributivo item = new PartidasDistributivo();
//                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {
//
//                    item.setDistributivo(data.getDistributivo());
//                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
//                    item.setCodigoReforma(BigInteger.valueOf(r.getId()));
//                    item.setEstado(data.isEstado());
//                    item.setEstadoPartida(estado);
//                    item.setEstructuraAp(data.getEstructuraAp());
//                    item.setItemAp(data.getItemAp());
//                    item.setFechaCreacion(new Date());
//                    item.setFechaModificacion(new Date());
//                    item.setFuenteAp(data.getFuenteAp());
//                    item.setFuenteDirecta(data.getFuenteDirecta());
//                    item.setPartidaAp(data.getPartidaAp());
//                    item.setPeriodo(data.getPeriodo());
//                    item.setUsuarioCreacion(userSession.getNameUser());
//                    item.setUsuarioModificacion(userSession.getNameUser());
//                    item.setReformaSuplemento(BigDecimal.ZERO);
//                    item.setReformaReduccion(BigDecimal.ZERO);
//
//                    item.setReformaCodificado(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//
//                    item.setMonto(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//
//                    item = partidasService.create(item);
//                    item = new PartidasDistributivo();
//                }
//
//            }
//        }

        totalCupoD = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;

        if (r.getTipo()) {
            totalCupoD = valoresService.getCupoDistributivo(r, "D");
            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
            totalcupoAsignado();
            totalcupoAsignadoDa();
        } else {

            totalCupoD = BigDecimal.ZERO;
            cupoAsignado = BigDecimal.ZERO;
        }
        distributivosNuevosList = new ArrayList<>();
        distributivosNuevosList = valoresService.getDistributivoReformaNulos(periodo, BigInteger.valueOf(r.getId()));
        partidasDistributivosListNuevosAnexo = new ArrayList<>();
        partidasDistributivosListNuevosAnexo = valoresService.getDistributivoReformaNulosAnexo(periodo, BigInteger.valueOf(r.getId()));
        codigoReformaTotal = BigInteger.ZERO;
        codigoReformaTotal = BigInteger.valueOf(r.getId());
        panel1 = false;
        pane21 = false;
        panel3 = true;
        panel4 = false;
        enabledReformas = false;
    }

    public void registrarDistributivoModificacion(ReformaIngresoSuplemento r) {
        //usuario = new ArrayList<>();
        this.reformaDistr = new ReformaIngresoSuplemento();
        this.reformaDistr = r;
        periodo = r.getPeriodo();
        copiaDiReforma = new ArrayList<>();
//        List<PartidasDistributivo> verificarList = valoresService.verificarReformaDis(BigInteger.valueOf(r.getId()));
//        if (verificarList.isEmpty()) {
//
//            listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
//            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            if (listaValoresDistributivo.size() > 0) {
//                for (ValoresDistributivo item : listaValoresDistributivo) {
//                    partidaDistributivo = new PartidasDistributivo();
//                    partidaDistributivo.setDistributivo(item);
//                    partidaDistributivo.setEstado(Boolean.TRUE);
//                    partidaDistributivo.setPeriodo(periodo);
//                    partidaDistributivo.setUsuarioCreacion(user.getName());
//                    partidaDistributivo.setFechaCreacion(new Date());
//                    partidaDistributivo.setUsuarioModificacion(user.getName());
//                    partidaDistributivo.setFechaModificacion(new Date());
//                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
//                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
//                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
//                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
//                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
//                    partidaDistributivo.setMonto(item.getValorResultante());
//                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
//                    partidaDistributivo = partidasService.create(partidaDistributivo);
//                }
//            }
//
//            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(periodo, estadoAprobado);
//
//            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");
//
//            if (!copiaRubrosPartidasReforma.isEmpty()) {
//                PartidasDistributivo item = new PartidasDistributivo();
//                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {
//
//                    item.setDistributivo(data.getDistributivo());
//                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
//                    item.setCodigoReforma(BigInteger.valueOf(r.getId()));
//                    item.setEstado(data.isEstado());
//                    item.setEstadoPartida(estado);
//                    item.setEstructuraAp(data.getEstructuraAp());
//                    item.setItemAp(data.getItemAp());
//                    item.setFechaCreacion(new Date());
//                    item.setFechaModificacion(new Date());
//                    item.setFuenteAp(data.getFuenteAp());
//                    item.setFuenteDirecta(data.getFuenteDirecta());
//                    item.setPartidaAp(data.getPartidaAp());
//                    item.setPeriodo(data.getPeriodo());
//                    item.setUsuarioCreacion(userSession.getNameUser());
//                    item.setUsuarioModificacion(userSession.getNameUser());
//                    item.setReformaSuplemento(BigDecimal.ZERO);
//                    item.setReformaReduccion(BigDecimal.ZERO);
//
//                    item.setReformaCodificado(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//
//                    item.setMonto(data.getDistributivo().getValorResultante().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//
//                    item = partidasService.create(item);
//                    item = new PartidasDistributivo();
//                }
//
//            }
//        }

        totalCupoD = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;

        if (r.getTipo()) {
            totalCupoD = valoresService.getCupoDistributivo(r, "D");
            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
            totalcupoAsignado();
            totalcupoAsignadoDa();
        } else {

            totalCupoD = BigDecimal.ZERO;
            cupoAsignado = BigDecimal.ZERO;
        }
        distributivosListModificaacion = new ArrayList<>();
        distributivosListModificaacion = valoresService.getDistributivoReformaModificacion(periodo, BigInteger.valueOf(r.getId()));
        partidasDistributivosListModificacionAnexo = new ArrayList<>();
        partidasDistributivosListModificacionAnexo = valoresService.getDistributivoReformaModificacionAnexo(periodo, BigInteger.valueOf(r.getId()));
        codigoReformaTotal = BigInteger.ZERO;
        codigoReformaTotal = BigInteger.valueOf(r.getId());
        panel1 = false;
        pane21 = false;
        panel3 = false;
        panel4 = true;
        enabledReformas = false;
    }

    public BigDecimal totalcupoAsignado() {
        if (reformaDistr == null) {
            return BigDecimal.ZERO;
        }
        return suplementoIngresoService.getCupoAsignado(BigInteger.valueOf(reformaDistr.getId()));
    }

    public BigDecimal totalcupoAsignadoDa() {
        if (reformaDistr == null) {
            return BigDecimal.ZERO;
        }
        return suplementoIngresoService.getCupoAsignadoDa(BigInteger.valueOf(reformaDistr.getId()));
    }

    public void abriDlgoPartidasPresupuestariaDistributiov(ThCargo d) {
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaRubros = valoresService.listaPartidasDisReforma(d, codigoReformaTotal);

        listaItem = new ArrayList<>();
        listaItem = catalogoServiceNew.findTipoPresupuesto(false);
        listaEstructura = new ArrayList<>();
        listaEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
        listaFuente = new ArrayList<>();
        listaFuente = fuenteServiceNew.getFuenteFinanciamiento();

        // setRmu(valoresService.getRMu(d, periodo));
        if (reformaDistr.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }
        mostrarData = new ThCargo();
        mostrarData = d;
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivos').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivos");

    }

    public void abriDlgoPartidasPresupuestariaDistributivoNuevos(ThCargo d) {
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.partidasDistributivosListNuevos = valoresService.listaPartidasDistReformaNulos(d, codigoReformaTotal);

        listaItem = new ArrayList<>();
        listaItem = catalogoServiceNew.findTipoPresupuesto(false);
        listaEstructura = new ArrayList<>();
        listaEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
        listaFuente = new ArrayList<>();
        listaFuente = fuenteServiceNew.getFuenteFinanciamiento();

        if (reformaDistr.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }
        mostrarData = new ThCargo();
        mostrarData = d;
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosNuevos').show()");
        PrimeFaces.current().ajax().update(":DlogopartidasDistributivosNuevos");

    }

    public void abriDlgoPartidasPresupuestariaDistributivoModificacion(ThCargo d) {
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.partidasDistributivosListModificacion = valoresService.listaPartidasDisReformaModificar(d, codigoReformaTotal);
        listaItem = new ArrayList<>();
        listaItem = catalogoServiceNew.findTipoPresupuesto(false);
        listaEstructura = new ArrayList<>();
        listaEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
        listaFuente = new ArrayList<>();
        listaFuente = fuenteServiceNew.getFuenteFinanciamiento();

        // setRmu(valoresService.getRMu(d, periodo));
        if (reformaDistr.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }

        mostrarData = new ThCargo();
        mostrarData = d;
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosModificacion').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivosModificacion");

    }

    public BigDecimal revisarReserva(PartidasDistributivo v) {

        //CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
        return valoresService.verReserva(v.getPartidaAp(), reformaDistr.getPeriodo());
    }

    public void editarPartidas(ThCargoRubros p) {
        if (p.getIdPresupuesto() != null && p.getIdEstructura() != null && p.getIdFuente() != null) {

            p.setPartidaPresupuestaria(p.getIdEstructura().getCodigo() + p.getIdPresupuesto().getCodigo() + p.getIdFuente().getCodFuente());

        } else {

            p.setPartidaPresupuestaria(null);
        }

        thCargoRubrosService.edit(p);

    }

    public void editarCeldas(ThCargoRubros p) {
        ThCargoRubros par = valoresService.verValorActualThCargosRubros(p);

        BigDecimal valorAntiguo = valoresService.getValorAntiguoSuplementoDistribuvtivo(p);
        BigDecimal valor = valoresService.getSumaDistributivosSuplemento(BigInteger.valueOf(reformaDistr.getId()), p.getIdRubro().getOrigen()).add(p.getMonto());

        if (p.getIdRubro() != null && p.getIdRubro().getOrigen() != null) {
            if (p.getIdRubro().getOrigen()) {
                if (valor.subtract(valorAntiguo).doubleValue() > totalCupoD.doubleValue()) {
                    p.setReformaSuplemento(BigDecimal.ZERO);
                    p.setReformaReduccion(BigDecimal.ZERO);
                    p.setReformaCodificado(BigDecimal.ZERO);
                    p.setIdPresupuesto(null);
                    p.setIdEstructura(null);
                    p.setIdFuente(null);
                    thCargoRubrosService.edit(p);
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Error", "No hay suiciente cupo");
                    return;
                }
            } else {
                if (valor.subtract(valorAntiguo).doubleValue() > totalCupoDA.doubleValue()) {
                    p.setReformaSuplemento(BigDecimal.ZERO);
                    p.setReformaReduccion(BigDecimal.ZERO);
                    p.setReformaCodificado(BigDecimal.ZERO);
                    p.setIdPresupuesto(null);
                    p.setIdEstructura(null);
                    p.setIdFuente(null);
                    thCargoRubrosService.edit(p);
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Error", "No hay suiciente cupo");
                    return;
                }
            }
        }

        try {

            if (p.getIdPresupuesto() != null && p.getIdEstructura() != null && p.getIdFuente() != null) {

                p.setPartidaPresupuestaria(p.getIdEstructura().getCodigo() + p.getIdPresupuesto().getCodigo() + p.getIdFuente().getCodFuente());

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

                p.setPartidaPresupuestaria(null);
                p.setReformaCodificado(BigDecimal.ZERO);
            }

            thCargoRubrosService.edit(p);
            totalcupoAsignado();
            totalcupoAsignadoDa();

        } catch (Exception e) {

        }

    }

    public void suplementoEdit(ThCargoRubros p) {
        ThCargoRubros par = valoresService.verValorActualThCargosRubros(p);
        BigDecimal valor = valoresService.getSumaDistributivosSuplemento(BigInteger.valueOf(reformaDistr.getId()), p.getIdRubro().getOrigen()).add(p.getReformaSuplemento()).subtract(par.getReformaSuplemento());
        if (p.getIdRubro() != null && p.getIdRubro().getOrigen()) {
            if (valor.doubleValue() > totalCupoD.doubleValue()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Error", "No tiene suficiente cupo");
                p.setReformaSuplemento(BigDecimal.ZERO);
                p.setReformaReduccion(BigDecimal.ZERO);
                p.setReformaCodificado(p.getMonto());
                return;
            }

        } else if (p.getIdRubro() != null && !p.getIdRubro().getOrigen()) {
            if (valor.doubleValue() > totalCupoDA.doubleValue()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Error", "No tiene suficiente cupo");
                p.setReformaSuplemento(BigDecimal.ZERO);
                p.setReformaReduccion(BigDecimal.ZERO);
                p.setReformaCodificado(p.getMonto());
                return;
            }

        }

        if (p.getMonto().doubleValue() == 0) {
            p.setReformaSuplemento(par.getReformaSuplemento());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No puede aumentar a un registro que es nuevo");
            return;
        }

        if (reformaDistr.getTipo()) {
            totalcupoAsignado();
            totalcupoAsignadoDa();
        }

        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()).subtract(p.getReformaReduccion()));
        thCargoRubrosService.edit(p);

    }

    public void reduccion(ThCargoRubros p) {
        if (p.getReformaReduccion().doubleValue() > p.getMonto().doubleValue()) {
            p.setReformaReduccion(BigDecimal.ZERO);
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaCodificado(p.getMonto());
            JsfUtil.addErrorMessage("Error", "El valor esa mayor al valor original");
            return;
        }
        p.setReformaSuplemento(BigDecimal.ZERO);
        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()).subtract(p.getReformaReduccion()));
        thCargoRubrosService.edit(p);
    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public void listaVisualizacion(ThCargo d) {
        dataView = new ThCargo();
        dataView = d;
        listaVista = valoresService.listaPartidasDistriReforma(d, BigInteger.valueOf(reformaDistr.getId()));
    //    setRmu(valoresService.getRMu(d, periodo));
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
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")
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

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="setter and getter">
    public BigInteger getCodigoReformaTotal() {
        return codigoReformaTotal;
    }

    public void setCodigoReformaTotal(BigInteger codigoReformaTotal) {
        this.codigoReformaTotal = codigoReformaTotal;
    }

    public BigDecimal getTotalCupoDA() {
        return totalCupoDA;
    }

    public void setTotalCupoDA(BigDecimal totalCupoDA) {
        this.totalCupoDA = totalCupoDA;
    }

    public BigDecimal getCupoAsignadoDA() {
        return cupoAsignadoDA;
    }

    public void setCupoAsignadoDA(BigDecimal cupoAsignadoDA) {
        this.cupoAsignadoDA = cupoAsignadoDA;
    }

    public List<ThCargoRubros> getPartidasDistributivosListNuevosAnexo() {
        return partidasDistributivosListNuevosAnexo;
    }

    public void setPartidasDistributivosListNuevosAnexo(List<ThCargoRubros> partidasDistributivosListNuevosAnexo) {
        this.partidasDistributivosListNuevosAnexo = partidasDistributivosListNuevosAnexo;
    }

    public List<ThCargoRubros> getPartidasDistributivosListModificacionAnexo() {
        return partidasDistributivosListModificacionAnexo;
    }

    public void setPartidasDistributivosListModificacionAnexo(List<ThCargoRubros> partidasDistributivosListModificacionAnexo) {
        this.partidasDistributivosListModificacionAnexo = partidasDistributivosListModificacionAnexo;
    }

    public boolean isEnabledReformas() {
        return enabledReformas;
    }

    public void setEnabledReformas(boolean enabledReformas) {
        this.enabledReformas = enabledReformas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public MasterCatalogoService getPeriodoService() {
        return periodoService;
    }

    public void setPeriodoService(MasterCatalogoService periodoService) {
        this.periodoService = periodoService;
    }

    public ValoresDistributivoService getValoresService() {
        return valoresService;
    }

    public void setValoresService(ValoresDistributivoService valoresService) {
        this.valoresService = valoresService;
    }

    public PartidaDistributivoService getPartidasService() {
        return partidasService;
    }

    public void setPartidasService(PartidaDistributivoService partidasService) {
        this.partidasService = partidasService;
    }

    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

    public PlanProgramaticoService getEstructuraService() {
        return estructuraService;
    }

    public void setEstructuraService(PlanProgramaticoService estructuraService) {
        this.estructuraService = estructuraService;
    }

    public CatalogoPresupuestoService getItemService() {
        return itemService;
    }

    public void setItemService(CatalogoPresupuestoService itemService) {
        this.itemService = itemService;
    }

    public ProformaPresupuestoPlanificadoService getProformaPresupuestoPlanificadoService() {
        return proformaPresupuestoPlanificadoService;
    }

    public void setProformaPresupuestoPlanificadoService(ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService) {
        this.proformaPresupuestoPlanificadoService = proformaPresupuestoPlanificadoService;
    }

    public FuenteFinanciamientoService getFuenteService() {
        return fuenteService;
    }

    public void setFuenteService(FuenteFinanciamientoService fuenteService) {
        this.fuenteService = fuenteService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public UnidadAdministrativaService getUnidadService() {
        return unidadService;
    }

    public void setUnidadService(UnidadAdministrativaService unidadService) {
        this.unidadService = unidadService;
    }

    public DistributivoService getDisService() {
        return disService;
    }

    public void setDisService(DistributivoService disService) {
        this.disService = disService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<ThCargo> getCopiaDiReforma() {
        return copiaDiReforma;
    }

    public void setCopiaDiReforma(List<ThCargo> copiaDiReforma) {
        this.copiaDiReforma = copiaDiReforma;
    }

    public List<ThCargoRubros> getCopiaDiReformaAnexo() {
        return copiaDiReformaAnexo;
    }

    public void setCopiaDiReformaAnexo(List<ThCargoRubros> copiaDiReformaAnexo) {
        this.copiaDiReformaAnexo = copiaDiReformaAnexo;
    }

    public ReformaSuplementoIngresoService getSuplementoIngresoService() {
        return suplementoIngresoService;
    }

    public void setSuplementoIngresoService(ReformaSuplementoIngresoService suplementoIngresoService) {
        this.suplementoIngresoService = suplementoIngresoService;
    }

    public CuposService getCupoService() {
        return cupoService;
    }

    public void setCupoService(CuposService cupoService) {
        this.cupoService = cupoService;
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

    public List<ThCargoRubros> getListaRubros() {
        return listaRubros;
    }

    public void setListaRubros(List<ThCargoRubros> listaRubros) {
        this.listaRubros = listaRubros;
    }

    public List<ThCargo> getDistributivosNuevosList() {
        return distributivosNuevosList;
    }

    public void setDistributivosNuevosList(List<ThCargo> distributivosNuevosList) {
        this.distributivosNuevosList = distributivosNuevosList;
    }

    public List<ThCargoRubros> getPartidasDistributivosListNuevos() {
        return partidasDistributivosListNuevos;
    }

    public void setPartidasDistributivosListNuevos(List<ThCargoRubros> partidasDistributivosListNuevos) {
        this.partidasDistributivosListNuevos = partidasDistributivosListNuevos;
    }

    public List<ThCargo> getDistributivosListModificaacion() {
        return distributivosListModificaacion;
    }

    public void setDistributivosListModificaacion(List<ThCargo> distributivosListModificaacion) {
        this.distributivosListModificaacion = distributivosListModificaacion;
    }

    public List<ThCargoRubros> getPartidasDistributivosListModificacion() {
        return partidasDistributivosListModificacion;
    }

    public void setPartidasDistributivosListModificacion(List<ThCargoRubros> partidasDistributivosListModificacion) {
        this.partidasDistributivosListModificacion = partidasDistributivosListModificacion;
    }

    public ThCargo getMostrarData() {
        return mostrarData;
    }

    public void setMostrarData(ThCargo mostrarData) {
        this.mostrarData = mostrarData;
    }

    public ThCargo getDataView() {
        return dataView;
    }

    public void setDataView(ThCargo dataView) {
        this.dataView = dataView;
    }

    public List<PresPlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }

    public void setListaEstructura(List<PresPlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
    }

    public List<PresCatalogoPresupuestario> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<PresCatalogoPresupuestario> listaItem) {
        this.listaItem = listaItem;
    }

    public List<PresFuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<PresFuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public PresCatalogoPresupuestarioService getCatalogoServiceNew() {
        return catalogoServiceNew;
    }

    public void setCatalogoServiceNew(PresCatalogoPresupuestarioService catalogoServiceNew) {
        this.catalogoServiceNew = catalogoServiceNew;
    }

    public PresFuenteFinanciamientoService getFuenteServiceNew() {
        return fuenteServiceNew;
    }

    public void setFuenteServiceNew(PresFuenteFinanciamientoService fuenteServiceNew) {
        this.fuenteServiceNew = fuenteServiceNew;
    }

    public PresPlanProgramaticoService getEstructuraServiceNew() {
        return estructuraServiceNew;
    }

    public void setEstructuraServiceNew(PresPlanProgramaticoService estructuraServiceNew) {
        this.estructuraServiceNew = estructuraServiceNew;
    }

    public ThCargoRubrosService getThCargoRubrosService() {
        return thCargoRubrosService;
    }

    public void setThCargoRubrosService(ThCargoRubrosService thCargoRubrosService) {
        this.thCargoRubrosService = thCargoRubrosService;
    }

    public List<ThCargoRubros> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<ThCargoRubros> listaVista) {
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
