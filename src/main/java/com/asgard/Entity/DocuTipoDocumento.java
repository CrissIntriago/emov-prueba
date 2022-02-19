/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "docu_tipo_documento", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuTipoDocumento.findAll", query = "SELECT d FROM DocuTipoDocumento d"),
    @NamedQuery(name = "DocuTipoDocumento.findById", query = "SELECT d FROM DocuTipoDocumento d WHERE d.id = :id"),
    @NamedQuery(name = "DocuTipoDocumento.findByNombre", query = "SELECT d FROM DocuTipoDocumento d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "DocuTipoDocumento.findByDescripcion", query = "SELECT d FROM DocuTipoDocumento d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocuTipoDocumento.findByUsuario", query = "SELECT d FROM DocuTipoDocumento d WHERE d.usuario = :usuario"),
    @NamedQuery(name = "DocuTipoDocumento.findByFecha", query = "SELECT d FROM DocuTipoDocumento d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocuTipoDocumento.findByEstado", query = "SELECT d FROM DocuTipoDocumento d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuTipoDocumento.findByCodigo", query = "SELECT d FROM DocuTipoDocumento d WHERE d.codigo = :codigo")})
public class DocuTipoDocumento implements Serializable {

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
    @Size(max = 100)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 3)
    @Column(name = "estado")
    private String estado;
    @Size(max = 10)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(mappedBy = "tipoDocumento")
    private List<DocuTipoDocumentoDetalle> docuTipoDocumentoDetalleList;
    @OneToMany(mappedBy = "tipoDocumentoHijo")
    private List<DocuTipoDocumentoDetalle> docuTipoDocumentoDetalleList1;

    public DocuTipoDocumento() {
    }

    public DocuTipoDocumento(Long id) {
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
    public List<DocuTipoDocumentoDetalle> getDocuTipoDocumentoDetalleList() {
        return docuTipoDocumentoDetalleList;
    }

    public void setDocuTipoDocumentoDetalleList(List<DocuTipoDocumentoDetalle> docuTipoDocumentoDetalleList) {
        this.docuTipoDocumentoDetalleList = docuTipoDocumentoDetalleList;
    }

    
    public List<DocuTipoDocumentoDetalle> getDocuTipoDocumentoDetalleList1() {
        return docuTipoDocumentoDetalleList1;
    }

    public void setDocuTipoDocumentoDetalleList1(List<DocuTipoDocumentoDetalle> docuTipoDocumentoDetalleList1) {
        this.docuTipoDocumentoDetalleList1 = docuTipoDocumentoDetalleList1;
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
        if (!(object instanceof DocuTipoDocumento)) {
            return false;
        }
        DocuTipoDocumento other = (DocuTipoDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuTipoDocumento[ id=" + id + " ]";
    }
    
}
