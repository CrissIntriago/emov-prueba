/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */

@Named(value = "mejoras")
@ViewScoped
public class MejorasMB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ServletSession servletSession;
    @Inject
    private UserSession session;
    @Inject
    private ManagerService manager;

    protected Long tipoReporte = 1L;
    protected Long anioReporte;
    protected List<Long> aniosReporte;
    protected Date fechaDesde;
    protected Date fechaHasta;

    @PostConstruct
    public void initView() {
        try {
            fechaHasta = new Date();
            fechaDesde = Utils.getPrimerDiaAnio(Utils.getAnio(new Date()));
            anioReporte = Utils.getAnio(new Date()).longValue();
            aniosReporte =  manager.findAllEasy("SELECT DISTINCT o.anio FROM Obra o ORDER BY 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReporteMejoras() {
//        try {
//            servletSession.instanciarParametros();
//     // servletSession.setTieneDatasource(true);
//            servletSession.setNombreSubCarpeta("mejoras");
//            servletSession.agregarParametro("SUBREPORT_DIR", Faces.getRealPath("/reportes/").concat("/"));
//            servletSession.agregarParametro("LOGO", JsfUti.getRealPath(SisVars.sisLogo1));
//            servletSession.agregarParametro("ANIO", this.anioReporte);
//            servletSession.agregarParametro("DESDE", fechaDesde);
//            servletSession.agregarParametro("HASTA", fechaHasta);
//            servletSession.agregarParametro("USUARIO", session.getName_user());
//            switch (tipoReporte.intValue()) {
//                case 1:
//                    servletSession.setNombreReporte("recaudacionPorPeriodo");
//                    break;
//                case 2:
//                    servletSession.setNombreReporte("detalleEmisionBajasMejoras");
//                    break;
//                case 3:
//                    servletSession.setNombreReporte("saldoMejorasPorObra");
//                    break;
//                case 4:
//                    servletSession.setNombreReporte("recaudacionPorObra");
//                    break;
//                case 5:
//                    servletSession.setNombreSubCarpeta("recaudaciones");
//                    servletSession.agregarParametro("TIPO_LIQUIDACION", 13L);
//                    servletSession.setNombreReporte("saldoPorRubrosPrediales");
//                    break;
//                case 6:
//                    servletSession.setNombreSubCarpeta("recaudaciones");
//                    servletSession.agregarParametro("TIPO_LIQUIDACION", 7L);
//                    servletSession.setNombreReporte("saldoPorRubrosPrediales");
//                    break;
//                default:
//                    break;
//            }
//            JsfUti.redirectNewTab(SisVars.urlServidorPublica + "/Documento");            
//        } catch (Exception e) {
//            Logger.getLogger(Obras.class.getName()).log(Level.SEVERE, null, e);
//        }
    }

    public Long getAnioReporte() {
        return anioReporte;
    }

    public void setAnioReporte(Long anioReporte) {
        this.anioReporte = anioReporte;
    }

    public ServletSession getServletSession() {
        return servletSession;
    }

    public void setServletSession(ServletSession servletSession) {
        this.servletSession = servletSession;
    }

    public Long getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Long tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public List<Long> getAniosReporte() {
        return aniosReporte;
    }

    public void setAniosReporte(List<Long> aniosReporte) {
        this.aniosReporte = aniosReporte;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

}
