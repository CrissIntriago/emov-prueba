/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_grupo_servidor_detalle", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThGrupoServidorDetalle.findAll", query = "SELECT t FROM ThGrupoServidorDetalle t"),
    @NamedQuery(name = "ThGrupoServidorDetalle.findById", query = "SELECT t FROM ThGrupoServidorDetalle t WHERE t.id = :id"),
    @NamedQuery(name = "ThGrupoServidorDetalle.findByEstado", query = "SELECT t FROM ThGrupoServidorDetalle t WHERE t.estado = :estado")})
public class ThGrupoServidorDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor idServidor;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThGrupoServidor idGrupo;

    public ThGrupoServidorDetalle() {
    }

    public ThGrupoServidorDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Servidor getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Servidor idServidor) {
        this.idServidor = idServidor;
    }

    public ThGrupoServidor getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(ThGrupoServidor idGrupo) {
        this.idGrupo = idGrupo;
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
        if (!(object instanceof ThGrupoServidorDetalle)) {
            return false;
        }
        ThGrupoServidorDetalle other = (ThGrupoServidorDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThGrupoServidorDetalle[ id=" + id + " ]";
    }

}
