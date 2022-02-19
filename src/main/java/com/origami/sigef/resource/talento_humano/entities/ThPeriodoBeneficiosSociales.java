/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.util.Calendar;
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
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_periodo_beneficios_sociales", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findAll", query = "SELECT t FROM ThPeriodoBeneficiosSociales t"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findById", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.id = :id"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByPeriodo", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByFechaInicio", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByFechaFin", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.fechaFin = :fechaFin"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByEstado", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByUsuarioCreacion", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByFechaCreacion", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByUsuarioModificacion", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThPeriodoBeneficiosSociales.findByFechaModificacion", query = "SELECT t FROM ThPeriodoBeneficiosSociales t WHERE t.fechaModificacion = :fechaModificacion")})
public class ThPeriodoBeneficiosSociales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;

    public ThPeriodoBeneficiosSociales() {
        this.estado = true;
    }

    public ThPeriodoBeneficiosSociales(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
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
        if (!(object instanceof ThPeriodoBeneficiosSociales)) {
            return false;
        }
        ThPeriodoBeneficiosSociales other = (ThPeriodoBeneficiosSociales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThPeriodoBeneficiosSociales[ id=" + id + " ]";
    }

}
