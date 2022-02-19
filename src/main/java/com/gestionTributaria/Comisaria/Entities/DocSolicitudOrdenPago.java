/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Entities;

import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMIEC
 */
@Entity
@Table(name = "doc_solicitud_orden_pago", schema = "comisaria")
@XmlRootElement
public class DocSolicitudOrdenPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private HistoricoTramites tramite;
    @Column(name = "documentos_datos")
    private String documentosDatos;
    @Column(name = "nombres_usuario")
    private String nombresUsuario;
    @Column(name = "cliente_id")
    private Long clienteId;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "actividad")
    private String actividad;
    @Column(name = "usuario_crea")
    private String usuarioCrea;
    @Column(name = "usuario_mod")
    private String usuarioMod;
    @Column(name = "fecha_crea")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechamod;
    @Column(name = "estado")
    private Boolean estado;

    public DocSolicitudOrdenPago() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public String getDocumentosDatos() {
        return documentosDatos;
    }

    public void setDocumentosDatos(String documentosDatos) {
        this.documentosDatos = documentosDatos;
    }

    public String getNombresUsuario() {
        return nombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        this.nombresUsuario = nombresUsuario;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getUsuarioCrea() {
        return usuarioCrea;
    }

    public void setUsuarioCrea(String usuarioCrea) {
        this.usuarioCrea = usuarioCrea;
    }

    public String getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(String usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public Date getFechamod() {
        return fechamod;
    }

    public void setFechamod(Date fechamod) {
        this.fechamod = fechamod;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "DocSolicitudOrdenPago{" + "id=" + id + ", tramite=" + tramite + ", documentosDatos=" + documentosDatos + ", nombresUsuario=" + nombresUsuario + ", clienteId=" + clienteId + ", direccion=" + direccion + ", actividad=" + actividad + ", usuarioCrea=" + usuarioCrea + ", usuarioMod=" + usuarioMod + ", fechaCrea=" + fechaCrea + ", fechamod=" + fechamod + '}';
    }

}
