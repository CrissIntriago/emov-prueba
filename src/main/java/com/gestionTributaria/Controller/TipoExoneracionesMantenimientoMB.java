
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.FnExoneracionClase;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class TipoExoneracionesMantenimientoMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(TipoExoneracionesMantenimientoMB.class.getName());

    @Inject
    private ManagerService services;

    private FnExoneracionTipo exoneracionTipo;
    private FnExoneracionClase exoneracionClase;
    private LazyModel<FnExoneracionTipo> exoneracionTipos;
    private LazyModel<FnExoneracionClase> exoneracionClases;
    private List<FnExoneracionClase> exoneracionClaseList;
    private FnResolucionTipo resolucionTipo;
    private List<FnResolucionTipo> resolucionList;

    public List<FnResolucionTipo> getResolucionList() {
        return resolucionList;
    }

    public void setResolucionList(List<FnResolucionTipo> resolucionList) {
        this.resolucionList = resolucionList;
    }

    public FnResolucionTipo getResolucionTipo() {
        return resolucionTipo;
    }

    public void setResolucionTipo(FnResolucionTipo resolucionTipo) {
        this.resolucionTipo = resolucionTipo;
    }

    @PostConstruct
    public void initView() {
        exoneracionTipo = new FnExoneracionTipo();
        exoneracionClase = new FnExoneracionClase();
        cargarTipoExoneraciones();
    }

    public void actualizarTipoExoneracion(FnExoneracionTipo exoneracionTipo) {
        if (exoneracionTipo != null) {
            this.exoneracionTipo = exoneracionTipo;
        } else {
            this.exoneracionTipo = new FnExoneracionTipo();
        }
        JsfUtil.update("frmEdit");
    }

    public void actualizarClaseExoneracion(FnExoneracionClase exoneracionClase) {
        if (exoneracionClase != null) {
            this.exoneracionClase = exoneracionClase;
        } else {
            this.exoneracionClase = new FnExoneracionClase();
        }
        JsfUtil.update("frmEdit");
    }

    public void saveEditTipoExoneracion() {
        if (exoneracionTipo.getId() != null) {
            services.update(exoneracionTipo);
        } else {
            services.save(exoneracionTipo);
        }
        JsfUtil.update("frmMain");
    }

    public void saveEditClaseExoneracion() {
        if (exoneracionClase.getId() != null) {
            services.update(exoneracionClase);
        } else {
            exoneracionClase.setFechaIngreso(new Date());
            services.save(exoneracionClase);
            JsfUtil.update("frmMain");
        }
        JsfUtil.update("frmMain");
    }

    public void cargarTipoExoneraciones() {
        try {
            exoneracionClases = new LazyModel<>(FnExoneracionClase.class);
            exoneracionClaseList = services.findAllEasy("Select c from FnExoneracionClase c");
            exoneracionTipos = new LazyModel<>(FnExoneracionTipo.class);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void editVariable(FnExoneracionTipo exo) {
        try {
            FnExoneracionTipo result = services.find(FnExoneracionTipo.class, exo.getId());
            if (result.getVariable() != null) {
                if (result.getVariable()) {
                    result.setVariable(Boolean.FALSE);
                } else {
                    result.setVariable(Boolean.TRUE);
                }
            } else {
                result.setVariable(Boolean.TRUE);
            }
            services.update(result);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public FnExoneracionTipo getExoneracionTipo() {
        return exoneracionTipo;
    }

    public void setExoneracionTipo(FnExoneracionTipo exoneracionTipo) {
        this.exoneracionTipo = exoneracionTipo;
    }

    public LazyModel<FnExoneracionTipo> getExoneracionTipos() {
        return exoneracionTipos;
    }

    public void setExoneracionTipos(LazyModel<FnExoneracionTipo> exoneracionTipos) {
        this.exoneracionTipos = exoneracionTipos;
    }

    public LazyModel<FnExoneracionClase> getExoneracionClases() {
        return exoneracionClases;
    }

    public void setExoneracionClases(LazyModel<FnExoneracionClase> exoneracionClases) {
        this.exoneracionClases = exoneracionClases;
    }

    public FnExoneracionClase getExoneracionClase() {
        return exoneracionClase;
    }

    public void setExoneracionClase(FnExoneracionClase exoneracionClase) {
        this.exoneracionClase = exoneracionClase;
    }

    public List<FnExoneracionClase> getExoneracionClaseList() {
        return exoneracionClaseList;
    }

    public void setExoneracionClaseList(List<FnExoneracionClase> exoneracionClaseList) {
        this.exoneracionClaseList = exoneracionClaseList;
    }

}
