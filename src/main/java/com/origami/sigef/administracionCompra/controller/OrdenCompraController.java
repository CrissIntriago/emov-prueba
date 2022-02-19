/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.controller;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.CatalogoExistenciasService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.ResponsableAdquisicionService;
import com.origami.sigef.administracionCompra.Model.BienesInventarioModel;
import com.origami.sigef.administracionCompra.service.CaracteristicaTecnicaService;
import com.origami.sigef.administracionCompra.service.DetallePresupuestoService;
import com.origami.sigef.administracionCompra.service.EspecificacionTecnicaService;
import com.origami.sigef.administracionCompra.service.ObligacionResponsableService;
import com.origami.sigef.administracionCompra.service.SolicitudOrdenCompraService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.CaracteristicaTecnica;
import com.origami.sigef.common.entities.CatalogoExistencias;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.DetallePresupuesto;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.EspecificacionTecnica;
import com.origami.sigef.common.entities.ObligacionResponsable;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.ResponsableAdquisicion;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "ordenCompraView")
@ViewScoped
public class OrdenCompraController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(OrdenCompraController.class.getName());

    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private SolicitudOrdenCompraService ordenCompraService;
    @Inject
    private CaracteristicaTecnicaService caracteristicaService;
    @Inject
    private EspecificacionTecnicaService especificacionService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private DatosGeneralesEntidadService datosGeneralesEntidadService;
    @Inject
    private ObligacionResponsableService obligacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private ReservaCompromisoService reservaService;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ResponsableAdquisicionService responsableAdquisicionService;
    @Inject
    private DetallePresupuestoService detallePresupuestoService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private CatalogoExistenciasService catExistenciaService;
    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private ThServidorCargoService thServidorCargoService;

    //<editor-fold defaultstate="collapsed" desc="Atributos">
    private List<CatalogoItem> tipoCompraList;
    private List<CatalogoItem> tipoMedidaList;
    private List<CatalogoItem> tipoPagoPlazo;
    private List<CatalogoItem> subTiposAdquisicion;
    private List<CatalogoItem> tiposProcesos;
    private List<EspecificacionTecnica> especificacionesList;
    private List<CaracteristicaTecnica> caracteristicasList;
    private List<ResponsableAdquisicion> responsableList;
    private List<ObligacionResponsable> obligacionRespList;
    private List<SolicitudReservaCompromiso> reservaList;
    private List<DetalleSolicitudCompromiso> detalleReservaList;
    private UploadedFile files;

    private LazyModel<SolicitudOrdenCompra> lazySolicitud;
    private LazyModel<SolicitudOrdenCompra> lazyProcesa;
    private LazyModel<SolicitudOrdenCompra> lazy;

    private SolicitudOrdenCompra ordenCompra;
    private OpcionBusqueda busqueda;
    private CaracteristicaTecnica caracteristicaTecnica;
    private EspecificacionTecnica especificacionTecnica;
    private Numero_Letras numeroLetra;
    private DatosGeneralesEntidad entidad;
    private ObligacionResponsable obligacionResponsable;
    private SolicitudReservaCompromiso reservaCompromiso;
    private DetalleSolicitudCompromiso detalleReserva;
    private Adquisiciones adquisicion;
    private ResponsableAdquisicion responsableAdqui;
    private CatalogoItem estadoProceso;
    private CatalogoItem estadoRechazado;
    private CatalogoItem estadoLegalizado;
    private CatalogoItem estadoNotificado;
    private CatalogoItem estadoAprobado;
    private CatalogoExistencias catalogoExistencia;
    private List<CatalogoExistencias> listCatalogoExistencias;

    private String codigo;
    private String caracteristicaString;
    private String consideraciones;
    private String valorAbjudicado;
    private String plazoEntrega;
    private String formaCondicionPlazo;
    private String lugarEntregaPlazo;
    private String multa;
    private String vigencia;
    private String solucionControversia;
    private String terminacionOrden;
    private String notificacionTerm;
    private String detalleCertificacion;
    private String detallePresupuestoResfe;
    private BigInteger numTramite;
    private int numCaracteristica;
    private int codTarea;
    private int numEspecificacion;
    private int numObligacionResponsable;
    private boolean skip;
    private boolean aplicaIva = Boolean.FALSE;
    private BigDecimal presupuestoTotal;
    private BigDecimal valorTotalSin;
    private Boolean editCaracteristicas;
    private Boolean editEspecificaciones;
    private Boolean completeTextRendered;
    private Boolean btnCompletarTarea;
    private Boolean editObligacion;
    private Boolean btnDetalle;
    private Integer tipo = 0;
    private BienesInventarioModel detalleBien;
    private BienesInventarioModel detalleInventario;
    private List<BienesInventarioModel> resultModelBienes;

    private Converter bienesConverter = new Converter() {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            BienesInventarioModel orElse = null;
            try {
                System.out.println("resultModelBienes " + resultModelBienes.size());
                orElse = resultModelBienes.stream()
                        .filter(p -> (p.getDescripcion() + ":" + p.getCodigo()).equals(value)).findFirst().orElse(null);
            } catch (Exception e) {
                System.out.println("Error converter " + e);
            }
            return orElse;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return (value != null)
                    ? (((BienesInventarioModel) value).getDescripcion() + ":" + ((BienesInventarioModel) value).getCodigo())
                    : null;
        }
    };
//</editor-fold>

    @PostConstruct
    //<editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public void init() {

        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
                numCaracteristica = 0;
                numEspecificacion = 0;
                numObligacionResponsable = 0;
                consideraciones = "";
                plazoEntrega = "";
                formaCondicionPlazo = "";
                lugarEntregaPlazo = "";
                multa = "";
                vigencia = "";
                solucionControversia = "";
                terminacionOrden = "";
                notificacionTerm = "";
                detalleCertificacion = "";
                detallePresupuestoResfe = "";
                skip = false;
                btnDetalle = false;
                tipoMedidaList = catalogoItemService.findCatalogoItems("sistema_unidad_medida");
//                tipoCompraList = adquisicionesService.getTipoProcesosOC("tipo_adquisicion_servicios", "tipo_adquisicion_bienes");
                tiposProcesos = adquisicionesService.getTipoProcesosOC("tipo_proceso_catalogo", "tipo_proceso_infima");
                tipoPagoPlazo = catalogoItemService.findCatalogoItems("tipo_pago_plazo");
                subTiposAdquisicion = adquisicionesService.getTipos("sub_tipo_adquisicion");
                reservaList = reservaService.getReservaCompromisoApro();
                detalleReservaList = new ArrayList<>();
                especificacionesList = new ArrayList<>();
                caracteristicasList = new ArrayList<>();
                responsableList = new ArrayList<>();
                obligacionRespList = new ArrayList<>();
                ordenCompra = new SolicitudOrdenCompra();
                ordenCompra.setAdquisicion(new Adquisiciones());
                ordenCompra.setFechaElaboracion(new Date());
                ordenCompra.setAplicaIva(Boolean.FALSE);
                ordenCompra.setAplicaImpuesto(Boolean.FALSE);
                ordenCompra.setValorImpuesto(BigDecimal.ZERO);
                busqueda = new OpcionBusqueda();
                numeroLetra = new Numero_Letras();
                entidad = datosGeneralesEntidadService.getEntidad();
                obligacionResponsable = new ObligacionResponsable();
                reservaCompromiso = new SolicitudReservaCompromiso();
                detalleReserva = new DetalleSolicitudCompromiso();
                adquisicion = new Adquisiciones();
                adquisicion.setProveedor(new Proveedor());
                adquisicion.getProveedor().setCliente(new Cliente());
                responsableAdqui = new ResponsableAdquisicion();
                estadoProceso = catalogoItemService.getEstadoConstatacion("R", "estado_solicitud");
                estadoRechazado = catalogoItemService.getEstadoConstatacion("O", "estado_solicitud");
                estadoAprobado = catalogoItemService.getEstadoConstatacion("APRO", "estado_solicitud");
                estadoLegalizado = catalogoItemService.getEstadoConstatacion("LEG", "estado_solicitud");
                estadoNotificado = catalogoItemService.getEstadoConstatacion("LIQUI", "estado_solicitud");
                lazySolicitud = new LazyModel<>(SolicitudOrdenCompra.class);
                lazyProcesa = new LazyModel<>(SolicitudOrdenCompra.class);
                lazySolicitud.getFilterss().put("estado", true);
                lazySolicitud.getFilterss().put("numeroTramite", new BigInteger("" + this.tramite.getNumTramite()));
                lazyProcesa.getFilterss().put("estadoProceso", estadoProceso);
                lazyProcesa.getFilterss().put("estado", true);
                lazyProcesa.getFilterss().put("numeroTramite", new BigInteger("" + this.tramite.getNumTramite()));
                lazySolicitud.getSorteds().put("codigoOrden", "ASC");
                resultModelBienes = new ArrayList<>();
                accionesCatalogoElectronico();
            }
        } else {
            lazy = new LazyModel<>(SolicitudOrdenCompra.class);
            lazy.getFilterss().put("estado", true);
        }
    }
//</editor-fold>

    public void accionesCatalogoElectronico() {
        if (consultar()) {
            btnCompletarTarea = true;
            this.adquisicion.setTipoProceso(catalogoItemService.getEstadoRol("tipo_proceso_catalogo"));
            tipoCompraList();
        } else {
            btnCompletarTarea = false;
        }
    }

    public boolean consultar() {
        return ordenCompraService.findCatalogoElectronico(numTramite);
    }

    public boolean anularTramite() {
        return ordenCompraService.findSinProcesar(numTramite);
    }

    public void guardar(boolean reset) {
        boolean edit = ordenCompra.getId() != null;
        try {
            crearAdquisicion(edit);
            if (edit) {
                ordenCompra.setUsuarioModifica(userSession.getNameUser());
                ordenCompra.setFechaModifica(new Date());
                ordenCompra.setAdquisicion(adquisicion);
                ordenCompra.setEstadoProceso(estadoProceso);
                ordenCompraService.edit(ordenCompra);
                addArchivo();
            } else {
                ordenCompra.setFechaCreacion(new Date());
                ordenCompra.setUsuarioCreacion(userSession.getNameUser());
                ordenCompra.setAdquisicion(adquisicion);
                ordenCompra.setCodigoOrden(codigo);
                ordenCompra.setEstadoProceso(estadoProceso);
                ordenCompra.setNumeroTramite(numTramite);
                ordenCompra.setIdTramite(this.tramite.getId());
                ordenCompra.setElaboracionDoc(userSession.getUsuario().getFuncionario().getPersona().getNombreCompleltoSql());
                ordenCompra.setEmailSolicitante(userSession.getUsuario().getFuncionario().getEmailInstitucion());
                ordenCompra = ordenCompraService.create(ordenCompra);
            }
            guardarCaracteristicas();
            guardarDetallePresupuesto();
            guardarEspecificaciones();
            guardarObligaciones();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
        if (reset) {
            resetValue();
            if (consultar()) {
                accionesCatalogoElectronico();
            }
        }
        JsfUtil.addSuccessMessage("Registro", "Datos registrados con éxito.");
    }

    public void continuarTarea(SolicitudOrdenCompra sol) {
        ordenCompra = sol;
        this.codTarea = 1;
        observacion = new Observaciones();
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void openDlgObservacion(int valor) {
        this.codTarea = valor;
        observacion = new Observaciones();
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void continuarTarea() {
        try {
            switch (codTarea) {
                case 0:
                    getParamts().put("aprobado", 0);
                    List<SolicitudOrdenCompra> temp = ordenCompraService.getListSolicitud(numTramite);
                    for (SolicitudOrdenCompra item : temp) {
                        item.setEstado(Boolean.FALSE);
                        ordenCompraService.edit(item);
                    }
                    break;
                case 1:
                    getParamts().put("aprobado", 1);
                    if (consultar()) {
                        getParamts().put("aprobado", 0);
                        getParamts().put("usuario_2", clienteService.getrolsUser(RolUsuario.responsableAdministrtivo));
//                        getParamts().put("usuarioResponsable", ordenCompraService.getResponsable(numTramite));
//                        getParamts().put("urlForm", "/proceso/ordenCompra/ingresoInvCE");
                    } else {
                        getParamts().put("aprobado", 1);
                        getParamts().put("usuario_2", clienteService.getrolsUser(RolUsuario.responsableAdministrtivo));
                    }
                    break;
                case 2:
                    getParamts().put("aprobado", 2);
                    getParamts().put("usuario2", clienteService.getrolsUser(RolUsuario.maximaAutoridad));
                    break;
            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void crearAdquisicion(boolean edit) {
        try {
            if (edit) {
                adquisicion.setFechaModificacion(new Date());
                adquisicion.setUsuarioModificacion(userSession.getNameUser());
                adquisicion.setFechaEmision(ordenCompra.getFechaElaboracion());
                adquisicion.setDescripcion(ordenCompra.getObjetoContratacion());
                adquisicionesService.edit(adquisicion);
                if (responsableAdqui.getId() != null) {
                    responsableAdqui.setAdquisicion(adquisicion);
                    responsableAdquisicionService.edit(responsableAdqui);
                } else if (responsableAdqui.getResponsable() != null) {
                    responsableAdqui.setAdquisicion(adquisicion);
                    responsableAdquisicionService.create(responsableAdqui);
                }
            } else {
                adquisicion.setFechaCreacion(new Date());
                adquisicion.setUsuarioCreacion(userSession.getNameUser());
                adquisicion.setFechaAceptacion(ordenCompra.getFechaElaboracion());
                adquisicion.setFechaContrato(ordenCompra.getFechaElaboracion());
                adquisicion.setFechaEmision(ordenCompra.getFechaElaboracion());
                adquisicion.setNumeroContrato(codigo);
                adquisicion.setIdProceso(codigo);
                adquisicion.setNumTramite(numTramite.longValue());
                adquisicion.setDescripcion(ordenCompra.getObjetoContratacion());
                adquisicion = adquisicionesService.create(adquisicion);
                if (responsableAdqui.getResponsable() != null) {
                    responsableAdqui.setAdquisicion(adquisicion);
                    responsableAdquisicionService.create(responsableAdqui);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void generalSolicitud(SolicitudOrdenCompra sol) {
        Boolean metodologia = Boolean.FALSE;
        Boolean caracteristicas = Boolean.FALSE;
        Boolean catalogo = Boolean.TRUE;
        String nombreReporte = "ORDEN DE COMPRA";
        List<ResponsableAdquisicion> responsablesList = responsableAdquisicionService.getListaDeResponsablesActivo(sol.getAdquisicion());
        ResponsableAdquisicion responsable = new ResponsableAdquisicion();
        for (ResponsableAdquisicion r : responsablesList) {
            if (r.getEstado() == true && r.getFechaFinalizacion() == null) {
                responsable = r;
                break;
            }
        }
        adquisicion = new Adquisiciones();
        adquisicion = sol.getAdquisicion();
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_alimento_bebidas")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_repuesto_acce"))) {
            caracteristicas = Boolean.TRUE;
            metodologia = Boolean.FALSE;
//            System.out.println("if 1");
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")) {
            caracteristicas = Boolean.TRUE;
            metodologia = Boolean.TRUE;
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima") && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_servicios")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_seguros")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_mant_obras")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_arrendamiento"))) {
            caracteristicas = Boolean.FALSE;
            metodologia = Boolean.TRUE;
//            System.out.println("if 2");
        }
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            catalogo = Boolean.FALSE;
            nombreReporte = "CATALOGO ELECTRONICO";
//            System.out.println("if 2");
        }
        ThServidorCargo thServidorCargo = null;
        if (responsable.getId() != null) {
            thServidorCargo = thServidorCargoService.findByThServidor(responsable.getResponsable());
        }
//        System.out.println("tipo 1 " + metodologia);
//        System.out.println("tipo 2 " + caracteristicas);
        serveltSession.addParametro("id_orden", sol.getId());
        serveltSession.addParametro("METODOLOGIA_CARACTERISTICAS", metodologia);
        serveltSession.addParametro("NOMBRE_REPORTE", nombreReporte);
        serveltSession.addParametro("CARACTERISTICAS_BIEN", caracteristicas);
        serveltSession.addParametro("CATALOGO_ELECTRONICO", true);
//        serveltSession.addParametro("CATALOGO_ELECTRONICO", catalogo);
        serveltSession.addParametro("nombreCompleto", responsable.getId() != null ? responsable.getResponsable().getPersona().getNombreCompleto() : "RESPONSABLE NO ASIGNADO");
//        serveltSession.addParametro("cargo", responsable.getId() != null ? responsable.getResponsable().getDistributivo().getCargo().getNombreCargo() : "RESPONSABLE NO ASIGNADO");
        serveltSession.addParametro("cargo", thServidorCargo != null ? thServidorCargo.getIdCargo().getNombreCargo() : "RESPONSABLE NO ASIGNADO");
        serveltSession.addParametro("correo", responsable.getId() != null ? responsable.getResponsable().getEmailInstitucion() : "RESPONSABLE NO ASIGNADO");
        serveltSession.addParametro("telefonos", responsable.getId() != null ? responsable.getResponsable().getPersona().getCelular() + "-" + responsable.getResponsable().getPersona().getTelefono() : "RESPONSABLE NO ASIGNADO");
        serveltSession.setNombreReporte("OrdenCompra");
        serveltSession.setNombreSubCarpeta("SolicitudOC");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

//<editor-fold defaultstate="collapsed" desc="Guardar Listas">
    public void guardarCaracteristicas() {
        if (!caracteristicasList.isEmpty()) {
            try {
                for (CaracteristicaTecnica c : caracteristicasList) {
                    if (c.getId() == null) {
                        c.setOrdenCompra(ordenCompra);
                        caracteristicaService.create(c);
                    } else {
                        caracteristicaService.edit(c);
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Select ", e);
            }
        }
    }

    public void guardarEspecificaciones() {
        if (!especificacionesList.isEmpty()) {
            try {
                for (EspecificacionTecnica e : especificacionesList) {
                    if (e.getId() == null) {
                        e.setOrdenCompra(ordenCompra);
                        especificacionService.create(e);
                    } else {
                        especificacionService.edit(e);
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Select ", e);
            }
        }
    }

    public void guardarObligaciones() {
        if (!obligacionRespList.isEmpty()) {
            int num = 0;
            try {
                for (ObligacionResponsable o : obligacionRespList) {
                    if (o.getId() == null) {
                        o.setNumero((short) num++);
                        o.setOrdenCompra(ordenCompra);
                        obligacionService.create(o);
                    } else {
                        obligacionService.edit(o);
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Select ", e);
            }
        }
    }

    public void guardarDetallePresupuesto() {
        DetallePresupuesto detallePresuesto;
        if (!detalleReservaList.isEmpty()) {
            try {
                for (DetalleSolicitudCompromiso d : detalleReservaList) {
                    detallePresuesto = new DetallePresupuesto();
                    detallePresuesto.setOrdenCompra(new SolicitudOrdenCompra());
                    detallePresuesto.setDetalleReserva(new DetalleSolicitudCompromiso());
                    detallePresuesto.setOrdenCompra(ordenCompra);
                    detallePresuesto.setDetalleReserva(d);
                    detallePresuesto.setValor(d.getMontoSolicitado());
                    if (ordenCompra.getId() == null) {
                        detallePresuesto = detallePresupuestoService.create(detallePresuesto);
                    } else {
                        detallePresupuestoService.edit(detallePresuesto);
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Select ", e);
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Editar-Eliminar">
    public void editar(SolicitudOrdenCompra oc) {
        List<DetalleSolicitudCompromiso> auxReserList = new ArrayList<>();
        ordenCompra = oc;
        adquisicion = oc.getAdquisicion();
        codigo = oc.getCodigoOrden();
        tiposProcesos = adquisicionesService.getTipoProcesosOC("tipo_proceso_catalogo", "tipo_proceso_infima");
        tipoCompraList();
        caracteristicas_metodologia();
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            tipo = 1;
            btnCompletarTarea = true;
        } else {
            tipo = 0;
            btnCompletarTarea = false;
        }
        caracteristicasList = ordenCompra.getCaracteristicaTecnicaList();
        especificacionesList = ordenCompra.getEspecificacionTecnicaList();
        obligacionRespList = ordenCompra.getObligacionResponsableList();
        auxReserList = reservaService.getDetalleReservaApro(ordenCompra.getReservaCompromiso());
        llenarDetalleReserv(auxReserList);
        //actualizarTiposProcesos();
        responsableList = ordenCompra.getAdquisicion().getResponsableAdquisicionList();
        for (ResponsableAdquisicion r : responsableList) {
            if (r.getEstado() == true && r.getFechaFinalizacion() == null) {
                responsableAdqui = r;
                break;
            }
        }
        JsfUtil.update("formMain");
    }

    public void eliminarSol(SolicitudOrdenCompra sol) {
        sol.setEstado(Boolean.FALSE);
        sol.setUsuarioModifica(userSession.getNameUser());
        sol.setFechaModifica(new Date());
        adquisicion = sol.getAdquisicion();
        ordenCompraService.edit(sol);
        adquisicion.setEstado(Boolean.FALSE);
        adquisicion.setUsuarioCreacion(userSession.getNameUser());
        adquisicion.setFechaEmision(new Date());
        adquisicionesService.edit(adquisicion);
        accionesCatalogoElectronico();
        resetValue();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Llenar Listas">
    public void llenarCarat(List<CaracteristicaTecnica> auxCaratList) {
        if (!auxCaratList.isEmpty()) {
            caracteristicasList = new ArrayList<>();
            for (CaracteristicaTecnica c : auxCaratList) {
                caracteristicasList.add(c);
            }
        }

    }

    public Boolean caracteristicas_metodologia() {
        if (adquisicion.getTipoProceso() != null
                && adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            return Boolean.TRUE;
        }
        if (adquisicion.getTipoAdquisicion() != null
                && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes"))) {
            completeTextRendered = Boolean.TRUE;
        } else {
            completeTextRendered = Boolean.FALSE;
        }
        if (adquisicion.getTipoAdquisicion() != null
                && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_alimento_bebidas")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_repuesto_acce"))) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean caracteristicasDetalle() {
        if (adquisicion.getTipoAdquisicion() != null
                && (adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")
                || adquisicion.getTipoAdquisicion().getCodigo().equals("tipo_repuesto_acce"))) {
            btnDetalle = Boolean.TRUE;
            return Boolean.TRUE;
        } else {
            btnDetalle = Boolean.TRUE;
            return Boolean.FALSE;
        }
    }

    public void tipoCompraList() {
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            ordenCompra.setCodigoProceso("CE-" + Utils.getAnio(new Date()));
            tipoCompraList = adquisicionesService.getTipoProcesosOC("tipo_adquisicion_servicios", "tipo_adquisicion_bienes");
            btnCompletarTarea = true;
        } else {
            ordenCompra.setCodigoProceso("");
            tipoCompraList = adquisicionesService.getTipoProcesosOCInfimas();
            btnCompletarTarea = false;
        }
    }

    public void llenarDetalleReserv(List<DetalleSolicitudCompromiso> auxReserList) {
        if (!auxReserList.isEmpty()) {
            detalleReservaList = new ArrayList<>();
            for (DetalleSolicitudCompromiso d : auxReserList) {
                detalleReservaList.add(d);
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="TAREAS PROCESOS">
    public void completarTareaProceso(int var) {
        try {
            getParamts().put("aprobado", var);
            if (saveTramite() == null) {
                return;
            }
            if (var == 1) {
                if (ordenCompra.getAdquisicion().getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")) {
                    getParamts().put("servicio", 0);
                    getParamts().put("usuario_guardalmacen", ordenCompra.getAdquisicion().getResponsableAdquisicionList().get(0).getResponsable().getUsuario().getUsuario());
                    if (ordenCompra.getAdquisicion().getSubTipoAdquisicion().getCodigo().equals("subtipo_adquisicion_inventario")) {
                        getParamts().put("subProcessBodega", "altaInventario");
                    } else {
                        getParamts().put("subProcessBodega", "procesoAltaBienes");
                    }
                    System.out.println("user " + ordenCompra.getAdquisicion().getResponsableAdquisicionList().get(0).getResponsable().getUsuario().getUsuario());
                } else {
                    getParamts().put("servicio", 1);
                }
                ordenCompra.setEstadoProceso(estadoAprobado);
                generalSolicitud(ordenCompra);
                enviarNotificacion(ordenCompra);
            } else {
                ordenCompra.setEstadoProceso(estadoRechazado);
                getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.responsableAdministrtivo));
                super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
                JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            }
            ordenCompraService.edit(ordenCompra);
            ordenCompra = new SolicitudOrdenCompra();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void enviarNotificacion(SolicitudOrdenCompra sol) {
        sol.setEstadoProceso(estadoNotificado);
        Correo c = new Correo();
        c.setDestinatario(sol.getEmailSolicitante()
        );
        c.setAsunto("APROBACION DE SOLICITUD ORDEN DE COMPRA IC");
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"width:200px;\">SR(a). " + sol.getElaboracionDoc() + "\n POR MEDIO DE LA PRESENTE SE LE INFORMA QUE EL RESPONSABLE ADMINISTRATIVO LEGALIZO Y APROBÓ SU SOLICITUD DE ORDEN DE COMPRA"
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + sol.getNumeroTramite() + " </p>\n"
                + "</body>\n"
                + "</html>");
//            List<CorreoArchivo> archivos = new ArrayList();
//            Map<String, Object> parametros = new HashMap();
//            parametros.put("tramite", impresion.getNumeroTramite());
//            String rutaArchivo = katalinaService.buildJasper(impresion.getId(), "\\activos\\actaEntregaRecepInventario", parametros);
//            CorreoArchivo archivo = new CorreoArchivo("Reporte", Utils.encodeFileToBase64Binary(rutaArchivo), "pdf");
//            archivos.add(archivo);
//            c.setArchivos(archivos);
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de emai: " + sol.getEmailSolicitante() + " relacionada con: " + sol.getElaboracionDoc());
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        ordenCompraService.edit(sol);
    }

    public void completarTareaLegalizar(SolicitudOrdenCompra sol) {
        sol.setEstadoProceso(estadoLegalizado);
        ordenCompraService.edit(sol);
        enviarNotificacion(sol);
//        getParamts().put("usuario4", session.getNameUser());
//        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
//        JsfUtil.redirect(CONFIG.URL_APP + "/procesos/bandeja-tareas");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DETALLE DE SOLICITUD">
    public void modifyValorAdjudicado() {
        BigDecimal valorIva = BigDecimal.ZERO;
        BigDecimal valorImpuesto = BigDecimal.ZERO;
        if (ordenCompra.getValorSubtotal() != null) {
            if (ordenCompra.getAplicaIva() != null) {
                if (ordenCompra.getAplicaIva()) {
                    BigDecimal ivaPorc = BigDecimal.valueOf(12L);
                    valorIva = ordenCompra.getValorSubtotal().multiply(ivaPorc, MathContext.DECIMAL32).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
                }
            }
            if (ordenCompra.getAplicaImpuesto() != null) {
                if (ordenCompra.getAplicaImpuesto()) {
                    valorImpuesto = ordenCompra.getValorSubtotal().multiply(ordenCompra.getValorImpuesto(), MathContext.DECIMAL32).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
                } else {
                    ordenCompra.setValorImpuesto(BigDecimal.ZERO);
                }
            }
        } else {
            JsfUtil.addWarningMessage("Información", "Ingresar Valor Subtotal");
            return;
        }

        adquisicion.setValorContratado(ordenCompra.getValorSubtotal().add(valorIva).add(valorImpuesto));

    }

    public void addValorAbjudicado() {
        if (adquisicion.getValorContratado() == null) {
            JsfUtil.addWarningMessage("Error", "Debe de Agregar un Valor...");
            return;
        }
        valorAbjudicado = "Son: "
                + numeroLetra.convertir(adquisicion.getValorContratado() + "", true) + (ordenCompra.getAplicaIva() ? " INCLUIDO IVA. " : " NO INCLUYE IVA.");
//        valorAbjudicado = "El valor adjudicado es $" + adquisicion.getValorContratado()
//                + " (" + numeroLetra.convertir(adquisicion.getValorContratado() + "", true) + "), incluye IVA. ";
        ordenCompra.setDetalleValorAdjudicado(valorAbjudicado);
//        PrimeFaces.current().ajax().update("formMain");
    }

    public void textoInicial() {
        String etiqueta = "";
        int secuencia = 0;
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima")) {
            etiqueta = "OC-IC-";
            secuencia = ordenCompraService.getCantReg("tipo_proceso_infima");
            cargarObligaciones();
            garantiaObligacion();
        } else {
            obligacionRespList = new ArrayList<>();
            ordenCompra.setMulta("");
            secuencia = ordenCompraService.getCantReg("tipo_proceso_catalogo");
            etiqueta = "OC-CE-";
        }
        codigo = etiqueta + Utils.completarCadenaConCeros((secuencia + 1) + "", 3) + "-" + busqueda.getAnio();
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_catalogo")) {
            tipo = 1;
            consideraciones = String.format(valoresService.findByCodigo("ORDER_COMPRA_CONSIDERACION"), "43, 44 de la LOSNCP",
                    adquisicion.getProveedor().getCliente().getNombreCompleto(), adquisicion.getProveedor().getCliente().getIdentificacionCompleta(),
                    ordenCompra.getObjetoContratacion(), "de acuerdo a lo estipulado en el convenio marco respectivo.\n"
                    + "\n"
                    + "Garantías y multas se aplicaran de acuerdo al convenio marco emitido por el SERCOP ");
        } else {
            consideraciones = String.format(valoresService.findByCodigo("ORDER_COMPRA_CONSIDERACION"), "330 al 337 de la RESOLUCIÓN No. RE-SERCOP-2016-0000072",
                    adquisicion.getProveedor().getCliente().getNombreCompleto(), adquisicion.getProveedor().getCliente().getIdentificacionCompleta(),
                    ordenCompra.getObjetoContratacion(), "");
            vigencia = "La presente orden entrará en vigencia a partir de la "
                    + "fecha de suscripción y notificación al proveedor.";
            ordenCompra.setDetalleVigencia(vigencia);
        }
        ordenCompra.setDetalleConsideracion(consideraciones);
        solucionControversia = "De surgir controversias las partes se someten al procedimiento de "
                + "mediación a través del Centro de Mediación de la Procuraduría General del Estado. "
                + "A falta de acuerdo ante los Tribunales Distritales de lo Contencioso Administrativo, "
                + "aplicando para ello la Ley de la Jurisdicción Contenciosa Administrativa. ";
        ordenCompra.setDetalleControversia(solucionControversia);
        terminacionOrden = "La presente orden de compra podrá darse por terminada de conformidad "
                + "con lo previsto en la Ley Orgánica del Sistema Nacional de Contratación Pública "
                + "y su Reglamento General. ";
        ordenCompra.setDetalleTerminacionOrden(terminacionOrden);
        notificacionTerm = "Por medio de la presente se deja constancia de la notificación de la "
                + "presente orden de compra al oferente seleccionado. \n"
                + "\n"
                + "Adicionalmente, los comparecientes libre y voluntariamente declaran su aceptación a "
                + "todo lo convenido, y se someten a las condiciones establecidas en el presente documento.\n"
                + "\n"
                + "Para constancia y cumplimiento firman en dos ejemplares de igual contenido.\n"
                + "\n"
                + "Dado en Durán, " + Utils.dateFormatPattern("dd/MM/yyyy", new Date());
        ordenCompra.setDetalleNotificacion(notificacionTerm);
    }

    public List<BienesInventarioModel> completeText(String busqueda) {
        try {
            if (busqueda == null) {
                return null;
            }
            busqueda = "%" + busqueda + "%";
            String b = busqueda.toUpperCase().replaceAll(" ", "%");
//            List<BienesItem> resultBien = bienesItemService.getItemBien(this.busqueda.getAnio(), b);
//            List<DetalleItem> resultInv = detalleItemService.getDetalleItemInventario(this.busqueda.getAnio(), b);
            System.out.println("Busqueda " + b);
            return getListaModelBienes(b);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busquedad de item", e);
        }
        return null;
    }

    public List<BienesInventarioModel> getListaModelBienes(String busq) {
        List<BienesItem> resultBien = bienesItemService.getItemBien(busq);
        List<DetalleItem> resultInv = detalleItemService.getDetalleItemInventario(busq);
        resultModelBienes = new ArrayList<>();
        for (BienesItem bi : resultBien) {
            BienesInventarioModel bm = new BienesInventarioModel();
            bm.setDescripcion(bi.getDescripcion());
            bm.setCodigo(bi.getCodigoBienAgrupado());
            bm.setBien(bi);
            if (!resultModelBienes.contains(bm)) {
                resultModelBienes.add(bm);
            }
        }
        for (DetalleItem di : resultInv) {
            BienesInventarioModel bm = new BienesInventarioModel();
            bm.setDescripcion(di.getDescripcion());
            bm.setCodigo(di.getCodigoAgrupado());
            bm.setInv(di);
            if (!resultBien.contains(bm)) {
                resultModelBienes.add(bm);
            }
        }
        return resultModelBienes;
    }

    public List<BienesInventarioModel> completeTextInventario(String busqueda) {
        try {
            if (busqueda == null) {
                return null;
            }
            busqueda = "%" + busqueda + "%";
            String b = busqueda.toUpperCase().replaceAll(" ", "%");
            System.out.println("busqueda--> " + b);
            return getListaModelBienes(b);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busquedad de item", e);
        }
        return null;
    }

    public void plazoPago() {
        if (ordenCompra.getDiaPlazo() == null || ordenCompra.getTipoPlazo() == null) {
            JsfUtil.addWarningMessage("Error", "Verifique que esten llenos los campos de Plazo Y forma de Pago.");
            return;
        }
        Servidor recepcion, compra;
        recepcion = clienteService.getResponsableXRol(RolUsuario.guardaAlmacen);
//        compra = clienteService.getResponsableTransferencia(RolUsuario.comprasPublicas);
        plazoEntrega = "El plazo de ejecución es de " + ordenCompra.getDiaPlazo() + " " + ordenCompra.getTipoPlazo().getTexto()
                + " una vez suscrita la orden "
                + "de compra, en coordinación con el administrador de la orden de compra.";
        ordenCompra.setPlazoEntrerga(plazoEntrega);
        formaCondicionPlazo = "";
        ordenCompra.setCondicionPlazo(formaCondicionPlazo);
        lugarEntregaPlazo = "La entrega debe realizarse en las instalaciones de oficinas administrativas de " + entidad.getAbreviatura() + ", "
                + "para la facturación se contactará con la responsable de asuntos administrativos.\n"
                + "\n"
                + "Número telefónico de contacto: " + entidad.getMovil() + " " + entidad.getTelefono1() + " ext 1007\n"
                + "Correo institucional de la persona responsable de la recepción: " + (recepcion.getEmailInstitucion() != null ? recepcion.getEmailInstitucion() : "") + "\n"
                + "Correo institucional de la persona responsable de la orden de compra: " + (recepcion.getEmailInstitucion() != null ? recepcion.getEmailInstitucion() : "");
        ordenCompra.setDetalleEntrega(lugarEntregaPlazo);
//        System.out.println("lugarEntregaPlazo " + lugarEntregaPlazo);
    }

    public void garantiaObligacion() {
        multa = "Si el proveedor no cumpliere con el tiempo establecido para la entrega de los bienes, "
                + "se impondrá una multa equivalente al 1X1000 del valor total de la orden de compra por cada día de retraso. ";
        ordenCompra.setMulta(multa);
    }

    public void detallePresupueto() {
        if (ordenCompra.getReservaCompromiso() == null) {
            JsfUtil.addWarningMessage("Error", "Debe Seleccionar una Reserva de Compromiso.");
            return;
        }
        detalleReservaList = reservaService.getDetalleReservaApro(ordenCompra.getReservaCompromiso());
        presupuestoTotal = BigDecimal.ZERO;
        if (!detalleReservaList.isEmpty()) {
            for (DetalleSolicitudCompromiso dt : detalleReservaList) {
                presupuestoTotal = new BigDecimal(presupuestoTotal.doubleValue() + dt.getMontoSolicitado().doubleValue());
            }
        }
//        System.out.println("presupuesto Total ---> " + presupuestoTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() + " Valor Contratado --> " + adquisicion.getValorContratado());
        if (adquisicion.getValorContratado().doubleValue() <= presupuestoTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() && adquisicion.getValorContratado().doubleValue() > 0) {
            cargardetallePresupuesto();
        } else {
            JsfUtil.addWarningMessage("Error", "El valor Adjudicado NO PUEDE SER MAYOR al monto total de la reserva.");
        }

    }

    public String certificacionPresupuesto() {
        String aux = "", detalle = "", concepto = "", partida = "";
        if (ordenCompra.getReservaCompromiso() != null) {
            String codigo = formatoCodigo(ordenCompra.getReservaCompromiso().getSecuencial()) + "-" + ordenCompra.getReservaCompromiso().getPeriodo();
            detalle = "La presente adquisición cuenta con la certificación presupuestaria"
                    + " (Reserva de Compromiso N° " + codigo + ") de fecha (" + Utils.dateFormatPattern("dd/MM/yyyy", ordenCompra.getReservaCompromiso().getFechaAprobacion()) + ") de acuerdo al siguiente detalle:\n";

            if (!detalleReservaList.isEmpty()) {
                for (DetalleSolicitudCompromiso dt : detalleReservaList) {
                    concepto = itemConcepto(dt);
                    if (dt.getActividadProducto() == null) {
                        partida = dt.getPresupuesto().getPartida();
                    } else {
                        partida = dt.getActividadProducto().getCodigoPresupuestario();
                    }
                    aux = aux + "\nCÓDIGO PRESUPUESTARIO: " + partida + ".\n"
                            + "DENOMINACION: " + concepto + "\n"
                            + "VALOR: $" + dt.getMontoSolicitado() + " INCLUIDO IVA.\n\n";
                }
            }
        }
        return detalle + aux;
    }

    public String nombrePartida(String partida) {
        return reservaService.conceptoPartidas(partida, ordenCompra.getReservaCompromiso().getPeriodo());
    }

    public String itemConcepto(DetalleSolicitudCompromiso dtRe) {
        String concepto = "";
        if (dtRe.getActividadProducto() == null) {
            if (nombrePartida(dtRe.getPresupuesto().getPartida()).equals("PD")) {
                concepto = "PARTIDA DISTRIBUTIVO";
            }
            if (nombrePartida(dtRe.getPresupuesto().getPartida()).equals("PDI")) {
                concepto = "PARTIDA DIRECTAS";
            }
            if (nombrePartida(dtRe.getPresupuesto().getPartida()).equals("PDA")) {
                concepto = "PARTIDA DISTRIBUTIVO ANEXO";
            }
        } else {
            concepto = dtRe.getActividadProducto().getDescripcion();
        }
        return concepto;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CARGAR LISTAS">
    public void cargardetallePresupuesto() {
        if (ordenCompra.getReservaCompromiso() != null) {
            detalleCertificacion = certificacionPresupuesto();
            detallePresupuestoResfe = ordenCompra.getDetalleValorAdjudicado();
            ordenCompra.setDetalleCertificacion(detalleCertificacion);
            ordenCompra.setDetallePresupuesto(detallePresupuestoResfe);
        }
    }

    public void addCaracteristicas() {
        caracteristicaTecnica.setCaracteristica(caracteristicaString);
        if (editCaracteristicas) {
            caracteristicasList.add(caracteristicaTecnica);
        }
        PrimeFaces.current().executeScript("PF('dlgCaracteristica').hide()");
        PrimeFaces.current().ajax().update("formCaract:formPanel");
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("Información", "Caracteristica agregada con Éxito..");
    }

    public void addObligacion() {
        obligacionResponsable.setNumero((short) numObligacionResponsable);
        if (editObligacion) {
            obligacionRespList.add(obligacionResponsable);
        }
        PrimeFaces.current().executeScript("PF('dlgObligacion').hide()");
        PrimeFaces.current().ajax().update("formPanelOblResp");
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("Información", "Obligacion de Responsable agregada con Éxito..");
    }

    public void cargarObligaciones() {
        ObligacionResponsable ob1 = new ObligacionResponsable();
        ObligacionResponsable ob2 = new ObligacionResponsable();
        ObligacionResponsable ob3 = new ObligacionResponsable();
        ObligacionResponsable ob4 = new ObligacionResponsable();
        ObligacionResponsable ob5 = new ObligacionResponsable();
        ob1.setNumero((short) 1);
        ob2.setNumero((short) 2);
        ob3.setNumero((short) 3);
        ob4.setNumero((short) 4);
        ob5.setNumero((short) 5);
        ob1.setDescripcion("Velar por el cabal cumplimiento el objeto de contratación, y demás obligaciones pactadas en la presente orden de compra.");
        ob2.setDescripcion("Verificación de cumplimiento de plazo. ");
        ob3.setDescripcion("Verificación cumplimiento de especificaciones técnicas o términos de referencia. ");
        ob4.setDescripcion("Aplicación de multas. ");
        ob5.setDescripcion("Firma acta entrega recepción.");
        obligacionRespList.add(ob1);
        obligacionRespList.add(ob2);
        obligacionRespList.add(ob3);
        obligacionRespList.add(ob4);
        obligacionRespList.add(ob5);
    }

    public void addEspecificacion() {
        if (detalleBien != null) {
            especificacionTecnica.setDetalle(detalleBien.getDescripcion());
            especificacionTecnica.setBienItem(detalleBien.getBien());
            detalleBien = null;
        }
        if (detalleInventario != null) {
            especificacionTecnica.setDetalle(detalleInventario.getDescripcion());
            especificacionTecnica.setInventarioItem(detalleInventario.getInv());
            detalleInventario = null;
        }
        especificacionTecnica.setItem((short) numEspecificacion);
        if (editEspecificaciones) {
            especificacionesList.add(especificacionTecnica);
        }

        PrimeFaces.current().executeScript("PF('dlgItem4').hide()");
        PrimeFaces.current().ajax().update(":panelItem4");
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.addSuccessMessage("Información", "Especificación agregada con Éxito..");
    }

    public void calcularValorTotal() {
        if (especificacionTecnica.getCantidad() == null || especificacionTecnica.getPrecioUnitario() == null) {
            return;
        }
        especificacionTecnica.
                setPrecioTotal(especificacionTecnica.getCantidad().
                        multiply(especificacionTecnica.getPrecioUnitario()).setScale(4, RoundingMode.HALF_UP));
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="DIALOGOS">
    public void dlgObligacion() {
        editObligacion = Boolean.TRUE;
        numObligacionResponsable = obligacionRespList.size() + 1;
//        vigencia = "La presente orden entrará en vigencia a partir de la "
//                + "fecha de suscripción y notificación al proveedor.";
//        ordenCompra.setDetalleVigencia(vigencia);
        obligacionResponsable = new ObligacionResponsable();
        PrimeFaces.current().executeScript("PF('dlgObligacion').show()");
        PrimeFaces.current().ajax().update(":formPanelOblResp");
    }

    public void editarObligacion(ObligacionResponsable oblig) {
        editObligacion = Boolean.FALSE;
        numObligacionResponsable = oblig.getNumero();
        obligacionResponsable = oblig;
        PrimeFaces.current().executeScript("PF('dlgObligacion').show()");
        PrimeFaces.current().ajax().update(":formPanelOblResp");
    }

    public void dlgCaracteristica() {
        editCaracteristicas = Boolean.TRUE;
        numCaracteristica++;
        caracteristicaTecnica = new CaracteristicaTecnica();
        caracteristicaString = "CARACTERÍSTICA TÉCNICA " + (caracteristicasList.size() + 1);
        tipoMedidaList = catalogoItemService.findCatalogoItems("sistema_unidad_medida");
        PrimeFaces.current().executeScript("PF('dlgCaracteristica').show()");
        PrimeFaces.current().ajax().update(":formPanel");
    }

    public void editarCaracteristicas(CaracteristicaTecnica caract) {
        editCaracteristicas = Boolean.FALSE;
        caracteristicaTecnica = caract;
        caracteristicaString = caract.getCaracteristica();
        PrimeFaces.current().executeScript("PF('dlgCaracteristica').show()");
        PrimeFaces.current().ajax().update(":formPanel");
    }

    public void dlgEspecificacionesTecn() {
        editEspecificaciones = Boolean.TRUE;
        numEspecificacion = especificacionesList.size() + 1;
        especificacionTecnica = new EspecificacionTecnica();
        if (adquisicion.getTipoProceso().getCodigo().equals("tipo_proceso_infima")) {
        } else {
            especificacionTecnica.setCodigoOrdenCompra("CE-" + busqueda.getAnio());
            especificacionTecnica.setPrecioUnitario(BigDecimal.ZERO);
            especificacionTecnica.setPrecioTotal(BigDecimal.ZERO);
        }
        PrimeFaces.current().executeScript("PF('dlgItem4').show()");
        PrimeFaces.current().ajax().update(":panelItem4");
    }

    public void editarEspecificacion(EspecificacionTecnica esp) {
        editEspecificaciones = Boolean.FALSE;
        numEspecificacion = esp.getItem();
        especificacionTecnica = esp;
        PrimeFaces.current().executeScript("PF('dlgItem4').show()");
        PrimeFaces.current().ajax().update(":panelItem4");
    }

    public void closeFormEspecificacion(CloseEvent event) {
        especificacionTecnica = new EspecificacionTecnica();
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("panelItem4");
    }

    public void openDialogUnidad() {
        Utils.openDialog("/facelet/talentoHumano/unidadAdministrativa/dialogUnidadAdministrativa", "550", "400");
    }

    public void openDialogoProveedor() {
        Utils.openDialog("/facelet/talentoHumano/dialogProveedor", "850", "400");
    }

    public void openDialogServidor() {
        responsableAdqui = new ResponsableAdquisicion();
        if (!responsableList.isEmpty()) {
            JsfUtil.addWarningMessage("Información", "No puede Agregar mas de un Responsable");
            return;
        }
        Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "400");
    }

    public void openDialogoAutorizador() {
        Utils.openDialog("/facelet/talentoHumano/dialgo/DlgUsuarioRol", "850", "400");
    }

    public void dlogoObservaciones(SolicitudOrdenCompra sol) {
        try {
            ordenCompra = sol;
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="SELECT EVENT">
    public void selectProveedor(SelectEvent evt) {
        try {
            adquisicion.setProveedor(new Proveedor());
            adquisicion.setProveedor((Proveedor) evt.getObject());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void selectUnidad(SelectEvent evt) {
        try {
            ordenCompra.setUnidad((UnidadAdministrativa) evt.getObject());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void selectResponsable(SelectEvent evt) {
        try {
            responsableAdqui.setResponsable((Servidor) evt.getObject());;
            responsableList.add(responsableAdqui);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void selectAutorizador(SelectEvent evt) {
        try {
            UsuarioRol serv = (UsuarioRol) evt.getObject();
            if (serv != null) {
                ordenCompra.setAutorizador(serv.getUsuario().getFuncionario());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CARGA DE ARCHIVO">
    public void handleFileUpload(FileUploadEvent event) {
        try {
            files = event.getFile();
            System.out.println("documento " + files);
            addArchivo();
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("formMain");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void adjuntarDucumento() {
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void adjuntarDucumento(SolicitudOrdenCompra sol) {
        ordenCompra = sol;
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void addArchivo() {
        if (files != null) {
            System.out.println("entro archivo " + files + " plan " + ordenCompra.getCodigoOrden());
            uploadDoc.upload(tramite, files);
            files = null;
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
//            ordenCompra = new SolicitudOrdenCompra();
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Eliminar datos de Lista">
    public void eliminarCaracteristica(CaracteristicaTecnica c, int val) {
        if (c.getId() != null) {
            caracteristicaService.remove(c);
        } else {
            caracteristicasList.remove(val);
        }
    }

    public void eliminarEspecificaciones(EspecificacionTecnica e) {
        especificacionesList.remove(e);
        if (e.getId() != null) {
            especificacionService.remove(e);
        }
    }

    public void eliminarObligaciones(ObligacionResponsable o) {
        obligacionRespList.remove(o);
        if (o.getId() != null) {
            obligacionService.remove(o);
        }
    }

    public void eliminarResponsable(ResponsableAdquisicion resp) {
        responsableList.remove(resp);
        if (resp.getId() != null) {
            responsableAdquisicionService.remove(resp);
        }
    }

    public void eliminarDetalleReserva(DetalleSolicitudCompromiso d) {
        detalleReservaList.remove(d);
    }
//</editor-fold>

    public String onFlowProcess(FlowEvent event) {
        try {
            switch (event.getOldStep()) {
                case "tabInicio":
                    break;
            }
            if (tipo == 1) {
                if (event.getOldStep().equalsIgnoreCase("tabPlazo")) {
                    return "responsableObligacion";
                }
                if (event.getOldStep().equalsIgnoreCase("responsableObligacion")
                        && event.getNewStep().equalsIgnoreCase("garantiaObligacion")) {
                    return "tabPlazo";
                }
            }
            if (skip) {
                skip = false;
                return "confirm";
            } else {
                return event.getNewStep();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
            return "id_value_of_first_tab";
        }
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();

        return a;
    }

    /*Actualiza los tipos de proceso*/
    public void actualizarTiposProcesos() {
        this.tiposProcesos = adquisicionesService.getTiposProcesos(adquisicion.getTipoAdquisicion());
    }

    public void resetValue() {
        ordenCompra = new SolicitudOrdenCompra();
        adquisicion = new Adquisiciones();
        responsableAdqui = new ResponsableAdquisicion();
        caracteristicasList = new ArrayList<>();
        especificacionesList = new ArrayList<>();
        obligacionRespList = new ArrayList<>();
        detalleReservaList = new ArrayList<>();
        responsableList = new ArrayList<>();
        codigo = "";
    }

//<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public Boolean getBtnCompletarTarea() {
        return btnCompletarTarea;
    }

    public void setBtnCompletarTarea(Boolean btnCompletarTarea) {
        this.btnCompletarTarea = btnCompletarTarea;
    }

    public List<CatalogoItem> getTipoCompraList() {
        return tipoCompraList;
    }

    public void setTipoCompraList(List<CatalogoItem> tipoCompraList) {
        this.tipoCompraList = tipoCompraList;
    }

    public Boolean getCompleteTextRendered() {
        return completeTextRendered;
    }

    public void setCompleteTextRendered(Boolean completeTextRendered) {
        this.completeTextRendered = completeTextRendered;
    }

    public LazyModel<SolicitudOrdenCompra> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<SolicitudOrdenCompra> lazy) {
        this.lazy = lazy;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public SolicitudOrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(SolicitudOrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public String getConsideraciones() {
        return consideraciones;
    }

    public void setConsideraciones(String consideraciones) {
        this.consideraciones = consideraciones;
    }

    public List<CatalogoItem> getTipoMedidaList() {
        return tipoMedidaList;
    }

    public void setTipoMedidaList(List<CatalogoItem> tipoMedidaList) {
        this.tipoMedidaList = tipoMedidaList;
    }

    public List<EspecificacionTecnica> getEspecificacionesList() {
        return especificacionesList;
    }

    public void setEspecificacionesList(List<EspecificacionTecnica> especificacionesList) {
        this.especificacionesList = especificacionesList;
    }

    public List<CaracteristicaTecnica> getCaracteristicasList() {
        return caracteristicasList;
    }

    public void setCaracteristicasList(List<CaracteristicaTecnica> caracteristicasList) {
        this.caracteristicasList = caracteristicasList;
    }

    public CaracteristicaTecnica getCaracteristicaTecnica() {
        return caracteristicaTecnica;
    }

    public void setCaracteristicaTecnica(CaracteristicaTecnica caracteristicaTecnica) {
        this.caracteristicaTecnica = caracteristicaTecnica;
    }

    public EspecificacionTecnica getEspecificacionTecnica() {
        return especificacionTecnica;
    }

    public void setEspecificacionTecnica(EspecificacionTecnica especificacionTecnica) {
        this.especificacionTecnica = especificacionTecnica;
    }

    public String getCaracteristicaString() {
        return caracteristicaString;
    }

    public void setCaracteristicaString(String caracteristicaString) {
        this.caracteristicaString = caracteristicaString;
    }

    public int getNumCaracteristica() {
        return numCaracteristica;
    }

    public void setNumCaracteristica(int numCaracteristica) {
        this.numCaracteristica = numCaracteristica;
    }

    public String getValorAbjudicado() {
        return valorAbjudicado;
    }

    public void setValorAbjudicado(String valorAbjudicado) {
        this.valorAbjudicado = valorAbjudicado;
    }

    public int getNumEspecificacion() {
        return numEspecificacion;
    }

    public void setNumEspecificacion(int numEspecificacion) {
        this.numEspecificacion = numEspecificacion;
    }

    public List<CatalogoItem> getTipoPagoPlazo() {
        return tipoPagoPlazo;
    }

    public void setTipoPagoPlazo(List<CatalogoItem> tipoPagoPlazo) {
        this.tipoPagoPlazo = tipoPagoPlazo;
    }

    public String getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(String plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public String getFormaCondicionPlazo() {
        return formaCondicionPlazo;
    }

    public void setFormaCondicionPlazo(String formaCondicionPlazo) {
        this.formaCondicionPlazo = formaCondicionPlazo;
    }

    public String getLugarEntregaPlazo() {
        return lugarEntregaPlazo;
    }

    public void setLugarEntregaPlazo(String lugarEntregaPlazo) {
        this.lugarEntregaPlazo = lugarEntregaPlazo;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public List<ResponsableAdquisicion> getResponsableList() {
        return responsableList;
    }

    public void setResponsableList(List<ResponsableAdquisicion> responsableList) {
        this.responsableList = responsableList;
    }

    public int getNumObligacionResponsable() {
        return numObligacionResponsable;
    }

    public void setNumObligacionResponsable(int numObligacionResponsable) {
        this.numObligacionResponsable = numObligacionResponsable;
    }

    public List<ObligacionResponsable> getObligacionRespList() {
        return obligacionRespList;
    }

    public void setObligacionRespList(List<ObligacionResponsable> obligacionRespList) {
        this.obligacionRespList = obligacionRespList;
    }

    public ObligacionResponsable getObligacionResponsable() {
        return obligacionResponsable;
    }

    public void setObligacionResponsable(ObligacionResponsable obligacionResponsable) {
        this.obligacionResponsable = obligacionResponsable;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getSolucionControversia() {
        return solucionControversia;
    }

    public void setSolucionControversia(String solucionControversia) {
        this.solucionControversia = solucionControversia;
    }

    public String getTerminacionOrden() {
        return terminacionOrden;
    }

    public void setTerminacionOrden(String terminacionOrden) {
        this.terminacionOrden = terminacionOrden;
    }

    public String getNotificacionTerm() {
        return notificacionTerm;
    }

    public void setNotificacionTerm(String notificacionTerm) {
        this.notificacionTerm = notificacionTerm;
    }

    public List<SolicitudReservaCompromiso> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<SolicitudReservaCompromiso> reservaList) {
        this.reservaList = reservaList;
    }

    public List<DetalleSolicitudCompromiso> getDetalleReservaList() {
        return detalleReservaList;
    }

    public void setDetalleReservaList(List<DetalleSolicitudCompromiso> detalleReservaList) {
        this.detalleReservaList = detalleReservaList;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public DetalleSolicitudCompromiso getDetalleReserva() {
        return detalleReserva;
    }

    public void setDetalleReserva(DetalleSolicitudCompromiso detalleReserva) {
        this.detalleReserva = detalleReserva;
    }

    public String getDetalleCertificacion() {
        return detalleCertificacion;
    }

    public void setDetalleCertificacion(String detalleCertificacion) {
        this.detalleCertificacion = detalleCertificacion;
    }

    public String getDetallePresupuestoResfe() {
        return detallePresupuestoResfe;
    }

    public void setDetallePresupuestoResfe(String detallePresupuestoResfe) {
        this.detallePresupuestoResfe = detallePresupuestoResfe;
    }

    public List<CatalogoItem> getSubTiposAdquisicion() {
        return subTiposAdquisicion;
    }

    public void setSubTiposAdquisicion(List<CatalogoItem> subTiposAdquisicion) {
        this.subTiposAdquisicion = subTiposAdquisicion;
    }

    public List<CatalogoItem> getTiposProcesos() {
        return tiposProcesos;
    }

    public void setTiposProcesos(List<CatalogoItem> tiposProcesos) {
        this.tiposProcesos = tiposProcesos;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public LazyModel<SolicitudOrdenCompra> getLazySolicitud() {
        return lazySolicitud;
    }

    public void setLazySolicitud(LazyModel<SolicitudOrdenCompra> lazySolicitud) {
        this.lazySolicitud = lazySolicitud;
    }

    public LazyModel<SolicitudOrdenCompra> getLazyProcesa() {
        return lazyProcesa;
    }

    public void setLazyProcesa(LazyModel<SolicitudOrdenCompra> lazyProcesa) {
        this.lazyProcesa = lazyProcesa;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public CatalogoItem getEstadoRechazado() {
        return estadoRechazado;
    }

    public void setEstadoRechazado(CatalogoItem estadoRechazado) {
        this.estadoRechazado = estadoRechazado;
    }

    public CatalogoExistencias getCatalogoExistencia() {
        return catalogoExistencia;
    }

    public void setCatalogoExistencia(CatalogoExistencias catalogoExistencia) {
        this.catalogoExistencia = catalogoExistencia;
    }

    public List<CatalogoExistencias> getListCatalogoExistencias() {
        return listCatalogoExistencias;
    }

    public void setListCatalogoExistencias(List<CatalogoExistencias> listCatalogoExistencias) {
        this.listCatalogoExistencias = listCatalogoExistencias;
    }

    public BienesInventarioModel getDetalleBien() {
        return detalleBien;
    }

    public void setDetalleBien(BienesInventarioModel detalleBien) {
        this.detalleBien = detalleBien;
    }

    public Converter getBienesConverter() {
        return bienesConverter;
    }

    public void setBienesConverter(Converter bienesConverter) {
        this.bienesConverter = bienesConverter;
    }

    public BienesInventarioModel getDetalleInventario() {
        return detalleInventario;
    }

    public void setDetalleInventario(BienesInventarioModel detalleInventario) {
        this.detalleInventario = detalleInventario;
    }

    public Boolean getBtnDetalle() {
        return btnDetalle;
    }

    public void setBtnDetalle(Boolean btnDetalle) {
        this.btnDetalle = btnDetalle;
    }

    public BigDecimal getValorTotalSin() {
        return valorTotalSin;
    }

    public void setValorTotalSin(BigDecimal valorTotalSin) {
        this.valorTotalSin = valorTotalSin;
    }

    public boolean isAplicaIva() {
        return aplicaIva;
    }

    public void setAplicaIva(boolean aplicaIva) {
        this.aplicaIva = aplicaIva;
    }
//</editor-fold>

}
