/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.*;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.ObservacionesLocal;
import com.gestionTributaria.Entities.RenLocalCantidadAccesorios;
import com.gestionTributaria.Entities.RenLocalComercialFoto;
import com.gestionTributaria.Entities.RenLocalComercialHorario;
import com.gestionTributaria.Entities.RenLocalTipoAccesorio;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.*;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@Named(value = "turismoLocalComercial")
@ViewScoped
public class TurismoLocalesComerciales extends FotosLocalComercial implements Serializable {

    private static final Logger LOG = Logger.getLogger(TurismoLocalesComerciales.class.getName());

    private static final Long serialVersionUID = 1L;

    @Inject
    private UserSession uSession;

    @Inject
    private ManagerService services;

    @Inject
    private ServletSession ss;

    private Cliente propietario;
    private Cliente representanteLegal;
    private LazyModel<Cliente> contribuyentes;
    private CatPredioModel predio;
    private LazyModel<CatPredioModel> predios;

    private FinaRenPatente patente;
    private LazyModel<FinaRenPatente> patentes;

    private String mensaje;
    private String observaciones;

    private LazyModel<FinaRenActividadComercial> actividades;
    private FinaRenActividadComercial actividad;

    private FinaRenLocalComercial localComercial;
    private List<FinaRenLocalComercial> locales;

    private List<FinaRenActividadComercial> actividadesSeleccionadas;
    private List<RenLocalTipoAccesorio> tipos;
    private RenLocalCantidadAccesorios cantidad;

    private FinaRenLocalComercial local;

    private boolean registro = true;
    private boolean registroActividad = true;
    private boolean registrarLocal = true;

    private DualListModel<FinaRenActividadComercial> dualListActividades;
    private List<FinaRenActividadComercial> targetActividades, sourceActividades;
    private long idLocal;

    private LazyModel<CatPredioModel> predioListLazy;

    private Long idPatente;
    private Boolean esEditable;

    @PostConstruct
    public void init() {
        idLocal = -1;
        try {
            if (uSession.esLogueado()) {
                if (ss.getParametros() == null) {

                    actividades = new LazyModel<>(FinaRenActividadComercial.class);
                    // predios = new CatPredioLazy("A");
                    patentes = new LazyModel<>(FinaRenPatente.class);
                    locales = new ArrayList<>();
                    local = new FinaRenLocalComercial();
                    actividad = new FinaRenActividadComercial();
                    sourceActividades = services.findAllEasy("SELECT r FROM FinaRenActividadComercial r WHERE r.estado = true ORDER BY r.descripcion");
                    targetActividades = new ArrayList<>();
                    dualListActividades = new DualListModel<>(sourceActividades, targetActividades);
                    actividadesSeleccionadas = new ArrayList<>();
                    esEditable = Boolean.TRUE;
                    initRegistro();
                    initRegistroLocal();
                } else {
                    if ((ss.getParametros().get("idPatente") != null)) {
                        idPatente = Long.valueOf(ss.getParametros().get("idPatente").toString());
                        esEditable = Boolean.valueOf(ss.getParametros().get("ver").toString());
                        patente = services.find(FinaRenPatente.class, idPatente);
                        actividad = patente.getActividadPrincipal();
                        actividades = new LazyModel<>(FinaRenActividadComercial.class);
                        locales = patente.getRenLocalComercialList();
                        sourceActividades = services.findAllEasy("SELECT r FROM FinaRenActividadComercial r WHERE r.estado = true ORDER BY r.descripcion");
                        targetActividades = new ArrayList<>();
                        dualListActividades = new DualListModel<>(sourceActividades, targetActividades);
                        actividadesSeleccionadas = new ArrayList<>();
                        registro = false;
                        cantidad = new RenLocalCantidadAccesorios();
                        cantidad.setAnio(Utils.getAnio(new Date()));
                        cantidad.setEstado(Boolean.TRUE);
                        cantidad.setFechaIngreso(new Date());
                        cantidad.setUsuarioIngreso(uSession.getNameUser());
                        tipos = (List) services.findAllEasy("SELECT r FROM RenLocalTipoAccesorio r WHERE r.estado = true");

                    }
                }
                ss.borrarDatos();
            }
        } catch (Exception e) {
        }
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
        tipos = (List) services.findAllEasy("SELECT r FROM RenLocalTipoAccesorio r WHERE r.estado = true");
        cantidad = new RenLocalCantidadAccesorios();
        cantidad.setAnio(Utils.getAnio(new Date()));
        cantidad.setEstado(Boolean.TRUE);
        cantidad.setFechaIngreso(new Date());
        cantidad.setUsuarioIngreso(uSession.getNameUser());

        if (local.getLocalComercialHorarios() == null) {
            local.setLocalComercialHorarios(new RenLocalComercialHorario());
            local.getLocalComercialHorarios().setRenLocalComercial(local);
        }
    }

    public void guardarPatente() {
        try {
            Boolean exiteMatriz = Boolean.FALSE;
            patente.setActividadPrincipal(actividad);
            String msg = "Local Comercial actualizada con exito.";
            if (registro) {
                patente.setUsuarioIngreso(uSession.getNameUser());
                mensaje = "Local Comercial registrada con exito.";
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
            patente = (FinaRenPatente) services.save(patente);
            try {
                FinaRenLocalComercial l;
                for (FinaRenLocalComercial rlc : locales) {
                    rlc.setPropietario(this.patente.getPropietario());
                    rlc.setPatente(patente);
                    l = (FinaRenLocalComercial) services.save(rlc);
                    if (l != null && l.getId() != null) {
                        for (RenLocalCantidadAccesorios rlca : rlc.getCantidadAccesoriosCollection()) {
                            if (rlca.getId() == null) {
                                rlca.setLocalComercial(l);
                                services.save(rlca);
                            }
                        }
                        for (RenLocalComercialFoto rlcf : rlc.getFotosLocalesComerciales()) {
                            rlcf.setLocalComercial(l);
                            services.save(rlcf);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("guardarPatente" + e);
            }

            if (patente != null) {
                JsfUtil.addInformationMessage("Info", msg);
                JsfUtil.redirect("/faces/vistaprocesos/turismo/localesComerciales.xhtml");
            } else {
                JsfUtil.addErrorMessage("Info", "Hubo un error al registrar la patente. Inténtelo nuevamente");
            }
        } catch (Exception e) {
            System.out.println("guardarPatente " + e.toString());
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
    }

    public void buscarRepresentanteLegal(BigInteger id) {
        if (id != null) {
            representanteLegal = services.find(Cliente.class, id.longValue());
        }
        if (representanteLegal == null) {
            representanteLegal = new Cliente();
        }
    }

    /*
     * MANTENIMIENTO ACTIVIDAD COMERCIAL
     */
    public void initRegistroActividad() {
        actividad = new FinaRenActividadComercial();
        registroActividad = true;
    }

    public void initEditActividad(FinaRenActividadComercial a) {
        actividad = a;
        registroActividad = false;
    }

    public void cancelActividad() {
        initRegistroActividad();
    }

    public void editarPatente(FinaRenPatente rlc, Boolean edita) {
        if (rlc != null) {
            ss.borrarDatos();
            ss.borrarParametros();
            ss.instanciarParametros();
            ss.addParametro("idPatente", rlc.getId());
            ss.addParametro("ver", edita);
            JsfUtil.redirect(CONFIG.URL_APP + "moduloGestionTributario/Turismo/nuevoLocalComercial.xhtml");
        }
    }

    /*
     * MANTENIMIENTO DE ESTABLECIMIENTOS COMERCIALES
     */
    public void initRegistroLocal() {
        localComercialFotoSeleccionada = new RenLocalComercialFoto();
        fotosLocalesComerciales = new ArrayList<>();
        local = new FinaRenLocalComercial(idLocal++);
        //verifica si exite la matriz
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
        dualListActividades.setTarget(new ArrayList<>());
        dualListActividades.setSource(sourceActividades);
        predio = new CatPredioModel();

        if (local.getLocalComercialHorarios() == null) {
            local.setLocalComercialHorarios(new RenLocalComercialHorario());
            local.getLocalComercialHorarios().setRenLocalComercial(local);
        }
    }

    public void initEditarLocal(FinaRenLocalComercial l) {
        FinaRenTipoLocalComercial rtlc = null;
        sourceActividades = services.findAll(FinaRenActividadComercial.class, null);
        dualListActividades.setSource(sourceActividades);
        local = l;
        rtlc = services.find(FinaRenTipoLocalComercial.class, local.getTipoLocal().getId());
        local.setTipoLocal(rtlc);
        if (local.getNumPredio() != null) {
          //  predio = (CatPredioModel) services.find(CatPredio.class, local.getNumPredio());
        }
        if (local.getLocalComercialHorarios() == null) {
            local.setLocalComercialHorarios(new RenLocalComercialHorario());
            local.getLocalComercialHorarios().setRenLocalComercial(local);
        }
        actividadesSeleccionadas = (List<FinaRenActividadComercial>) local.getRenActividadComercialCollection();
        actividadesSeleccionadas.stream().filter((act) -> (dualListActividades.getSource().contains(act))).forEachOrdered((act) -> {
            dualListActividades.getSource().remove(act);
        });
        dualListActividades.setTarget(actividadesSeleccionadas);
        if (local.getCategoria() != null) {
            FinaRenLocalCategoria cat = services.find(FinaRenLocalCategoria.class, local.getCategoria().getId());
            local.setCategoria(cat);
            idCategoria = local.getCategoria().getPadre().longValue();
            this.getCategoriasHijas();
        }
        loadFotosLocalesComerciales(l.getId());
        cantidad = new RenLocalCantidadAccesorios();
        cantidad.setAnio(Utils.getAnio(new Date()));
        cantidad.setEstado(Boolean.TRUE);
        cantidad.setFechaIngreso(new Date());
        cantidad.setUsuarioIngreso(uSession.getNameUser());
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

    public void guardarEstablecimiento() {
        try {
            Boolean existeMatriz = Boolean.FALSE;
            local.setPropietario(propietario);
            local.setEstado(Boolean.TRUE);
            local.setFechaIngreso(new Date());
            local.setUsuarioIngreso(uSession.getNameUser());
            if (predio != null && predio.getNumPredio() != null) {
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
            local.setPatente(patente);
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
        if (rlc.getId() > 1L) {
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
        contribuyentes = new LazyModel<>(Cliente.class);
        contribuyentes.getFilterss().put("estado", true);
        JsfUtil.executeJS("PF('dlgContribuyentes').show();");
    }

    public void seleccionarActividadPrincipal() {
        patente.setActividadPrincipal(actividad);
    }

    public void seleccionarContribuyente() {
        FinaRenPatente existePatenteParaContribuyente = (FinaRenPatente) services.find("SELECT a FROM FinaRenPatente a WHERE a.propietario.identificacion = :ciRuc AND a.estado = true",
                new String[]{"ciRuc"},
                new Object[]{propietario.getIdentificacion()});
        if (existePatenteParaContribuyente == null) {
            patente.setPropietario(propietario);
            // buscarRepresentanteLegal(new BigInteger(propietario.getRepresentanteLegal()));
            JsfUtil.executeJS("PF('dlgContribuyentes').hide();");
        } else {
            JsfUtil.addErrorMessage("Info", "Contribuyente ya Posee asignada Patentes");
        }

    }

    public String fullName(Cliente ente) {
        if (ente != null) {
            if (ente.getId() != null) {
                if ("PER_NAT".equals(ente.getTipoProv().getCodigo())) {
                    return Utils.isEmpty(ente.getApellido()) + " " + Utils.isEmpty(ente.getNombre());
                } else {
                    return Utils.isEmpty(ente.getRazonSocial());
                }
            }
        }
        return "";
    }

    //REDIRECT PAGE TO CREATE A NEW CLIENT
    public void nuevoCliente() {
        JsfUtil.redirect(CONFIG.URL_APP + "cliente-adm");
    }

    public void nuevaPatente() {
        JsfUtil.redirect(CONFIG.URL_APP + "moduloGestionTributario/Turismo/nuevoLocalComercial.xhtml");
    }

    ////NUEVAS PATENTES WIZARD
    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("actvidadEconomica")) {
            if (patente.getPropietario() == null || patente.getPropietario().getId() == null) {
                JsfUtil.addErrorMessage("Info", "Debe Seleccionar un Propietario Para Continuar");
                return event.getOldStep();
            } else {
                JsfUtil.update("wizardFlow");
                JsfUtil.update("growl");
            }
        }
        if (event.getNewStep().equals("establecimientos")) {
            if (actividad == null) {
                JsfUtil.addErrorMessage("Info", "Debe Seleccionar una Actividad");
                return event.getOldStep();
            } else {
                if (idPatente == null) {
                    seleccionarActividadPrincipal();
                    initRegistroLocal();
                } else {
                    local = new FinaRenLocalComercial();
                }
                JsfUtil.update("wizardFlow");
                JsfUtil.update("growl");
            }
        }
        JsfUtil.update("wizardFlow");

        return event.getNewStep();
    }

    ////SELECCION DE PREDIOS
    public void openDlgPredios() {
        predioListLazy = new LazyModel<>(CatPredioModel.class);
        JsfUtil.executeJS("PF('dlgPredios').show();");
    }

    public void predioSeleccionado() {
        JsfUtil.addInformationMessage("El predio seleccionado es: " + predio.getNumPredio(), "");
        JsfUtil.executeJS("PF('dlgPredios').hide();");
    }

    public void guardarActividad() {
        if (actividad.getDescripcion() == null || actividad.getDescripcion().equals("")) {
            JsfUtil.addErrorMessage("Advertencia", "Debe ingresar la descripcion");
            return;
        }
        try {
            actividad.setDescripcion(actividad.getDescripcion().toUpperCase());
            actividad.setEstado(Boolean.TRUE);
            actividad = services.guardarActividad(actividad);
            if (actividad != null) {
                actividadesSeleccionadas.add(actividad);
                agregarActvs();
                JsfUtil.addInformationMessage("Informacion", "Actividad Agregada Correctamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarActividadComercial() {
        if (actividad.getDescripcion() == null || actividad.getDescripcion().equals("")) {
            JsfUtil.addErrorMessage("Advertencia", "Debe ingresar la descripcion");
            return;
        }
        try {
            actividad.setDescripcion(actividad.getDescripcion().toUpperCase());
            actividad.setEstado(Boolean.TRUE);
            actividad = services.guardarActividad(actividad);
            if (actividad != null) {
                actividadesSeleccionadas.add(actividad);
                JsfUtil.addInformationMessage("Informacion", "Actividad Agregada Correctamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarActividadDelLocal(FinaRenActividadComercial actv) {
        localComercial.getRenActividadComercialCollection().remove(actv);
    }

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
                        JsfUtil.addInformationMessage("Informacion", "Local Actializado Correctamente");
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

    public void agregarCantidad() {
        if (local.getCantidadAccesoriosCollection() == null) {
            local.setCantidadAccesoriosCollection(new ArrayList());
        } else {
            for (RenLocalCantidadAccesorios rlca : local.getCantidadAccesoriosCollection()) {
                if (cantidad.getAnio().equals(rlca.getAnio())) {
                    JsfUtil.addErrorMessage("Info", "Año ya fue registrado");
                    return;
                }
            }
        }
        local.getCantidadAccesoriosCollection().add(cantidad);
        cantidad = new RenLocalCantidadAccesorios();
        cantidad.setAnio(Utils.getAnio(new Date()));
        cantidad.setEstado(Boolean.TRUE);
        cantidad.setFechaIngreso(new Date());
        cantidad.setUsuarioIngreso(uSession.getNameUser());
    }

    public void eliminarCantidadNew(RenLocalCantidadAccesorios acc) {
        if (acc.getId() != null) {
            acc.setEstado(Boolean.FALSE);
            services.update(acc);
        }
        local.getCantidadAccesoriosCollection().remove(acc);
    }

    public void enteSeleccionado(Cliente ente) {
        //this. = ente;
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
            e.printStackTrace();
        }
    }

    public void seleccionarUbicacion(FinaRenLocalUbicacion ubicacion) {
        this.localComercial.setUbicacion(ubicacion);
        JsfUtil.addInformationMessage("Info", "Ubicacion seleccionada: " + ubicacion.getDescripcion());
    }

    public void guardarLocalEdit() {
        Boolean validar = false;
        try {
//            if (ente == null) {
//                JsfUtil.addInformationMessage( "Info", "Debe seleccionar un propietario");
//                return;
//            }
//            if (predio == null) {
//                JsfUtil.addInformationMessage( "Info", "Debe seleccionar un predio");
//                return;
//            }
//            if (localComercial.getRenActividadComercialCollection() == null || localComercial.getRenActividadComercialCollection().isEmpty()) {
//                JsfUtil.addInformationMessage( "Info", "Debe seleccionar al menos una actividad");
//                return;
//            }
            // localComercial.setPropietario(ente);
            if (predio != null) {
                localComercial.setNumPredio(predio.getId());
            }
//            services.update(localComercial);
//            if (servicesRentas.guardarLocalComercial(localComercial, activosLocal) != null) {
//
//                JsfUtil.addInformationMessage( "Info", "Local comercial actualizado correctamente");
//            } else {
//                JsfUtil.addErrorMessage("Info", "Hubo un error al crear el local comercial. Inténtelo nuevamente");
//            }

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
//            //String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.instanciarParametros();
//            CertificadoExoneracionLocalActivos certificado;
//            ss.addParametro("logo1", path + SisVars.sisLogo);
//            ss.addParametro("logo2", path + SisVars.sisLogo);
//
//            if ((certificado = (CertificadoExoneracionLocalActivos) services.find(Querys.getCertExoLocalActByAnioAndLocal, new String[]{"anio", "local"}, new Object[]{Utils.getAnio(new Date()), local})) == null) {
//
//                certificado = new CertificadoExoneracionLocalActivos();
//                certificado.setFechaCreacion(new Date());
//                certificado.setUserCreador((AclUser) services.find(Querys.getAclUserByUser, new String[]{"user"}, new Object[]{uSession.getName_user()}));
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
//                    ss.setTieneDatasource(Boolean.FALSE);
//                    ss.setNombreReporte("certificado_exoneracion_activos_local_comercial");
//                    ss.setNombreSubCarpeta("permisoFuncionamiento");
//                    JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//                }
//            } else {
//                ss.addParametro("id_certificado", certificado.getId());
//                ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/") + "/");
//                ss.setTieneDatasource(Boolean.TRUE);
//                ss.setNombreReporte("certificado_exoneracion_activos_local_comercial");
//                ss.addParametro("ciRuc", local.getPropietario().getCiRuc());
//                ss.setNombreSubCarpeta("permisoFuncionamiento");
//                JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//                JsfUtil.addInformationMessage("Info", "El certificado fue generado anteriormente");
//            }
//            //
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

    public void verLiquidaciones(FinaRenLocalComercial local) {
        ss.instanciarParametros();
        ss.addParametro("localComercial", local);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "moduloGestionTributario/rentas//liquidacionesLocales.xhtml");
    }

    public String getDescripcionRubro(Long idRubro) {
        return (String) services.find("SELECT r.descripcion FROM FinaRenRubrosLiquidacion r WHERE r.id=:idRubro", new String[]{"idRubro"}, new Object[]{idRubro});
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public UserSession getuSession() {
        return uSession;
    }

    public void setuSession(UserSession uSession) {
        this.uSession = uSession;
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
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

    public CatPredioModel getPredio() {
        return predio;
    }

    public void setPredio(CatPredioModel predio) {
        this.predio = predio;
    }

    public LazyModel<CatPredioModel> getPredios() {
        return predios;
    }

    public void setPredios(LazyModel<CatPredioModel> predios) {
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

    public List<RenLocalTipoAccesorio> getTipos() {
        return tipos;
    }

    public void setTipos(List<RenLocalTipoAccesorio> tipos) {
        this.tipos = tipos;
    }

    public RenLocalCantidadAccesorios getCantidad() {
        return cantidad;
    }

    public void setCantidad(RenLocalCantidadAccesorios cantidad) {
        this.cantidad = cantidad;
    }

    public FinaRenLocalComercial getLocal() {
        return local;
    }

    public void setLocal(FinaRenLocalComercial local) {
        this.local = local;
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

    public DualListModel<FinaRenActividadComercial> getDualListActividades() {
        return dualListActividades;
    }

    public void setDualListActividades(DualListModel<FinaRenActividadComercial> dualListActividades) {
        this.dualListActividades = dualListActividades;
    }

    public List<FinaRenActividadComercial> getTargetActividades() {
        return targetActividades;
    }

    public void setTargetActividades(List<FinaRenActividadComercial> targetActividades) {
        this.targetActividades = targetActividades;
    }

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

    public LazyModel<CatPredioModel> getPredioListLazy() {
        return predioListLazy;
    }

    public void setPredioListLazy(LazyModel<CatPredioModel> predioListLazy) {
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
//</editor-fold>    
}
