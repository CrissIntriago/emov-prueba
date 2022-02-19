/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.conf.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import java.io.Serializable;
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
@Named(value = "planCuentaView")
@ViewScoped
public class PlanCuentasController implements Serializable {

    @Inject
    private PlanCuentasService planCuentasService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<PlanCuentas> cuentaContableLazy;
    private LazyModel<PlanCuentas> itemPresupuestarioLazy;

    private List<CatalogoItem> tipoEstructuraList;

    private PlanCuentas planCuentas;

    private boolean tipoIngreso;
    private boolean tipoConfPresupuesto; // si es true es programatico, y si es false es presupuestario

    String codeTipo;

    @PostConstruct
    public void initView() {
        tipoEstructuraList = catalogoItemService.findByCatalogo("tipo_estructura");
        //lazy para cuentas contables
        this.cuentaContableLazy = new LazyModel<>(PlanCuentas.class);
        this.cuentaContableLazy.getSorteds().put("nivel", "ASC");
        this.cuentaContableLazy.getFilterss().put("estado", true);
        this.cuentaContableLazy.getFilterss().put("codigo", CONFIG.PLAN_CUENTA_CONTABLE);
        //lazy para item presupuestario
        updateTable(false);
        planCuentas = new PlanCuentas();
        tipoConfPresupuesto = false;
    }

    public void updateTable(Boolean accion) {
        tipoConfPresupuesto = accion;
        this.itemPresupuestarioLazy = new LazyModel<>(PlanCuentas.class);
        this.itemPresupuestarioLazy.getSorteds().put("nivel", "ASC");
        this.itemPresupuestarioLazy.getFilterss().put("estado", true);
        this.itemPresupuestarioLazy.getFilterss().put("codigo", CONFIG.PLAN_ITEM_PRESUPUESTARIO);
        this.itemPresupuestarioLazy.getFilterss().put("programatico", tipoConfPresupuesto);
    }

    public void form(PlanCuentas registro, boolean tipo) {
        tipoIngreso = tipo;
        setParametros();
        if (registro != null) {
            planCuentas = registro;
        } else {
            planCuentas = new PlanCuentas();
            planCuentas.setNivel(planCuentasService.getUltimoNivel(codeTipo, tipoConfPresupuesto));
            planCuentas.setProgramatico(tipoConfPresupuesto);
        }
        JsfUtil.executeJS("PF('registroDlg').show()");
        PrimeFaces.current().ajax().update("registroForm");
    }

    private void setParametros() {
        if (tipoIngreso) {
            codeTipo = CONFIG.PLAN_CUENTA_CONTABLE;
        } else {
            codeTipo = CONFIG.PLAN_ITEM_PRESUPUESTARIO;
        }
    }

    public void save() {
        boolean edit = planCuentas.getId() != null;
        //validar nivel
        if (planCuentas.getNivel() <= 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "El nivel ingresado debe ser mayor a 0");
            return;
        }
        //validar num_digito
        if (planCuentas.getNivel() <= 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "El numero de dígitos ingresado debe ser mayor a 0");
            return;
        }
        //validar separador
        if (planCuentas.getSeparador() && planCuentas.getCaracter().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "debe ingresar un caracter que tenga la funcion de separador de nivel");
            return;
        }
        if (codeTipo.equals(CONFIG.PLAN_ITEM_PRESUPUESTARIO) && planCuentas.getProgramatico()) {
            if (planCuentas.getIdTipo_estructura() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un tipo de estructura programática");
                return;
            }
        }
        //guardar o editar
        if (edit) {
            planCuentasService.edit(planCuentas);
        } else {
            if (planCuentasService.getVerificarNivel(planCuentas.getNivel(), codeTipo,tipoConfPresupuesto)) {
                JsfUtil.addWarningMessage("AVISO!!!", "Ya tiene un registro de nivel: " + planCuentas.getNivel());
                return;
            }
            planCuentas.setCodigo(codeTipo);
            planCuentasService.create(planCuentas);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        JsfUtil.executeJS("PF('registroDlg').hide()");
    }

    public void delete(PlanCuentas registro) {
        registro.setEstado(false);
        planCuentasService.edit(registro);
        JsfUtil.addSuccessMessage("INFO!!!", "Se a eliminado correctamente el registro");
    }

    public LazyModel<PlanCuentas> getCuentaContableLazy() {
        return cuentaContableLazy;
    }

    public void setCuentaContableLazy(LazyModel<PlanCuentas> cuentaContableLazy) {
        this.cuentaContableLazy = cuentaContableLazy;
    }

    public LazyModel<PlanCuentas> getItemPresupuestarioLazy() {
        return itemPresupuestarioLazy;
    }

    public void setItemPresupuestarioLazy(LazyModel<PlanCuentas> itemPresupuestarioLazy) {
        this.itemPresupuestarioLazy = itemPresupuestarioLazy;
    }

    public PlanCuentas getPlanCuentas() {
        return planCuentas;
    }

    public void setPlanCuentas(PlanCuentas planCuentas) {
        this.planCuentas = planCuentas;
    }

    public boolean isTipoConfPresupuesto() {
        return tipoConfPresupuesto;
    }

    public void setTipoConfPresupuesto(boolean tipoConfPresupuesto) {
        this.tipoConfPresupuesto = tipoConfPresupuesto;
    }

    public List<CatalogoItem> getTipoEstructuraList() {
        return tipoEstructuraList;
    }

    public void setTipoEstructuraList(List<CatalogoItem> tipoEstructuraList) {
        this.tipoEstructuraList = tipoEstructuraList;
    }

}
