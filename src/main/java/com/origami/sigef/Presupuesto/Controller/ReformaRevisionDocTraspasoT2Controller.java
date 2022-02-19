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
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
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
@Named(value = "revisionDocumentoTraspasoT2View")
@ViewScoped
public class ReformaRevisionDocTraspasoT2Controller extends BpmnBaseRoot implements Serializable {

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
    private String observaciones;
    private BigDecimal rmu;
    private boolean vistaPartidaDisGeneral;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;
    private double totalMontoReformado;
    private double totalTraspasoReduccionDGeneral;
    private double totalTraspasoIncrementoDGeneral;
    private double totalMontoReformadoDGeneral;
    private double totalTraspasoReduccionDAnexo;
    private double totalTraspasoIncrementoDAnexo;
    private double totalMontoReformadoDAnexo;
    private double totalTraspasoReduccionPDirecta;
    private double totalTraspasoIncrementoPDirecta;
    private double totalMontoReformadoPDirecta;

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
                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REGP-REF");
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("estadoReformaTramite:equal", estadoReforma);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                listaProducto = new ArrayList<>();
                listaProformaPresupuestoPlanificado = new ArrayList<>();
                listaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
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
    
    public void consultarReforma(ReformaTraspaso reforma) {
        listaProducto = new ArrayList<>();
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        listaDistributivo = new ArrayList<>();
        listaPartidaDistributivoAnexo = new ArrayList<>();
        reformaTraspaso = reforma;
        System.out.println("reforma: "+reformaTraspaso.toString());
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta(reformaTraspaso.getPeriodo(), true, BigInteger.valueOf(reformaTraspaso.getId()));
//        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
//        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
//        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        busqueda.setAnio(reforma.getPeriodo());
        renderedPAPP = !listaProducto.isEmpty();
        renderedDistributivo = !listaDistributivo.isEmpty();
        renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        if (!listaProducto.isEmpty()) {
            actualizarTotales();
        }
        if (!listaDistributivo.isEmpty()) {
            actualizarTotalesDistributivo();
        }
        PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
    }
    
//    public Double obtTotalIncRedPAPP (Boolean incremento) {
//        Double totalIncrementoPAPP = 0.00;
//        Double totalReduccionPAPP = 0.00;
//        if (!reformaTraspaso.getTipo()) {
//            listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
//            listaPartidaDistributivo = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
//            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
//            listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
//            if (!listaProducto.isEmpty()) {
//                for (Producto item : listaProducto) {
//                    if (item.getEstado()) {
//                        if (incremento) {
//                            totalIncrementoPAPP = totalIncrementoPAPP + item.getTraspasoIncremento().doubleValue();
//                        } else {
//                            totalReduccionPAPP = totalReduccionPAPP + item.getTraspasoReduccion().doubleValue();
//                        }
//                    }
//                }
//            }
//            if (!listaPartidaDistributivo.isEmpty()) {
//                for (PartidasDistributivo listaRubro : listaPartidaDistributivo) {
//                    totalIncrementoPAPP = totalIncrementoPAPP + listaRubro.getTraspasoIncremento().doubleValue();
//                    totalReduccionPAPP = totalReduccionPAPP + listaRubro.getTraspasoReduccion().doubleValue();
//                }
//            }
//            if (!listaPartidaDistributivoAnexo.isEmpty()) {
//                for (PartidasDistributivoAnexo listaAnexo : listaPartidaDistributivoAnexo) {
//                    totalIncrementoPAPP = totalIncrementoPAPP + listaAnexo.getTraspasoIncremento().doubleValue();
//                    totalReduccionPAPP = totalReduccionPAPP + listaAnexo.getTraspasoReduccion().doubleValue();
//                }
//            }
//            if (!listaPartidaDirecta.isEmpty()) {
//                for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirecta) {
//                    totalIncrementoPAPP = totalIncrementoPAPP + partidaDirecta.getTraspasoIncremento().doubleValue();
//                    totalReduccionPAPP = totalReduccionPAPP + partidaDirecta.getTraspasoReduccion().doubleValue();
//                }
//            }
//            if (incremento) {return totalIncrementoPAPP;} else{return totalReduccionPAPP;}
//        }
//        return 0.00;
//    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = reforma;
        if (reforma.getTipo()) {
            listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId())); //ENRIQUE
            //reformaTraspasoService.getListProductoByReforma(reforma.getPeriodo(), reforma.getUnidadRequiriente().getId(), "REGT", true, BigInteger.valueOf(reforma.getId()));
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
            listaProducto = obtProdUniRespRefor(reforma.getUnidadRequiriente().getId(), BigInteger.valueOf(reforma.getId())); //ENRIQUE
            //reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
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
            totalTraspasoIncremento = totalTraspasoIncrementoR;
            return totalTraspasoIncrementoR;
        } else {
            totalTraspasoReduccion = totalTraspasoReduccionR;
            return totalTraspasoReduccionR;
        }
    }

    public void actualizarTotales() {
        totalTraspasoIncremento = 0;
        totalTraspasoReduccion = 0;
        totalMontoReformado = 0;
        for (Producto item : listaProducto) {
            if (item.getEstado()) {
                totalTraspasoIncremento = totalTraspasoIncremento + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccion = totalTraspasoReduccion + item.getTraspasoReduccion().doubleValue();
                totalMontoReformado = totalMontoReformado + item.getMontoReformada().doubleValue();
            }
        }
    }

    public void actualizarTotalesDistributivo() {
        totalTraspasoReduccionDGeneral = 0;
        totalTraspasoIncrementoDGeneral = 0;
        totalMontoReformadoDGeneral = 0;
        listaPartidaDistributivo = detalleReformaTraspasoService.getListPartidaDistributivoReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (Utils.isNotEmpty(listaPartidaDistributivo)) {
            for (PartidasDistributivo item : listaPartidaDistributivo) {
                totalTraspasoIncrementoDGeneral = totalTraspasoIncrementoDGeneral + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionDGeneral = totalTraspasoReduccionDGeneral + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoDGeneral = totalMontoReformadoDGeneral + item.getReformaCodificado().doubleValue();
            }
        }
    }

    public void actualizarTotalesDistributivoAnexo() {
        totalTraspasoReduccionDAnexo = 0;
        totalTraspasoIncrementoDAnexo = 0;
        totalMontoReformadoDAnexo = 0;
        if (Utils.isNotEmpty(listaPartidaDistributivoAnexo)) {
            for (PartidasDistributivoAnexo item : listaPartidaDistributivoAnexo) {
                totalTraspasoIncrementoDAnexo = totalTraspasoIncrementoDAnexo + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionDAnexo = totalTraspasoReduccionDAnexo + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoDAnexo = totalMontoReformadoDAnexo + item.getReformaCodificado().doubleValue();
            }
        }
    }

    public void actualizarTotalesPartidaDirecta() {
        totalTraspasoIncrementoPDirecta = 0;
        totalTraspasoReduccionPDirecta = 0;
        totalMontoReformadoPDirecta = 0;
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (Utils.isNotEmpty(listaProformaPresupuestoPlanificado)) {
            for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoPDirecta = totalMontoReformadoPDirecta + item.getReformaCodificado().doubleValue();
            }
        }
    }

    public void enviar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REV-REF");

        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " enviada con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
    }

    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
     //   setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        dataView = d;
        vistaPartidaDisGeneral = vista;
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void dlogoObservaciones(ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        if (busqueda.getAnio() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Eliga un Período");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            enviar(reformaTraspaso);
            observacion.setObservacion(observaciones);
            getParamts().put("userPresupuesto", clienteService.getrolsUser(RolUsuario.presupuesto));
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
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

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
    }

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
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

    public double getTotalMontoReformado() {
        return totalMontoReformado;
    }

    public void setTotalMontoReformado(double totalMontoReformado) {
        this.totalMontoReformado = totalMontoReformado;
    }

    public double getTotalTraspasoReduccionDGeneral() {
        return totalTraspasoReduccionDGeneral;
    }

    public void setTotalTraspasoReduccionDGeneral(double totalTraspasoReduccionDGeneral) {
        this.totalTraspasoReduccionDGeneral = totalTraspasoReduccionDGeneral;
    }

    public double getTotalTraspasoIncrementoDGeneral() {
        return totalTraspasoIncrementoDGeneral;
    }

    public void setTotalTraspasoIncrementoDGeneral(double totalTraspasoIncrementoDGeneral) {
        this.totalTraspasoIncrementoDGeneral = totalTraspasoIncrementoDGeneral;
    }

    public double getTotalMontoReformadoDGeneral() {
        return totalMontoReformadoDGeneral;
    }

    public void setTotalMontoReformadoDGeneral(double totalMontoReformadoDGeneral) {
        this.totalMontoReformadoDGeneral = totalMontoReformadoDGeneral;
    }

    public double getTotalTraspasoReduccionDAnexo() {
        return totalTraspasoReduccionDAnexo;
    }

    public void setTotalTraspasoReduccionDAnexo(double totalTraspasoReduccionDAnexo) {
        this.totalTraspasoReduccionDAnexo = totalTraspasoReduccionDAnexo;
    }

    public double getTotalTraspasoIncrementoDAnexo() {
        return totalTraspasoIncrementoDAnexo;
    }

    public void setTotalTraspasoIncrementoDAnexo(double totalTraspasoIncrementoDAnexo) {
        this.totalTraspasoIncrementoDAnexo = totalTraspasoIncrementoDAnexo;
    }

    public double getTotalMontoReformadoDAnexo() {
        return totalMontoReformadoDAnexo;
    }

    public void setTotalMontoReformadoDAnexo(double totalMontoReformadoDAnexo) {
        this.totalMontoReformadoDAnexo = totalMontoReformadoDAnexo;
    }

    public double getTotalTraspasoReduccionPDirecta() {
        return totalTraspasoReduccionPDirecta;
    }

    public void setTotalTraspasoReduccionPDirecta(double totalTraspasoReduccionPDirecta) {
        this.totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta;
    }

    public double getTotalTraspasoIncrementoPDirecta() {
        return totalTraspasoIncrementoPDirecta;
    }

    public void setTotalTraspasoIncrementoPDirecta(double totalTraspasoIncrementoPDirecta) {
        this.totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta;
    }

    public double getTotalMontoReformadoPDirecta() {
        return totalMontoReformadoPDirecta;
    }

    public void setTotalMontoReformadoPDirecta(double totalMontoReformadoPDirecta) {
        this.totalMontoReformadoPDirecta = totalMontoReformadoPDirecta;
    }
//</editor-fold>

}
