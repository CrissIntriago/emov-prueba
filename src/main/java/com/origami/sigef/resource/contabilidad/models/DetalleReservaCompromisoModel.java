/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.models;

import com.origami.sigef.common.entities.Adquisiciones;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Criss Intriago
 */
public class DetalleReservaCompromisoModel implements Serializable {

    private List<PartePresupuestariaModel> detalleList;
    private List<Adquisiciones> adquicionesList;

    public void DetalleReservaCompromisoModel() {

    }

    public DetalleReservaCompromisoModel(List<PartePresupuestariaModel> detalleList, List<Adquisiciones> adquicionesList) {
        this.detalleList = detalleList;
        this.adquicionesList = adquicionesList;
    }

    public List<PartePresupuestariaModel> getDetalleList() {
        return detalleList;
    }

    public void setDetalleList(List<PartePresupuestariaModel> detalleList) {
        this.detalleList = detalleList;
    }

    public List<Adquisiciones> getAdquicionesList() {
        return adquicionesList;
    }

    public void setAdquicionesList(List<Adquisiciones> adquicionesList) {
        this.adquicionesList = adquicionesList;
    }

}
