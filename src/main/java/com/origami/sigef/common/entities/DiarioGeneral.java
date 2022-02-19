/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "diario_general", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "DiarioGeneral.findAll", query = "SELECT d FROM DiarioGeneral d"),
    @NamedQuery(name = "DiarioGeneral.findById", query = "SELECT d FROM DiarioGeneral d WHERE d.id = :id"),
    @NamedQuery(name = "DiarioGeneral.findByNumeroTransaccion", query = "SELECT d FROM DiarioGeneral d WHERE d.numeroTransaccion = :numeroTransaccion"),
    @NamedQuery(name = "DiarioGeneral.findByFechaElaboracion", query = "SELECT d FROM DiarioGeneral d WHERE d.fechaElaboracion = :fechaElaboracion"),
    @NamedQuery(name = "DiarioGeneral.findByObservacion", query = "SELECT d FROM DiarioGeneral d WHERE d.observacion = :observacion"),
    @NamedQuery(name = "DiarioGeneral.findByFechaCreacion", query = "SELECT d FROM DiarioGeneral d WHERE d.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "DiarioGeneral.findByUsuarioCreacion", query = "SELECT d FROM DiarioGeneral d WHERE d.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "DiarioGeneral.findByFechaModificacion", query = "SELECT d FROM DiarioGeneral d WHERE d.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "DiarioGeneral.findByUsuarioModificacion", query = "SELECT d FROM DiarioGeneral d WHERE d.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "DiarioGeneral.findByEstado", query = "SELECT d FROM DiarioGeneral d WHERE d.estado = :estado"),
    @NamedQuery(name = "DiarioGeneral.findByPeriodo", query = "SELECT d FROM DiarioGeneral d WHERE d.periodo = :periodo"),
    @NamedQuery(name = "DiarioGeneral.findByClase", query = "SELECT d FROM DiarioGeneral d WHERE d.clase = :clase"),
    @NamedQuery(name = "DiarioGeneral.findByTipo", query = "SELECT d FROM DiarioGeneral d WHERE d.tipo = :tipo")})
public class DiarioGeneral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_transaccion")
    private BigInteger numeroTransaccion;
    @Basic(optional = false)
    @Column(name = "fecha_elaboracion")
    @Temporal(TemporalType.DATE)
    private Date fechaElaboracion;
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @JoinColumn(name = "clase", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clase;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @JoinColumn(name = "certificaciones_presupuestario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso certificacionesPresupuestario;
    @Column(name = "estado_transaccion")
    private String estadoTransaccion;
    @Column(name = "retencion")
    private Boolean retencion;
    @Column(name = "retenido")
    private Boolean retenido;
    @Basic
    @Column(name = "comprobante_pago")
    private Boolean comprobantePago;
    @Transient
    private Cliente otorgante;
    @Transient
    private Cliente receptor;
    @Column(name = "total_debe")
    private BigDecimal totalDebe;
    @Column(name = "total_haber")
    private BigDecimal totalHaber;
    @Column(name = "varios_beneficiarios")
    private Boolean variosBeneficiarios = false;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente beneficiario;
    @Column(name = "tipo_beneficiario")
    private Boolean tipoBeneficiario;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAnulacion;
    @Column(name = "estado_diario")
    private String estadoDiario;
    @JoinColumn(name = "enlace", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem enlace;
    @JoinColumn(name = "ref_diario_anulado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral refDiarioAnulado;
    @OneToMany(mappedBy = "diarioGeneralEmision", fetch = FetchType.LAZY)
    private List<CorteOrdenCobro> corteOrdenCobroList;

    public DiarioGeneral() {
        this.retencion = Boolean.FALSE;
        this.comprobantePago = Boolean.FALSE;
    }

    public DiarioGeneral(Long id) {
        this.id = id;
    }

    public DiarioGeneral(Long id, Date fechaElaboracion, Date fechaCreacion, String usuarioCreacion) {
        this.id = id;
        this.fechaElaboracion = fechaElaboracion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.comprobantePago = Boolean.FALSE;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getRetencion() {
        return retencion;
    }

    public void setRetencion(Boolean retencion) {
        this.retencion = retencion;
    }

    public BigInteger getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(BigInteger numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public CatalogoItem getClase() {
        return clase;
    }

    public void setClase(CatalogoItem clase) {
        this.clase = clase;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public SolicitudReservaCompromiso getCertificacionesPresupuestario() {
        return certificacionesPresupuestario;
    }

    public void setCertificacionesPresupuestario(SolicitudReservaCompromiso certificacionesPresupuestario) {
        this.certificacionesPresupuestario = certificacionesPresupuestario;
    }

    public String getEstadoTransaccion() {
        return estadoTransaccion;
    }

    public void setEstadoTransaccion(String estadoTransaccion) {
        this.estadoTransaccion = estadoTransaccion;
    }

    public Boolean getRetenido() {
        return retenido;
    }

    public void setRetenido(Boolean retenido) {
        this.retenido = retenido;
    }

    public Boolean getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(Boolean comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public Cliente getOtorgante() {
        return otorgante;
    }

    public void setOtorgante(Cliente otorgante) {
        this.otorgante = otorgante;
    }

    public Cliente getReceptor() {
        return receptor;
    }

    public void setReceptor(Cliente receptor) {
        this.receptor = receptor;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public Boolean getVariosBeneficiarios() {
        return variosBeneficiarios;
    }

    public void setVariosBeneficiarios(Boolean variosBeneficiarios) {
        this.variosBeneficiarios = variosBeneficiarios;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getEstadoDiario() {
        return estadoDiario;
    }

    public void setEstadoDiario(String estadoDiario) {
        this.estadoDiario = estadoDiario;
    }

    public CatalogoItem getEnlace() {
        return enlace;
    }

    public void setEnlace(CatalogoItem enlace) {
        this.enlace = enlace;
    }

    public DiarioGeneral getRefDiarioAnulado() {
        return refDiarioAnulado;
    }

    public void setRefDiarioAnulado(DiarioGeneral refDiarioAnulado) {
        this.refDiarioAnulado = refDiarioAnulado;
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
        if (!(object instanceof DiarioGeneral)) {
            return false;
        }
        DiarioGeneral other = (DiarioGeneral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DiarioGeneral[ id=" + id + " ]";
    }

}
