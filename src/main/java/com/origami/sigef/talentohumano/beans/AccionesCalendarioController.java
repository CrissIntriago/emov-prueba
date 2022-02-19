/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.AccionesCalendario;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.Vacaciones;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.AccionesCalendarioService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named(value = "calendarioView")
@ViewScoped
public class AccionesCalendarioController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private AccionesCalendarioService accionesCalendarioService;
    @Inject
    private UserSession userSession;

    private AccionesCalendario accionesCalendario;
    private ScheduleModel model;
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event;
    private List<AccionesCalendario> accionesList;
    private List<AccionesCalendario> listaServidor;
    private Boolean isAllDay;
    private String localTime;
    private String pattern;
    private String style;
    private Vacaciones vacaciones;
    private Date fechaParam;

    @PostConstruct
    public void init() {
        pattern = "";
        style = "";
        localTime = getLocalTimeZone();
        fechaParam = new Date();
        vacaciones = new Vacaciones();
        event = new DefaultScheduleEvent();
        model = new DefaultScheduleModel();
        accionesCalendario = new AccionesCalendario();
        accionesList = new ArrayList<>();
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                accionesList = accionesCalendarioService.getAccionesCalendario(start, end);
                accionesList.forEach((c) -> {
                    DefaultScheduleEvent event = new DefaultScheduleEvent();
                    event.setTitle(c.getTitulo());
                    event.setDescription(c.getDescripcion());
                    event.setStartDate(c.getInicioDate());
                    event.setEndDate(c.getFinDate());
                    event.setAllDay(c.getAllDay());
                    event.setData(c);
                    event.setStyleClass("ev1");
                    addEvent(event);
                });
            }
        };
        if (!(userSession.getVarTemp() instanceof Vacaciones)) {
            userSession.setVarTemp(null);
        }
        if (userSession.getVarTemp() != null) {
            if (userSession.getVarTemp() instanceof Vacaciones) {
                vacaciones = (Vacaciones) userSession.getVarTemp();
            } else {
                userSession.setVarTemp(null);
            }
            listaServidor = accionesCalendarioService.getAccionesCalendarioBuscar(vacaciones.getServidor(), vacaciones.getFechaDesde(), vacaciones.getFechaHasta());
            if (!getBuscarServidor(vacaciones.getServidor())) {
                DefaultScheduleEvent eventVaca = new DefaultScheduleEvent();
                eventVaca.setTitle("Vacaciones de Personal");
                eventVaca.setDescription("Registro del Servidor " + vacaciones.getServidor().getPersona().getNombreCompleto() + "");
                eventVaca.setStartDate(vacaciones.getFechaDesde());
                eventVaca.setEndDate(vacaciones.getFechaHasta());
                setIsAllDay(Boolean.TRUE);
                accionesCalendario.setTitulo(eventVaca.getTitle());
                accionesCalendario.setDescripcion(eventVaca.getDescription());
                accionesCalendario.setInicioDate(eventVaca.getStartDate());
                accionesCalendario.setFinDate(eventVaca.getEndDate());
                accionesCalendario.setEstado(Boolean.TRUE);
                accionesCalendario.setAllDay(getIsAllDay());
                accionesCalendario.setServidor(vacaciones.getServidor());
                accionesCalendario = accionesCalendarioService.create(accionesCalendario);
                model.addEvent(eventVaca);
                fechaParam = vacaciones.getFechaDesde();
                PrimeFaces.current().executeScript("PF('myschedule').update()");
                PrimeFaces.current().ajax().update("messages");
                vacaciones = new Vacaciones();
                accionesCalendario = new AccionesCalendario();
            }
        } else {
            fechaParam = new Date();
        }
    }

    private boolean getBuscarServidor(Servidor s) {
        return listaServidor.stream().anyMatch((a) -> (a.getServidor().equals(s)));
    }

    public void addEvent() {
        Boolean edit = event.getId() != null;
        if (!edit) {
            if (event.getTitle().isEmpty() || event.getStartDate() == null || event.getEndDate() == null) {
                JsfUtil.addSuccessMessage("Evento Calendario", "Ingrese los campos requeridos");
                PrimeFaces.current().ajax().update("messages");
                return;
            }
            setAccionesCalendarioByEvent();
            accionesCalendario.setEstado(Boolean.TRUE);
            accionesCalendario = accionesCalendarioService.create(accionesCalendario);
            model.addEvent(event);
        } else {
            setAccionesCalendarioByEvent();
            accionesCalendarioService.edit(accionesCalendario);
            model.updateEvent(event);
        }
        PrimeFaces.current().executeScript("PF('eventDialog').hide()");
        PrimeFaces.current().executeScript("PF('myschedule').update()");
        JsfUtil.addSuccessMessage("Evento Calendario", event.getTitle() + (edit ? " editado" : " registrado") + " con éxito.");
        PrimeFaces.current().ajax().update("messages");
        newObject();
    }

    public void setAccionesCalendarioByEvent() {
        accionesCalendario.setTitulo(event.getTitle());
        accionesCalendario.setDescripcion(event.getDescription());
        accionesCalendario.setInicioDate(event.getStartDate());
        accionesCalendario.setFinDate(event.getEndDate());
        accionesCalendario.setAllDay(isAllDay);
    }

    public void newObject() {
        event = new DefaultScheduleEvent();
        accionesCalendario = new AccionesCalendario();
    }

    public void deleteEvent() {
        if (event.getId() == null && event.getData() == null) {
            JsfUtil.addSuccessMessage("Evento Calendario", "No hay evento creado.");
            PrimeFaces.current().ajax().update("messages");
            return;
        }
        accionesCalendario = (AccionesCalendario) event.getData();
        accionesCalendarioService.remove(accionesCalendario);
        model.deleteEvent(event);
        PrimeFaces.current().executeScript("PF('eventDialog').hide()");
        PrimeFaces.current().executeScript("PF('myschedule').update()");
        PrimeFaces.current().ajax().update("output");
        JsfUtil.addSuccessMessage("Evento Calendario", event.getTitle() + " Eliminado con éxito.");
        PrimeFaces.current().ajax().update("messages");
        newObject();

    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        setIsAllDay(event.isAllDay());
        if (isAllDay) {
            setPattern("yyyy/MM/dd");
        } else {
            setPattern("yyyy/MM/dd HH:mm");
        }
        accionesCalendario = (AccionesCalendario) event.getData();
        PrimeFaces.current().executeScript("PF('eventDialog').show()");
        PrimeFaces.current().ajax().update("output");
        PrimeFaces.current().ajax().update("formdialog");
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        setIsAllDay(Boolean.TRUE);
        setPattern("yyyy/MM/dd");
        PrimeFaces.current().executeScript("PF('eventDialog').show()");
        PrimeFaces.current().ajax().update("output");
    }

    public void onEventMove(ScheduleEntryMoveEvent selecEvent) {
        AccionesCalendario accion = (AccionesCalendario) selecEvent.getScheduleEvent().getData();
        accion.setInicioDate(selecEvent.getScheduleEvent().getStartDate());
        accion.setFinDate(selecEvent.getScheduleEvent().getEndDate());
        accionesCalendarioService.edit(accion);
        JsfUtil.addInformationMessage("Acciones de Calendario", accion.getTitulo() + " Movido,"
                + " Fecha " + getCurrentDateFormat("yyyy/MM/dd HH:mm", accion.getInicioDate()));
        PrimeFaces.current().ajax().update("messages");
    }

    public void onEventResize(ScheduleEntryResizeEvent selectEvent) {
        AccionesCalendario accion = (AccionesCalendario) selectEvent.getScheduleEvent().getData();
        accion.setInicioDate(selectEvent.getScheduleEvent().getStartDate());
        accion.setFinDate(selectEvent.getScheduleEvent().getEndDate());
        accionesCalendarioService.edit(accion);
        JsfUtil.addInformationMessage("Acciones de Calendario", accion.getTitulo() + " Horas modificadas "
                + "desde " + getCurrentDateFormat("HH:mm", accion.getInicioDate()) + " hasta " + getCurrentDateFormat("HH:mm", accion.getFinDate()));
        PrimeFaces.current().ajax().update("messages");
    }

    public String getLocalTimeZone() {
        return FacesContext.getCurrentInstance().getExternalContext().getInitParameter("FIXED_LOCAL_TIMEZONE");
    }

    public static String getCurrentDateFormat(String format, Date currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(currentDate);
    }

    public void actualizarPatter() {
        if (isAllDay) {
            setPattern("yyyy/MM/dd");
        } else {
            setPattern("yyyy/MM/dd HH:mm");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter y Setters">
    public ScheduleModel getModel() {
        return model;
    }

    public void setModel(ScheduleModel model) {
        this.model = model;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public AccionesCalendario getAccionesCalendario() {
        return accionesCalendario;
    }

    public void setAccionesCalendario(AccionesCalendario accionesCalendario) {
        this.accionesCalendario = accionesCalendario;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public Boolean getIsAllDay() {
        return isAllDay;
    }

    public void setIsAllDay(Boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Date getFechaParam() {
        return fechaParam;
    }

    public void setFechaParam(Date fechaParam) {
        this.fechaParam = fechaParam;
    }
    //</editor-fold>
}
