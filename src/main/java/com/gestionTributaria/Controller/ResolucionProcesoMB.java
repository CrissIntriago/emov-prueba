/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.ResolucionesService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class ResolucionProcesoMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ResolucionesService resolucionesService;
    @Inject
    private ManagerService services;
    @Inject
    private UserSession uSession;
    private static final Logger LOG = Logger.getLogger(CoactivaProcesoMB.class.getName());
    private FnResolucion resolucion;
    private Documentos documento;
    private List<Documentos> listaDocumentos;
    private LazyModel<FnResolucion> listaResoluciones;
    private LazyModel<CoaJuicio> lazyJuicos;
    private Boolean abrirModal = false;
    private String observaciones = "";
    private List<FnResolucion> resolucionRegistrada;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            observacion = new Observaciones();
            listaResoluciones = new LazyModel<>(FnResolucion.class);
            resolucion = new FnResolucion();
            listaDocumentos = new ArrayList<>();
            lazyJuicos = new LazyModel<>(CoaJuicio.class);
            lazyJuicos.getSorteds().put("anioJuicio", "DESC");
            documento = new Documentos();
            this.setTaskId(this.session.getTaskID());
            if (resolucionesService.findByTramite(tramite).size() > 0) {
                resolucionRegistrada = resolucionesService.findByTramite(tramite);
            } else {
                resolucionRegistrada = new ArrayList<>();
            }
        }
    }

    public void handleFileDocumentBySave(FileUploadEvent event) throws ClassNotFoundException {
        try {
            Boolean bandera = false;
            if (resolucion.getId() != null) {
                documento = new Documentos();
                String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                        + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
                documento.setDepartamento(uSession.getDepartamento());
                documento.setRutaDocumento(ruta);
                documento.setFechaCreacion(new Date());
                documento.setNombre(event.getFile().getFileName());
                documento.setDescripcion(event.getFile().getContentType());
                documento.setEstado(Boolean.TRUE);
                documento.setClaseNombre(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName());
                documento.setIdentificador(resolucion.getId());
                listaDocumentos.add(documento);
                if (listaDocumentos.size() <= 1) {
                    Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
                    for (Documentos doc : listaDocumentos) {
                        documento = (Documentos) services.save(doc);
                    }
                    bandera = true;
                } else {
                    documento = new Documentos();
                    listaDocumentos.remove(listaDocumentos.size() - 1);
                    JsfUtil.addInformationMessage("Solo puede subir un documento", "");
                }
                if (bandera == true) {
                    JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
                }
                JsfUtil.update("mainForm:dtResoluciones");
            } else {
                JsfUtil.addWarningMessage("Debe registrar la resolucion antes de subir el documento", "");
            }

        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subir el documento");
        }
    }

    public void viewDocumento(FnResolucion resolucion) throws ClassNotFoundException {
        try {
            System.out.println("LA RESOLUCION" + resolucion);
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

    public void limpiar() {
        resolucion = new FnResolucion();
        documento = new Documentos();
        listaDocumentos.clear();

    }

    public void saveResolucion() {
        try {
            if (resolucion.getId() != null) {
                resolucionesService.edit(resolucion);

            } else {
                resolucion.setEnte(uSession.getUsuario().getEnte());
//                resolucion.setEstado(Boolean.TRUE);
                resolucion.setFechaIngreso(new Date());
                resolucion.setUsuarioCreacion(uSession.getUsuario().getNameUsuario());
//                resolucion.setTramite(tramite);
                resolucion = (FnResolucion) resolucionesService.create(resolucion);
                resolucionRegistrada.add(resolucion);
                JsfUtil.update("dtResoluciones");
                JsfUtil.addInformationMessage("INFO", "La resolucion se creó con éxito");
            }
            resolucion = new FnResolucion();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error", "Ocurrió un error al crear la resolución" + ex.getMessage());
        }
    }

    public void inactivarDocumento(Documentos doc, int index) {
        doc = listaDocumentos.get(index);
        if (doc.getId() != null) {
            doc.setEstado(Boolean.FALSE);
            services.update(doc);
        }
        listaDocumentos.remove(index);
        JsfUtil.addInformationMessage("", "El documento se inactivo con exito");
    }

    public void editar(FnResolucion resolucion) {
        this.resolucion = resolucion;
        this.listaDocumentos = ((List<Documentos>) services.documentoGestionTribtariaActivos(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName(), resolucion.getId()));
        if (this.listaDocumentos == null) {
            this.listaDocumentos = new ArrayList<>();
        }
    }

    public void adjuntarProcesoCoactivo(CoaJuicio juicio) {
//        resolucion.setJuico(juicio);
        JsfUtil.update("mainForm:procesoSeleccionado");
    }

    public void continuarTarea(String key) {
        try {
            observacion.setObservacion(observaciones);
            System.out.println("ESTO LA OBSERVACION: " + observacion.getObservacion());
            if (!observacion.getObservacion().isEmpty()) {
                if (key.equals("REVISION")) {
                    this.getParamts().put("abogadoResolucion", session.getNameUser());
                }
                if (key.equals("APROBADO")) {
                    this.getParamts().put("abogadoResolucion", session.getNameUser());
                    this.getParamts().put("opcion", 1);
                }
                if (key.equals("RECHAZADO")) {
                    this.getParamts().put("abogadoResolucion", session.getNameUser());
                    this.getParamts().put("opcion", 0);
                }
                //aqui terminan las condiciones
                if (saveTramite() == null) {
                    return;
                }
                this.session.setVarTemp(null);
                super.completeTask((HashMap<String, Object>) getParamts());
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            } else {
                JsfUtil.addWarningMessage("La Observación es necesaria", "");
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "completas tareas", ex);
        }

    }

    public FnResolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(FnResolucion resolucion) {
        this.resolucion = resolucion;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public LazyModel<FnResolucion> getListaResoluciones() {
        return listaResoluciones;
    }

    public void setListaResoluciones(LazyModel<FnResolucion> listaResoluciones) {
        this.listaResoluciones = listaResoluciones;
    }

    public LazyModel<CoaJuicio> getLazyJuicos() {
        return lazyJuicos;
    }

    public void setLazyJuicos(LazyModel<CoaJuicio> lazyJuicos) {
        this.lazyJuicos = lazyJuicos;
    }

    public Boolean getAbrirModal() {
        return abrirModal;
    }

    public void setAbrirModal(Boolean abrirModal) {
        this.abrirModal = abrirModal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<FnResolucion> getResolucionRegistrada() {
        return resolucionRegistrada;
    }

    public void setResolucionRegistrada(List<FnResolucion> resolucionRegistrada) {
        this.resolucionRegistrada = resolucionRegistrada;
    }

}
