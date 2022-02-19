/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "plan_local_politica", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PlanLocalPolitica.findAll", query = "SELECT p FROM PlanLocalPolitica p"),
    @NamedQuery(name = "PlanLocalPolitica.findById", query = "SELECT p FROM PlanLocalPolitica p WHERE p.id = :id"),
    @NamedQuery(name = "PlanLocalPolitica.findByCodigo", query = "SELECT p FROM PlanLocalPolitica p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "PlanLocalPolitica.findByPeriodo", query = "SELECT p FROM PlanLocalPolitica p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "PlanLocalPolitica.findByDescripcion", query = "SELECT p FROM PlanLocalPolitica p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PlanLocalPolitica.findByFechaVigencia", query = "SELECT p FROM PlanLocalPolitica p WHERE p.fechaVigencia = :fechaVigencia"),
    @NamedQuery(name = "PlanLocalPolitica.findByFechaCaducidad", query = "SELECT p FROM PlanLocalPolitica p WHERE p.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "PlanLocalPolitica.findByEstado", query = "SELECT p FROM PlanLocalPolitica p WHERE p.estado = TRUE ORDER BY p.codigo ASC"),
    @NamedQuery(name = "PlanLocalPolitica.findByPolitica", query = "SELECT p FROM PlanLocalPolitica p WHERE p.objetivo = ?1 AND p.estado=TRUE ORDER BY p.codigo"),
    @NamedQuery(name = "PlanLocalPolitica.findByFechaCreacion", query = "SELECT p FROM PlanLocalPolitica p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PlanLocalPolitica.findByUsuarioCreacion", query = "SELECT p FROM PlanLocalPolitica p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PlanLocalPolitica.findByFechaModificacion", query = "SELECT p FROM PlanLocalPolitica p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PlanLocalPolitica.findByUsuarioModifica", query = "SELECT p FROM PlanLocalPolitica p WHERE p.usuarioModifica = :usuarioModifica")})
public class PlanLocalPolitica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "periodo")
    private short periodo;
    @Basic(optional = false)
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Basic(optional = false)
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "objetivo", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanLocalObjetivo objetivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "politica", fetch = FetchType.LAZY)
    private List<PlanLocalProgramaProyecto> nombrePlanProgramaProyectoList;

    public PlanLocalPolitica() {
        this.estado = Boolean.TRUE;
    }

    public PlanLocalPolitica(Long id) {
        this.id = id;
    }

    public PlanLocalPolitica(Long id, short periodo, String descripcion, Date fechaVigencia, Date fechaCaducidad, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.periodo = periodo;
        this.descripcion = descripcion;
        this.fechaVigencia = fechaVigencia;
        this.fechaCaducidad = fechaCaducidad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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

    public PlanLocalObjetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(PlanLocalObjetivo objetivo) {
        this.objetivo = objetivo;
    }

    public List<PlanLocalProgramaProyecto> getNombrePlanProgramaProyectoList() {
        return nombrePlanProgramaProyectoList;
    }

    public void setNombrePlanProgramaProyectoList(List<PlanLocalProgramaProyecto> nombrePlanProgramaProyectoList) {
        this.nombrePlanProgramaProyectoList = nombrePlanProgramaProyectoList;
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
        if (!(object instanceof PlanLocalPolitica)) {
            return false;
        }
        PlanLocalPolitica other = (PlanLocalPolitica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject5.PlanLocalPolitica[ id=" + id + " ]";
    }

}
