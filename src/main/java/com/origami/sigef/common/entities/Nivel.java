/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "nivel")
@NamedQueries({
    @NamedQuery(name = "Nivel.findAll", query = "SELECT n FROM Nivel n")
    ,
    @NamedQuery(name = "Nivel.findById", query = "SELECT n FROM Nivel n WHERE n.id = :id")
    ,
    @NamedQuery(name = "Nivel.findByCatalogoAndCodigo", query = "SELECT n FROM Nivel n JOIN n.tipo t JOIN t.catalogo c WHERE c.codigo = ?1 AND t.codigo = ?2 ORDER BY n.orden ASC")
    ,
    @NamedQuery(name = "Nivel.findByCatalogoAndCodigoAndOrden", query = "SELECT n FROM Nivel n JOIN n.tipo t JOIN t.catalogo c WHERE c.codigo = ?1 AND t.codigo = ?2 AND n.orden = ?3")
    ,
    @NamedQuery(name = "Nivel.findByNombre", query = "SELECT n FROM Nivel n WHERE n.nombre = :nombre")
    ,
    @NamedQuery(name = "Nivel.findByDescripcion", query = "SELECT n FROM Nivel n WHERE n.descripcion = :descripcion")
    ,
    @NamedQuery(name = "Nivel.findByLongitud", query = "SELECT n FROM Nivel n WHERE n.longitud = :longitud")
    ,
    @NamedQuery(name = "Nivel.findNext", query = "SELECT n FROM Nivel n JOIN n.tipo t WHERE t.id = ?1 AND n.orden = ?2")
    ,
    @NamedQuery(name = "Nivel.findByOrden", query = "SELECT n FROM Nivel n WHERE n.orden = :orden")})
public class Nivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitud")
    private Short longitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private Short orden;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @OneToMany(mappedBy = "nivel")
    private List<CatalogoPresupuesto> catalogosPresupuesto;

    public Nivel() {
    }

    public Nivel(Long id) {
        this.id = id;
    }

    public Nivel(Long id, String nombre, short longitud, short orden) {
        this.id = id;
        this.nombre = nombre;
        this.longitud = longitud;
        this.orden = orden;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getLongitud() {
        return longitud;
    }

    public void setLongitud(Short longitud) {
        this.longitud = longitud;
    }

    public short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public List<CatalogoPresupuesto> getCatalogosPresupuesto() {
        return catalogosPresupuesto;
    }

    public void setCatalogosPresupuesto(List<CatalogoPresupuesto> catalogosPresupuesto) {
        this.catalogosPresupuesto = catalogosPresupuesto;
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
        if (!(object instanceof Nivel)) {
            return false;
        }
        Nivel other = (Nivel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Nivel[ id=" + id + " ]";
    }

}
