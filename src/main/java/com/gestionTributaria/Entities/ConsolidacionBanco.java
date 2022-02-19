/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "consolidacion_banco", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsolidacionBanco.findAll", query = "SELECT m FROM ConsolidacionBanco m")})
public class ConsolidacionBanco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id

    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "num_predio")
    private BigInteger numPredio;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "anio_fin")
    private Integer anioFinal;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 14, scale = 4)
    private BigDecimal valor;
    @Size(max = 20)
    @Column(name = "banco", length = 20)
    private String banco;
    @Size(max = 10)
    @Column(name = "estado", length = 10)
    private String estado;
    @Column(name = "fecha_cobro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCobro;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Size(max = 100)
    @Column(name = "usr_cre", length = 100)
    private String usrCre;
    @Column(name = "fec_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;

    public ConsolidacionBanco() {
    }

    public ConsolidacionBanco(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(BigInteger numPredio) {
        this.numPredio = numPredio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(Date fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getUsrCre() {
        return usrCre;
    }

    public void setUsrCre(String usrCre) {
        this.usrCre = usrCre;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public Integer getAnioFinal() {
        return anioFinal;
    }

    public void setAnioFinal(Integer anioFinal) {
        this.anioFinal = anioFinal;
    }
    
    

}
