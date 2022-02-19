/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Service.ProformaIngresoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.NivelService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.lazy.CatalogoPresupuestoLazy;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "proformaIngresoVista")
@ViewScoped
public class ProformaIngresoController extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CatalogoPresupuestoController.class.getName());
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoProformaPresupuestoService proformaService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private NivelService nivelService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private ServletSession ss;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    @Inject
    private ClienteService clienteService;

    private List<FuenteFinanciamiento> tipoFuente;
    private List<CatalogoItem> listClasificacion;
    private List<Nivel> listNivel;
    private List<FuenteFinanciamiento> listFuenteF;
    private List<String> listaCodigos;

    //PresupuestoIngreso
    private CatalogoPresupuestoLazy presupuestoIngresoLazy;

    private LazyModel<CatalogoPresupuesto> lazy;
    private CatalogoPresupuesto cuenta;
    private CatalogoPresupuesto tipoCtaMovimiento;
    private CatalogoPresupuesto catItem;
    private CatalogoPresupuesto nivelN;
    private CatalogoPresupuesto fuenteF;
    private CatalogoPresupuesto codigoC;
    private String adicionalString;

    private CatalogoPresupuesto cuentaSeleccionada;
    private List<Nivel> niveles;
    private List<MasterCatalogo> periodos;
    private List<MasterCatalogo> maxPeriodos;
    private List<CatalogoItem> clasificaciones;
    private CatalogoProformaPresupuesto catalogoProforma;
    private CatalogoProformaPresupuesto catalogoProAdicional;
    private MasterCatalogo mc;

    private int cantColumnas;
    private String columnClass;
    private boolean etica = Boolean.FALSE;
    private boolean accionNaturaleza = Boolean.FALSE;
    private boolean accionSubgrupo = Boolean.FALSE;
    private boolean accionRubro = Boolean.FALSE;
    private double totalIngreso = 0;
    private String aprobado = "";
    private CatalogoProformaPresupuesto proformaPresupuestoActualizar;
    private short newPeriodo;
    private Boolean disabled;
    private List<CuentaContable> creditos;
    private List<CuentaContable> debitos;
    private List<CuentaContable> lista;
    private boolean bloqueabtn;
    private String observaciones;
    private BigDecimal total;
    private OpcionBusqueda opcionBusqueda;

    //NUEVO
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private ManagerService services;
    @Inject
    private ProformaIngresoService proformaIngresoService;
    private LazyModel<ProformaIngreso> lazyProforma;
    private List<PresFuenteFinanciamiento> listaFuenteNew;
    private Map<String, Object> param;
    private ProformaIngreso proformaIngreso;
    private ProformaIngreso proformaSeleccionada;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                //NUEVO
                listaFuenteNew = new ArrayList<>();
                proformaIngreso = new ProformaIngreso();
                proformaSeleccionada = new ProformaIngreso();
                //
                lista = new ArrayList<>();
                opcionBusqueda = new OpcionBusqueda();
                catalogoProforma = new CatalogoProformaPresupuesto();
                catalogoProAdicional = new CatalogoProformaPresupuesto();
                tipoCtaMovimiento = new CatalogoPresupuesto();
                catItem = new CatalogoPresupuesto();
                nivelN = new CatalogoPresupuesto();
                fuenteF = new CatalogoPresupuesto();
                codigoC = new CatalogoPresupuesto();
                adicionalString = new String();
                mc = new MasterCatalogo();
                mc.setAnio(Utils.getAnio(new Date()).shortValue());
                catalogoProforma.setPeriodoCatalogo(mc);
                //actualizarTable();

                lazy = new LazyModel(CatalogoPresupuesto.class);
                lazy.getSorteds().put("codigo", "ASC");
                lazy.getFilterss().put("anio:equal", catalogoProforma.getPeriodoCatalogo().getAnio());

                niveles = nivelService.findByNamedQuery("Nivel.findByCatalogoAndCodigo", new Object[]{"tipo_cuenta", "CP"});
                periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
                cuenta = new CatalogoPresupuesto();
                clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");
                cantColumnas = 4;
                columnClass = "ui-grid-col-3";
                //   presupuestoIngresoLazy = new CatalogoPresupuestoLazy(catalogoProforma);
                tipoFuente = fuenteService.findAll();
                disabled = false;
                creditos = catalogoPresupuestoService.getAsociacionPresupuestaria(catalogoProforma.getPeriodoCatalogo().getAnio());
                debitos = catalogoPresupuestoService.getAsociacionPresupuestaria(catalogoProforma.getPeriodoCatalogo().getAnio());
                desactivarAprobado(catalogoProforma.getPeriodoCatalogo().getAnio());
                // updateTotal();
                catalogoProforma = new CatalogoProformaPresupuesto();
                proformaPresupuestoActualizar = new CatalogoProformaPresupuesto();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void updateTotal() {
        total = BigDecimal.ZERO;
        total = proformaService.getTotalPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio());
    }

    public void actualizarTable() {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            setDisabled(Boolean.FALSE);
            resetValuesCatalogoProforma();
            return;
        }
        if (catalogoProforma != null) {
            if (catalogoProforma.getPeriodoCatalogo() != null) {
                proformaPresupuestoActualizar = proformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), true);
                if (proformaPresupuestoActualizar != null) {
                    catalogoProforma.setDescripcion(proformaPresupuestoActualizar.getDescripcion());
                    catalogoProAdicional.setId(proformaPresupuestoActualizar.getId());
                } else {
                    catalogoProAdicional = new CatalogoProformaPresupuesto();
                    catalogoProforma.setDescripcion("");
                }
//                listClasificacion = catalogoPresupuestoService.filtroClasificacionCatPre(catalogoProforma.getPeriodoCatalogo().getAnio());
//                listNivel = catalogoPresupuestoService.filtroNivelOrden(catalogoProforma.getPeriodoCatalogo().getAnio());
//                listFuenteF = catalogoPresupuestoService.filtroFuenteFinanciamiento(catalogoProforma.getPeriodoCatalogo().getAnio());
//                listaCodigos = catalogoPresupuestoService.filtroCodigoC(catalogoProforma.getPeriodoCatalogo().getAnio());
                listaFuenteNew = fuenteServiceNew.getFuenteFinanciamiento();

            } else {
                setDisabled(Boolean.FALSE);
                resetValuesCatalogoProforma();
                return;
            }
            param = new HashMap<>();
            param.put("anio", catalogoProforma.getPeriodoCatalogo().getAnio());

            services.executeFunction("presupuesto.ftc_generando_proforma_ingreso", Arrays.asList(catalogoProforma.getPeriodoCatalogo().getAnio()), true);

            lazyProforma = new LazyModel<>(ProformaIngreso.class);
            lazyProforma.getFilterss().put("periodo", catalogoProforma.getPeriodoCatalogo().getAnio());
            lazyProforma.getSorteds().put("item.codigo", "ASC");
            lazyProforma.setDistinct(false);
            if (proformaPresupuestoActualizar != null) {
                if (proformaPresupuestoActualizar.getAprobado().equals(true)) {
                    setDisabled(Boolean.FALSE);
                    setAprobado("Aprobado");
                } else {
                    setDisabled(Boolean.TRUE);
                    setAprobado("No Aprobado");
                }
            } else {
                setDisabled(Boolean.TRUE);
                resetValuesCatalogoProforma();
                setAprobado("No Aprobado");
            }
        } else {

            lazyProforma = null;
        }
        updateTotal();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void imprimirResumenIngresos(boolean excel) {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Aviso", "Escoja un año para proceder");
            return;
        }
        proformaPresupuestoActualizar = proformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), true);
        if (proformaPresupuestoActualizar == null) {
            JsfUtil.addWarningMessage("Aviso", "Para este informe necesita registrar la proforma de ingreso (año a convenir)");
            return;
        }
        ss.addParametro("anio", catalogoProforma.getPeriodoCatalogo().getAnio());
        ss.addParametro("id", catalogoProAdicional.getId());
        if (excel) {
            ss.setContentType("xlsx");
            ss.setNombreReporte("resumenProformaIngresos");
        } else {
            ss.setNombreReporte("resumenProformaIngresos");
        }
//        ss.addParametro("NOMBRE_REPORTE", "PROFORMA INGRESOS");
        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
        ss.setNombreSubCarpeta("presupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

    }

    public void resetValuesCatalogoProforma() {
        proformaPresupuestoActualizar = new CatalogoProformaPresupuesto();
        catalogoProforma.setDescripcion("");
        setTotal(BigDecimal.ZERO);
        setAprobado("");
    }

    public void buscar() {
        lazy = new LazyModel(CatalogoPresupuesto.class);
        lazy.getFilterss().put("anio", catalogoProforma.getPeriodoCatalogo().getAnio());
    }

    public void cancelar() {

        lazy = new LazyModel(CatalogoPresupuesto.class);
        lazy.getFilterss().put("anio", catalogoProforma.getPeriodoCatalogo().getAnio());
    }

    public void cancelarProforma() {
        presupuestoIngresoLazy = new CatalogoPresupuestoLazy(catalogoProforma);
//        resetValuesCatalogoProforma();
    }

    /**
     * este metodo nos dirve para poder
     *
     * @param c
     * @param edit
     */
    public void form(CatalogoPresupuesto c, boolean edit) {
        lista.clear();
        clasificaciones = null;
        if (c != null) {
            if (edit) {
                List<CatalogoPresupuesto> hijos = catalogoPresupuestoService.getHijosByPadre(c);

                if (hijos != null) {
                    if (!hijos.isEmpty()) {
                        setEtica(Boolean.TRUE);
                        setAccionNaturaleza(Boolean.TRUE);
                        setAccionSubgrupo(Boolean.TRUE);
                        setAccionRubro(Boolean.TRUE);
                    } else {
                        setEtica(Boolean.FALSE);
                        setAccionNaturaleza(Boolean.FALSE);
                        setAccionSubgrupo(Boolean.FALSE);
                        setAccionRubro(Boolean.FALSE);
                    }
                }
                cuenta = c;
                if (cuenta.getFlujoIngreso()) {
                    this.clasificaciones = catalogoService.getClasificacionPresupuestoItem("tipo_cuenta", "I");
                } else {
                    this.clasificaciones = catalogoService.getClasificacionPresupuestoItem("tipo_cuenta", "E");
                }
            } else {
                setEtica(Boolean.FALSE);
                setAccionNaturaleza(Boolean.FALSE);
                setAccionSubgrupo(Boolean.FALSE);
                setAccionRubro(Boolean.FALSE);
                cuenta = new CatalogoPresupuesto();
                cuenta.setPadre(c);
                cuenta.setNivel(nivelService.getProximoNivel(c.getNivel()));
                cuenta.setAnio(c.getAnio());
                cuenta.setCuentaMovimiento(c.getCuentaMovimiento());
                cuenta.setClasificacion(c.getClasificacion());
                cuenta.setFlujoIngreso(c.getFlujoIngreso());
                lista.clear();
                if (cuenta.getFlujoIngreso()) {
                    this.clasificaciones = catalogoService.getClasificacionPresupuestoItem("tipo_cuenta", "I");
                } else {
                    this.clasificaciones = catalogoService.getClasificacionPresupuestoItem("tipo_cuenta", "E");
                }
                switch (c.getNivel().getOrden()) {
                    case 1:
                        cuenta.setTitulo(c.getTitulo());
                        cuenta.setNaturaleza(catalogoPresupuestoService.getMaxValueForChild(c, false));
                        break;
                    case 2:
                        cuenta.setTitulo(c.getTitulo());
                        cuenta.setNaturaleza(c.getNaturaleza());
                        cuenta.setSubGrupo(catalogoPresupuestoService.getMaxValueForChild(c, false));
                        break;
                    case 3:
                        cuenta.setTitulo(c.getTitulo());
                        cuenta.setNaturaleza(c.getNaturaleza());
                        cuenta.setSubGrupo(c.getSubGrupo());
                        cuenta.setRubro(catalogoPresupuestoService.getMaxValueForChild(c, false));
                        break;

                }
            }
        }
        actulizarInfoGridFom();
//        Map<String, Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("width", 1200);
//        options.put("height", 540);
//        options.put("contentWidth", "100%");
//        options.put("contentHeight", "100%");
//        options.put("headerElement", "customheader");
//        PrimeFaces.current().dialog().openDynamic("/common/Dialog/dlgcatalogoPresupuesto",options,null);
        PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
        PrimeFaces.current().ajax().update(":formCuenta");
    }

    public Boolean disabedFlujo(CatalogoPresupuesto edit) {
        if (edit.getId() != null || edit.getPadre() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void formAdd() {
        clasificaciones = null;
        cuenta = new CatalogoPresupuesto();
        cuenta.setNivel(nivelService.getFirstNivel("tipo_cuenta", "CP"));
        cuenta.setTitulo(catalogoPresupuestoService.getMaxValueForChild(cuenta, true));
        if (catalogoProforma != null || cuenta != null) {
            if (catalogoProforma.getPeriodoCatalogo() != null || cuenta.getAnio() != null) {
                cuenta.setAnio(catalogoProforma.getPeriodoCatalogo().getAnio());
            } else {
                cuenta.setAnio(opcionBusqueda.getAnio());
            }
            actulizarInfoGridFom();
            PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
            PrimeFaces.current().ajax().update("formCuenta");
        }

    }
    //metodo para actualizar en el formulario el tipo de cuenta
    //si el flujo es de ingreso se carga en la clasificacion solo los de ingreso

    public void actualizarClasificacion() {
        if (cuenta.getFlujoIngreso() != null) {
            if (cuenta.getFlujoIngreso()) {
                this.clasificaciones = catalogoService.getClasificacionPresupuestoItem("tipo_cuenta", "I");
            } else {
                this.clasificaciones = catalogoService.getClasificacionPresupuestoItem("tipo_cuenta", "E");
            }
        }
    }

    public void resetValues() {
        cuenta = new CatalogoPresupuesto();
        cuenta.setFlujoIngreso(null);
        clasificaciones = new ArrayList<>();
    }

    public Boolean renderedAsociacion(CatalogoPresupuesto item) {
        return item.getNivel().getOrden() != 4;
    }

    public void guardar() {
        try {

            boolean edit = cuenta.getId() != null;
            cuenta.setEstado(Boolean.TRUE);
            cuenta.setCodigo(generarCodigo(cuenta));
            CatalogoPresupuesto existeCuenta = catalogoPresupuestoService.existeCuenta(cuenta);

            if (cuenta.getCuentaMovimiento() == false) {
                String toUpperCase = cuenta.getDescripcion().toUpperCase();
                cuenta.setDescripcion(toUpperCase);
            }

            if (cuenta.getId() == null) {
                if (existeCuenta == null) {
                    cuenta.setFechaCreacion(new Date());
                    cuenta.setUsuarioCreacion(userSession.getNameUser());
                    cuenta.setPresupuestoInicial(BigDecimal.ZERO);
                    cuenta.setReformaSuplementaria(BigDecimal.ZERO);
                    cuenta.setReformaReduccion(BigDecimal.ZERO);
                    cuenta.setPresupuestoCodificado(BigDecimal.ZERO);
                    cuentaSeleccionada = catalogoPresupuestoService.create(cuenta);
                    clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");

                } else {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("Cuenta Presupuesto", cuenta.getCodigo() + " ya se ecunetra registrada");
                    return;
                }
            } else {

                cuenta.setFechaModificacion(new Date());
                cuenta.setUsuarioModifica(userSession.getNameUser());
                catalogoPresupuestoService.edit(cuenta);
                clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");
            }
            PrimeFaces.current().executeScript("PF('cuentaDialog').hide()");
            PrimeFaces.current().ajax().update(":cuentas");
            PrimeFaces.current().ajax().update(":formMain");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Cuenta Presupuesto", cuenta.getCodigo() + (edit ? " editada" : " registrada") + " con éxito.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");
    }

    public void eliminar(CatalogoPresupuesto c) {

        List<CatalogoPresupuesto> hijos = catalogoPresupuestoService.getHijosByPadre(c);

        if (hijos != null) {
            if (!hijos.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Cuenta Presupuesto", c.getCodigo() + " tiene cuentas de movimientos asociadas.");
                return;
            }
        }
        JsfUtil.addSuccessMessage("Cuenta Presupuesto", c.getCodigo() + " eliminada con éxito");
        c.setEstado(Boolean.FALSE);
        catalogoPresupuestoService.edit(c);
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("messages");
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("mostrarColumnas");
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("formCuenta");
        clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");
    }

    private void actulizarInfoGridFom() {

        switch (cuenta.getNivel().getOrden()) {
            case 1:
            case 4:
            case 6:
                cantColumnas = 3;
                columnClass = "ui-grid-col-4";
                break;
            case 2:
            case 3:
            case 5:
            case 7:
                cantColumnas = 4;
                columnClass = "ui-grid-col-3";
                break;
        }

    }

    private String generarCodigo(CatalogoPresupuesto c) {

        if (c.getPadre() != null) {
            return c.getPadre().getCodigo() + getSubCodigo(c);
        }

        return getSubCodigo(c);
    }

    private String getSubCodigo(CatalogoPresupuesto c) {

        String format = "%0" + c.getNivel().getLongitud() + "d";
        switch (c.getNivel().getOrden()) {

            case 1: {
                return String.format(format, c.getTitulo());
            }
            case 2: {
                return String.format(format, c.getNaturaleza());
            }
            case 3: {
                return String.format(format, c.getSubGrupo());
            }
            case 4: {
                return String.format(format, c.getRubro());
            }

        }
        return "";
    }

    public boolean renderElementForm(CatalogoPresupuesto cc, int position) {

        if (cc.getNivel() != null) {
            return position <= cc.getNivel().getOrden();
        }
        return false;
    }

    public boolean readOnlyElementForm(CatalogoPresupuesto cc, int position) {

        return position != cc.getNivel().getOrden();

    }

    //METODOS DE PRESUPUESTO INGRESO
    public void guardarPresupuestoIngreso(ProformaIngreso proforma) {
        try {
            proformaIngreso = new ProformaIngreso();
            if (proforma.getId() != null) {
                proforma.setFechaModificacion(new Date());
                proforma.setUsuarioModificacion(userSession.getNameUser());
                if (proforma.getFuente() == null) {
                    proforma.setPresupuestoInicial(BigDecimal.ZERO);
                    proforma.setReformaReduccion(BigDecimal.ZERO);
                    proforma.setReformaSuplementaria(BigDecimal.ZERO);
                    proforma.setPresupuestoCodificado(BigDecimal.ZERO);
                }
                proforma.setPresupuestoInicial(proforma.getPresupuestoInicial());
                proforma.setPresupuestoCodificado(proforma.getPresupuestoInicial());
//
                 proformaIngresoService.edit(proforma);
                proformaIngreso = proforma;
                proformaIngresoService.actualizandoSuma(proformaIngreso,catalogoProforma.getPeriodoCatalogo().getAnio());
            } else {
                proformaIngreso = new ProformaIngreso();
            }

            updateTotal();
            PrimeFaces.current().ajax().update("cuentas");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, aprobado, ex);
        }
    }

    public void generarProforma() {
        try {
            //consulta si existe un catalogoProformaPresupuesto
            if (catalogoProforma != null) {
                if (catalogoProforma.getPeriodoCatalogo() != null && !catalogoProforma.getDescripcion().isEmpty()) {
                    CatalogoProformaPresupuesto result = proformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), true);
                    if (result != null) {
                        result.setDescripcion(catalogoProforma.getDescripcion());
                        updateTotal();
                        setTotalIngreso(total.doubleValue());
                        result.setTotal((BigDecimal.valueOf(getTotalIngreso())));
                        proformaService.edit(result);
                        JsfUtil.addInformationMessage("Información", "La Proforma se Editado Correctamente");
                    } else {
                        crearProforma(catalogoProforma);
                        executeMessagesProforma(catalogoProforma, " Proforma del Período ", false);
                        resetValuesProforma();
                    }
                } else {
                    executeMessagesProforma(null, "Ingrese Datos Requeridos", true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearProforma(CatalogoProformaPresupuesto proforma) {
        proforma.setUsuarioCreacion(userSession.getNameUser());
        proforma.setFechaCreacion(new Date());
        proforma.setAprobado(Boolean.FALSE);
        proforma.setTipoFlujo(Boolean.TRUE);
        proforma.setEstado(Boolean.TRUE);
        proforma.setPeriodo(proforma.getPeriodoCatalogo().getAnio());

        setTotalIngreso(catalogoPresupuestoService.getPresupuestoTotal(catalogoProforma, true).doubleValue());
        proforma.setTotal((BigDecimal.valueOf(getTotalIngreso())));

        catalogoProforma = proformaService.create(proforma);
    }

    public void executeMessagesProforma(CatalogoProformaPresupuesto proforma, String messages, Boolean error) {
        if (proforma != null) {
            if (error) {
                JsfUtil.addErrorMessage("Catalogo Presupuesto", messages + proforma.getPeriodoCatalogo().getAnio() + " registrado.");
            } else {
                JsfUtil.addSuccessMessage("Catalogo Presupuesto", messages + proforma.getPeriodoCatalogo().getAnio() + " registrado.");
            }
        } else {
            JsfUtil.addErrorMessage("Catalogo Presupuesto", messages);
        }

        PrimeFaces.current().ajax().update("messages");
    }

    public void resetValuesProforma() {
        catalogoProforma = new CatalogoProformaPresupuesto();
        catalogoProforma.setDescripcion("");
        catalogoProforma.setPeriodoCatalogo(null);
    }

    public Boolean disabledCellEdit(ProformaIngreso cp) {

        if (proformaPresupuestoActualizar.getId() == null) {
            return true;
        } else {
            if (proformaPresupuestoActualizar.getAprobado().equals(true)) {
                return true;
            }
            return cp.getItem().getMovimiento() == null ? false : (!cp.getItem().getMovimiento());
        }
    }

    /**
     * habilita y desabilita los botones de acciones si el presupuesto es
     * aprobado recibe un catalogo presupuesto para poder saber el año es ese
     * catalogo presupuesto y preguntar si es aprobado o no
     *
     * @return
     */
    public Boolean disableAcciones() {
        if (proformaPresupuestoActualizar == null) {
            return true;
        } else {
            if (proformaPresupuestoActualizar.getAprobado() != null) {
                return proformaPresupuestoActualizar.getAprobado().equals(true);
            } else {
                return true;
            }
        }
    }

    public void imprimirRep(boolean excel) {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Aviso", "Escoja un año para proceder");
            return;
        }

        proformaPresupuestoActualizar = proformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), true);
        if (proformaPresupuestoActualizar == null) {
            JsfUtil.addWarningMessage("Aviso", "Para este informe necesita registrar la proforma de ingreso (año a convenir)");
            return;
        }
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
            return;
        } else if (lazyProforma.getRowCount() == 0) {
            JsfUtil.addWarningMessage("Información", "No existe información que imprimir");
            return;
        } else {

            if ((codigoC.getCodigo() != null)) {

                ss.addParametro("cod", codigoC.getCodigo() + "%");
            }
            if ((catItem.getClasificacion() != null)) {

                ss.addParametro("cl", catItem.getClasificacion().getTexto());
            }
            if ((nivelN.getNivel() != null)) {

                ss.addParametro("nii", nivelN.getNivel().getOrden());
            }
            if ((fuenteF.getFuente() != null)) {

                ss.addParametro("fuente", fuenteF.getFuente().getNombre());
            }
            if ((adicionalString != null)) {
//                ss.addParametro("tipo", LOG);
            }
            ss.addParametro("anio", catalogoProforma.getPeriodoCatalogo().getAnio());
            if ((catalogoProAdicional.getId() != null)) {

                ss.addParametro("idPro", catalogoProAdicional.getId());
                if (excel) {
                    ss.setContentType("xlsx");
                    ss.setNombreReporte("proformaPresupuestoIngresoExcel");
                } else {
                    ss.setNombreReporte("proformaPresupuestoIngresoPdfNuevo");
                }
            } else {
                if (excel) {
                    ss.setContentType("xlsx");
                    ss.setNombreReporte("proformaPresupuestoIngresoNoAprobadoExcel");
                } else {
                    ss.setNombreReporte("proformaPresupuestoIngresoNoAprobadoPdf");
                }
            }
            ss.setNombreSubCarpeta("presupuesto");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:cuentas");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formMain:cuentas");
        }
    }

    public void desactivarAprobado(Short periodo) {

        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, true, true);

        if (li.size() > 0) {
            bloqueabtn = false;
        } else {
            bloqueabtn = true;
        }

    }

    public void dlgoObservaciones() {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Eliga un Período");
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
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));//PRESUPUESTO

            if (saveTramite() == null) {
                return;
            }
            generarProforma();
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

//<editor-fold defaultstate="collapsed" desc="getter and setter">
    public boolean isBloqueabtn() {
        return bloqueabtn;
    }

    public void setBloqueabtn(boolean bloqueabtn) {
        this.bloqueabtn = bloqueabtn;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<CuentaContable> getLista() {
        return lista;
    }

    public void setLista(List<CuentaContable> lista) {
        this.lista = lista;
    }

    public List<CuentaContable> getCreditos() {
        return creditos;
    }

    public void setCreditos(List<CuentaContable> creditos) {
        this.creditos = creditos;
    }

    public List<CuentaContable> getDebitos() {
        return debitos;
    }

    public void setDebitos(List<CuentaContable> debitos) {
        this.debitos = debitos;
    }

    public short getNewPeriodo() {
        return newPeriodo;
    }

    public void setNewPeriodo(short newPeriodo) {
        this.newPeriodo = newPeriodo;
    }

    public LazyModel<CatalogoPresupuesto> getPresupuestoIngresoLazy() {
        return presupuestoIngresoLazy;
    }

    public void setPresupuestoIngresoLazy(CatalogoPresupuestoLazy presupuestoIngresoLazy) {
        this.presupuestoIngresoLazy = presupuestoIngresoLazy;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public CatalogoPresupuesto getCuenta() {
        return cuenta;
    }

    public void setCuenta(CatalogoPresupuesto cuenta) {
        this.cuenta = cuenta;
    }

    public CatalogoPresupuesto getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(CatalogoPresupuesto cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<CatalogoItem> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public int getCantColumnas() {
        return cantColumnas;
    }

    public void setCantColumnas(int cantColumnas) {
        this.cantColumnas = cantColumnas;
    }

    public String getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public boolean isEtica() {
        return etica;
    }

    public void setEtica(boolean etica) {
        this.etica = etica;
    }

    public boolean isAccionNaturaleza() {
        return accionNaturaleza;
    }

    public void setAccionNaturaleza(boolean accionNaturaleza) {
        this.accionNaturaleza = accionNaturaleza;
    }

    public boolean isAccionSubgrupo() {
        return accionSubgrupo;
    }

    public void setAccionSubgrupo(boolean accionSubgrupo) {
        this.accionSubgrupo = accionSubgrupo;
    }

    public boolean isAccionRubro() {
        return accionRubro;
    }

    public void setAccionRubro(boolean accionRubro) {
        this.accionRubro = accionRubro;
    }

    public List<FuenteFinanciamiento> getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(List<FuenteFinanciamiento> tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public double getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(double toatalIngreso) {
        this.totalIngreso = toatalIngreso;
    }

    public CatalogoProformaPresupuesto getCatalogoProforma() {
        return catalogoProforma;
    }

    public void setCatalogoProforma(CatalogoProformaPresupuesto catalogoProforma) {
        this.catalogoProforma = catalogoProforma;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public CatalogoProformaPresupuesto getProformaPresupuestoActualizar() {
        return proformaPresupuestoActualizar;
    }

    public void setProformaPresupuestoActualizar(CatalogoProformaPresupuesto proformaPresupuestoActualizar) {
        this.proformaPresupuestoActualizar = proformaPresupuestoActualizar;
    }

    public List<MasterCatalogo> getMaxPeriodos() {
        return maxPeriodos;
    }

    public void setMaxPeriodos(List<MasterCatalogo> maxPeriodos) {
        this.maxPeriodos = maxPeriodos;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public CatalogoPresupuesto getTipoCtaMovimiento() {
        return tipoCtaMovimiento;
    }

    public void setTipoCtaMovimiento(CatalogoPresupuesto tipoCtaMovimiento) {
        this.tipoCtaMovimiento = tipoCtaMovimiento;
    }

    public List<CatalogoItem> getListClasificacion() {
        return listClasificacion;
    }

    public void setListClasificacion(List<CatalogoItem> listClasificacion) {
        this.listClasificacion = listClasificacion;
    }

    public CatalogoPresupuesto getCatItem() {
        return catItem;
    }

    public void setCatItem(CatalogoPresupuesto catItem) {
        this.catItem = catItem;
    }

    public List<Nivel> getListNivel() {
        return listNivel;
    }

    public void setListNivel(List<Nivel> listNivel) {
        this.listNivel = listNivel;
    }

    public CatalogoPresupuesto getNivelN() {
        return nivelN;
    }

    public void setNivelN(CatalogoPresupuesto nivelN) {
        this.nivelN = nivelN;
    }

    public List<FuenteFinanciamiento> getListFuenteF() {
        return listFuenteF;
    }

    public void setListFuenteF(List<FuenteFinanciamiento> listFuenteF) {
        this.listFuenteF = listFuenteF;
    }

    public CatalogoPresupuesto getFuenteF() {
        return fuenteF;
    }

    public void setFuenteF(CatalogoPresupuesto fuenteF) {
        this.fuenteF = fuenteF;
    }

    public List<String> getListaCodigos() {
        return listaCodigos;
    }

    public void setListaCodigos(List<String> listaCodigos) {
        this.listaCodigos = listaCodigos;
    }

    public CatalogoPresupuesto getCodigoC() {
        return codigoC;
    }

    public void setCodigoC(CatalogoPresupuesto codigoC) {
        this.codigoC = codigoC;
    }

    public CatalogoProformaPresupuesto getCatalogoProAdicional() {
        return catalogoProAdicional;
    }

    public void setCatalogoProAdicional(CatalogoProformaPresupuesto catalogoProAdicional) {
        this.catalogoProAdicional = catalogoProAdicional;
    }

    public LazyModel<CatalogoPresupuesto> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<CatalogoPresupuesto> lazy) {
        this.lazy = lazy;
    }

    public String getAdicionalString() {
        return adicionalString;
    }

    public void setAdicionalString(String adicionalString) {
        this.adicionalString = adicionalString;
    }
//</editor-fold>

    public LazyModel<ProformaIngreso> getLazyProforma() {
        return lazyProforma;
    }

    public void setLazyProforma(LazyModel<ProformaIngreso> lazyProforma) {
        this.lazyProforma = lazyProforma;
    }

    public List<PresFuenteFinanciamiento> getListaFuenteNew() {
        return listaFuenteNew;
    }

    public void setListaFuenteNew(List<PresFuenteFinanciamiento> listaFuenteNew) {
        this.listaFuenteNew = listaFuenteNew;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public ProformaIngreso getProformaIngreso() {
        return proformaIngreso;
    }

    public void setProformaIngreso(ProformaIngreso proformaIngreso) {
        this.proformaIngreso = proformaIngreso;
    }

    public ProformaIngreso getProformaSeleccionada() {
        return proformaSeleccionada;
    }

    public void setProformaSeleccionada(ProformaIngreso proformaSeleccionada) {
        this.proformaSeleccionada = proformaSeleccionada;
    }

}
