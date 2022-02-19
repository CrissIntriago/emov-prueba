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
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
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
@Named(value = "revisionPresupuestoReformaView")
@ViewScoped
public class ReformaRevisionPresupuestoController extends BpmnBaseRoot implements Serializable {

    private Producto producto;
    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private OpcionBusqueda busqueda;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    private List<Producto> listaProducto;
    private List<Producto> listaProductoPartida;
    private List<PlanProgramatico> listaPlanProgramatico;
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

    private boolean btnAprobar, btnRechazar;
    private boolean btnAnular, btnObservar;
    private String observaciones;
    private boolean btnasignarpartida, renderTabPAPP, renderedPartidaDirecta;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;

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
    private PlanProgramaticoService estrucPlanProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;

    //NUEVO
    @Inject
    private PresCatalogoPresupuestarioService catalogoServiceNew;
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private PresPlanProgramaticoService estructuraServiceNew;

    private List<PresCatalogoPresupuestario> listItem;
    private List<PresPlanProgramatico> listEstructura;
    private List<PresFuenteFinanciamiento> listFuente;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;

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
                lazyReformaTraspaso.getFilterss().put("estadoReformaTramite:equal", estadoReforma);
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

                //NUEVO
                listItem = new ArrayList<>();
                listItem = catalogoServiceNew.findTipoPresupuesto(false);
                listEstructura = new ArrayList<>();
                listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
                listFuente = new ArrayList<>();
                listFuente = fuenteServiceNew.getFuenteFinanciamiento();

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
            listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId())); //ENRIQUE
            //reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId())); 
        } else {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        }
        boolean btnAsignarPartidaFinal = false;
        for (Producto producto1 : listaProducto) {
            boolean mostrar = reformaTraspasoService.getProductoSinPartidaBol(producto1);
            if (btnAsignarPartidaFinal) {
            } else {
                btnAsignarPartidaFinal = mostrar;
            }
        }

        return btnAsignarPartidaFinal;
    }

    public void asignarPartida(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        if (reforma.getTipo()) {
            listaProductoPartida = reformaTraspasoService.getListProductoByReformaT1SinPartida(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        } else {
            listaProductoPartida = reformaTraspasoService.getListProductoByReformaT2SinPartida(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        }
        if (listaProductoPartida.isEmpty()) {
            JsfUtil.addErrorMessage("Producto", "No existen Productos sin Partida");
        }
        PrimeFaces.current().executeScript("PF('dlgAsignarPartida').show()");
    }

    public void editarCell(Producto p) {
        try {

            Boolean modifico = false;
//            proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(busqueda.getAnio(), p.getCodigoPresupuestario());

            if (p.getItemNew() != null && p.getEstructruaNew() != null && p.getFuenteNew() != null) {
                p.setCodigoPresupuestario(p.getEstructruaNew().getCodigo() + p.getItemNew().getCodigo() + p.getFuenteNew().getCodFuente());
                modifico = true;
            } else {
                p.setCodigoPresupuestario("");
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

    public boolean aprobarPresupuesto(boolean aprobar, ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        btnasignarpartida = false;
        CatalogoItem estadoReformaRechazada = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RECT");
        CatalogoItem estadoReformaAprobada = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        CatalogoItem estadopappaprobado = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        CatalogoItem estadopappanulado = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "ANU-REF");

        if (reforma.getTipo()) {
            listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        } else {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        }
        for (Producto producto1 : listaProducto) {
            if (!btnasignarpartida) {
                boolean mostrar = reformaTraspasoService.getProductoSinPartida(producto1);
                btnasignarpartida = mostrar;
            }
        }

        if (aprobar) {
            if (btnasignarpartida) {
                JsfUtil.addWarningMessage("Producto", "No se puede aprobar, existen productos sin partidas asignadas");
                return false;
            } else {
//                reformaTraspaso.setEstadoReforma(estadoReformaAprobada);
                reformaTraspaso.setEstadoReformaTramite(estadopappaprobado);
                reformaTraspasoService.edit(reformaTraspaso);
                return true;
            }
        } else {
            reformaTraspaso.setEstadoReforma(estadoReformaRechazada);
            reformaTraspaso.setEstadoReformaTramite(estadopappanulado);
            reformaTraspasoService.edit(reformaTraspaso);
            return true;
        }

//        PrimeFaces.current().ajax().update("messages");
//        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + (aprobar ? " aprobada" : " anulada") + " con éxito");
//        this.reformaTraspaso = new ReformaTraspaso();
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
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        reformaTraspaso = reforma;
        busqueda.setAnio(reforma.getPeriodo());
        totalTraspasoIncremento = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, true);
        totalTraspasoReduccion = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, false);
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
        renderTabPAPP = !listaProducto.isEmpty();
        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPDIReformaT1(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
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
            btnObservar = false;
        } else if (a == 0) {
            btnAprobar = false;
            btnRechazar = true;
            btnAnular = false;
            btnObservar = false;
        } else {
            btnAprobar = false;
            btnRechazar = false;
            btnAnular = true;
            btnObservar = true;
        }

        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);
            if (aprobar == 1) {
                if (!aprobarPresupuesto(true, reformaTraspaso)) {
                    JsfUtil.executeJS("PF('dlgObservaciones').hide()");
                    PrimeFaces.current().ajax().update(":frmDlgObser");
                    return;
                }
                if (listaProducto.isEmpty() && !listaPartidaDirecta.isEmpty()) {
                    getParamts().put("usuarioPresupuesto", clienteService.getrolsUser(RolUsuario.rolReformasPDI));
                } else {
                    getParamts().put("usuarioPresupuesto", clienteService.getrolsUser(RolUsuario.presupuesto));
                }
            } else if (aprobar == 1) {

            } else { // = 0
                getParamts().put("userAsistDireccion", clienteService.getrolsUser(RolUsuario.asistenteDireccion));
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
                JsfUtil.addInformationMessage("Información", "Solicitud se ha aprobado con éxito");
            } else {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solicitud se ha rechazado con éxito");
            }

            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void imprimirInforme(ReformaTraspaso reforma) {

    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public boolean isBtnObservar() {
        return btnObservar;
    }

    public void setBtnObservar(boolean btnObservar) {
        this.btnObservar = btnObservar;
    }

    public List<PresCatalogoPresupuestario> getListItem() {
        return listItem;
    }

    public void setListItem(List<PresCatalogoPresupuestario> listItem) {
        this.listItem = listItem;
    }

    public List<PresPlanProgramatico> getListEstructura() {
        return listEstructura;
    }

    public void setListEstructura(List<PresPlanProgramatico> listEstructura) {
        this.listEstructura = listEstructura;
    }

    public List<PresFuenteFinanciamiento> getListFuente() {
        return listFuente;
    }

    public void setListFuente(List<PresFuenteFinanciamiento> listFuente) {
        this.listFuente = listFuente;
    }

    public Producto getProducto() {
        return producto;
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

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
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

    public boolean isRenderTabPAPP() {
        return renderTabPAPP;
    }

    public void setRenderTabPAPP(boolean renderTabPAPP) {
        this.renderTabPAPP = renderTabPAPP;
    }

    public boolean isRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

//</editor-fold>
}
