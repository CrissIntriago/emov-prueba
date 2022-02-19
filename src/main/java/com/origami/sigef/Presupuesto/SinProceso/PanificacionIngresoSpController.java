/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.SinProceso;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProgramacionIngresoEgresoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "pimSpView")
@ViewScoped
public class PanificacionIngresoSpController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ProgramacionIngresoEgresoService pimService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
    private ProgramacionIngresoEgreso pim;
    private ProgramacionIngresoEgreso pimSeleccionado;
    private LazyModel<ProgramacionIngresoEgreso> lazyIngreso;
    private BigDecimal meses;
    private Double totalItem;
    private Double totalPIM;
    private Double totalEnero;
    private Double totalFebrero;
    private Double totalMarzo;
    private Double totalAbril;
    private Double totalMayo;
    private Double totalJunio;
    private Double totalJulio;
    private Double totalAgosto;
    private Double totalSeptiembre;
    private Double totalOctubre;
    private Double totalNoviembre;
    private Double totalDiciembre;
    private String observaciones;

    private OpcionBusqueda busqueda;
    private List<CatalogoProformaPresupuesto> periodosIngreso;
    private CatalogoItem estadoGeneral;

    //NUEVO
    @Inject
    private ManagerService service;

    @PostConstruct
    public void init() {
        try {
            pim = new ProgramacionIngresoEgreso();
            busqueda = new OpcionBusqueda();
            lazyIngreso = new LazyModel(ProgramacionIngresoEgreso.class);
            lazyIngreso.getFilterss().put("periodo", busqueda.getAnio());
            lazyIngreso.getFilterss().put("tipoFlujo", true);
            lazyIngreso.setDistinct(false);

//              lazyIngreso.getFilterss().put("estadoA.codigo", "REG");
            meses = new BigDecimal(12);
            estadoGeneral = new CatalogoItem();
            estadoGeneral = pimService.getestado(busqueda.getAnio());
            totalPIM = pimService.getTotalPIE(busqueda.getAnio(), true).doubleValue();
            periodosIngreso = pimService.getProformaIngresoEgreso(true);
            if (periodosIngreso.isEmpty()) {
                return;
            }
            totalItem = pimService.getTotalCatalogoProforma(busqueda.getAnio(), Boolean.TRUE).doubleValue();
            actualizarTotalMeses();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

//    public Boolean disabledCellEditor(ProgramacionIngresoEgreso item) {
//        return item.getDistribucion().equals(true);
//    }
    public void actualizar() {
        if (periodosIngreso.isEmpty()) {
            return;
        }
        lazyIngreso = new LazyModel(ProgramacionIngresoEgreso.class);
        lazyIngreso.getFilterss().put("periodo", busqueda.getAnio());
        lazyIngreso.getFilterss().put("tipoFlujo", true);
        totalItem = pimService.getTotalCatalogoProforma(busqueda.getAnio(), Boolean.TRUE).doubleValue();
        totalPIM = pimService.getTotalPIE(busqueda.getAnio(), true).doubleValue();
        actualizarTotalMeses();
        estadoGeneral = new CatalogoItem();
        estadoGeneral = pimService.getestado(busqueda.getAnio());
    }

    public double suma(ProgramacionIngresoEgreso item) {
        double val = (item.getEnero().doubleValue() + item.getFebrero().doubleValue() + item.getMarzo().doubleValue()
                + item.getAbril().doubleValue() + item.getMayo().doubleValue() + item.getJunio().doubleValue()
                + item.getJulio().doubleValue() + item.getAgosto().doubleValue() + item.getSeptiembre().doubleValue()
                + item.getOctubre().doubleValue() + item.getNoviembre().doubleValue() + item.getDiciembre().doubleValue());
        return val;
    }

    public void guardarPIM(ProgramacionIngresoEgreso item) {
        BigDecimal total;
        try {
            if (suma(item) > presupuestoInical(item.getPeriodo(), item.getItemNew()).doubleValue()) {
                PrimeFaces.current().executeScript("PF('DialogPIM').show()");
            } else {
                total = new BigDecimal(suma(item));
                item.setTotalAnio(total);
                item.setFechaModificacion(new Date());
                item.setUsuarioModifica(userSession.getNameUser());
                pimService.edit(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        actualizarTotalMeses();
        totalPIM = pimService.getTotalPIE(busqueda.getAnio(), true).doubleValue();
    }

    public BigDecimal presupuestoInical(Short periodo, PresCatalogoPresupuestario item) {
        return service.getValorPresupuestoInicialPim(periodo, item);
    }

    public void getDistribucion(ProgramacionIngresoEgreso item) {
        if (item.getDistribucion().equals(true)) {
            BigDecimal aux = (presupuestoInical(item.getPeriodo(), item.getItemNew()).divide(meses, 2, RoundingMode.HALF_UP));
            getsumaDistribucion(true, aux, item);
        } else {
            getsumaDistribucion(false, BigDecimal.ZERO, item);
        }

    }

    public void dialogoError() {
        PrimeFaces.current().executeScript("PF('DialogPIM').hide()");
        PrimeFaces.current().ajax().update("pimTable");
    }

    public void getsumaDistribucion(Boolean distribucion, BigDecimal aux, ProgramacionIngresoEgreso item) {
        item.setEnero(aux);
        item.setFebrero(aux);
        item.setMarzo(aux);
        item.setAbril(aux);
        item.setMayo(aux);
        item.setJunio(aux);
        item.setJulio(aux);
        item.setAgosto(aux);
        item.setSeptiembre(aux);
        item.setOctubre(aux);
        item.setNoviembre(aux);
        item.setDiciembre(aux);
        if (distribucion.equals(true)) {
            BigDecimal total = new BigDecimal(suma(item));
            item.setTotalAnio(total);
        } else {
            item.setTotalAnio(aux);
        }
        if (presupuestoInical(item.getPeriodo(), item.getItemNew()).doubleValue() < item.getTotalAnio().doubleValue()) {
            BigDecimal diferencia = item.getTotalAnio().subtract(presupuestoInical(item.getPeriodo(), item.getItemNew()));
            BigDecimal aux2 = item.getDiciembre().subtract(diferencia);
            item.setDiciembre(aux2);
            BigDecimal total = new BigDecimal(suma(item));
            item.setTotalAnio(total);
        }
        if (presupuestoInical(item.getPeriodo(), item.getItemNew()).doubleValue() > item.getTotalAnio().doubleValue() && item.getTotalAnio().doubleValue() > 0) {
            BigDecimal diferencia = presupuestoInical(item.getPeriodo(), item.getItemNew()).subtract(item.getTotalAnio());
            BigDecimal aux2 = item.getDiciembre().add(diferencia);
            item.setDiciembre(aux2);
            BigDecimal total = new BigDecimal(suma(item));
            item.setTotalAnio(total);
        }
        pimService.edit(item);
        actualizarTotalMeses();
        totalPIM = pimService.getTotalPIE(busqueda.getAnio(), true).doubleValue();
    }

    public void actualizarTotalMeses() {
        if (periodosIngreso.isEmpty()) {
            return;
        }
        totalEnero = pimService.getSumaEnero("enero", busqueda.getAnio()).doubleValue();
        totalFebrero = pimService.getSumaEnero("febrero", busqueda.getAnio()).doubleValue();
        totalMarzo = pimService.getSumaEnero("marzo", busqueda.getAnio()).doubleValue();
        totalAbril = pimService.getSumaEnero("abril", busqueda.getAnio()).doubleValue();
        totalMayo = pimService.getSumaEnero("mayo", busqueda.getAnio()).doubleValue();
        totalJunio = pimService.getSumaEnero("junio", busqueda.getAnio()).doubleValue();
        totalJulio = pimService.getSumaEnero("julio", busqueda.getAnio()).doubleValue();
        totalAgosto = pimService.getSumaEnero("agosto", busqueda.getAnio()).doubleValue();
        totalSeptiembre = pimService.getSumaEnero("septiembre", busqueda.getAnio()).doubleValue();
        totalOctubre = pimService.getSumaEnero("octubre", busqueda.getAnio()).doubleValue();
        totalNoviembre = pimService.getSumaEnero("noviembre", busqueda.getAnio()).doubleValue();
        totalDiciembre = pimService.getSumaEnero("diciembre", busqueda.getAnio()).doubleValue();
    }

    public void reportePIM(String formato) {
        ss.addParametro("periodo", busqueda.getAnio());
        ss.setNombreReporte("pim");
        ss.setContentType(formato);
        ss.setNombreSubCarpeta("reportesPresupuesto");
        if (formato.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(true);
        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public ProgramacionIngresoEgreso getPim() {
        return pim;
    }

    public void setPim(ProgramacionIngresoEgreso pim) {
        this.pim = pim;
    }

    public LazyModel<ProgramacionIngresoEgreso> getLazyIngreso() {
        return lazyIngreso;
    }

    public void setLazyIngreso(LazyModel<ProgramacionIngresoEgreso> lazyIngreso) {
        this.lazyIngreso = lazyIngreso;
    }

    public Double getTotalPIM() {
        return totalPIM;
    }

    public void setTotalPIM(Double totalPIM) {
        this.totalPIM = totalPIM;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<CatalogoProformaPresupuesto> getPeriodosIngreso() {
        return periodosIngreso;
    }

    public void setPeriodosIngreso(List<CatalogoProformaPresupuesto> periodosIngreso) {
        this.periodosIngreso = periodosIngreso;
    }

    public ProgramacionIngresoEgreso getPimSeleccionado() {
        return pimSeleccionado;
    }

    public void setPimSeleccionado(ProgramacionIngresoEgreso pimSeleccionado) {
        this.pimSeleccionado = pimSeleccionado;
    }

    public Double getTotalEnero() {
        return totalEnero;
    }

    public void setTotalEnero(Double totalEnero) {
        this.totalEnero = totalEnero;
    }

    public Double getTotalFebrero() {
        return totalFebrero;
    }

    public void setTotalFebrero(Double totalFebrero) {
        this.totalFebrero = totalFebrero;
    }

    public Double getTotalMarzo() {
        return totalMarzo;
    }

    public void setTotalMarzo(Double totalMarzo) {
        this.totalMarzo = totalMarzo;
    }

    public Double getTotalAbril() {
        return totalAbril;
    }

    public void setTotalAbril(Double totalAbril) {
        this.totalAbril = totalAbril;
    }

    public Double getTotalMayo() {
        return totalMayo;
    }

    public void setTotalMayo(Double totalMayo) {
        this.totalMayo = totalMayo;
    }

    public Double getTotalJunio() {
        return totalJunio;
    }

    public void setTotalJunio(Double totalJunio) {
        this.totalJunio = totalJunio;
    }

    public Double getTotalJulio() {
        return totalJulio;
    }

    public void setTotalJulio(Double totalJulio) {
        this.totalJulio = totalJulio;
    }

    public Double getTotalAgosto() {
        return totalAgosto;
    }

    public void setTotalAgosto(Double totalAgosto) {
        this.totalAgosto = totalAgosto;
    }

    public Double getTotalSeptiembre() {
        return totalSeptiembre;
    }

    public void setTotalSeptiembre(Double totalSeptiembre) {
        this.totalSeptiembre = totalSeptiembre;
    }

    public Double getTotalOctubre() {
        return totalOctubre;
    }

    public void setTotalOctubre(Double totalOctubre) {
        this.totalOctubre = totalOctubre;
    }

    public Double getTotalNoviembre() {
        return totalNoviembre;
    }

    public void setTotalNoviembre(Double totalNoviembre) {
        this.totalNoviembre = totalNoviembre;
    }

    public Double getTotalDiciembre() {
        return totalDiciembre;
    }

    public void setTotalDiciembre(Double totalDiciembre) {
        this.totalDiciembre = totalDiciembre;
    }

    public Double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Double totalItem) {
        this.totalItem = totalItem;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public CatalogoItem getEstadoGeneral() {
        return estadoGeneral;
    }

    public void setEstadoGeneral(CatalogoItem estadoGeneral) {
        this.estadoGeneral = estadoGeneral;
    }

//</editor-fold>
}
