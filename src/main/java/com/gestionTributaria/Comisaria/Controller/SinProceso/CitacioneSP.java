package com.gestionTributaria.Comisaria.Controller.SinProceso;

import com.gestionTributaria.Comisaria.Entities.Citaciones;
import com.gestionTributaria.Comisaria.Service.CitacionesService;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class CitacioneSP implements Serializable {

    @Inject
    private CitacionesService citacionesService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ManagerService service;

    private List<ScheduleEvent> scheduleEvents;
    private Citaciones citaciones;
    private List<CatalogoItem> listaComisarias;
    private List<CatalogoItem> listaMotivoCitaciones;
    private List<CatalogoItem> listaNiveles;
    private LazyModel<Cliente> lazyCliente;
    private static final Logger LOG = Logger.getLogger(CitacioneSP.class.getName());
    private boolean tipo = false;

    private ScheduleModel lazyEventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    @PostConstruct
    public void init() {
        listaComisarias = new ArrayList<>();
        listaComisarias = catalogoService.MostarTodoCatalogo("GT_lista_comisarias");
        listaMotivoCitaciones = new ArrayList<>();
        listaMotivoCitaciones = catalogoService.MostarTodoCatalogo("GT_motivos_citaciones");
        listaNiveles = new ArrayList<>();
        listaNiveles = catalogoService.MostarTodoCatalogo("GT_nivelea");
        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                List<Citaciones> eventos = citacionesService.listaEventos(start, end,userSession.getNameUser());
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

    public void onDateSelect(SelectEvent selectEvent) {
        this.citaciones = new Citaciones();
        Usuarios deman = usuarioService.findByUsuario(userSession.getNameUser());
        if (deman != null && deman.getFuncionario() != null && deman.getFuncionario().getPersona() != null) {
            citaciones.setDenuciante(deman.getFuncionario().getPersona());
            citaciones.setCedulaDenunciante(deman.getFuncionario().getPersona().getIdentificacionCompleta());
            citaciones.setNombreCompletoDenunciante(deman.getFuncionario().getPersona().getNombreCompleto());
        }
        citaciones.setFecha((Date) selectEvent.getObject());
        citaciones.setFechaPresentacion((Date) selectEvent.getObject());
        citaciones.setHora((Date) selectEvent.getObject());
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        citaciones = (Citaciones) event.getData();
    }

    public void openDlogo() {
        this.citaciones = new Citaciones();
        Usuarios deman = usuarioService.findByUsuario(userSession.getNameUser());
        if (deman != null && deman.getFuncionario() != null && deman.getFuncionario().getPersona() != null) {
            citaciones.setDenuciante(deman.getFuncionario().getPersona());
            citaciones.setCedulaDenunciante(deman.getFuncionario().getPersona().getIdentificacionCompleta());
            citaciones.setNombreCompletoDenunciante(deman.getFuncionario().getPersona().getNombreCompleto());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 1);  // numero de días a añadir, o restar en caso de días
        citaciones.setFecha(calendar.getTime());
        citaciones.setFechaPresentacion(calendar.getTime());
        citaciones.setHora(new Date());
    }

    public void save() {
        if (validateCitacion()) {
            return;
        }
        if (citaciones.getId() == null) {
            citaciones.setNum(citacionesService.numCitaciones());
            citaciones.setFechaCrea(new Date());
            citaciones.setUsuarioCrea(userSession.getNameUser());
            citaciones = citacionesService.create(citaciones);
        } else {
            citaciones.setUsuarioMod(userSession.getNameUser());
            citaciones.setFechaMod(new Date());
            citacionesService.edit(citaciones);
        }
        init();
        JsfUtil.executeJS("PF('eventDialog').hide()");
        JsfUtil.addInformationMessage("", "TRANSACCIÓN CORRECTA");
        PrimeFaces.current().ajax().update("mainForm", "frmEvento");

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
        lazyCliente = new LazyModel<>(Cliente.class);
        lazyCliente.getFilterss().put("validado", true);
        lazyCliente.getFilterss().put("estado", true);
    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public CitacionesService getCitacionesService() {
        return citacionesService;
    }

    public void setCitacionesService(CitacionesService citacionesService) {
        this.citacionesService = citacionesService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public List<ScheduleEvent> getScheduleEvents() {
        return scheduleEvents;
    }

    public void setScheduleEvents(List<ScheduleEvent> scheduleEvents) {
        this.scheduleEvents = scheduleEvents;
    }

    public Citaciones getCitaciones() {
        return citaciones;
    }

    public void setCitaciones(Citaciones citaciones) {
        this.citaciones = citaciones;
    }

    public List<CatalogoItem> getListaComisarias() {
        return listaComisarias;
    }

    public void setListaComisarias(List<CatalogoItem> listaComisarias) {
        this.listaComisarias = listaComisarias;
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

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }
//</editor-fold>

}
