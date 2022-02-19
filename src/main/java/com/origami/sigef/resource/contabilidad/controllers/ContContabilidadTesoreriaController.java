/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Services.RenRubroLiquidacionService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.util.Variables;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "dgTesoreriaView")
@ViewScoped
public class ContContabilidadTesoreriaController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable contRegistroContable;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private RenRubroLiquidacionService rubrosLiquidacionService;

    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneral contDiarioGeneral;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;
    private PartePresupuestariaModel partePresupuestariaModel;

    private LazyModel<ContDiarioGeneral> contDiarioGeneralLazy;

    private List<Short> listaPeriodo;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete;
    private List<CatalogoItem> clasesDiarioGeneral;
    private List<CatalogoItem> tiposDiarioGeneral;
    private List<PartePresupuestariaModel> partePresupuestariaModelList;
    private Date fechaEmision;
    private String tipoContable;

    private double totalDebe, totalHaber, totalComprometido, totalDevengado, totalEjecutado;
    private Boolean view, loadData, tipoDlg, tipoRelacion;

    @PostConstruct
    public void initialize() {
        clear();
        loadLazyModel();
        formInicializar();
        listaPeriodo = catalogoItemService.getPeriodo();
    }

    public void loadLazyModel() {
        if (opcionBusqueda.getAnio() != null) {
            contDiarioGeneralLazy = new LazyModel<>(ContDiarioGeneral.class);
            contDiarioGeneralLazy.getSorteds().put("numRegistro", "ASC");
            contDiarioGeneralLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        } else {
            contDiarioGeneralLazy = null;
        }
    }

    public void form(ContDiarioGeneral contDiarioGeneral, Boolean editView) {
        JsfUtil.redirect("/contabilidad/diario/registos/manuales");
    }

    public void reporte(ContDiarioGeneral contDiarioGeneral, String tipoDocumento) {
        contRegistroContable.ContabilidadImprimirReporte(contDiarioGeneral, tipoDocumento);
    }

    public void anular(ContDiarioGeneral contDiarioGeneral) {
        contRegistroContable.ContabilidadAnular(contDiarioGeneral);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha anulado correctamente");
    }

    public void clear() {
        opcionBusqueda = new OpcionBusqueda();
        fechaEmision = new Date();
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
    }

    private void formInicializar() {
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneralDetallesList = new ArrayList<>();
        clasesDiarioGeneral = catalogoItemService.findByCatalogo("diario_general_clases");
    }

    public void actualizarTipol() {
        if (contDiarioGeneral.getClase() != null) {
            this.tiposDiarioGeneral = catalogoService.getTiposDiarioGeneral(contDiarioGeneral.getClase(), "diario_general_tipos");
        } else {
            this.tiposDiarioGeneral = new ArrayList<>();
        }
    }

    public void openDlg(String dlg) {
        Utils.openDialog("/facelet/view/commons/" + dlg + "", "45%", "70%");
    }

    public void selectContCuenta(SelectEvent evt) {
        try {
            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
            detalle.setIdContCuentas((ContCuentas) evt.getObject());
            detalle.setNumeracion(contDiarioGeneralDetallesList.size());
            contDiarioGeneralDetallesList.add(detalle);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar cta. contable", e);
        }
    }

    private void valoresCero(ContDiarioGeneralDetalle detalle) {
        detalle.setComprometido(BigDecimal.ZERO);
        detalle.setDevengado(BigDecimal.ZERO);
        detalle.setEjecutado(BigDecimal.ZERO);
        detalle.setPartidaPresupuestaria(null);
        detalle.setIdPresCatalogoPresupuestario(null);
        detalle.setIdPresPlanProgramatico(null);
        detalle.setIdPresFuenteFinanciamiento(null);
        partePresupuestariaModelList = new ArrayList<>();
        partePresupuestariaModel = null;

    }

    public void determinarRelacionPresupuestaria(ContDiarioGeneralDetalle detalle, boolean tipoRegistro) {
        if (contDiarioGeneral.getTipo() == null || contDiarioGeneral.getClase() == null) {
            detalle.setDebe(BigDecimal.ZERO);
            detalle.setHaber(BigDecimal.ZERO);
            JsfUtil.addErrorMessage("AVISO!!!", "Debe seleccionar el tipo y la clase");
            return;
        }
        contDiarioGeneralDetalle = detalle;
        valoresCero(contDiarioGeneralDetalle);
        if (tipoRegistro) {
            detalle.setHaber(BigDecimal.ZERO);
        } else {
            detalle.setDebe(BigDecimal.ZERO);
        }
        relacion(tipoRegistro);
        calcularTotales();
    }

    private void relacion(Boolean accion) {
        if (contDiarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
            if (accion) {
                String[] arrayDebe = valoresService.findByCodigo(CONFIG.RESTRINGIR_DEBE).split(",");
                for (String text : arrayDebe) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().contains(text)) {
                        calcularTotales();
                        return;
                    }
                }
            } else {
                String[] arrayHaber = valoresService.findByCodigo(CONFIG.RESTRINGIR_DEBE).split(",");
                for (String text : arrayHaber) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().contains(text)) {
                        calcularTotales();
                        return;
                    }
                }
            }
//            partePresupuestariaModelList = contRegistroContable.ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral.getPeriodo());
            if (!partePresupuestariaModelList.isEmpty()) {
                if (partePresupuestariaModelList.size() > 1) {
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                } else {
                    partePresupuestariaModel = partePresupuestariaModelList.get(0);
                    guardarRelacionesPresupuestarias();
                }
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        if (partePresupuestariaModel != null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una relacion presupuestaria");
            return;
        }
        contDiarioGeneralDetalle.setPartidaPresupuestaria(partePresupuestariaModel.getPartidapresupuestaria());
        contDiarioGeneralDetalle.setIdPresCatalogoPresupuestario(new PresCatalogoPresupuestario(partePresupuestariaModel.getIdprescatalogopresupuestario()));
        contDiarioGeneralDetalle.setIdPresPlanProgramatico(new PresPlanProgramatico(partePresupuestariaModel.getIdpresplanprogramatico()));
        contDiarioGeneralDetalle.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(partePresupuestariaModel.getIdpresfuentefinanciamiento()));
    }

    public void calcularTotales() {
        totalDebe = 0;
        totalHaber = 0;
        totalComprometido = 0;
        totalDevengado = 0;
        totalEjecutado = 0;
        if (!contDiarioGeneralDetallesList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                totalDebe = totalDebe + item.getDebe().doubleValue();
                totalHaber = totalHaber + item.getHaber().doubleValue();
                totalComprometido = totalComprometido + item.getComprometido().doubleValue();
                totalDevengado = totalDevengado + item.getDevengado().doubleValue();
                totalEjecutado = totalEjecutado + item.getEjecutado().doubleValue();
            }
        }
        totalDebe = Math.round(totalDebe * 100.0) / 100.0;
        totalHaber = Math.round(totalHaber * 100.0) / 100.0;
        totalComprometido = Math.round(totalComprometido * 100.0) / 100.0;
        totalDevengado = Math.round(totalDevengado * 100.0) / 100.0;
        totalEjecutado = Math.round(totalEjecutado * 100.0) / 100.0;
        if (totalDebe == totalHaber) {
            contDiarioGeneral.setCuadrado(Boolean.TRUE);
        } else {
            contDiarioGeneral.setCuadrado(Boolean.FALSE);
        }
    }

    public void saveDiario() {
        contDiarioGeneral.setDebe(new BigDecimal(totalDebe));
        contDiarioGeneral.setHaber(new BigDecimal(totalHaber));
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_EMISION_RECAUDACION);
        String mensaje = contRegistroContable.validaciones(contDiarioGeneral, contDiarioGeneralDetallesList);
        if (mensaje.equals("")) {
            if (contDiarioGeneral.getId() != null) {
                contRegistroContable.edit(contDiarioGeneral, contDiarioGeneralDetallesList, contDiarioGeneralDetallesDelete);
            } else {
                contDiarioGeneral = contRegistroContable.create(contDiarioGeneral, contDiarioGeneralDetallesList, null, true);
            }
            JsfUtil.executeJS("PF('dlgConfirmacion').show()");
            PrimeFaces.current().ajax().update("dlgConfirmacionForm");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", mensaje);
        }
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
                formInicializar();
                JsfUtil.executeJS("PF('dlgConfirmacion').hide()");
                PrimeFaces.current().ajax().update("formMain");
                break;
        }
    }

    public void deleteRegistro(ContDiarioGeneralDetalle detalle, int indice) {
        if (detalle.getId() != null) {
            contDiarioGeneralDetallesDelete.add(detalle);
            contDiarioGeneralDetallesList.remove(detalle);
        } else {
            contDiarioGeneralDetallesList.remove(indice);
        }
        actualizarList();
        calcularTotales();
    }

    public void actualizarList() {
        int aux = 1;
        for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
            item.setNumeracion(aux);
            aux += 1;
        }
    }

    public void cargarEmision() {
        String fechaEMi = Utils.dateFormatPattern("yyyy-MM-dd", fechaEmision);
        System.out.println("fecha emision>>" + fechaEMi);
        System.out.println("tipo contable>>" + tipoContable);
        switch (tipoContable) {
            case "EMISION":
                cargarDatoEmisiones(fechaEMi);
                break;
            case "RECAUDACION":
                break;
            case "ANULACIONES":
                break;
            case "EMISION_FONDO_TERCERO":
                cargarDatoEmisionesFondoTercero(fechaEMi);
                break;
        }

        contDiarioGeneral.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_egreso"));
        actualizarTipol();
        contDiarioGeneral.setCodModulo(Variables.DG_PARTE_EMISIONES_RECAUDACIONES);
        contDiarioGeneral.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_financiero"));
        this.calcularTotales();
    }

    public void cargarDatoEmisiones(String fechaEMi) {
        partePresupuestariaModelList = contRegistroContable.getEmisionDiaria(fechaEMi);
        if (partePresupuestariaModelList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar uno o varios registros");
            return;
        }
        int cont = 1;
        for (PartePresupuestariaModel item : partePresupuestariaModelList) {
            if (item.getIdreserva() != null) {
                ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                detalle.setIdContCuentas(contRegistroContable.findByIdContCuenta(item.getIdreserva()));
                detalle.setPartidaPresupuestaria(item.getPartidapresupuestaria());
                detalle.setIdPresCatalogoPresupuestario(contRegistroContable.findByIdCatalogo(item.getIdprescatalogopresupuestario()));
                detalle.setNumeracion(cont);

                detalle.setDebe(item.getSaldodisponible());
                detalle.setHaber(item.getMontoDevengado());

                detalle.setDevengado(item.getMontoDevengado());
                detalle.setAnio(item.getAnio());
                detalle.setEstadoLiquidacion(item.getEstadoLiquidacion());
                detalle.setTipoRegistro(catalogoService.getTipoItem(CONFIG.COD_DEVENGADO));
                detalle.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(item.getIdpresfuentefinanciamiento()));
//            detalle.setIdPresPlanProgramatico(new PresPlanProgramatico(item.getIdpresplanprogramatico()));
//            detalle.setIdDetalleReservaCompromiso(new BigInteger(item.getIdreserva().toString()));
                detalle.setDatoCargado(Boolean.TRUE);
                contDiarioGeneralDetallesList.add(detalle);
                cont += 1;
                loadData = true;
            }
        }
    }

    public void cargarDatoEmisionesFondoTercero(String fechaEMi) {
        FinaRenRubrosLiquidacion rubro = null;
        BigDecimal valor = BigDecimal.ZERO, porcentaje = BigDecimal.ZERO, cien = new BigDecimal(100);

        partePresupuestariaModelList = contRegistroContable.getEmisionFondoTercero(fechaEMi);
        if (partePresupuestariaModelList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar uno o varios registros");
            return;
        }
        int cont = 1;
        for (PartePresupuestariaModel item : partePresupuestariaModelList) {
            if (item.getIdreserva() != null) {

                ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                detalle.setIdContCuentas(contRegistroContable.findByIdContCuenta(item.getIdreserva()));
                detalle.setPartidaPresupuestaria(item.getPartidapresupuestaria());
                detalle.setIdPresCatalogoPresupuestario(contRegistroContable.findByIdCatalogo(item.getIdprescatalogopresupuestario()));
                detalle.setNumeracion(cont);
                if (item.getRubro() != null) {
                    rubro = rubrosLiquidacionService.find(item.getRubro());
                    if (rubro.getPorcentajeServicio()) {
                        System.out.println("cien>>" + cien + " rubro porcentaje>>" + rubro.getPorcentaje());
                        porcentaje = cien.subtract(rubro.getPorcentaje());
                        System.out.println("porcentaje>>" + porcentaje + " saldoDisponible>>" + item.getSaldodisponible());
                        detalle.setDebe(item.getSaldodisponible().multiply(porcentaje).divide(cien));
                        detalle.setHaber(item.getMontoDevengado().multiply(porcentaje).divide(cien));
                        detalle.setDevengado(item.getMontoDevengado().multiply(porcentaje).divide(cien));
                    } else {
                        System.out.println("porcentaje_2>>" + porcentaje + " saldoDisponible>>" + item.getSaldodisponible());
                        detalle.setDebe(item.getSaldodisponible().multiply(porcentaje).divide(cien));
                        detalle.setHaber(item.getMontoDevengado().multiply(porcentaje).divide(cien));
                        detalle.setDevengado(item.getMontoDevengado().multiply(porcentaje).divide(cien));
                    }
                } else {
                    detalle.setDebe(item.getSaldodisponible());
                    detalle.setHaber(item.getMontoDevengado());
                    detalle.setDevengado(item.getMontoDevengado());
                }
                detalle.setAnio(item.getAnio());
                detalle.setEstadoLiquidacion(item.getEstadoLiquidacion());
                detalle.setTipoRegistro(catalogoService.getTipoItem(CONFIG.COD_DEVENGADO));
                detalle.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(item.getIdpresfuentefinanciamiento()));
//            detalle.setIdPresPlanProgramatico(new PresPlanProgramatico(item.getIdpresplanprogramatico()));
//            detalle.setIdDetalleReservaCompromiso(new BigInteger(item.getIdreserva().toString()));
                detalle.setDatoCargado(Boolean.TRUE);
                contDiarioGeneralDetallesList.add(detalle);
                cont += 1;
                loadData = true;
            }
        }
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ContDiarioGeneral> getContDiarioGeneralLazy() {
        return contDiarioGeneralLazy;
    }

    public void setContDiarioGeneralLazy(LazyModel<ContDiarioGeneral> contDiarioGeneralLazy) {
        this.contDiarioGeneralLazy = contDiarioGeneralLazy;
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

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetallesList() {
        return contDiarioGeneralDetallesList;
    }

    public void setContDiarioGeneralDetallesList(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList) {
        this.contDiarioGeneralDetallesList = contDiarioGeneralDetallesList;
    }

    public List<CatalogoItem> getClasesDiarioGeneral() {
        return clasesDiarioGeneral;
    }

    public void setClasesDiarioGeneral(List<CatalogoItem> clasesDiarioGeneral) {
        this.clasesDiarioGeneral = clasesDiarioGeneral;
    }

    public List<CatalogoItem> getTiposDiarioGeneral() {
        return tiposDiarioGeneral;
    }

    public void setTiposDiarioGeneral(List<CatalogoItem> tiposDiarioGeneral) {
        this.tiposDiarioGeneral = tiposDiarioGeneral;
    }

    public List<PartePresupuestariaModel> getPartePresupuestariaModelList() {
        return partePresupuestariaModelList;
    }

    public void setPartePresupuestariaModelList(List<PartePresupuestariaModel> partePresupuestariaModelList) {
        this.partePresupuestariaModelList = partePresupuestariaModelList;
    }

    public double getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(double totalDebe) {
        this.totalDebe = totalDebe;
    }

    public double getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(double totalHaber) {
        this.totalHaber = totalHaber;
    }

    public double getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(double totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public double getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(double totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public double getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(double totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public PartePresupuestariaModel getPartePresupuestariaModel() {
        return partePresupuestariaModel;
    }

    public void setPartePresupuestariaModel(PartePresupuestariaModel partePresupuestariaModel) {
        this.partePresupuestariaModel = partePresupuestariaModel;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoContable() {
        return tipoContable;
    }

    public void setTipoContable(String tipoContable) {
        this.tipoContable = tipoContable;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getLoadData() {
        return loadData;
    }

    public void setLoadData(Boolean loadData) {
        this.loadData = loadData;
    }

    public Boolean getTipoDlg() {
        return tipoDlg;
    }

    public void setTipoDlg(Boolean tipoDlg) {
        this.tipoDlg = tipoDlg;
    }

    public Boolean getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(Boolean tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

}
