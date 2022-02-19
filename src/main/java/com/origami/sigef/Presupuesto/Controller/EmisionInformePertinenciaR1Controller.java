/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Entidad.Controller.DatosGeneralesEntidadController;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Model.FormularioEmisionDatos;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Valores;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;

import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;

import com.origami.sigef.resource.procesos.entities.Observaciones;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 * @author Sandra Arroba
 */
@Named(value = "emisionRTipoUno")
@ViewScoped
public class EmisionInformePertinenciaR1Controller extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReformaTraspasoService reformaTraspasoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession user;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    @Inject
    private ServletSession ss;
    @Inject
    private ValoresService valoresService;
    @Inject
    private FileUploadDoc uploadDoc;

    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;
    private ReformaTraspaso reformaTraspaso;
    private OpcionBusqueda busqueda;
    private FormularioEmisionDatos formulario;
    private List<Producto> listaProducto;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<ProformaPresupuestoPlanificado> listaPartidaDirecta;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private String observaciones;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;
    private Boolean renderTabPAPP, renderedPartidaDirecta, renderAnexosPAPP;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                reformaTraspaso = new ReformaTraspaso();
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                busqueda = new OpcionBusqueda();
                listaProducto = new ArrayList<>();
                listaPartidaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
                listaPartidaDirecta = new ArrayList<>();
                formulario = new FormularioEmisionDatos();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
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
        if (incremento) {
            return totalTraspasoIncrementoR;
        } else {
            return totalTraspasoReduccionR;
        }
    }

    public void setearValorInforme() {
        String valor = valoresService.findByCodigo("HTML_INFORME_PERTINENCIA");
        formulario.setParrafoUno(valor);
        reformaTraspaso.setValorInforme(valor);
    }

    public void realizarFormulario(ReformaTraspaso r, Boolean prueba) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = r;
        listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (!listaProducto.isEmpty()) {
            renderAnexosPAPP = Boolean.TRUE;
            JsfUtil.update("dlgFormularioPer");
            //JsfUtil.update("dlgFormularioPer");
        }
        formulario = new FormularioEmisionDatos();
        if (r.getTipo()) {
            formulario.setNum("No." + r.getNumeracion() + "-TRASPASO TIPO 1-" + r.getPeriodo());
        } else {
            formulario.setNum("No." + r.getNumeracion() + "-TRASPASO TIPO 2-" + r.getPeriodo());
        }
        String nombre1 = clienteService.getrolsUser(RolUsuario.presupuesto);
        if (nombre1 == null) {
            formulario.setNombrePresupuesto("");
            formulario.setCargoPresupuesto("");
        } else {
            Distributivo dist1 = clienteService.getuusuarioLogeado(nombre1);
            if (dist1 != null) {
                formulario.setNombrePresupuesto(dist1.getServidorPublico().getPersona().getNombreCompleto());
                formulario.setCargoPresupuesto(dist1.getCargo().getNombreCargo());
            } else {
                formulario.setNombrePresupuesto("");
                formulario.setCargoPresupuesto("");
            }
        }
        String nombre2 = clienteService.getrolsUser(RolUsuario.financiero);
        if (nombre2 == null) {
            formulario.setNombreFinanciero("");
            formulario.setCargoFinanciero("");
        } else {
            Distributivo dist2 = clienteService.getuusuarioLogeado(nombre2);
            if (dist2 != null) {
                formulario.setNombreFinanciero(dist2.getServidorPublico().getPersona().getNombreCompleto());
                formulario.setCargoFinanciero(dist2.getCargo().getNombreCargo());
            } else {
                formulario.setNombreFinanciero("");
                formulario.setCargoFinanciero("");
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formulario.setFecha(simpleDateFormat.format(reformaTraspaso.getFechaTraspasoReforma()));
        if (prueba) {
            PrimeFaces.current().executeScript("PF('dlgFormulario').show()");
            PrimeFaces.current().ajax().update(":frmFormulario");
        } else {
            if (reformaTraspaso.getValorInforme() == null) {
                setearValorInforme();
            }
            PrimeFaces.current().executeScript("PF('dlgFormularioPer').show()");
            PrimeFaces.current().ajax().update(":frmFormularioPer");
        }
    }

    public void handleFileUploadInfPert(FileUploadEvent event) {
        try {
            Map<String, Object> mp = new HashMap<>();
            mp.put("NOMBRE ARCHIVO", "INFORME DE PERTINENCIA " + (reformaTraspaso.getTipo() ? "T1" : "T2"));
            mp.put("NUMERO DE TRAMITE", reformaTraspaso.getPeriodo() + "-" + reformaTraspaso.getNumTramite());
            uploadDoc.uploadRepositorio(reformaTraspaso, Arrays.asList(event.getFile()), mp);
        } catch (Exception ex) {
            Logger.getLogger(DatosGeneralesEntidadController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex.getMessage(), "Ocurrio un error al cargar archivo");
            return;
        }
    }

    public void imprimirInformePertinencia(Boolean prueba) {
        if (prueba) {
            ss.addParametro("num", formulario.getNum());
            ss.addParametro("fecha", formulario.getFecha());
            ss.addParametro("parrafouno", formulario.getParrafoUno());
            ss.addParametro("parrafodos", formulario.getParrafoDos());
            ss.addParametro("parrafotres", formulario.getParrafoTres());
            ss.addParametro("parrafocuatro", formulario.getParrafoCuatro());
            ss.addParametro("parrafocinco", formulario.getParrafoCinco());
            ss.addParametro("nombrePresupuesto", formulario.getNombrePresupuesto());
            ss.addParametro("cargoPresupuesto", formulario.getCargoPresupuesto());
            ss.addParametro("tipoPresupuesto", "Presupuesto");
            ss.addParametro("nombreFinanciero", formulario.getNombreFinanciero());
            ss.addParametro("cargoFinanciero", formulario.getCargoFinanciero());
            ss.addParametro("tipoFinanciero", "Financiero");
            ss.setNombreReporte("refromaGeneralPertinencia");
        } else {
            if (reformaTraspaso.getValorInforme() != null) {
                ss.addParametro("parrafouno", reformaTraspaso.getValorInforme());
                reformaTraspasoService.edit(reformaTraspaso);
            }
            ss.addParametro("num", formulario.getNum());
            ss.addParametro("fecha", formulario.getFecha());
//            ss.addParametro("parrafouno", formulario.getParrafoUno());
            ss.addParametro("nombrePresupuesto", formulario.getNombrePresupuesto());
            ss.addParametro("cargoPresupuesto", formulario.getCargoPresupuesto());
            ss.addParametro("nombreFinanciero", formulario.getNombreFinanciero());
            ss.addParametro("cargoFinanciero", formulario.getCargoFinanciero());
            ss.setNombreReporte("reformaGeneralPertinenciaTraspaso");
        }
        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirAnexos(int a) {
        ss.addParametro("id", reformaTraspaso.getId());
        switch (a) {
            case 1:
                ss.setNombreReporte("reformaTraspasoT1AnexoUno");
                break;
            case 2:
                ss.setNombreReporte("reformaTraspasoT1PAPP");
                break;
            case 3:
                ss.setNombreReporte("reformaTraspasoT1PAPPSinDesglose");
                break;
            default:
                break;
        }
        ss.setNombreSubCarpeta("reformasPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void consultarPapp(ReformaTraspaso reforma) {
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        reformaTraspaso = reforma;
        busqueda.setAnio(reforma.getPeriodo());
        totalTraspasoIncremento = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, true);
        totalTraspasoReduccion = getTotalIncrementoOrReduccionByReforma(reformaTraspaso, false);
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta2(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));
        renderTabPAPP = !listaProducto.isEmpty();
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPDIReformaT1(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
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
            observacion.setObservacion(observaciones);
            if (listaProducto.isEmpty() && !listaPartidaDirecta.isEmpty()) {
                getParamts().put("userFinan", clienteService.getrolsUser(RolUsuario.rolReformasPDI));
            } else {
                getParamts().put("userFinan", clienteService.getrolsUser(RolUsuario.rolAprobarReformaMaxAutoridad));
            }
            if (saveTramite() == null) {
                return;
            }
            reformaTraspaso = new ReformaTraspaso();
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public Boolean getRenderAnexosPAPP() {
        return renderAnexosPAPP;
    }

    public void setRenderAnexosPAPP(Boolean renderAnexosPAPP) {
        this.renderAnexosPAPP = renderAnexosPAPP;
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

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public FormularioEmisionDatos getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioEmisionDatos formulario) {
        this.formulario = formulario;
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

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public Boolean getRenderTabPAPP() {
        return renderTabPAPP;
    }

    public void setRenderTabPAPP(Boolean renderTabPAPP) {
        this.renderTabPAPP = renderTabPAPP;
    }

    public Boolean getRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(Boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }

//</editor-fold>
}
