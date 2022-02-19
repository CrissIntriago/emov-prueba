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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "docu_tipo_dato_compuesto", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuTipoDatoCompuesto.findAll", query = "SELECT d FROM DocuTipoDatoCompuesto d"),
    @NamedQuery(name = "DocuTipoDatoCompuesto.findById", query = "SELECT d FROM DocuTipoDatoCompuesto d WHERE d.id = :id"),
    @NamedQuery(name = "DocuTipoDatoCompuesto.findByValor", query = "SELECT d FROM DocuTipoDatoCompuesto d WHERE d.valor = :valor"),
    @NamedQuery(name = "DocuTipoDatoCompuesto.findByDescripcion", query = "SELECT d FROM DocuTipoDatoCompuesto d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DocuTipoDatoCompuesto.findByEstado", query = "SELECT d FROM DocuTipoDatoCompuesto d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocuTipoDatoCompuesto.findByOrden", query = "SELECT d FROM DocuTipoDatoCompuesto d WHERE d.orden = :orden")})
public class DocuTipoDatoCompuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "valor")
    private String valor;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 3)
    @Column(name = "estado")
    private String estado;
    @Column(name = "orden")
    private Integer orden;
    @OneToMany(mappedBy = "datoCompuesto")
    private List<DocuMetadata> docuMetadataList;
    @OneToMany(mappedBy = "datoCompuesto")
    private List<DocuArchivador> docuArchivadorList;
    @JoinColumn(name = "tipo_dato", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoDato tipoDato;

    public DocuTipoDatoCompuesto() {
    }

    public DocuTipoDatoCompuesto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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

    public DocuTipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(DocuTipoDato tipoDato) {
        this.tipoDato = tipoDato;
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
        if (!(object instanceof DocuTipoDatoCompuesto)) {
            return false;
        }
        DocuTipoDatoCompuesto other = (DocuTipoDatoCompuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuTipoDatoCompuesto[ id=" + id + " ]";
    }
    
}
