/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Entidad.Controller;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Criss Intriago
 */
@Named(value = "datosGeneralesView")
@ViewScoped
public class DatosGeneralesEntidadController implements Serializable {

    /*Inject Services*/
    @Inject
    private DatosGeneralesEntidadService datosGeneralesEntidadService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    /*Listas*/
    private List<Provincia> provincias;
    private List<Canton> cantones;
    private String abreviaturaTemp;
    /*Objetos*/
    private DatosGeneralesEntidad datosGeneralesEntidad;

    /*Lazy Model*/
    private LazyModel<DatosGeneralesEntidad> datoGeneralesLazyModel;

    /*Variables Auyxiliares*/
    private Long registroUnico;

    /*Contructor inicializado*/
    @PostConstruct
    public void initialize() {
        this.datosGeneralesEntidad = new DatosGeneralesEntidad();
        this.provincias = datosGeneralesEntidadService.getProvincias();
        this.datoGeneralesLazyModel = new LazyModel<>(DatosGeneralesEntidad.class);
        this.datoGeneralesLazyModel.getSorteds().put("id", "ASC");
        this.datoGeneralesLazyModel.getFilterss().put("estado", true);
        this.registroUnico = datosGeneralesEntidadService.getRegistroUnico();
    }

    /*Carga el formulario o vista*/
    public void form(DatosGeneralesEntidad datosGeneralesEntidad) {
        if (datosGeneralesEntidad != null) {
            /*Carga los datos al formulario para editarlo*/
            this.datosGeneralesEntidad = datosGeneralesEntidad;
            if (datosGeneralesEntidad.getProvincia() != null) {
                this.actualizarCantones();
            }
            ss.setNombreDocumento(this.datosGeneralesEntidad.getUrlLogoReporte());
            ss.setContentType("image/png");
            abreviaturaTemp = datosGeneralesEntidad.getAbreviatura();
        } else {
            /*Carga nuevo formulario vacio*/
            this.datosGeneralesEntidad = new DatosGeneralesEntidad();
        }
        PrimeFaces.current().executeScript("PF('datosGeneralesDialog').show()");
        PrimeFaces.current().ajax().update(":formDatosGenerales");
    }

    /*Guardar al crear uno nuevo o al editarlo*/
    public void save() {
        boolean edit = datosGeneralesEntidad.getId() != null;
        if (edit) {
            datosGeneralesEntidadService.edit(datosGeneralesEntidad);
        } else {
            datosGeneralesEntidad = datosGeneralesEntidadService.create(datosGeneralesEntidad);
        }
        registroUnico = datosGeneralesEntidadService.getRegistroUnico();
        PrimeFaces.current().executeScript("PF('datosGeneralesDialog').hide()");
        JsfUtil.addSuccessMessage("Datos Generales", (edit ? " editado" : " registrado") + " con éxito.");
    }

    /*Visualizar Datos Generales*/
    public void visualizarDatos(DatosGeneralesEntidad datosGeneralesEntidad) {
        this.datosGeneralesEntidad = datosGeneralesEntidad;
        ss.setNombreDocumento(this.datosGeneralesEntidad.getUrlLogoReporte());
        ss.setContentType("image/png");
        //actualizarCantones();
        PrimeFaces.current().executeScript("PF('viewDatosGeneralesDialog').show()");
        PrimeFaces.current().ajax().update(":formDatosGeneralesView");
    }

    /*Actualizar Cantones dependiendo de la provincia seleccionada*/
    public void actualizarCantones() {
        this.cantones = datosGeneralesEntidadService.getCantones(datosGeneralesEntidad.getProvincia());
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (datosGeneralesEntidad.getAbreviatura() == null) {
            JsfUtil.addErrorMessage("Abreviatura", "Debe ingresar la abreviatura primero.");
            return;
        }
        try {
            String findByCodigo = valoresService.findByCodigo("REPOSITORIO_ARCHIVO");
            if (!findByCodigo.endsWith("/") || !findByCodigo.endsWith("\\")) {
                findByCodigo += "/";
            }
            String rutaFile = findByCodigo + datosGeneralesEntidad.getAbreviatura() + "/";
            File file = FilesUtil.copyFileServer1(event, rutaFile);
            if (file != null) {
                datosGeneralesEntidad.setUrlLogoReporte(file.getAbsolutePath().replaceAll("([A-Z]:\\\\)|(\\\\)", "/"));
                ss.setNombreDocumento(this.datosGeneralesEntidad.getUrlLogoReporte());
                ss.setContentType("image/png");
                JsfUtil.update("logoRepo");
            }
        } catch (IOException ex) {
            Logger.getLogger(DatosGeneralesEntidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUploadMarca(FileUploadEvent event) {
        if (datosGeneralesEntidad.getAbreviatura() == null) {
            JsfUtil.addErrorMessage("Abreviatura", "Debe ingresar la abreviatura primero.");
            return;
        }
        try {
            String findByCodigo = valoresService.findByCodigo("REPOSITORIO_ARCHIVO");
            if (!findByCodigo.endsWith("/") || !findByCodigo.endsWith("\\")) {
                findByCodigo += "/";
            }
            String rutaFile = findByCodigo + datosGeneralesEntidad.getAbreviatura() + "/";
            File file = FilesUtil.copyFileServer1(event, rutaFile);
            if (file != null) {
                datosGeneralesEntidad.setUrlMarcaAgua(file.getAbsolutePath().replaceAll("([A-Z]:\\\\)|(\\\\)", "/"));
                ss.setNombreDocumento(this.datosGeneralesEntidad.getUrlLogoReporte());
                ss.setContentType("image/png");
                JsfUtil.update("logoRepo");
            }
        } catch (IOException ex) {
            Logger.getLogger(DatosGeneralesEntidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get - Set">
    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public DatosGeneralesEntidad getDatosGeneralesEntidad() {
        return datosGeneralesEntidad;
    }

    public void setDatosGeneralesEntidad(DatosGeneralesEntidad datosGeneralesEntidad) {
        this.datosGeneralesEntidad = datosGeneralesEntidad;
    }

    public LazyModel<DatosGeneralesEntidad> getDatoGeneralesLazyModel() {
        return datoGeneralesLazyModel;
    }

    public void setDatoGeneralesLazyModel(LazyModel<DatosGeneralesEntidad> datoGeneralesLazyModel) {
        this.datoGeneralesLazyModel = datoGeneralesLazyModel;
    }

    public Long getRegistroUnico() {
        return registroUnico;
    }

    public void setRegistroUnico(Long registroUnico) {
        this.registroUnico = registroUnico;
    }
//</editor-fold>

    public String getAbreviaturaTemp() {
        return abreviaturaTemp;
    }

    public void setAbreviaturaTemp(String abreviaturaTemp) {
        this.abreviaturaTemp = abreviaturaTemp;
    }

}
