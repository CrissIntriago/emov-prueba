/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.doc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author SandraArroba
 */
@Entity
@Table(name = "documento", schema = "archivo")
public class Documento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "metadata")
    private String metadata;
    @Column(name = "paginas")
    private Integer paginas;
    @Column(name = "size_doc")
    private Long sizeDoc;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Documento padre;
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
    @OneToMany(mappedBy = "documento", fetch = FetchType.LAZY)
    @OrderBy
    private List<DocumentoBlob> documentoBlobs;

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

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public Long getSizeDoc() {
        return sizeDoc;
    }

    public void setSizeDoc(Long sizeDoc) {
        this.sizeDoc = sizeDoc;
    }

    public Documento getPadre() {
        return padre;
    }

    public void setPadre(Documento padre) {
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

    public List<DocumentoBlob> getDocumentoBlobs() {
        return documentoBlobs;
    }

    public void setDocumentoBlobs(List<DocumentoBlob> documentoBlobs) {
        this.documentoBlobs = documentoBlobs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        return "Documento{" + "id=" + id + ", descripcion=" + descripcion + ", metadata=" + metadata + ", paginas=" + paginas + ", sizeDoc=" + sizeDoc + ", padre=" + padre + ", creUsuario=" + creUsuario + ", creFecha=" + creFecha + ", ultModUsuario=" + ultModUsuario + ", ultModFecha=" + ultModFecha + ", estado=" + estado + ", bloqueado=" + bloqueado + '}';
    }

}
