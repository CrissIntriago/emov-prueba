/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Model.ReformasUnificadas;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reporteReformaPresupView")
@ViewScoped
public class ReporteReformaPresupuestariaController implements Serializable {

    private List<ReformasUnificadas> listaReforma;
    private List<String> listaSubReforma;
    private List<MasterCatalogo> periodos;
    private List<Producto> listaProducto;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;
    private ReformaIngresoSuplemento reformaIngresoSuplemento;
    private ReformaIngresoSuplemento reformaIngresoReduccion;
    private ReformaIngresoSuplemento reformaEgresoSuplemento;
    private ReformaIngresoSuplemento reformaEgresoReduccion;
    private ReformaIngresoSuplemento reformaObjeto;
    private ReformaTraspaso reformaTraspasoT1;
    private ReformaTraspaso reformaTraspasoT2;
    private OpcionBusqueda busqueda;
    private boolean traspaso;
    private boolean suplemento;
    private boolean reduccion;

    @Inject
    private ServletSession ss;

    @Inject
    private ReformaTraspasoService reformaTraspasoService;

    @Inject
    private ReformaSuplementoIngresoService reformaSuplementoIngresoService;

    @Inject
    private CatalogoService catalogoService;

    @Inject
    private MasterCatalogoService masterCatalogoService;

    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;

    @PostConstruct
    public void initView() {
        listaReforma = new ArrayList<>();
        reformaIngresoReduccion = new ReformaIngresoSuplemento();
        reformaIngresoSuplemento = new ReformaIngresoSuplemento();
        reformaEgresoSuplemento = new ReformaIngresoSuplemento();
        reformaEgresoReduccion = new ReformaIngresoSuplemento();

        reformaTraspasoT1 = new ReformaTraspaso();
        reformaTraspasoT2 = new ReformaTraspaso();
        busqueda = new OpcionBusqueda();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        listarReformasEnMemoria();
    }

    public void listarReformasEnMemoria() {
        listaReforma = new ArrayList<>();
        List<ReformaIngresoSuplemento> listReformaIngreso1 = new ArrayList<>();
        List<ReformaIngresoSuplemento> listReformaIngreso2 = new ArrayList<>();
        List<ReformaTraspaso> listReformaT1 = new ArrayList<>();
        List<ReformaTraspaso> listReformaT2 = new ArrayList<>();

        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        CatalogoItem estadoReformAprobada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        listReformaT1 = reformaTraspasoService.getListReforma(busqueda.getAnio(), estadoReform, true, estadoReformAprobada);
        listReformaT2 = reformaTraspasoService.getListReforma(busqueda.getAnio(), estadoReform, false, estadoReformAprobada);
        listReformaIngreso1 = reformaSuplementoIngresoService.listaReformaSuplmento(true);

        if (!listReformaT1.isEmpty()) {
            for (ReformaTraspaso refT1 : listReformaT1) {

                listaReforma.add(setEstructuraReformaT1(refT1));
            }
        }
        
        if(listReformaT2.isEmpty()){
            for (ReformaTraspaso refT2 : listReformaT2) {
                listaReforma.add(setEstructuraReformaT2(refT2));
            }
        }
    }

    public void imprimirPdf() {

    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formularioGeneral:dataReformaUnificada");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formularioGeneral:dataReformaUnificada");
        }
    }

    public ReformasUnificadas setEstructuraReformaT1(ReformaTraspaso ref) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        listaProducto = reformaTraspasoService.getListProductoByReforma(ref.getPeriodo(), ref.getUnidadRequiriente().getId(), "APRT", true, BigInteger.valueOf(ref.getId()));
        if (!listaProducto.isEmpty()) {
            for (Producto item : listaProducto) {

                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
        }
        ReformasUnificadas reforma = new ReformasUnificadas(ref.getPeriodo().toString(), "T1", ref.getId(), ref.getCodigo(), ref.getFechaTraspasoReforma(), ref.getFechaAprobacion(),
                true, false, totalTraspasoIncrementoR, totalTraspasoReduccionR, "APROBADA");
        return reforma;
    }

    public ReformasUnificadas setEstructuraReformaT2(ReformaTraspaso reform) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reform.getPeriodo(), "REGT", true, BigInteger.valueOf(reform.getId()));
        listaPartidaDistributivo = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reform.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reform.getId()));
        listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reform.getPeriodo(), BigInteger.valueOf(reform.getId()));
        if (listaProducto.isEmpty()) {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reform.getPeriodo(), "APRT", true, BigInteger.valueOf(reform.getId()));
        }

        if (!listaProducto.isEmpty()) {
            for (Producto item : listaProducto) {

                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
        }
        if (!listaPartidaDistributivo.isEmpty()) {
            for (PartidasDistributivo listaRubro : listaPartidaDistributivo) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaRubro.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + listaRubro.getTraspasoReduccion().doubleValue();
            }
        }
        if (!listaPartidaDistributivoAnexo.isEmpty()) {
            for (PartidasDistributivoAnexo listaAnexo : listaPartidaDistributivoAnexo) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaAnexo.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + listaAnexo.getTraspasoReduccion().doubleValue();
            }
        }
        if (!listaPartidaDirecta.isEmpty()) {
            for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
            }
        }
        ReformasUnificadas reforma = new ReformasUnificadas(reform.getPeriodo().toString(), "T2", reform.getId(), reform.getCodigo(), reform.getFechaTraspasoReforma(), reform.getFechaAprobacion(),
                false, false, totalTraspasoIncrementoR, totalTraspasoReduccionR, "APROBADA");
        return reforma;
    }

    public List<ReformasUnificadas> getListaReforma() {
        return listaReforma;
    }

    public void setListaReforma(List<ReformasUnificadas> listaReforma) {
        this.listaReforma = listaReforma;
    }

    public List<String> getListaSubReforma() {
        return listaSubReforma;
    }

    public void setListaSubReforma(List<String> listaSubReforma) {
        this.listaSubReforma = listaSubReforma;
    }

    public ReformaIngresoSuplemento getReformaIngresoSuplemento() {
        return reformaIngresoSuplemento;
    }

    public void setReformaIngresoSuplemento(ReformaIngresoSuplemento reformaIngresoSuplemento) {
        this.reformaIngresoSuplemento = reformaIngresoSuplemento;
    }

    public ReformaIngresoSuplemento getReformaIngresoReduccion() {
        return reformaIngresoReduccion;
    }

    public void setReformaIngresoReduccion(ReformaIngresoSuplemento reformaIngresoReduccion) {
        this.reformaIngresoReduccion = reformaIngresoReduccion;
    }

    public ReformaIngresoSuplemento getReformaEgresoSuplemento() {
        return reformaEgresoSuplemento;
    }

    public void setReformaEgresoSuplemento(ReformaIngresoSuplemento reformaEgresoSuplemento) {
        this.reformaEgresoSuplemento = reformaEgresoSuplemento;
    }

    public ReformaIngresoSuplemento getReformaEgresoReduccion() {
        return reformaEgresoReduccion;
    }

    public void setReformaEgresoReduccion(ReformaIngresoSuplemento reformaEgresoReduccion) {
        this.reformaEgresoReduccion = reformaEgresoReduccion;
    }

    public ReformaIngresoSuplemento getReformaObjeto() {
        return reformaObjeto;
    }

    public void setReformaObjeto(ReformaIngresoSuplemento reformaObjeto) {
        this.reformaObjeto = reformaObjeto;
    }

    public ReformaTraspaso getReformaTraspasoT1() {
        return reformaTraspasoT1;
    }

    public void setReformaTraspasoT1(ReformaTraspaso reformaTraspasoT1) {
        this.reformaTraspasoT1 = reformaTraspasoT1;
    }

    public ReformaTraspaso getReformaTraspasoT2() {
        return reformaTraspasoT2;
    }

    public void setReformaTraspasoT2(ReformaTraspaso reformaTraspasoT2) {
        this.reformaTraspasoT2 = reformaTraspasoT2;
    }

    public boolean isTraspaso() {
        return traspaso;
    }

    public void setTraspaso(boolean traspaso) {
        this.traspaso = traspaso;
    }

    public boolean isReduccion() {
        return reduccion;
    }

    public void setReduccion(boolean reduccion) {
        this.reduccion = reduccion;
    }

    public boolean isSuplemento() {
        return suplemento;
    }

    public void setSuplemento(boolean suplemento) {
        this.suplemento = suplemento;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

}
