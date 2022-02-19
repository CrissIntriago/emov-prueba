/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "conf_equipo_autorizado", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfEquipoAutorizado.findAll", query = "SELECT c FROM ConfEquipoAutorizado c"),
    @NamedQuery(name = "ConfEquipoAutorizado.findById", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.id = :id"),
    @NamedQuery(name = "ConfEquipoAutorizado.findByIp", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.ip = :ip"),
    @NamedQuery(name = "ConfEquipoAutorizado.findByMac", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.mac = :mac"),
    @NamedQuery(name = "ConfEquipoAutorizado.findByNombreEquipo", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.nombreEquipo = :nombreEquipo"),
    @NamedQuery(name = "ConfEquipoAutorizado.findByRango", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.rango = :rango"),
    @NamedQuery(name = "ConfEquipoAutorizado.findByExcluir", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.excluir = :excluir"),
    @NamedQuery(name = "ConfEquipoAutorizado.findByUsuario", query = "SELECT c FROM ConfEquipoAutorizado c WHERE c.usuario = :usuario")})
public class ConfEquipoAutorizado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "ip")
    private String ip;
    @Size(max = 255)
    @Column(name = "mac")
    private String mac;
    @Size(max = 255)
    @Column(name = "nombre_equipo")
    private String nombreEquipo;
    @Column(name = "rango")
    private Boolean rango;
    @Column(name = "excluir")
    private Boolean excluir;
    @Column(name = "usuario")
    private BigInteger usuario;

    public ConfEquipoAutorizado() {
    }

    public ConfEquipoAutorizado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public Boolean getRango() {
        return rango;
    }

    public void setRango(Boolean rango) {
        this.rango = rango;
    }

    public Boolean getExcluir() {
        return excluir;
    }

    public void setExcluir(Boolean excluir) {
        this.excluir = excluir;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof ConfEquipoAutorizado)) {
            return false;
        }
        ConfEquipoAutorizado other = (ConfEquipoAutorizado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfEquipoAutorizado[ id=" + id + " ]";
    }
    
}
