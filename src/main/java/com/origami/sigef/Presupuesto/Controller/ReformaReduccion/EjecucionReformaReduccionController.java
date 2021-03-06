/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.DetalleSuplementoIngresoService;
import com.origami.sigef.Presupuesto.Service.ProformaIngresoService;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.model.presupuestoModel;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.ProgramacionIngresoEgresoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
@Named(value = "ejecucionReduccionReformaview")
@ViewScoped
public class EjecucionReformaReduccionController extends BpmnBaseRoot implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @Inject
    private UserSession user;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ProductoService productoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    @Inject
    private PresupuestoService presupuestoService;
    @Inject
    private PartidaDistributivoService partidasDistributivoService;
    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;
    @Inject
    private CatalogoPresupuestoService itemService;
    @Inject
    private DetalleSuplementoIngresoService detalleIngresoService;
    @Inject
    private ReformaSuplementoIngresoService reformaService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CatalogoProformaPresupuestoService mantenimientoProformaService;
    @Inject
    private ProgramacionIngresoEgresoService pimService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private ReformaCupoReduccionService cupoReduccionService;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="OBJETOS">
    //REFORMA
    private ReformaIngresoSuplemento reforma;

    //INGRESOS
    private CatalogoPresupuesto itemIngreso;
    private DetalleReformaIngresoSuplemento detalleItemReformaIngreso;

    //PAPP
    //private PlanLocalProgramaProyecto planLocal;
    //private PlanAnualPoliticaPublica planPolitica;
    //private PlanAnualProgramaProyecto planAnual;
    private ActividadOperativa actividad;
    private Producto producto;

//PARTIDAS DISTRIBUTIVO
    private ThCargoRubros partidaDistributivo;
    //private PartidasDistributivoAnexo partidasDistributivoAnexo;
    private ProformaPresupuestoPlanificado partidasDirectas;

    //PRESUPUESTO
    private Presupuesto presupuesto;
    private ProgramacionIngresoEgreso pim;

    //CUPOS
    private CupoReduccion cupoReduccion;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="LISTAS">
    private List<ProformaIngreso> listaitemOriginal;
    private List<DetalleReformaIngresoSuplemento> listaItemNuevo;

//    private List<PlanLocalProgramaProyecto> listaPlanLocalOriginal;
//    private List<PlanLocalProgramaProyecto> listaPlanLocalNuevo;
//    
//    private List<PlanAnualPoliticaPublica> listaPlanPoliticaOriginal;
//    private List<PlanAnualPoliticaPublica> listaPlanPoliticaNuevo;
//    
//    private List<PlanAnualProgramaProyecto> listaPlanAnualOriginal;
//    private List<PlanAnualProgramaProyecto> listaPlanAnualNuevo;
//    
    private List<ActividadOperativa> listaActividadOriginal;
    private List<ActividadOperativa> listaActividadNuevo;

    private List<Producto> listaProdutoOriginal;
    private List<Producto> listaProductoNuevo;

    private List<ThCargoRubros> listaPartidasDistributivoNuevo;
    //  private List<PartidasDistributivoAnexo> listaPartidasDistributivoAnexoNuevo;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private List<ProformaPresupuestoPlanificado> profromaReformaList;

    private List<ProformaPresupuestoPlanificado> showCodigoRepetidosList;

    private List<presupuestoModel> lista;
    private List<presupuestoModel> lista2;

    private List<Presupuesto> listaPresupuestoNuevo;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="LAZY">
    private LazyModel<ReformaIngresoSuplemento> lazyReforma;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="VARIABLES LOCALES">
    private String observaciones;
    private boolean verificacionGlobal;
//</editor-fold>

    @Inject
    private ProformaIngresoService proformaIngresoService;

    @Inject
    private ThCargoRubrosService thCargoRubrosService;

    @PostConstruct
    public void inicializador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.reforma = new ReformaIngresoSuplemento();
                this.itemIngreso = new CatalogoPresupuesto();
                this.detalleItemReformaIngreso = new DetalleReformaIngresoSuplemento();
//                this.planLocal = new PlanLocalProgramaProyecto();
//                this.planPolitica = new PlanAnualPoliticaPublica();
//                this.planAnual = new PlanAnualProgramaProyecto();
                this.actividad = new ActividadOperativa();
                this.producto = new Producto();
                this.partidaDistributivo = new ThCargoRubros();
                //  this.partidasDistributivoAnexo = new PartidasDistributivoAnexo();
                this.partidasDirectas = new ProformaPresupuestoPlanificado();
                verificacionGlobal = false;
                this.listaitemOriginal = new ArrayList<>();
                this.listaItemNuevo = new ArrayList<>();
//                this.listaPlanLocalOriginal = new ArrayList<>();
//                this.listaPlanLocalNuevo = new ArrayList<>();
//                this.listaPlanPoliticaOriginal = new ArrayList<>();
//                this.listaPlanPoliticaNuevo = new ArrayList<>();
//                this.listaPlanAnualOriginal = new ArrayList<>();
//                this.listaPlanAnualNuevo = new ArrayList<>();
                this.listaActividadOriginal = new ArrayList<>();
                this.listaActividadNuevo = new ArrayList<>();
                this.listaProdutoOriginal = new ArrayList<>();
                this.listaProductoNuevo = new ArrayList<>();
                lazyReforma = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReforma.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyReforma.getFilterss().put("tipo:equal", false);
                profromaReformaList = new ArrayList<>();
                showCodigoRepetidosList = new ArrayList<>();
                pim = new ProgramacionIngresoEgreso();
                lista = new ArrayList<>();
                lista2 = new ArrayList<>();
                listaPresupuestoNuevo = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /**
     * modificara todos valores en el PAPP, Partidas Distributivo, Partidas
     * Distributivo , Partidas Directas, ademas de la proforma y preuspuesto, y
     * de los item de ingresos.
     *
     * @param r ==> se le envia com parametro a la reforma que se va a ejecutar
     */
    public void ejecutarReforma(ReformaIngresoSuplemento r) {
        verificacionGlobal = false;
        verificacionGlobal = confirmarEjecucion(r);
        if (verificacionGlobal) {
            JsfUtil.addWarningMessage("AVISO", "LA REFORMA NO FUE EJECUTADA PORQUE ANTES SE HA EJECUTADO UNA, SE HA PROCEDIDO A RECHAZARLA PARA QUE REVISE SU REFORMA");
            return;
        }

        //        this.listaPlanLocalOriginal = new ArrayList<>();
//        this.listaPlanLocalNuevo = new ArrayList<>();
//        this.listaPlanPoliticaOriginal = new ArrayList<>();
//        this.listaPlanPoliticaNuevo = new ArrayList<>();
//        this.listaPlanAnualOriginal = new ArrayList<>();
//        this.listaPlanAnualNuevo = new ArrayList<>();
        this.listaitemOriginal = new ArrayList<>();
        this.listaItemNuevo = new ArrayList<>();
        this.listaActividadOriginal = new ArrayList<>();
        this.listaActividadNuevo = new ArrayList<>();
        this.listaProdutoOriginal = new ArrayList<>();
        this.listaProductoNuevo = new ArrayList<>();
        profromaReformaList = new ArrayList<>();
        reforma = new ReformaIngresoSuplemento();
        reforma = r;
        CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        CatalogoItem estadoPartida = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");

        listaItemNuevo = new ArrayList<>();
        listaItemNuevo = reformaService.getDetalleReormaIngreso(r);

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZANDO INGRESOS">
        if (listaItemNuevo != null && !listaItemNuevo.isEmpty()) {

            ProformaIngreso ci = new ProformaIngreso();
            for (DetalleReformaIngresoSuplemento data : listaItemNuevo) {

                ci = new ProformaIngreso();
                if (data.getCodigoReferencia() != null) {
                    ci = reformaService.getShowUTem(data.getCodigoReferencia());
                    ci.setReformaSuplementaria(ci.getReformaSuplementaria().add(data.getSuplemento()));
                    ci.setReformaReduccion(ci.getReformaReduccion().add(data.getReducido()));
                    ci.setPresupuestoCodificado(ci.getPresupuestoInicial().add(ci.getReformaSuplementaria()).subtract(ci.getReformaReduccion()));
                    ci.setFechaModificacion(new Date());
                    ci.setUsuarioModificacion(user.getNameUser());

                    proformaIngresoService.edit(ci);

                } else {
                    ci = new ProformaIngreso();
                    ci = data.getProformaIngreso();
                    ci.setId(null);
                    ci.setReformaSuplementaria(data.getSuplemento());
                    ci.setReformaReduccion(data.getReducido());
                    ci.setPresupuestoCodificado(data.getCodificado());
                    ci.setFechaModificacion(new Date());
                    ci.setUsuarioModificacion(user.getNameUser());
                    ci = proformaIngresoService.create(ci);
                }

            }

        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ACTUAIZANDO EGRESOS">
//<editor-fold defaultstate="collapsed" desc="PAPP">
//        listaPlanLocalNuevo = reformaService.showPlanaLocalNuevo(BigInteger.valueOf(r.getId()));
//        if (!listaPlanLocalNuevo.isEmpty()) {
//
//            for (PlanLocalProgramaProyecto dataLocal : listaPlanLocalNuevo) {
//                planLocal = new PlanLocalProgramaProyecto();
//                if (dataLocal.getNumeroOrdenId() != null) {
//                    planLocal = reformaService.getPlanlocal(dataLocal.getNumeroOrdenId());
//                    planLocal.setUsuarioModifica(user.getNameUser());
//                    planLocal.setFechaModificacion(new Date());
//                    planLocal.setEstadoPapp(estado);
//                    planLocalProgramaProyectoService.edit(planLocal);
//                } else {
//
//                    planLocal = Utils.clone(dataLocal);
//                    planLocal.setId(null);
//                    planLocal.setCodigoReforma(null);
//                    planLocal.setCodigoReformaTraspaso(null);
//                    planLocal.setUsuarioModifica(user.getNameUser());
//                    planLocal.setFechaModificacion(new Date());
//                    planLocal.setEstadoPapp(estado);
//                    planLocal.setRegistroNuevoReferencia(BigInteger.valueOf(dataLocal.getId()));
//                    planLocal = planLocalProgramaProyectoService.create(planLocal);
//                }
//            }
//
//        }
//
//        listaPlanPoliticaNuevo = reformaService.showPlanaPoliticalNuevo(BigInteger.valueOf(r.getId()));
//
//        if (!listaPlanPoliticaNuevo.isEmpty()) {
//            for (PlanAnualPoliticaPublica dataPolitica : listaPlanPoliticaNuevo) {
//                planPolitica = new PlanAnualPoliticaPublica();
//                if (dataPolitica.getNumeroOrdenId() != null) {
//                    planPolitica = reformaService.getPlanPolitica(dataPolitica.getNumeroOrdenId());
//                    planPolitica.setUsuarioModifica(user.getNameUser());
//                    planPolitica.setFechaModificacion(new Date());
//                    planPolitica.setEstadoPapp(estado);
//                    planAnualPoliticaPublicaService.edit(planPolitica);
//                } else {
//
//                    planPolitica = Utils.clone(dataPolitica);
//                    planPolitica.setId(null);
//
//                    if (planPolitica.getPlanLocal().getNumeroOrdenId() != null) {
//                        PlanLocalProgramaProyecto pl = reformaService.getPlanlocal(planPolitica.getPlanLocal().getNumeroOrdenId());
//                        planPolitica.setPlanLocal(pl);
//
//                    } else {
//                        PlanLocalProgramaProyecto p = reformaService.getPlanLocalCreado(BigInteger.valueOf(planPolitica.getPlanLocal().getId()));
//                        planPolitica.setPlanLocal(p);
//                    }
//                    planPolitica.setUsuarioModifica(user.getNameUser());
//                    planPolitica.setFechaModificacion(new Date());
//                    planPolitica.setEstadoPapp(estado);
//                    planPolitica.setCodigoReforma(null);
//                    planPolitica.setCodigoReformaTraspaso(null);
//                    planPolitica.setRegistroNuevoReferencia(BigInteger.valueOf(dataPolitica.getId()));
//                    planPolitica = planAnualPoliticaPublicaService.create(planPolitica);
//
//                }
//
//            }
//
//        }
//
//        listaPlanAnualNuevo = reformaService.showPlanAnualNuevo(BigInteger.valueOf(r.getId()));
//        if (!listaPlanAnualNuevo.isEmpty()) {
//
//            for (PlanAnualProgramaProyecto dataPlanAnual : listaPlanAnualNuevo) {
//                planAnual = new PlanAnualProgramaProyecto();
//                if (dataPlanAnual.getNumeroOrdenId() != null) {
//                    planAnual = reformaService.getPlanAnual(dataPlanAnual.getNumeroOrdenId());
//                    planAnual.setUsuarioModifica(user.getNameUser());
//                    planAnual.setFechaModificacion(new Date());
//                    planAnual.setEstadoPapp(estado);
//                    planAnualProgramaProyectoService.edit(planAnual);
//                } else {
//
//                    planAnual = Utils.clone(dataPlanAnual);
//                    planAnual.setId(null);
//                    if (dataPlanAnual.getPlanAnual() != null) {
//                        if (dataPlanAnual.getPlanAnual().getNumeroOrdenId() != null) {
//                            PlanAnualPoliticaPublica pl = reformaService.getPlanPolitica(planAnual.getPlanAnual().getNumeroOrdenId());
//                            planAnual.setPlanAnual(pl);
//
//                        } else {
//                            PlanAnualPoliticaPublica p = reformaService.getPlanPoliticaCreado(BigInteger.valueOf(dataPlanAnual.getPlanAnual().getId()));
//                            planAnual.setPlanAnual(p);
//
//                        }
//                    }
//                    planAnual.setUsuarioModifica(user.getNameUser());
//                    planAnual.setFechaModificacion(new Date());
//                    planAnual.setEstadoPapp(estado);
//                    planAnual.setCodigoReforma(null);
//                    planAnual.setCodigoReformaTraspaso(null);
//                    planAnual.setRegistroNuevoReferencia(BigInteger.valueOf(dataPlanAnual.getId()));
//                    planAnual = planAnualProgramaProyectoService.create(planAnual);
//                }
//            }
//        }
        listaActividadNuevo = reformaService.showActividadOperativaNuevo(BigInteger.valueOf(r.getId()));
        if (!listaActividadNuevo.isEmpty()) {

            for (ActividadOperativa dataActividad : listaActividadNuevo) {
                actividad = new ActividadOperativa();
                if (dataActividad.getNumeroOrdenId() != null) {
                    actividad = reformaService.getActividadOperativa(dataActividad.getNumeroOrdenId());
                    actividad.setUsuarioModifica(user.getNameUser());
                    actividad.setFechaModificacion(new Date());
                    actividad.setEstadoPapp(estado);
                    actividad.setMonto(dataActividad.getMonto());
                    actividad.setMonotReformado(dataActividad.getMonotReformado());
                    actividad.setEneroReforma(dataActividad.getEneroReforma());
                    actividad.setFebreroReforma(dataActividad.getFebreroReforma());
                    actividad.setMarzoReforma(dataActividad.getMarzoReforma());
                    actividad.setAbrilReforma(dataActividad.getAbrilReforma());
                    actividad.setMayoReforma(dataActividad.getMayoReforma());
                    actividad.setJunioreforma(dataActividad.getJunioreforma());
                    actividad.setJulioReforma(dataActividad.getJulioReforma());
                    actividad.setAgostoReforma(dataActividad.getAgostoReforma());
                    actividad.setSeptiembreReforma(dataActividad.getSeptiembreReforma());
                    actividad.setOctubreReforma(dataActividad.getOctubreReforma());
                    actividad.setNoviembreReforma(dataActividad.getNoviembreReforma());
                    actividad.setDiciembreReforma(dataActividad.getDiciembreReforma());
                    actividadService.edit(actividad);
                } else {

                    actividad = Utils.clone(dataActividad);
                    actividad.setId(null);

                    if (actividad.getPlanProgramaProyecto().getNumeroOrdenId() != null) {
                        PlanAnualProgramaProyecto pl = reformaService.getPlanAnual(actividad.getPlanProgramaProyecto().getNumeroOrdenId());
                        actividad.setPlanProgramaProyecto(pl);
                    } else {

                        PlanAnualProgramaProyecto p = reformaService.getPlanAnualCreado(BigInteger.valueOf(dataActividad.getPlanProgramaProyecto().getId()));
                        actividad.setPlanProgramaProyecto(p);
                    }

                    actividad.setCodigoReforma(null);
                    actividad.setCodigoReformaTraspaso(null);
                    actividad.setUsuarioModifica(user.getNameUser());
                    actividad.setFechaModificacion(new Date());
                    actividad.setEstadoPapp(estado);
                    actividad.setRegistroNuevoReferencia(BigInteger.valueOf(dataActividad.getId()));
                    actividad = actividadService.create(actividad);

                }
            }

        }

        listaProductoNuevo = reformaService.showProductoNuevo(BigInteger.valueOf(r.getId()));

        if (listaProductoNuevo != null && !listaProductoNuevo.isEmpty()) {

            for (Producto dataProducto : listaProductoNuevo) {
                if (dataProducto.getReduccionEgreso().setScale(2).compareTo(BigDecimal.ZERO.setScale(2)) == 1) {
                    producto = new Producto();
                    if (dataProducto.getNumeroOrdenId() != null) {
                        producto = reformaService.getProductoNuevo(dataProducto.getNumeroOrdenId());
                        producto.setUsuarioModifica(user.getNameUser());
                        producto.setFechaModificacion(new Date());
                        producto.setEstadoPapp(estado);

                        if (r.getTipo()) {
                            if (producto.getSuplementoEgreso().add(dataProducto.getSuplementoEgreso()) == null) {
                                producto.setSuplementoEgreso(BigDecimal.ZERO);
                            } else {
                                producto.setSuplementoEgreso(producto.getSuplementoEgreso().add(dataProducto.getSuplementoEgreso()));
                            }
                        } else {
                            if (producto.getReduccionEgreso().add(dataProducto.getReduccionEgreso()) == null) {
                                producto.setReduccionEgreso(BigDecimal.ZERO);
                            } else {
                                producto.setReduccionEgreso(producto.getReduccionEgreso().add(dataProducto.getReduccionEgreso()));

                            }
                        }

                        producto.setMontoReformada(producto.getMonto().add(producto.getSuplementoEgreso()).add(producto.getTraspasoIncremento()).subtract(producto.getReduccionEgreso()).subtract(producto.getTraspasoReduccion()));
                        producto.setSaldoDisponible(producto.getMontoReformada().subtract(producto.getReserva()));
                        productoService.edit(producto);
                    } else {
                        producto = Utils.clone(dataProducto);

                        producto.setId(null);

                        if (producto.getActividadOperativa().getNumeroOrdenId() != null) {
                            ActividadOperativa act = reformaService.getActividadOperativa(producto.getActividadOperativa().getNumeroOrdenId());
                            producto.setActividadOperativa(act);
                        } else {
                            ActividadOperativa a = reformaService.getActividadCreado(BigInteger.valueOf(producto.getActividadOperativa().getId()));
                            producto.setActividadOperativa(a);

                        }

                        producto.setUsuarioModifica(user.getNameUser());
                        producto.setFechaModificacion(new Date());
                        producto.setUsuarioCreacion(user.getNameUser());
                        producto.setFechaCreacion(new Date());
                        producto.setEstadoPapp(estado);
                        producto.setCodigoReforma(null);
                        producto.setCodigoReformaTraspaso(null);
                        producto.setMonto(BigDecimal.ZERO);
                        producto.setReserva(BigDecimal.ZERO);
                        producto.setRegistroNuevoReferencia(BigInteger.valueOf(dataProducto.getId()));
                        producto.setSaldoDisponible(producto.getMontoReformada().subtract(producto.getReserva()));
                        producto = productoService.create(producto);
                    }
                }
            }
        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO">
        listaPartidasDistributivoNuevo = reformaService.showPartidasDistributivoNnuevo(BigInteger.valueOf(r.getId()));
        if (listaPartidasDistributivoNuevo != null && !listaPartidasDistributivoNuevo.isEmpty()) {
            for (ThCargoRubros dataDistributivo : listaPartidasDistributivoNuevo) {
                if (dataDistributivo.getReformaReduccion().setScale(2).compareTo(BigDecimal.ZERO.setScale(2)) == 1) {
                    partidaDistributivo = new ThCargoRubros();
                    if (dataDistributivo.getCodigoReferencia() != null) {
                        partidaDistributivo = reformaService.getPartidasDistributivoNuevo(dataDistributivo.getCodigoReferencia());
                        partidaDistributivo.setReformaSuplemento(dataDistributivo.getReformaSuplemento().add(partidaDistributivo.getReformaSuplemento()));
                        partidaDistributivo.setReformaReduccion(dataDistributivo.getReformaReduccion().add(partidaDistributivo.getReformaReduccion()));
                        partidaDistributivo.setEstadoPartida(estadoPartida);
                        partidaDistributivo.setUsuarioModificacion(user.getNameUser());
                        partidaDistributivo.setFechaModificacion(new Date());
                        partidaDistributivo.setIdPresupuesto(dataDistributivo.getIdPresupuesto());
                        partidaDistributivo.setIdEstructura(dataDistributivo.getIdEstructura());
                        partidaDistributivo.setIdFuente(dataDistributivo.getIdFuente());
                        partidaDistributivo.setPartidaPresupuestaria(dataDistributivo.getPartidaPresupuestaria());
                        BigDecimal result = partidaDistributivo.getMonto().add(partidaDistributivo.getReformaSuplemento()).add(partidaDistributivo.getTraspasoIncremento());

                        if (result == null) {
                            result = BigDecimal.ZERO;
                        }

                        partidaDistributivo.setReformaCodificado(result.subtract(partidaDistributivo.getReformaReduccion()).subtract(partidaDistributivo.getTraspasoReduccion()));
                        thCargoRubrosService.edit(partidaDistributivo);
                    } else {
                        partidaDistributivo = Utils.clone(dataDistributivo);
                        partidaDistributivo.setId(null);
                        partidaDistributivo.setCodigoReforma(null);
                        partidaDistributivo.setCodigoReformaTraspaso(null);
                        partidaDistributivo.setEstadoPartida(estadoPartida);
                        partidaDistributivo.setUsuarioModificacion(user.getNameUser());
                        partidaDistributivo.setUsuarioCreacion(user.getNameUser());
                        partidaDistributivo.setFechaModificacion(new Date());
                        partidaDistributivo.setFechaCreacion(new Date());
                        partidaDistributivo.setMonto(BigDecimal.ZERO);
                        partidaDistributivo = thCargoRubrosService.create(partidaDistributivo);

                    }

                }
            }
        }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO ANEXO">
//        listaPartidasDistributivoAnexoNuevo = reformaService.showPartidasDistributivoAnexoNnuevo(BigInteger.valueOf(r.getId()));
//
//        if (!listaPartidasDistributivoAnexoNuevo.isEmpty()) {
//            for (PartidasDistributivoAnexo dataAnexo : listaPartidasDistributivoAnexoNuevo) {
//                if (dataAnexo.getReformaSuplemento().setScale(2).compareTo(BigDecimal.ZERO.setScale(2)) == 1) {
//                    partidasDistributivoAnexo = new PartidasDistributivoAnexo();
//                    if (dataAnexo.getCodigoReferencia() != null) {
//                        partidasDistributivoAnexo = reformaService.getPartidasDistributivoAnexoNuevo(dataAnexo.getCodigoReferencia());
//                        partidasDistributivoAnexo.setReformaSuplemento(partidasDistributivoAnexo.getReformaSuplemento().add(dataAnexo.getReformaSuplemento()));
//                        partidasDistributivoAnexo.setReformaReduccion(partidasDistributivoAnexo.getReformaReduccion().add(dataAnexo.getReformaReduccion()));
//                        partidasDistributivoAnexo.setReformaCodificado(partidasDistributivoAnexo.getMonto().add(partidasDistributivoAnexo.getReformaSuplemento()).add(partidasDistributivoAnexo.getTraspasoIncremento()).subtract(partidasDistributivoAnexo.getReformaReduccion()).subtract(partidasDistributivoAnexo.getTraspasoReduccion()));
//                        partidasDistributivoAnexo.setFechaModificacion(new Date());
//                        partidasDistributivoAnexo.setUsuarioModificacion(user.getNameUser());
//                        partidasDistributivoAnexo.setEstadoPartida(estadoPartida);
//                        partidaAnexoService.edit(partidasDistributivoAnexo);
//                    } else {
//
//                        partidasDistributivoAnexo = Utils.clone(dataAnexo);
//                        partidasDistributivoAnexo.setId(null);
//                        partidasDistributivoAnexo.setUsuarioModificacion(user.getNameUser());
//                        partidasDistributivoAnexo.setFechaModificacion(new Date());
//                        partidasDistributivoAnexo.setEstadoPartida(estadoPartida);
//                        partidasDistributivoAnexo.setCodigoReforma(null);
//                        partidasDistributivoAnexo.setCodigoReformaTraspaso(null);
//                        partidasDistributivoAnexo.setMonto(BigDecimal.ZERO);
//                        partidasDistributivoAnexo = partidaAnexoService.create(partidasDistributivoAnexo);
//
//                    }
//                }
//            }
//        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="PARTIDAS DIRECTAS">
        listaPartidasDirectas = reformaService.showPartidasDirectasNnuevo(BigInteger.valueOf(r.getId()));
        if (listaPartidasDirectas != null && !listaPartidasDirectas.isEmpty()) {

            for (ProformaPresupuestoPlanificado item : listaPartidasDirectas) {
                if (item.getReformaReduccion().setScale(2).compareTo(BigDecimal.ZERO.setScale(2)) == 1) {
                    partidasDirectas = new ProformaPresupuestoPlanificado();
                    if (item.getCodigoReferencia() != null) {

                        partidasDirectas = reformaService.getPartidasDirectasNuevo(item.getCodigoReferencia());
                        partidasDirectas.setReformaSuplemento(partidasDirectas.getReformaSuplemento().add(item.getReformaSuplemento()));
                        partidasDirectas.setReformaReduccion(partidasDirectas.getReformaReduccion().add(item.getReformaReduccion()));
                        partidasDirectas.setReformaCodificado(partidasDirectas.getValor().add(partidasDirectas.getReformaSuplemento()).add(partidasDirectas.getTraspasoIncremento()).subtract(partidasDirectas.getReformaReduccion()).subtract(partidasDirectas.getTraspasoReduccion()));
                        partidasDirectas.setUsuarioModificacion(user.getNameUser());
                        partidasDirectas.setFechaModificacion(new Date());
                        partidasDirectas.setEstadoPartida(estadoPartida);
                        proformaService.edit(partidasDirectas);
                    } else {

                        partidasDirectas = Utils.clone(item);
                        partidasDirectas.setId(null);
                        partidasDirectas.setUsuarioModificacion(user.getNameUser());
                        partidasDirectas.setFechaModificacion(new Date());
                        partidasDirectas.setEstadoPartida(estadoPartida);
                        partidasDirectas.setCodigoReforma(null);
                        partidasDirectas.setCodigoReformaTraspaso(null);
                        partidasDirectas.setValor(BigDecimal.ZERO);
                        partidasDirectas = proformaService.create(partidasDirectas);
                    }
                }
            }
        }

//</editor-fold>
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="ACTUALIZANDO PROFORMA Y EL PRESUPUESTO">
        agruparProformaReforma(r);
        actualizarPresupuesto(r);
//</editor-fold>

        r.setEjecutado(true);
        reformaService.edit(r);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Informaci??n", "Se ha ejecutado la reforma con ??xito");

    }

    public void agruparProformaReforma(ReformaIngresoSuplemento r) {
        BigInteger integer = BigInteger.valueOf(r.getId());
        profromaReformaList = new ArrayList<>();
        this.showCodigoRepetidosList = new ArrayList<>();
        List<ProformaPDTO> listaAgrupacionPapp = reformaService.gePappGroupReforma(r.getPeriodo(), r);
        List<ProformaPDTO> listaagrupacionDistr = reformaService.showCodigosAgrupadosReformas(r.getPeriodo(), r);
        List<PartidasDistributivoAnexo> listaagrupacionDistrAnexo = partidaAnexoService.showAllPartidasAnexoReforma(r.getPeriodo(), r);
        List<ProformaPresupuestoPlanificado> listaPartidasdirectas = reformaService.showPartidaDirectasReforma(r.getPeriodo(), r);
        //<editor-fold defaultstate="collapsed" desc="AGRUPAR PAPP">
        if (listaAgrupacionPapp != null && !listaAgrupacionPapp.isEmpty()) {
            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO ob : listaAgrupacionPapp) {

                proformaPresupuesto.setValor(ob.getTotal());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setPartidaPresupuestaria(ob.getPartida());
                proformaPresupuesto.setItemNew(ob.getItemNew());
                proformaPresupuesto.setEstructruaNew(ob.getEstructuraNew());
                proformaPresupuesto.setFuenteNew(ob.getFuenteNew());
                // proformaPresupuesto.setFuenteDirecta(ob.getFuenteDirecta());
                proformaPresupuesto.setCodigo("PAPP");
                proformaPresupuesto.setReformaSuplemento(ob.getReformaSuplemento());
                proformaPresupuesto.setReformaReduccion(ob.getReformaReduccion());
                proformaPresupuesto.setTraspasoIncremento(ob.getIncrementoTraspaso());
                proformaPresupuesto.setTraspasoReduccion(ob.getReduccionTraspaso());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
                profromaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
            }

        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="AGRUPAR DISTRIBUTIVO">
        if (listaagrupacionDistr != null && !listaagrupacionDistr.isEmpty()) {

            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
            for (ProformaPDTO i : listaagrupacionDistr) {

                for (ProformaPresupuestoPlanificado data : profromaReformaList) {
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
                //    proformaPresupuesto.setFuenteDirecta(i.getFuenteDirecta());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(r.getPeriodo());
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setCodigo(i.getCodigo());
                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
                profromaReformaList.add(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();

            }
        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="AGREGAR DISTRIBUTIVO ANEXO">
//        if (!listaagrupacionDistrAnexo.isEmpty()) {
//            ProformaPresupuestoPlanificado proformaPresupuesto = new ProformaPresupuestoPlanificado();
//            for (PartidasDistributivoAnexo p : listaagrupacionDistrAnexo) {
//                for (ProformaPresupuestoPlanificado data : profromaReformaList) {
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
//                proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
//                proformaPresupuesto.setFechaCreacion(new Date());
//                proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
//                proformaPresupuesto.setTraspasoIncremento(p.getTraspasoIncremento());
//                proformaPresupuesto.setTraspasoReduccion(p.getTraspasoReduccion());
//                proformaPresupuesto.setReformaSuplemento(p.getReformaSuplemento());
//                proformaPresupuesto.setReformaReduccion(p.getReformaReduccion());
//                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor().add(proformaPresupuesto.getReformaSuplemento()).subtract(proformaPresupuesto.getReformaReduccion()));
//                proformaPresupuesto.setFechaModificacion(new Date());
//                proformaPresupuesto.setCodigo("PDA");
//                proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
//                profromaReformaList.add(proformaPresupuesto);
//                proformaPresupuesto = new ProformaPresupuestoPlanificado();
//
//            }
//
//        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="AGREGAR PARTIDAS DIRECTAS">
//        if (!listaPartidasdirectas.isEmpty()) {
//
//            for (ProformaPresupuestoPlanificado k : listaPartidasdirectas) {
//
//                for (ProformaPresupuestoPlanificado data : profromaReformaList) {
//                    if (k.getPartidaPresupuestaria() == null ? data.getPartidaPresupuestaria() == null : k.getPartidaPresupuestaria().equals(data.getPartidaPresupuestaria())) {
//                        showCodigoRepetidosList.add(data);
//                    }
//                }
//
//                profromaReformaList.add(k);
//
//            }
//
//        }
//</editor-fold>
        //VERIFICACION DE CODIGOS REPETIDOS
        if (showCodigoRepetidosList != null && !showCodigoRepetidosList.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "HAY PARTIDAS QUE SE REPITEN POR FAVOR CORRIGA");
            return;
        }

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZANDO PROFORMA">
        List<ProformaPresupuestoPlanificado> proformaVieja = reformaService.proformaVieja(r.getPeriodo());

        int count = 0;
        if (proformaVieja != null && profromaReformaList != null && !proformaVieja.isEmpty() && !profromaReformaList.isEmpty()) {
            for (ProformaPresupuestoPlanificado x : proformaVieja) {
                for (ProformaPresupuestoPlanificado y : profromaReformaList) {
                    ProformaPresupuestoPlanificado editProforma = new ProformaPresupuestoPlanificado();
                    if (x.getPartidaPresupuestaria().equals(y.getPartidaPresupuestaria())) {

                        editProforma = reformaService.editProformavieja(x);
                        editProforma.setReformaSuplemento(y.getReformaSuplemento().add(x.getReformaSuplemento()));
                        editProforma.setReformaReduccion(y.getReformaReduccion().add(x.getReformaReduccion()));
                        editProforma.setTraspasoIncremento(y.getTraspasoIncremento().add(x.getTraspasoIncremento()));
                        editProforma.setTraspasoReduccion(y.getTraspasoReduccion().add(x.getTraspasoReduccion()));
                        editProforma.setReformaCodificado(editProforma.getValor().add(editProforma.getReformaSuplemento()).add(editProforma.getTraspasoIncremento()).subtract(editProforma.getReformaReduccion()).subtract(editProforma.getTraspasoReduccion()));
                        editProforma.setFechaModificacion(new Date());
                        editProforma.setUsuarioModificacion(user.getNameUser());
                        proformaService.edit(editProforma);
                        count = count + 1;

                    }

                }

            }

        }

        if (proformaVieja != null && profromaReformaList != null && !proformaVieja.isEmpty() && !profromaReformaList.isEmpty()) {
            count = 0;
            boolean verificador = true;
            ProformaPresupuestoPlanificado editPro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado x : profromaReformaList) {
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
                    editPro.setUsuarioModificacion(user.getNameUser());
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
        JsfUtil.addInformationMessage("Informaci??n", "Acualizado la proforma");

    }

    public void actualizarPresupuesto(ReformaIngresoSuplemento r) {
        short periodo = r.getPeriodo();
        List<Presupuesto> presupuestoViejo = presupuestoService.findByNamedQuery("Presupuesto.findByPeriodoVerificador", periodo);
        //List<Presupuesto> presupuestoNuevoCalculado = new ArrayList<>();
        presupuestoReformado(periodo);

        Presupuesto pr = new Presupuesto();

        if (presupuestoViejo != null && listaPresupuestoNuevo != null && !presupuestoViejo.isEmpty() && !listaPresupuestoNuevo.isEmpty()) {
            for (Presupuesto x : presupuestoViejo) {

                for (Presupuesto y : listaPresupuestoNuevo) {
                    pr = new Presupuesto();
                    if (x.getPartida().equals(y.getPartida())) {
                        pr = reformaService.getReformaPresupuestoObject(x);
                        pr.setReformaSuplemetario(y.getReformaSuplemetario());
                        pr.setReformaReducido(y.getReformaReducido());
                        pr.setCodificado(pr.getPresupuestoInicial().add(pr.getReformaSuplemetario()).subtract(pr.getReformaReducido()));
                        pr.setFechaModificacion(new Date());
                        pr.setUsuarioModificacion(user.getNameUser());
                        presupuestoService.edit(pr);
                        pr = new Presupuesto();
                    }

                }
            }
        }

        if (presupuestoViejo != null && listaPresupuestoNuevo != null && !presupuestoViejo.isEmpty() && !listaPresupuestoNuevo.isEmpty()) {
            boolean verificador = true;
            for (Presupuesto v : listaPresupuestoNuevo) {
                pr = new Presupuesto();
                verificador = true;
                for (Presupuesto w : presupuestoViejo) {
                    if (v.getPartida().equals(w.getPartida())) {
                        verificador = false;
                    }
                }
                if (verificador) {
                    pr = Utils.clone(v);
                    pr.setId(null);
                    pr.setPresupuestoInicial(BigDecimal.ZERO);
                    pr = presupuestoService.create(pr);
                }
            }
        }

    }

    public void presupuestoReformado(short periodo) {
        lista2 = new ArrayList<>();
        lista = new ArrayList<>();
        // enviarMetodosIngresos(periodo, listaPresupuesto);
        // enviarMetodosEgresos(periodo, listaPresupuesto);
        lista2 = presupuestoService.presupuestoIngresosReformada(periodo);

        listaPresupuestoNuevo = new ArrayList<>();
        if (lista2 != null && !lista2.isEmpty()) {
            for (presupuestoModel item : lista2) {
                this.presupuesto = new Presupuesto();
                presupuesto.setPartida(item.getPartida());
                presupuesto.setItemNew(item.getItemPresupuestario());
                presupuesto.setEstructruaNew(item.getEstructuraProgramatica());
                presupuesto.setFuenteNew(item.getFuente());
              //  presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setUsuarioCreacion(user.getNameUser());
                presupuesto.setFechaCreacion(new Date());
                presupuesto.setUsuarioModificacion(user.getNameUser());
                presupuesto.setFechaModificacion(new Date());
                presupuesto.setPeriodo(periodo);
                presupuesto.setTipo(item.getTipo());
                presupuesto.setReformaSuplemetario(item.getReformaSuplemento());
                presupuesto.setReformaReducido(item.getReformaReduccion());
                presupuesto.setPresupuestoInicial(item.getPresupuestoInicial());
                presupuesto.setCodificado(presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario()).add(item.getTraspasoIncremento()).subtract(presupuesto.getReformaReducido()).subtract(item.getTraspasoReduccion()));
                listaPresupuestoNuevo.add(presupuesto);
                presupuesto = new Presupuesto();

            }
        }

        this.lista = presupuestoService.presupuestoEgresosReformado(periodo);

        if (lista != null) {
            for (presupuestoModel item : lista) {
                this.presupuesto = new Presupuesto();
                presupuesto.setPartida(item.getPartida());
                presupuesto.setItemNew(item.getItemPresupuestario());
                presupuesto.setEstructruaNew(item.getEstructuraProgramatica());
                presupuesto.setFuenteNew(item.getFuente());
                //   presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setUsuarioCreacion(user.getNameUser());
                presupuesto.setFechaCreacion(new Date());
                presupuesto.setUsuarioModificacion(user.getNameUser());
                presupuesto.setFechaModificacion(new Date());
                presupuesto.setPeriodo(periodo);
                presupuesto.setTipo(item.getTipo());
                //presupuesto.setValorIngreso(item.getTotalingreso());
                //presupuesto.setValorEgreso(item.getTotalegresos());
                presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setReformaSuplemetario(item.getReformaSuplemento().add(item.getTraspasoIncremento()));
                presupuesto.setReformaReducido(item.getReformaReduccion().add(item.getTraspasoReduccion()));
                presupuesto.setPresupuestoInicial(item.getPresupuestoInicial());
                presupuesto.setCodificado(presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario()).subtract(presupuesto.getReformaReducido()));
                listaPresupuestoNuevo.add(presupuesto);
                this.presupuesto = new Presupuesto();

            }
        }
    }

    public void enviarMetodosIngresos(Short opcionBusqueda, List<Presupuesto> listaPresupuesto) {

        lista2 = presupuestoService.presupuestoIngresosReformada(opcionBusqueda);

        for (presupuestoModel item : lista2) {
            this.presupuesto = new Presupuesto();
            presupuesto.setPartida(item.getPartida());
            presupuesto.setItemNew(item.getItemPresupuestario());
            presupuesto.setEstructruaNew(item.getEstructuraProgramatica());
            presupuesto.setFuenteNew(item.getFuente());
            //  presupuesto.setFuenteDirecta(item.getFuenteDirecta());
            presupuesto.setUsuarioCreacion(user.getNameUser());
            presupuesto.setFechaCreacion(new Date());
            presupuesto.setUsuarioModificacion(user.getNameUser());
            presupuesto.setFechaModificacion(new Date());
            presupuesto.setPeriodo(opcionBusqueda);
            presupuesto.setTipo(item.getTipo());
            presupuesto.setValorIngreso(item.getTotalingreso());
            presupuesto.setValorEgreso(item.getTotalegresos());
            presupuesto.setReformaSuplemetario(BigDecimal.ZERO);
            presupuesto.setReformaReducido(BigDecimal.ZERO);
            presupuesto.setPresupuestoInicial(item.getTotalingreso());
            presupuesto.setCodificado(presupuesto.getPresupuestoInicial());
            listaPresupuesto.add(presupuesto);
            presupuesto = new Presupuesto();

        }
        //enviarProgramado(periodo);

    }

    public void enviarProgramado(short periodo) {
        List<ProformaIngreso> resultPresupuesto = mantenimientoProformaService.getCatalogoPresupuesto(periodo, (short) 4, true);
        if (!resultPresupuesto.isEmpty()) {
            for (ProformaIngreso item : resultPresupuesto) {
                pim.setItemNew(item.getItem());
                pim.setEnero(BigDecimal.ZERO);
                pim.setFebrero(BigDecimal.ZERO);
                pim.setMarzo(BigDecimal.ZERO);
                pim.setAbril(BigDecimal.ZERO);
                pim.setMayo(BigDecimal.ZERO);
                pim.setJunio(BigDecimal.ZERO);
                pim.setJulio(BigDecimal.ZERO);
                pim.setAgosto(BigDecimal.ZERO);
                pim.setSeptiembre(BigDecimal.ZERO);
                pim.setOctubre(BigDecimal.ZERO);
                pim.setNoviembre(BigDecimal.ZERO);
                pim.setDiciembre(BigDecimal.ZERO);
                pim.setPeriodo(periodo);
                pim.setTotalAnio(BigDecimal.ZERO);
                pim.setTipoFlujo(Boolean.TRUE);
                pim.setDistribucion(Boolean.FALSE);
                pim.setFechaCreacion(new Date());
                pim.setUsuarioCreacion(user.getNameUser());
                pim = pimService.create(pim);

                pim = new ProgramacionIngresoEgreso();
            }
        }
    }

    public void enviarMetodosEgresos(Short opcionBusqueda, List<Presupuesto> listaPresupuesto) {
//       this.lista=new ArrayList<>();
        this.lista = presupuestoService.presupuestoEgresosReformado(opcionBusqueda);
        if (lista != null) {
            for (presupuestoModel item : lista) {
                this.presupuesto = new Presupuesto();
                presupuesto.setPartida(item.getPartida());
                presupuesto.setItemNew(item.getItemPresupuestario());
                presupuesto.setEstructruaNew(item.getEstructuraProgramatica());
                presupuesto.setFuenteNew(item.getFuente());
                // presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setUsuarioCreacion(user.getNameUser());
                presupuesto.setFechaCreacion(new Date());
                presupuesto.setUsuarioModificacion(user.getNameUser());
                presupuesto.setFechaModificacion(new Date());
                presupuesto.setPeriodo(opcionBusqueda);
                presupuesto.setTipo(item.getTipo());
                presupuesto.setValorIngreso(item.getTotalingreso());
                presupuesto.setValorEgreso(item.getTotalegresos());
                presupuesto.setFuenteDirecta(item.getFuenteDirecta());
                presupuesto.setReformaSuplemetario(item.getReformaSuplemento());
                presupuesto.setReformaReducido(item.getReformaReduccion());
                presupuesto.setPresupuestoInicial(item.getTotalegresos());
                presupuesto.setCodificado(item.getReformaCodificado());
                listaPresupuesto.add(presupuesto);
                this.presupuesto = new Presupuesto();

            }
        }
        // enviarProgramacionIngresoEgreso(opcionBusqueda);

    }

    public void enviarProgramacionIngresoEgreso(Short opcionBusqueda) {
        List<ProformaPresupuestoPlanificado> resultProforma = presupuestoService.getProformaEgreso(opcionBusqueda);
        if (!resultProforma.isEmpty()) {
            for (ProformaPresupuestoPlanificado egreso : resultProforma) {
                pim.setItemPresupuestario(egreso.getItemPresupuestario());
                pim.setPeriodo(opcionBusqueda);
                pim.setMontoActividad(egreso.getValor());
                pim.setTipoFlujo(Boolean.FALSE);
                pim.setCodigoItem(egreso.getPartidaPresupuestaria());
                pim.setEnero(BigDecimal.ZERO);
                pim.setFebrero(BigDecimal.ZERO);
                pim.setMarzo(BigDecimal.ZERO);
                pim.setAbril(BigDecimal.ZERO);
                pim.setMayo(BigDecimal.ZERO);
                pim.setJunio(BigDecimal.ZERO);
                pim.setJulio(BigDecimal.ZERO);
                pim.setAgosto(BigDecimal.ZERO);
                pim.setSeptiembre(BigDecimal.ZERO);
                pim.setOctubre(BigDecimal.ZERO);
                pim.setNoviembre(BigDecimal.ZERO);
                pim.setDiciembre(BigDecimal.ZERO);
                pim.setDistribucion(Boolean.FALSE);
                pim.setMontoCuatrimestre1(BigDecimal.ZERO);
                pim.setMontoCuatrimestre2(BigDecimal.ZERO);
                pim.setMontoCuatrimestre3(BigDecimal.ZERO);
                pim.setTotalAnio(BigDecimal.ZERO);
                pim.setTipoCodigo(egreso.getCodigo());
                pim.setActividad(Boolean.FALSE);
                pim.setFechaCreacion(new Date());
                pim.setUsuarioCreacion(user.getNameUser());
                pim = pimService.create(pim);
                this.pim = new ProgramacionIngresoEgreso();
            }
        }
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

    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")
            ejecutarReforma(reforma);
            getParamts().put("usuario", user.getNameUser());
            if (verificacionGlobal) {
                getParamts().put("ejecucion", 0);
            } else {
                getParamts().put("ejecucion", 1);
            }

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reforma = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public boolean confirmarEjecucion(ReformaIngresoSuplemento r) {
        boolean verificacion = false;
        BigDecimal montoEncerado = BigDecimal.ZERO;

        CatalogoItem estado = reformaService.getlistaEstado("RECHA", "estado_solicitud");
        CatalogoItem estadoPartidas = reformaService.getlistaEstado("RERD", "estado_distributivo");
        CatalogoItem estado_producto = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RRE");

        //<editor-fold defaultstate="collapsed" desc="INGRESOS">
        List<ProformaIngreso> itemOriginal = catalogoPresupuestoService.getPresupuestoIngreso(Boolean.TRUE, r.getPeriodo());
        List<DetalleReformaIngresoSuplemento> itemReformado = reformaService.getDetalleReormaIngreso(r);

        if (!itemOriginal.isEmpty() && !itemReformado.isEmpty()) {

            for (DetalleReformaIngresoSuplemento item : itemReformado) {
                for (ProformaIngreso data : itemOriginal) {

                    if (data.getId() == item.getCodigoReferencia().longValue()) {
                        if (item.getReducido().setScale(2).compareTo(data.getPresupuestoCodificado().setScale(2)) == 1) {
                            verificacion = true;
                            item.setReducido(montoEncerado);
                            item.setSuplemento(montoEncerado);
                            item.setCodificado(data.getPresupuestoCodificado());
                            detalleIngresoService.edit(item);
                        }
                    }
                }
            }

        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PRODUCTO">
        List<Producto> productoOriginal = productoService.getListaProductoByPeriodo(r.getPeriodo());
        List<Producto> productoReformado = reformaService.showProductoNuevo(BigInteger.valueOf(r.getId()));
        if (!productoOriginal.isEmpty() && !productoReformado.isEmpty()) {

            for (Producto data : productoReformado) {
                for (Producto item : productoOriginal) {

                    if (item.getId() == data.getNumeroOrdenId().longValue()) {
                        if (data.getReduccionEgreso().setScale(2).compareTo(item.getMontoReformada().subtract(productoService.productoReservado(item, false)).setScale(2)) == 1) {
                            verificacion = true;
                            data.setMonto(item.getMontoReformada());
                            data.setReserva(item.getReserva());
                            data.setComprometido(item.getComprometido());
                            data.setSaldoDisponible(item.getMontoReformada().subtract(item.getReserva()));
                            data.setSuplementoEgreso(montoEncerado);
                            data.setReduccionEgreso(montoEncerado);
                            data.setEstadoPapp(estado_producto);
                            data.setMontoReformada(item.getMontoReformada());
                            productoService.edit(data);

                        }
                    }
                }
            }

        }

//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="ACTIVIDAD OPERATIVA">
        if (verificacion) {
            List<ActividadOperativa> actividadOriginal = actividadService.getActividadOriginal(r.getPeriodo());
            List<ActividadOperativa> actividadReformando = reformaService.showActividadOperativaNuevo(BigInteger.valueOf(r.getId()));
            if (!actividadOriginal.isEmpty() && !actividadReformando.isEmpty()) {

                for (ActividadOperativa data : actividadReformando) {
                    for (ActividadOperativa item : actividadOriginal) {

                        if (item.getId() == data.getNumeroOrdenId().longValue()) {
                            data.setMonto(montoEncerado);
                            data.setMonotReformado(item.getMonotReformado());
                            data.setEneroReforma(item.getEneroReforma());
                            data.setFebreroReforma(item.getFebreroReforma());
                            data.setMarzoReforma(item.getMarzoReforma());
                            data.setAbrilReforma(item.getAbrilReforma());
                            data.setMayoReforma(item.getMayoReforma());
                            data.setJunioreforma(item.getJunioreforma());
                            data.setJulioReforma(item.getJulioReforma());
                            data.setAgostoReforma(item.getAgostoReforma());
                            data.setSeptiembreReforma(item.getSeptiembreReforma());
                            data.setOctubreReforma(item.getOctubreReforma());
                            data.setNoviembreReforma(item.getNoviembreReforma());
                            data.setDiciembreReforma(item.getDiciembreReforma());

                            data.setEstadoPapp(estado_producto);
                            actividadService.edit(data);

                        }
                    }
                }
            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO">
        List<ThCargoRubros> distributivoOriginal = partidasDistributivoService.getDistributivoOriginal(r.getPeriodo());
        List<ThCargoRubros> distributivoReformado = reformaService.showPartidasDistributivoNnuevo(BigInteger.valueOf(r.getId()));

        if (!distributivoOriginal.isEmpty() && !distributivoOriginal.isEmpty()) {

            for (ThCargoRubros data : distributivoReformado) {
                for (ThCargoRubros item : distributivoOriginal) {

                    if (item.getId() == data.getCodigoReferencia().longValue()) {
                        if (data.getReformaReduccion().setScale(2, RoundingMode.HALF_UP).compareTo(presupuestoService.getReservaDistributivos(item.getId(), item.getPeriodo()).setScale(2, RoundingMode.HALF_UP)) == 1) {
                            verificacion = true;
                            data.setMonto(item.getReformaCodificado());
                            data.setReserva(item.getReserva());
                            data.setReformaSuplemento(montoEncerado);
                            data.setReformaReduccion(montoEncerado);
                            data.setReformaCodificado(item.getReformaCodificado());
                            data.setEstadoPartida(estadoPartidas);
                            thCargoRubrosService.edit(data);

                        }
                    }
                }
            }

        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO ANEXO">
//        List<PartidasDistributivoAnexo> distributivoAnexoOriginal = partidaAnexoService.getDistributivoAnexoOriginal(r.getPeriodo());
//        List<PartidasDistributivoAnexo> distributivoAnexoReformado = partidaAnexoService.getPartidasAnexoReformaVerificacion(BigInteger.valueOf(r.getId()));
//
//        if (!distributivoAnexoOriginal.isEmpty() && !distributivoAnexoReformado.isEmpty()) {
//
//            for (PartidasDistributivoAnexo data : distributivoAnexoReformado) {
//                for (PartidasDistributivoAnexo item : distributivoAnexoOriginal) {
//
//                    if (item.getId() == data.getCodigoReferencia().longValue()) {
//                        if (data.getReformaReduccion().setScale(2).compareTo(item.getReformaCodificado().subtract(partidaAnexoService.getMontoReservado(item.getPartidaAp(), r.getPeriodo())).setScale(2)) == 1) {
//                            verificacion = true;
//                            data.setMonto(item.getReformaCodificado());
//                            data.setReserva(item.getReserva());
//                            data.setReformaSuplemento(montoEncerado);
//                            data.setReformaReduccion(montoEncerado);
//                            data.setEstadoPartida(estadoPartidas);
//                            data.setReformaCodificado(item.getReformaCodificado());
//                            partidaAnexoService.edit(data);
//
//                        }
//
//                    }
//                }
//            }
//        }

//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DIRECTAS">
        List<ProformaPresupuestoPlanificado> partidaDirectaOriginal = proformaService.getProformaPresupuestoPartidaDirecta(r.getPeriodo(), "PDI");
        List<ProformaPresupuestoPlanificado> partidaDirectaReformado = proformaService.getPdRefroma(BigInteger.valueOf(r.getId()));

        if (!partidaDirectaOriginal.isEmpty() && !partidaDirectaReformado.isEmpty()) {

            for (ProformaPresupuestoPlanificado data : partidaDirectaReformado) {
                for (ProformaPresupuestoPlanificado item : partidaDirectaOriginal) {

                    if (item.getId() == data.getCodigoReferencia().longValue()) {
                        if (data.getReformaReduccion().setScale(2, RoundingMode.HALF_UP).compareTo(presupuestoService.getReservasPartidasDirectas(item.getId(),item.getPeriodo()).setScale(2, RoundingMode.HALF_UP)) == 1) {
                            verificacion = true;
                            data.setValor(item.getReformaCodificado());
                            data.setValor(item.getReserva());
                            data.setReformaSuplemento(montoEncerado);
                            data.setReformaReduccion(montoEncerado);
                            data.setEstadoPartida(estadoPartidas);
                            data.setReformaCodificado(item.getReformaCodificado());
                            proformaService.edit(data);

                        }
                    }

                }

            }

        }

//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="RESERVA Y CUPOS DE REDUCCION - ACTUALIZANDO">
        if (verificacion) {
            r.setFechaModificacion(new Date());
            r.setUsuarioModificacion(user.getNameUser());
            r.setEstado(estado);
            reformaService.edit(r);
            reformaService.actualizarEstadoPapp(estado_producto, BigInteger.valueOf(r.getId()));
            reformaService.actualizarIngresoEgreso(r, estadoPartidas);

            List<UnidadAdministrativa> unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");

            for (UnidadAdministrativa data : unidades) {
                this.cupoReduccion = new CupoReduccion();
                this.cupoReduccion = cupoReduccionService.getDataUnidad(data, r);
                if (cupoReduccion != null) {
                    this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleUnidad(BigInteger.valueOf(r.getId()), data, r.getPeriodo()));
                    this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificado(BigInteger.valueOf(r.getId()), data));
                    cupoReduccionService.edit(cupoReduccion);
                }
            }

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion = cupoReduccionService.getValoresCupo("D", r);
            if (cupoReduccion != null) {
                this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDistributivo(BigInteger.valueOf(r.getId()), r.getPeriodo()));
                this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDistributivo(BigInteger.valueOf(r.getId())));
                cupoReduccionService.edit(cupoReduccion);
            }

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion = cupoReduccionService.getValoresCupo("DA", r);
            if (cupoReduccion != null) {
                this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDistributivoAnexo(BigInteger.valueOf(r.getId()), r.getPeriodo()));
                this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDistributivoAnexo(BigInteger.valueOf(r.getId())));
                cupoReduccionService.edit(cupoReduccion);
            }

            this.cupoReduccion = new CupoReduccion();
            this.cupoReduccion = cupoReduccionService.getValoresCupo("PD", r);
            if (cupoReduccion != null) {
                this.cupoReduccion.setMotoDisponible(cupoReduccionService.getMontoDisponibleDirectas(BigInteger.valueOf(r.getId()), r.getPeriodo()));
                this.cupoReduccion.setMontoCodificado(cupoReduccionService.getmontocodificadoDirectas(BigInteger.valueOf(r.getId())));
                cupoReduccionService.edit(cupoReduccion);
            }
            this.cupoReduccion = new CupoReduccion();

        }
//</editor-fold>
        return verificacion;
    }

    public void actualizandoUltimaReforma(ReformaIngresoSuplemento r) {
//
//        //<editor-fold defaultstate="collapsed" desc="INGRESOS ACTUALIZAR">
//        List<CatalogoPresupuesto> itemOriginal = catalogoPresupuestoService.getPresupuestoIngreso(Boolean.TRUE, r.getPeriodo());
//        List<DetalleReformaIngresoSuplemento> itemReformado = reformaService.getDetalleReormaIngreso(r);
//
//        if (!itemOriginal.isEmpty() && !itemReformado.isEmpty()) {
//            for (DetalleReformaIngresoSuplemento data : itemReformado) {
//                for (CatalogoPresupuesto item : itemOriginal) {
//                    if (item.getId() == data.getCodigoReferencia().longValue()) {
//                        data.setReducido(montoEncerado);
//                        data.setSuplemento(montoEncerado);
//                        data.setCodificado(item.getCodificado());
//                        detalleIngresoService.edit(data);
//                    }
//                }
//            }
//        }
////</editor-fold>
//        //<editor-fold defaultstate="collapsed" desc="ACTIVIDAD OPERATIVA ACTUALIZADA">
//        List<ActividadOperativa> actividadOriginal = actividadService.getActividadOriginal(r.getPeriodo());
//        List<ActividadOperativa> actividadReformando = reformaService.showActividadOperativaNuevo(BigInteger.valueOf(r.getId()));
//        if (!actividadOriginal.isEmpty() && !actividadReformando.isEmpty()) {
//
//            for (ActividadOperativa data : actividadReformando) {
//                for (ActividadOperativa item : actividadOriginal) {
//                    if (item.getId() == data.getNumeroOrdenId().longValue()) {
//                        data.setMonto(montoEncerado);
//                        data.setMonotReformado(item.getMonotReformado());
//                        data.setCr1(item.getCr1());
//                        data.setCr2(item.getCr2());
//                        data.setCr3(item.getCr3());
//
//                        actividadService.edit(data);
//                    }
//                }
//            }
//        }
//        //</editor-fold>
//        //<editor-fold defaultstate="collapsed" desc="PRODUCTO">
//        List<Producto> productoOriginal = productoService.getListaProductoByPeriodo(r.getPeriodo());
//        List<Producto> productoReformado = reformaService.showProductoNuevo(BigInteger.valueOf(r.getId()));
//        if (!productoOriginal.isEmpty() && !productoReformado.isEmpty()) {
//            for (Producto data : productoReformado) {
//                for (Producto item : productoOriginal) {
//                    if (item.getId() == data.getNumeroOrdenId().longValue()) {
//                        data.setMonto(item.getMontoReformada());
//                        data.setReserva(item.getReserva());
//                        data.setComprometido(item.getComprometido());
//                        data.setSaldoDisponible(item.getMontoReformada().subtract(item.getReserva()));
//                        data.setSuplementoEgreso(montoEncerado);
//                        data.setReduccionEgreso(montoEncerado);
//                        data.setMontoReformada(item.getMontoReformada());
//                        productoService.edit(data);
//
//                    }
//                }
//            }
//        }
////</editor-fold>
//        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO">
//        List<PartidasDistributivo> distributivoOriginal = partidasDistributivoService.getDistributivoOriginal(r.getPeriodo());
//        List<PartidasDistributivo> distributivoReformado = reformaService.showPartidasDistributivoNnuevo(BigInteger.valueOf(r.getId()));
//
//        if (!distributivoOriginal.isEmpty() && !distributivoOriginal.isEmpty()) {
//            for (PartidasDistributivo data : distributivoReformado) {
//                for (PartidasDistributivo item : distributivoOriginal) {
//                    if (item.getId() == data.getCodigoReferencia().longValue()) {
//                        data.setMonto(item.getReformaCodificado());
//                        data.setReserva(item.getReserva());
//                        data.setReformaSuplemento(montoEncerado);
//                        data.setReformaReduccion(montoEncerado);
//                        data.setReformaCodificado(item.getReformaCodificado());
//                        partidasDistributivoService.edit(data);
//                    }
//                }
//            }
//
//        }
////</editor-fold>
//        //<editor-fold defaultstate="collapsed" desc="PARTIDAS DISTRIBUTIVO ANEXO">
//        List<PartidasDistributivoAnexo> distributivoAnexoOriginal = partidaAnexoService.getDistributivoAnexoOriginal(r.getPeriodo());
//        List<PartidasDistributivoAnexo> distributivoAnexoReformado = partidaAnexoService.getPartidasAnexoReformaVerificacion(BigInteger.valueOf(r.getId()));
//
//        if (!distributivoAnexoOriginal.isEmpty() && !distributivoAnexoReformado.isEmpty()) {
//            for (PartidasDistributivoAnexo data : distributivoAnexoReformado) {
//                for (PartidasDistributivoAnexo item : distributivoAnexoOriginal) {
//                    if (item.getId() == data.getCodigoReferencia().longValue()) {
//                        data.setMonto(item.getReformaCodificado());
//                        data.setReserva(item.getReserva());
//                        data.setReformaSuplemento(montoEncerado);
//                        data.setReformaReduccion(montoEncerado);
//                        data.setReformaCodificado(item.getReformaCodificado());
//                    }
//                }
//            }
//        }
////</editor-fold>
//        //<editor-fold defaultstate="collapsed" desc="PARTIDA DIRECTA">
//        List<ProformaPresupuestoPlanificado> partidaDirectaOriginal = proformaService.getProformaPresupuestoPartidaDirecta(r.getPeriodo(), "PDI");
//        List<ProformaPresupuestoPlanificado> partidaDirectaReformado = proformaService.getPdRefroma(BigInteger.valueOf(r.getId()));
//
//        if (!partidaDirectaOriginal.isEmpty() && !partidaDirectaReformado.isEmpty()) {
//            for (ProformaPresupuestoPlanificado data : partidaDirectaReformado) {
//                for (ProformaPresupuestoPlanificado item : partidaDirectaOriginal) {
//                    if (item.getId() == data.getCodigoReferencia().longValue()) {
//
//                    }
//
//                }
//
//            }
//
//        }
////</editor-fold>
    }

    public void pruebas(ReformaIngresoSuplemento r) {
        short periodo = r.getPeriodo();
        List<Presupuesto> presupuestoViejo = presupuestoService.findByNamedQuery("Presupuesto.findByPeriodoVerificador", periodo);
        //List<Presupuesto> presupuestoNuevoCalculado = new ArrayList<>();
        presupuestoReformado(periodo);

        Presupuesto pr = new Presupuesto();

        if (!presupuestoViejo.isEmpty() && !listaPresupuestoNuevo.isEmpty()) {
            for (Presupuesto x : presupuestoViejo) {

                for (Presupuesto y : listaPresupuestoNuevo) {
                    pr = new Presupuesto();
                    if (x.getPartida().equals(y.getPartida())) {
                        pr = reformaService.getReformaPresupuestoObject(x);
                        pr.setReformaSuplemetario(y.getReformaSuplemetario());
                        pr.setReformaReducido(y.getReformaReducido());
                        pr.setCodificado(pr.getPresupuestoInicial().add(pr.getReformaSuplemetario()).subtract(pr.getReformaReducido()));
                        pr.setFechaModificacion(new Date());
                        pr.setUsuarioModificacion(user.getNameUser());
                        //presupuestoService.edit(pr);

                        pr = new Presupuesto();
                    }

                }
            }
        }

        if (!presupuestoViejo.isEmpty() && !listaPresupuestoNuevo.isEmpty()) {
            boolean verificador = true;
            for (Presupuesto v : listaPresupuestoNuevo) {
                pr = new Presupuesto();
                verificador = true;
                for (Presupuesto w : presupuestoViejo) {
                    if (v.getPartida().equals(w.getPartida())) {
                        verificador = false;
                    }
                }
                if (verificador) {
                    pr = Utils.clone(v);
                    pr.setId(null);
                    pr.setPresupuestoInicial(BigDecimal.ZERO);
                    //  pr = presupuestoService.create(pr);
                }
            }
        }

    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

    public CatalogoPresupuestoService getCatalogoPresupuestoService() {
        return catalogoPresupuestoService;
    }

    public void setCatalogoPresupuestoService(CatalogoPresupuestoService catalogoPresupuestoService) {
        this.catalogoPresupuestoService = catalogoPresupuestoService;
    }

    public PlanLocalProgramaProyectoService getPlanLocalProgramaProyectoService() {
        return planLocalProgramaProyectoService;
    }

    public void setPlanLocalProgramaProyectoService(PlanLocalProgramaProyectoService planLocalProgramaProyectoService) {
        this.planLocalProgramaProyectoService = planLocalProgramaProyectoService;
    }

    public PlanAnualPoliticaPublicaService getPlanAnualPoliticaPublicaService() {
        return planAnualPoliticaPublicaService;
    }

    public void setPlanAnualPoliticaPublicaService(PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService) {
        this.planAnualPoliticaPublicaService = planAnualPoliticaPublicaService;
    }

    public PlanAnualProgramaProyectoService getPlanAnualProgramaProyectoService() {
        return planAnualProgramaProyectoService;
    }

    public void setPlanAnualProgramaProyectoService(PlanAnualProgramaProyectoService planAnualProgramaProyectoService) {
        this.planAnualProgramaProyectoService = planAnualProgramaProyectoService;
    }

    public ActividadOperativaService getActividadService() {
        return actividadService;
    }

    public void setActividadService(ActividadOperativaService actividadService) {
        this.actividadService = actividadService;
    }

    public ProductoService getProductoService() {
        return productoService;
    }

    public void setProductoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public ProformaPresupuestoPlanificadoService getProformaService() {
        return proformaService;
    }

    public void setProformaService(ProformaPresupuestoPlanificadoService proformaService) {
        this.proformaService = proformaService;
    }

    public PresupuestoService getPresupuestoService() {
        return presupuestoService;
    }

    public void setPresupuestoService(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    public PartidaDistributivoService getPartidasDistributivoService() {
        return partidasDistributivoService;
    }

    public void setPartidasDistributivoService(PartidaDistributivoService partidasDistributivoService) {
        this.partidasDistributivoService = partidasDistributivoService;
    }

    public PartidaDistributivoAnexoService getPartidaAnexoService() {
        return partidaAnexoService;
    }

    public void setPartidaAnexoService(PartidaDistributivoAnexoService partidaAnexoService) {
        this.partidaAnexoService = partidaAnexoService;
    }

    public CatalogoPresupuestoService getItemService() {
        return itemService;
    }

    public void setItemService(CatalogoPresupuestoService itemService) {
        this.itemService = itemService;
    }

    public DetalleSuplementoIngresoService getDetalleIngresoService() {
        return detalleIngresoService;
    }

    public void setDetalleIngresoService(DetalleSuplementoIngresoService detalleIngresoService) {
        this.detalleIngresoService = detalleIngresoService;
    }

    public ReformaSuplementoIngresoService getReformaService() {
        return reformaService;
    }

    public void setReformaService(ReformaSuplementoIngresoService reformaService) {
        this.reformaService = reformaService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public CatalogoProformaPresupuestoService getMantenimientoProformaService() {
        return mantenimientoProformaService;
    }

    public void setMantenimientoProformaService(CatalogoProformaPresupuestoService mantenimientoProformaService) {
        this.mantenimientoProformaService = mantenimientoProformaService;
    }

    public ProgramacionIngresoEgresoService getPimService() {
        return pimService;
    }

    public void setPimService(ProgramacionIngresoEgresoService pimService) {
        this.pimService = pimService;
    }

    public UnidadAdministrativaService getUnidadService() {
        return unidadService;
    }

    public void setUnidadService(UnidadAdministrativaService unidadService) {
        this.unidadService = unidadService;
    }

    public ReformaCupoReduccionService getCupoReduccionService() {
        return cupoReduccionService;
    }

    public void setCupoReduccionService(ReformaCupoReduccionService cupoReduccionService) {
        this.cupoReduccionService = cupoReduccionService;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
    }

    public CatalogoPresupuesto getItemIngreso() {
        return itemIngreso;
    }

    public void setItemIngreso(CatalogoPresupuesto itemIngreso) {
        this.itemIngreso = itemIngreso;
    }

    public DetalleReformaIngresoSuplemento getDetalleItemReformaIngreso() {
        return detalleItemReformaIngreso;
    }

    public void setDetalleItemReformaIngreso(DetalleReformaIngresoSuplemento detalleItemReformaIngreso) {
        this.detalleItemReformaIngreso = detalleItemReformaIngreso;
    }

    public ActividadOperativa getActividad() {
        return actividad;
    }

    public void setActividad(ActividadOperativa actividad) {
        this.actividad = actividad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ThCargoRubros getPartidaDistributivo() {
        return partidaDistributivo;
    }

    public void setPartidaDistributivo(ThCargoRubros partidaDistributivo) {
        this.partidaDistributivo = partidaDistributivo;
    }

    public ProformaPresupuestoPlanificado getPartidasDirectas() {
        return partidasDirectas;
    }

    public void setPartidasDirectas(ProformaPresupuestoPlanificado partidasDirectas) {
        this.partidasDirectas = partidasDirectas;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public ProgramacionIngresoEgreso getPim() {
        return pim;
    }

    public void setPim(ProgramacionIngresoEgreso pim) {
        this.pim = pim;
    }

    public CupoReduccion getCupoReduccion() {
        return cupoReduccion;
    }

    public void setCupoReduccion(CupoReduccion cupoReduccion) {
        this.cupoReduccion = cupoReduccion;
    }

    public List<ProformaIngreso> getListaitemOriginal() {
        return listaitemOriginal;
    }

    public void setListaitemOriginal(List<ProformaIngreso> listaitemOriginal) {
        this.listaitemOriginal = listaitemOriginal;
    }

    public List<DetalleReformaIngresoSuplemento> getListaItemNuevo() {
        return listaItemNuevo;
    }

    public void setListaItemNuevo(List<DetalleReformaIngresoSuplemento> listaItemNuevo) {
        this.listaItemNuevo = listaItemNuevo;
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

    public List<ThCargoRubros> getListaPartidasDistributivoNuevo() {
        return listaPartidasDistributivoNuevo;
    }

    public void setListaPartidasDistributivoNuevo(List<ThCargoRubros> listaPartidasDistributivoNuevo) {
        this.listaPartidasDistributivoNuevo = listaPartidasDistributivoNuevo;
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

    public List<ProformaPresupuestoPlanificado> getShowCodigoRepetidosList() {
        return showCodigoRepetidosList;
    }

    public void setShowCodigoRepetidosList(List<ProformaPresupuestoPlanificado> showCodigoRepetidosList) {
        this.showCodigoRepetidosList = showCodigoRepetidosList;
    }

    public List<presupuestoModel> getLista() {
        return lista;
    }

    public void setLista(List<presupuestoModel> lista) {
        this.lista = lista;
    }

    public List<presupuestoModel> getLista2() {
        return lista2;
    }

    public void setLista2(List<presupuestoModel> lista2) {
        this.lista2 = lista2;
    }

    public List<Presupuesto> getListaPresupuestoNuevo() {
        return listaPresupuestoNuevo;
    }

    public void setListaPresupuestoNuevo(List<Presupuesto> listaPresupuestoNuevo) {
        this.listaPresupuestoNuevo = listaPresupuestoNuevo;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyReforma() {
        return lazyReforma;
    }

    public void setLazyReforma(LazyModel<ReformaIngresoSuplemento> lazyReforma) {
        this.lazyReforma = lazyReforma;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isVerificacionGlobal() {
        return verificacionGlobal;
    }

    public void setVerificacionGlobal(boolean verificacionGlobal) {
        this.verificacionGlobal = verificacionGlobal;
    }

    public ProformaIngresoService getProformaIngresoService() {
        return proformaIngresoService;
    }

    public void setProformaIngresoService(ProformaIngresoService proformaIngresoService) {
        this.proformaIngresoService = proformaIngresoService;
    }

    public ThCargoRubrosService getThCargoRubrosService() {
        return thCargoRubrosService;
    }

    public void setThCargoRubrosService(ThCargoRubrosService thCargoRubrosService) {
        this.thCargoRubrosService = thCargoRubrosService;
    }
//</editor-fold>

}
