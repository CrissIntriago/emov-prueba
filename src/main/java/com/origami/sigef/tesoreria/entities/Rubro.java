/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema = "tesoreria", name = "rubro")
public class Rubro implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "rubro_tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RubroTipo rubroTipo;
    private String codigo;
    private String descripcion;
    @Column(name = "tipo_impuesto")
    private String tipoImpuesto;
    @Column(name = "valor")
    private Double valor;
    @Column(name = "porcentual_sbu")
    private Boolean porcentualSbu = Boolean.FALSE;
    private Boolean estado;
    @Column(name = "porcentaje_libre")
    private Boolean porcentajeLibre;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "porcentaje_retencion")
    private BigDecimal porcentajeRetencion;
    private BigDecimal porcentaje;
    private Boolean predeterminado = Boolean.FALSE;

    @JoinColumn(name = "partida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto partida;
    @Column(name = "vigencia_hasta")
    @Temporal(TemporalType.DATE)
    private Date vigenciaHasta;
    @Column(name = "vigente")
    private boolean vigente;

    public Rubro() {
    }

    public Rubro(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getPorcentualSbu() {
        return porcentualSbu;
    }

    public void setPorcentualSbu(Boolean porcentualSbu) {
        this.porcentualSbu = porcentualSbu;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public RubroTipo getRubroTipo() {
        return rubroTipo;
    }

    public void setRubroTipo(RubroTipo rubroTipo) {
        this.rubroTipo = rubroTipo;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public Boolean getPorcentajeLibre() {
        return porcentajeLibre;
    }

    public void setPorcentajeLibre(Boolean porcentajeLibre) {
        this.porcentajeLibre = porcentajeLibre;
    }

    public BigDecimal getPorcentajeRetencion() {
        return porcentajeRetencion;
    }

    public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getPredeterminado() {
        return predeterminado;
    }

    public void setPredeterminado(Boolean predeterminado) {
        this.predeterminado = predeterminado;
    }

    public CatalogoPresupuesto getPartida() {
        return partida;
    }

    public void setPartida(CatalogoPresupuesto partida) {
        this.partida = partida;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Rubro)) {
            return false;
        }
        Rubro other = (Rubro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
