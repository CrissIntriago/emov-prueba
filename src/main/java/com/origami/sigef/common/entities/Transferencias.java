/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "transferencias", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "Transferencias.findAll", query = "SELECT t FROM Transferencias t")
    , @NamedQuery(name = "Transferencias.findById", query = "SELECT t FROM Transferencias t WHERE t.id = :id")
    , @NamedQuery(name = "Transferencias.findByDescripcion", query = "SELECT t FROM Transferencias t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Transferencias.findByPeriodo", query = "SELECT t FROM Transferencias t WHERE t.periodo = :periodo")
    , @NamedQuery(name = "Transferencias.findByFechaAfectacion", query = "SELECT t FROM Transferencias t WHERE t.fechaAfectacion = :fechaAfectacion")
    , @NamedQuery(name = "Transferencias.findByNumReferencia", query = "SELECT t FROM Transferencias t WHERE t.numReferencia = :numReferencia")
    , @NamedQuery(name = "Transferencias.findByLocalidad", query = "SELECT t FROM Transferencias t WHERE t.localidad = :localidad")
    , @NamedQuery(name = "Transferencias.findByCtaCteBceIp", query = "SELECT t FROM Transferencias t WHERE t.ctaCteBceIp = :ctaCteBceIp")
    , @NamedQuery(name = "Transferencias.findByNombreInstitucion", query = "SELECT t FROM Transferencias t WHERE t.nombreInstitucion = :nombreInstitucion")
    , @NamedQuery(name = "Transferencias.findByCorresponsal", query = "SELECT t FROM Transferencias t WHERE t.corresponsal = :corresponsal")
    , @NamedQuery(name = "Transferencias.findByCtaRotativa", query = "SELECT t FROM Transferencias t WHERE t.ctaRotativa = :ctaRotativa")
    , @NamedQuery(name = "Transferencias.findByCtaCteBceCorresponsal", query = "SELECT t FROM Transferencias t WHERE t.ctaCteBceCorresponsal = :ctaCteBceCorresponsal")
    , @NamedQuery(name = "Transferencias.findByNombreCorresponsal", query = "SELECT t FROM Transferencias t WHERE t.nombreCorresponsal = :nombreCorresponsal")
    , @NamedQuery(name = "Transferencias.findByUsuarioCreacion", query = "SELECT t FROM Transferencias t WHERE t.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "Transferencias.findByFechaCreacion", query = "SELECT t FROM Transferencias t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Transferencias.findByUsuarioModificacion", query = "SELECT t FROM Transferencias t WHERE t.usuarioModificacion = :usuarioModificacion")
    , @NamedQuery(name = "Transferencias.findByFechaModificacion", query = "SELECT t FROM Transferencias t WHERE t.fechaModificacion = :fechaModificacion")})
public class Transferencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_afectacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAfectacion;
    @Column(name = "num_referencia")
    private BigInteger numReferencia;
    @Column(name = "localidad")
    private String localidad;
    @JoinColumn(name = "responsable_1", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor responsable1;
    @JoinColumn(name = "responsable_2", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor responsable2;
    @Column(name = "cta_cte_bce_ip")
    private String ctaCteBceIp;
    @Column(name = "nombre_institucion")
    private String nombreInstitucion;
    @Column(name = "corresponsal")
    private Boolean corresponsal;
    @Column(name = "cta_rotativa")
    private String ctaRotativa;
    @Column(name = "cta_cte_bce_corresponsal")
    private String ctaCteBceCorresponsal;
    @Column(name = "nombre_corresponsal")
    private String nombreCorresponsal;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;
    @Column(name = "estado_transferencia")
    private String estadoTransferencia;
    @Column(name = "fecha_acreditacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAcreditacion;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAnulacion;

    public Transferencias() {

    }
    
    public Transferencias(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Transferencias(Long id) {
        this.id = id;
    }

    public Transferencias(Long id, String usuarioCreacion, Date fechaCreacion) {
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

    public BigInteger getNumReferencia() {
        return numReferencia;
    }

    public void setNumReferencia(BigInteger numReferencia) {
        this.numReferencia = numReferencia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Servidor getResponsable1() {
        return responsable1;
    }

    public void setResponsable1(Servidor responsable1) {
        this.responsable1 = responsable1;
    }

    public Servidor getResponsable2() {
        return responsable2;
    }

    public void setResponsable2(Servidor responsable2) {
        this.responsable2 = responsable2;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transferencias)) {
            return false;
        }
        Transferencias other = (Transferencias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Transferencias[ id=" + id + " ]";
    }

}
