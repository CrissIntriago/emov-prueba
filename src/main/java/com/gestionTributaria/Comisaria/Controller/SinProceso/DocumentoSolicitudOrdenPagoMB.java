/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller.SinProceso;

import com.gestionTributaria.Comisaria.Entities.DocSolicitudOrdenPago;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "documentoSolicitudOrdenPagoMB")
@ViewScoped
public class DocumentoSolicitudOrdenPagoMB implements Serializable {

    @Inject
    private ServletSession ss;

    private DocSolicitudOrdenPago documentoSolicitud;
    private LazyModel<DocSolicitudOrdenPago> lazy;

    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(DocSolicitudOrdenPago.class);
        lazy.getFilterss().put("estado", Boolean.TRUE);
        //
    }

    public void form() {
    }

    public void guardar() {
    }

    public void generateReport(DocSolicitudOrdenPago d) {
        ss.setNombreSubCarpeta("GestionTributatia/comisaria");
        ss.setNombreReporte("documentoTasaHabilitacioPatente");
//        ss.setDataSource(reporteNew);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

    }

    public DocSolicitudOrdenPago getDocumentoSolicitud() {
        return documentoSolicitud;
    }

    public void setDocumentoSolicitud(DocSolicitudOrdenPago documentoSolicitud) {
        this.documentoSolicitud = documentoSolicitud;
    }

    public LazyModel<DocSolicitudOrdenPago> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<DocSolicitudOrdenPago> lazy) {
        this.lazy = lazy;
    }

}
