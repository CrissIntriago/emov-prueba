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
import javax.persistence.JoinColumn;
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
@Table(name = "conf_msg_formato_notificacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfMsgFormatoNotificacion.findAll", query = "SELECT c FROM ConfMsgFormatoNotificacion c"),
    @NamedQuery(name = "ConfMsgFormatoNotificacion.findById", query = "SELECT c FROM ConfMsgFormatoNotificacion c WHERE c.id = :id"),
    @NamedQuery(name = "ConfMsgFormatoNotificacion.findByHeader", query = "SELECT c FROM ConfMsgFormatoNotificacion c WHERE c.header = :header"),
    @NamedQuery(name = "ConfMsgFormatoNotificacion.findByFooter", query = "SELECT c FROM ConfMsgFormatoNotificacion c WHERE c.footer = :footer"),
    @NamedQuery(name = "ConfMsgFormatoNotificacion.findByEstado", query = "SELECT c FROM ConfMsgFormatoNotificacion c WHERE c.estado = :estado"),
    @NamedQuery(name = "ConfMsgFormatoNotificacion.findByAsunto", query = "SELECT c FROM ConfMsgFormatoNotificacion c WHERE c.asunto = :asunto")})
public class ConfMsgFormatoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2000)
    @Column(name = "header")
    private String header;
    @Size(max = 2000)
    @Column(name = "footer")
    private String footer;
    @Column(name = "estado")
    private Integer estado;
    @Size(max = 100)
    @Column(name = "asunto")
    private String asunto;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @OneToOne
    private ConfMsgTipoFormatoNotificacion tipo;

    public ConfMsgFormatoNotificacion() {
    }

    public ConfMsgFormatoNotificacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public ConfMsgTipoFormatoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(ConfMsgTipoFormatoNotificacion tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof ConfMsgFormatoNotificacion)) {
            return false;
        }
        ConfMsgFormatoNotificacion other = (ConfMsgFormatoNotificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfMsgFormatoNotificacion[ id=" + id + " ]";
    }
    
}
