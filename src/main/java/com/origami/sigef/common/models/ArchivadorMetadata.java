/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.models;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ANGEL NAVARRO
 */
public class ArchivadorMetadata implements Serializable {

    private Long id;
    private String archivador;
    private String carpeta;
    private String separador;
    private String descripcion;
    private String campoDescripcion;
    private String campoTipo;
    private String campoKey;
    private Integer campoOrden;
    private Archivador archivador1;

    public ArchivadorMetadata() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchivador() {
        return archivador;
    }

    public void setArchivador(String archivador) {
        this.archivador = archivador;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getSeparador() {
        return separador;
    }

    public void setSeparador(String separador) {
        this.separador = separador;
    }

    public String getCampoDescripcion() {
        return campoDescripcion;
    }

    public void setCampoDescripcion(String campoDescripcion) {
        this.campoDescripcion = campoDescripcion;
    }

    public String getCampoTipo() {
        return campoTipo;
    }

    public void setCampoTipo(String campoTipo) {
        this.campoTipo = campoTipo;
    }

    public String getCampoKey() {
        return campoKey;
    }

    public void setCampoKey(String campoKey) {
        this.campoKey = campoKey;
    }

    public Integer getCampoOrden() {
        return campoOrden;
    }

    public void setCampoOrden(Integer campoOrden) {
        this.campoOrden = campoOrden;
    }

    public Archivador getArchivador1() {
        return archivador1;
    }

    public void setArchivador1(Archivador archivador1) {
        this.archivador1 = archivador1;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final ArchivadorMetadata other = (ArchivadorMetadata) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArchivadorMetadata{" + "id=" + id + ", archivador=" + archivador + ", carpeta=" + carpeta + ", separador=" + separador + ", campoDescripcion=" + campoDescripcion + ", campoTipo=" + campoTipo + ", campoKey=" + campoKey + ", campoOrden=" + campoOrden + ", archivador1=" + archivador1 + '}';
    }

}
