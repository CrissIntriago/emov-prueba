/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class AvalBandaImpositivaDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private BigDecimal multiploImpuestoPredial;
    private Integer anioInicio;
    private Integer anioFin;
    private String estado;
    private Boolean predeterminada = Boolean.FALSE;
    private BigDecimal desdeUs;
    private BigDecimal hastaUs;
    private List<AvalImpuestoPrediosDTO> avalImpuestoPredioss;
    
    public AvalBandaImpositivaDTO() {
    }
    public AvalBandaImpositivaDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMultiploImpuestoPredial() {
        return multiploImpuestoPredial;
    }

    public void setMultiploImpuestoPredial(BigDecimal multiploImpuestoPredial) {
        this.multiploImpuestoPredial = multiploImpuestoPredial;
    }

    public Integer getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Integer anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getPredeterminada() {
        return predeterminada;
    }

    public void setPredeterminada(Boolean predeterminada) {
        this.predeterminada = predeterminada;
    }

    public BigDecimal getDesdeUs() {
        return desdeUs;
    }

    public void setDesdeUs(BigDecimal desdeUs) {
        this.desdeUs = desdeUs;
    }

    public BigDecimal getHastaUs() {
        return hastaUs;
    }

    public void setHastaUs(BigDecimal hastaUs) {
        this.hastaUs = hastaUs;
    }

    public List<AvalImpuestoPrediosDTO> getAvalImpuestoPredioss() {
        return avalImpuestoPredioss;
    }

    public void setAvalImpuestoPredioss(List<AvalImpuestoPrediosDTO> avalImpuestoPredioss) {
        this.avalImpuestoPredioss = avalImpuestoPredioss;
    }
    
    
}
