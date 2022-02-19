/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "comprobante_pago", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ComprobantePago.findAll", query = "SELECT c FROM ComprobantePago c")
    , @NamedQuery(name = "ComprobantePago.findById", query = "SELECT c FROM ComprobantePago c WHERE c.id = :id")
    , @NamedQuery(name = "ComprobantePago.findByNumComprobante", query = "SELECT c FROM ComprobantePago c WHERE c.numComprobante = :numComprobante")
    , @NamedQuery(name = "ComprobantePago.findByFechaComprobante", query = "SELECT c FROM ComprobantePago c WHERE c.fechaComprobante = :fechaComprobante")
    , @NamedQuery(name = "ComprobantePago.findByEstado", query = "SELECT c FROM ComprobantePago c WHERE c.estado = :estado")
    , @NamedQuery(name = "ComprobantePago.findByDetalle", query = "SELECT c FROM ComprobantePago c WHERE c.detalle = :detalle")
    , @NamedQuery(name = "ComprobantePago.findByReservaCompromiso", query = "SELECT c FROM ComprobantePago c WHERE c.reservaCompromiso = :reservaCompromiso")
    , @NamedQuery(name = "ComprobantePago.findByPeriodo", query = "SELECT c FROM ComprobantePago c WHERE c.periodo = :periodo")
    , @NamedQuery(name = "ComprobantePago.findByFechaCreacion", query = "SELECT c FROM ComprobantePago c WHERE c.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "ComprobantePago.findByUsuarioCreacion", query = "SELECT c FROM ComprobantePago c WHERE c.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "ComprobantePago.findByFechaModificacion", query = "SELECT c FROM ComprobantePago c WHERE c.fechaModificacion = :fechaModificacion")
    , @NamedQuery(name = "ComprobantePago.findByUsuarioModificacion", query = "SELECT c FROM ComprobantePago c WHERE c.usuarioModificacion = :usuarioModificacion")})
public class ComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Column(name = "fecha_comprobante")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaComprobante;
    @Column(name = "estado")
    private String estado;
    @Column(name = "detalle")
    private String detalle;
    @JoinColumn(name = "reserva_compromiso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso reservaCompromiso;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;
    @Basic(optional = false)
    @Column(name = "periodo")
    private short periodo;
    @Size(max = 100)
    @Column(name = "tipo_comprobante")
    private String tipoComprobante;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;

    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @JoinColumn(name = "cuenta_bancaria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaBancaria cuentaBancaria;
    @JoinColumn(name = "enlace", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem enlace;
    @OneToMany(mappedBy = "comprobantePago", fetch = FetchType.LAZY)
    private List<BeneficiarioComprobantePago> beneficiarioComprobantePagos;

    public ComprobantePago() {
    }

    public ComprobantePago(Long id) {
        this.id = id;
    }

    public ComprobantePago(Long id, short periodo, Date fechaCreacion, String usuarioCreacion) {
        this.id = id;
        this.periodo = periodo;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BeneficiarioComprobantePago> getBeneficiarioComprobantePagos() {
        return beneficiarioComprobantePagos;
    }

    public void setBeneficiarioComprobantePagos(List<BeneficiarioComprobantePago> beneficiarioComprobantePagos) {
        this.beneficiarioComprobantePagos = beneficiarioComprobantePagos;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public Date getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(Date fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
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

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public CatalogoItem getEnlace() {
        return enlace;
    }

    public void setEnlace(CatalogoItem enlace) {
        this.enlace = enlace;
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
        if (!(object instanceof ComprobantePago)) {
            return false;
        }
        ComprobantePago other = (ComprobantePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ComprobantePago[ id=" + id + " ]";
    }

}
