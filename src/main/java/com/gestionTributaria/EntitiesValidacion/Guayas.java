/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "guayas", schema = Utils.SCHEMA_MIGRACION)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Guayas.findAll", query = "SELECT g FROM Guayas g"),
    @NamedQuery(name = "Guayas.findByCedula", query = "SELECT g FROM Guayas g WHERE g.cedula = :cedula"),
    @NamedQuery(name = "Guayas.findByNombres", query = "SELECT g FROM Guayas g WHERE g.nombres = :nombres"),
    @NamedQuery(name = "Guayas.findByProvincia", query = "SELECT g FROM Guayas g WHERE g.provincia = :provincia"),
    @NamedQuery(name = "Guayas.findByCanton", query = "SELECT g FROM Guayas g WHERE g.canton = :canton"),
    @NamedQuery(name = "Guayas.findByParroquia", query = "SELECT g FROM Guayas g WHERE g.parroquia = :parroquia"),
    @NamedQuery(name = "Guayas.findByZona", query = "SELECT g FROM Guayas g WHERE g.zona = :zona"),
    @NamedQuery(name = "Guayas.findById", query = "SELECT g FROM Guayas g WHERE g.id = :id")})
public class Guayas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cedula")
    private String cedula;
    @Size(max = 255)
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 255)
    @Column(name = "provincia")
    private String provincia;
    @Size(max = 255)
    @Column(name = "canton")
    private String canton;
    @Size(max = 255)
    @Column(name = "parroquia")
    private String parroquia;
    @Size(max = 255)
    @Column(name = "zona")
    private String zona;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    public Guayas() {
    }

    public Guayas(Long id) {
        this.id = id;
    }

    public Guayas(Long id, String cedula) {
        this.id = id;
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Guayas)) {
            return false;
        }
        Guayas other = (Guayas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.EntitiesValidacion.Guayas[ id=" + id + " ]";
    }

}
