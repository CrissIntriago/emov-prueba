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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_config",schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThConfig.findAll", query = "SELECT t FROM ThConfig t"),
    @NamedQuery(name = "ThConfig.findById", query = "SELECT t FROM ThConfig t WHERE t.id = :id"),
    @NamedQuery(name = "ThConfig.findByCode", query = "SELECT t FROM ThConfig t WHERE t.code = :code"),
    @NamedQuery(name = "ThConfig.findByCodConfig", query = "SELECT t FROM ThConfig t WHERE t.codConfig = :codConfig")})
public class ThConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "cod_config")
    private String codConfig;

    public ThConfig() {
    }

    public ThConfig(Long id) {
        this.id = id;
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

    public String getCodConfig() {
        return codConfig;
    }

    public void setCodConfig(String codConfig) {
        this.codConfig = codConfig;
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
        if (!(object instanceof ThConfig)) {
            return false;
        }
        ThConfig other = (ThConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThConfig[ id=" + id + " ]";
    }
    
}
