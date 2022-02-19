/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.conf.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "plan_cuentas", schema = "conf")
@NamedQueries({
    @NamedQuery(name = "PlanCuentas.findAll", query = "SELECT p FROM PlanCuentas p"),
    @NamedQuery(name = "PlanCuentas.findById", query = "SELECT p FROM PlanCuentas p WHERE p.id = :id"),
    @NamedQuery(name = "PlanCuentas.findByNivel", query = "SELECT p FROM PlanCuentas p WHERE p.nivel = :nivel"),
    @NamedQuery(name = "PlanCuentas.findByEstado", query = "SELECT p FROM PlanCuentas p WHERE p.estado = :estado"),
    @NamedQuery(name = "PlanCuentas.findByNumDigito", query = "SELECT p FROM PlanCuentas p WHERE p.numDigito = :numDigito"),
    @NamedQuery(name = "PlanCuentas.findBySeparador", query = "SELECT p FROM PlanCuentas p WHERE p.separador = :separador"),
    @NamedQuery(name = "PlanCuentas.findByCaracter", query = "SELECT p FROM PlanCuentas p WHERE p.caracter = :caracter")})
public class PlanCuentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "num_digito")
    private Integer numDigito;
    @Column(name = "separador")
    private Boolean separador;
    @Size(max = 1)
    @Column(name = "caracter")
    private String caracter;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "programatico")
    private Boolean programatico;
    @JoinColumn(name = "id_tipo_estructura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idTipo_estructura;

    public PlanCuentas() {
        estado = true;
        separador = false;
        caracter = "";
        programatico=false;
    }

    public PlanCuentas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getNumDigito() {
        return numDigito;
    }

    public void setNumDigito(Integer numDigito) {
        this.numDigito = numDigito;
    }

    public Boolean getSeparador() {
        return separador;
    }

    public void setSeparador(Boolean separador) {
        this.separador = separador;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getProgramatico() {
        return programatico;
    }

    public void setProgramatico(Boolean programatico) {
        this.programatico = programatico;
    }

    public CatalogoItem getIdTipo_estructura() {
        return idTipo_estructura;
    }

    public void setIdTipo_estructura(CatalogoItem idTipo_estructura) {
        this.idTipo_estructura = idTipo_estructura;
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
        if (!(object instanceof PlanCuentas)) {
            return false;
        }
        PlanCuentas other = (PlanCuentas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.conf.entities.PlanCuentas[ id=" + id + " ]";
    }

}
