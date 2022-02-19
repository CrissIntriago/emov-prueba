/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "conciliacion_bancaria", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ConciliacionBancaria.findAll", query = "SELECT c FROM ConciliacionBancaria c")})
@SqlResultSetMapping(name = "debeHaberDiarioComprobante",
        classes = @ConstructorResult(targetClass = ConciliacionBancaria.class,
                columns = {
                    @ColumnResult(name = "debe", type = BigDecimal.class)
                    ,@ColumnResult(name = "haber", type = BigDecimal.class)
                })
)
public class ConciliacionBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cuenta_contable")
    private Long cuentaContable;
    @Column(name = "saldo_libro_banco")
    private BigDecimal saldoLibroBanco;
    @Column(name = "saldo_cuenta_monetaria")
    private BigDecimal saldoCuentaMonetaria;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "estado_conciliacion")
    private String estadoConciliacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "libro_banco_saldo_inicial")
    private BigDecimal libroBancoSaldoInicial;
    @Column(name = "libro_banco_debito")
    private BigDecimal libroBancoDebito;
    @Column(name = "libro_banco_credito")
    private BigDecimal libroBancoCredito;
    @Column(name = "estado_cuenta_saldo_inicial")
    private BigDecimal estadoCuentaSaldoInicial = BigDecimal.ZERO;
    @Column(name = "estado_cuenta_debito")
    private BigDecimal estadoCuentaDebito = BigDecimal.ZERO;
    @Column(name = "estado_cuenta_credito")
    private BigDecimal estadoCuentaCredito = BigDecimal.ZERO;
    @Column(name = "cuenta_contable_banco")
    private Long cuentaContableBanco;
    @Column(name = "nombre_cuenta_contable_banco")
    private String nombreCuentaContableBanco;
    @Column(name = "numero_nombre_cuenta_banco")
    private String numeroNombreCuentaBanco;
    @OneToMany(mappedBy = "conciliacionBancaria", fetch = javax.persistence.FetchType.LAZY)
    private List<DetalleConciliacionBancaria> detallesConciliacionesBancarias;
    
    @Transient
    private BigDecimal debe = BigDecimal.ZERO;
    @Transient
    private BigDecimal haber = BigDecimal.ZERO;

    @Transient
    private BigDecimal saldoIgualesLibroBancoDebito;
    @Transient
    private BigDecimal saldoIgualesLibroBanoCredito;

    @Transient
    private BigDecimal saldoIgualesEstadoCuentaDebito;
    @Transient
    private BigDecimal saldoIgualesEstadoCuentaCredito;

    @Column(name = "credito_no_efect_libro_banco") // CAMBIAR EN BASE 
    private BigDecimal creditoNoEfectLB = BigDecimal.ZERO;
    @Column(name = "debitos_no_regist_libro_banco")
    private BigDecimal debitosNoRegistLB = BigDecimal.ZERO;
    @Column(name = "saldo_conciliado_libro_banco")
    private BigDecimal saldoConciliadoLB = BigDecimal.ZERO;

    @Column(name = "pago_no_efect__estado_cuenta")
    private BigDecimal pagoNoEfectEstC = BigDecimal.ZERO;
    @Column(name = "debitos_no_efect_estado_cuenta")
    private BigDecimal debitoNoEfectEstC = BigDecimal.ZERO;
    @Column(name = "notas_credito_no_efect_estado_cuenta")
    private BigDecimal notasCredNoEfectEstC = BigDecimal.ZERO;
    @Column(name = "depositos_no_efect_estado_cuenta")
    private BigDecimal depositosNoEfectEstC = BigDecimal.ZERO;
    @Column(name = "saldo_conciliado_estado_cuenta")
    private BigDecimal saldoConciliadoEstC = BigDecimal.ZERO;

    public ConciliacionBancaria(BigDecimal debe, BigDecimal haber) {
        this.debe = debe;
        this.haber = haber;
    }

    public ConciliacionBancaria() {
        this.saldoConciliadoEstC = BigDecimal.ZERO;
        this.depositosNoEfectEstC = BigDecimal.ZERO;
        this.notasCredNoEfectEstC = BigDecimal.ZERO;
        this.debitoNoEfectEstC = BigDecimal.ZERO;
        this.pagoNoEfectEstC = BigDecimal.ZERO;
        this.saldoConciliadoLB = BigDecimal.ZERO;
        this.debitosNoRegistLB = BigDecimal.ZERO;
        this.creditoNoEfectLB = BigDecimal.ZERO;
        this.saldoCuentaMonetaria = BigDecimal.ZERO;
        this.saldoLibroBanco = BigDecimal.ZERO;
        this.estadoCuentaCredito = BigDecimal.ZERO;
        this.estadoCuentaDebito = BigDecimal.ZERO;
        this.estadoCuentaSaldoInicial = BigDecimal.ZERO;
        this.libroBancoCredito = BigDecimal.ZERO;
        this.libroBancoDebito = BigDecimal.ZERO;
        this.libroBancoSaldoInicial = BigDecimal.ZERO;
        this.debe = BigDecimal.ZERO;
        this.haber = BigDecimal.ZERO;
    }

    public BigDecimal getSaldoIgualesLibroBancoDebito() {
        if (this.libroBancoDebito != null && this.debitosNoRegistLB != null) {
            return saldoIgualesLibroBancoDebito = this.libroBancoDebito.add(this.debitosNoRegistLB);
        }
        return saldoIgualesLibroBancoDebito;
    }

    public void setSaldoIgualesLibroBancoDebito(BigDecimal saldoIgualesLibroBancoDebito) {
        this.saldoIgualesLibroBancoDebito = saldoIgualesLibroBancoDebito;
    }

    public BigDecimal getSaldoIgualesLibroBanoCredito() {
        if (this.libroBancoCredito != null && this.creditoNoEfectLB != null) {
            return saldoIgualesLibroBanoCredito = this.libroBancoCredito.add(this.creditoNoEfectLB);
        }
        return saldoIgualesLibroBanoCredito;
    }

    public void setSaldoIgualesLibroBanoCredito(BigDecimal saldoIgualesLibroBanoCredito) {
        this.saldoIgualesLibroBanoCredito = saldoIgualesLibroBanoCredito;
    }

    public BigDecimal getSaldoIgualesEstadoCuentaCredito() {
        if (this.estadoCuentaCredito != null && this.depositosNoEfectEstC != null && this.notasCredNoEfectEstC != null) {
            return saldoIgualesEstadoCuentaCredito = this.estadoCuentaCredito.add(this.depositosNoEfectEstC).add(this.notasCredNoEfectEstC);
        }
        return saldoIgualesEstadoCuentaCredito;
    }

    public void setSaldoIgualesEstadoCuentaCredito(BigDecimal saldoIgualesEstadoCuentaCredito) {
        this.saldoIgualesEstadoCuentaCredito = saldoIgualesEstadoCuentaCredito;
    }

    public BigDecimal getSaldoIgualesEstadoCuentaDebito() {
        if (this.estadoCuentaDebito != null && this.debitoNoEfectEstC != null && this.pagoNoEfectEstC != null) {
            return saldoIgualesEstadoCuentaDebito = this.estadoCuentaDebito.add(this.debitoNoEfectEstC).add(this.pagoNoEfectEstC);
        }
        return saldoIgualesEstadoCuentaDebito;
    }

    public void setSaldoIgualesEstadoCuentaDebito(BigDecimal saldoIgualesEstadoCuentaDebito) {
        this.saldoIgualesEstadoCuentaDebito = saldoIgualesEstadoCuentaDebito;
    }

    public Long getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(Long cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public BigDecimal getCreditoNoEfectLB() {
        return creditoNoEfectLB;
    }

    public void setCreditoNoEfectLB(BigDecimal creditoNoEfectLB) {
        this.creditoNoEfectLB = creditoNoEfectLB;
    }

    public BigDecimal getDebitosNoRegistLB() {
        return debitosNoRegistLB;
    }

    public void setDebitosNoRegistLB(BigDecimal debitosNoRegistLB) {
        this.debitosNoRegistLB = debitosNoRegistLB;
    }

    public BigDecimal getSaldoConciliadoLB() {
        if (this.libroBancoSaldoInicial != null && this.saldoIgualesLibroBancoDebito != null && this.saldoIgualesLibroBanoCredito != null) {
            return saldoConciliadoLB = this.libroBancoSaldoInicial.add(this.saldoIgualesLibroBanoCredito).subtract(this.saldoIgualesLibroBancoDebito);
        }
        return saldoConciliadoLB;
    }

    public void setSaldoConciliadoLB(BigDecimal saldoConciliadoLB) {
        this.saldoConciliadoLB = saldoConciliadoLB;
    }

    public BigDecimal getSaldoConciliadoEstC() {
        if (this.estadoCuentaSaldoInicial != null && this.saldoIgualesEstadoCuentaCredito != null && this.saldoIgualesEstadoCuentaDebito != null) {
            return saldoConciliadoEstC = this.estadoCuentaSaldoInicial.add(this.saldoIgualesEstadoCuentaCredito).subtract(this.saldoIgualesEstadoCuentaDebito);
        }
        return saldoConciliadoEstC;
    }

    public void setSaldoConciliadoEstC(BigDecimal saldoConciliadoEstC) {
        this.saldoConciliadoEstC = saldoConciliadoEstC;
    }

    public BigDecimal getPagoNoEfectEstC() {
        return pagoNoEfectEstC;
    }

    public void setPagoNoEfectEstC(BigDecimal pagoNoEfectEstC) {
        this.pagoNoEfectEstC = pagoNoEfectEstC;
    }

    public BigDecimal getDebitoNoEfectEstC() {
        return debitoNoEfectEstC;
    }

    public void setDebitoNoEfectEstC(BigDecimal debitoNoEfectEstC) {
        this.debitoNoEfectEstC = debitoNoEfectEstC;
    }

    public BigDecimal getNotasCredNoEfectEstC() {
        return notasCredNoEfectEstC;
    }

    public void setNotasCredNoEfectEstC(BigDecimal notasCredNoEfectEstC) {
        this.notasCredNoEfectEstC = notasCredNoEfectEstC;
    }

    public BigDecimal getDepositosNoEfectEstC() {
        return depositosNoEfectEstC;
    }

    public void setDepositosNoEfectEstC(BigDecimal depositosNoEfectEstC) {
        this.depositosNoEfectEstC = depositosNoEfectEstC;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public ConciliacionBancaria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldoLibroBanco() {
        return saldoLibroBanco;
    }

    public void setSaldoLibroBanco(BigDecimal saldoLibroBanco) {
        this.saldoLibroBanco = saldoLibroBanco;
    }

    public BigDecimal getSaldoCuentaMonetaria() {
        return saldoCuentaMonetaria;
    }

    public void setSaldoCuentaMonetaria(BigDecimal saldoCuentaMonetaria) {
        this.saldoCuentaMonetaria = saldoCuentaMonetaria;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getEstadoConciliacion() {
        return estadoConciliacion;
    }

    public void setEstadoConciliacion(String estadoConciliacion) {
        this.estadoConciliacion = estadoConciliacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getLibroBancoSaldoInicial() {
        return libroBancoSaldoInicial;
    }

    public void setLibroBancoSaldoInicial(BigDecimal libroBancoSaldoInicial) {
        this.libroBancoSaldoInicial = libroBancoSaldoInicial;
    }

    public BigDecimal getLibroBancoDebito() {
        return libroBancoDebito;
    }

    public void setLibroBancoDebito(BigDecimal libroBancoDebito) {
        this.libroBancoDebito = libroBancoDebito;
    }

    public BigDecimal getLibroBancoCredito() {
        return libroBancoCredito;
    }

    public void setLibroBancoCredito(BigDecimal libroBancoCredito) {
        this.libroBancoCredito = libroBancoCredito;
    }

    public BigDecimal getEstadoCuentaSaldoInicial() {
        return estadoCuentaSaldoInicial;
    }

    public void setEstadoCuentaSaldoInicial(BigDecimal estadoCuentaSaldoInicial) {
        this.estadoCuentaSaldoInicial = estadoCuentaSaldoInicial;
    }

    public BigDecimal getEstadoCuentaDebito() {
        return estadoCuentaDebito;
    }

    public void setEstadoCuentaDebito(BigDecimal estadoCuentaDebito) {
        this.estadoCuentaDebito = estadoCuentaDebito;
    }

    public BigDecimal getEstadoCuentaCredito() {
        return estadoCuentaCredito;
    }

    public void setEstadoCuentaCredito(BigDecimal estadoCuentaCredito) {
        this.estadoCuentaCredito = estadoCuentaCredito;
    }

    public List<DetalleConciliacionBancaria> getDetallesConciliacionesBancarias() {
        return detallesConciliacionesBancarias;
    }

    public void setDetallesConciliacionesBancarias(List<DetalleConciliacionBancaria> detallesConciliacionesBancarias) {
        this.detallesConciliacionesBancarias = detallesConciliacionesBancarias;
    }

    public Long getCuentaContableBanco() {
        return cuentaContableBanco;
    }

    public void setCuentaContableBanco(Long cuentaContableBanco) {
        this.cuentaContableBanco = cuentaContableBanco;
    }

    public String getNombreCuentaContableBanco() {
        return nombreCuentaContableBanco;
    }

    public void setNombreCuentaContableBanco(String nombreCuentaContableBanco) {
        this.nombreCuentaContableBanco = nombreCuentaContableBanco;
    }

    public String getNumeroNombreCuentaBanco() {
        return numeroNombreCuentaBanco;
    }

    public void setNumeroNombreCuentaBanco(String numeroNombreCuentaBanco) {
        this.numeroNombreCuentaBanco = numeroNombreCuentaBanco;
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
        if (!(object instanceof ConciliacionBancaria)) {
            return false;
        }
        ConciliacionBancaria other = (ConciliacionBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "com.origami.sigef.common.entities.ConciliacionBancaria{" + "id=" + id + ", "
//                + "saldoLibroBanco=" + saldoLibroBanco + ", "
//                + "saldoCuentaMonetaria=" + saldoCuentaMonetaria + ", fechaCreacion=" + fechaCreacion + ", "
//                + "fechaModifica=" + fechaModifica + ", usuarioCreacion=" + usuarioCreacion + ", usuarioModifica="
//                + usuarioModifica + ", estadoConciliacion=" + estadoConciliacion + ", estado=" + estado + ", anio=" + anio
//                + ", mes=" + mes + ", periodo=" + periodo + ", libroBancoSaldoInicial=" + libroBancoSaldoInicial + ", libroBancoDebito="
//                + libroBancoDebito + ", libroBancoCredito=" + libroBancoCredito + ", estadoCuentaSaldoInicial=" + estadoCuentaSaldoInicial
//                + ", estadoCuentaDebito=" + estadoCuentaDebito + ", estadoCuentaCredito=" + estadoCuentaCredito + ", detallesConciliacionesBancarias="
//                + detallesConciliacionesBancarias + '}';
//    }
//    
    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ConciliacionBancaria[ id=" + id + " ]";
    }

}
