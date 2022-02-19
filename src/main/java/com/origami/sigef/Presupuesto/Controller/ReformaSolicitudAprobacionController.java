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
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reformaSolicitudAprobacionView")
@ViewScoped
public class ReformaSolicitudAprobacionController extends BpmnBaseRoot implements Serializable {

    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private OpcionBusqueda busqueda;

    //PAPP
    private PlanLocalProgramaProyecto planLocal;
    private PlanAnualPoliticaPublica planPolitica;
    private PlanAnualProgramaProyecto planAnual;
    private ActividadOperativa actividad;
    private Producto producto;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    //<editor-fold defaultstate="collapsed" desc="Listas">
    private List<Producto> listaProducto;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;
    private List<ProformaPresupuestoPlanificado> proformaReformaList;
    private List<ProformaPresupuestoPlanificado> showCodigoRepetidosList;

    private List<PlanLocalProgramaProyecto> listaPlanLocalOriginal;
    private List<PlanLocalProgramaProyecto> listaPlanLocalNuevo;

    private List<PlanAnualPoliticaPublica> listaPlanPoliticaOriginal;
    private List<PlanAnualPoliticaPublica> listaPlanPoliticaNuevo;

    private List<PlanAnualProgramaProyecto> listaPlanAnualOriginal;
    private List<PlanAnualProgramaProyecto> listaPlanAnualNuevo;

    private List<ActividadOperativa> listaActividadOriginal;
    private List<ActividadOperativa> listaActividadNuevo;

    private List<Producto> listaProdutoOriginal;
    private List<Producto> listaProductoNuevo;

//</editor-fold>
    private String observaciones;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;
    private Cliente clienteNotifacacion;

    //<editor-fold defaultstate="collapsed" desc="Servicios">
    @Inject
    private ReformaTraspasoService reformaTraspasoService;

    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;

    @Inject
    private UserSession userSession;

    @Inject
    private ClienteService clienteService;

    @Inject
    private CatalogoService catalogoService;

    @Inject
    private PlanProgramaticoService planProgramaticoService;

    @Inject
    private ProductoService productoService;

    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;

    @Inject
    private KatalinaService katalinaService;

    @Inject
    private ReservaCompromisoService solicitudService;

    @Inject
    private PresupuestoService presupuestoService;

    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;

    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;

    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private ActividadOperativaService actividadOperativaService;
//</editor-fold>

    //NUEVO
    private Boolean renderNegarSolicitud;
    private Boolean renderAprobarSolicitud;
    private Boolean renderTabPAPP, renderedPartidaDirecta;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private ProformaPresupuestoPlanificado partidasDirectas;

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
                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                listaPlanProgramatico = new ArrayList<>();
                clienteNotifacacion = new Cliente();
                renderAprobarSolicitud = true;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (reforma.getTipo()) {
            listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId()));
            if (listaProducto.isEmpty()) {
                listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "APRT", true, BigInteger.valueOf(reforma.getId()));
            }
            for (Producto item : listaProducto) {
                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (!listaPartidaDirecta.isEmpty()) {
                for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
                }
            }
        } else {
            busqueda.setAnio(reforma.getPeriodo());
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivo = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reforma.getId()));
            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (listaProducto.isEmpty()) {
                listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "APRT", true, BigInteger.valueOf(reforma.getId()));
            }

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

    public void consultarPapp(ReformaTraspaso reforma) {
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        reformaTraspaso = reforma;
        totalTraspasoIncremento = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, true);
        totalTraspasoReduccion = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, false);
        if (reforma.getTipo()) {
            listaProducto = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (listaProducto.isEmpty()) {
                listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "APRT", true, BigInteger.valueOf(reforma.getId()));
            }
        } else {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            if (listaProducto.isEmpty()) {
                listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "APRT", true, BigInteger.valueOf(reforma.getId()));
            }
        }
        busqueda.setAnio(reforma.getPeriodo());
        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        renderTabPAPP = !listaProducto.isEmpty();
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPDIReformaT1(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
    }

    @Transactional
    public boolean enviar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        CatalogoItem estadoReformAprob = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        CatalogoItem estadoReformaCambio = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");

        if (Utils.isNotEmpty(listaProducto)) {
            for (Producto producto1 : listaProducto) {
                if (producto1.getNumeroOrdenId() != null) {
                    Producto pro = reformaTraspasoService.getProductoById(producto1.getId());
                    Producto proOriginal = reformaTraspasoService.getProductoByIdReformaCopia(producto1.getNumeroOrdenId().longValue(), estadoReformAprob);
                    BigDecimal montoRefTras = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().subtract(proOriginal.getTraspasoReduccion()));
                    BigDecimal montoRefSupRed = montoRefTras.add(proOriginal.getSuplementoEgreso().subtract(proOriginal.getReduccionEgreso()));
                    if (montoRefSupRed.compareTo(pro.getMonto()) != 0) {
                        JsfUtil.addErrorMessage("Producto", "Se ha generado otro tipo de reforma, no puede continuar.");
                        return false;
                    }
                    proOriginal.setMontoReformada(montoRefSupRed);
                }
            }
        }

        reformaTraspaso.setEstadoReforma(estadoReformaCambio);
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspaso.setFechaAprobacion(new Date());
        reformaTraspasoService.edit(reformaTraspaso);

        /*Cambiar los valores al PAPP original*/
        if (reformaTraspaso.getTipo()) {
            //listaProducto = reformaTraspasoService.getListProductoByReforma(reformaTraspaso.getPeriodo(), reformaTraspaso.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
            listaProducto = reformaTraspasoService.getListProductoByReforma2(reformaTraspaso.getUnidadRequiriente().getId(),BigInteger.valueOf(reformaTraspaso.getId()));
            CatalogoItem estadopappFinal = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
            CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
            listaPlanLocalNuevo = reformaTraspasoService.showPlanLocalNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            //<editor-fold defaultstate="collapsed" desc="PLAN LOCAL NUEVO">
            if (!listaPlanLocalNuevo.isEmpty()) {
                // Actualiza Plan local o crea si no existe
                for (PlanLocalProgramaProyecto dataLocal : listaPlanLocalNuevo) {
                    planLocal = new PlanLocalProgramaProyecto();
                    if (dataLocal.getNumeroOrdenId() != null) {
                        planLocal = reformaTraspasoService.getPlanlocal(dataLocal.getNumeroOrdenId());
                        planLocal.setUsuarioModifica(userSession.getNameUser());
                        planLocal.setFechaModificacion(new Date());
                        planLocal.setEstadoPapp(estado2);
                        planLocalProgramaProyectoService.edit(planLocal);
                    } else {
                        planLocal = Utils.clone(dataLocal);
                        planLocal.setId(null);
                        planLocal.setCodigoReforma(null);
                        planLocal.setCodigoReformaTraspaso(null);
                        planLocal.setUsuarioModifica(userSession.getNameUser());
                        planLocal.setFechaModificacion(new Date());
                        planLocal.setEstadoPapp(estado2);
                        planLocal.setRegistroNuevoReferencia(BigInteger.valueOf(dataLocal.getId()));
                        planLocal = planLocalProgramaProyectoService.create(planLocal);
                    }
                }
            } else {

            }
//</editor-fold>
            listaPlanPoliticaNuevo = reformaTraspasoService.showPlanPoliticalNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            //<editor-fold defaultstate="collapsed" desc="POLITICA NUEVA">
            if (!listaPlanPoliticaNuevo.isEmpty()) {
                for (PlanAnualPoliticaPublica dataPolitica : listaPlanPoliticaNuevo) {
                    planPolitica = new PlanAnualPoliticaPublica();
                    if (dataPolitica.getNumeroOrdenId() != null) {
                        planPolitica = reformaTraspasoService.getPlanPolitica(dataPolitica.getNumeroOrdenId());
                        planPolitica.setUsuarioModifica(userSession.getNameUser());
                        planPolitica.setFechaModificacion(new Date());
                        planPolitica.setEstadoPapp(estado2);
                        planAnualPoliticaPublicaService.edit(planPolitica);
                    } else {
                        planPolitica = Utils.clone(dataPolitica);
                        planPolitica.setId(null);
                        if (planPolitica.getPlanLocal().getNumeroOrdenId() != null) {
                            PlanLocalProgramaProyecto pl = reformaTraspasoService.getPlanlocal(planPolitica.getPlanLocal().getNumeroOrdenId());
                            planPolitica.setPlanLocal(pl);
                        } else {
                            PlanLocalProgramaProyecto p = reformaTraspasoService.getPlanLocalCreado(BigInteger.valueOf(planPolitica.getPlanLocal().getId()));
                            planPolitica.setPlanLocal(p);
                        }
                        planPolitica.setUsuarioModifica(userSession.getNameUser());
                        planPolitica.setFechaModificacion(new Date());
                        planPolitica.setEstadoPapp(estado2);
                        planPolitica.setCodigoReforma(null);
                        planPolitica.setCodigoReformaTraspaso(null);
                        planPolitica.setRegistroNuevoReferencia(BigInteger.valueOf(dataPolitica.getId()));
                        planPolitica = planAnualPoliticaPublicaService.create(planPolitica);
                    }
                }
            } else {

            }
//</editor-fold>
            listaPlanAnualNuevo = reformaTraspasoService.showPlanAnualNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            //<editor-fold defaultstate="collapsed" desc="PLAN ANUAL NUEVO">
            if (!listaPlanAnualNuevo.isEmpty()) {
                for (PlanAnualProgramaProyecto dataPlanAnual : listaPlanAnualNuevo) {
                    planAnual = new PlanAnualProgramaProyecto();
                    if (dataPlanAnual.getNumeroOrdenId() != null) {
                        planAnual = reformaTraspasoService.getPlanAnual(dataPlanAnual.getNumeroOrdenId());
                        planAnual.setUsuarioModifica(userSession.getNameUser());
                        planAnual.setFechaModificacion(new Date());
                        planAnual.setEstadoPapp(estado2);
                        planAnualProgramaProyectoService.edit(planAnual);
                    } else {
                        planAnual = Utils.clone(dataPlanAnual);
                        planAnual.setId(null);
                        if (dataPlanAnual.getPlanAnual() != null) {
                            if (dataPlanAnual.getPlanAnual().getNumeroOrdenId() != null) {
                                PlanAnualPoliticaPublica pl = reformaTraspasoService.getPlanPolitica(planAnual.getPlanAnual().getNumeroOrdenId());
                                planAnual.setPlanAnual(pl);
                            } else {
                                PlanAnualPoliticaPublica p = reformaTraspasoService.getPlanPoliticaCreado(BigInteger.valueOf(dataPlanAnual.getPlanAnual().getId()));
                                planAnual.setPlanAnual(p);
                            }
                        }
                        planAnual.setUsuarioModifica(userSession.getNameUser());
                        planAnual.setFechaModificacion(new Date());
                        planAnual.setEstadoPapp(estado2);
                        planAnual.setCodigoReforma(null);
                        planAnual.setCodigoReformaTraspaso(null);
                        planAnual.setRegistroNuevoReferencia(BigInteger.valueOf(dataPlanAnual.getId()));
                        planAnual = planAnualProgramaProyectoService.create(planAnual);
                    }
                }
            } else {

            }
//</editor-fold>
            listaActividadNuevo = reformaTraspasoService.showActividadOperativaNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            //<editor-fold defaultstate="collapsed" desc="ACTIVIDAD NUEVA">
            if (!listaActividadNuevo.isEmpty()) {
                for (ActividadOperativa dataActividad : listaActividadNuevo) {
                    actividad = new ActividadOperativa();
                    if (dataActividad.getNumeroOrdenId() != null) {
                        actividad = reformaTraspasoService.getActividadOperativa(dataActividad.getNumeroOrdenId());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setEstadoPapp(estado2);
                        actividad.setMonto(dataActividad.getMonto());
                        actividad.setMonotReformado(dataActividad.getMonotReformado());
                        actividad.setCr1(dataActividad.getCr1());
                        actividad.setCr2(dataActividad.getCr2());
                        actividad.setCr3(dataActividad.getCr3());
                        actividadOperativaService.edit(actividad);
                    } else {
                        actividad = Utils.clone(dataActividad);
                        actividad.setId(null);
//                        if (actividad.getPlanProgramaProyecto().getNumeroOrdenId() != null) {
//                            PlanAnualProgramaProyecto pl = reformaTraspasoService.getPlanAnual(actividad.getPlanProgramaProyecto().getNumeroOrdenId());
//                            actividad.setPlanProgramaProyecto(pl);
//                        } else {
//                            PlanAnualProgramaProyecto p = reformaTraspasoService.getPlanAnualCreado(BigInteger.valueOf(dataActividad.getPlanProgramaProyecto().getId()));
//                            actividad.setPlanProgramaProyecto(p);
//                        }
                        actividad.setPlanProgramaProyecto(dataActividad.getPlanProgramaProyecto());
                        actividad.setCodigoReforma(null);
                        actividad.setCodigoReformaTraspaso(null);
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setEstadoPapp(estado2);
                        actividad.setRegistroNuevoReferencia(BigInteger.valueOf(dataActividad.getId()));
                        actividad = actividadOperativaService.create(actividad);
                    }
                }
            } else {

            }
//</editor-fold>

            for (Producto producto1 : listaProducto) {
                if (producto1.getNumeroOrdenId() != null) {
                    producto1.setEstadoPapp(estadopappFinal);
                    productoService.edit(producto1);
                    Producto pro = reformaTraspasoService.getProductoById(producto1.getId());
//                    pro.setMonto(producto1.getMontoReformada());
                    productoService.edit(pro);
                    Producto proOriginal = reformaTraspasoService.getProductoByIdReformaCopia(producto1.getNumeroOrdenId().longValue(), estadoReformAprob);
                    proOriginal.setTraspasoIncremento(proOriginal.getTraspasoIncremento().add(pro.getTraspasoIncremento()));
                    proOriginal.setTraspasoReduccion(proOriginal.getTraspasoReduccion().add(pro.getTraspasoReduccion()));
                    BigDecimal montoRefTras = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().subtract(proOriginal.getTraspasoReduccion()));
                    BigDecimal montoRefSupRed = montoRefTras.add(proOriginal.getSuplementoEgreso().subtract(proOriginal.getReduccionEgreso()));
                    if (montoRefSupRed.compareTo(pro.getMontoReformada()) != 0) {
                        JsfUtil.addErrorMessage("Producto", "Se ha generado otro tipo de reforma, no puede continuar.");
                        return false;
                    }
                    proOriginal.setMontoReformada(pro.getMontoReformada());
                    productoService.edit(proOriginal);
                } else {
                    producto = new Producto();
                    producto = Utils.clone(producto1);
                    producto.setId(null);
                    producto1.setEstadoPapp(estadopappFinal);
                    productoService.edit(producto1);
                    if (producto.getActividadOperativa().getNumeroOrdenId() != null) {
                        ActividadOperativa act = reformaTraspasoService.getActividadOperativa(producto.getActividadOperativa().getNumeroOrdenId());
                        producto.setActividadOperativa(act);
                    } else {
                        ActividadOperativa a = reformaTraspasoService.getActividadCreado(BigInteger.valueOf(producto.getActividadOperativa().getId()));
                        producto.setActividadOperativa(a);
                    }
                    producto.setId(null);
                    producto.setDescripcion(producto1.getDescripcion());
                    producto.setMonto(producto1.getMontoReformada());
                    producto.setItemPresupuestario(producto1.getItemPresupuestario());
                    producto.setEstructuraProgramatica(producto1.getEstructuraProgramatica());
                    producto.setFuente(producto1.getFuente());
                    producto.setItemNew(producto1.getItemNew());
                    producto.setEstructruaNew(producto1.getEstructruaNew());
                    producto.setFuenteNew(producto1.getFuenteNew());
                    producto.setEstado(true);
                    producto.setFechaCreacion(producto1.getFechaCreacion());
                    producto.setUsuarioCreacion(producto1.getUsuarioCreacion());
                    producto.setFechaModificacion(producto1.getFechaModificacion());
                    producto.setUsuarioModifica(producto1.getUsuarioModifica());
                    producto.setCodigoPresupuestario(producto1.getCodigoPresupuestario());
                    producto.setPeriodo(producto1.getPeriodo());
                    producto.setFuenteDirecta(producto1.getFuenteDirecta());
                    producto.setReserva(producto1.getReserva());
                    producto.setSaldoDisponible(producto.getMonto().subtract(producto1.getReserva()));
                    producto.setTraspasoIncremento(producto1.getTraspasoIncremento());
                    producto.setTraspasoReduccion(BigDecimal.ZERO);
                    producto.setSuplementoEgreso(BigDecimal.ZERO);
                    producto.setMontoReformada(producto1.getMontoReformada());
                    producto.setCodigoReformaTraspaso(null);
                    producto.setRegistroNuevoReferencia(BigInteger.valueOf(producto1.getId()));
                    producto.setNumeroOrdenId(null);
                    producto.setNumeroTramite(null);
                    producto.setEstadoPapp(estado2);
                    producto.setMonto(BigDecimal.ZERO);
                    producto = productoService.create(producto);
                    producto = new Producto();
                }
                /*Cambiar valores en Presupuesto*/
                if (producto1.getTraspasoIncremento() != BigDecimal.ZERO || producto1.getTraspasoReduccion() != BigDecimal.ZERO) {
                    Presupuesto presupuesto = reformaTraspasoService.valoresPresupuestoPapp(busqueda.getAnio(), producto1.getCodigoPresupuestario());
                    if (presupuesto != null) {
                        presupuesto.setReformaSuplemetario(presupuesto.getReformaSuplemetario().add(producto1.getTraspasoIncremento()));
                        presupuesto.setReformaReducido(presupuesto.getReformaReducido().add(producto1.getTraspasoReduccion()));
                        presupuesto.setCodificado((presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario())).subtract(presupuesto.getReformaReducido()));
                        presupuestoService.edit(presupuesto);
                    } else {
                        Presupuesto pre = new Presupuesto();
                        pre.setEstructura(producto1.getEstructuraProgramatica());
                        pre.setItem(producto1.getItemPresupuestario());
                        pre.setFuente(producto1.getFuente());
                        pre.setItemNew(producto1.getItemNew());
                        pre.setEstructruaNew(producto1.getEstructruaNew());
                        pre.setFuenteNew(producto1.getFuenteNew());
                        pre.setFuenteDirecta(producto1.getFuenteDirecta());
                        pre.setPartida(producto1.getCodigoPresupuestario());
                        pre.setTipo(false);
                        pre.setPeriodo(producto1.getPeriodo());
                        pre.setComprometido(BigDecimal.ZERO);
                        pre.setPresupuestoInicial(BigDecimal.ZERO);
                        pre.setReformaSuplemetario(producto1.getTraspasoIncremento());
                        pre.setReformaReducido(producto1.getTraspasoReduccion());
                        pre.setCodificado((pre.getPresupuestoInicial().add(pre.getReformaSuplemetario())).subtract(pre.getReformaReducido()));
                        presupuestoService.create(pre);
                    }
                }
            }

            CatalogoItem estadodistri = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "ATD");
            listaPartidasDirectas = reformaTraspasoService.showPartidasDirectasNnuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaPartidasDirectas.isEmpty()) {
                for (ProformaPresupuestoPlanificado item : listaPartidasDirectas) {
                    // UPDATE CREATE PROFORMA PRESUPUESTO PLANIFICADO
                    partidasDirectas = new ProformaPresupuestoPlanificado();
                    if (item.getCodigoReferencia() != null) {
                        partidasDirectas = reformaTraspasoService.getPartidasDirectasNuevo(item.getCodigoReferencia());
                        partidasDirectas.setTraspasoIncremento(partidasDirectas.getTraspasoIncremento().add(item.getTraspasoIncremento()));
                        partidasDirectas.setTraspasoReduccion(partidasDirectas.getTraspasoReduccion().add(item.getTraspasoReduccion()));
                        partidasDirectas.setReformaCodificado(partidasDirectas.getValor().add(partidasDirectas.getTraspasoIncremento()).subtract(partidasDirectas.getTraspasoReduccion()));
                        partidasDirectas.setUsuarioModificacion(userSession.getNameUser());
                        partidasDirectas.setFechaModificacion(new Date());
                        partidasDirectas.setEstadoPartida(estadodistri);
                        proformaService.edit(partidasDirectas);
                    } else {
                        partidasDirectas = Utils.clone(item);
                        partidasDirectas.setId(null);
                        partidasDirectas.setUsuarioModificacion(userSession.getNameUser());
                        partidasDirectas.setFechaModificacion(new Date());
                        partidasDirectas.setEstadoPartida(estadodistri);
                        partidasDirectas.setCodigoReforma(null);
                        partidasDirectas.setCodigoReformaTraspaso(null);
                        partidasDirectas.setValor(BigDecimal.ZERO);
                        partidasDirectas = proformaService.create(partidasDirectas);
                    }
                    // UPDATE CREATE PRESUPUESTO
                    if (item.getTraspasoIncremento() != BigDecimal.ZERO || item.getTraspasoReduccion() != BigDecimal.ZERO) {
                        Presupuesto presupuesto = reformaTraspasoService.valoresPresupuestoPapp(busqueda.getAnio(), item.getPartidaPresupuestaria());
                        if (presupuesto != null) {
                            presupuesto.setReformaSuplemetario(presupuesto.getReformaSuplemetario().add(item.getTraspasoIncremento()));
                            presupuesto.setReformaReducido(presupuesto.getReformaReducido().add(item.getTraspasoReduccion()));
                            presupuesto.setCodificado((presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario())).subtract(presupuesto.getReformaReducido()));
                            presupuestoService.edit(presupuesto);
                        } else {
                            Presupuesto pre = new Presupuesto();
                            pre.setEstructruaNew(item.getEstructruaNew());
                            pre.setItemNew(item.getItemNew());
                            pre.setFuenteNew(item.getFuenteNew());
                            pre.setFuenteDirecta(item.getFuenteDirecta());
                            pre.setPartida(item.getPartidaPresupuestaria());
                            pre.setTipo(false);
                            pre.setPeriodo(item.getPeriodo());
                            pre.setComprometido(BigDecimal.ZERO);
                            pre.setPresupuestoInicial(BigDecimal.ZERO);
                            pre.setReformaSuplemetario(item.getTraspasoIncremento());
                            pre.setReformaReducido(item.getTraspasoReduccion());
                            pre.setMontoDisponible(item.getTraspasoIncremento());
                            pre.setCodificado((pre.getPresupuestoInicial().add(pre.getReformaSuplemetario())).subtract(pre.getReformaReducido()));
                            presupuestoService.create(pre);
                        }
                    }
                }
            }
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " aprobado informe con éxito");
            this.reformaTraspaso = new ReformaTraspaso();
            return true;
        } else {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
            return false;
        }
    }

    public boolean enviarNew(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        CatalogoItem estadoReformAprob = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        CatalogoItem estadoReformaCambio = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId())); //ENRIQUE
        if (Utils.isNotEmpty(listaProducto)) {
            for (Producto producto1 : listaProducto) {
                if (producto1.getNumeroOrdenId() != null) {
                    Producto pro = reformaTraspasoService.getProductoById(producto1.getId());
                    Producto proOriginal = reformaTraspasoService.getProductoByIdReformaCopia(producto1.getNumeroOrdenId().longValue(), estadoReformAprob);
                    BigDecimal montoRefTras = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().subtract(proOriginal.getTraspasoReduccion()));
                    BigDecimal montoRefSupRed = montoRefTras.add(proOriginal.getSuplementoEgreso().subtract(proOriginal.getReduccionEgreso()));
                    if (montoRefSupRed.compareTo(pro.getMonto()) != 0) {
                        JsfUtil.addErrorMessage("Producto", "Se ha generado otro tipo de reforma, no puede continuar.");
                        return false;
                    }
                    proOriginal.setMontoReformada(montoRefSupRed);
                }
            }
        }
        reformaTraspaso.setEstadoReforma(estadoReformaCambio);
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspaso.setFechaAprobacion(new Date());
        reformaTraspasoService.edit(reformaTraspaso);
        if (reformaTraspaso.getTipo()) {
            CatalogoItem estadopappFinal = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
            CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
            listaActividadNuevo = reformaTraspasoService.showActividadOperativaNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaActividadNuevo.isEmpty()) {
                for (ActividadOperativa dataActividad : listaActividadNuevo) {
                    actividad = new ActividadOperativa();
                    if (dataActividad.getNumeroOrdenId() != null) {
                        actividad = reformaTraspasoService.getActividadOperativa(dataActividad.getNumeroOrdenId());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setEstadoPapp(estado2);
                        actividad.setMonto(dataActividad.getMonto());
                        actividad.setMonotReformado(dataActividad.getMonotReformado());
                        actividad.setCr1(dataActividad.getCr1());
                        actividad.setCr2(dataActividad.getCr2());
                        actividad.setCr3(dataActividad.getCr3());
                        actividadOperativaService.edit(actividad);
                    } else {
                        actividad = Utils.clone(dataActividad);
                        actividad.setId(null);
                        if (actividad.getPlanProgramaProyecto().getNumeroOrdenId() != null) {
                            PlanAnualProgramaProyecto pl = reformaTraspasoService.getPlanAnual(actividad.getPlanProgramaProyecto().getNumeroOrdenId());
                            actividad.setPlanProgramaProyecto(pl);
                        } else {
                            PlanAnualProgramaProyecto p = reformaTraspasoService.getPlanAnualCreado(BigInteger.valueOf(dataActividad.getPlanProgramaProyecto().getId()));
                            actividad.setPlanProgramaProyecto(p);
                        }
                        actividad.setCodigoReforma(null);
                        actividad.setCodigoReformaTraspaso(null);
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setEstadoPapp(estado2);
                        actividad.setRegistroNuevoReferencia(BigInteger.valueOf(dataActividad.getId()));
                        actividad = actividadOperativaService.create(actividad);
                    }
                }
            } else {

            }
            for (Producto producto1 : listaProducto) {
                if (producto1.getNumeroOrdenId() != null) {
                    producto1.setEstadoPapp(estadopappFinal);
                    productoService.edit(producto1);
                    Producto pro = reformaTraspasoService.getProductoById(producto1.getId());
                    productoService.edit(pro);
                    Producto proOriginal = reformaTraspasoService.getProductoByIdReformaCopia(producto1.getNumeroOrdenId().longValue(), estadoReformAprob);
                    proOriginal.setTraspasoIncremento(proOriginal.getTraspasoIncremento().add(pro.getTraspasoIncremento()));
                    proOriginal.setTraspasoReduccion(proOriginal.getTraspasoReduccion().add(pro.getTraspasoReduccion()));
                    BigDecimal montoRefTras = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().subtract(proOriginal.getTraspasoReduccion()));
                    BigDecimal montoRefSupRed = montoRefTras.add(proOriginal.getSuplementoEgreso().subtract(proOriginal.getReduccionEgreso()));
                    if (montoRefSupRed.compareTo(pro.getMontoReformada()) != 0) {
                        JsfUtil.addErrorMessage("Producto", "Se ha generado otro tipo de reforma, no puede continuar.");
                        return false;
                    }
                    proOriginal.setMontoReformada(pro.getMontoReformada());
                    productoService.edit(proOriginal);
                } else {
                    producto = new Producto();
                    producto = Utils.clone(producto1);
                    producto.setId(null);
                    producto1.setEstadoPapp(estadopappFinal);
                    productoService.edit(producto1);
                    if (producto.getActividadOperativa().getNumeroOrdenId() != null) {
                        ActividadOperativa act = reformaTraspasoService.getActividadOperativa(producto.getActividadOperativa().getNumeroOrdenId());
                        producto.setActividadOperativa(act);
                    } else {
                        ActividadOperativa a = reformaTraspasoService.getActividadCreado(BigInteger.valueOf(producto.getActividadOperativa().getId()));
                        producto.setActividadOperativa(a);
                    }
                    producto.setId(null);
                    producto.setDescripcion(producto1.getDescripcion());
                    producto.setMonto(producto1.getMontoReformada());
                    producto.setItemPresupuestario(producto1.getItemPresupuestario());
                    producto.setEstructuraProgramatica(producto1.getEstructuraProgramatica());
                    producto.setFuente(producto1.getFuente());
                    producto.setEstado(true);
                    producto.setFechaCreacion(producto1.getFechaCreacion());
                    producto.setUsuarioCreacion(producto1.getUsuarioCreacion());
                    producto.setFechaModificacion(producto1.getFechaModificacion());
                    producto.setUsuarioModifica(producto1.getUsuarioModifica());
                    producto.setCodigoPresupuestario(producto1.getCodigoPresupuestario());
                    producto.setPeriodo(producto1.getPeriodo());
                    producto.setFuenteDirecta(producto1.getFuenteDirecta());
                    producto.setReserva(producto1.getReserva());
                    producto.setSaldoDisponible(producto.getMonto().subtract(producto1.getReserva()));
                    producto.setTraspasoIncremento(producto1.getTraspasoIncremento());
                    producto.setTraspasoReduccion(BigDecimal.ZERO);
                    producto.setSuplementoEgreso(BigDecimal.ZERO);
                    producto.setMontoReformada(producto1.getMontoReformada());
                    producto.setCodigoReformaTraspaso(null);
                    producto.setNumeroOrdenId(null);
                    producto.setNumeroTramite(null);
                    producto.setEstadoPapp(estado2);
                    producto.setMonto(BigDecimal.ZERO);
                    producto = productoService.create(producto);
                    producto = new Producto();
                }
                /*Cambiar valores en Presupuesto*/
                if (producto1.getTraspasoIncremento() != BigDecimal.ZERO || producto1.getTraspasoReduccion() != BigDecimal.ZERO) {
                    Presupuesto presupuesto = reformaTraspasoService.valoresPresupuestoPapp(busqueda.getAnio(), producto1.getCodigoPresupuestario());
                    if (presupuesto != null) {
                        presupuesto.setReformaSuplemetario(presupuesto.getReformaSuplemetario().add(producto1.getTraspasoIncremento()));
                        presupuesto.setReformaReducido(presupuesto.getReformaReducido().add(producto1.getTraspasoReduccion()));
                        presupuesto.setCodificado((presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario())).subtract(presupuesto.getReformaReducido()));
                        presupuestoService.edit(presupuesto);
                    } else {
                        Presupuesto pre = new Presupuesto();
                        pre.setEstructura(producto1.getEstructuraProgramatica());
                        pre.setItem(producto1.getItemPresupuestario());
                        pre.setFuente(producto1.getFuente());
                        pre.setFuenteDirecta(producto1.getFuenteDirecta());
                        pre.setPartida(producto1.getCodigoPresupuestario());
                        pre.setTipo(false);
                        pre.setPeriodo(producto1.getPeriodo());
                        pre.setComprometido(BigDecimal.ZERO);
                        pre.setPresupuestoInicial(BigDecimal.ZERO);
                        pre.setReformaSuplemetario(producto1.getTraspasoIncremento());
                        pre.setReformaReducido(producto1.getTraspasoReduccion());
                        pre.setCodificado((pre.getPresupuestoInicial().add(pre.getReformaSuplemetario())).subtract(pre.getReformaReducido()));
                        presupuestoService.create(pre);
                    }
                }
            }
            agruparActualizarProformaReforma(reformaTraspaso);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " aprobado informe con éxito");
            this.reformaTraspaso = new ReformaTraspaso();
            return true;
        }
        return false;
    }

    public void agruparActualizarProformaReforma(ReformaTraspaso r) {
        BigInteger integer = BigInteger.valueOf(r.getId());
        proformaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaTraspasoService.getPappGroupReforma(r.getPeriodo(), r);
//        List<ProformaPDTO> listaagrupacionDistr = reformaTraspasoService.showCodigosAgrupadosReformas(r.getPeriodo(), r);
//        List<PartidasDistributivoAnexo> listaagrupacionDistrAnexo = partidaAnexoService.showAllPartidasAnexoReforma(r.getPeriodo(), r);
        List<ProformaPresupuestoPlanificado> listaPartidasdirectas = reformaTraspasoService.showPartidaDirectasReforma(r.getPeriodo(), r);
        //<editor-fold defaultstate="collapsed" desc="AGRUPAR PAPP">
        if (listaAgrupacionPapp != null) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO ob : listaAgrupacionPapp) {
                proformaPresupuesto.setValor(ob.getTotal());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setPartidaPresupuestaria(ob.getPartida());
                proformaPresupuesto.setItemPresupuestario(ob.getItemPresupuestario());
                proformaPresupuesto.setEstructuraProgramatica(ob.getEstructuraProgramatica());
                proformaPresupuesto.setFuente(ob.getFuente());
                proformaPresupuesto.setFuenteDirecta(ob.getFuenteDirecta());
                proformaPresupuesto.setCodigo("PAPP");
                proformaPresupuesto.setReformaSuplemento(ob.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(ob.getReformaReduccion());
                proformaPresupuesto.setTraspasoIncremento(ob.getIncrementoTraspaso());
                proformaPresupuesto.setTraspasoReduccion(ob.getReduccionTraspaso());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getTraspasoIncremento().add(proformaPresupuesto.getReformaSuplemento())).subtract(proformaPresupuesto.getReformaReduccion().add(proformaPresupuesto.getTraspasoReduccion())));
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
            }

        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGRUPAR DISTRIBUTIVO">
//        if (!listaagrupacionDistr.isEmpty()) {
//
//            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
//            for (ProformaPDTO i : listaagrupacionDistr) {
//
//                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
//                    if (i.getPartida() == null ? data.getPartidaPresupuestaria() == null : i.getPartida().equals(data.getPartidaPresupuestaria())) {
//                        showCodigoRepetidosList.add(data);
//                    }
//                }
//
//                proformaPresupuesto.setPartidaPresupuestaria(i.getPartida());
//                proformaPresupuesto.setValor(i.getTotal());
//                proformaPresupuesto.setReformaSuplemento(i.getReformaSuplemento());
//                proformaPresupuesto.setReformaReduccion(i.getReformaReduccion());
//                proformaPresupuesto.setTraspasoIncremento(i.getIncrementoTraspaso());
//                proformaPresupuesto.setTraspasoReduccion(i.getReduccionTraspaso());
//                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
//                proformaPresupuesto.setEstructuraProgramatica(i.getEstructuraProgramatica());
//                proformaPresupuesto.setItemPresupuestario(i.getItemPresupuestario());
//                proformaPresupuesto.setFuente(i.getFuente());
//                proformaPresupuesto.setFuenteDirecta(i.getFuenteDirecta());
//                proformaPresupuesto.setEstado(true);
//                proformaPresupuesto.setPeriodo(r.getPeriodo());
//                proformaPresupuesto.setGenerado(false);
//                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
//                proformaPresupuesto.setFechaCreacion(new Date());
//                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
//                proformaPresupuesto.setFechaModificacion(new Date());
//                proformaPresupuesto.setCodigo("PD");
//                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
//                proformaReformaList.add(proformaPresupuesto);
//                proformaPresupuesto = new ProformaPresupuestoPlanificado();
//
//            }
//        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR DISTRIBUTIVO ANEXO">
//        if (!listaagrupacionDistrAnexo.isEmpty()) {
//            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
//            for (PartidasDistributivoAnexo p : listaagrupacionDistrAnexo) {
//                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
//                    if (p.getPartidaAp() == null ? data.getPartidaPresupuestaria() == null : p.getPartidaAp().equals(data.getPartidaPresupuestaria())) {
//                        showCodigoRepetidosList.add(data);
//                    }
//                }
//
//                proformaPresupuesto.setPartidaPresupuestaria(p.getPartidaAp());
//                proformaPresupuesto.setValor(p.getMonto());
//                proformaPresupuesto.setEstructuraProgramatica(p.getEstructuraApA());
//                proformaPresupuesto.setItemPresupuestario(p.getItemApA());
//                proformaPresupuesto.setFuente(p.getFuenteApA());
//                proformaPresupuesto.setFuenteDirecta(p.getFuenteDirectaA());
//                proformaPresupuesto.setEstado(true);
//                proformaPresupuesto.setPeriodo(r.getPeriodo());
//                proformaPresupuesto.setGenerado(false);
//                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
//                proformaPresupuesto.setFechaCreacion(new Date());
//                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
//                proformaPresupuesto.setTraspasoIncremento(p.getTraspasoIncremento());
//                proformaPresupuesto.setTraspasoReduccion(p.getTraspasoReduccion());
//                proformaPresupuesto.setReformaSuplemento(p.getReformaSuplemento());
//                proformaPresupuesto.setReformaReduccion(p.getReformaReduccion());
//                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento().add(proformaPresupuesto.getTraspasoIncremento())).subtract(proformaPresupuesto.getReformaReduccion().add(proformaPresupuesto.getTraspasoReduccion())));
//                proformaPresupuesto.setFechaModificacion(new Date());
//                proformaPresupuesto.setCodigo("PDA");
//                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
//                proformaReformaList.add(proformaPresupuesto);
//                proformaPresupuesto = new ProformaPresupuestoPlanificado();
//
//            }
//
//        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR PARTIDAS DIRECTAS">
        if (!listaPartidasdirectas.isEmpty()) {
            for (ProformaPresupuestoPlanificado k : listaPartidasdirectas) {
                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (k.getPartidaPresupuestaria() == null ? data.getPartidaPresupuestaria() == null : k.getPartidaPresupuestaria().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }
                proformaReformaList.add(k);
            }
        }
//</editor-fold>
        //VERIFICACION DE CODIGOS REPETIDOS
        if (!showCodigoRepetidosList.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "HAY PARTIDAS QUE SE REPITEN POR FAVOR CORRIJA");
            return;
        }

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZANDO PROFORMA Y PRESUPUESTO">
        List<ProformaPresupuestoPlanificado> proformaVieja = reformaTraspasoService.proformaVieja(r.getPeriodo());
        int count = 0;
        if (!proformaVieja.isEmpty() && !proformaReformaList.isEmpty()) {
            for (ProformaPresupuestoPlanificado x : proformaVieja) {
                for (ProformaPresupuestoPlanificado y : proformaReformaList) {
                    ProformaPresupuestoPlanificado editProforma = new ProformaPresupuestoPlanificado();
                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {
                        editProforma = reformaTraspasoService.editProformavieja(x);
                        editProforma.setReformaSuplemento(y.getReformaSuplemento().add(x.getReformaSuplemento()));
                        editProforma.setReformaReduccion(y.getReformaReduccion().add(x.getReformaReduccion()));
                        editProforma.setTraspasoIncremento(y.getTraspasoIncremento().add(x.getTraspasoIncremento()));
                        editProforma.setTraspasoReduccion(y.getTraspasoReduccion().add(x.getTraspasoReduccion()));
                        editProforma.setReformaCodificado(editProforma.getValor().add(editProforma.getTraspasoIncremento()).subtract(editProforma.getTraspasoReduccion()));
                        editProforma.setFechaModificacion(new Date());
                        editProforma.setUsuarioModificacion(userSession.getNameUser());
                        proformaService.edit(editProforma);
                        count = count + 1;
                    }
                }
            }
        }
        if (!proformaVieja.isEmpty() && !proformaReformaList.isEmpty()) {
            count = 0;
            boolean verificador = true;
            ProformaPresupuestoPlanificado editPro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado x : proformaReformaList) {
//                editPro = new ProformaPresupuestoPlanificado();
//                verificador = true;
//                for (ProformaPresupuestoPlanificado y : proformaVieja) {
//                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {
//                        verificador = false;
//                    }
//                }
//                if (verificador) {
//                    editPro = Utils.clone(x);
//                    editPro.setId(null);
//                    editPro.setReformaCodificado(editPro.getValor().add(editPro.getReformaSuplemento()).subtract(editPro.getReformaReduccion()));
//                    editPro.setFechaModificacion(new Date());
//                    editPro.setUsuarioModificacion(userSession.getNameUser());
//                    editPro.setCodigoReforma(null);
//                    editPro.setCodigoReformaTraspaso(null);
//                    editPro.setValor(BigDecimal.ZERO);
//                    editPro = proformaService.create(editPro);
//                    count = count + 1;
//                }
                //CREACION DE LAS NUEVAS PARTIDAS EN EL PRESUPUESTO

            }

        }

//</editor-fold>
//        actualizarTotalIngresosEgresos(r);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Acualizado la proforma");
    }

    public void enviarCorreo(String email, String asunto, String mensaje, String userStart, ReformaTraspaso r, String aprp) {
        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;text-align: justify;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE La REFORMA CON NO." + r.getCodigo() + " HA SIDO " + aprp
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + email + " relacionada con: " + clienteNotifacacion.getNombreCompleto());
    }

    public void dlogoObservaciones(ReformaTraspaso r, int num) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        if (busqueda.getAnio() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Elija un Período");
            return;
        }
        if (num == 2) {
            renderNegarSolicitud = true;
            renderAprobarSolicitud = false;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea(int num) {
        try {
            ReformaTraspaso aux = reformaTraspaso;
            clienteNotifacacion = solicitudService.devuelveClienteNotitfacion(BigInteger.valueOf(tramite.getNumTramite()));
            if (num == 0) {
                enviarCorreo(clienteNotifacacion.getEmail(), "REFORMA TRASPASO TIPO 1", "", clienteNotifacacion.getNombreCompleto(), aux, "NEGADA");
            } else {
                if (!enviar(reformaTraspaso)) {
                    return;
                }
                if (listaProducto.isEmpty() && !listaPartidaDirecta.isEmpty()) {
                    getParamts().put("usuarioPresupuesto", clienteService.getrolsUser(RolUsuario.rolReformasPDI));
                } else {
                    getParamts().put("usuarioPresupuesto", clienteService.getrolsUser(RolUsuario.presupuesto));
                }
            }
            observacion.setObservacion(observaciones);
            if (saveTramite() == null) {
                return;
            }
            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public Boolean getRenderNegarSolicitud() {
        return renderNegarSolicitud;
    }

    public void setRenderNegarSolicitud(Boolean renderNegarSolicitud) {
        this.renderNegarSolicitud = renderNegarSolicitud;
    }

    public Boolean getRenderAprobarSolicitud() {
        return renderAprobarSolicitud;
    }

    public void setRenderAprobarSolicitud(Boolean renderAprobarSolicitud) {
        this.renderAprobarSolicitud = renderAprobarSolicitud;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

    public ActividadOperativa getActividad() {
        return actividad;
    }

    public void setActividad(ActividadOperativa actividad) {
        this.actividad = actividad;
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

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
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

    public List<ProformaPresupuestoPlanificado> getProformaReformaList() {
        return proformaReformaList;
    }

    public void setProformaReformaList(List<ProformaPresupuestoPlanificado> proformaReformaList) {
        this.proformaReformaList = proformaReformaList;
    }

    public List<ProformaPresupuestoPlanificado> getShowCodigoRepetidosList() {
        return showCodigoRepetidosList;
    }

    public void setShowCodigoRepetidosList(List<ProformaPresupuestoPlanificado> showCodigoRepetidosList) {
        this.showCodigoRepetidosList = showCodigoRepetidosList;
    }

    public List<PlanLocalProgramaProyecto> getListaPlanLocalOriginal() {
        return listaPlanLocalOriginal;
    }

    public void setListaPlanLocalOriginal(List<PlanLocalProgramaProyecto> listaPlanLocalOriginal) {
        this.listaPlanLocalOriginal = listaPlanLocalOriginal;
    }

    public List<PlanLocalProgramaProyecto> getListaPlanLocalNuevo() {
        return listaPlanLocalNuevo;
    }

    public void setListaPlanLocalNuevo(List<PlanLocalProgramaProyecto> listaPlanLocalNuevo) {
        this.listaPlanLocalNuevo = listaPlanLocalNuevo;
    }

    public List<PlanAnualPoliticaPublica> getListaPlanPoliticaOriginal() {
        return listaPlanPoliticaOriginal;
    }

    public void setListaPlanPoliticaOriginal(List<PlanAnualPoliticaPublica> listaPlanPoliticaOriginal) {
        this.listaPlanPoliticaOriginal = listaPlanPoliticaOriginal;
    }

    public List<PlanAnualPoliticaPublica> getListaPlanPoliticaNuevo() {
        return listaPlanPoliticaNuevo;
    }

    public void setListaPlanPoliticaNuevo(List<PlanAnualPoliticaPublica> listaPlanPoliticaNuevo) {
        this.listaPlanPoliticaNuevo = listaPlanPoliticaNuevo;
    }

    public List<PlanAnualProgramaProyecto> getListaPlanAnualOriginal() {
        return listaPlanAnualOriginal;
    }

    public void setListaPlanAnualOriginal(List<PlanAnualProgramaProyecto> listaPlanAnualOriginal) {
        this.listaPlanAnualOriginal = listaPlanAnualOriginal;
    }

    public List<PlanAnualProgramaProyecto> getListaPlanAnualNuevo() {
        return listaPlanAnualNuevo;
    }

    public void setListaPlanAnualNuevo(List<PlanAnualProgramaProyecto> listaPlanAnualNuevo) {
        this.listaPlanAnualNuevo = listaPlanAnualNuevo;
    }

    public List<ActividadOperativa> getListaActividadOriginal() {
        return listaActividadOriginal;
    }

    public void setListaActividadOriginal(List<ActividadOperativa> listaActividadOriginal) {
        this.listaActividadOriginal = listaActividadOriginal;
    }

    public List<ActividadOperativa> getListaActividadNuevo() {
        return listaActividadNuevo;
    }

    public void setListaActividadNuevo(List<ActividadOperativa> listaActividadNuevo) {
        this.listaActividadNuevo = listaActividadNuevo;
    }

    public List<Producto> getListaProdutoOriginal() {
        return listaProdutoOriginal;
    }

    public void setListaProdutoOriginal(List<Producto> listaProdutoOriginal) {
        this.listaProdutoOriginal = listaProdutoOriginal;
    }

    public List<Producto> getListaProductoNuevo() {
        return listaProductoNuevo;
    }

    public void setListaProductoNuevo(List<Producto> listaProductoNuevo) {
        this.listaProductoNuevo = listaProductoNuevo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getTotalTraspasoReduccion() {
        return totalTraspasoReduccion;
    }

    public void setTotalTraspasoReduccion(double totalTraspasoReduccion) {
        this.totalTraspasoReduccion = totalTraspasoReduccion;
    }

    public double getTotalTraspasoIncremento() {
        return totalTraspasoIncremento;
    }

    public void setTotalTraspasoIncremento(double totalTraspasoIncremento) {
        this.totalTraspasoIncremento = totalTraspasoIncremento;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public Boolean getRenderTabPAPP() {
        return renderTabPAPP;
    }

    public void setRenderTabPAPP(Boolean renderTabPAPP) {
        this.renderTabPAPP = renderTabPAPP;
    }

    public Boolean getRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(Boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }

//</editor-fold>
}
