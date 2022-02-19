/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
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
@Named(value = "resolucionesComisariaMB")
@ViewScoped
public class ResolucionesComisariaMB implements Serializable {

    @Inject
    private ManagerService services;

    private LazyModel<FnResolucion> resolucionesLazy;
    private Documentos documento;

    @PostConstruct
    private void init() {
        resolucionesLazy = new LazyModel<>(FnResolucion.class);
        resolucionesLazy.setDistinct(Boolean.FALSE);
        resolucionesLazy.getFilterss().put("idTramite.tipoTramite.id", 126L);

    }

    public void viewDocumento(FnResolucion resolucion) throws ClassNotFoundException {
        try {
            documento = (Documentos) services.documentoGestionTribtaria(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName(), resolucion.getId());
            if (documento != null) {
                JsfUtil.executeJS("PF('dowloadDoc').show()");
                System.out.println("DOCUMENTO-->" + documento.getRutaDocumento());
            } else {
                JsfUtil.addWarningMessage("Debe subir el documento", "");
            }
        } catch (Exception ex) {
            System.out.println("ERROR AL MOSTRAR EL DOCUMENTOS" + ex.getMessage());
        }
    }

    public LazyModel<FnResolucion> getResolucionesLazy() {
        return resolucionesLazy;
    }

    public void setResolucionesLazy(LazyModel<FnResolucion> resolucionesLazy) {
        this.resolucionesLazy = resolucionesLazy;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

}
