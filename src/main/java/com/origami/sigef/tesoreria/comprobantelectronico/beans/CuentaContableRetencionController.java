/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CuentaContableRetencionService;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.entities.RubroTipo;
import com.origami.sigef.tesoreria.service.RubroService;
import com.origami.sigef.tesoreria.service.RubroTipoService;
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
 * @author jesus
 */
@Named(value = "cuentaRetencionView")
@ViewScoped
public class CuentaContableRetencionController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CuentaContableRetencionService cuentaContableRetencionService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private RubroService rubroService;
    @Inject
    private RubroTipoService rubroTipoService;

    private CuentaContableRetencion cuentaContableRetencion;
    private List<MasterCatalogo> periodos;
//    private List<ContCuentas> cuentaContables;
    private List<Rubro> rubros;
    private Short periodo;
    private LazyModel<CuentaContableRetencion> retenciones;
    private List<RubroTipo> rubroTipos;
    private LazyModel<ContCuentas> cuentaContables;
    private ContCuentas cuentaContableSeleccionado;

    private Boolean edit;
    private Integer idTipoRubro;

    @PostConstruct
    public void init() {
        inicializarDatos();
        edit = Boolean.FALSE;
        retenciones = new LazyModel<>(CuentaContableRetencion.class);
        retenciones.getFilterss().put("estado", true);
//        retenciones.getFilterss().put("periodo", periodo);
    }

    public void guardar() {
        if (validar()) {
            edit = cuentaContableRetencion.getId() != null;
            if (edit) {
                loadData();
                cuentaContableRetencionService.edit(cuentaContableRetencion);
            } else {
                loadData();
                cuentaContableRetencion.setEstado(Boolean.TRUE);
                cuentaContableRetencionService.create(cuentaContableRetencion);
            }
            inicializarDatos();
            JsfUtil.addSuccessMessage("", "Guardado con Éxito");
        }
    }

    public void loadData() {
        cuentaContableRetencion.setPeriodo(periodo);
    }

    public void inicializarDatos() {
        cuentaContableRetencion = new CuentaContableRetencion();
        cuentaContableRetencion.setContContable(new ContCuentas());
        cuentaContableRetencion.setRetencion(new Rubro());
        rubros = new ArrayList<>();
//        cuentaContables = new ArrayList<>();
        idTipoRubro = null;
        periodo = Utils.getAnio(new Date()).shortValue();
        edit = Boolean.FALSE;
        actualizarCuentas();
    }

    public Boolean validar() {
        if (cuentaContableRetencion.getContContable()== null) {
            JsfUtil.addWarningMessage("", "Cuenta Contable campo Requerido");
            return false;
        }
        if (cuentaContableRetencion.getRetencion() == null) {
            JsfUtil.addWarningMessage("", "Rubro campo Requerido");
            return false;
        }
        return true;
    }

    public void editar(CuentaContableRetencion retencion) {
        edit = Boolean.TRUE;
        periodo = retencion.getPeriodo();
        idTipoRubro = retencion.getRetencion().getRubroTipo().getId();
        cuentaContableRetencion = retencion;
        actualizarCuentas();
        actualizarRubros();
        PrimeFaces.current().ajax().update("datos");

    }

    public void openDlgCuentaContable(int codigo) {
        //this.tipoCuenta = accion;
        int codigoAccion = codigo;
        this.cuentaContables = new LazyModel<>(ContCuentas.class);
        this.cuentaContables.getSorteds().put("codigo", "ASC");
        this.cuentaContables.getFilterss().put("estado", true);
        this.cuentaContables.getFilterss().put("movimiento", true);
//        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        switch (codigoAccion) {
            case 1:
//                this.cuentaContableLazyModel.getFilterss().put("titulo", 1);
//                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
//                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 3);
                break;
            case 2:
//                this.cuentaContableLazyModel.getFilterss().put("titulo", 6);
//                this.cuentaContableLazyModel.getFilterss().put("grupo", 2);
                break;
            case 4:
//                this.cuentaContableLazyModel.getFilterss().put("titulo", 1);
//                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
//                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 2);
                break;
            case 5:
//                this.cuentaContableLazyModel.getFilterss().put("titulo", 2);
//                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
//                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 2);
                break;
        }
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
        PrimeFaces.current().ajax().update("cuentasPresupuestarioTable");
    }

    public void aniadirCuentaContable() {
        cuentaContableRetencion.setContContable(cuentaContableSeleccionado);
        cuentaContableSeleccionado = new ContCuentas();
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
        JsfUtil.update("frmMain");
    }

    public void eliminar(CuentaContableRetencion retencion) {
        retencion.setEstado(Boolean.FALSE);
        cuentaContableRetencionService.edit(retencion);
        PrimeFaces.current().ajax().update("dataTableRetenciones");
        JsfUtil.addSuccessMessage("", "Registro Eliminado con Éxito");

    }

    public void actualizarCuentas() {
//        cuentaContables = contCuentasService.findMovimientos(Boolean.TRUE, Boolean.TRUE);
//        System.out.println("lista de cuentas >> " + cuentaContables.size());
        rubroTipos = rubroTipoService.findAll();
        retenciones = new LazyModel<>(CuentaContableRetencion.class);
        retenciones.getFilterss().put("estado", true);
//        retenciones.getFilterss().put("periodo", periodo);
    }

    public void actualizarRubros() {
        if (idTipoRubro != null) {
            rubros = rubroService.findRubrosByTipo(idTipoRubro);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Integer getIdTipoRubro() {
        return idTipoRubro;
    }

    public void setIdTipoRubro(Integer idTipoRubro) {
        this.idTipoRubro = idTipoRubro;
    }

    public List<RubroTipo> getRubroTipos() {
        return rubroTipos;
    }

    public void setRubroTipos(List<RubroTipo> rubroTipos) {
        this.rubroTipos = rubroTipos;
    }

    public LazyModel<CuentaContableRetencion> getRetenciones() {
        return retenciones;
    }

    public void setRetenciones(LazyModel<CuentaContableRetencion> retenciones) {
        this.retenciones = retenciones;
    }

    public List<Rubro> getRubros() {
        return rubros;
    }

    public void setRubros(List<Rubro> rubros) {
        this.rubros = rubros;
    }

    public LazyModel<ContCuentas> getCuentaContables() {
        return cuentaContables;
    }

    public void setCuentaContables(LazyModel<ContCuentas> cuentaContables) {
        this.cuentaContables = cuentaContables;
    }

    public CuentaContableRetencion getCuentaContableRetencion() {
        return cuentaContableRetencion;
    }

    public void setCuentaContableRetencion(CuentaContableRetencion cuentaContableRetencion) {
        this.cuentaContableRetencion = cuentaContableRetencion;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public ContCuentas getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(ContCuentas cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

//</editor-fold>
}
