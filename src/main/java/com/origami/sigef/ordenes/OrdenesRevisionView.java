/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.OrdenDet;
import com.origami.sigef.common.entities.OrdenTrabajo;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.entities.transporte.CooperativaVehiculo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.ordenes.services.OrdenDetService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
public class OrdenesRevisionView implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OrdenesRevisionView.class.getName());

    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private FileUploadDoc fileSUploadDoc;
    @Inject
    private OrdenDetService service;

    private List<Usuarios> usuarios;
    private LazyModel<OrdenDet> ordenesLazy;
    private OrdenDet orden;
    private Cooperativa coop;
    private Boolean ver = true;

    @PostConstruct
    protected void iniView() {
        try {
            if (!JsfUtil.isAjaxRequest()) {
                ordenesLazy = new LazyModel<>(OrdenDet.class);
                ordenesLazy.getSorteds().put("orden.numOrden", "DESC");
                ordenesLazy.setDistinct(false);
                orden = new OrdenDet();
                usuarios = this.usuarioService.findAllOrderActivos();
                coop = new Cooperativa();
                coop.setEmpresa(new Cliente());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void revisar(OrdenDet det) {
        try {
            this.orden = det;
            JsonUtil js = new JsonUtil();
            coop = (Cooperativa) js.fromJson(det.getDatoActulizado(), Cooperativa.class);
            if (coop != null) {
                if (Utils.isNotEmpty(coop.getCooperativaVehiculoList())) {
                    for (CooperativaVehiculo cv : coop.getCooperativaVehiculoList()) {
                        cv.setRevisado(this.service.checkRevisado(cv));
                    }
                }
            }
            JsfUtil.executeJS("PF('dlgOrden').show()");
            JsfUtil.update("frmOrden");
        } catch (IOException ex) {
            Logger.getLogger(OrdenesRevisionView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verCambios(CooperativaVehiculo vehi) {
        us.setVarTemp(vehi);
        JsfUtil.redirectNewTab(JsfUtil.getHostContextUrl() + "transporte/ordenes/aprobacion");
    }

    public void eliminar(OrdenDet det) {
        try {
            det.setEstadoDet("IN");
            this.service.edit(det);
            JsfUtil.update("frmMain");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void cerrarDialogo() {
        JsfUtil.executeJS("PF('dlgOrden').hide()");
        ver = true;
    }

    public void reporte() {

    }

    public void reporteIndividual(OrdenTrabajo orden) {

    }

    public LazyModel<OrdenDet> getOrdenesLazy() {
        return ordenesLazy;
    }

    public void setOrdenesLazy(LazyModel<OrdenDet> ordenesLazy) {
        this.ordenesLazy = ordenesLazy;
    }

    public OrdenDet getOrden() {
        return orden;
    }

    public void setOrden(OrdenDet orden) {
        this.orden = orden;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public List<Usuarios> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }

    public Cooperativa getCoop() {
        return coop;
    }

    public void setCoop(Cooperativa coop) {
        this.coop = coop;
    }

}
