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
 * @author ORIGAMI1
 */
@Entity
@Table(name = "beneficios_decimo_cuarto", schema = "talento_humano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BeneficiosDecimoCuarto.findAll", query = "SELECT b FROM BeneficiosDecimoCuarto b")
    ,
    @NamedQuery(name = "BeneficiosDecimoCuarto.findById", query = "SELECT b FROM BeneficiosDecimoCuarto b WHERE b.id = :id")
    ,
    @NamedQuery(name = "BeneficiosDecimoCuarto.findByEstado", query = "SELECT b FROM BeneficiosDecimoCuarto b WHERE b.estado = :estado")
    ,
    @NamedQuery(name = "BeneficiosDecimoCuarto.findByMeses", query = "SELECT b FROM BeneficiosDecimoCuarto b WHERE b.meses = :meses")
    ,
    @NamedQuery(name = "BeneficiosDecimoCuarto.findByAjuste", query = "SELECT b FROM BeneficiosDecimoCuarto b WHERE b.ajuste = :ajuste")
    ,
    @NamedQuery(name = "BeneficiosDecimoCuarto.findByCobrar", query = "SELECT b FROM BeneficiosDecimoCuarto b WHERE b.cobrar = :cobrar")
    ,
    @NamedQuery(name = "BeneficiosDecimoCuarto.findByBaseImponible", query = "SELECT b FROM BeneficiosDecimoCuarto b WHERE b.baseImponible = :baseImponible")})
public class BeneficiosDecimoCuarto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "meses")
    private Short meses;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ajuste", precision = 19, scale = 2)
    private BigDecimal ajuste;
    @Column(name = "cobrar", precision = 19, scale = 2)
    private BigDecimal cobrar;
    @Column(name = "base_imponible", precision = 19, scale = 2)
    private BigDecimal baseImponible;
    @Column(name = "descuento", precision = 19, scale = 2)
    private BigDecimal descuento;
    @JoinColumn(name = "acumulacion_fondos", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AcumulacionFondoReserva acumulacionFondos;
    @JoinColumn(name = "tipo_rol_beneficio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRolBeneficios tipoRolBeneficio;
    @JoinColumn(name = "valor_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ValoresRoles valorRol;
    @Column(name = "cobrado_rol", precision = 19, scale = 2)
    private BigDecimal cobradoRol;
    @Column(name = "activo")
    private Boolean activo;

    public BeneficiosDecimoCuarto() {
    }

    public BeneficiosDecimoCuarto(Boolean estado, Short meses, BigDecimal ajuste, BigDecimal cobrar, BigDecimal baseImponible, BigDecimal descuento) {
        this.estado = estado;
        this.meses = meses;
        this.ajuste = ajuste;
        this.cobrar = cobrar;
        this.baseImponible = baseImponible;
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

    public Short getMeses() {
        return meses;
    }

    public void setMeses(Short meses) {
        this.meses = meses;
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

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public AcumulacionFondoReserva getAcumulacionFondos() {
        return acumulacionFondos;
    }

    public void setAcumulacionFondos(AcumulacionFondoReserva acumulacionFondos) {
        this.acumulacionFondos = acumulacionFondos;
    }

    public TipoRolBeneficios getTipoRolBeneficio() {
        return tipoRolBeneficio;
    }

    public void setTipoRolBeneficio(TipoRolBeneficios tipoRolBeneficio) {
        this.tipoRolBeneficio = tipoRolBeneficio;
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

    public BigDecimal getCobradoRol() {
        return cobradoRol;
    }

    public void setCobradoRol(BigDecimal cobradoRol) {
        this.cobradoRol = cobradoRol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        if (!(object instanceof BeneficiosDecimoCuarto)) {
            return false;
        }
        BeneficiosDecimoCuarto other = (BeneficiosDecimoCuarto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.BeneficiosDecimoCuarto[ id=" + id + " ]";
    }

}
