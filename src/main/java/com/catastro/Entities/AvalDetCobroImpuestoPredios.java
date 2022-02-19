/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Entities.AvalImpuestoPredios;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "aval_det_cobro_impuesto_predios", schema = Utils.SCHEMA_CATASTRO)
@NamedQueries({
    @NamedQuery(name = "AvalDetCobroImpuestoPredios.findAll", query = "SELECT a FROM AvalDetCobroImpuestoPredios a")})
public class AvalDetCobroImpuestoPredios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_rubro_cobrar", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenRubrosLiquidacion idRubroCobrar;
    @JoinColumn(name = "id_aval_impuesto_predio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AvalImpuestoPredios idAvalImpuestoPredio;

    public AvalDetCobroImpuestoPredios() {
    }

    public AvalDetCobroImpuestoPredios(Long id) {
        this.id = id;
    }

    public AvalDetCobroImpuestoPredios(Long id, FinaRenRubrosLiquidacion idRubroCobrar, AvalImpuestoPredios idAvalImpuestoPredio) {
        this.id = id;
        this.idRubroCobrar = idRubroCobrar;
        this.idAvalImpuestoPredio = idAvalImpuestoPredio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenRubrosLiquidacion getIdRubroCobrar() {
        return idRubroCobrar;
    }

    public void setIdRubroCobrar(FinaRenRubrosLiquidacion idRubroCobrar) {
        this.idRubroCobrar = idRubroCobrar;
    }

    public AvalImpuestoPredios getIdAvalImpuestoPredio() {
        return idAvalImpuestoPredio;
    }

    public void setIdAvalImpuestoPredio(AvalImpuestoPredios idAvalImpuestoPredio) {
        this.idAvalImpuestoPredio = idAvalImpuestoPredio;
    }

}
