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
@Table(name = "plan_programatico")
@NamedQueries({
    @NamedQuery(name = "PlanProgramatico.findAll", query = "SELECT p FROM PlanProgramatico p"),
    @NamedQuery(name = "PlanProgramatico.findById", query = "SELECT p FROM PlanProgramatico p WHERE p.id = :id"),
    @NamedQuery(name = "PlanProgramatico.findByEstadoValido", query = "SELECT p FROM PlanProgramatico p WHERE p.estado=?1"),
    @NamedQuery(name = "PlanProgramatico.findByCodigo", query = "SELECT p FROM PlanProgramatico p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "PlanProgramatico.findByDescripcion", query = "SELECT p FROM PlanProgramatico p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PlanProgramatico.findByPeriodo", query = "SELECT p FROM PlanProgramatico p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "PlanProgramatico.findByEstado", query = "SELECT p FROM PlanProgramatico p WHERE p.estado = :estado"),
    @NamedQuery(name = "PlanProgramatico.findByNivel", query = "SELECT p FROM PlanProgramatico p LEFT JOIN p.nivel cn WHERE p.estado = ?1 AND cn.orden = ?2"),
    @NamedQuery(name = "PlanProgramatico.findByNivelPeriodo", query = "SELECT p FROM PlanProgramatico p LEFT JOIN p.nivel cn WHERE p.estado = ?1 AND cn.orden = ?2 AND p.periodo= ?3 order by p.codigo ASC"),
    @NamedQuery(name = "PlanProgramatico.findByFechaVigente", query = "SELECT p FROM PlanProgramatico p WHERE p.fechaVigente = :fechaVigente"),
    @NamedQuery(name = "PlanProgramatico.findByFechaCaducidad", query = "SELECT p.fechaCaducidad FROM PlanProgramatico p WHERE p.fechaCaducidad = ?1 AND p.estado = TRUE"),
    @NamedQuery(name = "PlanProgramatico.findByPadre", query = "SELECT p FROM PlanProgramatico p WHERE p.padre.id = ?1 AND p.estado = TRUE"),
    @NamedQuery(name = "PlanProgramatico.findByFuncionMax", query = "SELECT MAX(p.funcion) FROM PlanProgramatico p WHERE p.nivel.orden = ?1"),
    @NamedQuery(name = "PlanProgramatico.findByFuncion", query = "SELECT p FROM PlanProgramatico p WHERE p.funcion = :funcion"),
    @NamedQuery(name = "PlanProgramatico.findByPrograma", query = "SELECT p FROM PlanProgramatico p WHERE p.programa = :programa"),
    @NamedQuery(name = "PlanProgramatico.findBySubprograma", query = "SELECT p FROM PlanProgramatico p WHERE p.subprograma = :subprograma"),
    @NamedQuery(name = "PlanProgramatico.findByFechaCreacion", query = "SELECT p FROM PlanProgramatico p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PlanProgramatico.findByUsuarioCreacion", query = "SELECT p FROM PlanProgramatico p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PlanProgramatico.findByFechaModificacion", query = "SELECT p FROM PlanProgramatico p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PlanProgramatico.findByUsuarioModifica", query = "SELECT p FROM PlanProgramatico p WHERE p.usuarioModifica = :usuarioModifica")})
public class PlanProgramatico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "codigo", nullable = false, length = 200)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion", nullable = false, length = 2147483647)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private short periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_vigente")
    @Temporal(TemporalType.DATE)
    private Date fechaVigente;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Column(name = "funcion")
    private Short funcion;
    @Column(name = "programa")
    private Short programa;
    @Column(name = "subprograma")
    private Short subprograma;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @JoinColumn(name = "nivel", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Nivel nivel;
    @OneToMany(mappedBy = "padre")
    private List<PlanProgramatico> planProgramaticoList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico padre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estructuraApA")
    private List<PartidasDistributivoAnexo> ListaPartidaDisAnexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estructuraApR")
    private List<ValoresRoles> ListaEstructuraApRTipoRol;

    public PlanProgramatico() {
        this.estado = Boolean.TRUE;
    }

    public PlanProgramatico(Long id) {
        this.id = id;
    }

    public PlanProgramatico(Long id, String codigo, String descripcion, short periodo, boolean estado) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.periodo = periodo;
        this.estado = estado;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaVigente() {
        return fechaVigente;
    }

    public void setFechaVigente(Date fechaVigente) {
        this.fechaVigente = fechaVigente;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Short getFuncion() {
        return funcion;
    }

    public void setFuncion(Short funcion) {
        this.funcion = funcion;
    }

    public Short getPrograma() {
        return programa;
    }

    public void setPrograma(Short programa) {
        this.programa = programa;
    }

    public Short getSubprograma() {
        return subprograma;
    }

    public void setSubprograma(Short subprograma) {
        this.subprograma = subprograma;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<PlanProgramatico> getPlanProgramaticoList() {
        return planProgramaticoList;
    }

    public void setPlanProgramaticoList(List<PlanProgramatico> planProgramaticoList) {
        this.planProgramaticoList = planProgramaticoList;
    }

    public PlanProgramatico getPadre() {
        return padre;
    }

    public void setPadre(PlanProgramatico padre) {
        this.padre = padre;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanProgramatico)) {
            return false;
        }
        PlanProgramatico other = (PlanProgramatico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject4.PlanProgramatico[ id=" + id + " ]";
    }

}
