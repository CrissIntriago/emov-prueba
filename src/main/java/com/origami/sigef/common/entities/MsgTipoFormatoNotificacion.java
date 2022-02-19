package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Xndy
 */
@Entity
@Table(name = "msg_tipo_formato_notificacion", schema = "conf")
public class MsgTipoFormatoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 1000)
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    @OneToMany(mappedBy = "tipo", fetch = FetchType.LAZY)
    private Collection<MsgFormatoNotificacion> msgFormatoNotificacionCollection;

    public MsgTipoFormatoNotificacion() {
    }

    public MsgTipoFormatoNotificacion(Long id) {
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

    public Collection<MsgFormatoNotificacion> getMsgFormatoNotificacionCollection() {
        return msgFormatoNotificacionCollection;
    }

    public void setMsgFormatoNotificacionCollection(Collection<MsgFormatoNotificacion> msgFormatoNotificacionCollection) {
        this.msgFormatoNotificacionCollection = msgFormatoNotificacionCollection;
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
        if (!(object instanceof MsgTipoFormatoNotificacion)) {
            return false;
        }
        MsgTipoFormatoNotificacion other = (MsgTipoFormatoNotificacion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.MsgTipoFormatoNotificacion[ id=" + id + " ]";
    }

}
