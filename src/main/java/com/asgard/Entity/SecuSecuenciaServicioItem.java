/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "secu_secuencia_servicio_item", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SecuSecuenciaServicioItem.findAll", query = "SELECT s FROM SecuSecuenciaServicioItem s"),
    @NamedQuery(name = "SecuSecuenciaServicioItem.findById", query = "SELECT s FROM SecuSecuenciaServicioItem s WHERE s.id = :id"),
    @NamedQuery(name = "SecuSecuenciaServicioItem.findByAnio", query = "SELECT s FROM SecuSecuenciaServicioItem s WHERE s.anio = :anio"),
    @NamedQuery(name = "SecuSecuenciaServicioItem.findByCode", query = "SELECT s FROM SecuSecuenciaServicioItem s WHERE s.code = :code"),
    @NamedQuery(name = "SecuSecuenciaServicioItem.findBySecuencia", query = "SELECT s FROM SecuSecuenciaServicioItem s WHERE s.secuencia = :secuencia")})
public class SecuSecuenciaServicioItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    @Size(max = 255)
    @Column(name = "code")
    private String code;
    @Column(name = "secuencia")
    private BigInteger secuencia;
    @JoinColumn(name = "servicios_departamento_items", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamento serviciosDepartamentoItems;

    public SecuSecuenciaServicioItem() {
    }

    public SecuSecuenciaServicioItem(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public AppServiciosDepartamento getServiciosDepartamentoItems() {
        return serviciosDepartamentoItems;
    }

    public void setServiciosDepartamentoItems(AppServiciosDepartamento serviciosDepartamentoItems) {
        this.serviciosDepartamentoItems = serviciosDepartamentoItems;
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
        if (!(object instanceof SecuSecuenciaServicioItem)) {
            return false;
        }
        SecuSecuenciaServicioItem other = (SecuSecuenciaServicioItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.SecuSecuenciaServicioItem[ id=" + id + " ]";
    }
    
}
