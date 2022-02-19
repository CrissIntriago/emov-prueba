package com.origami.sigef.common.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author Anyelo
 */
@Entity
@Table(name = "msg_formato_notificacion", schema = "conf")
public class MsgFormatoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private MsgTipoFormatoNotificacion tipo;
    @Size(max = 100)
    @Column(name = "asunto", length = 100)
    private String asunto;
    @Size(max = 2000)
    @Column(name = "header", length = 2000)
    private String header;
    @Size(max = 2000)
    @Column(name = "footer", length = 2000)
    private String footer;
    @Column(name = "estado")
    private Boolean estado;

    public MsgFormatoNotificacion() {
    }

    public MsgFormatoNotificacion(Long id) {
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public MsgTipoFormatoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(MsgTipoFormatoNotificacion tipo) {
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
        if (!(object instanceof MsgFormatoNotificacion)) {
            return false;
        }
        MsgFormatoNotificacion other = (MsgFormatoNotificacion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.MsgFormatoNotificacion[ id=" + id + " ]";
    }

}
