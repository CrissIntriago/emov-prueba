package com.gestionTributaria.models.tipoPagos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PacificoModel implements Serializable {

    private static final Logger LOG = Logger.getLogger(PacificoModel.class.getName());

    private BigDecimal valor;
    private String codPredio;
    private Integer anio;
    private Integer anioLiq;
    private String nombre;
    private String proceso;
    private String status;
    private String tipoPredio;
    private String codTipoPredio;
    private String fecha;
    private String valorMov;
    private String formaMov;
    private String numPredio;
    //fecha de pago
    private int aniopg;
    private int mes;
    private int dia;

    public PacificoModel() {
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCodPredio() {
        return codPredio;
    }

    public void setCodPredio(String codPredio) {
        this.codPredio = codPredio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getValorMov() {
        return valorMov;
    }

    public void setValorMov(String valorMov) {
        this.valorMov = valorMov;
    }

    public Integer getAnioLiq() {
        return anioLiq;
    }

    public void setAnioLiq(Integer anioLiq) {
        this.anioLiq = anioLiq;
    }

    public String getFormaMov() {
        return formaMov;
    }

    public void setFormaMov(String formaMov) {
        this.formaMov = formaMov;
    }

    public String getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(String numPredio) {
        this.numPredio = numPredio;
    }

    public String getCodTipoPredio() {
        return codTipoPredio;
    }

    public void setCodTipoPredio(String codTipoPredio) {
        this.codTipoPredio = codTipoPredio;
    }

    public int getAniopg() {
        return aniopg;
    }

    public void setAniopg(int aniopg) {
        this.aniopg = aniopg;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public void armarCadenas() {
        this.valor = new BigDecimal(this.valorMov.substring(32, 44) + "." + this.valorMov.substring(45, 47));
        this.codTipoPredio = this.codPredio.substring(0, 2);
        this.numPredio = this.codPredio.substring(2, this.codPredio.length());
        this.dia = Integer.parseInt(this.valorMov.substring(26, 28));
        this.mes = Integer.parseInt(this.valorMov.substring(24, 26));
        this.aniopg = Integer.parseInt(this.valorMov.substring(20, 24));
        switch (codTipoPredio) {
            //urbano
            case "02":
                this.tipoPredio = "U";
                break;
            //rural
            case "03":
                this.tipoPredio = "R";
                break;
        }
        System.out.println("tipo predio>>" + codTipoPredio + " >>" + this.tipoPredio);
        System.out.println("num predio>>" + numPredio);
        System.out.println("anio>>" + aniopg + " mes>>" + mes + " dia>>>" + dia);
    }

}
