/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
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
 * @author Sandra Arroba
 */
@Named(value = "revisionPreReformaT2View")
@ViewScoped
public class ReformaRevisionPresupuestoT2Controller extends BpmnBaseRoot implements Serializable {

    private Producto producto;
    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private OpcionBusqueda busqueda;
    private Distributivo dataView, mostrarData;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    private List<Producto> listaProducto;
    private List<Producto> listaProductoPartida;
    private List<PresPlanProgramatico> listaPlanProgramatico;
//    private List<PlanProgramatico> listaPlanProgramatico;
    private List<CatalogoItem> listafuenteFinanciamiento;
    private List<PlanProgramatico> listaPlanProgramaticoo;
    private List<CatalogoPresupuesto> listaPresupuesto;
    private List<UnidadAdministrativa> unidades;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PlanProgramatico> listaPlanProgramaticos;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;

    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;

    private List<Distributivo> listaDistributivo;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<PartidasDistributivo> listaVista;

    private PartidasDistributivo partidaDistributivo;
    private List<PartidasDistributivo> listaRubros;
    private List<PresPlanProgramatico> listaEstructura;
//    private List<PlanProgramatico> listaEstructura;
    private List<CatalogoPresupuesto> listaItem;
    private List<Distributivo> distributivosNuevosList;
    private List<PartidasDistributivo> partidasDistributivosListNuevos;

    private List<Distributivo> distributivosListModificaacion;
    private List<PartidasDistributivo> partidasDistributivosListModificacion;
    private List<ValoresDistributivo> listaValoresDistributivo;

    private boolean btnAprobar, btnRechazar;
    private boolean btnAnular;
    private String observaciones;
    private boolean btnasignarpartida;
    private BigDecimal rmu;
    private boolean vistaPartidaDisGeneral;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;
    private boolean panel1;
    private boolean pane21;
    private boolean panel3;
    private boolean panel4;

    @Inject
    private ReformaTraspasoService reformaTraspasoService;

    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;

    @Inject
    private UserSession userSession;

    @Inject
    private ClienteService clienteService;

    @Inject
    private ProductoService productoService;

    @Inject
    private CatalogoService catalogoService;

    @Inject
    private PlanProgramaticoService planProgramaticoService;

    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private PresPlanProgramaticoService estrucPlanProgramaticoService;
//    private PlanProgramaticoService estrucPlanProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private CatalogoPresupuestoService itemService;
    @Inject
    private PartidaDistributivoService partidasService;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                reformaTraspaso = new ReformaTraspaso();
                detalleReformaTraspaso = new DetalleReformaTraspaso();
                busqueda = new OpcionBusqueda();
                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REV-REF");
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
//        lazyReformaTraspaso.getFilterss().put("id:equal", reformaTraspaso.getId());
//                lazyReformaTraspaso.getFilterss().put("estadoReformaTramite:equal", estadoReforma);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                listaPlanProgramatico = new ArrayList<>();
                listafuenteFinanciamiento = catalogoService.MostarTodoCatalogo("tipo_fuente_financiamiento");
                listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
                listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
                listaCatalogoPresupuestos = catalogoPresupuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), busqueda.getAnio());
                btnAprobar = false;
                btnRechazar = false;
                btnAnular = false;
                btnasignarpartida = Boolean.FALSE;
                listaProducto = new ArrayList<>();
                listaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
                listaProformaPresupuestoPlanificado = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (reforma.getTipo()) {
            listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            for (Producto item : listaProducto) {
                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
        } else {
            busqueda.setAnio(reforma.getPeriodo());
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivo = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reforma.getId()));
            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (!listaProducto.isEmpty()) {
                for (Producto item : listaProducto) {
                    if (item.getEstado()) {
                        totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                        totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                        totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                        totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                    }
                }
            }
            if (!listaPartidaDistributivo.isEmpty()) {
                for (PartidasDistributivo listaRubro : listaPartidaDistributivo) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaRubro.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + listaRubro.getTraspasoReduccion().doubleValue();
                }
            }
            if (!listaPartidaDistributivoAnexo.isEmpty()) {
                for (PartidasDistributivoAnexo listaAnexo : listaPartidaDistributivoAnexo) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaAnexo.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + listaAnexo.getTraspasoReduccion().doubleValue();
                }
            }
            if (!listaPartidaDirecta.isEmpty()) {
                for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
                }
            }
        }
        if (incremento) {
            return totalTraspasoIncrementoR;
        } else {
            return totalTraspasoReduccionR;
        }
    }

    public boolean mostrarOcultarBtnAsignarPartida(ReformaTraspaso reforma) {
        if (reforma.getTipo()) {
            listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        } else {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        }
        boolean btnAsignarPartidaFinal = false;
        for (Producto producto1 : listaProducto) {
            boolean mostrar = reformaTraspasoService.getProductoSinPartida(producto1);
            if (btnAsignarPartidaFinal) {
            } else {
                btnAsignarPartidaFinal = mostrar;
            }
        }
        return btnAsignarPartidaFinal;
    }

    public void asignarPartida(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        listaProductoPartida = reformaTraspasoService.getListProductoByReformaT2SinPartida(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), reformaTraspaso.getPeriodo());
        listaCatalogoPresupuestos = catalogoPresupuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), reformaTraspaso.getPeriodo());
        renderedPAPP = !listaProducto.isEmpty();
        renderedDistributivo = !listaDistributivo.isEmpty();
        renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        if (Utils.isNotEmpty(listaProducto)) {
            if (listaProductoPartida.isEmpty()) {
                JsfUtil.addWarningMessage("Producto", "No existen Productos sin Partida");
            }
        }
        if (Utils.isNotEmpty(listaDistributivo)) {

            List<PartidasDistributivo> listPDistributivo = reformaTraspasoService.listaPDistributivoSinPartidasReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (Utils.isEmpty(listPDistributivo)) {
                JsfUtil.addWarningMessage("Distributivo", "No existen Rubros de Distributivos sin Partidas");
            }
        }
        if (listaPartidaDistributivoAnexo.isEmpty()) {
            JsfUtil.addWarningMessage("Distributivo Anexo", "No existen Distributivos Anexos sin Partidas");
        }
        if (listaProformaPresupuestoPlanificado.isEmpty()) {
            JsfUtil.addWarningMessage("Partidas Directas", "No existen Partidas Directas sin Partidas");
        }
        PrimeFaces.current().executeScript("PF('DlgReformaAsignarPartida').show()");
    }

    public void abriDlgoPartidasPresupuestariaDistributivoNuevos(Distributivo d) {
        Short periodo = reformaTraspaso.getPeriodo();
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.partidasDistributivosListNuevos = valoresService.listaPresupuestoPartidasReformaNulos(d, BigInteger.valueOf(reformaTraspaso.getId()));
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), periodo);
        this.listaEstructura = estrucPlanProgramaticoService.getEstructuraProgramaticaAll();
//        this.listaEstructura = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
     //   setRmu(valoresService.getRMu(d, periodo));
        partidasDistributivosListNuevos = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        mostrarData = new Distributivo();
        mostrarData = d;
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosNuevos').show()");
        PrimeFaces.current().ajax().update(":DlogopartidasDistributivosNuevos");

    }

    public boolean verificarPartidaDistributivo(Distributivo d) {
        busqueda.setAnio(reformaTraspaso.getPeriodo());
        System.out.println("PERIDO: "+busqueda.getAnio());
        List<PartidasDistributivo> partidaverificar = reformaTraspasoService.verificarlistaPresupuestoSinPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (!partidaverificar.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void editarCell(Producto p) {
        try {

            Boolean modifico = false;
//            proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(busqueda.getAnio(), p.getCodigoPresupuestario());

            if (p.getItemPresupuestario() != null && p.getEstructuraProgramatica() != null && p.getFuente() != null) {
                p.setCodigoPresupuestario(p.getEstructuraProgramatica().getCodigo() + p.getItemPresupuestario().getCodigo() + p.getFuente().getTipoFuente().getOrden());
                modifico = true;
            } else {
                p.setCodigoPresupuestario("");
            }
            if (p.getFuente() != null) {
                p.setFuenteDirecta(p.getFuente().getTipoFuente());
                modifico = true;
            }
            productoService.edit(p);

//            Boolean consultaTabla = proformaPresupuestoPlanificadoService.consultarDatos(p.getPeriodo(), "PPPA");
//            if (consultaTabla) {
//                //Al actualizar un item, estructura o fuente, en la tabla de proforma_presupuesto_planificado se actualizara esta partida modificada
//                //if (!partidaAntigua.equals("") && !partidaAntigua.equals(partidaNueva)) {
//                seModificoPPPA(modifico, p.getPeriodo());
//                // }
//            }
//            PrimeFaces.current().ajax().update("dataTReformaT1Partida");
            JsfUtil.addInformationMessage("Partida de Producto", "Se ha registrado correctamente");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void editarCellDistributivo(PartidasDistributivo p) {
        try {
            if (p.getFuenteAp() != null) {
                p.setFuenteDirecta(p.getFuenteAp().getTipoFuente());
            } else {
                p.setFuenteDirecta(null);
            }

            if (p.getItemAp() != null && p.getEstructuraAp() != null && p.getFuenteAp() != null) {
                p.setFuenteDirecta(p.getFuenteAp().getTipoFuente());
                p.setPartidaAp(p.getEstructuraAp().getCodigo() + p.getItemAp().getCodigo() + p.getFuenteDirecta().getOrden());
            } else {
                if (p.getFuenteAp() == null) {
                    p.setFuenteDirecta(null);
                }
                p.setPartidaAp(null);
            }

            partidasService.edit(p);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean aprobarPresupuesto(boolean aprobar, ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        CatalogoItem estadoReformaRechazada = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RECT");
        CatalogoItem estadoReformaAprobada = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        CatalogoItem estadopappaprobado = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        CatalogoItem estadopappanulado = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "ANU-REF");
        boolean productoSinPartida = false;
        boolean distributivoSinPartida = false;
//        boolean distributivoAnexoSinPartida = true;
//        boolean partidaDirectaSinPartida = true;
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (!listaProducto.isEmpty()) {
            for (Producto producto1 : listaProducto) {
                boolean mostrar = reformaTraspasoService.getProductoSinPartida(producto1);
                if (!productoSinPartida) {
                    productoSinPartida = mostrar;
                }
            }
        } else {
            productoSinPartida = false;
        }
        if (!listaDistributivo.isEmpty()) {
            for (Distributivo distributivo : listaDistributivo) {
                boolean dis = !verificarPartidaDistributivo(distributivo);
                if (!distributivoSinPartida) {
                    distributivoSinPartida = dis;
                }
            }
        } else {
            distributivoSinPartida = false;
        }
        if (distributivoSinPartida || productoSinPartida) {
            btnasignarpartida = true;
        } else {
            btnasignarpartida = false;
        }
        if (aprobar) {
            if (btnasignarpartida) {
                JsfUtil.addWarningMessage("Reforma", "No se puede aprobar, existen registros sin partidas asignadas");
                return false;
            } else {
                reformaTraspaso.setEstadoReformaTramite(estadopappaprobado);
                reformaTraspasoService.edit(reformaTraspaso);
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + (aprobar ? " aprobada" : " anulada") + " con éxito");
                this.reformaTraspaso = new ReformaTraspaso();
                return true;
            }
        } else {
            reformaTraspaso.setEstadoReforma(estadoReformaRechazada);
            reformaTraspaso.setEstadoReformaTramite(estadopappanulado);
            reformaTraspasoService.edit(reformaTraspaso);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + (aprobar ? " aprobada" : " anulada") + " con éxito");
            this.reformaTraspaso = new ReformaTraspaso();
            return true;
        }

    }

    public void observar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "OBSP-REF");
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " enviada a corrección con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
    }

    public void consultarPapp(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        busqueda.setAnio(reforma.getPeriodo());
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma2(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));

        renderedPAPP = !listaProducto.isEmpty();
        renderedDistributivo = !listaDistributivo.isEmpty();
        renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
//        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        listaPlanProgramatico = estrucPlanProgramaticoService.getEstructuraProgramaticaAll();
        PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
    }

    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
     //   setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        dataView = d;
        vistaPartidaDisGeneral = vista;
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void aprobarSolicitud(int a, ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (a == 1) {
            btnAprobar = true;
            btnRechazar = false;
            btnAnular = false;
        } else if (a == 0) {
            btnAprobar = false;
            btnRechazar = false;
            btnAnular = true;
        } else {
            btnAprobar = false;
            btnRechazar = true;
            btnAnular = false;
        }

        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);
            if (aprobar == 1) {//aprobar
                if (!aprobarPresupuesto(true, reformaTraspaso)) {
                    return;
                }
                getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
            } else if (aprobar == 0) {//Anular
                getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
                if (!aprobarPresupuesto(false, reformaTraspaso)) {
                    return;
                }
            } else {//Enviar a Corrección
                getParamts().put("usuario", reformaTraspaso.getUsuarioCreacion());
                observar(reformaTraspaso);
            }

            getParamts().put("aprobado", aprobar);

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            if (aprobar == 1) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solcitud se aprobado con éxito");
            } else {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solcitud se ha rechazado con éxito");
            }

            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public Producto getProducto() {
        return producto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public DetalleReformaTraspaso getDetalleReformaTraspaso() {
        return detalleReformaTraspaso;
    }

    public void setDetalleReformaTraspaso(DetalleReformaTraspaso detalleReformaTraspaso) {
        this.detalleReformaTraspaso = detalleReformaTraspaso;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public PlanAnualPoliticaPublica getPlanAnualPoliticaPublica() {
        return planAnualPoliticaPublica;
    }

    public void setPlanAnualPoliticaPublica(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanAnualProgramaProyecto getPlanAnualProgramaProyecto() {
        return planAnualProgramaProyecto;
    }

    public void setPlanAnualProgramaProyecto(PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public LazyModel<ReformaTraspaso> getLazyReformaTraspaso() {
        return lazyReformaTraspaso;
    }

    public void setLazyReformaTraspaso(LazyModel<ReformaTraspaso> lazyReformaTraspaso) {
        this.lazyReformaTraspaso = lazyReformaTraspaso;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<PresPlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PresPlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public List<Producto> getListaProductoPartida() {
        return listaProductoPartida;
    }

    public void setListaProductoPartida(List<Producto> listaProductoPartida) {
        this.listaProductoPartida = listaProductoPartida;
    }

    public List<CatalogoItem> getListafuenteFinanciamiento() {
        return listafuenteFinanciamiento;
    }

    public void setListafuenteFinanciamiento(List<CatalogoItem> listafuenteFinanciamiento) {
        this.listafuenteFinanciamiento = listafuenteFinanciamiento;
    }

    public List<PlanProgramatico> getListaPlanProgramaticoo() {
        return listaPlanProgramaticoo;
    }

    public void setListaPlanProgramaticoo(List<PlanProgramatico> listaPlanProgramaticoo) {
        this.listaPlanProgramaticoo = listaPlanProgramaticoo;
    }

    public List<CatalogoPresupuesto> getListaPresupuesto() {
        return listaPresupuesto;
    }

    public void setListaPresupuesto(List<CatalogoPresupuesto> listaPresupuesto) {
        this.listaPresupuesto = listaPresupuesto;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<PlanProgramatico> getListaPlanProgramaticos() {
        return listaPlanProgramaticos;
    }

    public void setListaPlanProgramaticos(List<PlanProgramatico> listaPlanProgramaticos) {
        this.listaPlanProgramaticos = listaPlanProgramaticos;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }

    public List<PartidasDistributivo> getListaPartidaDistributivo() {
        return listaPartidaDistributivo;
    }

    public void setListaPartidaDistributivo(List<PartidasDistributivo> listaPartidaDistributivo) {
        this.listaPartidaDistributivo = listaPartidaDistributivo;
    }

    public List<PartidasDistributivoAnexo> getListaPartidaDistributivoAnexo() {
        return listaPartidaDistributivoAnexo;
    }

    public void setListaPartidaDistributivoAnexo(List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo) {
        this.listaPartidaDistributivoAnexo = listaPartidaDistributivoAnexo;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidaDirecta() {
        return listaPartidaDirecta;
    }

    public void setListaPartidaDirecta(List<ProformaPresupuestoPlanificado> listaPartidaDirecta) {
        this.listaPartidaDirecta = listaPartidaDirecta;
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

    public boolean isBtnAnular() {
        return btnAnular;
    }

    public void setBtnAnular(boolean btnAnular) {
        this.btnAnular = btnAnular;
    }

    public boolean getBtnasignarpartida() {
        return btnasignarpartida;
    }

    public void setBtnasignarpartida(boolean btnasignarpartida) {
        this.btnasignarpartida = btnasignarpartida;
    }

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
    }

    public List<Distributivo> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<Distributivo> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public List<PartidasDistributivo> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<PartidasDistributivo> listaVista) {
        this.listaVista = listaVista;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
    }

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
    }

    public boolean isRenderedDistributivo() {
        return renderedDistributivo;
    }

    public void setRenderedDistributivo(boolean renderedDistributivo) {
        this.renderedDistributivo = renderedDistributivo;
    }

    public boolean isRenderedDistributivoAnexo() {
        return renderedDistributivoAnexo;
    }

    public void setRenderedDistributivoAnexo(boolean renderedDistributivoAnexo) {
        this.renderedDistributivoAnexo = renderedDistributivoAnexo;
    }

    public boolean isRenderedPAPP() {
        return renderedPAPP;
    }

    public void setRenderedPAPP(boolean renderedPAPP) {
        this.renderedPAPP = renderedPAPP;
    }

    public boolean isRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }

    public Distributivo getMostrarData() {
        return mostrarData;
    }

    public void setMostrarData(Distributivo mostrarData) {
        this.mostrarData = mostrarData;
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

    public List<PresPlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }

    public void setListaEstructura(List<PresPlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
    }

    public List<CatalogoPresupuesto> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<CatalogoPresupuesto> listaItem) {
        this.listaItem = listaItem;
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
//</editor-fold>

}
