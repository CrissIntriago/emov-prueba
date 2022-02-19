/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.contabilidad.services.ContCuentaEntidadService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contCuentaEntidadView")
@ViewScoped
public class ContCuentaEntidadController implements Serializable {

    @Inject
    private ContCuentaEntidadService contCuentaEntidadService;

    private ContCuentaEntidad contCuentaEntidad;

    private LazyModel<ContCuentaEntidad> contCuentaEntidadLazyModel;
    private LazyModel<Banco> bancoLazyModel;
    private LazyModel<ContCuentas> contCuentasLazyModel;

    private boolean view, tipoCuenta;

    @PostConstruct
    public void init() {
        closeForm();
        contCuentaEntidadLazyModel = new LazyModel<>(ContCuentaEntidad.class);
        contCuentaEntidadLazyModel.getSorteds().put("id", "ASC");
        contCuentaEntidadLazyModel.getFilterss().put("estado", true);

        bancoLazyModel = new LazyModel<>(Banco.class);
        bancoLazyModel.getSorteds().put("nombreBanco", "ASC");
        bancoLazyModel.getFilterss().put("estado", true);

        contCuentasLazyModel = new LazyModel<>(ContCuentas.class);
        contCuentasLazyModel.getSorteds().put("codigo", "ASC");
        contCuentasLazyModel.getFilterss().put("estado", true);
        contCuentasLazyModel.getFilterss().put("activo", true);
        contCuentasLazyModel.getFilterss().put("movimiento", true);
    }

    public void closeForm() {
        contCuentaEntidad = new ContCuentaEntidad();
    }

    public void form(ContCuentaEntidad object, Boolean view) {
        closeForm();
        this.view = view;
        if (object != null) {
            this.contCuentaEntidad = object;
        }
        JsfUtil.executeJS("PF('contCuentaEntidadDlg').show()");
        PrimeFaces.current().ajax().update("contCuentaEntidadForm");
    }

    public void save() {
        boolean edit = contCuentaEntidad.getId() != null;
        if (contCuentaEntidad.getIdCuentaPatrimonial() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una cta. contable de patrimonio");
            return;
        }
        if (contCuentaEntidad.getIdCuentaMovimiento() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una cta. contable de movimiento");
            return;
        }
        if (contCuentaEntidad.getIdBanco() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un banco");
            return;
        }
        if (contCuentaEntidad.getNombre() == null || contCuentaEntidad.getNombre().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un nombre de la cta. bancaria");
            return;
        }
        if (contCuentaEntidad.getNumeroCuenta() == null || contCuentaEntidad.getNumeroCuenta().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el numero de la cta. bancaria");
            return;
        }
        if (contCuentaEntidad.getTipoCuenta() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar el tipo de cta. bancaria");
            return;
        }
        if (edit) {
            contCuentaEntidadService.edit(contCuentaEntidad);
        } else {
            contCuentaEntidadService.create(contCuentaEntidad);
        }
        JsfUtil.executeJS("PF('contCuentaEntidadDlg').hide()");
        PrimeFaces.current().ajax().update("contCuentaEntidadForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void delete(ContCuentaEntidad contCuentaEntidad) {
        contCuentaEntidad.setEstado(false);
        contCuentaEntidadService.edit(contCuentaEntidad);
        JsfUtil.addSuccessMessage("AVISO!!!", "Se ha eliminado correctamente");
    }

    public void openDlgCuenta(Boolean accion) {
        this.tipoCuenta = accion;
        JsfUtil.executeJS("PF('contCuentasDlg').show()");
        PrimeFaces.current().ajax().update("contCuentasForm");
    }

    public void closeDlgCuenta(ContCuentas contCuentas) {
        if (tipoCuenta) {
            contCuentaEntidad.setIdCuentaPatrimonial(contCuentas);
            PrimeFaces.current().ajax().update("fieldsetPatrimonio");
        } else {
            contCuentaEntidad.setIdCuentaMovimiento(contCuentas);
            PrimeFaces.current().ajax().update("fieldsetMovimiento");
        }
        JsfUtil.executeJS("PF('contCuentasDlg').hide()");
        PrimeFaces.current().ajax().update("contCuentasForm");
    }

    public void openDlgBanco() {
        JsfUtil.executeJS("PF('bancoDlg').show()");
        PrimeFaces.current().ajax().update("bancoForm");
    }

    public void closeDlgBanco(Banco banco) {
        contCuentaEntidad.setIdBanco(banco);
        JsfUtil.executeJS("PF('bancoDlg').hide()");
        PrimeFaces.current().ajax().update("bancoForm");
        PrimeFaces.current().ajax().update("fieldsetBanco");
    }

    public ContCuentaEntidad getContCuentaEntidad() {
        return contCuentaEntidad;
    }

    public void setContCuentaEntidad(ContCuentaEntidad contCuentaEntidad) {
        this.contCuentaEntidad = contCuentaEntidad;
    }

    public LazyModel<ContCuentaEntidad> getContCuentaEntidadLazyModel() {
        return contCuentaEntidadLazyModel;
    }

    public void setContCuentaEntidadLazyModel(LazyModel<ContCuentaEntidad> contCuentaEntidadLazyModel) {
        this.contCuentaEntidadLazyModel = contCuentaEntidadLazyModel;
    }

    public LazyModel<Banco> getBancoLazyModel() {
        return bancoLazyModel;
    }

    public void setBancoLazyModel(LazyModel<Banco> bancoLazyModel) {
        this.bancoLazyModel = bancoLazyModel;
    }

    public LazyModel<ContCuentas> getContCuentasLazyModel() {
        return contCuentasLazyModel;
    }

    public void setContCuentasLazyModel(LazyModel<ContCuentas> contCuentasLazyModel) {
        this.contCuentasLazyModel = contCuentasLazyModel;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(boolean tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

}
