/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author ENRIQUE
 */
@Named(value = "reformaRecepcionSolicitudView")
@ViewScoped
public class ReformaRecepcionSolicitudController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaTraspasoService reformaTraspasoService;
    @Inject
    private PlanProgramaticoService planProgramaticoService;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;
    private ReformaTraspaso reformaTraspaso;
    private OpcionBusqueda busqueda;
    private double totalTraspasoReduccion, totalTraspasoIncremento;
    private List<Producto> listaProducto;
    private List<Producto> listaProductoSA;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private boolean btnAprobar, btnRechazar, btnAnular, renderTabPAPP, renderedPartidaDirecta;
    private String observaciones;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                CatalogoItem estadopappaprobado = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REGP-REF");
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("estadoReformaTramite:equal", estadopappaprobado);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                btnAprobar = false;
                btnRechazar = false;
                btnAnular = false;
                busqueda = new OpcionBusqueda();
                listaProductoSA = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void verificarProductosActualizados(){
        reformaTraspaso = reformaTraspasoService.verificarNumTramiteReforma(BigInteger.valueOf(tramite.getNumTramite()), busqueda.getAnio());
        listaProductoSA = reformaTraspasoService.getProductoModificadas( BigInteger.valueOf(reformaTraspaso.getId()));
        System.out.println("Size: "+listaProductoSA.size());
    }

    public void consultarPapp(ReformaTraspaso reforma) {
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        reformaTraspaso = reforma;
        busqueda.setAnio(reforma.getPeriodo());
        totalTraspasoIncremento = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, true);
        totalTraspasoReduccion = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, false);
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
        renderTabPAPP = !listaProducto.isEmpty();
        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPDIReformaT1(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
    }

    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (reforma.getTipo()) {
            listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId()));
            for (Producto item : listaProducto) {
                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (!listaPartidaDirecta.isEmpty()) {
                for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
                }
            }
        } else {
            busqueda.setAnio(reforma.getPeriodo());
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivo = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reforma.getId()));
            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reforma.getId()));
            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
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
        }
        if (incremento) {
            return totalTraspasoIncrementoR;
        } else {
            return totalTraspasoReduccionR;
        }
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            if (listaProducto.isEmpty() && !listaPartidaDirecta.isEmpty()) {
                getParamts().put("userFinan", clienteService.getrolsUser(RolUsuario.rolReformasPDI));
            } else {
                getParamts().put("userFinan", clienteService.getrolsUser(RolUsuario.directorFinanciero));
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.addInformationMessage("Información", "Solicitud se ha entregado con éxito");
            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public LazyModel<ReformaTraspaso> getLazyReformaTraspaso() {
        return lazyReformaTraspaso;
    }

    public void setLazyReformaTraspaso(LazyModel<ReformaTraspaso> lazyReformaTraspaso) {
        this.lazyReformaTraspaso = lazyReformaTraspaso;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public double getTotalTraspasoReduccion() {
        return totalTraspasoReduccion;
    }

    public void setTotalTraspasoReduccion(double totalTraspasoReduccion) {
        this.totalTraspasoReduccion = totalTraspasoReduccion;
    }

    public double getTotalTraspasoIncremento() {
        return totalTraspasoIncremento;
    }

    public void setTotalTraspasoIncremento(double totalTraspasoIncremento) {
        this.totalTraspasoIncremento = totalTraspasoIncremento;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public List<PartidasDistributivo> getListaPartidaDistributivo() {
        return listaPartidaDistributivo;
    }

    public void setListaPartidaDistributivo(List<PartidasDistributivo> listaPartidaDistributivo) {
        this.listaPartidaDistributivo = listaPartidaDistributivo;
    }

    public List<PartidasDistributivoAnexo> getListaPartidaDistributivoAnexo() {
        return listaPartidaDistributivoAnexo;
    }

    public void setListaPartidaDistributivoAnexo(List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo) {
        this.listaPartidaDistributivoAnexo = listaPartidaDistributivoAnexo;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidaDirecta() {
        return listaPartidaDirecta;
    }

    public void setListaPartidaDirecta(List<ProformaPresupuestoPlanificado> listaPartidaDirecta) {
        this.listaPartidaDirecta = listaPartidaDirecta;
    }

    public boolean isBtnAprobar() {
        return btnAprobar;
    }

    public void setBtnAprobar(boolean btnAprobar) {
        this.btnAprobar = btnAprobar;
    }

    public boolean isBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public boolean isBtnAnular() {
        return btnAnular;
    }

    public void setBtnAnular(boolean btnAnular) {
        this.btnAnular = btnAnular;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public boolean isRenderTabPAPP() {
        return renderTabPAPP;
    }

    public void setRenderTabPAPP(boolean renderTabPAPP) {
        this.renderTabPAPP = renderTabPAPP;
    }

    public boolean isRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }

    public List<Producto> getListaProductoSA() {
        return listaProductoSA;
    }

    public void setListaProductoSA(List<Producto> listaProductoSA) {
        this.listaProductoSA = listaProductoSA;
    }
    
    

}
