/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import com.origami.sigef.common.entities.Usuarios;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "his_persona_referencias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HisPersonaReferencias.findAll", query = "SELECT h FROM HisPersonaReferencias h"),
    @NamedQuery(name = "HisPersonaReferencias.findById", query = "SELECT h FROM HisPersonaReferencias h WHERE h.id = :id"),
    @NamedQuery(name = "HisPersonaReferencias.findByQuery", query = "SELECT h FROM HisPersonaReferencias h WHERE h.query = :query"),
    @NamedQuery(name = "HisPersonaReferencias.findByNewValue", query = "SELECT h FROM HisPersonaReferencias h WHERE h.newValue = :newValue"),
    @NamedQuery(name = "HisPersonaReferencias.findByOldValue", query = "SELECT h FROM HisPersonaReferencias h WHERE h.oldValue = :oldValue")})
public class HisPersonaReferencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "query")
    private String query;
    @Column(name = "new_value")
    private Long newValue;
    @Column(name = "old_value")
    private Long oldValue;
    @Column(name = "esquema")
    private String esquema;
    @Column(name = "tabla")
    private String tabla;
    @Column(name = "references_id")
    private String referenciaId;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios idUsuario;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public HisPersonaReferencias() {
        fechaCreacion = new Date();
    }

    public HisPersonaReferencias(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getNewValue() {
        return newValue;
    }

    public void setNewValue(Long newValue) {
        this.newValue = newValue;
    }

    public Long getOldValue() {
        return oldValue;
    }

    public void setOldValue(Long oldValue) {
        this.oldValue = oldValue;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(String referenciaId) {
        this.referenciaId = referenciaId;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
        if (!(object instanceof HisPersonaReferencias)) {
            return false;
        }
        HisPersonaReferencias other = (HisPersonaReferencias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.HisPersonaReferencias[ id=" + id + " ]";
    }

}
