/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.doc.DocumentoBlob;
import com.origami.sigef.common.models.Documento;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class DocumentsView implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private FileUploadDoc uploadDoc;
    private List<Documento> files;
    private Object obj;

    /**
     * Busca los archivos asociados a esa entidad
     *
     * @param obj Entiti a buscar archivos
     * @return Listado de archivos asociados a la entidad
     */
    public List<Documento> files(Object obj) {
        if (obj == null) {
            return null;
        }
        Object id = ReflexionEntity.getIdFromEntity(obj);
        if (id == null) {
            return null;
        }
        files = uploadDoc.fileEntiti(obj);
        return files;
    }

    /**
     * Muestra el archivo en otra pestaña
     *
     * @param doc Archivo a visualizar
     */
    public void viewFile(Documento doc) {
        ss.addParametro("ContentType", doc.getExtension());
        ss.addParametro("oid", doc.getBlobData());
        ss.setNombreDocumento(doc.getArchivoNombre());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "FileDoc");
    }

    /**
     * Descarga el archivo
     *
     * @param doc Archivo a descargar
     */
    public void downloadFile(Documento doc) {
        ss.addParametro("ContentType", doc.getExtension());
        ss.addParametro("oid", doc.getBlobData());
        ss.addParametro("download", true);
        ss.setNombreDocumento(doc.getArchivoNombre());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "FileDoc");
    }

    /**
     * Elimina el archivo
     *
     * @param doc Archivo a eliminar
     */
    public void deleteFile(Documento doc) {
        try {
            this.uploadDoc.eliminar(doc);
            JsfUtil.addInformationMessage(doc.getArchivoNombre(), "Archivo eliminado correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(doc.getArchivoNombre(), "Error el eliminar archivo.");
            Logger.getLogger(DocumentsView.class.getName()).log(Level.SEVERE, "deleteFile", e);
        }
    }

    public String getSize(Long size) {
        try {
            return Utils.humanReadableByteCountSI(size);
        } catch (Exception e) {
            Logger.getLogger(DocumentsView.class.getName()).log(Level.SEVERE, "deleteFile", e);
        }
        return null;
    }

    public String getSize(Integer size) {
        try {
            return Utils.humanReadableByteCountSI(size);
        } catch (Exception e) {
            Logger.getLogger(DocumentsView.class.getName()).log(Level.SEVERE, "deleteFile", e);
        }
        return null;
    }
    
    /*METODOS PARA LA RUTA_REPOSITORIO*/
    
    /**
     * Busca los archivos asociados a esa entidad
     *
     * @param obj Entiti a buscar archivos
     * @return Listado de archivos asociados a la entidad
     */
    public List<DocumentoBlob> filesRepositorio(Object obj) {
        if (obj == null) {
            return null;
        }
        Object id = ReflexionEntity.getIdFromEntity(obj);
        if (id == null) {
            return null;
        }
        List<DocumentoBlob> files = uploadDoc.fileEntitiRepositorio(obj);
        return files;
    }

    /**
     * Muestra el archivo en otra pestaña
     *
     * @param doc Archivo a visualizar
     */
    public void viewFileRepositorio(DocumentoBlob doc) {
        ss.addParametro("blob", doc);
        ss.setNombreDocumento(doc.getArchivoNombre());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "FileDoc?i=1");
    }

    /**
     * Descarga el archivo
     *
     * @param doc Archivo a descargar
     */
    public void downloadFileRepositorio(DocumentoBlob doc) {
        ss.addParametro("blob", doc);
        ss.addParametro("download", true);
        ss.setNombreDocumento(doc.getArchivoNombre());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "FileDoc?i=1");
    }

    /**
     * Elimina el archivo
     *
     * @param doc Archivo a eliminar
     */
    public void deleteFileRepositorio(DocumentoBlob doc) {
        try {
            this.uploadDoc.eliminarRepositorio(doc);
            JsfUtil.addInformationMessage(doc.getArchivoNombre(), "Archivo eliminado correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(doc.getArchivoNombre(), "Error el eliminar archivo.");
            Logger.getLogger(DocumentsView.class.getName()).log(Level.SEVERE, "deleteFile", e);
        }
    }
}
