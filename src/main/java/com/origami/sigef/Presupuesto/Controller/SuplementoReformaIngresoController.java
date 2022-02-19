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
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
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
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.util.Jsf22Helper;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "suplementosIngresoView")
@ViewScoped
public class SuplementoReformaIngresoController extends BpmnBaseRoot implements Serializable {

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
    @Inject
    private ClienteService clienteService;

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
    private boolean btnRegistrar;
    private boolean panel1, panel2, registrarSolicitud, panel3;
    private LazyModel<ReformaIngresoSuplemento> lazyReformas;
    private List<CatalogoItem> estadoFiltros;
    @Inject
    private CatalogoService catalogoService1;
    @Inject
    private DistributivoAnexoService distributivoAnexoService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
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
    private List<PresFuenteFinanciamiento> tipoFuente;
    private List<ProformaIngreso> listaPresupuestoIngreso;
    private String nombreUsuario;
    //NUEVO
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
                listaPosicion = new ArrayList<>();
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.suplementoIngreso = new ReformaIngresoSuplemento();
                this.detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
                this.listaPeridos = suplementoIngresoService.getListaPeriodosAprobados(true, true);
                this.suplementoIngreso.setTipo(true);
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
                nombreUsuario = userSession.getNameUser();
                panel1 = true;
                panel2 = false;
                panel3 = false;
                lazyReformas = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReformas.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));

                estadoFiltros = catalogoService.MostarTodoCatalogo("estado_solicitud");
                listaVerificador = suplementoIngresoService.getSolictudesTramite(BigInteger.valueOf(tramite.getNumTramite()));
                enabledReformas = true;
                ensabledNuevo = false;
                tipoFuente = presFuenteFinanciamientoService.getFuenteFinanciamiento();
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

    public void showPaneles() {
        panel1 = true;
        panel2 = false;
        panel3 = false;
        enabledReformas = true;
        ensabledNuevo = false;
    }

    public void registraSolicitudNuevo() {
        panel1 = false;
        panel2 = true;
        panel3 = false;
        enabledReformas = false;
        ensabledNuevo = true;
    }

    public void anadirFuentes() {
        panel1 = false;
        panel2 = false;
        panel3 = true;
        enabledReformas = false;
        ensabledNuevo = true;
    }

    public void cancelar() {
        btnRegistrar = true;
        suplementoIngreso = new ReformaIngresoSuplemento();
        detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
        totalSuplemeto = 0;
        totalCodificado = 0;
        totalReducido = 0;
        this.suplementoIngreso.setTipo(true);
        totalPresupuestoInicial = 0;
        this.listaItemReforma.clear();
        this.listaFiltroItem = new ArrayList<>();
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

    public void clearAllFilters(int a) {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("fornGeneral:cuentas");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("fornGeneral:cuentas");
        }
    }

    public void nuevoRegistro() {
        Calendar fecha = new GregorianCalendar();
        suplementoIngreso = new ReformaIngresoSuplemento();
        detalleSuplementoIngreso = new DetalleReformaIngresoSuplemento();
        totalSuplemeto = 0;
        totalCodificado = 0;
        totalReducido = 0;
        totalPresupuestoInicial = 0;

        this.suplementoIngreso.setTipo(true);
        this.listaFiltroItem = new ArrayList<>();
        this.suplementoIngreso.setFechaOficioReforma(new Date());
        this.listaItemReforma.clear();
        btnRegistrar = false;
        int fechaActual = fecha.get(Calendar.YEAR);

        String anio = String.valueOf(fechaActual);

        this.suplementoIngreso.setPeriodo(Short.valueOf(anio));

    }

    public void cargarDataTabla() {
        listaItemReforma = new ArrayList<>();
        totalPresupuestoInicial = BigDecimal.ZERO.doubleValue();
        totalSuplemeto = BigDecimal.ZERO.doubleValue();
        totalReducido = BigDecimal.ZERO.doubleValue();
        totalCodificado = BigDecimal.ZERO.doubleValue();
        if (suplementoIngreso.getFechaOficioReforma() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "PRIMERO TIENE QUE DARLE CLICK AL BOTON NUEVO QUE SE ENCUENTRA EN LA PARTE SUPERIOR DERECHA");
            return;
        }
        if (!verificarMes()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "NO PUEDE REALIZAR ESTA REFORMA PORQUE EL MES SE ENCUENTRA CERRADO PARA EL AÑO REQUIRIENTE");
            return;

        }

        listaPresupuestoIngreso = new ArrayList<>();
        listaPresupuestoIngreso = detalleSuplementoService.getPresupuestoIngresoVerificador(suplementoIngreso.getPeriodo());

        if (!listaPresupuestoIngreso.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "INGRESE LA FUENTE DE FINANCIAMIENTO A LOS REGISTROS QUE SE MOSTRARAN A CONTINUACIÓN");
            abrirDlogFuentePresupuesto();

        } else {

            List<CatalogoProformaPresupuesto> lis = suplementoIngresoService.getPeriodoVer(suplementoIngreso.getPeriodo());

            if (lis.size() != 2) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Error", "Tiene que estar aprobado el de Ingreso como el de Egreso");
                return;
            }

            Long id = 0L;

            this.listaFiltroNivel = suplementoIngresoService.getListaNiveles(suplementoIngreso.getPeriodo(), true);
            this.listaFiltroItem = suplementoIngresoService.getListaItemFiltro(suplementoIngreso.getPeriodo(), true);

            List<PresCatalogoPresupuestario> listaTempItems = detalleSuplementoService.listaItemNew(suplementoIngreso.getPeriodo());
            for (PresCatalogoPresupuestario data : listaTempItems) {
                ProformaIngreso proTemp = new ProformaIngreso();
                proTemp.setItem(data);
                proTemp.setFechaCreacion(new Date());
                proTemp.setFechaModificacion(new Date());
                proTemp.setPresupuestoInicial(BigDecimal.ZERO);
                proTemp.setPresupuestoCodificado(BigDecimal.ZERO);
                proTemp.setReformaReduccion(BigDecimal.ZERO);
                proTemp.setReformaSuplementaria(BigDecimal.ZERO);
                proTemp.setUsuarioCreacion(userSession.getNameUser());
                proTemp.setUsuarioModificacion(userSession.getNameUser());
                proTemp.setPeriodo(suplementoIngreso.getPeriodo());
                System.out.println("Wiii >> "+proTemp);
                service.persist(proTemp);
            }

            listaPosicion = new ArrayList<>();
            this.listaItem = suplementoIngresoService.getListaCatalogoPresupuesto(suplementoIngreso.getPeriodo(), true);
            for (ProformaIngreso data : listaItem) {

                DetalleReformaIngresoSuplemento item = new DetalleReformaIngresoSuplemento();
                item.setProformaIngreso(data);
                item.setReforma(null);
                item.setReducido(BigDecimal.ZERO);
                item.setSuplemento(BigDecimal.ZERO);
                item.setCodificado(data.getPresupuestoCodificado());
                item.setId(id);
                listaItemReforma.add(item);

                listaPosicion.add(new PosicionRefomaIngreso(id.intValue(), item.getProformaIngreso().getId()));

                id = id + 1;
            }

            actualizarTotales();
        }
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

        o.setReducido(BigDecimal.ZERO);
        o.setCodificado(d.getProformaIngreso().getPresupuestoCodificado().add(d.getSuplemento()));

        int posicionTemp = this.listaItemReforma.indexOf(d);
        this.listaItemReforma.remove(posicionTemp);
        this.listaItemReforma.add(posicionTemp, o);

        sumaArbol(o);
        actualizarTotales();

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

    public void saveReofomaSuplmento() {

        if (suplementoIngreso.getPeriodo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Eliga un Período");
            return;
        }
        if (listaItemReforma.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No hay datos que registrar");
            return;
        }

        if (totalSuplemeto < 1) {

            JsfUtil.addWarningMessage("Advertencia", "No hay ningun valor de suplemento");
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
                suplementoIngreso.setNumeracion(suplementoIngresoService.maxNumeracion());
                suplementoIngreso.setEstado(suplementoIngresoService.getlistaEstado("E", "estado_solicitud"));
                suplementoIngreso.setUsuarioCreacion(userSession.getNameUser());
                suplementoIngreso.setUsuarioModificacion(userSession.getNameUser());
                suplementoIngreso.setFechaCreacion(new Date());
                suplementoIngreso.setFechaModificacion(new Date());
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

                // crearPartidasDistributivo(suplementoIngreso);               
                //crearPartidasDistributivoAnexo(suplementoIngreso);
                //catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RR")
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
            listaVerificador = suplementoIngresoService.getSolictudesTramite(BigInteger.valueOf(tramite.getNumTramite()));

            if (!listaVerificador.isEmpty()) {
                verificadorSolicitud = false;
            } else {
                verificadorSolicitud = true;
            }
            panel1 = true;
            panel2 = false;
            enabledReformas = false;
            ensabledNuevo = true;
        } catch (Exception e) {
            btnRegistrar = false;
            System.out.println("error " + e);
            e.printStackTrace();
        }
        //getItemByCatalogoAndCodigo
    }

    /**
     *
     * @param periodo
     * @param c
     * @param estado
     * @param num
     *
     * Se crear un copia del PAPP original pader reaizar una reforma y alterar
     * el monto de esos producto para despues aprobarlo o rechazarlo
     */
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
        System.out.println("DISTRIBUTIVO AND DISTRIBUTIVO ANEXO CREATED...!");
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

//    public void crearPartidasDistributivoAnexo(ReformaIngresoSuplemento r) {
//        ReformaIngresoSuplemento reformaGlobal = new ReformaIngresoSuplemento();
//        reformaGlobal = r;
//        short periodo = r.getPeriodo();
//
//        List<DistributivoAnexo> listaDistributivoAnexo = new ArrayList<>();
//
//        List<PartidasDistributivoAnexo> listavVerificacion = PartidaDisAnexoService.getPartidasAnexoReforma(BigInteger.valueOf(r.getId()));
//        if (listavVerificacion.size() < 1) {
//            listaDistributivoAnexo = distributivoAnexoService.getDisAnexosNoExistInPartidaAnexo(periodo);
//
//            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RD");
//            if (!listaDistributivoAnexo.isEmpty()) {
//                PartidasDistributivoAnexo partidaDistributivoAnexo = new PartidasDistributivoAnexo();
//                for (DistributivoAnexo item : listaDistributivoAnexo) {
//                    partidaDistributivoAnexo = new PartidasDistributivoAnexo();
//                    partidaDistributivoAnexo.setDistributivoAnexo(item);
//                    partidaDistributivoAnexo.setEstado(Boolean.TRUE);
//                    partidaDistributivoAnexo.setPeriodo(periodo);
//                    partidaDistributivoAnexo.setUsuarioCreacion(userSession.getName());
//                    partidaDistributivoAnexo.setFechaCreacion(new Date());
//                    partidaDistributivoAnexo.setFechaModificacion(new Date());
//                    partidaDistributivoAnexo.setUsuarioModificacion(userSession.getName());
//                    partidaDistributivoAnexo.setEstadoPartida(estadoRegistarAprobado);
//                    partidaDistributivoAnexo.setReformaSuplemento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoIncremento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setMonto(item.getMonto());
//                    partidaDistributivoAnexo.setReformaCodificado(item.getMonto());
//                    partidaDistributivoAnexo = PartidaDisAnexoService.create(partidaDistributivoAnexo);
//
//                }
//            }
//
//            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            List<PartidasDistributivoAnexo> listaPartidasReforma = PartidaDisAnexoService.getPartidasAnexo(estadoAprobado, periodo);
//
//            if (!listaPartidasReforma.isEmpty()) {
//                PartidasDistributivoAnexo reformaPartida = new PartidasDistributivoAnexo();
//
//                CatalogoItem estadoAprobadoItem = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");
//                for (PartidasDistributivoAnexo item : listaPartidasReforma) {
//                    reformaPartida.setDistributivoAnexo(item.getDistributivoAnexo());
//                    reformaPartida.setEstado(Boolean.TRUE);
//                    reformaPartida.setPeriodo(item.getPeriodo());
//                    reformaPartida.setUsuarioCreacion(userSession.getName());
//                    reformaPartida.setFechaCreacion(item.getFechaCreacion());
//                    reformaPartida.setFechaCreacion(new Date());
//                    reformaPartida.setUsuarioModificacion(userSession.getName());
//                    reformaPartida.setEstadoPartida(estadoAprobadoItem);
//                    reformaPartida.setCodigoReforma(BigInteger.valueOf(r.getId()));
//                    reformaPartida.setCodigoReferencia(BigInteger.valueOf(item.getId()));
//                    reformaPartida.setItemApA(item.getItemApA());
//                    reformaPartida.setEstructuraApA(item.getEstructuraApA());
//                    reformaPartida.setFuenteApA(item.getFuenteApA());
//                    reformaPartida.setFuenteDirectaA(item.getFuenteDirectaA());
//                    reformaPartida.setPartidaAp(item.getPartidaAp());
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoIncremento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoReduccion(BigDecimal.ZERO);
//                    reformaPartida.setMonto(item.getReformaCodificado());
//                    reformaPartida.setReformaCodificado(item.getReformaCodificado());
//                    reformaPartida = PartidaDisAnexoService.create(reformaPartida);
//                    reformaPartida = new PartidasDistributivoAnexo();
//
//                }
//
//            }
//
//            List<PartidasDistributivoAnexo> listaPartidasAnexo = new ArrayList<>();
//            listaPartidasAnexo = PartidaDisAnexoService.getDisAnexosEstadoFalse(periodo);
//            if (!listaPartidasAnexo.isEmpty()) {
//                for (PartidasDistributivoAnexo item : listaPartidasAnexo) {
//
//                    PartidaDisAnexoService.remove(item);
//                }
//            }
//
//        }
//
//        System.out.println("DISTRIBUTIVO ANEXO CREATED...!");
//
//    }
    public void crearPartidasDistributivo(ReformaIngresoSuplemento r) {
        ReformaIngresoSuplemento reformaDistr = new ReformaIngresoSuplemento();
        reformaDistr = r;
        Short periodo = r.getPeriodo();
        listaValoresDistributivo = new ArrayList<>();
        copiaDiReforma = new ArrayList<>();
        List<PartidasDistributivo> verificarList = valoresService.verificarReformaDis(BigInteger.valueOf(r.getId()));
        if (verificarList.isEmpty()) {

            listaValoresDistributivo = valoresService.getObjetoDistributivo(periodo);
            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RD");
            if (listaValoresDistributivo.size() > 0) {
                PartidasDistributivo partidaDistributivo = new PartidasDistributivo();
                for (ValoresDistributivo item : listaValoresDistributivo) {
                    partidaDistributivo = new PartidasDistributivo();
                    partidaDistributivo.setDistributivo(item);
                    partidaDistributivo.setEstado(Boolean.TRUE);
                    partidaDistributivo.setPeriodo(periodo);
                    partidaDistributivo.setUsuarioCreacion(userSession.getName());
                    partidaDistributivo.setFechaCreacion(new Date());
                    partidaDistributivo.setUsuarioModificacion(userSession.getName());
                    partidaDistributivo.setFechaModificacion(new Date());
                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setMonto(item.getValorResultante());
                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
                    partidaDistributivo = partidasService.create(partidaDistributivo);
                }
            }

            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(periodo, estadoAprobado);

            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");

            if (!copiaRubrosPartidasReforma.isEmpty()) {
                PartidasDistributivo item = new PartidasDistributivo();
                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {

                    item.setDistributivo(data.getDistributivo());
                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
                    item.setCodigoReforma(BigInteger.valueOf(r.getId()));
                    item.setEstado(data.isEstado());
                    item.setEstadoPartida(estado);
                    item.setEstructuraAp(data.getEstructuraAp());
                    item.setItemAp(data.getItemAp());
                    item.setFechaCreacion(new Date());
                    item.setFechaModificacion(new Date());
                    item.setFuenteAp(data.getFuenteAp());
                    item.setFuenteDirecta(data.getFuenteDirecta());
                    item.setPartidaAp(data.getPartidaAp());
                    item.setPeriodo(data.getPeriodo());
                    item.setUsuarioCreacion(userSession.getNameUser());
                    item.setUsuarioModificacion(userSession.getNameUser());
                    item.setReformaSuplemento(BigDecimal.ZERO);
                    item.setReformaReduccion(BigDecimal.ZERO);

                    item.setReformaCodificado(data.getReformaCodificado());

                    item.setMonto(data.getReformaCodificado());

                    item = partidasService.create(item);
                    item = new PartidasDistributivo();
                }

            }
        }
        System.out.println("DISTRIBUTIVO CREATED...!");
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("fmreformas1:datareformas");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("fmreformas1:datareformas");
        }
    }

    public BigDecimal totalSuplemento(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
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

    public void abrirDlogFuentePresupuesto() {
        PrimeFaces.current().executeScript("PF('dlgFuente').show()");
        PrimeFaces.current().ajax().update(":frmDlgoFuente");
    }

    public void editarFuente(ProformaIngreso d) {
        service.update(d);
        JsfUtil.addInformationMessage("INFORMACIÓN", "FUENTE AÑADIDA CORRECTAMENTE");
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
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

    public void verificarfuente() {
        if (suplementoIngreso.getFechaOficioReforma() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "PRIMERO TIENE QUE DARLE CICK AL BOTON NUEVO QUE SE ENCUENTRA EN LA PARTE SUPERIOR DERECHA");
            return;
        }

        if (!verificarMes()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "NO PUEDE REALIZAR ESTA REFORMA PORQUE EL MES SE ENCUENTRA CERRADO PARA EL AÑO REQUIRIENTE");
            return;

        }

        listaPresupuestoIngreso = new ArrayList<>();
        listaPresupuestoIngreso = detalleSuplementoService.getPresupuestoIngreso(suplementoIngreso.getPeriodo());

        if (!listaPresupuestoIngreso.isEmpty()) {
            abrirDlogFuentePresupuesto();

        } else {
            JsfUtil.addWarningMessage("AVISO", "NO HAY DATOS");
        }
    }

    public void placeOrder(ActionEvent ae) {
        //simulating order placement
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public LazyModel<ReformaIngresoSuplemento> getLazyReformas() {
        return lazyReformas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<ProformaIngreso> getListaPresupuestoIngreso() {
        return listaPresupuestoIngreso;
    }

    public void setListaPresupuestoIngreso(List<ProformaIngreso> listaPresupuestoIngreso) {
        this.listaPresupuestoIngreso = listaPresupuestoIngreso;
    }

    public List<PresFuenteFinanciamiento> getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(List<PresFuenteFinanciamiento> tipoFuente) {
        this.tipoFuente = tipoFuente;
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

    public boolean isRegistrarSolicitud() {
        return registrarSolicitud;
    }

    public void setRegistrarSolicitud(boolean registrarSolicitud) {
        this.registrarSolicitud = registrarSolicitud;
    }

    public boolean isVerificadorSolicitud() {
        return verificadorSolicitud;
    }

    public void setVerificadorSolicitud(boolean verificadorSolicitud) {
        this.verificadorSolicitud = verificadorSolicitud;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public boolean isBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
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

    public boolean isPanel3() {
        return panel3;
    }

    public void setPanel3(boolean panel3) {
        this.panel3 = panel3;
    }

//</editor-fold>
}
