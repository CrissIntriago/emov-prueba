/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.talentohumano.services.CuentaBancariaService;
import com.origami.sigef.tesoreria.modelTarifario.BancoOrigenModel;
import com.origami.sigef.tesoreria.service.DetalleCorteOrdenCobroService;
import com.origami.sigef.tesoreria.service.RecaudacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "recaudacionView")
@ViewScoped
public class RecaudacionController implements Serializable {

    @Inject
    private CuentaBancariaService cuentaBancoService;
    @Inject
    private RecaudacionService recaudacionService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private DetalleCorteOrdenCobroService detalleCorteService;
    @Inject
    private UserSession userSession;
    @Inject
    private CuentaContableService cuentaService;
    @Inject
    private ServletSession serveltSession;

    private LazyModel<Recaudacion> lazy;
    private LazyModel<Recaudacion> lazyAjuste;
    private Recaudacion recaudacion;
    private Recaudacion ajuste;
    private CatalogoItem tipo;
    private CuentaContable cuentaContable;
    private CuentaBancaria cuentaSeleccionanda;
    private Long bancoseleccionado;
    private OpcionBusqueda busqueda;
    private CatalogoItem cobroTipo;
    private CatalogoItem ajusteTipo;
    private CatalogoItem ajusteTipoSelect;

    private List<CatalogoItem> tipoList;
    private List<CatalogoItem> tipoAjusteList;
    private List<CuentaBancaria> cuentaBancoList;
    private List<BancoOrigenModel> bancoOrigenList;
    private List<Recaudacion> recaudaciones;
    private List<CuentaContable> cuentas;

    private Boolean edit;
    private Boolean renderedCodigo;
    private String codigo;
    private String codigoAjuste;
    private BigDecimal valorEdit;

    @PostConstruct
    public void init() {
        edit = Boolean.FALSE;
        renderedCodigo = Boolean.FALSE;
        busqueda = new OpcionBusqueda();
        recaudacion = new Recaudacion();
        tipo = new CatalogoItem();
        ajusteTipoSelect = new CatalogoItem();
        cuentaSeleccionanda = new CuentaBancaria();
        ajuste = new Recaudacion();
        ajuste.setAjuste(new Recaudacion());
        ajuste.setCuentaContable(new CuentaContable());
        cuentaContable = new CuentaContable();
        tipoList = catalogoItemService.findCatalogoItems("tipo_cobro");
        tipoAjusteList = catalogoItemService.findCatalogoItems("tipo_ajuste_T");
        cuentaBancoList = cuentaBancoService.getListaCuenta();
        bancoOrigenList = detalleCorteService.bancoOrigenList();
        recaudaciones = recaudacionService.recaudaciones();
        cuentas = cuentaService.getHijosCtaBanco("21209", busqueda.getAnio());
        cobroTipo = catalogoItemService.getCatalogoI("tipo_recaudacion_T", "COBROEM");
        ajusteTipo = catalogoItemService.getCatalogoI("tipo_recaudacion_T", "AJUSTEX");
        codigo = "COB-" + Utils.completarCadenaConCeros((recaudacionService.getCantReg(cobroTipo) + 1) + "", 3) + "-" + busqueda.getAnio().intValue() + "";
        codigoAjuste = "AJU-" + Utils.completarCadenaConCeros((recaudacionService.getCantReg(ajusteTipo) + 1) + "", 3) + "-" + busqueda.getAnio().intValue() + "";
        lazy = new LazyModel<>(Recaudacion.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRecaudacion", cobroTipo);
        lazyAjuste = new LazyModel<>(Recaudacion.class);
        lazyAjuste.getFilterss().put("estado", true);
        lazyAjuste.getFilterss().put("tipoRecaudacion", ajusteTipo);
        if (userSession.getVarTemp() instanceof Recaudacion) {
            recaudacion = (Recaudacion) userSession.getVarTemp();
            renderedCodigo = Boolean.TRUE;
            userSession.setVarTemp(null);
            PrimeFaces.current().ajax().update("frmMain");
        }
    }

    public void guardar() {
        if (edit) {
            recaudacion.setFechaMidificacion(new Date());
            recaudacion.setUsuarioModifica(userSession.getNameUser());
            recaudacion.setSaldoAfectar(recaudacion.getValor());
            recaudacion.setAgenciaDestino(cuentaSeleccionanda);
            recaudacion.setTipoRecaudacion(cobroTipo);
            recaudacion.setTipo(tipo);
            recaudacion.setAgenciaOrigen(bancoOrigen().getNombre());
            recaudacion.setId_banco(bancoOrigen().getId());
            recaudacionService.edit(recaudacion);
        } else {
            recaudacion.setFechaCreacion(new Date());
            recaudacion.setUsuarioCreacion(userSession.getNameUser());
            recaudacion.setAgenciaDestino(cuentaSeleccionanda);
            recaudacion.setCodigo(codigo);
            recaudacion.setTipo(tipo);
            recaudacion.setAgenciaOrigen(bancoOrigen().getNombre());
            recaudacion.setId_banco(bancoOrigen().getId());
            recaudacion.setFechaRegistro(new Date());
            recaudacion.setTipoRecaudacion(cobroTipo);
            recaudacion.setSaldoAfectar(recaudacion.getValor());
            recaudacion = recaudacionService.create(recaudacion);
        }
        JsfUtil.addInformationMessage("Información", "Datos de Registro de Cobro: " + codigo + "" + (edit ? "Editados" : "Registrados") + " Correctamente");
        resetvalue();
    }

    public void guardarAjuste() {
        boolean editAj = ajuste.getId() != null;
        if (ajuste.getValor().doubleValue() > recaudacion.getSaldoAfectar().doubleValue()) {
            JsfUtil.addWarningMessage("Error", "El valor asignado no puede ser mayor que el Saldo por afectar.");
            return;
        }
        if (editAj) {
            BigDecimal saldoVar;
            if (ajuste.getValor().doubleValue() > valorEdit.doubleValue()) {
                saldoVar = ajuste.getValor().subtract(valorEdit);
                recaudacion.setSaldoAfectar(recaudacion.getValor().subtract(saldoVar));
            } else {
                saldoVar = valorEdit.subtract(ajuste.getValor());
                recaudacion.setSaldoAfectar(recaudacion.getValor().add(saldoVar));
            }
            recaudacionService.edit(recaudacion);
            ajuste.setAjuste(recaudacion);
            ajuste.setCuentaContable(cuentaContable);
            ajuste.setSaldoAfectar(ajuste.getValor());
            ajuste.setTipo(ajusteTipoSelect);
            recaudacionService.edit(ajuste);
        } else {
            if (restarSaldoAfectar() != null) {
                recaudacion.setSaldoAfectar(restarSaldoAfectar());
                recaudacionService.edit(recaudacion);
            }
            ajuste.setAjuste(recaudacion);
            ajuste.setCuentaContable(cuentaContable);
            ajuste.setTipo(ajusteTipoSelect);
            ajuste.setTipoRecaudacion(ajusteTipo);
            ajuste.setFechaCreacion(new Date());
            ajuste.setAgenciaDestino(recaudacion.getAgenciaDestino());
            ajuste.setId_banco(recaudacion.getId_banco());
            ajuste.setAgenciaOrigen(recaudacion.getAgenciaOrigen());
            ajuste.setSaldoAfectar(ajuste.getValor());
            ajuste.setFechaRegistro(new Date());
            ajuste.setUsuarioCreacion(userSession.getNameUser());
            ajuste.setCodigo(codigoAjuste);
            ajuste.setFechaAfeccion(recaudacion.getFechaAfeccion());
            ajuste = recaudacionService.create(ajuste);
        }
        JsfUtil.addInformationMessage("Información", "Datos Registro de Ajuste: " + codigoAjuste + " " + (editAj ? "Editados" : "Registrados") + " Correctamente");
        newAjuste();
    }

    private BancoOrigenModel bancoOrigen() {
        if (bancoseleccionado != null) {
            for (BancoOrigenModel b : bancoOrigenList) {
                if (b.getId().intValue() == bancoseleccionado.intValue()) {
                    return b;
                }
            }
        }
        return null;
    }

    public void eliminarAjuste(Recaudacion rc) {
        if (rc.getSaldoAfectar().doubleValue() < rc.getValor().doubleValue()) {
            JsfUtil.addWarningMessage("Error", "El saldo del ajuste ya ah sido afectado.");
            return;
        }
        recaudacion = rc.getAjuste();
        valorEdit = rc.getValor();
        recaudacion.setValor(recaudacion.getValor().add(rc.getValor()));
        recaudacionService.edit(recaudacion);
        rc.setEstado(Boolean.FALSE);
        recaudacionService.edit(rc);
        recaudacion = new Recaudacion();
    }

    public void editar(Recaudacion rc, Boolean var) {
        if (rc.getSaldoAfectar().doubleValue() < rc.getValor().doubleValue()) {
            JsfUtil.addWarningMessage("Información", "Recaudación " + rc.getCodigo() + " Ya ha sido Afectado");
            return;
        }
        edit = var;
        if (var) {
            recaudacion = rc;
            codigo = rc.getCodigo();
            cuentaSeleccionanda = rc.getAgenciaDestino();
            bancoseleccionado = rc.getId_banco();
            tipo = rc.getTipo();
        } else {
            ajuste = rc;
            recaudacion = rc.getAjuste();
            ajusteTipoSelect = rc.getTipo();
            cuentaContable = rc.getCuentaContable();
            valorEdit = rc.getValor();
        }
    }

    public void eliminar(Recaudacion rc) {
        if (rc.getSaldoAfectar().doubleValue() < rc.getValor().doubleValue()) {
            JsfUtil.addWarningMessage("Información", "Recaudación " + rc.getCodigo() + " Ya ha sido Afectado");
            return;
        }
        rc.setEstado(Boolean.FALSE);
        recaudacionService.edit(rc);
        codigo = "COB-" + Utils.completarCadenaConCeros((recaudacionService.getCantReg(cobroTipo) + 1) + "", 3) + "-" + busqueda.getAnio().intValue() + "";
    }

    public void resetvalue() {
        edit = Boolean.FALSE;
        recaudacion = new Recaudacion();
        tipo = new CatalogoItem();
        cuentaSeleccionanda = new CuentaBancaria();
        bancoseleccionado = null;
        codigo = "COB-" + Utils.completarCadenaConCeros((recaudacionService.getCantReg(cobroTipo) + 1) + "", 3) + "-" + busqueda.getAnio().intValue() + "";
    }

    public void newAjuste() {
        ajuste = new Recaudacion();
        ajuste.setAjuste(new Recaudacion());
        ajuste.setCuentaContable(new CuentaContable());
        recaudacion = new Recaudacion();
        cuentaContable = new CuentaContable();
        ajusteTipoSelect = new CatalogoItem();
        recaudaciones = recaudacionService.recaudaciones();
        codigoAjuste = "AJU-" + Utils.completarCadenaConCeros((recaudacionService.getCantReg(ajusteTipo) + 1) + "", 3) + "-" + busqueda.getAnio().intValue() + "";
    }

    public void generarReporte(Recaudacion rc, boolean var) {
        if (rc.getValor().doubleValue() == rc.getSaldoAfectar().doubleValue()) {
            JsfUtil.addWarningMessage("Iformación", "El " + rc.getCodigo() + " no cuenta con datos o movimientos en su saldo.");
            return;
        }
        serveltSession.addParametro("id_cobro", rc.getId());
        if (var) {
            serveltSession.setNombreReporte("recaudacion_cobro");
        } else {
            serveltSession.setNombreReporte("recaudacion_ajuste");
        }
        serveltSession.setNombreSubCarpeta("OrdenCobro");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void realizarCobro(Recaudacion rc) {
        userSession.setVarTemp(rc);
        JsfUtil.redirectFaces("/tesoreria/Recaudacion/_RegistroRecaudacion_Cobro");
    }

    public void realizarAjuste(Recaudacion rc) {
        userSession.setVarTemp(rc);
        JsfUtil.redirectFaces("/tesoreria/Recaudacion/_AjusteRecaudacion");
    }

    public Boolean renderedAcciones(Recaudacion rec) {
        if (rec.getSaldoAfectar().intValue() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public BigDecimal restarSaldoAfectar() {
        if (recaudacion.getId() != null) {
            return recaudacion.getSaldoAfectar().subtract(ajuste.getValor());
        }
        return null;
    }

    public Recaudacion getAjuste() {
        return ajuste;
    }

    public void setAjuste(Recaudacion ajuste) {
        this.ajuste = ajuste;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public LazyModel<Recaudacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<Recaudacion> lazy) {
        this.lazy = lazy;
    }

    public Recaudacion getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(Recaudacion recaudacion) {
        this.recaudacion = recaudacion;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public CuentaBancaria getCuentaSeleccionanda() {
        return cuentaSeleccionanda;
    }

    public void setCuentaSeleccionanda(CuentaBancaria cuentaSeleccionanda) {
        this.cuentaSeleccionanda = cuentaSeleccionanda;
    }

    public List<CatalogoItem> getTipoList() {
        return tipoList;
    }

    public void setTipoList(List<CatalogoItem> tipoList) {
        this.tipoList = tipoList;
    }

    public List<CuentaBancaria> getCuentaBancoList() {
        return cuentaBancoList;
    }

    public void setCuentaBancoList(List<CuentaBancaria> cuentaBancoList) {
        this.cuentaBancoList = cuentaBancoList;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getBancoseleccionado() {
        return bancoseleccionado;
    }

    public void setBancoseleccionado(Long bancoseleccionado) {
        this.bancoseleccionado = bancoseleccionado;
    }

    public List<BancoOrigenModel> getBancoOrigenList() {
        return bancoOrigenList;
    }

    public void setBancoOrigenList(List<BancoOrigenModel> bancoOrigenList) {
        this.bancoOrigenList = bancoOrigenList;
    }

    public LazyModel<Recaudacion> getLazyAjuste() {
        return lazyAjuste;
    }

    public void setLazyAjuste(LazyModel<Recaudacion> lazyAjuste) {
        this.lazyAjuste = lazyAjuste;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public List<Recaudacion> getRecaudaciones() {
        return recaudaciones;
    }

    public void setRecaudaciones(List<Recaudacion> recaudaciones) {
        this.recaudaciones = recaudaciones;
    }

    public List<CuentaContable> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaContable> cuentas) {
        this.cuentas = cuentas;
    }

    public String getCodigoAjuste() {
        return codigoAjuste;
    }

    public void setCodigoAjuste(String codigoAjuste) {
        this.codigoAjuste = codigoAjuste;
    }

    public CatalogoItem getAjusteTipoSelect() {
        return ajusteTipoSelect;
    }

    public void setAjusteTipoSelect(CatalogoItem ajusteTipoSelect) {
        this.ajusteTipoSelect = ajusteTipoSelect;
    }

    public List<CatalogoItem> getTipoAjusteList() {
        return tipoAjusteList;
    }

    public void setTipoAjusteList(List<CatalogoItem> tipoAjusteList) {
        this.tipoAjusteList = tipoAjusteList;
    }

    public Boolean getRenderedCodigo() {
        return renderedCodigo;
    }

    public void setRenderedCodigo(Boolean renderedCodigo) {
        this.renderedCodigo = renderedCodigo;
    }
}
