/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.ProformaPresupuestoPlanificadoLazy;
import com.origami.sigef.contabilidad.model.presupuestoModel;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "presupuestoAprobadoView")
@ViewScoped
public class PresupuestoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private PresupuestoService presService;
    @Inject
    private PlanProgramaticoService planService;
    @Inject
    private CatalogoPresupuestoService itemService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaService;
    @Inject
    private CatalogoService catalogoService;

    private List<CatalogoProformaPresupuesto> listaFechas;
    private ProformaPresupuestoPlanificadoLazy lazyEgresos;
    private OpcionBusqueda opcionBusqueda;
    private List<presupuestoModel> lista;
    private List<presupuestoModel> lista2;
    private Presupuesto presupuesto;
    private LazyModel<Presupuesto> lazy;

    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private List<PresPlanProgramatico> listaEstrcutura;
    private List<PresCatalogoPresupuestario> listaItem;
    private List<PresFuenteFinanciamiento> listaFuente;
    private BigDecimal toialPresupuesto;
    private List<CatalogoItem> listafuenteFinanciamiento;
    private BigDecimal totalPresupuestoInicial;
    private BigDecimal totalCodificado;
    private BigDecimal totalReformaS;
    private BigDecimal totalReformaR;
    private Boolean tipoPresupuesto;

    @PostConstruct
    public void inicializar() {
        Calendar fecha = new GregorianCalendar();
        presupuesto = new Presupuesto();
        lista = new ArrayList<>();
        lista2 = new ArrayList<>();
        listaItem = presService.listaItmes();
        listaEstrcutura = presService.listaEstructura();
        listaFuente = presService.listaFuente();
        listaFechas = catalogoProformaService.getFechaAprobada();
        totalPresupuestoInicial = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;
        totalReformaS = BigDecimal.ZERO;
        totalReformaR = BigDecimal.ZERO;
        totalIngresos = BigDecimal.ZERO;
        totalEgresos = BigDecimal.ZERO;

        int fechaActual = fecha.get(Calendar.YEAR);

        String anio = String.valueOf(fechaActual);
        presupuesto.setPeriodo(Short.valueOf(anio));
        lazy = new LazyModel(Presupuesto.class);
        lazy.getFilterss().put("periodo:equal", Short.valueOf(anio));
        lazy.getSorteds().put("partida", "ASC");
        lazy.setDistinct(false);

        totalIngresos = presService.totalIngresoAprobados(presupuesto.getPeriodo());
        totalEgresos = presService.totalEgresoAprobados(presupuesto.getPeriodo());
        totalPresupuestoInicial = presService.totalPresupuestoInicial(presupuesto.getPeriodo());
        totalReformaS = presService.totalReformaSuplemenataria(presupuesto.getPeriodo());
        totalReformaR = presService.totalReformaReduccion(presupuesto.getPeriodo());
        totalCodificado = presService.totalPresupuestoCodificado(presupuesto.getPeriodo());
        tipoPresupuesto = null;

    }

//    @SuppressWarnings("null")
    public void actualizarTable() {

        totalPresupuestoInicial = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;
        totalReformaS = BigDecimal.ZERO;
        totalReformaR = BigDecimal.ZERO;
        totalIngresos = BigDecimal.ZERO;
        totalEgresos = BigDecimal.ZERO;

        if (presupuesto != null) {

            CatalogoProformaPresupuesto ingreso = new CatalogoProformaPresupuesto();
            CatalogoProformaPresupuesto egreso = new CatalogoProformaPresupuesto();
            List<CatalogoProformaPresupuesto> catalogo = presService.getProformaAprobadas(presupuesto.getPeriodo());
            if (catalogo.size() == 2) {
                for (CatalogoProformaPresupuesto item : catalogo) {
                    if (item.getTipoFlujo().equals(true)) {
                        ingreso = item;
                    } else {
                        egreso = item;
                    }
                    if (ingreso != null && egreso != null) {
                        lazyEgresos = new ProformaPresupuestoPlanificadoLazy(presupuesto.getPeriodo());
                        lazy = new LazyModel(Presupuesto.class);
                        lazy.getFilterss().put("periodo:equal", null);
                    }
                }
                if (ingreso != null && egreso != null) {
                    lazyEgresos = new ProformaPresupuestoPlanificadoLazy(presupuesto.getPeriodo());
                    lazy = new LazyModel(Presupuesto.class);
                    lazy.getFilterss().put("periodo:equal", presupuesto.getPeriodo());
                    lazy.getSorteds().put("partida", "ASC");
                    lazy.setDistinct(false);

                }
            } else {
                return;
            }

            totalIngresos = presService.totalIngresoAprobados(presupuesto.getPeriodo());
            totalEgresos = presService.totalEgresoAprobados(presupuesto.getPeriodo());
            totalPresupuestoInicial = presService.totalPresupuestoInicial(presupuesto.getPeriodo());
            totalReformaS = presService.totalReformaSuplemenataria(presupuesto.getPeriodo());
            totalReformaR = presService.totalReformaReduccion(presupuesto.getPeriodo());
            totalCodificado = presService.totalPresupuestoCodificado(presupuesto.getPeriodo());
        } else {
            lazy = null;
        }
    }

    public void abrirDlogo(Presupuesto p) {
        this.presupuesto = p;
        PrimeFaces.current().executeScript("PF('DlgPresupuesto').show()");
        PrimeFaces.current().ajax().update(":formPresuto");
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("presupuestoForm:tablePresupuesto");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("presupuestoForm:tablePresupuesto");
        }
    }

    public void modificarPresupuesto() {

        presService.edit(presupuesto);
        PrimeFaces.current().executeScript("PF('DlgPresupuesto').hide()");
        PrimeFaces.current().ajax().update(":formPresuto");
        PrimeFaces.current().ajax().update("messages");
    }

    public void actulizarValorIngresEgreso() {
        totalPresupuestoInicial = BigDecimal.ZERO;
        totalReformaS = BigDecimal.ZERO;
        totalReformaR = BigDecimal.ZERO;
        totalCodificado = BigDecimal.ZERO;

        if (tipoPresupuesto == null) {
            totalIngresos = presService.totalIngresoAprobados(presupuesto.getPeriodo());
            totalEgresos = presService.totalEgresoAprobados(presupuesto.getPeriodo());
            totalPresupuestoInicial = presService.totalPresupuestoInicial(presupuesto.getPeriodo());
            totalReformaS = presService.totalReformaSuplemenataria(presupuesto.getPeriodo());
            totalReformaR = presService.totalReformaReduccion(presupuesto.getPeriodo());
            totalCodificado = presService.totalPresupuestoCodificado(presupuesto.getPeriodo());
        } else if (tipoPresupuesto) {

            totalPresupuestoInicial = presService.totalPresupuestoInicialFiltrado(presupuesto.getPeriodo(), true);
            totalReformaS = presService.totalReformaSuplemenatariaFiltrado(presupuesto.getPeriodo(), true);
            totalReformaR = presService.totalReformaReduccionFiltrado(presupuesto.getPeriodo(), true);
            totalCodificado = presService.totalPresupuestoCodificadoFiltrado(presupuesto.getPeriodo(), true);

        } else {
            totalPresupuestoInicial = presService.totalPresupuestoInicialFiltrado(presupuesto.getPeriodo(), false);
            totalReformaS = presService.totalReformaSuplemenatariaFiltrado(presupuesto.getPeriodo(), false);
            totalReformaR = presService.totalReformaReduccionFiltrado(presupuesto.getPeriodo(), false);
            totalCodificado = presService.totalPresupuestoCodificadoFiltrado(presupuesto.getPeriodo(), false);
        }

    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public PresupuestoService getPresService() {
        return presService;
    }

    public void setPresService(PresupuestoService presService) {
        this.presService = presService;
    }

    public Boolean getTipoPresupuesto() {
        return tipoPresupuesto;
    }

    public void setTipoPresupuesto(Boolean tipoPresupuesto) {
        this.tipoPresupuesto = tipoPresupuesto;
    }

    public PlanProgramaticoService getPlanService() {
        return planService;
    }

    public void setPlanService(PlanProgramaticoService planService) {
        this.planService = planService;
    }

    public CatalogoPresupuestoService getItemService() {
        return itemService;
    }

    public void setItemService(CatalogoPresupuestoService itemService) {
        this.itemService = itemService;
    }

    public FuenteFinanciamientoService getFuenteService() {
        return fuenteService;
    }

    public void setFuenteService(FuenteFinanciamientoService fuenteService) {
        this.fuenteService = fuenteService;
    }

    public CatalogoProformaPresupuestoService getCatalogoProformaService() {
        return catalogoProformaService;
    }

    public void setCatalogoProformaService(CatalogoProformaPresupuestoService catalogoProformaService) {
        this.catalogoProformaService = catalogoProformaService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public List<CatalogoItem> getListafuenteFinanciamiento() {
        return listafuenteFinanciamiento;
    }

    public void setListafuenteFinanciamiento(List<CatalogoItem> listafuenteFinanciamiento) {
        this.listafuenteFinanciamiento = listafuenteFinanciamiento;
    }

    public List<CatalogoProformaPresupuesto> getListaFechas() {
        return listaFechas;
    }

    public void setListaFechas(List<CatalogoProformaPresupuesto> listaFechas) {
        this.listaFechas = listaFechas;
    }

    public ProformaPresupuestoPlanificadoLazy getLazyEgresos() {
        return lazyEgresos;
    }

    public void setLazyEgresos(ProformaPresupuestoPlanificadoLazy lazyEgresos) {
        this.lazyEgresos = lazyEgresos;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<presupuestoModel> getLista() {
        return lista;
    }

    public void setLista(List<presupuestoModel> lista) {
        this.lista = lista;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(BigDecimal totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public LazyModel<Presupuesto> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<Presupuesto> lazy) {
        this.lazy = lazy;
    }

    public List<presupuestoModel> getLista2() {
        return lista2;
    }

    public void setLista2(List<presupuestoModel> lista2) {
        this.lista2 = lista2;
    }

    public BigDecimal getToialPresupuesto() {
        return toialPresupuesto;
    }

    public void setToialPresupuesto(BigDecimal toialPresupuesto) {
        this.toialPresupuesto = toialPresupuesto;
    }

    public List<PresPlanProgramatico> getListaEstrcutura() {
        return listaEstrcutura;
    }

    public void setListaEstrcutura(List<PresPlanProgramatico> listaEstrcutura) {
        this.listaEstrcutura = listaEstrcutura;
    }

    public List<PresCatalogoPresupuestario> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<PresCatalogoPresupuestario> listaItem) {
        this.listaItem = listaItem;
    }

    public List<PresFuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<PresFuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public BigDecimal getTotalPresupuestoInicial() {
        return totalPresupuestoInicial;
    }

    public void setTotalPresupuestoInicial(BigDecimal totalPresupuestoInicial) {
        this.totalPresupuestoInicial = totalPresupuestoInicial;
    }

    public BigDecimal getTotalCodificado() {
        return totalCodificado;
    }

    public void setTotalCodificado(BigDecimal totalCodificado) {
        this.totalCodificado = totalCodificado;
    }

    public BigDecimal getTotalReformaS() {
        return totalReformaS;
    }

    public void setTotalReformaS(BigDecimal totalReformaS) {
        this.totalReformaS = totalReformaS;
    }

    public BigDecimal getTotalReformaR() {
        return totalReformaR;
    }

    public void setTotalReformaR(BigDecimal totalReformaR) {
        this.totalReformaR = totalReformaR;
    }
//</editor-fold>

}
