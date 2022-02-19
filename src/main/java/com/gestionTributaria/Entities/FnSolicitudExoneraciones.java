/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
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

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_solicitud_exoneraciones", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnSolicitudExoneraciones.findAll", query = "SELECT f FROM FnSolicitudExoneraciones f"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findById", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.id = :id"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByAnioInicio", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.anioInicio = :anioInicio"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByAnioFin", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.anioFin = :anioFin"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByUsuarioCreacion", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByFechaIngreso", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByFechaAprobacion", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.fechaAprobacion = :fechaAprobacion"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByInteres", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.interes = :interes"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByMultas", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.multas = :multas"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByNumResolucion", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.numResolucion = :numResolucion"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByPorcentajeEmision", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.porcentajeEmision = :porcentajeEmision"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByRecargo", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.recargo = :recargo"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findBySolicitante", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.solicitante = :solicitante"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByTotalPago", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.totalPago = :totalPago"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByTotalRemision", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.totalRemision = :totalRemision"),
    @NamedQuery(name = "FnSolicitudExoneraciones.findByTramiteTipo", query = "SELECT f FROM FnSolicitudExoneraciones f WHERE f.tramiteTipo = :tramiteTipo")})
public class FnSolicitudExoneraciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_inicio")
    private Integer anioInicio;
    @Column(name = "anio_fin")
    private Integer anioFin;
    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioCreacion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "multas")
    private BigDecimal multas;
    @Size(max = 20)
    @Column(name = "num_resolucion")
    private String numResolucion;
    @Column(name = "porcentaje_emision")
    private BigDecimal porcentajeEmision;
    @Column(name = "recargo")
    private BigDecimal recargo;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne
    private Cliente solicitante;
    @Column(name = "total_pago")
    private BigDecimal totalPago;
    @Column(name = "total_remision")
    private BigDecimal totalRemision;
    @JoinColumn(name = "tramite_tipo", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem tramiteTipo;
    @OneToMany(mappedBy = "exoneracion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnExoneracionLiquidaciones> fnExoneracionLiquidacionesList;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne
    private FnEstadoExoneracion estado;
    @JoinColumn(name = "exoneracion_tipo", referencedColumnName = "id")
    @ManyToOne
    private FnExoneracionTipo exoneracionTipo;
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne
    private FnResolucion resolucion;
    @Column(name = "fecha_pago_maximo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPagoMaximo;

    public FnSolicitudExoneraciones() {
    }

    public FnSolicitudExoneraciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Integer anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }

    public Usuarios getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuarios usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getMultas() {
        return multas;
    }

    public void setMultas(BigDecimal multas) {
        this.multas = multas;
    }

    public String getNumResolucion() {
        return numResolucion;
    }

    public void setNumResolucion(String numResolucion) {
        this.numResolucion = numResolucion;
    }

    public BigDecimal getPorcentajeEmision() {
        return porcentajeEmision;
    }

    public Date getFechaPagoMaximo() {
        return fechaPagoMaximo;
    }

    public void setFechaPagoMaximo(Date fechaPagoMaximo) {
        this.fechaPagoMaximo = fechaPagoMaximo;
    }

    public void setPorcentajeEmision(BigDecimal porcentajeEmision) {
        this.porcentajeEmision = porcentajeEmision;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public BigDecimal getTotalRemision() {
        return totalRemision;
    }

    public void setTotalRemision(BigDecimal totalRemision) {
        this.totalRemision = totalRemision;
    }

    public CatalogoItem getTramiteTipo() {
        return tramiteTipo;
    }

    public void setTramiteTipo(CatalogoItem tramiteTipo) {
        this.tramiteTipo = tramiteTipo;
    }

    
    public List<FnExoneracionLiquidaciones> getFnExoneracionLiquidacionesList() {
        return fnExoneracionLiquidacionesList;
    }

    public void setFnExoneracionLiquidacionesList(List<FnExoneracionLiquidaciones> fnExoneracionLiquidacionesList) {
        this.fnExoneracionLiquidacionesList = fnExoneracionLiquidacionesList;
    }

    public FnEstadoExoneracion getEstado() {
        return estado;
    }

    public void setEstado(FnEstadoExoneracion estado) {
        this.estado = estado;
    }

    public FnExoneracionTipo getExoneracionTipo() {
        return exoneracionTipo;
    }

    public void setExoneracionTipo(FnExoneracionTipo exoneracionTipo) {
        this.exoneracionTipo = exoneracionTipo;
    }

    public FnResolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(FnResolucion resolucion) {
        this.resolucion = resolucion;
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
        if (!(object instanceof FnSolicitudExoneraciones)) {
            return false;
        }
        FnSolicitudExoneraciones other = (FnSolicitudExoneraciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnSolicitudExoneraciones[ id=" + id + " ]";
    }

}
