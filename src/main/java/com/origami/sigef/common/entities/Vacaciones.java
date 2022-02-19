/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "vacaciones", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "Vacaciones.findAll", query = "SELECT v FROM Vacaciones v"),
    @NamedQuery(name = "Vacaciones.findById", query = "SELECT v FROM Vacaciones v WHERE v.id = :id"),
    @NamedQuery(name = "Vacaciones.findByDias", query = "SELECT v FROM Vacaciones v WHERE v.dias = :dias"),
    @NamedQuery(name = "Vacaciones.findByPeriodo", query = "SELECT v FROM Vacaciones v WHERE v.periodo = :periodo"),
    @NamedQuery(name = "Vacaciones.findByFechaDesde", query = "SELECT v FROM Vacaciones v WHERE v.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "Vacaciones.findByFechaHasta", query = "SELECT v FROM Vacaciones v WHERE v.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "Vacaciones.findByEstado", query = "SELECT v FROM Vacaciones v WHERE v.estado = :estado"),
    @NamedQuery(name = "Vacaciones.findByFechaCreacion", query = "SELECT v FROM Vacaciones v WHERE v.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Vacaciones.findByUsuarioCreacion", query = "SELECT v FROM Vacaciones v WHERE v.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Vacaciones.findByFechaModificacion", query = "SELECT v FROM Vacaciones v WHERE v.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Vacaciones.findByUsuarioModifica", query = "SELECT v FROM Vacaciones v WHERE v.usuarioModifica = :usuarioModifica")})
public class Vacaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "dias")
    private Short dias;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "aprobado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor aprobado;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;

    public Vacaciones() {
        this.estado = Boolean.TRUE;
    }

    public Vacaciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getDias() {
        return dias;
    }

    public void setDias(Short dias) {
        this.dias = dias;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Servidor getAprobado() {
        return aprobado;
    }

    public void setAprobado(Servidor aprobado) {
        this.aprobado = aprobado;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
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
        if (!(object instanceof Vacaciones)) {
            return false;
        }
        Vacaciones other = (Vacaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Vacaciones[ id=" + id + " ]";
    }

}
