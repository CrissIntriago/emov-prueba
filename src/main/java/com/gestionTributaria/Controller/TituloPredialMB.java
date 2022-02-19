/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Services.FinaRenDetalleLiquidacionService;
import com.gestionTributaria.Services.FinaRenEstadoLiquidacionService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FnExoneracionLiquidacionService;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.FnTipoExoneracionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.RenRubroLiquidacionService;
import com.gestionTributaria.models.RubrosPorTipoLiq;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class TituloPredialMB implements Serializable {

    @Inject
    private FnExoneracionLiquidacionService exoneracionLiquidacionService;
    @Inject
    private ManagerService services;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private UserSession session;
    @Inject
    private FnTipoExoneracionService fnTipoExoneracionService;
    @Inject
    private FnSolicitudExoneracionServices fnSolicitudExoneracionServices;
    @Inject
    private FinaRenDetalleLiquidacionService finaRenDetalleLiquidacionService;
    @Inject
    private FinaRenPagoService finaRenPagoService;
    @Inject
    private FinaRenEstadoLiquidacionService estadoLiquidacionService;
    @Inject
    private RenRubroLiquidacionService renRubroLiquidacionService;
    private Map<String, Object> parametros;
    private List<FinaRenLiquidacion> liquidacionseleccionadas;
    private BigDecimal cemParquesPlazas;
    private LazyModel<FinaRenLiquidacion> liquidacionesLazy;
    private String observacion;
    private List<FnExoneracionLiquidacion> exoneraciones;
    private List<RubrosPorTipoLiq> detList1, detList2;
    private FinaRenLiquidacion original, posterior, cemLiquidacion;
    private FnExoneracionLiquidacion exoneracion;
    private List<FinaRenDetLiquidacion> detLiq;
    private List<FinaRenEstadoLiquidacion> estadoLiquidacion;

    @PostConstruct
    public void initView() {
        try {
            estadoLiquidacion = new ArrayList<>();
            estadoLiquidacion = estadoLiquidacionService.finAll();
            liquidacionseleccionadas = new ArrayList<>();
            cemParquesPlazas = BigDecimal.ZERO;
//            cemAlcantarillado = BigDecimal.ZERO;
            liquidacionesLazy = new LazyModel<>(FinaRenLiquidacion.class);
            liquidacionesLazy.addFilter("estadoLiquidacion.id:notEqual", 3L);//INACTIVO
            liquidacionesLazy.addSorted("anio", "DESC");
            liquidacionesLazy.addSorted("fechaIngreso", "DESC");
        } catch (Exception e) {
            Logger.getLogger(TituloPredialMB.class.getName()).log(Level.SEVERE, "ERROR AL INICIAR", e);
        }
    }

    public void upload(FileUploadEvent file) {
        try {
//            String archivoFD = valoresService.findByCodigo("RUTA_FIRMA_ELECTRONICA");
            String archivoFD = "D:\\servers_files\\duranArchivos";
            FilesUtil.copyFileServer(file, archivoFD);
            for (FinaRenLiquidacion item : liquidacionseleccionadas) {
                item.setNombreDocumento(file.getFile().getFileName());
                item.setDireccionDocumento(archivoFD + file.getFile().getFileName());
            }
        } catch (IOException e) {
            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo de la Firma Electrónica", "");
        }
    }

    public void openDialog() {
        if (liquidacionseleccionadas.isEmpty()) {
            JsfUtil.addInformationMessage("Información", "Debe seleccionar emisiones");
            return;
        } else {
            JsfUtil.executeJS("PF('dlgAnular').show()");
        }
    }

    public void anularLiquidacionPagoIndebido() {
        if (observacion == null || observacion.equals("")) {
            JsfUtil.addInformationMessage("Información", "Las observaciones son obligatorias");
            return;
        }
        if (liquidacionseleccionadas == null || liquidacionseleccionadas.size() > 1) {
            JsfUtil.addInformationMessage("Información", "Pago Indebido se realiza individualmente");
            return;
        }
        if (liquidacionseleccionadas.isEmpty()) {
            JsfUtil.addInformationMessage("Información", "Debe seleccionar una Emisión");
            return;
        }
        FinaRenLiquidacion l = liquidacionseleccionadas.get(0);
        if (l.getEstadoLiquidacion().getDescripcion().equals("Pagado")) {
            List<FinaRenPago> pagos = finaRenPagoService.finAllPagoIndebido(l);
            if (pagos != null && pagos.size() >= 1) {
                FinaRenPago p = pagos.get(0);
                p.setObservacion(p.getObservacion() != null ? (p.getObservacion() + " " + observacion) : observacion);
                if (p.getId() == null) {
                    finaRenPagoService.create(p);
                } else {
                    finaRenPagoService.edit(p);
                }
                l.setEstadoLiquidacion(estadoLiquidacionService.findByParameter("Por pagar"));
                List<FinaRenDetLiquidacion> detalles = finaRenDetalleLiquidacionService.findByLiquidacionOrderAsc(l);
                for (FinaRenDetLiquidacion d : detalles) {
                    d.setValorRecaudado(BigDecimal.ZERO);
                    if (d.getId() == null) {
                        finaRenDetalleLiquidacionService.create(d);
                    } else {
                        finaRenDetalleLiquidacionService.edit(d);
                    }
                }
                l.setSaldo(l.getTotalPago());
                if (l.getId() == null) {
                    finaRenLiquidacionService.create(l);
                } else {
                    finaRenLiquidacionService.edit(l);
                }
                liquidacionesLazy.getFilterss().clear();
//                liquidacionesLazy = new LazyModel<>(FinaRenLiquidacion.class);
//                liquidacionesLazy.addFilter("estadoLiquidacion:notEqual", new FinaRenEstadoLiquidacion(3L));//inactivo
//                liquidacionesLazy.addSorted("anio", "DESC");
//                liquidacionesLazy.addSorted("fechaIngreso", "DESC");
                JsfUtil.update("mainForm:dtPagos");
            } else {
                JsfUtil.addInformationMessage("Información", "Proceden solo Pagos Totales");
            }
        } else {
            JsfUtil.addInformationMessage("Información", "Proceden solo las Emisiones pagadas");
        }
    }

    public Boolean bajaAnioActual() {
        System.out.println("SUPER USUARIOS" + session.getEsSuperUser());
        if (session.getEsSuperUser()) {
            return false;
        } else {
            if (session.getDepts().contains(2L)) {
                if (session.getRoles().contains(66L)) {
                    return true;
                } else if (session.getRoles().contains(68L)) {
                    return true;
                } else if (session.getRoles().contains(174L)) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public void anularLiquidacion() {
        if (observacion == null || observacion.equals("")) {
            JsfUtil.addErrorMessage("Información", "Las observaciones son obligatorias");
            return;
        }
        FinaRenTipoLiquidacion tipoLiq = null;
        System.out.println("liquidaciones tamaño" + liquidacionseleccionadas.size());
        for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
            if (tipoLiq == null) {
                tipoLiq = rl.getTipoLiquidacion();
                System.out.println("EL TIPO DE LIQUIDACION" + rl.getTipoLiquidacion().getNombreTransaccion() + "id" + rl.getTipoLiquidacion().getId());
            } else {
                if (tipoLiq != null) {
                    if (!tipoLiq.equals(rl.getTipoLiquidacion())) {
                        JsfUtil.addErrorMessage("Error", "Solo Puede dar de Baja a Emisiones Del mismo tipo");
                        return;
                    }
                }
            }
            //revisar
//            if (bajaAnioActual()) {
//                if (rl.getAnio() < Utils.getAnio(new Date())) {
//                    JsfUtil.addErrorMessage("Información", "Solo Puede dar de Baja a Emisiones del Año Actual");
//                    return;
//                }
//
//            }
        }

        Boolean prediosDiferentes = Boolean.FALSE;
        ///SE ORDERNA LA ,LISTA PARA OBTENER EL MENOR DE LOS ANIOS
        Collections.sort(liquidacionseleccionadas, (FinaRenLiquidacion rl1, FinaRenLiquidacion rl2)
                -> new Integer(rl1.getAnio())
                        .compareTo(rl2.getAnio()));
        CatPredio predio = null;
        //pago predio urbano y rustico
//        if (tipoLiq.getId().equals(new Long(3)) || tipoLiq.getId().equals(new Long(2))) {
//            List<FinaRenLiquidacion> liquidacionesTemp = new ArrayList();
//            List<CatPredio> prediosUrbanos = new ArrayList();
//            FnSolicitudExoneracionPredios exoneracionPredios = null;
//            List<FnSolicitudExoneracionPredios> prediosEnSolicitud = new ArrayList<>();
//            for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
//                exoneracionPredios = new FnSolicitudExoneracionPredios();
//                exoneracionPredios.setPredio(rl.getPredio());
//                prediosUrbanos.add(rl.getPredio());
//                if (!prediosUrbanos.isEmpty()) {
//                    for (CatPredio cp : prediosUrbanos) {
//                        if (!rl.getPredio().getId().equals(cp.getId())) {
//                            prediosDiferentes = Boolean.TRUE;
//                            break;
//                        }
//                    }
//                }
//                rl.setObservacion(observacion);
//                prediosEnSolicitud.add(exoneracionPredios);
//                liquidacionesTemp.add(rl);
//            }
//            if (prediosDiferentes) {
//                JsfUtil.addErrorMessage("Información", "Las anulaciones deben de ser un mismo predio");
//                return;
//
//            }
//            //baja de titulo 34L
//            FnExoneracionTipo tipo = fnTipoExoneracionService.findById(34L);
//            List<FinaRenPago> pagos;
//            if (!prediosUrbanos.isEmpty()) {
//                predio = prediosUrbanos.get(0);
//                if (tipo.getId() != 40L) {
//                    for (CatPredio pu : prediosUrbanos) {
//                        pagos = finaRenPagoService.getPagosByPredioTipoLiquidacionAnio(pu.getId(), null, new FinaRenTipoLiquidacion(13L),
//                                liquidacionseleccionadas.get(0).getAnio(), liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getAnio());
//                        if (pagos != null && !pagos.isEmpty()) {
//                            JsfUtil.addInformationMessage("Información", "Solo procede las Emisiones Pendientes de Pago");
//                            return;
//                        }
//                    }
//                } else {
//                    for (CatPredio pu : prediosUrbanos) {
//                        pagos = finaRenPagoService.getPagosByPredioTipoLiquidacionAnioPagada(pu.getId(), null, new FinaRenTipoLiquidacion(13L),
//                                liquidacionseleccionadas.get(0).getAnio(), liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getAnio());
//                        if (pagos != null && !pagos.isEmpty()) {
//                            JsfUtil.addInformationMessage("Información", "Solo procede las Emisiones Pendientes de Pago");
//                            return;
//                        }
//                    }
//                }
//            }
//            for (FinaRenLiquidacion r : liquidacionesTemp) {
//                if (r.getId() != null) {
//                    finaRenLiquidacionService.edit(r);
//                } else {
//                    finaRenLiquidacionService.create(r);
//                }
//            }
//            FnSolicitudExoneracion solicitud = new FnSolicitudExoneracion();
//            solicitud.setExoneracionTipo(tipo);
//            solicitud.setFechaIngreso(new Date());
//            solicitud.setUsuarioCreacion(session.getNameUser());
//            solicitud.setSolicitante(liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getComprador());
//            solicitud.setPredio(liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getPredio());
//            solicitud.setAnioInicio(liquidacionseleccionadas.get(0).getAnio());
//            solicitud.setAnioFin(liquidacionseleccionadas.get(liquidacionseleccionadas.size() - 1).getAnio());
//            System.out.println("solicitud " + solicitud.getAnioInicio() + "FIN " + solicitud.getAnioFin());
//            solicitud.setEstado(new FnEstadoExoneracion(2L));// en proceso
//            solicitud = (FnSolicitudExoneracion) fnSolicitudExoneracionServices.create(solicitud);
//            fnSolicitudExoneracionServices.registarDatoSolicitudExoneracion(solicitud, null, prediosEnSolicitud);
//            //revisar la apliacion de exoneracion
//            exoneraciones = services.aplicarExoneracion(null, solicitud, session.getNameUser());
//
//            if (exoneraciones != null && !exoneraciones.isEmpty()) {
//                detList1 = new ArrayList();
//                detList2 = new ArrayList();
//                FinaRenRubrosLiquidacion rubro = new FinaRenRubrosLiquidacion();
//                exoneracion = exoneraciones.get(0);
//                if (exoneracion.getLiquidacionOriginal() != null) {
//                    original = exoneracion.getLiquidacionOriginal();
//                    detLiq = finaRenDetalleLiquidacionService.findAllTipoLiquidacion(original);
//                    for (FinaRenDetLiquidacion temp : detLiq) {
//                        if (temp.getRubro().getId() != null) {
//                            rubro = renRubroLiquidacionService.create(temp.getRubro());
//                        } else {
//                            renRubroLiquidacionService.edit(temp.getRubro());
//                        }
//                        detList1.add(new RubrosPorTipoLiq(rubro, temp.getValor()));
//                    }
//                }
//                if (exoneracion.getLiquidacionPosterior() != null) {
//                    posterior = exoneracion.getLiquidacionPosterior();
//                    posterior.setTotalPago(posterior.getTotalPago().setScale(2, RoundingMode.HALF_UP));
//                    posterior.setUsuarioIngreso(session.getNameUser());
//                    if (posterior.getId() != null) {
//                        posterior = finaRenLiquidacionService.create(posterior);
//                    } else {
//                        finaRenLiquidacionService.edit(posterior);
//                    }
//                    detLiq = finaRenDetalleLiquidacionService.findAllTipoLiquidacion(posterior);
//                    for (FinaRenDetLiquidacion temp2 : detLiq) {
//                        if (temp2.getRubro().getId() != null) {
//                            rubro = renRubroLiquidacionService.create(temp2.getRubro());
//                        } else {
//                            renRubroLiquidacionService.edit(temp2.getRubro());
//                        }
//                        detList2.add(new RubrosPorTipoLiq(rubro, temp2.getValor()));
//                    }
//                }
////            JsfUti.update("frmExoAp");
//                JsfUtil.executeJS("PF('dlgAnular').hide()");
//                JsfUtil.addInformationMessage("Info", "Exoneracion aplicada correctamente");
//                exoneraciones = new ArrayList();
//                exoneracion = new FnExoneracionLiquidacion();
//                solicitud = new FnSolicitudExoneracion();
//                posterior = new FinaRenLiquidacion();
//                original = new FinaRenLiquidacion();
//
//            } else {
//                JsfUtil.addInformationMessage("Info", "Hubo un problema al aplicar la exoneracion, Revise las emisiones realizadas con anterioridad");
//            }
//        } else {
        for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
            if (!rl.getEstadoLiquidacion().equals(new FinaRenEstadoLiquidacion(2L))) {//estado por pagar
                JsfUtil.addInformationMessage("Advertencia", "Solo se permite dar de baja a liquidacion pendientes de pago.");
                return;
            }
            List<FinaRenPago> pagos = finaRenPagoService.obtenerPagos(rl, true);
            if (Utils.isNotEmpty(pagos)) {
                JsfUtil.addInformationMessage("Advertencia", "Una de las liquidaciones seleccionadas tiene pagos realizaizados.");
                return;
            }
        }
        for (FinaRenLiquidacion rl : liquidacionseleccionadas) {
            rl.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(4L));//dado de baja
            rl.setObservacion(observacion);
            finaRenLiquidacionService.edit(rl);
            List<FinaRenDetLiquidacion> detalle = finaRenDetalleLiquidacionService.findByidLiquidacion(rl);
            if (detalle != null) {
                for (FinaRenDetLiquidacion det : detalle) {
                    det.setValor(BigDecimal.ZERO);
                }
            }
            List<FnExoneracionLiquidacion> exoneraciones = exoneracionLiquidacionService.findByLiquidacion(rl);
            if (exoneraciones != null) {
                for (FnExoneracionLiquidacion item : exoneraciones) {
                    item.setEstado(false);
                    exoneracionLiquidacionService.edit(item);
                }
            }
        }
        JsfUtil.executeJS("PF('dlgAnular').hide()");
        JsfUtil.addInformationMessage("Info", "Baja de titulo realizado correctamente");
//        }
        JsfUtil.update("mainForm:dtPagos");

    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public List<FinaRenLiquidacion> getLiquidacionseleccionadas() {
        return liquidacionseleccionadas;
    }

    public void setLiquidacionseleccionadas(List<FinaRenLiquidacion> liquidacionseleccionadas) {
        this.liquidacionseleccionadas = liquidacionseleccionadas;
    }

    public List<FinaRenEstadoLiquidacion> getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(List<FinaRenEstadoLiquidacion> estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public BigDecimal getCemParquesPlazas() {
        return cemParquesPlazas;
    }

    public void setCemParquesPlazas(BigDecimal cemParquesPlazas) {
        this.cemParquesPlazas = cemParquesPlazas;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidacionesLazy() {
        return liquidacionesLazy;
    }

    public void setLiquidacionesLazy(LazyModel<FinaRenLiquidacion> liquidacionesLazy) {
        this.liquidacionesLazy = liquidacionesLazy;
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
//</editor-fold>

}
