/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.Presupuesto.Service.PlanificacionPlanService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ReporteDeActividades;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "consultaPappView")
@ViewScoped
public class ConsultaPappController implements Serializable {

    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ProductoService productoService;
    @Inject
    private MasterCatalogoService masterService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession userSession;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private ServletSession ss;
    @Inject
    private PlanificacionPlanService planService;
    @Inject
    private PappProcesoService pappService;

    private LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy;
    private OpcionBusqueda busqueda;
    private ArrayList<ReporteDeActividades> list;
    private ReporteDeActividades reporte;
    private boolean filtroDatosNullTablaGeneralPlanes;
    private List<MasterCatalogo> listaPeriodos;
    private UnidadAdministrativa unidadAdministrativa;
    private boolean bloqueoBtn;

    @PostConstruct
    public void inicializar() {
        unidadAdministrativa = new UnidadAdministrativa();
        unidadAdministrativa = clienteService.getUnidadPrincipalUSer(userSession.getNameUser());
        busqueda = new OpcionBusqueda();
        reporte = new ReporteDeActividades();
        bloqueoBtn = false;
        listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        filtroDatosNullTablaGeneralPlanes = true;
        list = new ArrayList<>();
        if (listaPeriodos != null) {
            int indice = listaPeriodos.size();
            if (!listaPeriodos.isEmpty() && indice == 1) {
                busqueda.setAnio(listaPeriodos.get(0).getAnio());
            } else {
                //busqueda.setAnio(listaPeriodos.get(indice - 1).getAnio());
                busqueda.setAnio(Short.valueOf(Utils.getAnio(new Date()).toString()));
            }
        }
        newBusqueda();
    }

    public String obtNombrePlan(VistaGeneralPlanAnual item) {
        return planService.obtieneNombrePlan(item.getPlan()).getDescripcion();
    }

    public String obtNombrePrograma(VistaGeneralPlanAnual item) {
        return planService.obtieneNombrePrograma(item.getPrograma()).getDescripcion();
    }

    public BigDecimal getMontoDisponible(Long id) {
        if (id != null) {
            Producto da = productoService.find(id);
            if (da == null || da.getId() == null) {
                return BigDecimal.ZERO;
            }
            BigDecimal valor = pappService.getValorAproabdo(da);
            return da.getMontoReformada().subtract(valor);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getMontoSolitado(Long id) {
        if (id != null) {
            Producto da = productoService.find(id);
            if (da == null || da.getId() == null) {
                return BigDecimal.ZERO;
            }
            BigDecimal valor = pappService.getValorAproabdo(da);
            return valor;
        } else {
            return BigDecimal.ZERO;
        }

    }

    public void newBusqueda() {
        if (busqueda == null) {
            return;
        }
        List<Rol> rol = new ArrayList<>();
        rol = clienteService.getRolCategoriaUnidad(userSession.getNameUser());
        boolean verififcar = false;
        for (Rol item : rol) {
            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
            vistaGeneralPlanAnualLazy.getFilterss().put("periodoActividadOperativa:equal", busqueda.getAnio());
            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
            vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);

            switch (item.getCategoria().getCodigo()) {
                case "2":
                case "6":
                case "7":
                case "15":
                    vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                    vistaGeneralPlanAnualLazy.getFilterss().put("periodoActividadOperativa:equal", busqueda.getAnio());
                    vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                    vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                    vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);
                    bloqueoBtn = false;
                    verififcar = true;
                    break;

                case "1":
                    List<String> unidades = unidadService.getListaPadresAdministrativas(item.getUnidadAdministrativa());
                    vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                    vistaGeneralPlanAnualLazy.getFilterss().put("periodoActividadOperativa:equal", busqueda.getAnio());
                    vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                    vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                    vistaGeneralPlanAnualLazy.getFilterss().put("unidadResponsable", unidades);
                    vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);
                    bloqueoBtn = false;
                    verififcar = true;
                    break;

                default:
                    bloqueoBtn = true;
                    verififcar = false;
                    break;
            }
            if (verififcar) {
                break;
            }
        }

        if (verififcar) {
            return;
        } else {
            PrimeFaces.current().ajax().update("formTablaMain");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "USTED NO TIENE EL PERMISO PARA PODER CONSULTAR PAPP");
        }

        //vistaGeneralPlanAnualLazy.getFilterss().put("unidadResponsable:equal", unidadAdministrativa.getNombre());
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

            List<Rol> rol = new ArrayList<>();
            rol = clienteService.getRolCategoriaUnidad(userSession.getNameUser());
            boolean verififcar = false;
            for (Rol item : rol) {
                switch (item.getCategoria().getCodigo()) {
                    case "2":
                    case "6":
                    case "7":
                    case "15":
                        if (!filtroDatosNullTablaGeneralPlanes) {
                            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                            vistaGeneralPlanAnualLazy.getFilterss().put("nombrePlanLocalProgramaProyecto:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);
                            //new VistaGeneralPlanAnualLazy(!filtroDatosNullTablaGeneralPlanes, busqueda);
                            PrimeFaces.current().ajax().update("formTablaMain:dataGeneral");
                        } else {
                            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                            vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);
                            PrimeFaces.current().ajax().update("formTablaMain:dataGeneral");
                        }

                        bloqueoBtn = false;
                        verififcar = true;
                        break;

                    case "1":
                        List<String> unidades = unidadService.getListaPadresAdministrativas(item.getUnidadAdministrativa());
                        if (!filtroDatosNullTablaGeneralPlanes) {
                            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                            vistaGeneralPlanAnualLazy.getFilterss().put("nombrePlanLocalProgramaProyecto:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("unidadResponsable", unidades);
                            vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);
                            //new VistaGeneralPlanAnualLazy(!filtroDatosNullTablaGeneralPlanes, busqueda);
                            PrimeFaces.current().ajax().update("formTablaMain:dataGeneral");
                        } else {
                            vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                            vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                            vistaGeneralPlanAnualLazy.getFilterss().put("unidadResponsable", unidades);
                            vistaGeneralPlanAnualLazy.getFilterss().put("idProducto:notEqual", null);
                            PrimeFaces.current().ajax().update("formTablaMain:dataGeneral");
                        }

                        bloqueoBtn = false;
                        verififcar = true;
                        break;

                    default:
                        bloqueoBtn = true;
                        verififcar = false;
                        break;
                }

                if (verififcar) {
                    break;
                }
            }

            if (verififcar) {
                return;
            } else {
                PrimeFaces.current().ajax().update("formTablaMain");
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("AVISO", "USTED NO TIENE EL PERMISO PARA PODER CONSULTAR PAPP");
            }

        }
    }

    public void imprimir(boolean excel) {
        if (busqueda == null) {
            JsfUtil.addWarningMessage("AVISO", "ELIGA UN PERIODO");
            return;
        }
        if (excel) {
            ss.setContentType("xlsx");
        }
        ss.addParametro("periodo", busqueda.getAnio());
        ss.setNombreReporte("papp");
        ss.setNombreSubCarpeta("presupuesto");
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formTablaMain:dataGeneral");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formTablaMain:dataGeneral");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
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

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public LazyModel<VistaGeneralPlanAnual> getVistaGeneralPlanAnualLazy() {
        return vistaGeneralPlanAnualLazy;
    }

    public void setVistaGeneralPlanAnualLazy(LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy) {
        this.vistaGeneralPlanAnualLazy = vistaGeneralPlanAnualLazy;
    }

    public List<MasterCatalogo> getListaPeriodos() {
        return listaPeriodos;
    }

    public void setListaPeriodos(List<MasterCatalogo> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public boolean isBloqueoBtn() {
        return bloqueoBtn;
    }

    public void setBloqueoBtn(boolean bloqueoBtn) {
        this.bloqueoBtn = bloqueoBtn;
    }
//</editor-fold>

}
