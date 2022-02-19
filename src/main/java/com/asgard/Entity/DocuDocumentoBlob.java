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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "docu_documento_blob", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuDocumentoBlob.findAll", query = "SELECT d FROM DocuDocumentoBlob d"),
    @NamedQuery(name = "DocuDocumentoBlob.findById", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.id = :id"),
    @NamedQuery(name = "DocuDocumentoBlob.findByBlobData", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.blobData = :blobData"),
    @NamedQuery(name = "DocuDocumentoBlob.findByPaginas", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.paginas = :paginas"),
    @NamedQuery(name = "DocuDocumentoBlob.findByPageSize", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.pageSize = :pageSize"),
    @NamedQuery(name = "DocuDocumentoBlob.findByTipoArchivo", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.tipoArchivo = :tipoArchivo"),
    @NamedQuery(name = "DocuDocumentoBlob.findByPageWidth", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.pageWidth = :pageWidth"),
    @NamedQuery(name = "DocuDocumentoBlob.findByPageHeight", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.pageHeight = :pageHeight"),
    @NamedQuery(name = "DocuDocumentoBlob.findByArchivoNombre", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.archivoNombre = :archivoNombre"),
    @NamedQuery(name = "DocuDocumentoBlob.findByArchivoOrden", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.archivoOrden = :archivoOrden"),
    @NamedQuery(name = "DocuDocumentoBlob.findByCreUsuario", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.creUsuario = :creUsuario"),
    @NamedQuery(name = "DocuDocumentoBlob.findByCreFecha", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.creFecha = :creFecha"),
    @NamedQuery(name = "DocuDocumentoBlob.findByUltModUsuario", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.ultModUsuario = :ultModUsuario"),
    @NamedQuery(name = "DocuDocumentoBlob.findByUltModFecha", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.ultModFecha = :ultModFecha"),
    @NamedQuery(name = "DocuDocumentoBlob.findByEstado", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuDocumentoBlob.findByBloqueado", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.bloqueado = :bloqueado"),
    @NamedQuery(name = "DocuDocumentoBlob.findByNumberPage", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.numberPage = :numberPage"),
    @NamedQuery(name = "DocuDocumentoBlob.findByFileName", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.fileName = :fileName"),
    @NamedQuery(name = "DocuDocumentoBlob.findByFilePath", query = "SELECT d FROM DocuDocumentoBlob d WHERE d.filePath = :filePath")})
public class DocuDocumentoBlob implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "blob_data")
    private BigInteger blobData;
    @Column(name = "paginas")
    private Integer paginas;
    @Column(name = "page_size")
    private BigInteger pageSize;
    @Size(max = 2147483647)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Column(name = "page_width")
    private Integer pageWidth;
    @Column(name = "page_height")
    private Integer pageHeight;
    @Size(max = 2147483647)
    @Column(name = "archivo_nombre")
    private String archivoNombre;
    @Column(name = "archivo_orden")
    private Integer archivoOrden;
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
    @Column(name = "number_page")
    private BigInteger numberPage;
    @Size(max = 2147483647)
    @Column(name = "file_name")
    private String fileName;
    @Size(max = 2147483647)
    @Column(name = "file_path")
    private String filePath;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne
    private DocuDocumento documento;

    public DocuDocumentoBlob() {
    }

    public DocuDocumentoBlob(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getBlobData() {
        return blobData;
    }

    public void setBlobData(BigInteger blobData) {
        this.blobData = blobData;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public BigInteger getPageSize() {
        return pageSize;
    }

    public void setPageSize(BigInteger pageSize) {
        this.pageSize = pageSize;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Integer getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Integer pageHeight) {
        this.pageHeight = pageHeight;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
    }

    public Integer getArchivoOrden() {
        return archivoOrden;
    }

    public void setArchivoOrden(Integer archivoOrden) {
        this.archivoOrden = archivoOrden;
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

    public BigInteger getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(BigInteger numberPage) {
        this.numberPage = numberPage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DocuDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(DocuDocumento documento) {
        this.documento = documento;
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
        if (!(object instanceof DocuDocumentoBlob)) {
            return false;
        }
        DocuDocumentoBlob other = (DocuDocumentoBlob) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuDocumentoBlob[ id=" + id + " ]";
    }
    
}
