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
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "tipo_rol_beneficios", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "TipoRolBeneficios.findAll", query = "SELECT t FROM TipoRolBeneficios t"),
    @NamedQuery(name = "TipoRolBeneficios.findById", query = "SELECT t FROM TipoRolBeneficios t WHERE t.id = :id"),
    @NamedQuery(name = "TipoRolBeneficios.findByEstado", query = "SELECT t FROM TipoRolBeneficios t WHERE t.estado = :estado"),
    @NamedQuery(name = "TipoRolBeneficios.findByPeriodo", query = "SELECT t FROM TipoRolBeneficios t WHERE t.periodo = ?1 AND t.estado = TRUE"),
    @NamedQuery(name = "TipoRolBeneficios.findByDescripcion", query = "SELECT t FROM TipoRolBeneficios t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoRolBeneficios.findByTipo", query = "SELECT t FROM TipoRolBeneficios t WHERE t.tipo = :tipo"),
    @NamedQuery(name = "TipoRolBeneficios.findByPeriodoDesde", query = "SELECT t FROM TipoRolBeneficios t WHERE t.periodoDesde = :periodoDesde"),
    @NamedQuery(name = "TipoRolBeneficios.findByPeriodoHasta", query = "SELECT t FROM TipoRolBeneficios t WHERE t.periodoHasta = :periodoHasta"),
    @NamedQuery(name = "TipoRolBeneficios.findByFechaCreacion", query = "SELECT t FROM TipoRolBeneficios t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TipoRolBeneficios.findByFechaModificacion", query = "SELECT t FROM TipoRolBeneficios t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "TipoRolBeneficios.findByUsuarioCreacion", query = "SELECT t FROM TipoRolBeneficios t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "TipoRolBeneficios.findByUsuarioModificacion", query = "SELECT t FROM TipoRolBeneficios t WHERE t.usuarioModificacion = :usuarioModificacion")})
public class TipoRolBeneficios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Size(max = 100)
    @Column(length = 100, name = "descripcion")
    private String descripcion;
    @Column(name = "periodo_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodoDesde;
    @Column(name = "periodo_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodoHasta;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;
    @Size(max = 255)
    @Column(name = "descripcion_rol", length = 255)
    private String descripcionRol;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @JoinColumn(name = "estado_aprobacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoAprobacionBen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRolBeneficio")
    private List<BeneficiosDecimoTercero> listBeneficiosDecimoTercero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRolBeneficio")
    private List<BeneficiosDecimoCuarto> listBeneficiosDecimoCuarto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRolBeneficios")
    private List<BeneficiosSindicales> listBeneficiosSindicales;
    @Column(name = "diario_rol_beneficios")
    private Boolean diarioRolBeneficios;
    @JoinColumn(name = "id_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;

    public TipoRolBeneficios() {
        this.diarioRolBeneficios = Boolean.FALSE;
    }

    public Long getId() {
        return id;
    }

    public TipoRolBeneficios(Long id, Boolean estado, Short periodo, String descripcion, Date periodoDesde, Date periodoHasta, Date fechaCreacion, Date fechaModificacion, String usuarioCreacion, String usuarioModificacion) {
        this.id = id;
        this.estado = estado;
        this.periodo = periodo;
        this.descripcion = descripcion;
        this.periodoDesde = periodoDesde;
        this.periodoHasta = periodoHasta;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getPeriodoDesde() {
        return periodoDesde;
    }

    public void setPeriodoDesde(Date periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    public Date getPeriodoHasta() {
        return periodoHasta;
    }

    public void setPeriodoHasta(Date periodoHasta) {
        this.periodoHasta = periodoHasta;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Boolean getDiarioRolBeneficios() {
        return diarioRolBeneficios;
    }

    public void setDiarioRolBeneficios(Boolean diarioRolBeneficios) {
        this.diarioRolBeneficios = diarioRolBeneficios;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public CatalogoItem getEstadoAprobacionBen() {
        return estadoAprobacionBen;
    }

    public void setEstadoAprobacionBen(CatalogoItem estadoAprobacionBen) {
        this.estadoAprobacionBen = estadoAprobacionBen;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
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
        if (!(object instanceof TipoRolBeneficios)) {
            return false;
        }
        TipoRolBeneficios other = (TipoRolBeneficios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ventas.TipoRolBeneficios[ id=" + id + " ]";
    }

}
