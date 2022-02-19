/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Entity;

import com.origami.sigef.common.entities.CatalogoItem;
import java.io.Serializable;
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

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "herramientas_utilizadas", schema = "public")

@NamedQueries({
    @NamedQuery(name = "HerramientasUtilizadas.findAll", query = "SELECT s FROM HerramientasUtilizadas s"),
    @NamedQuery(name = "HerramientasUtilizadas.findById", query = "SELECT s FROM HerramientasUtilizadas s WHERE s.id = :id")})

public class HerramientasUtilizadas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "teletrabajo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Teletrabajo teletrabajo;

    @JoinColumn(name = "herramientas", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem herramientas;

    public HerramientasUtilizadas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teletrabajo getTeletrabajo() {
        return teletrabajo;
    }

    public void setTeletrabajo(Teletrabajo teletrabajo) {
        this.teletrabajo = teletrabajo;
    }

    public CatalogoItem getHerramientas() {
        return herramientas;
    }

    public void setHerramientas(CatalogoItem herramientas) {
        this.herramientas = herramientas;
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
        if (!(object instanceof HerramientasUtilizadas)) {
            return false;
        }
        HerramientasUtilizadas other = (HerramientasUtilizadas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Teletrbajo.Entity.HerramientasUtilizadas[ id=" + id + " ]";
    }

}
