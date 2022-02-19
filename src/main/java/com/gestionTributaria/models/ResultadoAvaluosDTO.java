/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultadoAvaluosDTO implements Serializable {

    private static final Long serialVersionUID = 1L;
    private List<DatosAvaluos> avaluos;
    private List<DatosNovedad> novedades;

    public ResultadoAvaluosDTO() {
    }

    public ResultadoAvaluosDTO(List<DatosAvaluos> avaluos, List<DatosNovedad> novedades) {
        this.avaluos = avaluos;
        this.novedades = novedades;
    }

    public List<DatosAvaluos> getAvaluos() {
        return avaluos;
    }

    public void setAvaluos(List<DatosAvaluos> avaluos) {
        this.avaluos = avaluos;
    }

    public List<DatosNovedad> getNovedades() {
        return novedades;
    }

    public void setNovedades(List<DatosNovedad> novedades) {
        this.novedades = novedades;
    }
}