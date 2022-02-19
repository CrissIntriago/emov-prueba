/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.conf.entities;

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

/**
 *
 * @author Criss Intriagos
 */
@Entity
@Table(name = "query", schema = "conf")
@NamedQueries({
    @NamedQuery(name = "Query.findAll", query = "SELECT c FROM Query c"),
    @NamedQuery(name = "Query.findById", query = "SELECT c FROM Query c WHERE c.id = :id"),
    @NamedQuery(name = "Query.findByCode", query = "SELECT c FROM Query c WHERE c.code = :code"),
    @NamedQuery(name = "Query.findBySql", query = "SELECT c FROM Query c WHERE c.sql = :sql"),
    @NamedQuery(name = "Query.findByParametros", query = "SELECT c FROM Query c WHERE c.parametros = :parametros"),
    @NamedQuery(name = "Query.findByTipo", query = "SELECT c FROM Query c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "Query.findByDescripcion", query = "SELECT c FROM Query c WHERE c.descripcion = :descripcion")})
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "sql")
    private String sql;
    @Column(name = "parametros")
    private Integer parametros;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private boolean tipo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;

    public Query() {
    }

    public Query(Long id) {
        this.id = id;
    }

    public Query(Long id, String code, String sql, boolean tipo) {
        this.id = id;
        this.code = code;
        this.sql = sql;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getParametros() {
        return parametros;
    }

    public void setParametros(Integer parametros) {
        this.parametros = parametros;
    }

    public boolean getTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Query)) {
            return false;
        }
        Query other = (Query) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.conf.entities [ id=" + id + " ]";
    }

}
