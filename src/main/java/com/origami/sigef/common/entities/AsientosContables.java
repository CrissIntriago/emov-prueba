/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "asientos_contables", schema = "contabilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsientosContables.findAll", query = "SELECT a FROM AsientosContables a")
    , @NamedQuery(name = "AsientosContables.findById", query = "SELECT a FROM AsientosContables a WHERE a.id = :id")
    , @NamedQuery(name = "AsientosContables.findByTitulo", query = "SELECT a FROM AsientosContables a WHERE a.titulo = :titulo")
    , @NamedQuery(name = "AsientosContables.findBySubtitulo", query = "SELECT a FROM AsientosContables a WHERE a.subtitulo = :subtitulo")
    , @NamedQuery(name = "AsientosContables.findByGrupo", query = "SELECT a FROM AsientosContables a WHERE a.grupo = :grupo")
    , @NamedQuery(name = "AsientosContables.findByCodigo", query = "SELECT a FROM AsientosContables a WHERE a.codigo = :codigo")
    , @NamedQuery(name = "AsientosContables.findByTipo", query = "SELECT a FROM AsientosContables a WHERE a.tipo = :tipo")})
public class AsientosContables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(length = 100, name = "titulo")
    private String titulo;
    @Size(max = 19)
    @Column(length = 100, name = "subtitulo")
    private String subtitulo;
    @Size(max = 19)
    @Column(length = 100, name = "grupo")
    private String grupo;
    @Size(max = 19)
    @Column(length = 100, name = "codigo")
    private String codigo;
    @Column(length = 100, name = "subgrupo")
    private String subgrupo;
    @Column( name = "tipo")
    private BigInteger tipo;
    @Column( name = "orden")
    private Short orden;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asientoContable")
//    private List<BalanceComprobacion> listBalanceComprobacion;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asientoContable")
//    private List<EstadoSituacionFinanciera> listestadoSituacionFinanciera;

    public AsientosContables() {
    }

    public AsientosContables(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
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
        if (!(object instanceof AsientosContables)) {
            return false;
        }
        AsientosContables other = (AsientosContables) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.AsientosContables[ id=" + id + " ]";
    }

}
