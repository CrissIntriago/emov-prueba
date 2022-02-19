/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "aval_edad_zona_const", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvalEdadZonaConst.findAll", query = "SELECT a FROM AvalEdadZonaConst a"),
    @NamedQuery(name = "AvalEdadZonaConst.findById", query = "SELECT a FROM AvalEdadZonaConst a WHERE a.id = :id"),
    @NamedQuery(name = "AvalEdadZonaConst.findByEdad", query = "SELECT a FROM AvalEdadZonaConst a WHERE a.edad = :edad"),
    @NamedQuery(name = "AvalEdadZonaConst.findByParroquia", query = "SELECT a FROM AvalEdadZonaConst a WHERE a.parroquia = :parroquia"),
    @NamedQuery(name = "AvalEdadZonaConst.findByZona", query = "SELECT a FROM AvalEdadZonaConst a WHERE a.zona = :zona")})
public class AvalEdadZonaConst implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "edad")
    private Short edad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "parroquia")
    private short parroquia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona")
    private short zona;

    public AvalEdadZonaConst() {
    }

    public AvalEdadZonaConst(Long id) {
        this.id = id;
    }

    public AvalEdadZonaConst(Long id, short parroquia, short zona) {
        this.id = id;
        this.parroquia = parroquia;
        this.zona = zona;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getEdad() {
        return edad;
    }

    public void setEdad(Short edad) {
        this.edad = edad;
    }

    public short getParroquia() {
        return parroquia;
    }

    public void setParroquia(short parroquia) {
        this.parroquia = parroquia;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
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
        if (!(object instanceof AvalEdadZonaConst)) {
            return false;
        }
        AvalEdadZonaConst other = (AvalEdadZonaConst) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AvalEdadZonaConst[ id=" + id + " ]";
    }
    
}
