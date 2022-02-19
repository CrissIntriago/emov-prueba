/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.modelTarifario;

import java.math.BigDecimal;
import javax.persistence.Transient;

/**
 *
 * @author OrigamiEC
 */
public class ItemCatalogoModelTS {

    @Transient
    private Long idordencobro;
    private String numero_orden;
    @Transient
    private Long iddetalletarifarioservicio;
    private String nombre_item;
    private String numeropapeleta;
    private BigDecimal valor;
    private String codigo_item;

    public Long getIdordencobro() {
        return idordencobro;
    }

    public void setIdordencobro(Long idordencobro) {
        this.idordencobro = idordencobro;
    }

    public String getNumero_orden() {
        return numero_orden;
    }

    public void setNumero_orden(String numero_orden) {
        this.numero_orden = numero_orden;
    }

    public Long getIddetalletarifarioservicio() {
        return iddetalletarifarioservicio;
    }

    public void setIddetalletarifarioservicio(Long iddetalletarifarioservicio) {
        this.iddetalletarifarioservicio = iddetalletarifarioservicio;
    }

    public String getNombre_item() {
        return nombre_item;
    }

    public void setNombre_item(String nombre_item) {
        this.nombre_item = nombre_item;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCodigo_item() {
        return codigo_item;
    }

    public void setCodigo_item(String codigo_item) {
        this.codigo_item = codigo_item;
    }

    public String getNumeropapeleta() {
        return numeropapeleta;
    }

    public void setNumeropapeleta(String numeropapeleta) {
        this.numeropapeleta = numeropapeleta;
    }


}
