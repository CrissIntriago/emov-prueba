package com.origami.sigef.resource.contabilidad.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MovimientoCuentasModel implements Serializable {
    private BigDecimal sum_debe;
    private BigDecimal sum_haber;
    private Integer num_diario;
    private Boolean anulado;
    private String tipo;
    private Date fecha_registro;
    private Integer num_comprobante;
    private String beneficiario;
    private String descripcion;
    private String codigo;
    private BigDecimal debe;
    private BigDecimal haber;
    private String nombre_cuenta;

    public MovimientoCuentasModel() {
    }

    public MovimientoCuentasModel(BigDecimal sum_debe, BigDecimal sum_haber, Integer num_diario, Boolean anulado, String tipo, Date fecha_registro, Integer num_comprobante, String beneficiario, String descripcion, String codigo, BigDecimal debe, BigDecimal haber, String nombre_cuenta) {
        this.sum_debe = sum_debe;
        this.sum_haber = sum_haber;
        this.num_diario = num_diario;
        this.anulado = anulado;
        this.tipo = tipo;
        this.fecha_registro = fecha_registro;
        this.num_comprobante = num_comprobante;
        this.beneficiario = beneficiario;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.debe = debe;
        this.haber = haber;
        this.nombre_cuenta = nombre_cuenta;
    }
    
    

    public BigDecimal getSum_debe() {
        return sum_debe;
    }

    public void setSum_debe(BigDecimal sum_debe) {
        this.sum_debe = sum_debe;
    }

    public BigDecimal getSum_haber() {
        return sum_haber;
    }

    public void setSum_haber(BigDecimal sum_haber) {
        this.sum_haber = sum_haber;
    }

    public Integer getNum_diario() {
        return num_diario;
    }

    public void setNum_diario(Integer num_diario) {
        this.num_diario = num_diario;
    }

    public Boolean getAnulado() {
        return anulado;
    }

    public void setAnulado(Boolean anulado) {
        this.anulado = anulado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public Integer getNum_comprobante() {
        return num_comprobante;
    }

    public void setNum_comprobante(Integer num_comprobante) {
        this.num_comprobante = num_comprobante;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public String getNombre_cuenta() {
        return nombre_cuenta;
    }

    public void setNombre_cuenta(String nombre_cuenta) {
        this.nombre_cuenta = nombre_cuenta;
    }

    @Override
    public String toString() {
        return "MovimientoCuentasModel{" + "sum_debe=" + sum_debe + ", sum_haber=" + sum_haber + ", num_diario=" + num_diario + ", anulado=" + anulado + ", tipo=" + tipo + ", fecha_registro=" + fecha_registro + ", num_comprobante=" + num_comprobante + ", beneficiario=" + beneficiario + ", descripcion=" + descripcion + ", codigo=" + codigo + ", debe=" + debe + ", haber=" + haber + ", nombre_cuenta=" + nombre_cuenta + '}';
    }
    
    
    
}
