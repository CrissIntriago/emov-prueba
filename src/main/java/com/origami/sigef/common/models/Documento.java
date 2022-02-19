/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ANGEL NAVARRO
 */
public class Documento implements Serializable {

    private Long id;
    private String descripcion;
    private String metadata;
    private Long archivador;
    private Integer paginas;
    private Integer sizeDoc;
    private Long padre;
    private String creUsuario;
    private Date creFecha;
    private Integer pageSize;
    private String extension;
    private String archivoNombre;
    private Long blobData;
    private Integer pageHeight;
    private Integer pageWidth;

    public Documento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Long getArchivador() {
        return archivador;
    }

    public void setArchivador(Long archivador) {
        this.archivador = archivador;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public Integer getSizeDoc() {
        return sizeDoc;
    }

    public void setSizeDoc(Integer sizeDoc) {
        this.sizeDoc = sizeDoc;
    }

    public Long getPadre() {
        return padre;
    }

    public void setPadre(Long padre) {
        this.padre = padre;
    }

    public String getCreUsuario() {
        return creUsuario;
    }

    public void setCreUsuario(String creUsuario) {
        this.creUsuario = creUsuario;
    }

    public Date getCreFecha() {
        return creFecha;
    }

    public void setCreFecha(Date creFecha) {
        this.creFecha = creFecha;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
    }

    public Long getBlobData() {
        return blobData;
    }

    public void setBlobData(Long blobData) {
        this.blobData = blobData;
    }

    public Integer getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Integer pageHeight) {
        this.pageHeight = pageHeight;
    }

    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Documento other = (Documento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Documento{" + "id=" + id + ", descripcion=" + descripcion + ", metadata=" + metadata + ", archivador=" + archivador + ", paginas=" + paginas + ", sizeDoc=" + sizeDoc + ", padre=" + padre + ", creUsuario=" + creUsuario + ", creFecha=" + creFecha + '}';
    }

}
