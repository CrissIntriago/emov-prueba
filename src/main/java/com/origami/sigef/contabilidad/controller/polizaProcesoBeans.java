/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.GarantiaService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Origami
 */
@Named(value = "polizaProceso")
@ViewScoped
public class polizaProcesoBeans extends BpmnBaseRoot implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private GarantiaService garantiaservice;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private ClienteService clienteService;

    private Short dias;
    private Date fechaDesde;
    private Date fechaHasta;
    private Adquisiciones adquisicion;
    private Boolean ver;

    private Garantias garantia;
    private LazyModel<Garantias> garantiaLazy;
    private List<CatalogoItem> tiposdoc;
    private List<CatalogoItem> riesgoAsegurado;
    private OpcionBusqueda opcionBusqueda;

    private LazyModel<CuentaContable> cuentaContableLazyModel;
    private TareasActivas tareasActivas;

    @PostConstruct
    public void inicializate() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
            }
        }
        this.dias = 0;
        this.ver = false;
        this.garantia = new Garantias();
        this.garantia.setAseguradora(new Proveedor());
        adquisicion = new Adquisiciones();
        garantiaLazy = new LazyModel<>(Garantias.class);
        garantiaLazy.getFilterss().put("estadoproceso", true);
        if (this.session.getVarTemp() instanceof Adquisiciones) {
            adquisicion = (Adquisiciones) this.session.getVarTemp();
        } else if (this.session.getVarTemp() instanceof TareasActivas) {
            tareasActivas = (TareasActivas) this.session.getVarTemp();
            adquisicion = adquisicionesService.findAllAdquisicionesByNumTramite(tareasActivas.getNumTramite());
        }
        garantiaLazy.getFilterss().put("adquisicion", adquisicion);
        garantiaLazy.getFilterss().put("estado", true);
        tiposdoc = catalogoService.getTipoCuenta("tipo_doc", "ti_doc");
        riesgoAsegurado = catalogoService.getTipoCuenta("riesgo_asegurado", "FIEL_CUMPL");
        opcionBusqueda = new OpcionBusqueda();
    }

    public void buscarProveedor() {
        if (!garantia.getIdentificacionAseguradora().isEmpty()) {
            Proveedor p = servidorService.findByProveedor(garantia.getIdentificacionAseguradora());
            if (p != null) {
                garantia.setAseguradora(p);
                garantia.setIdentificacionAseguradora(p.getCliente().getIdentificacionCompleta());
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
        }
    }

    public void selectData(SelectEvent evt) {
        garantia.setAseguradora((Proveedor) evt.getObject());
        garantia.setIdentificacionAseguradora(((Proveedor) evt.getObject()).getCliente().getIdentificacionCompleta());
        PrimeFaces.current().ajax().update("fieldset");
    }

    public void completar() {
        try {
            getParamts().put("contabilidadC", clienteService.getrolsUser(RolUsuario.contador));
            getParamts().put("idServidor", session.getUserId());
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception ex) {
            Logger.getLogger(RevisionRecepcionTramiteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updialogObservacion() {
        if (garantiaLazy.getRowCount() == 0) {
            JsfUtil.addWarningMessage("", "Debe Ingresar al menos una garantía");
            return;
        }
        observacion.setEstado(Boolean.TRUE);
        observacion.setFecCre(new Date());
        observacion.setUserCre(this.session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void guardar() {
        boolean edit = garantia.getId() != null;
        if (validar()) {
            if (fechaHasta.compareTo(new Date()) < 0) {
                JsfUtil.addWarningMessage("Informacíon", "el Documento Vencido");
            }
            if (fechaHasta.compareTo(new Date()) > 0) {
                JsfUtil.addWarningMessage("Informacíon", "el Documento Vigente");
            }
            if (edit) {
                garantia.setFechaModifica(new Date());
                garantia.setUsuarioModifica(userSession.getNameUser());
                garantiaservice.edit(garantia);
            } else {
                if (tareasActivas != null) {
                    garantia.setNumTramite(tareasActivas.getNumTramite());
                }
                garantia.setFechaCreacion(new Date());
                garantia.setFechaDesde(fechaDesde);
                garantia.setFechaHasta(fechaHasta);
                garantia.setDuracionDias(dias);
                garantia.setUsuarioCreacion(userSession.getNameUser());
                garantia.setEstadoproceso(Boolean.TRUE);
                garantia.setAdquisicion(adquisicion);
                garantia = garantiaservice.create(garantia);
            }
            garantia = new Garantias();
            fechaDesde = null;
            fechaHasta = null;
            dias = 0;
            JsfUtil.addInformationMessage("Poliza", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public Boolean validar() {
        if (garantia.getRiesgoAsegurado() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Seleccionar un Riesgo Asegurado");
            return false;
        }
        if (garantia.getTipoDocumento() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Seleccionar un Tipo de Documento");
            return false;
        }
        if (garantia.getSuma().intValue() <= 0) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar una Cantidad");
            return false;
        }
        if (garantia.getDetalle() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Debe Ingresar un Detalle");
            return false;
        }
        if (garantia.getDetalle() == null) {
            JsfUtil.addWarningMessage("Informacíon", "Ingrese un Detalle");
            return false;
        }
        return true;
    }

    public void editar(Garantias g, Boolean b) {
        this.garantia = g;
        this.garantia.setIdentificacionAseguradora(g.getAseguradora().getCliente().getIdentificacion());
        this.ver = b;
        this.fechaDesde = g.getFechaDesde();
        this.fechaHasta = g.getFechaHasta();

    }

    public void calculoDias() {
        this.dias = (TalentoHumano.diasDiferencia(fechaDesde, fechaHasta)).shortValue();
    }

    public void cancelar() {
        garantia = new Garantias();
        fechaDesde = null;
        fechaHasta = null;
        ver = false;
    }

    public void eliminar(Garantias g) {
        JsfUtil.addSuccessMessage("Poliza", " Eliminada con Exito");
        g.setEstado(Boolean.FALSE);
        garantiaservice.edit(g);

    }

    public Boolean polizaVigente(Date fechaHasta) {
        return fechaHasta.compareTo(new Date()) > 0;
    }

    public void openDlgCuentasContables() {
        this.cuentaContableLazyModel = new LazyModel<>(CuentaContable.class);
        this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
        this.cuentaContableLazyModel.getFilterss().put("estado", true);
        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        this.cuentaContableLazyModel.getFilterss().put("titulo", 9);
        this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
        this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
    }

    public void aniadirCuentaContable() {
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
        PrimeFaces.current().ajax().update("gridCuentaContable");
    }

    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }

    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public Garantias getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantias garantia) {
        this.garantia = garantia;
    }

    public LazyModel<Garantias> getGarantiaLazy() {
        return garantiaLazy;
    }

    public void setGarantiaLazy(LazyModel<Garantias> garantiaLazy) {
        this.garantiaLazy = garantiaLazy;
    }

    public List<CatalogoItem> getTiposdoc() {
        return tiposdoc;
    }

    public void setTiposdoc(List<CatalogoItem> tiposdoc) {
        this.tiposdoc = tiposdoc;
    }

    public List<CatalogoItem> getRiesgoAsegurado() {
        return riesgoAsegurado;
    }

    public void setRiesgoAsegurado(List<CatalogoItem> riesgoAsegurado) {
        this.riesgoAsegurado = riesgoAsegurado;
    }

    public Short getDias() {
        return dias;
    }

    public void setDias(Short dias) {
        this.dias = dias;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }
//</editor-fold>
}
