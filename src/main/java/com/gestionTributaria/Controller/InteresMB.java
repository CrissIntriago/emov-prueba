/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenIntereses;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author DEVELOPER
 */
@Named(value = "intereses")
@ViewScoped
public class InteresMB implements Serializable {

    public static final Long serialVersionUID = 1L;
    final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
    @Inject
    private ManagerService service;

    protected LazyModel<FinaRenIntereses> intereses;
    protected LazyModel<RenParametrosInteresMulta> lazyInteresMulta;
    private RenParametrosInteresMulta parametrosInteresMulta;
    protected FinaRenIntereses interes;
    private Map<String, Object> paramt;
    private List<FinaRenTipoLiquidacion> listaTipoLiquidaciones;


    @PostConstruct
    public void initView() {
        try {
            lazyInteresMulta = new LazyModel<>(RenParametrosInteresMulta.class);
            intereses = new LazyModel<>(FinaRenIntereses.class);
            intereses.getSorteds().put("id", "DESC");
            parametrosInteresMulta = new RenParametrosInteresMulta();
            listaTipoLiquidaciones = new ArrayList<>();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public void newInteresMulta(RenParametrosInteresMulta r) {
        listaTipoLiquidaciones = new ArrayList<>();
        listaTipoLiquidaciones = service.findAllEasy("select r from FinaRenTipoLiquidacion r order by r.nombreTransaccion asc");
        parametrosInteresMulta = new RenParametrosInteresMulta();
        if (r != null) {
            parametrosInteresMulta = r;
        }
    }

    public void saveUpdateInteresMulta() {
       
        if (parametrosInteresMulta.getId() == null) {
            service.save(parametrosInteresMulta);
        } else {
            service.update(parametrosInteresMulta);
        }
        JsfUtil.addInformationMessage("", "Transacción realizada cone exito");
        parametrosInteresMulta = new RenParametrosInteresMulta();
    }

    public boolean validarInteres(FinaRenIntereses i) {
        if (i.getDesde() == null || i.getHasta() == null || i.getDias() == null || i.getPorcentaje() == null) {
            JsfUtil.addWarningMessage("Información", "Los campos son Obligatorios");
            return false;
        }
        if (i.getId() == null) {
            paramt = new HashMap<>();
            paramt.put("desde", i.getDesde());
            if (service.verificacionInteresFechaDesdeHasta(i.getDesde(), true)) {
                JsfUtil.addInformationMessage("Información", "Registro ya existe");
                return false;
            }
            paramt = new HashMap<>();
            paramt.put("hasta", i.getHasta());
            if (service.verificacionInteresFechaDesdeHasta(i.getHasta(), false)) {
                JsfUtil.addInformationMessage("Información", "Registro ya existe");
                return false;
            }
        }
        return true;
    }

    public void seleccionarInteres(FinaRenIntereses i) {
        try {
            if (i == null) {
                this.interes = new FinaRenIntereses();
                Calendar fechaActual = Calendar.getInstance();
                fechaActual.set(Calendar.HOUR, 0);
                fechaActual.set(Calendar.MINUTE, 0);
                fechaActual.set(Calendar.SECOND, 0);
                fechaActual.set(Calendar.DAY_OF_MONTH, 1);
                this.interes.setDesde(fechaActual.getTime());
                fechaActual.add(Calendar.MONTH, 1);
                fechaActual.add(Calendar.DAY_OF_MONTH, -1);
                this.interes.setHasta(fechaActual.getTime());
                this.interes.setDias(new Integer(((this.interes.getHasta().getTime() - this.interes.getDesde().getTime()) / MILLSECS_PER_DAY) + 1 + ""));
            } else {
                this.interes = i;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void guardarInteres() {
        try {
            if (validarInteres(this.interes)) {

                if (this.interes == null) {
                    this.interes = service.grabarInteres(this.interes);
                    JsfUtil.addInformationMessage("Información", "Registro Grabado Exitosamente");
                } else {
                    if (service.update(this.interes)) {
                        JsfUtil.addInformationMessage("Información", "Registro Editado Exitosamente");
                    } else {
                        JsfUtil.addErrorMessage("Error", "No se pudo grabar el Registro");
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "No se pudo grabar el Registro");
            System.err.println(e);
        }
    }

    public LazyModel<FinaRenIntereses> getIntereses() {
        return intereses;
    }

    public void setIntereses(LazyModel<FinaRenIntereses> intereses) {
        this.intereses = intereses;
    }

    public FinaRenIntereses getInteres() {
        return interes;
    }

    public void setInteres(FinaRenIntereses interes) {
        this.interes = interes;
    }

    public Map<String, Object> getParamt() {
        return paramt;
    }

    public void setParamt(Map<String, Object> paramt) {
        this.paramt = paramt;
    }

    public LazyModel<RenParametrosInteresMulta> getLazyInteresMulta() {
        return lazyInteresMulta;
    }

    public void setLazyInteresMulta(LazyModel<RenParametrosInteresMulta> lazyInteresMulta) {
        this.lazyInteresMulta = lazyInteresMulta;
    }

    public RenParametrosInteresMulta getParametrosInteresMulta() {
        return parametrosInteresMulta;
    }

    public void setParametrosInteresMulta(RenParametrosInteresMulta parametrosInteresMulta) {
        this.parametrosInteresMulta = parametrosInteresMulta;
    }

    public List<FinaRenTipoLiquidacion> getListaTipoLiquidaciones() {
        return listaTipoLiquidaciones;
    }

    public void setListaTipoLiquidaciones(List<FinaRenTipoLiquidacion> listaTipoLiquidaciones) {
        this.listaTipoLiquidaciones = listaTipoLiquidaciones;
    }


}
