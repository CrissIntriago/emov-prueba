/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller.ViaPublica;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.SolicitudServicios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Admin
 */
@Named
@ViewScoped
public class AprobacionRentasMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ManagerService services;
    @Inject
    private ServletSession viewReport;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private ComisariaServices comisariaServices;
    private LazyModel<FinaRenLiquidacion> titulosCredito;
    private FinaRenLiquidacion liquidacion;
  
    private boolean aprobacion;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                liquidacion = new FinaRenLiquidacion();
                titulosCredito = new LazyModel<>(FinaRenLiquidacion.class);
                titulosCredito.getFilterss().put("numTramite", tramite.getNumTramite());
              
                aprobacion = true;

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public BigDecimal interesReporte(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = services.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public void imprimirDistribuccion(FinaRenLiquidacion liq) {
        viewReport.borrarParametros();
        viewReport.instanciarParametros();
        viewReport.addParametro("id", liq.getId());
        viewReport.addParametro("descuento", liq.getDescuento());
        viewReport.addParametro("interes", interesReporte(liq));
        viewReport.addParametro("valor_exonerado", liq.getValorExoneracion());
        viewReport.addParametro("total", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.addParametro("recargo", liq.getRecargo());
        viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
        viewReport.addParametro("pagon_final", liq.getTotalPago().add(interesReporte(liq)));
        viewReport.setNombreReporte("distribucionActivoTotales");
        viewReport.setNombreSubCarpeta("GestionTributatia/general");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimir(FinaRenLiquidacion liq) {
        System.out.println("liq " + liq.getId());

        interesReporte(liq);
        liq.setPagoFinal(liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));

        if (liq.getListDistribuciionCantones() != null && liq.getListDistribuciionCantones().size() > 0) {
            imprimirDistribuccion(liq);
        } else {

            viewReport.borrarParametros();
            viewReport.instanciarParametros();
            viewReport.addParametro("id", liq.getId());
            viewReport.addParametro("descuento", liq.getDescuento());
            viewReport.addParametro("interes", liq.getInteres());
            viewReport.addParametro("valor_exonerado", liq.getExoneracionSumValor());
            viewReport.addParametro("total", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.addParametro("recargo", liq.getRecargo());
            viewReport.addParametro("valor_coactiva", liq.getValorCoactiva());
            viewReport.addParametro("pagon_final", liq.getTotalPago().add(liq.getRecargo()).add(liq.getInteres()));
            viewReport.setNombreReporte("general");
            viewReport.setNombreSubCarpeta("GestionTributatia/general");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void validarLiquidacion(FinaRenLiquidacion liq) {
        liq.setValidada(Boolean.TRUE);
        liq.setUsuarioValida(session.getNameUser());
        liq.setLiquidadorAprobador(clienteService.getUsuarioNombre(session.getNameUser()));
        liquidacionService.edit(liq);
        JsfUtil.addInformationMessage("Liquidacion " + liq.getIdLiquidacion() + " validada con exito.", "");
    }

    public void denegarPreliquidacion(FinaRenLiquidacion f) {
        f.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
        liquidacionService.edit(f);
        JsfUtil.addSuccessMessage("", "La Preliquidación se inactivo correctamente");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));

            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void send(boolean index, FinaRenLiquidacion titulo) {
        liquidacion = new FinaRenLiquidacion();
        liquidacion = titulo;
        aprobacion = index;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
    }

    public void completarTarea() {

        try {

            int index = aprobacion ? 1 : 0;
            getParamts().put("preliquidacion", index);
            if (index == 1) {
                validarLiquidacion(liquidacion);
                String usuario = clienteService.getrolsUser(RolUsuario.tesorero);
                getParamts().put("recuadaciones", usuario.equals("") ? session.getNameUser() : usuario);
            } else {
                denegarPreliquidacion(liquidacion);
                this.enviarCorreo();
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

  
    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public LazyModel<FinaRenLiquidacion> getTitulosCredito() {
        return titulosCredito;
    }

    public void setTitulosCredito(LazyModel<FinaRenLiquidacion> titulosCredito) {
        this.titulosCredito = titulosCredito;
    }

   

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
//</editor-fold>

}
