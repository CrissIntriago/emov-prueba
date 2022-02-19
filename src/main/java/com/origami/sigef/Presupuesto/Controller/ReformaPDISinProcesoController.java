/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PresupuestoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

@Named(value = "reformaPDISinProcesoView")
@ViewScoped
public class ReformaPDISinProcesoController implements Serializable {

//<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @Inject
    private UserSession userSession;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ReformaTraspasoService reformaTraspasoService;
    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    @Inject
    private PresPlanProgramaticoService presPlanProgramaticoService;
    @Inject
    private PresCatalogoPresupuestarioService presCataPresupuestarioService;
    @Inject
    private PresFuenteFinanciamientoService presFuenteFinanService;
    @Inject
    private PresupuestoService presupuestoService;
    @Inject
    private ServletSession ss;
    @Inject
    private ValoresService valoresService;
//</editor-fold>

    private Boolean formBusqRegistrar, formBusqConsultar, bolPartidaDirecta;
    private Boolean renderBtnGenRefPAPP, renderBtnGenRefPDI, bolPapp;
    private Boolean editar, btnGenerarReforma, btnGenerarReformaPDI;
    private Boolean btnNuevoPartidaDirecta, btnTraspasos, btnGenerarPDI;
    private Boolean renderSeleccionReforma, disBtnGenReformaPDI, renderFielEncabezadoReforma;
    private Boolean renderFormularioInicial, renderRegresarInicio, disableGuardarReforma;
    private Long idUnidadA;
    private String tipoTraspaso, observaciones, referencia, motivacion, autorizado;
    private BigDecimal totalSaldoDisponible, totalTraspasoIncremento, totalTraspasoReduccion;
    private BigDecimal totalMontoReformado, totalTraspasoIncrementoPDirecta, totalTraspasoReduccionPDirecta;
    private BigDecimal totalMontoReformadoPDirecta;

    private List<UnidadAdministrativa> unidades;
    private List<Producto> listaProducto;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<ReformaTraspaso> reformas;
    private List<MasterCatalogo> listaAnio;
    private List<PresPlanProgramatico> listaPresPlanProgramatico;
    private List<PresCatalogoPresupuestario> listaPresCataPresupuestario;
    private List<PresFuenteFinanciamiento> listaPresFuenteFinanciamiento;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;

    private LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy;
    private LazyModel<ReformaTraspaso> reformasLazy;

    private ReformaTraspaso reformaTraspaso;
    private OpcionBusqueda busqueda;
    private ProformaPresupuestoPlanificado proformaPresupuestoPlanificado, partidasDirectas;

    @PostConstruct
    public void initView() {
        busqueda = new OpcionBusqueda();
        formBusqConsultar = Boolean.FALSE;
        formBusqRegistrar = Boolean.FALSE;
        btnNuevoPartidaDirecta = Boolean.FALSE;
        btnTraspasos = Boolean.FALSE;
        unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        idUnidadA = d.getUnidadAdministrativa().getId();
        tipoTraspaso = "PDI";
        editar = Boolean.TRUE;
        renderFormularioInicial = Boolean.TRUE;
        renderRegresarInicio = Boolean.FALSE;
        reformas = reformaTraspasoService.obtReformasByUsuario(userSession.getNameUser());
        bolPartidaDirecta = Boolean.FALSE;
        proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        if (reformas.size() > 1) {
            reformaTraspaso = reformas.get(reformas.size() - 1);
        } else if (reformas.size() == 1) {
            reformaTraspaso = reformas.get(0);
        }
        if (null != reformaTraspaso) {
            renderSeleccionReforma = Boolean.TRUE;
        }
        listaAnio = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        generarTipoTraspaso();
        filtroReformas(0);
    }

    public void nuevaReforma() {
        disBtnGenReformaPDI = Boolean.FALSE;
        disableGuardarReforma = Boolean.TRUE;
        seleccionarReforma(new ReformaTraspaso());
        JsfUtil.update("botonesReforma");
    }

    public void seleccionarReforma(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        renderFielEncabezadoReforma = Boolean.TRUE;
        renderFormularioInicial = Boolean.FALSE;
        renderRegresarInicio = Boolean.TRUE;
        btnTraspasos = Boolean.TRUE;
        if (reformaTraspaso.getId() != null) {
            bolPartidaDirecta = Boolean.TRUE;
            idUnidadA = reformaTraspaso.getUnidadRequiriente().getId();
            if (null != reformaTraspaso.getEstadoReforma()) {
                if (reformaTraspaso.getEstadoReforma().getCodigo().equals("APRT")) {
                    btnNuevoPartidaDirecta = Boolean.TRUE;
                    disableGuardarReforma = Boolean.TRUE;
                }
            }
        }
        cargarPartidas();
        actualizarTotalesPartidaDirecta();
        JsfUtil.update("formularioInicial");
        JsfUtil.update("formularioEncabezado");
        JsfUtil.update("formularioCuerpo");
        JsfUtil.update("botonesReforma");
    }

    public void regresarInicio() {
        renderFielEncabezadoReforma = Boolean.FALSE;
        renderFormularioInicial = Boolean.TRUE;
        renderRegresarInicio = Boolean.FALSE;
        bolPartidaDirecta = Boolean.FALSE;
        btnNuevoPartidaDirecta = Boolean.FALSE;
        btnTraspasos = Boolean.FALSE;
        JsfUtil.update("formularioInicial");
        JsfUtil.update("formularioEncabezado");
        JsfUtil.update("formularioCuerpo");
        JsfUtil.update("botonesReforma");
    }

    public void filtroReformas(int valor) {
        reformasLazy = new LazyModel<>(ReformaTraspaso.class);
        reformasLazy.getFilterss().put("usuarioCreacion", userSession.getNameUser());
        switch (valor) {
            case 1:
                CatalogoItem estadoReformaAprob = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
                reformasLazy.getFilterss().put("estadoReforma", estadoReformaAprob);
                break;
            case 2:
                CatalogoItem estadoReformReg = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
                reformasLazy.getFilterss().put("estadoReforma", estadoReformReg);
                break;
        }
    }

    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }

    public void generarTipoTraspaso() {
        switch (tipoTraspaso) {
            case "PAPP":
                bolPapp = Boolean.TRUE;
                renderBtnGenRefPAPP = Boolean.TRUE;
                renderBtnGenRefPDI = Boolean.FALSE;
                if (reformaTraspaso != null) {
                    listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
                    if (listaProducto.isEmpty()) {
                        btnGenerarReforma = Boolean.FALSE;
                    } else {
                        btnGenerarReforma = Boolean.TRUE;
                        actualizarTotales();
                    }
                }
                break;
            case "PDI":
                bolPapp = Boolean.FALSE;
                renderBtnGenRefPDI = Boolean.TRUE;
                renderBtnGenRefPAPP = Boolean.FALSE;
                if (reformaTraspaso != null) {
                    btnNuevoPartidaDirecta = Boolean.FALSE;
                    proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
                    proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
                    proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
                    listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                    if (listaProformaPresupuestoPlanificado.isEmpty()) {
                        disBtnGenReformaPDI = Boolean.FALSE;
                    } else {
                        btnGenerarReformaPDI = Boolean.TRUE;
                        disBtnGenReformaPDI = Boolean.TRUE;
                        actualizarTotalesPartidaDirecta();
                    }
                }
                break;
            default:
                break;
        }
        //btnTraspasos = Boolean.TRUE;
    }

    public void generarReforma() {
        if (idUnidadA == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Unidad Administrativa");
            return;
        }
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        reformaTraspaso.setNumeracion(reformaTraspasoService.maxNumeracion(Boolean.TRUE, busqueda.getAnio()));
        String codigo = Utils.completarCadenaConCeros(reformaTraspasoService.obtieneMaxNumeracionByUsuario(userSession.getNameUser()).toString(), 3);
        reformaTraspaso.setCodigo("T1-" + codigo + "-" + reformaTraspaso.getPeriodo());
        reformaTraspaso.setFechaTraspasoReforma(new Date());
        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        reformaTraspaso.setEstadoReforma(estadoReform);
        UnidadAdministrativa unidad = reformaTraspasoService.getUnidadById(idUnidadA);
        reformaTraspaso.setUnidadRequiriente(unidad);
        reformaTraspaso.setTipo(Boolean.TRUE);
        reformaTraspaso.setEstado(Boolean.TRUE);
        reformaTraspaso.setUsuarioCreacion(userSession.getNameUser());
        reformaTraspaso.setUsuarioModificacion(userSession.getNameUser());
        reformaTraspaso.setFechaCreacion(new Date());
        reformaTraspaso.setFechaModificacion(new Date());
        reformaTraspaso.setNumTramite(null);
        if (reformaTraspaso.getId() == null) {
            reformaTraspaso = reformaTraspasoService.create(reformaTraspaso);
        }
        disBtnGenReformaPDI = Boolean.TRUE;
        cargarCopiasPartidas();
        switch (tipoTraspaso) {
            case "PDI":
                bolPartidaDirecta = Boolean.TRUE;
                bolPapp = Boolean.FALSE;
                listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                if (!listaProformaPresupuestoPlanificado.isEmpty()) {
                    disBtnGenReformaPDI = Boolean.TRUE;
                } else {
                    crearPartidaDirecta(reformaTraspaso);
                    listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                    if (!listaProformaPresupuestoPlanificado.isEmpty()) {
                        disBtnGenReformaPDI = Boolean.TRUE;
                    } else {
                        disBtnGenReformaPDI = Boolean.FALSE;
                    }
                }
                actualizarTotalesPartidaDirecta();
                break;
        }
        disableGuardarReforma = Boolean.FALSE;
        JsfUtil.update("formularioEncabezado");
        JsfUtil.update("formularioCuerpo");
        JsfUtil.update("proformaEgresos");
        JsfUtil.update("botonesReforma");
    }

    public void cargarCopiasPartidas() {
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
    }

    public void actualizarTotales() {
        totalSaldoDisponible = BigDecimal.ZERO;
        totalTraspasoIncremento = BigDecimal.ZERO;
        totalTraspasoReduccion = BigDecimal.ZERO;
        totalMontoReformado = BigDecimal.ZERO;
        for (Producto item : listaProducto) {
            if (item.getEstado()) {
                totalSaldoDisponible.add(item.getSaldoDisponible());
                totalTraspasoIncremento.add(item.getTraspasoIncremento());
                totalTraspasoReduccion.add(item.getTraspasoReduccion());
                totalMontoReformado.add(item.getMontoReformada());
            }
        }
    }

    public void actualizarTotalesPartidaDirecta() {
        if (null != reformaTraspaso.getId()) {
            totalTraspasoIncrementoPDirecta = BigDecimal.ZERO;
            totalTraspasoReduccionPDirecta = BigDecimal.ZERO;
            totalMontoReformadoPDirecta = BigDecimal.ZERO;
            listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
            if (Utils.isNotEmpty(listaProformaPresupuestoPlanificado)) {
                for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                    totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta.add(item.getTraspasoIncremento());
                    totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta.add(item.getTraspasoReduccion());
                    totalMontoReformadoPDirecta = totalMontoReformadoPDirecta.add(item.getReformaCodificado());
                }
            }
        }
    }

    public Boolean disabledTipoTraspaso() {
        return clienteService.isUnidadAdministrativa("PRESUPUESTO", userSession.getNameUser());
    }

    public BigDecimal obtieneTotalComprometido(String partida) {
        BigDecimal resultado = reservaCompromisoService.obtTotalComprometido(partida);
        return resultado;
    }

    public BigDecimal obtieneTotalReservado(String partida) {
        BigDecimal resultado = reservaCompromisoService.obtTotalReservado(partida);
        return resultado;
    }

    public void calcularPartidaDirecta(ProformaPresupuestoPlanificado p, Boolean esIncremento) {
        BigDecimal montoReformado = p.getValor().add(p.getTraspasoIncremento()).subtract(p.getTraspasoReduccion());
        if (p.getTraspasoIncremento().doubleValue() > 0 && p.getTraspasoReduccion().doubleValue() > 0) {
            if (esIncremento) {
                p.setTraspasoIncremento(BigDecimal.ZERO);
            } else {
                p.setTraspasoReduccion(BigDecimal.ZERO);
            }
            proformaPresupuestoPlanificadoService.edit(p);
            actualizarTotalesPartidaDirecta();
            JsfUtil.update("proformaEgresos");
            JsfUtil.addWarningMessage("TRASPASO", "No se puede realizar un incremento y reducción de la misma partida a la vez.");
            return;
        }
        if (p.getTraspasoReduccion().doubleValue() > p.getValor().subtract(obtieneTotalComprometido(p.getPartidaPresupuestaria())).subtract(obtieneTotalReservado(p.getPartidaPresupuestaria())).doubleValue()) {
            p.setTraspasoReduccion(BigDecimal.ZERO);
            proformaPresupuestoPlanificadoService.edit(p);
            actualizarTotalesPartidaDirecta();
            JsfUtil.update("proformaEgresos");
            JsfUtil.addWarningMessage("TRASPASO", "La reducción no puede ser mayor al saldo disponible");
            return;
        }
        p.setReformaCodificado(montoReformado);
        proformaPresupuestoPlanificadoService.edit(p);
        actualizarTotalesPartidaDirecta();
        JsfUtil.update("formularioCuerpo");
        JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
    }

    public void crearPartidaDirecta(ReformaTraspaso reforma) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = reforma;
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        List<ProformaPresupuestoPlanificado> listVerificar = reformaTraspasoService.getListaVerificacion(BigInteger.valueOf(reformaTraspaso.getId()));
        if (listVerificar.isEmpty()) {
            listaProformaPresupuestoPlanificado = reformaTraspasoService.getPartidasDirectasReformas(reformaTraspaso.getPeriodo(), "PDI");
            ProformaPresupuestoPlanificado pro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                pro = new ProformaPresupuestoPlanificado();
                pro.setId(null);
                pro.setCodigo(item.getCodigo());
                pro.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                pro.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                pro.setCondicion(item.getCondicion());
                pro.setEstado(item.getEstado());
                pro.setEstructuraProgramatica(item.getEstructuraProgramatica());
                pro.setItemPresupuestario(item.getItemPresupuestario());
                pro.setItemNew(item.getItemNew());
                pro.setEstructruaNew(item.getEstructruaNew());
                pro.setFuenteNew(item.getFuenteNew());
                pro.setFechaCreacion(item.getFechaCreacion());
                pro.setFechaModificacion(item.getFechaModificacion());
                pro.setFuente(item.getFuente());
                pro.setFuenteDirecta(item.getFuenteDirecta());
                pro.setGenerado(item.getGenerado());
                pro.setCondicion(item.getCondicion());
                pro.setValor(item.getReformaCodificado());
                pro.setReformaSuplemento(BigDecimal.ZERO);
                pro.setReformaReduccion(BigDecimal.ZERO);
                pro.setTraspasoIncremento(BigDecimal.ZERO);
                pro.setTraspasoReduccion(BigDecimal.ZERO);
                pro.setUsuarioModificacion(userSession.getNameUser());
                pro.setPartidaPresupuestaria(item.getPartidaPresupuestaria());
                pro.setReformaCodificado(item.getValor().add(item.getTraspasoIncremento().add(item.getReformaSuplemento())).subtract(item.getTraspasoReduccion().add(item.getReformaReduccion())));
                pro.setPeriodo(reformaTraspaso.getPeriodo());
                pro.setUsuarioCreacion(item.getUsuarioCreacion());
                pro.setGenerado(item.getGenerado());
                proformaPresupuestoPlanificadoService.create(pro);
            }
        }
        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
        proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
    }

    public void abriDlgPartidas() {
        listaPresPlanProgramatico = presPlanProgramaticoService.getEstructurasSubProg();
        listaPresCataPresupuestario = presCataPresupuestarioService.getCatPresMovimiento();
        listaPresFuenteFinanciamiento = presFuenteFinanService.getFteFinanTrue();
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        JsfUtil.update("formPresegresoPartidas");
    }

    public void savePDIReforma() {
        if (proformaPresupuestoPlanificado.getEstructruaNew() == null || proformaPresupuestoPlanificado.getFuenteNew() == null || proformaPresupuestoPlanificado.getItemNew() == null) {
            JsfUtil.addErrorMessage("Error", "Los Campos no deben estar vacios");
            JsfUtil.update("formPresegresoPartidas");
            return;
        }
        if (proformaPresupuestoPlanificado.getId() == null) {
            proformaPresupuestoPlanificado.setPartidaPresupuestaria(proformaPresupuestoPlanificado.getEstructruaNew().getCodigo() + proformaPresupuestoPlanificado.getItemNew().getCodigo() + proformaPresupuestoPlanificado.getFuenteNew().getTipoFuente().getOrden());
            proformaPresupuestoPlanificado.setFuenteDirecta(proformaPresupuestoPlanificado.getFuenteNew().getTipoFuente());
            proformaPresupuestoPlanificado.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
            proformaPresupuestoPlanificado.setCondicion(false);
            proformaPresupuestoPlanificado.setGenerado(true);
            proformaPresupuestoPlanificado.setCodigo("PDI");
            proformaPresupuestoPlanificado.setEstado(true);
            proformaPresupuestoPlanificado.setValor(BigDecimal.ZERO);
            proformaPresupuestoPlanificado.setUsuarioCreacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setUsuarioModificacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setFechaCreacion(new Date());
            proformaPresupuestoPlanificado.setPeriodo(reformaTraspaso.getPeriodo());
            proformaPresupuestoPlanificado.setFechaModificacion(new Date());
            proformaPresupuestoPlanificado.setTraspasoIncremento(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado.setReformaCodificado(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado = proformaPresupuestoPlanificadoService.create(proformaPresupuestoPlanificado);
            proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        } else {
            proformaPresupuestoPlanificado.setPartidaPresupuestaria(proformaPresupuestoPlanificado.getEstructruaNew().getCodigo() + proformaPresupuestoPlanificado.getItemNew().getCodigo() + proformaPresupuestoPlanificado.getFuenteNew().getTipoFuente().getOrden());
            proformaPresupuestoPlanificado.setFuenteDirecta(proformaPresupuestoPlanificado.getFuente().getTipoFuente());
            proformaPresupuestoPlanificado.setUsuarioModificacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setFechaModificacion(new Date());
            proformaPresupuestoPlanificado.setTraspasoIncremento(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado.setReformaCodificado(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificadoService.edit(proformaPresupuestoPlanificado);
            proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        }
        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
        proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        JsfUtil.addInformationMessage("Exitoso", "El registro se realizo correctamente");
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').hide()");
        JsfUtil.update("formularioEncabezado");
        JsfUtil.update("formularioCuerpo");
        JsfUtil.update("proformaEgresos");
    }

    public void actualizarReformaTraspaso() {
        if (idUnidadA != null) {
            UnidadAdministrativa unidad = reformaTraspasoService.getUnidadById(idUnidadA);
            reformaTraspaso.setUnidadRequiriente(unidad);
            reformaTraspaso.setFechaModificacion(new Date());
            reformaTraspaso.setUsuarioModificacion(userSession.getNameUser());
            JsfUtil.addSuccessMessage("EXITO", "Reforma Actualizada correctamente");
            JsfUtil.update("formularioEncabezado");
            JsfUtil.update("formularioCuerpo");
            JsfUtil.update("proformaEgresos");
        } else {
            JsfUtil.addWarningMessage("ALERTA", "Debe seleccionar la Unidad Requiriente");
        }
    }

    public Boolean disableAprobarReforma(ReformaTraspaso reforma) {
        totalTraspasoIncrementoPDirecta = BigDecimal.ZERO;
        totalTraspasoReduccionPDirecta = BigDecimal.ZERO;
        totalMontoReformadoPDirecta = BigDecimal.ZERO;
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
        if (Utils.isNotEmpty(listaProformaPresupuestoPlanificado)) {
            for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta.add(item.getTraspasoIncremento());
                totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta.add(item.getTraspasoReduccion());
                totalMontoReformadoPDirecta = totalMontoReformadoPDirecta.add(item.getReformaCodificado());
            }
        }
        if (totalTraspasoIncrementoPDirecta.compareTo(totalTraspasoReduccionPDirecta) == 0
                && (totalTraspasoIncrementoPDirecta.compareTo(BigDecimal.ZERO) != 0 && totalTraspasoReduccionPDirecta.compareTo(BigDecimal.ZERO) != 0)
                && reforma.getEstadoReforma().getCodigo().equals("REGT")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void seleccionReformaAprobacion(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        PrimeFaces.current().executeScript("PF('dialogoObservacion').show()");
    }

    @Transactional
    public void aprobarReforma() {
        CatalogoItem estadoReformaCambio = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "APRT");
        reformaTraspaso.setEstadoReforma(estadoReformaCambio);
        reformaTraspaso.setEstadoReformaTramite(estadoReformaCambio);
        reformaTraspaso.setFechaAprobacion(new Date());
        reformaTraspaso.setValorInforme(observaciones);
        reformaTraspasoService.edit(reformaTraspaso);
        CatalogoItem estadodistri = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "ATD");
        listaPartidasDirectas = reformaTraspasoService.showPartidasDirectasNnuevo(BigInteger.valueOf(reformaTraspaso.getId()));
        if (!listaPartidasDirectas.isEmpty()) {
            for (ProformaPresupuestoPlanificado item : listaPartidasDirectas) {
                partidasDirectas = new ProformaPresupuestoPlanificado();
                if (item.getCodigoReferencia() != null) {
                    partidasDirectas = reformaTraspasoService.getPartidasDirectasNuevo(item.getCodigoReferencia());
                    if (null != partidasDirectas) {
                        partidasDirectas.setTraspasoIncremento(partidasDirectas.getTraspasoIncremento().add(item.getTraspasoIncremento()));
                        partidasDirectas.setTraspasoReduccion(partidasDirectas.getTraspasoReduccion().add(item.getTraspasoReduccion()));
                        partidasDirectas.setReformaCodificado(partidasDirectas.getValor().add(partidasDirectas.getTraspasoIncremento()).subtract(partidasDirectas.getTraspasoReduccion()));
                        partidasDirectas.setUsuarioModificacion(userSession.getNameUser());
                        partidasDirectas.setFechaModificacion(new Date());
                        partidasDirectas.setEstadoPartida(estadodistri);
                        proformaPresupuestoPlanificadoService.edit(partidasDirectas);
                    }
                } else {
                    partidasDirectas = Utils.clone(item);
                    partidasDirectas.setId(null);
                    partidasDirectas.setUsuarioModificacion(userSession.getNameUser());
                    partidasDirectas.setFechaModificacion(new Date());
                    partidasDirectas.setEstadoPartida(estadodistri);
                    partidasDirectas.setCodigoReforma(null);
                    partidasDirectas.setCodigoReformaTraspaso(null);
                    partidasDirectas.setValor(BigDecimal.ZERO);
                    partidasDirectas = proformaPresupuestoPlanificadoService.create(partidasDirectas);
                }
                if (item.getTraspasoIncremento() != BigDecimal.ZERO || item.getTraspasoReduccion() != BigDecimal.ZERO) {
                    Presupuesto presupuesto = reformaTraspasoService.valoresPresupuestoPapp(busqueda.getAnio(), item.getPartidaPresupuestaria());
                    if (presupuesto != null) {
                        presupuesto.setReformaSuplemetario(presupuesto.getReformaSuplemetario().add(item.getTraspasoIncremento()));
                        presupuesto.setReformaReducido(presupuesto.getReformaReducido().add(item.getTraspasoReduccion()));
                        presupuesto.setCodificado((presupuesto.getPresupuestoInicial().add(presupuesto.getReformaSuplemetario())).subtract(presupuesto.getReformaReducido()));
                        presupuestoService.edit(presupuesto);
                    } else {
                        Presupuesto pre = new Presupuesto();
                        pre.setEstructruaNew(item.getEstructruaNew());
                        pre.setItemNew(item.getItemNew());
                        pre.setFuenteNew(item.getFuenteNew());
                        pre.setFuenteDirecta(item.getFuenteDirecta());
                        pre.setPartida(item.getPartidaPresupuestaria());
                        pre.setTipo(false);
                        pre.setPeriodo(item.getPeriodo());
                        pre.setComprometido(BigDecimal.ZERO);
                        pre.setPresupuestoInicial(BigDecimal.ZERO);
                        pre.setReformaSuplemetario(item.getTraspasoIncremento());
                        pre.setReformaReducido(item.getTraspasoReduccion());
                        pre.setMontoDisponible(item.getTraspasoIncremento());
                        pre.setCodificado((pre.getPresupuestoInicial().add(pre.getReformaSuplemetario())).subtract(pre.getReformaReducido()));
                        presupuestoService.create(pre);
                    }
                }
            }
        }
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + "aprobada con éxito");
        JsfUtil.update("formularioInicial");
        JsfUtil.update("formularioEncabezado");
        JsfUtil.update("formularioCuerpo");
        reformaTraspaso = new ReformaTraspaso();
    }

    public void generateReport(ReformaTraspaso r) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        String autorizado1 = "", autorizado2 = "", alcaldeCanton = "", alcalde = "";
        String valor_informe = reformaTraspasoService.obtieneValorInforme(r);
        if (valor_informe.equals(null) || valor_informe.equals("") || valor_informe.equals(" ")) {
            autorizado1 = valoresService.findByCodigo("txt_autorizado1_reformaT1");
            autorizado2 = valoresService.findByCodigo("txt_autorizado2_reformaT1");
            autorizado = autorizado1 + "\n" + autorizado2;
            motivacion = reformaTraspasoService.obtieneConceptoTramite(reformaTraspaso);
        } else {
            JSONObject json = new JSONObject(valor_informe);
            autorizado = json.getString("AUTORIZADO");
            referencia = json.getString("REFERENCIA");
            motivacion = json.getString("MOTIVACION");
        }
        alcaldeCanton = valoresService.findByCodigo("txt_alcalde");
        alcalde = reformaTraspasoService.getClienteAlcalde();
        ss.addParametro("id_reforma", r.getId());
        ss.addParametro("codigo_reforma", r.getCodigo());
        ss.addParametro("periodo", r.getPeriodo());
        ss.addParametro("fecha_traspaso_reforma", r.getFechaAprobacion());
        ss.addParametro("unidad_solicitante", r.getUnidadRequiriente().getNombre());
        ss.addParametro("rolAlcalde", alcaldeCanton);
        ss.addParametro("elabPresupuesto", r.getUsuarioCreacion());
        ss.addParametro("revFinan", "");
        ss.addParametro("nombreAlcalde", alcalde);
        PrimeFaces.current().executeScript("PF('dlgFormularioReforma').show()");
        JsfUtil.update("frmFormularioReforma");
    }

    public void imprimirFormularioReforma() {
        JSONObject valor = new JSONObject();
        valor.put("AUTORIZADO", autorizado);
        valor.put("REFERENCIA", referencia);
        valor.put("MOTIVACION", motivacion);
        reformaTraspaso.setValorFormularioPdi(valor.toString());
        reformaTraspasoService.edit(reformaTraspaso);
        ss.addParametro("autorizado1", autorizado);
        ss.addParametro("referencia", referencia);
        ss.addParametro("motivacion", motivacion);
        ss.setNombreReporte("reformaTraspasoT1PDI");
        ss.setNombreSubCarpeta("reformasPresupuesto");
        PrimeFaces.current().executeScript("PF('dlgFormularioReforma').hide()");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        resetValoresInform();
    }

    public void resetValoresInform() {
        reformaTraspaso = new ReformaTraspaso();
        autorizado = "";
        referencia = "";
        motivacion = "";
    }

    public Boolean disableImprimirFormularioReforma(ReformaTraspaso reforma) {
        if (reforma.getEstadoReforma().getCodigo().equals("APRT")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void cargarPartidas() {
        if (null != reformaTraspaso.getId()) {
            proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
            proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
            proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS AND SETTERS">
    public Boolean getDisableGuardarReforma() {
        return disableGuardarReforma;
    }

    public void setDisableGuardarReforma(Boolean disableGuardarReforma) {
        this.disableGuardarReforma = disableGuardarReforma;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(String motivacion) {
        this.motivacion = motivacion;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<PresPlanProgramatico> getListaPresPlanProgramatico() {
        return listaPresPlanProgramatico;
    }

    public void setListaPresPlanProgramatico(List<PresPlanProgramatico> listaPresPlanProgramatico) {
        this.listaPresPlanProgramatico = listaPresPlanProgramatico;
    }

    public List<PresCatalogoPresupuestario> getListaPresCataPresupuestario() {
        return listaPresCataPresupuestario;
    }

    public void setListaPresCataPresupuestario(List<PresCatalogoPresupuestario> listaPresCataPresupuestario) {
        this.listaPresCataPresupuestario = listaPresCataPresupuestario;
    }

    public List<PresFuenteFinanciamiento> getListaPresFuenteFinanciamiento() {
        return listaPresFuenteFinanciamiento;
    }

    public void setListaPresFuenteFinanciamiento(List<PresFuenteFinanciamiento> listaPresFuenteFinanciamiento) {
        this.listaPresFuenteFinanciamiento = listaPresFuenteFinanciamiento;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuestoPlanificado() {
        return proformaPresupuestoPlanificado;
    }

    public void setProformaPresupuestoPlanificado(ProformaPresupuestoPlanificado proformaPresupuestoPlanificado) {
        this.proformaPresupuestoPlanificado = proformaPresupuestoPlanificado;
    }

    public Boolean getRenderRegresarInicio() {
        return renderRegresarInicio;
    }

    public void setRenderRegresarInicio(Boolean renderRegresarInicio) {
        this.renderRegresarInicio = renderRegresarInicio;
    }

    public Boolean getRenderFormularioInicial() {
        return renderFormularioInicial;
    }

    public void setRenderFormularioInicial(Boolean renderFormularioInicial) {
        this.renderFormularioInicial = renderFormularioInicial;
    }

    public Boolean getRenderFielEncabezadoReforma() {
        return renderFielEncabezadoReforma;
    }

    public void setRenderFielEncabezadoReforma(Boolean renderFielEncabezadoReforma) {
        this.renderFielEncabezadoReforma = renderFielEncabezadoReforma;
    }

    public LazyModel<ReformaTraspaso> getReformasLazy() {
        return reformasLazy;
    }

    public void setReformasLazy(LazyModel<ReformaTraspaso> reformasLazy) {
        this.reformasLazy = reformasLazy;
    }

    public Boolean getDisBtnGenReformaPDI() {
        return disBtnGenReformaPDI;
    }

    public void setDisBtnGenReformaPDI(Boolean disBtnGenReformaPDI) {
        this.disBtnGenReformaPDI = disBtnGenReformaPDI;
    }

    public Boolean getRenderSeleccionReforma() {
        return renderSeleccionReforma;
    }

    public void setRenderSeleccionReforma(Boolean renderSeleccionReforma) {
        this.renderSeleccionReforma = renderSeleccionReforma;
    }

    public Boolean getBtnGenerarPDI() {
        return btnGenerarPDI;
    }

    public void setBtnGenerarPDI(Boolean btnGenerarPDI) {
        this.btnGenerarPDI = btnGenerarPDI;
    }

    public List<ReformaTraspaso> getReformas() {
        return reformas;
    }

    public void setReformas(List<ReformaTraspaso> reformas) {
        this.reformas = reformas;
    }

    public List<MasterCatalogo> getListaAnio() {
        return listaAnio;
    }

    public void setListaAnio(List<MasterCatalogo> listaAnio) {
        this.listaAnio = listaAnio;
    }

    public Boolean getBolPartidaDirecta() {
        return bolPartidaDirecta;
    }

    public void setBolPartidaDirecta(Boolean bolPartidaDirecta) {
        this.bolPartidaDirecta = bolPartidaDirecta;
    }

    public Boolean getRenderBtnGenRefPAPP() {
        return renderBtnGenRefPAPP;
    }

    public void setRenderBtnGenRefPAPP(Boolean renderBtnGenRefPAPP) {
        this.renderBtnGenRefPAPP = renderBtnGenRefPAPP;
    }

    public Boolean getRenderBtnGenRefPDI() {
        return renderBtnGenRefPDI;
    }

    public void setRenderBtnGenRefPDI(Boolean renderBtnGenRefPDI) {
        this.renderBtnGenRefPDI = renderBtnGenRefPDI;
    }

    public Boolean getBolPapp() {
        return bolPapp;
    }

    public void setBolPapp(Boolean bolPapp) {
        this.bolPapp = bolPapp;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public Boolean getBtnGenerarReforma() {
        return btnGenerarReforma;
    }

    public void setBtnGenerarReforma(Boolean btnGenerarReforma) {
        this.btnGenerarReforma = btnGenerarReforma;
    }

    public Boolean getBtnGenerarReformaPDI() {
        return btnGenerarReformaPDI;
    }

    public void setBtnGenerarReformaPDI(Boolean btnGenerarReformaPDI) {
        this.btnGenerarReformaPDI = btnGenerarReformaPDI;
    }

    public Boolean getBtnNuevoPartidaDirecta() {
        return btnNuevoPartidaDirecta;
    }

    public void setBtnNuevoPartidaDirecta(Boolean btnNuevoPartidaDirecta) {
        this.btnNuevoPartidaDirecta = btnNuevoPartidaDirecta;
    }

    public Boolean getBtnTraspasos() {
        return btnTraspasos;
    }

    public void setBtnTraspasos(Boolean btnTraspasos) {
        this.btnTraspasos = btnTraspasos;
    }

    public Long getIdUnidadA() {
        return idUnidadA;
    }

    public void setIdUnidadA(Long idUnidadA) {
        this.idUnidadA = idUnidadA;
    }

    public String getTipoTraspaso() {
        return tipoTraspaso;
    }

    public void setTipoTraspaso(String tipoTraspaso) {
        this.tipoTraspaso = tipoTraspaso;
    }

    public BigDecimal getTotalSaldoDisponible() {
        return totalSaldoDisponible;
    }

    public void setTotalSaldoDisponible(BigDecimal totalSaldoDisponible) {
        this.totalSaldoDisponible = totalSaldoDisponible;
    }

    public BigDecimal getTotalTraspasoIncremento() {
        return totalTraspasoIncremento;
    }

    public void setTotalTraspasoIncremento(BigDecimal totalTraspasoIncremento) {
        this.totalTraspasoIncremento = totalTraspasoIncremento;
    }

    public BigDecimal getTotalTraspasoReduccion() {
        return totalTraspasoReduccion;
    }

    public void setTotalTraspasoReduccion(BigDecimal totalTraspasoReduccion) {
        this.totalTraspasoReduccion = totalTraspasoReduccion;
    }

    public BigDecimal getTotalMontoReformado() {
        return totalMontoReformado;
    }

    public void setTotalMontoReformado(BigDecimal totalMontoReformado) {
        this.totalMontoReformado = totalMontoReformado;
    }

    public BigDecimal getTotalTraspasoIncrementoPDirecta() {
        return totalTraspasoIncrementoPDirecta;
    }

    public void setTotalTraspasoIncrementoPDirecta(BigDecimal totalTraspasoIncrementoPDirecta) {
        this.totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta;
    }

    public BigDecimal getTotalTraspasoReduccionPDirecta() {
        return totalTraspasoReduccionPDirecta;
    }

    public void setTotalTraspasoReduccionPDirecta(BigDecimal totalTraspasoReduccionPDirecta) {
        this.totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta;
    }

    public BigDecimal getTotalMontoReformadoPDirecta() {
        return totalMontoReformadoPDirecta;
    }

    public void setTotalMontoReformadoPDirecta(BigDecimal totalMontoReformadoPDirecta) {
        this.totalMontoReformadoPDirecta = totalMontoReformadoPDirecta;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public LazyModel<ProformaPresupuestoPlanificado> getProformaPresupuestoLazy() {
        return proformaPresupuestoLazy;
    }

    public void setProformaPresupuestoLazy(LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy) {
        this.proformaPresupuestoLazy = proformaPresupuestoLazy;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public Boolean getFormBusqRegistrar() {
        return formBusqRegistrar;
    }

    public void setFormBusqRegistrar(Boolean formBusqRegistrar) {
        this.formBusqRegistrar = formBusqRegistrar;
    }

    public Boolean getFormBusqConsultar() {
        return formBusqConsultar;
    }

    public void setFormBusqConsultar(Boolean formBusqConsultar) {
        this.formBusqConsultar = formBusqConsultar;
    }

//</editor-fold>
}
