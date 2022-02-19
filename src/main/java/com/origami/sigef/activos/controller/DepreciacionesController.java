/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.DepreciacionesService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.Depreciaciones;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Named(value = "depreciacionesView")
@ViewScoped
public class DepreciacionesController implements Serializable {

    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private DepreciacionesService depreciacionesService;
    @Inject
    private ServletSession servletSession;

    /*Variables Auxiliares*/
    private String tipoDepreciacion;
    private Date fechaDepreciacionDesde;
    private Date fechaDepreciacion;
    private Boolean deshabilitarColectivo;
    private Boolean deshabilitarIndividual;
    private Boolean deshabilitarResumen;
    private Boolean porPeriodo;

    /*Objetos*/
    private CuentaContable cuentaContableSeleccionado;
    private BienesItem bienSeleccionado;
    private BienesItem grupoSeleccionado;
    private OpcionBusqueda opcionBusqueda;

    /*Lista*/
    private List<CuentaContable> cuentaContableList;
    private List<BienesItem> bienesItemList;
    private List<BienesItem> grupoBienesList;

    /*LazyModel*/
    private LazyModel<BienesItem> bienesItemLazy;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        restablecerView();
    }

    public void metodoPorPeriodo() {
        if (porPeriodo) {
            String fecha = "01/01/" + opcionBusqueda.getAnio();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                fechaDepreciacionDesde = dateFormat.parse(fecha);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        } else {
            fechaDepreciacionDesde = null;
        }
    }

    public void añadirCuenta(CuentaContable cuenta) {
        this.cuentaContableSeleccionado = cuenta;
        PrimeFaces.current().executeScript("PF('cuentaContableDlg').hide()");
        PrimeFaces.current().ajax().update("colectivoGrid");
    }

    public void openDlgCuenta() {
        cuentaContableList = cuentaContableService.getCuentaMovimiento(opcionBusqueda.getAnio());
        PrimeFaces.current().executeScript("PF('cuentaContableDlg').show()");
        PrimeFaces.current().ajax().update("cuentaContableTable");
    }

    public void añadirGrupo(BienesItem grupo) {
        this.grupoSeleccionado = grupo;
        PrimeFaces.current().executeScript("PF('grupoInventarioDlg').hide()");
        PrimeFaces.current().ajax().update("grupoGrid");
    }

    public void openDlgGrupoBienes() {
        grupoBienesList = bienesItemService.getListadoBienes(cuentaContableSeleccionado, false);
        PrimeFaces.current().executeScript("PF('grupoInventarioDlg').show()");
        PrimeFaces.current().ajax().update("grupoInventarioTable");
    }

    public void generarDepreciacion() {
        if (cuentaContableSeleccionado.getId() != null) {
            if (getFechaDepreciacion() != null) {
                if (grupoSeleccionado.getId() != null) {
                    bienesItemList = bienesItemService.getListadoBienesClasificados(cuentaContableSeleccionado, grupoSeleccionado);
                } else {
                    bienesItemList = bienesItemService.getListadoBienes(cuentaContableSeleccionado, true);
                }
                depreciacionesService.eliminandoRegistros();
                for (BienesItem bienes : bienesItemList) {
                    BigDecimal depreciacionAcumulada;
                    BigDecimal valorResidual;
                    BigDecimal depreciacionAnual;
                    double dias = 0;
                    Depreciaciones depreciaciones = new Depreciaciones();
                    if (porPeriodo) {
                        if (bienes.getFechaAdquisicion().before(fechaDepreciacionDesde)) {
                            dias = diferenciaDeFechas(fechaDepreciacionDesde);
                        } else {
                            dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                        }
                    } else {
                        dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                    }
                    if ((dias / 365) > bienes.getVidaUtil()) {
                        dias = bienes.getVidaUtil() * 365;
                    } else {
                        if (porPeriodo) {
                            if (bienes.getFechaAdquisicion().before(fechaDepreciacionDesde)) {
                                dias = diferenciaDeFechas(fechaDepreciacionDesde);
                            } else {
                                dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                            }
                        } else {
                            dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                        }
                    }
                    valorResidual = bienes.getValorTotal().multiply(new BigDecimal(0.1));
                    depreciacionAnual = (bienes.getValorTotal().subtract(valorResidual)).divide(new BigDecimal(bienes.getVidaUtil()), 2, RoundingMode.HALF_DOWN);
                    if (bienes.getVidaUtilBien().getVidaUtil() != null) {
                        /*Formula con los años de vida util del UTPE*/
                        depreciacionAcumulada = depreciacionAnual.multiply(new BigDecimal(dias / 365).setScale(2, RoundingMode.HALF_DOWN)).setScale(2, RoundingMode.HALF_DOWN);
                    } else {
                        /*Formula con los años de vida util dado en las normas*/
                        depreciacionAcumulada = depreciacionAnual.multiply(bienes.getUtpe()).setScale(2, RoundingMode.HALF_DOWN);
                    }
                    depreciaciones.setBien(bienes);
                    depreciaciones.setValorResidual(valorResidual);
                    depreciaciones.setDepreciacionAcumulada(depreciacionAcumulada);
                    depreciaciones.setFechaDepreciacion(getFechaDepreciacion());
                    depreciacionesService.create(depreciaciones);
                }
                imprimirDepreciacion();
                restablecerView();
            } else {
                JsfUtil.addWarningMessage("AVISO", "Se debe seleccionar una fecha para realizar la depreciación");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Se debe seleccionar una cuenta contable antes de generar la depreciación");
        }
    }

    private void imprimirDepreciacion() {
        if (cuentaContableSeleccionado.getId() != null) {
            double totalResidual = 0;
            double totalLibro = 0;
            double totalDepreciacion = 0;
            double totalValorContable = 0;
            List<Depreciaciones> depreciacionesList = depreciacionesService.findAll();
            for (Depreciaciones depreciacion : depreciacionesList) {
                totalResidual = totalResidual + depreciacion.getValorResidual().doubleValue();
                totalDepreciacion = totalDepreciacion + depreciacion.getDepreciacionAcumulada().doubleValue();
                totalValorContable = totalValorContable + depreciacion.getBien().getValorTotal().doubleValue();
                totalLibro = totalLibro + (depreciacion.getBien().getValorTotal().doubleValue() - depreciacion.getDepreciacionAcumulada().doubleValue());
            }
            servletSession.addParametro("totalResidual", Math.rint(totalResidual * 100) / 100);
            servletSession.addParametro("totalDepreciacion", Math.rint(totalDepreciacion * 100) / 100);
            servletSession.addParametro("totalValorContable", Math.rint(totalValorContable * 100) / 100);
            servletSession.addParametro("totalLibro", Math.rint(totalLibro * 100) / 100);
            servletSession.addParametro("codigo_cuenta", cuentaContableSeleccionado.getCodigo());
            servletSession.addParametro("nombre_cuenta", cuentaContableSeleccionado.getNombre());
            servletSession.setNombreReporte("DepreciacionesColectivo");
            servletSession.setNombreSubCarpeta("Depreciaciones");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addWarningMessage("AVISO", "No se puede generar el reporte");
        }
    }

    private double diferenciaDeFechas(Date fechaAdquisicion) {
        int dias = 0;
        return dias = (int) ((fechaDepreciacion.getTime() - fechaAdquisicion.getTime()) / 86400000);
    }

    public void openDlgBienes() {
        this.bienesItemLazy = new LazyModel<>(BienesItem.class);
        this.bienesItemLazy.getSorteds().put("id", "ASC");
        this.bienesItemLazy.getFilterss().put("estado", true);
        this.bienesItemLazy.getFilterss().put("itemBienBoolean", true);
        this.bienesItemLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        this.bienesItemLazy.getFilterss().put("tipoBien.codigo", "BLD");
        PrimeFaces.current().executeScript("PF('BienesDLG').show()");
        PrimeFaces.current().ajax().update("bienesRegistradoTable");
    }

    public void añadirBien(BienesItem bien) {
        this.bienSeleccionado = bien;
        PrimeFaces.current().executeScript("PF('BienesDLG').hide()");
        PrimeFaces.current().ajax().update("bienesGrid");
    }

    public void generarDepreciacionIndividual() {
        if (bienSeleccionado.getId() != null) {
            if (getFechaDepreciacion() != null) {
                depreciacionesService.eliminandoRegistros();
                BigDecimal depreciacionAcumulada;
                BigDecimal valorResidual;
                BigDecimal depreciacionAnual;
                double dias = 0;
                Depreciaciones depreciaciones = new Depreciaciones();
                if (porPeriodo) {
                    if (bienSeleccionado.getFechaAdquisicion().before(fechaDepreciacionDesde)) {
                        dias = diferenciaDeFechas(fechaDepreciacionDesde);
                    } else {
                        dias = diferenciaDeFechas(bienSeleccionado.getFechaAdquisicion());
                    }
                } else {
                    dias = diferenciaDeFechas(bienSeleccionado.getFechaAdquisicion());
                }
                if ((dias / 365) > bienSeleccionado.getVidaUtil()) {
                    dias = bienSeleccionado.getVidaUtil() * 365;
                } else {
                    if (porPeriodo) {
                        if (bienSeleccionado.getFechaAdquisicion().before(fechaDepreciacionDesde)) {
                            dias = diferenciaDeFechas(fechaDepreciacionDesde);
                        } else {
                            dias = diferenciaDeFechas(bienSeleccionado.getFechaAdquisicion());
                        }
                    } else {
                        dias = diferenciaDeFechas(bienSeleccionado.getFechaAdquisicion());
                    }
                }
                valorResidual = bienSeleccionado.getValorTotal().multiply(new BigDecimal(0.1));
                depreciacionAnual = (bienSeleccionado.getValorTotal().subtract(valorResidual)).divide(new BigDecimal(bienSeleccionado.getVidaUtil()), 2, RoundingMode.HALF_DOWN);
                if (bienSeleccionado.getVidaUtilBien().getVidaUtil() != null) {
                    /*Formula con los años de vida util del UTPE*/
                    depreciacionAcumulada = depreciacionAnual.multiply(new BigDecimal(dias / 365).setScale(2, RoundingMode.HALF_DOWN)).setScale(2, RoundingMode.HALF_DOWN);
                } else {
                    /*Formula con los años de vida util dado en las normas*/
                    depreciacionAcumulada = depreciacionAnual.multiply(bienSeleccionado.getUtpe()).setScale(2, RoundingMode.HALF_DOWN);
                }
                depreciaciones.setBien(bienSeleccionado);
                depreciaciones.setValorResidual(valorResidual);
                depreciaciones.setDepreciacionAcumulada(depreciacionAcumulada);
                depreciaciones.setFechaDepreciacion(getFechaDepreciacion());
                depreciacionesService.create(depreciaciones);
                imprimirDepreciacionIndividual();
                restablecerView();
            } else {
                JsfUtil.addWarningMessage("AVISO", "Se debe seleccionar una fecha para realizar la depreciación");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Se debe seleccionar un bien antes de generar la depreciación");
        }
    }

    private void imprimirDepreciacionIndividual() {
        if (bienSeleccionado.getId() != null) {
            double totalResidual = 0;
            double totalLibro = 0;
            double totalDepreciacion = 0;
            double totalValorContable = 0;
            List<Depreciaciones> depreciacionesList = depreciacionesService.findAll();
            for (Depreciaciones depreciacion : depreciacionesList) {
                totalResidual = totalResidual + depreciacion.getValorResidual().doubleValue();
                totalDepreciacion = totalDepreciacion + depreciacion.getDepreciacionAcumulada().doubleValue();
                totalValorContable = totalValorContable + depreciacion.getBien().getValorTotal().doubleValue();
                totalLibro = totalLibro + (depreciacion.getBien().getValorTotal().doubleValue() - depreciacion.getDepreciacionAcumulada().doubleValue());
            }
            servletSession.addParametro("totalResidual", Math.rint(totalResidual * 100) / 100);
            servletSession.addParametro("totalDepreciacion", Math.rint(totalDepreciacion * 100) / 100);
            servletSession.addParametro("totalValorContable", Math.rint(totalValorContable * 100) / 100);
            servletSession.addParametro("totalLibro", Math.rint(totalLibro * 100) / 100);
            servletSession.setNombreReporte("DepreciacionesIndividual");
            servletSession.setNombreSubCarpeta("Depreciaciones");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addWarningMessage("AVISO", "No se puede generar el reporte");
        }
    }

    private void restablecerView() {
        this.tipoDepreciacion = null;
        this.fechaDepreciacionDesde = null;
        this.fechaDepreciacion = new Date();
        this.porPeriodo = Boolean.FALSE;
        this.deshabilitarResumen = Boolean.FALSE;
        this.deshabilitarColectivo = Boolean.TRUE;
        this.deshabilitarIndividual = Boolean.TRUE;
        this.cuentaContableSeleccionado = new CuentaContable();
        this.bienSeleccionado = new BienesItem();
        this.grupoSeleccionado = new BienesItem();
    }

    public void actualizarFields() {
        if (getTipoDepreciacion() != null) {
            if (getTipoDepreciacion().equals("COLECTIVO")) {
                deshabilitarResumen = Boolean.FALSE;
                deshabilitarColectivo = Boolean.FALSE;
                deshabilitarIndividual = Boolean.TRUE;
            } else if (getTipoDepreciacion().equals("RESUMEN")) {
                deshabilitarResumen = Boolean.TRUE;
                deshabilitarIndividual = Boolean.TRUE;
                deshabilitarColectivo = Boolean.TRUE;
            } else {
                deshabilitarResumen = Boolean.FALSE;
                deshabilitarIndividual = Boolean.FALSE;
                deshabilitarColectivo = Boolean.TRUE;
            }
        } else {
            deshabilitarResumen = Boolean.FALSE;
            deshabilitarIndividual = Boolean.TRUE;
            deshabilitarColectivo = Boolean.TRUE;
        }

    }

    public void imprimirResumen() {
        if (getFechaDepreciacion() != null) {
            depreciacionesService.eliminandoRegistros();
            List<CuentaContable> listaCuentaContable = depreciacionesService.getListaCuentasContable(opcionBusqueda.getAnio(), "BLD");
            for (CuentaContable cuenta : listaCuentaContable) {
                Depreciaciones depreciaciones = new Depreciaciones();
                List<BienesItem> bienesAsociados = depreciacionesService.getListaBienes(cuenta, opcionBusqueda.getAnio());
                BigDecimal total_contable = BigDecimal.ZERO;
                BigDecimal total_depreciacion = BigDecimal.ZERO;
                for (BienesItem bienes : bienesAsociados) {
                    System.out.println("Bien: "+bienes.getCodigoBienAgrupado());
                    BigDecimal valorResidual;
                    double dias = 0;
                    BigDecimal depreciacionAnual;
                    BigDecimal depreciacionAcumulada;
                    if (porPeriodo) {
                        if (bienes.getFechaAdquisicion().before(fechaDepreciacionDesde)) {
                            dias = diferenciaDeFechas(fechaDepreciacionDesde);
                        } else {
                            dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                        }
                    } else {
                        dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                    }
                    if ((dias / 365) > bienes.getVidaUtil()) {
                        dias = bienes.getVidaUtil() * 365;
                    } else {
                        if (porPeriodo) {
                            if (bienes.getFechaAdquisicion().before(fechaDepreciacionDesde)) {
                                dias = diferenciaDeFechas(fechaDepreciacionDesde);
                            } else {
                                dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                            }
                        } else {
                            dias = diferenciaDeFechas(bienes.getFechaAdquisicion());
                        }
                    }
                    valorResidual  = bienes.getValorTotal().multiply(new BigDecimal(0.1));
                    depreciacionAnual = (bienes.getValorTotal().subtract(valorResidual)).divide(new BigDecimal(bienes.getVidaUtil()), 2, RoundingMode.HALF_DOWN);
                    if (bienes.getVidaUtilBien().getVidaUtil() != null) {
                        /*Formula con los años de vida util del UTPE*/
                        depreciacionAcumulada = depreciacionAnual.multiply(new BigDecimal(dias / 365).setScale(2, RoundingMode.HALF_DOWN)).setScale(2, RoundingMode.HALF_DOWN);
                    } else {
                        /*Formula con los años de vida util dado en las normas*/
                        depreciacionAcumulada = depreciacionAnual.multiply(bienes.getUtpe()).setScale(2, RoundingMode.HALF_DOWN);
                    }
                    total_contable = total_contable.add(bienes.getValorTotal()).setScale(2, RoundingMode.HALF_DOWN);;
                    total_depreciacion = total_depreciacion.add(depreciacionAcumulada).setScale(2, RoundingMode.HALF_DOWN);;
                }
                depreciaciones.setCuentaContable(cuenta);
                depreciaciones.setFechaDepreciacion(fechaDepreciacion);
                depreciaciones.setDepreciacionAcumulada(total_depreciacion);
                depreciaciones.setTotalValorContable(total_contable);
                depreciaciones.setTotalValorLibro(total_contable.subtract(total_depreciacion));
                depreciacionesService.create(depreciaciones);
            }
            restablecerView();
            servletSession.setNombreReporte("DepreciacionResumen");
            servletSession.setNombreSubCarpeta("Depreciaciones");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addWarningMessage("AVISO", "Nesecita ingresar una fecha de depreciación");
        }

    }

    //<editor-fold defaultstate="collapsed" desc="GET - SET ">
    public Boolean getPorPeriodo() {
        return porPeriodo;
    }

    public void setPorPeriodo(Boolean porPeriodo) {
        this.porPeriodo = porPeriodo;
    }

    public Date getFechaDepreciacionDesde() {
        return fechaDepreciacionDesde;
    }

    public void setFechaDepreciacionDesde(Date fechaDepreciacionDesde) {
        this.fechaDepreciacionDesde = fechaDepreciacionDesde;
    }

    public BienesItemService getBienesItemService() {
        return bienesItemService;
    }

    public void setBienesItemService(BienesItemService bienesItemService) {
        this.bienesItemService = bienesItemService;
    }

    public CuentaContableService getCuentaContableService() {
        return cuentaContableService;
    }

    public void setCuentaContableService(CuentaContableService cuentaContableService) {
        this.cuentaContableService = cuentaContableService;
    }

    public String getTipoDepreciacion() {
        return tipoDepreciacion;
    }

    public void setTipoDepreciacion(String tipoDepreciacion) {
        this.tipoDepreciacion = tipoDepreciacion;
    }

    public Date getFechaDepreciacion() {
        return fechaDepreciacion;
    }

    public void setFechaDepreciacion(Date fechaDepreciacion) {
        this.fechaDepreciacion = fechaDepreciacion;
    }

    public Boolean getDeshabilitarColectivo() {
        return deshabilitarColectivo;
    }

    public void setDeshabilitarColectivo(Boolean deshabilitarColectivo) {
        this.deshabilitarColectivo = deshabilitarColectivo;
    }

    public Boolean getDeshabilitarIndividual() {
        return deshabilitarIndividual;
    }

    public void setDeshabilitarIndividual(Boolean deshabilitarIndividual) {
        this.deshabilitarIndividual = deshabilitarIndividual;
    }

    public CuentaContable getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(CuentaContable cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

    public BienesItem getBienSeleccionado() {
        return bienSeleccionado;
    }

    public void setBienSeleccionado(BienesItem bienSeleccionado) {
        this.bienSeleccionado = bienSeleccionado;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<CuentaContable> getCuentaContableList() {
        return cuentaContableList;
    }

    public void setCuentaContableList(List<CuentaContable> cuentaContableList) {
        this.cuentaContableList = cuentaContableList;
    }

    public List<BienesItem> getBienesItemList() {
        return bienesItemList;
    }

    public void setBienesItemList(List<BienesItem> bienesItemList) {
        this.bienesItemList = bienesItemList;
    }

    public BienesItem getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(BienesItem grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public List<BienesItem> getGrupoBienesList() {
        return grupoBienesList;
    }

    public void setGrupoBienesList(List<BienesItem> grupoBienesList) {
        this.grupoBienesList = grupoBienesList;
    }

    public LazyModel<BienesItem> getBienesItemLazy() {
        return bienesItemLazy;
    }

    public void setBienesItemLazy(LazyModel<BienesItem> bienesItemLazy) {
        this.bienesItemLazy = bienesItemLazy;
    }

    public Boolean getDeshabilitarResumen() {
        return deshabilitarResumen;
    }

    public void setDeshabilitarResumen(Boolean deshabilitarResumen) {
        this.deshabilitarResumen = deshabilitarResumen;
    }
//</editor-fold>  
}
