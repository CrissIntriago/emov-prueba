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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "docu_tipo_dato", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuTipoDato.findAll", query = "SELECT d FROM DocuTipoDato d"),
    @NamedQuery(name = "DocuTipoDato.findById", query = "SELECT d FROM DocuTipoDato d WHERE d.id = :id"),
    @NamedQuery(name = "DocuTipoDato.findByNombre", query = "SELECT d FROM DocuTipoDato d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "DocuTipoDato.findByDescripcion", query = "SELECT d FROM DocuTipoDato d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocuTipoDato.findByEstado", query = "SELECT d FROM DocuTipoDato d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuTipoDato.findByTipo", query = "SELECT d FROM DocuTipoDato d WHERE d.tipo = :tipo")})
public class DocuTipoDato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 3)
    @Column(name = "estado")
    private String estado;
    @Column(name = "tipo")
    private Integer tipo;
    @OneToMany(mappedBy = "tipoDato")
    private List<DocuTipoDocumentoDetalle> docuTipoDocumentoDetalleList;
    @OneToMany(mappedBy = "tipoDato")
    private List<DocuTipoDatoCompuesto> docuTipoDatoCompuestoList;

    public DocuTipoDato() {
    }

    public DocuTipoDato(Long id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    
    public List<DocuTipoDocumentoDetalle> getDocuTipoDocumentoDetalleList() {
        return docuTipoDocumentoDetalleList;
    }

    public void setDocuTipoDocumentoDetalleList(List<DocuTipoDocumentoDetalle> docuTipoDocumentoDetalleList) {
        this.docuTipoDocumentoDetalleList = docuTipoDocumentoDetalleList;
    }

    
    public List<DocuTipoDatoCompuesto> getDocuTipoDatoCompuestoList() {
        return docuTipoDatoCompuestoList;
    }

    public void setDocuTipoDatoCompuestoList(List<DocuTipoDatoCompuesto> docuTipoDatoCompuestoList) {
        this.docuTipoDatoCompuestoList = docuTipoDatoCompuestoList;
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
        if (!(object instanceof DocuTipoDato)) {
            return false;
        }
        DocuTipoDato other = (DocuTipoDato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuTipoDato[ id=" + id + " ]";
    }
    
}
