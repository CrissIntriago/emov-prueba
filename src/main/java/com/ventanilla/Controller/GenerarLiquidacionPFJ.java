/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenActividadComercial;
import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenPatente;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.FinaRenTipoLocalComercial;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.ParticipacionCantones;
import com.gestionTributaria.Entities.RenActivosLocalComercial;
import com.gestionTributaria.Entities.RenBalancePatente;
import com.gestionTributaria.Entities.RenDesvalorizacion;
import com.gestionTributaria.Entities.RenFactorPorCapital;
import com.gestionTributaria.Entities.RenFactorPorMetro;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.ParticipacionCantonesServices;
import com.gestionTributaria.Services.RemisionInteresServices;
import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.SolicitudServicios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "generarLiquidacionPFJ")
@ViewScoped
public class GenerarLiquidacionPFJ extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    private SolicitudServicios SolicitudServicios;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private RemisionInteresServices remisionInteresService;
    private UploadedFile file;
    @Inject
    private ManagerService services;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CantonService cantonService;
    @Inject
    private ParticipacionCantonesServices participacionCantonesServices;
    @Inject
    private ServletSession viewReport;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    private FinaRenPatente patente;
    private LazyModel<FinaRenPatente> patentes;
    private Integer tipo = 1;
    private Integer tipoCons = 2;
    private Integer maxAnio;
    private Integer maxAnioAct;
    private Integer minAnio = 1990;
    private Boolean esPersonaProp = true;
    private Boolean esLocal = false, variosRotulos = false;
    private BigDecimal valor;
    private Cliente persona;
    private Cliente propietario;
    private FinaRenLiquidacion liquidacion;
    private FinaRenLiquidacion liquidacionPatente;
    private FinaRenLiquidacion liquidacionHabilitacion;
    private FinaRenLiquidacion liquidacionPermisosAdicionales;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private FinaRenTipoLiquidacion tipoLiquidacionPatente;
    private FinaRenTipoLiquidacion tipoLiquidacionHabilitacion;
    private FinaRenTipoLiquidacion tipoLiquidacionPermisoAdicional;
    private List<FinaRenTipoLiquidacion> tiposLiquidacions;
    private List<FinaRenRubrosLiquidacion> rubrosLiquidacion;
    private RenDesvalorizacion desvalorizacion;
    private Cliente solicitante;
    private LazyModel<Cliente> solicitantes;
    private CatPredioModel predioModel = new CatPredioModel();
    private Boolean mostrarRequisitos;
    private String cedulaRuc;
    private List<FinaRenLocalComercial> localesEnte;
    private LazyModel<FinaRenLocalComercial> localesLazy;
    private FinaRenLocalComercial localSel;
    private List<FinaRenDetLiquidacion> detalle;
    private List<FinaRenRubrosLiquidacion> detallePatente;
    private List<FinaRenRubrosLiquidacion> detalleHabilitacion;
    private List<FinaRenRubrosLiquidacion> detallePermisoAdicional;
    private RenActivosLocalComercial local;
    private Short tipoExoneracion = 0;
    private Short formaPago = 0;
    private BigDecimal tasaPermiso;
    private Integer mesesInteres = 0;
    private FnConvenioPago convenioPago;
    private Boolean liqProvicional;
    private Map<String, Object> parametros;
    private List<FinaRenLocalComercial> localescomerciales;
    private List<FnExoneracionTipo> listaExoneraciones;
    private FnExoneracionTipo exoneracionSeleccionada;
    private FnSolicitudExoneracion solicitudExoneracion;
    private FnExoneracionLiquidacion exoneracionLiquidacion;
    private BigDecimal valorExonerado = BigDecimal.ZERO;
    private ParticipacionCantones participacionCantones;
    private List<ParticipacionCantones> listParticipacionCantones;
    private List<Canton> listaCantones;
    private Boolean distribuccion;
    private BigDecimal verificaciondistibuccion = BigDecimal.ZERO;
    private BigDecimal verificacionValor = BigDecimal.ZERO;
    private List<FinaRenLiquidacion> liquidaciones;
    private List<FinaRenDetLiquidacion> detLiquidaciones;
    private boolean activoShow;
    private boolean patenteShow;
    private boolean habilitacionShow;
    private boolean permisoAdicionalesShow;
    private RenBalancePatente balance;
    private RenBalancePatente balanceActivos;
    private BigDecimal porcentajeFaltante = BigDecimal.ZERO;
    private FinaRenActividadComercial seleccionActividad;
    private List<FinaRenTipoLocalComercial> listaTipoLocales;
    private FinaRenTipoLocalComercial tipoLocal;
    private List<CatalogoItem> listaFactoresUbicacion;
    private CatalogoItem factorUbicacion;
    private List<FinaRenActividadComercial> actividesLocales;
    private List<FinaRenTipoLiquidacion> listLiquidacionPermisosAdicionales;
    private String idActivos, idPatente, idHabilitacion, idPermisos;
    private LazyModel<FinaRenLiquidacion> lazyLiquidacionDeudas;
    private FinaRenLiquidacion liquidacionSelect;
    private String observacion1;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                iniciarDatos();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void loadcliente() {
        solicitantes = new LazyModel(Cliente.class);
        solicitantes.getFilterss().put("validado", true);
        solicitantes.getFilterss().put("estado", true);
    }

    public void loadDeudasLocales(FinaRenLocalComercial local) {
        lazyLiquidacionDeudas = new LazyModel(FinaRenLiquidacion.class);
        lazyLiquidacionDeudas.getFilterss().put("localComercial", local);
        lazyLiquidacionDeudas.getFilterss().put("estadoLiquidacion.id:notEqual", 1L);
        JsfUtil.executeJS("PF('dlgDeudasLocales').show()");
        JsfUtil.update("frmDeudasLocales");
    }

    public void iniciarDatos() {
        liquidacionSelect = new FinaRenLiquidacion();
        listLiquidacionPermisosAdicionales = new ArrayList<>();
        seleccionActividad = new FinaRenActividadComercial();
        actividesLocales = new ArrayList<>();
        listaTipoLocales = new ArrayList<>();
        tipoLocal = new FinaRenTipoLocalComercial();
        listaFactoresUbicacion = new ArrayList<>();
        factorUbicacion = new CatalogoItem();
        tipoLiquidacionPatente = new FinaRenTipoLiquidacion();
        tipoLiquidacionHabilitacion = new FinaRenTipoLiquidacion();
        tipoLiquidacionPermisoAdicional = new FinaRenTipoLiquidacion();
        activoShow = false;
        patenteShow = false;
        habilitacionShow = false;
        permisoAdicionalesShow = false;
        participacionCantones = new ParticipacionCantones();
        listParticipacionCantones = new ArrayList<>();
        localescomerciales = new ArrayList<>();
        solicitudExoneracion = new FnSolicitudExoneracion();
        persona = new Cliente();
        parametros = new HashMap<>();
        liqProvicional = false;
        liquidacion = new FinaRenLiquidacion();
        tiposLiquidacions = services.gettiposLiquidacionByCodTitRep(2);
        localesLazy = new LazyModel(FinaRenLocalComercial.class);
        localesLazy.getFilterss().put("estadoLocalComercial", 1L);
        localesLazy.getFilterss().put("estado", true);
        maxAnio = Utils.getAnio(new Date()) - 1;
        maxAnioAct = Utils.getAnio(new Date());
        balance = new RenBalancePatente();
        balance.setAnioBalance(Utils.getAnio(new Date()) - 1);
        balanceActivos = new RenBalancePatente();
        balanceActivos.setAnioBalance(Utils.getAnio(new Date()) - 1);
        patente = null;
        exoneracionLiquidacion = new FnExoneracionLiquidacion();
        rubrosLiquidacion = new ArrayList<>();
        exoneracionSeleccionada = new FnExoneracionTipo();
        solicitudExoneracion = new FnSolicitudExoneracion();
        localSel = new FinaRenLocalComercial();
        tipoLiquidacion = services.find(FinaRenTipoLiquidacion.class, 7L);
        tipoLiquidacionPatente = services.find(FinaRenTipoLiquidacion.class, 9L);
        tipoLiquidacionHabilitacion = services.find(FinaRenTipoLiquidacion.class, 64L);
        tipoLiquidacionPermisoAdicional = new FinaRenTipoLiquidacion();
        liquidacion = new FinaRenLiquidacion();
        liquidacionPatente = new FinaRenLiquidacion();
        liquidacionHabilitacion = new FinaRenLiquidacion();
        liquidacionPermisosAdicionales = new FinaRenLiquidacion();
        detalle = new ArrayList<>();
        detallePatente = new ArrayList<>();
        detalleHabilitacion = new ArrayList<>();
        detallePermisoAdicional = new ArrayList<>();

    }

    public void loadCantones() {
        listaCantones = new ArrayList<>();
        listaCantones = cantonService.getCantones();
    }

    public void seleccionLocal(FinaRenLocalComercial local) {
        this.localSel = new FinaRenLocalComercial();
        this.localSel = local;
        initLiquidacion();

    }

    public void loadExoneraciones() {
        listaExoneraciones = services.listTipoExoneraciones(31L);
    }

    public void initLiquidacion() {
        loadExoneraciones();
        liquidacion = new FinaRenLiquidacion();
        liquidacion.setTotalPago(new BigDecimal(0));
        liquidacion.setFechaContratoAnt(new Date());
        liquidacion.setCostoAdq(BigDecimal.ZERO);
        liquidacion.setCuantia(BigDecimal.ZERO);
        liquidacion.setAnio(Utils.getAnio(new Date()));
        liquidacion.setTipoLiquidacion(tipoLiquidacion);
        liquidacion.setFechaIngreso(new Date());
        liquidacion.setLocalComercial(localSel);
        if (localSel.getPatente() != null && localSel.getPatente().getPropietario() != null) {
            liquidacion.setComprador(localSel.getPatente().getPropietario());
            liquidacion.setIdentificacion(localSel.getPatente().getPropietario().getIdentificacionCompleta());
        }
        liquidacionPatente = new FinaRenLiquidacion();
        liquidacionPatente.setTotalPago(new BigDecimal(0));
        liquidacionPatente.setFechaContratoAnt(new Date());
        liquidacionPatente.setCostoAdq(BigDecimal.ZERO);
        liquidacionPatente.setCuantia(BigDecimal.ZERO);
        liquidacionPatente.setAnio(Utils.getAnio(new Date()));
        liquidacionPatente.setTipoLiquidacion(tipoLiquidacionPatente);
        liquidacionPatente.setFechaIngreso(new Date());
        liquidacionPatente.setLocalComercial(localSel);
        if (localSel.getPatente() != null && localSel.getPatente().getPropietario() != null) {
            liquidacionPatente.setComprador(localSel.getPatente().getPropietario());
            liquidacionPatente.setIdentificacion(localSel.getPatente().getPropietario().getIdentificacionCompleta());
        }

        liquidacionHabilitacion = new FinaRenLiquidacion();
        liquidacionHabilitacion.setTotalPago(new BigDecimal(0));
        liquidacionHabilitacion.setFechaContratoAnt(new Date());
        liquidacionHabilitacion.setCostoAdq(BigDecimal.ZERO);
        liquidacionHabilitacion.setCuantia(BigDecimal.ZERO);
        liquidacionHabilitacion.setAnio(Utils.getAnio(new Date()));
        liquidacionHabilitacion.setTipoLiquidacion(tipoLiquidacionHabilitacion);
        liquidacionHabilitacion.setFechaIngreso(new Date());
        liquidacionHabilitacion.setLocalComercial(localSel);
        if (localSel.getPatente() != null && localSel.getPatente().getPropietario() != null) {
            liquidacionHabilitacion.setComprador(localSel.getPatente().getPropietario());
            liquidacionHabilitacion.setIdentificacion(localSel.getPatente().getPropietario().getIdentificacionCompleta());
        }

        liquidacionPermisosAdicionales = new FinaRenLiquidacion();
        liquidacionPermisosAdicionales.setTotalPago(new BigDecimal(0));
        liquidacionPermisosAdicionales.setFechaContratoAnt(new Date());
        liquidacionPermisosAdicionales.setCostoAdq(BigDecimal.ZERO);
        liquidacionPermisosAdicionales.setCuantia(BigDecimal.ZERO);
        liquidacionPermisosAdicionales.setAnio(Utils.getAnio(new Date()));
        liquidacionPermisosAdicionales.setTipoLiquidacion(null);
        liquidacionPermisosAdicionales.setFechaIngreso(new Date());
        liquidacionPermisosAdicionales.setLocalComercial(localSel);
        if (localSel.getPatente() != null && localSel.getPatente().getPropietario() != null) {
            liquidacionPermisosAdicionales.setComprador(localSel.getPatente().getPropietario());
            liquidacionPermisosAdicionales.setIdentificacion(localSel.getPatente().getPropietario().getIdentificacionCompleta());
        }

    }

    public void loadFuncionalidad(int tipo) {
        try {
            switch (tipo) {
                case 1:
                    activos();
                    break;
                case 2:
                    patentes();
                    break;
                case 3:
                    tasaHabilitacion();
                    break;
                case 4:
                    permisosAdicionales();
                    break;
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cargar Rubros", e);
        }
        JsfUtil.update("frmAlcPlus");

    }

    public void activos() {
        rubrosLiquidacion = new ArrayList<>();
        detalle = new ArrayList();

        if (tipoLiquidacion != null) {
            System.out.println("TipoLiquidacion " + tipoLiquidacion.getNombreTransaccion());
            parametros = new HashMap<>();
            parametros.put("liqui", tipoLiquidacion);
            parametros.put("estado", true);
            rubrosLiquidacion = services.getRubrosPorLiquidacion(tipoLiquidacion.getId());
            if (tipoLiquidacion.getId().intValue() == 7) {
                //    listaExcepciones = catalogoService.MostarTodoCatalogo("GT_EXCEPCIONES_ACTIVOS_TOTALES");
                local = new RenActivosLocalComercial();
                local.setFechaIngreso(new Date());
                local.setAnioBalance(maxAnio);
                esLocal = true;

                detalle.add(0, new FinaRenDetLiquidacion(BigDecimal.ZERO, null, "Valor de Activos".toUpperCase()));
                detalle.add(1, new FinaRenDetLiquidacion(BigDecimal.ZERO, null, "Valor de Pasivos".toUpperCase()));
                detalle.add(2, new FinaRenDetLiquidacion(BigDecimal.ZERO, null, "Diferencia de Activos vs Pasivos".toUpperCase()));
                detalle.add(3, new FinaRenDetLiquidacion(BigDecimal.ZERO, null, "Base Imponible para el Cálculo".toUpperCase()));
            }

            for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
                detalle.add(new FinaRenDetLiquidacion(temp.getValor(), temp, temp.getDescripcion()));
            }

            liquidacion.setAreaTotal(BigDecimal.ZERO);
        }
        if (propietario != null) {
            esPersonaProp = "PER_NAT".equals(propietario.getTipoProv().getCodigo());
        }
    }

    public void patentes() {
        List<FinaRenRubrosLiquidacion> result = new ArrayList<>();
        result = services.getRubrosPorLiquidacion(tipoLiquidacionPatente.getId());
        detallePatente = new ArrayList<>();
        for (FinaRenRubrosLiquidacion item : result) {
            item.setCantidad(1);
            if (item.getValor() == null) {
                item.setValor(BigDecimal.ZERO);
            }

            item.setValorTotal(item.getValor());
            if (item.getValorTotal() == null) {
                item.setValorTotal(BigDecimal.ZERO);
            }

            detallePatente.add(item);
        }
    }

    public void tasaHabilitacion() {
        detalleHabilitacion = new ArrayList<>();
        List<FinaRenRubrosLiquidacion> result = new ArrayList<>();
        result = services.getRubrosPorLiquidacion(tipoLiquidacionHabilitacion.getId());

        for (FinaRenRubrosLiquidacion item : result) {
            item.setCantidad(1);
            if (item.getValor() == null) {
                item.setValor(BigDecimal.ZERO);
            }
            item.setValorTotal(item.getValor());

            detalleHabilitacion.add(item);
        }

        actividesLocales = new ArrayList<>();
        FinaRenRubrosLiquidacion rubroSelect = new FinaRenRubrosLiquidacion();
        parametros = new HashMap<>();
        parametros.put("estado", true);

        setTipoLocal(tipoLocal);
        setFactorUbicacion(factorUbicacion);
        setSeleccionActividad(seleccionActividad);
        System.out.println("Tipo local " + tipoLocal);
        System.out.println("activ " + seleccionActividad);
        System.out.println("factorUbicacion " + factorUbicacion);

        actividesLocales = services.findObjectByParameterList(FinaRenActividadComercial.class, parametros);

        parametros = new HashMap<>();
        parametros.put("estado", true);
        listaTipoLocales = new ArrayList<>();
        listaTipoLocales = services.findObjectByParameterList(FinaRenTipoLocalComercial.class, parametros);
        listaFactoresUbicacion = new ArrayList<>();
        listaFactoresUbicacion = catalogoService.MostarTodoCatalogo("GT_FACTOR_UBICACION_LOCALES");

        BigDecimal actividad = BigDecimal.ZERO;
        BigDecimal tamanio = BigDecimal.ZERO;
        BigDecimal ubicacion = BigDecimal.ZERO;
        if (localSel != null) {
            System.out.println("local " + localSel.getId());
            System.out.println("liquidacion " + liquidacion.getAnio());
            FinaRenActividadComercial actividadLocal = services.actividadPOrLocal(localSel.getId(), liquidacion.getAnio());
            System.out.println("actividadLocal " + actividadLocal);
            rubroSelect = new FinaRenRubrosLiquidacion();
            rubroSelect = detalleHabilitacion.get(0);
            rubroSelect.setCantidad(1);
            rubroSelect.setAnio(Utils.getAnio(new Date()));

            if (actividadLocal != null && actividadLocal.getValor() != null && actividadLocal.getValor().compareTo(BigDecimal.ZERO) == 1) {
                actividad = actividadLocal.getValor();
                seleccionActividad = actividadLocal;
                System.out.println("entrando seleccionActividad  " + actividad);
            } else {
                actividad = seleccionActividad.getValor();
                System.out.println("entrando " + actividad);

            }
            if (localSel.getTipoLocal() != null && localSel.getTipoLocal().getValor() != null && localSel.getTipoLocal().getValor().compareTo(BigDecimal.ZERO) == 1) {
                System.out.println("localComercial.getTipoLocal() " + localSel.getTipoLocal().toString());
                System.out.println("localComercial.getTipoLocal().getValor() " + localSel.getTipoLocal().getValor());
                tamanio = localSel.getTipoLocal().getValor();
                tipoLocal = localSel.getTipoLocal();
                System.out.println("entrando getTipoLocal " + tamanio);
            } else {
                System.out.println("tipoLocal.getValor() " + tipoLocal.getValor());
                tamanio = tipoLocal.getValor();
                System.out.println(" tamanio " + tamanio);

            }
            if (localSel.getFactorUbicacion() != null && localSel.getFactorUbicacion().getValor() != null && localSel.getFactorUbicacion().getValor().compareTo(BigDecimal.ZERO) == 1) {
                ubicacion = localSel.getFactorUbicacion().getValor();
                factorUbicacion = localSel.getFactorUbicacion();
                System.out.println("entrando factorUbicacion " + ubicacion);
            } else {

                ubicacion = factorUbicacion.getValor();
                System.out.println(" ubicacion " + ubicacion);
            }

            if (tamanio == null) {
                System.out.println("null");
                tamanio = BigDecimal.ZERO;

            }
            if (ubicacion == null) {
                System.out.println("null");
                ubicacion = BigDecimal.ZERO;
            }
            if (actividad == null) {
                System.out.println("null");
                actividad = BigDecimal.ZERO;
            }

            System.out.println("tasa htl " + ubicacion + "\t \t" + tamanio + "\t \t" + actividad);

            BigDecimal temp = ubicacion.multiply(tamanio).multiply(actividad);
            rubroSelect.setValor(temp);
            rubroSelect.setCantidad(1);
            rubroSelect.setValorTotal(rubroSelect.getValor());
            detalleHabilitacion.get(0).setValorTotal(rubroSelect.getValor());
            detalleHabilitacion.get(4).setValorTotal(services.interesTemp(new Date(), rubroSelect.getValor(), tipoLiquidacionHabilitacion.getId()));

        }

        if (tamanio.doubleValue() == 0 && ubicacion.doubleValue() == 0 && actividad.doubleValue() == 0) {
            JsfUtil.addWarningMessage("Requisitios Necesario para Hallar La tasa de habilitacion y control",
                    "El Local Seleccionado no posee tamaño o actividad o ubicación");
            return;
        }

        liquidacionHabilitacion.setTotalPago(totalHabilitacion());

    }

    public BigDecimal interestmp(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = services.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");

        return interes;
    }

    public void imprimirPermisoFuncionamiento() {

        ss.setNombreReporte("permisoFuncionamientoGlobal");

        List<FinaRenLiquidacion> liquidacionTemp = liquidacionService.listaLiquidacionTmp(localSel, liquidacion.getAnio());
        if (liquidacionTemp != null) {
            for (FinaRenLiquidacion item : liquidacionTemp) {
                item.setInteresTemp(interestmp(item));
                liquidacionService.edit(item);
            }
        }

        String descripcion = "";
        String cliente = "";
        String identificacion = "";
        String liquidadorResponsable = "";

        if (liquidacionTemp != null) {
            FinaRenLiquidacion tmp = liquidacionTemp.get(0);
            if (tmp.getPatente() != null && tmp.getPatente().getPropietario() != null) {
                cliente = tmp.getPatente().getPropietario().getNombreCompleto();
                identificacion = tmp.getPatente().getPropietario().getIdentificacionCompleta();
            } else {
                cliente = tmp.getNombreComprador();
                identificacion = tmp.getIdentificacion();
            }
            cliente = tmp.getLocalComercial().getNombreLocal();
            liquidadorResponsable = tmp.getLiquidadorResponsable();
            String clave = "";
            String numLocal = localSel.getNumLocal();
            if (tmp.getPredio() != null) {
                clave = tmp.getPredio().getClaveCat();
            } else if (tmp.getLocalComercial() != null) {
                clave = tmp.getLocalComercial().getClavePreial();
                numLocal = localSel.getNumLocal();

            }

            if (clave == null) {
                clave = "";
            }

            descripcion = "El Departamento de Rentas del Gobierno Autónomo Descentralizado Municipal del Cantón Durán le envia a Ustedes los valores que su empresa con código "
                    + clave + "-LC-" + numLocal + " e Identificación " + identificacion + " adeuda por los siguiente conceptos:";
        }
        ss.addParametro("cliente", cliente);
        ss.addParametro("id_local", localSel.getId());
        ss.addParametro("anio", liquidacion.getAnio());
        ss.addParametro("liquidadorResponsable", liquidadorResponsable);
        ss.addParametro("usuario_impresion", session.getNameUser());
        ss.addParametro("descripcion", descripcion);
//                    if (excel) {
//                        ss.setOnePagePerSheet(false);
//                        ss.setContentType("xlsx");
//                    }
        System.out.println("imprimeendo");
        ss.setNombreSubCarpeta("GestionTributatia/Transacciones");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    public void permisosAdicionales() {
        liquidacionPermisosAdicionales.setTipoLiquidacion(null);
        listLiquidacionPermisosAdicionales = new ArrayList<>();
        parametros = new HashMap<>();
        parametros.put("transaccionPadre", 178L);
        parametros.put("estado", true);
        listLiquidacionPermisosAdicionales = services.findAll(FinaRenTipoLiquidacion.class, parametros);

    }

    public void cargarRubros() {
        if (liquidacionPermisosAdicionales.getTipoLiquidacion() != null && liquidacionPermisosAdicionales.getTipoLiquidacion().getId() != null) {
            detallePermisoAdicional = new ArrayList<>();
            List<FinaRenRubrosLiquidacion> result = new ArrayList<>();
            result = services.getRubrosPorLiquidacion(liquidacionPermisosAdicionales.getTipoLiquidacion().getId());

            for (FinaRenRubrosLiquidacion item : result) {
                item.setCantidad(1);
                if (item.getValor() == null) {
                    item.setValor(BigDecimal.ZERO);
                }

                if (item.getPrioridad() != null && item.getPrioridad().intValue() == 3) {

                    BigDecimal valor = detallePermisoAdicional.get(0).getValorTotal();
                    if (valor == null) {
                        valor = BigDecimal.ZERO;
                    }
                    item.setValor(services.interesTemp(new Date(), valor, liquidacionPermisosAdicionales.getTipoLiquidacion().getId()));
                }

                item.setValorTotal(item.getValor());
                detallePermisoAdicional.add(item);
            }
        }
        liquidacionPermisosAdicionales.setTotalPago(totalPermisosAdicionales());
    }

    public BigDecimal totalPermisosAdicionales() {
        BigDecimal sum = BigDecimal.ZERO;
        if (detallePermisoAdicional != null) {
            for (FinaRenRubrosLiquidacion item : detallePermisoAdicional) {
                if (item.getCobrar() != null && item.getCobrar()) {
                    sum = sum.add(item.getValorTotal());
                }
            }
        }
        return sum;
    }

    public void sumarActivos() {
        if (balanceActivos.getActivoTotal() == null) {
            balanceActivos.setActivoTotal(BigDecimal.ZERO);
        }
        if (balanceActivos.getPasivoTotal() == null) {
            balanceActivos.setPasivoTotal(BigDecimal.ZERO);
        }
        detalle.get(0).setValor(balanceActivos.getActivoTotal());
        impuesto();
    }

    public void sumarPasivos() {
        if (balanceActivos.getPasivoTotal() == null) {
            balanceActivos.setPasivoTotal(BigDecimal.ZERO);
        }
        detalle.get(1).setValor(balanceActivos.getPasivoTotal());
        detalle.get(2).setValor(balanceActivos.getActivoTotal().subtract(balanceActivos.getPasivoTotal()));

        impuesto();
    }

    public void modificarTotal() {
        try {
            switch (tipoLiquidacion.getCodigoTituloReporte().intValue()) {
                case 7:
                    detalle.get(4).setValor(liquidacion.getTotalPago());
                    break;
                default:
                    detalle.get(0).setValor(liquidacion.getTotalPago());
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.OFF, "", e);
        }
    }

    public void impuesto() {
        BigDecimal base = baseImponible(); //(BigDecimal) util.getExpression("baseImponible", new Object[]{local});

        BigDecimal porcentajeImpuesto = services.find(FinaRenRubrosLiquidacion.class, 417L).getValor().divide(BigDecimal.valueOf(1000));

        BigDecimal total = base.multiply(porcentajeImpuesto); //(BigDecimal) util.getExpression("impuesto", new Object[]{local});
        BigDecimal interesAcumulado = BigDecimal.ZERO;
        System.out.println("base " + base + "    " + total);
        if (balanceActivos.getPorcentajeParticipacion() != null) {
            total = total.multiply(balanceActivos.getPorcentajeParticipacion()).divide(new BigDecimal("100"));
        }

        liquidacion.setTotalPago(total);
        if (balanceActivos.getAnioBalance() != null) {
            mesesInteres = 0;
        }
        detalle.get(3).setValor(base);
        detalle.get(4).setValor(total);
        detalle.get(5).setValor(services.find(FinaRenRubrosLiquidacion.class, 418L).getValor());
        detalle.get(6).setValor(services.interesTemp(new Date(), detalle.get(4).getValor(), tipoLiquidacion.getId()));
        detalle.get(10).setValor(verificacionValor);
        liquidacion.setTotalTmp(detalle.get(4).getValor().add(detalle.get(5).getValor()).add(detalle.get(6).getValor()).add(detalle.get(10).getValor()).add(detalle.get(6).getValor()));
        liquidacion.setTotalPago(detalle.get(4).getValor().add(detalle.get(5).getValor()).add(detalle.get(6).getValor()).add(detalle.get(10).getValor()).add(detalle.get(6).getValor()));
    }

    public BigDecimal sumarActivos(RenActivosLocalComercial local) {
        if (local.getActivoTotal() == null && local.getActivoContingente() == null) {
            return BigDecimal.ZERO;
        }
        return local.getActivoTotal().add(local.getActivoContingente());
    }

    public BigDecimal sumarPasivos(RenActivosLocalComercial local) {
        if (local.getPasivoTotal() == null && local.getPasivoContingente() == null) {
            return BigDecimal.ZERO;
        }
        return local.getPasivoTotal().add(local.getPasivoContingente());
    }

    public BigDecimal diferenciaActivosVsPasivos(RenActivosLocalComercial local) {
        return sumarActivos(local).subtract(sumarPasivos(local));
    }

    public BigDecimal baseImponible() {
        BigDecimal base = BigDecimal.ZERO;
        BigDecimal div = new BigDecimal("100");
        try {
            if (balanceActivos.getPorcentajeIngreso() == null || balanceActivos.getPorcentajeIngreso().compareTo(BigDecimal.ZERO) == 0) {
                base = balanceActivos.getActivoTotal().subtract(balanceActivos.getPasivoTotal());
            } else {
                if (balanceActivos.getPorcentajeIngreso().compareTo(div) == 0) {
                    base = BigDecimal.ZERO;
                } else {
                    base = balance.getActivoTotal().subtract(balanceActivos.getPasivoTotal()).multiply(balanceActivos.getPorcentajeIngreso()).divide(div);

                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Base Imponible", e);
        }
        return base;
    }

    public BigDecimal impuesto(RenActivosLocalComercial local) {
        try {
            BigDecimal porcentajeImpuesto = BigDecimal.valueOf(1.5).divide(BigDecimal.valueOf(1000));
            return baseImponible().multiply(porcentajeImpuesto);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Impuesto", e);
        }
        return BigDecimal.ZERO;
    }

    public void openDlogoDistibuccion() {

        if (distribuccion) {
            BigDecimal porcentajeImpuesto = services.find(FinaRenRubrosLiquidacion.class, 417L).getValor().divide(BigDecimal.valueOf(1000));

            BigDecimal total = baseImponible().multiply(porcentajeImpuesto);

            porcentajeFaltante = new BigDecimal("100").subtract(balanceActivos.getPorcentajeParticipacion());

            verificacionValor = (total.multiply(porcentajeFaltante)).divide(new BigDecimal("100"));
            participacionCantones = new ParticipacionCantones();
            loadCantones();
            JsfUtil.executeJS("PF('dlogoDistribuccion').show();");
            JsfUtil.update("frmDistribuccion");
            JsfUtil.update("frmAlcPlus");
            if (listParticipacionCantones != null && !listParticipacionCantones.isEmpty()) {
                listParticipacionCantones.get(0).setPorcentaje(balanceActivos.getPorcentajeParticipacion());
                listParticipacionCantones.get(0).setValor((total.multiply(balanceActivos.getPorcentajeParticipacion())).divide(new BigDecimal("100")));
                return;
            }
            detalle.get(10).setValor(verificacionValor);
            participacionCantones.setCantones(cantonService.getCantonCodigo("0907"));
            participacionCantones.setPorcentaje(balanceActivos.getPorcentajeParticipacion());
            participacionCantones.setValor((total.multiply(balanceActivos.getPorcentajeParticipacion())).divide(new BigDecimal("100")));
            listParticipacionCantones.add(participacionCantones);
            verificaciondistibuccion = verificaciondistibuccion.add(participacionCantones.getPorcentaje());
            participacionCantones = new ParticipacionCantones();
            JsfUtil.update("frmDistribuccion");
            JsfUtil.update("frmAlcPlus");
        }
    }

    public void addDistribuccion() {
        BigDecimal porcentajeImpuesto = services.find(FinaRenRubrosLiquidacion.class, 417L).getValor().divide(BigDecimal.valueOf(1000));

        BigDecimal total = baseImponible().multiply(porcentajeImpuesto);
        if (verificaciondistibuccion.add(participacionCantones.getPorcentaje()).compareTo(new BigDecimal("100.00")) == 1) {
            JsfUtil.addWarningMessage("", "No se puede ingresar esta distribucion porque sobrepasa el 100%");
            return;
        }

        participacionCantones.setValor((total.multiply(participacionCantones.getPorcentaje())).divide(new BigDecimal("100")));
        listParticipacionCantones.add(participacionCantones);
        verificaciondistibuccion = verificaciondistibuccion.add(participacionCantones.getPorcentaje());
        participacionCantones = new ParticipacionCantones();

    }

    public void actualizarTotalActivosDistibuccion() {
        BigDecimal suma = BigDecimal.ZERO;
        if (listParticipacionCantones != null && !listParticipacionCantones.isEmpty()) {
            for (ParticipacionCantones item : listParticipacionCantones) {
                if (item.getPorcentaje() != null) {
                    suma = suma.add(item.getPorcentaje());
                }
            }
        }

        if (suma.compareTo(new BigDecimal("100.00")) != 0) {
            JsfUtil.addWarningMessage("", "El porcentaje de distribucción tiene que ser el 100%");

        } else {
            liquidacion.setTotalPago(detalle.get(4).getValor().add(detalle.get(5).getValor()).add(detalle.get(6).getValor()).add(detalle.get(10).getValor()));

            JsfUtil.executeJS("PF('dlogoDistribuccion').hide()");
            JsfUtil.update("frmDistribuccion");
        }
    }

    public void eliminarDistribuccion(int index) {
        verificaciondistibuccion = verificaciondistibuccion.subtract(listParticipacionCantones.get(index).getPorcentaje());
        listParticipacionCantones.remove(index);
    }

    public void guardardistribuccion(FinaRenLiquidacion li) {
        if (listParticipacionCantones != null && !listParticipacionCantones.isEmpty() && liquidacion.getTipoLiquidacion().getId().equals(7L)) {
            for (ParticipacionCantones item : listParticipacionCantones) {
                ParticipacionCantones datos = Utils.clone(item);
                datos.setLiquidacion(li);
                participacionCantonesServices.create(datos);
            }

        }
    }

    public void editarDetallePago(FinaRenRubrosLiquidacion item, int index) {

        System.out.println("item " + item.getValorTotal());
        detallePermisoAdicional.remove(index);
        detallePermisoAdicional.add(index, item);
        liquidacionPermisosAdicionales.setTotalPago(totalPermisosAdicionales());
    }

    public void imprimirDist(FinaRenLiquidacion liq) {
        viewReport.borrarParametros();
        viewReport.instanciarParametros();
        viewReport.addParametro("id", liq.getId());
        viewReport.addParametro("descuento", liq.getDescuento());
        viewReport.addParametro("interes", interes(liq));
        viewReport.addParametro("valor_exonerado", liq.getValorExoneracion());
        viewReport.addParametro("total", liq.getTotalPago().add(interes(liq)));
        viewReport.addParametro("recargo", liq.getRecargo());
        viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
        viewReport.addParametro("pagon_final", liq.getTotalPago().add(interes(liq)));
        viewReport.setNombreReporte("distribucionActivoTotales");
        viewReport.setNombreSubCarpeta("GestionTributatia/general");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = services.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public void generarValorLiquidacion() {
        try {
            RenFactorPorMetro factor1;
            RenFactorPorCapital factor2;
            BigDecimal valorLiq = BigDecimal.ZERO;
            BigDecimal baseImp;
            BigDecimal interesAcumulado = BigDecimal.ZERO;
            BigDecimal calculoArticuloOcho = BigDecimal.ZERO;
            valorExonerado = BigDecimal.ZERO;
            patente = localSel.getPatente();
            System.out.println("(balance.getCapital()  " + balance.getCapital());
            if (balance.getCapital() != null && balance.getCapital().compareTo(BigDecimal.ZERO) > 0) {
//                if (patente != null) {
                BigDecimal capital = BigDecimal.ZERO;
                if (balance.getPorcentajeParticipacion() != null) {
                    capital = balance.getCapital().multiply(balance.getPorcentajeParticipacion().divide(new BigDecimal("100")));
                } else {
                    capital = balance.getCapital();
                }
                factor2 = (RenFactorPorCapital) services.find("SELECT r FROM RenFactorPorCapital r WHERE :capital > r.desde AND :capital <= r.hasta", new String[]{"capital"}, new Object[]{capital});

                System.out.println("factor " + factor2.getExcedente() + " \t\t" + factor2.getValor());
                if (factor2 != null) {
                    if (factor2.getAplicaPorcentaje()) {
                        valorLiq = capital.multiply(factor2.getValor()).divide(new BigDecimal("100"));
                    } else {
                        baseImp = ((capital.subtract(factor2.getDesde())).multiply(factor2.getExcedente())).divide(new BigDecimal("100"));
                        valorLiq = factor2.getValor().add(baseImp);
                    }
                } else {
                    valorLiq = capital;
                }

                System.out.println("valorLiqui " + valorLiq);

                rubrosLiquidacion = services.getRubrosPorLiquidacion(tipoLiquidacionPatente.getId());

                if (valorLiq.compareTo(new BigDecimal("10.00")) == -1) {
                    valorLiq = new BigDecimal("10.00");
                }
                if (valorLiq.compareTo(new BigDecimal("25000.00")) == 1) {
                    valorLiq = new BigDecimal("25000.00");
                }

                detallePatente.get(0).setValorTotal(valorLiq);

                for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
                    if (temp.getPrioridad().intValue() == 1) {
                        if (exoneracionSeleccionada != null && exoneracionSeleccionada.getId() != null) {
                            if ("tercera_edad".equals(exoneracionSeleccionada.getCodigoCategoriaPredioRes())) {
                                valorExonerado = (valorLiq.multiply(exoneracionSeleccionada.getPorcentaje()).divide(new BigDecimal("100")));
                                detallePatente.get(0).setValorTotal(valorLiq.subtract(valorExonerado));

                            } else if ("deduccion_tres_anios".equals(exoneracionSeleccionada.getCodigoCategoriaPredioRes())) {

                                BigDecimal promedio = valorLiq.multiply(new BigDecimal("0.33333"));
                                BigDecimal result = valorLiq.subtract(promedio);
                                valorExonerado = promedio;
                                if (promedio.compareTo(BigDecimal.ZERO) == 0) {
                                    detallePatente.get(0).setValorTotal(valorLiq);
                                } else {
                                    detallePatente.get(0).setValorTotal(result);
                                }

                            } else if ("incentivo_tributario".equals(exoneracionSeleccionada.getCodigoCategoriaPredioRes())) {

                            } else if ("articulo_ocho".equals(exoneracionSeleccionada.getCodigoCategoriaPredioRes())) {

                                valorExonerado = valorLiq.multiply(exoneracionSeleccionada.getPorcentaje().divide(new BigDecimal("100")));
                                calculoArticuloOcho = valorLiq.subtract(valorExonerado);
                                detallePatente.get(0).setValorTotal(calculoArticuloOcho);
                            } else {
                                detallePatente.get(0).setValorTotal(valorLiq);
                            }

                        } else {
                            detallePatente.get(0).setValorTotal(valorLiq);
                        }

                    }
                    if (temp.getPrioridad().intValue() == 2) {

                        liquidacionPatente.setTotalPago(liquidacionPatente.getTotalPago().add(detallePatente.get(1).getValor()));
                    }
                    if (temp.getPrioridad().intValue() == 3) {

                        if (balance.getAnioBalance() != null) {
                            mesesInteres = 0;
                            //     Map<String, BigDecimal> calculo = services.valoresInteres(liquidacionPatente, new Date());//                                     
                            detallePatente.get(2).setValorTotal(interesAcumulado);
                            //mesesInteres = calculo.get("mesesInteres").intValue();
                            System.out.println("Menses interes " + mesesInteres);
                            balance.setInteres(interesAcumulado);
                            balance.setMesesInteres(mesesInteres);
                        } else {
                            detallePatente.get(2).setValorTotal(BigDecimal.ZERO);
                            mesesInteres = 0;
                        }
                    }
                }

                liquidacionPatente.setBaseImponible(valorLiq);
                liquidacionPatente.setEstaExonerado(Boolean.TRUE);
                balance.setMesesInteres(mesesInteres);
                balance.setInteres(interesAcumulado);
//                }

            }
            detallePatente.get(3).setValorTotal(services.interesTemp(new Date(), detallePatente.get(0).getValorTotal(), tipoLiquidacionPatente.getId()));
            liquidacionPatente.setTotalPago(totalPago(2));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public BigDecimal totalHabilitacion() {
        BigDecimal sum = BigDecimal.ZERO;
        if (detalleHabilitacion != null) {
            for (FinaRenRubrosLiquidacion item : detalleHabilitacion) {
                sum = sum.add(item.getValorTotal());
            }
        }
        return sum;
    }

    public void aplicarMulta() throws ParseException {

        if (remisionInteresService.aplicaMulta(tipoLiquidacionPatente, Utils.getAnio(new Date()))) {

            BigDecimal valorMul = BigDecimal.ZERO;
            valorMul = detallePatente.get(0).getValorTotal();
            BigDecimal porc = new BigDecimal("6").divide(new BigDecimal("12"));
            Calendar fecha = Calendar.getInstance();
            fecha.set(liquidacion.getAnio(), 6, 1);

            int meses = Utils.obtenerDistanciaMeses(fecha.getTime(), new Date());
            System.out.println("meses " + meses);
            if (meses > 0) {

                System.out.println("meses " + meses);
                BigDecimal re = valorMul.multiply(porc).multiply(new BigDecimal(meses)).divide(new BigDecimal("100"));
                System.out.println("re " + re);
                if (re.compareTo(new BigDecimal("30.00")) == -1) {
                    re = new BigDecimal("30.00");
                } else if (re.compareTo(new BigDecimal("1500.00")) == 1) {
                    re = new BigDecimal("1500.00");
                }

                detallePatente.get(2).setValorTotal(re);
                System.out.println("re " + re);
                liquidacionPatente.setTotalPago(totalPago(2));
            }
        } else {
            detallePatente.get(2).setValorTotal(BigDecimal.ZERO);
            liquidacionPatente.setTotalPago(totalPago(2));
            JsfUtil.addWarningMessage("", "Según el año no se permite generar multa");
        }
    }

    public void quitarMulta() {
        detallePatente.get(2).setValorTotal(BigDecimal.ZERO);
        liquidacionPatente.setTotalPago(totalPago(2));
    }

    public BigDecimal totalPago(int a) {

        BigDecimal sum = BigDecimal.ZERO;

        if (a == 1) {
            sum = detalle.get(4).getValor().add(detalle.get(5).getValor()).add(detalle.get(6).getValor()).add(detalle.get(10).getValor()).add(detalle.get(6).getValor());

        } else if (a == 2) {
            for (FinaRenRubrosLiquidacion item : detallePatente) {
                sum = sum.add(item.getValorTotal());
            }

        } else if (a == 3) {
            for (FinaRenRubrosLiquidacion item : detalleHabilitacion) {
                sum = sum.add(item.getValorTotal());
            }
        } else if (a == 4) {
            for (FinaRenRubrosLiquidacion item : detallePermisoAdicional) {
                sum = sum.add(item.getValorTotal());
            }
        }

        return sum.setScale(2, RoundingMode.UP);
    }

    private FinaRenEstadoLiquidacion getEstadoLiquidacionByDesc(Long id) {
        return services.find(FinaRenEstadoLiquidacion.class, id);
    }

    public void procesar() {

        idActivos = null;
        idPatente = null;
        idHabilitacion = null;
        idPermisos = null;
        if (patenteShow) {
            if (liquidacionService.verificarLiquidacionPermiso(localSel, liquidacion.getAnio(), liquidacionPatente.getTipoLiquidacion(), 2L)) {
                JsfUtil.addWarningMessage("", "El Local ya tiene una liquidacion pendiente de patente para el año " + liquidacion.getAnio());
                return;
            }
        }
        if (activoShow) {
            if (liquidacionService.verificarLiquidacionPermiso(localSel, liquidacion.getAnio(), liquidacion.getTipoLiquidacion(), 2L)) {
                JsfUtil.addWarningMessage("", "El Local ya tiene una liquidacion pendiente de activos totales para el año " + liquidacion.getAnio());
                return;
            }
        }
        if (habilitacionShow) {
            if (liquidacionService.verificarLiquidacionPermiso(localSel, liquidacion.getAnio(), liquidacionHabilitacion.getTipoLiquidacion(), 2L)) {
                JsfUtil.addWarningMessage("", "El Local ya tiene una liquidacion pendiente de tasa de habilitacion para el año " + liquidacion.getAnio());
                return;
            }
        }
        if (permisoAdicionalesShow) {

            if (liquidacionService.verificarLiquidacionPermiso(localSel, liquidacion.getAnio(), liquidacionPermisosAdicionales.getTipoLiquidacion(), 2L)) {
                JsfUtil.addWarningMessage("", "El Local ya tiene una liquidacion pendiente de permisos Adicionales para el año " + liquidacion.getAnio());
                return;
            }
        }
        observacion.setObservacion(observacion1);
        if (activoShow) {
            try {
                if (localSel == null) {
                    JsfUtil.addErrorMessage("Info", "Debe seleccionar el local para proceder al cobro");
                    return;
                }
                if (localSel.getId() == null) {
                    JsfUtil.addErrorMessage("Info", "Debe seleccionar un local para proceder al cobro");
                    return;
                }

                patente = localSel.getPatente();
                Object numLiquidacion = null;
                liquidacion.setTramite(BigInteger.valueOf(tramite.getId()));
                liquidacion.setObservacion(observacion.getObservacion());
                liquidacion.setTipoLiquidacion(tipoLiquidacion);
                liquidacion.setComprador(patente.getPropietario());
                liquidacion.setVendedor(patente.getPropietario());
                liquidacion.setUsuarioIngreso(session.getNameUser());

                if (liquidacion.getEstadoLiquidacion() == null) {
                    liquidacion.setEstadoLiquidacion(getEstadoLiquidacionByDesc(2L));
                }

                liquidacion.setTipoLiquidacion(tipoLiquidacion);
                liquidacion.setPatente(patente);
                liquidacion.setLocalComercial(localSel);
                liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
                liquidacion.setIpUserSession(session.getIpClient());
                if (balanceActivos != null) {
                    balanceActivos.setPatente(patente);
                }

                liquidacion.setSaldo(liquidacion.getTotalPago());
                liquidacion.setValidada(Boolean.TRUE);
                liquidacion.setLiquidadorAprobador(session.getNameUser());
                liquidacion.setLiquidadorResponsable(session.getNameUser());
                liquidacion.setUsuarioValida(session.getNameUser());
                balanceActivos.setUsuarioIngreso(session.getNameUser());
                liquidacion.setRentas(Boolean.TRUE);
                detalle.get(6).setValor(BigDecimal.ZERO);
                liquidacion.setTotalPago(totalPago(1));

                liquidacion = services.guardarLiquidacionPatente(liquidacion, detalle, tipoLiquidacion, balanceActivos);

                guardardistribuccion(liquidacion);
                idActivos = liquidacion.getIdLiquidacion();

            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Guardar Liquidacion LC", e);
            }

        }
        if (patenteShow) {

            try {
                if (localSel == null) {
                    JsfUtil.addErrorMessage("Info", "Debe seleccionar el local para proceder al cobro");
                    return;
                }
                if (localSel.getId() == null) {
                    JsfUtil.addErrorMessage("Info", "Debe seleccionar un local para proceder al cobro");
                    return;
                }

                patente = localSel.getPatente();
                Object numLiquidacion = null;

                liquidacionPatente.setTramite(BigInteger.valueOf(tramite.getId()));
                liquidacionPatente.setObservacion(observacion.getObservacion());
                liquidacionPatente.setTipoLiquidacion(tipoLiquidacionPatente);
                liquidacionPatente.setComprador(patente.getPropietario());
                liquidacionPatente.setVendedor(patente.getPropietario());
                liquidacionPatente.setUsuarioIngreso(session.getNameUser());

                if (liquidacionPatente.getEstadoLiquidacion() == null) {
                    liquidacionPatente.setEstadoLiquidacion(getEstadoLiquidacionByDesc(2L));
                }

                liquidacionPatente.setTipoLiquidacion(tipoLiquidacionPatente);
                liquidacionPatente.setPatente(patente);
                liquidacionPatente.setLocalComercial(localSel);
                liquidacionPatente.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
                liquidacionPatente.setIpUserSession(session.getIpClient());
                if (balance != null) {
                    balance.setPatente(patente);
                }

                liquidacionPatente.setSaldo(liquidacionPatente.getTotalPago());
                liquidacionPatente.setValidada(Boolean.TRUE);
                liquidacionPatente.setLiquidadorAprobador(session.getNameUser());
                liquidacionPatente.setLiquidadorResponsable(session.getNameUser());
                liquidacionPatente.setUsuarioValida(session.getNameUser());
                balance.setUsuarioIngreso(session.getNameUser());
                liquidacionPatente.setRentas(Boolean.TRUE);
                detallePatente.get(3).setValorTotal(BigDecimal.ZERO);
                liquidacionPatente.setTotalPago(totalPago(2));

                List<FinaRenDetLiquidacion> detalleDetPatente = new ArrayList<>();

                for (FinaRenRubrosLiquidacion dl : detallePatente) {
                    FinaRenDetLiquidacion detalle = new FinaRenDetLiquidacion();
                    if (dl.getCantidad() == null) {
                        dl.setCantidad(1);
                    }
                    detalle.setCantidad(dl.getCantidad());
                    detalle.setLiquidacion(liquidacionPatente);
                    detalle.setRubro(dl);
                    detalle.setValor(dl.getValorTotal());
                    detalleDetPatente.add(detalle);
                }

                liquidacionPatente = services.guardarLiquidacionPatente(liquidacionPatente, detalleDetPatente, tipoLiquidacionPatente, balance);
                guardarExoneracion(liquidacionPatente);

                idPatente = liquidacionPatente.getIdLiquidacion();

            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Guardar Liquidacion LC", e);
            }

        }
        if (habilitacionShow) {
            liquidacionHabilitacion.setTramite(BigInteger.valueOf(tramite.getId()));
            liquidacionHabilitacion.setLocalComercial(localSel);
            liquidacionHabilitacion.setObservacion(observacion.getObservacion());
            liquidacionHabilitacion.setTipoLiquidacion(tipoLiquidacionHabilitacion);
            liquidacionHabilitacion.setUsuarioIngreso(session.getNameUser());
            liquidacionHabilitacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            if (liquidacionHabilitacion.getTipoLiquidacion().getNecesitaValidacionRentas()) {
                liquidacionHabilitacion.setValidada(Boolean.FALSE);
            } else {
                liquidacionHabilitacion.setValidada(Boolean.TRUE);
            }

            detalleHabilitacion.get(4).setValorTotal(BigDecimal.ZERO);
            liquidacionHabilitacion.setTotalPago(totalPago(3));
            List<FinaRenDetLiquidacion> detalleHabilitacionList = new ArrayList<>();

            for (FinaRenRubrosLiquidacion item : detalleHabilitacion) {
                FinaRenDetLiquidacion dat = new FinaRenDetLiquidacion();
                dat.setRubro(item);
                dat.setValorSinDescuento(BigDecimal.ZERO);
                dat.setEstado(true);
                dat.setValor(item.getValorTotal());
                dat.setEstado(true);
                dat.setCantidad(item.getCantidad());

                if (item.getValorTotal() != null && item.getValorTotal().compareTo(BigDecimal.ZERO) == 1) {
                    detalleHabilitacionList.add(dat);
                }

            }

            liquidacionHabilitacion.setFechaIngreso(new Date());
            liquidacionHabilitacion = liquidacionService.crearLiquidacion(liquidacionHabilitacion, detalleHabilitacionList);
            //      liquidacionHabilitacion.calcularPago();
            idHabilitacion = liquidacionHabilitacion.getIdLiquidacion();
        }

        if (permisoAdicionalesShow) {
            liquidacionPermisosAdicionales.setTramite(BigInteger.valueOf(tramite.getId()));
            liquidacionPermisosAdicionales.setLocalComercial(localSel);
            liquidacionPermisosAdicionales.setObservacion(observacion.getObservacion());
            liquidacionPermisosAdicionales.setTipoLiquidacion(tipoLiquidacionPermisoAdicional);
            liquidacionPermisosAdicionales.setUsuarioIngreso(session.getNameUser());
            liquidacionPermisosAdicionales.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            if (liquidacionPermisosAdicionales.getTipoLiquidacion().getNecesitaValidacionRentas()) {
                liquidacionPermisosAdicionales.setValidada(Boolean.FALSE);
            } else {
                liquidacionPermisosAdicionales.setValidada(Boolean.TRUE);
            }

            detallePermisoAdicional.get(3).setValorTotal(BigDecimal.ZERO);
            liquidacionPermisosAdicionales.setTotalPago(totalPago(4));

            List<FinaRenDetLiquidacion> detallePermisosList = new ArrayList<>();

            for (FinaRenRubrosLiquidacion item : detallePermisoAdicional) {
                FinaRenDetLiquidacion dat = new FinaRenDetLiquidacion();
                dat.setRubro(item);
                dat.setValorSinDescuento(BigDecimal.ZERO);
                dat.setEstado(true);
                dat.setValor(item.getValorTotal());
                dat.setEstado(true);
                dat.setCantidad(item.getCantidad());
                if (item.getValorTotal() != null && item.getValorTotal().compareTo(BigDecimal.ZERO) == 1) {
                    detallePermisosList.add(dat);
                }
            }

            liquidacionPermisosAdicionales.setFechaIngreso(new Date());
            liquidacionPermisosAdicionales = liquidacionService.crearLiquidacion(liquidacionPermisosAdicionales, detallePermisosList);
            // liquidacionPermisosAdicionales.calcularPago();
            idPermisos = liquidacionPermisosAdicionales.getIdLiquidacion();
        }
        JsfUtil.executeJS("PF('dlgIdLiquidacion').show()");
        JsfUtil.update("numLiq");
    }

    public void calculosAdicionales() {
        interes(liquidacion);
        liquidacion.setPagoFinal(liquidacion.getTotalPago().add(liquidacion.getRecargo()).add(liquidacion.getInteres()));
    }

    public void guardarExoneracion(FinaRenLiquidacion liquidacion) {
        if (exoneracionSeleccionada != null && liquidacion.getTipoLiquidacion().getId().equals(9L)) {
            solicitudExoneracion = new FnSolicitudExoneracion();
            solicitudExoneracion.setValor(valorExonerado);
            solicitudExoneracion.setExoneracionTipo(exoneracionSeleccionada);
            solicitudExoneracion = (FnSolicitudExoneracion) services.updateEntity(solicitudExoneracion);
            exoneracionLiquidacion = new FnExoneracionLiquidacion();
            exoneracionLiquidacion.setFechaIngreso(new Date());
            exoneracionLiquidacion.setExoneracion(solicitudExoneracion);
            exoneracionLiquidacion.setEstado(true);
            exoneracionLiquidacion.setUsuarioIngreso(session.getNameUser());
            exoneracionLiquidacion.setLiquidacionOriginal(liquidacion);
            exoneracionLiquidacion = (FnExoneracionLiquidacion) services.updateEntity(exoneracionLiquidacion);

        }
    }

    public BigDecimal interesReporte(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = services.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public void imprimirDistribuccion(FinaRenLiquidacion liq) {
        viewReport.borrarParametros();
        viewReport.instanciarParametros();
        viewReport.addParametro("id", liq.getId());
        viewReport.addParametro("descuento", liq.getDescuento());
        viewReport.addParametro("interes", interesReporte(liq));
        viewReport.addParametro("valor_exonerado", liq.getValorExoneracion());
        viewReport.addParametro("total", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.addParametro("recargo", liq.getRecargo());
        viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
        viewReport.addParametro("pagon_final", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.setNombreReporte("distribucionActivoTotales");
        viewReport.setNombreSubCarpeta("GestionTributatia/general");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimir(FinaRenLiquidacion liq) {
        System.out.println("liq " + liq.getId());

        interesReporte(liq);
        liq.setPagoFinal(liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));

        if (liq.getListDistribuciionCantones() != null && liq.getListDistribuciionCantones().size() > 0) {
            imprimirDistribuccion(liq);
        } else {

            viewReport.borrarParametros();
            viewReport.instanciarParametros();
            viewReport.addParametro("id", liq.getId());
            viewReport.addParametro("descuento", liq.getDescuento());
            viewReport.addParametro("interes", liq.getInteres());
            viewReport.addParametro("valor_exonerado", liq.getExoneracionSumValor());
            viewReport.addParametro("total", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.addParametro("recargo", liq.getRecargo());
            viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
            viewReport.addParametro("pagon_final", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.setNombreReporte("general");
            viewReport.setNombreSubCarpeta("GestionTributatia/general");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void completarTarea() {
        try {
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            String usuario = clienteService.getrolsUser(RolUsuario.tesorero);
            getParamts().put("usuarioTesoreria", usuario.equals("") ? "admin_1" : usuario);
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void cerrarDialog() {
        iniciarDatos();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getServices() {
        return services;
    }

    public List<FinaRenRubrosLiquidacion> getDetallePatente() {
        return detallePatente;
    }

    public LazyModel<FinaRenLiquidacion> getLazyLiquidacionDeudas() {
        return lazyLiquidacionDeudas;
    }

    public void setLazyLiquidacionDeudas(LazyModel<FinaRenLiquidacion> lazyLiquidacionDeudas) {
        this.lazyLiquidacionDeudas = lazyLiquidacionDeudas;
    }

    public ServletSession getViewReport() {
        return viewReport;
    }

    public void setViewReport(ServletSession viewReport) {
        this.viewReport = viewReport;
    }

    public FinaRenActividadComercial getSeleccionActividad() {
        return seleccionActividad;
    }

    public void setSeleccionActividad(FinaRenActividadComercial seleccionActividad) {
        this.seleccionActividad = seleccionActividad;
    }

    public List<FinaRenTipoLocalComercial> getListaTipoLocales() {
        return listaTipoLocales;
    }

    public void setListaTipoLocales(List<FinaRenTipoLocalComercial> listaTipoLocales) {
        this.listaTipoLocales = listaTipoLocales;
    }

    public FinaRenTipoLocalComercial getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(FinaRenTipoLocalComercial tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public List<CatalogoItem> getListaFactoresUbicacion() {
        return listaFactoresUbicacion;
    }

    public void setListaFactoresUbicacion(List<CatalogoItem> listaFactoresUbicacion) {
        this.listaFactoresUbicacion = listaFactoresUbicacion;
    }

    public CatalogoItem getFactorUbicacion() {
        return factorUbicacion;
    }

    public void setFactorUbicacion(CatalogoItem factorUbicacion) {
        this.factorUbicacion = factorUbicacion;
    }

    public List<FinaRenActividadComercial> getActividesLocales() {
        return actividesLocales;
    }

    public void setActividesLocales(List<FinaRenActividadComercial> actividesLocales) {
        this.actividesLocales = actividesLocales;
    }

    public void setDetallePatente(List<FinaRenRubrosLiquidacion> detallePatente) {
        this.detallePatente = detallePatente;
    }

    public List<FinaRenRubrosLiquidacion> getDetalleHabilitacion() {
        return detalleHabilitacion;
    }

    public String getIdActivos() {
        return idActivos;
    }

    public void setIdActivos(String idActivos) {
        this.idActivos = idActivos;
    }

    public String getIdPatente() {
        return idPatente;
    }

    public void setIdPatente(String idPatente) {
        this.idPatente = idPatente;
    }

    public String getIdHabilitacion() {
        return idHabilitacion;
    }

    public void setIdHabilitacion(String idHabilitacion) {
        this.idHabilitacion = idHabilitacion;
    }

    public String getIdPermisos() {
        return idPermisos;
    }

    public void setIdPermisos(String idPermisos) {
        this.idPermisos = idPermisos;
    }

    public void setDetalleHabilitacion(List<FinaRenRubrosLiquidacion> detalleHabilitacion) {
        this.detalleHabilitacion = detalleHabilitacion;
    }

    public List<FinaRenRubrosLiquidacion> getDetallePermisoAdicional() {
        return detallePermisoAdicional;
    }

    public void setDetallePermisoAdicional(List<FinaRenRubrosLiquidacion> detallePermisoAdicional) {
        this.detallePermisoAdicional = detallePermisoAdicional;
    }

    public BigDecimal getPorcentajeFaltante() {
        return porcentajeFaltante;
    }

    public void setPorcentajeFaltante(BigDecimal porcentajeFaltante) {
        this.porcentajeFaltante = porcentajeFaltante;
    }

    public boolean isActivoShow() {
        return activoShow;
    }

    public FinaRenLiquidacion getLiquidacionPatente() {
        return liquidacionPatente;
    }

    public void setLiquidacionPatente(FinaRenLiquidacion liquidacionPatente) {
        this.liquidacionPatente = liquidacionPatente;
    }

    public FinaRenLiquidacion getLiquidacionHabilitacion() {
        return liquidacionHabilitacion;
    }

    public void setLiquidacionHabilitacion(FinaRenLiquidacion liquidacionHabilitacion) {
        this.liquidacionHabilitacion = liquidacionHabilitacion;
    }

    public FinaRenLiquidacion getLiquidacionPermisosAdicionales() {
        return liquidacionPermisosAdicionales;
    }

    public void setLiquidacionPermisosAdicionales(FinaRenLiquidacion liquidacionPermisosAdicionales) {
        this.liquidacionPermisosAdicionales = liquidacionPermisosAdicionales;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionPatente() {
        return tipoLiquidacionPatente;
    }

    public void setTipoLiquidacionPatente(FinaRenTipoLiquidacion tipoLiquidacionPatente) {
        this.tipoLiquidacionPatente = tipoLiquidacionPatente;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionHabilitacion() {
        return tipoLiquidacionHabilitacion;
    }

    public void setTipoLiquidacionHabilitacion(FinaRenTipoLiquidacion tipoLiquidacionHabilitacion) {
        this.tipoLiquidacionHabilitacion = tipoLiquidacionHabilitacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionPermisoAdicional() {
        return tipoLiquidacionPermisoAdicional;
    }

    public void setTipoLiquidacionPermisoAdicional(FinaRenTipoLiquidacion tipoLiquidacionPermisoAdicional) {
        this.tipoLiquidacionPermisoAdicional = tipoLiquidacionPermisoAdicional;
    }

    public void setActivoShow(boolean activoShow) {
        this.activoShow = activoShow;
    }

    public boolean isPatenteShow() {
        return patenteShow;
    }

    public void setPatenteShow(boolean patenteShow) {
        this.patenteShow = patenteShow;
    }

    public boolean isHabilitacionShow() {
        return habilitacionShow;
    }

    public void setHabilitacionShow(boolean habilitacionShow) {
        this.habilitacionShow = habilitacionShow;
    }

    public boolean isPermisoAdicionalesShow() {
        return permisoAdicionalesShow;
    }

    public void setPermisoAdicionalesShow(boolean permisoAdicionalesShow) {
        this.permisoAdicionalesShow = permisoAdicionalesShow;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public void setPatente(FinaRenPatente patente) {
        this.patente = patente;
    }

    public LazyModel<FinaRenPatente> getPatentes() {
        return patentes;
    }

    public void setPatentes(LazyModel<FinaRenPatente> patentes) {
        this.patentes = patentes;
    }

    public RenBalancePatente getBalance() {
        return balance;
    }

    public void setBalance(RenBalancePatente balance) {
        this.balance = balance;
    }

    public RenBalancePatente getBalanceActivos() {
        return balanceActivos;
    }

    public void setBalanceActivos(RenBalancePatente balanceActivos) {
        this.balanceActivos = balanceActivos;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public Integer getMaxAnio() {
        return maxAnio;
    }

    public void setMaxAnio(Integer maxAnio) {
        this.maxAnio = maxAnio;
    }

    public Integer getMaxAnioAct() {
        return maxAnioAct;
    }

    public void setMaxAnioAct(Integer maxAnioAct) {
        this.maxAnioAct = maxAnioAct;
    }

    public Integer getMinAnio() {
        return minAnio;
    }

    public void setMinAnio(Integer minAnio) {
        this.minAnio = minAnio;
    }

    public Boolean getEsPersonaProp() {
        return esPersonaProp;
    }

    public void setEsPersonaProp(Boolean esPersonaProp) {
        this.esPersonaProp = esPersonaProp;
    }

    public Boolean getEsLocal() {
        return esLocal;
    }

    public void setEsLocal(Boolean esLocal) {
        this.esLocal = esLocal;
    }

    public Boolean getVariosRotulos() {
        return variosRotulos;
    }

    public void setVariosRotulos(Boolean variosRotulos) {
        this.variosRotulos = variosRotulos;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cliente getPersona() {
        return persona;
    }

    public void setPersona(Cliente persona) {
        this.persona = persona;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public List<FinaRenTipoLiquidacion> getTiposLiquidacions() {
        return tiposLiquidacions;
    }

    public void setTiposLiquidacions(List<FinaRenTipoLiquidacion> tiposLiquidacions) {
        this.tiposLiquidacions = tiposLiquidacions;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosLiquidacion() {
        return rubrosLiquidacion;
    }

    public void setRubrosLiquidacion(List<FinaRenRubrosLiquidacion> rubrosLiquidacion) {
        this.rubrosLiquidacion = rubrosLiquidacion;
    }

    public RenDesvalorizacion getDesvalorizacion() {
        return desvalorizacion;
    }

    public void setDesvalorizacion(RenDesvalorizacion desvalorizacion) {
        this.desvalorizacion = desvalorizacion;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public CatPredioModel getPredioModel() {
        return predioModel;
    }

    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }

    public Boolean getMostrarRequisitos() {
        return mostrarRequisitos;
    }

    public void setMostrarRequisitos(Boolean mostrarRequisitos) {
        this.mostrarRequisitos = mostrarRequisitos;
    }

    public String getCedulaRuc() {
        return cedulaRuc;
    }

    public void setCedulaRuc(String cedulaRuc) {
        this.cedulaRuc = cedulaRuc;
    }

    public List<FinaRenLocalComercial> getLocalesEnte() {
        return localesEnte;
    }

    public void setLocalesEnte(List<FinaRenLocalComercial> localesEnte) {
        this.localesEnte = localesEnte;
    }

    public LazyModel<FinaRenLocalComercial> getLocalesLazy() {
        return localesLazy;
    }

    public void setLocalesLazy(LazyModel<FinaRenLocalComercial> localesLazy) {
        this.localesLazy = localesLazy;
    }

    public FinaRenLocalComercial getLocalSel() {
        return localSel;
    }

    public void setLocalSel(FinaRenLocalComercial localSel) {
        this.localSel = localSel;
    }

    public List<FinaRenDetLiquidacion> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FinaRenDetLiquidacion> detalle) {
        this.detalle = detalle;
    }

    public RenActivosLocalComercial getLocal() {
        return local;
    }

    public void setLocal(RenActivosLocalComercial local) {
        this.local = local;
    }

    public Short getTipoExoneracion() {
        return tipoExoneracion;
    }

    public void setTipoExoneracion(Short tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }

    public Short getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(Short formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getTasaPermiso() {
        return tasaPermiso;
    }

    public void setTasaPermiso(BigDecimal tasaPermiso) {
        this.tasaPermiso = tasaPermiso;
    }

    public Integer getMesesInteres() {
        return mesesInteres;
    }

    public void setMesesInteres(Integer mesesInteres) {
        this.mesesInteres = mesesInteres;
    }

    public FnConvenioPago getConvenioPago() {
        return convenioPago;
    }

    public void setConvenioPago(FnConvenioPago convenioPago) {
        this.convenioPago = convenioPago;
    }

    public Boolean getLiqProvicional() {
        return liqProvicional;
    }

    public void setLiqProvicional(Boolean liqProvicional) {
        this.liqProvicional = liqProvicional;
    }

    public List<FinaRenLocalComercial> getLocalescomerciales() {
        return localescomerciales;
    }

    public void setLocalescomerciales(List<FinaRenLocalComercial> localescomerciales) {
        this.localescomerciales = localescomerciales;
    }

    public List<FnExoneracionTipo> getListaExoneraciones() {
        return listaExoneraciones;
    }

    public void setListaExoneraciones(List<FnExoneracionTipo> listaExoneraciones) {
        this.listaExoneraciones = listaExoneraciones;
    }

    public FnExoneracionTipo getExoneracionSeleccionada() {
        return exoneracionSeleccionada;
    }

    public void setExoneracionSeleccionada(FnExoneracionTipo exoneracionSeleccionada) {
        this.exoneracionSeleccionada = exoneracionSeleccionada;
    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

    public FnExoneracionLiquidacion getExoneracionLiquidacion() {
        return exoneracionLiquidacion;
    }

    public void setExoneracionLiquidacion(FnExoneracionLiquidacion exoneracionLiquidacion) {
        this.exoneracionLiquidacion = exoneracionLiquidacion;
    }

    public BigDecimal getValorExonerado() {
        return valorExonerado;
    }

    public void setValorExonerado(BigDecimal valorExonerado) {
        this.valorExonerado = valorExonerado;
    }

    public ParticipacionCantones getParticipacionCantones() {
        return participacionCantones;
    }

    public void setParticipacionCantones(ParticipacionCantones participacionCantones) {
        this.participacionCantones = participacionCantones;
    }

    public List<ParticipacionCantones> getListParticipacionCantones() {
        return listParticipacionCantones;
    }

    public void setListParticipacionCantones(List<ParticipacionCantones> listParticipacionCantones) {
        this.listParticipacionCantones = listParticipacionCantones;
    }

    public List<Canton> getListaCantones() {
        return listaCantones;
    }

    public void setListaCantones(List<Canton> listaCantones) {
        this.listaCantones = listaCantones;
    }

    public Boolean getDistribuccion() {
        return distribuccion;
    }

    public void setDistribuccion(Boolean distribuccion) {
        this.distribuccion = distribuccion;
    }

    public BigDecimal getVerificaciondistibuccion() {
        return verificaciondistibuccion;
    }

    public void setVerificaciondistibuccion(BigDecimal verificaciondistibuccion) {
        this.verificaciondistibuccion = verificaciondistibuccion;
    }

    public BigDecimal getVerificacionValor() {
        return verificacionValor;
    }

    public void setVerificacionValor(BigDecimal verificacionValor) {
        this.verificacionValor = verificacionValor;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<FinaRenTipoLiquidacion> getListLiquidacionPermisosAdicionales() {
        return listLiquidacionPermisosAdicionales;
    }

    public void setListLiquidacionPermisosAdicionales(List<FinaRenTipoLiquidacion> listLiquidacionPermisosAdicionales) {
        this.listLiquidacionPermisosAdicionales = listLiquidacionPermisosAdicionales;
    }

    public FinaRenLiquidacion getLiquidacionSelect() {
        return liquidacionSelect;
    }

    public void setLiquidacionSelect(FinaRenLiquidacion liquidacionSelect) {
        this.liquidacionSelect = liquidacionSelect;
    }

    public List<FinaRenDetLiquidacion> getDetLiquidaciones() {
        return detLiquidaciones;
    }

    public void setDetLiquidaciones(List<FinaRenDetLiquidacion> detLiquidaciones) {
        this.detLiquidaciones = detLiquidaciones;
    }

    public String getObservacion1() {
        return observacion1;
    }

    public void setObservacion1(String observacion1) {
        this.observacion1 = observacion1;
    }

//</editor-fold>
}
