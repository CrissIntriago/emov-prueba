/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_transferencias", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContTransferencias.findAll", query = "SELECT c FROM ContTransferencias c"),
    @NamedQuery(name = "ContTransferencias.findById", query = "SELECT c FROM ContTransferencias c WHERE c.id = :id"),
    @NamedQuery(name = "ContTransferencias.findByDescripcion", query = "SELECT c FROM ContTransferencias c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "ContTransferencias.findByPeriodo", query = "SELECT c FROM ContTransferencias c WHERE c.periodo = :periodo"),
    @NamedQuery(name = "ContTransferencias.findByFechaAfectacion", query = "SELECT c FROM ContTransferencias c WHERE c.fechaAfectacion = :fechaAfectacion"),
    @NamedQuery(name = "ContTransferencias.findByLocalidad", query = "SELECT c FROM ContTransferencias c WHERE c.localidad = :localidad"),
    @NamedQuery(name = "ContTransferencias.findByCtaCteBceIp", query = "SELECT c FROM ContTransferencias c WHERE c.ctaCteBceIp = :ctaCteBceIp"),
    @NamedQuery(name = "ContTransferencias.findByNombreInstitucion", query = "SELECT c FROM ContTransferencias c WHERE c.nombreInstitucion = :nombreInstitucion"),
    @NamedQuery(name = "ContTransferencias.findByCorresponsal", query = "SELECT c FROM ContTransferencias c WHERE c.corresponsal = :corresponsal"),
    @NamedQuery(name = "ContTransferencias.findByCtaRotativa", query = "SELECT c FROM ContTransferencias c WHERE c.ctaRotativa = :ctaRotativa"),
    @NamedQuery(name = "ContTransferencias.findByCtaCteBceCorresponsal", query = "SELECT c FROM ContTransferencias c WHERE c.ctaCteBceCorresponsal = :ctaCteBceCorresponsal"),
    @NamedQuery(name = "ContTransferencias.findByNombreCorresponsal", query = "SELECT c FROM ContTransferencias c WHERE c.nombreCorresponsal = :nombreCorresponsal"),
    @NamedQuery(name = "ContTransferencias.findByUsuarioCreacion", query = "SELECT c FROM ContTransferencias c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ContTransferencias.findByFechaCreacion", query = "SELECT c FROM ContTransferencias c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ContTransferencias.findByUsuarioModificacion", query = "SELECT c FROM ContTransferencias c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ContTransferencias.findByFechaModificacion", query = "SELECT c FROM ContTransferencias c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ContTransferencias.findByNumReferencia", query = "SELECT c FROM ContTransferencias c WHERE c.numReferencia = :numReferencia"),
    @NamedQuery(name = "ContTransferencias.findByValor", query = "SELECT c FROM ContTransferencias c WHERE c.valor = :valor"),
    @NamedQuery(name = "ContTransferencias.findByEstadoTransferencia", query = "SELECT c FROM ContTransferencias c WHERE c.estadoTransferencia = :estadoTransferencia"),
    @NamedQuery(name = "ContTransferencias.findByFechaAcreditacion", query = "SELECT c FROM ContTransferencias c WHERE c.fechaAcreditacion = :fechaAcreditacion"),
    @NamedQuery(name = "ContTransferencias.findByFechaAnulacion", query = "SELECT c FROM ContTransferencias c WHERE c.fechaAnulacion = :fechaAnulacion"),
    @NamedQuery(name = "ContTransferencias.findByNumeroTramite", query = "SELECT c FROM ContTransferencias c WHERE c.numeroTramite = :numeroTramite")})
public class ContTransferencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_afectacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAfectacion;
    @Size(max = 100)
    @Column(name = "localidad")
    private String localidad;
    @Size(max = 100)
    @Column(name = "cta_cte_bce_ip")
    private String ctaCteBceIp;
    @Size(max = 2147483647)
    @Column(name = "nombre_institucion")
    private String nombreInstitucion;
    @Column(name = "corresponsal")
    private Boolean corresponsal;
    @Size(max = 100)
    @Column(name = "cta_rotativa")
    private String ctaRotativa;
    @Size(max = 100)
    @Column(name = "cta_cte_bce_corresponsal")
    private String ctaCteBceCorresponsal;
    @Size(max = 2147483647)
    @Column(name = "nombre_corresponsal")
    private String nombreCorresponsal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Column(name = "num_referencia")
    private BigInteger numReferencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 50)
    @Column(name = "estado_transferencia")
    private String estadoTransferencia;
    @Column(name = "fecha_acreditacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAcreditacion;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAnulacion;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;
    @JoinColumn(name = "maxima_autoridad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThServidorCargo maximaAutoridad;
    @JoinColumn(name = "responsable_tesoreria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThServidorCargo responsableTesoreria;

    public ContTransferencias() {
        this.fechaAfectacion = new Date();
        this.estadoTransferencia = "REGISTRADO";
    }

    public ContTransferencias(Long id) {
        this.id = id;
    }

    public ContTransferencias(Long id, String usuarioCreacion, Date fechaCreacion) {
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
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

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaAfectacion() {
        return fechaAfectacion;
    }

    public void setFechaAfectacion(Date fechaAfectacion) {
        this.fechaAfectacion = fechaAfectacion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCtaCteBceIp() {
        return ctaCteBceIp;
    }

    public void setCtaCteBceIp(String ctaCteBceIp) {
        this.ctaCteBceIp = ctaCteBceIp;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public Boolean getCorresponsal() {
        return corresponsal;
    }

    public void setCorresponsal(Boolean corresponsal) {
        this.corresponsal = corresponsal;
    }

    public String getCtaRotativa() {
        return ctaRotativa;
    }

    public void setCtaRotativa(String ctaRotativa) {
        this.ctaRotativa = ctaRotativa;
    }

    public String getCtaCteBceCorresponsal() {
        return ctaCteBceCorresponsal;
    }

    public void setCtaCteBceCorresponsal(String ctaCteBceCorresponsal) {
        this.ctaCteBceCorresponsal = ctaCteBceCorresponsal;
    }

    public String getNombreCorresponsal() {
        return nombreCorresponsal;
    }

    public void setNombreCorresponsal(String nombreCorresponsal) {
        this.nombreCorresponsal = nombreCorresponsal;
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

    public BigInteger getNumReferencia() {
        return numReferencia;
    }

    public void setNumReferencia(BigInteger numReferencia) {
        this.numReferencia = numReferencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getEstadoTransferencia() {
        return estadoTransferencia;
    }

    public void setEstadoTransferencia(String estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public ThServidorCargo getMaximaAutoridad() {
        return maximaAutoridad;
    }

    public void setMaximaAutoridad(ThServidorCargo maximaAutoridad) {
        this.maximaAutoridad = maximaAutoridad;
    }

    public ThServidorCargo getResponsableTesoreria() {
        return responsableTesoreria;
    }

    public void setResponsableTesoreria(ThServidorCargo responsableTesoreria) {
        this.responsableTesoreria = responsableTesoreria;
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
        if (!(object instanceof ContTransferencias)) {
            return false;
        }
        ContTransferencias other = (ContTransferencias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContTransferencias[ id=" + id + " ]";
    }

}
