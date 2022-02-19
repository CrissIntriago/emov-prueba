/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.model.ReporteDeActividades;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.ProductoService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Named(value = "revisionGeneralNormalView")
@ViewScoped
public class RevisionGeneralPresupuestoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ProductoService productoService;
    @Inject
    private MasterCatalogoService masterService;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private ValoresDistributivoService valoresDistributivoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession user;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    private ThCargoRubrosService cargoRubrosService;
    @Inject
    private ClienteService clienteService;

    private LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy;
    private OpcionBusqueda busqueda;
    private ArrayList<ReporteDeActividades> list;
    private ReporteDeActividades reporte;
    private boolean filtroDatosNullTablaGeneralPlanes;
    private List<MasterCatalogo> listaPeriodos;
    private LazyModel<Distributivo> lazyDistributivo;
    private LazyModel<DistributivoAnexo> lazyDistributivoAnexo;
    private boolean panelPApp, panelDistributivo, panelDistributivoAnexo;
    private BigDecimal totalPapp, totalDistributivo, totalDistributivoAnexo;
    private List<ValoresDistributivo> valoresDistributivoList;
    private List<ValoresDistributivo> valorList;
    private short periodo;
    private String observaciones;
    private boolean btnAprobar, btnRechazar;
    //NUEVO
    @Inject
    private ManagerService service;
    private LazyModel<ThCargo> lazyCargo;
    private List<ThCargoRubros> listCargoRubros;
    private Map<String, Object> param;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodo = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "CP", periodo});
                busqueda = new OpcionBusqueda();
                busqueda.setAnio(periodo);
                reporte = new ReporteDeActividades();

                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);

                lazyDistributivo = new LazyModel(Distributivo.class);
                lazyDistributivo.getFilterss().put("estado", true);
                lazyDistributivo.getSorteds().put("id", "ASC");

                lazyDistributivoAnexo = new LazyModel(DistributivoAnexo.class);
                lazyDistributivoAnexo.getFilterss().put("estado", true);
                lazyDistributivoAnexo.getFilterss().put("anio", busqueda.getAnio());

                filtroDatosNullTablaGeneralPlanes = true;
                list = new ArrayList<>();
                panelPApp = false;
                panelDistributivo = false;
                panelDistributivoAnexo = false;
                totalPapp = BigDecimal.ZERO;
                totalDistributivo = BigDecimal.ZERO;
                totalDistributivoAnexo = BigDecimal.ZERO;
                valoresDistributivoList = new ArrayList();
                valorList = new ArrayList();
                btnAprobar = false;
                btnRechazar = false;
                cargarNuevoDistributivo();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    //NUEVO
    public void cargarNuevoDistributivo() {
        lazyCargo = new LazyModel<>(ThCargo.class);
        lazyCargo.getFilterss().put("estado", true);
    }

    public void cargarNuevoCargoRubros(ThCargo thcargo) {
        listCargoRubros = new ArrayList();

        listCargoRubros = cargoRubrosService.findByCargoXanio(thcargo, periodo);
    }

    public void cargando() {
        vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
        vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);

        lazyDistributivo = new LazyModel(Distributivo.class);
        lazyDistributivo.getFilterss().put("estado", true);
        lazyDistributivo.getSorteds().put("servidorPublico.persona.apellido", "ASC");

        lazyDistributivoAnexo = new LazyModel(DistributivoAnexo.class);
        lazyDistributivoAnexo.getFilterss().put("estado", true);
        lazyDistributivoAnexo.getFilterss().put("anio", busqueda.getAnio());

    }

    public List<ValoresDistributivo> detalleDistributivo(Distributivo d) {
        valoresDistributivoList = new ArrayList<>();
        valorList = new ArrayList<>();
        valorList = valoresDistributivoService.findvaloresDistributivo(d, busqueda);

        for (ValoresDistributivo data : valorList) {
            valoresDistributivoList.add(data);
        }

        return valorList;
    }

    public void verificadorDeValores() {
        list = new ArrayList<>();
        ArrayList<ReporteDeActividades> ListEquilibrado = new ArrayList<>();
        ArrayList<ReporteDeActividades> ListDesequilibrado = new ArrayList<>();
        ListEquilibrado = new ArrayList<>();
        List<ActividadOperativa> resultActividades = actividadService.getListActividades(busqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
        List<Producto> resultProductos = productoService.getListProductos(busqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
        if (resultActividades.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay Actividades Operativas registrados en el período " + busqueda.getAnio());
            return;
        } else {
            if (resultProductos.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO", "No hay Productos registrados en el período " + busqueda.getAnio());
                return;
            } else {
                for (ActividadOperativa actividad : resultActividades) {
                    BigDecimal totalProducto = productoService.getSumaProducto(actividad, busqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
                    if (!actividad.getMonto().equals(totalProducto)) {
                        /*Se iran agregando las actividades que la suma de sus productos no sean igual al monto asignado*/
                        reporte.setActividadOperativa(actividad);
                        reporte.setMontoTotalDeProductos(totalProducto);
                        reporte.setDiferencia(actividad.getMonto().subtract(totalProducto));
                        ListDesequilibrado.add(reporte);
                        reporte = new ReporteDeActividades();
                    } else {
                        reporte.setActividadOperativa(actividad);
                        reporte.setMontoTotalDeProductos(totalProducto);
                        reporte.setDiferencia(actividad.getMonto().subtract(totalProducto));
                        ListEquilibrado.add(reporte);
                        reporte = new ReporteDeActividades();
                    }
                }
                if (ListDesequilibrado.isEmpty()) {
                    list = ListEquilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductosConsulta').show()");
                    PrimeFaces.current().ajax().update("formActividadProductosConsulta");
                } else {
                    list = ListDesequilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductosConsulta').show()");
                    PrimeFaces.current().ajax().update("formActividadProductosConsulta");
                }
            }
        }
    }

    public void filtroDeDatosNullVistaGeneralPlanAnual() {
        if (busqueda.getAnio() == 0) {
            JsfUtil.addWarningMessage("AVISO", "SELECIONES UN PERDIODO ANTES DE INICIAR EL PROCESO");
            PrimeFaces.current().ajax().update(":dataGeneral");
            filtroDatosNullTablaGeneralPlanes = true;
        } else {
            if (!filtroDatosNullTablaGeneralPlanes) {
                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("nombrePlanLocalProgramaProyecto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                //new VistaGeneralPlanAnualLazy(!filtroDatosNullTablaGeneralPlanes, busqueda);
                PrimeFaces.current().ajax().update("formTablaMain");
            } else {
                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                PrimeFaces.current().ajax().update("formTablaMain");
            }
        }
    }

    public void AprobarRevision(boolean aprobar) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (aprobar) {
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
        }

        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);

            CatalogoItem estadoAprobadoPapp = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
            CatalogoItem estadoRecahazadoPapp = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REP");

            if (aprobar == 1) {

                actividadService.aprobarTodo(busqueda.getAnio(), estadoAprobadoPapp);

            } else {

                actividadService.aprobarTodo(busqueda.getAnio(), estadoRecahazadoPapp);

            }

            getParamts().put("aprobado", aprobar);
            //clienteService.getUnidadUserCodigo("CPR", "2")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto)); //presupuesto

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public BigDecimal getTotalPapp() {
        return totalPapp;
    }

    public void setTotalPapp(BigDecimal totalPapp) {
        this.totalPapp = totalPapp;
    }

    public BigDecimal getTotalDistributivo() {
        return totalDistributivo;
    }

    public void setTotalDistributivo(BigDecimal totalDistributivo) {
        this.totalDistributivo = totalDistributivo;
    }

    public BigDecimal getTotalDistributivoAnexo() {
        return totalDistributivoAnexo;
    }

    public void setTotalDistributivoAnexo(BigDecimal totalDistributivoAnexo) {
        this.totalDistributivoAnexo = totalDistributivoAnexo;
    }

    public List<ValoresDistributivo> getValoresDistributivoList() {
        return valoresDistributivoList;
    }

    public void setValoresDistributivoList(List<ValoresDistributivo> valoresDistributivoList) {
        this.valoresDistributivoList = valoresDistributivoList;
    }

    public List<ValoresDistributivo> getValorList() {
        return valorList;
    }

    public void setValorList(List<ValoresDistributivo> valorList) {
        this.valorList = valorList;
    }

    public boolean isPanelPApp() {
        return panelPApp;
    }

    public void setPanelPApp(boolean panelPApp) {
        this.panelPApp = panelPApp;
    }

    public boolean isPanelDistributivo() {
        return panelDistributivo;
    }

    public void setPanelDistributivo(boolean panelDistributivo) {
        this.panelDistributivo = panelDistributivo;
    }

    public boolean isPanelDistributivoAnexo() {
        return panelDistributivoAnexo;
    }

    public void setPanelDistributivoAnexo(boolean panelDistributivoAnexo) {
        this.panelDistributivoAnexo = panelDistributivoAnexo;
    }

    public LazyModel<VistaGeneralPlanAnual> getVistaGeneralPlanAnualLazy() {
        return vistaGeneralPlanAnualLazy;
    }

    public void setVistaGeneralPlanAnualLazy(LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy) {
        this.vistaGeneralPlanAnualLazy = vistaGeneralPlanAnualLazy;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public ArrayList<ReporteDeActividades> getList() {
        return list;
    }

    public void setList(ArrayList<ReporteDeActividades> list) {
        this.list = list;
    }

    public ReporteDeActividades getReporte() {
        return reporte;
    }

    public void setReporte(ReporteDeActividades reporte) {
        this.reporte = reporte;
    }

    public boolean isFiltroDatosNullTablaGeneralPlanes() {
        return filtroDatosNullTablaGeneralPlanes;
    }

    public void setFiltroDatosNullTablaGeneralPlanes(boolean filtroDatosNullTablaGeneralPlanes) {
        this.filtroDatosNullTablaGeneralPlanes = filtroDatosNullTablaGeneralPlanes;
    }

    public List<MasterCatalogo> getListaPeriodos() {
        return listaPeriodos;
    }

    public void setListaPeriodos(List<MasterCatalogo> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public LazyModel<Distributivo> getLazyDistributivo() {
        return lazyDistributivo;
    }

    public void setLazyDistributivo(LazyModel<Distributivo> lazyDistributivo) {
        this.lazyDistributivo = lazyDistributivo;
    }

    public LazyModel<DistributivoAnexo> getLazyDistributivoAnexo() {
        return lazyDistributivoAnexo;
    }

    public void setLazyDistributivoAnexo(LazyModel<DistributivoAnexo> lazyDistributivoAnexo) {
        this.lazyDistributivoAnexo = lazyDistributivoAnexo;
    }

//</editor-fold>
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public LazyModel<ThCargo> getLazyCargo() {
        return lazyCargo;
    }

    public void setLazyCargo(LazyModel<ThCargo> lazyCargo) {
        this.lazyCargo = lazyCargo;
    }

    public List<ThCargoRubros> getListCargoRubros() {
        return listCargoRubros;
    }

    public void setListCargoRubros(List<ThCargoRubros> listCargoRubros) {
        this.listCargoRubros = listCargoRubros;
    }

}
