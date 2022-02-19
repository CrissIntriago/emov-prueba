/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Controller.FnResolucionTipo;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class MantenimientoResolucion implements Serializable {

    private List<FnResolucionTipo> resolucionT;
    private String descripcion;
    private String abrev;

    public String getAbrev() {
        return abrev;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<FnResolucionTipo> getResolucionT() {
        return resolucionT;
    }

    public void setResolucionT(List<FnResolucionTipo> resolucionT) {
        this.resolucionT = resolucionT;
    }

    @Inject
    private ManagerService services;

    @PostConstruct
    public void initView() {
        try {
            resolucionT = new ArrayList<>();
            resolucionT = (List<FnResolucionTipo>) services.findAllQuery("SELECT r FROM FnResolucionTipo r", null);
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.getMessage());
        }

    }

    public void guardarTipoExoneracion() {

        FnResolucionTipo reso = null;
        reso = new FnResolucionTipo();
        Boolean existe = Boolean.FALSE;
        try {
            reso.setAbreviatura(abrev);
            reso.setDescripcion(descripcion);
            reso.setCodigo("GADMCD-DGF-SR-" + abrev);

//        existe =services.existeAbre(reso);
            if (resolucionT.contains(reso)) {
                JsfUtil.addWarningMessage("No se puede guardar", "Ya existe");
                return;
            } else {
                services.update(reso);
                
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
