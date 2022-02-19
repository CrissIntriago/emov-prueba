/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.ReformaReduccion;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.ReformaCupoReduccionService;
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
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
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
 * @author Luis Suarez
 */
@Named(value = "asignaPartDistAnexoReduccionView")
@ViewScoped
public class AsiganaPartidasDisAnexoReduccionController extends BpmnBaseRoot implements Serializable {

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
    private List<PresCatalogoPresupuestario> listaItem;
    @Inject
    private PresCatalogoPresupuestarioService itemService;
    private List<PresFuenteFinanciamiento> listaFuente;
    @Inject
    private PresFuenteFinanciamientoService fuenteService;

    private List<PresPlanProgramatico> listaEstructura;
    @Inject
    private PresPlanProgramaticoService estructuraService;
    //lista partida distributivo
    private PartidasDistributivoAnexo partidaDistributivoAnexo;
    @Inject
    private PartidaDistributivoAnexoService PartidaDisAnexoService;
    private List<ThCargoRubros> listaPartidasAnexo;//para mostrar
    private List<ThCargoRubros> listaPartidasdistributivo;//para mostrar
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
    @Inject
    private ReformaCupoReduccionService cupoReduccionService;
    private LazyModel<ReformaIngresoSuplemento> lazyReformas;
    private boolean panelReforma, panelData, columnaSuplemento, columnaReduccion;
    private ReformaIngresoSuplemento reformaGlobal;
    private BigDecimal totalCupoDA, totalCupoD;
    private BigDecimal cupoAsignado, cupoAsignadoD;
    private boolean panelData2, panelData3;
    private List<PartidasDistributivoAnexo> listaPartidasAnexoNuevo;
    private List<PartidasDistributivoAnexo> listaPartidasAnexoModificacion;
    private String observaciones;
    private Boolean enbaledReforma;
    @Inject
    private ClienteService clienteService;

    //NUEVO
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    private String nombreUsuario;

    private LazyModel<ThCargo> lazyRubros;
    private List<ThCargoRubros> lazyRubrosAnexo;
    private List<ThCargoRubros> listaVista;
    private ThCargo mostrarData;
    private ThCargo dataView;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                nombreUsuario = user.getNameUser();
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
                lazyReformas.getFilterss().put("tipo:equal", false);
                panelData = false;
                panelData2 = false;
                panelData3 = false;
                listaPartidasAnexoNuevo = new ArrayList<>();
                listaPartidasAnexoModificacion = new ArrayList<>();
                listaPartidasdistributivo = new ArrayList<>();
                enbaledReforma = true;
                listaPartidasAnexo = new ArrayList<>();

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reduccion(ThCargoRubros p) {
        if (p.getReformaReduccion().doubleValue() > p.getMonto().doubleValue()) {
            p.setReformaReduccion(BigDecimal.ZERO);
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaCodificado(p.getMonto());
            JsfUtil.addErrorMessage("Error", "El valor esa mayor al valor original");
            return;
        }
        p.setReformaSuplemento(BigDecimal.ZERO);
        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()).subtract(p.getReformaReduccion()));
        thCargoRubrosService.edit(p);
    }

    public void abriDlgoPartidasPresupuestariaDistributiov(ThCargo d) {
        this.listaPartidasdistributivo = new ArrayList<>();
        mostrarData = new ThCargo();
        mostrarData = d;
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaPartidasdistributivo = valoresService.listaPartidasDisReforma(d, BigInteger.valueOf(reformaGlobal.getId()));
        // setRmu(valoresService.getRMu(d, periodo));
        if (reformaGlobal.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
        }

        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivos').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivos");

    }

    public void listaVisualizacion(ThCargo d) {
        dataView = new ThCargo();
        dataView = d;
        listaVista = valoresService.listaPartidasDistriReforma(d, BigInteger.valueOf(reformaGlobal.getId()));
        //    setRmu(valoresService.getRMu(d, periodo));
        PrimeFaces.current().executeScript("PF('DlogopartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlogopartidasDistributivosvista");
    }

    public void showPanel1() {
        panelReforma = true;
        panelData = false;
        panelData2 = false;
        panelData3 = false;
        enbaledReforma = true;

    }

    public void loadingRubros(ThCargo th) {
        listaPartidasdistributivo = thCargoRubrosService.listaDistributivoReformando(th, BigInteger.valueOf(reformaGlobal.getId()));

    }

    public void cargarRubrosDistributivos() {

        lazyRubros = new LazyModel<>(ThCargo.class);
        lazyRubros.getFilterss().put("estado", true);
        lazyRubros.getFilterss().put("activo", true);
        lazyRubrosAnexo = valoresService.getDistributivoAnexoReforma(reformaGlobal.getPeriodo(), BigInteger.valueOf(reformaGlobal.getId()));
    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public void registrarDistributivoAnexo(ReformaIngresoSuplemento r) {
        reformaGlobal = new ReformaIngresoSuplemento();
        reformaGlobal = r;
        periodo = r.getPeriodo();
        cargarRubrosDistributivos();
        cargarDatosGenerarPartida();
        listaDistributivoAnexo = new ArrayList<>();

        List<ThCargoRubros> distributivos = thCargoRubrosService.copiaReforma(Boolean.TRUE, r.getPeriodo());
        List<ThCargoRubros> verificar = thCargoRubrosService.copiaVerificacion(BigInteger.valueOf(r.getId().longValue()));

        if (verificar.isEmpty()) {
            if (!distributivos.isEmpty()) {
                for (ThCargoRubros item : distributivos) {
                    ThCargoRubros data = new ThCargoRubros();
                    data = Utils.clone(item);
                    data.setId(null);
                    data.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                    data.setCodigoReforma(BigInteger.valueOf(r.getId()));
                    data.setMonto(item.getReformaCodificado());
                    data.setReformaCodificado(data.getMonto());
                    data.setReformaSuplemento(BigDecimal.ZERO);
                    data.setReformaReduccion(BigDecimal.ZERO);
                    data.setFechaCreacion(new Date());
                    data.setFechaModificacion(new Date());
                    data.setUsuarioCreacion(nombreUsuario);
                    data.setUsuarioModificacion(nombreUsuario);
                    thCargoRubrosService.create(data);
                    data = new ThCargoRubros();

                }
            }
        }

        if (r.getTipo()) {
            columnaSuplemento = true;
            columnaReduccion = false;
            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");

        } else {
            columnaSuplemento = false;
            columnaReduccion = true;
            totalCupoDA = BigDecimal.ZERO;
            cupoAsignado = BigDecimal.ZERO;
        }
        totalCupoReduccion();

        panelReforma = false;
        panelData = true;
        panelData2 = false;
        panelData3 = false;
        enbaledReforma = false;

    }

    public void cargarRubros(ThCargo cargo, boolean origen) {

        if (origen) {
            listaPartidasAnexo = new ArrayList<>();
            listaPartidasAnexo = thCargoRubrosService.listaDistributivos(BigInteger.valueOf(reformaGlobal.getId()), cargo);

        } else {
            listaPartidasAnexo = new ArrayList<>();
            listaPartidasAnexo = thCargoRubrosService.listaDistributivosQnexo(BigInteger.valueOf(reformaGlobal.getId()));
        }

    }

    public void totalCupoReduccion() {

        CupoReduccion c = cupoReduccionService.getValoresCupo("DA", reformaGlobal);
        CupoReduccion cd = cupoReduccionService.getValoresCupo("D", reformaGlobal);
        totalCupoDA = BigDecimal.ZERO;
        totalCupoDA = c.getMotoDisponible();
        cupoAsignado = BigDecimal.ZERO;
        cupoAsignado = cupoReduccionService.retornaValorReducido(BigInteger.valueOf(reformaGlobal.getId()), "PDA");
        totalCupoD = BigDecimal.ZERO;
        totalCupoD = cd.getMotoDisponible();
        cupoAsignadoD = BigDecimal.ZERO;
        cupoAsignadoD = cupoReduccionService.retornaValorReducido(BigInteger.valueOf(reformaGlobal.getId()), "PD");
        System.out.println("reformaGlobal " + reformaGlobal.getId());
        System.out.println("totalCupoDA " + totalCupoDA);
        System.out.println("cupoAsignado " + cupoAsignado);
        System.out.println("totalCupoD " + totalCupoD);
        System.out.println("cupoAsignadoD " + cupoAsignadoD);
    }

    public BigDecimal mostrarValorPrincipal(ReformaIngresoSuplemento r) {
        CupoReduccion c = cupoReduccionService.getValoresCupo("DA", r);
        CupoReduccion cd = cupoReduccionService.getValoresCupo("D", r);
        return c.getMotoDisponible().add(cd.getMotoDisponible());
    }

    public BigDecimal revisarReserva(PartidasDistributivoAnexo v) {

        //CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
        return valoresService.verReserva(v.getPartidaAp(), reformaGlobal.getPeriodo());
    }

    public void cargarDatosGenerarPartida() {
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaItem = itemService.findTipoPresupuesto(false);
        this.listaEstructura = estructuraService.getEstructuraProgramatica("programatico_subprograma");;
        this.listaFuente = fuenteService.getFuenteFinanciamiento();

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

        }

        p.setReformaCodificado(p.getMonto().add(p.getReformaSuplemento()));

        PartidaDisAnexoService.edit(p);

    }

    public void cellEditReduccion(ThCargoRubros p) {
        BigDecimal resultado = BigDecimal.ZERO;
        resultado = p.getMonto().subtract(p.getReserva());
        ThCargoRubros valorActual = PartidaDisAnexoService.getValorActual(p);

        if (p.getReformaReduccion().doubleValue() > resultado.doubleValue()) {
            p.setReformaReduccion(valorActual.getReformaReduccion());
            p.setReformaSuplemento(BigDecimal.ZERO);
            p.setReformaCodificado(valorActual.getReformaCodificado());
            thCargoRubrosService.edit(p);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "El valor de reduccion es mayor al monto normal");
            return;
        }

        p.setReformaSuplemento(BigDecimal.ZERO);
        p.setReformaCodificado(p.getMonto().subtract(p.getReformaReduccion()));
        thCargoRubrosService.edit(p);
        totalCupoReduccion();
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("", "Transacción Exitosa");

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

        try {
            if (pa.getFuenteApA() != null) {
                pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
            } else {
                pa.setFuenteDirectaA(null);
            }

            if (pa.getItemApA() != null && pa.getEstructuraApA() != null && pa.getFuenteApA() != null) {
                pa.setFuenteDirectaA(pa.getFuenteApA().getTipoFuente());
                pa.setPartidaAp(pa.getEstructuraApA().getCodigo() + pa.getItemApA().getCodigo() + pa.getFuenteDirectaA().getOrden());

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
            observacion.setObservacion(observaciones.toUpperCase());
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

    public Boolean getEnbaledReforma() {
        return enbaledReforma;
    }

    public void setEnbaledReforma(Boolean enbaledReforma) {
        this.enbaledReforma = enbaledReforma;
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

    public List<PresPlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }

    public void setListaEstructura(List<PresPlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
    }

    public LazyModel<ThCargo> getLazyRubros() {
        return lazyRubros;
    }

    public void setLazyRubros(LazyModel<ThCargo> lazyRubros) {
        this.lazyRubros = lazyRubros;
    }

    public List<ThCargoRubros> getListaPartidasAnexo() {
        return listaPartidasAnexo;
    }

    public void setListaPartidasAnexo(List<ThCargoRubros> listaPartidasAnexo) {
        this.listaPartidasAnexo = listaPartidasAnexo;
    }
//</editor-fold>

    public List<ThCargoRubros> getLazyRubrosAnexo() {
        return lazyRubrosAnexo;
    }

    public void setLazyRubrosAnexo(List<ThCargoRubros> lazyRubrosAnexo) {
        this.lazyRubrosAnexo = lazyRubrosAnexo;
    }

    public BigDecimal getTotalCupoD() {
        return totalCupoD;
    }

    public void setTotalCupoD(BigDecimal totalCupoD) {
        this.totalCupoD = totalCupoD;
    }

    public BigDecimal getCupoAsignadoD() {
        return cupoAsignadoD;
    }

    public void setCupoAsignadoD(BigDecimal cupoAsignadoD) {
        this.cupoAsignadoD = cupoAsignadoD;
    }

    public MasterCatalogoService getMasterService() {
        return masterService;
    }

    public void setMasterService(MasterCatalogoService masterService) {
        this.masterService = masterService;
    }

    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

    public DistributivoAnexoService getDistributivoAnexoService() {
        return distributivoAnexoService;
    }

    public void setDistributivoAnexoService(DistributivoAnexoService distributivoAnexoService) {
        this.distributivoAnexoService = distributivoAnexoService;
    }

    public PresCatalogoPresupuestarioService getItemService() {
        return itemService;
    }

    public void setItemService(PresCatalogoPresupuestarioService itemService) {
        this.itemService = itemService;
    }

    public PresFuenteFinanciamientoService getFuenteService() {
        return fuenteService;
    }

    public void setFuenteService(PresFuenteFinanciamientoService fuenteService) {
        this.fuenteService = fuenteService;
    }

    public PresPlanProgramaticoService getEstructuraService() {
        return estructuraService;
    }

    public void setEstructuraService(PresPlanProgramaticoService estructuraService) {
        this.estructuraService = estructuraService;
    }

    public PartidaDistributivoAnexoService getPartidaDisAnexoService() {
        return PartidaDisAnexoService;
    }

    public void setPartidaDisAnexoService(PartidaDistributivoAnexoService PartidaDisAnexoService) {
        this.PartidaDisAnexoService = PartidaDisAnexoService;
    }

    public ReformaSuplementoIngresoService getSuplementoIngresoService() {
        return suplementoIngresoService;
    }

    public void setSuplementoIngresoService(ReformaSuplementoIngresoService suplementoIngresoService) {
        this.suplementoIngresoService = suplementoIngresoService;
    }

    public ProformaPresupuestoPlanificadoService getProformaPresupuestoPlanificadoService() {
        return proformaPresupuestoPlanificadoService;
    }

    public void setProformaPresupuestoPlanificadoService(ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService) {
        this.proformaPresupuestoPlanificadoService = proformaPresupuestoPlanificadoService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public ValoresDistributivoService getValoresService() {
        return valoresService;
    }

    public void setValoresService(ValoresDistributivoService valoresService) {
        this.valoresService = valoresService;
    }

    public ReformaCupoReduccionService getCupoReduccionService() {
        return cupoReduccionService;
    }

    public void setCupoReduccionService(ReformaCupoReduccionService cupoReduccionService) {
        this.cupoReduccionService = cupoReduccionService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public ThCargoRubrosService getThCargoRubrosService() {
        return thCargoRubrosService;
    }

    public void setThCargoRubrosService(ThCargoRubrosService thCargoRubrosService) {
        this.thCargoRubrosService = thCargoRubrosService;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public ThCargo getMostrarData() {
        return mostrarData;
    }

    public void setMostrarData(ThCargo mostrarData) {
        this.mostrarData = mostrarData;
    }

    public List<ThCargoRubros> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<ThCargoRubros> listaVista) {
        this.listaVista = listaVista;
    }

    public ThCargo getDataView() {
        return dataView;
    }

    public void setDataView(ThCargo dataView) {
        this.dataView = dataView;
    }

    public List<ThCargoRubros> getListaPartidasdistributivo() {
        return listaPartidasdistributivo;
    }

    public void setListaPartidasdistributivo(List<ThCargoRubros> listaPartidasdistributivo) {
        this.listaPartidasdistributivo = listaPartidasdistributivo;
    }

}
