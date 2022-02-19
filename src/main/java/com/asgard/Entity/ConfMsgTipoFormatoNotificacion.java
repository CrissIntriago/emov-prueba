/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "conf_msg_tipo_formato_notificacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfMsgTipoFormatoNotificacion.findAll", query = "SELECT c FROM ConfMsgTipoFormatoNotificacion c"),
    @NamedQuery(name = "ConfMsgTipoFormatoNotificacion.findById", query = "SELECT c FROM ConfMsgTipoFormatoNotificacion c WHERE c.id = :id"),
    @NamedQuery(name = "ConfMsgTipoFormatoNotificacion.findByDescripcion", query = "SELECT c FROM ConfMsgTipoFormatoNotificacion c WHERE c.descripcion = :descripcion")})
public class ConfMsgTipoFormatoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToOne(mappedBy = "tipo")
    private ConfMsgFormatoNotificacion confMsgFormatoNotificacion;

    public ConfMsgTipoFormatoNotificacion() {
    }

    public ConfMsgTipoFormatoNotificacion(Long id) {
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

    public ConfMsgFormatoNotificacion getConfMsgFormatoNotificacion() {
        return confMsgFormatoNotificacion;
    }

    public void setConfMsgFormatoNotificacion(ConfMsgFormatoNotificacion confMsgFormatoNotificacion) {
        this.confMsgFormatoNotificacion = confMsgFormatoNotificacion;
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
        if (!(object instanceof ConfMsgTipoFormatoNotificacion)) {
            return false;
        }
        ConfMsgTipoFormatoNotificacion other = (ConfMsgTipoFormatoNotificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfMsgTipoFormatoNotificacion[ id=" + id + " ]";
    }
    
}
