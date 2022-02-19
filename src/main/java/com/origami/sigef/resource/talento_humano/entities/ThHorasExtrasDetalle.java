/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_horas_extras_detalle", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThHorasExtrasDetalle.findAll", query = "SELECT t FROM ThHorasExtrasDetalle t"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findById", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.id = :id"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByIdHorasExtras", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.idHorasExtras = ?1 AND t.estado = true ORDER BY t.porcentaje"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByPorcentaje", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.porcentaje = :porcentaje"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByIndice", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.indice = :indice"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByHoras", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.horas = :horas"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByValor", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.valor = :valor"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByEstado", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThHorasExtrasDetalle.findByTipo", query = "SELECT t FROM ThHorasExtrasDetalle t WHERE t.tipo = :tipo")})
public class ThHorasExtrasDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "porcentaje")
    private Integer porcentaje;
    @Column(name = "indice")
    private BigDecimal indice;
    @Column(name = "horas")
    private Integer horas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "id_horas_extras", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThHorasExtras idHorasExtras;
    @JoinColumn(name = "id_th_config_horas", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThConfigHorasExtra idThConfigHoras;

    public ThHorasExtrasDetalle() {
        this.estado = Boolean.TRUE;
        this.valor = BigDecimal.ZERO;
    }

    public ThHorasExtrasDetalle(Long id) {
        this.id = id;
    }

    public ThHorasExtrasDetalle(Integer porcentaje, BigDecimal indice, String tipo, ThHorasExtras idHorasExtras, ThConfigHorasExtra idThConfigHoras) {
        this.porcentaje = porcentaje;
        this.indice = indice;
        this.tipo = tipo;
        this.idHorasExtras = idHorasExtras;
        this.estado = true;
        this.horas = 0;
        this.valor = BigDecimal.ZERO;
        this.idThConfigHoras = idThConfigHoras;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getIndice() {
        return indice;
    }

    public void setIndice(BigDecimal indice) {
        this.indice = indice;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ThHorasExtras getIdHorasExtras() {
        return idHorasExtras;
    }

    public void setIdHorasExtras(ThHorasExtras idHorasExtras) {
        this.idHorasExtras = idHorasExtras;
    }

    public ThConfigHorasExtra getIdThConfigHoras() {
        return idThConfigHoras;
    }

    public void setIdThConfigHoras(ThConfigHorasExtra idThConfigHoras) {
        this.idThConfigHoras = idThConfigHoras;
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
        if (!(object instanceof ThHorasExtrasDetalle)) {
            return false;
        }
        ThHorasExtrasDetalle other = (ThHorasExtrasDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThHorasExtrasDetalle[ id=" + id + " ]";
    }

}
