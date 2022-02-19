/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "flow_pub_solicitud", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowPubSolicitud.findAll", query = "SELECT f FROM FlowPubSolicitud f"),
    @NamedQuery(name = "FlowPubSolicitud.findById", query = "SELECT f FROM FlowPubSolicitud f WHERE f.id = :id"),
    @NamedQuery(name = "FlowPubSolicitud.findByTramite", query = "SELECT f FROM FlowPubSolicitud f WHERE f.tramite = :tramite"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolApellidos", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solApellidos = :solApellidos"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolNombres", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solNombres = :solNombres"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolCedula", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solCedula = :solCedula"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolDireccion", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solDireccion = :solDireccion"),
    @NamedQuery(name = "FlowPubSolicitud.findBySector", query = "SELECT f FROM FlowPubSolicitud f WHERE f.sector = :sector"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolParroquia", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solParroquia = :solParroquia"),
    @NamedQuery(name = "FlowPubSolicitud.findByManzana", query = "SELECT f FROM FlowPubSolicitud f WHERE f.manzana = :manzana"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolCelular", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solCelular = :solCelular"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolConvencional", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solConvencional = :solConvencional"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolCorreo", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solCorreo = :solCorreo"),
    @NamedQuery(name = "FlowPubSolicitud.findByFechaSolicitud", query = "SELECT f FROM FlowPubSolicitud f WHERE f.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "FlowPubSolicitud.findByOtroMotivo", query = "SELECT f FROM FlowPubSolicitud f WHERE f.otroMotivo = :otroMotivo"),
    @NamedQuery(name = "FlowPubSolicitud.findByNumeroFicha", query = "SELECT f FROM FlowPubSolicitud f WHERE f.numeroFicha = :numeroFicha"),
    @NamedQuery(name = "FlowPubSolicitud.findByObservacion", query = "SELECT f FROM FlowPubSolicitud f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FlowPubSolicitud.findByTipoBien", query = "SELECT f FROM FlowPubSolicitud f WHERE f.tipoBien = :tipoBien"),
    @NamedQuery(name = "FlowPubSolicitud.findByTipoCertificado", query = "SELECT f FROM FlowPubSolicitud f WHERE f.tipoCertificado = :tipoCertificado"),
    @NamedQuery(name = "FlowPubSolicitud.findByEstado", query = "SELECT f FROM FlowPubSolicitud f WHERE f.estado = :estado"),
    @NamedQuery(name = "FlowPubSolicitud.findByTipoPago", query = "SELECT f FROM FlowPubSolicitud f WHERE f.tipoPago = :tipoPago"),
    @NamedQuery(name = "FlowPubSolicitud.findByTotal", query = "SELECT f FROM FlowPubSolicitud f WHERE f.total = :total"),
    @NamedQuery(name = "FlowPubSolicitud.findBySolEstadoCivil", query = "SELECT f FROM FlowPubSolicitud f WHERE f.solEstadoCivil = :solEstadoCivil"),
    @NamedQuery(name = "FlowPubSolicitud.findByCodigoVerificacion", query = "SELECT f FROM FlowPubSolicitud f WHERE f.codigoVerificacion = :codigoVerificacion"),
    @NamedQuery(name = "FlowPubSolicitud.findByBanco", query = "SELECT f FROM FlowPubSolicitud f WHERE f.banco = :banco"),
    @NamedQuery(name = "FlowPubSolicitud.findByCodigoComprobante", query = "SELECT f FROM FlowPubSolicitud f WHERE f.codigoComprobante = :codigoComprobante"),
    @NamedQuery(name = "FlowPubSolicitud.findByIdSolicitudVentanilla", query = "SELECT f FROM FlowPubSolicitud f WHERE f.idSolicitudVentanilla = :idSolicitudVentanilla"),
    @NamedQuery(name = "FlowPubSolicitud.findByBancoDesde", query = "SELECT f FROM FlowPubSolicitud f WHERE f.bancoDesde = :bancoDesde"),
    @NamedQuery(name = "FlowPubSolicitud.findByPropietarioCuenta", query = "SELECT f FROM FlowPubSolicitud f WHERE f.propietarioCuenta = :propietarioCuenta"),
    @NamedQuery(name = "FlowPubSolicitud.findByNumeroCuenta", query = "SELECT f FROM FlowPubSolicitud f WHERE f.numeroCuenta = :numeroCuenta"),
    @NamedQuery(name = "FlowPubSolicitud.findByFechaTransferencia", query = "SELECT f FROM FlowPubSolicitud f WHERE f.fechaTransferencia = :fechaTransferencia")})
public class FlowPubSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Size(max = 2147483647)
    @Column(name = "sol_apellidos")
    private String solApellidos;
    @Size(max = 2147483647)
    @Column(name = "sol_nombres")
    private String solNombres;
    @Size(max = 2147483647)
    @Column(name = "sol_cedula")
    private String solCedula;
    @Size(max = 2147483647)
    @Column(name = "sol_direccion")
    private String solDireccion;
    @Size(max = 2147483647)
    @Column(name = "sector")
    private String sector;
    @Size(max = 2147483647)
    @Column(name = "sol_parroquia")
    private String solParroquia;
    @Size(max = 2147483647)
    @Column(name = "manzana")
    private String manzana;
    @Size(max = 2147483647)
    @Column(name = "sol_celular")
    private String solCelular;
    @Size(max = 2147483647)
    @Column(name = "sol_convencional")
    private String solConvencional;
    @Size(max = 2147483647)
    @Column(name = "sol_correo")
    private String solCorreo;
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Size(max = 2147483647)
    @Column(name = "otro_motivo")
    private String otroMotivo;
    @Column(name = "numero_ficha")
    private Integer numeroFicha;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "tipo_bien")
    private BigInteger tipoBien;
    @Column(name = "tipo_certificado")
    private BigInteger tipoCertificado;
    @Size(max = 2147483647)
    @Column(name = "estado")
    private String estado;
    @Column(name = "tipo_pago")
    private Boolean tipoPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private Double total;
    @Size(max = 2147483647)
    @Column(name = "sol_estado_civil")
    private String solEstadoCivil;
    @Size(max = 2147483647)
    @Column(name = "codigo_verificacion")
    private String codigoVerificacion;
    @Column(name = "banco")
    private Integer banco;
    @Size(max = 2147483647)
    @Column(name = "codigo_comprobante")
    private String codigoComprobante;
    @Column(name = "id_solicitud_ventanilla")
    private BigInteger idSolicitudVentanilla;
    @Size(max = 2147483647)
    @Column(name = "banco_desde")
    private String bancoDesde;
    @Size(max = 2147483647)
    @Column(name = "propietario_cuenta")
    private String propietarioCuenta;
    @Size(max = 2147483647)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Size(max = 2147483647)
    @Column(name = "fecha_transferencia")
    private String fechaTransferencia;

    public FlowPubSolicitud() {
    }

    public FlowPubSolicitud(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public String getSolApellidos() {
        return solApellidos;
    }

    public void setSolApellidos(String solApellidos) {
        this.solApellidos = solApellidos;
    }

    public String getSolNombres() {
        return solNombres;
    }

    public void setSolNombres(String solNombres) {
        this.solNombres = solNombres;
    }

    public String getSolCedula() {
        return solCedula;
    }

    public void setSolCedula(String solCedula) {
        this.solCedula = solCedula;
    }

    public String getSolDireccion() {
        return solDireccion;
    }

    public void setSolDireccion(String solDireccion) {
        this.solDireccion = solDireccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSolParroquia() {
        return solParroquia;
    }

    public void setSolParroquia(String solParroquia) {
        this.solParroquia = solParroquia;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getSolCelular() {
        return solCelular;
    }

    public void setSolCelular(String solCelular) {
        this.solCelular = solCelular;
    }

    public String getSolConvencional() {
        return solConvencional;
    }

    public void setSolConvencional(String solConvencional) {
        this.solConvencional = solConvencional;
    }

    public String getSolCorreo() {
        return solCorreo;
    }

    public void setSolCorreo(String solCorreo) {
        this.solCorreo = solCorreo;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getOtroMotivo() {
        return otroMotivo;
    }

    public void setOtroMotivo(String otroMotivo) {
        this.otroMotivo = otroMotivo;
    }

    public Integer getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Integer numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigInteger getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(BigInteger tipoBien) {
        this.tipoBien = tipoBien;
    }

    public BigInteger getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(BigInteger tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Boolean tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getSolEstadoCivil() {
        return solEstadoCivil;
    }

    public void setSolEstadoCivil(String solEstadoCivil) {
        this.solEstadoCivil = solEstadoCivil;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public BigInteger getIdSolicitudVentanilla() {
        return idSolicitudVentanilla;
    }

    public void setIdSolicitudVentanilla(BigInteger idSolicitudVentanilla) {
        this.idSolicitudVentanilla = idSolicitudVentanilla;
    }

    public String getBancoDesde() {
        return bancoDesde;
    }

    public void setBancoDesde(String bancoDesde) {
        this.bancoDesde = bancoDesde;
    }

    public String getPropietarioCuenta() {
        return propietarioCuenta;
    }

    public void setPropietarioCuenta(String propietarioCuenta) {
        this.propietarioCuenta = propietarioCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(String fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
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
        if (!(object instanceof FlowPubSolicitud)) {
            return false;
        }
        FlowPubSolicitud other = (FlowPubSolicitud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowPubSolicitud[ id=" + id + " ]";
    }
    
}
