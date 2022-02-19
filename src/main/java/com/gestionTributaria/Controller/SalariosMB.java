/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CtlgSalario;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author DEVELOPER
 */
@Named(value = "salarios")
@ViewScoped
public class SalariosMB implements Serializable {

    public static final Long serialVersionUID = 1L;
    protected LazyModel<CtlgSalario> salarios;
    protected CtlgSalario salario;

    private Map<String, Object> paramt;
    @Inject
    private ManagerService service;
//    private RecaudacionesService recaudacion;

    @PostConstruct
    public void initView() {
        try {
            salarios = new LazyModel<>(CtlgSalario.class);
            salarios.getSorteds().put("id", "DESC");
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public boolean validarSalario(CtlgSalario salario) {

        if (salario != null) {
            if (salario.getValor() == null || salario.getAnio() == null) {
                JsfUtil.addInformationMessage("Información", "Los campos son Obligatorios");
                return false;
            }
            if (salario.getId() == null) {
                paramt = new HashMap<>();
                paramt.put("valor", salario.getValor());
                paramt.put("anio", salario.getAnio());

                if (service.verificacionSalrios(salario.getAnio(), salario.getValor())) {
                    JsfUtil.addInformationMessage("Información", "Registro ya existe");
                    return false;
                }
            }

        }
        return true;
    }

    public void seleccionarSalario(CtlgSalario i) {
        try {
            if (i == null) {
                this.salario = new CtlgSalario();
            } else {
                this.salario = i;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void eliminar(CtlgSalario s) {
        service.remove(s);
        JsfUtil.addInformationMessage("Información", "Registro eliminado correctamente");
    }

    public void guardarSalario() {
        try {
            if (validarSalario(this.salario)) {
                if (this.salario.getId() == null) {
                    this.salario = (CtlgSalario) service.create(this.salario);
                    JsfUtil.addInformationMessage("Información", "Registro Grabado Exitosamente");
                } else {
                    service.edit(this.salario);
                    JsfUtil.addInformationMessage("Información", "Registro Editado Exitosamente");
                }

            } else {
                JsfUtil.addWarningMessage("Error", "El salairo para el año " + this.salario.getAnio() + " ya existe");
                return;
            }
            salarios = new LazyModel<>(CtlgSalario.class);
            salarios.getSorteds().put("id", "DESC");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "No se pudo grabar el Registro");
            System.err.println(e);
        }
    }

    public LazyModel<CtlgSalario> getSalarios() {
        return salarios;
    }

    public void setSalarios(LazyModel<CtlgSalario> salarios) {
        this.salarios = salarios;
    }

    public CtlgSalario getSalario() {
        return salario;
    }

    public void setSalario(CtlgSalario salario) {
        this.salario = salario;
    }

    public Map<String, Object> getParamt() {
        return paramt;
    }

    public void setParamt(Map<String, Object> paramt) {
        this.paramt = paramt;
    }

}
