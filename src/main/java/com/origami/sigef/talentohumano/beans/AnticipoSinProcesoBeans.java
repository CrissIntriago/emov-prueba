/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuotaAnticipo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.talentohumano.services.CuotaAnticipoService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "anticipoSinProceso")
@ViewScoped
public class AnticipoSinProcesoBeans implements Serializable {

    private static final Logger LOG = Logger.getLogger(AnticipoRemuneracionController.class.getName());

    @Inject
    private AnticipoRemuneracionService anticipoRemunercionService;//
    @Inject
    private CuotaAnticipoService cuotaAnticipoService;
    @Inject
    private ParametrizableService parametroService;

    private LazyModel<AnticipoRemuneracion> lazy;
    private LazyModel<CuotaAnticipo> lazyCuotas;//
    private AnticipoRemuneracion anticipoRemuneracion;
    private AnticipoRemuneracion anticipoRemuneracionAdic;//
    private BigDecimal totalPagado;//
    private BigDecimal saldoPagar;//
    private List<CuotaAnticipo> listaCuota;//
    private CatalogoItem registrado, deuda, pagado, autorizado, incompleto, rechazado, completo, aprobado;//
    private List<CatalogoItem> estadoList;
    private ParametrosTalentoHumano valoPrametro;//
    private short periodo;

    @PostConstruct
    public void init() {
        estadoList = new ArrayList<>();
        anticipoRemuneracion = new AnticipoRemuneracion();
        anticipoRemuneracionAdic = new AnticipoRemuneracion();
        totalPagado = BigDecimal.ZERO;
        saldoPagar = BigDecimal.ZERO;
        listaCuota = new ArrayList<>();
        lazy = new LazyModel<>(AnticipoRemuneracion.class);
        lazy.setDistinct(false);
        lazy.getFilterss().put("estado", true);
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
    }

    public void openDialogAmortizacion(AnticipoRemuneracion anticipo) {
        //sumar total pagado ; saldo a pagar
        listaCuota = cuotaAnticipoService.getCuotaList(anticipo);
        Integer aa = listaCuota.size();
        BigDecimal sumaTotal = BigDecimal.ZERO;
        for (int i = 0; i < aa; i++) {
            if (listaCuota.get(i).getEstadoCuota()) {
                sumaTotal = sumaTotal.add(listaCuota.get(i).getValorCuota());
            }
        }
        totalPagado = sumaTotal;
        saldoPagar = (anticipo.getMontoAnticipo()).subtract(totalPagado);
        //llenar datatable de amortizacion
        lazyCuotas = new LazyModel<>(CuotaAnticipo.class);
        lazyCuotas.setDistinct(false);
        lazyCuotas.getFilterss().put("anticipoRemuneracion", anticipo);
        anticipoRemuneracionAdic = anticipo;
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public void eliminar(AnticipoRemuneracion anticipo) {
        if (anticipo.getEstadoAnticipo().equals(rechazado)) {
            anticipo.setEstado(false);
            eliminarCuota(anticipo);
            anticipoRemunercionService.edit(anticipo);
            JsfUtil.addInformationMessage("Anticipo de Remuneración", "Anticipo eliminado con éxito");
        } else {
            JsfUtil.addWarningMessage("Anticipo de Remuneración", "No puede eliminar Anticipo ");
        }
        listaCuota = new ArrayList<>();
    }

    public void eliminarCuota(AnticipoRemuneracion anticipo) {
        listaCuota = cuotaAnticipoService.getCuotaList(anticipo);
        for (CuotaAnticipo c : listaCuota) {
            cuotaAnticipoService.remove(c);
        }
    }

    public void estadosAnticipo() {
        valoPrametro = parametroService.valorParametroTipo("ANT_RMU");
        registrado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 1);
        deuda = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 2);
        pagado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 3);
        incompleto = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 4);
        rechazado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 5);
        autorizado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 6);
        completo = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 7);
        aprobado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 8);
    }

    public LazyModel<AnticipoRemuneracion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<AnticipoRemuneracion> lazy) {
        this.lazy = lazy;
    }

    public LazyModel<CuotaAnticipo> getLazyCuotas() {
        return lazyCuotas;
    }

    public void setLazyCuotas(LazyModel<CuotaAnticipo> lazyCuotas) {
        this.lazyCuotas = lazyCuotas;
    }

    public AnticipoRemuneracion getAnticipoRemuneracion() {
        return anticipoRemuneracion;
    }

    public void setAnticipoRemuneracion(AnticipoRemuneracion anticipoRemuneracion) {
        this.anticipoRemuneracion = anticipoRemuneracion;
    }

    public AnticipoRemuneracion getAnticipoRemuneracionAdic() {
        return anticipoRemuneracionAdic;
    }

    public void setAnticipoRemuneracionAdic(AnticipoRemuneracion anticipoRemuneracionAdic) {
        this.anticipoRemuneracionAdic = anticipoRemuneracionAdic;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getSaldoPagar() {
        return saldoPagar;
    }

    public void setSaldoPagar(BigDecimal saldoPagar) {
        this.saldoPagar = saldoPagar;
    }

    public List<CuotaAnticipo> getListaCuota() {
        return listaCuota;
    }

    public void setListaCuota(List<CuotaAnticipo> listaCuota) {
        this.listaCuota = listaCuota;
    }

    public CatalogoItem getRegistrado() {
        return registrado;
    }

    public void setRegistrado(CatalogoItem registrado) {
        this.registrado = registrado;
    }

    public CatalogoItem getDeuda() {
        return deuda;
    }

    public void setDeuda(CatalogoItem deuda) {
        this.deuda = deuda;
    }

    public CatalogoItem getPagado() {
        return pagado;
    }

    public void setPagado(CatalogoItem pagado) {
        this.pagado = pagado;
    }

    public CatalogoItem getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(CatalogoItem autorizado) {
        this.autorizado = autorizado;
    }

    public CatalogoItem getIncompleto() {
        return incompleto;
    }

    public void setIncompleto(CatalogoItem incompleto) {
        this.incompleto = incompleto;
    }

    public CatalogoItem getRechazado() {
        return rechazado;
    }

    public void setRechazado(CatalogoItem rechazado) {
        this.rechazado = rechazado;
    }

    public CatalogoItem getCompleto() {
        return completo;
    }

    public void setCompleto(CatalogoItem completo) {
        this.completo = completo;
    }

    public CatalogoItem getAprobado() {
        return aprobado;
    }

    public void setAprobado(CatalogoItem aprobado) {
        this.aprobado = aprobado;
    }

    public List<CatalogoItem> getEstadoList() {
        return estadoList;
    }

    public void setEstadoList(List<CatalogoItem> estadoList) {
        this.estadoList = estadoList;
    }

    public ParametrosTalentoHumano getValoPrametro() {
        return valoPrametro;
    }

    public void setValoPrametro(ParametrosTalentoHumano valoPrametro) {
        this.valoPrametro = valoPrametro;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

}
