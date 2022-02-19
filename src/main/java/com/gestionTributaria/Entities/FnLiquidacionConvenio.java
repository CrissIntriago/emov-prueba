package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Administrator
 */
@Entity
@Table(name = "fn_liquidacion_convenio", schema = "sgm")
@NamedQueries({
    @NamedQuery(name = "FnLiquidacionConvenio.findAll", query = "SELECT f FROM FnLiquidacionConvenio f"),
    @NamedQuery(name = "FnLiquidacionConvenio.findById", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.id = :id"),
    @NamedQuery(name = "FnLiquidacionConvenio.findByLiquidacion", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.liquidacion = :liquidacion"),
    @NamedQuery(name = "FnLiquidacionConvenio.findByRecargo", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.recargo = :recargo"),
    @NamedQuery(name = "FnLiquidacionConvenio.findByInteres", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.interes = :interes"),
    @NamedQuery(name = "FnLiquidacionConvenio.findByCoactiva", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.coactiva = :coactiva"),
    @NamedQuery(name = "FnLiquidacionConvenio.findByTotalDeuda", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.totalDeuda = :totalDeuda"),
    @NamedQuery(name = "FnLiquidacionConvenio.findByFechaRegistro", query = "SELECT f FROM FnLiquidacionConvenio f WHERE f.fechaRegistro = :fechaRegistro")})
public class FnLiquidacionConvenio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "recargo", precision = 19, scale = 2)
    private BigDecimal recargo;
    @Column(name = "interes", precision = 19, scale = 2)
    private BigDecimal interes;
    @Column(name = "coactiva", precision = 19, scale = 2)
    private BigDecimal coactiva;
    @Column(name = "total_deuda", precision = 19, scale = 2)
    private BigDecimal totalDeuda;
    @Column(name = "saldo", precision = 19, scale = 2)
    private BigDecimal saldo;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @JoinColumn(name = "convenio", referencedColumnName = "id")
    @ManyToOne
    private FnConvenioPago convenio;

    public FnLiquidacionConvenio() {
    }

    public FnLiquidacionConvenio(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getCoactiva() {
        return coactiva;
    }

    public void setCoactiva(BigDecimal coactiva) {
        this.coactiva = coactiva;
    }

    public BigDecimal getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(BigDecimal totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public FnConvenioPago getConvenio() {
        return convenio;
    }

    public void setConvenio(FnConvenioPago convenio) {
        this.convenio = convenio;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
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
        if (!(object instanceof FnLiquidacionConvenio)) {
            return false;
        }
        FnLiquidacionConvenio other = (FnLiquidacionConvenio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.FnLiquidacionConvenio[ id=" + id + " ]";
    }

}
