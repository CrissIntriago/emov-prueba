/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.doc;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author SandraArroba
 */
@Entity
@Table(name = "documento_blob", schema = "archivo")
public class DocumentoBlob implements java.io.Serializable {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Documento documento;
    @Column(name = "url_data")
    private String urlData;
    @Column(name = "paginas")
    private Integer paginas;
    @Column(name = "page_size")
    private Integer pageSize;
    @Column(name = "extension")
    private String extension;
    @Column(name = "page_width")
    private Integer pageWidth;
    @Column(name = "page_height")
    private Integer pageHeight;
    @Column(name = "archivo_nombre")
    private String archivoNombre;
    @Column(name = "archivo_orden")
    private Short archivoOrden;
    @Column(name = "cre_usuario")
    private String creUsuario;
    @Column(name = "cre_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creFecha;
    @Column(name = "ult_mod_usuario")
    private String ultModUsuario;
    @Column(name = "ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultModFecha;
    @Column(name = "estado")
    private String estado;
    @Column(name = "bloqueado")
    private Boolean bloqueado;

    public DocumentoBlob() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public String getUrlData() {
        return urlData;
    }

    public void setUrlData(String urlData) {
        this.urlData = urlData;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
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

    public Short getArchivoOrden() {
        return archivoOrden;
    }

    public void setArchivoOrden(Short archivoOrden) {
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final DocumentoBlob other = (DocumentoBlob) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DocumentoBlob{" + "id=" + id + ", urlData=" + urlData + ", paginas=" + paginas + ", pageSize=" + pageSize + ", extension=" + extension + ", pageWidth=" + pageWidth + ", pageHeight=" + pageHeight + ", archivoNombre=" + archivoNombre + ", archivoOrden=" + archivoOrden + ", creUsuario=" + creUsuario + ", cre_fecha=" + creFecha + ", ultModUsuario=" + ultModUsuario + ", ultModFecha=" + ultModFecha + ", estado=" + estado + ", bloqueado=" + bloqueado + '}';
    }

}
