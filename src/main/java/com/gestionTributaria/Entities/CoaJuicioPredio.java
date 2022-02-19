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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "coa_juicio_predio", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaJuicioPredio.findAll", query = "SELECT c FROM CoaJuicioPredio c"),
    @NamedQuery(name = "CoaJuicioPredio.findById", query = "SELECT c FROM CoaJuicioPredio c WHERE c.id = :id"),
    @NamedQuery(name = "CoaJuicioPredio.findByAnioDeuda", query = "SELECT c FROM CoaJuicioPredio c WHERE c.anioDeuda = :anioDeuda"),
    @NamedQuery(name = "CoaJuicioPredio.findByEstado", query = "SELECT c FROM CoaJuicioPredio c WHERE c.estado = :estado"),
    @NamedQuery(name = "CoaJuicioPredio.findByLiquidacion", query = "SELECT c FROM CoaJuicioPredio c WHERE c.liquidacion = ?1 AND c.estado = TRUE"),
    @NamedQuery(name = "CoaJuicioPredio.findByObservacion", query = "SELECT c FROM CoaJuicioPredio c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "CoaJuicioPredio.findByValorDeuda", query = "SELECT c FROM CoaJuicioPredio c WHERE c.valorDeuda = :valorDeuda")})
public class CoaJuicioPredio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_deuda")
    private Integer anioDeuda;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_deuda")
    private BigDecimal valorDeuda;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @JoinColumn(name = "abogado_juicio", referencedColumnName = "id")
    @ManyToOne
    private CoaAbogado abogadoJuicio;
    @JoinColumn(name = "estado_juicio", referencedColumnName = "id")
    @ManyToOne
    private CoaEstadoJuicio estadoJuicio;
    @JoinColumn(name = "juicio", referencedColumnName = "id")
    @ManyToOne
    private CoaJuicio juicio;
    @JoinColumn(name = "convenio_pago", referencedColumnName = "id")
    @ManyToOne
    private FnConvenioPago convenioPago;

    @Transient
    private Integer anioDesde;
    @Transient
    private Integer anioHasta;

    public CoaJuicioPredio() {
    }

    public CoaJuicioPredio(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioDeuda() {
        return anioDeuda;
    }

    public void setAnioDeuda(Integer anioDeuda) {
        this.anioDeuda = anioDeuda;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public CoaAbogado getAbogadoJuicio() {
        return abogadoJuicio;
    }

    public void setAbogadoJuicio(CoaAbogado abogadoJuicio) {
        this.abogadoJuicio = abogadoJuicio;
    }

    public CoaEstadoJuicio getEstadoJuicio() {
        return estadoJuicio;
    }

    public void setEstadoJuicio(CoaEstadoJuicio estadoJuicio) {
        this.estadoJuicio = estadoJuicio;
    }

    public CoaJuicio getJuicio() {
        return juicio;
    }

    public void setJuicio(CoaJuicio juicio) {
        this.juicio = juicio;
    }

    public FnConvenioPago getConvenioPago() {
        return convenioPago;
    }

    public void setConvenioPago(FnConvenioPago convenioPago) {
        this.convenioPago = convenioPago;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
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
        if (!(object instanceof CoaJuicioPredio)) {
            return false;
        }
        CoaJuicioPredio other = (CoaJuicioPredio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CoaJuicioPredio{" + "id=" + id + ", anioDeuda=" + anioDeuda + ", estado=" + estado + ", liquidacion=" + liquidacion + ", observacion=" + observacion + ", valorDeuda=" + valorDeuda + ", predio=" + predio + ", abogadoJuicio=" + abogadoJuicio + ", estadoJuicio=" + estadoJuicio + ", juicio=" + juicio + ", convenioPago=" + convenioPago + ", anioDesde=" + anioDesde + ", anioHasta=" + anioHasta + '}';
    }

   

}
