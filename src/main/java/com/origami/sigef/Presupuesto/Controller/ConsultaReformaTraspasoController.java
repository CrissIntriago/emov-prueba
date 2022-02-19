/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Entidad.Controller.DatosGeneralesEntidadController;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "consultaReformaTraspasoView")
@ViewScoped
public class ConsultaReformaTraspasoController implements Serializable {

    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private OpcionBusqueda busqueda;
    private Distributivo dataView;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;

    private List<Producto> listaProducto;
    private List<Producto> listaProductoT1;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;

    private List<PlanProgramatico> listaPlanProgramatico;
    private List<Distributivo> listaDistributivo;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<PartidasDistributivo> listaVista;

    private String num, referencia, motivacion, autorizado;
    private BigDecimal rmu;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;

    private boolean vistaPartidaDisGeneral;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;
    private Boolean filtroTipo, renderTabPAPP, renderedTabPartidaDirecta;

    @Inject
    private ReformaTraspasoService reformaTraspasoService;
    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private PlanProgramaticoService planProgramaticoService;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private ValoresService valorService;
    @Inject
    private ServletSession ss;
    @Inject
    private ValoresService valoresServices;
    @Inject
    private FileUploadDoc uploadDoc;

    @PostConstruct
    public void initView() {
        reformaTraspaso = new ReformaTraspaso();
        detalleReformaTraspaso = new DetalleReformaTraspaso();
        busqueda = new OpcionBusqueda();
        dataView = new Distributivo();
//        CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "APROB-REF");
        lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
        lazyReformaTraspaso.getFilterss().put("estado:equal", true);
        lazyReformaTraspaso.getFilterss().put("unidadRequiriente:notEqual", null);
        listaProducto = new ArrayList<>();
        listaProductoT1 = new ArrayList<>();
    }
    
    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }
    
    public Boolean renderBtnImpPAPP(ReformaTraspaso r) {
        if (r.getUnidadRequiriente().getId() != null && r.getId() != null) {
            listaProducto = obtProdUniRespRefor(r.getUnidadRequiriente().getId(), BigInteger.valueOf(r.getId()));
            if (!listaProducto.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public Boolean renderBtnImpPDI(ReformaTraspaso r) {
        listaPartidaDirecta = detalleReformaTraspasoService.getListPartidaDirectaReforma(r.getPeriodo(), BigInteger.valueOf(r.getId()));
        if (!listaPartidaDirecta.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (reforma.getTipo()) {
            List<Producto> listaProductoT = new ArrayList<>();
            listaProductoT = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            if (Utils.isNotEmpty(listaProductoT)) {
                for (Producto item : listaProductoT) {
                    if (item.getEstado()) {
                        totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                        totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                        totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                        totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                    }
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
            List<Producto> listaProT2 = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
            List<PartidasDistributivo> listaPartidaDistT2 = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reforma.getId()));
            List<PartidasDistributivoAnexo> listaPartidaDistAnexoT2 = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reforma.getId()));
            List<ProformaPresupuestoPlanificado> listaPartidaDirectaT2 = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));

            if (Utils.isNotEmpty(listaProT2)) {
                for (Producto item : listaProT2) {

                    if (item.getEstado()) {
                        totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                        totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                        totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                        totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                    }
                }
            }
            if (Utils.isNotEmpty(listaPartidaDistT2)) {
                for (PartidasDistributivo listaRubro : listaPartidaDistT2) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaRubro.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + listaRubro.getTraspasoReduccion().doubleValue();
                }
            }
            if (Utils.isNotEmpty(listaPartidaDistAnexoT2)) {
                for (PartidasDistributivoAnexo listaAnexo : listaPartidaDistAnexoT2) {
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaAnexo.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + listaAnexo.getTraspasoReduccion().doubleValue();
                }
            }
            if (Utils.isNotEmpty(listaPartidaDirectaT2)) {
                for (ProformaPresupuestoPlanificado partidaDirecta : listaPartidaDirectaT2) {
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

    public void consultarReforma(ReformaTraspaso reforma) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = reforma;
        busqueda.setAnio(reforma.getPeriodo());
        totalTraspasoIncremento = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, true);
        totalTraspasoReduccion = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, false);
        listaProducto = new ArrayList<>();
        listaProductoT1 = new ArrayList<>();
        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        if (reforma.getTipo()) {
            if (reformaTraspaso.getEstadoReforma().getCodigo().equals("APRT") || reformaTraspaso.getEstadoReforma().getCodigo().equals("RECT")) {
                listaProductoT1 = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(),BigInteger.valueOf(reformaTraspaso.getId()));
            } else if (reformaTraspaso.getEstadoReforma().getCodigo().equals("REGT")) {
                listaProductoT1 = reformaTraspasoService.getListProductoByReforma2(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
            }
            renderTabPAPP = !listaProductoT1.isEmpty();
            listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPDIReformaT1(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
            renderedTabPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
            PrimeFaces.current().executeScript("PF('dlgPapp').show()");
        } else {
            if (reformaTraspaso.getEstadoReforma().getCodigo().equals("APRT") || reformaTraspaso.getEstadoReforma().getCodigo().equals("RECT")) {
                listaProducto = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma2(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
                listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
            } else if (reformaTraspaso.getEstadoReforma().getCodigo().equals("REGT")) {
                listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
                listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma2(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
                listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
            }
            renderedPAPP = !listaProducto.isEmpty();
            renderedDistributivo = !listaDistributivo.isEmpty();
            renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
            renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
            PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
            PrimeFaces.current().ajax().update("formDlgVistaReforma");
        }
    }

    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        dataView = d;
    //    setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        if (vista) {
            vistaPartidaDisGeneral = true;
        } else {
            vistaPartidaDisGeneral = false;
        }
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void clearAllFilters() {
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formConsultaReformaTraspaso:dataReformaTraspaso");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();
            PrimeFaces.current().ajax().update("formConsultaReformaTraspaso:dataReformaTraspaso");
        }
    }
    
    public void generateReport(ReformaTraspaso r) {
        reformaTraspaso = r;
        String alcaldeCanton = "", alcalde = "";
        String valor_informe = reformaTraspasoService.obtieneValorInforme(r);
        if (valor_informe.equals(null) || valor_informe.equals("") || valor_informe.equals(" ")) {
            String autorizado1 = valoresServices.findByCodigo("txt_autorizado1_reformaT1");
            String autorizado2 = valoresServices.findByCodigo("txt_autorizado2_reformaT1");
            autorizado = autorizado1 + "\n" + autorizado2;
            motivacion = reformaTraspasoService.obtieneConceptoTramite(reformaTraspaso);
        }else {
            JSONObject json = new JSONObject(valor_informe);
            autorizado = json.getString("AUTORIZADO");
            referencia = json.getString("REFERENCIA");
            motivacion = json.getString("MOTIVACION");
        }
        alcaldeCanton = valoresServices.findByCodigo("txt_alcalde");
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
    }

    public void visualizarReportes(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        if (reformaTraspaso.getTipo()) {
            num = "No." + reformaTraspaso.getNumeracion() + "-TRASPASO TIPO 1-" + reformaTraspaso.getPeriodo();
        } else {
            num = "No." + reformaTraspaso.getNumeracion() + "-TRASPASO TIPO 2-" + reformaTraspaso.getPeriodo();
        }
        PrimeFaces.current().executeScript("PF('dlgFormulario').show()");
    }

    public void imprimirReportes(int prueba) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ss.borrarParametros();
        switch (prueba) {
            case 0:

                ss.addParametro("num", num);
                ss.addParametro("fecha", simpleDateFormat.format(reformaTraspaso.getFechaTraspasoReforma()));
                ss.addParametro("parrafouno", reformaTraspaso.getValorInforme());
                ss.setNombreReporte("reformaGeneralPertinenciaTraspaso");
                break;
            case 1:
                ss.addParametro("id", reformaTraspaso.getId());
                if (reformaTraspaso.getTipo()) {
                    ss.setNombreReporte("reformaTraspasoT1AnexoUno");
                } else {
                    ss.setNombreReporte("reformaTraspasoT2AnexoUno");
                }
                break;
            case 2:
                ss.addParametro("id", reformaTraspaso.getId());
                ss.setNombreReporte("reformaTraspasoT1PAPP");
                break;
            case 3:
                ss.addParametro("id", reformaTraspaso.getId());
                ss.setNombreReporte("reformaTraspasoT1PAPPSinDesglose");
                break;
            default:
                break;
        }

        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

    }

    public void handleFileUploadInfPert(FileUploadEvent event) {
        try {
            Map<String, Object> mp = new HashMap<>();
            mp.put("NOMBRE ARCHIVO", "INFORME DE PERTINENCIA "+ (reformaTraspaso.getTipo()?"T1":"T2"));
            mp.put("NUMERO DE TRAMITE", reformaTraspaso.getPeriodo() + "-"+ reformaTraspaso.getNumTramite());
            uploadDoc.uploadRepositorio(reformaTraspaso, Arrays.asList(event.getFile()), mp);
        } catch (Exception ex) {
            Logger.getLogger(DatosGeneralesEntidadController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex.getMessage(), "Ocurrio un error al cargar archivo");
            return;
        }
    }

    public void editarInforme() {
        String valor = valorService.findByCodigo("HTML_INFORME_PERTINENCIA");
        if (reformaTraspaso.getValorInforme() == null || reformaTraspaso.getValorInforme().equals("")) {
            reformaTraspaso.setValorInforme(valor);
        }
        if (reformaTraspaso.getTipo()) {
            num = "No." + reformaTraspaso.getNumeracion() + "-TRASPASO TIPO 1-" + reformaTraspaso.getPeriodo();
        } else {
            num = "No." + reformaTraspaso.getNumeracion() + "-TRASPASO TIPO 2-" + reformaTraspaso.getPeriodo();
        }
        PrimeFaces.current().executeScript("PF('dlgFormularioEdit').show()");
    }

    public void imprimirInformePertinenciaEdit() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (reformaTraspaso.getValorInforme() != null) {
            ss.addParametro("parrafouno", reformaTraspaso.getValorInforme());
            reformaTraspasoService.edit(reformaTraspaso);
        }
        ss.addParametro("num", num);
        ss.addParametro("fecha", simpleDateFormat.format(reformaTraspaso.getFechaTraspasoReforma()));
        ss.setNombreReporte("reformaGeneralPertinenciaTraspaso");

        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
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

    public Boolean getRenderTabPAPP() {
        return renderTabPAPP;
    }

    public void setRenderTabPAPP(Boolean renderTabPAPP) {
        this.renderTabPAPP = renderTabPAPP;
    }

    public Boolean getRenderedTabPartidaDirecta() {
        return renderedTabPartidaDirecta;
    }

    public void setRenderedTabPartidaDirecta(Boolean renderedTabPartidaDirecta) {
        this.renderedTabPartidaDirecta = renderedTabPartidaDirecta;
    }
    
    public void setearValorInforme() {
        String valor = valorService.findByCodigo("HTML_INFORME_PERTINENCIA");
        reformaTraspaso.setValorInforme(valor);
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

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
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

    public List<Producto> getListaProductoT1() {
        return listaProductoT1;
    }

    public void setListaProductoT1(List<Producto> listaProductoT1) {
        this.listaProductoT1 = listaProductoT1;
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

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
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

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
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

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
    }

    public Boolean getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(Boolean filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
