/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author CRISS
 */
@Entity
@Table(name = "catalogo_proforma_presupuesto")
@NamedQueries({
    @NamedQuery(name = "CatalogoProformaPresupuesto.findAll", query = "SELECT c FROM CatalogoProformaPresupuesto c")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findById", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.id = :id")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByDescripcion", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.descripcion = :descripcion")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByTotal", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.total = :total")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByAprobado", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.aprobado = :aprobado")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByEstado", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.estado = :estado")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByFechaCreacion", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByUsuarioCreacion", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByFechaModificacion", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.fechaModificacion = :fechaModificacion")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByUsuarioModifica", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.usuarioModifica = :usuarioModifica")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByTipoFlujo", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.tipoFlujo = :tipoFlujo")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByPeriodo", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.periodo = :periodo")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByFechaAprobacion", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.fechaAprobacion = :fechaAprobacion")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findPeriodosAprobados", query = "SELECT p FROM CatalogoProformaPresupuesto p where p.estado=?1 and p.aprobado=?2")
    ,
    @NamedQuery(name = "CatalogoProformaPresupuesto.findByNumResolucionActa", query = "SELECT c FROM CatalogoProformaPresupuesto c WHERE c.numResolucionActa = :numResolucionActa")})

public class CatalogoProformaPresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @Basic(optional = false)
    @NotNull
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
    @Column(name = "tipo_flujo")
    private Boolean tipoFlujo;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Size(max = 100)
    @Column(name = "num_resolucion_acta")
    private String numResolucionActa;
    @JoinColumn(name = "periodo_catalogo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private MasterCatalogo periodoCatalogo;

    public CatalogoProformaPresupuesto() {
    }

    public CatalogoProformaPresupuesto(Long id) {
        this.id = id;
    }

    public CatalogoProformaPresupuesto(Long id, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica, short periodo) {
        this.id = id;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
        this.periodo = periodo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
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

    public Boolean getTipoFlujo() {
        return tipoFlujo;
    }

    public void setTipoFlujo(Boolean tipoFlujo) {
        this.tipoFlujo = tipoFlujo;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getNumResolucionActa() {
        return numResolucionActa;
    }

    public void setNumResolucionActa(String numResolucionActa) {
        this.numResolucionActa = numResolucionActa;
    }

    public MasterCatalogo getPeriodoCatalogo() {
        return periodoCatalogo;
    }

    public void setPeriodoCatalogo(MasterCatalogo periodoCatalogo) {
        this.periodoCatalogo = periodoCatalogo;
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
        if (!(object instanceof CatalogoProformaPresupuesto)) {
            return false;
        }
        CatalogoProformaPresupuesto other = (CatalogoProformaPresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CatalogoProformaPresupuesto[ id=" + id + " ]";
    }

}
