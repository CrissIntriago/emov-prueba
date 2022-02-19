/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
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
@Named(value = "contTesoreriaEmisionesView")
@ViewScoped
public class ContTesoreriaEmisionesController implements Serializable {

    @Inject
    private ContRegistroContable contRegistroContable;
    @Inject
    private CatalogoItemService catalogoItemService;

    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneral contDiarioGeneral;
    private FinaRenTipoLiquidacion tipoSeleccionado;

    private Date fechaDesde, fechaHasta, fechaRegistro;

    private List<ContDiarioGeneral> contDiarioGeneralList;
    private List<ContDiarioGeneral> contDiarioGeneralSeleccionadoList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleList;
    private List<Short> listaPeriodo;
    private List<Long> idModulos;
    private List<FinaRenTipoLiquidacion> tipoLiquidacionList;

    private BigDecimal totalDebe, totalHaber, totalComprometido, totalDevengado, totalEjecutado, diferencia;

    @PostConstruct
    public void init() {
        listaPeriodo = catalogoItemService.getPeriodo();
        tipoLiquidacionList = contRegistroContable.getTipoLiquidacionList();
        cleanForm();
        updateFecha();
        updateListDiario();
    }

    public void cleanForm() {
        opcionBusqueda = new OpcionBusqueda();
        contDiarioGeneralList = new ArrayList<>();
        contDiarioGeneralSeleccionadoList = new ArrayList<>();
        contDiarioGeneralDetalleList = new ArrayList<>();
        idModulos = new ArrayList<>();
        contDiarioGeneral = new ContDiarioGeneral();
        fechaRegistro = new Date();
        calcularTotales();
        loadDiarioGeneral();
    }

    public void updateListDiario() {
        if (opcionBusqueda.getAnio() != null && (fechaDesde.before(fechaHasta) || fechaDesde.equals(fechaDesde))) {
            Boolean accion = Boolean.TRUE;
            if (tipoSeleccionado != null) {
                if (tipoSeleccionado.getId() != null) {
                    if (tipoSeleccionado.getPrefijo() != null) {
                        if (tipoSeleccionado.getPrefijo().equals("PRU") || tipoSeleccionado.getPrefijo().equals("PDR")) {
                            contDiarioGeneralList = new ArrayList<>();
                            contDiarioGeneralDetalleList = contRegistroContable.getTesoreriaPredios(MOD_CONTABILIDAD.MOD_TES_EMISION, fechaDesde, fechaHasta, tipoSeleccionado, opcionBusqueda.getAnio());
                            accion = Boolean.FALSE;
                        }
                    } else {
                        contDiarioGeneralList = contRegistroContable.getTesoreriaList(MOD_CONTABILIDAD.MOD_TES_EMISION, fechaDesde, fechaHasta, tipoSeleccionado, true);
                        contDiarioGeneralDetalleList = new ArrayList<>();
                    }
                } else {
                    contDiarioGeneralList = contRegistroContable.getTesoreriaList(MOD_CONTABILIDAD.MOD_TES_EMISION, fechaDesde, fechaHasta, tipoSeleccionado, false);
                    contDiarioGeneralDetalleList = new ArrayList<>();
                }
                if (accion) {
                    if (contDiarioGeneralList.isEmpty()) {
                        JsfUtil.addWarningMessage("AVISO!!!", "No existen datos con los parametros especificados o ya se encuentran contabilizados");
                    }
                    PrimeFaces.current().ajax().update("diarioGeneralTable");
                } else {
                    if (contDiarioGeneralDetalleList.isEmpty()) {
                        JsfUtil.addWarningMessage("AVISO!!!", "No existen datos con los parametros especificados o ya se encuentran contabilizados");
                    }
                    calcularTotales();
                    PrimeFaces.current().ajax().update("registroContableDetalleTable");
                }
            } else {
                contDiarioGeneralList = contRegistroContable.getTesoreriaList(MOD_CONTABILIDAD.MOD_TES_EMISION, fechaDesde, fechaHasta, tipoSeleccionado, false);
                contDiarioGeneralDetalleList = new ArrayList<>();
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Seleccione un período o verifique el rango de fecha");
            contDiarioGeneralList = new ArrayList<>();
            contDiarioGeneralSeleccionadoList = new ArrayList<>();
            contDiarioGeneralDetalleList = new ArrayList<>();
        }
    }

    public void updateFecha() {
        if (opcionBusqueda.getAnio() != null) {
            Calendar calendar = Calendar.getInstance();
            int mes = Utils.getMes(new Date()) - 1;
            calendar.set(opcionBusqueda.getAnio(), mes, 1);
            fechaDesde = calendar.getTime();
            fechaHasta = new Date();
        } else {
            fechaDesde = new Date();
            fechaHasta = new Date();
        }
    }

    public void updateListDetalle() {
        if (!contDiarioGeneralSeleccionadoList.isEmpty()) {
            idModulos = new ArrayList<>();
            for (ContDiarioGeneral diario : contDiarioGeneralSeleccionadoList) {
                idModulos.add(diario.getId());
            }
            contDiarioGeneralDetalleList = contRegistroContable.getTesoreriaDetalle(idModulos, opcionBusqueda.getAnio());
            if (!contDiarioGeneralDetalleList.isEmpty()) {
                calcularTotales();
            }
        } else {
            contDiarioGeneralDetalleList = new ArrayList<>();
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha cargado el detalle de forma correcta");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void calcularTotales() {
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        diferencia = BigDecimal.ZERO;
        for (ContDiarioGeneralDetalle detalle : contDiarioGeneralDetalleList) {
            if (detalle.getDebe() != null) {
                totalDebe = totalDebe.add(detalle.getDebe().setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
            }
            if (detalle.getHaber() != null) {
                totalHaber = totalHaber.add(detalle.getHaber().setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
            }
            if (detalle.getDevengado() != null) {
                totalDevengado = totalDevengado.add(detalle.getDevengado().setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
            }
            if (detalle.getEjecutado() != null) {
                totalEjecutado = totalEjecutado.add(detalle.getEjecutado().setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        if (Utils.redondearDosDecimales(totalDebe.doubleValue()) == Utils.redondearDosDecimales(totalHaber.doubleValue())) {
            contDiarioGeneral.setCuadrado(Boolean.TRUE);
        }
        diferencia = totalDebe.subtract(totalHaber.setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
    }

    public void save() {
        loadDiarioGeneral();
        String msj = contRegistroContable.validaciones(contDiarioGeneral, contDiarioGeneralDetalleList);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        List<Long> temp;
        if (!contDiarioGeneralList.isEmpty()) {
            temp = contRegistroContable.findRelacionModulos(contDiarioGeneralSeleccionadoList, MOD_CONTABILIDAD.MOD_TES_EMISION);
        } else {
            temp = contRegistroContable.findRelacionModulosPredios(MOD_CONTABILIDAD.MOD_TES_EMISION, fechaDesde, fechaHasta, tipoSeleccionado);
        }
        contDiarioGeneral = contRegistroContable.create(contDiarioGeneral, contDiarioGeneralDetalleList, temp, Boolean.TRUE);
        if (contDiarioGeneralSeleccionadoList.isEmpty()) {
            contRegistroContable.updateRegistroContablePredios(MOD_CONTABILIDAD.MOD_TES_EMISION, fechaDesde, fechaHasta, tipoSeleccionado);
        } else {
            for (ContDiarioGeneral cabecera : contDiarioGeneralSeleccionadoList) {
                cabecera.setEstado(Boolean.FALSE);
                cabecera.setRevisado(Boolean.TRUE);
                contRegistroContable.editRegistroContable(contDiarioGeneral);
            }
        }
        JsfUtil.executeJS("PF('dlgConfirmacion').show()");
        PrimeFaces.current().ajax().update("dlgConfirmacionForm");
        JsfUtil.addWarningMessage("INFO!!!", "Se ha creado correctamente");
    }

    private void loadDiarioGeneral() {
        //CABECERA DEL DIARIO
        if (contDiarioGeneralSeleccionadoList != null) {
            for (ContDiarioGeneral cabecera : contDiarioGeneralSeleccionadoList) {
                contDiarioGeneral.setDescripcion(cabecera.getDescripcion().concat(", "));
            }
        }
        contDiarioGeneral.setDebe(totalDebe);
        contDiarioGeneral.setHaber(totalHaber);
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneral.setFechaRegistro(fechaRegistro);
        contDiarioGeneral.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario"));
        contDiarioGeneral.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_financiero"));
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_TES_EMISION);
    }

    public void openConfirmacion(int code, String tipoDocumento) {
        switch (code) {
            case 1:
                JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/manual");
                break;
            case 2:
                System.out.println("CODE: " + contDiarioGeneral.getId());
                contRegistroContable.ContabilidadImprimirReporte(contDiarioGeneral, tipoDocumento);
                break;
            default:
                cleanForm();
                JsfUtil.executeJS("PF('dlgConfirmacion').hide()");
                PrimeFaces.current().ajax().update("formMain");
                break;
        }
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
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

    public List<ContDiarioGeneral> getContDiarioGeneralList() {
        return contDiarioGeneralList;
    }

    public void setContDiarioGeneralList(List<ContDiarioGeneral> contDiarioGeneralList) {
        this.contDiarioGeneralList = contDiarioGeneralList;
    }

    public List<ContDiarioGeneral> getContDiarioGeneralSeleccionadoList() {
        return contDiarioGeneralSeleccionadoList;
    }

    public void setContDiarioGeneralSeleccionadoList(List<ContDiarioGeneral> contDiarioGeneralSeleccionadoList) {
        this.contDiarioGeneralSeleccionadoList = contDiarioGeneralSeleccionadoList;
    }

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetalleList() {
        return contDiarioGeneralDetalleList;
    }

    public void setContDiarioGeneralDetalleList(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleList) {
        this.contDiarioGeneralDetalleList = contDiarioGeneralDetalleList;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public ContDiarioGeneral getContDiarioGeneral() {
        return contDiarioGeneral;
    }

    public void setContDiarioGeneral(ContDiarioGeneral contDiarioGeneral) {
        this.contDiarioGeneral = contDiarioGeneral;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(BigDecimal totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidacionList() {
        return tipoLiquidacionList;
    }

    public void setTipoLiquidacionList(List<FinaRenTipoLiquidacion> tipoLiquidacionList) {
        this.tipoLiquidacionList = tipoLiquidacionList;
    }

    public FinaRenTipoLiquidacion getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(FinaRenTipoLiquidacion tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

}
