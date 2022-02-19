/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.modelTarifario;

import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.ItemTarifario;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.persistence.Transient;

/**
 *
 * @author ORIGAMI2
 */
public class CorteOrdenCobroModelTS implements Serializable {

    private String codSolicitud;
    @Transient
    private Long idordencobro;
    private String ordencobro;
    private String placa;
    private String codigotarifa;
    private String nombreitem;
    private String identificacion;
    private String nombre;
    private BigDecimal total;
    private String numeropapeleta;
    private String fechaemision;
    private String estadoorden;
    private String comprobanteinterno;
    private String banco;
    @Transient
    private Long idbanco;

    private ItemTarifario item;
    private CuentaContable cuentaCaja;
    boolean estado;
    boolean seteado;

    @PostConstruct
    public void init() {
        this.estado = false;
        this.seteado = false;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getOrdencobro() {
        return ordencobro;
    }

    public void setOrdencobro(String ordencobro) {
        this.ordencobro = ordencobro;
    }

    public Long getIdordencobro() {
        return idordencobro;
    }

    public void setIdordencobro(Long idordencobro) {
        this.idordencobro = idordencobro;
    }

    public String getCodigotarifa() {
        return codigotarifa;
    }

    public void setCodigotarifa(String codigotarifa) {
        this.codigotarifa = codigotarifa;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNumeropapeleta() {
        return numeropapeleta;
    }

    public void setNumeropapeleta(String numeropapeleta) {
        this.numeropapeleta = numeropapeleta;
    }

    public String getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(String fechaemision) {
        this.fechaemision = fechaemision;
    }

    public String getEstadoorden() {
        return estadoorden;
    }

    public void setEstadoorden(String estadoorden) {
        this.estadoorden = estadoorden;
    }

    public String getComprobanteinterno() {
        return comprobanteinterno;
    }

    public void setComprobanteinterno(String comprobanteinterno) {
        this.comprobanteinterno = comprobanteinterno;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public ItemTarifario getItem() {
        return item;
    }

    public void setItem(ItemTarifario item) {
        this.item = item;
    }

    public Long getIdbanco() {
        return idbanco;
    }

    public void setIdbanco(Long idbanco) {
        this.idbanco = idbanco;
    }

    public CuentaContable getCuentaCaja() {
        return cuentaCaja;
    }

    public void setCuentaCaja(CuentaContable cuentaCaja) {
        this.cuentaCaja = cuentaCaja;
    }

    public String getnombreitem() {
        return nombreitem;
    }

    public void setnombreitem(String nombreitem) {
        this.nombreitem = nombreitem;
    }

    public String getNombreitem() {
        return nombreitem;
    }

    public void setNombreitem(String nombreitem) {
        this.nombreitem = nombreitem;
    }

    public boolean getSeteado() {
        return seteado;
    }

    public void setSeteado(boolean seteado) {
        this.seteado = seteado;
    }

    public String getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(String codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

}
