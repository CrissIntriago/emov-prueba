/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_config_horas_extra", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThConfigHorasExtra.findAll", query = "SELECT t FROM ThConfigHorasExtra t"),
    @NamedQuery(name = "ThConfigHorasExtra.findById", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.id = :id"),
    @NamedQuery(name = "ThConfigHorasExtra.validar", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.idClasificacion = ?1 AND t.idTipo = ?2 AND t.porcentaje = ?3 AND t.maxHoras = ?4 AND t.indice = ?5 AND t.estado = true"),
    @NamedQuery(name = "ThConfigHorasExtra.findByIdClasificacion", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.idClasificacion = :idClasificacion"),
    @NamedQuery(name = "ThConfigHorasExtra.findByIdTipo", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.idClasificacion = ?1 AND t.estado = true ORDER BY t.porcentaje ASC"),
    @NamedQuery(name = "ThConfigHorasExtra.findByPorcentaje", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.porcentaje = :porcentaje"),
    @NamedQuery(name = "ThConfigHorasExtra.findByEstado", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThConfigHorasExtra.findByUsuarioCreacion", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThConfigHorasExtra.findByFechaCreacion", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThConfigHorasExtra.findByUsuarioModificacion", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThConfigHorasExtra.findByFechaModificacion", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThConfigHorasExtra.findByMaxHoras", query = "SELECT t FROM ThConfigHorasExtra t WHERE t.maxHoras = :maxHoras")})
public class ThConfigHorasExtra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idClasificacion;
    @Column(name = "porcentaje")
    private Integer porcentaje;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Column(name = "max_horas")
    private Integer maxHoras;
    @Column(name = "indice")
    private BigDecimal indice;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idTipo;

    public ThConfigHorasExtra() {
        this.estado = Boolean.TRUE;
        this.porcentaje = 0;
        this.maxHoras = 0;
        this.indice = BigDecimal.ZERO;
    }

    public ThConfigHorasExtra(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(CatalogoItem idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getMaxHoras() {
        return maxHoras;
    }

    public void setMaxHoras(Integer maxHoras) {
        this.maxHoras = maxHoras;
    }

    public BigDecimal getIndice() {
        return indice;
    }

    public void setIndice(BigDecimal indice) {
        this.indice = indice;
    }

    public CatalogoItem getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(CatalogoItem idTipo) {
        this.idTipo = idTipo;
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
        if (!(object instanceof ThConfigHorasExtra)) {
            return false;
        }
        ThConfigHorasExtra other = (ThConfigHorasExtra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThConfigHorasExtra[ id=" + id + " ]";
    }

}
