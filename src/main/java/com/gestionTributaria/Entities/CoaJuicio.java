/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "coa_juicio", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaJuicio.findAll", query = "SELECT c FROM CoaJuicio c"),
    @NamedQuery(name = "CoaJuicio.findById", query = "SELECT c FROM CoaJuicio c WHERE c.id = :id"),
    @NamedQuery(name = "CoaJuicio.findByAnioJuicio", query = "SELECT c FROM CoaJuicio c WHERE c.anioJuicio = :anioJuicio"),
    @NamedQuery(name = "CoaJuicio.findByFechaEdicion", query = "SELECT c FROM CoaJuicio c WHERE c.fechaEdicion = :fechaEdicion"),
    @NamedQuery(name = "CoaJuicio.findByFechaIngreso", query = "SELECT c FROM CoaJuicio c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CoaJuicio.findByFechaJuicio", query = "SELECT c FROM CoaJuicio c WHERE c.fechaJuicio = :fechaJuicio"),
    @NamedQuery(name = "CoaJuicio.findByEstado", query = "SELECT c FROM CoaJuicio c WHERE c.estado = :estado"),
    @NamedQuery(name = "CoaJuicio.findByNumeroJuicio", query = "SELECT c FROM CoaJuicio c WHERE c.numeroJuicio = :numeroJuicio"),
    @NamedQuery(name = "CoaJuicio.findByObservacion", query = "SELECT c FROM CoaJuicio c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "CoaJuicio.findByTipoJuicio", query = "SELECT c FROM CoaJuicio c WHERE c.tipoJuicio = :tipoJuicio"),
    @NamedQuery(name = "CoaJuicio.findByTotalDeuda", query = "SELECT c FROM CoaJuicio c WHERE c.totalDeuda = :totalDeuda"),
    @NamedQuery(name = "CoaJuicio.findByTramite", query = "SELECT c FROM CoaJuicio c WHERE c.tramite = :tramite"),
    @NamedQuery(name = "CoaJuicio.findByUsuarioAsignado", query = "SELECT c FROM CoaJuicio c WHERE c.usuarioAsignado = :usuarioAsignado"),
    @NamedQuery(name = "CoaJuicio.findByUsuarioEdicion", query = "SELECT c FROM CoaJuicio c WHERE c.usuarioEdicion = :usuarioEdicion"),
    @NamedQuery(name = "CoaJuicio.findByUsuarioIngreso", query = "SELECT c FROM CoaJuicio c WHERE c.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "CoaJuicio.findByIdPersonaAsigando", query = "SELECT c FROM CoaJuicio c WHERE c.idPersonaAsigando = :idPersonaAsigando"),
    @NamedQuery(name = "CoaJuicio.findByCodigoJuicio", query = "SELECT c FROM CoaJuicio c WHERE c.codigoJuicio = :codigoJuicio"),
    @NamedQuery(name = "CoaJuicio.findByNumLiquidacion", query = "SELECT c FROM CoaJuicio c WHERE c.numLiquidacion = :numLiquidacion"),
    @NamedQuery(name = "CoaJuicio.findByCodigoUsuarioMigracionFail", query = "SELECT c FROM CoaJuicio c WHERE c.codigoUsuarioMigracionFail = :codigoUsuarioMigracionFail"),
    @NamedQuery(name = "CoaJuicio.findByCedulaAbogadoMigracionFail", query = "SELECT c FROM CoaJuicio c WHERE c.cedulaAbogadoMigracionFail = :cedulaAbogadoMigracionFail")})
public class CoaJuicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_juicio")
    private Integer anioJuicio;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_juicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaJuicio;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "numero_juicio")
    private Integer numeroJuicio;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "tipo_juicio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenTipoLiquidacion tipoJuicio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_deuda")
    private BigDecimal totalDeuda;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne
    private HistoricoTramites tramite;
    @JoinColumn(name = "usuario_asignado", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioAsignado;
    @Size(max = 100)
    @Column(name = "usuario_edicion")
    private String usuarioEdicion;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "id_persona_asigando")
    private BigInteger idPersonaAsigando;
    @Size(max = 250)
    @Column(name = "codigo_juicio")
    private String codigoJuicio;
    @Column(name = "num_liquidacion")
    private BigInteger numLiquidacion;
    @Size(max = 2147483647)
    @Column(name = "codigo_usuario_migracion_fail")
    private String codigoUsuarioMigracionFail;
    @Size(max = 2147483647)
    @Column(name = "cedula_abogado_migracion_fail")
    private String cedulaAbogadoMigracionFail;
    @OneToMany(mappedBy = "juicio", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CoaJuicioNotificacion> coaJuicioNotificacionList;
    @JoinColumn(name = "abogado_juicio", referencedColumnName = "id")
    @ManyToOne
    private CoaAbogado abogadoJuicio;
    @JoinColumn(name = "estado_juicio", referencedColumnName = "id")
    @ManyToOne
    private CoaEstadoJuicio estadoJuicio;
    @OneToMany(mappedBy = "convenioPago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CoaJuicio> coaJuicioList;
    @JoinColumn(name = "convenio_pago", referencedColumnName = "id")
    @ManyToOne
    private CoaJuicio convenioPago;
    @OneToMany(mappedBy = "juicio", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CoaJuicioPredio> coaJuicioPredioList;
    @Column(name = "notificaciones")
    private Boolean notificaciones;
    @JoinColumn(name = "solicitud_exoneracion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FnSolicitudExoneracion solicitudExoneracion;
    @Column(name = "pagado_por_convernio")
    private Boolean pagadoPorConvenio;

    public CoaJuicio() {
    }

    public CoaJuicio(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioJuicio() {
        return anioJuicio;
    }

    public void setAnioJuicio(Integer anioJuicio) {
        this.anioJuicio = anioJuicio;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaJuicio() {
        return fechaJuicio;
    }

    public void setFechaJuicio(Date fechaJuicio) {
        this.fechaJuicio = fechaJuicio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getNumeroJuicio() {
        return numeroJuicio;
    }

    public void setNumeroJuicio(Integer numeroJuicio) {
        this.numeroJuicio = numeroJuicio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public FinaRenTipoLiquidacion getTipoJuicio() {
        return tipoJuicio;
    }

    public void setTipoJuicio(FinaRenTipoLiquidacion tipoJuicio) {
        this.tipoJuicio = tipoJuicio;
    }

    public Usuarios getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(Usuarios usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public BigDecimal getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(BigDecimal totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public String getUsuarioEdicion() {
        return usuarioEdicion;
    }

    public void setUsuarioEdicion(String usuarioEdicion) {
        this.usuarioEdicion = usuarioEdicion;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public BigInteger getIdPersonaAsigando() {
        return idPersonaAsigando;
    }

    public void setIdPersonaAsigando(BigInteger idPersonaAsigando) {
        this.idPersonaAsigando = idPersonaAsigando;
    }

    public String getCodigoJuicio() {
        return codigoJuicio;
    }

    public void setCodigoJuicio(String codigoJuicio) {
        this.codigoJuicio = codigoJuicio;
    }

    public BigInteger getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(BigInteger numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }

    public String getCodigoUsuarioMigracionFail() {
        return codigoUsuarioMigracionFail;
    }

    public void setCodigoUsuarioMigracionFail(String codigoUsuarioMigracionFail) {
        this.codigoUsuarioMigracionFail = codigoUsuarioMigracionFail;
    }

    public String getCedulaAbogadoMigracionFail() {
        return cedulaAbogadoMigracionFail;
    }

    public void setCedulaAbogadoMigracionFail(String cedulaAbogadoMigracionFail) {
        this.cedulaAbogadoMigracionFail = cedulaAbogadoMigracionFail;
    }

    @XmlTransient
    public List<CoaJuicioNotificacion> getCoaJuicioNotificacionList() {
        return coaJuicioNotificacionList;
    }

    public void setCoaJuicioNotificacionList(List<CoaJuicioNotificacion> coaJuicioNotificacionList) {
        this.coaJuicioNotificacionList = coaJuicioNotificacionList;
    }

    public CoaAbogado getAbogadoJuicio() {
        return abogadoJuicio;
    }

    public void setAbogadoJuicio(CoaAbogado abogadoJuicio) {
        this.abogadoJuicio = abogadoJuicio;
    }

    public CoaEstadoJuicio getEstadoJuicio() {
        return estadoJuicio;
    }

    public void setEstadoJuicio(CoaEstadoJuicio estadoJuicio) {
        this.estadoJuicio = estadoJuicio;
    }

    @XmlTransient
    public List<CoaJuicio> getCoaJuicioList() {
        return coaJuicioList;
    }

    public void setCoaJuicioList(List<CoaJuicio> coaJuicioList) {
        this.coaJuicioList = coaJuicioList;
    }

    public CoaJuicio getConvenioPago() {
        return convenioPago;
    }

    public void setConvenioPago(CoaJuicio convenioPago) {
        this.convenioPago = convenioPago;
    }

    public List<CoaJuicioPredio> getCoaJuicioPredioList() {
        return coaJuicioPredioList;
    }

    public Boolean getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(Boolean notificaciones) {
        this.notificaciones = notificaciones;
    }

    public void setCoaJuicioPredioList(List<CoaJuicioPredio> coaJuicioPredioList) {
        this.coaJuicioPredioList = coaJuicioPredioList;
    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

    public Boolean getPagadoPorConvenio() {
        return pagadoPorConvenio;
    }

    public void setPagadoPorConvenio(Boolean pagadoPorConvenio) {
        this.pagadoPorConvenio = pagadoPorConvenio;
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
        if (!(object instanceof CoaJuicio)) {
            return false;
        }
        CoaJuicio other = (CoaJuicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CoaJuicio{" + "id=" + id + '}';
    }

}
