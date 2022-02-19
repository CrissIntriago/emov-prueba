/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.AppDepartamentoDetalleTitulos;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_departamento_detalle_titulos", schema = Utils.SCHEMA_SGM)
public class RenDepartamentoDetalleTitulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "departamento_detalle_titulos", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppDepartamentoDetalleTitulos planificacion;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenTipoLiquidacion tipoLiquidacion;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "valor_calcular")
    private BigDecimal valorCalcular;

    public RenDepartamentoDetalleTitulos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppDepartamentoDetalleTitulos getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(AppDepartamentoDetalleTitulos planificacion) {
        this.planificacion = planificacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorCalcular() {
        return valorCalcular;
    }

    public void setValorCalcular(BigDecimal valorCalcular) {
        this.valorCalcular = valorCalcular;
    }

}
