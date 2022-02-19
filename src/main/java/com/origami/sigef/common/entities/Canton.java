/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
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

/*
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "canton")
@NamedQueries({
    @NamedQuery(name = "Canton.findAll", query = "SELECT c FROM Canton c"),
    @NamedQuery(name = "Canton.findById", query = "SELECT c FROM Canton c WHERE c.id = :id"),
    @NamedQuery(name = "Canton.findByCodProvincia", query = "SELECT c FROM Canton c WHERE c.codProvincia = :codProvincia"),
    @NamedQuery(name = "Canton.findByCodCanton", query = "SELECT c FROM Canton c WHERE c.codCanton = :codCanton"),
    @NamedQuery(name = "Canton.findByCodigo", query = "SELECT c FROM Canton c WHERE c.codigo = ?1"),
    @NamedQuery(name = "Canton.findByCanton", query = "SELECT c FROM Canton c WHERE UPPER(c.canton) LIKE ?1"),
    @NamedQuery(name = "Canton.findByDescripcion", query = "SELECT c FROM Canton c WHERE UPPER(c.descripcion) LIKE ?1"),
    @NamedQuery(name = "Canton.findByHabilitado", query = "SELECT c FROM Canton c WHERE c.habilitado = :habilitado")})
public class Canton implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "cod_provincia")
    private Short codProvincia;
    @Basic(optional = false)
    @Column(name = "cod_canton")
    private Short codCanton;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "canton")
    private String canton;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "habilitado")
    private Boolean habilitado;

    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Provincia idProvincia;

    public Canton() {
    }

    public Canton(Long id) {
        this.id = id;
    }

    public Canton(Long id, Short codProvincia, Short codCanton, String codigo, String canton, Boolean habilitado) {
        this.id = id;
        this.codProvincia = codProvincia;
        this.codCanton = codCanton;
        this.codigo = codigo;
        this.canton = canton;
        this.habilitado = habilitado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(Short codProvincia) {
        this.codProvincia = codProvincia;
    }

    public Short getCodCanton() {
        return codCanton;
    }

    public void setCodCanton(Short codCanton) {
        this.codCanton = codCanton;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Provincia getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Provincia idProvincia) {
        this.idProvincia = idProvincia;
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
        if (!(object instanceof Canton)) {
            return false;
        }
        Canton other = (Canton) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Canton{" + "id=" + id + ", codProvincia=" + codProvincia + ", codCanton=" + codCanton + ", codigo=" + codigo + ", canton=" + canton + ", descripcion=" + descripcion + ", habilitado=" + habilitado + ", idProvincia=" + idProvincia + '}';
    }

}
