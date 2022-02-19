/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThVacaciones;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThVacacionesService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thVacacionesView")
@ViewScoped
public class ThVacacionesController implements Serializable {

    @Inject
    private ThVacacionesService thVacacionesService;
    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ServletSession servletSession;

    private ThVacaciones thVacaciones;
    private OpcionBusqueda opcionBusqueda;

    private LazyModel<ThVacaciones> thVacacionesLazy;

    private List<ThVacaciones> thVacacionesAnteriores;
    private List<Short> periodos;
    private List<Servidor> thServidoresList;

    private Boolean view;

    private String identificacion;

    @PostConstruct
    public void init() {
        thVacacionesLazy = new LazyModel<>(ThVacaciones.class);
        thVacacionesLazy.getSorteds().put("idServidor.persona.apellido", "ASC");
        thVacacionesLazy.getFilterss().put("estado", true);
        periodos = thInterfaces.getPeriodos();
        opcionBusqueda = new OpcionBusqueda();
        clear(true);
        updateLazy();
    }

    public void clear(Boolean accion) {
        if (accion) {
            view = Boolean.TRUE;
        }
        identificacion = "";
        thVacaciones = new ThVacaciones();
        thVacaciones.setPeriodo(opcionBusqueda.getAnio());
        thVacacionesAnteriores = new ArrayList<>();
    }

    public void updateLazy() {
        if (thVacaciones.getPeriodo() != null) {
            thVacacionesLazy.getSorteds().put("fechaSalida", "ASC");
            thVacacionesLazy.getFilterss().put("estado", true);
            thVacacionesLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            thVacacionesLazy.setDistinct(false);
        } else {
            thVacacionesLazy = null;
        }
    }

    public void form(ThVacaciones thVacaciones, Boolean view) {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
            return;
        }
        this.view = view;
        if (thVacaciones == null) {
            clear(false);
        } else {
            this.thVacaciones = thVacaciones;
            loadList(false);
        }
        JsfUtil.executeJS("PF('thVacacionesDlg').show()");
        PrimeFaces.current().ajax().update("thVacacionesForm");
    }

    public void findServidor() {
        thVacaciones.setIdServidor(thInterfaces.findByServidor(identificacion));
        if (thVacaciones.getIdServidor() != null) {
            List<ThVacaciones> aux = thVacacionesService.findByNamedQuery("ThVacaciones.findByIdServidorNoAprobado", thVacaciones.getIdServidor());
            if (aux != null && !aux.isEmpty()) {
                thVacaciones.setIdServidor(null);
                JsfUtil.addWarningMessage("AVISO!!!!", "El servidor ya mantiene un registro sin aprobar");
            } else {
                loadList(true);
                JsfUtil.addSuccessMessage("INFO!!!", "Datos del servidor cargado");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se encuentra ningun servidor público con el número de identificación ingresado");
        }

    }

    public void loadList(Boolean accion) {
        if (accion) {
            thVacacionesAnteriores = thVacacionesService.findByNamedQuery("ThVacaciones.findByIdServidor", thVacaciones.getIdServidor());
        } else {
            thVacacionesAnteriores = thVacacionesService.findByNamedQuery("ThVacaciones.findByIdServidorAnteriores", thVacaciones.getIdServidor(), thVacaciones.getId());
        }
        int diasAcumulado = 0;
        if (thVacacionesAnteriores != null) {
            if (!thVacacionesAnteriores.isEmpty()) {
                for (ThVacaciones item : thVacacionesAnteriores) {
                    diasAcumulado = diasAcumulado + item.getDias();
                }
            }
        }
        thVacaciones.setDiasFaltantes(diasAcumulado);
        thVacaciones.setDiasDisponibles(thVacaciones.getIdServidor().getDiasAcumuladosVacaciones() - thVacaciones.getDiasFaltantes());
    }

    public void calcularDias() {
        if (thVacaciones.getFechaSalida() != null && thVacaciones.getFechaReingreso() != null) {
            if (!(thVacaciones.getFechaSalida().before(thVacaciones.getFechaReingreso()))) {
                JsfUtil.addWarningMessage("AVISO!!!", "La fecha de salida no debe ser mayor a la fecha de reingreso");
                thVacaciones.setDias(0);
                return;
            } else {
                thVacaciones.setDias(Utils.restarFechas(thVacaciones.getFechaSalida(), thVacaciones.getFechaReingreso()).intValue());
            }
        }
        PrimeFaces.current().ajax().update("panelDias");
    }

    public void save() {
        Boolean edit = thVacaciones.getId() != null;
        if (thVacaciones.getIdServidor() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Se debe seleccionar un servidor");
            return;
        }
        if (!(thVacaciones.getFechaSalida().before(thVacaciones.getFechaReingreso()))) {
            JsfUtil.addWarningMessage("AVISO!!!", "La fecha de salida no debe ser mayor a la fecha de reingreso");
            return;
        }
        Date fechaTemp = new Date();
        if (!thVacacionesAnteriores.isEmpty()) {
            fechaTemp = thVacacionesAnteriores.get(thVacacionesAnteriores.size() - 1).getFechaReingreso();
            if (!fechaTemp.before(thVacaciones.getFechaSalida())) {
                JsfUtil.addWarningMessage("AVISO!!!", "La fecha de salida debe mayor al de la ultima fecha de reingreso");
                return;
            }
        }
        if (thVacaciones.getIdServidor().getDiasAcumuladosVacaciones() > 0) {
            if (thVacaciones.getDiasFaltantes() > 0) {
                int dias = thVacaciones.getIdServidor().getDiasAcumuladosVacaciones() - thVacaciones.getDiasFaltantes();
                if (dias < thVacaciones.getDias()) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Los dias ingresados son superior a los que tiene pendiente");
                    return;
                }
            }
        }
        if (edit) {
            thVacaciones.setFechaModificacion(new Date());
            thVacaciones.setUsuarioModificacion(thInterfaces.getUser());
            thVacacionesService.edit(thVacaciones);
        } else {
            thVacaciones.setFechaCreacion(new Date());
            thVacaciones.setUsuarioCreacion(thInterfaces.getUser());
            thVacacionesService.create(thVacaciones);
        }
        JsfUtil.executeJS("PF('thVacacionesDlg').hide()");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        clear(false);
        PrimeFaces.current().ajax().update("thVacacionesForm");
        PrimeFaces.current().ajax().update("thVacacionesTable");
    }

    public void detele(ThVacaciones thVacaciones) {
        thVacaciones.setEstado(Boolean.FALSE);
        thVacaciones.setFechaModificacion(new Date());
        thVacaciones.setUsuarioModificacion(thInterfaces.getUser());
        thVacacionesService.edit(thVacaciones);
        JsfUtil.addSuccessMessage("INFO!!!", "Eliminado correctamente");
    }

    public void aprobado(ThVacaciones thVacaciones) {
        thVacaciones.setAprobado(Boolean.TRUE);
        thVacaciones.setFechaModificacion(new Date());
        thVacaciones.setUsuarioModificacion(thInterfaces.getUser());
        thVacacionesService.edit(thVacaciones);
        JsfUtil.addSuccessMessage("INFO!!!", "Aprobado correctamente");
    }

    public void printReport() {
        thServidoresList = thVacacionesService.getServidores();
        JsfUtil.executeJS("PF('thVacacionesDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("thVacacionesDocumentoTable");
    }

    public void print(String tipoDocumento, Servidor servidor) {
        servletSession.addParametro("idservidor", servidor.getId());
        servletSession.setNombreReporte("vacaciones");
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public ThVacaciones getThVacaciones() {
        return thVacaciones;
    }

    public void setThVacaciones(ThVacaciones thVacaciones) {
        this.thVacaciones = thVacaciones;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ThVacaciones> getThVacacionesLazy() {
        return thVacacionesLazy;
    }

    public void setThVacacionesLazy(LazyModel<ThVacaciones> thVacacionesLazy) {
        this.thVacacionesLazy = thVacacionesLazy;
    }

    public List<ThVacaciones> getThVacacionesAnteriores() {
        return thVacacionesAnteriores;
    }

    public void setThVacacionesAnteriores(List<ThVacaciones> thVacacionesAnteriores) {
        this.thVacacionesAnteriores = thVacacionesAnteriores;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<Servidor> getThServidoresList() {
        return thServidoresList;
    }

    public void setThServidoresList(List<Servidor> thServidoresList) {
        this.thServidoresList = thServidoresList;
    }

}
