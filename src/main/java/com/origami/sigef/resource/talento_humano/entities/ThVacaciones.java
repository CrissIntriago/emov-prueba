/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_vacaciones", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThVacaciones.findAll", query = "SELECT t FROM ThVacaciones t"),
    @NamedQuery(name = "ThVacaciones.findById", query = "SELECT t FROM ThVacaciones t WHERE t.id = :id"),
    @NamedQuery(name = "ThVacaciones.findByIdServidor", query = "SELECT t FROM ThVacaciones t WHERE t.idServidor = ?1 AND t.estado = true ORDER BY t.periodo,t.id ASC"),
    @NamedQuery(name = "ThVacaciones.findByIdServidorNoAprobado", query = "SELECT t FROM ThVacaciones t WHERE t.idServidor = ?1 AND t.estado = true AND t.aprobado = false ORDER BY t.periodo,t.id ASC"),
    @NamedQuery(name = "ThVacaciones.findByIdServidorAnteriores", query = "SELECT t FROM ThVacaciones t WHERE t.idServidor = ?1 AND t.estado = true AND t.id < ?2 ORDER BY t.periodo,t.id ASC"),
    @NamedQuery(name = "ThVacaciones.findByFechaSalida", query = "SELECT t FROM ThVacaciones t WHERE t.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "ThVacaciones.findByFechaReingreso", query = "SELECT t FROM ThVacaciones t WHERE t.fechaReingreso = :fechaReingreso"),
    @NamedQuery(name = "ThVacaciones.findByPeriodo", query = "SELECT t FROM ThVacaciones t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThVacaciones.findByDias", query = "SELECT t FROM ThVacaciones t WHERE t.dias = :dias"),
    @NamedQuery(name = "ThVacaciones.findByFechaCreacion", query = "SELECT t FROM ThVacaciones t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThVacaciones.findByUsuarioCreacion", query = "SELECT t FROM ThVacaciones t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThVacaciones.findByFechaModificacion", query = "SELECT t FROM ThVacaciones t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThVacaciones.findByUsuarioModificacion", query = "SELECT t FROM ThVacaciones t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThVacaciones.findByEstado", query = "SELECT t FROM ThVacaciones t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThVacaciones.findByAprobado", query = "SELECT t FROM ThVacaciones t WHERE t.aprobado = :aprobado")})
public class ThVacaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @Column(name = "fecha_reingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaReingreso;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "dias")
    private Integer dias;
    @Column(name = "dias_faltantes")
    private Integer diasFaltantes;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @JoinColumn(name = "id_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor idServidor;
    
    @Transient
    private Integer diasDisponibles;

    public ThVacaciones() {
        this.estado = Boolean.TRUE;
        this.aprobado = Boolean.FALSE;
        this.dias = 0;
        this.diasFaltantes = 0;
        this.fechaSalida = new Date();        
        this.fechaReingreso = new Date();
    }

    public ThVacaciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaReingreso() {
        return fechaReingreso;
    }

    public void setFechaReingreso(Date fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
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

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public Servidor getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Servidor idServidor) {
        this.idServidor = idServidor;
    }

    public Integer getDiasFaltantes() {
        return diasFaltantes;
    }

    public void setDiasFaltantes(Integer diasFaltantes) {
        this.diasFaltantes = diasFaltantes;
    }

    public Integer getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(Integer diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
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
        if (!(object instanceof ThVacaciones)) {
            return false;
        }
        ThVacaciones other = (ThVacaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThVacaciones[ id=" + id + " ]";
    }

}
