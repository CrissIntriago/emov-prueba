/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.TareasWFLazy;
import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import com.origami.sigef.resource.procesos.services.HistoricoTramiteService;
import com.origami.sigef.resource.procesos.services.TipoTramiteService;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;



/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class BandejaTareasVentanillaMB implements Serializable{
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManagerService ems;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private BpmBaseEngine baseEngine;
    @Inject
    private FileUploadDoc fileSUploadDoc;
    @Inject
    private TipoTramiteService tipotramiteService;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private HistoricoTramiteService tramiteService;

    protected TareasWFLazy lazy;
    protected String usuario;
    protected Integer cantidad = 0;
    protected StringBuilder queryCount = null;
    private List<HistoricTaskInstance> tasks;
    private TareasActivas tarea;
    private StreamedContent imageProcessInstance;
    private List<TipoTramite> tipoTramites;
    private OpcionBusqueda opcionBusqueda;
    private List<MasterCatalogo> listaPeriodo;
    private int tiempoFaltante;

    @PostConstruct
    protected void iniView() {
        try {
            opcionBusqueda = new OpcionBusqueda();
            this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
            if (!JsfUtil.isAjaxRequest()) {
                if (listaPeriodo != null) {
                    int indice = listaPeriodo.size();
                    if (!listaPeriodo.isEmpty() && indice == 1) {
                        opcionBusqueda.setAnio(listaPeriodo.get(0).getAnio());
                    } else {
                        if (indice > 0) {
                            opcionBusqueda.setAnio(listaPeriodo.get(indice - 1).getAnio());
                        }
                    }
                }
                tipoTramites = tipotramiteService.findByUnidad(null);
                updateTaskUser();
                openDlgMensaje();
            }
        } catch (Exception e) {
            Logger.getLogger(BandejaTareasVentanillaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void updateTaskUser() {
        try {
            if (usuario == null) {
                usuario = us.getNameUser();
            }
            if (usuario.trim().isEmpty()) {
                usuario = us.getNameUser();
            }
            if (opcionBusqueda.getAnio() != null) {
                lazy = new TareasWFLazy(usuario, opcionBusqueda);
                lazy.setDistinct(false);
                lazy.getFilterss().put("idTipoTramite.id", 130);
                actualizarContadorTareas();
            } else {
                lazy = null;
                cantidad = 0;
            }
        } catch (Exception e) {
            Logger.getLogger(BandejaTareasVentanillaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void openDlgMensaje() {
        String ordenesAuto = valoresService.findByCodigo("PRESENTAR_MENSAJE");
        int maximoDia = valoresService.findByCodigo1("MAX_DIA_CAMBIO_CONTRASENIA").intValue();
        if (Boolean.valueOf(ordenesAuto)) {
            Usuarios user = usuarioService.findByUsuario(usuario);
            if (user.getCaducarClave()) {
                if (user.getFechaCaducidad() != null) {
                    tiempoFaltante = Utils.restarFechas(new Date(), user.getFechaCaducidad()).intValue();
                    if (tiempoFaltante <= maximoDia) {
                        PrimeFaces.current().executeScript("PF('DlgMensajeDiario').show()");
                        PrimeFaces.current().ajax().update("formMensajeDiario");
                    }
                }
            }
        }
    }

    private void actualizarContadorTareas() {
        if (usuario != null) {
            queryCount = new StringBuilder("SELECT CAST(COUNT(*) AS INTEGER) FROM procesos.tareas_activas "
                    + "WHERE id_tipo_tramite = 130 AND (assignee = ? OR candidate ~ ?) AND EXTRACT(YEAR FROM fecha_ingreso) = ?");
            cantidad = (Integer) ems.getNativeQuery(queryCount.toString(), new Object[]{usuario, usuario, opcionBusqueda.getAnio().intValue()});
        }
    }

    public void sendTask(TareasActivas task) {
        us.setTaskID(task.getTaskId());
        us.setVarTemp(task);
        JsfUtil.redirectFaces(task.getFormKey());
    }

    public void viewProcess(TareasActivas task) {
        tasks = baseEngine.getTaskByProcessInstanceId(task.getProcInstId());
        tarea = task;
        this.tarea.getTramite().setNombreBeneficiario(tramiteService.findReferencia(tarea.getTramite()));
        InputStream stream = baseEngine.getProcessInstanceDiagram(task.getProcInstId());
        imageProcessInstance = new DefaultStreamedContent(stream);
        JsfUtil.executeJS("PF('dlgTarea').show()");
        JsfUtil.update("frmDetalleTarea");
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public int getTiempoFaltante() {
        return tiempoFaltante;
    }

    public void setTiempoFaltante(int tiempoFaltante) {
        this.tiempoFaltante = tiempoFaltante;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public TareasWFLazy getLazy() {
        return lazy;
    }

    public void setLazy(TareasWFLazy lazy) {
        this.lazy = lazy;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public List<HistoricTaskInstance> getTasks() {
        return tasks;
    }

    public void setTasks(List<HistoricTaskInstance> tasks) {
        this.tasks = tasks;
    }

    public TareasActivas getTarea() {
        return tarea;
    }

    public void setTarea(TareasActivas tarea) {
        this.tarea = tarea;
    }

    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }

    public List<TipoTramite> getTipoTramites() {
        return tipoTramites;
    }

    public void setTipoTramites(List<TipoTramite> tipoTramites) {
        this.tipoTramites = tipoTramites;
    }
//</editor-fold>
}