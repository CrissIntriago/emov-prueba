/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.DetalleRecaudacion;
import com.gestionTributaria.Entities.PapeletaRecaudacion;
import com.gestionTributaria.Services.DetalleRecaudacionService;
import com.gestionTributaria.Services.PapeletaRecaudacionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class RegistroCierrerecaudacionMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(RegistroCierrerecaudacionMB.class.getName());

    @Inject
    private DetalleRecaudacionService detalleRecaudacionService;
    @Inject
    private PapeletaRecaudacionService papeletaRecaudacionService;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;

    private PapeletaRecaudacion papeletaRecaudacion;
    private DetalleRecaudacion detalleRecaudacion;
    private List<PapeletaRecaudacion> papeletaRecaudacionList;
    private List<DetalleRecaudacion> detallerecaudacionList;
    private Date fechaRegistro;

    @PostConstruct
    public void init() {
        fechaRegistro = new Date();
        papeletaRecaudacion = new PapeletaRecaudacion();
        detalleRecaudacion = new DetalleRecaudacion();
        papeletaRecaudacionList = new ArrayList<>();
        detallerecaudacionList = detalleRecaudacionService.getdetalleRecaudacion(fechaRegistro);
        papeletaRecaudacionList = papeletaRecaudacionService.getPapeletarecaudacion(fechaRegistro);
    }

    public void buscarDetallesRecaudacion() {
        detallerecaudacionList = detalleRecaudacionService.getdetalleRecaudacion(fechaRegistro);
        papeletaRecaudacionList = papeletaRecaudacionService.getPapeletarecaudacion(fechaRegistro);
    }

    public void guardar() {
        if (Utils.isNotEmpty(detallerecaudacionList)) {
            for (DetalleRecaudacion d : detallerecaudacionList) {
                if (d.getId() == null) {
                    d.setFechaRegistro(fechaRegistro);
                    detalleRecaudacionService.create(d);
                } else {
                    detalleRecaudacionService.edit(d);
                }
            }
        }
        if (Utils.isNotEmpty(papeletaRecaudacionList)) {
            for (PapeletaRecaudacion p : papeletaRecaudacionList) {
                if (p.getId() == null) {
                    p.setFecharegistro(fechaRegistro);
                    papeletaRecaudacionService.create(p);
                } else {
                    papeletaRecaudacionService.edit(p);
                }
            }
        }
        this.generarReporte();
    }

    public void generarReporte() {
        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
        ss.addParametro("FECHA", Utils.dateFormatPattern("yyyy-MM-dd",fechaRegistro));
        ss.addParametro("DESDE", Utils.dateFormatPattern("yyyy-MM-dd",fechaRegistro));
        ss.addParametro("HASTA", Utils.dateFormatPattern("yyyy-MM-dd",fechaRegistro));
        ss.addParametro("CAJA", 0L);
        ss.addParametro("USER", session.getNameUser());
        ss.addParametro("PERIODO", Utils.getAnio(new Date()));
        ss.setNombreReporte("recaudacionCajeros");
        ss.addParametro("SUBREPORT_DIR_REC", JsfUtil.getRealPath("/reportes/GestionTributatia/Recaudacion/"));
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public PapeletaRecaudacion getPapeletaRecaudacion() {
        return papeletaRecaudacion;
    }

    public void setPapeletaRecaudacion(PapeletaRecaudacion papeletaRecaudacion) {
        this.papeletaRecaudacion = papeletaRecaudacion;
    }

    public DetalleRecaudacion getDetalleRecaudacion() {
        return detalleRecaudacion;
    }

    public void setDetalleRecaudacion(DetalleRecaudacion detalleRecaudacion) {
        this.detalleRecaudacion = detalleRecaudacion;
    }

    public List<PapeletaRecaudacion> getPapeletaRecaudacionList() {
        return papeletaRecaudacionList;
    }

    public void setPapeletaRecaudacionList(List<PapeletaRecaudacion> papeletaRecaudacionList) {
        this.papeletaRecaudacionList = papeletaRecaudacionList;
    }

    public List<DetalleRecaudacion> getDetallerecaudacionList() {
        return detallerecaudacionList;
    }

    public void setDetallerecaudacionList(List<DetalleRecaudacion> detallerecaudacionList) {
        this.detallerecaudacionList = detallerecaudacionList;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
