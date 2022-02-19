/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller.ViaPublica;

import com.gestionTributaria.Comisaria.Entities.Inspecciones;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Comisaria.Service.InspeccionService;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.ventanilla.Entity.ServicioRequisito;
import com.ventanilla.Entity.SolicitudServicios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;

/**
 *
 * @author Admin
 */
@Named
@ViewScoped
public class DegalagoInspeccionMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private InspeccionService inspeccionService;
    @Inject
    private ComisariaServices comisariaServices;
    @Inject
    private ManagerService service;
    private Inspecciones inspeccion;
    private CatPredio predio;
    private LazyModel<Inspecciones> lazyInspeccion;
    private List<ServicioRequisito> listRequisitoTasa;
    private int tipoCons;
    private String esUrbano;
    private SolicitudServicios solicitud;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                inspeccion = new Inspecciones();
                loadInspecciones();
                tipoCons = 4;
                getRequisitosByServicio();
                esUrbano = "1";
                predio = new CatPredio();
                solicitud = new SolicitudServicios();
                solicitud = comisariaServices.getSolcitud(tramite.getId());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void loadInspecciones() {
        lazyInspeccion = new LazyModel<>(Inspecciones.class);
        lazyInspeccion.getFilterss().put("numTramite", tramite.getNumTramite());
    }

    public void getRequisitosByServicio() {
        if (tramite.getServicio().getId() != null) {
            listRequisitoTasa = new ArrayList<>();
            listRequisitoTasa = comisariaServices.getRequisitosTrmaitesServices(tramite.getId());
        }

    }

    public void generarSolciitud(Inspecciones solicitudInspeccion) {
        this.inspeccion = new Inspecciones();
        if (solicitudInspeccion != null) {
            this.inspeccion = solicitudInspeccion;
            if (solicitudInspeccion.getPredio() != null) {
                this.predio = service.find(CatPredio.class, solicitudInspeccion.getPredio());
            }
        } else {
            this.inspeccion.setComisariaUser(session.getUsuario());
        }

    }

    public void saveSolcitud() {
        String mensaje = "";

        inspeccion.setNumTramite(tramite.getNumTramite());

        if (predio != null && predio.getId() != null) {
            inspeccion.setPredio(predio.getId());
        }

        if (inspeccion.getId() != null) {

            mensaje = "edito";
            inspeccionService.edit(inspeccion);

        } else {
            if (this.tramite.getTipoTramite().getAbreviatura().equals("IPVE") || this.tramite.getTipoTramite().getAbreviatura().equals("IPRN")) {
                inspeccion.setTipoComisaria("INQUILINATO");

            } else if (this.tramite.getTipoTramite().getAbreviatura().equals("PVPU")) {
                inspeccion.setTipoComisaria("VIA_PUBLICA");
            }
            inspeccion.setOrigen(inspeccion.getTipoComisaria());
            mensaje = "creo";
            inspeccion.setNoSolicitud(inspeccionService.noSolicitud());
            inspeccion = inspeccionService.create(inspeccion);
        }

        JsfUtil.addInformationMessage("", "La solciitud se " + mensaje + " exitosamente");
        JsfUtil.executeJS("PF('dlgoInspeccion').hide()");
        getLazyInspeccion();

    }

    public void consultar() {

        try {

            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                inspeccion.setLugar(predio.getCalle());

            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CatPredio consultarP(String p) {
        CatPredio tem = null;
        tem = consultar(tipoCons, predio);
        return tem;

    }

    public CatPredio consultar(Integer tipoCons, CatPredio pred) {
        CatPredio temp = new CatPredio();
        CatPredio predio = pred;
        switch (tipoCons) {
            case 1: // Codigo Anterior

                break;
            case 2: // Codigo Nuevo
                System.out.println("estructura predial");
                if (predio.getSector() > 0 || predio.getMz() > 0 || predio.getProvincia() > 0 || predio.getCanton() > 0
                        || predio.getParroquia() > 0 || predio.getZona() > 0 || predio.getSolar() > 0 || predio.getPiso() >= 0
                        || predio.getUnidad() >= 0 || predio.getBloque() >= 0) {
                    Map<String, Object> paramtr = new HashMap<>();
                    paramtr.put("estado", "A");
                    paramtr.put("sector", predio.getSector());
                    paramtr.put("mz", predio.getMz());
                    paramtr.put("provincia", predio.getProvincia());
                    paramtr.put("canton", predio.getCanton());
                    paramtr.put("parroquia", predio.getParroquia());
                    paramtr.put("zona", predio.getZona());
                    paramtr.put("solar", predio.getSolar());
                    paramtr.put("piso", predio.getPiso());
                    paramtr.put("unidad", predio.getUnidad());
                    paramtr.put("bloque", predio.getBloque());
                    if (esUrbano == "1") {
                        paramtr.put("tipoPredio", "U");
                    } else {
                        paramtr.put("tipoPredio", "R");
                    }
                    temp = service.findByParameter(CatPredio.class, paramtr);
                }
                break;

            case 3:// Numero de Predio

                if (predio.getNumPredio() != null) {
                    System.out.println("num predio");
                    System.out.println("EL PREDIO---->" + predio.getId());
                    temp = service.getPredioNumPredio(predio.getNumPredio().longValue(), esUrbano);
                    System.out.println("EL PREDIO---->" + temp.getId());
                }

                break;
            case 4:
                if (predio.getClaveCat() != null) {
                    System.out.println("clave catastral" + predio.getClaveCat() + "  " + esUrbano);
                    temp = service.getPredioByClaveCat(predio.getClaveCat(), esUrbano);
                    System.out.println("el predio ser encontro--->  " + temp);
                }

                break;
            case 5:// Clave anterior
                if (predio.getPredialant() != null) {
                    temp = service.getPredioByClaveCatAnt(predio.getPredialant(), esUrbano);
                }

                break;

        }
        if (temp != null) {
            JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.predioEncontrado + temp.getClaveCat());
            return temp;
        } else {
            return null;
        }
    }

    public void send() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
    }

    public void completarTarea(int index) {
        try {
            String usuario = "";
            getParamts().put("soli_delegado", index);
            if (index == 1) {

                if (!inspeccionService.verificarSolciitud(tramite.getNumTramite())) {
                    JsfUtil.addWarningMessage("", "Debe generar una solicitud de inspección para poder solicitar un delegado");
                    return;
                }

                usuario = clienteService.getrolsUser(RolUsuario.jefeDelegado);
                getParamts().put("solicitacion_delegado", usuario);

            } else {
                List<HistoricTaskInstance> tasks = engine.getTaskByProcessInstanceId(this.tramite.getIdProceso());
                System.out.println("tastk " + tasks.size());
                if (tasks != null && !tasks.isEmpty()) {
                    for (HistoricTaskInstance item : tasks) {
                    
                        if (item.getTaskDefinitionKey().equals("usertask10")) {
                           
                            usuario = item.getAssignee();
                            break;
                        }
                    }
                }

                getParamts().put("emision", usuario);

            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }
    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">

    public Inspecciones getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(Inspecciones inspeccion) {
        this.inspeccion = inspeccion;
    }

    public SolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public LazyModel<Inspecciones> getLazyInspeccion() {
        return lazyInspeccion;
    }

    public List<ServicioRequisito> getListRequisitoTasa() {
        return listRequisitoTasa;
    }

    public void setListRequisitoTasa(List<ServicioRequisito> listRequisitoTasa) {
        this.listRequisitoTasa = listRequisitoTasa;
    }

    public void setLazyInspeccion(LazyModel<Inspecciones> lazyInspeccion) {
        this.lazyInspeccion = lazyInspeccion;
    }

    public int getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(int tipoCons) {
        this.tipoCons = tipoCons;
    }

    public String getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(String esUrbano) {
        this.esUrbano = esUrbano;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }
//</editor-fold>

}
