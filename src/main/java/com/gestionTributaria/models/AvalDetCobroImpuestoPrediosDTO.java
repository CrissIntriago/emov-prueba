/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class AvalDetCobroImpuestoPrediosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
  
    private Long id;
   
    private FinaRenRubrosLiquidacion idRubroCobrar;

    private AvalImpuestoPrediosDTO idAvalImpuestoPredio;

    public AvalDetCobroImpuestoPrediosDTO() {
    }

    public AvalDetCobroImpuestoPrediosDTO(Long id) {
        this.id = id;
    }

    public AvalDetCobroImpuestoPrediosDTO(Long id, FinaRenRubrosLiquidacion idRubroCobrar, AvalImpuestoPrediosDTO idAvalImpuestoPredio) {
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

    public AvalImpuestoPrediosDTO getIdAvalImpuestoPredio() {
        return idAvalImpuestoPredio;
    }

    public void setIdAvalImpuestoPredio(AvalImpuestoPrediosDTO idAvalImpuestoPredio) {
        this.idAvalImpuestoPredio = idAvalImpuestoPredio;
    }

    
    
}