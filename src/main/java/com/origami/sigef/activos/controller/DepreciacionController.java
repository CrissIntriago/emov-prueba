/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.activos.entities.DepreciacionDetalle;
import com.origami.sigef.activos.service.DepreciacionDetalleService;
import com.origami.sigef.activos.service.DepreciacionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author jintr
 */
@Named(value = "depreciacionView")
@ViewScoped
public class DepreciacionController implements Serializable {

    @Inject
    private DepreciacionService depreciacionService;
    @Inject
    private DepreciacionDetalleService depreciacionDetalleService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;

    private List<Depreciacion> depreciacionList;
    private List<DepreciacionDetalle> depreciacionDetalleList;
    private List<DepreciacionDetalle> depreciacionDetalleListTemp;

    private Depreciacion depreciacion, depreciacionSeleccionada;

    private double totalValorResidual;
    private double totalDepreciacion;
    private double totalValorContable;

    private Date fechaAuxDesde;
    private Date fechaAuxHasta;

    @PostConstruct
    public void init() {
        actualizarDepreciacionDetalle();
        cancelar();
    }

    private void actualizarDepreciacionDetalle() {
        depreciacionList = depreciacionService.getList(true);
    }

    public void depreciacionSeleccionada() {
        depreciacion = depreciacionSeleccionada;
        depreciacionDetalleList = new ArrayList<>();
        depreciacionDetalleListTemp = new ArrayList<>();
        List<DepreciacionDetalle> auxList = depreciacionDetalleService.getList(depreciacion);
        for (DepreciacionDetalle item : auxList) {
            depreciacionDetalleList.add(item);
        }
        fechaAuxDesde = depreciacion.getFechaDesde();
        fechaAuxHasta = depreciacion.getFechaHasta();
        calcularTotal();
    }

    private void actualizarDepreciacion() {
        depreciacion.setUserModifica(userSession.getNameUser());
        depreciacion.setFechaModifica(new Date());
        depreciacionService.edit(depreciacion);
    }

    public void save() {
        Boolean edit = depreciacion.getId() != null;
        if (edit) {
            actualizarDepreciacion();
            //elimino detalle
            for (DepreciacionDetalle item : depreciacionDetalleListTemp) {
                item.setEstado(Boolean.FALSE);
                depreciacionDetalleService.edit(item);
            }
            //agrego detalle
            for (DepreciacionDetalle item : depreciacionDetalleList) {
                if (item.getId() != null) {
                    depreciacionDetalleService.edit(item);
                } else {
                    item.setDepreciacion(depreciacion);
                    depreciacionDetalleService.create(item);
                }
            }
        } else {
            depreciacion.setTotalBienes(depreciacionDetalleList.size());
            depreciacion.setSecuencial(depreciacionService.getIndice());
            depreciacion.setPeriodo(Utils.getAnio(depreciacion.getFechaHasta()).shortValue());
            depreciacion.setCodigoDepreciacion("DEPRECIACIÓN - " + depreciacion.getPeriodo() + " - " + Utils.completarCadenaConCeros(depreciacion.getSecuencial().toString(), 6));
            depreciacion.setUserCreacion(userSession.getNameUser());
            depreciacion.setFechaCreacion(new Date());
            depreciacion = depreciacionService.create(depreciacion);
            for (DepreciacionDetalle item : depreciacionDetalleList) {
                item.setDepreciacion(depreciacion);
                depreciacionDetalleService.create(item);
            }
        }
        actualizarDepreciacionDetalle();
        cancelar();
        JsfUtil.addSuccessMessage("INFO!!", (edit ? "editado" : " registrado") + " con éxito");
    }

    public void cancelar() {
        depreciacionDetalleListTemp = new ArrayList<>();
        depreciacionDetalleList = new ArrayList<>();
        depreciacion = new Depreciacion();
        depreciacionSeleccionada = new Depreciacion();
        fechaAuxDesde = null;
        fechaAuxHasta = null;
        calcularTotal();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void deleteDepreciacion() {
        depreciacion.setEstado(Boolean.FALSE);
        actualizarDepreciacion();
        actualizarDepreciacionDetalle();
        depreciacion = new Depreciacion();
        depreciacionDetalleList = new ArrayList<>();
        depreciacion.setFechaHasta(new Date());
        JsfUtil.addInformationMessage("INFO!!", "La depreciacion fue eliminado correctamente");
    }

    public void deleteDepreciacionDetalle(DepreciacionDetalle item, int indice) {
        if (item.getId() != null) {
            depreciacionDetalleList.remove(item);
            depreciacionDetalleListTemp.add(item);
        } else {
            depreciacionDetalleList.remove(indice);
        }
        totalValorResidual = totalValorResidual - redondearDosDecimales(item.getValorResidual().doubleValue());
        totalDepreciacion = totalDepreciacion - redondearDosDecimales(item.getDepreciacionAcumulada().doubleValue());
        totalValorContable = totalValorContable - redondearDosDecimales(item.getValorContable().doubleValue());
        actualizarSecuencialDetalle();
    }

    private void actualizarSecuencialDetalle() {
        int aux = 1;
        for (DepreciacionDetalle item : depreciacionDetalleList) {
            item.setSecuencial(aux);
            aux += 1;
        }
    }

    private boolean validadorFecha() {
        return depreciacionService.getUltimoRegistro(depreciacion.getFechaDesde());
    }

    public void consultarPeriodos() {
        if (depreciacion.getFechaDesde() != null && depreciacion.getFechaHasta() != null) {
            boolean accionTemp = depreciacion.getId() != null;
            int temp = 0;
            List<BienesItem> aux = new ArrayList<>();
            if (accionTemp) {
                if ((depreciacion.getFechaDesde().compareTo(fechaAuxDesde) != 0) || (depreciacion.getFechaHasta().compareTo(fechaAuxHasta) != 0)) {
                    actualizarRegistros();
                }
                aux = depreciacionService.getListaBienesAgregados(depreciacion);
                temp = agregarDetalle(aux, depreciacionDetalleList.size() + 1);
            } else {
                //consultamos que al seleccionar la fecha desde no deber ser igual o menor a la fecha hasta del ultimo registro
                if (validadorFecha()) {
                    JsfUtil.addWarningMessage("AVISO!!", "El periodo seleccionado debe ser mayor al del ultimo registro");
                    return;
                }
                depreciacionDetalleList = new ArrayList<>();
                aux = depreciacionService.getListaBienes(depreciacion.getFechaHasta());
                temp = agregarDetalle(aux, 1);
            }
            calcularTotal();
//            System.out.println("depreciacionDetalleList " + depreciacionDetalleList);
            PrimeFaces.current().ajax().update("depreciacionDetalle");
            if (temp == 0) {
                JsfUtil.addSuccessMessage("INFO!!!", "Se han cargado correctamente los datos");
            } else {
                JsfUtil.addWarningMessage("AVISO!!", "Hay " + temp + " bienes sin cuenta de depreciacion asociado");
            }
        }
    }

    private int agregarDetalle(List<BienesItem> aux, int cont) {
        int i = cont;
        int temp = 0;
        for (BienesItem item : aux) {
            DepreciacionDetalle detalle = new DepreciacionDetalle();
            if (item.getVidaUtilBien() != null) {
                detalle.setCuentaContable(item.getVidaUtilBien().getCtaDepreciacion());
            } else {
                temp += 1;
            }
            detalle.setIdBien(item);
            detalle.setSecuencial(i);
            detalle.setEstadoBien(item.getEstadoBien().getTexto().toUpperCase());
            calcularValores(detalle);
            i += 1;
            depreciacionDetalleList.add(detalle);
        }
        return temp;
    }

    private void actualizarRegistros() {
        for (DepreciacionDetalle item : depreciacionDetalleList) {
            calcularValores(item);
        }
    }

    private void calcularValores(DepreciacionDetalle detalle) {
        BienesItem item = detalle.getIdBien();
        //Calcular valores
        double depreciacionAnual = 0;
        double depreciacionAcumulada = 0;
        double valorResidual = 0;
        int dias = 0;
        int vidaUtil = 0;
        int anio = 365;
        boolean accion = false;
        double indiceDepreciacion = 0.1;
        dias = calcularDias(item);
        //determinar vid util
        if (item.getVidaUtilBien() != null) {
            accion = true;
            vidaUtil = item.getVidaUtilBien().getVidaUtil().intValue();
        } else {
            vidaUtil = item.getVidaUtil().intValue();
        }
        //determinar vida util final
        if ((dias / anio) > vidaUtil) {
            dias = vidaUtil * anio;
        } else {
            dias = calcularDias(item);
        }
        //calcular el valor residual
        valorResidual = redondearDosDecimales(item.getValorTotal().doubleValue() * indiceDepreciacion);
        //calcular la depreciacion
        depreciacionAnual = redondearDosDecimales((item.getValorTotal().doubleValue() - valorResidual) / vidaUtil);
        if (accion) {
            /*Formula con los años de vida util dado en las normas*/
            double cociente = redondearDosDecimales((double) dias / (double) anio);
            depreciacionAcumulada = redondearDosDecimales(depreciacionAnual * cociente);
        } else {
            /*Formula con los años de vida util del UTPE*/
            depreciacionAcumulada = redondearDosDecimales(depreciacionAnual * item.getUtpe().intValue());
        }
        detalle.setDepreciacionAcumulada(new BigDecimal(depreciacionAcumulada));
        detalle.setValorContable(new BigDecimal(redondearDosDecimales(item.getValorTotal().doubleValue() - depreciacionAcumulada)));
        detalle.setValorResidual(item.getValorTotal());
    }

    private double redondearDosDecimales(double valor) {
        return Math.round(valor * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    private void calcularTotal() {
        reiniciarTotales();
        for (DepreciacionDetalle item : depreciacionDetalleList) {
            totalValorResidual = redondearDosDecimales(totalValorResidual + Utils.bigdecimalTo2Decimals(item.getValorResidual()).doubleValue());
            totalDepreciacion = redondearDosDecimales(totalDepreciacion + Utils.bigdecimalTo2Decimals(item.getDepreciacionAcumulada()).doubleValue());
            totalValorContable = redondearDosDecimales(totalValorContable + Utils.bigdecimalTo2Decimals(item.getValorContable()).doubleValue());
        }
    }

    private void reiniciarTotales() {
        totalValorResidual = 0;
        totalDepreciacion = 0;
        totalValorContable = 0;
    }

    private int calcularDias(BienesItem item) {
        int result = 0;
        if (item.getFechaAdquisicion().before(depreciacion.getFechaDesde())) {
            result = diferenciaDeFechas(depreciacion.getFechaDesde());
        } else {
            result = diferenciaDeFechas(item.getFechaAdquisicion());
        }
        return result;
    }

    private int diferenciaDeFechas(Date fechaAdquisicion) {
        return (int) ((depreciacion.getFechaHasta().getTime() - fechaAdquisicion.getTime()) / 86400000);
    }

    public void imprimir(String tipoArchivo) {
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        servletSession.addParametro("id_depreciacion", depreciacionSeleccionada.getId());
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreReporte("Depreciacion");
        servletSession.setNombreSubCarpeta("Depreciaciones");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public List<Depreciacion> getDepreciacionList() {
        return depreciacionList;
    }

    public void setDepreciacionList(List<Depreciacion> depreciacionList) {
        this.depreciacionList = depreciacionList;
    }

    public List<DepreciacionDetalle> getDepreciacionDetalleList() {
        return depreciacionDetalleList;
    }

    public void setDepreciacionDetalleList(List<DepreciacionDetalle> depreciacionDetalleList) {
        this.depreciacionDetalleList = depreciacionDetalleList;
    }

    public Depreciacion getDepreciacion() {
        return depreciacion;
    }

    public void setDepreciacion(Depreciacion depreciacion) {
        this.depreciacion = depreciacion;
    }

    public Depreciacion getDepreciacionSeleccionada() {
        return depreciacionSeleccionada;
    }

    public void setDepreciacionSeleccionada(Depreciacion depreciacionSeleccionada) {
        this.depreciacionSeleccionada = depreciacionSeleccionada;
    }

    public double getTotalValorResidual() {
        return totalValorResidual;
    }

    public void setTotalValorResidual(double totalValorResidual) {
        this.totalValorResidual = totalValorResidual;
    }

    public double getTotalDepreciacion() {
        return totalDepreciacion;
    }

    public void setTotalDepreciacion(double totalDepreciacion) {
        this.totalDepreciacion = totalDepreciacion;
    }

    public double getTotalValorContable() {
        return totalValorContable;
    }

    public void setTotalValorContable(double totalValorContable) {
        this.totalValorContable = totalValorContable;
    }

}
