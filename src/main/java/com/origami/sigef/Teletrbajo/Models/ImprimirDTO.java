/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author DEVELOPER
 */
public class ImprimirDTO implements Serializable {

    private Long id;
    private ActividadesDTO data;
    private List<TeletrabajoDTO> datos;

    public ImprimirDTO() {
    }

    public ImprimirDTO(ActividadesDTO data, List<TeletrabajoDTO> datos ) {
        this.data = data;
        this.datos=datos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActividadesDTO getData() {
        return data;
    }

    public void setData(ActividadesDTO data) {
        this.data = data;
    }

    public List<TeletrabajoDTO> getDatos() {
        return datos;
    }

    public void setDatos(List<TeletrabajoDTO> datos) {
        this.datos = datos;
    }

    

    
    
}
