package com.gestionTributaria.Entities;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "categoria_mercado", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "CategoriaMercado.findAll", query = "SELECT c FROM CategoriaMercado c WHERE c.estado = true"),
    @NamedQuery(name = "CategoriaMercado.findById", query = "SELECT c FROM CategoriaMercado c WHERE c.id = :id"),
    @NamedQuery(name = "CategoriaMercado.findByDescripcion", query = "SELECT c FROM CategoriaMercado c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CategoriaMercado.findByNombre", query = "SELECT c FROM CategoriaMercado c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CategoriaMercado.findByEstado", query = "SELECT c FROM CategoriaMercado c WHERE c.estado = :estado")})
public class CategoriaMercado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @Size(max = 2147483647)
    @Column(name = "nombre", length = 2147483647)
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;

    public CategoriaMercado() {
        this.estado = Boolean.TRUE;
    }

    public CategoriaMercado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof CategoriaMercado)) {
            return false;
        }
        CategoriaMercado other = (CategoriaMercado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.Categoria[ id=" + id + " ]";
    }

}
