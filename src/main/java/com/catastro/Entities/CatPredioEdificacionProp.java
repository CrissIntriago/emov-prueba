/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.gestionTributaria.Entities.CatEdfProp;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_predio_edificacion_prop", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioEdificacionProp.findAll", query = "SELECT c FROM CatPredioEdificacionProp c"),
    @NamedQuery(name = "CatPredioEdificacionProp.findById", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioEdificacionProp.findByPorcentaje", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.porcentaje = :porcentaje"),
    @NamedQuery(name = "CatPredioEdificacionProp.findByEstado", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatPredioEdificacionProp.findByFecha", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CatPredioEdificacionProp.findByUsuario", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CatPredioEdificacionProp.findByModificado", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.modificado = :modificado"),
    @NamedQuery(name = "CatPredioEdificacionProp.findByValor", query = "SELECT c FROM CatPredioEdificacionProp c WHERE c.valor = :valor")})
public class CatPredioEdificacionProp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "porcentaje")
    private BigInteger porcentaje;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 40)
    @Column(name = "modificado")
    private String modificado;
    @Column(name = "valor")
    private BigInteger valor;
    @JoinColumn(name = "prop", referencedColumnName = "id")
    @ManyToOne
    private CatEdfProp prop;
    @JoinColumn(name = "edificacion", referencedColumnName = "id")
    @ManyToOne
    private CatPredioEdificacion edificacion;

    public CatPredioEdificacionProp() {
    }

    public CatPredioEdificacionProp(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigInteger porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public CatEdfProp getProp() {
        return prop;
    }

    public void setProp(CatEdfProp prop) {
        this.prop = prop;
    }

    public CatPredioEdificacion getEdificacion() {
        return edificacion;
    }

    public void setEdificacion(CatPredioEdificacion edificacion) {
        this.edificacion = edificacion;
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
        if (!(object instanceof CatPredioEdificacionProp)) {
            return false;
        }
        CatPredioEdificacionProp other = (CatPredioEdificacionProp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioEdificacionProp[ id=" + id + " ]";
    }

}
