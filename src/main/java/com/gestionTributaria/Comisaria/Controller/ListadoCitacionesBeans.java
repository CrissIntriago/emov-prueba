/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Comisaria.Entities.Citaciones;
import com.gestionTributaria.Comisaria.Service.CitacionesService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "listadoCitacionesBeans")
@ViewScoped
public class ListadoCitacionesBeans implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private CitacionesService citacionesService;
    @Inject
    private UserSession userSession;

    private LazyModel<Citaciones> lazyCitaciones;
    private Citaciones citacionComparecencia;
    private Date desde, hasta;
    private Short periodo;
    private String tipoEstado;

    @PostConstruct
    public void init() {
        lazyCitaciones = new LazyModel<>(Citaciones.class);
        lazyCitaciones.getFilterss().put("usuarioCrea", userSession.getNameUser());
        Short anio_sistema = Utils.getAnio(new Date()).shortValue();
        String fechaInicial = anio_sistema.toString() + "-01-01";
        periodo = anio_sistema;
        Calendar calendar = Calendar.getInstance();
        calendar.set(periodo, 0, 1);
        desde = calendar.getTime();
        hasta = new Date();
        tipoEstado = "";
    }

    public void dialogCitacionesComparecencia(Citaciones c) {
        citacionComparecencia = c;
        JsfUtil.update("idFormCompadecencia");
        JsfUtil.executeJS("PF('dialogCompadecencia').show()");
    }

    public void saveCompadecencia() {
        if (citacionComparecencia.getAcuerdo().isEmpty() || citacionComparecencia.getAcuerdo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe agregar el acuerdo");
            return;
        }
        if (citacionComparecencia.getAcuerdo().isEmpty() || citacionComparecencia.getAcuerdo() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe agregar el acuerdo");
            return;
        }
        citacionComparecencia.setUsuarioMod(userSession.getNameUser());
        citacionComparecencia.setFechaMod(new Date());
        citacionesService.edit(citacionComparecencia);
    }

    public void generarReporte(Boolean tipo) {

        if (tipo) {
        } else {
            servletSession.setContentType("xlsx");
        }
        servletSession.addParametro("desde", desde);
        servletSession.addParametro("hasta", hasta);
        servletSession.addParametro("tipoEstado", tipoEstado);

        servletSession.setNombreReporte("citaciones");
        servletSession.setNombreSubCarpeta("GestionTributatia/comisaria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModel<Citaciones> getLazyCitaciones() {
        return lazyCitaciones;
    }

    public void setLazyCitaciones(LazyModel<Citaciones> lazyCitaciones) {
        this.lazyCitaciones = lazyCitaciones;
    }

    public Citaciones getCitacionComparecencia() {
        return citacionComparecencia;
    }

    public void setCitacionComparecencia(Citaciones citacionComparecencia) {
        this.citacionComparecencia = citacionComparecencia;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }
//</editor-fold>

}
