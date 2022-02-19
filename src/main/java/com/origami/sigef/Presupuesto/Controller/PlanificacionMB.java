/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.PlanificacionPrograma;
import com.origami.sigef.Presupuesto.Service.PlanificacionPlanService;
import com.origami.sigef.Presupuesto.Service.PlanificacionProgramaService;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
import com.origami.sigef.common.entities.PlanificacionPlan;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalPoliticaService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanNacionalPoliticaService;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author ENRIQUE
 */
@Named("planificacionMbView")
@ViewScoped
public class PlanificacionMB implements Serializable {

    @Inject
    private PlanificacionPlanService planifiPlanService;
    @Inject
    private PlanificacionProgramaService planifiProgService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ManagerService service;
    @Inject
    private PlanNacionalPoliticaService planNacionalPoliticaService;
    @Inject
    private PlanLocalPoliticaService planLocalPoliticaService;
    @Inject
    private UserSession userSession;
    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private ActividadOperativaService actividadService;

    private List<CatalogoItem> catalogoItem;
    private List<PlanificacionPlan> planes;
    private List<PlanNacionalObjetivo> planNacionalObjetivos;
    private List<PlanLocalObjetivo> planLocalObjetivos;
    private List<PlanNacionalPolitica> politicasNacionales;
    private List<PlanLocalPolitica> politicasLocales;
    private List<PlanificacionPrograma> listaProgramas;
    private List<PlanificacionPrograma> listaProgramasid;

    private LazyModel<PlanificacionPlan> planesLazy;
    private LazyModel<PlanificacionPrograma> programasLazy;
    private Map<String, Object> param;

    private PlanNacionalObjetivo objetivoNacionalSeleccionado;
    private PlanNacionalPolitica politicaNacionalSeleccionada;
    private PlanLocalObjetivo objetivoLocalSeleccionado;

    private PlanLocalObjetivo objetivoLocalSelect;

    private PlanificacionPlan planificacionPlan;
    private PlanificacionPlan planSeleccionado;
    private PlanLocalPolitica politicaLocalSeleccionado;
    private PlanificacionPrograma planificacionPrograma;
    private PlanAnualPoliticaPublica planAnual;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private CatalogoItem estadoRegistrado, estadoRechazada, estadoAprobado, lineaAccionProyecto;

    private String planDescripcion;
    private Integer tamListaProgramas;
    private PlanificacionPrograma programaSeleccionado;

    private LazyModel<PlanAnualProgramaProyecto> lazyProyectos;
    private LazyModel<PlanificacionPrograma> listaProgramasLazy;

    private List<PlanAnualProgramaProyecto> listProyectos;
    private PlanAnualProgramaProyecto planificacionProyecto;
    private List<CatalogoItem> listaLineaAccion;

    private Boolean disabledEstatusPlanCreate;
    private Boolean disabledEstatusProgCreate;
    private Boolean disabledEstatusPryCreate;
    private Boolean renderObjetivoLocal;
    private Boolean renderObjetivoProyecto;
    private Boolean estadoPlan;
    private Boolean estadoPrograma;
    private Boolean estadoProyecto;
    private Boolean renderBtnReg;
    private Boolean renderBtnEdit;
    private Boolean renderBtnRegProg;
    private Boolean renderBtnEditProg;
    private Boolean renderBtnRegPry;
    private Boolean renderBtnEditPry;
    private Date fechaVigenciaPlan, fechaCaducidadPlan;
    private Date fechaVigenciaPrograma, fechaCaducidadPrograma;
    private Date fechaVigenciaProyecto, fechaCaducidadProyecto;
    private String dscPlan, dscPrograma, dscProyecto;

    @PostConstruct
    public void init() {
        loadingPlan(false);
        planes = new ArrayList<>();
        planNacionalObjetivos = new ArrayList<>();
        param = new HashMap<>();
        param.put("estado", true);
        planNacionalObjetivos = service.findAll(PlanNacionalObjetivo.class, param);
        planLocalObjetivos = new ArrayList<>();
        planLocalObjetivos = service.findAll(PlanLocalObjetivo.class, param);
        planes = planifiPlanService.getListAll();
        objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
        politicaNacionalSeleccionada = new PlanNacionalPolitica();
        objetivoLocalSeleccionado = new PlanLocalObjetivo();
        planificacionPlan = new PlanificacionPlan();
        planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
        planAnual = new PlanAnualPoliticaPublica();
        listaProgramas = new ArrayList<>();
        estadoRegistrado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RP");
        estadoRechazada = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REP");
        estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        listaLineaAccion = new ArrayList<>();
        listaLineaAccion = catalogoService.getItemsByCatalogo("LINEA_ACCION_PAPP");
        programaSeleccionado = new PlanificacionPrograma();
        renderObjetivoProyecto = true;
        planSeleccionado = new PlanificacionPlan();
        valoresIniciales();
    }

    public void valoresIniciales() {
        try {
            fechaVigenciaPlan = new SimpleDateFormat("yyyy/MM/dd").parse(Utils.getAnio(new Date()) + "/01/01");
            fechaVigenciaPrograma = new SimpleDateFormat("yyyy/MM/dd").parse(Utils.getAnio(new Date()) + "/01/01");
            fechaVigenciaProyecto = new SimpleDateFormat("yyyy/MM/dd").parse(Utils.getAnio(new Date()) + "/01/01");
            lineaAccionProyecto = new CatalogoItem();
            fechaCaducidadPlan = null;
            fechaCaducidadPrograma = null;
            fechaCaducidadProyecto = null;
            dscPlan = "";
            dscPrograma = "";
            dscProyecto = "";
            estadoPlan = Boolean.TRUE;
            estadoPrograma = Boolean.TRUE;
            estadoProyecto = Boolean.TRUE;
            disabledEstatusPlanCreate = Boolean.TRUE;
            renderBtnReg = Boolean.TRUE;
            renderBtnRegProg = Boolean.TRUE;
            renderBtnRegPry = Boolean.TRUE;
        } catch (Exception e) {
        }
    }
    public PlanLocalProgramaProyecto insertarEditarPlanLocal(PlanLocalPolitica politicaLocal, Boolean insertEdit, Long id) {
        PlanLocalProgramaProyecto planLocal = new PlanLocalProgramaProyecto();
        planLocal.setCodigo(null);
        planLocal.setFechaVigencia(new Date());
        planLocal.setFechaCaducidad(new Date());
        planLocal.setEstado(Boolean.TRUE);
        planLocal.setFechaCreacion(new Date());
        planLocal.setFechaModificacion(new Date());
        planLocal.setUsuarioCreacion(userSession.getNameUser());
        planLocal.setUsuarioModifica(userSession.getNameUser());
        planLocal.setEstadoPapp(estadoRegistrado);
        planLocal.setPolitica(politicaLocal);
        if (insertEdit) {
            planLocal = planLocalProgramaProyectoService.create(planLocal);
        } else {
            planLocal.setId(id);
            planLocalProgramaProyectoService.edit(planLocal);
        }
        return planLocal;
    }
    public PlanAnualPoliticaPublica insertarEditarPlanAnual(PlanLocalProgramaProyecto planLocal, PlanNacionalPolitica politicaNacional, Boolean insertEdit, Long id) {
        PlanAnualPoliticaPublica planAnual = new PlanAnualPoliticaPublica();
        planAnual.setPlanLocal(planLocal);
        planAnual.setPoliticaNacional(politicaNacional);
        planAnual.setAsignacionInicial(null);
        planAnual.setReformas(null);
        planAnual.setCodificado(null);
        planAnual.setComprometido(null);
        planAnual.setDevengado(null);
        planAnual.setRecaudado(null);
        planAnual.setEstado(true);
        planAnual.setEstadoPapp(estadoRegistrado);
        planAnual.setFechaCreacion(new Date());
        planAnual.setFechaModificacion(new Date());
        planAnual.setUsuarioModifica(userSession.getNameUser());
        planAnual.setUsuarioCreacion(userSession.getNameUser());
        if (insertEdit) {
            planAnual = (PlanAnualPoliticaPublica) planAnualPoliticaPublicaService.create(planAnual);
        } else {
            planAnual.setId(id);
            planAnualPoliticaPublicaService.edit(planAnual);
        }
        return planAnual;
    }
    public PlanificacionPlan insertPlan(PlanAnualPoliticaPublica planAnual, Date fechaVigencia, Date fechaCaducidad, String descripcion) {
        PlanificacionPlan plan = new PlanificacionPlan();
        if (descripcion.equals("NO APLICA")) {
            plan.setCodigo(null);
        } else {
            BigInteger idPlan = planifiPlanService.getNewIdPlanificacion("PLN");
            plan.setCodigo("PLN-" + Utils.completarCadenaConCeros(idPlan.toString(), 5));
        }
        plan.setEstado(true);
        plan.setDescripcion(descripcion);
        plan.setPoliticaPublica(planAnual);
        plan.setFechaCreacion(new Date());
        plan.setFechaCaducidad(fechaCaducidad);
        plan.setFechaVigencia(fechaVigencia);
        plan.setUsuarioCreacion(userSession.getNameUser());
        plan = planifiPlanService.create(plan);
        return plan;
    }
    public PlanificacionPrograma insertPrograma(PlanificacionPlan plan, Date fechaVigencia, Date fechaCaducidad, String descripcion) {
        PlanificacionPrograma programa = new PlanificacionPrograma();
        if (descripcion.equals("NO APLICA")) {
            programa.setCodigo(null);
        } else {
            BigInteger idPrograma = planifiPlanService.getNewIdPlanificacion("PRG");
            programa.setCodigo("PRG-" + Utils.completarCadenaConCeros(idPrograma.toString(), 5));
        }
        programa.setEstado(true);
        programa.setPanificacion(plan);
        programa.setDescripcion(descripcion);
        programa.setFechaCreacion(new Date());
        programa.setFechaCaducidad(fechaCaducidad);
        programa.setFechaVigencia(fechaVigencia);
        programa.setUsuarioCreacion(userSession.getNameUser());
        programa = planifiProgService.create(programa);
        return programa;
    }
    public PlanAnualProgramaProyecto insertProyecto(PlanificacionPrograma programa, Date fechaVigencia, Date fechaCaducidad, String descripcion, CatalogoItem lineAccion) {
        PlanAnualProgramaProyecto proyecto = new PlanAnualProgramaProyecto();
        if (descripcion.equals("NO APLICA")) {
            proyecto.setCodigo(null);
        } else {
            BigInteger idPry = planifiPlanService.getNewIdPlanificacion("PRY");
            proyecto.setCodigo("PRY-" + Utils.completarCadenaConCeros(idPry.toString(), 5));
        }
        proyecto.setPrograma(programa);
        proyecto.setNombreProgramaProyecto(descripcion);
        proyecto.setLineaAccion(lineAccion);
        proyecto.setEstado(true);
        proyecto.setFechaCreacion(new Date());
        proyecto.setFechaCaducidad(fechaCaducidad);
        proyecto.setFechaVigencia(fechaVigencia);
        proyecto.setUsuarioCreacion(userSession.getNameUser());
        proyecto = planAnualProgramaProyectoService.create(proyecto);
        return proyecto;
    }
    
    public PlanificacionPlan editPlan(PlanificacionPlan plan, PlanAnualPoliticaPublica planAnual, Date fechaVigencia, Date fechaCaducidad, String descripcion, Boolean estado) {
        plan.setEstado(estado);
        plan.setDescripcion(descripcion);
        plan.setPoliticaPublica(planAnual);
        plan.setFechaCaducidad(fechaCaducidad);
        plan.setFechaVigencia(fechaVigencia);
        plan.setFechaModificacion(new Date());
        plan.setUsuarioModifica(userSession.getNameUser());
        planifiPlanService.edit(plan);
        return plan;
    }
    public PlanificacionPrograma editPrograma(PlanificacionPlan plan, PlanificacionPrograma programa, Date fechaVigencia, Date fechaCaducidad, String descripcion, Boolean estado) {
        programa.setPanificacion(plan);
        programa.setDescripcion(descripcion);
        programa.setFechaModificacion(new Date());
        programa.setFechaCaducidad(fechaCaducidad);
        programa.setFechaVigencia(fechaVigencia);
        programa.setUsuarioModifica(userSession.getNameUser());
        programa.setEstado(estado);
        planifiProgService.edit(programa);
        return programa;
    }
    public PlanAnualProgramaProyecto editProyecto(PlanificacionPrograma prg, PlanAnualProgramaProyecto pry, Date fechaVig, Date fechaCad, String descrip, Boolean estado, CatalogoItem lineaAccion) {
        pry.setPrograma(prg);
        pry.setNombreProgramaProyecto(descrip);
        pry.setLineaAccion(lineaAccion);
        pry.setEstado(estado);
        pry.setFechaVigencia(fechaVig);
        pry.setFechaCaducidad(fechaCad);
        pry.setUsuarioModifica(userSession.getNameUser());
        pry.setFechaModificacion(new Date());
        planAnualProgramaProyectoService.edit(pry);
        return pry;
    }
    
    public PlanLocalPolitica registrarPoliticaLocalNoAplica(PlanLocalObjetivo objetivoLocal) {
        try {
            PlanLocalPolitica politicaLocal = new PlanLocalPolitica();
            politicaLocal.setObjetivo(objetivoLocal);
            politicaLocal.setPeriodo(objetivoLocal.getPeriodo());
            politicaLocal.setDescripcion("NO APLICA");
            politicaLocal.setFechaVigencia(new Date());
            politicaLocal.setFechaCreacion(new Date());
            politicaLocal.setUsuarioCreacion(userSession.getNameUser());
            String idPoliticaLocal = planifiPlanService.getIdObjetivoLocal(objetivoLocal);
            if (idPoliticaLocal == null) {
                idPoliticaLocal = "1";
            } else {
                Integer val = Integer.parseInt(idPoliticaLocal) + 1;
                idPoliticaLocal = val.toString();
            }
            politicaLocal.setCodigo(idPoliticaLocal);
            politicaLocal = planLocalPoliticaService.create(politicaLocal);
            return politicaLocal;
        } catch (Exception e) {
            e.printStackTrace();
            return new PlanLocalPolitica();
        }
    }
    public void registrarPlanVista() {
        try {
            if (null == politicaLocalSeleccionado) {
                politicaLocalSeleccionado = planifiPlanService.getPoliticaNoAplica(objetivoLocalSeleccionado);
                if (null == politicaLocalSeleccionado) {
                    politicaLocalSeleccionado = registrarPoliticaLocalNoAplica(objetivoLocalSeleccionado);
                }
            }
            PlanLocalProgramaProyecto planLocal = insertarEditarPlanLocal(politicaLocalSeleccionado, Boolean.TRUE, null);
            PlanAnualPoliticaPublica planAnual = insertarEditarPlanAnual(planLocal, politicaNacionalSeleccionada, Boolean.TRUE, null);
            PlanificacionPlan plan = insertPlan(planAnual, fechaVigenciaPlan, fechaCaducidadPlan, dscPlan);
            JsfUtil.addSuccessMessage("Mensaje", "Plan Registrado con Éxito " + plan.getCodigo());
            JsfUtil.executeJS("PF('dialogPlan').hide()");
            valoresIniciales();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Advertencia", "ERROR DE SISTEMA");
            resetValues();
        }
    }
    public void registrarProgramaVista() {
        try {
            if (planificacionPlan.getId() == null) {
                planificacionPlan = registrarPlanNoAplica();
            }
            planificacionPrograma = insertPrograma(planificacionPlan, fechaVigenciaPrograma, fechaCaducidadPrograma, dscPrograma);
            JsfUtil.addSuccessMessage("Mensaje", "Programa Registrado con Éxito " + planificacionPrograma.getCodigo());
            JsfUtil.executeJS("PF('dialogPrograma').hide()");
            valoresIniciales();
        } catch (Exception e) {
        }
    }
    public void registrarProyectoVista() {
        try {
            if (planificacionPrograma.getId() == null) {
                planificacionPrograma = registrarProgramaNoAplica();
            }
            planificacionProyecto = insertProyecto(planificacionPrograma, fechaVigenciaProyecto, fechaCaducidadProyecto, dscProyecto, lineaAccionProyecto);
            JsfUtil.addSuccessMessage("Mensaje", "Proyecto Registrado con Éxito " + planificacionProyecto.getCodigo());
            JsfUtil.executeJS("PF('dialogProyecto').hide()");
            valoresIniciales();
        } catch (Exception e) {
        }
    }
    
    public void editarPlanVista() {
        try {
            if (!estadoPlan) {
                if (eliminarPlnPrgPry(planSeleccionado, null, null)) {
                    fechaCaducidadPlan = new Date();
                } else {
                    JsfUtil.addErrorMessage("Error", "No se puede inactivar el Plan " + planSeleccionado.getCodigo() + " Posee proyectos activos");
                    return;
                }
            }
            if (null == politicaLocalSeleccionado) {
                politicaLocalSeleccionado = planifiPlanService.getPoliticaNoAplica(objetivoLocalSeleccionado);
                if (null == politicaLocalSeleccionado) {
                    politicaLocalSeleccionado = registrarPoliticaLocalNoAplica(objetivoLocalSeleccionado);
                }
            }
            Long id = planSeleccionado.getPoliticaPublica().getPlanLocal().getId();
            PlanLocalProgramaProyecto planLocal = insertarEditarPlanLocal(politicaLocalSeleccionado, Boolean.FALSE, id);
            id = planSeleccionado.getPoliticaPublica().getId();
            PlanAnualPoliticaPublica planAnual = insertarEditarPlanAnual(planLocal, politicaNacionalSeleccionada, Boolean.FALSE, id);
            PlanificacionPlan plan = editPlan(planSeleccionado, planAnual, fechaVigenciaPlan, fechaCaducidadPlan, dscPlan, estadoPlan);
            JsfUtil.addSuccessMessage("Mensaje", "Plan Editado con Éxito " + plan.getCodigo());
            JsfUtil.executeJS("PF('dialogPlan').hide()");
            valoresIniciales();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("", "ERROR DE SISTEMA");
            resetValues();
        }
    }
    public void editarProgramaVista() {
        try {
            if (!estadoPrograma) {
                if (eliminarPlnPrgPry(null, planificacionPrograma, null)) {
                    fechaCaducidadPrograma = new Date();
                } else {
                    JsfUtil.addErrorMessage("Error", "No se puede inactivar el Programa " + planificacionPrograma.getCodigo() + " Posee proyectos activos");
                    return;
                }
            }
            if (null == politicaLocalSeleccionado) {
                politicaLocalSeleccionado = planifiPlanService.getPoliticaNoAplica(objetivoLocalSeleccionado);
                if (null == politicaLocalSeleccionado) {
                    politicaLocalSeleccionado = registrarPoliticaLocalNoAplica(objetivoLocalSeleccionado);
                }
            }
            if (planificacionPlan.getCodigo() == null) { //SI POSEE UN PLAN NO APLICA
                Long id = planificacionPlan.getPoliticaPublica().getPlanLocal().getId();
                PlanLocalProgramaProyecto planLocal = insertarEditarPlanLocal(politicaLocalSeleccionado, Boolean.FALSE, id);
                id = planificacionPlan.getPoliticaPublica().getId();
                PlanAnualPoliticaPublica planAnual = insertarEditarPlanAnual(planLocal, politicaNacionalSeleccionada, Boolean.FALSE, id);
                planificacionPlan = editPlan(planificacionPlan, planAnual, null, null, "NO APLICA", Boolean.TRUE);
            }
            PlanificacionPrograma prog = editPrograma(planificacionPlan, planificacionPrograma, fechaVigenciaPrograma, fechaCaducidadPrograma, dscPrograma, estadoPrograma);
            JsfUtil.addSuccessMessage("Mensaje", "Programa Editado con Éxito " + prog.getCodigo());
            JsfUtil.executeJS("PF('dialogPrograma').hide()");
            valoresIniciales();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("", "ERROR DE SISTEMA");
            resetValues();
        }
    }
    public void editarProyectoVista() {
        try {
            if (!estadoProyecto) {
                if (eliminarPlnPrgPry(null, null, planificacionProyecto)) {
                    fechaCaducidadProyecto = new Date();
                } else {
                    JsfUtil.addErrorMessage("Error", "No se puede inactivar el Proyecto " + planificacionProyecto.getCodigo() + " Posee Actividades");
                    return;
                }
            }
            if (null == politicaLocalSeleccionado) {
                politicaLocalSeleccionado = planifiPlanService.getPoliticaNoAplica(objetivoLocalSeleccionado);
                if (null == politicaLocalSeleccionado) {
                    politicaLocalSeleccionado = registrarPoliticaLocalNoAplica(objetivoLocalSeleccionado);
                }
            }
            if (planificacionPrograma.getCodigo() == null) { //SI POSEE UN PROGRAMA "NO APLICA"
                //if (planificacionPlan.getCodigo() == null) { //SI POSEE UN PLAN "NO APLICA"
                    Long id = planificacionPlan.getPoliticaPublica().getPlanLocal().getId();
                    PlanLocalProgramaProyecto planLocal = insertarEditarPlanLocal(politicaLocalSeleccionado, Boolean.FALSE, id);
                    id = planificacionPlan.getPoliticaPublica().getId();
                    PlanAnualPoliticaPublica planAnual = insertarEditarPlanAnual(planLocal, politicaNacionalSeleccionada, Boolean.FALSE, id);
                    planificacionPlan = editPlan(planificacionPlan, planAnual, null, null, "NO APLICA", Boolean.TRUE);
                //}
                planificacionPrograma = editPrograma(planificacionPlan, planificacionPrograma, fechaVigenciaPrograma, fechaCaducidadPrograma, "NO APLICA", estadoPrograma);
            }
            PlanAnualProgramaProyecto proyecto = editProyecto(planificacionPrograma, planificacionProyecto, fechaVigenciaProyecto, fechaCaducidadProyecto, dscProyecto, estadoProyecto, lineaAccionProyecto);
            JsfUtil.addSuccessMessage("Mensaje", "Proyecto Editado con Éxito " + proyecto.getCodigo());
            JsfUtil.executeJS("PF('dialogProyecto').hide()");
            valoresIniciales();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("", "ERROR DE SISTEMA");
            resetValues();
        }
    }
    
    public void abrirEditarPlan(PlanificacionPlan plan) {
        planSeleccionado = plan;
        politicaLocalSeleccionado = new PlanLocalPolitica();
        objetivoLocalSeleccionado = new PlanLocalObjetivo();
        politicaNacionalSeleccionada = new PlanNacionalPolitica();
        objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
        politicaNacionalSeleccionada = plan.getPoliticaPublica().getPoliticaNacional();
        objetivoNacionalSeleccionado = politicaNacionalSeleccionada.getObjetivo();
        actualizarCbPoliticas();
        politicaLocalSeleccionado = plan.getPoliticaPublica().getPlanLocal().getPolitica();
        objetivoLocalSeleccionado = politicaLocalSeleccionado.getObjetivo();
        actualizarPoliticaLocal();
        dscPlan = plan.getDescripcion();
        fechaVigenciaPlan = plan.getFechaVigencia();
        estadoPlan = plan.getEstado();
        disabledEstatusPlanCreate = Boolean.FALSE;
        renderBtnReg = Boolean.FALSE;
        renderBtnEdit = Boolean.TRUE;
        JsfUtil.update("formPlanes");
    }
    public void abrirEditarPrograma(PlanificacionPrograma p) {
        planificacionPrograma = p;
        planificacionPlan = planificacionPrograma.getPanificacion();
        politicaLocalSeleccionado = new PlanLocalPolitica();
        objetivoLocalSeleccionado = new PlanLocalObjetivo();
        politicaNacionalSeleccionada = new PlanNacionalPolitica();
        objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
        politicaNacionalSeleccionada = p.getPanificacion().getPoliticaPublica().getPoliticaNacional();
        if (null != politicaNacionalSeleccionada) {
            objetivoNacionalSeleccionado = politicaNacionalSeleccionada.getObjetivo();
        }
        actualizarCbPoliticas();
        politicaLocalSeleccionado = p.getPanificacion().getPoliticaPublica().getPlanLocal().getPolitica();
        objetivoLocalSeleccionado = politicaLocalSeleccionado.getObjetivo();
        actualizarPoliticaLocal();
        dscPrograma = p.getDescripcion();
        fechaVigenciaPrograma = p.getFechaVigencia();
        estadoPrograma = p.getEstado();
        disabledEstatusProgCreate = Boolean.FALSE;
        renderBtnRegProg = Boolean.FALSE;
        renderBtnEditProg = Boolean.TRUE;
        JsfUtil.update("formProgramas");
    }
    public void abrirEditarProyecto(PlanAnualProgramaProyecto p) {
        planificacionProyecto = p;
        planificacionPrograma = p.getPrograma();
        planificacionPlan = planificacionPrograma.getPanificacion();
        politicaLocalSeleccionado = new PlanLocalPolitica();
        objetivoLocalSeleccionado = new PlanLocalObjetivo();
        politicaNacionalSeleccionada = new PlanNacionalPolitica();
        objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
        politicaNacionalSeleccionada = planificacionPlan.getPoliticaPublica().getPoliticaNacional();
        if (null != politicaNacionalSeleccionada) {
            objetivoNacionalSeleccionado = politicaNacionalSeleccionada.getObjetivo();
        }
        actualizarCbPoliticas();
        politicaLocalSeleccionado = planificacionPlan.getPoliticaPublica().getPlanLocal().getPolitica();
        objetivoLocalSeleccionado = politicaLocalSeleccionado.getObjetivo();
        actualizarPoliticaLocal();
        dscProyecto = p.getNombreProgramaProyecto();
        fechaVigenciaProyecto = p.getFechaVigencia();
        estadoProyecto = p.isEstado();
        lineaAccionProyecto = p.getLineaAccion();
        cargarLineaAccion();
        dscProyecto = p.getNombreProgramaProyecto();
        disabledEstatusPryCreate = Boolean.FALSE;
        renderBtnRegPry = Boolean.FALSE;
        renderBtnEditPry = Boolean.TRUE;
        JsfUtil.update("formProyecto");
    }
    
    public void newEditPlan(PlanificacionPlan p) {
        try {
            if (p == null) {
                resetValues();
                valoresIniciales();
                JsfUtil.update("formPlanes");
                cargarObjetivos();
                planificacionPlan = new PlanificacionPlan();
                planificacionPlan.setEstado(true);
                planificacionPlan.setFechaVigencia(new SimpleDateFormat("yyyy/MM/dd").parse(Utils.getAnio(new Date()) + "/01/01"));
                objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
                politicaNacionalSeleccionada = new PlanNacionalPolitica();
                objetivoLocalSeleccionado = new PlanLocalObjetivo();
                politicaLocalSeleccionado = new PlanLocalPolitica();
                planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                planAnual = new PlanAnualPoliticaPublica();
                disabledEstatusPlanCreate = true;
            } else {
                abrirEditarPlan(p);
                PrimeFaces.current().ajax().update(":tableplans");
                disabledEstatusPlanCreate = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void newEditPrograma(PlanificacionPrograma p) {
        try {
            if (p == null) {
                resetValues();
                PrimeFaces.current().ajax().update(":formProgramas");
                planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                planificacionPlan = new PlanificacionPlan();
                planificacionPrograma = new PlanificacionPrograma();
                planificacionPrograma.setEstado(true);
                planificacionPrograma.setFechaVigencia(new SimpleDateFormat("yyyy/MM/dd").parse(Utils.getAnio(new Date()) + "/01/01"));
                listaProgramas = new ArrayList<>();
                objetivoLocalSeleccionado = new PlanLocalObjetivo();
                politicaLocalSeleccionado = new PlanLocalPolitica();
                disabledEstatusProgCreate = true;
                cargarObjetivos();
            } else {
                abrirEditarPrograma(p);
                PrimeFaces.current().ajax().update(":tablaprogramaslist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resetValues();
        }
    }
    public void newEditProyecto(PlanAnualProgramaProyecto p) {
        try {
            if (p == null) {
                resetValues();
                JsfUtil.update("formProyecto");
                planificacionPlan = new PlanificacionPlan();
                planificacionPrograma = new PlanificacionPrograma();
                planificacionProyecto = new PlanAnualProgramaProyecto();
                planificacionProyecto.setEstado(true);
                planificacionProyecto.setFechaVigencia(new SimpleDateFormat("yyyy/MM/dd").parse(Utils.getAnio(new Date()) + "/01/01"));
                objetivoLocalSeleccionado = new PlanLocalObjetivo();
                politicaLocalSeleccionado = new PlanLocalPolitica();
                disabledEstatusProgCreate = true;
                planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                cargarObjetivos();
            } else {
                abrirEditarProyecto(p);
                PrimeFaces.current().ajax().update(":tablaproyectoslist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resetValues();
        }
    }
    
    public void inactivarPlan(PlanificacionPlan p) {
        try {
            if (eliminarPlnPrgPry(p, null, null)) {
                planificacionPlan = p;
                planificacionPlan.setUsuarioModifica(userSession.getNameUser());
                planificacionPlan.setFechaModificacion(new Date());
                planificacionPlan.setEstado(false);
                planifiPlanService.edit(planificacionPlan);
                JsfUtil.addSuccessMessage("Advertencia", "INACTIVADO CON ÉXITO");
            } else {
                JsfUtil.addErrorMessage("Advertencia", "No se puede inactivar el Plan " + p.getCodigo() + " Posee programas activos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
        }
    }
    public void inactivarPrograma(PlanificacionPrograma p) {
        try {
            if (eliminarPlnPrgPry(null, p, null)) {
                p.setEstado(Boolean.FALSE);
                p.setFechaCaducidad(new Date());
                p.setFechaModificacion(new Date());
                p.setUsuarioModifica(userSession.getNameUser());
                planifiProgService.edit(p);
                JsfUtil.addSuccessMessage("Advertencia", "INACTIVADO CON ÉXITO");
                JsfUtil.update("tablaprogramaslist");
            } else {
                JsfUtil.addErrorMessage("Advertencia", "No se puede inactivar el Programa " + p.getCodigo() + " Posee proyectos activos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
        }
    }
    public void inactivarProyecto(PlanAnualProgramaProyecto p) {
        try {
            if (eliminarPlnPrgPry(null, null, p)) {
                p.setEstado(Boolean.FALSE);
                p.setFechaCaducidad(new Date());
                p.setFechaModificacion(new Date());
                p.setUsuarioModifica(userSession.getNameUser());
                planAnualProgramaProyectoService.edit(p);
                JsfUtil.addSuccessMessage("Advertencia", "INACTIVADO CON ÉXITO");
            } else {
                JsfUtil.addErrorMessage("Advertencia", "No se puede inactivar el Proyecto " + p.getCodigo() + " Posee actividades");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
        }
    }
    
    public void eliminarPlan(PlanificacionPlan p) {
        try {
            if (eliminarPlnPrgPry(p, null, null)) {
                planifiPlanService.remove(p);
                JsfUtil.addSuccessMessage("Advertencia", "ELIMINADO CON ÉXITO");
            } else {
                JsfUtil.addErrorMessage("Advertencia", "No se puede eliminar el Plan " + p.getCodigo() + " Posee programas activos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
        }
    }
    public void eliminarPrograma(PlanificacionPrograma programa) {
        try {
            PlanificacionPrograma prog = programa;
            if (prog.getId() != null) {
                if (eliminarPlnPrgPry(null, prog, null)) {
                    planifiProgService.remove(prog);
                    listaProgramas.remove(programa);
                    JsfUtil.addSuccessMessage("Advertencia", "ELIMINADO CON ÉXITO");
                } else {
                    JsfUtil.addErrorMessage("Advertencia", "No se puede eliminar el Programa " + prog.getCodigo() + " Posee proyectos activos");
                }
            } else {
                listaProgramas.remove(programa);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
        }
    }
    public void eliminarProyecto(PlanAnualProgramaProyecto p) {
        try {
            if (eliminarPlnPrgPry(null, null, p)) {
                planAnualProgramaProyectoService.remove(p);
                JsfUtil.addSuccessMessage("Advertencia", "ELIMINADO CON ÉXITO");
            } else {
                JsfUtil.addErrorMessage("Advertencia", "No se puede eliminar el Proyecto " + p.getCodigo() + " Posee actividades");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
        }
    }
    
    public void actualizarCbPoliticas() {
        if (objetivoNacionalSeleccionado != null && objetivoNacionalSeleccionado.getId() != null) {
            this.politicasNacionales = planNacionalPoliticaService.findByNamedQuery("PlanNacionalPolitica.findFiltrarPolitica", new Object[]{objetivoNacionalSeleccionado});
        }
    }
    public void actualizarPoliticaLocal() {
        if (objetivoLocalSeleccionado != null && objetivoLocalSeleccionado.getId() != null) {
            this.politicasLocales = planLocalPoliticaService.findByNamedQuery("PlanLocalPolitica.findByPolitica", new Object[]{objetivoLocalSeleccionado});
        }
        planLocalObjetivos = new ArrayList<>();
        param = new HashMap<>();
        param.put("estado", true);
        planLocalObjetivos = service.findAll(PlanLocalObjetivo.class, param);
    }
    public PlanificacionPlan registrarPlanNoAplica() {
        try {
            if (null == politicaLocalSeleccionado) {
                politicaLocalSeleccionado = planifiPlanService.getPoliticaNoAplica(objetivoLocalSeleccionado);
                if (null == politicaLocalSeleccionado) {
                    politicaLocalSeleccionado = registrarPoliticaLocalNoAplica(objetivoLocalSeleccionado);
                }
            }
            PlanLocalProgramaProyecto planLocal = (PlanLocalProgramaProyecto) insertarEditarPlanLocal(politicaLocalSeleccionado, Boolean.TRUE, null);
            PlanAnualPoliticaPublica planAnual = (PlanAnualPoliticaPublica) insertarEditarPlanAnual(planLocal, null, Boolean.TRUE, null);
            PlanificacionPlan planNoAplica = new PlanificacionPlan();
            planNoAplica = planifiPlanService.obtienePlanNoAplica(planAnual);
            if (planNoAplica.getId() == null) {
                PlanificacionPlan plan = (PlanificacionPlan) insertPlan(planAnual, fechaVigenciaPlan, fechaCaducidadPlan, "NO APLICA");
                return plan;
            }
            return planNoAplica;
        } catch (Exception e) {
            return null;
        }
    }
    public PlanificacionPrograma registrarProgramaNoAplica() {
        try {
            if (null == politicaLocalSeleccionado) {
                politicaLocalSeleccionado = planifiPlanService.getPoliticaNoAplica(objetivoLocalSeleccionado);
                if (null == politicaLocalSeleccionado) {
                    politicaLocalSeleccionado = registrarPoliticaLocalNoAplica(objetivoLocalSeleccionado);
                }
            }
            PlanLocalProgramaProyecto planLocal = (PlanLocalProgramaProyecto) insertarEditarPlanLocal(politicaLocalSeleccionado, Boolean.TRUE, null);
            PlanAnualPoliticaPublica planAnual = (PlanAnualPoliticaPublica) insertarEditarPlanAnual(planLocal, null, Boolean.TRUE, null);
            PlanificacionPlan planNoAplica = new PlanificacionPlan();
            planNoAplica = planifiPlanService.obtienePlanNoAplica(planAnual);
            if (planNoAplica.getId() == null) {
                planNoAplica = (PlanificacionPlan) insertPlan(planAnual, fechaVigenciaPlan, fechaCaducidadPlan, "NO APLICA");
            }
            PlanificacionPrograma programaNoAplica = new PlanificacionPrograma();
            programaNoAplica = planifiPlanService.obtieneProyectoNoAplica(planNoAplica);
            if (programaNoAplica.getId() == null) {
                programaNoAplica = insertPrograma(planNoAplica, fechaVigenciaPlan, fechaCaducidadPlan, "NO APLICA");
            }
            return programaNoAplica;
        } catch (Exception e) {
            return null;
        }
    }
    
    //////////////////////////////////////////////////////////////////////////////////////
    
    
    public void obtieneHijosPlan(PlanificacionPlan plan) {
        listaProgramas = new ArrayList<>();
        param = new HashMap<>();
        param.put("panificacion", plan);
        param.put("estado", true);
        listaProgramas = service.findAll(PlanificacionPrograma.class, param);
        tamListaProgramas = listaProgramas.size();
    }

    public String existePoliticaLocalNoAplica(PlanLocalObjetivo objetivoLocal) {
        try {
            String idPoliticaLocal = planifiPlanService.getIdObjetivoLocal(objetivoLocal);
            return idPoliticaLocal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void filtroPlanesActivosInactivos(int valor) {
        planesLazy = new LazyModel<>(PlanificacionPlan.class);
        planesLazy.getFilterss().put("codigo:notEqual", null);
        switch (valor) {
            case 1:
                planesLazy.getFilterss().put("estado", true);
                break;
            case 2:
                planesLazy.getFilterss().put("estado", false);
                break;
        }
    }

    public void filtroProgramasActivosInactivos(int valor) {
        listaProgramasLazy = new LazyModel<>(PlanificacionPrograma.class);
        listaProgramasLazy.getFilterss().put("codigo:notEqual", null);
        switch (valor) {
            case 1:
                listaProgramasLazy.getFilterss().put("estado", true);
                break;
            case 2:
                listaProgramasLazy.getFilterss().put("estado", false);
                break;
        }
    }

    public void filtroProyectosActivosInactivos(int valor) {
        lazyProyectos = new LazyModel<>(PlanAnualProgramaProyecto.class);
        lazyProyectos.getFilterss().put("Codigo:notEqual", null);
        switch (valor) {
            case 1:
                lazyProyectos.getFilterss().put("estado", true);
                break;
            case 2:
                lazyProyectos.getFilterss().put("estado", false);
                break;
        }
    }

    public void elegiendoPlan(PlanificacionPlan p) {
        planificacionPlan = new PlanificacionPlan();
        planificacionPlan = p;
        JsfUtil.executeJS("PF('dlogoBusquedaPlan').hide()");
        JsfUtil.update("formProgramas");
        JsfUtil.update("formdlogoBusquedaPlan");
    }

    public void loadingPlan(Boolean dialogo) {
        if (!dialogo) {
            planesLazy = new LazyModel<>(PlanificacionPlan.class);
            planesLazy.getFilterss().put("codigo:notEqual", null);
        } else {
            planesLazy = new LazyModel<>(PlanificacionPlan.class);
            planesLazy.getFilterss().put("codigo:notEqual", null);
            planesLazy.getFilterss().put("estado", true);
        }

    }

    public void onTabChange(TabChangeEvent event) {
        switch (event.getTab().getId()) {
            case "planes":
                loadingPlan(false);
                resetValues();
                break;
            case "programas":
                obtieneProgramasLazy(false);
                resetValues();
                break;
            case "proyectos":
                obtieneProyectos();
                resetValues();
                break;
        }
    }

    public void cargarLineaAccion() {
        listaLineaAccion = new ArrayList<>();
        listaLineaAccion = catalogoService.getItemsByCatalogo("LINEA_ACCION_PAPP");
    }

    public void seleccionPrograma(PlanificacionPrograma p) {
        planificacionPrograma = new PlanificacionPrograma();
        planificacionPrograma = p;
        planificacionProyecto.setPrograma(planificacionPrograma);
    }

    public void obtieneProgramasLazy(Boolean dialogo) {
        if (!dialogo) {
            listaProgramasLazy = new LazyModel<>(PlanificacionPrograma.class);
            listaProgramasLazy.getFilterss().put("codigo:notEqual", null);
        } else {
            listaProgramasLazy = new LazyModel<>(PlanificacionPrograma.class);
            listaProgramasLazy.getFilterss().put("codigo:notEqual", null);
            listaProgramasLazy.getFilterss().put("estado", true);
        }
    }

    public void obtieneProyectos() {
        lazyProyectos = new LazyModel<>(PlanAnualProgramaProyecto.class);
        lazyProyectos.getFilterss().put("Codigo:notEqual", null);
    }

    public void obtieneProgramas() {
        listaProgramas = new ArrayList<>();
        param = new HashMap<>();
        //param.put("estado", true);
        listaProgramas = service.findAll(PlanificacionPrograma.class, param);
    }

    public List<PlanificacionPrograma> obtieneHijosPlan1(PlanificacionPlan plan) {
        param = new HashMap<>();
        param.put("panificacion", plan);
        param.put("estado", true);
        return service.findAll(PlanificacionPrograma.class, param);
    }


    public void resetValues() {
        planificacionPlan = new PlanificacionPlan();
        planificacionPrograma = new PlanificacionPrograma();
        planificacionProyecto = new PlanAnualProgramaProyecto();
        listaProgramas = new ArrayList<>();
        objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
        politicaNacionalSeleccionada = new PlanNacionalPolitica();
        objetivoLocalSeleccionado = new PlanLocalObjetivo();
        planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
        planAnual = new PlanAnualPoliticaPublica();
        renderObjetivoProyecto = true;
    }

    public void cargarObjetivos() {
        param = new HashMap<>();
        param.put("estado", true);
        planNacionalObjetivos = new ArrayList<>();
        planLocalObjetivos = new ArrayList<>();
        planNacionalObjetivos = service.findAll(PlanNacionalObjetivo.class, param);
        planLocalObjetivos = service.findAll(PlanLocalObjetivo.class, param);
    }

    public boolean eliminarPlnPrgPry(PlanificacionPlan pln, PlanificacionPrograma prg, PlanAnualProgramaProyecto pry) {
        try {
            param = new HashMap<>();
            param.put("estado", true);
            if (pln != null) {
                param.put("panificacion", pln);
                List<PlanificacionPrograma> listProgramas = service.findAll(PlanificacionPrograma.class, param);
                if (listProgramas.isEmpty()) {
                    return true;
                }
            } else if (prg != null) {
                param.put("programa", prg);
                List<PlanAnualProgramaProyecto> listProyectos = service.findAll(PlanAnualProgramaProyecto.class, param);
                if (listProyectos.isEmpty()) {
                    return true;
                }
            } else if (pry != null) {
                List<ActividadOperativa> listActividades = actividadService.findByNamedQuery("ActividadOperativa.findByVerificarHijos", new Object[]{pry.getId()});
                if (listActividades.isEmpty()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Error ", e.getMessage());
            return false;
        }

    }

    //<editor-fold defaultstate="collapsed" desc="SETTERS AND GETTERS">

    public CatalogoItem getLineaAccionProyecto() {
        return lineaAccionProyecto;
    }

    public void setLineaAccionProyecto(CatalogoItem lineaAccionProyecto) {
        this.lineaAccionProyecto = lineaAccionProyecto;
    }
    
    public Boolean getDisabledEstatusPryCreate() {
        return disabledEstatusPryCreate;
    }

    public void setDisabledEstatusPryCreate(Boolean disabledEstatusPryCreate) {
        this.disabledEstatusPryCreate = disabledEstatusPryCreate;
    }

    public Boolean getRenderBtnRegPry() {
        return renderBtnRegPry;
    }

    public void setRenderBtnRegPry(Boolean renderBtnRegPry) {
        this.renderBtnRegPry = renderBtnRegPry;
    }

    public Boolean getRenderBtnEditPry() {
        return renderBtnEditPry;
    }

    public void setRenderBtnEditPry(Boolean renderBtnEditPry) {
        this.renderBtnEditPry = renderBtnEditPry;
    }

    public String getDscProyecto() {
        return dscProyecto;
    }

    public void setDscProyecto(String dscProyecto) {
        this.dscProyecto = dscProyecto;
    }
    
    
    public Boolean getRenderBtnRegProg() {
        return renderBtnRegProg;
    }

    public void setRenderBtnRegProg(Boolean renderBtnRegProg) {
        this.renderBtnRegProg = renderBtnRegProg;
    }

    public Boolean getRenderBtnEditProg() {
        return renderBtnEditProg;
    }

    public void setRenderBtnEditProg(Boolean renderBtnEditProg) {
        this.renderBtnEditProg = renderBtnEditProg;
    }

    public String getDscPrograma() {
        return dscPrograma;
    }

    public void setDscPrograma(String dscPrograma) {
        this.dscPrograma = dscPrograma;
    }

    public Boolean getEstadoPrograma() {
        return estadoPrograma;
    }

    public void setEstadoPrograma(Boolean estadoPrograma) {
        this.estadoPrograma = estadoPrograma;
    }

    public Boolean getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(Boolean estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }

    public Boolean getRenderBtnReg() {
        return renderBtnReg;
    }

    public void setRenderBtnReg(Boolean renderBtnReg) {
        this.renderBtnReg = renderBtnReg;
    }

    public Boolean getRenderBtnEdit() {
        return renderBtnEdit;
    }

    public void setRenderBtnEdit(Boolean renderBtnEdit) {
        this.renderBtnEdit = renderBtnEdit;
    }

    public Boolean getEstadoPlan() {
        return estadoPlan;
    }

    public void setEstadoPlan(Boolean estadoPlan) {
        this.estadoPlan = estadoPlan;
    }

    public String getDscPlan() {
        return dscPlan;
    }

    public void setDscPlan(String dscPlan) {
        this.dscPlan = dscPlan;
    }

    public Date getFechaVigenciaPlan() {
        return fechaVigenciaPlan;
    }

    public void setFechaVigenciaPlan(Date fechaVigenciaPlan) {
        this.fechaVigenciaPlan = fechaVigenciaPlan;
    }

    public Date getFechaCaducidadPlan() {
        return fechaCaducidadPlan;
    }

    public void setFechaCaducidadPlan(Date fechaCaducidadPlan) {
        this.fechaCaducidadPlan = fechaCaducidadPlan;
    }

    public Date getFechaVigenciaPrograma() {
        return fechaVigenciaPrograma;
    }

    public void setFechaVigenciaPrograma(Date fechaVigenciaPrograma) {
        this.fechaVigenciaPrograma = fechaVigenciaPrograma;
    }

    public Date getFechaCaducidadPrograma() {
        return fechaCaducidadPrograma;
    }

    public void setFechaCaducidadPrograma(Date fechaCaducidadPrograma) {
        this.fechaCaducidadPrograma = fechaCaducidadPrograma;
    }

    public Date getFechaVigenciaProyecto() {
        return fechaVigenciaProyecto;
    }

    public void setFechaVigenciaProyecto(Date fechaVigenciaProyecto) {
        this.fechaVigenciaProyecto = fechaVigenciaProyecto;
    }

    public Date getFechaCaducidadProyecto() {
        return fechaCaducidadProyecto;
    }

    public void setFechaCaducidadProyecto(Date fechaCaducidadProyecto) {
        this.fechaCaducidadProyecto = fechaCaducidadProyecto;
    }

    public Boolean getRenderObjetivoProyecto() {
        return renderObjetivoProyecto;
    }

    public void setRenderObjetivoProyecto(Boolean renderObjetivoProyecto) {
        this.renderObjetivoProyecto = renderObjetivoProyecto;
    }

    public List<CatalogoItem> getListaLineaAccion() {
        return listaLineaAccion;
    }

    public Boolean getRenderObjetivoLocal() {
        return renderObjetivoLocal;
    }

    public void setRenderObjetivoLocal(Boolean renderObjetivoLocal) {
        this.renderObjetivoLocal = renderObjetivoLocal;
    }

    public PlanLocalObjetivo getObjetivoLocalSelect() {
        return objetivoLocalSelect;
    }

    public void setObjetivoLocalSelect(PlanLocalObjetivo objetivoLocalSelect) {
        this.objetivoLocalSelect = objetivoLocalSelect;
    }

    public Boolean getDisabledEstatusProgCreate() {
        return disabledEstatusProgCreate;
    }

    public void setDisabledEstatusProgCreate(Boolean disabledEstatusProgCreate) {
        this.disabledEstatusProgCreate = disabledEstatusProgCreate;
    }

    public void setListaLineaAccion(List<CatalogoItem> listaLineaAccion) {
        this.listaLineaAccion = listaLineaAccion;
    }

    public Boolean getDisabledEstatusPlanCreate() {
        return disabledEstatusPlanCreate;
    }

    public void setDisabledEstatusPlanCreate(Boolean disabledEstatusPlanCreate) {
        this.disabledEstatusPlanCreate = disabledEstatusPlanCreate;
    }

    public List<PlanificacionPrograma> getListaProgramasid() {
        return listaProgramasid;
    }

    public void setListaProgramasid(List<PlanificacionPrograma> listaProgramasid) {
        this.listaProgramasid = listaProgramasid;
    }

    public LazyModel<PlanificacionPrograma> getProgramasLazy() {
        return programasLazy;
    }

    public void setProgramasLazy(LazyModel<PlanificacionPrograma> programasLazy) {
        this.programasLazy = programasLazy;
    }

    public PlanAnualPoliticaPublica getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualPoliticaPublica planAnual) {
        this.planAnual = planAnual;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public List<PlanNacionalObjetivo> getPlanNacionalObjetivos() {
        return planNacionalObjetivos;
    }

    public void setPlanNacionalObjetivos(List<PlanNacionalObjetivo> planNacionalObjetivos) {
        this.planNacionalObjetivos = planNacionalObjetivos;
    }

    public List<PlanLocalObjetivo> getPlanLocalObjetivos() {
        return planLocalObjetivos;
    }

    public void setPlanLocalObjetivos(List<PlanLocalObjetivo> planLocalObjetivos) {
        this.planLocalObjetivos = planLocalObjetivos;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public List<CatalogoItem> getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(List<CatalogoItem> catalogoItem) {
        this.catalogoItem = catalogoItem;
    }

    public PlanificacionPlanService getPlanifiPlanService() {
        return planifiPlanService;
    }

    public void setPlanifiPlanService(PlanificacionPlanService planifiPlanService) {
        this.planifiPlanService = planifiPlanService;
    }

    public List<PlanificacionPlan> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanificacionPlan> planes) {
        this.planes = planes;
    }

    public LazyModel<PlanificacionPlan> getPlanesLazy() {
        return planesLazy;
    }

    public void setPlanesLazy(LazyModel<PlanificacionPlan> planesLazy) {
        this.planesLazy = planesLazy;
    }

    public List<PlanNacionalPolitica> getPoliticasNacionales() {
        return politicasNacionales;
    }

    public void setPoliticasNacionales(List<PlanNacionalPolitica> politicasNacionales) {
        this.politicasNacionales = politicasNacionales;
    }

    public List<PlanLocalPolitica> getPoliticasLocales() {
        return politicasLocales;
    }

    public void setPoliticasLocales(List<PlanLocalPolitica> politicasLocales) {
        this.politicasLocales = politicasLocales;
    }

    public PlanNacionalObjetivo getObjetivoNacionalSeleccionado() {
        return objetivoNacionalSeleccionado;
    }

    public void setObjetivoNacionalSeleccionado(PlanNacionalObjetivo objetivoNacionalSeleccionado) {
        this.objetivoNacionalSeleccionado = objetivoNacionalSeleccionado;
    }

    public PlanNacionalPolitica getPoliticaNacionalSeleccionada() {
        return politicaNacionalSeleccionada;
    }

    public void setPoliticaNacionalSeleccionada(PlanNacionalPolitica politicaNacionalSeleccionada) {
        this.politicaNacionalSeleccionada = politicaNacionalSeleccionada;
    }

    public PlanLocalObjetivo getObjetivoLocalSeleccionado() {
        return objetivoLocalSeleccionado;
    }

    public void setObjetivoLocalSeleccionado(PlanLocalObjetivo objetivoLocalSeleccionado) {
        this.objetivoLocalSeleccionado = objetivoLocalSeleccionado;
    }

    public PlanLocalPolitica getPoliticaLocalSeleccionado() {
        return politicaLocalSeleccionado;
    }

    public void setPoliticaLocalSeleccionado(PlanLocalPolitica politicaLocalSeleccionado) {
        this.politicaLocalSeleccionado = politicaLocalSeleccionado;
    }

    public PlanificacionPlan getPlanificacionPlan() {
        return planificacionPlan;
    }

    public void setPlanificacionPlan(PlanificacionPlan planificacionPlan) {
        this.planificacionPlan = planificacionPlan;
    }

    public List<PlanificacionPrograma> getListaProgramas() {
        return listaProgramas;
    }

    public void setListaProgramas(List<PlanificacionPrograma> listaProgramas) {
        this.listaProgramas = listaProgramas;
    }

    public PlanificacionPrograma getPlanificacionPrograma() {
        return planificacionPrograma;
    }

    public void setPlanificacionPrograma(PlanificacionPrograma planificacionPrograma) {
        this.planificacionPrograma = planificacionPrograma;
    }

    public LazyModel<PlanAnualProgramaProyecto> getLazyProyectos() {
        return lazyProyectos;
    }

    public void setLazyProyectos(LazyModel<PlanAnualProgramaProyecto> lazyProyectos) {
        this.lazyProyectos = lazyProyectos;
    }

    public LazyModel<PlanificacionPrograma> getListaProgramasLazy() {
        return listaProgramasLazy;
    }

    public void setListaProgramasLazy(LazyModel<PlanificacionPrograma> listaProgramasLazy) {
        this.listaProgramasLazy = listaProgramasLazy;
    }

    public List<PlanAnualProgramaProyecto> getListProyectos() {
        return listProyectos;
    }

    public void setListProyectos(List<PlanAnualProgramaProyecto> listProyectos) {
        this.listProyectos = listProyectos;
    }

    public PlanAnualProgramaProyecto getPlanificacionProyecto() {
        return planificacionProyecto;
    }

    public void setPlanificacionProyecto(PlanAnualProgramaProyecto planificacionProyecto) {
        this.planificacionProyecto = planificacionProyecto;
    }

    public PlanificacionPrograma getProgramaSeleccionado() {
        return programaSeleccionado;
    }

    public void setProgramaSeleccionado(PlanificacionPrograma programaSeleccionado) {
        this.programaSeleccionado = programaSeleccionado;
    }

    public Integer getTamListaProgramas() {
        return tamListaProgramas;
    }

    public void setTamListaProgramas(Integer tamListaProgramas) {
        this.tamListaProgramas = tamListaProgramas;
    }

    public String getPlanDescripcion() {
        return planDescripcion;
    }

    public void setPlanDescripcion(String planDescripcion) {
        this.planDescripcion = planDescripcion;
    }
//</editor-fold>
}
