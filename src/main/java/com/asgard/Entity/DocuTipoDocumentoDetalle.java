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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "docu_tipo_documento_detalle", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findAll", query = "SELECT d FROM DocuTipoDocumentoDetalle d"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findById", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.id = :id"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByTipoDetalle", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.tipoDetalle = :tipoDetalle"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByNombre", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByFormato", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.formato = :formato"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByUsuario", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.usuario = :usuario"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByFecha", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByEstado", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByOrden", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.orden = :orden"),
    @NamedQuery(name = "DocuTipoDocumentoDetalle.findByOpcional", query = "SELECT d FROM DocuTipoDocumentoDetalle d WHERE d.opcional = :opcional")})
public class DocuTipoDocumentoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo_detalle")
    private Integer tipoDetalle;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "formato")
    private String formato;
    @Size(max = 100)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 3)
    @Column(name = "estado")
    private String estado;
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "opcional")
    private Boolean opcional;
    @JoinColumn(name = "tipo_dato", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDato tipoDato;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDocumento tipoDocumento;
    @JoinColumn(name = "tipo_documento_hijo", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDocumento tipoDocumentoHijo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumentoDetalle")
    private List<DocuMetadata> docuMetadataList;
    @OneToMany(mappedBy = "tipoDocumentoDetalle")
    private List<DocuArchivador> docuArchivadorList;

    public DocuTipoDocumentoDetalle() {
    }

    public DocuTipoDocumentoDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(Integer tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public DocuTipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(DocuTipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public DocuTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(DocuTipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public DocuTipoDocumento getTipoDocumentoHijo() {
        return tipoDocumentoHijo;
    }

    public void setTipoDocumentoHijo(DocuTipoDocumento tipoDocumentoHijo) {
        this.tipoDocumentoHijo = tipoDocumentoHijo;
    }

    
    public List<DocuMetadata> getDocuMetadataList() {
        return docuMetadataList;
    }

    public void setDocuMetadataList(List<DocuMetadata> docuMetadataList) {
        this.docuMetadataList = docuMetadataList;
    }

    
    public List<DocuArchivador> getDocuArchivadorList() {
        return docuArchivadorList;
    }

    public void setDocuArchivadorList(List<DocuArchivador> docuArchivadorList) {
        this.docuArchivadorList = docuArchivadorList;
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
        if (!(object instanceof DocuTipoDocumentoDetalle)) {
            return false;
        }
        DocuTipoDocumentoDetalle other = (DocuTipoDocumentoDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuTipoDocumentoDetalle[ id=" + id + " ]";
    }
    
}
