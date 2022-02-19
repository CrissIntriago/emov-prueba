/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenActividadComercial;
import com.asgard.Entity.FinaRenLocalCategoria;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenLocalUbicacion;
import com.asgard.Entity.FinaRenPatente;
import com.asgard.Entity.FinaRenTipoLocalComercial;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Controller.FotosLocalComercial;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.ObservacionesLocal;
import com.gestionTributaria.Entities.RenLocalCantidadAccesorios;
import com.gestionTributaria.Entities.RenLocalComercialFoto;
import com.gestionTributaria.Entities.SecuenciaMemo;
import com.gestionTributaria.Services.FinaRenActividadComercialServices;
import com.gestionTributaria.Services.FinaRenLocalComercialService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.PatenteService;
import com.gestionTributaria.Services.RenLocalCantidadAccesoriosServices;
import com.gestionTributaria.Services.RenLocalComercialFotoServices;
import com.gestionTributaria.Services.SecuenciaMemoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "registroLocalPFPMB")
@ViewScoped
public class RegistroLocalPFPMB extends BpmnBaseRoot implements Serializable {
    @Inject
    private UserSession uSession;
    @Inject
    private ManagerService services;
    @Inject
    private ServletSession ss;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private FinaRenLocalComercialService comercialService;
    @Inject
    private FinaRenActividadComercialServices actividadComercialServices;
    @Inject
    private PatenteService patenteService;
    @Inject
    private RenLocalComercialFotoServices comercialFotoService;
    @Inject
    private RenLocalCantidadAccesoriosServices accesoriosServices;
    @Inject
    private SecuenciaMemoService secuenciaMemoService;
    @Inject
    private ServletSession viewReport;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;

    private UploadedFile file;
    private Cliente propietario;
    private Cliente representanteLegal;
    private LazyModel<Cliente> contribuyentes;
    private CatPredio predio;
    private LazyModel<CatPredio> predios;
    private FinaRenPatente patente;
    private LazyModel<FinaRenPatente> patentes;
    private String mensaje;
    private String observaciones;
    private LazyModel<FinaRenActividadComercial> actividades;
    private FinaRenActividadComercial actividad;
    private RenLocalCantidadAccesorios cantidad;
    private List<RenLocalCantidadAccesorios> listAccesorios;
    private FinaRenLocalComercial localComercial;
    private List<FinaRenLocalComercial> locales;
    private List<FinaRenActividadComercial> actividadesSeleccionadas;
    private boolean registro = true;
    private boolean registroActividad = true;
    private boolean registrarLocal = true;
    private DualListModel<FinaRenActividadComercial> dualListActividades;
    private List<FinaRenActividadComercial> targetActividades, sourceActividades;
    private long idLocal;
    private FinaRenLocalComercial local;
    private LazyModel<CatPredio> predioListLazy;
    private Integer index;
    private Long idPatente;
    private Boolean esEditable;
    private Map<String, Object> param;
    protected List<RenLocalComercialFoto> fotosLocalesComerciales;
    protected RenLocalComercialFoto localComercialFotoSeleccionada;
    private List<CatalogoItem> tipoNegocioList, tipoNegoicoSeleccionados, ubicacionList;
    private FinaRenLocalCategoria padreTuristico;
    private List<FinaRenLocalCategoria> padreTuristicoList;
    private List<FinaRenLocalCategoria> listaTuristicasActividad;
    private List<CatalogoItem> tiposAccesorios;
    private List<CatalogoItem> tiposAccesoriosSave;
    private RenLocalComercialFoto fotoLocalComercial;
    private FinaRenTipoLocalComercial tipoLocalCalculado;
    private String numMemo;
    private String nombres;
    private String codigoLocal;
    private String asunto;
    private String para;
    private String descripcionMemo;
    private String numTramite;
    private String ubicacionLocal;
    private String director;
    private String fechaMemo;
    private String descrpcioFinal;
    private Long numMemorandum;
    private Long numMemorandActual;
    private SecuenciaMemo secuenciaMemo;
    private SecuenciaMemo secuenciaMemoNumDos;
    private String memoDescripcion;
    private String comisaria;
    private List<FinaRenLocalComercial> localesLista;
    private SecuenciaMemo mantenimientoSecuencia;
    private Integer anioMan;

    @PostConstruct
    public void init() {
          try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
        mantenimientoSecuencia = new SecuenciaMemo();
        anioMan = Utils.getAnio(new Date());
        localesLista = new ArrayList<>();
        cantidad = new RenLocalCantidadAccesorios();
        fotoLocalComercial = new RenLocalComercialFoto();
        tiposAccesorios = new ArrayList<>();
        tiposAccesorios = catalogoService.MostarTodoCatalogo("GT_TIPO_TASA_TURISMO");
        tipoNegocioList = new ArrayList<>();
        ubicacionList = new ArrayList<>();
        padreTuristicoList = services.findAllEasy("select f from FinaRenLocalCategoria f where f.padre=0 and f.estado=true");
        tipoNegocioList = catalogoService.MostarTodoCatalogo("GT_TIPO_NEGOCIO");
        ubicacionList = catalogoService.MostarTodoCatalogo("GT_FACTOR_UBICACION_LOCALES");
        idLocal = -1;
        tipoLocalCalculado = new FinaRenTipoLocalComercial();
        param = new HashMap<>();

        listAccesorios = new ArrayList<>();
        contribuyentes = new LazyModel<>(Cliente.class);
        contribuyentes.getFilterss().put("validado", true);
        contribuyentes.getFilterss().put("estado", true);
        actividades = new LazyModel<>(FinaRenActividadComercial.class);
        actividades.getFilterss().put("estado", Boolean.TRUE);
        predios = new LazyModel<>(CatPredio.class);
        predios.getFilterss().put("estado", "A");
        patentes = new LazyModel<>(FinaRenPatente.class);
        //patentes.addFilter("estado", Boolean.TRUE);
        patentes.getSorteds().put("id", "ASC");
        patentes.setDistinct(false);
        locales = new ArrayList<>();
        local = new FinaRenLocalComercial();
        actividad = new FinaRenActividadComercial();
        sourceActividades = services.findAllQuery("SELECT r FROM FinaRenActividadComercial r WHERE r.estado = true ORDER BY r.descripcion", null);
        targetActividades = new ArrayList<>();
        dualListActividades = new DualListModel<>(sourceActividades, targetActividades);
        actividadesSeleccionadas = new ArrayList<>();
        esEditable = Boolean.TRUE;
        initRegistro();
        initRegistroLocal();      
        localComercialFotoSeleccionada = new RenLocalComercialFoto();
        fotosLocalesComerciales = new ArrayList<>();
        ss.borrarDatos();
        
        JsfUtil.addInformationMessage("Información", "Registrar Patente y Local Comercial.");
        JsfUtil.addInformationMessage("Información", "Adjuntar Memo firmado del local por director de Justicia y vigilancia.");
         }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String clavePredial(Long predio) {
        CatPredio tmp = services.find(CatPredio.class, predio);
        if (tmp != null) {
            return tmp.getClaveCat();
        }
        return "";
    }

    public void openMemo(FinaRenLocalComercial lo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM", Locale.forLanguageTag("es"));

        fechaMemo = Utils.getDia(new Date()).toString() + " de " + simpleDateFormat.format(new Date()).toUpperCase() + ", Del " + Utils.getAnio(new Date());
        secuenciaMemo = new SecuenciaMemo();
        secuenciaMemo = secuenciaMemoService.secuenciaMemoUno(Utils.getAnio(new Date()), "DGJVVCACM");
        numMemorandum = secuenciaMemo.getNum();
        secuenciaMemoNumDos = new SecuenciaMemo();
        secuenciaMemoNumDos = secuenciaMemoService.secuenciaMemoUno(Utils.getAnio(new Date()), "CTM");
        numMemorandActual = secuenciaMemoNumDos.getNum();
        if (lo.getPatente() != null && lo.getPatente().getPropietario() != null) {
            nombres = lo.getPatente().getPropietario().getNombreCompleto();
        } else {
            nombres = "";
        }
        numTramite = tramite.getNumTramite() + "";
//        if(lo.getIdTramite() != null){
//            HistoricoTramites h = tramiteService.find(new HistoricoTramites(lo.getIdTramite()));
//            numTramite = h.getNumTramite() + " ";
//        }       
        numMemo = "Memorándum No.GADMCD-DGJVCACM-R-" + Utils.getAnio(new Date()).toString() + "-PFPN-" + secuenciaMemo.getNum() + "-M";
        codigoLocal = lo.getClavePreial() + "LC" + lo.getNumLocal();
        memoDescripcion = "No. GADMCD-CTM-" + Utils.getAnio(new Date()).toString() + " - M";
        para = "C.P.A. Diana Iler. Mgs / Jef@ de Rentas Municipales";
        asunto = "Solicitud de Orden de Pago de tasa de Habilitación y Patente";
        comisaria = "COMISARIA MUNICIPAL DE PERMISOS DE FUNCIONAMIENTO DEL GADMCD";
        director = "Ab. Alfredo Yuquilema D. Mgs";
        descrpcioFinal = "Adjunto: Documentación de soporte del Memorándum No. GADMCD-CTM-" + Utils.getAnio(new Date()).toString() + "-" + numMemorandActual + "-M";
        if (lo.getPredio() != null) {
            CatPredio pre = services.find(CatPredio.class, lo.getPredio());
            if (pre != null && pre.getId() != null) {
                ubicacionLocal = pre.getDireccion();
            } else {
                ubicacionLocal = "";
            }
        } else {
            ubicacionLocal = "";
        }
        JsfUtil.executeJS("PF('dlogMemo').show()");
        JsfUtil.update("frmMemo");
    }

    public void opneSecuencia() {
        mantenimientoSecuencia = new SecuenciaMemo();
        anioMan = Utils.getAnio(new Date());
        JsfUtil.executeJS("PF('dlogoManSecuen').show()");
        JsfUtil.update("frmManSecuencia");
    }

    public void executarLocales(FinaRenPatente patente) {
        localesLista = new ArrayList<>();
        localesLista = patente.getFinaRenLocalComercialList();
    }

    public void generarMemo() {
        if (secuenciaMemo.getId() != null) {
            secuenciaMemoService.edit(secuenciaMemo);
        } else {
            secuenciaMemoService.create(secuenciaMemo);
        }
        if (secuenciaMemoNumDos.getId() != null) {
            secuenciaMemoService.edit(secuenciaMemoNumDos);
        } else {
            secuenciaMemoService.create(secuenciaMemoNumDos);
        }
        descripcionMemo = "Con fecha, " + fechaMemo + " se recibió el Memorandum " + memoDescripcion + ", de fecha " + fechaMemo + ", suscrito por la  " + comisaria + " quien remitió"
                + " la documentación como cunplimiento a la solicitud de la orden de Pago de Tasa de Habilitación y Patente del:";
        viewReport.borrarParametros();
        viewReport.instanciarParametros();
        viewReport.addParametro("adjunto", descrpcioFinal);
        viewReport.addParametro("asunto", asunto);
        viewReport.addParametro("descripcion", descripcionMemo);
        viewReport.addParametro("director_justicia", director);
        viewReport.addParametro("fecha", fechaMemo);
        viewReport.addParametro("local", codigoLocal);
        viewReport.addParametro("num_memo", numMemo);
        viewReport.addParametro("num_tramite", numTramite);
        viewReport.addParametro("para", para);
        viewReport.addParametro("pertenciente", nombres);
        viewReport.addParametro("ubicacion", ubicacionLocal);
        viewReport.setNombreReporte("memo_uno");
        viewReport.setNombreSubCarpeta("GestionTributatia/justiciaVigilancia");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void actualizarPAgina() {
        ss.borrarDatos();
        cantidad = new RenLocalCantidadAccesorios();
        fotoLocalComercial = new RenLocalComercialFoto();
        tiposAccesorios = new ArrayList<>();
        tiposAccesorios = catalogoService.MostarTodoCatalogo("GT_TIPO_TASA_TURISMO");
        tipoNegocioList = new ArrayList<>();
        ubicacionList = new ArrayList<>();
        padreTuristicoList = services.findAllEasy("select f from FinaRenLocalCategoria f where f.padre=0 and f.estado=true");
        tipoNegocioList = catalogoService.MostarTodoCatalogo("GT_TIPO_NEGOCIO");
        ubicacionList = catalogoService.MostarTodoCatalogo("GT_FACTOR_UBICACION_LOCALES");
        idLocal = -1;
        param = new HashMap<>();
        listAccesorios = new ArrayList<>();
        contribuyentes = new LazyModel<>(Cliente.class);
        contribuyentes.getFilterss().put("validado", true);
        contribuyentes.getFilterss().put("estado", true);
        actividades = new LazyModel<>(FinaRenActividadComercial.class);
        actividades.getFilterss().put("estado", Boolean.TRUE);
        predios = new LazyModel<>(CatPredio.class);
        predios.getFilterss().put("estado", "A");
        patentes = new LazyModel<>(FinaRenPatente.class);
        //patentes.addFilter("estado", Boolean.TRUE);
        patentes.getSorteds().put("id", "ASC");
        patentes.setDistinct(false);
        locales = new ArrayList<>();
        local = new FinaRenLocalComercial();
        actividad = new FinaRenActividadComercial();
        sourceActividades = services.findAllQuery("SELECT r FROM FinaRenActividadComercial r WHERE r.estado = true ORDER BY r.descripcion", null);
        targetActividades = new ArrayList<>();
        dualListActividades = new DualListModel<>(sourceActividades, targetActividades);
        actividadesSeleccionadas = new ArrayList<>();
        esEditable = Boolean.TRUE;
        initRegistro();
        initRegistroLocal();
        ss.borrarDatos();
        localComercialFotoSeleccionada = new RenLocalComercialFoto();
        fotosLocalesComerciales = new ArrayList<>();
        ss.borrarDatos();
    }

    public void eliminarFoto(RenLocalComercialFoto localComercialFoto, Integer index) {
        if (localComercialFoto.getId() != null) {
            localComercialFoto.setSisEnabled(Boolean.FALSE);
            services.update(localComercialFoto);
        }
        fotosLocalesComerciales.remove(index.intValue());
    }

    public void loadValueFotoLocal(RenLocalComercialFoto renLocalComercialFoto, Integer index) {
        ss.borrarDatos();
        ss.borrarParametros();
        this.index = index;
        this.localComercialFotoSeleccionada = fotosLocalesComerciales.get(index);
        ss.addParametro("foto", localComercialFotoSeleccionada);      
        JsfUtil.executeJS("PF('dlgFoto').show()");
        JsfUtil.update("frmFotos");
    }

    /**
     * MANTENIMIENTO PATENTES
     */
    public void initRegistro() {
        patente = new FinaRenPatente();
        patente.setPropietario(new Cliente());
        patente.setActividadPrincipal(new FinaRenActividadComercial());
        registro = true;
        representanteLegal = new Cliente();
        locales = new ArrayList<>();
        local = new FinaRenLocalComercial(idLocal++);
    }

    public void guardarPatente() {
        try {
            Boolean exiteMatriz = Boolean.FALSE;
            patente.setActividadPrincipal(actividad);
            String msg = "Patente actualizada con exito.";
            if (registro) {
                patente.setUsuarioIngreso(uSession.getNameUser());
                mensaje = "Patente registrada con exito.";
            }
            for (FinaRenLocalComercial rlc : locales) {
                if (rlc.getMatriz()) {
                    exiteMatriz = Boolean.TRUE;
                    break;
                } else {
                    exiteMatriz = Boolean.FALSE;
                }
            }
            if (!exiteMatriz) {
                FinaRenLocalComercial rlc = locales.get(0);
                rlc.setMatriz(Boolean.TRUE);
                locales.set(0, rlc);
            }
            patente.setEstado(Boolean.TRUE);
            patente.setRenLocalComercialList(null);
//            patente.set
            patente = (FinaRenPatente) services.updateEntity(patente);
            try {              
                for (FinaRenLocalComercial rlc : locales) {
                    FinaRenLocalComercial comercial;
                    rlc.setPatente(patente);
                    comercial = (FinaRenLocalComercial) services.updateEntity(rlc);
                    if (rlc.getFotosLocalesComerciales() != null) {
                        for (RenLocalComercialFoto rlcf : rlc.getFotosLocalesComerciales()) {
                            rlcf.setLocalComercial(comercial);
                            services.updateEntity(rlcf);
                        }
                    }                   
                    if (rlc.getCantidadAccesoriosCollection() != null) {
                        for (RenLocalCantidadAccesorios accesorios : rlc.getCantidadAccesoriosCollection()) {
                            accesorios.setLocalComercial(comercial);
                            services.updateEntity(accesorios);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("guardarPatente" + e);
            }                  
            if (patente != null) {
                JsfUtil.addInformationMessage("Info", msg);
                JsfUtil.redirect(CONFIG.URL_APP + "rentas/patentes/actividadesLocales");
            } else {
                JsfUtil.addErrorMessage("Info", "Hubo un error al registrar la patente. Inténtelo nuevamente");
            }
            ss.borrarDatos();
            ss.borrarParametros();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void initEdit(FinaRenPatente p) {
        patente = p;
        actividad = p.getActividadPrincipal();
        locales = p.getRenLocalComercialList();
        registro = false;
    }

    public void cancel() {
        initRegistro();
    }

    public void eliminarPatente(FinaRenPatente pa) {
        pa.setEstado(Boolean.FALSE);
        services.update(pa);
        JsfUtil.addInformationMessage("", "Se inactivo la patente de " + pa.getPropietario().getNombreCompleto());
    }

    public void buscarRepresentanteLegal(BigInteger id) {
        if (id != null) {
            representanteLegal = services.find(Cliente.class, id.longValue());
        }
        if (representanteLegal == null) {
            representanteLegal = new Cliente();
        }
    }

//    /*
//     * MANTENIMIENTO ACTIVIDAD COMERCIAL
//     */
//    public void initRegistroActividad() {
//        actividad = new FinaRenActividadComercial();
//        registroActividad = true;
//    }
//
//    public void initEditActividad(FinaRenActividadComercial a) {
//        actividad = a;
//        registroActividad = false;
//    }

//    public void cancelActividad() {
//        initRegistroActividad();
//    }

    public void editarPatente(FinaRenPatente rlc, Boolean edita) {
        if (rlc != null) {
            patente = new FinaRenPatente();
            patente = rlc;
            actividad = patente.getActividadPrincipal();
            param = new HashMap<>();
            param.put("patente", patente);
            locales = services.findAll(FinaRenLocalComercial.class, param);
            esEditable = edita;
            JsfUtil.update("frmNew");
            JsfUtil.executeJS("PF('dlogoNewPatente').show()");
        }
    }

    public void viewPatente(FinaRenPatente rlc, Boolean edita) {
        if (rlc != null) {
            patente = new FinaRenPatente();
            patente = rlc;
            actividad = patente.getActividadPrincipal();
            param = new HashMap<>();
            param.put("patente", patente);
            locales = services.findAll(FinaRenLocalComercial.class, param);
            esEditable = edita;
            JsfUtil.update("frmView");
            JsfUtil.executeJS("PF('dlogoView').show()");
        }
    }

    /*
     * MANTENIMIENTO DE ESTABLECIMIENTOS COMERCIALES
     */
    public void agregarAccesorios(int index, RenLocalCantidadAccesorios acc) {
        if (index == 0) {
            cantidad.setAnio(Utils.getAnio(new Date()));
            cantidad.setEstado(Boolean.TRUE);
            cantidad.setFechaIngreso(new Date());
            cantidad.setUsuarioIngreso(uSession.getNameUser());
            cantidad.setLocalComercial(localComercial);
            listAccesorios.add(cantidad);
            JsfUtil.addInformationMessage("", "Accesorio Agregado con exitó");
        } else {
            listAccesorios.add(listAccesorios.indexOf(acc), acc);
        }
        cantidad = new RenLocalCantidadAccesorios();
    }

    public void eliminarAcesorios(int index) {
        RenLocalCantidadAccesorios data = listAccesorios.get(index);
        if (data.getId() != null) {
            data.setEstado(Boolean.FALSE);
            services.update(data);
        }
        listAccesorios.remove(index);
    }

    public void editAcc(int index) {
        cantidad = listAccesorios.get(index);
    }

    public void refreshTipoAccesorios() {
        tiposAccesorios = new ArrayList<>();
        tiposAccesorios = catalogoService.MostarTodoCatalogo("GT_TIPO_TASA_TURISMO");
    }

    public void initRegistroLocal() {
        localComercialFotoSeleccionada = new RenLocalComercialFoto();
        fotosLocalesComerciales = new ArrayList<>();
        listAccesorios = new ArrayList<>();
        local = new FinaRenLocalComercial(idLocal++);
        //     verifica si exite la matriz
        if (locales != null && !locales.isEmpty()) {
            locales.forEach((rlc) -> {
                if (rlc.getMatriz() != null) {
                    if (rlc.getMatriz()) {
                        local.setMatriz(Boolean.FALSE);
                    } else {
                        local.setMatriz(Boolean.TRUE);
                    }
                } else {
                    local.setMatriz(Boolean.TRUE);
                }
            });
        } else {
            local.setMatriz(Boolean.TRUE);
        }
        local.setEstadoLocalComercial(1L);
        actividadesSeleccionadas.clear();
        tipoNegoicoSeleccionados = new ArrayList<>();
        dualListActividades.setTarget(new ArrayList<>());
        dualListActividades.setSource(sourceActividades);
        predio = new CatPredio();
    }

    public void initEditarLocal(FinaRenLocalComercial l) {
        FinaRenTipoLocalComercial rtlc = null;
        sourceActividades = services.findAll(FinaRenActividadComercial.class, null);
        dualListActividades.setSource(sourceActividades);
        local = l;
//        param = new HashMap<>();
//        param.put("localComercial", local);
        tipoNegoicoSeleccionados = new ArrayList<>();
        if (local.getTipoNegocio().trim() != null && local.getTipoNegocio().trim() != "") {
            tipoNegoicoSeleccionados = services.listaNegocioSeleccionados(local.getTipoNegocio());
        }
        tipoLocalCalculado = new FinaRenTipoLocalComercial();
        tipoLocalCalculado = l.getTipoLocal();
    
        if (local.getNumPredio() != null) {
            param = new HashMap<>();
            param.put("id", local.getNumPredio());
            predio = services.findByParameter(CatPredio.class, param);

        }
        actividadesSeleccionadas = (List<FinaRenActividadComercial>) local.getRenActividadComercialCollection();
        actividadesSeleccionadas.stream().filter((act) -> (dualListActividades.getSource().contains(act))).forEachOrdered((act) -> {
            dualListActividades.getSource().remove(act);
        });
        dualListActividades.setTarget(actividadesSeleccionadas);
        loadAccesorios();
        loadFotosLocalesComerciales();
    }

    public void loadAccesorios() {
        if (local != null && local.getId() != null) {          
            param = new HashMap<>();
            param.put("localComercial", local);
            this.listAccesorios = services.findAllQuery("select e from RenLocalCantidadAccesorios e where e.localComercial = :localComercial", param);
        }       
    }

    public void loadFotosLocalesComerciales() {
        if (local != null && local.getId() != null) {          
            param = new HashMap<>();
            param.put("localComercial", local);
            this.fotosLocalesComerciales = services.findAllQuery("select e from RenLocalComercialFoto e where e.localComercial = :localComercial", param);
        }      
    }

    public void onTranferActividad(TransferEvent event) {
        if (dualListActividades.getTarget().size() > 6) {
            dualListActividades.getSource().add(dualListActividades.getTarget().get(dualListActividades.getTarget().size() - 1));
            dualListActividades.getTarget().remove(dualListActividades.getTarget().get(dualListActividades.getTarget().size() - 1));
            JsfUtil.addErrorMessage("Solo se Permiten un maximo de Seis Actividades", null);
        } else {
            actividadesSeleccionadas = (List<FinaRenActividadComercial>) event.getItems();
        }
    }

    public void cancelLocal() {
        initRegistroLocal();
    }

    public void calcularTipoLocal() {
        if (local.getAncho() != null && local.getAltura() != null) {
            local.setArea(local.getAncho().multiply(local.getAltura()).setScale(2, RoundingMode.HALF_UP));
            tipoLocalCalculado = new FinaRenTipoLocalComercial();
            if (local.getArea().setScale(2, RoundingMode.UP).doubleValue() <= BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                tipoLocalCalculado = services.find(FinaRenTipoLocalComercial.class, 3L);
            } else if (local.getArea().setScale(2, RoundingMode.UP).doubleValue() >= BigDecimal.valueOf(21.00).setScale(2, RoundingMode.HALF_UP).doubleValue()
                    && local.getArea().setScale(2, RoundingMode.UP).doubleValue() <= BigDecimal.valueOf(60.00).setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                tipoLocalCalculado = services.find(FinaRenTipoLocalComercial.class, 2L);
            } else if (local.getArea().setScale(2, RoundingMode.UP).doubleValue() > BigDecimal.valueOf(60.00).setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                tipoLocalCalculado = services.find(FinaRenTipoLocalComercial.class, 1L);
            }
        } else if (local.getArea() != null) {
            tipoLocalCalculado = new FinaRenTipoLocalComercial();
            if (local.getArea().setScale(2, RoundingMode.UP).doubleValue() <= BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                tipoLocalCalculado = services.find(FinaRenTipoLocalComercial.class, 3L);
            } else if (local.getArea().setScale(2, RoundingMode.UP).doubleValue() >= BigDecimal.valueOf(21.00).setScale(2, RoundingMode.HALF_UP).doubleValue()
                    && local.getArea().setScale(2, RoundingMode.UP).doubleValue() <= BigDecimal.valueOf(60.00).setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                tipoLocalCalculado = services.find(FinaRenTipoLocalComercial.class, 2L);
            } else if (local.getArea().setScale(2, RoundingMode.UP).doubleValue() > BigDecimal.valueOf(60.00).setScale(2, RoundingMode.HALF_UP).doubleValue()) {
                tipoLocalCalculado = services.find(FinaRenTipoLocalComercial.class, 1L);
            }
        }     
    }

    public void guardarEstablecimiento() {
        try {           
            if (local.getNumLocal() == null) {
                JsfUtil.addWarningMessage("", "El Numero del establecimiento deber ser ingresado");
                return;
            }
            if (local.getEstadoLocalComercial() == null) {
                JsfUtil.addWarningMessage("", "El Estado del establecimiento debe ser ingreado");
                return;
            }
            if (local.getAncho() == null) {
                JsfUtil.addWarningMessage("", "Debe ingresar el ancho del establecimiento");
                return;
            }
            if (local.getAltura() == null) {
                JsfUtil.addWarningMessage("", "Debe ingresar la altura del establecimiento");
                return;
            }
            propietario = patente.getPropietario();
            Boolean existeMatriz = Boolean.FALSE;
            local.setPropietario(propietario);
            local.setEstado(Boolean.TRUE);
            local.setFechaIngreso(new Date());
            local.setUsuarioIngreso(uSession.getNameUser());
            local.setTipoLocal(tipoLocalCalculado);
           
            if (predio != null && predio.getId() != null) {
                local.setNumPredio(predio.getId());               
            }
            local.setNombreLocal(local.getNombreLocal().toUpperCase());
            local.setActividadComercial(local.getActividadComercial().toUpperCase());
            local.setNumLocal(local.getNumLocal().toUpperCase());
            actividadesSeleccionadas = dualListActividades.getTarget();

            if (dualListActividades.getTarget().isEmpty()) {
                JsfUtil.addInformationMessage("Info", "Debe seleccionar al menos una actividad");
                return;
            }
            List<FinaRenActividadComercial> actividadComercials = new ArrayList();
            for (FinaRenActividadComercial rac : dualListActividades.getTarget()) {
                actividadComercials.add(rac);
            }
            local.setRenActividadComercialCollection(actividadComercials);
            local.setTipoNegocio(local.tipoNegociosAnidados(tipoNegoicoSeleccionados));
            local.setEstado(Boolean.TRUE);
            local.setPatente(patente);
            local.setIdTramite(tramite.getId());
            local.setCantidadAccesoriosCollection(new ArrayList<>());
            local.setCantidadAccesoriosCollection(listAccesorios);          
            if (!locales.contains(local)) {
                for (FinaRenLocalComercial rlc : locales) {
                    if (rlc.getMatriz()) {
                        if (local.getMatriz().equals(rlc.getMatriz())) {
                            JsfUtil.addErrorMessage("Info", "No pueden existir dos o mas locales Matrices");
                            return;
                        }
                    }
                }
                locales.add(local);
            } else {
                for (FinaRenLocalComercial rlc : locales) {
                    if (!rlc.equals(local)) {
                        if (rlc.getMatriz()) {
                            if (local.getMatriz().equals(rlc.getMatriz())) {
                                existeMatriz = Boolean.TRUE;
                                local.setMatriz(Boolean.FALSE);
                                JsfUtil.addErrorMessage("Info", "No pueden existir dos o mas locales Matrices");
                                return;
                            }
                        }
                    }
                }
                if (!existeMatriz) {
                    for (int i = 0; i < locales.size(); i++) {
                        if (locales.get(i).equals(local)) {
                            locales.set(i, local);
                            break;
                        }
                    }
                }
            }
            if (Utils.isNotEmpty(fotosLocalesComerciales)) {
                local.setFotosLocalesComerciales(new ArrayList());
                local.setFotosLocalesComerciales(fotosLocalesComerciales);
            }
            if (local != null) {
                JsfUtil.addInformationMessage("Info", "Local comercial creado correctamente");
                initRegistroLocal();                
            } else {
                JsfUtil.addErrorMessage("Info", "Hubo un error al crear el local comercial. Inténtelo nuevamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeLocal(int index) {
        FinaRenLocalComercial rlc = locales.get(index);
        if (rlc.getId() == null) {
            locales.remove(index);
            return;
        }
        if (rlc.getId() != null) {
            if (rlc.getMatriz()) {
                int count = 0;
                FinaRenLocalComercial temp = null;
                for (FinaRenLocalComercial r : locales) {
                    if (!r.getMatriz()) {
                        r.setMatriz(Boolean.TRUE);
                        services.update(r);
                        temp = r;
                        break;
                    }
                    count++;
                }
                rlc.setMatriz(Boolean.FALSE);
                locales.set(count, temp);
            }
            rlc.setEstado(Boolean.FALSE);
            services.update(rlc);
            locales.remove(index);
        } else {
            locales.remove(index);
        }
    }

    /*
     * SELECCION DE PROPIETARIOS
     */
    public void openDlgContribuyentes() {
        JsfUtil.executeJS("PF('dlgContribuyentes').show();");
    }

    public void seleccionarActividadPrincipal() {
        patente.setActividadPrincipal(actividad);
    }

    public void seleccionarContribuyente() {
        FinaRenPatente existePatenteParaContribuyente = (FinaRenPatente) services.find("SELECT a FROM FinaRenPatente a WHERE a.propietario.identificacion = :ciRuc AND a.estado = true", new String[]{"ciRuc"},
                new Object[]{propietario.getIdentificacion()});
        if (existePatenteParaContribuyente == null) {
            patente.setPropietario(propietario);
//            buscarRepresentanteLegal(BigInteger.valueOf(propietario.getRepresentanteLegal()));
            JsfUtil.executeJS("PF('dlgContribuyentes').hide();");
        } else {
            JsfUtil.addErrorMessage("Info", "Contribuyente ya Posee asignada Patentes");
        }
    }

    public String fullName(Cliente ente) {
        if (ente != null) {
            if (ente.getId() != null) {
                if (ente.getTipoProv().getCodigo() == "PER_NAT") {
                    return Utils.isEmpty(ente.getApellido()) + " " + Utils.isEmpty(ente.getNombre());
                } else {
                    return Utils.isEmpty(ente.getRazonSocial());
                }
            }
        }
        return "";
    }

    //   REDIRECT PAGE TO CREATE A NEW CLIENT
    public void nuevoCliente() {
        JsfUtil.redirectNewTab("/faces/generic/entefaces.xhtml");
    }

    public void nuevaPatente() {
        patente = new FinaRenPatente();
        JsfUtil.update("frmNew");
        JsfUtil.executeJS("PF('dlogoNewPatente').show()");
    }

    //NUEVAS PATENTES WIZARD
    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("actvidadEconomica")) {
            if (patente.getPropietario() == null || patente.getPropietario().getId() == null) {
                JsfUtil.addErrorMessage("Info", "Debe Seleccionar un Propietario Para Continuar");
                return event.getOldStep();
            } else {
                JsfUtil.update("frmMain");
            }
        }
        if (event.getNewStep().equals("establecimientos")) {
            if (actividad == null) {
                JsfUtil.addErrorMessage("Info", "Debe Seleccionar una Actividad");
                return event.getOldStep();
            } else {
                if (patente == null) {
                    seleccionarActividadPrincipal();
                    initRegistroLocal();
                } else {
                    local = new FinaRenLocalComercial();
                }
                JsfUtil.update("frmMain");
            }
        }
        JsfUtil.update("frmMain");
        return event.getNewStep();
    }

    public void ajaxTipoActividadTuristica() {
        if (padreTuristico != null) {
            param = new HashMap<>();
            param.put("padre", padreTuristico.getId());
            listaTuristicasActividad = services.findAll(FinaRenLocalCategoria.class, param);
        }
    }

    //SELECCION DE PREDIOS
    public void openDlgPredios() {
        //     consumir web services los predios
        predioListLazy = new LazyModel<>(CatPredio.class);
        predioListLazy.getFilterss().put("estado", "A");
        JsfUtil.executeJS("PF('dlgPredios').show();");
    }

    public void predioSeleccionado() {
        local.setNumPredio(this.predio.getId());
        JsfUtil.addInformationMessage("El predio seleccionado es: " + predio.getNumPredio(), "");
        JsfUtil.executeJS("PF('dlgPredios').hide();");
    }

    public void save() {
        if (actividad.getDescripcion() == null || actividad.getDescripcion().equals("")) {
            JsfUtil.addErrorMessage("Advertencia", "Debe ingresar la descripcion");
            return;
        }
        try {
            actividad.setDescripcion(actividad.getDescripcion().toUpperCase());
            actividad.setEstado(Boolean.TRUE);
            actividad = (FinaRenActividadComercial) services.save(actividad);
            if (actividad != null) {
                actividadesSeleccionadas.add(actividad);
                agregarActvs();
                JsfUtil.addInformationMessage("Informacion", "Actividad Agregada Correctamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveComercial() {
        if (actividad.getDescripcion() == null || actividad.getDescripcion().equals("")) {
            JsfUtil.addErrorMessage("Advertencia", "Debe ingresar la descripcion");
            return;
        }
        try {
            actividad.setDescripcion(actividad.getDescripcion().toUpperCase());
            actividad.setEstado(Boolean.TRUE);
            actividad = (FinaRenActividadComercial) services.save(actividad);
            if (actividad != null) {
                actividadesSeleccionadas.add(actividad);
                JsfUtil.addInformationMessage("Informacion", "Actividad Agregada Correctamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void eliminarActividadDelLocal(FinaRenActividadComercial actv) {
//        localComercial.getRenActividadComercialCollection().remove(actv);
//    }

    public void actualizarLocal() {
        if (observaciones == null && observaciones.length() == 0) {
            JsfUtil.addErrorMessage("Advertencia", "Debe ingresar las Observaciones");
            return;
        }
        try {
            if (localComercial.getId() != null) {
                ObservacionesLocal ol = new ObservacionesLocal(localComercial, observaciones, uSession.getNameUser(), "Edicion de estado del Local");
                if (services.save(ol) != null) {
                    if (services.update(localComercial)) {
                        JsfUtil.addInformationMessage("Informacion", "Local Actualizado Correctamente");
                        inicarDatos();
                    } else {
                        JsfUtil.addErrorMessage("Advertencia", "Ocurrio un error al actualizar Local");
                    }
                } else {
                    JsfUtil.addErrorMessage("Advertencia", "Ocurrio un error al actualizar Local");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicarDatos() {
        localComercial = null;
        observaciones = null;
    }

    public void eliminarCantidadNew(RenLocalCantidadAccesorios acc) {
        localComercial.getCantidadAccesoriosCollection().remove(acc);
    }

    public void eliminarCantidadEdit(RenLocalCantidadAccesorios acc) {
        if (acc.getId() != null) {
            acc.setEstado(Boolean.FALSE);
            services.update(acc);
        }
        localComercial.getCantidadAccesoriosCollection().remove(acc);
    }

    public void enteSeleccionado(Cliente ente) {
        //  this. = ente;
        if ("PER_NAT".equals(ente.getTipoProv().getCodigo())) {
            JsfUtil.addInformationMessage("Info", "El ente propietario es: " + ente.getNombre() + " " + ente.getApellido());
        } else {
            JsfUtil.addInformationMessage("Info", "El ente propietario es: " + ente.getRazonSocial());
        }
    }

    public void razonSocialSeleccionado(Cliente ente) {
        try {
            this.localComercial.setRazonSocial(ente.getId());
            JsfUtil.addInformationMessage("Info", "La Razón Social es: " + ente.getNombreCompleto());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "razonSocialSeleccionado", e);
        }
    }

    public void nuevoLocalComercial() {
        cantidad = new RenLocalCantidadAccesorios();
        cantidad.setAnio(Utils.getAnio(new Date()));
        cantidad.setEstado(Boolean.TRUE);
        cantidad.setFechaIngreso(new Date());
        cantidad.setUsuarioIngreso(uSession.getNameUser());
    }

    public void seleccionarUbicacion(FinaRenLocalUbicacion ubicacion) {
        this.localComercial.setUbicacion(ubicacion);
        JsfUtil.addInformationMessage("Info", "Ubicacion seleccionada: " + ubicacion.getDescripcion());
    }

    public void guardarLocalEdit() {
        Boolean validar = false;
        try {
            if (predio != null) {
                localComercial.setNumPredio(predio.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarActvs() {
        try {
            if (this.localComercial.getRenActividadComercialCollection() == null || this.localComercial.getRenActividadComercialCollection().isEmpty()) {
                localComercial.setRenActividadComercialCollection(actividadesSeleccionadas);
                for (FinaRenActividadComercial temp : actividadesSeleccionadas) {
                }
            } else {
                for (FinaRenActividadComercial temp : actividadesSeleccionadas) {
                    if (!localComercial.getRenActividadComercialCollection().contains(temp)) {
                        localComercial.getRenActividadComercialCollection().add(temp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accionLocal(FinaRenLocalComercial localComercial, Integer x) {
        this.localComercial = localComercial;
        switch (x) {
            case 2:
                mensaje = "Observaciones para inhabilitar Local";
                this.localComercial.setEstadoLocalComercial(2L);
                this.localComercial.setEstado(false);
                break;
            default:
                mensaje = "Observaciones para habilitar Local";
                this.localComercial.setEstadoLocalComercial(1L);
                this.localComercial.setEstado(true);
                break;
        }
        JsfUtil.executeJS("PF('obs').show()");
        JsfUtil.update("frmObsCor");
    }

    public void imprimirCertificado(FinaRenLocalComercial local) {
//        try {
//            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.instanciarParametros();
//            FinaCertificadoExoneracionLocalActivos certificado;
//            ss.addParametro("logo1", path + SisVars.sisLogo);
//            ss.addParametro("logo2", path + SisVars.sisLogo);
//
//            if ((certificado = (CertificadoExoneracionLocalActivos) services.find(Querys.getCertExoLocalActByAnioAndLocal, new String[]{"anio", "local"}, new Object[]{Utils.getAnio(new Date()), local})) == null) {
//
//                certificado = new CertificadoExoneracionLocalActivos();
//                certificado.setFechaCreacion(new Date());
//                certificado.setUserCreador((Usuarios) services.find(Querys.getAclUserByUser, new String[]{"user"}, new Object[]{uSession.getNameUser()}));
//                certificado.setLocalComercial(local);
//                if (local.getPropietario() != null) {
//                    certificado.setRazonSocial(local.getPropietario());
//                } else {
//                    certificado.setRazonSocial(local.getRazonSocial());
//                }
//
//                certificado.setEstado(Boolean.TRUE);
//                certificado.setAnio(Utils.getAnio(new Date()));
//
//                if ((certificado = (CertificadoExoneracionLocalActivos) services.persist(certificado)) != null) {
//                    ss.addParametro("id_certificado", certificado.getId());
//                    ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/") + "/");
//                    //       ss.setTieneDatasource(Boolean.FALSE);
//                    ss.setNombreReporte("certificado_exoneracion_activos_local_comercial");
//                    ss.setNombreSubCarpeta("permisoFuncionamiento");
//                    JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
//                }
//            } else {
//                ss.addParametro("id_certificado", certificado.getId());
//                ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/") + "/");
//                //ss.setTieneDatasource(Boolean.TRUE);
//                ss.setNombreReporte("certificado_exoneracion_activos_local_comercial");
//                ss.addParametro("ciRuc", local.getPropietario().getIdentificacion());
//                ss.setNombreSubCarpeta("permisoFuncionamiento");
//                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
//                JsfUtil.addInformationMessage("Info", "El certificado fue generado anteriormente");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void eliminarActividad(FinaRenActividadComercial act) {
        try {
            act.setEstado(Boolean.FALSE);
            services.update(act);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarActividad() {
        try {
            if (actividad.getDescripcion() == null) {
                JsfUtil.addErrorMessage("Advertencia", "Debe ingresar la descripcion");
                return;
            }
            services.update(actividad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verLiquidaciones(List<FinaRenLocalComercial> local) {
        ss.instanciarParametros();
        ss.addParametro("localComercial", local);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "rentas/liquidaciones/locales");
    }

//    public void guardarActividadComercial() {
//        if (actividad.getDescripcion() == null || actividad.getDescripcion().equals("")) {
//            JsfUtil.addErrorMessage("Advertencia", "Debe ingresar la descripcion");
//            return;
//        }
//        try {
//            actividad.setDescripcion(actividad.getDescripcion().toUpperCase());
//            actividad.setEstado(Boolean.TRUE);
//            if (actividad.getId() == null) {
//                actividad = (FinaRenActividadComercial) services.save(actividad);
//            } else {
//                services.update(actividad);
//            }
//            if (actividad != null) {
//                actividadesSeleccionadas.add(actividad);
//                JsfUtil.addInformationMessage("Informacion", "Actividad Agregada Correctamente");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void subirIamgenes(FileUploadEvent event) {
        try {
            String ruta = SisVars.RUTA_GESTION_TRIBUTARIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
           
            fotoLocalComercial = new RenLocalComercialFoto();
            fotoLocalComercial.setNombre(event.getFile().getFileName());
            fotoLocalComercial.setContentType(event.getFile().getContentType());

            fotoLocalComercial.setRuta(ruta);

            fotoLocalComercial.setSisEnabled(Boolean.TRUE);
            if (localComercial != null && localComercial.getNombreLocal() != null) {
                fotoLocalComercial.setDescripcion(this.localComercial.getNombreLocal() + ":" + fotoLocalComercial.getNombre());
            }
            fotoLocalComercial.setUsuario(uSession.getNameUser());
            if (localComercial != null && localComercial.getId() != null) {
                fotoLocalComercial.setLocalComercial(local);
            }
            fotoLocalComercial.setInputStream(event.getFile().getInputstream());

            Utils.copyFileServerOne(fotoLocalComercial);
            fotosLocalesComerciales.add(fotoLocalComercial);
            fotoLocalComercial = new RenLocalComercialFoto();
            JsfUtil.addInformationMessage("Nota1", "Foto guardada satisfactoriamente");
        } catch (Exception e) {
            Logger.getLogger(FotosLocalComercial.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<FinaRenTipoLocalComercial> getTipoLocal() {
        Map m = new HashMap<>();
        m.put("estado", true);
        return services.findAllQuery("select c from FinaRenTipoLocalComercial c where c.estado=:estado order by c.descripcion asc ", m);
    }
    
    
    public void openDialogUpload() {
        PrimeFaces.current().executeScript("PF('dlgoUpload').show()");
        PrimeFaces.current().ajax().update("formUpload");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        uploadDoc.upload(tramite, file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        PrimeFaces.current().executeScript("PF('dlgoUpload').hide()");
    }
    public void opendlg() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            String usuario = clienteService.getrolsUser(RolUsuario.jefeRentas);
            getParamts().put("usuarioRentas", usuario.equals("") ? "admin_1" : usuario);
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
   
    public String getDescrpcioFinal() {
        return descrpcioFinal;
    }

    public void setDescrpcioFinal(String descrpcioFinal) {
        this.descrpcioFinal = descrpcioFinal;
    }

    public SecuenciaMemo getSecuenciaMemo() {
        return secuenciaMemo;
    }

    public void setSecuenciaMemo(SecuenciaMemo secuenciaMemo) {
        this.secuenciaMemo = secuenciaMemo;
    }

    public SecuenciaMemo getSecuenciaMemoNumDos() {
        return secuenciaMemoNumDos;
    }

    public void setSecuenciaMemoNumDos(SecuenciaMemo secuenciaMemoNumDos) {
        this.secuenciaMemoNumDos = secuenciaMemoNumDos;
    }

    public String getMemoDescripcion() {
        return memoDescripcion;
    }

    public void setMemoDescripcion(String memoDescripcion) {
        this.memoDescripcion = memoDescripcion;
    }

    public String getComisaria() {
        return comisaria;
    }

    public void setComisaria(String comisaria) {
        this.comisaria = comisaria;
    }

    public List<FinaRenLocalComercial> getLocalesLista() {
        return localesLista;
    }

    public void setLocalesLista(List<FinaRenLocalComercial> localesLista) {
        this.localesLista = localesLista;
    }
   
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<CatalogoItem> getTiposAccesoriosSave() {
        return tiposAccesoriosSave;
    }

    public void setTiposAccesoriosSave(List<CatalogoItem> tiposAccesoriosSave) {
        this.tiposAccesoriosSave = tiposAccesoriosSave;
    }

    public RenLocalComercialFoto getFotoLocalComercial() {
        return fotoLocalComercial;
    }

    public void setFotoLocalComercial(RenLocalComercialFoto fotoLocalComercial) {
        this.fotoLocalComercial = fotoLocalComercial;
    }

    public String getNumMemo() {
        return numMemo;
    }

    public void setNumMemo(String numMemo) {
        this.numMemo = numMemo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(String codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getDescripcionMemo() {
        return descripcionMemo;
    }

    public void setDescripcionMemo(String descripcionMemo) {
        this.descripcionMemo = descripcionMemo;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getUbicacionLocal() {
        return ubicacionLocal;
    }

    public void setUbicacionLocal(String ubicacionLocal) {
        this.ubicacionLocal = ubicacionLocal;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getFechaMemo() {
        return fechaMemo;
    }

    public void setFechaMemo(String fechaMemo) {
        this.fechaMemo = fechaMemo;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public Cliente getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Cliente representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public LazyModel<Cliente> getContribuyentes() {
        return contribuyentes;
    }

    public void setContribuyentes(LazyModel<Cliente> contribuyentes) {
        this.contribuyentes = contribuyentes;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public LazyModel<CatPredio> getPredios() {
        return predios;
    }

    public void setPredios(LazyModel<CatPredio> predios) {
        this.predios = predios;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LazyModel<FinaRenActividadComercial> getActividades() {
        return actividades;
    }

    public void setActividades(LazyModel<FinaRenActividadComercial> actividades) {
        this.actividades = actividades;
    }

    public FinaRenActividadComercial getActividad() {
        return actividad;
    }

    public void setActividad(FinaRenActividadComercial actividad) {
        this.actividad = actividad;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public List<FinaRenLocalComercial> getLocales() {
        return locales;
    }

    public void setLocales(List<FinaRenLocalComercial> locales) {
        this.locales = locales;
    }

    public List<FinaRenActividadComercial> getActividadesSeleccionadas() {
        return actividadesSeleccionadas;
    }

    public void setActividadesSeleccionadas(List<FinaRenActividadComercial> actividadesSeleccionadas) {
        this.actividadesSeleccionadas = actividadesSeleccionadas;
    }

    public boolean isRegistro() {
        return registro;
    }

    public void setRegistro(boolean registro) {
        this.registro = registro;
    }

    public boolean isRegistroActividad() {
        return registroActividad;
    }

    public void setRegistroActividad(boolean registroActividad) {
        this.registroActividad = registroActividad;
    }

    public boolean isRegistrarLocal() {
        return registrarLocal;
    }

    public void setRegistrarLocal(boolean registrarLocal) {
        this.registrarLocal = registrarLocal;
    }

//    public DualListModel<FinaRenActividadComercial> getDualListActividades() {
//        return dualListActividades;
//    }
//
//    public void setDualListActividades(DualListModel<FinaRenActividadComercial> dualListActividades) {
//        this.dualListActividades = dualListActividades;
//    }

//    public List<FinaRenActividadComercial> getTargetActividades() {
//        return targetActividades;
//    }
//
//    public void setTargetActividades(List<FinaRenActividadComercial> targetActividades) {
//        this.targetActividades = targetActividades;
//    }

    public List<FinaRenActividadComercial> getSourceActividades() {
        return sourceActividades;
    }

    public void setSourceActividades(List<FinaRenActividadComercial> sourceActividades) {
        this.sourceActividades = sourceActividades;
    }

    public long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(long idLocal) {
        this.idLocal = idLocal;
    }

    public FinaRenLocalComercial getLocal() {
        return local;
    }

    public void setLocal(FinaRenLocalComercial local) {
        this.local = local;
    }

    public LazyModel<CatPredio> getPredioListLazy() {
        return predioListLazy;
    }

    public void setPredioListLazy(LazyModel<CatPredio> predioListLazy) {
        this.predioListLazy = predioListLazy;
    }

    public Long getIdPatente() {
        return idPatente;
    }

    public void setIdPatente(Long idPatente) {
        this.idPatente = idPatente;
    }

    public Boolean getEsEditable() {
        return esEditable;
    }

    public void setEsEditable(Boolean esEditable) {
        this.esEditable = esEditable;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public List<RenLocalComercialFoto> getFotosLocalesComerciales() {
        return fotosLocalesComerciales;
    }

    public void setFotosLocalesComerciales(List<RenLocalComercialFoto> fotosLocalesComerciales) {
        this.fotosLocalesComerciales = fotosLocalesComerciales;
    }

    public RenLocalComercialFoto getLocalComercialFotoSeleccionada() {
        return localComercialFotoSeleccionada;
    }

    public void setLocalComercialFotoSeleccionada(RenLocalComercialFoto localComercialFotoSeleccionada) {
        this.localComercialFotoSeleccionada = localComercialFotoSeleccionada;
    }

    public List<CatalogoItem> getTipoNegocioList() {
        return tipoNegocioList;
    }

    public void setTipoNegocioList(List<CatalogoItem> tipoNegocioList) {
        this.tipoNegocioList = tipoNegocioList;
    }

    public List<CatalogoItem> getUbicacionList() {
        return ubicacionList;
    }

    public void setUbicacionList(List<CatalogoItem> ubicacionList) {
        this.ubicacionList = ubicacionList;
    }

    public FinaRenLocalCategoria getPadreTuristico() {
        return padreTuristico;
    }

    public void setPadreTuristico(FinaRenLocalCategoria padreTuristico) {
        this.padreTuristico = padreTuristico;
    }

    public List<FinaRenLocalCategoria> getPadreTuristicoList() {
        return padreTuristicoList;
    }

    public void setPadreTuristicoList(List<FinaRenLocalCategoria> padreTuristicoList) {
        this.padreTuristicoList = padreTuristicoList;
    }

    public List<FinaRenLocalCategoria> getListaTuristicasActividad() {
        return listaTuristicasActividad;
    }

    public void setListaTuristicasActividad(List<FinaRenLocalCategoria> listaTuristicasActividad) {
        this.listaTuristicasActividad = listaTuristicasActividad;
    }

    public List<CatalogoItem> getTipoNegoicoSeleccionados() {
        return tipoNegoicoSeleccionados;
    }

    public void setTipoNegoicoSeleccionados(List<CatalogoItem> tipoNegoicoSeleccionados) {
        this.tipoNegoicoSeleccionados = tipoNegoicoSeleccionados;
    }

    public List<RenLocalCantidadAccesorios> getListAccesorios() {
        return listAccesorios;
    }

    public void setListAccesorios(List<RenLocalCantidadAccesorios> listAccesorios) {
        this.listAccesorios = listAccesorios;
    }

    public RenLocalCantidadAccesorios getCantidad() {
        return cantidad;
    }

    public void setCantidad(RenLocalCantidadAccesorios cantidad) {
        this.cantidad = cantidad;
    }

    public FinaRenTipoLocalComercial getTipoLocalCalculado() {
        return tipoLocalCalculado;
    }

    public void setTipoLocalCalculado(FinaRenTipoLocalComercial tipoLocalCalculado) {
        this.tipoLocalCalculado = tipoLocalCalculado;
    }

    public Long getNumMemorandum() {
        return numMemorandum;
    }

    public void setNumMemorandum(Long numMemorandum) {
        this.numMemorandum = numMemorandum;
    }

    public Long getNumMemorandActual() {
        return numMemorandActual;
    }

    public void setNumMemorandActual(Long numMemorandActual) {
        this.numMemorandActual = numMemorandActual;
    }

    public SecuenciaMemo getMantenimientoSecuencia() {
        return mantenimientoSecuencia;
    }

    public void setMantenimientoSecuencia(SecuenciaMemo mantenimientoSecuencia) {
        this.mantenimientoSecuencia = mantenimientoSecuencia;
    }

    public Integer getAnioMan() {
        return anioMan;
    }

    public void setAnioMan(Integer anioMan) {
        this.anioMan = anioMan;
    }

    public List<CatalogoItem> getTiposAccesorios() {
        return tiposAccesorios;
    }

    public void setTiposAccesorios(List<CatalogoItem> tiposAccesorios) {
        this.tiposAccesorios = tiposAccesorios;
    }

    public DualListModel<FinaRenActividadComercial> getDualListActividades() {
        return dualListActividades;
    }

    public void setDualListActividades(DualListModel<FinaRenActividadComercial> dualListActividades) {
        this.dualListActividades = dualListActividades;
    }    
}
