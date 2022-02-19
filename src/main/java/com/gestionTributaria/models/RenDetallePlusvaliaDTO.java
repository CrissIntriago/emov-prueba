/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.gestionTributaria.Entities.RenValoresPlusvalia;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class RenDetallePlusvaliaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private CatPredioModel prediosAsociados;
    private RenValoresPlusvalia valoresPlusvalia;

    public RenDetallePlusvaliaDTO() {
    }

    public RenDetallePlusvaliaDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatPredioModel getPrediosAsociados() {
        return prediosAsociados;
    }

    public void setPrediosAsociados(CatPredioModel prediosAsociados) {
        this.prediosAsociados = prediosAsociados;
    }

    public RenValoresPlusvalia getValoresPlusvalia() {
        return valoresPlusvalia;
    }

    public void setValoresPlusvalia(RenValoresPlusvalia valoresPlusvalia) {
        this.valoresPlusvalia = valoresPlusvalia;
    }
}
