/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Reportes;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Angel Navarro
 * @Date 26/07/2016
 */
@Named
@ViewScoped
public class ReportesDiarios implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReportesDiarios.class.getName());

    @Inject
    private ServletSession ss;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private UserSession session;
    
    private Map<String, Object> paramt;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    private Date fecha;
    private Date fechaHasta;
    private Integer numInforme;
    private Integer tipoReporte;
    private Integer anio;
    private Integer anioMax;
    private String oficio;

    // Variables Render
    private Boolean desde = false;
    private Boolean hasta = false;
    private Boolean informe = false;
    private Boolean oficioRender = false;
    protected Date fechaCaja = new Date();
    private List<Cajero> cajeros;
    private Long idCaja;

    @PostConstruct
    protected void initView() {
        iniciarDatos();
    }

    private void iniciarDatos() {
        fecha = new Date();
        fechaHasta = new Date();
        desde = false;
        hasta = false;
        informe = false;
        oficioRender = false;
        cajeros = cajeroService.getCajerosByActivo();
    }

    public void mostrarEtiquetas() {

        try {
            iniciarDatos();
            if (tipoReporte == 1 || tipoReporte == 3 || tipoReporte == 4 || tipoReporte == 5 || tipoReporte == 6 || tipoReporte == 8 || tipoReporte == 23
                    || tipoReporte == 9 || tipoReporte == 14 || tipoReporte == 17 || tipoReporte == 18 || tipoReporte == 22) {
                desde = true;
                hasta = true;
            } else if (tipoReporte == 2) {
                desde = true;
                informe = true;
                anio = Utils.getAnio(new Date());
                anioMax = anio;
            } else if (tipoReporte == 7) {
                desde = true;
                informe = true;
                anio = Utils.getAnio(new Date());
                anioMax = anio;
                oficioRender = true;
            } else if (tipoReporte == 12) {
                desde = true;
                hasta = true;
                oficioRender = true;
            } else if (tipoReporte == 13) {
                desde = true;
                anio = Utils.getAnio(new Date());
                anioMax = anio;
                informe = true;
                /*CATASTRO PREDIAL URBANO RESUMEN DE VALORES*/
            } else if (tipoReporte == 14) {
                desde = true;
                anio = Utils.getAnio(new Date());
                anioMax = anio;
                informe = true;

            } else if (tipoReporte == 15) {
                desde = true;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void generar(Boolean excel) {
        if (fecha == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe ingresar la fecha desde");
            return;
        }
        if (excel) {
            ss.setContentType("xlsx");
        }
        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
        ss.addParametro("FECHA", Utils.dateFormatPattern("yyyy-MM-dd",fechaHasta));
        ss.addParametro("DESDE", Utils.dateFormatPattern("yyyy-MM-dd",fecha));
        ss.addParametro("HASTA", Utils.dateFormatPattern("yyyy-MM-dd",fechaHasta));
        ss.addParametro("CAJA", idCaja);
        ss.addParametro("USER", session.getNameUser());
        ss.addParametro("PERIODO", Utils.getAnio(new Date()));
        ss.addParametro("SUBREPORT_DIR_REC", JsfUtil.getRealPath("/reportes/GestionTributatia/Recaudacion/"));
        switch (tipoReporte) {
            case 1://REPORTES POR TRANSACCION :V
                ss.setNombreReporte("recaudacionesPorTransaccion");
                ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                ss.addParametro("FECHA", sdf.format(fechaHasta));
                ss.addParametro("DESDE", sdf.format(fecha));
                ss.addParametro("id_user", session.getUserId());
                ss.addParametro("HASTA", sdf.format(fechaHasta));
                ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/GestionTributatia/Recaudacion/"));
                break;
            case 2:
                System.out.println("fecha "+Utils.dateFormatPattern("yyyy-MM-dd",fecha));
                ss.setNombreReporte("cierreCaja");
                ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                ss.addParametro("FECHA", Utils.dateFormatPattern("yyyy-MM-dd",fecha));
                ss.addParametro("USUARIO", session.getUserId());
                ss.addParametro("IDCAJA", idCaja);
                ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/GestionTributatia/Recaudacion/"));
                break;
            case 3:
                ss.setNombreReporte("Comprobante_Anulado");
                ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
                ss.addParametro("FECHA", sdf.format(fechaHasta));
                ss.addParametro("DESDE", sdf.format(fecha));
                ss.addParametro("HASTA", sdf.format(fechaHasta));
                ss.addParametro("USUARIO", session.getUserId());
                break;
            case 4:
                ss.setNombreReporte("prediosUrbano");
                break;
            case 5:
                ss.setNombreReporte("prediosRustico");
                break;
            case 6:
                ss.setNombreReporte("registroEfectivo");
                break;
            case 7:
                ss.setNombreReporte("tasasEspecies");
                break;
            case 8:
                ss.setNombreReporte("formaPago");
                break;
            case 9:
                ss.setNombreReporte("titulosRecaudados");
                break;
            case 10:
                ss.setNombreReporte("recaudacionCajeros");                
                break;
            case 11:
                ss.setNombreReporte("parteRecaudacion");
                ss.addParametro("USER", session.getUserId());
                ss.addParametro("PERIODO", Utils.getAnio(new Date()));
                break;
            case 12:
                ss.setNombreReporte("recaudacionRubro");
                ss.addParametro("USER", session.getUserId());
                ss.addParametro("PERIODO", Utils.getAnio(new Date()));
                break;
            case 13:
                ss.setNombreReporte("formaPagoRecaudador");
                break;
            case 20:
                ss.setNombreReporte("sReporteNotaCredito");
                ss.addParametro("FECHA", sdf2.format(fechaHasta));
                ss.addParametro("DESDE", sdf2.format(fecha));
                break;
            case 21:
                ss.setNombreReporte("sExoneracionesAplicadas");
                ss.addParametro("FECHA", sdf2.format(fechaHasta));
                ss.addParametro("DESDE", sdf2.format(fecha));
                
                break;
            case 22:
                ss.setNombreReporte("sReporteConvenios");
                ss.addParametro("FECHA", sdf2.format(fechaHasta));
                ss.addParametro("DESDE", sdf2.format(fecha));
                break;
            case 23:
                ss.setNombreReporte("NotaCreditoMov");
                ss.addParametro("FECHA", sdf2.format(fechaHasta));
                ss.addParametro("DESDE", sdf2.format(fecha));
                break;
            default:
                break;
        }
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Integer tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public Integer getNumInforme() {
        return numInforme;
    }

    public void setNumInforme(Integer numInforme) {
        this.numInforme = numInforme;
    }

    public Boolean getDesde() {
        return desde;
    }

    public void setDesde(Boolean desde) {
        this.desde = desde;
    }

    public Boolean getHasta() {
        return hasta;
    }

    public void setHasta(Boolean hasta) {
        this.hasta = hasta;
    }

    public Boolean getInforme() {
        return informe;
    }

    public void setInforme(Boolean informe) {
        this.informe = informe;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getAnioMax() {
        return anioMax;
    }

    public void setAnioMax(Integer anioMax) {
        this.anioMax = anioMax;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public Boolean getOficioRender() {
        return oficioRender;
    }

    public void setOficioRender(Boolean oficioRender) {
        this.oficioRender = oficioRender;
    }

    public Date getFechaCaja() {
        return fechaCaja;
    }

    public void setFechaCaja(Date fechaCaja) {
        this.fechaCaja = fechaCaja;
    }

    public List<Cajero> getCajeros() {
        return cajeros;
    }

    public void setCajeros(List<Cajero> cajeros) {
        this.cajeros = cajeros;
    }

    public Long getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Long idCaja) {
        this.idCaja = idCaja;
    }

}
