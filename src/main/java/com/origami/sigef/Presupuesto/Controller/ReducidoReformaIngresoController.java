/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Model.PosicionRefomaIngreso;
import com.origami.sigef.Presupuesto.Service.DetalleSuplementoIngresoService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "reducidoIngresoView")
@ViewScoped
public class ReducidoReformaIngresoController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(SuplementoReformaIngresoController.class.getName());

    //<editor-fold defaultstate="collapsed" desc="VARIBALES">
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    private DetalleSuplementoIngresoService detalleSuplementoService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private CatalogoService catalogoService;

    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ProductoService productoService;
    private boolean btnRegistrar;
    private ReformaIngresoSuplemento suplementoIngreso;
    private DetalleReformaIngresoSuplemento detalleSuplementoIngreso;
    private List<Short> listaPeridos;
    private List<DetalleReformaIngresoSuplemento> listaItemReforma;
    private List<ProformaIngreso> listaItem;
    private OpcionBusqueda busqueda;
    private List<CatalogoItem> listaFiltroItem;
    private Boolean columnaSuplementaria = Boolean.TRUE;
    private Boolean columnaReducido = Boolean.FALSE;
    private double totalSuplemeto;
    private double totalCodificado;
    private double totalReducido;
    private double totalPresupuestoInicial;
    private List<Nivel> listaFiltroNivel;

    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private ActividadOperativa actividadOperativa;
    private Producto producto;
//</editor-fold>

    private boolean panel1, panel2, registrarSolicitud;
    private LazyModel<ReformaIngresoSuplemento> lazyReformas;
    private List<CatalogoItem> estadoFiltros;
    @Inject
    private CatalogoService catalogoService1;
    @Inject
    private DistributivoAnexoService distributivoAnexoService;

    @Inject
    private PartidaDistributivoAnexoService PartidaDisAnexoService;
    @Inject
    private PartidaDistributivoService partidasService;
    @Inject
    private ValoresDistributivoService valoresService;
    private List<ValoresDistributivo> listaValoresDistributivo;
    private List<Distributivo> copiaDiReforma;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaService;
    private String observaciones;
    private List<ReformaIngresoSuplemento> listaVerificador;
    private boolean verificadorSolicitud, enabledReformas, ensabledNuevo;
    @Inject
    private ClienteService clienteService;

    //NUEVO
    private List<PresFuenteFinanciamiento> tipoFuente;
    private List<ProformaIngreso> listaPresupuestoIngreso;
    private String nombreUsuario;
    @Inject
    private ManagerService service;
    @Inject
    private PresFuenteFinanciamientoService presFuenteFinanciamientoService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    private List<PosicionRefomaIngreso> listaPosicion;

    @PostConstruct
    public void inicializador() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.suplementoIngreso = new ReformaIngresoSuplemento();
                this.detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
                //this.suplementoIngreso.setCodigo("REF-SUPL-ING-" + suplementoIngresoService.maxNumeracion());
                this.listaItemReforma = new ArrayList<>();
                this.listaItem = new ArrayList<>();
                this.busqueda = new OpcionBusqueda();
                this.listaFiltroItem = new ArrayList<>();
                this.totalSuplemeto = 0;
                this.totalCodificado = 0;
                this.totalReducido = 0;
                this.totalPresupuestoInicial = 0;
                this.columnaSuplementaria = Boolean.TRUE;
                this.columnaReducido = Boolean.FALSE;

                this.listaFiltroNivel = new ArrayList<>();
                this.planAnualPoliticaPublica = new PlanAnualPoliticaPublica();
                this.planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                this.planAnualProgramaProyecto = new PlanAnualProgramaProyecto();
                this.actividadOperativa = new ActividadOperativa();
                this.producto = new Producto();
                btnRegistrar = true;

                panel1 = true;
                panel2 = false;

                lazyReformas = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReformas.getFilterss().put("tipo", false);
                lazyReformas.getFilterss().put("numTramite", BigInteger.valueOf(tramite.getNumTramite()));
                estadoFiltros = catalogoService.MostarTodoCatalogo("estado_solicitud");
                listaVerificador = suplementoIngresoService.getSolictudesTramite(BigInteger.valueOf(tramite.getNumTramite()));
                enabledReformas = true;
                ensabledNuevo = false;
                if (!listaVerificador.isEmpty()) {
                    verificadorSolicitud = false;
                } else {
                    verificadorSolicitud = true;
                }
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void columnaDataTable() {
        if (suplementoIngreso.getTipo()) {

            this.columnaSuplementaria = Boolean.TRUE;
            this.columnaReducido = Boolean.FALSE;
        } else {
            this.columnaReducido = Boolean.TRUE;
            this.columnaSuplementaria = Boolean.FALSE;
        }

    }

    public void showPaneles() {
        panel1 = true;
        panel2 = false;
        enabledReformas = true;
        ensabledNuevo = false;
    }

    public void registraSolicitudNuevo() {
        panel1 = false;
        panel2 = true;
        enabledReformas = false;
        ensabledNuevo = true;
    }

    public BigDecimal totalReducido(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public boolean verificarMes() {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");

        simpleDateFormat = new SimpleDateFormat("MM");
        int mes = Integer.valueOf(simpleDateFormat.format(suplementoIngreso.getFechaOficioReforma())) - 1;
        String mesString = String.valueOf(mes);
        Short mesShort = Short.valueOf(mesString);
        simpleDateFormat = new SimpleDateFormat("YYYY");
        Short anio = Short.valueOf(simpleDateFormat.format(suplementoIngreso.getFechaOficioReforma()));

        ControlCuentaPresupuestario control = suplementoIngresoService.getVerificarMesAnio(mesShort, anio);

        if (control != null) {

            return control.getEstado();

        } else {
            return false;
        }

    }

    public void cargarDataTabla() {

        listaItemReforma = new ArrayList<>();
        totalPresupuestoInicial = BigDecimal.ZERO.doubleValue();
        totalSuplemeto = BigDecimal.ZERO.doubleValue();
        totalReducido = BigDecimal.ZERO.doubleValue();
        totalCodificado = BigDecimal.ZERO.doubleValue();

        if (suplementoIngreso.getPeriodo() == null && suplementoIngreso.getFechaOficioReforma() == null) {
            JsfUtil.addWarningMessage("AVISO", "PRIMERO TIENE QUE DARLE CLICK AL BOTON NUEVO QUE SE ENCUENTRA EN LA PARTE SUPERIOR DERECHA");
            return;

        }
        if (!verificarMes()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "NO PUEDE REALIZAR ESTA REFORMA PORQUE EL MES SE ENCUENTRA CERRADO PARA EL AÑO REQUIRIENTE");
            return;

        }

        List<CatalogoProformaPresupuesto> lis = suplementoIngresoService.getPeriodoVer(suplementoIngreso.getPeriodo());

        if (lis.size() != 2) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Tiene que estar aprobado el de Ingreso como el de Egreso");
            return;
        }

        Long id = 0L;
        this.listaFiltroNivel = suplementoIngresoService.getListaNiveles(suplementoIngreso.getPeriodo(), true);
        this.listaFiltroItem = suplementoIngresoService.getListaItemFiltro(suplementoIngreso.getPeriodo(), true);
        this.listaItem = suplementoIngresoService.getListaCatalogoPresupuesto(suplementoIngreso.getPeriodo(), true);

        for (ProformaIngreso data : listaItem) {
            id = id + 1;
            DetalleReformaIngresoSuplemento item = new DetalleReformaIngresoSuplemento();
            item.setProformaIngreso(data);
            item.setReforma(null);
            item.setReducido(BigDecimal.ZERO);
            item.setSuplemento(BigDecimal.ZERO);
            item.setCodificado(data.getPresupuestoCodificado());
            item.setId(id);
            listaItemReforma.add(item);

        }

        actualizarTotales();
    }

    public void sumaArbol(DetalleReformaIngresoSuplemento s) {
        if (s != null && s.getId() != null) {

            int dataId = -2;
            ProformaIngreso padreTmp = new ProformaIngreso();
            DetalleReformaIngresoSuplemento data = new DetalleReformaIngresoSuplemento();
            BigDecimal valor = BigDecimal.ZERO;

            if (s.getProformaIngreso().getItem().getPadre() != null) {
                for (DetalleReformaIngresoSuplemento item : listaItemReforma) {
                    if (s.getProformaIngreso().getItem().getPadre() == item.getProformaIngreso().getItem().getPadre()) {
                        valor = valor.add(item.getCodificado());

                    }

                }
                padreTmp = detalleSuplementoService.obtenerPadreTmp(suplementoIngreso.getPeriodo(), s.getProformaIngreso().getItem().getPadre().getId());
                if (padreTmp != null) {
                    for (DetalleReformaIngresoSuplemento pos : listaItemReforma) {
                        if (pos.getProformaIngreso().getId().equals(padreTmp.getId())) {
                            dataId = listaItemReforma.indexOf(pos);
                            break;
                        }
                    }

                    DetalleReformaIngresoSuplemento itemTemp = listaItemReforma.get(dataId);
                    itemTemp.setReducido(BigDecimal.ZERO);
                    itemTemp.setCodificado(valor);
                    this.listaItemReforma.remove(dataId);
                    this.listaItemReforma.add(dataId, itemTemp);

                    if (itemTemp != null) {
                        sumaArbol(itemTemp);
                    }
                }
            }
        }
    }

    public void cellEdicion(DetalleReformaIngresoSuplemento d) {
        DetalleReformaIngresoSuplemento o = d;
        if (o.getProformaIngreso().getFuente() == null) {
            o.setSuplemento(BigDecimal.ZERO);
            o.setCodificado(d.getCatalogoPresupuesto().getPresupuestoCodificado());
            JsfUtil.addErrorMessage("Error", "Eliga una fuente de financiamiento");
            return;
        }

        if (d.getReducido().doubleValue() > d.getProformaIngreso().getPresupuestoCodificado().doubleValue()) {
            d.setSuplemento(BigDecimal.ZERO);
            d.setReducido(BigDecimal.ZERO);
            d.setCodificado(d.getProformaIngreso().getPresupuestoCodificado());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "El valor es mayor al presupuesto Inicial");

        } else {
            d.setSuplemento(BigDecimal.ZERO);
            d.setCodificado(d.getProformaIngreso().getPresupuestoCodificado().subtract(d.getReducido()));

        }

        this.listaItemReforma.add(this.listaItemReforma.indexOf(d), d);
        this.listaItemReforma.remove(this.listaItemReforma.indexOf(d));

        sumaArbol(d);
        actualizarTotales();

    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("fmreformas1:datareformas");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("fmreformas1:datareformas");
        }
    }

    public void clearAllFilters(int a) {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("fornGeneral:cuentas");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("fornGeneral:cuentas");
        }
    }

    public void actualizarTotales() {
        double valor1 = 0, valor2 = 0, valor3 = 0, valor4 = 0;

        totalSuplemeto = 0;
        totalCodificado = 0;
        totalReducido = 0;
        totalPresupuestoInicial = 0;

        for (DetalleReformaIngresoSuplemento item : listaItemReforma) {

            if (item.getProformaIngreso().getItem().getMovimiento()) {
                valor1 = valor1 + item.getProformaIngreso().getPresupuestoCodificado().doubleValue();
                valor2 = valor2 + item.getSuplemento().doubleValue();
                valor3 = valor3 + item.getReducido().doubleValue();
                valor4 = valor4 + item.getCodificado().doubleValue();
            }

            totalSuplemeto = valor2;
            totalCodificado = valor4;
            totalReducido = valor3;
            totalPresupuestoInicial = valor1;

        }
    }

    public void nuevoRegistro() {
        Calendar fecha = new GregorianCalendar();
        btnRegistrar = false;
        suplementoIngreso = new ReformaIngresoSuplemento();
        detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
        totalSuplemeto = 0;
        totalCodificado = 0;
        totalReducido = 0;
        totalPresupuestoInicial = 0;
        this.listaItemReforma.clear();

        this.suplementoIngreso.setTipo(false);
        this.listaFiltroItem = new ArrayList<>();
        this.suplementoIngreso.setFechaOficioReforma(new Date());

        int fechaActual = fecha.get(Calendar.YEAR);

        String anio = String.valueOf(fechaActual);

        this.suplementoIngreso.setPeriodo(Short.valueOf(anio));

    }

    public void cancelar() {
        btnRegistrar = true;
        suplementoIngreso = new ReformaIngresoSuplemento();
        detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
        totalSuplemeto = 0;
        totalCodificado = 0;
        totalReducido = 0;
        totalPresupuestoInicial = 0;
        this.listaItemReforma.clear();
        this.listaFiltroItem = new ArrayList<>();
    }

    public void editarReforma(ReformaIngresoSuplemento r) {
        suplementoIngreso = new ReformaIngresoSuplemento();
        suplementoIngreso = r;
        List<DetalleReformaIngresoSuplemento> listaTemporalDetalle = suplementoIngresoService.getDetalleReormaIngreso(r);
        listaItemReforma.clear();

        if (!listaTemporalDetalle.isEmpty()) {
            for (DetalleReformaIngresoSuplemento data : listaTemporalDetalle) {
                listaItemReforma.add(data);
            }
        }

        panel1 = false;
        panel2 = true;
        btnRegistrar = false;
        actualizarTotales();
        listaVerificador = new ArrayList<>();
        listaVerificador = suplementoIngresoService.getSolictudesTramite(BigInteger.valueOf(tramite.getNumTramite()));

        if (!listaVerificador.isEmpty()) {
            verificadorSolicitud = false;
        } else {
            verificadorSolicitud = true;
        }

        enabledReformas = false;
        ensabledNuevo = true;
    }

    public void saveReofomaSuplmento() {
        if (suplementoIngreso.getPeriodo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Eliga un Período");
            return;
        }
        if (listaItemReforma.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No hay datos que registrar");
            return;
        }

        if (totalReducido < 1) {
            JsfUtil.addWarningMessage("Advertencia", "No hay ningun valor de Reducción");
            return;
        }

        if (!verificarMes()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERRO", "NO PUEDE REALIZAR ESTA REFORMA PORQUE EL MES SE ENCUENTRA CERRADO PARA EL AÑO REQUIRIENTE");
            return;

        }

        try {
            btnRegistrar = true;
            if (suplementoIngreso.getId() == null) {
                detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
                suplementoIngreso.setEstado(suplementoIngresoService.getlistaEstado("E", "estado_solicitud"));
                suplementoIngreso.setUsuarioCreacion(userSession.getNameUser());
                suplementoIngreso.setUsuarioModificacion(userSession.getNameUser());
                suplementoIngreso.setFechaCreacion(new Date());
                suplementoIngreso.setFechaModificacion(new Date());
                suplementoIngreso.setNumeracion(suplementoIngresoService.maxNumeracion());
                suplementoIngreso.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
                suplementoIngreso = suplementoIngresoService.create(suplementoIngreso);

                for (DetalleReformaIngresoSuplemento data : listaItemReforma) {
                    detalleSuplementoIngreso.setId(null);
                    detalleSuplementoIngreso.setReforma(suplementoIngreso);
                    detalleSuplementoIngreso.setProformaIngreso(data.getProformaIngreso());
                    detalleSuplementoIngreso.setSuplemento(data.getSuplemento());
                    detalleSuplementoIngreso.setReducido(data.getReducido());
                    detalleSuplementoIngreso.setCodificado(data.getCodificado());
                    detalleSuplementoIngreso.setCodigoReferencia(BigInteger.valueOf(data.getProformaIngreso().getId()));
                    detalleSuplementoIngreso = detalleSuplementoService.create(detalleSuplementoIngreso);
                    detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();

                }
                JsfUtil.addInformationMessage("Exitoso", "Registro Correcto");
                CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");

                Thread data = new Thread() {
                    @Override
                    public void run() {
                        crearPapp(suplementoIngreso.getPeriodo(), estado, true, suplementoIngreso.getId());
                        crearPartidasDirectas(suplementoIngreso);
                        creacionDistributivoCompleteNew(suplementoIngreso);
                    }

                };

                data.start();
                JsfUtil.addInformationMessage("Exitoso", "Registro Correcto");
            } else {
                suplementoIngreso.setUsuarioModificacion(userSession.getNameUser());
                suplementoIngreso.setFechaModificacion(new Date());
                suplementoIngresoService.edit(suplementoIngreso);

                for (DetalleReformaIngresoSuplemento data : listaItemReforma) {
                    detalleSuplementoIngreso.setId(data.getId());
                    detalleSuplementoIngreso.setReforma(suplementoIngreso);
                    detalleSuplementoIngreso.setProformaIngreso(data.getProformaIngreso());
                    detalleSuplementoIngreso.setSuplemento(data.getSuplemento());
                    detalleSuplementoIngreso.setReducido(data.getReducido());
                    detalleSuplementoIngreso.setCodificado(data.getCodificado());
                    detalleSuplementoIngreso.setCodigoReferencia(BigInteger.valueOf(data.getProformaIngreso().getId()));
                    detalleSuplementoService.edit(detalleSuplementoIngreso);
                }
                JsfUtil.addInformationMessage("Exitoso", "Se ha editado Correctamente");

            }
            panel1 = true;
            panel2 = false;
            btnRegistrar = true;
            enabledReformas = true;
            ensabledNuevo = false;
            listaVerificador = suplementoIngresoService.getSolictudesTramite(BigInteger.valueOf(tramite.getNumTramite()));

            if (!listaVerificador.isEmpty()) {
                verificadorSolicitud = false;
            } else {
                verificadorSolicitud = true;
            }
        } catch (Exception e) {
            btnRegistrar = false;
            System.out.println("error " + e);
            LOG.log(Level.SEVERE, "error" + e);
        }

        //getItemByCatalogoAndCodigo
    }

    public void crearPapp(Short periodo, CatalogoItem c, Boolean estado, Long num) {

        try {
//            List<PlanLocalProgramaProyecto> planLocalList = actividadService.getReformaPlanLocalProgramaProyecto(periodo, c, estado);
//            List<PlanAnualPoliticaPublica> planAnualPoliticaList = actividadService.getReformaPlanPoliticaPublica(periodo, c, estado);
//            List<PlanAnualProgramaProyecto> planAnualProgramaList = actividadService.getReformaPlanAnulaProgramaProyecto(periodo, c, estado);
            List<ActividadOperativa> activiadOperativaList = actividadService.getReformaActividadOperativa(periodo, c, estado);
            List<Producto> productoList = actividadService.getReformaProducto(periodo, c, estado);

            CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RR");

            for (ActividadOperativa data4 : activiadOperativaList) {
                this.actividadOperativa = new ActividadOperativa();
                this.actividadOperativa = Utils.clone(data4);
                this.actividadOperativa.setId(null);
                this.actividadOperativa.setEstado(true);
                this.actividadOperativa.setFechaCreacion(data4.getFechaCreacion());
                this.actividadOperativa.setUsuarioCreacion(data4.getUsuarioCreacion());
                this.actividadOperativa.setFechaModificacion(data4.getFechaModificacion());
                this.actividadOperativa.setUsuarioModifica(data4.getUsuarioModifica());
                this.actividadOperativa.setCodigoReforma(BigInteger.valueOf(num));
                this.actividadOperativa.setNumeroOrdenId(BigInteger.valueOf(data4.getId()));
                this.actividadOperativa.setNumeroTramite(null);
                this.actividadOperativa.setMonotReformado(data4.getMonotReformado());
                this.actividadOperativa.setEstadoPapp(estado2);
                this.actividadOperativa.setEstadoPapp(estado2);
                this.actividadOperativa = actividadService.create(actividadOperativa);
                this.actividadOperativa = new ActividadOperativa();
            }

            for (Producto data5 : productoList) {

                ActividadOperativa actividad = actividadService.showAcividad(BigInteger.valueOf(num), BigInteger.valueOf(data5.getActividadOperativa().getId()));
                this.producto = new Producto();
                this.producto = Utils.clone(data5);
                this.producto.setId(null);
                this.producto.setEstado(true);
                this.producto.setActividadOperativa(actividad);
                this.producto.setFechaCreacion(data5.getFechaCreacion());
                this.producto.setUsuarioCreacion(data5.getUsuarioCreacion());
                this.producto.setFechaModificacion(data5.getFechaModificacion());
                this.producto.setUsuarioModifica(data5.getUsuarioModifica());
                this.producto.setPeriodo(data5.getPeriodo());
                this.producto.setSaldoDisponible(producto.getMonto().subtract(producto.getReserva()));
                this.producto.setTraspasoIncremento(BigDecimal.ZERO);
                this.producto.setTraspasoReduccion(BigDecimal.ZERO);
                this.producto.setSuplementoEgreso(BigDecimal.ZERO);
                this.producto.setReduccionEgreso(BigDecimal.ZERO);
                this.producto.setMontoReformada(data5.getMontoReformada());
                this.producto.setMonto(data5.getMontoReformada());
                this.producto.setCodigoReforma(BigInteger.valueOf(num));
                this.producto.setNumeroOrdenId(BigInteger.valueOf(data5.getId()));
                this.producto.setNumeroTramite(null);
                this.producto.setEstadoPapp(estado2);
                this.producto = productoService.create(producto);
                this.producto = new Producto();
            }
            System.out.println("PAPP CREATED...!");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error " + e);
        }

    }

    public void creacionDistributivoCompleteNew(ReformaIngresoSuplemento i) {

        List<ThCargoRubros> distributivos = thCargoRubrosService.copiaReforma(Boolean.TRUE, i.getPeriodo());
        List<ThCargoRubros> verificar = thCargoRubrosService.copiaVerificacion(BigInteger.valueOf(i.getId().longValue()));

        if (verificar.isEmpty()) {
            if (!distributivos.isEmpty()) {
                for (ThCargoRubros item : distributivos) {
                    ThCargoRubros data = new ThCargoRubros();
                    data = Utils.clone(item);
                    data.setId(null);
                    data.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                    data.setCodigoReforma(BigInteger.valueOf(i.getId()));
                    data.setMonto(item.getReformaCodificado());
                    data.setReformaCodificado(data.getMonto());
                    data.setReformaSuplemento(BigDecimal.ZERO);
                    data.setReformaReduccion(BigDecimal.ZERO);
                    data.setFechaCreacion(new Date());
                    data.setFechaModificacion(new Date());
                    data.setUsuarioCreacion(nombreUsuario);
                    data.setUsuarioModificacion(nombreUsuario);
                    thCargoRubrosService.create(data);
                    data = new ThCargoRubros();

                }
            }
        }
    }

    public void crearPartidasDirectas(ReformaIngresoSuplemento r) {
        ReformaIngresoSuplemento reformaGlobal = new ReformaIngresoSuplemento();
        reformaGlobal = r;
        List<ProformaPresupuestoPlanificado> proformaList = new ArrayList<>();

        List<ProformaPresupuestoPlanificado> listVerificar = proformaService.getListaVerificacion(BigInteger.valueOf(r.getId()));

        if (listVerificar.isEmpty()) {
            proformaList = proformaService.getPratidasDirectasReformas(r.getPeriodo(), "PDI");

            ProformaPresupuestoPlanificado pro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado item : proformaList) {
                pro.setCodigo(item.getCodigo());
                pro.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                pro.setCodigoReforma(BigInteger.valueOf(r.getId()));
                pro.setCondicion(item.getCondicion());
                pro.setEstado(item.getEstado());
                pro.setFechaCreacion(item.getFechaCreacion());
                pro.setFechaModificacion(item.getFechaModificacion());
                pro.setFuenteNew(item.getFuenteNew());
                pro.setEstructruaNew(item.getEstructruaNew());
                pro.setItemNew(item.getItemNew());
                pro.setGenerado(pro.getGenerado());
                pro.setCondicion(item.getCondicion());
                pro.setValor(item.getReformaCodificado());
                pro.setReformaSuplemento(BigDecimal.ZERO);
                pro.setReformaReduccion(BigDecimal.ZERO);
                pro.setTraspasoIncremento(BigDecimal.ZERO);
                pro.setTraspasoReduccion(BigDecimal.ZERO);
                pro.setUsuarioModificacion(nombreUsuario);
                pro.setPartidaPresupuestaria(item.getPartidaPresupuestaria());
                pro.setReformaCodificado(item.getReformaCodificado());
                pro.setPeriodo(r.getPeriodo());
                pro.setNumTramite(Short.valueOf("0"));
                pro.setUsuarioCreacion(item.getUsuarioCreacion());
                pro.setGenerado(item.getGenerado());
                pro = proformaService.create(pro);
                pro = new ProformaPresupuestoPlanificado();

            }
        }
        System.out.println("PARTIDAS DIRECTAS CREATED...!");
    }

    public void abriDlogo(ReformaIngresoSuplemento r) {
        suplementoIngreso = new ReformaIngresoSuplemento();
        suplementoIngreso = r;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones.toUpperCase());
            getParamts().put("usuario_financiero", clienteService.getrolsUser(RolUsuario.asistenteFinanciero));
            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            suplementoIngreso = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public boolean isBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    public boolean isEnabledReformas() {
        return enabledReformas;
    }

    public void setEnabledReformas(boolean enabledReformas) {
        this.enabledReformas = enabledReformas;
    }

    public boolean isEnsabledNuevo() {
        return ensabledNuevo;
    }

    public void setEnsabledNuevo(boolean ensabledNuevo) {
        this.ensabledNuevo = ensabledNuevo;
    }

    public boolean isPanel1() {
        return panel1;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPanel2() {
        return panel2;
    }

    public void setPanel2(boolean panel2) {
        this.panel2 = panel2;
    }

    public boolean isRegistrarSolicitud() {
        return registrarSolicitud;
    }

    public void setRegistrarSolicitud(boolean registrarSolicitud) {
        this.registrarSolicitud = registrarSolicitud;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyReformas() {
        return lazyReformas;
    }

    public void setLazyReformas(LazyModel<ReformaIngresoSuplemento> lazyReformas) {
        this.lazyReformas = lazyReformas;
    }

    public List<CatalogoItem> getEstadoFiltros() {
        return estadoFiltros;
    }

    public void setEstadoFiltros(List<CatalogoItem> estadoFiltros) {
        this.estadoFiltros = estadoFiltros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ReformaIngresoSuplemento> getListaVerificador() {
        return listaVerificador;
    }

    public void setListaVerificador(List<ReformaIngresoSuplemento> listaVerificador) {
        this.listaVerificador = listaVerificador;
    }

    public boolean isVerificadorSolicitud() {
        return verificadorSolicitud;
    }

    public void setVerificadorSolicitud(boolean verificadorSolicitud) {
        this.verificadorSolicitud = verificadorSolicitud;
    }

    public PlanAnualPoliticaPublica getPlanAnualPoliticaPublica() {
        return planAnualPoliticaPublica;
    }

    public void setPlanAnualPoliticaPublica(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public PlanAnualProgramaProyecto getPlanAnualProgramaProyecto() {
        return planAnualProgramaProyecto;
    }

    public void setPlanAnualProgramaProyecto(PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public ActividadOperativa getActividadOperativa() {
        return actividadOperativa;
    }

    public void setActividadOperativa(ActividadOperativa actividadOperativa) {
        this.actividadOperativa = actividadOperativa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ReformaIngresoSuplemento getSuplementoIngreso() {
        return suplementoIngreso;
    }

    public void setSuplementoIngreso(ReformaIngresoSuplemento suplementoIngreso) {
        this.suplementoIngreso = suplementoIngreso;
    }

    public DetalleReformaIngresoSuplemento getDetalleSuplementoIngreso() {
        return detalleSuplementoIngreso;
    }

    public void setDetalleSuplementoIngreso(DetalleReformaIngresoSuplemento detalleSuplementoIngreso) {
        this.detalleSuplementoIngreso = detalleSuplementoIngreso;
    }

    public List<Short> getListaPeridos() {
        return listaPeridos;
    }

    public void setListaPeridos(List<Short> listaPeridos) {
        this.listaPeridos = listaPeridos;
    }

    public List<DetalleReformaIngresoSuplemento> getListaItemReforma() {
        return listaItemReforma;
    }

    public void setListaItemReforma(List<DetalleReformaIngresoSuplemento> listaItemReforma) {
        this.listaItemReforma = listaItemReforma;
    }

    public List<ProformaIngreso> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<ProformaIngreso> listaItem) {
        this.listaItem = listaItem;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<CatalogoItem> getListaFiltroItem() {
        return listaFiltroItem;
    }

    public void setListaFiltroItem(List<CatalogoItem> listaFiltroItem) {
        this.listaFiltroItem = listaFiltroItem;
    }

    public Boolean getColumnaSuplementaria() {
        return columnaSuplementaria;
    }

    public void setColumnaSuplementaria(Boolean columnaSuplementaria) {
        this.columnaSuplementaria = columnaSuplementaria;
    }

    public Boolean getColumnaReducido() {
        return columnaReducido;
    }

    public void setColumnaReducido(Boolean columnaReducido) {
        this.columnaReducido = columnaReducido;
    }

    public double getTotalSuplemeto() {
        return totalSuplemeto;
    }

    public void setTotalSuplemeto(double totalSuplemeto) {
        this.totalSuplemeto = totalSuplemeto;
    }

    public double getTotalCodificado() {
        return totalCodificado;
    }

    public void setTotalCodificado(double totalCodificado) {
        this.totalCodificado = totalCodificado;
    }

    public double getTotalReducido() {
        return totalReducido;
    }

    public void setTotalReducido(double totalReducido) {
        this.totalReducido = totalReducido;
    }

    public double getTotalPresupuestoInicial() {
        return totalPresupuestoInicial;
    }

    public void setTotalPresupuestoInicial(double totalPresupuestoInicial) {
        this.totalPresupuestoInicial = totalPresupuestoInicial;
    }

    public List<Nivel> getListaFiltroNivel() {
        return listaFiltroNivel;
    }

    public void setListaFiltroNivel(List<Nivel> listaFiltroNivel) {
        this.listaFiltroNivel = listaFiltroNivel;
    }

//</editor-fold>
}
