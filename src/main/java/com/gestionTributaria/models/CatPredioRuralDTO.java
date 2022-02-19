/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

/**
 *
 * @author Administrator
 */
public class CatPredioRuralDTO {
     private static final Long serialVersionUID = 1L;
    
    private CatPredioRusticoDTO predioRustico;
    private EmisionesRuralesExcelDTO predioRusctico2017;

    public CatPredioRuralDTO() {
    }

    public CatPredioRuralDTO(CatPredioRusticoDTO predioRustico, EmisionesRuralesExcelDTO predioRusctico2017) {
        this.predioRustico = predioRustico;
        this.predioRusctico2017 = predioRusctico2017;
    }

    public CatPredioRusticoDTO getPredioRustico() {
        return predioRustico;
    }

    public void setPredioRustico(CatPredioRusticoDTO predioRustico) {
        this.predioRustico = predioRustico;
    }

    public EmisionesRuralesExcelDTO getPredioRusctico2017() {
        return predioRusctico2017;
    }

    public void setPredioRusctico2017(EmisionesRuralesExcelDTO predioRusctico2017) {
        this.predioRusctico2017 = predioRusctico2017;
    }
    
}
