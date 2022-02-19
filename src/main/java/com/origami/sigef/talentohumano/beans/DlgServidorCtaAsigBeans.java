/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "servidorCtaAsignadaView")
@ViewScoped
public class DlgServidorCtaAsigBeans implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RolesDePagoService rolesDePagoService;
    @Inject
    private TipoRolService tipoRolService;

    private LazyModel<RolesDePago> listaServidores;
    private List<RolesDePago> listaServ;
    private List<RolesDePago> rolesDataTable;
    private Short anio;
    private Long idRol;
    private String id;
    private String param;
    private TipoRol tipoRol;

    @PostConstruct
    public void init() {
        listaServ = new ArrayList<>();
        rolesDataTable = new ArrayList<>();
        param = JsfUtil.getRequestParameter("ANIO");
        id = JsfUtil.getRequestParameter("ROL");
        tipoRol = tipoRolService.tipoRolParam(Long.parseLong(id));
//        listaServ = rolesDePagoService.findRolesXPeriodo(Short.parseShort(param));
        listaServ = rolesDePagoService.getServidorInDiaLaboradoxTipoRol(tipoRol);
        for (RolesDePago r : listaServ) {
            if (!rolesDataTable.contains(r)) {
                if (TalentoHumano.validarFechaInicio(r.getServidor().getFechaIngreso(), tipoRol)) {
                    rolesDataTable.add(r);
                }
            }
        }
        listaServidores = new LazyModel<>(RolesDePago.class);
        listaServidores.getFilterss().put("estado", true);
        listaServidores.getFilterss().put("periodo", anio);
    }

    public void closePar(RolesDePago r) {
        PrimeFaces.current().dialog().closeDynamic(r);
    }

    public LazyModel<RolesDePago> getListaServidores() {
        return listaServidores;
    }

    public void setListaServidores(LazyModel<RolesDePago> listaServidores) {
        this.listaServidores = listaServidores;
    }

    public List<RolesDePago> getRolesDataTable() {
        return rolesDataTable;
    }

    public void setRolesDataTable(List<RolesDePago> rolesDataTable) {
        this.rolesDataTable = rolesDataTable;
    }

}
