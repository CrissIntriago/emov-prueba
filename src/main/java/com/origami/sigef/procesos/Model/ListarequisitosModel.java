/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.procesos.Model;

import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.ventanilla.Entity.ServicioRequisito;
import java.io.File;
import java.io.Serializable;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author alexa
 */
    public class ListarequisitosModel implements Serializable {

    public static final long serialVersionUID = 1L;

    private ProcedimientoRequisito requisitos;
    private ServicioRequisito servicioRequisitos;
    private UploadedFile file;
    private File FILE;

    public ListarequisitosModel() {

    }

    public ListarequisitosModel(ProcedimientoRequisito requisitos, UploadedFile file) {
        this.requisitos = requisitos;
        this.file = file;   
    }
    
    public ListarequisitosModel(ServicioRequisito servicioRequisitos){
        this.servicioRequisitos = servicioRequisitos;
    }

    public ProcedimientoRequisito getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(ProcedimientoRequisito requisitos) {
        this.requisitos = requisitos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public File getFILE() {
        return FILE;
    }

    public void setFILE(File FILE) {
        this.FILE = FILE;
    }

    public ServicioRequisito getServicioRequisitos() {
        return servicioRequisitos;
    }

    public void setServicioRequisitos(ServicioRequisito servicioRequisitos) {
        this.servicioRequisitos = servicioRequisitos;
    }
    
}
