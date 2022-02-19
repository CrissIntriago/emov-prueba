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
@Table(name = "docu_metadata", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuMetadata.findAll", query = "SELECT d FROM DocuMetadata d"),
    @NamedQuery(name = "DocuMetadata.findById", query = "SELECT d FROM DocuMetadata d WHERE d.id = :id"),
    @NamedQuery(name = "DocuMetadata.findByUsuarioIngreso", query = "SELECT d FROM DocuMetadata d WHERE d.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "DocuMetadata.findByFechaIngreso", query = "SELECT d FROM DocuMetadata d WHERE d.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "DocuMetadata.findByDataDouble", query = "SELECT d FROM DocuMetadata d WHERE d.dataDouble = :dataDouble"),
    @NamedQuery(name = "DocuMetadata.findByDataLong", query = "SELECT d FROM DocuMetadata d WHERE d.dataLong = :dataLong"),
    @NamedQuery(name = "DocuMetadata.findByDataText", query = "SELECT d FROM DocuMetadata d WHERE d.dataText = :dataText"),
    @NamedQuery(name = "DocuMetadata.findByDataDate", query = "SELECT d FROM DocuMetadata d WHERE d.dataDate = :dataDate"),
    @NamedQuery(name = "DocuMetadata.findByDataTime", query = "SELECT d FROM DocuMetadata d WHERE d.dataTime = :dataTime")})
public class DocuMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "data_double")
    private BigDecimal dataDouble;
    @Column(name = "data_long")
    private BigInteger dataLong;
    @Size(max = 1000)
    @Column(name = "data_text")
    private String dataText;
    @Column(name = "data_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDate;
    @Column(name = "data_time")
    @Temporal(TemporalType.TIME)
    private Date dataTime;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocuDocumento documento;
    @JoinColumn(name = "dato_compuesto", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDatoCompuesto datoCompuesto;
    @JoinColumn(name = "tipo_documento_detalle", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocuTipoDocumentoDetalle tipoDocumentoDetalle;

    public DocuMetadata() {
    }

    public DocuMetadata(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getDataDouble() {
        return dataDouble;
    }

    public void setDataDouble(BigDecimal dataDouble) {
        this.dataDouble = dataDouble;
    }

    public BigInteger getDataLong() {
        return dataLong;
    }

    public void setDataLong(BigInteger dataLong) {
        this.dataLong = dataLong;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
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
        if (!(object instanceof DocuMetadata)) {
            return false;
        }
        DocuMetadata other = (DocuMetadata) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuMetadata[ id=" + id + " ]";
    }
    
}
