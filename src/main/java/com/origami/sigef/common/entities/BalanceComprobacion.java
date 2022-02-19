/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "balance_comprobacion", schema = "contabilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BalanceComprobacion.findAll", query = "SELECT b FROM BalanceComprobacion b"),
    @NamedQuery(name = "BalanceComprobacion.findByCuentaContable", query = "SELECT b FROM BalanceComprobacion b WHERE b.cuentaContable = :cuentaContable"),
    @NamedQuery(name = "BalanceComprobacion.findBySaldoInicialDebe", query = "SELECT b FROM BalanceComprobacion b WHERE b.saldoInicialDebe = :saldoInicialDebe"),
    @NamedQuery(name = "BalanceComprobacion.findBySaldoInicialHaber", query = "SELECT b FROM BalanceComprobacion b WHERE b.saldoInicialHaber = :saldoInicialHaber"),
    @NamedQuery(name = "BalanceComprobacion.findByFlujoDebe", query = "SELECT b FROM BalanceComprobacion b WHERE b.flujoDebe = :flujoDebe"),
    @NamedQuery(name = "BalanceComprobacion.findByFlujoHaber", query = "SELECT b FROM BalanceComprobacion b WHERE b.flujoHaber = :flujoHaber"),
    @NamedQuery(name = "BalanceComprobacion.findByAcumuladoDebe", query = "SELECT b FROM BalanceComprobacion b WHERE b.acumuladoDebe = :acumuladoDebe"),
    @NamedQuery(name = "BalanceComprobacion.findByAcumuladoHaber", query = "SELECT b FROM BalanceComprobacion b WHERE b.acumuladoHaber = :acumuladoHaber"),
    @NamedQuery(name = "BalanceComprobacion.findByTotalDebe", query = "SELECT b FROM BalanceComprobacion b WHERE b.totalDebe = :totalDebe"),
    @NamedQuery(name = "BalanceComprobacion.findByTotalHaber", query = "SELECT b FROM BalanceComprobacion b WHERE b.totalHaber = :totalHaber"),
    @NamedQuery(name = "BalanceComprobacion.findById", query = "SELECT b FROM BalanceComprobacion b WHERE b.id = :id")})
@SqlResultSetMapping(name = "SaldoDebeHaberMapping",
        classes = @ConstructorResult(targetClass = SaldoDebeHaberDTO.class,
                columns = {
                    @ColumnResult(name = "saldoDebe", type = BigDecimal.class),
                    @ColumnResult(name = "saldoHaber", type = BigDecimal.class),})
)
public class BalanceComprobacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaContable;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_inicial_debe", precision = 19, scale = 2)
    private BigDecimal saldoInicialDebe;
    @Column(name = "saldo_inicial_haber", precision = 19, scale = 2)
    private BigDecimal saldoInicialHaber;
    @Column(name = "flujo_debe", precision = 19, scale = 2)
    private BigDecimal flujoDebe;
    @Column(name = "flujo_haber", precision = 19, scale = 2)
    private BigDecimal flujoHaber;
    @Column(name = "acumulado_debe", precision = 19, scale = 2)
    private BigDecimal acumuladoDebe;
    @Column(name = "acumulado_haber", precision = 19, scale = 2)
    private BigDecimal acumuladoHaber;
    @Column(name = "total_debe", precision = 19, scale = 2)
    private BigDecimal totalDebe;
    @Column(name = "total_haber", precision = 19, scale = 2)
    private BigDecimal totalHaber;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;

//    @JoinColumn(name = "asiento_contable", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private AsientosContables asientoContable;
    public BalanceComprobacion() {
    }

    public BalanceComprobacion(Long id) {
        this.id = id;
    }

//    public CuentaContable getCuentaContable() {
//        return cuentaContable;
//    }
//
//    public void setCuentaContable(CuentaContable cuentaContable) {
//        this.cuentaContable = cuentaContable;
//    }
    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public BigDecimal getSaldoInicialDebe() {
        return saldoInicialDebe;
    }

    public void setSaldoInicialDebe(BigDecimal saldoInicialDebe) {
        this.saldoInicialDebe = saldoInicialDebe;
    }

    public BigDecimal getSaldoInicialHaber() {
        return saldoInicialHaber;
    }

    public void setSaldoInicialHaber(BigDecimal saldoInicialHaber) {
        this.saldoInicialHaber = saldoInicialHaber;
    }

    public BigDecimal getFlujoDebe() {
        return flujoDebe;
    }

    public void setFlujoDebe(BigDecimal flujoDebe) {
        this.flujoDebe = flujoDebe;
    }

    public BigDecimal getFlujoHaber() {
        return flujoHaber;
    }

    public void setFlujoHaber(BigDecimal flujoHaber) {
        this.flujoHaber = flujoHaber;
    }

    public BigDecimal getAcumuladoDebe() {
        return acumuladoDebe;
    }

    public void setAcumuladoDebe(BigDecimal acumuladoDebe) {
        this.acumuladoDebe = acumuladoDebe;
    }

    public BigDecimal getAcumuladoHaber() {
        return acumuladoHaber;
    }

    public void setAcumuladoHaber(BigDecimal acumuladoHaber) {
        this.acumuladoHaber = acumuladoHaber;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof BalanceComprobacion)) {
            return false;
        }
        BalanceComprobacion other = (BalanceComprobacion) object;
        if (this.id == null) {
            if ((this.cuentaContable == null && other.cuentaContable != null) || (this.cuentaContable != null && !this.cuentaContable.equals(other.cuentaContable))) {
                return false;
            }
        } else {
            if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.BalanceComprobacion[ id=" + id + " ]";
    }

}
