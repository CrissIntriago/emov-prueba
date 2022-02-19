/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 *
 * @author Administrator
 */
public class ReportGenealModel implements Serializable {

    @Inject
    private ServletSession viewReport;
    @Inject
    private ManagerService services;

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

//    private BigDecimal interes(FinaRenLiquidacion l) {
//        Boolean aplicaRemision = services.aplicaRemision(l);
//        BigDecimal interes = BigDecimal.ZERO;
//        System.out.println("aplicaRemision " + aplicaRemision);
//        interes = interes2(l, aplicaRemision);
//        
//        l.setInteres(interes);
//        return l.getInteres();
//    }
//    
//    private BigDecimal interes2(FinaRenLiquidacion l, Boolean aplicaRemision) {
//        if (!aplicaRemision) {
//            l.setInteres(services.interesCalculado(l, new Date()));
//        } else {
//            l.setInteres(BigDecimal.ZERO);
//        }
//        
//        return l.getInteres();
//    }
    public void imprimirCertificadoPlanificacion(FinaRenLiquidacion l) {

        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirPermiso(FinaRenLiquidacion l) {
//        if (l.getEstadoLiquidacion().getId().equals(1L)) {
//            String firma = "";
//            //  String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarParametros();
//            ss.instanciarParametros();
//            ss.setNombreSubCarpeta("RentasMontecristi/Liquidaciones");
//            //    ss.setTieneDatasource(Boolean.TRUE);
//            //         ss.addParametro("LOGO", path + SisVars.sisLogo);
//            ///DIRECTOR DE GESTION AMBIENTAL
//            Rol catRol = (Rol) acl.find(Querys.getAclRolByDepaartamentoDirector, new String[]{"departamento"},
//                    new Object[]{47L});
//            if (catRol != null) {
//                for (AclUser dir : catRol.getAclUserCollection()) {
//                    System.out.println("dir.getUsuario(); " + dir.getUsuario());
//                    firma = dir.getUsuario();
//                    break;
//                }
//            }
//            System.out.println("firma " + firma);
//            ss.addParametro("FIRMA", path + SisVars.firmaUsuario + firma + ".png");
//            ss.addParametro("Fondo", path + SisVars.sisMarcaAgua);
//            ss.addParametro("ID", l.getCatPlanificacionTitulos().getId());
//            ss.setNombreReporte("sPermisoMunicipal");
//            JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//        } else {
//            JsfUtil.addErrorMessage("PERMISO TIENE DEUDAS PENDIENTE", null);
//        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirReporteTramites() {
//        try {
//            String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarDatos();
//            ss.instanciarParametros();
//            ss.setTieneDatasource(Boolean.TRUE);
//            ss.setNombreSubCarpeta("planificacion/certificados");
//            if (usuario == null) {
//                ss.addParametro("USUARIO", 0L);
//            } else {
//                ss.addParametro("USUARIO", usuario.getId());
//            }
//            ss.addParametro("DEPARTAMENTO", "%DEP. " + departamento.getNombre() + "%");
//            ss.addParametro("desde", desde);
//            ss.addParametro("hasta", hasta);
//            ss.addParametro("LOGO", JsfUtil.getRealPath("/").concat(SisVars.sisLogo1));
//            ss.addParametro("LOGO2", path + SisVars.sisLogo);
//            ss.setNombreReporte("reporte_tareas");
//            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
//            JsfUtil.redirectNewTab("/sgmEE/Documento");
//
//        } catch (Exception e) {
//            Logger.getLogger(GeneracionAvaluos.class
//                    .getName()).log(Level.SEVERE, null, e);
//        }

        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirPermisoTurismo(FinaRenLiquidacion l) {
//
//        if (l.getEstadoLiquidacion().getId().equals(1L)) {
//            String firma = "";
//            String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarParametros();
//            ss.instanciarParametros();
//            ss.setNombreSubCarpeta("RentasMontecristi/LiquidacionTurismo");
//            ss.setTieneDatasource(Boolean.TRUE);
//            ss.addParametro("Permiso", path + SisVars.sisPermisoFuncionamiento);
//            ss.addParametro("ID", l.getLocalComercial().getId());
//            ss.setNombreReporte("sComprobantePermisoTurismo");
//            JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//        } else {
//            JsfUtil.addErrorMessage("PERMISO TIENE DEUDAS PENDIENTE", null);
//        }
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }
}
