/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Comisaria.Entities.Citaciones;
import com.gestionTributaria.Comisaria.Service.CitacionesService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "generarCitacionMB")
@ViewScoped
public class GenerarCitacionMB extends BpmnBaseRoot implements Serializable {
    
    @Inject
    private CitacionesService citacionesService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    
    private ScheduleModel lazyEventModel;
    private Citaciones citaciones;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private List<CatalogoItem> listaMotivoCitaciones;
    private List<CatalogoItem> listaNiveles;
    private LazyModel<Cliente> lazyCliente;
    private boolean tipo = false;
    private CatalogoItem comisariaSelect;
    private Date now;
    private Cliente c;
    private Citaciones citacionComparecencia;
    private LazyModel<Citaciones> lazyCitaciones;
    private UploadedFile file;
    
    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                lazyCitaciones = new LazyModel<>(Citaciones.class);
                lazyCitaciones.getFilterss().put("tramite", tramite.getId());
                listaMotivoCitaciones = new ArrayList<>();
                listaMotivoCitaciones = catalogoService.MostarTodoCatalogo("GT_motivos_citaciones");
                listaNiveles = new ArrayList<>();
                listaNiveles = catalogoService.MostarTodoCatalogo("GT_nivelea");
                
                lazyEventModel = loadEventsScheduleMOdel();
                comisariaSelect = catalogoService.getItemByCatalogoAndCodigo("GT_lista_comisarias", (String) getVariable("tipo_comisaria"));
                c = clienteService.find(getVariable("COMISARIO_SELECT_ID"));
                now = new Date();
                
                lazyCliente = new LazyModel<>(Cliente.class);
                lazyCliente.getFilterss().put("validado", true);
                lazyCliente.getFilterss().put("estado", true);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public LazyScheduleModel loadEventsScheduleMOdel() {
        return new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                List<Citaciones> eventos = citacionesService.listaEventosByComisaria(start, end, comisariaSelect);
                for (Citaciones eventoAtual : eventos) {
                    DefaultScheduleEvent tmp = new DefaultScheduleEvent();
                    tmp.setId(eventoAtual.getId().toString());
                    tmp.setTitle(eventoAtual.getMotivo());
                    tmp.setStartDate(eventoAtual.getFecha());
                    tmp.setEndDate(eventoAtual.getFecha());
                    tmp.setDescription("Fecha Presentación " + eventoAtual.getFechaPresentacion());
                    tmp.setData(eventoAtual);
                    if (eventoAtual.getTipoEvento() != null) {
                        if (eventoAtual.getTipoEvento().getCodigo().equals("especial")) {
                            tmp.setStyleClass("especial");
                        } else if (eventoAtual.getTipoEvento().getCodigo().equals("advertencia")) {
                            tmp.setStyleClass("advertencia");
                        } else {
                            tmp.setStyleClass("importante");
                        }
                    } else {
                        tmp.setStyleClass("importante");
                    }
                    addEvent(tmp);
                }
            }
        };
    }
    
    public void save() {
        if (citaciones.getId() == null) {
            citaciones.setNum(citacionesService.numCitaciones());
            citaciones.setTramite(tramite.getId());
            citaciones.setFecha(new Date());
            citaciones.setTipo_tramite(tramite.getTipoTramite().getId());
            citaciones.setFechaCrea(new Date());
            citaciones.setPersona(c);
            citaciones.setUsuarioCrea(session.getNameUser());
            citaciones = citacionesService.create(citaciones);
        } else {
            citaciones.setUsuarioMod(session.getNameUser());
            citaciones.setFechaMod(new Date());
            citacionesService.edit(citaciones);
        }
        loadEventsScheduleMOdel();
        JsfUtil.executeJS("PF('eventDialog').hide()");
        JsfUtil.update("mainForm");
        JsfUtil.addInformationMessage("", "TRANSACCIÓN CORRECTA");
    }
    
    public void opendlg() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }
    
    public void onDateSelect(SelectEvent selectEvent) {
        now = new Date();
        this.citaciones = new Citaciones();
        Usuarios deman = usuarioService.findByUsuario(session.getNameUser());
        if (deman != null && deman.getFuncionario() != null && deman.getFuncionario().getPersona() != null) {
            citaciones.setDenuciante(deman.getFuncionario().getPersona());
            citaciones.setCedulaDenunciante(deman.getFuncionario().getPersona().getIdentificacionCompleta());
            citaciones.setNombreCompletoDenunciante(deman.getFuncionario().getPersona().getNombreCompleto());
        }
        citaciones.setComisaria(comisariaSelect);
        citaciones.setFecha((Date) selectEvent.getObject());
        citaciones.setFechaPresentacion((Date) selectEvent.getObject());
        citaciones.setHora((Date) selectEvent.getObject());
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        citaciones = (Citaciones) event.getData();
    }
    
    public void completarTarea() {
        try {
//            if (citaciones != null) {
//                if (validateCitacion()) {
//                    return;
//                }
//            }
//            System.out.println("getParamts().get(\"usuarioComisario\") " + getParamts().get("usuarioComisario"));
            String usuario = (String) getParamts().get("usuarioComisario");
            if (usuario == null) {
                usuario = "";
            }
            getParamts().put("usuarioComisarioMulta", usuario.equals("") ? "admin_1" : usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
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
    
    private Boolean validateCitacion() {
        if (citaciones.getComisaria() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "SELECCIONE UNA COMISARIA");
            return Boolean.TRUE;
        }
        if (citaciones.getMotivoCitaciones() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "SELECCIONE EL CITADO POR");
            return Boolean.TRUE;
        }
        if (citaciones.getDenunciado() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "SELECCIONE EL DENUNCIADO");
            return Boolean.TRUE;
        }
        if (citaciones.getNumCitacionDenunciado() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE EL NÚMERO DE CITACIÓN DEL DENUNCIADO");
            return Boolean.TRUE;
        }
        if (citaciones.getDireccionDenunciado() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE LA DIRECCIÓN DEL DENUNCIADO");
            return Boolean.TRUE;
        }
        if (citaciones.getDenuciante() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "SELECCIONE EL DENUNCIANTE");
            return Boolean.TRUE;
        }
        if (citaciones.getNumCitacionDenunciante() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE EL NÚMERO DE CITACIÓN DEL DENUNCIANTE");
            return Boolean.TRUE;
        }
        if (citaciones.getDireccion() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE LA DIRECCIÓN DEL DENUNCIANTE");
            return Boolean.TRUE;
        }
        if (citaciones.getMotivo() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE EL MOTIVO DE LA CITACIÓN");
            return Boolean.TRUE;
        }
        if (citaciones.getFecha() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE LA FECHA DE EMISIÓN DE LA CITACIÓN");
            return Boolean.TRUE;
        }
        if (citaciones.getFechaPresentacion() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "INGRESE LA FECHA DE LA PRESENTACIÓN DE LA CITACIÓN");
            return Boolean.TRUE;
        }
        if (citaciones.getTipoEvento() == null) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "SELECCIONE EL TIPO DE EVENTO DE LA CITACIÓN");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public void seleccionarCliente(Cliente c) {
        if (tipo) {
            citaciones.setDenuciante(c);
            citaciones.setCedulaDenunciante(c.getIdentificacionCompleta());
            citaciones.setNombreCompletoDenunciante(c.getNombreCompleto());
        } else {
            citaciones.setDenunciado(c);
            citaciones.setCedulaDenunciado(c.getIdentificacionCompleta());
            citaciones.setNombreCompeltoDennunciado(c.getNombreCompleto());
        }
    }
    
    public void cargarClientes(boolean tipo) {
        this.tipo = tipo;
    }
    
    public void dialogCitacionesComparecencia(Citaciones c) {
        citacionComparecencia = c;
        JsfUtil.update("idFormCompadecencia");
        JsfUtil.executeJS("PF('dialogCompadecencia').show()");
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
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }
    
    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }
    
    public Citaciones getCitaciones() {
        return citaciones;
    }
    
    public void setCitaciones(Citaciones citaciones) {
        this.citaciones = citaciones;
    }
    
    public List<CatalogoItem> getListaMotivoCitaciones() {
        return listaMotivoCitaciones;
    }
    
    public void setListaMotivoCitaciones(List<CatalogoItem> listaMotivoCitaciones) {
        this.listaMotivoCitaciones = listaMotivoCitaciones;
    }
    
    public List<CatalogoItem> getListaNiveles() {
        return listaNiveles;
    }
    
    public void setListaNiveles(List<CatalogoItem> listaNiveles) {
        this.listaNiveles = listaNiveles;
    }
    
    public LazyModel<Cliente> getLazyCliente() {
        return lazyCliente;
    }
    
    public void setLazyCliente(LazyModel<Cliente> lazyCliente) {
        this.lazyCliente = lazyCliente;
    }
    
    public CatalogoItem getComisariaSelect() {
        return comisariaSelect;
    }
    
    public void setComisariaSelect(CatalogoItem comisariaSelect) {
        this.comisariaSelect = comisariaSelect;
    }
    
    public Date getNow() {
        return now;
    }
    
    public void setNow(Date now) {
        this.now = now;
    }
    
    public Citaciones getCitacionComparecencia() {
        return citacionComparecencia;
    }
    
    public void setCitacionComparecencia(Citaciones citacionComparecencia) {
        this.citacionComparecencia = citacionComparecencia;
    }
    
    public LazyModel<Citaciones> getLazyCitaciones() {
        return lazyCitaciones;
    }
    
    public void setLazyCitaciones(LazyModel<Citaciones> lazyCitaciones) {
        this.lazyCitaciones = lazyCitaciones;
    }
//</editor-fold>

}
