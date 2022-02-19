/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "distributivoAnexoReformaView")
@ViewScoped
public class AsignacionDsitributivoAnexoReformaController extends BpmnBaseRoot implements Serializable {

    //catalogo master y busqueda
    private OpcionBusqueda busqueda;
    private List<MasterCatalogo> listaPeriodos;
    @Inject
    private MasterCatalogoService masterService;
    @Inject
    private UserSession user;
    private Short periodo;
    //distributivo anexo
    private List<DistributivoAnexo> listaDistributivoAnexo;
    @Inject
    private DistributivoAnexoService distributivoAnexoService;
    //listas para sacar partida
    private List<CatalogoPresupuesto> listaItem;
    @Inject
    private CatalogoPresupuestoService itemService;
    private List<FuenteFinanciamiento> listaFuente;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private CuposService cupoService;
    private List<PlanProgramatico> listaEstructura;
    @Inject
    private PlanProgramaticoService estructuraService;
    //lista partida distributivo
    private PartidasDistributivoAnexo partidaDistributivoAnexo;
    @Inject
    private PartidaDistributivoAnexoService PartidaDisAnexoService;
    private List<PartidasDistributivoAnexo> listaPartidasAnexo;//para mostrar
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    //bloqueo
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    private boolean bloqueo;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ServletSession ss;
    private short anio;
    @Inject
    private ValoresDistributivoService valoresService;
    private LazyModel<ReformaIngresoSuplemento> lazyReformas;
    private boolean panelReforma, panelData, columnaSuplemento, columnaReduccion;
    private ReformaIngresoSuplemento reformaGlobal;
    private BigDecimal totalCupoDA;
    private BigDecimal cupoAsignado;
    private boolean panelData2, panelData3;
    private List<PartidasDistributivoAnexo> listaPartidasAnexoNuevo;
    private List<PartidasDistributivoAnexo> listaPartidasAnexoModificacion;
    private String observaciones;
    private boolean enabledReforma;
    @Inject
    private ClienteService clienteService;
    
    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                panelReforma = true;
                columnaSuplemento = true;
                columnaReduccion = false;
                busqueda = new OpcionBusqueda();
                listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
                this.listaDistributivoAnexo = new ArrayList<>();
                this.periodo = null;
                this.listaEstructura = new ArrayList<>();
                this.listaFuente = new ArrayList<>();
                this.listaItem = new ArrayList<>();
                partidaDistributivoAnexo = new PartidasDistributivoAnexo();
                this.bloqueo = true;
                lazyReformas = new LazyModel(ReformaIngresoSuplemento.class);
                lazyReformas.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                lazyReformas.getFilterss().put("tipo:equal", true);
                panelData = false;
                panelData2 = false;
                panelData3 = false;
                listaPartidasAnexoNuevo = new ArrayList<>();
                listaPartidasAnexoModificacion = new ArrayList<>();
                enabledReforma = true;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void showPanel1() {
        panelReforma = true;
        panelData = false;
        panelData2 = false;
        panelData3 = false;
        enabledReforma = true;
        
    }
    
    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }
    
    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }
    
//    public void registrarDistributivoAnexo(ReformaIngresoSuplemento r) {
//        reformaGlobal = new ReformaIngresoSuplemento();
//        reformaGlobal = r;
//        periodo = r.getPeriodo();
//        
//        cargarDatosGenerarPartida();
//        listaDistributivoAnexo = new ArrayList<>();
//        
//        List<PartidasDistributivoAnexo> listavVerificacion = PartidaDisAnexoService.getPartidasAnexoReformaVerificacion(BigInteger.valueOf(r.getId()));
//        if (listavVerificacion.isEmpty()) {
//            listaDistributivoAnexo = distributivoAnexoService.getDisAnexosNoExistInPartidaAnexo(periodo);
//            
//            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            if (!listaDistributivoAnexo.isEmpty()) {
//                for (DistributivoAnexo item : listaDistributivoAnexo) {
//                    partidaDistributivoAnexo = new PartidasDistributivoAnexo();
//                    partidaDistributivoAnexo.setDistributivoAnexo(item);
//                    partidaDistributivoAnexo.setEstado(Boolean.TRUE);
//                    partidaDistributivoAnexo.setPeriodo(periodo);
//                    partidaDistributivoAnexo.setUsuarioCreacion(user.getName());
//                    partidaDistributivoAnexo.setFechaCreacion(new Date());
//                    partidaDistributivoAnexo.setFechaModificacion(new Date());
//                    partidaDistributivoAnexo.setUsuarioModificacion(user.getName());
//                    partidaDistributivoAnexo.setEstadoPartida(estadoRegistarAprobado);
//                    partidaDistributivoAnexo.setReformaSuplemento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoIncremento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaCodificado(item.getMonto());
//                    partidaDistributivoAnexo.setMonto(BigDecimal.ZERO);
//                    partidaDistributivoAnexo = PartidaDisAnexoService.create(partidaDistributivoAnexo);
//                    
//                }
//            }
//            
//            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            List<PartidasDistributivoAnexo> listaPartidasReforma = PartidaDisAnexoService.getPartidasAnexo(estadoAprobado, periodo);
//            
//            if (!listaPartidasReforma.isEmpty()) {
//                PartidasDistributivoAnexo reformaPartida = new PartidasDistributivoAnexo();
//                
//                CatalogoItem estadoAprobadoItem = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");
//                for (PartidasDistributivoAnexo item : listaPartidasReforma) {
//                    reformaPartida.setDistributivoAnexo(item.getDistributivoAnexo());
//                    reformaPartida.setEstado(Boolean.TRUE);
//                    reformaPartida.setPeriodo(item.getPeriodo());
//                    reformaPartida.setUsuarioCreacion(user.getName());
//                    reformaPartida.setFechaCreacion(item.getFechaCreacion());
//                    reformaPartida.setFechaCreacion(new Date());
//                    reformaPartida.setUsuarioModificacion(user.getName());
//                    reformaPartida.setEstadoPartida(estadoAprobadoItem);
//                    reformaPartida.setCodigoReforma(BigInteger.valueOf(r.getId()));
//                    reformaPartida.setCodigoReferencia(BigInteger.valueOf(item.getId()));
//                    reformaPartida.setItemApA(item.getItemApA());
//                    reformaPartida.setEstructuraApA(item.getEstructuraApA());
//                    reformaPartida.setFuenteApA(item.getFuenteApA());
//                    reformaPartida.setFuenteDirectaA(item.getFuenteDirectaA());
//                    reformaPartida.setPartidaAp(item.getPartidaAp());
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoIncremento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoReduccion(BigDecimal.ZERO);
//                    reformaPartida.setMonto(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//                    reformaPartida.setReformaCodificado(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//                    
//                    reformaPartida = PartidaDisAnexoService.create(reformaPartida);
//                    reformaPartida = new PartidasDistributivoAnexo();
//                    
//                }
//                
//            }
//            listaPartidasAnexo = new ArrayList<>();
//            listaPartidasAnexo = PartidaDisAnexoService.getDisAnexosEstadoFalse(periodo);
//            if (!listaPartidasAnexo.isEmpty()) {
//                for (PartidasDistributivoAnexo item : listaPartidasAnexo) {
//                    
//                    PartidaDisAnexoService.remove(item);
//                }
//            }
//            
//        }
//        
//        listaPartidasAnexo = PartidaDisAnexoService.getPartidasAnexoReforma(BigInteger.valueOf(r.getId()));
//        
//        if (r.getTipo()) {
//            columnaSuplemento = true;
//            columnaReduccion = false;
//            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
//            totalCupo();
//            totalCupoAsignado();
//        } else {
//            columnaSuplemento = false;
//            columnaReduccion = true;
//            totalCupoDA = BigDecimal.ZERO;
//            cupoAsignado = BigDecimal.ZERO;
//        }
//        
//        panelReforma = false;
//        panelData = true;
//        panelData2 = false;
//        panelData3 = false;
//        enabledReforma = false;
//        
//    }
    
//    public void registrarDistributivoAnexoNuevos(ReformaIngresoSuplemento r) {
//        reformaGlobal = new ReformaIngresoSuplemento();
//        reformaGlobal = r;
//        periodo = r.getPeriodo();
//        
//        cargarDatosGenerarPartida();
//        listaDistributivoAnexo = new ArrayList<>();
//        
//        List<PartidasDistributivoAnexo> listavVerificacion = PartidaDisAnexoService.getPartidasAnexoReformaVerificacion(BigInteger.valueOf(r.getId()));
//        if (listavVerificacion.size() < 1) {
//            listaDistributivoAnexo = distributivoAnexoService.getDisAnexosNoExistInPartidaAnexo(periodo);
//            
//            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            if (!listaDistributivoAnexo.isEmpty()) {
//                for (DistributivoAnexo item : listaDistributivoAnexo) {
//                    partidaDistributivoAnexo = new PartidasDistributivoAnexo();
//                    partidaDistributivoAnexo.setDistributivoAnexo(item);
//                    partidaDistributivoAnexo.setEstado(Boolean.TRUE);
//                    partidaDistributivoAnexo.setPeriodo(periodo);
//                    partidaDistributivoAnexo.setUsuarioCreacion(user.getName());
//                    partidaDistributivoAnexo.setFechaCreacion(new Date());
//                    partidaDistributivoAnexo.setFechaModificacion(new Date());
//                    partidaDistributivoAnexo.setUsuarioModificacion(user.getName());
//                    partidaDistributivoAnexo.setEstadoPartida(estadoRegistarAprobado);
//                    partidaDistributivoAnexo.setReformaSuplemento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoIncremento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaCodificado(item.getMonto());
//                    partidaDistributivoAnexo.setMonto(BigDecimal.ZERO);
//                    partidaDistributivoAnexo = PartidaDisAnexoService.create(partidaDistributivoAnexo);
//                    
//                }
//            }
//            
//            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            List<PartidasDistributivoAnexo> listaPartidasReforma = PartidaDisAnexoService.getPartidasAnexo(estadoAprobado, periodo);
//            
//            if (!listaPartidasReforma.isEmpty()) {
//                PartidasDistributivoAnexo reformaPartida = new PartidasDistributivoAnexo();
//                
//                CatalogoItem estadoAprobadoItem = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");
//                for (PartidasDistributivoAnexo item : listaPartidasReforma) {
//                    reformaPartida.setDistributivoAnexo(item.getDistributivoAnexo());
//                    reformaPartida.setEstado(Boolean.TRUE);
//                    reformaPartida.setPeriodo(item.getPeriodo());
//                    reformaPartida.setUsuarioCreacion(user.getName());
//                    reformaPartida.setFechaCreacion(item.getFechaCreacion());
//                    reformaPartida.setFechaCreacion(new Date());
//                    reformaPartida.setUsuarioModificacion(user.getName());
//                    reformaPartida.setEstadoPartida(estadoAprobadoItem);
//                    reformaPartida.setCodigoReforma(BigInteger.valueOf(r.getId()));
//                    reformaPartida.setCodigoReferencia(BigInteger.valueOf(item.getId()));
//                    reformaPartida.setItemApA(item.getItemApA());
//                    reformaPartida.setEstructuraApA(item.getEstructuraApA());
//                    reformaPartida.setFuenteApA(item.getFuenteApA());
//                    reformaPartida.setFuenteDirectaA(item.getFuenteDirectaA());
//                    reformaPartida.setPartidaAp(item.getPartidaAp());
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoIncremento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoReduccion(BigDecimal.ZERO);
//                    reformaPartida.setMonto(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//                    reformaPartida.setReformaCodificado(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//                    
//                    reformaPartida = PartidaDisAnexoService.create(reformaPartida);
//                    reformaPartida = new PartidasDistributivoAnexo();
//                    
//                }
//                
//            }
//            listaPartidasAnexoNuevo = new ArrayList<>();
//            listaPartidasAnexoNuevo = PartidaDisAnexoService.getDisAnexosEstadoFalse(periodo);
//            if (!listaPartidasAnexo.isEmpty()) {
//                for (PartidasDistributivoAnexo item : listaPartidasAnexo) {
//                    
//                    PartidaDisAnexoService.remove(item);
//                }
//            }
//            
//        }
//        
//        listaPartidasAnexoNuevo = PartidaDisAnexoService.getPartidasAnexoReformaNuevo(BigInteger.valueOf(r.getId()));
//        
//        if (r.getTipo()) {
//            columnaSuplemento = true;
//            columnaReduccion = false;
//            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
//            totalCupoAsignado();
//        } else {
//            columnaSuplemento = false;
//            columnaReduccion = true;
//            totalCupoDA = BigDecimal.ZERO;
//            cupoAsignado = BigDecimal.ZERO;
//        }
//        
//        panelReforma = false;
//        panelData = false;
//        panelData2 = true;
//        panelData3 = false;
//        enabledReforma = false;
//    }
    
//    public void registrarDistributivoAnexoModificacion(ReformaIngresoSuplemento r) {
//        cargarDatosGenerarPartida();
//        reformaGlobal = new ReformaIngresoSuplemento();
//        reformaGlobal = r;
//        periodo = r.getPeriodo();
//        
//        listaDistributivoAnexo = new ArrayList<>();
//        
//        List<PartidasDistributivoAnexo> listavVerificacion = PartidaDisAnexoService.getPartidasAnexoReformaVerificacion(BigInteger.valueOf(r.getId()));
//        if (listavVerificacion.size() < 1) {
//            listaDistributivoAnexo = distributivoAnexoService.getDisAnexosNoExistInPartidaAnexo(periodo);
//            
//            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            if (!listaDistributivoAnexo.isEmpty()) {
//                for (DistributivoAnexo item : listaDistributivoAnexo) {
//                    partidaDistributivoAnexo = new PartidasDistributivoAnexo();
//                    partidaDistributivoAnexo.setDistributivoAnexo(item);
//                    partidaDistributivoAnexo.setEstado(Boolean.TRUE);
//                    partidaDistributivoAnexo.setPeriodo(periodo);
//                    partidaDistributivoAnexo.setUsuarioCreacion(user.getName());
//                    partidaDistributivoAnexo.setFechaCreacion(new Date());
//                    partidaDistributivoAnexo.setFechaModificacion(new Date());
//                    partidaDistributivoAnexo.setUsuarioModificacion(user.getName());
//                    partidaDistributivoAnexo.setEstadoPartida(estadoRegistarAprobado);
//                    partidaDistributivoAnexo.setReformaSuplemento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoIncremento(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setTraspasoReduccion(BigDecimal.ZERO);
//                    partidaDistributivoAnexo.setReformaCodificado(item.getMonto());
//                    partidaDistributivoAnexo.setMonto(BigDecimal.ZERO);
//                    partidaDistributivoAnexo = PartidaDisAnexoService.create(partidaDistributivoAnexo);
//                    
//                }
//            }
//            
//            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
//            List<PartidasDistributivoAnexo> listaPartidasReforma = PartidaDisAnexoService.getPartidasAnexo(estadoAprobado, periodo);
//            
//            if (!listaPartidasReforma.isEmpty()) {
//                PartidasDistributivoAnexo reformaPartida = new PartidasDistributivoAnexo();
//                
//                CatalogoItem estadoAprobadoItem = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RRD");
//                for (PartidasDistributivoAnexo item : listaPartidasReforma) {
//                    reformaPartida.setDistributivoAnexo(item.getDistributivoAnexo());
//                    reformaPartida.setEstado(Boolean.TRUE);
//                    reformaPartida.setPeriodo(item.getPeriodo());
//                    reformaPartida.setUsuarioCreacion(user.getName());
//                    reformaPartida.setFechaCreacion(item.getFechaCreacion());
//                    reformaPartida.setFechaCreacion(new Date());
//                    reformaPartida.setUsuarioModificacion(user.getName());
//                    reformaPartida.setEstadoPartida(estadoAprobadoItem);
//                    reformaPartida.setCodigoReforma(BigInteger.valueOf(r.getId()));
//                    reformaPartida.setCodigoReferencia(BigInteger.valueOf(item.getId()));
//                    reformaPartida.setItemApA(item.getItemApA());
//                    reformaPartida.setEstructuraApA(item.getEstructuraApA());
//                    reformaPartida.setFuenteApA(item.getFuenteApA());
//                    reformaPartida.setFuenteDirectaA(item.getFuenteDirectaA());
//                    reformaPartida.setPartidaAp(item.getPartidaAp());
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoIncremento(BigDecimal.ZERO);
//                    reformaPartida.setTraspasoReduccion(BigDecimal.ZERO);
//                    reformaPartida.setMonto(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//                    reformaPartida.setReformaCodificado(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento()).subtract(item.getReformaReduccion()));
//                    
//                    reformaPartida = PartidaDisAnexoService.create(reformaPartida);
//                    reformaPartida = new PartidasDistributivoAnexo();
//                    
//                }
//                
//            }
//            listaPartidasAnexoModificacion = new ArrayList<>();
//            listaPartidasAnexoModificacion = PartidaDisAnexoService.getDisAnexosEstadoFalse(periodo);
//            if (!listaPartidasAnexo.isEmpty()) {
//                for (PartidasDistributivoAnexo item : listaPartidasAnexo) {
//                    
//                    PartidaDisAnexoService.remove(item);
//                }
//            }
//            
//        }
//        
//        listaPartidasAnexoModificacion = PartidaDisAnexoService.getPartidasAnexoReformaModificacion(BigInteger.valueOf(r.getId()));
//        
//        if (r.getTipo()) {
//            columnaSuplemento = true;
//            columnaReduccion = false;
//            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
//            totalCupoAsignado();
//        } else {
//            columnaSuplemento = false;
//            columnaReduccion = true;
//            totalCupoDA = BigDecimal.ZERO;
//            cupoAsignado = BigDecimal.ZERO;
//        }
//        
//        panelReforma = false;
//        panelData = false;
//        panelData2 = false;
//        panelData3 = true;
//        enabledReforma = false;
//    }
    
    public BigDecimal totalCupo() {
        
        Cupos c = cupoService.getCodigoCupo(reformaGlobal, "DA");
        totalCupoDA = c.getMontoCupo();
        return c.getMontoCupo();
    }
    
    public BigDecimal totalCupo(ReformaIngresoSuplemento r) {
        
        Cupos c = cupoService.getCodigoCupo(r, "DA");
        totalCupoDA = c.getMontoCupo();
        return c.getMontoCupo();
    }
    
    public BigDecimal totalCupoAsignado() {
        
        return distributivoAnexoService.valorCupoAsignado(BigInteger.valueOf(reformaGlobal.getId()));
    }
    
    public BigDecimal revisarReserva(PartidasDistributivoAnexo v) {

        //CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
        return valoresService.verReserva(v.getPartidaAp(), reformaGlobal.getPeriodo());
    }
    
    public void cargarDatosGenerarPartida() {
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), periodo);
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), periodo);
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
        
    }
    
    public void cellEditSuplemento(PartidasDistributivoAnexo p) {
        PartidasDistributivoAnexo valor = distributivoAnexoService.valorActual(p);
        if (distributivoAnexoService.sumaVerificacion(BigInteger.valueOf(reformaGlobal.getId())).subtract(valor.getReformaSuplemento()).add(p.getReformaSuplemento()).doubleValue() > totalCupoDA.doubleValue()) {
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaReduccion(BigDecimal.ZERO);
            p.setReformaCodificado(p.getMonto());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Sobrepasa el cupo asignado");
            return;
        }
        
        if (p.getMonto().doubleValue() == 0) {
            p.setReformaSuplemento(valor.getReformaSuplemento());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No puede aumentar a un registro que es nuevo");
            return;
        }
        if (reformaGlobal.getTipo()) {
            totalCupoAsignado();
        }
        
        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()));
        
        PartidaDisAnexoService.edit(p);
        
    }
    
    public void cellEditReduccion(PartidasDistributivoAnexo p) {
        if (p.getReformaReduccion().doubleValue() > p.getMonto().doubleValue()) {
            p.setReformaReduccion(BigDecimal.ZERO);
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaCodificado(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "El valor de reduccion es mayor al monto normal");
            return;
        }
        
        p.setReformaSuplemento(BigDecimal.ZERO);
        p.setReformaCodificado(p.getMonto().subtract(p.getReformaReduccion()));
        PartidaDisAnexoService.edit(p);
        cupoAsignado = BigDecimal.ZERO;
        
    }
    
    public void editPartidar(PartidasDistributivoAnexo pa) {
        if (pa.getItemApA() != null && pa.getEstructuraApA() != null && pa.getFuenteApA() != null) {
            pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
            pa.setPartidaAp(pa.getEstructuraApA().getCodigo() + pa.getItemApA().getCodigo() + pa.getFuenteDirectaA().getOrden());
        } else {
            
            if (pa.getFuenteApA() == null) {
                pa.setFuenteDirectaA(null);
            }
            
            pa.setPartidaAp(null);
            
        }
        PartidaDisAnexoService.edit(pa);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Partida asiganada correctamente");
        
    }
    
    public void EditCellAndCalculatePartida(PartidasDistributivoAnexo pa) {
        
        BigDecimal valorActual = distributivoAnexoService.valorActualSuplemeto(pa);
        
        if (distributivoAnexoService.sumaVerificacion(BigInteger.valueOf(reformaGlobal.getId())).add(pa.getMonto()).subtract(valorActual).doubleValue() > totalCupoDA.doubleValue()) {
            System.out.println(distributivoAnexoService.sumaVerificacion(BigInteger.valueOf(reformaGlobal.getId())).add(pa.getMonto()).subtract(valorActual) + "\t\t" + totalCupoDA);
            pa.setItemApA(null);
            pa.setEstructuraApA(null);
            pa.setFuenteApA(null);
            pa.setFuenteDirectaA(null);
            pa.setPartidaAp(null);
            pa.setReformaSuplemento(BigDecimal.ZERO);
            pa.setReformaReduccion(BigDecimal.ZERO);
            PartidaDisAnexoService.edit(pa);
        //    listaPartidasAnexoNuevo = PartidaDisAnexoService.getPartidasAnexoReformaNuevo(BigInteger.valueOf(reformaGlobal.getId()));
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Sobrepasa El cupo asignado");
            return;
        }
        
        try {
            
            if (pa.getFuenteApA() != null) {
                pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
            } else {
                pa.setFuenteDirectaA(null);
            }
            
            if (pa.getItemApA() != null && pa.getEstructuraApA() != null && pa.getFuenteApA() != null) {
                pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
                pa.setPartidaAp(pa.getEstructuraApA().getCodigo() + pa.getItemApA().getCodigo() + pa.getFuenteDirectaA().getOrden());
                
                if (PartidaDisAnexoService.validateIfExistPartidaInPeriodoReforma(pa.getPartidaAp(), periodo, BigInteger.valueOf(reformaGlobal.getId()))) {
                    pa.setFuenteDirectaA(null);
                    pa.setPartidaAp(null);
                    pa.setItemApA(null);
                    pa.setEstructuraApA(null);
                    pa.setFuenteApA(null);
                    // if (valorAsignado.add(pa.getMonto()).subtract(valorActual).doubleValue() > totalCupo.doubleValue()) {
                    PartidaDisAnexoService.edit(pa);
                    JsfUtil.addWarningMessage("Advertencia", "La partida no puede ser igual a una partida ya ingresada en este periodo");
                    return;
                    
                }
                
                BigDecimal confirmar = distributivoAnexoService.sumaVerificacion(BigInteger.valueOf(reformaGlobal.getId())).add(pa.getReformaSuplemento()).subtract(valorActual);
                
                if (confirmar.doubleValue() > totalCupoDA.doubleValue()) {
                    
                    pa.setReformaSuplemento(BigDecimal.ZERO);
                    pa.setReformaReduccion(BigDecimal.ZERO);
                    pa.setReformaCodificado(BigDecimal.ZERO);
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Error", "Sobrepasa El cupo asignado");
                    
                } else {
                    
                    if (reformaGlobal.getTipo()) {
                        if (pa.getReformaCodificado().doubleValue() > 0) {
                            if (pa.getMonto().doubleValue() == 0) {
                                
                                pa.setReformaReduccion(BigDecimal.ZERO);
                                pa.setReformaCodificado(pa.getMonto().add(pa.getReformaSuplemento()));
                                
                            }
                        } else {
                            pa.setReformaSuplemento(pa.getMonto());
                            pa.setReformaReduccion(BigDecimal.ZERO);
                            pa.setReformaCodificado(pa.getMonto());
                            pa.setMonto(BigDecimal.ZERO);
                            
                        }
                        
                    } else {
                        pa.setReformaSuplemento(BigDecimal.ZERO);
                        pa.setReformaReduccion(pa.getMonto());
                        pa.setReformaCodificado(pa.getMonto());
                        pa.setMonto(BigDecimal.ZERO);
                        
                    }
                    
                }
                
            } else {
                
                if (pa.getFuenteApA() == null) {
                    pa.setFuenteDirectaA(null);
                    
                }
                
                pa.setPartidaAp(null);
                
                pa.setReformaCodificado(BigDecimal.ZERO);
                
            }
            PartidaDisAnexoService.edit(pa);
            totalCupoAsignado();
           // listaPartidasAnexoNuevo = PartidaDisAnexoService.getPartidasAnexoReformaNuevo(BigInteger.valueOf(reformaGlobal.getId()));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void opendlgPrint() {
        PrimeFaces.current().executeScript("PF('dlgPrint').show()");
    }
    
    public void printReport() {
        if (anio == 0) {
            JsfUtil.addWarningMessage("Información", " Ingrese un año para generar Reporte.");
            return;
        }
        boolean blockPrint = false;
        blockPrint = proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(anio, false, true);
        ss.addParametro("anio", anio);
        ss.addParametro("bloqueo", blockPrint);
        ss.setNombreReporte("PartidaDistributivoAnexo");
        ss.setNombreSubCarpeta("Distributivos");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        PrimeFaces.current().executeScript("PF('dlgPrint').hide()");
        
    }
    
    public void abriDlogo(ReformaIngresoSuplemento r) {
        reformaGlobal = new ReformaIngresoSuplemento();
        reformaGlobal = r;
        
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
        
    }
    
    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto));
            if (saveTramite() == null) {
                return;
            }
            
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaGlobal = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get And Set">
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public boolean isEnabledReforma() {
        return enabledReforma;
    }
    
    public void setEnabledReforma(boolean enabledReforma) {
        this.enabledReforma = enabledReforma;
    }
    
    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }
    
    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }
    
    public LazyModel<ReformaIngresoSuplemento> getLazyReformas() {
        return lazyReformas;
    }
    
    public void setLazyReformas(LazyModel<ReformaIngresoSuplemento> lazyReformas) {
        this.lazyReformas = lazyReformas;
    }
    
    public ReformaIngresoSuplemento getReformaGlobal() {
        return reformaGlobal;
    }
    
    public void setReformaGlobal(ReformaIngresoSuplemento reformaGlobal) {
        this.reformaGlobal = reformaGlobal;
    }
    
    public boolean isPanelData2() {
        return panelData2;
    }
    
    public void setPanelData2(boolean panelData2) {
        this.panelData2 = panelData2;
    }
    
    public boolean isPanelData3() {
        return panelData3;
    }
    
    public void setPanelData3(boolean panelData3) {
        this.panelData3 = panelData3;
    }
    
    public List<PartidasDistributivoAnexo> getListaPartidasAnexoNuevo() {
        return listaPartidasAnexoNuevo;
    }
    
    public void setListaPartidasAnexoNuevo(List<PartidasDistributivoAnexo> listaPartidasAnexoNuevo) {
        this.listaPartidasAnexoNuevo = listaPartidasAnexoNuevo;
    }
    
    public List<PartidasDistributivoAnexo> getListaPartidasAnexoModificacion() {
        return listaPartidasAnexoModificacion;
    }
    
    public void setListaPartidasAnexoModificacion(List<PartidasDistributivoAnexo> listaPartidasAnexoModificacion) {
        this.listaPartidasAnexoModificacion = listaPartidasAnexoModificacion;
    }
    
    public boolean isBloqueo() {
        return bloqueo;
    }
    
    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }
    
    public short getAnio() {
        return anio;
    }
    
    public void setAnio(short anio) {
        this.anio = anio;
    }
    
    public PartidasDistributivoAnexo getPartidaDistributivoAnexo() {
        return partidaDistributivoAnexo;
    }
    
    public void setPartidaDistributivoAnexo(PartidasDistributivoAnexo partidaDistributivoAnexo) {
        this.partidaDistributivoAnexo = partidaDistributivoAnexo;
    }
    
    public boolean isPanelReforma() {
        return panelReforma;
    }
    
    public void setPanelReforma(boolean panelReforma) {
        this.panelReforma = panelReforma;
    }
    
    public boolean isPanelData() {
        return panelData;
    }
    
    public void setPanelData(boolean panelData) {
        this.panelData = panelData;
    }
    
    public boolean isColumnaSuplemento() {
        return columnaSuplemento;
    }
    
    public void setColumnaSuplemento(boolean columnaSuplemento) {
        this.columnaSuplemento = columnaSuplemento;
    }
    
    public boolean isColumnaReduccion() {
        return columnaReduccion;
    }
    
    public void setColumnaReduccion(boolean columnaReduccion) {
        this.columnaReduccion = columnaReduccion;
    }
    
    public LazyModel<ReformaIngresoSuplemento> getLazyReforma() {
        return lazyReformas;
    }
    
    public void setLazyReforma(LazyModel<ReformaIngresoSuplemento> lazyReformas) {
        this.lazyReformas = lazyReformas;
    }
    
    public BigDecimal getTotalCupoDA() {
        return totalCupoDA;
    }
    
    public void setTotalCupoDA(BigDecimal totalCupoDA) {
        this.totalCupoDA = totalCupoDA;
    }
    
    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }
    
    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }
    
    public List<MasterCatalogo> getListaPeriodos() {
        return listaPeriodos;
    }
    
    public void setListaPeriodos(List<MasterCatalogo> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }
    
    public List<DistributivoAnexo> getListaDistributivoAnexo() {
        return listaDistributivoAnexo;
    }
    
    public void setListaDistributivoAnexo(List<DistributivoAnexo> listaDistributivoAnexo) {
        this.listaDistributivoAnexo = listaDistributivoAnexo;
    }
    
    public Short getPeriodo() {
        return periodo;
    }
    
    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }
    
    public List<CatalogoPresupuesto> getListaItem() {
        return listaItem;
    }
    
    public void setListaItem(List<CatalogoPresupuesto> listaItem) {
        this.listaItem = listaItem;
    }
    
    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }
    
    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }
    
    public List<PlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }
    
    public void setListaEstructura(List<PlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
    }
    
    public List<PartidasDistributivoAnexo> getListaPartidasAnexo() {
        return listaPartidasAnexo;
    }
    
    public void setListaPartidasAnexo(List<PartidasDistributivoAnexo> listaPartidasAnexo) {
        this.listaPartidasAnexo = listaPartidasAnexo;
    }
//</editor-fold>   

}
