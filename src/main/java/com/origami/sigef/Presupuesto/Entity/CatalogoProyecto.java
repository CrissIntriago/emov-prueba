/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "catalogo_proyecto", schema = "presupuesto")
@NamedQueries({
    @NamedQuery(name = "CatalogoProyecto.findAll", query = "SELECT c FROM CatalogoProyecto c"),
    @NamedQuery(name = "CatalogoProyecto.findById", query = "SELECT c FROM CatalogoProyecto c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoProyecto.findByPlanNacional", query = "SELECT c FROM CatalogoProyecto c WHERE c.planNacional = :planNacional"),
    @NamedQuery(name = "CatalogoProyecto.findByPlanLocal", query = "SELECT c FROM CatalogoProyecto c WHERE c.planLocal = :planLocal"),
    @NamedQuery(name = "CatalogoProyecto.findByDescripcion", query = "SELECT c FROM CatalogoProyecto c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoProyecto.findByPeriodo", query = "SELECT c FROM CatalogoProyecto c WHERE c.periodo = :periodo"),
    @NamedQuery(name = "CatalogoProyecto.findByEstado", query = "SELECT c FROM CatalogoProyecto c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatalogoProyecto.findByFechaVigencia", query = "SELECT c FROM CatalogoProyecto c WHERE c.fechaVigencia = :fechaVigencia"),
    @NamedQuery(name = "CatalogoProyecto.findByFechaCaducidad", query = "SELECT c FROM CatalogoProyecto c WHERE c.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "CatalogoProyecto.findByFechaCreacion", query = "SELECT c FROM CatalogoProyecto c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "CatalogoProyecto.findByUsuarioCreacion", query = "SELECT c FROM CatalogoProyecto c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "CatalogoProyecto.findByFechaModificacion", query = "SELECT c FROM CatalogoProyecto c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "CatalogoProyecto.findByRelacionado", query = "SELECT c FROM CatalogoProyecto c WHERE c.relacionado = :relacionado"),
    @NamedQuery(name = "CatalogoProyecto.findByCodigo", query = "SELECT c FROM CatalogoProyecto c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CatalogoProyecto.findByTipo", query = "SELECT c FROM CatalogoProyecto c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "CatalogoProyecto.findBySupervisor", query = "SELECT c FROM CatalogoProyecto c WHERE c.supervisor = :supervisor"),
    @NamedQuery(name = "CatalogoProyecto.findByResponsable", query = "SELECT c FROM CatalogoProyecto c WHERE c.responsable = :responsable")})
public class CatalogoProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @NotNull
    @JoinColumn(name = "plan_nacional", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanNacionalPolitica planNacional;
    @NotNull
    @JoinColumn(name = "plan_local", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanLocalPolitica planLocal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private short periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
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
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "relacionado")
    private boolean relacionado;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "supervisor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor supervisor;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor responsable;

    public CatalogoProyecto() {
        this.estado = Boolean.TRUE;
        this.relacionado = Boolean.FALSE;
    }

    public CatalogoProyecto(Long id) {
        this.id = id;
    }

    public CatalogoProyecto(Long id, String descripcion, short periodo, boolean estado, Date fechaVigencia, Date fechaCaducidad) {
        this.id = id;
        this.descripcion = descripcion;
        this.periodo = periodo;
        this.estado = estado;
        this.fechaVigencia = fechaVigencia;
        this.fechaCaducidad = fechaCaducidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanNacionalPolitica getPlanNacional() {
        return planNacional;
    }

    public void setPlanNacional(PlanNacionalPolitica planNacional) {
        this.planNacional = planNacional;
    }

    public PlanLocalPolitica getPlanLocal() {
        return planLocal;
    }

    public void setPlanLocal(PlanLocalPolitica planLocal) {
        this.planLocal = planLocal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
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

    public boolean isRelacionado() {
        return relacionado;
    }

    public void setRelacionado(boolean relacionado) {
        this.relacionado = relacionado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Servidor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Servidor supervisor) {
        this.supervisor = supervisor;
    }

    public Servidor getResponsable() {
        return responsable;
    }

    public void setResponsable(Servidor responsable) {
        this.responsable = responsable;
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
        if (!(object instanceof CatalogoProyecto)) {
            return false;
        }
        CatalogoProyecto other = (CatalogoProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Presupuesto.Entity.CatalogoProyecto[ id=" + id + " ]";
    }

}
