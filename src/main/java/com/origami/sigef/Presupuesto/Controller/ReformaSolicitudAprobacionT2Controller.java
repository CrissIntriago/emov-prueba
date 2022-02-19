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
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
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
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
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
 * @author Sandra Arroba
 */
@Named(value = "reformaT2SolicitudAprobacionView")
@ViewScoped
public class ReformaSolicitudAprobacionT2Controller extends BpmnBaseRoot implements Serializable {

    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private OpcionBusqueda busqueda;
    private Distributivo dataView;

    //PAPP
    private PlanLocalProgramaProyecto planLocal;
    private PlanAnualPoliticaPublica planPolitica;
    private PlanAnualProgramaProyecto planAnual;
    private ActividadOperativa actividad;
    private Producto producto;

    //PARTIDAS DISTRIBUTIVO
    private PartidasDistributivo partidaDistributivo;
    private PartidasDistributivoAnexo partidasDistributivoAnexo;
    private ProformaPresupuestoPlanificado partidasDirectas;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    private List<Producto> listaProducto;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;

    private List<Distributivo> listaDistributivo;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<PartidasDistributivo> listaVista;

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

    private List<PartidasDistributivo> listaPartidasDistributivoNuevo;
    private List<PartidasDistributivoAnexo> listaPartidasDistributivoAnexoNuevo;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private List<ProformaPresupuestoPlanificado> profromaReformaList;

    private String observaciones;
    private Cliente clienteNotifacacion;
    private BigDecimal rmu;

    private boolean vistaPartidaDisGeneral;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;

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
    private ActividadOperativaService actividadOperativaService;

    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;

    @Inject
    private KatalinaService katalinaService;

    @Inject
    private ReservaCompromisoService solicitudService;

    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private PresupuestoService presupuestoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    @Inject
    private PartidaDistributivoService partidasDistributivoService;
    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;

    @Inject
    private ValoresDistributivoService valoresService;

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
                dataView = new Distributivo();

                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");

                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                listaPlanProgramatico = new ArrayList<>();
                clienteNotifacacion = new Cliente();

                listaProducto = new ArrayList<>();
                listaPartidaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
                listaPartidaDirecta = new ArrayList<>();

                listaPartidasDistributivoNuevo = new ArrayList<>();
                listaPartidasDistributivoAnexoNuevo = new ArrayList<>();

                profromaReformaList = new ArrayList<>();
                showCodigoRepetidosList = new ArrayList<>();
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

    public boolean enviar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        CatalogoItem estadoReformAprob = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        CatalogoItem estadoReformaCambio = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        System.out.println("REFORMA: " + reformaTraspaso.getId());
        System.out.println("REFORMA: " + reformaTraspaso.getId());
        //<editor-fold defaultstate="collapsed" desc="VALIDACIONES: Comprobar si se ha ejecutado una reforma - los valores no son iguales, verificar códigos repetidos">
        //<editor-fold defaultstate="collapsed" desc="PAPP - PRODUCTOS">
        if (Utils.isNotEmpty(listaProducto)) {
            for (Producto producto1 : listaProducto) {
                if (producto1.getNumeroOrdenId() != null) {
                    Producto pro = reformaTraspasoService.getProductoById(producto1.getId());
                    System.out.println("Descripciòn: "+producto1.getDescripcion());
//                    System.out.println("PRODUCTO: " + producto1.getId());
//                    System.out.println("PRO: " + pro.getId());
//                    System.out.println("PRO MONTO: " + pro.getMonto());
                    Producto proOriginal = reformaTraspasoService.getProductoByIdReformaCopia(producto1.getNumeroOrdenId().longValue(), estadoReformAprob);
//                    System.out.println("PARAMETRO 1: " + producto1.getNumeroOrdenId() + "     " + estadopapp);
                    BigDecimal montoRefTras = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().subtract(proOriginal.getTraspasoReduccion()));
//                    System.out.println("PARAMETROS: " + proOriginal.getTraspasoIncremento() + "       " + proOriginal.getTraspasoReduccion());
                    BigDecimal montoRefSupRed = montoRefTras.add(proOriginal.getSuplementoEgreso().subtract(proOriginal.getReduccionEgreso()));
//                    System.out.println("PARAMETROS: " + proOriginal.getSuplementoEgreso() + "       " + proOriginal.getReduccionEgreso());
                    System.out.println("Monto Reformado por medio de sumas: " + montoRefSupRed);
                    System.out.println("Monto Reformado del Reforma Directa Ref: " + pro.getMontoReformada());
                    System.out.println("Monto Reformado del Reforma Directa: " + pro.getMonto());
                    if (montoRefSupRed.compareTo(pro.getMonto()) != 0) {
                        JsfUtil.addErrorMessage("Producto", "Se ha generado otro tipo de reforma, no puede continuar.");
                        return false;
                    }
                    proOriginal.setMontoReformada(montoRefSupRed);
                }
            }
        }
//</editor-fold>

        if (!verificarRepetidosProformaReforma(reformaTraspaso)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "HAY CODIGOS REPETIDOS POR FAVOR REVISAR");
            PrimeFaces.current().executeScript("PF('dlogoCodigoRepetidos').show()");
            PrimeFaces.current().ajax().update("dlogoPartidasRepetidas");

            return false;
        }
//</editor-fold>

        reformaTraspaso.setEstadoReforma(estadoReformaCambio);
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspaso.setFechaAprobacion(new Date());
        reformaTraspasoService.edit(reformaTraspaso);

        /*Cambiar los valores al PAPP original*/
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));

        CatalogoItem estadopappFinal = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        CatalogoItem estadodistri = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZAR PAPP ORIGINAL y PRESUPUESTO">
        if (!listaProducto.isEmpty()) {
            listaPlanLocalNuevo = reformaTraspasoService.showPlanLocalNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaPlanLocalNuevo.isEmpty()) {

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
                System.out.println("No hay Plan Local Nuevo");
            }

            listaPlanPoliticaNuevo = reformaTraspasoService.showPlanPoliticalNuevo(BigInteger.valueOf(reformaTraspaso.getId()));

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
                System.out.println("No hay Plan Política Nuevo");
            }

            listaPlanAnualNuevo = reformaTraspasoService.showPlanAnualNuevo(BigInteger.valueOf(reformaTraspaso.getId()));
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
                System.out.println("No hay PlanAnualProgramaProyecto Nuevo");
            }

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
                System.out.println("No hay Actividad Operativa Nuevo");
            }

            for (Producto producto1 : listaProducto) {
                if (producto1.getNumeroOrdenId() != null) {
                    producto1.setEstadoPapp(estadopappFinal);
                    productoService.edit(producto1);
                    System.out.println("producto1--------------------- " + producto1);
//                    Producto pro = reformaTraspasoService.getProductoById(producto1.getId());
                    System.out.println("Descripción " + producto1.getDescripcion());
                    Producto proOriginal = reformaTraspasoService.getProductoByIdReformaCopia(producto1.getNumeroOrdenId().longValue(), estadoReformAprob);
                    BigDecimal montoRefTras = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().add(proOriginal.getSuplementoEgreso()));
                    BigDecimal montoRefSupRed = montoRefTras.subtract(proOriginal.getTraspasoReduccion().add(proOriginal.getReduccionEgreso()));
                    proOriginal.setTraspasoIncremento(proOriginal.getTraspasoIncremento().add(producto1.getTraspasoIncremento()));
                    proOriginal.setTraspasoReduccion(proOriginal.getTraspasoReduccion().add(producto1.getTraspasoReduccion()));
                    System.out.println("Incremento producto1: " + producto1.getTraspasoIncremento());
                    System.out.println("REducción producto1: " + producto1.getTraspasoReduccion());
                    System.out.println("Monto Reformado por medio de sumas: " + montoRefSupRed);
                    System.out.println("Monto Reformado del Reforma Directa-: " + producto1.getMontoReformada());
                    System.out.println("Monto del Reforma Directa: " + producto1.getMonto());
                    if (montoRefSupRed.compareTo(producto1.getMonto()) != 0) {
                        JsfUtil.addErrorMessage("Producto", "Se ha generado otro tipo de reforma, no puede continuar.");
                        return false;
                    }
                    System.out.println("Incremento proOriginal: " + proOriginal.getTraspasoIncremento());
                    System.out.println("REducción proOriginal: " + proOriginal.getTraspasoReduccion());
                    productoService.edit(proOriginal);
                    BigDecimal montoRefTrasp = proOriginal.getMonto().add(proOriginal.getTraspasoIncremento().add(proOriginal.getSuplementoEgreso()));
                    BigDecimal montoRefSupReduc = montoRefTras.subtract(proOriginal.getTraspasoReduccion().add(proOriginal.getReduccionEgreso()));
                    System.out.println("Monto Reformado ant- -: " + montoRefSupReduc);
                    proOriginal.setMontoReformada(producto1.getMontoReformada());
                    proOriginal.setSaldoDisponible(proOriginal.getMontoReformada().subtract(proOriginal.getReserva()));
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
                    producto.setTraspasoIncremento(producto1.getTraspasoIncremento());
                    producto.setTraspasoReduccion(BigDecimal.ZERO);
                    producto.setSuplementoEgreso(BigDecimal.ZERO);
                    producto.setReduccionEgreso(BigDecimal.ZERO);
                    producto.setMontoReformada(producto1.getMontoReformada());
                    producto.setSaldoDisponible(producto.getMontoReformada().subtract(producto.getReserva()));
                    producto.setCodigoReformaTraspaso(null);
                    producto.setNumeroOrdenId(null);
                    producto.setNumeroTramite(null);
                    producto.setEstadoPapp(estado2);
                    producto.setMonto(BigDecimal.ZERO);
                    producto.setRegistroNuevoReferencia(BigInteger.valueOf(producto1.getId()));
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
                        pre.setEstructruaNew(producto1.getEstructruaNew());
                        pre.setItemNew(producto1.getItemNew());
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
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO Y PRESUPUESTO">
        listaPartidasDistributivoNuevo = reformaTraspasoService.showPartidasDistributivoNnuevo(BigInteger.valueOf(reformaTraspaso.getId()));
        if (!listaPartidasDistributivoNuevo.isEmpty()) {
            for (PartidasDistributivo dataDistributivo : listaPartidasDistributivoNuevo) {
                partidaDistributivo = new PartidasDistributivo();
                if (dataDistributivo.getCodigoReferencia() != null) {
                    partidaDistributivo = reformaTraspasoService.getPartidasDistributivoNuevo(dataDistributivo.getCodigoReferencia());
                    partidaDistributivo.setTraspasoIncremento(dataDistributivo.getTraspasoIncremento().add(partidaDistributivo.getTraspasoIncremento()));
                    partidaDistributivo.setTraspasoReduccion(dataDistributivo.getTraspasoReduccion().add(partidaDistributivo.getTraspasoReduccion()));

                    partidaDistributivo.setEstadoPartida(estadodistri);
                    partidaDistributivo.setUsuarioModificacion(userSession.getNameUser());
                    partidaDistributivo.setFechaModificacion(new Date());
                    partidaDistributivo.setItemAp(dataDistributivo.getItemAp());
                    partidaDistributivo.setEstructuraAp(dataDistributivo.getEstructuraAp());
                    partidaDistributivo.setFuenteAp(dataDistributivo.getFuenteAp());
                    partidaDistributivo.setFuenteDirecta(dataDistributivo.getFuenteDirecta());
                    partidaDistributivo.setPartidaAp(dataDistributivo.getPartidaAp());
                    BigDecimal result = partidaDistributivo.getMonto().add(partidaDistributivo.getTraspasoIncremento()).add(partidaDistributivo.getReformaSuplemento());

                    if (result == null) {
                        result = BigDecimal.ZERO;
                    }
                    BigDecimal reducc = partidaDistributivo.getTraspasoReduccion().add(partidaDistributivo.getReformaReduccion());
                    partidaDistributivo.setReformaCodificado(result.subtract(reducc));
                    partidasDistributivoService.edit(partidaDistributivo);
                } else {
                    partidaDistributivo = Utils.clone(dataDistributivo);
                    partidaDistributivo.setId(null);
                    partidaDistributivo.setCodigoReforma(null);
                    partidaDistributivo.setCodigoReformaTraspaso(null);
                    partidaDistributivo.setEstadoPartida(estadodistri);
                    partidaDistributivo.setUsuarioModificacion(userSession.getNameUser());
                    partidaDistributivo.setUsuarioCreacion(userSession.getNameUser());
                    partidaDistributivo.setFechaModificacion(new Date());
                    partidaDistributivo.setFechaCreacion(new Date());
                    partidaDistributivo.setMonto(BigDecimal.ZERO);
                    partidaDistributivo = partidasDistributivoService.create(partidaDistributivo);

                }
                if (dataDistributivo.getTraspasoIncremento() != BigDecimal.ZERO || dataDistributivo.getTraspasoReduccion() != BigDecimal.ZERO) {
                    Presupuesto presupuesto = reformaTraspasoService.valoresPresupuestoPapp(busqueda.getAnio(), dataDistributivo.getPartidaAp());
                    if (presupuesto != null) {
                        presupuesto.setReformaSuplemetario(presupuesto.getReformaSuplemetario().add(dataDistributivo.getTraspasoIncremento()));
                        presupuesto.setReformaReducido(presupuesto.getReformaReducido().add(dataDistributivo.getTraspasoReduccion()));
                        presupuesto.setCodificado((presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario())).subtract(presupuesto.getReformaReducido()));
                        presupuestoService.edit(presupuesto);
                    } else {
                        Presupuesto pre = new Presupuesto();
                        pre.setEstructura(dataDistributivo.getEstructuraAp());
                        pre.setItem(dataDistributivo.getItemAp());
                        pre.setFuente(dataDistributivo.getFuenteAp());
                        pre.setFuenteDirecta(dataDistributivo.getFuenteDirecta());
                        pre.setPartida(dataDistributivo.getPartidaAp());
                        pre.setTipo(false);
                        pre.setPeriodo(dataDistributivo.getPeriodo());
                        pre.setComprometido(BigDecimal.ZERO);
                        pre.setPresupuestoInicial(BigDecimal.ZERO);
                        pre.setReformaSuplemetario(dataDistributivo.getTraspasoIncremento());
                        pre.setReformaReducido(dataDistributivo.getTraspasoReduccion());
                        pre.setCodificado((pre.getPresupuestoInicial().add(pre.getReformaSuplemetario())).subtract(pre.getReformaReducido()));
                        presupuestoService.create(pre);
                    }
                }

            }
        }

//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO ANEXO">
        listaPartidasDistributivoAnexoNuevo = reformaTraspasoService.showPartidasDistributivoAnexoNnuevo(BigInteger.valueOf(reformaTraspaso.getId()));

        if (!listaPartidasDistributivoAnexoNuevo.isEmpty()) {
            for (PartidasDistributivoAnexo dataAnexo : listaPartidasDistributivoAnexoNuevo) {
                partidasDistributivoAnexo = new PartidasDistributivoAnexo();
                if (dataAnexo.getCodigoReferencia() != null) {
                    partidasDistributivoAnexo = reformaTraspasoService.getPartidasDistributivoAnexoNuevo(dataAnexo.getCodigoReferencia());
                    partidasDistributivoAnexo.setTraspasoIncremento(partidasDistributivoAnexo.getTraspasoIncremento().add(dataAnexo.getTraspasoIncremento()));
                    partidasDistributivoAnexo.setTraspasoReduccion(partidasDistributivoAnexo.getTraspasoReduccion().add(dataAnexo.getTraspasoReduccion()));
                    partidasDistributivoAnexo.setReformaCodificado(partidasDistributivoAnexo.getMonto().add(partidasDistributivoAnexo.getTraspasoIncremento()).subtract(partidasDistributivoAnexo.getTraspasoReduccion()));
                    partidasDistributivoAnexo.setFechaModificacion(new Date());
                    partidasDistributivoAnexo.setUsuarioModificacion(userSession.getNameUser());
                    partidasDistributivoAnexo.setEstadoPartida(estadodistri);
                    partidaAnexoService.edit(partidasDistributivoAnexo);
                } else {

                    partidasDistributivoAnexo = Utils.clone(dataAnexo);
                    partidasDistributivoAnexo.setId(null);
                    partidasDistributivoAnexo.setUsuarioModificacion(userSession.getNameUser());
                    partidasDistributivoAnexo.setFechaModificacion(new Date());
                    partidasDistributivoAnexo.setEstadoPartida(estadodistri);
                    partidasDistributivoAnexo.setCodigoReforma(null);
                    partidasDistributivoAnexo.setCodigoReformaTraspaso(null);
                    partidasDistributivoAnexo.setMonto(BigDecimal.ZERO);
                    partidasDistributivoAnexo = partidaAnexoService.create(partidasDistributivoAnexo);

                }
            }

        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DIRECTAS">
        listaPartidasDirectas = reformaTraspasoService.showPartidasDirectasNnuevo(BigInteger.valueOf(reformaTraspaso.getId()));
        if (!listaPartidasDirectas.isEmpty()) {
            for (ProformaPresupuestoPlanificado item : listaPartidasDirectas) {
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
            }
        }

//</editor-fold>

        if (!actualizarProformaReforma(reformaTraspaso)) {
            return false;
        }
        
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " aprobado informe con éxito");
        
        return true;
    }

    public boolean verificarRepetidosProformaReforma(ReformaTraspaso r) {
        Boolean verificar = true;
        BigInteger integer = BigInteger.valueOf(r.getId());
        proformaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaTraspasoService.getPappGroupReforma(r.getPeriodo(), r);
        List<ProformaPDTO> listaagrupacionDistr = reformaTraspasoService.showCodigosAgrupadosReformas(r.getPeriodo(), r);
        List<PartidasDistributivoAnexo> listaagrupacionDistrAnexo = partidasDistributivoAnexoService.showAllPartidasAnexoReforma(r.getPeriodo(), r);
        List<ProformaPresupuestoPlanificado> listaPartidasdirectas = reformaTraspasoService.showPartidaDirectasReforma(r.getPeriodo(), r);

        //<editor-fold defaultstate="collapsed" desc="AGRUPAR PAPP">
        if (Utils.isNotEmpty(listaAgrupacionPapp)) {
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
        if (Utils.isNotEmpty(listaagrupacionDistr)) {

            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO i : listaagrupacionDistr) {

                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (i.getPartida() == null ? data.getPartidaPresupuestaria() == null : i.getPartida().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(i.getPartida());
                proformaPresupuesto.setValor(i.getTotal());
                proformaPresupuesto.setReformaSuplemento(i.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(i.getReformaReduccion());
                proformaPresupuesto.setTraspasoIncremento(i.getIncrementoTraspaso());
                proformaPresupuesto.setTraspasoReduccion(i.getReduccionTraspaso());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
                proformaPresupuesto.setEstructuraProgramatica(i.getEstructuraProgramatica());
                proformaPresupuesto.setItemPresupuestario(i.getItemPresupuestario());
                proformaPresupuesto.setFuente(i.getFuente());
                proformaPresupuesto.setFuenteDirecta(i.getFuenteDirecta());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PD");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR DISTRIBUTIVO ANEXO">
        if (Utils.isNotEmpty(listaagrupacionDistrAnexo)) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (PartidasDistributivoAnexo p : listaagrupacionDistrAnexo) {
                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (p.getPartidaAp() == null ? data.getPartidaPresupuestaria() == null : p.getPartidaAp().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(p.getPartidaAp());
                proformaPresupuesto.setValor(p.getMonto());
                proformaPresupuesto.setEstructuraProgramatica(p.getEstructuraApA());
                proformaPresupuesto.setItemPresupuestario(p.getItemApA());
                proformaPresupuesto.setFuente(p.getFuenteApA());
                proformaPresupuesto.setFuenteDirecta(p.getFuenteDirectaA());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setTraspasoIncremento(p.getTraspasoIncremento());
                proformaPresupuesto.setTraspasoReduccion(p.getTraspasoReduccion());
                proformaPresupuesto.setReformaSuplemento(p.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(p.getReformaReduccion());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento().add(proformaPresupuesto.getTraspasoIncremento())).subtract(proformaPresupuesto.getReformaReduccion().add(proformaPresupuesto.getTraspasoReduccion())));
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PDA");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }

        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR PARTIDAS DIRECTAS">
        if (Utils.isNotEmpty(listaPartidasdirectas)) {

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
            verificar = false;
        }
        return verificar;
    }

    public boolean actualizarProformaReforma(ReformaTraspaso r) {
        Boolean verificar = true;
        BigInteger integer = BigInteger.valueOf(r.getId());
        proformaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaTraspasoService.getPappGroupReforma(r.getPeriodo(), r);
        List<ProformaPDTO> listaagrupacionDistr = reformaTraspasoService.showCodigosAgrupadosReformas(r.getPeriodo(), r);
        List<PartidasDistributivoAnexo> listaagrupacionDistrAnexo = partidasDistributivoAnexoService.showAllPartidasAnexoReforma(r.getPeriodo(), r);
        List<ProformaPresupuestoPlanificado> listaPartidasdirectas = reformaTraspasoService.showPartidaDirectasReforma(r.getPeriodo(), r);

        //<editor-fold defaultstate="collapsed" desc="AGRUPAR PAPP">
        if (Utils.isNotEmpty(listaAgrupacionPapp)) {
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
        if (Utils.isNotEmpty(listaagrupacionDistr)) {

            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO i : listaagrupacionDistr) {

                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (i.getPartida() == null ? data.getPartidaPresupuestaria() == null : i.getPartida().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(i.getPartida());
                proformaPresupuesto.setValor(i.getTotal());
                proformaPresupuesto.setReformaSuplemento(i.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(i.getReformaReduccion());
                proformaPresupuesto.setTraspasoIncremento(i.getIncrementoTraspaso());
                proformaPresupuesto.setTraspasoReduccion(i.getReduccionTraspaso());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
                proformaPresupuesto.setEstructuraProgramatica(i.getEstructuraProgramatica());
                proformaPresupuesto.setItemPresupuestario(i.getItemPresupuestario());
                proformaPresupuesto.setFuente(i.getFuente());
                proformaPresupuesto.setFuenteDirecta(i.getFuenteDirecta());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PD");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR DISTRIBUTIVO ANEXO">
        if (Utils.isNotEmpty(listaagrupacionDistrAnexo)) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (PartidasDistributivoAnexo p : listaagrupacionDistrAnexo) {
                for (ProformaPresupuestoPlanificado data : proformaReformaList) {
                    if (p.getPartidaAp() == null ? data.getPartidaPresupuestaria() == null : p.getPartidaAp().equals(data.getPartidaPresupuestaria())) {
                        showCodigoRepetidosList.add(data);
                    }
                }

                proformaPresupuesto.setPartidaPresupuestaria(p.getPartidaAp());
                proformaPresupuesto.setValor(p.getMonto());
                proformaPresupuesto.setEstructuraProgramatica(p.getEstructuraApA());
                proformaPresupuesto.setItemPresupuestario(p.getItemApA());
                proformaPresupuesto.setFuente(p.getFuenteApA());
                proformaPresupuesto.setFuenteDirecta(p.getFuenteDirectaA());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
                proformaPresupuesto.setTraspasoIncremento(p.getTraspasoIncremento());
                proformaPresupuesto.setTraspasoReduccion(p.getTraspasoReduccion());
                proformaPresupuesto.setReformaSuplemento(p.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(p.getReformaReduccion());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento().add(proformaPresupuesto.getTraspasoIncremento())).subtract(proformaPresupuesto.getReformaReduccion().add(proformaPresupuesto.getTraspasoReduccion())));
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo("PDA");
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                proformaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }

        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="AGREGAR PARTIDAS DIRECTAS">
        if (Utils.isNotEmpty(listaPartidasdirectas)) {
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

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZANDO PROFORMA">
        List<ProformaPresupuestoPlanificado> proformaVieja = reformaTraspasoService.proformaVieja(r.getPeriodo());
        int count = 0;
        if (Utils.isNotEmpty(proformaVieja) && Utils.isNotEmpty(proformaReformaList)) {
            for (ProformaPresupuestoPlanificado x : proformaVieja) {
                for (ProformaPresupuestoPlanificado y : proformaReformaList) {
                    ProformaPresupuestoPlanificado editProforma = new ProformaPresupuestoPlanificado();
                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {
                        editProforma = reformaTraspasoService.editProformavieja(x);
                        editProforma.setReformaSuplemento(y.getReformaSuplemento().add(x.getReformaSuplemento()));
                        editProforma.setReformaReduccion(y.getReformaReduccion().add(x.getReformaReduccion()));
                        editProforma.setTraspasoIncremento(y.getTraspasoIncremento().add(x.getTraspasoIncremento()));
                        editProforma.setTraspasoReduccion(y.getTraspasoReduccion().add(x.getTraspasoReduccion()));
                        editProforma.setReformaCodificado(editProforma.getValor().add(editProforma.getTraspasoIncremento().add(editProforma.getReformaSuplemento())).subtract(editProforma.getTraspasoReduccion().add(editProforma.getReformaReduccion())));
                        editProforma.setFechaModificacion(new Date());
                        editProforma.setUsuarioModificacion(userSession.getNameUser());
                        proformaService.edit(editProforma);
                        count = count + 1;
                    }
                }
            }
        }

        if (Utils.isNotEmpty(proformaVieja) && Utils.isNotEmpty(proformaReformaList)) {
            count = 0;
            boolean verificador = true;
            ProformaPresupuestoPlanificado editPro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado x : proformaReformaList) {
                editPro = new ProformaPresupuestoPlanificado();
                verificador = true;
                for (ProformaPresupuestoPlanificado y : proformaVieja) {
                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {
                        verificador = false;
                    }
                }
                if (verificador) {
                    editPro = Utils.clone(x);
                    editPro.setId(null);
                    editPro.setReformaCodificado(editPro.getValor().add(editPro.getReformaSuplemento()).subtract(editPro.getReformaReduccion()));
                    editPro.setFechaModificacion(new Date());
                    editPro.setUsuarioModificacion(userSession.getNameUser());
                    editPro.setCodigoReforma(null);
                    editPro.setCodigoReformaTraspaso(null);
                    editPro.setValor(BigDecimal.ZERO);
                    editPro = proformaService.create(editPro);
                    count = count + 1;
                }

            }

        }
//</editor-fold>

//        actualizarTotalIngresosEgresos(r);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Actualizado la proforma");
        return true;

    }

    public void consultarPapp(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta(reforma.getPeriodo(), true, BigInteger.valueOf(reforma.getId()));
        if (listaProducto.isEmpty()) {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "APRT", true, BigInteger.valueOf(reforma.getId()));
        }
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        busqueda.setAnio(reformaTraspaso.getPeriodo());

        renderedPAPP = !listaProducto.isEmpty();
        renderedDistributivo = !listaDistributivo.isEmpty();
        renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();

        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
    }

    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        dataView = d;
      //  setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        if (vista) {
            vistaPartidaDisGeneral = true;
        } else {
            vistaPartidaDisGeneral = false;
        }
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void enviarCorreo(String email, String asunto, String mensaje, String userStart, ReformaTraspaso r) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;text-align: justify;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE La REFORMA CON NO." + r.getCodigo() + " HA SIDO APROBADO "
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito a la dirección de email: " + email + " relacionada con: " + clienteNotifacacion.getNombreCompleto());

    }

    public void dlogoObservaciones(ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        if (busqueda.getAnio() == null) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Elija un Período");
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
            ReformaTraspaso aux = this.reformaTraspaso;
            if (!enviar(reformaTraspaso)) {
                return;
            }
            clienteNotifacacion = solicitudService.devuelveClienteNotitfacion(BigInteger.valueOf(tramite.getNumTramite()));
            enviarCorreo(clienteNotifacacion.getEmail(), "REFORMA TIPO 2", "", clienteNotifacacion.getNombreCompleto(), aux);
            observacion.setObservacion(observaciones);
//           clienteService.getUnidadUserCodigo("THU", "")
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

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<PartidasDistributivo> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<PartidasDistributivo> listaVista) {
        this.listaVista = listaVista;
    }

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
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

    public Cliente getClienteNotifacacion() {
        return clienteNotifacacion;
    }

    public void setClienteNotifacacion(Cliente clienteNotifacacion) {
        this.clienteNotifacacion = clienteNotifacacion;
    }

    public PlanLocalProgramaProyecto getPlanLocal() {
        return planLocal;
    }

    public void setPlanLocal(PlanLocalProgramaProyecto planLocal) {
        this.planLocal = planLocal;
    }

    public PlanAnualPoliticaPublica getPlanPolitica() {
        return planPolitica;
    }

    public void setPlanPolitica(PlanAnualPoliticaPublica planPolitica) {
        this.planPolitica = planPolitica;
    }

    public PlanAnualProgramaProyecto getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualProgramaProyecto planAnual) {
        this.planAnual = planAnual;
    }

    public PartidasDistributivo getPartidaDistributivo() {
        return partidaDistributivo;
    }

    public void setPartidaDistributivo(PartidasDistributivo partidaDistributivo) {
        this.partidaDistributivo = partidaDistributivo;
    }

    public ProformaPresupuestoPlanificado getPartidasDirectas() {
        return partidasDirectas;
    }

    public void setPartidasDirectas(ProformaPresupuestoPlanificado partidasDirectas) {
        this.partidasDirectas = partidasDirectas;
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

    public List<PartidasDistributivo> getListaPartidasDistributivoNuevo() {
        return listaPartidasDistributivoNuevo;
    }

    public void setListaPartidasDistributivoNuevo(List<PartidasDistributivo> listaPartidasDistributivoNuevo) {
        this.listaPartidasDistributivoNuevo = listaPartidasDistributivoNuevo;
    }

    public List<PartidasDistributivoAnexo> getListaPartidasDistributivoAnexoNuevo() {
        return listaPartidasDistributivoAnexoNuevo;
    }

    public void setListaPartidasDistributivoAnexoNuevo(List<PartidasDistributivoAnexo> listaPartidasDistributivoAnexoNuevo) {
        this.listaPartidasDistributivoAnexoNuevo = listaPartidasDistributivoAnexoNuevo;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidasDirectas() {
        return listaPartidasDirectas;
    }

    public void setListaPartidasDirectas(List<ProformaPresupuestoPlanificado> listaPartidasDirectas) {
        this.listaPartidasDirectas = listaPartidasDirectas;
    }

    public List<ProformaPresupuestoPlanificado> getProfromaReformaList() {
        return profromaReformaList;
    }

    public void setProfromaReformaList(List<ProformaPresupuestoPlanificado> profromaReformaList) {
        this.profromaReformaList = profromaReformaList;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
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
//</editor-fold>

}
