/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Entity
@Table(name = "beneficios_decimo_tercero", schema = "talento_humano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BeneficiosDecimoTercero.findAll", query = "SELECT b FROM BeneficiosDecimoTercero b"),
    @NamedQuery(name = "BeneficiosDecimoTercero.findById", query = "SELECT b FROM BeneficiosDecimoTercero b WHERE b.id = :id"),
    @NamedQuery(name = "BeneficiosDecimoTercero.findByEstado", query = "SELECT b FROM BeneficiosDecimoTercero b WHERE b.estado = :estado")})
public class BeneficiosDecimoTercero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
//    @Column(name = "anio")
//    private Short anio;
    @JoinColumn(name = "tipo_rol_beneficio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRolBeneficios tipoRolBeneficio;
    @JoinColumn(name = "acumulacion_fondos_reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AcumulacionFondoReserva acumulacionFondoReserva;
    @Column(name = "meses")
    private Short meses;
    @Column(name = "total_ganado", precision = 19, scale = 2)
    private BigDecimal totalGanado;
    @Column(name = "ajuste", precision = 19, scale = 2)
    private BigDecimal ajuste;
    @Column(name = "cobrar", precision = 19, scale = 2)
    private BigDecimal cobrar;
    @Column(name = "decimo_tercer_ganado", precision = 19, scale = 2)
    private BigDecimal decimoTercerGanado;
    @Column(name = "descuento", precision = 19, scale = 2)
    private BigDecimal descuento;
    @JoinColumn(name = "valor_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ValoresRoles valorRol;

    public BeneficiosDecimoTercero() {
    }

    public BeneficiosDecimoTercero(Boolean estado, Short meses, BigDecimal totalGanado, BigDecimal ajuste, BigDecimal cobrar, BigDecimal decimoTercerGanado, BigDecimal descuento) {
        this.estado = estado;
        this.meses = meses;
        this.totalGanado = totalGanado;
        this.ajuste = ajuste;
        this.cobrar = cobrar;
        this.decimoTercerGanado = decimoTercerGanado;
        this.descuento = descuento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

//    public Short getAnio() {
//        return anio;
//    }
//
//    public void setAnio(Short anio) {
//        this.anio = anio;
//    }

    public TipoRolBeneficios getTipoRolBeneficio() {
        return tipoRolBeneficio;
    }

    public void setTipoRolBeneficio(TipoRolBeneficios tipoRolBeneficio) {
        this.tipoRolBeneficio = tipoRolBeneficio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Short getMeses() {
        return meses;
    }

    public void setMeses(Short meses) {
        this.meses = meses;
    }

    public BigDecimal getTotalGanado() {
        return totalGanado;
    }

    public void setTotalGanado(BigDecimal totalGanado) {
        this.totalGanado = totalGanado;
    }

    public BigDecimal getAjuste() {
        return ajuste;
    }

    public void setAjuste(BigDecimal ajuste) {
        this.ajuste = ajuste;
    }

    public BigDecimal getCobrar() {
        return cobrar;
    }

    public void setCobrar(BigDecimal cobrar) {
        this.cobrar = cobrar;
    }

    public AcumulacionFondoReserva getAcumulacionFondoReserva() {
        return acumulacionFondoReserva;
    }

    public void setAcumulacionFondoReserva(AcumulacionFondoReserva acumulacionFondoReserva) {
        this.acumulacionFondoReserva = acumulacionFondoReserva;
    }

    public BigDecimal getDecimoTercerGanado() {
        return decimoTercerGanado;
    }

    public void setDecimoTercerGanado(BigDecimal decimoTercerGanado) {
        this.decimoTercerGanado = decimoTercerGanado;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public ValoresRoles getValorRol() {
        return valorRol;
    }

    public void setValorRol(ValoresRoles valorRol) {
        this.valorRol = valorRol;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BeneficiosDecimoTercero)) {
            return false;
        }
        BeneficiosDecimoTercero other = (BeneficiosDecimoTercero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.BeneficiosDecimoTercero[ id=" + id + " ]";
    }

}
