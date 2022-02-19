/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller.Reportes;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FnConvenioPagoDetallerService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ActualizarListado;
import com.gestionTributaria.models.TitulosReporteCacheLocal;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ReporteLiquidacionMulta implements Serializable {

    public static final Long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ReporteLiquidacionMulta.class.getName());

    @Inject
    private Event<ActualizarListado> eventUpdate;
    @Inject
    private ManagerService manager;
    @Inject
    private TitulosReporteCacheLocal titulosReporte;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private UsuarioService userService;
    @Inject
    private ManagerService service;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FnConvenioPagoDetallerService cv;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;

    private Date fechaDesde1;
    private Date fechaHasta1;
    protected Long tipoReporte2;

    public Long getTipoReporte2() {
        return tipoReporte2;
    }

    public void setTipoReporte2(Long tipoReporte2) {
        this.tipoReporte2 = tipoReporte2;
    }
    protected FinaRenTipoLiquidacion tipoReporteLiqui;
    private Integer tipoContribuyente;

    public Integer getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(Integer tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }
    private FinaRenEstadoLiquidacion estadoLiquidacion;
    private List<FinaRenEstadoLiquidacion> estadosLiquidaciones;
    protected List<FinaRenTipoLiquidacion> tipoReporteLiquiList;

    @PostConstruct
    public void initView() {
        try {
            tipoContribuyente = 0;
            fechaDesde1 = new Date();
            fechaHasta1 = new Date();
            tipoReporteLiquiList = new ArrayList<>();
            tipoReporteLiquiList = manager.findAllQuery("select f from FinaRenTipoLiquidacion f where f.estado=true and f.id= 86 order by f.id", null);
            estadoLiquidacion = new FinaRenEstadoLiquidacion();
            estadosLiquidaciones = new ArrayList<>();
            estadosLiquidaciones = manager.getEstadoLiquidaciones(new String[]{"pagado,por_pagar,inactivo,baja_n,baja_exoneracion"});
            tipoReporteLiqui = service.find(FinaRenTipoLiquidacion.class, 86L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReporteCajero(Boolean excel) {
        try {
            ss.instanciarParametros();
            //ss.addParametro("FECHA", sdf.format(fechaReporte));
            ss.addParametro("DESDE", fechaDesde1);
            ss.addParametro("HASTA", fechaHasta1);
            System.out.println("fecha desde es--->" + fechaDesde1);
            System.out.println("fecha hasta es--->" + fechaHasta1);
//            estado = estadoLiquidacion.getId();

//            if (this.tipoReporte2 == null) {
//                JsfUtil.addWarningMessage("Error", "Seleccione un Tipo");
//                return;
//            }
            boolean impresion = false;
            switch (this.tipoReporte2.intValue()) {
                /*LIQUIDACIONES EMITIDAS PAGADAS JC  */
                case 1:
                    System.out.println("tipoReporteLiqui.getId(---> " + tipoReporteLiqui.getId());
                    if (tipoReporteLiqui != null && tipoReporteLiqui.getId() != null) {
                        System.out.println("SI");
                        ss.addParametro("persona", tipoContribuyente);
                        ss.addParametro("fecha_desde", fechaDesde1);
                        ss.addParametro("fecha_hasta", fechaHasta1);
                        if (estadoLiquidacion != null && estadoLiquidacion.getId() != null) {
                            System.out.println("NO");
                            ss.addParametro("estado", estadoLiquidacion.getId());
                        } else {
                            System.out.println("ESTADO NO");
                            ss.addParametro("estado", 0L);
                        }
                        System.out.println("entro aqui--> ");
                        ss.setNombreReporte("permisoFuncionamientoReporte2");
                    } else {
                        System.out.println("NO");
                    }
                    break;
            }
            if (excel) {
                ss.setOnePagePerSheet(false);
                ss.setContentType("xlsx");
            }
            ss.setNombreSubCarpeta("GestionTributatia/Transacciones");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
            tipoReporteLiqui = new FinaRenTipoLiquidacion();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Date getFechaDesde1() {
        return fechaDesde1;
    }

    public void setFechaDesde1(Date fechaDesde1) {
        this.fechaDesde1 = fechaDesde1;
    }

    public Date getFechaHasta1() {
        return fechaHasta1;
    }

    public void setFechaHasta1(Date fechaHasta1) {
        this.fechaHasta1 = fechaHasta1;
    }

    public FinaRenTipoLiquidacion getTipoReporteLiqui() {
        return tipoReporteLiqui;
    }

    public void setTipoReporteLiqui(FinaRenTipoLiquidacion tipoReporteLiqui) {
        this.tipoReporteLiqui = tipoReporteLiqui;
    }

    public FinaRenEstadoLiquidacion getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(FinaRenEstadoLiquidacion estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public List<FinaRenEstadoLiquidacion> getEstadosLiquidaciones() {
        return estadosLiquidaciones;
    }

    public void setEstadosLiquidaciones(List<FinaRenEstadoLiquidacion> estadosLiquidaciones) {
        this.estadosLiquidaciones = estadosLiquidaciones;
    }

    public List<FinaRenTipoLiquidacion> getTipoReporteLiquiList() {
        return tipoReporteLiquiList;
    }

    public void setTipoReporteLiquiList(List<FinaRenTipoLiquidacion> tipoReporteLiquiList) {
        this.tipoReporteLiquiList = tipoReporteLiquiList;
    }

}
