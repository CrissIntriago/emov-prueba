/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "reforma_traspaso", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReformaTraspaso.findAll", query = "SELECT r FROM ReformaTraspaso r"),
    @NamedQuery(name = "ReformaTraspaso.findById", query = "SELECT r FROM ReformaTraspaso r WHERE r.id = ?1"),
    @NamedQuery(name = "ReformaTraspaso.findByPeriodo", query = "SELECT r FROM ReformaTraspaso r WHERE r.periodo = :periodo"),
    @NamedQuery(name = "ReformaTraspaso.findByCodigo", query = "SELECT r FROM ReformaTraspaso r WHERE r.codigo = :codigo"),
    @NamedQuery(name = "ReformaTraspaso.findByNumeracion", query = "SELECT r FROM ReformaTraspaso r WHERE r.numeracion = :numeracion"),
    @NamedQuery(name = "ReformaTraspaso.findByTipo", query = "SELECT r FROM ReformaTraspaso r WHERE r.tipo = :tipo"),
    @NamedQuery(name = "ReformaTraspaso.findByFechaTraspasoReforma", query = "SELECT r FROM ReformaTraspaso r WHERE r.fechaTraspasoReforma = :fechaTraspasoReforma"),
    @NamedQuery(name = "ReformaTraspaso.findByEstado", query = "SELECT r FROM ReformaTraspaso r WHERE r.estado = :estado"),
    @NamedQuery(name = "ReformaTraspaso.findByFechaCreacion", query = "SELECT r FROM ReformaTraspaso r WHERE r.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ReformaTraspaso.findByUsuarioCreacion", query = "SELECT r FROM ReformaTraspaso r WHERE r.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ReformaTraspaso.findByFechaModificacion", query = "SELECT r FROM ReformaTraspaso r WHERE r.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ReformaTraspaso.findByUsuarioModificacion", query = "SELECT r FROM ReformaTraspaso r WHERE r.usuarioModificacion = :usuarioModificacion")})
public class ReformaTraspaso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periodo")
    private Short periodo;
    @Size(max = 100)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "numeracion")
    private Integer numeracion;
    @Column(name = "tipo")
    private Boolean tipo;
    @Column(name = "fecha_traspaso_reforma")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTraspasoReforma;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "estado_reforma", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoReforma;
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
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    
    @JoinColumn(name = "estado_reforma_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoReformaTramite;
    @Column(name = "numero_tramite")
    private Short numeroTramite;
    @JoinColumn(name = "unidad_requiriente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadRequiriente;
    
    @Column(name = "valor_informe")
    private String valorInforme;
    @Column(name = "valor_formulario_pdi")
    private String valorFormularioPdi;

    public ReformaTraspaso() {
    }

    public ReformaTraspaso(Long id) {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public Date getFechaTraspasoReforma() {
        return fechaTraspasoReforma;
    }

    public void setFechaTraspasoReforma(Date fechaTraspasoReforma) {
        this.fechaTraspasoReforma = fechaTraspasoReforma;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CatalogoItem getEstadoReforma() {
        return estadoReforma;
    }

    public void setEstadoReforma(CatalogoItem estadoReforma) {
        this.estadoReforma = estadoReforma;
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

    public CatalogoItem getEstadoReformaTramite() {
        return estadoReformaTramite;
    }

    public void setEstadoReformaTramite(CatalogoItem estadoReformaTramite) {
        this.estadoReformaTramite = estadoReformaTramite;
    }

    public Short getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Short numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public UnidadAdministrativa getUnidadRequiriente() {
        return unidadRequiriente;
    }

    public void setUnidadRequiriente(UnidadAdministrativa unidadRequiriente) {
        this.unidadRequiriente = unidadRequiriente;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public String getValorInforme() {
        return valorInforme;
    }

    public void setValorInforme(String valorInforme) {
        this.valorInforme = valorInforme;
    }

    public String getValorFormularioPdi() {
        return valorFormularioPdi;
    }

    public void setValorFormularioPdi(String valorFormularioPdi) {
        this.valorFormularioPdi = valorFormularioPdi;
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
        if (!(object instanceof ReformaTraspaso)) {
            return false;
        }
        ReformaTraspaso other = (ReformaTraspaso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Presupuesto.Entity.ReformaTraspaso[ id=" + id + " ]";
    }
    
}
