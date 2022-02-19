/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
@Table(name = "detalle_transferencias", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "DetalleTransferencias.findAll", query = "SELECT d FROM DetalleTransferencias d")
    , @NamedQuery(name = "DetalleTransferencias.findById", query = "SELECT d FROM DetalleTransferencias d WHERE d.id = :id")
    , @NamedQuery(name = "DetalleTransferencias.findByReferencia", query = "SELECT d FROM DetalleTransferencias d WHERE d.referencia = :referencia")
    , @NamedQuery(name = "DetalleTransferencias.findByIdentificacion", query = "SELECT d FROM DetalleTransferencias d WHERE d.identificacion = :identificacion")
    , @NamedQuery(name = "DetalleTransferencias.findByNombreBeneficiario", query = "SELECT d FROM DetalleTransferencias d WHERE d.nombreBeneficiario = :nombreBeneficiario")
    , @NamedQuery(name = "DetalleTransferencias.findByInstitucionFinanciera", query = "SELECT d FROM DetalleTransferencias d WHERE d.institucionFinanciera = :institucionFinanciera")
    , @NamedQuery(name = "DetalleTransferencias.findByCuentaBcoBeneficiario", query = "SELECT d FROM DetalleTransferencias d WHERE d.cuentaBcoBeneficiario = :cuentaBcoBeneficiario")
    , @NamedQuery(name = "DetalleTransferencias.findByTipoCuenta", query = "SELECT d FROM DetalleTransferencias d WHERE d.tipoCuenta = :tipoCuenta")
    , @NamedQuery(name = "DetalleTransferencias.findByValor", query = "SELECT d FROM DetalleTransferencias d WHERE d.valor = :valor")
    , @NamedQuery(name = "DetalleTransferencias.findByConcepto", query = "SELECT d FROM DetalleTransferencias d WHERE d.concepto = :concepto")
    , @NamedQuery(name = "DetalleTransferencias.findByDetalle", query = "SELECT d FROM DetalleTransferencias d WHERE d.detalle = :detalle")})
public class DetalleTransferencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "referencia")
    private BigInteger referencia;
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "nombre_beneficiario")
    private String nombreBeneficiario;
    @JoinColumn(name = "institucion_financiera", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Banco institucionFinanciera;
    @Column(name = "cuenta_bco_beneficiario")
    private String cuentaBcoBeneficiario;
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "concepto")
    private String concepto;
    @Column(name = "detalle")
    private String detalle;
    @JoinColumn(name = "transferencia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Transferencias transferencia;
    @JoinColumn(name = "comprobante_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ComprobantePago comprobantePago;
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_acreditacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAcreditacion;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAnulacion;
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    @Column(name = "tipo_beneficiario")
    private String tipoBeneficiario;
    @JoinColumn(name = "id_diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;
     @Column(name = "numero_tramite_anterior")
    private String numeroTramiteAnterior;

    public DetalleTransferencias() {
        contabilizado = Boolean.FALSE;
    }

    public DetalleTransferencias(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getReferencia() {
        return referencia;
    }

    public void setReferencia(BigInteger referencia) {
        this.referencia = referencia;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public Banco getInstitucionFinanciera() {
        return institucionFinanciera;
    }

    public void setInstitucionFinanciera(Banco institucionFinanciera) {
        this.institucionFinanciera = institucionFinanciera;
    }

    public String getCuentaBcoBeneficiario() {
        return cuentaBcoBeneficiario;
    }

    public void setCuentaBcoBeneficiario(String cuentaBcoBeneficiario) {
        this.cuentaBcoBeneficiario = cuentaBcoBeneficiario;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Transferencias getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencias transferencia) {
        this.transferencia = transferencia;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public String getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(String tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public String getNumeroTramiteAnterior() {
        return numeroTramiteAnterior;
    }

    public void setNumeroTramiteAnterior(String numeroTramiteAnterior) {
        this.numeroTramiteAnterior = numeroTramiteAnterior;
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
        if (!(object instanceof DetalleTransferencias)) {
            return false;
        }
        DetalleTransferencias other = (DetalleTransferencias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetalleTransferencias[ id=" + id + " ]";
    }

}
