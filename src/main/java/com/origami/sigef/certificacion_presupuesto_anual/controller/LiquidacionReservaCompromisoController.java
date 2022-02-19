/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.certificacion_presupuesto_anual.service.ContratoReservaService;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ContratosReservaEjecuion;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "liquidacionReservaView")
@ViewScoped
public class LiquidacionReservaCompromisoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReservaCompromisoService reservaService;
    @Inject
    private DetalleReservaCompromisoService detalleReservaService;
    @Inject
    private ContratoReservaService contratoService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ClienteService clienteService;

    private SolicitudReservaCompromiso solicitudReserva;
    private DetalleSolicitudCompromiso detalleReserva;

    private LazyModel<SolicitudReservaCompromiso> lazyReserva;
    private List<ContratosReservaEjecuion> listaAqusicionesGuardar;
    private List<DetalleSolicitudCompromiso> listaSolicitudes;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private boolean opcionContracto, listContratos;
    private List<ContDiarioGeneralDetalle> detalleAcumulado;
    private List<UnidadAdministrativa> listaFilroUnidad;
    private List<CatalogoItem> listaFiltroEstado;
    private List<DetalleReservaCompromisoService> detallesSeleccionados;
    private BigDecimal totalSolicitado, totalComprometido, totalEjecuato, totalLiquidado, totalLiberado;
    private String observaciones;
    private static final Logger LOG = Logger.getLogger(LiquidacionReservaCompromisoController.class.getName());

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.solicitudReserva = new SolicitudReservaCompromiso();
                this.detalleReserva = new DetalleSolicitudCompromiso();
                this.lazyReserva = new LazyModel(SolicitudReservaCompromiso.class);
                // this.lazyReserva.getFilterss().put("comprometido:equal", true);
                this.lazyReserva.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                this.lazyReserva.getSorteds().put("secuencial", "ASC");
                this.listaAqusicionesGuardar = new ArrayList<>();
                this.listaSolicitudes = new ArrayList<>();
                this.procedimientoRequisitoList = new ArrayList<>();
                opcionContracto = true;
                listContratos = false;
                detalleAcumulado = new ArrayList<>();
                this.listaFilroUnidad = reservaService.getListaUnidadesReserva();
                this.listaFiltroEstado = reservaService.getListaEstadosReserva();
                this.detallesSeleccionados = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();

        return a;
    }

    public void totales() {

        totalSolicitado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalEjecuato = BigDecimal.ZERO;
        totalLiquidado = BigDecimal.ZERO;
        totalLiberado = BigDecimal.ZERO;

        for (DetalleSolicitudCompromiso data : listaSolicitudes) {
            totalSolicitado = totalSolicitado.add(data.getMontoSolicitado());
            totalComprometido = totalComprometido.add(data.getMontoComprometido());
            totalEjecuato = totalEjecuato.add(data.getEjecutado());
            totalLiquidado = totalLiquidado.add(data.getLiquidado());
            if (data.getLiberado() != null) {
                totalLiberado = totalLiberado.add(data.getLiberado());
            }
        }
        totalSolicitado = totalSolicitado.add(totalLiberado).add(totalLiquidado);
    }

    public void showEjecutadoReservas() {
        showEjecutadoReservasGlobal(solicitudReserva);

        for (DetalleSolicitudCompromiso item : listaSolicitudes) {
            item.setLiquidado(item.getMontoSolicitado().subtract(item.getEjecutado()));
            item.setMontoSolicitado(item.getMontoSolicitado().subtract(item.getLiquidado()));
        }
        totales();
    }

    public void abrirDlogoLiquidacion(SolicitudReservaCompromiso r) {
        this.solicitudReserva = new SolicitudReservaCompromiso();
        this.solicitudReserva = r;
        listaAqusicionesGuardar = new ArrayList<>();
        this.listaAqusicionesGuardar = contratoService.getListaContratos(r);
        if (listaAqusicionesGuardar.size() > 0) {
            opcionContracto = false;
            listContratos = true;
        }
        listaSolicitudes = new ArrayList<>();
        List<DetalleSolicitudCompromiso> lista = reservaService.getListaDetlleSolciitud(r);

        for (DetalleSolicitudCompromiso i : lista) {

            this.listaSolicitudes.add(i);

        }
        procedimientoRequisitoList = new ArrayList<>();
        totales();
        this.procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(r.getProcedimiento());
        showEjecutadoReservasGlobal(r);
        PrimeFaces.current().executeScript("PF('DlgoLiquidacion').show()");
        PrimeFaces.current().ajax().update("formLiquidacion");
    }

    public void showEjecutadoReservasGlobal(SolicitudReservaCompromiso r) {

        detalleAcumulado = new ArrayList<>();

        List<ContDiarioGeneralDetalle> dataTrnsaccion = diarioGeneralService.showDetalleTransaccion(r);
        if (dataTrnsaccion.size() > 0) {
            for (ContDiarioGeneralDetalle item : dataTrnsaccion) {
                detalleAcumulado.add(item);
            }

            detallePartidasGlobal();
        }
    }

    public void detallePartidasGlobal() {
        BigDecimal sumandoOtros = BigDecimal.ZERO;
        totalEjecuato = BigDecimal.ZERO;
        listaSolicitudes = new ArrayList<>();
        listaSolicitudes.clear();
        List<DetalleSolicitudCompromiso> lista = reservaService.getListaDetlleSolciitud(solicitudReserva);
        for (DetalleSolicitudCompromiso i : lista) {
            this.listaSolicitudes.add(i);
        }
        for (DetalleSolicitudCompromiso item : listaSolicitudes) {
            sumandoOtros = BigDecimal.ZERO;
            for (ContDiarioGeneralDetalle data : detalleAcumulado) {
                if (data.getTipoRegistro() != null) {
                    if ("diario_general_devengado".equals(data.getTipoRegistro().getCodigo())) {

                        if (data.getIdDetalleReservaCompromiso() != null) {
                            if (item != null) {
                                if (data.getIdDetalleReservaCompromiso().equals(BigInteger.valueOf(item.getId()))) {
                                    sumandoOtros = sumandoOtros.add(data.getDevengado());
                                }
                            }
                        }
                    }
                }

            }
            item.setEjecutado(sumandoOtros);
            totalEjecuato = totalEjecuato.add(item.getEjecutado());
        }

    }

    public void detalleQuitandoTodoliquidacion() {
        DetalleSolicitudCompromiso item = new DetalleSolicitudCompromiso();
        for (DetalleSolicitudCompromiso data : listaSolicitudes) {
            data.setMontoSolicitado(data.getMontoSolicitado().add(data.getLiquidado()));
            data.setLiquidado(BigDecimal.ZERO);
        }
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("INFORMACIÓN", "LA LIQUIDACIÓN SE HA QUITADO CORRECTAMENTE");
    }

    public void detallePartidas() {

    }

    public void showEjecutadoReservasIndividual(DetalleSolicitudCompromiso data) {

    }

    public void detallePartidasIndividual(DetalleSolicitudCompromiso item) {
//        DetalleSolicitudCompromiso datos = new DetalleSolicitudCompromiso();
//        datos = item;
//        try {
//            if (datos.getLiquidado().doubleValue() > 0) {
//                datos.setMontoSolicitado(datos.getMontoSolicitado().add(datos.getLiquidado()));
//                datos.setLiquidado(BigDecimal.ZERO);
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addInformationMessage("INFORMACIÓN", "REGISTRO NO LIQUIDADO CORRECTAMENTE");
//
//            } else {
//
//                BigDecimal sumandoOtros = BigDecimal.ZERO;
//                for (DetalleTransaccion data : detalleAcumulado) {
//                    if (data.getTipoTransaccion() != null) {
//                        if ("diario_general_ejecucion".equals(data.getTipoTransaccion().getCodigo())) {
//                            if (data.getPartidaPresupuestaria() != null && data.getEstructuraProgramatica() != null) {
//                                if (datos.getActividadProducto() == null) {
//                                    if (datos.getPresupuesto().getItem().equals(data.getPartidaPresupuestaria()) && datos.getPresupuesto().getEstructura().equals(data.getEstructuraProgramatica())) {
//                                        sumandoOtros = sumandoOtros.add(data.getEjecutado());
//                                    }
//                                } else {
//                                    if (datos.getActividadProducto().getItemPresupuestario().equals(data.getPartidaPresupuestaria()) && datos.getActividadProducto().getEstructuraProgramatica().equals(data.getEstructuraProgramatica())) {
//                                        sumandoOtros = sumandoOtros.add(data.getEjecutado());
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                }
//                datos.setEjecutado(sumandoOtros);
//                datos.setLiquidado(datos.getMontoSolicitado().subtract(datos.getEjecutado()));
//                datos.setMontoSolicitado(datos.getMontoSolicitado().subtract(datos.getLiquidado()));
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addInformationMessage("INFORMACIÓN", "REGISTRO LIQUIDADO CORRECTAMENTE");
//            }
//
//            this.listaSolicitudes.add(this.listaSolicitudes.indexOf(datos), datos);
//            this.listaSolicitudes.remove(this.listaSolicitudes.indexOf(datos));
//
//        } catch (Exception e) {
//            PrimeFaces.current().ajax().update("messages");
//            JsfUtil.addErrorMessage("ERROR", "SE HA PRODUCIDO UN ERROR");
//        }

    }

    public void completarTarea() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void liquidadReserva() {

        try {
            short periodo = solicitudReserva.getPeriodo();
            observacion.setObservacion(observaciones);

            CatalogoItem estadoLiquidado = new CatalogoItem();
            estadoLiquidado = catalogoService.getItemByCatalogoAndCodigo("estado_solicitud", "LIQUI");
            solicitudReserva.setEstado(estadoLiquidado);
            reservaService.edit(solicitudReserva);
            DetalleSolicitudCompromiso detalle = new DetalleSolicitudCompromiso();
            for (DetalleSolicitudCompromiso data : listaSolicitudes) {
                detalle = new DetalleSolicitudCompromiso();
                detalle = Utils.clone(data);
                detalleReservaService.edit(detalle);
            }

            actualzandoReservaPartidasDistAndDa(solicitudReserva.getPeriodo());

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("INFORMACIÓN", "LA SOLICITUD SE HA LIQUIDADO CORRECTAMENTE Y HA SIDO ENVIADA CORRECTAMENTE");
            PrimeFaces.current().executeScript("PF('DlgoLiquidacion').hide()");
            PrimeFaces.current().ajax().update("formLiquidacion");
            getParamts().put("usuarioLegalizacion", clienteService.getrolsUser(RolUsuario.financiero));

            if (saveTramite() == null) {
                return;
            }
            solicitudReserva = new SolicitudReservaCompromiso();
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "SE HA PRODUCIDO UN ERROR");

        }
    }

    public void actualzandoReservaPartidasDistAndDa(Short periodo) {
        BigDecimal valor1 = BigDecimal.ZERO;
        BigDecimal valor2 = BigDecimal.ZERO;
        BigDecimal valor3 = BigDecimal.ZERO;
        List<Producto> listP = reservaService.listaReservasSinRepetirProducto(periodo);
        if (!listP.isEmpty()) {
            for (Producto pr : listP) {
                valor1 = reservaService.sumaTotalDeReservasProducto(periodo, pr.getId());
                int result = reservaService.updateReservaProducto(valor1, periodo, pr.getId());
                if (result > 0) {
                }
            }
        }
        List<ThCargoRubros> lis = reservaService.listaReservasSinRepetir(periodo);
        if (lis != null && !lis.isEmpty()) {
            for (ThCargoRubros li : lis) {
                valor2 = reservaService.sumaTotalDeReservasDevengado(periodo, li.getId());
                int result2 = reservaService.actualizarReservaPresupuesto(valor2, periodo, li.getId());

                if (result2 > 0) {

                }
            }
        }

        List<ProformaPresupuestoPlanificado> tmpPd = reservaService.listaReservasPdSinRepetir(periodo);

        if (tmpPd != null && !tmpPd.isEmpty()) {
            for (ProformaPresupuestoPlanificado li : tmpPd) {
                valor3 = reservaService.sumaTotalDeReservasDevengadoPd(periodo, li.getId());
                int result3 = reservaService.actualizarReservaPresupuestoPd(valor2, periodo, li.getId());

                if (result3 > 0) {

                }
            }
        }
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formGeneral:dataSolciitudesReserva");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formGeneral:dataSolciitudesReserva");
        }
    }

//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotalLiberado() {
        return totalLiberado;
    }

    public void setTotalLiberado(BigDecimal totalLiberado) {
        this.totalLiberado = totalLiberado;
    }

    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalEjecuato() {
        return totalEjecuato;
    }

    public void setTotalEjecuato(BigDecimal totalEjecuato) {
        this.totalEjecuato = totalEjecuato;
    }

    public BigDecimal getTotalLiquidado() {
        return totalLiquidado;
    }

    public void setTotalLiquidado(BigDecimal totalLiquidado) {
        this.totalLiquidado = totalLiquidado;
    }

    public List<DetalleReservaCompromisoService> getDetallesSeleccionados() {
        return detallesSeleccionados;
    }

    public void setDetallesSeleccionados(List<DetalleReservaCompromisoService> detallesSeleccionados) {
        this.detallesSeleccionados = detallesSeleccionados;
    }

    public List<UnidadAdministrativa> getListaFilroUnidad() {
        return listaFilroUnidad;
    }

    public void setListaFilroUnidad(List<UnidadAdministrativa> listaFilroUnidad) {
        this.listaFilroUnidad = listaFilroUnidad;
    }

    public List<CatalogoItem> getListaFiltroEstado() {
        return listaFiltroEstado;
    }

    public void setListaFiltroEstado(List<CatalogoItem> listaFiltroEstado) {
        this.listaFiltroEstado = listaFiltroEstado;
    }

    public List<ContratosReservaEjecuion> getListaAqusicionesGuardar() {
        return listaAqusicionesGuardar;
    }

    public void setListaAqusicionesGuardar(List<ContratosReservaEjecuion> listaAqusicionesGuardar) {
        this.listaAqusicionesGuardar = listaAqusicionesGuardar;
    }

    public List<DetalleSolicitudCompromiso> getListaSolicitudes() {
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<DetalleSolicitudCompromiso> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public boolean isOpcionContracto() {
        return opcionContracto;
    }

    public void setOpcionContracto(boolean opcionContracto) {
        this.opcionContracto = opcionContracto;
    }

    public boolean isListContratos() {
        return listContratos;
    }

    public void setListContratos(boolean listContratos) {
        this.listContratos = listContratos;
    }

    public SolicitudReservaCompromiso getSolicitudReserva() {
        return solicitudReserva;
    }

    public void setSolicitudReserva(SolicitudReservaCompromiso solicitudReserva) {
        this.solicitudReserva = solicitudReserva;
    }

    public DetalleSolicitudCompromiso getDetalleReserva() {
        return detalleReserva;
    }

    public void setDetalleReserva(DetalleSolicitudCompromiso detalleReserva) {
        this.detalleReserva = detalleReserva;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazyReserva() {
        return lazyReserva;
    }

    public void setLazyReserva(LazyModel<SolicitudReservaCompromiso> lazyReserva) {
        this.lazyReserva = lazyReserva;
    }
//</editor-fold>

}
