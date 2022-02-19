/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_regimen_laboral", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThRegimenLaboral.findAll", query = "SELECT t FROM ThRegimenLaboral t"),
    @NamedQuery(name = "ThRegimenLaboral.findById", query = "SELECT t FROM ThRegimenLaboral t WHERE t.id = :id"),
    @NamedQuery(name = "ThRegimenLaboral.findByNombre", query = "SELECT t FROM ThRegimenLaboral t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "ThRegimenLaboral.findByRegimen", query = "SELECT t FROM ThRegimenLaboral t WHERE t.estado = ?1 ORDER BY t.nombre ASC"),
    @NamedQuery(name = "ThRegimenLaboral.findByCodigo", query = "SELECT t FROM ThRegimenLaboral t WHERE t.codigo = :codigo")})
public class ThRegimenLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 320)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(mappedBy = "padre")
    private List<ThRegimenLaboral> thRegimenLaboralList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRegimenLaboral padre;
    @Column(name = "estado")
    private Boolean estado;

    public ThRegimenLaboral() {
        this.estado = true;
    }

    public ThRegimenLaboral(ThRegimenLaboral padre) {
        this.padre = padre;
        this.estado = true;
    }

    public ThRegimenLaboral(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<ThRegimenLaboral> getThRegimenLaboralList() {
        return thRegimenLaboralList;
    }

    public void setThRegimenLaboralList(List<ThRegimenLaboral> thRegimenLaboralList) {
        this.thRegimenLaboralList = thRegimenLaboralList;
    }

    public ThRegimenLaboral getPadre() {
        return padre;
    }

    public void setPadre(ThRegimenLaboral padre) {
        this.padre = padre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof ThRegimenLaboral)) {
            return false;
        }
        ThRegimenLaboral other = (ThRegimenLaboral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral[ id=" + id + " ]";
    }

}
