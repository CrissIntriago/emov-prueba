/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "docu_tipo_notificacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuTipoNotificacion.findAll", query = "SELECT d FROM DocuTipoNotificacion d"),
    @NamedQuery(name = "DocuTipoNotificacion.findById", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.id = :id"),
    @NamedQuery(name = "DocuTipoNotificacion.findByNombre", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "DocuTipoNotificacion.findByDescripcion", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocuTipoNotificacion.findByEstado", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuTipoNotificacion.findByAbreviatura", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.abreviatura = :abreviatura"),
    @NamedQuery(name = "DocuTipoNotificacion.findByCodigoSecuencia", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.codigoSecuencia = :codigoSecuencia"),
    @NamedQuery(name = "DocuTipoNotificacion.findByClase", query = "SELECT d FROM DocuTipoNotificacion d WHERE d.clase = :clase")})
public class DocuTipoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Size(max = 5)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Size(max = 100)
    @Column(name = "codigo_secuencia")
    private String codigoSecuencia;
    @Column(name = "clase")
    private Integer clase;
    @OneToMany(mappedBy = "tipoNotificacion")
    private List<DocuNotificacion> docuNotificacionList;

    public DocuTipoNotificacion() {
    }

    public DocuTipoNotificacion(Long id) {
        this.id = id;
    }

    public DocuTipoNotificacion(Long id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getCodigoSecuencia() {
        return codigoSecuencia;
    }

    public void setCodigoSecuencia(String codigoSecuencia) {
        this.codigoSecuencia = codigoSecuencia;
    }

    public Integer getClase() {
        return clase;
    }

    public void setClase(Integer clase) {
        this.clase = clase;
    }

    
    public List<DocuNotificacion> getDocuNotificacionList() {
        return docuNotificacionList;
    }

    public void setDocuNotificacionList(List<DocuNotificacion> docuNotificacionList) {
        this.docuNotificacionList = docuNotificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocuTipoNotificacion)) {
            return false;
        }
        DocuTipoNotificacion other = (DocuTipoNotificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuTipoNotificacion[ id=" + id + " ]";
    }
    
}
