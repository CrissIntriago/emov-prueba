/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @author ORIGAMIEC
 */
@Named(value = "presupuestoView")
@ViewScoped
public class CatalogoPresupuestoController implements Serializable {

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

    private OpcionBusqueda opcionBusqueda;
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

    @PostConstruct
    public void init() {
        lista = new ArrayList<>();
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
        actualizarTable();
        opcionBusqueda = new OpcionBusqueda();
        lazy = new LazyModel(CatalogoPresupuesto.class);
        lazy.getSorteds().put("codigo", "ASC");
        lazy.getFilterss().put("anio:equal", opcionBusqueda.getAnio());
        lazy.getFilterss().put("estado", true);
        niveles = nivelService.findByNamedQuery("Nivel.findByCatalogoAndCodigo", new Object[]{"tipo_cuenta", "CP"});
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        cuenta = new CatalogoPresupuesto();
        clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");
        cantColumnas = 4;
        columnClass = "ui-grid-col-3";
        //   presupuestoIngresoLazy = new CatalogoPresupuestoLazy(catalogoProforma);
        tipoFuente = fuenteService.findAll();
        disabled = false;
        creditos = catalogoPresupuestoService.getAsociacionPresupuestaria(opcionBusqueda.getAnio());
        debitos = catalogoPresupuestoService.getAsociacionPresupuestaria(opcionBusqueda.getAnio());
        desactivarAprobado(opcionBusqueda.getAnio());
        updateTotal();

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
                listClasificacion = catalogoPresupuestoService.filtroClasificacionCatPre(catalogoProforma.getPeriodoCatalogo().getAnio());
                listNivel = catalogoPresupuestoService.filtroNivelOrden(catalogoProforma.getPeriodoCatalogo().getAnio());
                listFuenteF = catalogoPresupuestoService.filtroFuenteFinanciamiento(catalogoProforma.getPeriodoCatalogo().getAnio());
                listaCodigos = catalogoPresupuestoService.filtroCodigoC(catalogoProforma.getPeriodoCatalogo().getAnio());

            } else {
                setDisabled(Boolean.FALSE);
                resetValuesCatalogoProforma();
                return;
            }
            presupuestoIngresoLazy = new CatalogoPresupuestoLazy(catalogoProforma);
            if (proformaPresupuestoActualizar != null) {
                if (proformaPresupuestoActualizar.getAprobado().equals(true)) {
                    setDisabled(Boolean.FALSE);
                    setAprobado("Aprobado");
                } else {
                    setDisabled(Boolean.TRUE);
                    setAprobado("No Aprobado");
                }
            } else {
                setDisabled(Boolean.FALSE);
                resetValuesCatalogoProforma();
                setAprobado("No Aprobado");
            }
        }
        updateTotal();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void resetValuesCatalogoProforma() {
        proformaPresupuestoActualizar = new CatalogoProformaPresupuesto();
        setAprobado("");
    }

    public void buscar() {
        lazy = new LazyModel<>(CatalogoPresupuesto.class);
        lazy.getSorteds().put("codigo", "ASC");
        lazy.getFilterss().put("anio", opcionBusqueda.getAnio());
        lazy.getFilterss().put("estado", true);
        if (opcionBusqueda.getTitulo() != null) {
            lazy.getFilterss().put("titulo", opcionBusqueda.getTitulo());
        }
        if (opcionBusqueda.getGrupo() != null) {
            lazy.getFilterss().put("naturaleza", opcionBusqueda.getGrupo());
        }
        if (opcionBusqueda.getSubGrupo() != null) {
            lazy.getFilterss().put("subGrupo", opcionBusqueda.getSubGrupo());
        }
        if (opcionBusqueda.getNivel1() != null) {
            lazy.getFilterss().put("rubro", opcionBusqueda.getNivel1());
        }
        PrimeFaces.current().ajax().update("cuentas");
    }

    public void cancelar() {
        opcionBusqueda = new OpcionBusqueda();
        lazy = new LazyModel<>(CatalogoPresupuesto.class);
        lazy.getFilterss().put("anio", opcionBusqueda.getAnio());
        lazy.getSorteds().put("codigo", "ASC");
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
                    default:
                        JsfUtil.addWarningMessage("AVISO", "YA NO SE PUEDE AGREGAR CUENTAS MAYORES AL NIVEL 4f");
                        break;
                }
            }
        }
        if (cuenta.getNivel() == null) {
            return;
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
        clasificaciones = catalogoService.getClasificacionPresupuesto("tipo_cuenta");
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
            JsfUtil.addSuccessMessage("Cuenta Presupuesto", cuenta.getCodigo() + (edit ? " editada" : " registrada") + " con ??xito.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar", e);
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
        JsfUtil.addSuccessMessage("Cuenta Presupuesto", c.getCodigo() + " eliminada con ??xito");
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
        if (cuenta.getNivel() != null) {
            switch (cuenta.getNivel().getOrden()) {
                case 1:
                case 4:

                    cantColumnas = 3;
                    columnClass = "ui-grid-col-4";
                    break;
                case 2:
                case 3:

                    cantColumnas = 4;
                    columnClass = "ui-grid-col-3";
                    break;
            }
        }
    }

    private String generarCodigo(CatalogoPresupuesto c) {
        if (c.getPadre() != null) {
            return c.getPadre().getCodigo() + getSubCodigo(c);
        }
        return getSubCodigo(c);
    }

    private String getSubCodigo(CatalogoPresupuesto c) {
        if (c.getNivel() != null) {
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
    public void guardarPresupuestoIngreso(CatalogoPresupuesto plan) {
        try {
            if (plan != null) {
                plan.setFechaModificacion(new Date());
                plan.setUsuarioModifica(userSession.getNameUser());
                if (plan.getFuente() == null) {
                    plan.setPresupuestoInicial(BigDecimal.ZERO);
                    plan.setReformaReduccion(BigDecimal.ZERO);
                    plan.setReformaSuplementaria(BigDecimal.ZERO);
                    plan.setPresupuestoCodificado(BigDecimal.ZERO);
                }
                plan.setReformaReduccion(BigDecimal.ZERO);
                plan.setReformaSuplementaria(BigDecimal.ZERO);
                plan.setPresupuestoCodificado(plan.getPresupuestoInicial());
                catalogoPresupuestoService.edit(plan);
                generarSuma(plan);
            } else {
                cuenta = new CatalogoPresupuesto();
            }
            PrimeFaces.current().ajax().update("cuentas");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, aprobado, ex);
        }
    }

    public void generarSuma(CatalogoPresupuesto plan) {
        try {
            BigDecimal suma = catalogoPresupuestoService.getPresupuestoInicial(plan);
            CatalogoPresupuesto padre = catalogoPresupuestoService.getPadre(plan);
            if (padre != null) {
                padre.setPresupuestoInicial(suma);
                padre.setReformaSuplementaria(BigDecimal.ZERO);
                padre.setReformaReduccion(BigDecimal.ZERO);
                padre.setPresupuestoCodificado(padre.getPresupuestoInicial());
                catalogoPresupuestoService.edit(padre);
                BigDecimal sumaPadres = catalogoPresupuestoService.getPresupuestoInicial(padre);
                CatalogoPresupuesto padrePadre = catalogoPresupuestoService.getPadre(padre);
                if (padrePadre != null) {
                    padrePadre.setPresupuestoInicial(sumaPadres);
                    padrePadre.setReformaSuplementaria(BigDecimal.ZERO);
                    padrePadre.setReformaReduccion(BigDecimal.ZERO);
                    padrePadre.setPresupuestoCodificado(padrePadre.getPresupuestoInicial());
                    catalogoPresupuestoService.edit(padrePadre);
                    BigDecimal sumaPadresPadres = catalogoPresupuestoService.getPresupuestoInicial(padrePadre);
                    CatalogoPresupuesto padrePadrePadre = catalogoPresupuestoService.getPadre(padrePadre);
                    if (padrePadrePadre != null) {
                        padrePadrePadre.setPresupuestoInicial(sumaPadresPadres);
                        padrePadrePadre.setReformaSuplementaria(BigDecimal.ZERO);
                        padrePadrePadre.setReformaReduccion(BigDecimal.ZERO);
                        padrePadrePadre.setPresupuestoCodificado(padrePadrePadre.getPresupuestoInicial());
                        catalogoPresupuestoService.edit(padrePadrePadre);
                    }
                }
            }
            setTotalIngreso(catalogoPresupuestoService.getPresupuestoTotal(proformaPresupuestoActualizar, true).doubleValue());
            proformaPresupuestoActualizar.setTotal((BigDecimal.valueOf(getTotalIngreso())));
            proformaPresupuestoActualizar.setUsuarioModifica(userSession.getNameUser());
            proformaPresupuestoActualizar.setFechaModificacion(new Date());
            proformaService.edit(proformaPresupuestoActualizar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarProforma() {
        try {
            //consulta si existe un catalogoProformaPresupuesto
            if (catalogoProforma != null) {
                if (catalogoProforma.getPeriodoCatalogo() != null && !catalogoProforma.getDescripcion().isEmpty()) {
                    CatalogoProformaPresupuesto result = proformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), true);
                    if (result != null) {
                        executeMessagesProforma(catalogoProforma, " Ya existe un Catalogo de Presupuesto del Per??odo ", true);
                    } else {
                        crearProforma(catalogoProforma);
                        executeMessagesProforma(catalogoProforma, " Proforma del Per??odo ", false);
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

    public Boolean disabledCellEdit(CatalogoPresupuesto cp) {
        if (proformaPresupuestoActualizar.getId() == null) {
            return true;
        } else {
            if (proformaPresupuestoActualizar.getAprobado().equals(true)) {
                return true;
            }
            return cp.getCuentaMovimiento() == false;
        }
    }

    /**
     * habilita y desabilita los botones de acciones si el presupuesto es
     * aprobado recibe un catalogo presupuesto para poder saber el a??o es ese
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
            JsfUtil.addWarningMessage("Informaci??n", "Escoja un a??o para proceder");
            return;
        } else if (presupuestoIngresoLazy.getRowCount() == 0) {
            JsfUtil.addWarningMessage("Informaci??n", "No existe informaci??n que imprimir");
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
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
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

}
