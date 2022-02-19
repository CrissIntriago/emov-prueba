/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Nivel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "estructura_programa_presupuestario", schema = "presupuesto")
@NamedQueries({
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findAll", query = "SELECT e FROM EstructuraProgramaPresupuestario e"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findById", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.id = :id"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByCodigo", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByDescripcion", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByPeriodo", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.periodo = :periodo"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByEstado", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.estado = :estado"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByFechaVigente", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.fechaVigente = :fechaVigente"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByFechaCaducidad", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByNivel", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.nivel = :nivel"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByFuncion", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.funcion = :funcion"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByPrograma", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.programa = :programa"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findBySubprograma", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.subprograma = :subprograma"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByClasificacion", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.clasificacion = :clasificacion"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByFechaCreacion", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByUsuarioCreacion", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByFechaModificacion", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByPadre", query = "SELECT p FROM EstructuraProgramaPresupuestario p WHERE p.padre.id = ?1 AND p.estado = TRUE"),
    @NamedQuery(name = "EstructuraProgramaPresupuestario.findByUsuarioModifica", query = "SELECT e FROM EstructuraProgramaPresupuestario e WHERE e.usuarioModifica = :usuarioModifica")})
public class EstructuraProgramaPresupuestario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "codigo")
    private String codigo;
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
    @Column(name = "fecha_vigente")
    @Temporal(TemporalType.DATE)
    private Date fechaVigente;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @JoinColumn(name = "nivel", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Nivel nivel;
    @Column(name = "funcion")
    private Short funcion;
    @Column(name = "programa")
    private Short programa;
    @Column(name = "subprograma")
    private Short subprograma;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
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
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<EstructuraProgramaPresupuestario> estructuraProgramaPresupuestarioList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private EstructuraProgramaPresupuestario padre;

    public EstructuraProgramaPresupuestario() {
        this.estado = Boolean.TRUE;
    }

    public EstructuraProgramaPresupuestario(Long id) {
        this.id = id;
    }

    public EstructuraProgramaPresupuestario(Long id, String codigo, String descripcion, short periodo, boolean estado) {
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

    public boolean isEstado() {
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

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
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

    public List<EstructuraProgramaPresupuestario> getEstructuraProgramaPresupuestarioList() {
        return estructuraProgramaPresupuestarioList;
    }

    public void setEstructuraProgramaPresupuestarioList(List<EstructuraProgramaPresupuestario> estructuraProgramaPresupuestarioList) {
        this.estructuraProgramaPresupuestarioList = estructuraProgramaPresupuestarioList;
    }

    public EstructuraProgramaPresupuestario getPadre() {
        return padre;
    }

    public void setPadre(EstructuraProgramaPresupuestario padre) {
        this.padre = padre;
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
        if (!(object instanceof EstructuraProgramaPresupuestario)) {
            return false;
        }
        EstructuraProgramaPresupuestario other = (EstructuraProgramaPresupuestario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Presupuesto.Entity.EstructuraProgramaPresupuestario[ id=" + id + " ]";
    }

}
