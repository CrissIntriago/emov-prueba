/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.model;

import com.origami.sigef.common.entities.ProcedimientoRequisito;
import java.io.Serializable;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMIEC
 */
public class DocumentosModel implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long id;
    private ProcedimientoRequisito requisito;
    private String url;
    private UploadedFile archivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcedimientoRequisito getRequisito() {
        return requisito;
    }

    public void setRequisito(ProcedimientoRequisito requisito) {
        this.requisito = requisito;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

}
