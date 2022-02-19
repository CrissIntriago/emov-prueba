/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
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
@Table(name = "docu_archivador", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuArchivador.findAll", query = "SELECT d FROM DocuArchivador d"),
    @NamedQuery(name = "DocuArchivador.findById", query = "SELECT d FROM DocuArchivador d WHERE d.id = :id"),
    @NamedQuery(name = "DocuArchivador.findByDescripcion", query = "SELECT d FROM DocuArchivador d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocuArchivador.findByArchivadorPadre", query = "SELECT d FROM DocuArchivador d WHERE d.archivadorPadre = :archivadorPadre"),
    @NamedQuery(name = "DocuArchivador.findByEstado", query = "SELECT d FROM DocuArchivador d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuArchivador.findByFechaIngreso", query = "SELECT d FROM DocuArchivador d WHERE d.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "DocuArchivador.findByOrden", query = "SELECT d FROM DocuArchivador d WHERE d.orden = :orden"),
    @NamedQuery(name = "DocuArchivador.findByComentario", query = "SELECT d FROM DocuArchivador d WHERE d.comentario = :comentario"),
    @NamedQuery(name = "DocuArchivador.findByFolderName", query = "SELECT d FROM DocuArchivador d WHERE d.folderName = :folderName"),
    @NamedQuery(name = "DocuArchivador.findByDataDate", query = "SELECT d FROM DocuArchivador d WHERE d.dataDate = :dataDate"),
    @NamedQuery(name = "DocuArchivador.findByDataDouble", query = "SELECT d FROM DocuArchivador d WHERE d.dataDouble = :dataDouble"),
    @NamedQuery(name = "DocuArchivador.findByDataLong", query = "SELECT d FROM DocuArchivador d WHERE d.dataLong = :dataLong"),
    @NamedQuery(name = "DocuArchivador.findByDataText", query = "SELECT d FROM DocuArchivador d WHERE d.dataText = :dataText"),
    @NamedQuery(name = "DocuArchivador.findByUsuarioIngreso", query = "SELECT d FROM DocuArchivador d WHERE d.usuarioIngreso = :usuarioIngreso")})
public class DocuArchivador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "archivador_padre")
    private BigInteger archivadorPadre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "orden")
    private Integer orden;
    @Size(max = 255)
    @Column(name = "comentario")
    private String comentario;
    @Size(max = 255)
    @Column(name = "folder_name")
    private String folderName;
    @Column(name = "data_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "data_double")
    private BigDecimal dataDouble;
    @Column(name = "data_long")
    private BigDecimal dataLong;
    @Size(max = 255)
    @Column(name = "data_text")
    private String dataText;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "archivador", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DocuDocumento> docuDocumentoList;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne
    private DocuDocumento documento;
    @JoinColumn(name = "dato_compuesto", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDatoCompuesto datoCompuesto;
    @JoinColumn(name = "tipo_documento_detalle", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDocumentoDetalle tipoDocumentoDetalle;

    public DocuArchivador() {
    }

    public DocuArchivador(Long id) {
        this.id = id;
    }

    public DocuArchivador(Long id, String descripcion, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
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

    public BigInteger getArchivadorPadre() {
        return archivadorPadre;
    }

    public void setArchivadorPadre(BigInteger archivadorPadre) {
        this.archivadorPadre = archivadorPadre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public BigDecimal getDataDouble() {
        return dataDouble;
    }

    public void setDataDouble(BigDecimal dataDouble) {
        this.dataDouble = dataDouble;
    }

    public BigDecimal getDataLong() {
        return dataLong;
    }

    public void setDataLong(BigDecimal dataLong) {
        this.dataLong = dataLong;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    
    public List<DocuDocumento> getDocuDocumentoList() {
        return docuDocumentoList;
    }

    public void setDocuDocumentoList(List<DocuDocumento> docuDocumentoList) {
        this.docuDocumentoList = docuDocumentoList;
    }

    public DocuDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(DocuDocumento documento) {
        this.documento = documento;
    }

    public DocuTipoDatoCompuesto getDatoCompuesto() {
        return datoCompuesto;
    }

    public void setDatoCompuesto(DocuTipoDatoCompuesto datoCompuesto) {
        this.datoCompuesto = datoCompuesto;
    }

    public DocuTipoDocumentoDetalle getTipoDocumentoDetalle() {
        return tipoDocumentoDetalle;
    }

    public void setTipoDocumentoDetalle(DocuTipoDocumentoDetalle tipoDocumentoDetalle) {
        this.tipoDocumentoDetalle = tipoDocumentoDetalle;
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
        if (!(object instanceof DocuArchivador)) {
            return false;
        }
        DocuArchivador other = (DocuArchivador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuArchivador[ id=" + id + " ]";
    }
    
}
