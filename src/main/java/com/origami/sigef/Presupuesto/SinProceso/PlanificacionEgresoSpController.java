/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.SinProceso;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProgramacionIngresoEgresoService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "pemSpVview")
@ViewScoped
public class PlanificacionEgresoSpController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private ProgramacionIngresoEgresoService pemService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession ss;
    private ProgramacionIngresoEgreso pem;
    private ProgramacionIngresoEgreso pemSeleccionado;
    private LazyModel<ProgramacionIngresoEgreso> lazyEgreso;
    private OpcionBusqueda busqueda;
    private List<CatalogoProformaPresupuesto> periodosEgreso;
    private BigDecimal meses;
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
    private Double totalMonto;
    private Double totalPem;
    private List<Producto> productosWithActividades;
    private final BigDecimal porcentaje = new BigDecimal(100);
    private double auxValor1;
    private double auxValor2;
    private double auxValor3;
    private double auxTotal1;
    private double auxTotal2;
    private double auxTotal3;
    double acuCuatrimestre1 = 0;
    double acuCuatrimestre2 = 0;
    double acuCuatrimestre3 = 0;
    private String observaciones;
    private CatalogoItem estadoGeneral;
    private Boolean tipoReporte;

    @Inject
    private ManagerService service;
    private Map<String, Object> param;

    @PostConstruct
    public void init() {
        try {

            pem = new ProgramacionIngresoEgreso();
            busqueda = new OpcionBusqueda();
            lazyEgreso = new LazyModel(ProgramacionIngresoEgreso.class);
            lazyEgreso.getFilterss().put("tipoFlujo", false);
            lazyEgreso.getFilterss().put("periodo", busqueda.getAnio());
            lazyEgreso.getSorteds().put("codigoItem", "ASC");
            lazyEgreso.getSorteds().put("tipoCodigo", "ASC");
            lazyEgreso.setDistinct(false);
//                lazyEgreso.getFilterss().put("estadoA.codigo", "REG");
            meses = new BigDecimal(12);
            productosWithActividades = new ArrayList<>();
            periodosEgreso = pemService.getProformaIngresoEgreso(false);
            if (periodosEgreso.isEmpty()) {
                return;
            }
            actualizarCuatrimestres();
            totalMonto = pemService.getTotalCatalogoProforma(busqueda.getAnio(), Boolean.FALSE).doubleValue();
            totalPem = pemService.getTotalPIE(busqueda.getAnio(), false).doubleValue();
            estadoGeneral = new CatalogoItem();
            estadoGeneral = pemService.getestado(busqueda.getAnio());

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void dialog(ProgramacionIngresoEgreso e) {
        if (e != null) {
            pem = e;
        } else {
            pem = new ProgramacionIngresoEgreso();
        }
        PrimeFaces.current().executeScript("PF('planDialog').show()");
        PrimeFaces.current().ajax().update("formMonto");
    }

    public Boolean renderedElementForm() {
        if (pem != null) {
            return pem.getActividad();
        }
        return false;
    }

    public void guardar() {
        try {
            boolean edit = pem.getId() != null;
            BigDecimal total;
            if (edit) {
                if (!pem.getActividad()) {
                    //si el total del PEM sobrepasa el monto de la actividad no se permite guardad
                    if (suma(pem) > pem.getMontoActividad().doubleValue()) {
                        JsfUtil.addErrorMessage("Programacion Egreso", "Error el valor ingresado sobrepasa el monto");
                        PrimeFaces.current().ajax().update("messages");
                        return;
                    }
                    total = new BigDecimal(suma(pem));
                    //setea el 
                    pem.setMontoCuatrimestre1(getCuatrimestre1(pem));
                    pem.setMontoCuatrimestre2(getCuatrimestre2(pem));
                    pem.setMontoCuatrimestre3(getCuatrimestre3(pem));
                    pem.setTotalAnio(total);

                } else {
                    total = getSumaCuatrimestres(pem);
                    if (total.doubleValue() > pem.getMontoActividad().doubleValue()) {
                        JsfUtil.addErrorMessage("Programacion Egreso", "Error el valor ingresado sobrepasa el monto");
                        PrimeFaces.current().ajax().update("messages");
                        return;
                    }
                    pem.setTotalAnio(total);
                }
                pem.setFechaModificacion(new Date());
                pem.setUsuarioModifica(userSession.getNameUser());
                pemService.edit(pem);
                actualizarCuatrimestres();
                PrimeFaces.current().ajax().update("tablepr");
                PrimeFaces.current().executeScript("PF('planDialog').hide()");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Producto> viewActividadByProducto(ProgramacionIngresoEgreso p) {
        productosWithActividades = pemService.getProductoWithAct(p.getCodigoItem(), p.getPeriodo());
        return productosWithActividades;
    }

    public BigDecimal getSumaCuatrimestres(ProgramacionIngresoEgreso p) {
        BigDecimal totalCuatrimestre;
        double val = p.getMontoCuatrimestre1().doubleValue() + p.getMontoCuatrimestre2().doubleValue() + p.getMontoCuatrimestre3().doubleValue();
        return totalCuatrimestre = new BigDecimal(val);
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("formMonto");
    }

    public void actualizar() {
        if (periodosEgreso.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Error", "no se hay registros de cuentas aprobadas");
            return;
        }
        lazyEgreso = new LazyModel(ProgramacionIngresoEgreso.class);
        lazyEgreso.getFilterss().put("tipoFlujo", false);
        lazyEgreso.getFilterss().put("periodo", busqueda.getAnio());
        lazyEgreso.getSorteds().put("codigoItem", "ASC");
        lazyEgreso.getSorteds().put("tipoCodigo", "ASC");
        lazyEgreso.setDistinct(false);
        totalMonto = pemService.getTotalCatalogoProforma(busqueda.getAnio(), Boolean.FALSE).doubleValue();
        totalPem = pemService.getTotalPIE(busqueda.getAnio(), false).doubleValue();
        actualizarCuatrimestres();
        estadoGeneral = new CatalogoItem();
        estadoGeneral = pemService.getestado(busqueda.getAnio());
    }

    //SI TIENE ACTIVIDAD ES DE PAPP
    public void distribucion(ProgramacionIngresoEgreso item) {
        if (item.getActividad().equals(false)) {
            if (item.getDistribucion().equals(true)) {
                BigDecimal aux = (item.getMontoActividad().divide(meses, 2, BigDecimal.ROUND_HALF_EVEN));
                getSumadistribucion(Boolean.TRUE, aux, item);
            } else {
                getSumadistribucion(Boolean.FALSE, BigDecimal.ZERO, item);
            }
        } else {
            if (item.getDistribucion().equals(true)) {
                productosWithActividades = pemService.getProductoWithAct(item.getCodigoItem(), item.getPeriodo());
                BigDecimal aux = (item.getMontoActividad().divide(meses, 2, BigDecimal.ROUND_HALF_EVEN));
                getSumadistribucion(Boolean.TRUE, aux, item);
//                for (Producto p : productosWithActividades) {
//                    if (p.getActividadOperativa().getCuatrimestre1Actividad() != BigDecimal.ZERO.setScale(2)
//                            || p.getActividadOperativa().getMonto() != BigDecimal.ZERO.setScale(2)) {
//
//                        this.auxValor1 = getDistribucionPAPP(p.getActividadOperativa().getCuatrimestre1Actividad(), p).doubleValue();
//                        this.auxTotal1 = p.getMonto().multiply(new BigDecimal(auxValor1)).doubleValue();
//                        this.auxValor2 = getDistribucionPAPP(p.getActividadOperativa().getCuatrimestre2Actividad(), p).doubleValue();
//                        this.auxTotal2 = p.getMonto().multiply(new BigDecimal(auxValor2)).doubleValue();
//                        this.auxValor3 = getDistribucionPAPP(p.getActividadOperativa().getCuatrimestre3Actividad(), p).doubleValue();
//                        this.auxTotal3 = p.getMonto().multiply(new BigDecimal(auxValor3)).doubleValue();
//
//                        acuCuatrimestre1 += this.auxTotal1;
//                        acuCuatrimestre2 += this.auxTotal2;
//                        acuCuatrimestre3 += this.auxTotal3;
//                    }
//                }

//                item.setMontoCuatrimestre1(new BigDecimal(acuCuatrimestre1).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//                item.setMontoCuatrimestre2(new BigDecimal(acuCuatrimestre2).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//                item.setMontoCuatrimestre3(new BigDecimal(acuCuatrimestre3).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//                BigDecimal total = BigDecimal.valueOf(suma(item));
                //BALANCEO DE DECIMALES 
                //   item.setMontoCuatrimestre3(item.getMontoCuatrimestre3().add(balanceoDecimal(total, item)));
                //   total = getSumaCuatrimestres(item);
                /////*********************
                //              item.setTotalAnio(total);
                //              pemService.edit(item);
                acuCuatrimestre1 = 0;
                acuCuatrimestre2 = 0;
                acuCuatrimestre3 = 0;
                //               actualizarCuatrimestres();
                //               actualizar();
            } else {
                getSumadistribucion(Boolean.TRUE, BigDecimal.ZERO, item);
            }
        }
        actualizarCuatrimestres();
    }

    private BigDecimal getDistribucionPAPP(BigDecimal valorCuatrimestre, Producto p) {
        return valorCuatrimestre.multiply(porcentaje).divide(p.getActividadOperativa().getMonto(), 6, BigDecimal.ROUND_HALF_EVEN).divide(porcentaje, 6, BigDecimal.ROUND_HALF_EVEN);
    }

    private BigDecimal balanceoDecimal(BigDecimal total, ProgramacionIngresoEgreso item) {
        if (!total.equals(item.getMontoActividad()) && total.compareTo(item.getMontoActividad()) < 0) {
            return item.getMontoActividad().subtract(total);
        }
        return BigDecimal.ZERO;
    }

    public void gaurdarPem(ProgramacionIngresoEgreso p, int posicion) {
        ProgramacionIngresoEgreso valorAntiguo = service.find(ProgramacionIngresoEgreso.class, p.getId());
        double valor = suma(p);
        switch (posicion) {
            case 1:
                valor = valor - valorAntiguo.getEnero().doubleValue();
                break;
            case 2:
                valor = valor - valorAntiguo.getFebrero().doubleValue();
                break;
            case 3:
                valor = valor - valorAntiguo.getMarzo().doubleValue();
                break;
            case 4:
                valor = valor - valorAntiguo.getAbril().doubleValue();
                break;
            case 5:
                valor = valor - valorAntiguo.getMayo().doubleValue();
                break;
            case 6:
                valor = valor - valorAntiguo.getJunio().doubleValue();
                break;
            case 7:
                valor = valor - valorAntiguo.getJulio().doubleValue();
                break;
            case 8:
                valor = valor - valorAntiguo.getAgosto().doubleValue();
                break;
            case 9:
                valor = valor - valorAntiguo.getSeptiembre().doubleValue();
                break;
            case 10:
                valor = valor - valorAntiguo.getOctubre().doubleValue();
                break;
            case 11:
                valor = valor - valorAntiguo.getNoviembre().doubleValue();
                break;
            case 12:
                valor = valor - valorAntiguo.getDiciembre().doubleValue();
                break;
        }

        if (valor <= p.getMontoActividad().doubleValue()) {
            service.update(p);
            JsfUtil.addInformationMessage("", "TRANSACCIÓN EXITOSA");
        } else {

            switch (posicion) {
                case 1:
                    p.setEnero(BigDecimal.ZERO);
                    break;
                case 2:
                    p.setFebrero(BigDecimal.ZERO);
                    break;
                case 3:
                    p.setMarzo(BigDecimal.ZERO);
                    break;
                case 4:
                    p.setAbril(BigDecimal.ZERO);
                    break;
                case 5:
                    p.setMayo(BigDecimal.ZERO);
                    break;
                case 6:
                    p.setJunio(BigDecimal.ZERO);
                    break;
                case 7:
                    p.setJulio(BigDecimal.ZERO);
                    break;
                case 8:
                    p.setAgosto(BigDecimal.ZERO);
                    break;
                case 9:
                    p.setSeptiembre(BigDecimal.ZERO);
                    break;
                case 10:
                    p.setOctubre(BigDecimal.ZERO);
                    break;
                case 11:
                    p.setNoviembre(BigDecimal.ZERO);
                    break;
                case 12:
                    p.setDiciembre(BigDecimal.ZERO);
                    break;
            }

            service.update(p);

            JsfUtil.addWarningMessage("", "La distribucción asignada supera el monto");
        }
        p.setTotalAnio(BigDecimal.valueOf(suma(p)));
        service.update(p);
        actualizarCuatrimestres();
        actualizar();
    }

    public void getSumadistribucion(Boolean distribucion, BigDecimal aux, ProgramacionIngresoEgreso item) {
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
        if (item.getMontoActividad().doubleValue() < item.getTotalAnio().doubleValue()) {
            BigDecimal diferencia = item.getTotalAnio().subtract(item.getMontoActividad());
            BigDecimal aux2 = item.getDiciembre().subtract(diferencia);
            item.setDiciembre(aux2);
            BigDecimal total = new BigDecimal(suma(item));
            item.setTotalAnio(total);
        }
        if (item.getMontoActividad().doubleValue() > item.getTotalAnio().doubleValue() && item.getTotalAnio().doubleValue() > 0) {
            BigDecimal diferencia = item.getMontoActividad().subtract(item.getTotalAnio());
            BigDecimal aux2 = item.getDiciembre().add(diferencia);
            item.setDiciembre(aux2);
            BigDecimal total = new BigDecimal(suma(item));
            item.setTotalAnio(total);
        }
//        item.setMontoCuatrimestre1(getCuatrimestre1(item));
//        item.setMontoCuatrimestre2(getCuatrimestre2(item));
//        item.setMontoCuatrimestre3(getCuatrimestre3(item));
        pemService.edit(item);
        actualizarCuatrimestres();
        actualizar();
    }

    public void actualizarCuatrimestres() {
        totalEnero = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 1).doubleValue();
        totalFebrero = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 2).doubleValue();
        totalMarzo = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 3).doubleValue();
        totalAbril = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 4).doubleValue();
        totalMayo = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 5).doubleValue();
        totalJunio = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 6).doubleValue();
        totalJulio = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 7).doubleValue();
        totalAgosto = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 8).doubleValue();
        totalSeptiembre = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 9).doubleValue();
        totalOctubre = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 10).doubleValue();
        totalNoviembre = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 11).doubleValue();
        totalDiciembre = pemService.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 12).doubleValue();
        totalPem = pemService.getTotalPIE(busqueda.getAnio(), false).doubleValue();
    }

    public double suma(ProgramacionIngresoEgreso item) {
        double val = (item.getEnero().doubleValue() + item.getFebrero().doubleValue() + item.getMarzo().doubleValue()
                + item.getAbril().doubleValue() + item.getMayo().doubleValue() + item.getJunio().doubleValue()
                + item.getJulio().doubleValue() + item.getAgosto().doubleValue() + item.getSeptiembre().doubleValue()
                + item.getOctubre().doubleValue() + item.getNoviembre().doubleValue() + item.getDiciembre().doubleValue());
        return val;
    }

    public BigDecimal getCuatrimestre1(ProgramacionIngresoEgreso item) {
        BigDecimal totalcuatrimestre;
        double val = (item.getEnero().doubleValue() + item.getFebrero().doubleValue() + item.getMarzo().doubleValue()
                + item.getAbril().doubleValue());
        totalcuatrimestre = new BigDecimal(val);
        return totalcuatrimestre;
    }

    public BigDecimal getCuatrimestre2(ProgramacionIngresoEgreso item) {
        BigDecimal totalcuatrimestre;
        double val = (item.getMayo().doubleValue() + item.getJulio().doubleValue() + item.getJulio().doubleValue()
                + item.getAgosto().doubleValue());
        totalcuatrimestre = new BigDecimal(val);
        return totalcuatrimestre;
    }

    public BigDecimal getCuatrimestre3(ProgramacionIngresoEgreso item) {
        BigDecimal totalcuatrimestre;
        double val = (item.getSeptiembre().doubleValue() + item.getOctubre().doubleValue() + item.getNoviembre().doubleValue()
                + item.getDiciembre().doubleValue());
        totalcuatrimestre = new BigDecimal(val);
        return totalcuatrimestre;
    }

    public void showOpcionReporte() {
        JsfUtil.executeJS("PF('dlgPrint').show()");
        PrimeFaces.current().ajax().update(":frmldlgprint");
    }

    public void imprimirReporte(String format) {
        System.out.println("entrando");
        System.out.println("tipoReporte" + tipoReporte);

        if (tipoReporte) {
            ss.setNombreReporte("peg");
        } else {
            ss.setNombreReporte("peg_detallado");
        }
        ss.addParametro("periodo", busqueda.getAnio());
        ss.setContentType(format);
        ss.setNombreSubCarpeta("reportesPresupuesto");
        if (format.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(true);
        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public ProgramacionIngresoEgreso getPem() {
        return pem;
    }

    public void setPem(ProgramacionIngresoEgreso pem) {
        this.pem = pem;
    }

    public ProgramacionIngresoEgreso getPemSeleccionado() {
        return pemSeleccionado;
    }

    public void setPemSeleccionado(ProgramacionIngresoEgreso pemSeleccionado) {
        this.pemSeleccionado = pemSeleccionado;
    }

    public LazyModel<ProgramacionIngresoEgreso> getLazyEgreso() {
        return lazyEgreso;
    }

    public void setLazyEgreso(LazyModel<ProgramacionIngresoEgreso> lazyEgreso) {
        this.lazyEgreso = lazyEgreso;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<CatalogoProformaPresupuesto> getPeriodosEgreso() {
        return periodosEgreso;
    }

    public void setPeriodosEgreso(List<CatalogoProformaPresupuesto> periodosEgreso) {
        this.periodosEgreso = periodosEgreso;
    }

    public BigDecimal getMeses() {
        return meses;
    }

    public void setMeses(BigDecimal meses) {
        this.meses = meses;
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

    public Double getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(Double totalMonto) {
        this.totalMonto = totalMonto;
    }

    public Double getTotalPem() {
        return totalPem;
    }

    public void setTotalPem(Double totalPem) {
        this.totalPem = totalPem;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

//</editor-fold>
    public CatalogoItem getEstadoGeneral() {
        return estadoGeneral;
    }

    public void setEstadoGeneral(CatalogoItem estadoGeneral) {
        this.estadoGeneral = estadoGeneral;
    }

    public Boolean getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Boolean tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

}
