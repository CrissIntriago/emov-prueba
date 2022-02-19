/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
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
@Table(name = "pres_plan_programatico", schema = "presupuesto")
@NamedQueries({
    @NamedQuery(name = "PresPlanProgramatico.findAll", query = "SELECT p FROM PresPlanProgramatico p"),
    @NamedQuery(name = "PresPlanProgramatico.findById", query = "SELECT p FROM PresPlanProgramatico p WHERE p.id = :id"),
    @NamedQuery(name = "PresPlanProgramatico.findByCodigo", query = "SELECT p FROM PresPlanProgramatico p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "PresPlanProgramatico.findByActivo", query = "SELECT p FROM PresPlanProgramatico p WHERE p.activo = :activo"),
    @NamedQuery(name = "PresPlanProgramatico.findByFechaCreacion", query = "SELECT p FROM PresPlanProgramatico p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PresPlanProgramatico.findByUsuarioCreacion", query = "SELECT p FROM PresPlanProgramatico p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PresPlanProgramatico.findByFechaModificacion", query = "SELECT p FROM PresPlanProgramatico p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PresPlanProgramatico.findByUsuarioModifica", query = "SELECT p FROM PresPlanProgramatico p WHERE p.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "PresPlanProgramatico.findByConfId", query = "SELECT p FROM PresPlanProgramatico p WHERE p.confId = :confId"),
    @NamedQuery(name = "PresPlanProgramatico.findByDescripcion", query = "SELECT p FROM PresPlanProgramatico p WHERE p.descripcion = :descripcion")})
public class PresPlanProgramatico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "activo")
    private Boolean activo;
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
    @JoinColumn(name = "conf_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanCuentas confId;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private PresPlanProgramatico padre;

    @Transient
    private String codIngreso;
    @Transient
    private String codPadre;

    public PresPlanProgramatico() {
        this.activo = Boolean.TRUE;
        this.estado = Boolean.TRUE;
    }

    public PresPlanProgramatico(Long id, String codigo, String descripcion, Boolean activo, Date fechaCreacion,
            String usuarioCreacion, Date fechaModificacion, String usuarioModifica, PlanCuentas confId, PresPlanProgramatico padre, String codIngreso) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
        this.confId = confId;
        this.padre = padre;
        this.codIngreso = codIngreso;
    }

    public PresPlanProgramatico(Long id) {
        this.id = id;
    }

    public PresPlanProgramatico(Long idestructura, String codestructura, String nomestructura) {
        this.id = idestructura;
        this.codigo = codestructura;
        this.descripcion = nomestructura;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public PlanCuentas getConfId() {
        return confId;
    }

    public void setConfId(PlanCuentas confId) {
        this.confId = confId;
    }

    public PresPlanProgramatico getPadre() {
        return padre;
    }

    public void setPadre(PresPlanProgramatico padre) {
        this.padre = padre;
    }

    public String getCodIngreso() {
        if (padre != null) {
            int aux = codigo.length() - padre.getCodigo().length();
            if (aux > 0) {
                codIngreso = codigo.substring(padre.getCodigo().length()).replace(confId.getCaracter(), "");
            }
        } else {
            codIngreso = codigo;
        }
        return codIngreso;
    }

    public void setCodIngreso(String codIngreso) {
        this.codIngreso = codIngreso;
    }

    public String getCodPadre() {
        if (padre != null) {
            codPadre = padre.getCodigo();
        }
        return codPadre;
    }

    public void setCodPadre(String codPadre) {
        this.codPadre = codPadre;
    }

    public String returnIngreso() {
        return codIngreso;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof PresPlanProgramatico)) {
            return false;
        }
        PresPlanProgramatico other = (PresPlanProgramatico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico[ id=" + id + " ]";
    }

}
