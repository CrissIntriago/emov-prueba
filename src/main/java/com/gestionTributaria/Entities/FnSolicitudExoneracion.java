/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenTipoValor;
import com.origami.sigef.common.entities.Cliente;
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
@Table(name = "fn_solicitud_exoneracion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnSolicitudExoneracion.findAll", query = "SELECT f FROM FnSolicitudExoneracion f")})
public class FnSolicitudExoneracion implements Serializable {

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
    @Column(name = "porcentaje")
    private Long porcentaje;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 30)
    @Column(name = "num_resolucion_sac")
    private String numResolucionSac;
    @Column(name = "num_sol_sac")
    private BigInteger numSolSac;
    @Column(name = "num_solicitud_sac")
    private BigInteger numSolicitudSac;
    @Column(name = "migrada")
    private Boolean migrada;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne
    private Cliente solicitante;

    @Column(name = "tramite")
    private BigInteger tramite;
    @Size(max = 25)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @OneToMany(mappedBy = "solicitudExoneracion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneracionPredios> fnSolicitudExoneracionPrediosList;
    @OneToMany(mappedBy = "solicitudExoneracion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnResolucion> fnResolucionList;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne
    private FnEstadoExoneracion estado;
    @JoinColumn(name = "exoneracion_tipo", referencedColumnName = "id")
    @ManyToOne
    private FnExoneracionTipo exoneracionTipo;
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne
    private FnResolucion resolucion;
    @OneToMany(mappedBy = "exoneracion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnExoneracionLiquidacion> fnExoneracionLiquidacionList;
    @OneToMany(mappedBy = "solExoneracion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenSolicitudesLiquidacion> renSolicitudesLiquidacionList;
    @Column(name = "fecha_pago_maximo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPagoMaximo;

    @JoinColumn(name = "tipo_valor", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoValor tipoValor;
    @Column(name = "nro_comprobante")
    private String nroComprobante;
    @Column(name = "nro_memorando_catastro")
    private String nroMemorandoCatastro;
    @Column(name = "nro_memorando_coactiva")
    private String nroMemorandoCoactiva;
    @Column(name = "nro_memorando_oficio")
    private String nroMemorandoOficio;

    public Long getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Long porcentaje) {
        this.porcentaje = porcentaje;
    }

    public FnSolicitudExoneracion() {
    }

    public FnSolicitudExoneracion(Long id) {
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

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getNumResolucionSac() {
        return numResolucionSac;
    }

    public void setNumResolucionSac(String numResolucionSac) {
        this.numResolucionSac = numResolucionSac;
    }

    public BigInteger getNumSolSac() {
        return numSolSac;
    }

    public void setNumSolSac(BigInteger numSolSac) {
        this.numSolSac = numSolSac;
    }

    public BigInteger getNumSolicitudSac() {
        return numSolicitudSac;
    }

    public void setNumSolicitudSac(BigInteger numSolicitudSac) {
        this.numSolicitudSac = numSolicitudSac;
    }

    public Boolean getMigrada() {
        return migrada;
    }

    public void setMigrada(Boolean migrada) {
        this.migrada = migrada;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public FinaRenTipoValor getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(FinaRenTipoValor tipoValor) {
        this.tipoValor = tipoValor;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<FnSolicitudExoneracionPredios> getFnSolicitudExoneracionPrediosList() {
        return fnSolicitudExoneracionPrediosList;
    }

    public void setFnSolicitudExoneracionPrediosList(List<FnSolicitudExoneracionPredios> fnSolicitudExoneracionPrediosList) {
        this.fnSolicitudExoneracionPrediosList = fnSolicitudExoneracionPrediosList;
    }

    public List<FnResolucion> getFnResolucionList() {
        return fnResolucionList;
    }

    public void setFnResolucionList(List<FnResolucion> fnResolucionList) {
        this.fnResolucionList = fnResolucionList;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
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

    public List<FnExoneracionLiquidacion> getFnExoneracionLiquidacionList() {
        return fnExoneracionLiquidacionList;
    }

    public void setFnExoneracionLiquidacionList(List<FnExoneracionLiquidacion> fnExoneracionLiquidacionList) {
        this.fnExoneracionLiquidacionList = fnExoneracionLiquidacionList;
    }

    public List<RenSolicitudesLiquidacion> getRenSolicitudesLiquidacionList() {
        return renSolicitudesLiquidacionList;
    }

    public void setRenSolicitudesLiquidacionList(List<RenSolicitudesLiquidacion> renSolicitudesLiquidacionList) {
        this.renSolicitudesLiquidacionList = renSolicitudesLiquidacionList;
    }

    public Date getFechaPagoMaximo() {
        return fechaPagoMaximo;
    }

    public void setFechaPagoMaximo(Date fechaPagoMaximo) {
        this.fechaPagoMaximo = fechaPagoMaximo;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }

    public String getNroMemorandoCatastro() {
        return nroMemorandoCatastro;
    }

    public void setNroMemorandoCatastro(String nroMemorandoCatastro) {
        this.nroMemorandoCatastro = nroMemorandoCatastro;
    }

    public String getNroMemorandoCoactiva() {
        return nroMemorandoCoactiva;
    }

    public void setNroMemorandoCoactiva(String nroMemorandoCoactiva) {
        this.nroMemorandoCoactiva = nroMemorandoCoactiva;
    }

    public String getNroMemorandoOficio() {
        return nroMemorandoOficio;
    }

    public void setNroMemorandoOficio(String nroMemorandoOficio) {
        this.nroMemorandoOficio = nroMemorandoOficio;
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
        if (!(object instanceof FnSolicitudExoneracion)) {
            return false;
        }
        FnSolicitudExoneracion other = (FnSolicitudExoneracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FnSolicitudExoneracion{" + "id=" + id + ", anioInicio=" + anioInicio + ", anioFin=" + anioFin + ", fechaAprobacion=" + fechaAprobacion + ", fechaIngreso=" + fechaIngreso + ", numResolucionSac=" + numResolucionSac + ", numSolSac=" + numSolSac + ", numSolicitudSac=" + numSolicitudSac + ", migrada=" + migrada + ", solicitante=" + solicitante + ", tramite=" + tramite + ", usuarioCreacion=" + usuarioCreacion + ", valor=" + valor + ", fnSolicitudExoneracionPrediosList=" + fnSolicitudExoneracionPrediosList + ", fnResolucionList=" + fnResolucionList + ", predio=" + predio + ", estado=" + estado + ", exoneracionTipo=" + exoneracionTipo + ", resolucion=" + resolucion + ", fnExoneracionLiquidacionList=" + fnExoneracionLiquidacionList + ", renSolicitudesLiquidacionList=" + renSolicitudesLiquidacionList + ", fechaPagoMaximo=" + fechaPagoMaximo + ", tipoValor=" + tipoValor + '}';
    }

}
