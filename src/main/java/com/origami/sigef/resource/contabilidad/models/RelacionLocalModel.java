/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.models;

import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Criss Intriago
 */
public class RelacionLocalModel implements Serializable {

    private List<PartePresupuestariaModel> resultList;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;

    public RelacionLocalModel() {
    }

    public RelacionLocalModel(List<PartePresupuestariaModel> resultList, ContDiarioGeneralDetalle contDiarioGeneralDetalle) {
        this.resultList = resultList;
        this.contDiarioGeneralDetalle = contDiarioGeneralDetalle;
    }

    public List<PartePresupuestariaModel> getResultList() {
        return resultList;
    }

    public void setResultList(List<PartePresupuestariaModel> resultList) {
        this.resultList = resultList;
    }

    public ContDiarioGeneralDetalle getContDiarioGeneralDetalle() {
        return contDiarioGeneralDetalle;
    }

    public void setContDiarioGeneralDetalle(ContDiarioGeneralDetalle contDiarioGeneralDetalle) {
        this.contDiarioGeneralDetalle = contDiarioGeneralDetalle;
    }

}
