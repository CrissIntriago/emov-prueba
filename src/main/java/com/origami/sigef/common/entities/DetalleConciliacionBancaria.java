/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author jesus
 */
@Entity
@Table(name = "detalle_conciliacion_bancaria", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "DetalleConciliacionBancaria.findAll", query = "SELECT d FROM DetalleConciliacionBancaria d")})
@SqlResultSetMapping(name = "CPConciliacionBancariaValueMapping",
        classes = @ConstructorResult(targetClass = DetalleConciliacionBancaria.class,
                columns = {
                    @ColumnResult(name = "fecha", type = Date.class)
                    ,@ColumnResult(name = "numDiarioGeneral", type = BigInteger.class)
                    ,@ColumnResult(name = "numComprobantePago", type = BigInteger.class)
                    ,@ColumnResult(name = "beneficiario", type = String.class)
                    ,@ColumnResult(name = "valor", type = BigDecimal.class)
                    ,@ColumnResult(name = "spi", type = BigInteger.class)
                    ,@ColumnResult(name = "estado", type = String.class)
                    ,@ColumnResult(name = "referencia", type = String.class)
                    ,@ColumnResult(name = "fechaReferencia", type = Date.class)
                    ,@ColumnResult(name = "detalle", type = String.class)
                })
)
@SqlResultSetMapping(name = "DGConciliacionBancariaValueMapping",
        classes = @ConstructorResult(targetClass = DetalleConciliacionBancaria.class,
                columns = {
                    @ColumnResult(name = "fecha", type = Date.class)
                    ,@ColumnResult(name = "numDiarioGeneral", type = BigInteger.class)
                    ,@ColumnResult(name = "beneficiario", type = String.class)
                    ,@ColumnResult(name = "valor", type = BigDecimal.class)
                    ,@ColumnResult(name = "estado", type = String.class)
                    ,@ColumnResult(name = "referencia", type = String.class)
                    ,@ColumnResult(name = "fechaReferencia", type = Date.class)
                    ,@ColumnResult(name = "detalle", type = String.class)
                })
)
public class DetalleConciliacionBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "num_diario_general")
    private BigInteger numDiarioGeneral;
    @Column(name = "num_comprobante_pago")
    private BigInteger numComprobantePago;
    @Column(name = "valor")
    private BigDecimal valor = BigDecimal.ZERO;
    @Column(name = "estado")
    private String estado;
    @Column(name = "conciliacion")
    private String conciliacion;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "fecha_referencia")
    @Temporal(TemporalType.DATE)
    private Date fechaReferencia;
    @Column(name = "beneficiario")
    private String beneficiario;
    @Column(name = "spi")
    private BigInteger spi;
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "conciliacion_bancaria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ConciliacionBancaria conciliacionBancaria;
    @Column(name = "tipo_referencia")
    private String tipoReferencia;
    @Column(name = "detalle")
    private String detalle;
    @Transient
    private String tipoAnterior;
    @Transient
    private String fechaReferenciaString;

    public DetalleConciliacionBancaria() {
    }

    public DetalleConciliacionBancaria(Long id) {
        this.id = id;
    }

    public DetalleConciliacionBancaria(Date fecha, BigInteger numDiarioGeneral,
            BigInteger numComprobantePago, String beneficiario, BigDecimal valor, BigInteger spi, String estado, String referencia, Date fechaReferencia, String detalle) {
        this.fecha = fecha;
        this.numDiarioGeneral = numDiarioGeneral;
        this.numComprobantePago = numComprobantePago;
        this.valor = valor;
        this.estado = estado;
        this.beneficiario = beneficiario;
        this.spi = spi;
        this.referencia = referencia;
        this.fechaReferencia = fechaReferencia;
        this.detalle = detalle;
    }

    public DetalleConciliacionBancaria(Date fecha, BigInteger numDiarioGeneral, String beneficiario, BigDecimal valor,
            String estado, String referencia, Date fechaReferencia, String detalle) {
        this.fecha = fecha;
        this.numDiarioGeneral = numDiarioGeneral;
        this.valor = valor;
        this.beneficiario = beneficiario;
        this.estado = estado;
        this.referencia = referencia;
        this.fechaReferencia = fechaReferencia;
        this.detalle = detalle;
    }

    public String getFechaReferenciaString() {
        return fechaReferenciaString;
    }

    public void setFechaReferenciaString(String fechaReferenciaString) {
        this.fechaReferenciaString = fechaReferenciaString;
    }

    public String getTipoReferencia() {
        return tipoReferencia;
    }

    public void setTipoReferencia(String tipoReferencia) {
        this.tipoReferencia = tipoReferencia;
    }

    public Date getFechaReferencia() {
        if (this.fechaReferenciaString != null && !this.fechaReferenciaString.isEmpty()) {
            try {
                if (this.fechaReferenciaString != null) {
                    return this.fechaReferencia = new SimpleDateFormat("yyyy/MM/dd").parse(this.fechaReferenciaString);
                }
            } catch (ParseException ex) {
                Logger.getLogger(DetalleConciliacionBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fechaReferencia;
    }

    public void setFechaReferencia(Date fechaReferencia) {
        this.fechaReferencia = fechaReferencia;
    }

    public String getTipoAnterior() {
        return tipoAnterior;
    }

    public void setTipoAnterior(String tipoAnterior) {
        this.tipoAnterior = tipoAnterior;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getNumDiarioGeneral() {
        return numDiarioGeneral;
    }

    public void setNumDiarioGeneral(BigInteger numDiarioGeneral) {
        this.numDiarioGeneral = numDiarioGeneral;
    }

    public BigInteger getNumComprobantePago() {
        return numComprobantePago;
    }

    public void setNumComprobantePago(BigInteger numComprobantePago) {
        this.numComprobantePago = numComprobantePago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getConciliacion() {
        return conciliacion;
    }

    public void setConciliacion(String conciliacion) {
        this.conciliacion = conciliacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public BigInteger getSpi() {
        return spi;
    }

    public void setSpi(BigInteger spi) {
        this.spi = spi;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ConciliacionBancaria getConciliacionBancaria() {
        return conciliacionBancaria;
    }

    public void setConciliacionBancaria(ConciliacionBancaria conciliacionBancaria) {
        this.conciliacionBancaria = conciliacionBancaria;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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
        if (!(object instanceof DetalleConciliacionBancaria)) {
            return false;
        }
        DetalleConciliacionBancaria other = (DetalleConciliacionBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetalleConciliacionBancaria{" + "id=" + id + ", fecha=" + fecha
                + ", numDiarioGeneral=" + numDiarioGeneral + ", numComprobantePago=" + numComprobantePago
                + ", valor=" + valor + ", estado=" + estado + ", conciliacion=" + conciliacion + ", referencia=" + referencia
                + ", beneficiario=" + beneficiario + ", spi=" + spi + ", tipo=" + tipo + ", conciliacionBancaria=" + conciliacionBancaria + '}';
    }

}
