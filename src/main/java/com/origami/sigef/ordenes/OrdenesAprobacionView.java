/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.transporte.CooperativaVehiculo;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.ordenes.services.OrdenDetService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class OrdenesAprobacionView implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OrdenesAprobacionView.class.getName());

    @Inject
    private UserSession us;
    @Inject
    private OrdenDetService service;

    private CooperativaVehiculo vehi;
    private CooperativaVehiculo vehiSys;
    private Boolean ver = true;

    @PostConstruct
    protected void iniView() {
        try {
            if (!JsfUtil.isAjaxRequest()) {
                if (us.getVarTemp() != null) {
                    vehi = (CooperativaVehiculo) us.getVarTemp();
                    if (vehi.getId() != null) {
                        vehiSys = service.getEntityManager().find(CooperativaVehiculo.class, vehi.getId());
                    } else {
                        vehiSys = new CooperativaVehiculo();
                    }
                } else {

                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void rechazar() {
        System.out.println("Aprobado...");
    }

    public void procesar() {
        Boolean procesado = service.procesarDatos(vehi, vehiSys);
        System.out.println("Aprobado...");
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public CooperativaVehiculo getVehi() {
        return vehi;
    }

    public void setVehi(CooperativaVehiculo vehi) {
        this.vehi = vehi;
    }

    public CooperativaVehiculo getVehiSys() {
        return vehiSys;
    }

    public void setVehiSys(CooperativaVehiculo vehiSys) {
        this.vehiSys = vehiSys;
    }

}
