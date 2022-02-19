/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.ThGastoPersonal;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThGastoPersonalService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * @author Jonathan Choez
 */
@Named(value = "thGastoPersonalView")
@ViewScoped
public class ThGastoPersonalController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThGastoPersonalService thGastoPersonalService;

    private ThGastoPersonal thGastoPersonal;
    private OpcionBusqueda opcionBusqueda;

    private LazyModel<ThGastoPersonal> thGastoPersonalLazy;

    private List<Short> periodos;
    private List<Provincia> provincias;
    private List<Canton> cantones;

    private Boolean view, collapsed;

    private String identificacion;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        provincias = thInterfaces.getProvincias();
        clean();
        updateLazy();
    }

    public void clean() {
        collapsed = Boolean.TRUE;
        view = Boolean.FALSE;
        thGastoPersonal = new ThGastoPersonal();
        thGastoPersonal.setEjercicioFiscal(opcionBusqueda.getAnio());
        thGastoPersonal.setFechaEntrega(new Date());
        identificacion = "";
        cantones = new ArrayList<>();
    }

    public void updateLazy() {
        if (opcionBusqueda.getAnio() != null) {
            thGastoPersonalLazy = new LazyModel<>(ThGastoPersonal.class);
            thGastoPersonalLazy.getSorteds().put("servidor.persona.apellido", "ASC");
            thGastoPersonalLazy.getFilterss().put("estado", true);
            thGastoPersonalLazy.getFilterss().put("ejercicioFiscal", opcionBusqueda.getAnio());
            thGastoPersonalLazy.setDistinct(false);
        } else {
            thGastoPersonalLazy = null;
        }
    }

    public void updateCantones() {
        if (thGastoPersonal.getProvincia() != null) {
            cantones = thInterfaces.getCantones(thGastoPersonal.getProvincia());
        } else {
            cantones = new ArrayList<>();
        }
    }

    public void findServidor() {
        thGastoPersonal.setServidor(thInterfaces.findByServidor(identificacion));
        if (thGastoPersonal.getServidor() != null) {
            actualizarIngresoGravado();
            JsfUtil.addSuccessMessage("INFO!!!", "Datos del servidor cargado");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se encuentra ningun servidor público con el número de identificación ingresado");
        }
    }

    public void calcularIngreso() {
        thGastoPersonal.setTotalIngreso(thGastoPersonal.getIngresoGravado().add(thGastoPersonal.getOtrosIngresos()).setScale(2, RoundingMode.HALF_UP));
    }

    public void calculaEgreso() {
        thGastoPersonal.setTotalGasto(thGastoPersonal.getGastoVivienda()
                .add((thGastoPersonal.getGastoEducacion().setScale(2, RoundingMode.HALF_UP))
                        .add((thGastoPersonal.getGastoSalud().setScale(2, RoundingMode.HALF_UP))
                                .add((thGastoPersonal.getGastoVestimenta().setScale(2, RoundingMode.HALF_UP))
                                        .add((thGastoPersonal.getGastoAlimentacion().setScale(2, RoundingMode.HALF_UP))
                                                .add(thGastoPersonal.getGastoTurismo().setScale(2, RoundingMode.HALF_UP)))))));
    }

    public void form(ThGastoPersonal thGastoPersonal, Boolean accion, Boolean actualizar) {
        this.view = accion;
        if (thGastoPersonal != null) {
            this.thGastoPersonal = thGastoPersonal;
            this.thGastoPersonal.setActualizacion(actualizar);
            updateCantones();
            if (actualizar) {
                this.thGastoPersonal.setFechaEntrega(new Date());
                actualizarIngresoGravado();
            }
        } else {
            this.thGastoPersonal = new ThGastoPersonal();
            this.thGastoPersonal.setActualizacion(actualizar);
            this.thGastoPersonal.setEjercicioFiscal(opcionBusqueda.getAnio());
            this.thGastoPersonal.setFechaEntrega(new Date());
        }
        collapsed = Boolean.FALSE;
        PrimeFaces.current().ajax().update("createEditPanel");
    }

    public void save() {
        Boolean edit = thGastoPersonal.getId() != null;
        if (thGastoPersonal.getEjercicioFiscal() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo fiscal");
            return;
        }
        if (thGastoPersonal.getProvincia() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una provincia");
            return;
        }
        if (thGastoPersonal.getCanton() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un cantón");
            return;
        }
        if (thGastoPersonal.getServidor() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un servidor");
            return;
        }
        System.out.println("1. " + thGastoPersonal.getOtrosIngresos().doubleValue());
        if (thGastoPersonal.getOtrosIngresos().doubleValue() > 0) {
            if (thGastoPersonal.getValorImpuestoRetenido().doubleValue() <= 0) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresa el valor del impuesto retenido y asumido por otros empleadores");
                return;
            }
        }
        if (validarPorcentaje()) {
            JsfUtil.addWarningMessage("AVISO!!!", "El total de los gastos es mayor al valor del porcentaje especificado en la ley");
            return;
        }
        thGastoPersonal.setPeriodo(Utils.getAnio(thGastoPersonal.getFechaEntrega()).shortValue());
        if (edit) {
            thGastoPersonal.setActualizacion(true);
            update(thGastoPersonal, "Editado con éxito");
        } else {
            thGastoPersonal.setUsuarioCreacion(thInterfaces.getUser());
            thGastoPersonal.setFechaCreacion(new Date());
            thGastoPersonalService.create(thGastoPersonal);
            JsfUtil.addSuccessMessage("INFO!!!", " Registrado con éxito");
        }
        clean();
        PrimeFaces.current().ajax().update("thGastoPersonalTable");
        PrimeFaces.current().ajax().update("createEditPanel");
    }

    public void delete(ThGastoPersonal thGastoPersonal) {
        thGastoPersonal.setEstado(false);
        update(thGastoPersonal, "Se ha eliminado correctamente la información");
    }

    public void update(ThGastoPersonal thGastoPersonal, String msj) {
        thGastoPersonal.setUsuarioModifica(thInterfaces.getUser());
        thGastoPersonal.setFechaModificacion(new Date());
        thGastoPersonalService.edit(thGastoPersonal);
        JsfUtil.addSuccessMessage("INFO!!!", msj);
        PrimeFaces.current().ajax().update("thGastoPersonalTable");
    }

    private boolean validarPorcentaje() {
        Boolean accion = Boolean.FALSE;
        Double maxPorcentaje = (thInterfaces.valorPorcentaje().doubleValue() / 100);
        Double porcentaje = (thGastoPersonal.getIngresoGravado().doubleValue() * maxPorcentaje);
        if (thGastoPersonal.getTotalGasto().doubleValue() > porcentaje) {
            accion = Boolean.TRUE;
        }
        return accion;
    }

    public void actualizarIngresoGravado() {
        if (thGastoPersonal.getServidor() != null) {
            ThServidorCargo temp = thInterfaces.setServidorCargo(thGastoPersonal.getServidor());
            int diferenciaMeses = 12 - (Utils.getMes(thGastoPersonal.getFechaEntrega()) - 1);
            thGastoPersonal.setIngresoGravado(new BigDecimal(temp.getIdCargo().getIdGrupo().getRemuneracionDolares().doubleValue() * diferenciaMeses));
            calcularIngreso();
        }
    }

    public ThGastoPersonal getThGastoPersonal() {
        return thGastoPersonal;
    }

    public void setThGastoPersonal(ThGastoPersonal thGastoPersonal) {
        this.thGastoPersonal = thGastoPersonal;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ThGastoPersonal> getThGastoPersonalLazy() {
        return thGastoPersonalLazy;
    }

    public void setThGastoPersonalLazy(LazyModel<ThGastoPersonal> thGastoPersonalLazy) {
        this.thGastoPersonalLazy = thGastoPersonalLazy;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
}
