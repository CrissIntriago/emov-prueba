/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ORIGAMI
 */
@Entity
@Table(schema = "ats", name = "pais_paraiso_fiscal")
@NamedQueries({
    @NamedQuery(name = "PaisParaisoFiscal.findAll", query = "SELECT p FROM PaisParaisoFiscal p ORDER BY p.paisElegir")})
public class PaisParaisoFiscal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "paraiso_fiscal")
    private String paraisoFiscal;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "pais_elegir")
    private String paisElegir;
    @Column(name = "codigo_pais")
    private String codigoPais;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    public PaisParaisoFiscal() {
    }

    public PaisParaisoFiscal(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParaisoFiscal() {
        return paraisoFiscal;
    }

    public void setParaisoFiscal(String paraisoFiscal) {
        this.paraisoFiscal = paraisoFiscal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPaisElegir() {
        return paisElegir;
    }

    public void setPaisElegir(String paisElegir) {
        this.paisElegir = paisElegir;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaisParaisoFiscal)) {
            return false;
        }
        PaisParaisoFiscal other = (PaisParaisoFiscal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "porSiseDania.PaisParaisoFiscal[ id=" + id + " ]";
    }

}
