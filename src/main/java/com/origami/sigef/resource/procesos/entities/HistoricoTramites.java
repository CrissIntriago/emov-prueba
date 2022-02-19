/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.entities;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import com.ventanilla.Entity.Servicio;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "historico_tramites", schema = "procesos")
@NamedQueries({
    @NamedQuery(name = "HistoricoTramites.findAll", query = "SELECT h FROM HistoricoTramites h"),
    @NamedQuery(name = "HistoricoTramites.findById", query = "SELECT h FROM HistoricoTramites h WHERE h.id = :id"),
    @NamedQuery(name = "HistoricoTramites.findByNumTramite", query = "SELECT h FROM HistoricoTramites h WHERE h.numTramite = :numTramite"),
    @NamedQuery(name = "HistoricoTramites.findBySolicitante", query = "SELECT h FROM HistoricoTramites h WHERE h.solicitante = :solicitante"),
    @NamedQuery(name = "HistoricoTramites.findByUsuarioRetiro", query = "SELECT h FROM HistoricoTramites h WHERE h.usuarioRetiro = :usuarioRetiro"),
    @NamedQuery(name = "HistoricoTramites.IdProceso", query = "SELECT ht FROM HistoricoTramites ht WHERE ht.idProceso = ?1"),
    @NamedQuery(name = "HistoricoTramites.IdProcesoTemp", query = "SELECT ht FROM HistoricoTramites ht WHERE ht.idProcesoTemp = ?1"),
    @NamedQuery(name = "HistoricoTramites.findByCarpetaRep", query = "SELECT h FROM HistoricoTramites h WHERE h.carpetaRep = :carpetaRep"),
    @NamedQuery(name = "HistoricoTramites.findByNombrePropietario", query = "SELECT h FROM HistoricoTramites h WHERE h.nombrePropietario = :nombrePropietario"),
    @NamedQuery(name = "HistoricoTramites.findByFecha", query = "SELECT h FROM HistoricoTramites h WHERE h.fecha = :fecha"),
    @NamedQuery(name = "HistoricoTramites.findByFechaIngreso", query = "SELECT h FROM HistoricoTramites h WHERE h.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "HistoricoTramites.findByFechaEntrega", query = "SELECT h FROM HistoricoTramites h WHERE h.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "HistoricoTramites.findByFechaRetiro", query = "SELECT h FROM HistoricoTramites h WHERE h.fechaRetiro = :fechaRetiro"),
    @NamedQuery(name = "HistoricoTramites.findByEntregado", query = "SELECT h FROM HistoricoTramites h WHERE h.entregado = :entregado"),
    @NamedQuery(name = "HistoricoTramites.findByBlocked", query = "SELECT h FROM HistoricoTramites h WHERE h.blocked = :blocked"),
    @NamedQuery(name = "HistoricoTramites.findByUserDesblock", query = "SELECT h FROM HistoricoTramites h WHERE h.userDesblock = :userDesblock"),
    @NamedQuery(name = "HistoricoTramites.findByFechaDesblock", query = "SELECT h FROM HistoricoTramites h WHERE h.fechaDesblock = :fechaDesblock"),
    @NamedQuery(name = "HistoricoTramites.findByDocumento", query = "SELECT h FROM HistoricoTramites h WHERE h.documento = :documento"),
    @NamedQuery(name = "HistoricoTramites.findByFechaNotificacion", query = "SELECT h FROM HistoricoTramites h WHERE h.fechaNotificacion = :fechaNotificacion"),
    @NamedQuery(name = "HistoricoTramites.findByIdReferencia", query = "SELECT h FROM HistoricoTramites h WHERE h.idReferencia = :idReferencia"),
    @NamedQuery(name = "HistoricoTramites.findByNameReferencia", query = "SELECT h FROM HistoricoTramites h WHERE h.nameReferencia = :nameReferencia"),
    @NamedQuery(name = "HistoricoTramites.findByCodigo", query = "SELECT h FROM HistoricoTramites h WHERE h.codigo = :codigo"),
    @NamedQuery(name = "HistoricoTramites.findByNotificacion", query = "SELECT h FROM HistoricoTramites h WHERE h.notificacion = :notificacion"),
    @NamedQuery(name = "HistoricoTramites.findBySecuencial", query = "SELECT h FROM HistoricoTramites h WHERE h.secuencial = :secuencial"),
    @NamedQuery(name = "HistoricoTramites.findByEstado", query = "SELECT h FROM HistoricoTramites h WHERE h.estado = :estado"),
    @NamedQuery(name = "HistoricoTramites.findByPeriodo", query = "SELECT h FROM HistoricoTramites h WHERE h.periodo = :periodo"),
    @NamedQuery(name = "HistoricoTramites.findByConcepto", query = "SELECT h FROM HistoricoTramites h WHERE h.concepto = :concepto"),
    @NamedQuery(name = "HistoricoTramites.findByMonto", query = "SELECT h FROM HistoricoTramites h WHERE h.monto = :monto")})
public class HistoricoTramites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente solicitante;
    @JoinColumn(name = "usuario_retiro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente usuarioRetiro;
    @Size(max = 100)
    @Column(name = "id_proceso")
    private String idProceso;
    @Size(max = 100)
    @Column(name = "id_proceso_temp")
    private String idProcesoTemp;
    @Size(max = 100)
    @Column(name = "carpeta_rep")
    private String carpetaRep;
    @Size(max = 250)
    @Column(name = "nombre_propietario")
    private String nombrePropietario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "fecha_retiro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRetiro;
    @Column(name = "entregado")
    private Boolean entregado;
    @Column(name = "blocked")
    private Boolean blocked;
    @Column(name = "user_desblock")
    private BigInteger userDesblock;
    @Column(name = "fecha_desblock")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesblock;
    @Column(name = "documento")
    private Boolean documento;
    @Column(name = "fecha_notificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNotificacion;
    @Column(name = "id_referencia")
    private BigInteger idReferencia;
    @Size(max = 2147483647)
    @Column(name = "name_referencia")
    private String nameReferencia;
    @Size(max = 250)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "notificacion")
    private Boolean notificacion;
    @Column(name = "secuencial")
    private BigInteger secuencial;
    @Size(max = 100)
    @Column(name = "estado")
    private String estado;
    @Column(name = "periodo")
    private Short periodo;
    @Size(max = 2147483647)
    @Column(name = "concepto")
    private String concepto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite tipoTramite;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servicio servicio;
    @OneToMany(mappedBy = "idTramite", fetch = FetchType.LAZY)
    @OrderBy
    private List<Observaciones> observaciones;

    @Transient
    private String nombreBeneficiario;

    @Transient
    private TipoTramite subTipoTramite;

    public HistoricoTramites() {
        periodo = Utils.getAnio(new Date()).shortValue();
        fecha = new Date();
    }

    public HistoricoTramites(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public Cliente getUsuarioRetiro() {
        return usuarioRetiro;
    }

    public void setUsuarioRetiro(Cliente usuarioRetiro) {
        this.usuarioRetiro = usuarioRetiro;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public String getCarpetaRep() {
        return carpetaRep;
    }

    public void setCarpetaRep(String carpetaRep) {
        this.carpetaRep = carpetaRep;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public BigInteger getUserDesblock() {
        return userDesblock;
    }

    public void setUserDesblock(BigInteger userDesblock) {
        this.userDesblock = userDesblock;
    }

    public Date getFechaDesblock() {
        return fechaDesblock;
    }

    public void setFechaDesblock(Date fechaDesblock) {
        this.fechaDesblock = fechaDesblock;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public BigInteger getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(BigInteger idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getNameReferencia() {
        return nameReferencia;
    }

    public void setNameReferencia(String nameReferencia) {
        this.nameReferencia = nameReferencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
        this.notificacion = notificacion;
    }

    public BigInteger getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(BigInteger secuencial) {
        this.secuencial = secuencial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public TipoTramite getSubTipoTramite() {
        return subTipoTramite;
    }

    public void setSubTipoTramite(TipoTramite subTipoTramite) {
        this.subTipoTramite = subTipoTramite;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
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
        if (!(object instanceof HistoricoTramites)) {
            return false;
        }
        HistoricoTramites other = (HistoricoTramites) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resources.commons.entities[ id=" + id + " ]";
    }

}
