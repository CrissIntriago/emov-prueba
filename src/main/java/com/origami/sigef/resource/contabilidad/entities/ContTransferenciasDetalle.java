/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.common.entities.Banco;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_transferencias_detalle", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContTransferenciasDetalle.findAll", query = "SELECT c FROM ContTransferenciasDetalle c"),
    @NamedQuery(name = "ContTransferenciasDetalle.findById", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.id = :id"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByReferencia", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.referencia = :referencia"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByIdentificacion", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.identificacion = :identificacion"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByNombreBeneficiario", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.nombreBeneficiario = :nombreBeneficiario"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByCuentaBcoBeneficiario", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.cuentaBcoBeneficiario = :cuentaBcoBeneficiario"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByTipoCuenta", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.tipoCuenta = :tipoCuenta"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByValor", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.valor = :valor"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByConcepto", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.concepto = :concepto"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByDetalle", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.detalle = :detalle"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByEstado", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.estado = :estado"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByFechaAcreditacion", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.fechaAcreditacion = :fechaAcreditacion"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByFechaAnulacion", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.fechaAnulacion = :fechaAnulacion"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByContabilizado", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.contabilizado = :contabilizado"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByIdTransferencia", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.idContTransferencia = ?1 ORDER BY c.referencia ASC"),
    @NamedQuery(name = "ContTransferenciasDetalle.findByTipoBeneficiario", query = "SELECT c FROM ContTransferenciasDetalle c WHERE c.tipoBeneficiario = :tipoBeneficiario")})
public class ContTransferenciasDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "referencia")
    private BigInteger referencia;
    @Size(max = 13)
    @Column(name = "identificacion")
    private String identificacion;
    @Size(max = 2147483647)
    @Column(name = "nombre_beneficiario")
    private String nombreBeneficiario;
    @Size(max = 18)
    @Column(name = "cuenta_bco_beneficiario")
    private String cuentaBcoBeneficiario;
    @Size(max = 2)
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 6)
    @Column(name = "concepto")
    private String concepto;
    @Size(max = 2147483647)
    @Column(name = "detalle")
    private String detalle;
    @JoinColumn(name = "institucion_financiera", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Banco institucionFinanciera;
    @Size(max = 50)
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
    @Column(name = "transferencia")
    private Boolean transferencia;
    @Size(max = 1)
    @Column(name = "tipo_beneficiario")
    private String tipoBeneficiario;
    @JoinColumn(name = "id_cont_comprobante_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContComprobantePago idContComprobantePago;
    @JoinColumn(name = "id_cont_transferencia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContTransferencias idContTransferencia;

    public ContTransferenciasDetalle() {
        this.transferencia = false;
        this.contabilizado = false;
        this.estado = "REGISTRADO";
    }

    public ContTransferenciasDetalle(Long id) {
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

    public Banco getInstitucionFinanciera() {
        return institucionFinanciera;
    }

    public void setInstitucionFinanciera(Banco institucionFinanciera) {
        this.institucionFinanciera = institucionFinanciera;
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

    public ContComprobantePago getIdContComprobantePago() {
        return idContComprobantePago;
    }

    public void setIdContComprobantePago(ContComprobantePago idContComprobantePago) {
        this.idContComprobantePago = idContComprobantePago;
    }

    public ContTransferencias getIdContTransferencia() {
        return idContTransferencia;
    }

    public void setIdContTransferencia(ContTransferencias idContTransferencia) {
        this.idContTransferencia = idContTransferencia;
    }

    public Boolean getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Boolean transferencia) {
        this.transferencia = transferencia;
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
        if (!(object instanceof ContTransferenciasDetalle)) {
            return false;
        }
        ContTransferenciasDetalle other = (ContTransferenciasDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContTransferenciasDetalle[ id=" + id + " ]";
    }

}
