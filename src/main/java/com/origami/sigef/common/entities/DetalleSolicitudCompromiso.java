/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Suarez
 */
@Entity
@Table(name = "detalle_solicitud_compromiso", schema = "certificacion_presupuestaria_anual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleSolicitudCompromiso.findAll", query = "SELECT d FROM DetalleSolicitudCompromiso d"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findById", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByActividadProducto", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.actividadProducto = :actividadProducto"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByPresupuesto", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.presupuesto = :presupuesto"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByMontoDisponible", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.montoDisponible = :montoDisponible"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByMontoSolicitado", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.montoSolicitado = :montoSolicitado"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByEstado", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.estado = TRUE AND d.periodo= ?1"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByPeriodo", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.periodo = :periodo"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByDistintoPeriodo", query = "SELECT DISTINCT d.presupuesto FROM DetalleSolicitudCompromiso d WHERE d.periodo = :periodo"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByUsuarioCreacion", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByFechaCreacion", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByUsuarioModificacion", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "DetalleSolicitudCompromiso.findByFechaModifcacion", query = "SELECT d FROM DetalleSolicitudCompromiso d WHERE d.fechaModifcacion = :fechaModifcacion")})
public class DetalleSolicitudCompromiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_disponible")
    private BigDecimal montoDisponible;
    @Column(name = "monto_solicitado")
    private BigDecimal montoSolicitado;
    @Column(name = "monto_comprometido")
    private BigDecimal montoComprometido;
    @Column(name = "liquidado")
    private BigDecimal liquidado;
    @Column(name = "ejecutado")
    private BigDecimal ejecutado;
    @Column(name = "liberado")
    private BigDecimal liberado;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modifcacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifcacion;
    
    @Column(name = "monto_devengar")
    private BigDecimal montoDevengar;
    
    @JoinColumn(name = "solicitud", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso solicitud;
    //@Column(name = "re_distributivo")
    //private BigInteger refDistributivo;
    @JoinColumn(name = "actividad_producto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Producto actividadProducto;
//     @JoinColumn(name = "distributivo", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private PartidasDistributivo distributivo;
    @JoinColumn(name = "partidas_directa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ProformaPresupuestoPlanificado partidasDirecta;

    @JoinColumn(name = "presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Presupuesto presupuesto;

    //NUEVO
    @JoinColumn(name = "re_distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThCargoRubros refDistributivo;
    
    

    public DetalleSolicitudCompromiso() {
        this.montoSolicitado = BigDecimal.ZERO;
        this.montoDisponible = BigDecimal.ZERO;
        this.montoComprometido = BigDecimal.ZERO;
        this.ejecutado = BigDecimal.ZERO;
        this.liquidado = BigDecimal.ZERO;
        this.liberado = BigDecimal.ZERO;
        this.montoDevengar = BigDecimal.ZERO;

    }

    public DetalleSolicitudCompromiso(Long id) {
        this.id = id;
    }

    public DetalleSolicitudCompromiso(Long id, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModifcacion) {
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModifcacion = fechaModifcacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontoDisponible() {
        return montoDisponible;
    }

    public void setMontoDisponible(BigDecimal montoDisponible) {
        this.montoDisponible = montoDisponible;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
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

    public Date getFechaModifcacion() {
        return fechaModifcacion;
    }

    public void setFechaModifcacion(Date fechaModifcacion) {
        this.fechaModifcacion = fechaModifcacion;
    }

    public SolicitudReservaCompromiso getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudReservaCompromiso solicitud) {
        this.solicitud = solicitud;
    }

    public Producto getActividadProducto() {
        return actividadProducto;
    }

    public void setActividadProducto(Producto actividadProducto) {
        this.actividadProducto = actividadProducto;
    }

//    public PartidasDistributivo getDistributivo() {
//        return distributivo;
//    }
//
//    public void setDistributivo(PartidasDistributivo distributivo) {
//        this.distributivo = distributivo;
//    }
    public ProformaPresupuestoPlanificado getPartidasDirecta() {
        return partidasDirecta;
    }

    public void setPartidasDirecta(ProformaPresupuestoPlanificado partidasDirecta) {
        this.partidasDirecta = partidasDirecta;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public BigDecimal getMontoComprometido() {
        return montoComprometido;
    }

    public void setMontoComprometido(BigDecimal montoComprometido) {
        this.montoComprometido = montoComprometido;
    }

    public BigDecimal getLiquidado() {
        return liquidado;
    }

    public void setLiquidado(BigDecimal liquidado) {
        this.liquidado = liquidado;
    }

    public BigDecimal getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(BigDecimal ejecutado) {
        this.ejecutado = ejecutado;
    }

    public ThCargoRubros getRefDistributivo() {
        return refDistributivo;
    }

    public void setRefDistributivo(ThCargoRubros refDistributivo) {
        this.refDistributivo = refDistributivo;
    }

    public void setLiberado(BigDecimal liberado) {
        this.liberado = liberado;
    }

    public BigDecimal getLiberado() {
        return liberado;
    }

    public BigDecimal getMontoDevengar() {
        return montoDevengar;
    }

    public void setMontoDevengar(BigDecimal montoDevengar) {
        this.montoDevengar = montoDevengar;
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
        if (!(object instanceof DetalleSolicitudCompromiso)) {
            return false;
        }
        DetalleSolicitudCompromiso other = (DetalleSolicitudCompromiso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tutorial.jsf.venta.entities.DetalleSolicitudCompromiso[ id=" + id + " ]";
    }

}
