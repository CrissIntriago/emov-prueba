/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import org.hibernate.annotations.Formula;

/**
 *
 * @author Luis Suarez
 */
@Entity
@Table(name = "solicitud_reserva_compromiso", schema = "certificacion_presupuestaria_anual")

@NamedQueries({
    @NamedQuery(name = "SolicitudReservaCompromiso.findAll", query = "SELECT s FROM SolicitudReservaCompromiso s"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findById", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.id = :id"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByFechaSolicitud", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByUnidadRequiriente", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.unidadRequiriente = :unidadRequiriente"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByEstado", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.estado = :estado"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByUsuarioCreacion", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByFechaCreacion", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByUsuarioModificacion", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "SolicitudReservaCompromiso.findByFechaModificacion", query = "SELECT s FROM SolicitudReservaCompromiso s WHERE s.fechaModificacion = :fechaModificacion")})
public class SolicitudReservaCompromiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Column(name = "fecha_actualizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "secuencial")
    private Integer secuencial;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @JoinColumn(name = "unidad_requiriente", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private UnidadAdministrativa unidadRequiriente;
    @Column(name = "tipo_beneficiario")
    private Boolean tipoBeneficiario;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente beneficiario;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private CatalogoItem estado;
//    @JoinColumn(name = "producto", referencedColumnName = "id")
//    @ManyToOne(optional = true)
//    private Producto producto;
    @JoinColumn(name = "procedimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Procedimiento procedimiento;
    @Column(name = "comprometido")
    private Boolean comprometido;
    @Column(name = "nomina")
    private Boolean nomina;
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    @Column(name = "fecha_contabilizado")
    @Temporal(TemporalType.DATE)
    private Date fechaContabilizado;
    @Column(name = "ruta_reserva")
    private String rutaReserva;
    @Column(name = "ruta_anulada")
    private String rutaAnulada;
    @Column(name = "ruta_modificada")
    private String rutaModificada;
    @Column(name = "ruta_liquidada")
    private String rutaLiquidada;
    @Column(name = "ruta_liberada")
    private String rutaLiberada;
    @Column(name = "ruta_otros")
    private String rutaOtros;
    @Column(name = "antiguo")
    private Boolean antiguo;
    @Column(name = "observacion_anulacion")
    private String observacionAnulacion;

    @Formula("(select concat(LPAD(secuencial::text, 5, '0'), '-',s.periodo) from certificacion_presupuestaria_anual.solicitud_reserva_compromiso s where s.id=id)")
    private String secuancialForFilter;

    public SolicitudReservaCompromiso() {
        this.comprometido = Boolean.FALSE;
        this.nomina = Boolean.FALSE;
        this.contabilizado = Boolean.FALSE;
        this.antiguo = Boolean.FALSE;
    }

    public SolicitudReservaCompromiso(Long id) {
        this.id = id;
    }

    public SolicitudReservaCompromiso(Long id, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion) {
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;

    }

    public String getSecuancialForFilter() {
//        Formatter fmt = new Formatter();
//        this.secuancialForFilter = fmt.format("%05d", secuencial).toString() + "-"+ periodo;
        return secuancialForFilter;
    }

    public void setSecuancialForFilter(String secuancialForFilter) {
        this.secuancialForFilter = secuancialForFilter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
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

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
        this.estado = estado;
    }

    public UnidadAdministrativa getUnidadRequiriente() {
        return unidadRequiriente;
    }

    public void setUnidadRequiriente(UnidadAdministrativa unidadRequiriente) {
        this.unidadRequiriente = unidadRequiriente;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Procedimiento getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(Procedimiento procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Boolean getComprometido() {
        return comprometido;
    }

    public void setComprometido(Boolean comprometido) {
        this.comprometido = comprometido;
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

    public Boolean getNomina() {
        return nomina;
    }

    public void setNomina(Boolean nomina) {
        this.nomina = nomina;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public Date getFechaContabilizado() {
        return fechaContabilizado;
    }

    public void setFechaContabilizado(Date fechaContabilizado) {
        this.fechaContabilizado = fechaContabilizado;
    }

    public String getRutaReserva() {
        return rutaReserva;
    }

    public void setRutaReserva(String rutaReserva) {
        this.rutaReserva = rutaReserva;
    }

    public String getRutaAnulada() {
        return rutaAnulada;
    }

    public void setRutaAnulada(String rutaAnulada) {
        this.rutaAnulada = rutaAnulada;
    }

    public String getRutaModificada() {
        return rutaModificada;
    }

    public void setRutaModificada(String rutaModificada) {
        this.rutaModificada = rutaModificada;
    }

    public String getRutaLiquidada() {
        return rutaLiquidada;
    }

    public void setRutaLiquidada(String rutaLiquidada) {
        this.rutaLiquidada = rutaLiquidada;
    }

    public String getRutaLiberada() {
        return rutaLiberada;
    }

    public void setRutaLiberada(String rutaLiberada) {
        this.rutaLiberada = rutaLiberada;
    }

    public String getRutaOtros() {
        return rutaOtros;
    }

    public void setRutaOtros(String rutaOtros) {
        this.rutaOtros = rutaOtros;
    }

    public Boolean getAntiguo() {
        return antiguo;
    }

    public void setAntiguo(Boolean antiguo) {
        this.antiguo = antiguo;
    }

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
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
        if (!(object instanceof SolicitudReservaCompromiso)) {
            return false;
        }
        SolicitudReservaCompromiso other = (SolicitudReservaCompromiso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tutorial.jsf.venta.entities.SolicitudReservaCompromiso[ id=" + id + " ]";
    }

}
