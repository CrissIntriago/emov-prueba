/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.procesos.entities.TipoTramite;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "tipo_tramite_requisito", schema = "procesos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTramiteRequisito.findAll", query = "SELECT h FROM TipoTramiteRequisito h"),
    @NamedQuery(name = "TipoTramiteRequisito.findById", query = "SELECT h FROM TipoTramiteRequisito h WHERE h.id = :id"),
    @NamedQuery(name = "TipoTramiteRequisito.findByDescripcion", query = "SELECT h FROM TipoTramiteRequisito h WHERE h.descripcion = :descripcion"),
    @NamedQuery(name = "TipoTramiteRequisito.findByFormatoArchivo", query = "SELECT h FROM TipoTramiteRequisito h WHERE h.formatoArchivo = :formatoArchivo"),
    @NamedQuery(name = "TipoTramiteRequisito.findByRequerido", query = "SELECT h FROM TipoTramiteRequisito h WHERE h.requerido = :requerido")})
public class TipoTramiteRequisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "formato_archivo")
    private String formatoArchivo;
    @Basic(optional = false)
    @Column(name = "requerido")
    private boolean requerido;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite tipoTramite;

    public TipoTramiteRequisito() {
    }

    public TipoTramiteRequisito(Long id) {
        this.id = id;
    }

    public TipoTramiteRequisito(Long id, boolean requerido) {
        this.id = id;
        this.requerido = requerido;
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

    public String getFormatoArchivo() {
        return formatoArchivo;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivo = formatoArchivo;
    }

    public boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(boolean requerido) {
        this.requerido = requerido;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
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
        if (!(object instanceof TipoTramiteRequisito)) {
            return false;
        }
        TipoTramiteRequisito other = (TipoTramiteRequisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tutorial.jsf.venta.entities.TipoTramiteRequisito[ id=" + id + " ]";
    }

}
