/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.util;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Dairon Freddy
 */
public class OpcionBusqueda implements Serializable {

    private Short anio;
    private Short titulo;
    private Short grupo;
    private Short subGrupo;
    private Short nivel1;
    private Short nivel2;
    private Short nivel3;
    private Short nivel4;
    private Short anioAnterior;
    private Short anioSiguiente;

    private String palabraClave;

    private boolean buscando;
    private boolean ejecutandoAccion;

    public OpcionBusqueda() {
        buscando = false;
        ejecutandoAccion = false;
        anio = Utils.getAnio(new Date()).shortValue();
        anioAnterior = (short) (anio.intValue() - 1);
        anioSiguiente = (short) (anio.intValue() + 1);
    }

    public Short getTitulo() {
        return titulo;
    }

    public void setTitulo(Short titulo) {
        this.titulo = titulo;
    }

    public Short getGrupo() {
        return grupo;
    }

    public void setGrupo(Short grupo) {
        this.grupo = grupo;
    }

    public Short getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(Short subGrupo) {
        this.subGrupo = subGrupo;
    }

    public Short getNivel1() {
        return nivel1;
    }

    public void setNivel1(Short nivel1) {
        this.nivel1 = nivel1;
    }

    public Short getNivel2() {
        return nivel2;
    }

    public void setNivel2(Short nivel2) {
        this.nivel2 = nivel2;
    }

    public Short getNivel3() {
        return nivel3;
    }

    public void setNivel3(Short nivel3) {
        this.nivel3 = nivel3;
    }

    public Short getNivel4() {
        return nivel4;
    }

    public void setNivel4(Short nivel4) {
        this.nivel4 = nivel4;
    }

    public String getPalabraClave() {
        return palabraClave;
    }

    public void setPalabraClave(String palabraClave) {
        this.palabraClave = palabraClave;
    }

    public boolean isBuscando() {
        return buscando;
    }

    public void setBuscando(boolean buscando) {
        this.buscando = buscando;
    }

    public boolean isEjecutandoAccion() {
        return ejecutandoAccion;
    }

    public void setEjecutandoAccion(boolean ejecutandoAccion) {
        this.ejecutandoAccion = ejecutandoAccion;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public Short getAnioAnterior() {
        return (short) (anio.intValue() - 1);
    }

    public void setAnioAnterior(Short anioAnterior) {
        this.anioAnterior = anioAnterior;
    }

    public Short getAnioSiguiente() {
        return anioSiguiente;
    }

    public void setAnioSiguiente(Short anioSiguiente) {
        this.anioSiguiente = anioSiguiente;
    }

    @Override
    public String toString() {
        return "OpcionBusqueda{" + "anio=" + anio + ", titulo=" + titulo + ", grupo=" + grupo + ", subGrupo=" + subGrupo + '}';
    }

}
