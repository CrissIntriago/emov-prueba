/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ReclasificacionCuentas;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.ReclasificacionCuentasService;
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
@Named(value = "traspasoCuentasView")
@ViewScoped
public class TraspasoCuentaController implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ReclasificacionCuentasService reclasificacionCuentasService;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private MasterCatalogoService masterCatalogoService;

    private OpcionBusqueda opcionBusqueda;
    private Boolean btnTraspaso;
    private List<MasterCatalogo> periodos;
    private List<ReclasificacionCuentas> reclasificacionList;

    private LazyModel<ReclasificacionCuentas> reclasificacioCuentasLazy;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        if (periodos != null) {
            if (!periodos.isEmpty()) {
                short anioMaximo = (short) (periodos.size() - 1);
                opcionBusqueda.setAnio(periodos.get(anioMaximo).getAnio());
                opcionBusqueda.setAnioSiguiente((short) (opcionBusqueda.getAnio().intValue() + 1));
            }
        }
        this.btnTraspaso = Boolean.FALSE;
    }

    public void actualizarGridConsulta() {
        if (opcionBusqueda.getAnio() != null) {
            opcionBusqueda.setAnioSiguiente((short) (opcionBusqueda.getAnio().intValue() + 1));
        } else {
            opcionBusqueda.setAnioSiguiente(null);
        }
        PrimeFaces.current().ajax().update("fieldMenu");
    }

    public void consultar() {
        if (opcionBusqueda.getAnio() != null && opcionBusqueda.getAnioSiguiente() != null) {
            if (reclasificacionCuentasService.getReclasificacionesTranspasadas(opcionBusqueda.getAnio())) {
                btnTraspaso = Boolean.FALSE;
            } else {
                btnTraspaso = Boolean.TRUE;
            }
            actualizar();
        }
        PrimeFaces.current().ajax().update("traspasarTableView");
        PrimeFaces.current().ajax().update("formTraspaso");
    }

    public void traspasar() {
        reclasificacionList = reclasificacionCuentasService.getReclasificacionesGuardadas(opcionBusqueda.getAnio());
        for (ReclasificacionCuentas reclasificar : reclasificacionList) {

            /*Editar el catalogo de cuenta contable*/
            if (reclasificar.getCuentaContableNueva() != null) {
                reclasificar.getCuentaContableNueva().setSaldoInicialDebe(reclasificar.getSaldoDebe());
                reclasificar.getCuentaContableNueva().setSaldoInicialHaber(reclasificar.getSaldoHaber());
                cuentaContableService.edit(reclasificar.getCuentaContableNueva());
            } else {
                reclasificacionCuentasService.actualizarCuenta(reclasificar, opcionBusqueda.getAnioSiguiente());
            }
            reclasificar.setFechaModificacion(new Date());
            reclasificar.setUsuarioModificacion(userSession.getNameUser());
            reclasificar.setTraspaso(Boolean.TRUE);
            reclasificacionCuentasService.edit(reclasificar);
        }
        btnTraspaso = Boolean.FALSE;
        JsfUtil.addInformationMessage("INFO!!", "Se han traspasado correctamente los saldos a las cuentas contables correspondiente");
        PrimeFaces.current().ajax().update("traspasarTableView");
        PrimeFaces.current().ajax().update("formTraspaso");
    }

    private void actualizar() {
        this.reclasificacioCuentasLazy = new LazyModel<>(ReclasificacionCuentas.class);
        this.reclasificacioCuentasLazy.getSorteds().put("cuentaContableAnterior.codigo", "ASC");
        this.reclasificacioCuentasLazy.getFilterss().put("cuentaContableAnterior.periodo", opcionBusqueda.getAnio());
        this.reclasificacioCuentasLazy.setDistinct(false);
    }

    public ReclasificacionCuentasService getReclasificacionCuentasService() {
        return reclasificacionCuentasService;
    }

    public void setReclasificacionCuentasService(ReclasificacionCuentasService reclasificacionCuentasService) {
        this.reclasificacionCuentasService = reclasificacionCuentasService;
    }

    public CuentaContableService getCuentaContableService() {
        return cuentaContableService;
    }

    public void setCuentaContableService(CuentaContableService cuentaContableService) {
        this.cuentaContableService = cuentaContableService;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public Boolean getBtnTraspaso() {
        return btnTraspaso;
    }

    public void setBtnTraspaso(Boolean btnTraspaso) {
        this.btnTraspaso = btnTraspaso;
    }

    public LazyModel<ReclasificacionCuentas> getReclasificacioCuentasLazy() {
        return reclasificacioCuentasLazy;
    }

    public void setReclasificacioCuentasLazy(LazyModel<ReclasificacionCuentas> reclasificacioCuentasLazy) {
        this.reclasificacioCuentasLazy = reclasificacioCuentasLazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

}
