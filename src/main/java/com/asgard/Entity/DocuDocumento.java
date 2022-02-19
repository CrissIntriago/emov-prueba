/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "docu_documento",schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuDocumento.findAll", query = "SELECT d FROM DocuDocumento d"),
    @NamedQuery(name = "DocuDocumento.findById", query = "SELECT d FROM DocuDocumento d WHERE d.id = :id"),
    @NamedQuery(name = "DocuDocumento.findByDescripcion", query = "SELECT d FROM DocuDocumento d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocuDocumento.findByMetadata", query = "SELECT d FROM DocuDocumento d WHERE d.metadata = :metadata"),
    @NamedQuery(name = "DocuDocumento.findByPaginas", query = "SELECT d FROM DocuDocumento d WHERE d.paginas = :paginas"),
    @NamedQuery(name = "DocuDocumento.findBySizeDoc", query = "SELECT d FROM DocuDocumento d WHERE d.sizeDoc = :sizeDoc"),
    @NamedQuery(name = "DocuDocumento.findByCreUsuario", query = "SELECT d FROM DocuDocumento d WHERE d.creUsuario = :creUsuario"),
    @NamedQuery(name = "DocuDocumento.findByCreFecha", query = "SELECT d FROM DocuDocumento d WHERE d.creFecha = :creFecha"),
    @NamedQuery(name = "DocuDocumento.findByUltModUsuario", query = "SELECT d FROM DocuDocumento d WHERE d.ultModUsuario = :ultModUsuario"),
    @NamedQuery(name = "DocuDocumento.findByUltModFecha", query = "SELECT d FROM DocuDocumento d WHERE d.ultModFecha = :ultModFecha"),
    @NamedQuery(name = "DocuDocumento.findByEstado", query = "SELECT d FROM DocuDocumento d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuDocumento.findByBloqueado", query = "SELECT d FROM DocuDocumento d WHERE d.bloqueado = :bloqueado"),
    @NamedQuery(name = "DocuDocumento.findByUuidValue", query = "SELECT d FROM DocuDocumento d WHERE d.uuidValue = :uuidValue"),
    @NamedQuery(name = "DocuDocumento.findByContentType", query = "SELECT d FROM DocuDocumento d WHERE d.contentType = :contentType"),
    @NamedQuery(name = "DocuDocumento.findByFileName", query = "SELECT d FROM DocuDocumento d WHERE d.fileName = :fileName")})
public class DocuDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2147483647)
    @Column(name = "metadata")
    private String metadata;
    @Column(name = "paginas")
    private Integer paginas;
    @Column(name = "size_doc")
    private BigInteger sizeDoc;
    @Size(max = 2147483647)
    @Column(name = "cre_usuario")
    private String creUsuario;
    @Column(name = "cre_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creFecha;
    @Size(max = 2147483647)
    @Column(name = "ult_mod_usuario")
    private String ultModUsuario;
    @Column(name = "ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultModFecha;
    @Size(max = 3)
    @Column(name = "estado")
    private String estado;
    @Column(name = "bloqueado")
    private Boolean bloqueado;
    @Size(max = 100)
    @Column(name = "uuid_value")
    private String uuidValue;
    @Size(max = 100)
    @Column(name = "content_type")
    private String contentType;
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    @JoinColumn(name = "archivador", referencedColumnName = "id")
    @ManyToOne
    private DocuArchivador archivador;
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DocuDocumento> docuDocumentoList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne
    private DocuDocumento padre;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DocuMetadata> docuMetadataList;
    @OneToMany(mappedBy = "documento", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DocuArchivador> docuArchivadorList;
    @OneToMany(mappedBy = "documento", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DocuDocumentoBlob> docuDocumentoBlobList;

    public DocuDocumento() {
    }

    public DocuDocumento(Long id) {
        this.id = id;
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

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public BigInteger getSizeDoc() {
        return sizeDoc;
    }

    public void setSizeDoc(BigInteger sizeDoc) {
        this.sizeDoc = sizeDoc;
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

    public String getUltModUsuario() {
        return ultModUsuario;
    }

    public void setUltModUsuario(String ultModUsuario) {
        this.ultModUsuario = ultModUsuario;
    }

    public Date getUltModFecha() {
        return ultModFecha;
    }

    public void setUltModFecha(Date ultModFecha) {
        this.ultModFecha = ultModFecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getUuidValue() {
        return uuidValue;
    }

    public void setUuidValue(String uuidValue) {
        this.uuidValue = uuidValue;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocuArchivador getArchivador() {
        return archivador;
    }

    public void setArchivador(DocuArchivador archivador) {
        this.archivador = archivador;
    }

    
    public List<DocuDocumento> getDocuDocumentoList() {
        return docuDocumentoList;
    }

    public void setDocuDocumentoList(List<DocuDocumento> docuDocumentoList) {
        this.docuDocumentoList = docuDocumentoList;
    }

    public DocuDocumento getPadre() {
        return padre;
    }

    public void setPadre(DocuDocumento padre) {
        this.padre = padre;
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

    
    public List<DocuDocumentoBlob> getDocuDocumentoBlobList() {
        return docuDocumentoBlobList;
    }

    public void setDocuDocumentoBlobList(List<DocuDocumentoBlob> docuDocumentoBlobList) {
        this.docuDocumentoBlobList = docuDocumentoBlobList;
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
        if (!(object instanceof DocuDocumento)) {
            return false;
        }
        DocuDocumento other = (DocuDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuDocumento[ id=" + id + " ]";
    }

}
