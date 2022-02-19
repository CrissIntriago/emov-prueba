/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author Sandra Arroba
 */
@Named(value = "planificacionReformaT2View")
@ViewScoped
public class ReformaPlanificacionTraspasoT2Controller extends BpmnBaseRoot implements Serializable {

    private Producto producto;
    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private OpcionBusqueda busqueda;
    private Distributivo dataView;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    private List<Producto> listaProducto;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;
    
    private List<Distributivo> listaDistributivo;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<PartidasDistributivo> listaVista;
    
    private String orbservaciones;
    private boolean btnAprobar, btnRechazar;
    private String observaciones;
    private boolean vistaPartidaDisGeneral;
    private BigDecimal rmu;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;

    @Inject
    private ReformaTraspasoService reformaTraspasoService;

    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;

    @Inject
    private UserSession userSession;

    @Inject
    private ClienteService clienteService;

    @Inject
    private ProductoService productoService;

    @Inject
    private CatalogoService catalogoService;

    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    
    @Inject
    private ValoresDistributivoService valoresService;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                reformaTraspaso = new ReformaTraspaso();
                detalleReformaTraspaso = new DetalleReformaTraspaso();
                busqueda = new OpcionBusqueda();
                dataView = new Distributivo();

                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
                CatalogoItem estadoReformaTramite = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");

                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
//        lazyReformaTraspaso.getFilterss().put("id:equal", reformaTraspaso.getId());
                lazyReformaTraspaso.getFilterss().put("estadoReforma:equal", estadoReforma);
                lazyReformaTraspaso.getFilterss().put("tipo:equal", false);
//                lazyReformaTraspaso.getFilterss().put("estadoReformaTramite:equal", estadoReformaTramite);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                btnAprobar = false;
                btnRechazar = false;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public List<Producto> obtProdUniRespRefor (Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }
    
    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (reforma.getTipo()) {
            listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId())); //ENRIQUE
            //reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId())); ENRIQUE

            for (Producto item : listaProducto) {

                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
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

    public void enviar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REGP-REF");

        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " enviada con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
    }

    public void observar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "OBS-REF");
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " observada con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
    }

    public void consultarPapp(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        listaProducto = new ArrayList<>();
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        listaDistributivo = new ArrayList<>();
        listaPartidaDistributivoAnexo = new ArrayList<>();
        if (reformaTraspaso.getTipo()) {
            listaProducto = reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), reforma.getEstadoReforma().getCodigo(), true, BigInteger.valueOf(reforma.getId()));            
        } else {
            listaProducto = reformaTraspasoService.getListProductoByReformaConsulta(reforma.getPeriodo(), true, BigInteger.valueOf(reforma.getId()));
//            listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
//            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
//            listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
            renderedPAPP = !listaProducto.isEmpty();
            renderedDistributivo = !listaDistributivo.isEmpty();
            renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
            renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        }

        busqueda.setAnio(reformaTraspaso.getPeriodo());

        PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
    }
    
    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
      //  setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        dataView = d;
        if (vista) {
            vistaPartidaDisGeneral = true;
        } else {
            vistaPartidaDisGeneral = false;
        }
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void aprobarSolicitud(boolean a, ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (a) {
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
        }
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }
    
    public void completarTarea(int aprobar) {

        try {
            observacion.setObservacion(observaciones);
            if (aprobar == 1) {
                getParamts().put("userAsistDireccion", clienteService.getrolsUser(RolUsuario.asistenteDireccion));
                enviar(reformaTraspaso);
            } else {
                getParamts().put("usuario", reformaTraspaso.getUsuarioCreacion());
                observar(reformaTraspaso);
            }

            getParamts().put("aprobado", aprobar);

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            if (aprobar == 1) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solicitud se aprobado con éxito");
            } else {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", "Solicitud se ha rechazado con éxito");
            }

            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
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

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
    }

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public DetalleReformaTraspaso getDetalleReformaTraspaso() {
        return detalleReformaTraspaso;
    }

    public void setDetalleReformaTraspaso(DetalleReformaTraspaso detalleReformaTraspaso) {
        this.detalleReformaTraspaso = detalleReformaTraspaso;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public PlanAnualPoliticaPublica getPlanAnualPoliticaPublica() {
        return planAnualPoliticaPublica;
    }

    public void setPlanAnualPoliticaPublica(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanAnualProgramaProyecto getPlanAnualProgramaProyecto() {
        return planAnualProgramaProyecto;
    }

    public void setPlanAnualProgramaProyecto(PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public LazyModel<ReformaTraspaso> getLazyReformaTraspaso() {
        return lazyReformaTraspaso;
    }

    public void setLazyReformaTraspaso(LazyModel<ReformaTraspaso> lazyReformaTraspaso) {
        this.lazyReformaTraspaso = lazyReformaTraspaso;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
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

    public List<Distributivo> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<Distributivo> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public List<PartidasDistributivo> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<PartidasDistributivo> listaVista) {
        this.listaVista = listaVista;
    }
    

    public String getOrbservaciones() {
        return orbservaciones;
    }

    public void setOrbservaciones(String orbservaciones) {
        this.orbservaciones = orbservaciones;
    }
    
    public boolean isRenderedDistributivo() {
        return renderedDistributivo;
    }

    public void setRenderedDistributivo(boolean renderedDistributivo) {
        this.renderedDistributivo = renderedDistributivo;
    }

    public boolean isRenderedDistributivoAnexo() {
        return renderedDistributivoAnexo;
    }

    public void setRenderedDistributivoAnexo(boolean renderedDistributivoAnexo) {
        this.renderedDistributivoAnexo = renderedDistributivoAnexo;
    }

    public boolean isRenderedPAPP() {
        return renderedPAPP;
    }

    public void setRenderedPAPP(boolean renderedPAPP) {
        this.renderedPAPP = renderedPAPP;
    }

    public boolean isRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }
//</editor-fold>

}
