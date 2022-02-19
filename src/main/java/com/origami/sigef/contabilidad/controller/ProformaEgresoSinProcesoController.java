/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaDTO;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "proformaEgresosSinProcesoView")
@ViewScoped
public class ProformaEgresoSinProcesoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;

    @Inject
    private ProductoService productoService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private PlanProgramaticoService estrucPlanProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPrespuestoService;
    @Inject
    private ActividadOperativaService actividadOperativaService;
    @Inject
    private ServletSession ss;
    @Inject
    private PartidaDistributivoService partidaDistributivoService;
    @Inject
    private UserSession user;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuesto;
    private List<MasterCatalogo> periodos;

    private ProformaPresupuestoPlanificado proformaPresupuesto;
    private CatalogoProformaPresupuesto catalogoProforma;
    private CatalogoProformaPresupuesto catalogoProAdicional;
    private MasterCatalogo mc;
    private OpcionBusqueda opcionBusqueda;
    private Producto objProducto;
    private ProformaPresupuestoPlanificado pp;
    private ProformaPresupuestoPlanificado cp;
    private ProformaPresupuestoPlanificado codigoPro;
    private LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy;
    private List<ProformaPresupuestoPlanificado> listaproforma;
    private BigDecimal Total;
    private Boolean Aprobado;
    private List<ProformaDTO> proformaDTO;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;
    private boolean tabla;
    private boolean botonHabilitar;
    private List<PlanProgramatico> listaPlanProgramatico2;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos2;
    private Short fecha;
    private boolean bloqueo;
    private boolean ocultarAprobacion;
    private List<ProformaPDTO> listaProformaList;
    private List<String> listaCodigosRepetidos;
    private List<PlanProgramatico> filtroEstrcutrua;
    private List<CatalogoPresupuesto> filtroItem;
    private List<FuenteFinanciamiento> filtroFuente;
    private BigDecimal totalCupo, cupoAsigando;
    private String estadoGeneral;
    private String observaciones;
    //private Short periodoConsultado;
    //partida anexo
    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;

    //NUEVO
    @Inject
    private PresCatalogoPresupuestarioService catalogoServiceNew;
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private PresPlanProgramaticoService estructuraServiceNew;
    private List<PresPlanProgramatico> listEstructura;
    private List<PresCatalogoPresupuestario> listItem;
    private List<PresFuenteFinanciamiento> listFuente;

    @PostConstruct
    public void init() {

        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        Total = BigDecimal.ZERO;
        tabla = false;
        botonHabilitar = true;
        bloqueo = true;
        opcionBusqueda = new OpcionBusqueda();

        String anioActual = String.valueOf(año);
        // periodoConsultado=2020;
        //  periodoConsultado = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
        catalogoProAdicional = new CatalogoProformaPresupuesto();
        catalogoProforma = new CatalogoProformaPresupuesto();
        codigoPro = new ProformaPresupuestoPlanificado();
        pp = new ProformaPresupuestoPlanificado();
        cp = new ProformaPresupuestoPlanificado();
        mc = new MasterCatalogo();
        mc.setAnio(Utils.getAnio(new Date()).shortValue());
        catalogoProforma.setPeriodoCatalogo(masterCatalogoService.getMasterCatalogoActual(opcionBusqueda.getAnio()));
        proformaPresupuesto = new ProformaPresupuestoPlanificado();
        objProducto = new Producto();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        this.listaProformaPresupuesto = proformaPresupuestoPlanificadoService.findAll();
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
        this.ocultarAprobacion = false;
        totalCupo = BigDecimal.ZERO;
        cupoAsigando = BigDecimal.ZERO;
        estadoGeneral = "";
        catalogoProAdicional = new CatalogoProformaPresupuesto();
        tabla = true;
        //NUEVO

    }

    public void actualizarPresupuestoTotal() {
        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        String anio = String.valueOf(año);
        setTotal(proformaPresupuestoPlanificadoService.totalPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio()));
        //actualizaMontoProforma(getTotal().doubleValue());
    }

    public void actualizaMontoProforma(double total) {
        CatalogoProformaPresupuesto catalogoProformaEdit = new CatalogoProformaPresupuesto();
        catalogoProformaEdit = catalogoProformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), false);
        BigDecimal MontoTotal = new BigDecimal(total);
        catalogoProformaEdit.setTotal(MontoTotal);
        catalogoProformaService.edit(catalogoProformaEdit);
    }

    public void eliminarProforma(ProformaPresupuestoPlanificado p) {
        proformaPresupuestoPlanificadoService.remove(p);
        PrimeFaces.current().ajax().update("messages");

        JsfUtil.addInformationMessage("Información", p.getPartidaPresupuestaria() + " Elimnado con éxito");
        actualizarTabla();
    }

    public void desactivarAprobado(Short periodo) {
        boolean verificar = true;
        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, false, true);

        if (li.size() > 0) {
            verificar = false;
        } else {
            verificar = true;
        }
        setBloqueo(verificar);
    }

    public void dlgProformaPresup() {
        //updateCuposTotale();
        if (catalogoProforma.getPeriodoCatalogo() != null) {
            opcionBusqueda.setAnio(catalogoProforma.getPeriodoCatalogo().getAnio());
            listItem = new ArrayList<>();
            listItem = catalogoServiceNew.findTipoPresupuesto(false);
            listEstructura = new ArrayList<>();
            listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
            listFuente = new ArrayList<>();
            listFuente = fuenteServiceNew.getFuenteFinanciamiento();
            proformaPresupuesto = new ProformaPresupuestoPlanificado();
            PrimeFaces.current().executeScript("PF('DlgproformaRegistro').show()");
            PrimeFaces.current().ajax().update(":formPresegres");
        } else {
            JsfUtil.addWarningMessage("Advertencia", "Seleccione un período para poder añadir una partida");
            PrimeFaces.current().ajax().update(":formMain");
        }

    }

    public void edatarDlgProformaPresup(ProformaPresupuestoPlanificado p) {
        opcionBusqueda.setAnio(catalogoProforma.getPeriodoCatalogo().getAnio());
        listItem = new ArrayList<>();
        listItem = catalogoServiceNew.findTipoPresupuesto(false);
        listEstructura = new ArrayList<>();
        listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
        listFuente = new ArrayList<>();
        listFuente = fuenteServiceNew.getFuenteFinanciamiento();
        proformaPresupuesto = p;
        PrimeFaces.current().executeScript("PF('DlgproformaRegistro').show()");
        PrimeFaces.current().ajax().update(":formPresegres");

    }

    public void calcularEgresos() {
        listaCodigosRepetidos = new ArrayList<>();

        if (catalogoProforma.getPeriodoCatalogo() == null) {
            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addWarningMessage("Información", "Eliga un año");
        }
        List<Producto> listapro = new ArrayList<>();
        listapro = productoService.findByNamedQuery("Producto.findByPeriodoActual", catalogoProforma.getPeriodoCatalogo().getAnio());

        proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(catalogoProforma.getPeriodoCatalogo().getAnio(), "PAPP");

        if (catalogoProforma.getPeriodoCatalogo() == null) {

            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addWarningMessage("Información", "Eliga un año");
            return;
        }

        List<ProformaPDTO> lista = new ArrayList<>();
        lista = proformaPresupuestoPlanificadoService.muestrameEgresos(catalogoProforma.getPeriodoCatalogo().getAnio());

        if (lista == null) {
            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addWarningMessage("Proforma Presupuesto", " no hay plan programa proyecto en el año " + opcionBusqueda.getAnio());
            return;
        }

        List<ProformaPresupuestoPlanificado> istaProformaPresupuestoPlanificados = proformaPresupuestoPlanificadoService.verListaAnual(catalogoProforma.getPeriodoCatalogo().getAnio());

        for (ProformaPDTO item : lista) {

            for (ProformaPresupuestoPlanificado itemProforma : istaProformaPresupuestoPlanificados) {
                if (itemProforma.getPartidaPresupuestaria() == null ? item.getPartida() == null : itemProforma.getPartidaPresupuestaria().equals(item.getPartida())) {
                    listaCodigosRepetidos.add(item.getPartida());
                }
            }
        }

        if (!listaCodigosRepetidos.isEmpty() || listaCodigosRepetidos.size() > 0) {
            PrimeFaces.current().executeScript("PF('DlgCodigosRepetidos').show()");
            PrimeFaces.current().ajax().update(":formcodigoRep");

            return;
        }

        lista.forEach((ob) -> {
            proformaPresupuesto.setValor(ob.getTotal());
            proformaPresupuesto.setEstado(true);
            proformaPresupuesto.setPeriodo(opcionBusqueda.getAnio());
            proformaPresupuesto.setGenerado(false);
            proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
            proformaPresupuesto.setFechaCreacion(new Date());
            proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
            proformaPresupuesto.setFechaModificacion(new Date());
            proformaPresupuesto.setPartidaPresupuestaria(ob.getPartida());
            proformaPresupuesto.setItemNew(ob.getItemNew());
            proformaPresupuesto.setEstructruaNew(ob.getEstructuraNew());
            proformaPresupuesto.setFuenteNew(ob.getFuenteNew());
            proformaPresupuesto.setCodigoReforma(null);
            proformaPresupuesto.setCodigoReformaTraspaso(null);
            proformaPresupuesto.setCodigo("PAPP");
            proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());

            this.proformaPresupuesto = proformaPresupuestoPlanificadoService.create(proformaPresupuesto);
            proformaPresupuesto = new ProformaPresupuestoPlanificado();

        });

        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "PAPP añadido correctamente");

    }

    public void saveEgresos() {

        this.listaCodigosRepetidos = new ArrayList<>();
        BigDecimal valorAsignado = cupoPresupuestoService.getcupoAsignadoPD(catalogoProforma.getPeriodoCatalogo().getAnio());

        if (proformaPresupuesto.getEstructruaNew() == null && proformaPresupuesto.getFuenteNew() == null && proformaPresupuesto.getItemNew() == null
                && (proformaPresupuesto.getValor() == null || proformaPresupuesto.getValor() == BigDecimal.ZERO)) {
            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addErrorMessage("Información", "Los Campos no deben estar vacios");
            return;

        }
        boolean edit = proformaPresupuesto.getId() != null;
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addWarningMessage("Información", "Eliga un año");
            return;

        }

        if (!edit) {
            proformaPresupuesto.setPeriodo(catalogoProforma.getPeriodoCatalogo().getAnio());
            proformaPresupuesto.setPartidaPresupuestaria(proformaPresupuesto.getEstructruaNew().getCodigo() + proformaPresupuesto.getItemNew().getCodigo() + proformaPresupuesto.getFuenteNew().getCodFuente());
            proformaPresupuesto.setEstado(Boolean.TRUE);
            proformaPresupuesto.setGenerado(Boolean.FALSE);
            proformaPresupuesto.setFechaCreacion(new Date());
            proformaPresupuesto.setFechaModificacion(new Date());
            proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
            proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
            proformaPresupuesto.setCondicion(true);
            proformaPresupuesto.setCodigoReforma(null);
            proformaPresupuesto.setCodigoReformaTraspaso(null);
            proformaPresupuesto.setCodigo("PDI");
            proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());

            List<ProformaPresupuestoPlanificado> lisataproformaEgr = proformaPresupuestoPlanificadoService.verListaAnual(catalogoProforma.getPeriodoCatalogo().getAnio());

            for (ProformaPresupuestoPlanificado itemPlanificado : lisataproformaEgr) {
                if (itemPlanificado.getPartidaPresupuestaria().equals(proformaPresupuesto.getPartidaPresupuestaria())) {
                    listaCodigosRepetidos.add(proformaPresupuesto.getPartidaPresupuestaria());
                }
            }

            if (!listaCodigosRepetidos.isEmpty() || listaCodigosRepetidos.size() > 0) {
                PrimeFaces.current().executeScript("PF('DlgCodigosRepetidos').show()");
                PrimeFaces.current().ajax().update(":formcodigoRep");
                return;
            }
            proformaPresupuesto.setTraspasoIncremento(BigDecimal.ZERO);
            proformaPresupuesto.setTraspasoReduccion(BigDecimal.ZERO);
            proformaPresupuesto.setReformaSuplemento(BigDecimal.ZERO);
            proformaPresupuesto.setReformaReduccion(BigDecimal.ZERO);
            proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());

            this.proformaPresupuesto = proformaPresupuestoPlanificadoService.create(proformaPresupuesto);
        } else {

            proformaPresupuesto.setPartidaPresupuestaria(proformaPresupuesto.getEstructruaNew().getCodigo() + proformaPresupuesto.getItemNew().getCodigo() + proformaPresupuesto.getFuenteNew().getCodFuente());
            proformaPresupuesto.setFechaModificacion(new Date());
            proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
            List<ProformaPresupuestoPlanificado> lisataproformaEgr = proformaPresupuestoPlanificadoService.verListaAnual(catalogoProforma.getPeriodoCatalogo().getAnio());

            for (ProformaPresupuestoPlanificado itemPlanificado : lisataproformaEgr) {
                if (itemPlanificado.getPartidaPresupuestaria() == null ? proformaPresupuesto.getPartidaPresupuestaria() == null : itemPlanificado.getPartidaPresupuestaria().equals(proformaPresupuesto.getPartidaPresupuestaria())) {
                    listaCodigosRepetidos.add(proformaPresupuesto.getPartidaPresupuestaria());
                }
            }
            proformaPresupuestoPlanificadoService.edit(proformaPresupuesto);
        }

        PrimeFaces.current().executeScript("PF('DlgproformaRegistro').hide()");
        PrimeFaces.current().ajax().update(":formPresegres");
        PrimeFaces.current().ajax().update("messages");

        JsfUtil.addInformationMessage("Proforma Presupuesto", " Realizado proforma de Egresos "
                + proformaPresupuesto.getPartidaPresupuestaria() + (edit ? "  editado" : "  guardado") + " con éxito.");

        actualizarPresupuestoTotal();
        if (isTabla()) {
            tabla = true;
        }
        this.proformaPresupuesto = new ProformaPresupuestoPlanificado();

    }

    public void actualizarTabla() {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            proformaPresupuestoLazy = null;
            estadoGeneral = "";
            catalogoProAdicional = new CatalogoProformaPresupuesto();
            catalogoProforma.setDescripcion("");
            setTotal(BigDecimal.ZERO);
            catalogoProAdicional.setId(null);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "Eliga un periodo para proceder");
        } else {
            CatalogoProformaPresupuesto data = catalogoProformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), false);
            opcionBusqueda.setAnio(catalogoProforma.getPeriodoCatalogo().getAnio());
            if (data != null) {
                catalogoProforma.setDescripcion(data.getDescripcion());
                catalogoProAdicional.setId(data.getId());
            } else {
                catalogoProAdicional = new CatalogoProformaPresupuesto();
                catalogoProforma.setDescripcion("");
            }

            this.filtroItem = proformaPresupuestoPlanificadoService.filtroItem(catalogoProforma.getPeriodoCatalogo().getAnio());
            if (catalogoProforma.getPeriodoCatalogo() == null) {
                proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
                proformaPresupuestoLazy.getFilterss().put("periodo:equal", 0);
                proformaPresupuestoLazy.getFilterss().put("codigoReforma:equal", null);
                proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", null);
                proformaPresupuestoLazy.getSorteds().put("id", "ASC");
            } else {
                proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
                proformaPresupuestoLazy.getFilterss().put("periodo:equal", catalogoProforma.getPeriodoCatalogo().getAnio());
                proformaPresupuestoLazy.getFilterss().put("codigoReforma:equal", null);
                proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", null);
                proformaPresupuestoLazy.getSorteds().put("id", "ASC");
                listItem = new ArrayList<>();
                listItem = catalogoServiceNew.findTipoPresupuesto(false);
                listEstructura = new ArrayList<>();
                listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
                listFuente = new ArrayList<>();
                listFuente = fuenteServiceNew.getFuenteFinanciamiento();
                //this.listaProformaList = proformaPresupuestoPlanificadoService.mostrarProfromaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio());
                this.filtroEstrcutrua = proformaPresupuestoPlanificadoService.filtroEstructura(catalogoProforma.getPeriodoCatalogo().getAnio());
                this.filtroItem = proformaPresupuestoPlanificadoService.filtroItem(catalogoProforma.getPeriodoCatalogo().getAnio());
                this.listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), catalogoProforma.getPeriodoCatalogo().getAnio());
                this.listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), catalogoProforma.getPeriodoCatalogo().getAnio());
                this.listaCatalogoPresupuestos2 = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), catalogoProforma.getPeriodoCatalogo().getAnio());
                listaProformaPresupuesto = proformaPresupuestoPlanificadoService.findByNamedQuery("ProformaPresupuestoPlanificado.findByPeriodos", catalogoProforma.getPeriodoCatalogo().getAnio(), true, true);
                this.listaPlanProgramatico2 = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), catalogoProforma.getPeriodoCatalogo().getAnio());
                opcionBusqueda.setAnio(catalogoProforma.getPeriodoCatalogo().getAnio());
            }
            desactivarAprobado(catalogoProforma.getPeriodoCatalogo().getAnio());
            setOcultarAprobacion(true);
            if (isTabla() == false) {
                tabla = true;
            }
            //this.listaProformaList=proformaPresupuestoPlanificadoService.mostrarProfromaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio());
            actualizarPresupuestoTotal();
        }

        PrimeFaces.current().ajax().update("formMain");
    }

    public void actualizarVistaPrevia() {
        this.listaProformaList = proformaPresupuestoPlanificadoService.mostrarProfromaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio());
    }

    public void anadirPppa() {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Seleccione un período para poder proceder");
            return;
        }

        BigDecimal TotalProducto = BigDecimal.ZERO;
        BigDecimal TotalActividades = BigDecimal.ZERO;
        List<Producto> productoList = productoService.findByNamedQuery("Producto.findByPeriodoActual", catalogoProforma.getPeriodoCatalogo().getAnio());
        List<ActividadOperativa> actividadList = actividadOperativaService.findByNamedQuery("ActividadOperativa.findByPeriodoActual", catalogoProforma.getPeriodoCatalogo().getAnio());
        for (Producto producto : productoList) {
            TotalProducto = TotalProducto.setScale(2, RoundingMode.HALF_UP).add(producto.getMonto().setScale(2, RoundingMode.HALF_UP));
        }
        for (ActividadOperativa actividad : actividadList) {
            TotalActividades = TotalActividades.setScale(2, RoundingMode.HALF_UP).add(actividad.getMonto().setScale(2, RoundingMode.HALF_UP));
        }

        System.out.println("TotalProducto " + TotalProducto.doubleValue());
        System.out.println("TotalActividades " + TotalActividades.doubleValue());
        if (TotalProducto.doubleValue() == TotalActividades.doubleValue()) {
            opcionBusqueda.setAnio(catalogoProforma.getPeriodoCatalogo().getAnio());
            calcularEgresos();
            proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
            proformaPresupuestoLazy.getFilterss().put("periodo:equal", catalogoProforma.getPeriodoCatalogo().getAnio());
            proformaPresupuestoLazy.getFilterss().put("codigoReforma:equal", null);
            proformaPresupuestoLazy.getSorteds().put("id", "ASC");
            if (isTabla() == false) {
                tabla = true;
            }
            actualizarPresupuestoTotal();
        } else {
            JsfUtil.addErrorMessage("Advertencia", "La Suma de las Actividades no son iguales al de los Productos" + catalogoProforma.getPeriodoCatalogo().getAnio());
        }
        proformaPresupuesto = new ProformaPresupuestoPlanificado();
    }

    public void generarProforma() {
        if (catalogoProforma.getPeriodoCatalogo() != null) {
            if (catalogoProforma != null) {
                /*COMO ES POROFORMA DE EGRESO SU TIPO DE FLUJO SERA FALSE*/
                catalogoProforma.setTipoFlujo(Boolean.FALSE);
                /*CONSULTAR SI EXISTE YA UNA PROFORMA CON EL PERIODO*/
                CatalogoProformaPresupuesto resultCatalogos = catalogoProformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), catalogoProforma.getTipoFlujo());

                if (resultCatalogos != null) {

                    actualizarPresupuestoTotal();
                    resultCatalogos.setDescripcion(catalogoProforma.getDescripcion());
                    resultCatalogos.setTotal(getTotal());
                    catalogoProformaService.edit(resultCatalogos);
                    JsfUtil.addInformationMessage("Información", "Se ha actualizado el profroma egreso");

                } else {
                    /*HACEMOS UN CAMBIO DE DOUBLE A BIGDECIMALL PARA PODER GUARDARLO EN LA TABLLA DE CATALOGO_PROFORMA_PRESUPUESTO*/
                    BigDecimal cambioTipo = getTotal();
                    catalogoProforma.setTotal(cambioTipo);
                    catalogoProforma.setAprobado(Boolean.FALSE);
                    catalogoProforma.setPeriodo(catalogoProforma.getPeriodoCatalogo().getAnio());
                    catalogoProforma.setEstado(Boolean.TRUE);
                    /*COMO ES POROFORMA DE EGRESO SU TIPO DE FLUJO SERA FALSE*/

                    catalogoProforma.setUsuarioCreacion(user.getNameUser());
                    catalogoProforma.setFechaCreacion(new Date());
                    catalogoProforma.setUsuarioModifica(user.getNameUser());
                    catalogoProforma.setFechaModificacion(new Date());

                    catalogoProforma = catalogoProformaService.create(catalogoProforma);

                    JsfUtil.addSuccessMessage("Catálogo Proformas", catalogoProforma.getDescripcion() + " se registro correctamente");

                    catalogoProforma = new CatalogoProformaPresupuesto();
                }
            }

        } else {
            JsfUtil.addWarningMessage("Advertencia", "Seleccione un período para poder añadir una proforma");
        }
    }

    public String condicionDeAprobacion() {
        if (getAprobado() == null) {
            return "SIN ESPECIFICAR";
        } else {
            if (getAprobado() == false) {
                return "NO APROBADO";
            } else {
                return "APROBADO";
            }
        }
    }

    public void anadirDistributivo() {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Eliga un periodo para poder proceder");
            return;
        }

        proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(catalogoProforma.getPeriodoCatalogo().getAnio(), "PD");
        proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(catalogoProforma.getPeriodoCatalogo().getAnio(), "PDA");

        listaCodigosRepetidos = new ArrayList<>();
        List<ProformaPDTO> lista = partidaDistributivoService.showCodigosAgrupados(catalogoProforma.getPeriodoCatalogo().getAnio());
        if (lista == null) {
            JsfUtil.addWarningMessage("AVISO", "NO HAY DISTRIBUTIVOS CON PARTIDAS ASIGNADAS PARA EL PERÍODO " + catalogoProforma.getPeriodoCatalogo().getAnio());
            return;
        }
        List<ProformaPresupuestoPlanificado> istaProformaPresupuestoPlanificados = proformaPresupuestoPlanificadoService.verListaAnual(catalogoProforma.getPeriodoCatalogo().getAnio());
        for (ProformaPDTO item : lista) {

            for (ProformaPresupuestoPlanificado itemProforma : istaProformaPresupuestoPlanificados) {
                if (itemProforma.getPartidaPresupuestaria() == null ? item.getPartida() == null : itemProforma.getPartidaPresupuestaria().equals(item.getPartida())) {
                    listaCodigosRepetidos.add(item.getPartida());
                }
            }
        }

        if (!listaCodigosRepetidos.isEmpty() || listaCodigosRepetidos.size() > 0) {
            PrimeFaces.current().executeScript("PF('DlgCodigosRepetidos').show()");
            PrimeFaces.current().ajax().update(":formcodigoRep");
            return;
        }
        for (ProformaPDTO i : lista) {
            proformaPresupuesto.setPartidaPresupuestaria(i.getPartida());
            proformaPresupuesto.setValor(i.getTotal());
            proformaPresupuesto.setEstructruaNew(i.getEstructuraNew());
            proformaPresupuesto.setItemNew(i.getItemNew());
            proformaPresupuesto.setFuenteNew(i.getFuenteNew());
            proformaPresupuesto.setCodigo(i.getCodigo());
            proformaPresupuesto.setEstado(true);
            proformaPresupuesto.setPeriodo(catalogoProforma.getPeriodoCatalogo().getAnio());
            proformaPresupuesto.setGenerado(false);
            proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
            proformaPresupuesto.setFechaCreacion(new Date());
            proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
            proformaPresupuesto.setFechaModificacion(new Date());
            proformaPresupuesto.setCodigoReforma(null);
            proformaPresupuesto.setCodigoReformaTraspaso(null);
            proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
            proformaPresupuesto = proformaPresupuestoPlanificadoService.create(proformaPresupuesto);
            proformaPresupuesto = new ProformaPresupuestoPlanificado();
        }
        actualizarPresupuestoTotal();
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", "Plan  Distributivo añadido correctamente");

    }

    public void añadirDistributivoAnexo() {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Eliga un periodo para poder proceder");
            return;
        }
        proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(catalogoProforma.getPeriodoCatalogo().getAnio(), "PDA");
        listaCodigosRepetidos = new ArrayList<>();

        List<PartidasDistributivoAnexo> listaAnexo = partidaAnexoService.showAllPartidasAnexo(catalogoProforma.getPeriodoCatalogo().getAnio());
        if (listaAnexo.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "NO HAY DISTRIBUTIVO ANEXO CON PARTIDAS ASIGNADAS PARA EL PERÍODO " + catalogoProforma.getPeriodoCatalogo().getAnio());
            return;
        }

        List<ProformaPresupuestoPlanificado> istaProformaPresupuestoPlanificados = proformaPresupuestoPlanificadoService.verListaAnual(catalogoProforma.getPeriodoCatalogo().getAnio());

        for (PartidasDistributivoAnexo item : listaAnexo) {

            for (ProformaPresupuestoPlanificado itemProfor : istaProformaPresupuestoPlanificados) {
                if (item.getPartidaAp().equals(itemProfor.getPartidaPresupuestaria())) {
                    listaCodigosRepetidos.add(item.getPartidaAp());
                }
            }
        }
        if (!listaCodigosRepetidos.isEmpty() || listaCodigosRepetidos.size() > 0) {
            PrimeFaces.current().executeScript("PF('DlgCodigosRepetidos').show()");
            PrimeFaces.current().ajax().update(":formcodigoRep");
            return;
        }

        for (PartidasDistributivoAnexo p : listaAnexo) {
            proformaPresupuesto.setPartidaPresupuestaria(p.getPartidaAp());
            proformaPresupuesto.setValor(p.getDistributivoAnexo().getMonto());
            proformaPresupuesto.setEstructuraProgramatica(p.getEstructuraApA());
            proformaPresupuesto.setItemPresupuestario(p.getItemApA());
            proformaPresupuesto.setFuente(p.getFuenteApA());
            proformaPresupuesto.setFuenteDirecta(p.getFuenteDirectaA());
            proformaPresupuesto.setEstado(true);
            proformaPresupuesto.setPeriodo(catalogoProforma.getPeriodoCatalogo().getAnio());
            proformaPresupuesto.setGenerado(false);
            proformaPresupuesto.setUsuarioCreacion(user.getNameUser());
            proformaPresupuesto.setFechaCreacion(new Date());
            proformaPresupuesto.setUsuarioModificacion(user.getNameUser());
            proformaPresupuesto.setFechaModificacion(new Date());
            proformaPresupuesto.setCodigoReforma(null);
            proformaPresupuesto.setCodigoReformaTraspaso(null);
            proformaPresupuesto.setCodigo("PDA");
            proformaPresupuesto.setReformaCodificado(proformaPresupuesto.getValor());
            proformaPresupuesto = proformaPresupuestoPlanificadoService.create(proformaPresupuesto);
            proformaPresupuesto = new ProformaPresupuestoPlanificado();
        }

        actualizarPresupuestoTotal();
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", "Plan  Distributivo añadido correctamente");
    }

    public void imprimirRep(boolean excel) {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Aviso", "Eliga un período o esta proforma no se encuentra Registrada");
            return;
        }
        CatalogoProformaPresupuesto data = catalogoProformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), false);
        if (data == null) {
            JsfUtil.addWarningMessage("Aviso", "Eliga un período o esta proforma no se encuentra Registrada");
            return;
        }
        if (proformaPresupuestoLazy.getRowCount() == 0) {
            JsfUtil.addWarningMessage("Información", "No existen cuentas que mostrar");
            return;
        } else {
            ss.addParametro("anio", catalogoProforma.getPeriodoCatalogo().getAnio());
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
            if ((catalogoProAdicional.getId() != null)) {
                ss.addParametro("idPro", catalogoProAdicional.getId());
                if (excel) {
                    ss.setContentType("xlsx");
                    ss.setNombreReporte("proformaPresupuestoEgresoExcel");
                } else {
                    ss.setNombreReporte("proformaPresupuestoEgresoPdf");
                }
            } else {
                if (excel) {
                    System.out.println("entrando 23");
                    ss.setContentType("xlsx");
                    ss.setNombreReporte("proformaPresupuestoEgresoNoCatalogoExcel");
                } else {
                    ss.setNombreReporte("proformaPresupuestoEgresoNoCatalogoPdf");
                }
            }
            ss.setNombreSubCarpeta("presupuesto");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        }
    }

    public void imprimirResumen(boolean excel) {
        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Aviso", "Eliga un período o esta proforma no se encuentra Registrada");
            return;
        }

        CatalogoProformaPresupuesto data = catalogoProformaService.getCatalogoProformaPresupuesto(catalogoProforma.getPeriodoCatalogo().getAnio(), false);
        if (data == null) {
            JsfUtil.addWarningMessage("Aviso", "Eliga un período o esta proforma no se encuentra Registrada");
            return;
        }

        if (catalogoProforma.getPeriodoCatalogo() == null) {
            JsfUtil.addWarningMessage("Información", "Escoja un año para proceder");
            return;
        } else if (proformaPresupuestoLazy.getRowCount() == 0) {
            JsfUtil.addWarningMessage("Información", "No existen cuentas que mostrar");
            return;
        } else {
            ss.addParametro("periodo", catalogoProforma.getPeriodoCatalogo().getAnio());
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
            if ((catalogoProAdicional.getId() != null)) {
                ss.addParametro("id", catalogoProAdicional.getId());
                if (excel) {
                    ss.setContentType("xlsx");
                    ss.setNombreReporte("resumenProformaEgre");
                } else {
                    ss.setNombreReporte("resumenProformaEgre");
                }
            }
            ss.setNombreSubCarpeta("presupuesto");
//            ss.addParametro("NOMBRE_REPORTE", "PROFORMA EGRESOS");
//
//            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getCupoAsigando() {
        return cupoAsigando;
    }

    public void setCupoAsigando(BigDecimal cupoAsigando) {
        this.cupoAsigando = cupoAsigando;
    }

    public String getEstadoGeneral() {
        return estadoGeneral;
    }

    public void setEstadoGeneral(String estadoGeneral) {
        this.estadoGeneral = estadoGeneral;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuesto() {
        return listaProformaPresupuesto;
    }

    public void setListaProformaPresupuesto(List<ProformaPresupuestoPlanificado> listaProformaPresupuesto) {
        this.listaProformaPresupuesto = listaProformaPresupuesto;
    }

    public CatalogoProformaPresupuesto getCatalogoProAdicional() {
        return catalogoProAdicional;
    }

    public void setCatalogoProAdicional(CatalogoProformaPresupuesto catalogoProAdicional) {
        this.catalogoProAdicional = catalogoProAdicional;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuesto() {
        return proformaPresupuesto;
    }

    public void setProformaPresupuesto(ProformaPresupuestoPlanificado proformaPresupuesto) {
        this.proformaPresupuesto = proformaPresupuesto;
    }

    public CatalogoProformaPresupuesto getCatalogoProforma() {
        return catalogoProforma;
    }

    public void setCatalogoProforma(CatalogoProformaPresupuesto catalogoProforma) {
        this.catalogoProforma = catalogoProforma;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Producto getObjProducto() {
        return objProducto;
    }

    public void setObjProducto(Producto objProducto) {
        this.objProducto = objProducto;
    }

    public List<ProformaPresupuestoPlanificado> getListaproforma() {
        return listaproforma;
    }

    public void setListaproforma(List<ProformaPresupuestoPlanificado> listaproforma) {
        this.listaproforma = listaproforma;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public ProformaPresupuestoPlanificado getPp() {
        return pp;
    }

    public void setPp(ProformaPresupuestoPlanificado pp) {
        this.pp = pp;
    }

    public ProformaPresupuestoPlanificado getCp() {
        return cp;
    }

    public void setCp(ProformaPresupuestoPlanificado cp) {
        this.cp = cp;
    }

    public void setTotal(BigDecimal Total) {
        this.Total = Total;
    }

    public Boolean getAprobado() {
        return Aprobado;
    }

    public void setAprobado(Boolean Aprobado) {
        this.Aprobado = Aprobado;
    }

    public List<ProformaDTO> getProformaDTO() {
        return proformaDTO;
    }

    public void setProformaDTO(List<ProformaDTO> proformaDTO) {
        this.proformaDTO = proformaDTO;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }

    public boolean isTabla() {
        return tabla;
    }

    public void setTabla(boolean tabla) {
        this.tabla = tabla;
    }

    public boolean isBotonHabilitar() {
        return botonHabilitar;
    }

    public void setBotonHabilitar(boolean botonHabilitar) {
        this.botonHabilitar = botonHabilitar;
    }

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public MasterCatalogo getMc() {
        return mc;
    }

    public void setMc(MasterCatalogo mc) {
        this.mc = mc;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public List<PlanProgramatico> getListaPlanProgramatico2() {
        return listaPlanProgramatico2;
    }

    public void setListaPlanProgramatico2(List<PlanProgramatico> listaPlanProgramatico2) {
        this.listaPlanProgramatico2 = listaPlanProgramatico2;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos2() {
        return listaCatalogoPresupuestos2;
    }

    public void setListaCatalogoPresupuestos2(List<CatalogoPresupuesto> listaCatalogoPresupuestos2) {
        this.listaCatalogoPresupuestos2 = listaCatalogoPresupuestos2;
    }

    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public boolean isOcultarAprobacion() {
        return ocultarAprobacion;
    }

    public void setOcultarAprobacion(boolean ocultarAprobacion) {
        this.ocultarAprobacion = ocultarAprobacion;
    }

    public List<ProformaPDTO> getListaProformaList() {
        return listaProformaList;
    }

    public void setListaProformaList(List<ProformaPDTO> listaProformaList) {
        this.listaProformaList = listaProformaList;
    }

    public List<String> getListaCodigosRepetidos() {
        return listaCodigosRepetidos;
    }

    public void setListaCodigosRepetidos(List<String> listaCodigosRepetidos) {
        this.listaCodigosRepetidos = listaCodigosRepetidos;
    }

    public List<PlanProgramatico> getFiltroEstrcutrua() {
        return filtroEstrcutrua;
    }

    public void setFiltroEstrcutrua(List<PlanProgramatico> filtroEstrcutrua) {
        this.filtroEstrcutrua = filtroEstrcutrua;
    }

    public List<CatalogoPresupuesto> getFiltroItem() {
        return filtroItem;
    }

    public void setFiltroItem(List<CatalogoPresupuesto> filtroItem) {
        this.filtroItem = filtroItem;
    }

    public ProformaPresupuestoPlanificado getCodigoPro() {
        return codigoPro;
    }

    public void setCodigoPro(ProformaPresupuestoPlanificado codigoPro) {
        this.codigoPro = codigoPro;
    }

    public LazyModel<ProformaPresupuestoPlanificado> getProformaPresupuestoLazy() {
        return proformaPresupuestoLazy;
    }

    public void setProformaPresupuestoLazy(LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy) {
        this.proformaPresupuestoLazy = proformaPresupuestoLazy;
    }

    public List<FuenteFinanciamiento> getFiltroFuente() {
        return filtroFuente;
    }

    public void setFiltroFuente(List<FuenteFinanciamiento> filtroFuente) {
        this.filtroFuente = filtroFuente;
    }

    public List<PresPlanProgramatico> getListEstructura() {
        return listEstructura;
    }

    public void setListEstructura(List<PresPlanProgramatico> listEstructura) {
        this.listEstructura = listEstructura;
    }

    public List<PresCatalogoPresupuestario> getListItem() {
        return listItem;
    }

    public void setListItem(List<PresCatalogoPresupuestario> listItem) {
        this.listItem = listItem;
    }

    public List<PresFuenteFinanciamiento> getListFuente() {
        return listFuente;
    }

    public void setListFuente(List<PresFuenteFinanciamiento> listFuente) {
        this.listFuente = listFuente;
    }
//</editor-fold>

}
