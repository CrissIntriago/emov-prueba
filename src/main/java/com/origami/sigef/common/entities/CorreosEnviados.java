/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.origami.sigef.common.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Basic;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author Arturo
 */

@Entity
@Table(name = "correos_enviados", schema = "public")
public class CorreosEnviados implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "destinatario")
    private String destinatario;

    @Column(name = "asunto")
    private String asunto;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "enviado")
    private Boolean enviado;
    
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_envio;

    public CorreosEnviados() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Date getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(Date fecha_envio) {
        this.fecha_envio = fecha_envio;
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
        if (!(object instanceof CorreosEnviados)) {
            return false;
        }
        CorreosEnviados other = (CorreosEnviados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "CorreosEnviados{" + "id=" + id + ", destinatario=" + destinatario + ", asunto=" + asunto + ", mensaje=" + mensaje + ", enviado=" + enviado + '}';
    }

    
    
}
