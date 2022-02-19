/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.entities.Canton;
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
import javax.xml.bind.annotation.XmlRootElement;
import sun.nio.cs.ext.Big5;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "participacion_cantones", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipacionCantones.findAll", query = "SELECT f FROM ParticipacionCantones f")})
public class ParticipacionCantones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "cantones", referencedColumnName = "id")
    @ManyToOne
    private Canton cantones;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "valor")
    private BigDecimal valor;

    public void ParticipacionCantones() {
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

    public Canton getCantones() {
        return cantones;
    }

    public void setCantones(Canton cantones) {
        this.cantones = cantones;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
