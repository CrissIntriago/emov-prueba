/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "view_reportes", schema = Utils.SCHEMA_MIGRACION)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewReportes.findAll", query = "SELECT v FROM ViewReportes v")})
public class ViewReportes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Size(max = 2147483647)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "total_asignados")
    private Integer totalAsignados;
    @Column(name = "total_realizados")
    private BigInteger totalRealizados;
    @Column(name = "eficiencia_validadores")
    private BigDecimal eficienciaValidadores;

    public ViewReportes() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getTotalAsignados() {
        return totalAsignados;
    }

    public void setTotalAsignados(Integer totalAsignados) {
        this.totalAsignados = totalAsignados;
    }

    public BigInteger getTotalRealizados() {
        return totalRealizados;
    }

    public void setTotalRealizados(BigInteger totalRealizados) {
        this.totalRealizados = totalRealizados;
    }

    public BigDecimal getEficienciaValidadores() {
        return eficienciaValidadores;
    }

    public void setEficienciaValidadores(BigDecimal eficienciaValidadores) {
        this.eficienciaValidadores = eficienciaValidadores;
    }
    
}
