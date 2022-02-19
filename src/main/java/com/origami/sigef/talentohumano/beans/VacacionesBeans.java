/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.Vacaciones;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.VacacionesService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Origami
 */
@Named(value = "vacacionesBeans")
@ViewScoped
public class VacacionesBeans implements Serializable {

    private Vacaciones vacaciones;
    private LazyModel<Vacaciones> lazyVacaciones;
    private short periodo;
    private String cedula;
    private Servidor servidor;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private DistributivoEscalaService distributivoService;
    @Inject
    private VacacionesService vacacionesservice;

    @PostConstruct
    public void initView() {
        this.cedula = "";
        vacaciones = new Vacaciones();
        vacaciones.setServidor(new Servidor());
        vacaciones.setAprobado(new Servidor());
        vacaciones.getServidor().setPersona(new Cliente());
        vacaciones.getAprobado().setPersona(new Cliente());
        vacaciones.getServidor().setDistributivo(new Distributivo());
        vacaciones.getAprobado().setDistributivo(new Distributivo());
        vacaciones.getServidor().getDistributivo().setCargo(new Cargo());
        vacaciones.getAprobado().getDistributivo().setCargo(new Cargo());
        periodo = Utils.getAnio(new Date()).shortValue();
        lazyVacaciones = new LazyModel<>(Vacaciones.class);
        lazyVacaciones.getFilterss().put("estado", true);
    }

    public void buscarServidor(boolean buscar) {
        Servidor serv = distributivoService.getServidorAnio(periodo, this.cedula);
        if (serv != null && buscar) {
            this.vacaciones.setServidor(serv);
        } else if (serv == null || !buscar) {
            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "830", "400");
        }

    }

    public String redirect(Vacaciones vaca) {
        if (vaca.getId() != null) {
            userSession.setVarTemp(vaca);
            return "../talentoHumano/controlPersonal/_evento_calendario.xhtml?faces-redirect=true";
        }
        return "";
    }

    public void selectData(SelectEvent evt) {
        vacaciones.setServidor((Servidor) evt.getObject());
        if (vacaciones.getServidor() != null) {
            cedula = vacaciones.getServidor().getPersona().getIdentificacion();
        }
    }

    public void selectDataServ(SelectEvent evt) {
        vacaciones.setAprobado((Servidor) evt.getObject());
        if (vacaciones.getServidor() != null) {
            cedula = vacaciones.getAprobado().getPersona().getIdentificacion();
        }
    }

    public void guardar() {
        boolean edit = vacaciones.getId() != null;
        if (vacaciones.getServidor().getId() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar un Servidor");
            return;
        }
        if (vacaciones.getDias() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar una Cantidad de dias");
            return;
        }
        if (vacaciones.getAprobado().getId() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar un Supervisor");
            return;
        }
        if (vacaciones.getFechaDesde().compareTo(vacaciones.getFechaHasta()) == 0) {
            JsfUtil.addWarningMessage("Informacíon", "Las fechas no pueden ser iguales");
            return;
        }
        if (vacaciones.getFechaDesde().compareTo(vacaciones.getFechaHasta()) > 0) {
            JsfUtil.addWarningMessage("Informacíon", "La fecha de inicio no pude ser mayor que la fecha Hasta");
            return;
        }
        if (vacaciones.getFechaHasta().compareTo(vacaciones.getFechaDesde()) < 0) {
            JsfUtil.addWarningMessage("Informacíon", "La fecha desde no puede ser menor que la fecha de inicio");
            return;
        }
        if (vacaciones.getDias().intValue() > TalentoHumano.diasDiferencia(vacaciones.getFechaDesde(), vacaciones.getFechaHasta())
                || vacaciones.getDias().intValue() < TalentoHumano.diasDiferencia(vacaciones.getFechaDesde(), vacaciones.getFechaHasta())) {
            JsfUtil.addWarningMessage("información", "Los Días ingresados no concuerdan con los rango de fechas ingresadas");
            return;
        }
        if (vacaciones.getDias().intValue() > 30 && vacaciones.getServidor().getDistributivo().getRegimen().getCodigo().equals("LOSEP")) {
            JsfUtil.addWarningMessage("información", "Los Días ingresados no pueden ser mayor a los 30 Días");
            return;
        }
        if (vacaciones.getDias().intValue() > 60 && vacaciones.getServidor().getDistributivo().getRegimen().getCodigo().equals("CT")) {
            JsfUtil.addWarningMessage("información", "Los Días ingresados no pueden ser mayor a los 30 Días");
            return;
        }

        if (TalentoHumano.calcularMesesAFecha(vacaciones.getServidor().getFechaIngreso(), vacaciones.getFechaDesde()) < 11
                && vacaciones.getServidor().getDistributivo().getRegimen().getCodigo().equals("LOSEP")) {
            JsfUtil.addWarningMessage("información", "El servidor no cuenta con los meses necesarios para vacaciones");
            return;
        }
        if (TalentoHumano.calcularMesesAFecha(vacaciones.getServidor().getFechaIngreso(), vacaciones.getFechaDesde()) < 12
                && vacaciones.getServidor().getDistributivo().getRegimen().getCodigo().equals("CT")) {
            JsfUtil.addWarningMessage("información", "El servidor no cuenta con los meses necesarios para vacaciones");
            return;
        }
        List<Vacaciones> listVacaciones;
        listVacaciones = vacacionesservice.getVacacionesXservidor(vacaciones.getServidor());
        if (!listVacaciones.isEmpty()) {
            for (Vacaciones item : listVacaciones) {
                if ((vacaciones.getFechaDesde().after(item.getFechaDesde()) && vacaciones.getFechaDesde().before(item.getFechaHasta()))
                        || (vacaciones.getFechaHasta().after(item.getFechaDesde()) && vacaciones.getFechaHasta().before(item.getFechaHasta()))) {
                    JsfUtil.addWarningMessage("información", "Este período tiene días existente en otro Período de Vacaciones. ");
                    return;
                }
            }
        }
        if (edit) {
            vacaciones.setFechaModificacion(new Date());
            vacaciones.setUsuarioModifica(userSession.getNameUser());
            vacacionesservice.edit(vacaciones);
        } else {
            vacaciones.setFechaCreacion(new Date());
            vacaciones.setUsuarioCreacion(userSession.getNameUser());
            vacaciones.setPeriodo(periodo);
            vacaciones = vacacionesservice.create(vacaciones);
        }
        vacaciones = new Vacaciones();
        this.cedula = "";
        JsfUtil.addInformationMessage("Vacaciones del Personal", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void calcularFechaHasta() {
        try {
            if (vacaciones.getFechaDesde() != null && vacaciones.getDias() != null) {
                vacaciones.setFechaHasta(Utils.sumarRestarDiasFecha(vacaciones.getFechaDesde(), vacaciones.getDias()));
            }
        } catch (Exception e) {
            Logger.getLogger(VacacionesBeans.class.getName()).log(Level.SEVERE, cedula, e);
        }
    }

    public void editar(Vacaciones v) {
        this.vacaciones = v;
        this.cedula = v.getServidor().getPersona().getIdentificacion();
    }

    public void cancelar() {
        vacaciones = new Vacaciones();
        this.cedula = "";
    }

    public void eliminar(Vacaciones v) {
        JsfUtil.addSuccessMessage("Servidor Público", v.getServidor().getPersona().getNombre() + " " + v.getServidor().getPersona().getApellido() + " Eliminada con Exito");
        v.setEstado(Boolean.FALSE);
        vacacionesservice.edit(v);

    }

    public Vacaciones getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(Vacaciones vacaciones) {
        this.vacaciones = vacaciones;
    }

    public LazyModel<Vacaciones> getLazyVacaciones() {
        return lazyVacaciones;
    }

    public void setLazyVacaciones(LazyModel<Vacaciones> lazyVacaciones) {
        this.lazyVacaciones = lazyVacaciones;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

}
