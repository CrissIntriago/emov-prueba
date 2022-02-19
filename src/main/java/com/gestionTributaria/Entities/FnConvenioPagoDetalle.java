/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_convenio_pago_detalle", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnConvenioPagoDetalle.findAll", query = "SELECT f FROM FnConvenioPagoDetalle f"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findById", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.id = :id"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findByDescripcion", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findByDeuda", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.deuda = :deuda"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findByEstado", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.estado = :estado"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findByFechaMaximaPago", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.fechaMaximaPago = :fechaMaximaPago"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findByLiquidacion", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.liquidacion = :liquidacion"),
    @NamedQuery(name = "FnConvenioPagoDetalle.findByMes", query = "SELECT f FROM FnConvenioPagoDetalle f WHERE f.mes = :mes")})
public class FnConvenioPagoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "deuda")
    private BigDecimal deuda;
    @Column(name = "capital_reducido")
    private BigDecimal capitalReducido;
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_maxima_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMaximaPago;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @Column(name = "dia_plazo")
    private Integer diaPlazo;
    @Column(name = "mes")
    private Integer mes;
    @JoinColumn(name = "convenio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnConvenioPago convenio;

    public FnConvenioPagoDetalle() {
        this.diaPlazo = 30;
    }

    public FnConvenioPagoDetalle(Long id) {
        this.id = id;
    }

    public FnConvenioPagoDetalle(Long id, BigDecimal deuda) {
        this.id = id;
        this.deuda = deuda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiaPlazo() {
        return diaPlazo;
    }

    public void setDiaPlazo(Integer diaPlazo) {
        this.diaPlazo = diaPlazo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDeuda() {
        return deuda;
    }

    public void setDeuda(BigDecimal deuda) {
        this.deuda = deuda;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaMaximaPago() {
        return fechaMaximaPago;
    }

    public void setFechaMaximaPago(Date fechaMaximaPago) {
        this.fechaMaximaPago = fechaMaximaPago;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public FnConvenioPago getConvenio() {
        return convenio;
    }

    public void setConvenio(FnConvenioPago convenio) {
        this.convenio = convenio;
    }

    public BigDecimal getCapitalReducido() {
        return capitalReducido;
    }

    public void setCapitalReducido(BigDecimal capitalReducido) {
        this.capitalReducido = capitalReducido;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
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
        if (!(object instanceof FnConvenioPagoDetalle)) {
            return false;
        }
        FnConvenioPagoDetalle other = (FnConvenioPagoDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnConvenioPagoDetalle[ id=" + id + " ]";
    }

}
